package com.quizengine.dao;

import com.quizengine.model.QuizResponse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResponseDAO {
    private final Connection connection;

    public ResponseDAO(Connection connection) {
        this.connection = connection;
    }

    public void insert(QuizResponse response) throws SQLException {
        String sql = "INSERT OR IGNORE INTO quiz_responses (session_id, question_id, user_answer, is_correct, time_taken_seconds) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, response.getSessionId());
            ps.setInt(2, response.getQuestionId());
            ps.setString(3, response.getUserAnswer());
            ps.setInt(4, response.isCorrect() ? 1 : 0);
            ps.setInt(5, response.getTimeTakenSeconds());
            ps.executeUpdate();
        }
    }

    public List<QuizResponse> getBySessionId(String sessionId) throws SQLException {
        String sql = "SELECT * FROM quiz_responses WHERE session_id = ?";
        List<QuizResponse> responses = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, sessionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    responses.add(mapRow(rs));
                }
            }
        }
        return responses;
    }

    public void deleteBySessionId(String sessionId) throws SQLException {
        String sql = "DELETE FROM quiz_responses WHERE session_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, sessionId);
            ps.executeUpdate();
        }
    }

    private QuizResponse mapRow(ResultSet rs) throws SQLException {
        return new QuizResponse.Builder()
                .id(rs.getInt("id"))
                .sessionId(rs.getString("session_id"))
                .questionId(rs.getInt("question_id"))
                .userAnswer(rs.getString("user_answer"))
                .isCorrect(rs.getInt("is_correct") == 1)
                .timeTakenSeconds(rs.getInt("time_taken_seconds"))
                .build();
    }
}
