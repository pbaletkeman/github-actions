package com.quizengine.service;

import com.quizengine.entity.Question;
import com.quizengine.entity.QuizResponse;
import com.quizengine.entity.QuizSession;
import com.quizengine.exception.QuizEngineException;
import com.quizengine.repository.QuestionRepository;
import com.quizengine.repository.ResponseRepository;
import com.quizengine.repository.SessionRepository;
import com.quizengine.util.QuizUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizEngine {

    private final QuestionRepository questionRepository;
    private final SessionRepository sessionRepository;
    private final ResponseRepository responseRepository;

    public QuizEngine(QuestionRepository questionRepository,
                      SessionRepository sessionRepository,
                      ResponseRepository responseRepository) {
        this.questionRepository = questionRepository;
        this.sessionRepository = sessionRepository;
        this.responseRepository = responseRepository;
    }

    @Transactional
    public QuizSession startNewSession(int numQuestions) {
        List<Question> questions = loadQuestions(numQuestions);
        if (questions.isEmpty()) {
            throw new QuizEngineException("No questions available. Please import questions first.");
        }
        if (questions.size() < numQuestions) {
            numQuestions = questions.size();
        }

        String sessionId = QuizUtils.generateSessionId();
        QuizSession session = QuizSession.builder()
            .sessionId(sessionId)
            .numQuestions(numQuestions)
            .numCorrect(0)
            .percentageCorrect(0.0)
            .responses(new ArrayList<>())
            .build();

        session = sessionRepository.save(session);
        return session;
    }

    public List<Question> loadQuestions(int numQuestions) {
        Integer currentCycle = questionRepository.findCurrentCycle();
        if (currentCycle == null) {
            return List.of();
        }
        List<Question> all = questionRepository.findByUsageCycle(currentCycle);
        Collections.shuffle(all);
        return all.stream().limit(numQuestions).collect(Collectors.toList());
    }

    @Transactional
    public boolean checkAnswer(String userAnswer, String correctAnswer) {
        return userAnswer != null && userAnswer.trim().equalsIgnoreCase(correctAnswer.trim());
    }

    @Transactional
    public QuizResponse recordAnswer(QuizSession session, Question question,
                                     String userAnswer, boolean isCorrect, int timeTakenSeconds) {
        QuizResponse response = QuizResponse.builder()
            .session(session)
            .question(question)
            .userAnswer(userAnswer.toUpperCase())
            .isCorrect(isCorrect ? 1 : 0)
            .timeTakenSeconds(timeTakenSeconds)
            .build();

        response = responseRepository.save(response);
        questionRepository.markQuestionUsed(question.getId());
        return response;
    }

    @Transactional
    public QuizSession finalizeSession(String sessionId, int numCorrect, int numTotal) {
        QuizSession session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new QuizEngineException("Session not found: " + sessionId));

        session.setEndedAt(LocalDateTime.now());
        session.setNumCorrect(numCorrect);
        session.setNumQuestions(numTotal);
        session.setPercentageCorrect(QuizUtils.calculatePercentage(numCorrect, numTotal));
        session.setTimeTakenSeconds(
            QuizUtils.calculateElapsedSeconds(session.getStartedAt()));

        long unusedCount = questionRepository.countUnusedInCurrentCycle();
        if (unusedCount == 0) {
            questionRepository.advanceCycle();
        }

        return sessionRepository.save(session);
    }
}
