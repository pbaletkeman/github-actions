package com.quizengine.service;

import com.quizengine.entity.Question;
import com.quizengine.entity.QuizSession;
import com.quizengine.exception.QuizEngineException;
import com.quizengine.repository.QuestionRepository;
import com.quizengine.repository.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class QuizService {

    private final QuizEngine quizEngine;
    private final QuestionRepository questionRepository;
    private final SessionRepository sessionRepository;

    private final Map<String, ActiveQuiz> activeQuizzes = new ConcurrentHashMap<>();

    public QuizService(QuizEngine quizEngine,
                       QuestionRepository questionRepository,
                       SessionRepository sessionRepository) {
        this.quizEngine = quizEngine;
        this.questionRepository = questionRepository;
        this.sessionRepository = sessionRepository;
    }

    @Transactional
    public QuizSession startQuiz(int numQuestions) {
        QuizSession session = quizEngine.startNewSession(numQuestions);
        List<Question> questions = quizEngine.loadQuestions(numQuestions);

        ActiveQuiz activeQuiz = new ActiveQuiz(session, questions);
        activeQuizzes.put(session.getSessionId(), activeQuiz);

        return session;
    }

    public Question getNextQuestion(String sessionId) {
        ActiveQuiz activeQuiz = getActiveQuiz(sessionId);
        int idx = activeQuiz.currentIndex;
        if (idx >= activeQuiz.questions.size()) {
            return null;
        }
        return activeQuiz.questions.get(idx);
    }

    @Transactional
    public SubmitResult submitAnswer(String sessionId, int questionIndex, String userAnswer, int timeTakenSeconds) {
        ActiveQuiz activeQuiz = getActiveQuiz(sessionId);

        if (questionIndex < 0 || questionIndex >= activeQuiz.questions.size()) {
            throw new QuizEngineException("Invalid question index: " + questionIndex);
        }

        Question question = activeQuiz.questions.get(questionIndex);
        boolean isCorrect = quizEngine.checkAnswer(userAnswer, question.getCorrectAnswer());

        quizEngine.recordAnswer(activeQuiz.session, question, userAnswer, isCorrect, timeTakenSeconds);

        if (isCorrect) {
            activeQuiz.numCorrect++;
        }
        activeQuiz.currentIndex = questionIndex + 1;

        return new SubmitResult(isCorrect, question.getCorrectAnswer(), question.getExplanation());
    }

    @Transactional
    public QuizSession finalizeQuiz(String sessionId) {
        ActiveQuiz activeQuiz = getActiveQuiz(sessionId);
        QuizSession result = quizEngine.finalizeSession(
            sessionId, activeQuiz.numCorrect, activeQuiz.questions.size());
        activeQuizzes.remove(sessionId);
        return result;
    }

    public QuizSession getSession(String sessionId) {
        return sessionRepository.findById(sessionId)
            .orElseThrow(() -> new QuizEngineException("Session not found: " + sessionId));
    }

    public List<Question> getSessionQuestions(String sessionId) {
        ActiveQuiz activeQuiz = activeQuizzes.get(sessionId);
        if (activeQuiz != null) {
            return activeQuiz.questions;
        }
        return List.of();
    }

    public int getCurrentQuestionIndex(String sessionId) {
        ActiveQuiz activeQuiz = activeQuizzes.get(sessionId);
        if (activeQuiz != null) {
            return activeQuiz.currentIndex;
        }
        return -1;
    }

    public boolean isSessionActive(String sessionId) {
        return activeQuizzes.containsKey(sessionId);
    }

    private ActiveQuiz getActiveQuiz(String sessionId) {
        ActiveQuiz activeQuiz = activeQuizzes.get(sessionId);
        if (activeQuiz == null) {
            throw new QuizEngineException("No active quiz session: " + sessionId);
        }
        return activeQuiz;
    }

    public static class ActiveQuiz {
        final QuizSession session;
        final List<Question> questions;
        int currentIndex = 0;
        int numCorrect = 0;

        public ActiveQuiz(QuizSession session, List<Question> questions) {
            this.session = session;
            this.questions = questions;
        }
    }

    public record SubmitResult(boolean correct, String correctAnswer, String explanation) {
        public boolean isCorrect() { return correct; }
    }
}
