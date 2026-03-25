package com.quizengine.dao;

import com.quizengine.model.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {
    private final Connection connection;

    public QuestionDAO(Connection connection) {
        this.connection = connection;
    }

    public void insert(Question q) throws SQLException {
        String sql = "INSERT OR IGNORE INTO questions (question_text, option_a, option_b, option_c, option_d, option_e, " +
                "correct_answer, explanation, section, difficulty, source_file) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, q.getQuestionText());
            ps.setString(2, q.getOptionA());
            ps.setString(3, q.getOptionB());
            ps.setString(4, q.getOptionC());
            ps.setString(5, q.getOptionD());
            ps.setString(6, q.getOptionE());
            ps.setString(7, q.getCorrectAnswer());
            ps.setString(8, q.getExplanation());
            ps.setString(9, q.getSection());
            ps.setString(10, q.getDifficulty());
            ps.setString(11, q.getSourceFile());
            ps.executeUpdate();
        }
    }

    public List<Question> getAll() throws SQLException {
        String sql = "SELECT * FROM questions ORDER BY id";
        List<Question> questions = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                questions.add(mapRow(rs, true));
            }
        }
        return questions;
    }

    public List<Question> getRandomQuestions(int n) throws SQLException {
        int cycle = getCurrentCycle();
        String sql = "SELECT * FROM questions WHERE usage_cycle = ? AND times_used = 0 ORDER BY RANDOM() LIMIT ?";
        List<Question> questions = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cycle);
            ps.setInt(2, n);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    questions.add(mapRow(rs, false));
                }
            }
        }
        return questions;
    }

    public List<Question> getRandomQuestions(int n, String difficulty, String section) throws SQLException {
        int cycle = getCurrentCycle();
        StringBuilder sql = new StringBuilder("SELECT * FROM questions WHERE usage_cycle = ? AND times_used = 0");
        List<Object> params = new ArrayList<>();
        params.add(cycle);
        if (difficulty != null && !difficulty.isEmpty()) {
            sql.append(" AND difficulty = ?");
            params.add(difficulty);
        }
        if (section != null && !section.isEmpty()) {
            sql.append(" AND section = ?");
            params.add(section);
        }
        sql.append(" ORDER BY RANDOM() LIMIT ?");
        params.add(n);

        List<Question> questions = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    questions.add(mapRow(rs, false));
                }
            }
        }
        return questions;
    }

    public int getCurrentCycle() throws SQLException {
        String sql = "SELECT MIN(usage_cycle) FROM questions WHERE times_used = 0";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int val = rs.getInt(1);
                return rs.wasNull() ? 1 : val;
            }
        }
        return 1;
    }

    public void markUsed(int questionId) throws SQLException {
        String sql = "UPDATE questions SET times_used = times_used + 1, last_used_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, questionId);
            ps.executeUpdate();
        }
    }

    public void advanceCycleIfExhausted() throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM questions WHERE times_used = 0";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {
            if (rs.next() && rs.getInt(1) == 0) {
                String updateSql = "UPDATE questions SET usage_cycle = usage_cycle + 1, times_used = 0";
                try (Statement updateStmt = connection.createStatement()) {
                    updateStmt.executeUpdate(updateSql);
                }
            }
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM questions";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public Question getById(int id) throws SQLException {
        String sql = "SELECT * FROM questions WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs, true);
                }
            }
        }
        return null;
    }

    private Question mapRow(ResultSet rs, boolean includeAnswer) throws SQLException {
        Question.Builder builder = new Question.Builder()
                .id(rs.getInt("id"))
                .questionText(rs.getString("question_text"))
                .optionA(rs.getString("option_a"))
                .optionB(rs.getString("option_b"))
                .optionC(rs.getString("option_c"))
                .optionD(rs.getString("option_d"))
                .optionE(rs.getString("option_e"))
                .explanation(rs.getString("explanation"))
                .section(rs.getString("section"))
                .difficulty(rs.getString("difficulty"))
                .sourceFile(rs.getString("source_file"))
                .usageCycle(rs.getInt("usage_cycle"))
                .timesUsed(rs.getInt("times_used"))
                .lastUsedAt(rs.getString("last_used_at"));
        if (includeAnswer) {
            builder.correctAnswer(rs.getString("correct_answer"));
        }
        return builder.build();
    }
}
