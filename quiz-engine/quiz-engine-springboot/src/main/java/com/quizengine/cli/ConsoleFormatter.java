package com.quizengine.cli;

public class ConsoleFormatter {

    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";
    public static final String BOLD = "\u001B[1m";

    private ConsoleFormatter() {}

    public static void printSuccess(String message) {
        System.out.println(GREEN + "✓ " + message + RESET);
    }

    public static void printError(String message) {
        System.out.println(RED + "✗ " + message + RESET);
    }

    public static void printInfo(String message) {
        System.out.println(CYAN + "ℹ " + message + RESET);
    }

    public static void printHeader(String title) {
        int width = 60;
        System.out.println(BOLD + CYAN + "═".repeat(width) + RESET);
        int padding = (width - title.length()) / 2;
        System.out.println(BOLD + CYAN + " ".repeat(padding) + title + RESET);
        System.out.println(BOLD + CYAN + "═".repeat(width) + RESET);
    }

    public static void printQuestion(int num, int total, String questionText) {
        System.out.println();
        System.out.println(BOLD + CYAN + "Question " + num + "/" + total + RESET);
        System.out.println(BOLD + questionText + RESET);
        System.out.println();
    }

    public static void printOptions(java.util.Map<String, String> options) {
        options.forEach((key, value) ->
            System.out.println("  " + BOLD + key + ")" + RESET + " " + value));
    }

    public static void printResult(boolean correct, String correctAnswer, String explanation) {
        if (correct) {
            printSuccess("Correct!");
        } else {
            printError("Incorrect. Correct answer: " + correctAnswer);
        }
        if (explanation != null && !explanation.isBlank()) {
            System.out.println(YELLOW + "Explanation: " + explanation + RESET);
        }
    }

    public static void printScore(double percentage, int correct, int total) {
        System.out.println();
        printHeader("QUIZ RESULTS");
        System.out.printf("Score: %d/%d (%.1f%%)%n", correct, total, percentage);
        String grade = com.quizengine.util.QuizUtils.gradeResult(percentage);
        if (percentage >= 75) {
            System.out.println(GREEN + BOLD + "Grade: " + grade + RESET);
        } else {
            System.out.println(RED + BOLD + "Grade: " + grade + RESET);
        }
    }

    public static void printReview(java.util.List<ReviewItem> items) {
        System.out.println();
        printHeader("ANSWER REVIEW");
        for (ReviewItem item : items) {
            System.out.println();
            System.out.println(BOLD + "Q" + item.number() + ": " + item.questionText() + RESET);
            if (item.correct()) {
                System.out.println(GREEN + "  Your answer: " + item.userLetter()
                    + " — " + item.correctAnswer() + RESET);
            } else {
                System.out.println(RED + "  Your answer: " + item.userLetter() + RESET);
                System.out.println(GREEN + "  Correct:     " + item.correctLetter()
                    + " — " + item.correctAnswer() + RESET);
            }
            if (item.explanation() != null && !item.explanation().isBlank()) {
                System.out.println(YELLOW + "  Explanation: " + item.explanation() + RESET);
            }
        }
    }

    public record ReviewItem(
        int number,
        String questionText,
        String userLetter,
        String correctLetter,
        String correctAnswer,
        boolean correct,
        String explanation
    ) {}
}
