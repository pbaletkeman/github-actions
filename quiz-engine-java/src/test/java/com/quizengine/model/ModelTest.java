package com.quizengine.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    @Test
    void question_settersAndGetters_workCorrectly() {
        Question q = new Question();
        q.setId(1);
        q.setQuestionText("What is CI?");
        q.setOptionA("Answer A");
        q.setOptionB("Answer B");
        q.setOptionC("Answer C");
        q.setOptionD("Answer D");
        q.setOptionE("Answer E");
        q.setCorrectAnswer("A");
        q.setExplanation("CI stands for Continuous Integration");
        q.setSection("section1");
        q.setDifficulty("easy");
        q.setSourceFile("test.md");
        q.setUsageCycle(2);
        q.setTimesUsed(3);
        q.setLastUsedAt("2024-01-01 10:00:00");

        assertEquals(1, q.getId());
        assertEquals("What is CI?", q.getQuestionText());
        assertEquals("Answer A", q.getOptionA());
        assertEquals("Answer B", q.getOptionB());
        assertEquals("Answer C", q.getOptionC());
        assertEquals("Answer D", q.getOptionD());
        assertEquals("Answer E", q.getOptionE());
        assertEquals("A", q.getCorrectAnswer());
        assertEquals("CI stands for Continuous Integration", q.getExplanation());
        assertEquals("section1", q.getSection());
        assertEquals("easy", q.getDifficulty());
        assertEquals("test.md", q.getSourceFile());
        assertEquals(2, q.getUsageCycle());
        assertEquals(3, q.getTimesUsed());
        assertEquals("2024-01-01 10:00:00", q.getLastUsedAt());
    }

    @Test
    void question_toString_containsId() {
        Question q = new Question.Builder().id(42).questionText("Test?").section("s1").build();
        String str = q.toString();
        assertTrue(str.contains("42"));
        assertTrue(str.contains("Test?"));
    }

    @Test
    void quizSession_settersAndGetters_workCorrectly() {
        QuizSession s = new QuizSession();
        s.setSessionId("sess-1");
        s.setStartedAt("2024-01-01 10:00:00");
        s.setEndedAt("2024-01-01 10:05:00");
        s.setNumQuestions(10);
        s.setNumCorrect(8);
        s.setPercentageCorrect(80.0);
        s.setTimeTakenSeconds(300);

        assertEquals("sess-1", s.getSessionId());
        assertEquals("2024-01-01 10:00:00", s.getStartedAt());
        assertEquals("2024-01-01 10:05:00", s.getEndedAt());
        assertEquals(10, s.getNumQuestions());
        assertEquals(8, s.getNumCorrect());
        assertEquals(80.0, s.getPercentageCorrect(), 0.001);
        assertEquals(300, s.getTimeTakenSeconds());
    }

    @Test
    void quizResponse_settersAndGetters_workCorrectly() {
        QuizResponse r = new QuizResponse();
        r.setId(5);
        r.setSessionId("sess-2");
        r.setQuestionId(10);
        r.setUserAnswer("B");
        r.setCorrect(true);
        r.setTimeTakenSeconds(20);

        assertEquals(5, r.getId());
        assertEquals("sess-2", r.getSessionId());
        assertEquals(10, r.getQuestionId());
        assertEquals("B", r.getUserAnswer());
        assertTrue(r.isCorrect());
        assertEquals(20, r.getTimeTakenSeconds());
    }

    @Test
    void quizResponse_incorrectAnswer_setCorrectly() {
        QuizResponse r = new QuizResponse();
        r.setCorrect(false);
        assertFalse(r.isCorrect());
    }
}
