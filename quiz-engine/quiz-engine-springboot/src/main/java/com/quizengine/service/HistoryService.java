package com.quizengine.service;

import com.quizengine.entity.QuizSession;
import com.quizengine.exception.QuizEngineException;
import com.quizengine.repository.SessionRepository;
import com.quizengine.util.QuizUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;

@Service
public class HistoryService {

    private final SessionRepository sessionRepository;

    public HistoryService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public List<QuizSession> getAllSessions() {
        return sessionRepository.findAllByOrderByStartedAtDesc();
    }

    public Page<QuizSession> getSessionsPaginated(int page, int size) {
        return sessionRepository.findAllByOrderByStartedAtDesc(PageRequest.of(page, size));
    }

    public QuizSession getSessionDetails(String sessionId) {
        return sessionRepository.findById(sessionId)
            .orElseThrow(() -> new QuizEngineException("Session not found: " + sessionId));
    }

    public SessionSummary getSummary() {
        List<QuizSession> sessions = getAllSessions();
        if (sessions.isEmpty()) {
            return new SessionSummary(0, 0.0, 0.0);
        }

        int totalSessions = sessions.size();
        double avgScore = sessions.stream()
            .filter(s -> s.getPercentageCorrect() != null)
            .mapToDouble(QuizSession::getPercentageCorrect)
            .average()
            .orElse(0.0);
        double bestScore = sessions.stream()
            .filter(s -> s.getPercentageCorrect() != null)
            .mapToDouble(QuizSession::getPercentageCorrect)
            .max()
            .orElse(0.0);

        return new SessionSummary(totalSessions, avgScore, bestScore);
    }

    public void exportToJson(List<QuizSession> sessions, Path path) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(path.toFile()))) {
            writer.println("[");
            for (int i = 0; i < sessions.size(); i++) {
                QuizSession s = sessions.get(i);
                writer.println("  {");
                writer.printf("    \"session_id\": \"%s\",%n", s.getSessionId());
                writer.printf("    \"date\": \"%s\",%n", s.getStartedAt());
                writer.printf("    \"score\": %d,%n", s.getNumCorrect() != null ? s.getNumCorrect() : 0);
                writer.printf("    \"total_questions\": %d,%n", s.getNumQuestions());
                writer.printf("    \"percentage_correct\": %.2f%n", s.getPercentageCorrect() != null ? s.getPercentageCorrect() : 0.0);
                writer.print("  }");
                if (i < sessions.size() - 1) writer.println(",");
                else writer.println();
            }
            writer.println("]");
        }
    }

    public void exportToCsv(List<QuizSession> sessions, Path path) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(path.toFile()))) {
            writer.println("session_id,date,score,total_questions,percentage_correct");
            for (QuizSession s : sessions) {
                writer.printf("%s,%s,%d,%d,%.2f%n",
                    s.getSessionId(),
                    s.getStartedAt(),
                    s.getNumCorrect() != null ? s.getNumCorrect() : 0,
                    s.getNumQuestions(),
                    s.getPercentageCorrect() != null ? s.getPercentageCorrect() : 0.0);
            }
        }
    }

    public record SessionSummary(int totalSessions, double averageScore, double bestScore) {}
}
