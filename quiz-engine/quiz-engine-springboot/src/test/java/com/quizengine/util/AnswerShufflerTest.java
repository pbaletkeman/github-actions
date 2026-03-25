package com.quizengine.util;

import com.quizengine.entity.Question;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AnswerShufflerTest {

    private Question buildQuestion() {
        return Question.builder()
            .questionText("What is CI?")
            .optionA("Continuous Integration")
            .optionB("Code Import")
            .optionC("Compile")
            .optionD("Configure")
            .correctAnswer("A")
            .build();
    }

    @Test
    void shuffleAnswers_returnsAllOptions() {
        Question q = buildQuestion();
        Map<String, String> shuffled = AnswerShuffler.shuffleAnswers(q);
        assertThat(shuffled).hasSize(4);
        assertThat(shuffled.values()).containsExactlyInAnyOrder(
            "Continuous Integration", "Code Import", "Compile", "Configure");
    }

    @Test
    void shuffleAnswers_withOptionE_returnsFiveOptions() {
        Question q = buildQuestion();
        q.setOptionE("Continuous Improvement");
        Map<String, String> shuffled = AnswerShuffler.shuffleAnswers(q);
        assertThat(shuffled).hasSize(5);
    }

    @Test
    void findCorrectLetter_returnsCorrectAfterShuffle() {
        Question q = buildQuestion();
        Map<String, String> shuffled = AnswerShuffler.shuffleAnswers(q);
        String correctLetter = AnswerShuffler.findCorrectLetter(shuffled, q);
        assertThat(shuffled.get(correctLetter)).isEqualTo("Continuous Integration");
    }

    @Test
    void shuffleAnswers_allLettersUsed() {
        Question q = buildQuestion();
        Map<String, String> shuffled = AnswerShuffler.shuffleAnswers(q);
        assertThat(shuffled.keySet()).containsExactlyInAnyOrder("A", "B", "C", "D");
    }
}
