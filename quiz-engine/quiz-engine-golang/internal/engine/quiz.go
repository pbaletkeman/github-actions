package engine

import (
	"database/sql"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/pbaletkeman/quiz-engine-golang/internal/database"
	"github.com/pbaletkeman/quiz-engine-golang/internal/models"
)

// QuizConfig holds configuration for a quiz session.
type QuizConfig struct {
	NumQuestions int
	Difficulty   string
	Section      string
	DBPath       string
}

// QuizEngine manages a single quiz session.
type QuizEngine struct {
	SessionID   string
	Questions   []models.Question
	Responses   []models.QuizResponse
	NumCorrect  int
	DB          *sql.DB
	Config      QuizConfig
	StartedAt   time.Time
	ShuffleData []ShuffleResult
}

// NewQuizEngine creates a new QuizEngine with a fresh session ID.
func NewQuizEngine(db *sql.DB, config QuizConfig) *QuizEngine {
	return &QuizEngine{
		SessionID: uuid.New().String(),
		DB:        db,
		Config:    config,
		StartedAt: time.Now(),
	}
}

// LoadQuestions fetches random questions and creates the session record.
func (qe *QuizEngine) LoadQuestions() error {
	questions, err := database.GetRandomQuestions(qe.DB, qe.Config.NumQuestions, qe.Config.Difficulty, qe.Config.Section)
	if err != nil {
		return err
	}
	qe.Questions = questions

	qe.ShuffleData = make([]ShuffleResult, len(questions))
	for i, q := range questions {
		fullQ, err := database.GetQuestionWithAnswer(qe.DB, q.ID)
		if err == nil && fullQ != nil {
			qe.Questions[i].CorrectAnswer = fullQ.CorrectAnswer
		}
		options := []string{qe.Questions[i].OptionA, qe.Questions[i].OptionB, qe.Questions[i].OptionC, qe.Questions[i].OptionD}
		if qe.Questions[i].OptionE != "" {
			options = append(options, qe.Questions[i].OptionE)
		}
		qe.ShuffleData[i] = ShuffleAnswers(options, qe.Questions[i].CorrectAnswer)
	}

	session := models.QuizSession{
		SessionID:    qe.SessionID,
		StartedAt:    qe.StartedAt,
		NumQuestions: len(questions),
	}
	return database.CreateSession(qe.DB, session)
}

// SubmitAnswer records the user's answer for question at qIdx.
func (qe *QuizEngine) SubmitAnswer(qIdx int, userAnswer string, timeTaken int) error {
	if qIdx >= len(qe.Questions) {
		return fmt.Errorf("question index out of range")
	}
	q := qe.Questions[qIdx]
	shuffle := qe.ShuffleData[qIdx]

	userAnswerIdx := map[string]int{"A": 0, "B": 1, "C": 2, "D": 3, "E": 4}
	originalLetter := ""
	if idx, ok := userAnswerIdx[userAnswer]; ok && idx < len(shuffle.ShuffledOptions) {
		originalLetter = shuffle.PositionMap[idx]
	}

	isCorrect := originalLetter == q.CorrectAnswer
	if isCorrect {
		qe.NumCorrect++
	}

	resp := models.QuizResponse{
		SessionID:        qe.SessionID,
		QuestionID:       q.ID,
		UserAnswer:       userAnswer,
		IsCorrect:        isCorrect,
		TimeTakenSeconds: timeTaken,
	}
	qe.Responses = append(qe.Responses, resp)
	return database.SaveResponse(qe.DB, resp)
}

// FinalizeQuiz closes the session and advances the question cycle if needed.
func (qe *QuizEngine) FinalizeQuiz() (models.QuizSession, error) {
	now := time.Now()
	timeTaken := int(now.Sub(qe.StartedAt).Seconds())
	pct := 0.0
	if len(qe.Questions) > 0 {
		pct = float64(qe.NumCorrect) / float64(len(qe.Questions)) * 100
	}

	session := models.QuizSession{
		SessionID:         qe.SessionID,
		StartedAt:         qe.StartedAt,
		EndedAt:           &now,
		NumQuestions:      len(qe.Questions),
		NumCorrect:        qe.NumCorrect,
		PercentageCorrect: pct,
		TimeTakenSeconds:  timeTaken,
	}

	err := database.UpdateSession(qe.DB, session)
	if err != nil {
		return session, err
	}

	for _, q := range qe.Questions {
		database.MarkQuestionUsed(qe.DB, q.ID)
	}
	database.AdvanceCycleIfExhausted(qe.DB)

	return session, nil
}

// GetReviewData returns full questions (with answers) and the responses from this session.
func (qe *QuizEngine) GetReviewData() ([]models.Question, []models.QuizResponse, error) {
	fullQuestions := make([]models.Question, len(qe.Questions))
	for i, q := range qe.Questions {
		fq, err := database.GetQuestionWithAnswer(qe.DB, q.ID)
		if err != nil || fq == nil {
			fullQuestions[i] = q
		} else {
			fullQuestions[i] = *fq
		}
	}
	return fullQuestions, qe.Responses, nil
}
