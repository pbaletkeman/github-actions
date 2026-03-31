package cli

import (
	"fmt"
	"os"
	"strings"

	"github.com/fatih/color"
	"github.com/olekukonko/tablewriter"
	"github.com/pbaletkeman/quiz-engine-golang/internal/engine"
	"github.com/pbaletkeman/quiz-engine-golang/internal/models"
)

// DisplayQuestion shows a question with its shuffled options.
func DisplayQuestion(qIdx int, total int, q models.Question, shuffleResult engine.ShuffleResult) {
	fmt.Printf("\n")
	color.New(color.FgYellow, color.Bold).Printf("Question %d of %d\n", qIdx+1, total)
	fmt.Println(strings.Repeat("-", 60))
	fmt.Printf("%s\n\n", q.QuestionText)

	letters := []string{"A", "B", "C", "D", "E"}
	for i, opt := range shuffleResult.ShuffledOptions {
		if i < len(letters) {
			fmt.Printf("  %s) %s\n", letters[i], opt)
		}
	}
	fmt.Println()
}

// DisplayResult shows whether the answer was correct with explanation.
func DisplayResult(correct bool, correctAnswer string, explanation string) {
	if correct {
		color.New(color.FgGreen, color.Bold).Println("✓ Correct!")
	} else {
		color.New(color.FgRed, color.Bold).Printf("✗ Incorrect. The correct answer was: %s\n", correctAnswer)
	}
	if explanation != "" {
		color.New(color.FgCyan).Printf("  Explanation: %s\n", explanation)
	}
	fmt.Println()
}

// DisplayFinalScore shows a score summary table.
func DisplayFinalScore(session models.QuizSession) {
	fmt.Printf("\n")
	color.New(color.FgCyan, color.Bold).Println("=== Quiz Complete ===")

	table := tablewriter.NewWriter(os.Stdout)
	table.SetHeader([]string{"Metric", "Value"})
	table.Append([]string{"Questions", fmt.Sprintf("%d", session.NumQuestions)})
	table.Append([]string{"Correct", fmt.Sprintf("%d", session.NumCorrect)})
	table.Append([]string{"Score", fmt.Sprintf("%.1f%%", session.PercentageCorrect)})
	table.Append([]string{"Time Taken", fmt.Sprintf("%ds", session.TimeTakenSeconds)})
	table.Render()
	fmt.Println()
}

// DisplayHistoryTable shows a table of past quiz sessions.
func DisplayHistoryTable(sessions []models.QuizSession) {
	if len(sessions) == 0 {
		fmt.Println("No quiz history found.")
		return
	}

	table := tablewriter.NewWriter(os.Stdout)
	table.SetHeader([]string{"Session ID", "Date", "Questions", "Correct", "Score", "Time"})
	for _, s := range sessions {
		endedStr := ""
		if s.EndedAt != nil {
			endedStr = s.EndedAt.Format("2006-01-02 15:04")
		}
		table.Append([]string{
			s.SessionID,
			endedStr,
			fmt.Sprintf("%d", s.NumQuestions),
			fmt.Sprintf("%d", s.NumCorrect),
			fmt.Sprintf("%.1f%%", s.PercentageCorrect),
			fmt.Sprintf("%ds", s.TimeTakenSeconds),
		})
	}
	table.Render()
}

// DisplayReview shows a review table of questions and responses.
func DisplayReview(questions []models.Question, responses []models.QuizResponse) {
	if len(questions) == 0 {
		fmt.Println("No review data available.")
		return
	}

	responseMap := make(map[int64]models.QuizResponse)
	for _, r := range responses {
		responseMap[r.QuestionID] = r
	}

	table := tablewriter.NewWriter(os.Stdout)
	table.SetHeader([]string{"#", "Question", "Correct Answer", "Your Answer", "Result"})
	table.SetColWidth(40)

	for i, q := range questions {
		resp, ok := responseMap[q.ID]
		userAnswer := ""
		result := ""
		if ok {
			userAnswer = resp.UserAnswer
			if resp.IsCorrect {
				result = "✓"
			} else {
				result = "✗"
			}
		}
		text := q.QuestionText
		if len(text) > 40 {
			text = text[:37] + "..."
		}
		table.Append([]string{
			fmt.Sprintf("%d", i+1),
			text,
			q.CorrectAnswer,
			userAnswer,
			result,
		})
	}
	table.Render()
}
