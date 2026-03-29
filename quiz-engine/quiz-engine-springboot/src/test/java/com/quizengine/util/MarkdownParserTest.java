package com.quizengine.util;

import com.quizengine.entity.Question;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MarkdownParserTest {

    // ── Legacy format ──────────────────────────────────────────────────────────

    private static final String LEGACY_SINGLE = """
        ## What is GitHub Actions?

        - A. A CI/CD platform
        - B. A version control system
        - C. A package manager
        - D. A code editor

        **Answer:** A
        **Explanation:** GitHub Actions is a CI/CD and automation platform.
        """;

    private static final String LEGACY_FIVE_OPTIONS = """
        ## Question 1: Best practice for secrets?

        - A. Hardcode them
        - B. Use environment variables
        - C. Use GitHub Secrets
        - D. Store in a file
        - E. None of the above

        **Answer:** C
        """;

    @Test
    void parseLegacyContent_singleQuestion() {
        List<Question> questions = MarkdownParser.parseContent(LEGACY_SINGLE, "test.md");
        assertThat(questions).hasSize(1);
        Question q = questions.get(0);
        assertThat(q.getQuestionText()).isEqualTo("What is GitHub Actions?");
        assertThat(q.getOptionA()).isEqualTo("A CI/CD platform");
        assertThat(q.getOptionB()).isEqualTo("A version control system");
        assertThat(q.getCorrectAnswer()).isEqualTo("A");
        assertThat(q.getExplanation()).contains("CI/CD");
        assertThat(q.getSourceFile()).isEqualTo("test.md");
    }

    @Test
    void parseLegacyContent_fiveOptions() {
        List<Question> questions = MarkdownParser.parseContent(LEGACY_FIVE_OPTIONS, "test.md");
        assertThat(questions).hasSize(1);
        Question q = questions.get(0);
        assertThat(q.getOptionE()).isEqualTo("None of the above");
        assertThat(q.getCorrectAnswer()).isEqualTo("C");
    }

    @Test
    void parseLegacyContent_emptyReturnsEmpty() {
        List<Question> questions = MarkdownParser.parseContent("# Just a header\n\nNo questions here.", "empty.md");
        assertThat(questions).isEmpty();
    }

    // ── gh-200 format ──────────────────────────────────────────────────────────

    private static final String GH200_CONTENT = """
        # GitHub Actions Study Guide

        ### Question 1 — Domain: Continuous Integration

        **Difficulty**: EASY
        **Answer Type**: Single Answer

        > **Scenario:**
        > A developer needs to automate tests on every commit.
        >
        > **Question:**
        > Which GitHub feature enables CI/CD workflows?

        - A) GitHub Actions
        - B) GitHub Pages
        - C) GitHub Packages
        - D) GitHub Copilot

        ---

        ### Question 2 — Domain: Security

        **Difficulty**: MEDIUM
        **Answer Type**: Single Answer

        > **Question:**
        > Where should you store sensitive credentials in GitHub Actions?

        - A) In the workflow YAML file
        - B) In a public repository
        - C) In GitHub Secrets
        - D) In a README file

        ---

        ### Question 3 — Domain: Multiple

        **Difficulty**: HARD
        **Answer Type**: Multiple Answer

        > **Question:**
        > Which of the following are valid trigger events?

        - A) push
        - B) pull_request
        - C) schedule
        - D) deploy

        ---

        ## Answer Key

        | Q# | Answer(s) | Explanation | Source | Difficulty |
        |----|-----------|-------------|--------|------------|
        | 1  | A         | GitHub Actions is the CI/CD platform. | GH Docs | EASY |
        | 2  | C         | Secrets are stored in repo settings under Secrets. | GH Docs | MEDIUM |
        | 3  | A, B, C   | Multiple valid triggers. | GH Docs | HARD |
        """;

    @Test
    void parseGh200Content_parsesQuestions() {
        List<Question> questions = MarkdownParser.parseContent(GH200_CONTENT, "gh200.md");
        // Q3 is multi-answer (A, B, C) so it gets skipped
        assertThat(questions).hasSize(2);
    }

    @Test
    void parseGh200Content_firstQuestion() {
        List<Question> questions = MarkdownParser.parseContent(GH200_CONTENT, "gh200.md");
        Question q = questions.get(0);
        assertThat(q.getQuestionText()).contains("Which GitHub feature enables CI/CD");
        assertThat(q.getOptionA()).isEqualTo("GitHub Actions");
        assertThat(q.getOptionB()).isEqualTo("GitHub Pages");
        assertThat(q.getOptionC()).isEqualTo("GitHub Packages");
        assertThat(q.getOptionD()).isEqualTo("GitHub Copilot");
        assertThat(q.getCorrectAnswer()).isEqualTo("A");
        assertThat(q.getExplanation()).contains("CI/CD platform");
        assertThat(q.getSection()).contains("Continuous Integration");
        assertThat(q.getDifficulty()).isEqualTo("EASY");
        assertThat(q.getSourceFile()).isEqualTo("gh200.md");
    }

    @Test
    void parseGh200Content_secondQuestion() {
        List<Question> questions = MarkdownParser.parseContent(GH200_CONTENT, "gh200.md");
        Question q = questions.get(1);
        assertThat(q.getCorrectAnswer()).isEqualTo("C");
        assertThat(q.getOptionC()).isEqualTo("In GitHub Secrets");
        assertThat(q.getDifficulty()).isEqualTo("MEDIUM");
    }

    @Test
    void parseGh200Content_skipsMultiAnswer() {
        // Q3 has "A, B, C" — should be skipped
        List<Question> questions = MarkdownParser.parseContent(GH200_CONTENT, "gh200.md");
        assertThat(questions).hasSize(2);
        assertThat(questions).noneMatch(q -> q.getQuestionText().contains("valid trigger events"));
    }

    @Test
    void parseGh200Content_emptyReturnsEmpty() {
        String content = "## Answer Key\n\n| Q# | Answer(s) | Explanation | Source | Difficulty |\n";
        List<Question> questions = MarkdownParser.parseContent(content, "empty.md");
        assertThat(questions).isEmpty();
    }

    // ── parseAnswerKey ──────────────────────────────────────────────────────────

    @Test
    void parseAnswerKey_parsesTable() {
        String content = """
            ## Answer Key

            | Q# | Answer(s) | Explanation | Source | Difficulty |
            |----|-----------|-------------|--------|------------|
            | 1  | A         | First explanation | GH  | EASY  |
            | 2  | B, C      | Multi answer      | GH  | HARD  |
            | 3  | D         |                   | GH  | EASY  |
            """;
        Map<Integer, String[]> key = MarkdownParser.parseAnswerKey(content);
        assertThat(key).containsKey(1);
        assertThat(key.get(1)[0]).isEqualTo("A");
        assertThat(key.get(1)[1]).isEqualTo("First explanation");
        assertThat(key).containsKey(2);
        assertThat(key.get(2)[0]).isEqualTo("B, C");
        assertThat(key).containsKey(3);
        assertThat(key.get(3)[1]).isNull();  // blank explanation
    }

    @Test
    void parseAnswerKey_noSection_returnsEmpty() {
        Map<Integer, String[]> key = MarkdownParser.parseAnswerKey("No answer key here.");
        assertThat(key).isEmpty();
    }

    // ── format dispatch ────────────────────────────────────────────────────────

    @Test
    void parseContent_dispatchesToGh200WhenAnswerKeyPresent() {
        String content = GH200_CONTENT;
        List<Question> gh200 = MarkdownParser.parseContent(content, "x.md");
        // gh-200 format is dispatched when "## Answer Key" is present
        assertThat(gh200).isNotEmpty();
        // Section field is populated (a gh-200-only field)
        assertThat(gh200.get(0).getSection()).isNotNull();
    }

    @Test
    void parseContent_dispatchesToLegacyWhenNoAnswerKey() {
        List<Question> legacy = MarkdownParser.parseContent(LEGACY_SINGLE, "x.md");
        assertThat(legacy).hasSize(1);
    }
}
