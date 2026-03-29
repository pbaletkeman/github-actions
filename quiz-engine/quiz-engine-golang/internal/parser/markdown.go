package parser

import (
	"fmt"
	"os"
	"regexp"
	"strconv"
	"strings"

	"github.com/pbaletkeman/quiz-engine-golang/internal/models"
)

var (
	questionHeaderRe = regexp.MustCompile(`(?m)^###\s+Question\s+(\d+)\s*[-—–]+\s*(.+)$`)
	difficultyRe     = regexp.MustCompile(`(?m)^\*\*Difficulty\*\*:\s*(.+)$`)
	answerTypeRe     = regexp.MustCompile(`(?m)^\*\*Answer Type\*\*:\s*(.+)$`)
	optionRe         = regexp.MustCompile(`(?m)^-\s+([A-E])\)\s+(.+)$`)
	answerRowRe      = regexp.MustCompile(`^\|\s*(\d+)\s*\|\s*([^|]+?)\s*\|\s*([^|]*?)\s*\|\s*([^|]*?)\s*\|\s*([^|]*?)\s*\|`)
)

type answerEntry struct {
	answer      string
	explanation string
}

// ParseMarkdownFile reads a file and parses questions from it.
func ParseMarkdownFile(filePath string) ([]models.Question, error) {
	data, err := os.ReadFile(filePath)
	if err != nil {
		return nil, fmt.Errorf("reading file %s: %w", filePath, err)
	}
	return ParseMarkdownContent(string(data))
}

// ParseMarkdownContent parses questions from markdown content string.
// Expects the format produced by the quiz source material generator:
//   - Questions under "## Questions" section, each starting with "### Question N — Section"
//   - Answers in a separate "## Answer Key" table: | N | Answer | Explanation | Source | Difficulty |
//   - Questions with Answer Type "many" or "none" are silently skipped.
func ParseMarkdownContent(content string) ([]models.Question, error) {
	content = strings.ReplaceAll(content, "\r\n", "\n")

	answerKey := parseAnswerKey(content)
	blocks := extractQuestionBlocks(content)

	var questions []models.Question
	var parseErrors []string

	for _, block := range blocks {
		q, skip, err := parseQuestionBlock(block, answerKey)
		if err != nil {
			parseErrors = append(parseErrors, err.Error())
			continue
		}
		if skip {
			continue
		}
		questions = append(questions, q)
	}

	if len(questions) == 0 && len(parseErrors) > 0 {
		return nil, fmt.Errorf("parse errors: %s", strings.Join(parseErrors, "; "))
	}
	return questions, nil
}

func parseAnswerKey(content string) map[int]answerEntry {
	result := make(map[int]answerEntry)

	idx := strings.Index(content, "## Answer Key")
	if idx == -1 {
		return result
	}

	for _, line := range strings.Split(content[idx:], "\n") {
		m := answerRowRe.FindStringSubmatch(line)
		if m == nil {
			continue
		}
		num, err := strconv.Atoi(m[1])
		if err != nil {
			continue
		}
		result[num] = answerEntry{
			answer:      m[2],
			explanation: m[3],
		}
	}
	return result
}

func extractQuestionBlocks(content string) []string {
	// Scope parsing to the ## Questions section only.
	qSection := content
	if idx := strings.Index(content, "## Questions"); idx != -1 {
		qSection = content[idx:]
		if next := strings.Index(qSection[1:], "\n## "); next != -1 {
			qSection = qSection[:next+1]
		}
	}

	var blocks []string
	var current []string
	for _, line := range strings.Split(qSection, "\n") {
		if questionHeaderRe.MatchString(line) {
			if block := strings.TrimSpace(strings.Join(current, "\n")); block != "" {
				blocks = append(blocks, block)
			}
			current = []string{line}
		} else if len(current) > 0 {
			current = append(current, line)
		}
	}
	if block := strings.TrimSpace(strings.Join(current, "\n")); block != "" {
		blocks = append(blocks, block)
	}
	return blocks
}

func parseQuestionBlock(block string, answers map[int]answerEntry) (q models.Question, skip bool, err error) {
	hm := questionHeaderRe.FindStringSubmatch(block)
	if hm == nil {
		return q, false, fmt.Errorf("no question header found in block")
	}
	qNum, _ := strconv.Atoi(hm[1])
	section := strings.TrimSpace(hm[2])

	difficulty := ""
	if m := difficultyRe.FindStringSubmatch(block); m != nil {
		difficulty = strings.TrimSpace(m[1])
	}

	// Skip multi-select and no-answer questions — engine supports single answer only.
	if m := answerTypeRe.FindStringSubmatch(block); m != nil {
		if at := strings.ToLower(strings.TrimSpace(m[1])); at != "one" {
			return q, true, nil
		}
	}

	// Extract optional scenario and question text line-by-line.
	var scenarioLines, questionLines []string
	inScenario, inQuestion := false, false

	for _, line := range strings.Split(block, "\n") {
		trimmed := strings.TrimSpace(line)
		switch {
		case strings.HasPrefix(trimmed, "**Scenario**:"):
			inScenario, inQuestion = true, false
			if rest := strings.TrimSpace(strings.TrimPrefix(trimmed, "**Scenario**:")); rest != "" {
				scenarioLines = append(scenarioLines, rest)
			}
		case strings.HasPrefix(trimmed, "**Question**:"):
			inScenario, inQuestion = false, true
			if rest := strings.TrimSpace(strings.TrimPrefix(trimmed, "**Question**:")); rest != "" {
				questionLines = append(questionLines, rest)
			}
		case inScenario:
			if optionRe.MatchString(trimmed) || trimmed == "---" {
				inScenario = false
			} else {
				scenarioLines = append(scenarioLines, line)
			}
		case inQuestion:
			if optionRe.MatchString(trimmed) || trimmed == "---" {
				inQuestion = false
			} else {
				questionLines = append(questionLines, line)
			}
		}
	}

	questionText := strings.TrimSpace(strings.Join(questionLines, "\n"))
	scenarioText := strings.TrimSpace(strings.Join(scenarioLines, "\n"))

	if questionText == "" {
		return q, false, fmt.Errorf("question %d has no question text", qNum)
	}
	if scenarioText != "" {
		questionText = scenarioText + "\n\n" + questionText
	}

	optMatches := optionRe.FindAllStringSubmatch(block, -1)
	optMap := make(map[string]string)
	for _, m := range optMatches {
		optMap[m[1]] = strings.TrimSpace(m[2])
	}
	if len(optMap) < 4 {
		return q, false, fmt.Errorf("question %d has fewer than 4 options", qNum)
	}

	entry, ok := answers[qNum]
	if !ok {
		return q, false, fmt.Errorf("question %d not found in answer key", qNum)
	}

	return models.Question{
		QuestionText:  questionText,
		OptionA:       optMap["A"],
		OptionB:       optMap["B"],
		OptionC:       optMap["C"],
		OptionD:       optMap["D"],
		OptionE:       optMap["E"],
		CorrectAnswer: entry.answer,
		Explanation:   entry.explanation,
		Section:       section,
		Difficulty:    difficulty,
	}, false, nil
}
