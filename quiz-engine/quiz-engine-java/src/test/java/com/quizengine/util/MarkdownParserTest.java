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

    private static final String SAMPLE_QUESTION = "## Q1\n" +
            "> What is CI/CD?\n" +
            "- A) Continuous Integration/Continuous Deployment\n" +
            "- B) Code Integration/Code Deployment\n" +
            "- C) Continuous Inspection/Continuous Delivery\n" +
            "- D) Code Inspection/Code Delivery\n" +
            "**Answer: A**\n" +
            "> **Explanation:** CI/CD is a method to deliver code changes frequently.\n";

    @Test
    void parseFile_extractsQuestionsCorrectly() throws IOException {
        Path file = tempDir.resolve("test.md");
        Files.writeString(file, SAMPLE_QUESTION);
        List<Question> questions = MarkdownParser.parseFile(file.toString());
        assertEquals(1, questions.size());
        assertEquals("A", questions.get(0).getCorrectAnswer());
    }

    @Test
    void parseBlock_throwsOnMissingAnswer() {
        String blockWithoutAnswer = "## Q1\n> A question?\n- A) Option A\n- B) Option B\n- C) Option C\n- D) Option D\n";
        assertThrows(IllegalArgumentException.class, () -> MarkdownParser.parseBlock(blockWithoutAnswer));
    }

    @Test
    void parseFile_handlesMultipleQuestions() throws IOException {
        String content = SAMPLE_QUESTION + "\n" +
                "## Q2\n" +
                "> What is GitHub?\n" +
                "- A) A code hosting platform\n" +
                "- B) A programming language\n" +
                "- C) An operating system\n" +
                "- D) A database system\n" +
                "**Answer: A**\n";
        Path file = tempDir.resolve("multi.md");
        Files.writeString(file, content);
        List<Question> questions = MarkdownParser.parseFile(file.toString());
        assertEquals(2, questions.size());
    }

    @Test
    void parseFile_parsesExplanation() throws IOException {
        Path file = tempDir.resolve("explain.md");
        Files.writeString(file, SAMPLE_QUESTION);
        List<Question> questions = MarkdownParser.parseFile(file.toString());
        assertNotNull(questions.get(0).getExplanation());
        assertTrue(questions.get(0).getExplanation().contains("CI/CD"));
    }
}
