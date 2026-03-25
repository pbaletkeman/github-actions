package cmd

import (
	"fmt"

	"github.com/pbaletkeman/quiz-engine-golang/internal/cli"
	"github.com/pbaletkeman/quiz-engine-golang/internal/database"
	"github.com/pbaletkeman/quiz-engine-golang/internal/parser"
	"github.com/spf13/cobra"
)

var importFile string

var importCmd = &cobra.Command{
	Use:   "import",
	Short: "Import questions from a markdown file",
	Long:  `Import quiz questions from a markdown file into the database.`,
	RunE: func(cmd *cobra.Command, args []string) error {
		if importFile == "" {
			return fmt.Errorf("--file flag is required")
		}

		db, err := database.NewDB(dbPath)
		if err != nil {
			return fmt.Errorf("failed to open database: %w", err)
		}
		defer db.Close()

		questions, err := parser.ParseMarkdownFile(importFile)
		if err != nil {
			return fmt.Errorf("failed to parse file: %w", err)
		}

		imported := 0
		skipped := 0
		var errs []string

		for _, q := range questions {
			q.SourceFile = importFile
			id, err := database.InsertQuestion(db, q)
			if err != nil {
				errs = append(errs, err.Error())
			} else if id == 0 {
				skipped++
			} else {
				imported++
			}
		}

		cli.PrintSuccess(fmt.Sprintf("Imported: %d questions", imported))
		if skipped > 0 {
			cli.PrintWarning(fmt.Sprintf("Skipped (duplicates): %d questions", skipped))
		}
		if len(errs) > 0 {
			for _, e := range errs {
				cli.PrintError(fmt.Sprintf("Error: %s", e))
			}
		}

		return nil
	},
}

func init() {
	importCmd.Flags().StringVarP(&importFile, "file", "f", "", "Markdown file to import (required)")
}
