package com.quizengine.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuizUtilsTest {

    @Test
    void calculateScore_returnsCorrectPercentage() {
        assertEquals(80.0, QuizUtils.calculateScore(8, 10), 0.001);
    }

    @Test
    void calculateScore_withZeroTotal_returnsZero() {
        assertEquals(0.0, QuizUtils.calculateScore(0, 0), 0.001);
    }

    @Test
    void calculateScore_perfectScore() {
        assertEquals(100.0, QuizUtils.calculateScore(5, 5), 0.001);
    }

    @Test
    void calculateScore_zeroCorrect() {
        assertEquals(0.0, QuizUtils.calculateScore(0, 10), 0.001);
    }

    @Test
    void formatTime_formatsCorrectly() {
        assertEquals("01:30", QuizUtils.formatTime(90));
    }

    @Test
    void formatTime_zeroSeconds() {
        assertEquals("00:00", QuizUtils.formatTime(0));
    }

    @Test
    void formatTime_largeValue() {
        assertEquals("10:00", QuizUtils.formatTime(600));
    }

    @Test
    void generateSessionId_returnsNonNull() {
        assertNotNull(QuizUtils.generateSessionId());
    }

    @Test
    void generateSessionId_returnsUniqueIds() {
        String id1 = QuizUtils.generateSessionId();
        String id2 = QuizUtils.generateSessionId();
        assertNotEquals(id1, id2);
    }

    @Test
    void now_returnsFormattedTimestamp() {
        String ts = QuizUtils.now();
        assertNotNull(ts);
        assertTrue(ts.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }
}
