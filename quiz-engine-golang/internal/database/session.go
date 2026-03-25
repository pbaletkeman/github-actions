package database

import (
	"database/sql"
	"fmt"

	"github.com/pbaletkeman/quiz-engine-golang/internal/models"
)

func CreateSession(db *sql.DB, s models.QuizSession) error {
	_, err := db.Exec(`INSERT INTO quiz_sessions (session_id, started_at, num_questions)
		VALUES (?, ?, ?)`, s.SessionID, s.StartedAt, s.NumQuestions)
	return err
}

func UpdateSession(db *sql.DB, s models.QuizSession) error {
	_, err := db.Exec(`UPDATE quiz_sessions SET ended_at=?, num_correct=?, percentage_correct=?, time_taken_seconds=?
		WHERE session_id=?`, s.EndedAt, s.NumCorrect, s.PercentageCorrect, s.TimeTakenSeconds, s.SessionID)
	return err
}

func GetSession(db *sql.DB, sessionID string) (*models.QuizSession, error) {
	row := db.QueryRow(`SELECT session_id, started_at, ended_at, num_questions, num_correct, percentage_correct, time_taken_seconds
		FROM quiz_sessions WHERE session_id = ?`, sessionID)

	var s models.QuizSession
	var endedAt sql.NullTime
	var startedAt sql.NullTime
	var timeTaken sql.NullInt64
	err := row.Scan(&s.SessionID, &startedAt, &endedAt, &s.NumQuestions, &s.NumCorrect, &s.PercentageCorrect, &timeTaken)
	if err == sql.ErrNoRows {
		return nil, fmt.Errorf("session not found: %s", sessionID)
	}
	if err != nil {
		return nil, err
	}
	if startedAt.Valid {
		s.StartedAt = startedAt.Time
	}
	if endedAt.Valid {
		s.EndedAt = &endedAt.Time
	}
	if timeTaken.Valid {
		s.TimeTakenSeconds = int(timeTaken.Int64)
	}
	return &s, nil
}

func ListSessions(db *sql.DB) ([]models.QuizSession, error) {
	rows, err := db.Query(`SELECT session_id, started_at, ended_at, num_questions, num_correct, percentage_correct, time_taken_seconds
		FROM quiz_sessions ORDER BY started_at DESC`)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var sessions []models.QuizSession
	for rows.Next() {
		var s models.QuizSession
		var endedAt sql.NullTime
		var startedAt sql.NullTime
		var timeTaken sql.NullInt64
		err := rows.Scan(&s.SessionID, &startedAt, &endedAt, &s.NumQuestions, &s.NumCorrect, &s.PercentageCorrect, &timeTaken)
		if err != nil {
			return nil, err
		}
		if startedAt.Valid {
			s.StartedAt = startedAt.Time
		}
		if endedAt.Valid {
			s.EndedAt = &endedAt.Time
		}
		if timeTaken.Valid {
			s.TimeTakenSeconds = int(timeTaken.Int64)
		}
		sessions = append(sessions, s)
	}
	return sessions, rows.Err()
}
