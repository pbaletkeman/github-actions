package database

import (
	"testing"
	"time"

	"github.com/pbaletkeman/quiz-engine-golang/internal/models"
)

func TestCreateSession(t *testing.T) {
	db := openTestDB(t)
	s := models.QuizSession{
		SessionID:    "test-session-1",
		StartedAt:    time.Now(),
		NumQuestions: 10,
	}
	err := CreateSession(db, s)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
}

func TestUpdateSession(t *testing.T) {
	db := openTestDB(t)
	s := models.QuizSession{
		SessionID:    "test-session-2",
		StartedAt:    time.Now(),
		NumQuestions: 10,
	}
	err := CreateSession(db, s)
	if err != nil {
		t.Fatalf("create session failed: %v", err)
	}

	now := time.Now()
	s.EndedAt = &now
	s.NumCorrect = 7
	s.PercentageCorrect = 70.0
	s.TimeTakenSeconds = 120

	err = UpdateSession(db, s)
	if err != nil {
		t.Fatalf("update session failed: %v", err)
	}

	retrieved, err := GetSession(db, s.SessionID)
	if err != nil {
		t.Fatalf("get session failed: %v", err)
	}
	if retrieved.NumCorrect != 7 {
		t.Errorf("expected NumCorrect=7, got %d", retrieved.NumCorrect)
	}
	if retrieved.PercentageCorrect != 70.0 {
		t.Errorf("expected PercentageCorrect=70.0, got %f", retrieved.PercentageCorrect)
	}
}

func TestGetSession_NotFound(t *testing.T) {
	db := openTestDB(t)
	_, err := GetSession(db, "non-existent-session")
	if err == nil {
		t.Error("expected error for non-existent session")
	}
}

func TestListSessions(t *testing.T) {
	db := openTestDB(t)
	s1 := models.QuizSession{SessionID: "s1", StartedAt: time.Now(), NumQuestions: 5}
	s2 := models.QuizSession{SessionID: "s2", StartedAt: time.Now(), NumQuestions: 10}
	CreateSession(db, s1)
	CreateSession(db, s2)

	sessions, err := ListSessions(db)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if len(sessions) != 2 {
		t.Errorf("expected 2 sessions, got %d", len(sessions))
	}
}
