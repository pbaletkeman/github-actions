package com.quizengine.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QuizUtilsTest {

    @Test
    void calculatePercentage_correct() {
        assertThat(QuizUtils.calculatePercentage(8, 10)).isEqualTo(80.0);
        assertThat(QuizUtils.calculatePercentage(0, 10)).isEqualTo(0.0);
        assertThat(QuizUtils.calculatePercentage(10, 10)).isEqualTo(100.0);
    }

    @Test
    void calculatePercentage_zeroTotal_returnsZero() {
        assertThat(QuizUtils.calculatePercentage(0, 0)).isEqualTo(0.0);
    }

    @Test
    void generateSessionId_notNull() {
        assertThat(QuizUtils.generateSessionId()).isNotNull().hasSize(36);
    }

    @Test
    void gradeResult_excellent() {
        assertThat(QuizUtils.gradeResult(95.0)).isEqualTo("EXCELLENT");
    }

    @Test
    void gradeResult_pass() {
        assertThat(QuizUtils.gradeResult(80.0)).isEqualTo("PASS");
    }

    @Test
    void gradeResult_fail() {
        assertThat(QuizUtils.gradeResult(55.0)).isEqualTo("FAIL");
    }

    @Test
    void formatDuration_seconds() {
        assertThat(QuizUtils.formatDuration(45)).isEqualTo("45s");
    }

    @Test
    void formatDuration_minutes() {
        assertThat(QuizUtils.formatDuration(90)).isEqualTo("1m 30s");
    }

    @Test
    void formatDuration_hours() {
        assertThat(QuizUtils.formatDuration(3661)).isEqualTo("1h 1m 1s");
    }
}
