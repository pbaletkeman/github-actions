package com.quizengine.util;

import com.quizengine.entity.Question;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownParser {

    private static final Pattern QUESTION_PATTERN = Pattern.compile(
        "##\\s+(?:Question\\s+\\d+[:\\s]+)?(.+?)\\n" +
        "(?:.*?)?\\n?" +
        "- A[).]\\s+(.+?)\\n" +
        "- B[).]\\s+(.+?)\\n" +
        "- C[).]\\s+(.+?)\\n" +
        "- D[).]\\s+(.+?)\\n?" +
        "(?:- E[).]\\s+(.+?)\\n)?" +
        "\\*\\*Answer:\\*\\*\\s+([A-E])",
        Pattern.DOTALL
    );

    private static final Pattern EXPLANATION_PATTERN = Pattern.compile(
        "\\*\\*Explanation:\\*\\*\\s+(.+?)(?=##|\\z)",
        Pattern.DOTALL
    );

    private MarkdownParser() {}

    public static List<Question> parseFile(Path filePath) throws IOException {
        String content = Files.readString(filePath);
        return parseContent(content, filePath.getFileName().toString());
    }

    public static List<Question> parseContent(String content, String sourceFile) {
        List<Question> questions = new ArrayList<>();
        Matcher matcher = QUESTION_PATTERN.matcher(content);

        while (matcher.find()) {
            String questionText = matcher.group(1).trim();
            String optionA = matcher.group(2).trim();
            String optionB = matcher.group(3).trim();
            String optionC = matcher.group(4).trim();
            String optionD = matcher.group(5).trim();
            String optionE = matcher.group(6) != null ? matcher.group(6).trim() : null;
            String correctAnswer = matcher.group(7).trim().toUpperCase();

            String explanation = extractExplanation(content, matcher.end());

            Question question = Question.builder()
                .questionText(questionText)
                .optionA(optionA)
                .optionB(optionB)
                .optionC(optionC)
                .optionD(optionD)
                .optionE(optionE)
                .correctAnswer(correctAnswer)
                .explanation(explanation)
                .sourceFile(sourceFile)
                .usageCycle(1)
                .timesUsed(0)
                .build();

            questions.add(question);
        }

        return questions;
    }

    private static String extractExplanation(String content, int startPos) {
        String remaining = content.substring(startPos);
        Matcher expMatcher = EXPLANATION_PATTERN.matcher(remaining);
        if (expMatcher.find()) {
            String explanation = expMatcher.group(1).trim();
            int nextQuestion = explanation.indexOf("##");
            if (nextQuestion > 0) {
                explanation = explanation.substring(0, nextQuestion).trim();
            }
            return explanation;
        }
        return null;
    }
}
