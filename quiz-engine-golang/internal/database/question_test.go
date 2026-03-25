package database

import (
	"database/sql"
	"testing"

	_ "github.com/mattn/go-sqlite3"
	"github.com/pbaletkeman/quiz-engine-golang/internal/models"
)

func openTestDB(t *testing.T) *sql.DB {
	t.Helper()
	db, err := sql.Open("sqlite3", ":memory:")
	if err != nil {
		t.Fatalf("failed to open test db: %v", err)
	}
	if err := InitSchema(db); err != nil {
		t.Fatalf("failed to init schema: %v", err)
	}
	t.Cleanup(func() { db.Close() })
	return db
}

func sampleQuestion() models.Question {
	return models.Question{
		QuestionText:  "What is CI?",
		OptionA:       "Continuous Integration",
		OptionB:       "Continuous Delivery",
		OptionC:       "Continuous Deployment",
		OptionD:       "Continuous Development",
		CorrectAnswer: "A",
	}
}

func TestInsertQuestion(t *testing.T) {
	db := openTestDB(t)
	q := sampleQuestion()
	id, err := InsertQuestion(db, q)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if id == 0 {
		t.Error("expected non-zero ID")
	}
}

func TestInsertQuestion_Duplicate(t *testing.T) {
	db := openTestDB(t)
	q := sampleQuestion()
	_, err := InsertQuestion(db, q)
	if err != nil {
		t.Fatalf("first insert failed: %v", err)
	}
	id2, err := InsertQuestion(db, q)
	if err != nil {
		t.Fatalf("duplicate insert returned error: %v", err)
	}
	if id2 != 0 {
		t.Logf("duplicate insert returned id %d (may be 0 for INSERT OR IGNORE)", id2)
	}
}

func TestGetRandomQuestions_OmitsCorrectAnswer(t *testing.T) {
	db := openTestDB(t)
	q := sampleQuestion()
	_, err := InsertQuestion(db, q)
	if err != nil {
		t.Fatalf("insert failed: %v", err)
	}
	questions, err := GetRandomQuestions(db, 1, "", "")
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if len(questions) == 0 {
		t.Fatal("expected at least 1 question")
	}
	if questions[0].CorrectAnswer != "" {
		t.Error("expected CorrectAnswer to be empty in random questions")
	}
	if questions[0].Explanation != "" {
		t.Error("expected Explanation to be empty in random questions")
	}
}

func TestGetRandomQuestions_WithFilters(t *testing.T) {
	db := openTestDB(t)
	q1 := sampleQuestion()
	q1.Difficulty = "easy"
	q1.Section = "intro"
	_, err := InsertQuestion(db, q1)
	if err != nil {
		t.Fatalf("insert failed: %v", err)
	}

	q2 := models.Question{
		QuestionText: "What is CD?", OptionA: "A", OptionB: "B", OptionC: "C", OptionD: "D",
		CorrectAnswer: "B", Difficulty: "hard", Section: "advanced",
	}
	_, err = InsertQuestion(db, q2)
	if err != nil {
		t.Fatalf("insert q2 failed: %v", err)
	}

	questions, err := GetRandomQuestions(db, 10, "easy", "intro")
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if len(questions) != 1 {
		t.Fatalf("expected 1 question, got %d", len(questions))
	}
}

func TestCountQuestions(t *testing.T) {
	db := openTestDB(t)
	count, err := CountQuestions(db)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if count != 0 {
		t.Errorf("expected 0, got %d", count)
	}

	InsertQuestion(db, sampleQuestion())
	count, err = CountQuestions(db)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if count != 1 {
		t.Errorf("expected 1, got %d", count)
	}
}

func TestMarkQuestionUsed(t *testing.T) {
	db := openTestDB(t)
	id, err := InsertQuestion(db, sampleQuestion())
	if err != nil || id == 0 {
		t.Fatalf("insert failed: %v", err)
	}
	err = MarkQuestionUsed(db, id)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	q, err := GetQuestionWithAnswer(db, id)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if q.TimesUsed != 1 {
		t.Errorf("expected TimesUsed=1, got %d", q.TimesUsed)
	}
	if q.LastUsedAt == nil {
		t.Error("expected LastUsedAt to be set")
	}
}

func TestGetCurrentCycle(t *testing.T) {
	db := openTestDB(t)
	cycle, err := GetCurrentCycle(db)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if cycle != 1 {
		t.Errorf("expected cycle=1, got %d", cycle)
	}

	InsertQuestion(db, sampleQuestion())
	cycle, err = GetCurrentCycle(db)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if cycle != 1 {
		t.Errorf("expected cycle=1, got %d", cycle)
	}
}

func TestAdvanceCycleIfExhausted(t *testing.T) {
	db := openTestDB(t)
	id, err := InsertQuestion(db, sampleQuestion())
	if err != nil || id == 0 {
		t.Fatalf("insert failed: %v", err)
	}
	// Mark question as used so cycle is exhausted
	err = MarkQuestionUsed(db, id)
	if err != nil {
		t.Fatalf("mark used failed: %v", err)
	}
	err = AdvanceCycleIfExhausted(db)
	if err != nil {
		t.Fatalf("advance cycle failed: %v", err)
	}
	cycle, err := GetCurrentCycle(db)
	if err != nil {
		t.Fatalf("get cycle failed: %v", err)
	}
	if cycle != 2 {
		t.Errorf("expected cycle=2, got %d", cycle)
	}
}

func TestAdvanceCycleIfExhausted_NotExhausted(t *testing.T) {
	db := openTestDB(t)
	InsertQuestion(db, sampleQuestion())
	// Do NOT mark question used - cycle is not exhausted
	err := AdvanceCycleIfExhausted(db)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	cycle, _ := GetCurrentCycle(db)
	if cycle != 1 {
		t.Errorf("expected cycle=1, got %d", cycle)
	}
}

func TestDeleteAllQuestions(t *testing.T) {
	db := openTestDB(t)
	InsertQuestion(db, sampleQuestion())
	err := DeleteAllQuestions(db)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	count, _ := CountQuestions(db)
	if count != 0 {
		t.Errorf("expected 0 questions, got %d", count)
	}
}

func TestGetQuestionWithAnswer(t *testing.T) {
	db := openTestDB(t)
	q := sampleQuestion()
	q.Explanation = "Because CI means continuous integration"
	id, err := InsertQuestion(db, q)
	if err != nil || id == 0 {
		t.Fatalf("insert failed: %v", err)
	}
	full, err := GetQuestionWithAnswer(db, id)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if full.CorrectAnswer != "A" {
		t.Errorf("expected CorrectAnswer=A, got %s", full.CorrectAnswer)
	}
	if full.Explanation != "Because CI means continuous integration" {
		t.Errorf("unexpected explanation: %s", full.Explanation)
	}
}

func TestGetAllQuestions(t *testing.T) {
	db := openTestDB(t)
	InsertQuestion(db, sampleQuestion())
	q2 := models.Question{
		QuestionText: "What is CD?", OptionA: "A", OptionB: "B", OptionC: "C", OptionD: "D",
		CorrectAnswer: "B",
	}
	InsertQuestion(db, q2)
	all, err := GetAllQuestions(db)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if len(all) != 2 {
		t.Errorf("expected 2 questions, got %d", len(all))
	}
}
