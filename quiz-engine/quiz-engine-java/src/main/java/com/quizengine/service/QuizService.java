package com.quizengine.service;

import com.quizengine.dao.QuestionDAO;
import com.quizengine.dao.ResponseDAO;
import com.quizengine.dao.SessionDAO;

import java.sql.Connection;

public class QuizService {
    private final Connection connection;

    public QuizService(Connection connection) {
        this.connection = connection;
    }

    public QuizEngine createQuizEngine(int numQuestions) {
        return new QuizEngine(
            new QuestionDAO(connection),
            new SessionDAO(connection),
            new ResponseDAO(connection),
            numQuestions,
            null,
            null
        );
    }

    public QuizEngine createQuizEngine(int numQuestions, String difficulty, String section) {
        return new QuizEngine(
            new QuestionDAO(connection),
            new SessionDAO(connection),
            new ResponseDAO(connection),
            numQuestions,
            difficulty,
            section
        );
    }
}
