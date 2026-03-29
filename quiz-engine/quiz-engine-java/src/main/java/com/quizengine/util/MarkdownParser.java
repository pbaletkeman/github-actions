package com.quizengine.util;

import com.quizengine.model.Question;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.*;

public class MarkdownParser {

    // Matches: ### Question 42 — Some Section Title (em-dash, en-dash, or hyphen)
    private static final Pattern QUESTION_HEADER = Pattern.compile(
            "^###\\s+Question\\s+(\\d+)\\s*[-\u2014\u2013]+\\s*(.+)$", Pattern.MULTILINE);

    private static final Pattern ANSWER_TYPE = Pattern.compile(
            "\\*\\*Answer Type\\*\\*:\\s*(\\w+)");

    private static final Pattern DIFFICULTY = Pattern.compile(
            "\\*\\*Difficulty\\*\\*:\\s*(\\w+)");

    // Captures scenario text: from **Scenario**:\n up to the next **Xxx** header or first option line
    private static final Pattern SCENARIO_TEXT = Pattern.compile(
            "\\*\\*Scenario\\*\\*:[^\\n]*\\n([\\s\\S]+?)(?=\\n\\*\\*[A-Z]|\\n- [A-E]\\))");

    // Captures question text: from **Question**:\n up to first option line
    private static final Pattern QUESTION_TEXT = Pattern.compile(
            "\\*\\*Question\\*\\*:[^\\n]*\\n([\\s\\S]+?)(?=\\n- [A-E]\\))");

    private static final Pattern OPTION = Pattern.compile(
            "^- ([A-E])\\) (.+)$", Pattern.MULTILINE);

    // Matches a single answer key table row: | N | Answer | Explanation | Source | Difficulty |
    private static final Pattern ANSWER_KEY_ROW = Pattern.compile(
            "^\\|\\s*(\\d+)\\s*\\|\\s*([^|]+)\\|\\s*([^|]*)\\|", Pattern.MULTILINE);

    public static List<Question> parseFile(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8)
                .replace("\r\n", "\n")
                .replace("\r", "\n");

        Map<Integer, AnswerEntry> answerKey = parseAnswerKey(content);

        int questionsStart = content.indexOf("## Questions");
        int answerKeyStart = content.indexOf("## Answer Key");
        if (questionsStart < 0) questionsStart = 0;
        String questionsContent = (answerKeyStart > questionsStart)
                ? content.substring(questionsStart, answerKeyStart)
                : content.substring(questionsStart);

        Matcher headerMatcher = QUESTION_HEADER.matcher(questionsContent);
        List<int[]> positions = new ArrayList<>();  // [blockStart, questionNum]
        while (headerMatcher.find()) {
            positions.add(new int[]{headerMatcher.start(), Integer.parseInt(headerMatcher.group(1))});
        }

        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < positions.size(); i++) {
            int blockStart = positions.get(i)[0];
            int questionNum = positions.get(i)[1];
            int blockEnd = (i + 1 < positions.size()) ? positions.get(i + 1)[0] : questionsContent.length();
            String block = questionsContent.substring(blockStart, blockEnd);
            try {
                Question q = parseBlock(block, questionNum, answerKey, filePath);
                if (q != null) questions.add(q);
            } catch (Exception ignored) {
                // skip malformed blocks
            }
        }

        return questions;
    }

    private static Question parseBlock(String block, int questionNum,
                                       Map<Integer, AnswerEntry> answerKey, String filePath) {
        Matcher headerMatcher = QUESTION_HEADER.matcher(block);
        String section = headerMatcher.find() ? headerMatcher.group(2).trim() : "";

        Matcher typeMatcher = ANSWER_TYPE.matcher(block);
        String answerType = typeMatcher.find() ? typeMatcher.group(1).toLowerCase() : "one";
        if (answerType.equals("many") || answerType.equals("none")) return null;

        Matcher diffMatcher = DIFFICULTY.matcher(block);
        String difficulty = diffMatcher.find() ? diffMatcher.group(1) : "";

        StringBuilder questionText = new StringBuilder();
        Matcher scenarioMatcher = SCENARIO_TEXT.matcher(block);
        if (scenarioMatcher.find()) {
            questionText.append(scenarioMatcher.group(1).trim()).append("\n\n");
        }
        Matcher qtMatcher = QUESTION_TEXT.matcher(block);
        if (qtMatcher.find()) {
            questionText.append(qtMatcher.group(1).trim());
        }
        if (questionText.length() == 0) return null;

        String optA = null, optB = null, optC = null, optD = null, optE = null;
        Matcher optMatcher = OPTION.matcher(block);
        while (optMatcher.find()) {
            String letter = optMatcher.group(1);
            String text = optMatcher.group(2).trim();
            switch (letter) {
                case "A": optA = text; break;
                case "B": optB = text; break;
                case "C": optC = text; break;
                case "D": optD = text; break;
                case "E": optE = text; break;
            }
        }

        AnswerEntry entry = answerKey.get(questionNum);
        if (entry == null) return null;

        return new Question.Builder()
                .questionText(questionText.toString().trim())
                .optionA(optA != null ? optA : "")
                .optionB(optB != null ? optB : "")
                .optionC(optC != null ? optC : "")
                .optionD(optD != null ? optD : "")
                .optionE(optE)
                .correctAnswer(entry.answer)
                .explanation(entry.explanation)
                .section(section)
                .difficulty(difficulty)
                .sourceFile(filePath)
                .build();
    }

    private static Map<Integer, AnswerEntry> parseAnswerKey(String content) {
        Map<Integer, AnswerEntry> map = new HashMap<>();
        int keyStart = content.indexOf("## Answer Key");
        if (keyStart < 0) return map;
        String keySection = content.substring(keyStart);
        Matcher rowMatcher = ANSWER_KEY_ROW.matcher(keySection);
        while (rowMatcher.find()) {
            String numStr = rowMatcher.group(1).trim();
            String answer = rowMatcher.group(2).trim();
            String explanation = rowMatcher.group(3).trim();
            try {
                int num = Integer.parseInt(numStr);
                if (answer.matches("[A-E]")) {
                    map.put(num, new AnswerEntry(answer, explanation));
                }
            } catch (NumberFormatException ignored) {
                // skip header row
            }
        }
        return map;
    }

    private static class AnswerEntry {
        final String answer;
        final String explanation;

        AnswerEntry(String answer, String explanation) {
            this.answer = answer;
            this.explanation = explanation;
        }
    }
}
