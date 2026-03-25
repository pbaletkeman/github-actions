package models

type QuizResponse struct {
	ID               int64
	SessionID        string
	QuestionID       int64
	UserAnswer       string
	IsCorrect        bool
	TimeTakenSeconds int
}
