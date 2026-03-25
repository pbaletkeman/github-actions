package service

import (
	"path/filepath"
	"testing"

	"github.com/pbaletkeman/quiz-engine-golang/internal/database"
	"github.com/pbaletkeman/quiz-engine-golang/internal/models"
)

func TestDefaultConfig(t *testing.T) {
	cfg := DefaultConfig()
	if cfg.DBPath == "" {
		t.Error("expected non-empty DBPath")
	}
	if cfg.NumQuestions == 0 {
		t.Error("expected non-zero NumQuestions")
	}
	if cfg.SecondsPerQ == 0 {
		t.Error("expected non-zero SecondsPerQ")
	}
	if cfg.TotalSeconds == 0 {
		t.Error("expected non-zero TotalSeconds")
	}
}

func TestNewQuizService(t *testing.T) {
	tmpDir := t.TempDir()
	cfg := Config{
		DBPath:       filepath.Join(tmpDir, "test.db"),
		NumQuestions: 10,
		SecondsPerQ:  60,
		TotalSeconds: 600,
	}

	svc, err := NewQuizService(cfg)
	if err != nil {
		t.Fatalf("NewQuizService failed: %v", err)
	}
	defer svc.Close()

	if svc.DB == nil {
		t.Error("expected non-nil DB")
	}
}

func TestNewQuizService_InvalidPath(t *testing.T) {
	cfg := Config{
		DBPath:       "/nonexistent/path/test.db",
		NumQuestions: 10,
	}
	_, err := NewQuizService(cfg)
	if err == nil {
		t.Error("expected error for invalid DB path")
	}
}

func TestQuizService_NewEngine(t *testing.T) {
	tmpDir := t.TempDir()
	cfg := Config{
		DBPath:       filepath.Join(tmpDir, "test.db"),
		NumQuestions: 5,
	}

	svc, err := NewQuizService(cfg)
	if err != nil {
		t.Fatalf("NewQuizService failed: %v", err)
	}
	defer svc.Close()

	eng := svc.NewEngine()
	if eng == nil {
		t.Error("expected non-nil engine")
	}
	if eng.SessionID == "" {
		t.Error("expected non-empty SessionID")
	}
}

func TestQuizService_Close(t *testing.T) {
	tmpDir := t.TempDir()
	cfg := Config{
		DBPath:       filepath.Join(tmpDir, "test.db"),
		NumQuestions: 5,
	}

	svc, err := NewQuizService(cfg)
	if err != nil {
		t.Fatalf("NewQuizService failed: %v", err)
	}

	if err := svc.Close(); err != nil {
		t.Fatalf("Close failed: %v", err)
	}
}

func TestListSessions_Empty(t *testing.T) {
	tmpDir := t.TempDir()
	db, err := database.NewDB(filepath.Join(tmpDir, "test.db"))
	if err != nil {
		t.Fatalf("NewDB failed: %v", err)
	}
	defer db.Close()

	sessions, err := ListSessions(db)
	if err != nil {
		t.Fatalf("ListSessions failed: %v", err)
	}
	if len(sessions) != 0 {
		t.Errorf("expected 0 sessions, got %d", len(sessions))
	}
}

func TestListSessions_WithData(t *testing.T) {
	tmpDir := t.TempDir()
	db, err := database.NewDB(filepath.Join(tmpDir, "test.db"))
	if err != nil {
		t.Fatalf("NewDB failed: %v", err)
	}
	defer db.Close()

	session := models.QuizSession{
		SessionID:    "svc-test-session",
		NumQuestions: 5,
	}
	if err := database.CreateSession(db, session); err != nil {
		t.Fatalf("CreateSession failed: %v", err)
	}

	sessions, err := ListSessions(db)
	if err != nil {
		t.Fatalf("ListSessions failed: %v", err)
	}
	if len(sessions) != 1 {
		t.Errorf("expected 1 session, got %d", len(sessions))
	}
}

func TestGetSessionWithResponses(t *testing.T) {
	tmpDir := t.TempDir()
	db, err := database.NewDB(filepath.Join(tmpDir, "test.db"))
	if err != nil {
		t.Fatalf("NewDB failed: %v", err)
	}
	defer db.Close()

	session := models.QuizSession{
		SessionID:    "svc-review-session",
		NumQuestions: 1,
	}
	if err := database.CreateSession(db, session); err != nil {
		t.Fatalf("CreateSession failed: %v", err)
	}

	s, responses, err := GetSessionWithResponses(db, "svc-review-session")
	if err != nil {
		t.Fatalf("GetSessionWithResponses failed: %v", err)
	}
	if s == nil {
		t.Error("expected non-nil session")
	}
	if s.SessionID != "svc-review-session" {
		t.Errorf("expected session ID svc-review-session, got %s", s.SessionID)
	}
	if len(responses) != 0 {
		t.Errorf("expected 0 responses, got %d", len(responses))
	}
}

func TestGetSessionWithResponses_NotFound(t *testing.T) {
	tmpDir := t.TempDir()
	db, err := database.NewDB(filepath.Join(tmpDir, "test.db"))
	if err != nil {
		t.Fatalf("NewDB failed: %v", err)
	}
	defer db.Close()

	_, _, err = GetSessionWithResponses(db, "nonexistent")
	if err == nil {
		t.Error("expected error for nonexistent session")
	}
}
