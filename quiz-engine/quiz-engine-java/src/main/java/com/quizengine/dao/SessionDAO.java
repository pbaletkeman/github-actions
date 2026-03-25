package com.quizengine.dao;

import com.quizengine.model.QuizSession;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessionDAO {
    private final Connection connection;

    public SessionDAO(Connection connection) {
        this.connection = connection;
    }

    public void insert(QuizSession session) throws SQLException {
        String sql = "INSERT OR IGNORE INTO quiz_sessions (session_id, started_at, ended_at, num_questions, num_correct, percentage_correct, time_taken_seconds) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, session.getSessionId());
            ps.setString(2, session.getStartedAt());
            ps.setString(3, session.getEndedAt());
            ps.setInt(4, session.getNumQuestions());
            ps.setInt(5, session.getNumCorrect());
            ps.setDouble(6, session.getPercentageCorrect());
            ps.setInt(7, session.getTimeTakenSeconds());
            ps.executeUpdate();
        }
    }

    public void update(QuizSession session) throws SQLException {
        String sql = "UPDATE quiz_sessions SET ended_at=?, num_correct=?, percentage_correct=?, time_taken_seconds=? WHERE session_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, session.getEndedAt());
            ps.setInt(2, session.getNumCorrect());
            ps.setDouble(3, session.getPercentageCorrect());
            ps.setInt(4, session.getTimeTakenSeconds());
            ps.setString(5, session.getSessionId());
            ps.executeUpdate();
        }
    }

    public QuizSession getById(String sessionId) throws SQLException {
        String sql = "SELECT * FROM quiz_sessions WHERE session_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, sessionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public List<QuizSession> getAll() throws SQLException {
        String sql = "SELECT * FROM quiz_sessions ORDER BY started_at DESC";
        List<QuizSession> sessions = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                sessions.add(mapRow(rs));
            }
        }
        return sessions;
    }

    public void deleteById(String sessionId) throws SQLException {
        String sql = "DELETE FROM quiz_sessions WHERE session_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, sessionId);
            ps.executeUpdate();
        }
    }

    public void deleteAll() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM quiz_sessions");
        }
    }

    public void deleteBefore(String dateStr) throws SQLException {
        String sql = "DELETE FROM quiz_sessions WHERE started_at < ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, dateStr);
            ps.executeUpdate();
        }
    }

    private QuizSession mapRow(ResultSet rs) throws SQLException {
        return new QuizSession.Builder()
                .sessionId(rs.getString("session_id"))
                .startedAt(rs.getString("started_at"))
                .endedAt(rs.getString("ended_at"))
                .numQuestions(rs.getInt("num_questions"))
                .numCorrect(rs.getInt("num_correct"))
                .percentageCorrect(rs.getDouble("percentage_correct"))
                .timeTakenSeconds(rs.getInt("time_taken_seconds"))
                .build();
    }
}
