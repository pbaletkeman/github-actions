package com.quizengine.service;

import com.quizengine.entity.QuizSession;
import com.quizengine.exception.QuizEngineException;
import com.quizengine.repository.SessionRepository;
import com.quizengine.util.QuizUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

    public record SessionSummary(int totalSessions, double averageScore, double bestScore) {}
}
