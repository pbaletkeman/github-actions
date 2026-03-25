package cmd

import (
	"fmt"

	"github.com/pbaletkeman/quiz-engine-golang/internal/cli"
	"github.com/pbaletkeman/quiz-engine-golang/internal/database"
	"github.com/pbaletkeman/quiz-engine-golang/internal/service"
	"github.com/spf13/cobra"
)

var (
	histSessionID string
	histReview    bool
)

var historyCmd = &cobra.Command{
	Use:   "history",
	Short: "View quiz history",
	Long:  `View past quiz sessions and results.`,
	RunE: func(cmd *cobra.Command, args []string) error {
		db, err := database.NewDB(dbPath)
		if err != nil {
			return fmt.Errorf("failed to open database: %w", err)
		}
		defer db.Close()

		if histSessionID != "" && histReview {
			session, responses, err := service.GetSessionWithResponses(db, histSessionID)
			if err != nil {
				return fmt.Errorf("failed to get session: %w", err)
			}
			cli.DisplayFinalScore(*session)
			fmt.Printf("Session has %d responses.\n", len(responses))
			return nil
		}

		sessions, err := service.ListSessions(db)
		if err != nil {
			return fmt.Errorf("failed to list sessions: %w", err)
		}
		cli.DisplayHistoryTable(sessions)
		return nil
	},
}

func init() {
	historyCmd.Flags().StringVar(&histSessionID, "session-id", "", "Session ID to view")
	historyCmd.Flags().BoolVar(&histReview, "review", false, "Show responses for the session")
}
