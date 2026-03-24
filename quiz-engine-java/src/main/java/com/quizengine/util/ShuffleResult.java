package com.quizengine.util;

import java.util.List;
import java.util.Map;

public class ShuffleResult {
    private final List<String> shuffledOptions;
    private final int correctShuffledIndex;
    private final Map<Integer, Integer> originalToShuffled;

    public ShuffleResult(List<String> shuffledOptions, int correctShuffledIndex, Map<Integer, Integer> originalToShuffled) {
        this.shuffledOptions = shuffledOptions;
        this.correctShuffledIndex = correctShuffledIndex;
        this.originalToShuffled = originalToShuffled;
    }

    public List<String> getShuffledOptions() { return shuffledOptions; }
    public int getCorrectShuffledIndex() { return correctShuffledIndex; }
    public Map<Integer, Integer> getOriginalToShuffled() { return originalToShuffled; }

    public String getCorrectShuffledLetter() {
        return String.valueOf((char) ('A' + correctShuffledIndex));
    }
}
