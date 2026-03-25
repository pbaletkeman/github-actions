package com.quizengine.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnswerShufflerTest {

    @Test
    void shuffle_preservesAllOptions() {
        List<String> options = Arrays.asList("Alpha", "Beta", "Gamma", "Delta");
        ShuffleResult result = AnswerShuffler.shuffle(options, "A");
        assertEquals(4, result.getShuffledOptions().size());
        assertTrue(result.getShuffledOptions().containsAll(options));
    }

    @Test
    void shuffle_mapsCorrectAnswerToNewPosition() {
        List<String> options = Arrays.asList("Correct", "Wrong1", "Wrong2", "Wrong3");
        ShuffleResult result = AnswerShuffler.shuffle(options, "A");
        int newIdx = result.getCorrectShuffledIndex();
        assertEquals("Correct", result.getShuffledOptions().get(newIdx));
    }

    @Test
    void shuffle_withFourOptions() {
        List<String> options = Arrays.asList("One", "Two", "Three", "Four");
        ShuffleResult result = AnswerShuffler.shuffle(options, "B");
        assertEquals(4, result.getShuffledOptions().size());
        int newIdx = result.getCorrectShuffledIndex();
        assertEquals("Two", result.getShuffledOptions().get(newIdx));
    }

    @Test
    void shuffle_correctShuffledLetterIsValid() {
        List<String> options = Arrays.asList("A text", "B text", "C text", "D text");
        ShuffleResult result = AnswerShuffler.shuffle(options, "C");
        String letter = result.getCorrectShuffledLetter();
        assertTrue(letter.matches("[A-D]"));
    }
}
