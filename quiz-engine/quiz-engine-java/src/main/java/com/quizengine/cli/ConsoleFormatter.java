package com.quizengine.cli;

import com.quizengine.model.Question;
import com.quizengine.model.QuizSession;
import com.quizengine.util.QuizUtils;
import com.quizengine.util.ShuffleResult;

import java.util.List;

public class ConsoleFormatter {
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";
    public static final String BOLD = "\u001B[1m";

    public static void printQuestion(int num, int total, Question q, ShuffleResult shuffle) {
        System.out.println();
        System.out.printf("%s%sQuestion %d of %d%s%n", BOLD, CYAN, num, total, RESET);
        printSeparator();
        System.out.println(q.getQuestionText());
        System.out.println();
        List<String> opts = shuffle.getShuffledOptions();
        char letter = 'A';
        for (String opt : opts) {
            System.out.printf("  %c) %s%n", letter++, opt);
        }
        System.out.println();
    }

    public static void printCorrect() {
        System.out.printf("%s%s✓ Correct!%s%n", BOLD, GREEN, RESET);
    }

    public static void printIncorrect(String correctLetter, String correctText) {
        System.out.printf("%s%s✗ Incorrect.%s The correct answer was: %s) %s%n",
                BOLD, RED, RESET, correctLetter, correctText);
    }

    public static void printReview(List<Question> questions, List<ShuffleResult> shuffles,
                                    List<String> givenAnswers, List<Boolean> corrects) {
        System.out.println();
        printSeparator();
        System.out.printf("%s%sAnswer Review%s%n", BOLD, CYAN, RESET);
        printSeparator();
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            ShuffleResult shuffle = shuffles.get(i);
            String given = givenAnswers.get(i);
            boolean correct = corrects.get(i);
            String marker = correct
                    ? GREEN + BOLD + "✓" + RESET
                    : RED + BOLD + "✗" + RESET;
            System.out.printf("%s Q%-3d %s%n", marker, i + 1, q.getQuestionText().lines().findFirst().orElse(""));
            if (!correct) {
                String correctLetter = shuffle.getCorrectShuffledLetter();
                int correctIdx = shuffle.getCorrectShuffledIndex();
                String correctText = shuffle.getShuffledOptions().get(correctIdx);
                System.out.printf("       Your answer: %s%s%s  |  Correct: %s%s) %s%s%n",
                        RED, given, RESET, GREEN, correctLetter, correctText, RESET);
                if (q.getExplanation() != null && !q.getExplanation().isEmpty()) {
                    System.out.printf("       %sExplanation: %s%s%n", YELLOW, q.getExplanation(), RESET);
                }
            }
        }
        printSeparator();
    }

    public static void printResults(QuizSession session) {
        System.out.println();
        printSeparator();
        System.out.printf("%s%sQuiz Complete!%s%n", BOLD, CYAN, RESET);
        printSeparator();
        System.out.printf("Score: %s%d/%d (%.1f%%)%s%n",
                BOLD, session.getNumCorrect(), session.getNumQuestions(),
                session.getPercentageCorrect(), RESET);
        System.out.printf("Time:  %s%n", QuizUtils.formatTime(session.getTimeTakenSeconds()));
        printSeparator();
    }

    public static void printSessionHistory(List<QuizSession> sessions) {
        if (sessions.isEmpty()) {
            System.out.println("No quiz sessions found.");
            return;
        }
        System.out.printf("%-38s %-20s %5s %7s %10s%n", "Session ID", "Date", "Qs", "Correct", "Score");
        System.out.println("-".repeat(85));
        for (QuizSession s : sessions) {
            System.out.printf("%-38s %-20s %5d %7d %9.1f%%%n",
                    s.getSessionId(),
                    s.getStartedAt() != null ? s.getStartedAt() : "N/A",
                    s.getNumQuestions(),
                    s.getNumCorrect(),
                    s.getPercentageCorrect());
        }
    }

    public static void printBanner() {
        System.out.println(CYAN + BOLD);
        System.out.println("  ╔═══════════════════════════════╗");
        System.out.println("  ║   GitHub Actions Quiz Engine  ║");
        System.out.println("  ╚═══════════════════════════════╝");
        System.out.println(RESET);
    }

    public static void printSeparator() {
        System.out.println("─".repeat(60));
    }
}
