package engine

import "math/rand"

// ShuffleResult contains the shuffled options and metadata for mapping back to original answers.
type ShuffleResult struct {
	ShuffledOptions      []string
	CorrectShuffledIndex int
	PositionMap          map[int]string // new position -> original option letter (A/B/C/D/E)
}

// ShuffleAnswers takes a question's options and the correct answer letter,
// and returns shuffled options with the index of the correct answer in the new order.
func ShuffleAnswers(options []string, correctAnswer string) ShuffleResult {
	letterToIdx := map[string]int{"A": 0, "B": 1, "C": 2, "D": 3, "E": 4}
	correctIdx := 0
	if idx, ok := letterToIdx[correctAnswer]; ok && idx < len(options) {
		correctIdx = idx
	}

	n := len(options)
	perm := rand.Perm(n)
	shuffled := make([]string, n)
	posMap := make(map[int]string)
	letters := []string{"A", "B", "C", "D", "E"}

	newCorrectIdx := 0
	for newIdx, oldIdx := range perm {
		shuffled[newIdx] = options[oldIdx]
		posMap[newIdx] = letters[oldIdx]
		if oldIdx == correctIdx {
			newCorrectIdx = newIdx
		}
	}

	return ShuffleResult{
		ShuffledOptions:      shuffled,
		CorrectShuffledIndex: newCorrectIdx,
		PositionMap:          posMap,
	}
}
