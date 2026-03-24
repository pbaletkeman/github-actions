package com.quizengine.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class QuizUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static double calculateScore(int correct, int total) {
        if (total == 0) return 0.0;
        return (double) correct / total * 100.0;
    }

    public static String formatTime(long seconds) {
        long minutes = seconds / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    public static String generateSessionId() {
        return UUID.randomUUID().toString();
    }

    public static String now() {
        return LocalDateTime.now().format(FORMATTER);
    }
}
