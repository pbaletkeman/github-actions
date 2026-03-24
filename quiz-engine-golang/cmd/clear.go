package cmd

import (
	"fmt"

	"github.com/pbaletkeman/quiz-engine-golang/internal/cli"
	"github.com/pbaletkeman/quiz-engine-golang/internal/database"
	"github.com/spf13/cobra"
)

var (
	clearQuestions bool
	clearHistory   bool
	clearConfirm   bool
)

var clearCmd = &cobra.Command{
	Use:   "clear",
	Short: "Clear data from the database",
	Long:  `Clear questions, history, or all data from the database. Use --confirm to execute.`,
	RunE: func(cmd *cobra.Command, args []string) error {
		if !clearQuestions && !clearHistory {
			clearQuestions = true
			clearHistory = true
		}

		if !clearConfirm {
			if clearQuestions {
				cli.PrintWarning("Would clear: all questions")
			}
			if clearHistory {
				cli.PrintWarning("Would clear: all quiz history and responses")
			}
			cli.PrintInfo("Use --confirm to execute the clear operation.")
			return nil
		}

		db, err := database.NewDB(dbPath)
		if err != nil {
			return fmt.Errorf("failed to open database: %w", err)
		}
		defer db.Close()

		if clearHistory {
			if err := database.DeleteAllResponses(db); err != nil {
				return fmt.Errorf("failed to delete responses: %w", err)
			}
			cli.PrintSuccess("Cleared all quiz responses.")
		}
		if clearQuestions {
			if err := database.DeleteAllQuestions(db); err != nil {
				return fmt.Errorf("failed to delete questions: %w", err)
			}
			cli.PrintSuccess("Cleared all questions.")
		}

		return nil
	},
}

func init() {
	clearCmd.Flags().BoolVar(&clearQuestions, "questions", false, "Clear all questions")
	clearCmd.Flags().BoolVar(&clearHistory, "history", false, "Clear quiz history and responses")
	clearCmd.Flags().BoolVar(&clearConfirm, "confirm", false, "Actually execute the clear operation")
}
