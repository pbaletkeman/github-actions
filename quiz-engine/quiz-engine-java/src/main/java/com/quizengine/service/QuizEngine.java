package com.quizengine.service;

import com.quizengine.dao.QuestionDAO;
import com.quizengine.dao.ResponseDAO;
import com.quizengine.dao.SessionDAO;
import com.quizengine.model.Question;
import com.quizengine.model.QuizResponse;
import com.quizengine.model.QuizSession;
import com.quizengine.util.AnswerShuffler;
import com.quizengine.util.QuizUtils;
import com.quizengine.util.ShuffleResult;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizEngine {
    private final QuestionDAO questionDAO;
    private final SessionDAO sessionDAO;
    private final ResponseDAO responseDAO;
    private final int numQuestions;
    private final String difficulty;
    private final String section;

    private List<Question> questions;
    private List<String> internalAnswers;
    private Map<Integer, ShuffleResult> shuffleResults;
    private int numCorrect;
    private String sessionId;
    private long startTime;

    public QuizEngine(QuestionDAO questionDAO, SessionDAO sessionDAO, ResponseDAO responseDAO, int numQuestions) {
        this(questionDAO, sessionDAO, responseDAO, numQuestions, null, null);
    }

    public QuizEngine(QuestionDAO questionDAO, SessionDAO sessionDAO, ResponseDAO responseDAO, int numQuestions, String difficulty, String section) {
        this.questionDAO = questionDAO;
        this.sessionDAO = sessionDAO;
        this.responseDAO = responseDAO;
        this.numQuestions = numQuestions;
        this.difficulty = difficulty;
        this.section = section;
    }

    public void loadQuestions() throws SQLException {
        List<Question> loaded;
        if (difficulty != null || section != null) {
            loaded = questionDAO.getRandomQuestions(numQuestions, difficulty, section);
        } else {
            loaded = questionDAO.getRandomQuestions(numQuestions);
        }

        internalAnswers = new ArrayList<>();
        shuffleResults = new HashMap<>();
        questions = new ArrayList<>();

        for (int i = 0; i < loaded.size(); i++) {
            Question q = loaded.get(i);
            Question full = questionDAO.getById(q.getId());
            internalAnswers.add(full.getCorrectAnswer());

            List<String> opts = new ArrayList<>();
            opts.add(full.getOptionA());
            opts.add(full.getOptionB());
            opts.add(full.getOptionC());
            opts.add(full.getOptionD());
            if (full.getOptionE() != null && !full.getOptionE().isEmpty()) {
                opts.add(full.getOptionE());
            }

            ShuffleResult shuffle = AnswerShuffler.shuffle(opts, full.getCorrectAnswer());
            shuffleResults.put(i, shuffle);

            Question.Builder builder = new Question.Builder()
                    .id(full.getId())
                    .questionText(full.getQuestionText())
                    .explanation(full.getExplanation())
                    .section(full.getSection())
                    .difficulty(full.getDifficulty())
                    .usageCycle(full.getUsageCycle())
                    .timesUsed(full.getTimesUsed());

            List<String> shuffled = shuffle.getShuffledOptions();
            if (shuffled.size() > 0) builder.optionA(shuffled.get(0));
            if (shuffled.size() > 1) builder.optionB(shuffled.get(1));
            if (shuffled.size() > 2) builder.optionC(shuffled.get(2));
            if (shuffled.size() > 3) builder.optionD(shuffled.get(3));
            if (shuffled.size() > 4) builder.optionE(shuffled.get(4));

            questions.add(builder.build());
        }

        numCorrect = 0;
        sessionId = QuizUtils.generateSessionId();
        startTime = System.currentTimeMillis();

        QuizSession session = new QuizSession.Builder()
                .sessionId(sessionId)
                .startedAt(QuizUtils.now())
                .numQuestions(questions.size())
                .build();
        sessionDAO.insert(session);
    }

    public boolean submitAnswer(int questionIdx, String userAnswer, int timeTakenSeconds) throws SQLException {
        if (questionIdx < 0 || questionIdx >= questions.size()) return false;
        ShuffleResult shuffle = shuffleResults.get(questionIdx);
        String correctShuffledLetter = shuffle.getCorrectShuffledLetter();
        boolean correct = correctShuffledLetter.equalsIgnoreCase(userAnswer);
        if (correct) numCorrect++;

        Question q = questions.get(questionIdx);
        QuizResponse response = new QuizResponse.Builder()
                .sessionId(sessionId)
                .questionId(q.getId())
                .userAnswer(userAnswer.toUpperCase())
                .isCorrect(correct)
                .timeTakenSeconds(timeTakenSeconds)
                .build();
        responseDAO.insert(response);
        return correct;
    }

    public QuizSession complete() throws SQLException {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        double percentage = QuizUtils.calculateScore(numCorrect, questions.size());

        QuizSession session = new QuizSession.Builder()
                .sessionId(sessionId)
                .endedAt(QuizUtils.now())
                .numQuestions(questions.size())
                .numCorrect(numCorrect)
                .percentageCorrect(percentage)
                .timeTakenSeconds((int) elapsed)
                .build();
        sessionDAO.update(session);

        for (Question q : questions) {
            questionDAO.markUsed(q.getId());
        }
        questionDAO.advanceCycleIfExhausted();

        return session;
    }

    public int getNumCorrect() { return numCorrect; }
    public List<Question> getQuestions() { return questions; }
    public ShuffleResult getShuffleResult(int idx) { return shuffleResults.get(idx); }
    public String getSessionId() { return sessionId; }
}
