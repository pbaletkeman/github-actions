package com.quizengine.dao;

import com.quizengine.model.QuizSession;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SessionDAOTest {
    private Connection connection;
    private SessionDAO dao;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS quiz_sessions (" +
                    "session_id TEXT PRIMARY KEY," +
                    "started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "ended_at TIMESTAMP," +
                    "num_questions INTEGER NOT NULL," +
                    "num_correct INTEGER DEFAULT 0," +
                    "percentage_correct REAL DEFAULT 0.0," +
                    "time_taken_seconds INTEGER," +
                    "UNIQUE(session_id))");
        }
        dao = new SessionDAO(connection);
    }

    @AfterEach
    void tearDown() throws Exception {
        connection.close();
    }

    private QuizSession buildSession(String id) {
        return new QuizSession.Builder()
                .sessionId(id)
                .startedAt("2024-01-01 10:00:00")
                .numQuestions(10)
                .numCorrect(7)
                .percentageCorrect(70.0)
                .timeTakenSeconds(300)
                .build();
    }

    @Test
    void insertAndRetrieve_returnsSession() throws Exception {
        dao.insert(buildSession("session-001"));
        QuizSession s = dao.getById("session-001");
        assertNotNull(s);
        assertEquals("session-001", s.getSessionId());
        assertEquals(10, s.getNumQuestions());
    }

    @Test
    void update_changesScore() throws Exception {
        dao.insert(buildSession("session-002"));
        QuizSession updated = new QuizSession.Builder()
                .sessionId("session-002")
                .endedAt("2024-01-01 10:05:00")
                .numCorrect(9)
                .percentageCorrect(90.0)
                .timeTakenSeconds(600)
                .build();
        dao.update(updated);
        QuizSession retrieved = dao.getById("session-002");
        assertEquals(9, retrieved.getNumCorrect());
        assertEquals(90.0, retrieved.getPercentageCorrect(), 0.01);
    }

    @Test
    void getAll_returnsAllSessions() throws Exception {
        dao.insert(buildSession("session-003"));
        dao.insert(buildSession("session-004"));
        List<QuizSession> all = dao.getAll();
        assertEquals(2, all.size());
    }

    @Test
    void deleteById_removesSession() throws Exception {
        dao.insert(buildSession("session-005"));
        dao.deleteById("session-005");
        assertNull(dao.getById("session-005"));
    }

    @Test
    void deleteAll_removesAllSessions() throws Exception {
        dao.insert(buildSession("session-006"));
        dao.insert(buildSession("session-007"));
        dao.deleteAll();
        assertTrue(dao.getAll().isEmpty());
    }

    @Test
    void deleteBefore_removesOldSessions() throws Exception {
        dao.insert(buildSession("old-session"));
        dao.deleteBefore("2025-01-01 00:00:00");
        assertNull(dao.getById("old-session"));
    }
}
