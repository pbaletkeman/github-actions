package com.quizengine.util;

import com.quizengine.model.Question;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownParser {
    private static final Pattern QUESTION_PATTERN = Pattern.compile("> (.+?)(?=\\n- [A-E]\\)|$)", Pattern.DOTALL);
    private static final Pattern OPTION_PATTERN = Pattern.compile("- ([A-E])\\) (.+)");
    private static final Pattern ANSWER_PATTERN = Pattern.compile("\\*\\*Answer:\\s*([A-E])\\*\\*");
    private static final Pattern EXPLANATION_PATTERN = Pattern.compile("> \\*\\*Explanation:\\*\\*\\s*(.+)", Pattern.DOTALL);

    public static List<Question> parseFile(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        String[] blocks = content.split("(?=## Q\\d+)");
        List<Question> questions = new ArrayList<>();
        for (String block : blocks) {
            block = block.trim();
            if (block.isEmpty() || !block.startsWith("## Q")) continue;
            try {
                Question q = parseBlock(block);
                q.setSourceFile(filePath);
                questions.add(q);
            } catch (IllegalArgumentException e) {
                // skip malformed blocks
            }
        }
        return questions;
    }

    public static Question parseBlock(String block) {
        Matcher qm = QUESTION_PATTERN.matcher(block);
        String questionText = "";
        if (qm.find()) {
            questionText = qm.group(1).trim();
        }

        String optionA = null, optionB = null, optionC = null, optionD = null, optionE = null;
        Matcher om = OPTION_PATTERN.matcher(block);
        while (om.find()) {
            String letter = om.group(1).toUpperCase();
            String text = om.group(2).trim();
            switch (letter) {
                case "A": optionA = text; break;
                case "B": optionB = text; break;
                case "C": optionC = text; break;
                case "D": optionD = text; break;
                case "E": optionE = text; break;
            }
        }

        Matcher am = ANSWER_PATTERN.matcher(block);
        if (!am.find()) {
            throw new IllegalArgumentException("No answer found in block: " + block);
        }
        String correctAnswer = am.group(1).toUpperCase();

        String explanation = null;
        Matcher em = EXPLANATION_PATTERN.matcher(block);
        if (em.find()) {
            explanation = em.group(1).trim();
        }

        return new Question.Builder()
                .questionText(questionText)
                .optionA(optionA != null ? optionA : "")
                .optionB(optionB != null ? optionB : "")
                .optionC(optionC != null ? optionC : "")
                .optionD(optionD != null ? optionD : "")
                .optionE(optionE)
                .correctAnswer(correctAnswer)
                .explanation(explanation)
                .build();
    }
}
