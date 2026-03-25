package com.quizengine.repository;

import com.quizengine.entity.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        questionRepository.deleteAll();
    }

    private Question buildQuestion(String text) {
        return Question.builder()
            .questionText(text)
            .optionA("Option A")
            .optionB("Option B")
            .optionC("Option C")
            .optionD("Option D")
            .correctAnswer("A")
            .usageCycle(1)
            .timesUsed(0)
            .build();
    }

    @Test
    void saveAndFind_returnsQuestion() {
        Question q = buildQuestion("What is CI?");
        questionRepository.save(q);
        List<Question> results = questionRepository.findByUsageCycle(1);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getQuestionText()).isEqualTo("What is CI?");
    }

    @Test
    void findCurrentCycle_returnsMinCycle() {
        questionRepository.save(buildQuestion("Q1"));
        Question q2 = buildQuestion("Q2");
        q2.setUsageCycle(2);
        questionRepository.save(q2);
        assertThat(questionRepository.findCurrentCycle()).isEqualTo(1);
    }

    @Test
    void findCurrentCycle_noQuestions_returnsNull() {
        assertThat(questionRepository.findCurrentCycle()).isNull();
    }

    @Test
    void markQuestionUsed_incrementsTimesUsed() {
        Question q = questionRepository.save(buildQuestion("Q1"));
        questionRepository.markQuestionUsed(q.getId());
        Question updated = questionRepository.findById(q.getId()).orElseThrow();
        assertThat(updated.getTimesUsed()).isEqualTo(1);
    }

    @Test
    void countUnusedInCurrentCycle_returnsCorrectCount() {
        questionRepository.save(buildQuestion("Q1"));
        questionRepository.save(buildQuestion("Q2"));
        assertThat(questionRepository.countUnusedInCurrentCycle()).isEqualTo(2);
    }
}
