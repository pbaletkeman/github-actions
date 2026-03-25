package parser

import (
	"fmt"
	"os"
	"regexp"
	"strings"

	"github.com/pbaletkeman/quiz-engine-golang/internal/models"
)

var (
	questionRe    = regexp.MustCompile(`(?m)^##\s+(.+)$`)
	optionRe      = regexp.MustCompile(`(?m)^-\s+([A-E])\)\s+(.+)$`)
	answerRe      = regexp.MustCompile(`(?m)^\*\*Answer:\s*([A-E])\*\*`)
	explanationRe = regexp.MustCompile(`(?m)^\*Explanation:\s*(.+)\*`)
)

// ParseMarkdownFile reads a file and parses questions from it.
func ParseMarkdownFile(filePath string) ([]models.Question, error) {
	data, err := os.ReadFile(filePath)
	if err != nil {
		return nil, fmt.Errorf("reading file %s: %w", filePath, err)
	}
	return ParseMarkdownContent(string(data))
}

// ParseMarkdownContent parses questions from markdown content string.
func ParseMarkdownContent(content string) ([]models.Question, error) {
	blocks := splitIntoBlocks(content)

	var questions []models.Question
	var parseErrors []string

	for _, block := range blocks {
		q, err := parseBlock(block)
		if err != nil {
			parseErrors = append(parseErrors, err.Error())
			continue
		}
		questions = append(questions, q)
	}

	if len(parseErrors) > 0 && len(questions) == 0 {
		return nil, fmt.Errorf("parse errors: %s", strings.Join(parseErrors, "; "))
	}
	return questions, nil
}

func splitIntoBlocks(content string) []string {
	lines := strings.Split(content, "\n")
	var blocks []string
	var current []string

	for _, line := range lines {
		if strings.HasPrefix(line, "## ") && len(current) > 0 {
			if block := strings.TrimSpace(strings.Join(current, "\n")); block != "" {
				blocks = append(blocks, strings.Join(current, "\n"))
			}
			current = []string{line}
		} else {
			current = append(current, line)
		}
	}
	if len(current) > 0 {
		if block := strings.TrimSpace(strings.Join(current, "\n")); block != "" {
			blocks = append(blocks, strings.Join(current, "\n"))
		}
	}
	return blocks
}

func parseBlock(block string) (models.Question, error) {
	var q models.Question

	qMatch := questionRe.FindStringSubmatch(block)
	if qMatch == nil {
		return q, fmt.Errorf("no question text found in block")
	}
	q.QuestionText = strings.TrimSpace(qMatch[1])

	optMatches := optionRe.FindAllStringSubmatch(block, -1)
	optMap := make(map[string]string)
	for _, m := range optMatches {
		optMap[m[1]] = strings.TrimSpace(m[2])
	}

	if len(optMap) < 4 {
		return q, fmt.Errorf("question %q has fewer than 4 options", q.QuestionText)
	}

	q.OptionA = optMap["A"]
	q.OptionB = optMap["B"]
	q.OptionC = optMap["C"]
	q.OptionD = optMap["D"]
	if v, ok := optMap["E"]; ok {
		q.OptionE = v
	}

	ansMatch := answerRe.FindStringSubmatch(block)
	if ansMatch == nil {
		return q, fmt.Errorf("question %q has no answer", q.QuestionText)
	}
	q.CorrectAnswer = ansMatch[1]

	expMatch := explanationRe.FindStringSubmatch(block)
	if expMatch != nil {
		q.Explanation = strings.TrimSpace(expMatch[1])
	}

	return q, nil
}
