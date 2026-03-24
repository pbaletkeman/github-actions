package cmd

import (
	"fmt"
	"os"

	"github.com/spf13/cobra"
)

var dbPath string

var rootCmd = &cobra.Command{
	Use:     "quiz-engine",
	Short:   "A CLI quiz engine for GH-200 certification practice",
	Long:    `A CLI quiz engine for GH-200 certification practice with question cycling, answer shuffling, and history tracking.`,
	Version: "1.0.0",
}

// Execute runs the root command.
func Execute() {
	if err := rootCmd.Execute(); err != nil {
		fmt.Fprintln(os.Stderr, err)
		os.Exit(1)
	}
}

func init() {
	rootCmd.PersistentFlags().StringVar(&dbPath, "db", "./quiz.db", "Database file path")
	rootCmd.AddCommand(quizCmd)
	rootCmd.AddCommand(importCmd)
	rootCmd.AddCommand(historyCmd)
	rootCmd.AddCommand(clearCmd)
}
