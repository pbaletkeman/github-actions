package com.quizengine.service;

import com.quizengine.dao.SessionDAO;
import com.quizengine.model.QuizSession;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryServiceTest {
    private Connection connection;
    private HistoryService historyService;

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
            stmt.execute("CREATE TABLE IF NOT EXISTS quiz_responses (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "session_id TEXT NOT NULL," +
                    "question_id INTEGER NOT NULL," +
                    "user_answer TEXT NOT NULL," +
                    "is_correct INTEGER DEFAULT 0," +
                    "time_taken_seconds INTEGER," +
                    "UNIQUE(session_id, question_id))");
            stmt.execute("CREATE TABLE IF NOT EXISTS questions (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "question_text TEXT NOT NULL," +
                    "option_a TEXT NOT NULL," +
                    "option_b TEXT NOT NULL," +
                    "option_c TEXT NOT NULL," +
                    "option_d TEXT NOT NULL," +
                    "option_e TEXT," +
                    "correct_answer TEXT NOT NULL," +
                    "explanation TEXT," +
                    "section TEXT," +
                    "difficulty TEXT," +
                    "source_file TEXT," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "usage_cycle INTEGER DEFAULT 1," +
                    "times_used INTEGER DEFAULT 0," +
                    "last_used_at TIMESTAMP," +
                    "UNIQUE(question_text, correct_answer))");
        }
        historyService = new HistoryService(connection);
    }

    @AfterEach
    void tearDown() throws Exception {
        connection.close();
    }

    @Test
    void getAllSessions_returnsEmptyListInitially() throws Exception {
        List<QuizSession> sessions = historyService.getAllSessions();
        assertTrue(sessions.isEmpty());
    }

    @Test
    void getAllSessions_returnsInsertedSession() throws Exception {
        SessionDAO sessionDAO = new SessionDAO(connection);
        sessionDAO.insert(new QuizSession.Builder()
                .sessionId("test-session")
                .startedAt("2024-01-01 10:00:00")
                .numQuestions(5)
                .numCorrect(4)
                .percentageCorrect(80.0)
                .timeTakenSeconds(120)
                .build());
        List<QuizSession> sessions = historyService.getAllSessions();
        assertEquals(1, sessions.size());
    }

    @Test
    void getSessionDetails_returnsCorrectSession() throws Exception {
        SessionDAO sessionDAO = new SessionDAO(connection);
        sessionDAO.insert(new QuizSession.Builder()
                .sessionId("detail-session")
                .startedAt("2024-01-01 10:00:00")
                .numQuestions(10)
                .numCorrect(8)
                .percentageCorrect(80.0)
                .timeTakenSeconds(300)
                .build());
        QuizSession s = historyService.getSessionDetails("detail-session");
        assertNotNull(s);
        assertEquals("detail-session", s.getSessionId());
        assertEquals(8, s.getNumCorrect());
    }

    @Test
    void formatSessionHistory_returnsNonEmptyString() throws Exception {
        SessionDAO sessionDAO = new SessionDAO(connection);
        sessionDAO.insert(new QuizSession.Builder()
                .sessionId("format-session")
                .startedAt("2024-01-01 10:00:00")
                .numQuestions(5)
                .numCorrect(3)
                .percentageCorrect(60.0)
                .timeTakenSeconds(200)
                .build());
        List<QuizSession> sessions = historyService.getAllSessions();
        String formatted = historyService.formatSessionHistory(sessions);
        assertNotNull(formatted);
        assertFalse(formatted.isEmpty());
        assertTrue(formatted.contains("format-session"));
    }

    @Test
    void exportToJson_returnsValidJson() throws Exception {
        SessionDAO sessionDAO = new SessionDAO(connection);
        sessionDAO.insert(new QuizSession.Builder()
                .sessionId("json-session")
                .startedAt("2024-01-01 10:00:00")
                .numQuestions(5)
                .numCorrect(5)
                .percentageCorrect(100.0)
                .timeTakenSeconds(150)
                .build());
        List<QuizSession> sessions = historyService.getAllSessions();
        String json = historyService.exportToJson(sessions);
        assertNotNull(json);
        assertTrue(json.startsWith("["));
        assertTrue(json.contains("json-session"));
    }

    @Test
    void exportToCsv_returnsValidCsv() throws Exception {
        SessionDAO sessionDAO = new SessionDAO(connection);
        sessionDAO.insert(new QuizSession.Builder()
                .sessionId("csv-session")
                .startedAt("2024-01-01 10:00:00")
                .endedAt("2024-01-01 10:05:00")
                .numQuestions(5)
                .numCorrect(3)
                .percentageCorrect(60.0)
                .timeTakenSeconds(300)
                .build());
        List<QuizSession> sessions = historyService.getAllSessions();
        String csv = historyService.exportToCsv(sessions);
        assertTrue(csv.contains("session_id"));
        assertTrue(csv.contains("csv-session"));
    }

    @Test
    void formatSessionHistory_emptyList_returnsNoSessionsMessage() {
        String result = historyService.formatSessionHistory(List.of());
        assertTrue(result.contains("No sessions found"));
    }

    @Test
    void getSessionResponses_returnsEmptyForNewSession() throws Exception {
        List<com.quizengine.model.QuizResponse> responses = historyService.getSessionResponses("nonexistent");
        assertTrue(responses.isEmpty());
    }
}
