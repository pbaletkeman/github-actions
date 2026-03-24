package com.quizengine.service;

import com.quizengine.entity.Question;
import com.quizengine.entity.QuizSession;
import com.quizengine.exception.QuizEngineException;
import com.quizengine.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class QuizServiceIntegrationTest {

    @Autowired
    private QuizService quizService;

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
    void startQuiz_noQuestions_throwsException() {
        assertThatThrownBy(() -> quizService.startQuiz(5))
            .isInstanceOf(QuizEngineException.class);
    }

    @Test
    void startQuiz_withQuestions_createsSession() {
        questionRepository.save(buildQuestion("Q1"));
        questionRepository.save(buildQuestion("Q2"));

        QuizSession session = quizService.startQuiz(2);
        assertThat(session).isNotNull();
        assertThat(session.getSessionId()).isNotNull();
        assertThat(quizService.isSessionActive(session.getSessionId())).isTrue();
    }

    @Test
    void submitAnswer_correct_recordsCorrectly() {
        questionRepository.save(buildQuestion("Q1"));
        QuizSession session = quizService.startQuiz(1);

        QuizService.SubmitResult result = quizService.submitAnswer(
            session.getSessionId(), 0, "A", 10);

        assertThat(result.isCorrect()).isTrue();
        assertThat(result.correctAnswer()).isEqualTo("A");
    }

    @Test
    void finalizeQuiz_completesSession() {
        questionRepository.save(buildQuestion("Q1"));
        QuizSession session = quizService.startQuiz(1);
        quizService.submitAnswer(session.getSessionId(), 0, "A", 10);

        QuizSession finalized = quizService.finalizeQuiz(session.getSessionId());
        assertThat(finalized.getPercentageCorrect()).isEqualTo(100.0);
        assertThat(quizService.isSessionActive(session.getSessionId())).isFalse();
    }
}
