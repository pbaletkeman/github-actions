package com.quizengine.util;

import com.quizengine.model.Question;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MarkdownParserTest {

    @TempDir
    Path tempDir;

    // Mini document matching the actual source format
    private static final String SAMPLE_DOCUMENT =
            "## Questions\n\n" +
            "---\n\n" +
            "### Question 1 \u2014 VS Code Extension\n\n" +
            "**Difficulty**: Easy\n" +
            "**Answer Type**: one\n" +
            "**Topic**: Extension capabilities\n\n" +
            "**Question**:\n" +
            "What is the primary purpose of the GitHub Actions extension for VS Code?\n\n" +
            "- A) Execute workflow runs locally\n" +
            "- B) Provide YAML schema validation and IntelliSense\n" +
            "- C) Deploy workflow files directly to GitHub\n" +
            "- D) Manage GitHub secrets from the IDE\n\n" +
            "---\n\n" +
            "### Question 2 \u2014 Multi-Select Section\n\n" +
            "**Difficulty**: Medium\n" +
            "**Answer Type**: many\n" +
            "**Topic**: Multi select\n\n" +
            "**Question**:\n" +
            "Which are valid?\n\n" +
            "- A) Option A\n" +
            "- B) Option B\n" +
            "- C) Option C\n\n" +
            "---\n\n" +
            "### Question 3 \u2014 Scenario Section\n\n" +
            "**Difficulty**: Medium\n" +
            "**Answer Type**: one\n" +
            "**Topic**: Scenario question\n\n" +
            "**Scenario**:\n" +
            "A developer has a workflow and wants to validate it.\n\n" +
            "**Question**:\n" +
            "Which step should the developer take?\n\n" +
            "- A) Run the workflow\n" +
            "- B) Use the extension's syntax validation\n" +
            "- C) Push to GitHub\n" +
            "- D) Install a plugin\n\n" +
            "---\n\n" +
            "## Answer Key\n\n" +
            "| Q# | Answer(s) | Explanation | Source | Difficulty |\n" +
            "|----|-----------|-------------|--------|------------|\n" +
            "| 1 | B | The extension provides YAML schema validation. | source.md | Easy |\n" +
            "| 2 | A, C | Multi-select answer. | source.md | Medium |\n" +
            "| 3 | B | Use the extension for validation. | source.md | Medium |\n";

    @Test
    void parseFile_skipsMultiSelectQuestions() throws IOException {
        Path file = tempDir.resolve("test.md");
        Files.writeString(file, SAMPLE_DOCUMENT);
        List<Question> questions = MarkdownParser.parseFile(file.toString());
        assertEquals(2, questions.size(), "Should import only 'one' type questions (Q1 and Q3, skipping Q2 'many')");
    }

    @Test
    void parseFile_correctAnswerFromKey() throws IOException {
        Path file = tempDir.resolve("test.md");
        Files.writeString(file, SAMPLE_DOCUMENT);
        List<Question> questions = MarkdownParser.parseFile(file.toString());
        assertEquals("B", questions.get(0).getCorrectAnswer());
    }

    @Test
    void parseFile_sectionExtracted() throws IOException {
        Path file = tempDir.resolve("test.md");
        Files.writeString(file, SAMPLE_DOCUMENT);
        List<Question> questions = MarkdownParser.parseFile(file.toString());
        assertEquals("VS Code Extension", questions.get(0).getSection());
    }

    @Test
    void parseFile_explanationFromKey() throws IOException {
        Path file = tempDir.resolve("test.md");
        Files.writeString(file, SAMPLE_DOCUMENT);
        List<Question> questions = MarkdownParser.parseFile(file.toString());
        assertTrue(questions.get(0).getExplanation().contains("YAML schema validation"));
    }

    @Test
    void parseFile_optionsParsed() throws IOException {
        Path file = tempDir.resolve("test.md");
        Files.writeString(file, SAMPLE_DOCUMENT);
        List<Question> questions = MarkdownParser.parseFile(file.toString());
        assertEquals("Execute workflow runs locally", questions.get(0).getOptionA());
        assertEquals("Provide YAML schema validation and IntelliSense", questions.get(0).getOptionB());
    }

    @Test
    void parseFile_scenarioPrependedToQuestion() throws IOException {
        Path file = tempDir.resolve("test.md");
        Files.writeString(file, SAMPLE_DOCUMENT);
        List<Question> questions = MarkdownParser.parseFile(file.toString());
        // Q3 has a scenario prepended to the question text
        String text = questions.get(1).getQuestionText();
        assertTrue(text.contains("A developer has a workflow"), "Should include scenario text");
        assertTrue(text.contains("Which step should the developer take?"), "Should include question text");
    }

    @Test
    void parseFile_emptyContent_returnsEmptyList() throws IOException {
        Path file = tempDir.resolve("empty.md");
        Files.writeString(file, "");
        List<Question> questions = MarkdownParser.parseFile(file.toString());
        assertTrue(questions.isEmpty());
    }

    @Test
    void parseFile_noAnswerKey_returnsEmptyList() throws IOException {
        String noKeyDoc = "## Questions\n\n" +
                "### Question 1 \u2014 Test\n\n" +
                "**Answer Type**: one\n**Difficulty**: Easy\n\n" +
                "**Question**:\nTest question?\n\n" +
                "- A) Option A\n- B) Option B\n\n";
        Path file = tempDir.resolve("nokey.md");
        Files.writeString(file, noKeyDoc);
        List<Question> questions = MarkdownParser.parseFile(file.toString());
        assertTrue(questions.isEmpty(), "Without answer key, should import nothing");
    }
}
