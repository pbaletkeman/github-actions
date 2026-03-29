package parser

import (
	"os"
	"testing"
)

const basicQuestion = `## Questions

### Question 1 — Test Section

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Test

**Question**:
What is CI?

- A) Continuous Integration
- B) Continuous Delivery
- C) Continuous Deployment
- D) Continuous Development

---

## Answer Key

| # | Answer | Explanation | Source | Difficulty |
|---|--------|-------------|--------|------------|
| 1 | A | CI stands for Continuous Integration. | test.md | Easy |
`

const questionWithExplanation = `## Questions

### Question 1 — Test Section

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Test

**Question**:
What is GitHub Actions?

- A) A CI/CD platform
- B) A code editor
- C) A database
- D) A programming language

---

## Answer Key

| # | Answer | Explanation | Source | Difficulty |
|---|--------|-------------|--------|------------|
| 1 | A | GitHub Actions is a CI/CD and automation platform. | test.md | Easy |
`

const fiveOptionQuestion = `## Questions

### Question 1 — Test Section

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Test

**Question**:
Which of the following is correct?

- A) Option A
- B) Option B
- C) Option C
- D) Option D
- E) Option E

---

## Answer Key

| # | Answer | Explanation | Source | Difficulty |
|---|--------|-------------|--------|------------|
| 1 | E | E is correct. | test.md | Easy |
`

const questionWithScenario = `## Questions

### Question 1 — Test Section

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Test

**Scenario**:
A developer is testing their workflow.

**Question**:
What should they do?

- A) Option A
- B) Option B
- C) Option C
- D) Option D

---

## Answer Key

| # | Answer | Explanation | Source | Difficulty |
|---|--------|-------------|--------|------------|
| 1 | B | B is the correct approach. | test.md | Medium |
`

const multiSelectQuestion = `## Questions

### Question 1 — Test Section

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Test

**Question**:
Which of the following are correct?

- A) Option A
- B) Option B
- C) Option C
- D) Option D

---

## Answer Key

| # | Answer | Explanation | Source | Difficulty |
|---|--------|-------------|--------|------------|
| 1 | A, C | Both A and C are correct. | test.md | Hard |
`

const missingAnswerKeyQuestion = `## Questions

### Question 1 — Test Section

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Test

**Question**:
What is this?

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
	if q.Section != "Test Section" {
		t.Errorf("expected section 'Test Section', got %q", q.Section)
	}
	if q.Difficulty != "Easy" {
		t.Errorf("expected difficulty 'Easy', got %q", q.Difficulty)
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

func TestParseMarkdownContent_WithScenario(t *testing.T) {
	questions, err := ParseMarkdownContent(questionWithScenario)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if len(questions) != 1 {
		t.Fatalf("expected 1 question, got %d", len(questions))
	}
	q := questions[0]
	if q.QuestionText == "" {
		t.Error("expected non-empty question text")
	}
	// Scenario text should be prepended to question text
	if !containsAll(q.QuestionText, "A developer is testing", "What should they do") {
		t.Errorf("expected question text to contain scenario and question, got %q", q.QuestionText)
	}
}

func TestParseMarkdownContent_MultiSelectSkipped(t *testing.T) {
	questions, err := ParseMarkdownContent(multiSelectQuestion)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if len(questions) != 0 {
		t.Errorf("expected multi-select questions to be skipped, got %d question(s)", len(questions))
	}
}

func TestParseMarkdownContent_MissingAnswerKey(t *testing.T) {
	_, err := ParseMarkdownContent(missingAnswerKeyQuestion)
	if err == nil {
		t.Error("expected error when answer key entry is missing")
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

func TestParseMarkdownContent_Empty(t *testing.T) {
	questions, err := ParseMarkdownContent("")
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if len(questions) != 0 {
		t.Errorf("expected 0 questions for empty content, got %d", len(questions))
	}
}

func containsAll(s string, substrings ...string) bool {
	for _, sub := range substrings {
		if !contains(s, sub) {
			return false
		}
	}
	return true
}

func contains(s, sub string) bool {
	return len(s) >= len(sub) && (s == sub || len(s) > 0 && containsRune(s, sub))
}

func containsRune(s, sub string) bool {
	for i := 0; i <= len(s)-len(sub); i++ {
		if s[i:i+len(sub)] == sub {
			return true
		}
	}
	return false
}
