package com.quizengine.service;

import com.quizengine.model.Question;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class QuizServiceTest {
    private Connection connection;
    private QuizService quizService;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        try (Statement stmt = connection.createStatement()) {
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
        }
        quizService = new QuizService(connection);
    }

    @AfterEach
    void tearDown() throws Exception {
        connection.close();
    }

    @Test
    void createQuizEngine_returnsNonNull() {
        QuizEngine engine = quizService.createQuizEngine(5);
        assertNotNull(engine);
    }

    @Test
    void createQuizEngine_withFilters_returnsNonNull() {
        QuizEngine engine = quizService.createQuizEngine(5, "easy", "section1");
        assertNotNull(engine);
    }

    @Test
    void createQuizEngine_loadsQuestionsAfterInsert() throws Exception {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO questions (question_text,option_a,option_b,option_c,option_d,correct_answer) VALUES (?,?,?,?,?,?)")) {
            ps.setString(1, "Test Q?");
            ps.setString(2, "A");
            ps.setString(3, "B");
            ps.setString(4, "C");
            ps.setString(5, "D");
            ps.setString(6, "A");
            ps.executeUpdate();
        }
        QuizEngine engine = quizService.createQuizEngine(1);
        engine.loadQuestions();
        assertEquals(1, engine.getQuestions().size());
    }
}
