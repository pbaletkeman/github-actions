package com.quizengine.model;

public class QuizResponse {
    private int id;
    private String sessionId;
    private int questionId;
    private String userAnswer;
    private boolean isCorrect;
    private int timeTakenSeconds;

    public QuizResponse() {}

    private QuizResponse(Builder builder) {
        this.id = builder.id;
        this.sessionId = builder.sessionId;
        this.questionId = builder.questionId;
        this.userAnswer = builder.userAnswer;
        this.isCorrect = builder.isCorrect;
        this.timeTakenSeconds = builder.timeTakenSeconds;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }
    public String getUserAnswer() { return userAnswer; }
    public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }
    public boolean isCorrect() { return isCorrect; }
    public void setCorrect(boolean correct) { isCorrect = correct; }
    public int getTimeTakenSeconds() { return timeTakenSeconds; }
    public void setTimeTakenSeconds(int timeTakenSeconds) { this.timeTakenSeconds = timeTakenSeconds; }

    public static class Builder {
        private int id;
        private String sessionId;
        private int questionId;
        private String userAnswer;
        private boolean isCorrect;
        private int timeTakenSeconds;

        public Builder id(int id) { this.id = id; return this; }
        public Builder sessionId(String sessionId) { this.sessionId = sessionId; return this; }
        public Builder questionId(int questionId) { this.questionId = questionId; return this; }
        public Builder userAnswer(String userAnswer) { this.userAnswer = userAnswer; return this; }
        public Builder isCorrect(boolean isCorrect) { this.isCorrect = isCorrect; return this; }
        public Builder timeTakenSeconds(int timeTakenSeconds) { this.timeTakenSeconds = timeTakenSeconds; return this; }
        public QuizResponse build() { return new QuizResponse(this); }
    }
}
