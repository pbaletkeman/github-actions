package models

import "time"

type QuizSession struct {
	SessionID         string
	StartedAt         time.Time
	EndedAt           *time.Time
	NumQuestions      int
	NumCorrect        int
	PercentageCorrect float64
	TimeTakenSeconds  int
}
