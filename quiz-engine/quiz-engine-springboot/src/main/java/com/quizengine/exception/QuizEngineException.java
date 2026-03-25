package com.quizengine.exception;

public class QuizEngineException extends RuntimeException {

    public QuizEngineException(String message) {
        super(message);
    }

    public QuizEngineException(String message, Throwable cause) {
        super(message, cause);
    }
}
