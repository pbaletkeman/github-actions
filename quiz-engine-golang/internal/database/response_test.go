package database

import (
	"testing"

	"github.com/pbaletkeman/quiz-engine-golang/internal/models"
)

func insertSampleSession(t *testing.T, db interface{ Exec(string, ...interface{}) (interface{}, error) }) {
	t.Helper()
}

func TestSaveResponse(t *testing.T) {
	db := openTestDB(t)
	// Insert a question and session first (FK constraints)
	qID, err := InsertQuestion(db, sampleQuestion())
	if err != nil || qID == 0 {
		t.Fatalf("insert question failed: %v", err)
	}
	session := models.QuizSession{
		SessionID:    "test-session-1",
		NumQuestions: 1,
	}
	if err := CreateSession(db, session); err != nil {
		t.Fatalf("create session failed: %v", err)
	}

	resp := models.QuizResponse{
		SessionID:        "test-session-1",
		QuestionID:       qID,
		UserAnswer:       "A",
		IsCorrect:        true,
		TimeTakenSeconds: 10,
	}
	err = SaveResponse(db, resp)
	if err != nil {
		t.Fatalf("save response failed: %v", err)
	}
}

func TestSaveResponse_Incorrect(t *testing.T) {
	db := openTestDB(t)
	qID, _ := InsertQuestion(db, sampleQuestion())
	session := models.QuizSession{SessionID: "test-session-2", NumQuestions: 1}
	CreateSession(db, session)

	resp := models.QuizResponse{
		SessionID:  "test-session-2",
		QuestionID: qID,
		UserAnswer: "B",
		IsCorrect:  false,
	}
	if err := SaveResponse(db, resp); err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
}

func TestGetSessionResponses(t *testing.T) {
	db := openTestDB(t)
	qID, _ := InsertQuestion(db, sampleQuestion())
	session := models.QuizSession{SessionID: "test-session-3", NumQuestions: 1}
	CreateSession(db, session)

	resp := models.QuizResponse{
		SessionID:        "test-session-3",
		QuestionID:       qID,
		UserAnswer:       "A",
		IsCorrect:        true,
		TimeTakenSeconds: 5,
	}
	SaveResponse(db, resp)

	responses, err := GetSessionResponses(db, "test-session-3")
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if len(responses) != 1 {
		t.Fatalf("expected 1 response, got %d", len(responses))
	}
	if !responses[0].IsCorrect {
		t.Error("expected IsCorrect=true")
	}
	if responses[0].TimeTakenSeconds != 5 {
		t.Errorf("expected TimeTakenSeconds=5, got %d", responses[0].TimeTakenSeconds)
	}
}

func TestGetSessionResponses_Empty(t *testing.T) {
	db := openTestDB(t)
	responses, err := GetSessionResponses(db, "nonexistent")
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if len(responses) != 0 {
		t.Errorf("expected 0 responses, got %d", len(responses))
	}
}

func TestCountCorrect(t *testing.T) {
	db := openTestDB(t)
	qID, _ := InsertQuestion(db, sampleQuestion())
	q2 := models.Question{
		QuestionText: "Q2", OptionA: "A", OptionB: "B", OptionC: "C", OptionD: "D", CorrectAnswer: "B",
	}
	qID2, _ := InsertQuestion(db, q2)

	session := models.QuizSession{SessionID: "count-session", NumQuestions: 2}
	CreateSession(db, session)

	SaveResponse(db, models.QuizResponse{
		SessionID: "count-session", QuestionID: qID, UserAnswer: "A", IsCorrect: true,
	})
	SaveResponse(db, models.QuizResponse{
		SessionID: "count-session", QuestionID: qID2, UserAnswer: "D", IsCorrect: false,
	})

	count, err := CountCorrect(db, "count-session")
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if count != 1 {
		t.Errorf("expected 1 correct, got %d", count)
	}
}

func TestDeleteSessionResponses(t *testing.T) {
	db := openTestDB(t)
	qID, _ := InsertQuestion(db, sampleQuestion())
	session := models.QuizSession{SessionID: "del-session", NumQuestions: 1}
	CreateSession(db, session)
	SaveResponse(db, models.QuizResponse{
		SessionID: "del-session", QuestionID: qID, UserAnswer: "A", IsCorrect: true,
	})

	if err := DeleteSessionResponses(db, "del-session"); err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	responses, _ := GetSessionResponses(db, "del-session")
	if len(responses) != 0 {
		t.Errorf("expected 0 responses after delete, got %d", len(responses))
	}
}

func TestDeleteAllResponses(t *testing.T) {
	db := openTestDB(t)
	qID, _ := InsertQuestion(db, sampleQuestion())
	session := models.QuizSession{SessionID: "del-all-session", NumQuestions: 1}
	CreateSession(db, session)
	SaveResponse(db, models.QuizResponse{
		SessionID: "del-all-session", QuestionID: qID, UserAnswer: "A", IsCorrect: true,
	})

	if err := DeleteAllResponses(db); err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	responses, _ := GetSessionResponses(db, "del-all-session")
	if len(responses) != 0 {
		t.Errorf("expected 0 responses after delete all, got %d", len(responses))
	}
}

func TestSaveResponse_Duplicate(t *testing.T) {
	db := openTestDB(t)
	qID, _ := InsertQuestion(db, sampleQuestion())
	session := models.QuizSession{SessionID: "dup-session", NumQuestions: 1}
	CreateSession(db, session)

	resp := models.QuizResponse{
		SessionID: "dup-session", QuestionID: qID, UserAnswer: "A", IsCorrect: true,
	}
	SaveResponse(db, resp)
	// Second insert should be ignored (UNIQUE constraint)
	err := SaveResponse(db, resp)
	if err != nil {
		t.Fatalf("duplicate save should not error (INSERT OR IGNORE): %v", err)
	}
}
