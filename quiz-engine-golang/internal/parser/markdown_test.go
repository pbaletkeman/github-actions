package parser

import (
	"os"
	"testing"
)

const basicQuestion = `## What is CI?
- A) Continuous Integration
- B) Continuous Delivery
- C) Continuous Deployment
- D) Continuous Development
**Answer: A**
`

const questionWithExplanation = `## What is GitHub Actions?
- A) A CI/CD platform
- B) A code editor
- C) A database
- D) A programming language
**Answer: A**
*Explanation: GitHub Actions is a CI/CD and automation platform.*
`

const fiveOptionQuestion = `## Which of the following is correct?
- A) Option A
- B) Option B
- C) Option C
- D) Option D
- E) Option E
**Answer: E**
`

const noAnswerQuestion = `## What is this?
- A) Something
- B) Another thing
- C) Yet another
- D) Last one
`

func TestParseMarkdownContent_BasicQuestion(t *testing.T) {
	questions, err := ParseMarkdownContent(basicQuestion)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if len(questions) != 1 {
		t.Fatalf("expected 1 question, got %d", len(questions))
	}
	q := questions[0]
	if q.QuestionText != "What is CI?" {
		t.Errorf("expected question text 'What is CI?', got %q", q.QuestionText)
	}
	if q.CorrectAnswer != "A" {
		t.Errorf("expected correct answer A, got %s", q.CorrectAnswer)
	}
	if q.OptionA != "Continuous Integration" {
		t.Errorf("expected option A 'Continuous Integration', got %q", q.OptionA)
	}
}

func TestParseMarkdownContent_FiveOptions(t *testing.T) {
	questions, err := ParseMarkdownContent(fiveOptionQuestion)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if len(questions) != 1 {
		t.Fatalf("expected 1 question, got %d", len(questions))
	}
	if questions[0].OptionE != "Option E" {
		t.Errorf("expected option E 'Option E', got %q", questions[0].OptionE)
	}
	if questions[0].CorrectAnswer != "E" {
		t.Errorf("expected correct answer E, got %s", questions[0].CorrectAnswer)
	}
}

func TestParseMarkdownContent_WithExplanation(t *testing.T) {
	questions, err := ParseMarkdownContent(questionWithExplanation)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if len(questions) != 1 {
		t.Fatalf("expected 1 question, got %d", len(questions))
	}
	if questions[0].Explanation == "" {
		t.Error("expected explanation to be populated")
	}
}

func TestParseMarkdownFile_File(t *testing.T) {
	f, err := os.CreateTemp("", "quiz-test-*.md")
	if err != nil {
		t.Fatalf("failed to create temp file: %v", err)
	}
	defer os.Remove(f.Name())
	f.WriteString(basicQuestion)
	f.Close()

	questions, err := ParseMarkdownFile(f.Name())
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if len(questions) != 1 {
		t.Fatalf("expected 1 question, got %d", len(questions))
	}
}

func TestParseMarkdownContent_NoAnswer(t *testing.T) {
	_, err := ParseMarkdownContent(noAnswerQuestion)
	if err == nil {
		t.Error("expected error for question with no answer")
	}
}

func TestParseMarkdownContent_Empty(t *testing.T) {
	questions, err := ParseMarkdownContent("")
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if len(questions) != 0 {
		t.Errorf("expected 0 questions for empty content, got %d", len(questions))
	}
}
