package com.quizengine.util;

import java.util.*;

public class AnswerShuffler {
    public static ShuffleResult shuffle(List<String> options, String correctAnswerLetter) {
        int correctIdx = correctAnswerLetter.toUpperCase().charAt(0) - 'A';

        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < options.size(); i++) indices.add(i);

        Collections.shuffle(indices);

        List<String> shuffledOptions = new ArrayList<>();
        Map<Integer, Integer> originalToShuffled = new HashMap<>();
        int newCorrectIdx = 0;

        for (int newIdx = 0; newIdx < indices.size(); newIdx++) {
            int origIdx = indices.get(newIdx);
            shuffledOptions.add(options.get(origIdx));
            originalToShuffled.put(origIdx, newIdx);
            if (origIdx == correctIdx) {
                newCorrectIdx = newIdx;
            }
        }

        return new ShuffleResult(shuffledOptions, newCorrectIdx, originalToShuffled);
    }
}
