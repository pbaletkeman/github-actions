package com.quizengine.dao;

import com.quizengine.model.Question;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionDAOTest {
    private Connection connection;
    private QuestionDAO dao;

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
        }
        dao = new QuestionDAO(connection);
    }

    @AfterEach
    void tearDown() throws Exception {
        connection.close();
    }

    private Question buildQuestion(String text, String correctAnswer) {
        return new Question.Builder()
                .questionText(text)
                .optionA("Option A text")
                .optionB("Option B text")
                .optionC("Option C text")
                .optionD("Option D text")
                .correctAnswer(correctAnswer)
                .build();
    }

    @Test
    void insertAndRetrieve_returnsInsertedQuestion() throws Exception {
        dao.insert(buildQuestion("What is GitHub Actions?", "A"));
        List<Question> all = dao.getAll();
        assertEquals(1, all.size());
        assertEquals("What is GitHub Actions?", all.get(0).getQuestionText());
    }

    @Test
    void insert_skipsExactDuplicate() throws Exception {
        Question q = buildQuestion("What is CI/CD?", "B");
        dao.insert(q);
        dao.insert(q);
        assertEquals(1, dao.count());
    }

    @Test
    void getRandomQuestions_omitsCorrectAnswer() throws Exception {
        dao.insert(buildQuestion("Test question?", "C"));
        List<Question> questions = dao.getRandomQuestions(1);
        assertEquals(1, questions.size());
        assertNull(questions.get(0).getCorrectAnswer());
    }

    @Test
    void advanceCycleIfExhausted_incrementsCycle() throws Exception {
        dao.insert(buildQuestion("Cycle test?", "A"));
        List<Question> questions = dao.getAll();
        dao.markUsed(questions.get(0).getId());
        dao.advanceCycleIfExhausted();
        List<Question> updated = dao.getAll();
        assertEquals(2, updated.get(0).getUsageCycle());
    }

    @Test
    void getCurrentCycle_returnsOne_initially() throws Exception {
        dao.insert(buildQuestion("Cycle question?", "D"));
        assertEquals(1, dao.getCurrentCycle());
    }

    @Test
    void count_returnsCorrectCount() throws Exception {
        dao.insert(buildQuestion("Q1?", "A"));
        dao.insert(buildQuestion("Q2?", "B"));
        assertEquals(2, dao.count());
    }

    @Test
    void getRandomQuestions_withDifficultyFilter_returnsMatchingQuestions() throws Exception {
        Question easy = new Question.Builder()
                .questionText("Easy question?")
                .optionA("A")
                .optionB("B")
                .optionC("C")
                .optionD("D")
                .correctAnswer("A")
                .difficulty("easy")
                .build();
        Question hard = new Question.Builder()
                .questionText("Hard question?")
                .optionA("A")
                .optionB("B")
                .optionC("C")
                .optionD("D")
                .correctAnswer("B")
                .difficulty("hard")
                .build();
        dao.insert(easy);
        dao.insert(hard);
        List<Question> results = dao.getRandomQuestions(10, "easy", null);
        assertEquals(1, results.size());
    }

    @Test
    void getRandomQuestions_withSectionFilter_returnsMatchingQuestions() throws Exception {
        Question q1 = new Question.Builder()
                .questionText("Section1 question?")
                .optionA("A")
                .optionB("B")
                .optionC("C")
                .optionD("D")
                .correctAnswer("A")
                .section("workflow")
                .build();
        dao.insert(q1);
        List<Question> results = dao.getRandomQuestions(10, null, "workflow");
        assertEquals(1, results.size());
    }

    @Test
    void markUsed_incrementsTimesUsed() throws Exception {
        dao.insert(buildQuestion("Mark used?", "A"));
        List<Question> all = dao.getAll();
        int id = all.get(0).getId();
        dao.markUsed(id);
        Question q = dao.getById(id);
        assertEquals(1, q.getTimesUsed());
    }

    @Test
    void getCurrentCycle_returnsOneWhenNoQuestions() throws Exception {
        assertEquals(1, dao.getCurrentCycle());
    }

}
