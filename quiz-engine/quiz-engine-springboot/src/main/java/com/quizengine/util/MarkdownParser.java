package com.quizengine.util;

import com.quizengine.entity.Question;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownParser {

    // ── Legacy format ──────────────────────────────────────────────────────────

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

    // ── gh-200 format ──────────────────────────────────────────────────────────

    private static final Pattern GH200_QUESTION_HEADER = Pattern.compile(
        "###\\s+Question\\s+(\\d+)(?:\\s*[\\u2014\\-]+\\s*(.+?))?\\s*$",
        Pattern.MULTILINE
    );

    private static final Pattern GH200_ANSWER_KEY_ROW = Pattern.compile(
        "\\|\\s*(\\d+)\\s*\\|\\s*([^|]+?)\\s*\\|\\s*([^|]*?)\\s*\\|[^\\n]*"
    );

    private static final Pattern GH200_OPTION = Pattern.compile(
        "^-\\s+([A-E])[).\\s]\\s*(.+)$",
        Pattern.MULTILINE
    );

    private static final Pattern GH200_DIFFICULTY = Pattern.compile(
        "\\*\\*Difficulty\\*\\*:\\s*(.+?)\\s*$",
        Pattern.MULTILINE
    );

    private static final Pattern GH200_ANSWER_TYPE = Pattern.compile(
        "\\*\\*Answer Type\\*\\*:\\s*(.+?)\\s*$",
        Pattern.MULTILINE
    );

    private MarkdownParser() {}

    public static List<Question> parseFile(Path filePath) throws IOException {
        String content = Files.readString(filePath);
        return parseContent(content, filePath.getFileName().toString());
    }

    public static List<Question> parseContent(String content, String sourceFile) {
        if (content.contains("## Answer Key")) {
            return parseGh200Content(content, sourceFile);
        }
        return parseLegacyContent(content, sourceFile);
    }

    // ── gh-200 implementation ──────────────────────────────────────────────────

    static List<Question> parseGh200Content(String content, String sourceFile) {
        Map<Integer, String[]> answerKey = parseAnswerKey(content);
        String[] blocks = content.split("(?=###\\s+Question\\s+\\d+)");

        List<Question> questions = new ArrayList<>();
        for (String block : blocks) {
            Matcher headerMatcher = GH200_QUESTION_HEADER.matcher(block);
            if (!headerMatcher.find()) continue;

            int qNum;
            try {
                qNum = Integer.parseInt(headerMatcher.group(1).trim());
            } catch (NumberFormatException e) {
                continue;
            }

            String sectionRaw = headerMatcher.group(2);
            String section = sectionRaw != null ? sectionRaw.trim() : null;

            String[] entry = answerKey.get(qNum);
            if (entry == null) continue;

            String answer = entry[0].trim();
            String explanation = entry[1];

            // Skip multi-answer, many, none
            if (answer.contains(",")
                    || answer.equalsIgnoreCase("many")
                    || answer.equalsIgnoreCase("none")) continue;

            answer = answer.toUpperCase();
            if (answer.length() != 1 || answer.charAt(0) < 'A' || answer.charAt(0) > 'E') continue;

            // Skip non-single-answer types
            Matcher atMatcher = GH200_ANSWER_TYPE.matcher(block);
            if (atMatcher.find()) {
                String answerType = atMatcher.group(1).trim().toLowerCase();
                if (!answerType.contains("single")) continue;
            }

            String questionText = extractGh200QuestionText(block);
            if (questionText == null || questionText.isBlank()) continue;

            Map<String, String> opts = new LinkedHashMap<>();
            Matcher optMatcher = GH200_OPTION.matcher(block);
            while (optMatcher.find()) {
                opts.put(optMatcher.group(1).toUpperCase(), optMatcher.group(2).trim());
            }
            if (!opts.containsKey("A") || !opts.containsKey("B")
                    || !opts.containsKey("C") || !opts.containsKey("D")) continue;

            String difficulty = null;
            Matcher diffMatcher = GH200_DIFFICULTY.matcher(block);
            if (diffMatcher.find()) {
                difficulty = diffMatcher.group(1).trim();
            }

            Question question = Question.builder()
                .questionText(questionText)
                .optionA(opts.get("A"))
                .optionB(opts.get("B"))
                .optionC(opts.get("C"))
                .optionD(opts.get("D"))
                .optionE(opts.get("E"))
                .correctAnswer(answer)
                .explanation(explanation)
                .section(section)
                .difficulty(difficulty)
                .sourceFile(sourceFile)
                .usageCycle(1)
                .timesUsed(0)
                .build();

            questions.add(question);
        }

        return questions;
    }

    static Map<Integer, String[]> parseAnswerKey(String content) {
        Map<Integer, String[]> map = new HashMap<>();
        int akIndex = content.indexOf("## Answer Key");
        if (akIndex < 0) return map;

        String akSection = content.substring(akIndex);
        Matcher m = GH200_ANSWER_KEY_ROW.matcher(akSection);
        while (m.find()) {
            String numStr = m.group(1).trim();
            String answerStr = m.group(2).trim();
            String expl = m.group(3).trim();
            try {
                int num = Integer.parseInt(numStr);
                String explanationOrNull = expl.isBlank() ? null : expl;
                map.put(num, new String[]{answerStr, explanationOrNull});
            } catch (NumberFormatException e) {
                // Skip header/separator rows
            }
        }
        return map;
    }

    private static String extractGh200QuestionText(String block) {
        int qPos = block.indexOf("**Question:**");
        if (qPos >= 0) {
            String after = block.substring(qPos + "**Question:**".length());
            String[] lines = after.split("\n");
            List<String> textLines = new ArrayList<>();
            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() && !textLines.isEmpty()) break;
                if (trimmed.matches("^-\\s+[A-E][).].*")) break;
                String cleaned = trimmed.replaceAll("^>\\s*", "").trim();
                if (!cleaned.isEmpty()) textLines.add(cleaned);
            }
            if (!textLines.isEmpty()) return String.join(" ", textLines);
        }

        // Fallback: first non-blank, non-metadata line after header
        for (String line : block.split("\n")) {
            if (line.startsWith("###") || line.startsWith("**")) continue;
            String trimmed = line.trim().replaceAll("^>\\s*", "").trim();
            if (!trimmed.isEmpty()) return trimmed;
        }
        return null;
    }

    // ── Legacy implementation ──────────────────────────────────────────────────

    static List<Question> parseLegacyContent(String content, String sourceFile) {
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

            String explanation = extractLegacyExplanation(content, matcher.end());

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

    private static String extractLegacyExplanation(String content, int startPos) {
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
