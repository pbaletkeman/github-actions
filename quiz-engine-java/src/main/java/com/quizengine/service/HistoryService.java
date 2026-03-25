package com.quizengine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizengine.dao.QuestionDAO;
import com.quizengine.dao.ResponseDAO;
import com.quizengine.dao.SessionDAO;
import com.quizengine.model.QuizResponse;
import com.quizengine.model.QuizSession;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class HistoryService {
    private final SessionDAO sessionDAO;
    private final ResponseDAO responseDAO;
    private final QuestionDAO questionDAO;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public HistoryService(Connection connection) {
        this.sessionDAO = new SessionDAO(connection);
        this.responseDAO = new ResponseDAO(connection);
        this.questionDAO = new QuestionDAO(connection);
    }

    public List<QuizSession> getAllSessions() throws SQLException {
        return sessionDAO.getAll();
    }

    public QuizSession getSessionDetails(String sessionId) throws SQLException {
        return sessionDAO.getById(sessionId);
    }

    public List<QuizResponse> getSessionResponses(String sessionId) throws SQLException {
        return responseDAO.getBySessionId(sessionId);
    }

    public String formatSessionHistory(List<QuizSession> sessions) {
        if (sessions.isEmpty()) return "No sessions found.";
        StringBuilder sb = new StringBuilder();
        String header = String.format("%-38s %-20s %5s %7s %10s%n",
                "Session ID", "Date", "Qs", "Correct", "Score");
        sb.append(header);
        sb.append("-".repeat(85)).append("\n");
        for (QuizSession s : sessions) {
            sb.append(String.format("%-38s %-20s %5d %7d %9.1f%%%n",
                    s.getSessionId(),
                    s.getStartedAt() != null ? s.getStartedAt() : "N/A",
                    s.getNumQuestions(),
                    s.getNumCorrect(),
                    s.getPercentageCorrect()));
        }
        return sb.toString();
    }

    public String exportToJson(List<QuizSession> sessions) throws Exception {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(sessions);
    }

    public String exportToCsv(List<QuizSession> sessions) {
        StringBuilder sb = new StringBuilder();
        sb.append("session_id,started_at,ended_at,num_questions,num_correct,percentage_correct,time_taken_seconds\n");
        for (QuizSession s : sessions) {
            sb.append(String.format("%s,%s,%s,%d,%d,%.2f,%d%n",
                    s.getSessionId(),
                    s.getStartedAt(),
                    s.getEndedAt(),
                    s.getNumQuestions(),
                    s.getNumCorrect(),
                    s.getPercentageCorrect(),
                    s.getTimeTakenSeconds()));
        }
        return sb.toString();
    }
}
