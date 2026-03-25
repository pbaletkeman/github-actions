package models

import "time"

type Question struct {
	ID            int64
	QuestionText  string
	OptionA       string
	OptionB       string
	OptionC       string
	OptionD       string
	OptionE       string
	CorrectAnswer string
	Explanation   string
	Section       string
	Difficulty    string
	SourceFile    string
	CreatedAt     time.Time
	UsageCycle    int64
	TimesUsed     int64
	LastUsedAt    *time.Time
}
