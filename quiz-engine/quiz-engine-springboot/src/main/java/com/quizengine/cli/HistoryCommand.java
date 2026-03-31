package com.quizengine.cli;

import com.quizengine.entity.QuizSession;
import com.quizengine.exception.QuizEngineException;
import com.quizengine.service.HistoryService;
import com.quizengine.util.QuizUtils;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Command(name = "history", description = "View quiz history", mixinStandardHelpOptions = true)
public class HistoryCommand implements Runnable {

    @Option(names = {"-s", "--session-id"}, description = "View specific session")
    private String sessionId;

    @Option(names = {"-r", "--review"}, description = "Show detailed review")
    private boolean review;

    @Option(names = {"--export"}, description = "Export history: json or csv")
    private String export;

    private final HistoryService historyService;

    public HistoryCommand(HistoryService historyService) {
        this.historyService = historyService;
    }

    @Override
    public void run() {
        if (sessionId != null) {
            showSessionDetail(sessionId);
        } else if (export != null) {
            exportHistory(export);
        } else {
            showAllHistory();
        }
    }

    private void showAllHistory() {
        ConsoleFormatter.printHeader("QUIZ HISTORY");
        List<QuizSession> sessions = historyService.getAllSessions();

        if (sessions.isEmpty()) {
            ConsoleFormatter.printInfo("No quiz history found.");
            return;
        }

        HistoryService.SessionSummary summary = historyService.getSummary();
        System.out.printf("Total Sessions: %d | Avg Score: %.1f%% | Best: %.1f%%%n%n",
            summary.totalSessions(), summary.averageScore(), summary.bestScore());

        System.out.printf("%-36s  %-20s  %5s  %5s  %6s%n",
            "Session ID", "Date", "Q", "Cor", "Score");
        System.out.println("-".repeat(80));

        for (QuizSession s : sessions) {
            System.out.printf("%-36s  %-20s  %5d  %5d  %5.1f%%%n",
                s.getSessionId(),
                s.getStartedAt() != null ? s.getStartedAt().toString() : "N/A",
                s.getNumQuestions(),
                s.getNumCorrect() != null ? s.getNumCorrect() : 0,
                s.getPercentageCorrect() != null ? s.getPercentageCorrect() : 0.0);
        }
    }

    private void exportHistory(String format) {
        List<QuizSession> sessions = historyService.getAllSessions();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        try {
            switch (format.toLowerCase()) {
                case "json" -> {
                    Path path = Paths.get("quiz-history-" + timestamp + ".json");
                    historyService.exportToJson(sessions, path);
                    ConsoleFormatter.printSuccess("Exported " + sessions.size() + " sessions to " + path);
                }
                case "csv" -> {
                    Path path = Paths.get("quiz-history-" + timestamp + ".csv");
                    historyService.exportToCsv(sessions, path);
                    ConsoleFormatter.printSuccess("Exported " + sessions.size() + " sessions to " + path);
                }
                default -> ConsoleFormatter.printError("Unknown export format '" + format + "': use 'json' or 'csv'.");
            }
        } catch (IOException e) {
            ConsoleFormatter.printError("Export failed: " + e.getMessage());
        }
    }

    private void showSessionDetail(String sid) {
        try {
            QuizSession session = historyService.getSessionDetails(sid);
            ConsoleFormatter.printHeader("SESSION DETAIL");
            System.out.println("Session ID: " + session.getSessionId());
            System.out.println("Date: " + session.getStartedAt());
            System.out.printf("Score: %d/%d (%.1f%%)%n",
                session.getNumCorrect(), session.getNumQuestions(),
                session.getPercentageCorrect());
            if (session.getTimeTakenSeconds() != null) {
                System.out.println("Time: " + QuizUtils.formatDuration(session.getTimeTakenSeconds()));
            }
        } catch (QuizEngineException e) {
            ConsoleFormatter.printError(e.getMessage());
        }
    }
}
