package com.quizengine.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class QuizUtils {

    private QuizUtils() {}

    public static String generateSessionId() {
        return UUID.randomUUID().toString();
    }

    public static double calculatePercentage(int correct, int total) {
        if (total == 0) return 0.0;
        return Math.round((double) correct / total * 10000.0) / 100.0;
    }

    public static int calculateElapsedSeconds(LocalDateTime start) {
        return (int) Duration.between(start, LocalDateTime.now()).getSeconds();
    }

    public static String formatDuration(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes, secs);
        } else if (minutes > 0) {
            return String.format("%dm %ds", minutes, secs);
        } else {
            return String.format("%ds", secs);
        }
    }

    public static String gradeResult(double percentage) {
        if (percentage >= 90) return "EXCELLENT";
        if (percentage >= 75) return "PASS";
        if (percentage >= 60) return "BORDERLINE";
        return "FAIL";
    }
}
