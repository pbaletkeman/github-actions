package com.quizengine.dao;

import com.quizengine.model.QuizResponse;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResponseDAOTest {
    private Connection connection;
    private ResponseDAO dao;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS quiz_responses (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "session_id TEXT NOT NULL," +
                    "question_id INTEGER NOT NULL," +
                    "user_answer TEXT NOT NULL," +
                    "is_correct INTEGER DEFAULT 0," +
                    "time_taken_seconds INTEGER," +
                    "UNIQUE(session_id, question_id))");
        }
        dao = new ResponseDAO(connection);
    }

    @AfterEach
    void tearDown() throws Exception {
        connection.close();
    }

    private QuizResponse buildResponse(String sessionId, int questionId, boolean correct) {
        return new QuizResponse.Builder()
                .sessionId(sessionId)
                .questionId(questionId)
                .userAnswer("A")
                .isCorrect(correct)
                .timeTakenSeconds(15)
                .build();
    }

    @Test
    void insertAndRetrieve_returnsResponse() throws Exception {
        dao.insert(buildResponse("s1", 1, true));
        List<QuizResponse> responses = dao.getBySessionId("s1");
        assertEquals(1, responses.size());
        assertEquals("s1", responses.get(0).getSessionId());
        assertTrue(responses.get(0).isCorrect());
    }

    @Test
    void insert_skipsDuplicate() throws Exception {
        QuizResponse r = buildResponse("s2", 1, false);
        dao.insert(r);
        dao.insert(r);
        assertEquals(1, dao.getBySessionId("s2").size());
    }

    @Test
    void getBySessionId_returnsMultipleResponses() throws Exception {
        dao.insert(buildResponse("s3", 1, true));
        dao.insert(buildResponse("s3", 2, false));
        assertEquals(2, dao.getBySessionId("s3").size());
    }

    @Test
    void deleteBySessionId_removesResponses() throws Exception {
        dao.insert(buildResponse("s4", 1, true));
        dao.deleteBySessionId("s4");
        assertTrue(dao.getBySessionId("s4").isEmpty());
    }

    @Test
    void getBySessionId_wrongSession_returnsEmpty() throws Exception {
        dao.insert(buildResponse("s5", 1, true));
        assertTrue(dao.getBySessionId("other").isEmpty());
    }

    @Test
    void insert_incorrectResponse_storedCorrectly() throws Exception {
        dao.insert(buildResponse("s6", 1, false));
        List<QuizResponse> responses = dao.getBySessionId("s6");
        assertFalse(responses.get(0).isCorrect());
    }
}
