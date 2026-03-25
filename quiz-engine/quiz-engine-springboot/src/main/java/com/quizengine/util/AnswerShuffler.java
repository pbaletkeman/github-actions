package com.quizengine.util;

import com.quizengine.entity.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AnswerShuffler {

    private AnswerShuffler() {}

    public static Map<String, String> shuffleAnswers(Question question) {
        List<String[]> options = new ArrayList<>();
        options.add(new String[]{"A", question.getOptionA()});
        options.add(new String[]{"B", question.getOptionB()});
        options.add(new String[]{"C", question.getOptionC()});
        options.add(new String[]{"D", question.getOptionD()});
        if (question.getOptionE() != null && !question.getOptionE().isBlank()) {
            options.add(new String[]{"E", question.getOptionE()});
        }

        Collections.shuffle(options);

        Map<String, String> shuffled = new LinkedHashMap<>();
        String[] letters = {"A", "B", "C", "D", "E"};
        for (int i = 0; i < options.size(); i++) {
            shuffled.put(letters[i], options.get(i)[1]);
        }
        return shuffled;
    }

    public static String findCorrectLetter(Map<String, String> shuffled, Question question) {
        String correctText = getCorrectAnswerText(question);
        for (Map.Entry<String, String> entry : shuffled.entrySet()) {
            if (entry.getValue().equals(correctText)) {
                return entry.getKey();
            }
        }
        return question.getCorrectAnswer();
    }

    private static String getCorrectAnswerText(Question question) {
        return switch (question.getCorrectAnswer().toUpperCase()) {
            case "A" -> question.getOptionA();
            case "B" -> question.getOptionB();
            case "C" -> question.getOptionC();
            case "D" -> question.getOptionD();
            case "E" -> question.getOptionE();
            default -> question.getOptionA();
        };
    }
}
