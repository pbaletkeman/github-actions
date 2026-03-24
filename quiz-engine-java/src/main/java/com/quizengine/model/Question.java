package com.quizengine.model;

public class Question {
    private int id;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String optionE;
    private String correctAnswer;
    private String explanation;
    private String section;
    private String difficulty;
    private String sourceFile;
    private int usageCycle = 1;
    private int timesUsed;
    private String lastUsedAt;

    public Question() {}

    private Question(Builder builder) {
        this.id = builder.id;
        this.questionText = builder.questionText;
        this.optionA = builder.optionA;
        this.optionB = builder.optionB;
        this.optionC = builder.optionC;
        this.optionD = builder.optionD;
        this.optionE = builder.optionE;
        this.correctAnswer = builder.correctAnswer;
        this.explanation = builder.explanation;
        this.section = builder.section;
        this.difficulty = builder.difficulty;
        this.sourceFile = builder.sourceFile;
        this.usageCycle = builder.usageCycle;
        this.timesUsed = builder.timesUsed;
        this.lastUsedAt = builder.lastUsedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public String getOptionA() { return optionA; }
    public void setOptionA(String optionA) { this.optionA = optionA; }
    public String getOptionB() { return optionB; }
    public void setOptionB(String optionB) { this.optionB = optionB; }
    public String getOptionC() { return optionC; }
    public void setOptionC(String optionC) { this.optionC = optionC; }
    public String getOptionD() { return optionD; }
    public void setOptionD(String optionD) { this.optionD = optionD; }
    public String getOptionE() { return optionE; }
    public void setOptionE(String optionE) { this.optionE = optionE; }
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public String getSourceFile() { return sourceFile; }
    public void setSourceFile(String sourceFile) { this.sourceFile = sourceFile; }
    public int getUsageCycle() { return usageCycle; }
    public void setUsageCycle(int usageCycle) { this.usageCycle = usageCycle; }
    public int getTimesUsed() { return timesUsed; }
    public void setTimesUsed(int timesUsed) { this.timesUsed = timesUsed; }
    public String getLastUsedAt() { return lastUsedAt; }
    public void setLastUsedAt(String lastUsedAt) { this.lastUsedAt = lastUsedAt; }

    @Override
    public String toString() {
        return "Question{id=" + id + ", questionText='" + questionText + "', section='" + section + "'}";
    }

    public static class Builder {
        private int id;
        private String questionText;
        private String optionA;
        private String optionB;
        private String optionC;
        private String optionD;
        private String optionE;
        private String correctAnswer;
        private String explanation;
        private String section;
        private String difficulty;
        private String sourceFile;
        private int usageCycle = 1;
        private int timesUsed;
        private String lastUsedAt;

        public Builder id(int id) { this.id = id; return this; }
        public Builder questionText(String questionText) { this.questionText = questionText; return this; }
        public Builder optionA(String optionA) { this.optionA = optionA; return this; }
        public Builder optionB(String optionB) { this.optionB = optionB; return this; }
        public Builder optionC(String optionC) { this.optionC = optionC; return this; }
        public Builder optionD(String optionD) { this.optionD = optionD; return this; }
        public Builder optionE(String optionE) { this.optionE = optionE; return this; }
        public Builder correctAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; return this; }
        public Builder explanation(String explanation) { this.explanation = explanation; return this; }
        public Builder section(String section) { this.section = section; return this; }
        public Builder difficulty(String difficulty) { this.difficulty = difficulty; return this; }
        public Builder sourceFile(String sourceFile) { this.sourceFile = sourceFile; return this; }
        public Builder usageCycle(int usageCycle) { this.usageCycle = usageCycle; return this; }
        public Builder timesUsed(int timesUsed) { this.timesUsed = timesUsed; return this; }
        public Builder lastUsedAt(String lastUsedAt) { this.lastUsedAt = lastUsedAt; return this; }
        public Question build() { return new Question(this); }
    }
}
