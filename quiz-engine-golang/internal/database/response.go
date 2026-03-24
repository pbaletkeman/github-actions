package database

import (
	"database/sql"

	"github.com/pbaletkeman/quiz-engine-golang/internal/models"
)

func SaveResponse(db *sql.DB, r models.QuizResponse) error {
	isCorrect := 0
	if r.IsCorrect {
		isCorrect = 1
	}
	_, err := db.Exec(`INSERT OR IGNORE INTO quiz_responses (session_id, question_id, user_answer, is_correct, time_taken_seconds)
		VALUES (?, ?, ?, ?, ?)`, r.SessionID, r.QuestionID, r.UserAnswer, isCorrect, r.TimeTakenSeconds)
	return err
}

func GetSessionResponses(db *sql.DB, sessionID string) ([]models.QuizResponse, error) {
	rows, err := db.Query(`SELECT id, session_id, question_id, user_answer, is_correct, time_taken_seconds
		FROM quiz_responses WHERE session_id = ?`, sessionID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var responses []models.QuizResponse
	for rows.Next() {
		var r models.QuizResponse
		var isCorrect int
		var timeTaken sql.NullInt64
		err := rows.Scan(&r.ID, &r.SessionID, &r.QuestionID, &r.UserAnswer, &isCorrect, &timeTaken)
		if err != nil {
			return nil, err
		}
		r.IsCorrect = isCorrect == 1
		if timeTaken.Valid {
			r.TimeTakenSeconds = int(timeTaken.Int64)
		}
		responses = append(responses, r)
	}
	return responses, rows.Err()
}

func CountCorrect(db *sql.DB, sessionID string) (int, error) {
	var count int
	err := db.QueryRow(`SELECT COUNT(*) FROM quiz_responses WHERE session_id = ? AND is_correct = 1`, sessionID).Scan(&count)
	return count, err
}

func DeleteSessionResponses(db *sql.DB, sessionID string) error {
	_, err := db.Exec("DELETE FROM quiz_responses WHERE session_id = ?", sessionID)
	return err
}

func DeleteAllResponses(db *sql.DB) error {
	_, err := db.Exec("DELETE FROM quiz_responses")
	return err
}
