package cmd

import (
	"fmt"

	"github.com/pbaletkeman/quiz-engine-golang/internal/cli"
	"github.com/pbaletkeman/quiz-engine-golang/internal/service"
	"github.com/spf13/cobra"
)

var (
	numQuestions int
	difficulty   string
	section      string
)

var quizCmd = &cobra.Command{
	Use:   "quiz",
	Short: "Take a quiz",
	Long:  `Start an interactive quiz session with random questions.`,
	RunE: func(cmd *cobra.Command, args []string) error {
		cfg := service.Config{
			DBPath:       dbPath,
			NumQuestions: numQuestions,
		}
		svc, err := service.NewQuizService(cfg)
		if err != nil {
			return fmt.Errorf("failed to initialize service: %w", err)
		}
		defer svc.Close()

		qe := svc.NewEngine()
		qe.Config.Difficulty = difficulty
		qe.Config.Section = section

		if err := qe.LoadQuestions(); err != nil {
			return fmt.Errorf("failed to load questions: %w", err)
		}

		if len(qe.Questions) == 0 {
			cli.PrintWarning("No questions found. Import questions first using: quiz-engine import --file <file>")
			return nil
		}

		cli.PrintHeader(fmt.Sprintf("Starting Quiz: %d Questions", len(qe.Questions)))

		letters := []string{"A", "B", "C", "D", "E"}
		for i, q := range qe.Questions {
			shuffle := qe.ShuffleData[i]
			cli.DisplayQuestion(i, len(qe.Questions), q, shuffle)

			validOpts := letters[:len(shuffle.ShuffledOptions)]
			answer, timeTaken := cli.GetTimedAnswer(validOpts, 60)
			if answer == "" {
				answer = "A"
			}

			if err := qe.SubmitAnswer(i, answer, timeTaken); err != nil {
				cli.PrintError(fmt.Sprintf("Error submitting answer: %v", err))
			}
		}

		session, err := qe.FinalizeQuiz()
		if err != nil {
			return fmt.Errorf("failed to finalize quiz: %w", err)
		}

		questions, responses, err := qe.GetReviewData()
		if err == nil {
			cli.DisplayReview(questions, responses)
		}

		cli.DisplayFinalScore(session)

		return nil
	},
}

func init() {
	quizCmd.Flags().IntVarP(&numQuestions, "questions", "q", 20, "Number of questions")
	quizCmd.Flags().StringVar(&difficulty, "difficulty", "", "Filter by difficulty (easy/medium/hard)")
	quizCmd.Flags().StringVar(&section, "section", "", "Filter by section")
}
