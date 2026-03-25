package com.quizengine.service;

import com.quizengine.dao.QuestionDAO;
import com.quizengine.dao.ResponseDAO;
import com.quizengine.dao.SessionDAO;
import com.quizengine.model.Question;
import com.quizengine.model.QuizSession;
import com.quizengine.util.ShuffleResult;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuizEngineTest {
    private Connection connection;
    private QuestionDAO questionDAO;
    private SessionDAO sessionDAO;
    private ResponseDAO responseDAO;

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
        questionDAO = new QuestionDAO(connection);
        sessionDAO = new SessionDAO(connection);
        responseDAO = new ResponseDAO(connection);

        for (int i = 1; i <= 5; i++) {
            questionDAO.insert(new Question.Builder()
                    .questionText("Question " + i + "?")
                    .optionA("Option A")
                    .optionB("Option B")
                    .optionC("Option C")
                    .optionD("Option D")
                    .correctAnswer("A")
                    .build());
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        connection.close();
    }

    @Test
    void loadQuestions_loadsExpectedCount() throws Exception {
        QuizEngine engine = new QuizEngine(questionDAO, sessionDAO, responseDAO, 3);
        engine.loadQuestions();
        assertEquals(3, engine.getQuestions().size());
    }

    @Test
    void submitCorrectAnswer_incrementsScore() throws Exception {
        QuizEngine engine = new QuizEngine(questionDAO, sessionDAO, responseDAO, 1);
        engine.loadQuestions();
        String correctLetter = engine.getShuffleResult(0).getCorrectShuffledLetter();
        boolean result = engine.submitAnswer(0, correctLetter, 10);
        assertTrue(result);
        assertEquals(1, engine.getNumCorrect());
    }

    @Test
    void submitWrongAnswer_doesNotScore() throws Exception {
        QuizEngine engine = new QuizEngine(questionDAO, sessionDAO, responseDAO, 1);
        engine.loadQuestions();
        ShuffleResult shuffle = engine.getShuffleResult(0);
        String wrongAnswer = shuffle.getCorrectShuffledLetter().equals("A") ? "B" : "A";
        boolean result = engine.submitAnswer(0, wrongAnswer, 10);
        assertFalse(result);
        assertEquals(0, engine.getNumCorrect());
    }

    @Test
    void finalize_persistsSessionToDatabase() throws Exception {
        QuizEngine engine = new QuizEngine(questionDAO, sessionDAO, responseDAO, 2);
        engine.loadQuestions();
        engine.complete();
        QuizSession session = sessionDAO.getById(engine.getSessionId());
        assertNotNull(session);
    }

    @Test
    void finalize_calculatesCorrectPercentage() throws Exception {
        QuizEngine engine = new QuizEngine(questionDAO, sessionDAO, responseDAO, 2);
        engine.loadQuestions();
        String correctLetter = engine.getShuffleResult(0).getCorrectShuffledLetter();
        engine.submitAnswer(0, correctLetter, 5);
        QuizSession session = engine.complete();
        assertEquals(50.0, session.getPercentageCorrect(), 0.01);
    }

    @Test
    void getShuffleResult_returnsValidMapping() throws Exception {
        QuizEngine engine = new QuizEngine(questionDAO, sessionDAO, responseDAO, 1);
        engine.loadQuestions();
        ShuffleResult shuffle = engine.getShuffleResult(0);
        assertNotNull(shuffle);
        assertEquals(4, shuffle.getShuffledOptions().size());
        assertTrue(shuffle.getCorrectShuffledIndex() >= 0 && shuffle.getCorrectShuffledIndex() < 4);
    }

    @Test
    void submitAnswer_withInvalidIndex_returnsFalse() throws Exception {
        QuizEngine engine = new QuizEngine(questionDAO, sessionDAO, responseDAO, 1);
        engine.loadQuestions();
        assertFalse(engine.submitAnswer(-1, "A", 5));
        assertFalse(engine.submitAnswer(99, "A", 5));
    }

    @Test
    void loadQuestions_withDifficultyAndSection_returnsAvailable() throws Exception {
        questionDAO.insert(new Question.Builder()
                .questionText("Filtered Q?")
                .optionA("Option A")
                .optionB("Option B")
                .optionC("Option C")
                .optionD("Option D")
                .correctAnswer("A")
                .difficulty("easy")
                .section("cicd")
                .build());
        QuizEngine engine = new QuizEngine(questionDAO, sessionDAO, responseDAO, 1, "easy", "cicd");
        engine.loadQuestions();
        assertEquals(1, engine.getQuestions().size());
    }

    @Test
    void loadQuestions_withFiveOptions_shufflesCorrectly() throws Exception {
        questionDAO.insert(new Question.Builder()
                .questionText("Five option Q?")
                .optionA("Option A")
                .optionB("Option B")
                .optionC("Option C")
                .optionD("Option D")
                .optionE("Option E")
                .correctAnswer("E")
                .difficulty("five")
                .section("five")
                .build());
        QuizEngine engine = new QuizEngine(questionDAO, sessionDAO, responseDAO, 1, "five", "five");
        engine.loadQuestions();
        assertEquals(1, engine.getQuestions().size());
        assertEquals(5, engine.getShuffleResult(0).getShuffledOptions().size());
    }
}
