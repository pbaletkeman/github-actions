package com.quizengine.cli;

import com.quizengine.model.QuizSession;
import com.quizengine.util.ShuffleResult;
import com.quizengine.model.Question;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleFormatterTest {

    private String captureOutput(Runnable action) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(baos));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return baos.toString();
    }

    @Test
    void printBanner_outputsTitle() {
        String output = captureOutput(ConsoleFormatter::printBanner);
        assertTrue(output.contains("Quiz Engine"));
    }

    @Test
    void printSeparator_outputsDashes() {
        String output = captureOutput(ConsoleFormatter::printSeparator);
        assertFalse(output.isEmpty());
    }

    @Test
    void printCorrect_outputsCorrect() {
        String output = captureOutput(ConsoleFormatter::printCorrect);
        assertTrue(output.contains("Correct"));
    }

    @Test
    void printIncorrect_showsCorrectAnswer() {
        String output = captureOutput(() -> ConsoleFormatter.printIncorrect("A", "Some answer"));
        assertTrue(output.contains("Incorrect"));
        assertTrue(output.contains("Some answer"));
    }

    @Test
    void printResults_showsScore() {
        QuizSession session = new QuizSession.Builder()
                .sessionId("test")
                .numQuestions(10)
                .numCorrect(8)
                .percentageCorrect(80.0)
                .timeTakenSeconds(300)
                .build();
        String output = captureOutput(() -> ConsoleFormatter.printResults(session));
        assertTrue(output.contains("8"));
        assertTrue(output.contains("10"));
    }

    @Test
    void printSessionHistory_withSessions() {
        QuizSession session = new QuizSession.Builder()
                .sessionId("session-abc")
                .startedAt("2024-01-01 10:00:00")
                .numQuestions(5)
                .numCorrect(4)
                .percentageCorrect(80.0)
                .build();
        String output = captureOutput(() -> ConsoleFormatter.printSessionHistory(List.of(session)));
        assertTrue(output.contains("session-abc"));
    }

    @Test
    void printSessionHistory_whenEmpty() {
        String output = captureOutput(() -> ConsoleFormatter.printSessionHistory(Collections.emptyList()));
        assertTrue(output.contains("No quiz sessions found"));
    }

    @Test
    void printQuestion_showsQuestionText() {
        Question q = new Question.Builder()
                .id(1)
                .questionText("What is GitHub Actions?")
                .optionA("Option A")
                .optionB("Option B")
                .optionC("Option C")
                .optionD("Option D")
                .build();
        ShuffleResult shuffle = new ShuffleResult(
                Arrays.asList("Option A", "Option B", "Option C", "Option D"),
                0,
                new HashMap<>()
        );
        String output = captureOutput(() -> ConsoleFormatter.printQuestion(1, 5, q, shuffle));
        assertTrue(output.contains("What is GitHub Actions?"));
        assertTrue(output.contains("Option A"));
    }

    @Test
    void printSessionHistory_nullStartedAt_showsNA() {
        QuizSession session = new QuizSession.Builder()
                .sessionId("null-date-session")
                .numQuestions(3)
                .numCorrect(2)
                .percentageCorrect(66.7)
                .build();
        String output = captureOutput(() -> ConsoleFormatter.printSessionHistory(List.of(session)));
        assertTrue(output.contains("N/A"));
    }
}
