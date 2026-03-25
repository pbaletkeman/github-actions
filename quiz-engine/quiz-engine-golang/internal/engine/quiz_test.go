package engine

import (
	"database/sql"
	"testing"

	_ "github.com/mattn/go-sqlite3"
	"github.com/pbaletkeman/quiz-engine-golang/internal/database"
	"github.com/pbaletkeman/quiz-engine-golang/internal/models"
)

func openTestDB(t *testing.T) *sql.DB {
	t.Helper()
	db, err := sql.Open("sqlite3", ":memory:")
	if err != nil {
		t.Fatalf("failed to open db: %v", err)
	}
	if err := database.InitSchema(db); err != nil {
		t.Fatalf("failed to init schema: %v", err)
	}
	t.Cleanup(func() { db.Close() })
	return db
}

func insertTestQuestion(t *testing.T, db *sql.DB) int64 {
	t.Helper()
	q := models.Question{
		QuestionText:  "What is CI?",
		OptionA:       "Continuous Integration",
		OptionB:       "Continuous Delivery",
		OptionC:       "Continuous Deployment",
		OptionD:       "Continuous Development",
		CorrectAnswer: "A",
	}
	id, err := database.InsertQuestion(db, q)
	if err != nil || id == 0 {
		t.Fatalf("failed to insert question: %v", err)
	}
	return id
}

func TestNewQuizEngine(t *testing.T) {
	db := openTestDB(t)
	cfg := QuizConfig{NumQuestions: 1}
	qe := NewQuizEngine(db, cfg)
	if qe == nil {
		t.Fatal("expected non-nil QuizEngine")
	}
	if qe.SessionID == "" {
		t.Error("expected non-empty SessionID")
	}
}

func TestSubmitAnswer_Correct(t *testing.T) {
	db := openTestDB(t)
	insertTestQuestion(t, db)
	cfg := QuizConfig{NumQuestions: 1}
	qe := NewQuizEngine(db, cfg)
	if err := qe.LoadQuestions(); err != nil {
		t.Fatalf("load questions failed: %v", err)
	}
	if len(qe.Questions) == 0 {
		t.Fatal("no questions loaded")
	}

	// Find which shuffled position maps to correct answer
	shuffle := qe.ShuffleData[0]
	correctLetter := ""
	letters := []string{"A", "B", "C", "D", "E"}
	for newIdx, origLetter := range shuffle.PositionMap {
		if origLetter == qe.Questions[0].CorrectAnswer {
			correctLetter = letters[newIdx]
			break
		}
	}

	err := qe.SubmitAnswer(0, correctLetter, 5)
	if err != nil {
		t.Fatalf("submit answer failed: %v", err)
	}
	if qe.NumCorrect != 1 {
		t.Errorf("expected NumCorrect=1, got %d", qe.NumCorrect)
	}
}

func TestSubmitAnswer_Incorrect(t *testing.T) {
	db := openTestDB(t)
	insertTestQuestion(t, db)
	cfg := QuizConfig{NumQuestions: 1}
	qe := NewQuizEngine(db, cfg)
	if err := qe.LoadQuestions(); err != nil {
		t.Fatalf("load questions failed: %v", err)
	}

	shuffle := qe.ShuffleData[0]
	// Find any shuffled position that is NOT the correct answer
	letters := []string{"A", "B", "C", "D"}
	correctNewIdx := shuffle.CorrectShuffledIndex
	wrongLetter := ""
	for i, l := range letters {
		if i != correctNewIdx {
			wrongLetter = l
			break
		}
	}
	if wrongLetter == "" {
		t.Skip("could not find wrong answer")
	}

	err := qe.SubmitAnswer(0, wrongLetter, 5)
	if err != nil {
		t.Fatalf("submit answer failed: %v", err)
	}
	if qe.NumCorrect != 0 {
		t.Errorf("expected NumCorrect=0, got %d", qe.NumCorrect)
	}
}

func TestFinalizeQuiz(t *testing.T) {
	db := openTestDB(t)
	insertTestQuestion(t, db)
	cfg := QuizConfig{NumQuestions: 1}
	qe := NewQuizEngine(db, cfg)
	if err := qe.LoadQuestions(); err != nil {
		t.Fatalf("load questions failed: %v", err)
	}

	session, err := qe.FinalizeQuiz()
	if err != nil {
		t.Fatalf("finalize quiz failed: %v", err)
	}
	if session.SessionID == "" {
		t.Error("expected non-empty SessionID")
	}
	if session.EndedAt == nil {
		t.Error("expected EndedAt to be set")
	}
}

func TestGetReviewData(t *testing.T) {
	db := openTestDB(t)
	insertTestQuestion(t, db)
	cfg := QuizConfig{NumQuestions: 1}
	qe := NewQuizEngine(db, cfg)
	if err := qe.LoadQuestions(); err != nil {
		t.Fatalf("load questions failed: %v", err)
	}

	qe.SubmitAnswer(0, "A", 5)
	questions, responses, err := qe.GetReviewData()
	if err != nil {
		t.Fatalf("get review data failed: %v", err)
	}
	if len(questions) == 0 {
		t.Error("expected questions")
	}
	if len(responses) == 0 {
		t.Error("expected responses")
	}
	// Full question should have correct answer populated
	if questions[0].CorrectAnswer == "" {
		t.Error("expected CorrectAnswer to be populated in review data")
	}
}
