package engine

import (
	"testing"
)

func TestShuffleAnswers_PreservesAll(t *testing.T) {
	options := []string{"Opt A", "Opt B", "Opt C", "Opt D"}
	result := ShuffleAnswers(options, "A")

	if len(result.ShuffledOptions) != 4 {
		t.Errorf("expected 4 options, got %d", len(result.ShuffledOptions))
	}

	seen := make(map[string]bool)
	for _, o := range result.ShuffledOptions {
		seen[o] = true
	}
	for _, o := range options {
		if !seen[o] {
			t.Errorf("option %q missing from shuffled result", o)
		}
	}
}

func TestShuffleAnswers_CorrectAnswerTracked(t *testing.T) {
	options := []string{"Opt A", "Opt B", "Opt C", "Opt D"}
	for i := 0; i < 20; i++ {
		result := ShuffleAnswers(options, "B")
		correctOption := result.ShuffledOptions[result.CorrectShuffledIndex]
		if correctOption != "Opt B" {
			t.Errorf("expected correct option to be 'Opt B', got %q", correctOption)
		}
		originalLetter := result.PositionMap[result.CorrectShuffledIndex]
		if originalLetter != "B" {
			t.Errorf("expected original letter B, got %s", originalLetter)
		}
	}
}

func TestShuffleAnswers_FiveOptions(t *testing.T) {
	options := []string{"Opt A", "Opt B", "Opt C", "Opt D", "Opt E"}
	result := ShuffleAnswers(options, "E")
	if len(result.ShuffledOptions) != 5 {
		t.Errorf("expected 5 options, got %d", len(result.ShuffledOptions))
	}
	correctOption := result.ShuffledOptions[result.CorrectShuffledIndex]
	if correctOption != "Opt E" {
		t.Errorf("expected correct option 'Opt E', got %q", correctOption)
	}
}

func TestShuffleAnswers_SingleOption(t *testing.T) {
	options := []string{"Only option"}
	result := ShuffleAnswers(options, "A")
	if len(result.ShuffledOptions) != 1 {
		t.Errorf("expected 1 option, got %d", len(result.ShuffledOptions))
	}
	if result.CorrectShuffledIndex != 0 {
		t.Errorf("expected correct index 0, got %d", result.CorrectShuffledIndex)
	}
}
