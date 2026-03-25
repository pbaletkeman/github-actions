package com.quizengine.model;

public class QuizSession {
    private String sessionId;
    private String startedAt;
    private String endedAt;
    private int numQuestions;
    private int numCorrect;
    private double percentageCorrect;
    private int timeTakenSeconds;

    public QuizSession() {}

    private QuizSession(Builder builder) {
        this.sessionId = builder.sessionId;
        this.startedAt = builder.startedAt;
        this.endedAt = builder.endedAt;
        this.numQuestions = builder.numQuestions;
        this.numCorrect = builder.numCorrect;
        this.percentageCorrect = builder.percentageCorrect;
        this.timeTakenSeconds = builder.timeTakenSeconds;
    }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getStartedAt() { return startedAt; }
    public void setStartedAt(String startedAt) { this.startedAt = startedAt; }
    public String getEndedAt() { return endedAt; }
    public void setEndedAt(String endedAt) { this.endedAt = endedAt; }
    public int getNumQuestions() { return numQuestions; }
    public void setNumQuestions(int numQuestions) { this.numQuestions = numQuestions; }
    public int getNumCorrect() { return numCorrect; }
    public void setNumCorrect(int numCorrect) { this.numCorrect = numCorrect; }
    public double getPercentageCorrect() { return percentageCorrect; }
    public void setPercentageCorrect(double percentageCorrect) { this.percentageCorrect = percentageCorrect; }
    public int getTimeTakenSeconds() { return timeTakenSeconds; }
    public void setTimeTakenSeconds(int timeTakenSeconds) { this.timeTakenSeconds = timeTakenSeconds; }

    public static class Builder {
        private String sessionId;
        private String startedAt;
        private String endedAt;
        private int numQuestions;
        private int numCorrect;
        private double percentageCorrect;
        private int timeTakenSeconds;

        public Builder sessionId(String sessionId) { this.sessionId = sessionId; return this; }
        public Builder startedAt(String startedAt) { this.startedAt = startedAt; return this; }
        public Builder endedAt(String endedAt) { this.endedAt = endedAt; return this; }
        public Builder numQuestions(int numQuestions) { this.numQuestions = numQuestions; return this; }
        public Builder numCorrect(int numCorrect) { this.numCorrect = numCorrect; return this; }
        public Builder percentageCorrect(double percentageCorrect) { this.percentageCorrect = percentageCorrect; return this; }
        public Builder timeTakenSeconds(int timeTakenSeconds) { this.timeTakenSeconds = timeTakenSeconds; return this; }
        public QuizSession build() { return new QuizSession(this); }
    }
}
