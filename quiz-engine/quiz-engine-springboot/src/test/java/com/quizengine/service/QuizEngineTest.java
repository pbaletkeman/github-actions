package com.quizengine.service;

import com.quizengine.entity.Question;
import com.quizengine.entity.QuizSession;
import com.quizengine.exception.QuizEngineException;
import com.quizengine.repository.QuestionRepository;
import com.quizengine.repository.ResponseRepository;
import com.quizengine.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizEngineTest {

    @Mock private QuestionRepository questionRepository;
    @Mock private SessionRepository sessionRepository;
    @Mock private ResponseRepository responseRepository;

    @InjectMocks
    private QuizEngine quizEngine;

    private Question buildQuestion() {
        return Question.builder()
            .id(1L)
            .questionText("What is CI?")
            .optionA("Continuous Integration")
            .optionB("Code Import")
            .optionC("Compile")
            .optionD("Configure")
            .correctAnswer("A")
            .usageCycle(1)
            .timesUsed(0)
            .build();
    }

    @Test
    void checkAnswer_correctAnswer_returnsTrue() {
        assertThat(quizEngine.checkAnswer("A", "A")).isTrue();
        assertThat(quizEngine.checkAnswer("a", "A")).isTrue();
    }

    @Test
    void checkAnswer_wrongAnswer_returnsFalse() {
        assertThat(quizEngine.checkAnswer("B", "A")).isFalse();
    }

    @Test
    void checkAnswer_nullAnswer_returnsFalse() {
        assertThat(quizEngine.checkAnswer(null, "A")).isFalse();
    }

    @Test
    void loadQuestions_callsRepository() {
        when(questionRepository.findCurrentCycle()).thenReturn(1);
        when(questionRepository.findByUsageCycle(anyInt()))
            .thenReturn(List.of(buildQuestion()));
        List<Question> result = quizEngine.loadQuestions(1);
        assertThat(result).hasSize(1);
        verify(questionRepository).findByUsageCycle(1);
    }

    @Test
    void loadQuestions_noCurrentCycle_returnsEmpty() {
        when(questionRepository.findCurrentCycle()).thenReturn(null);
        assertThat(quizEngine.loadQuestions(5)).isEmpty();
    }

    @Test
    void startNewSession_noQuestions_throwsException() {
        when(questionRepository.findCurrentCycle()).thenReturn(null);
        assertThatThrownBy(() -> quizEngine.startNewSession(5))
            .isInstanceOf(QuizEngineException.class)
            .hasMessageContaining("No questions available");
    }

    @Test
    void startNewSession_createsSession() {
        when(questionRepository.findCurrentCycle()).thenReturn(1);
        when(questionRepository.findByUsageCycle(anyInt()))
            .thenReturn(List.of(buildQuestion()));
        QuizSession mockSession = QuizSession.builder()
            .sessionId("test-id").numQuestions(1).build();
        when(sessionRepository.save(any(QuizSession.class))).thenReturn(mockSession);

        QuizSession result = quizEngine.startNewSession(1);
        assertThat(result).isNotNull();
        verify(sessionRepository).save(any(QuizSession.class));
    }

    @Test
    void finalizeSession_sessionNotFound_throwsException() {
        when(sessionRepository.findById("bad-id")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> quizEngine.finalizeSession("bad-id", 5, 10))
            .isInstanceOf(QuizEngineException.class)
            .hasMessageContaining("Session not found");
    }
}
