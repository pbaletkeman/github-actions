package service

import (
	"database/sql"

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
