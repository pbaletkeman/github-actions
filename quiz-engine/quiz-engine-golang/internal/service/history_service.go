package service

import (
	"database/sql"
	"encoding/csv"
	"encoding/json"
	"fmt"
	"os"

	"github.com/pbaletkeman/quiz-engine-golang/internal/database"
	"github.com/pbaletkeman/quiz-engine-golang/internal/models"
)

// ListSessions returns all quiz sessions ordered by date.
func ListSessions(db *sql.DB) ([]models.QuizSession, error) {
	return database.ListSessions(db)
}

// GetSessionWithResponses returns a session and its responses.
func GetSessionWithResponses(db *sql.DB, sessionID string) (*models.QuizSession, []models.QuizResponse, error) {
	session, err := database.GetSession(db, sessionID)
	if err != nil {
		return nil, nil, err
	}
	responses, err := database.GetSessionResponses(db, sessionID)
	if err != nil {
		return nil, nil, err
	}
	return session, responses, nil
}

// ExportToJSON writes all session data to a JSON file at path.
func ExportToJSON(db *sql.DB, sessions []models.QuizSession, path string) error {
	type responseRecord struct {
		QuestionID  int64  `json:"question_id"`
		UserAnswer  string `json:"user_answer"`
		IsCorrect   bool   `json:"is_correct"`
	}
	type sessionRecord struct {
		SessionID         string           `json:"session_id"`
		Date              string           `json:"date"`
		Score             int              `json:"score"`
		TotalQuestions    int              `json:"total_questions"`
		PercentageCorrect float64          `json:"percentage_correct"`
		Responses         []responseRecord `json:"responses"`
	}

	var records []sessionRecord
	for _, s := range sessions {
		responses, err := database.GetSessionResponses(db, s.SessionID)
		if err != nil {
			return fmt.Errorf("failed to fetch responses for session %s: %w", s.SessionID, err)
		}
		var respRecords []responseRecord
		for _, r := range responses {
			respRecords = append(respRecords, responseRecord{
				QuestionID: r.QuestionID,
				UserAnswer: r.UserAnswer,
				IsCorrect:  r.IsCorrect,
			})
		}
		records = append(records, sessionRecord{
			SessionID:         s.SessionID,
			Date:              s.StartedAt.Format("2006-01-02T15:04:05Z"),
			Score:             s.NumCorrect,
			TotalQuestions:    s.NumQuestions,
			PercentageCorrect: s.PercentageCorrect,
			Responses:         respRecords,
		})
	}

	f, err := os.Create(path)
	if err != nil {
		return fmt.Errorf("failed to create file %s: %w", path, err)
	}
	defer f.Close()

	enc := json.NewEncoder(f)
	enc.SetIndent("", "  ")
	return enc.Encode(records)
}

// ExportToCSV writes all session data to a CSV file at path.
func ExportToCSV(db *sql.DB, sessions []models.QuizSession, path string) error {
	f, err := os.Create(path)
	if err != nil {
		return fmt.Errorf("failed to create file %s: %w", path, err)
	}
	defer f.Close()

	w := csv.NewWriter(f)
	defer w.Flush()

	header := []string{"session_id", "date", "score", "total_questions", "percentage_correct", "question_id", "user_answer", "is_correct"}
	if err := w.Write(header); err != nil {
		return err
	}

	for _, s := range sessions {
		responses, err := database.GetSessionResponses(db, s.SessionID)
		if err != nil {
			return fmt.Errorf("failed to fetch responses for session %s: %w", s.SessionID, err)
		}
		if len(responses) == 0 {
			row := []string{
				s.SessionID,
				s.StartedAt.Format("2006-01-02T15:04:05Z"),
				fmt.Sprintf("%d", s.NumCorrect),
				fmt.Sprintf("%d", s.NumQuestions),
				fmt.Sprintf("%.2f", s.PercentageCorrect),
				"", "", "",
			}
			if err := w.Write(row); err != nil {
				return err
			}
		} else {
			for _, r := range responses {
				row := []string{
					s.SessionID,
					s.StartedAt.Format("2006-01-02T15:04:05Z"),
					fmt.Sprintf("%d", s.NumCorrect),
					fmt.Sprintf("%d", s.NumQuestions),
					fmt.Sprintf("%.2f", s.PercentageCorrect),
					fmt.Sprintf("%d", r.QuestionID),
					r.UserAnswer,
					fmt.Sprintf("%t", r.IsCorrect),
				}
				if err := w.Write(row); err != nil {
					return err
				}
			}
		}
	}

	return w.Error()
}
