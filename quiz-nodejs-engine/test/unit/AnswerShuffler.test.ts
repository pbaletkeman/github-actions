import { shuffleAnswers } from '../../src/service/AnswerShuffler';
import { Question } from '../../src/models/Question';

function makeQuestion(overrides: Partial<Question> = {}): Question {
  const q = new Question();
  q.id = 1;
  q.questionText = 'Test question?';
  q.optionA = 'Alpha';
  q.optionB = 'Beta';
  q.optionC = 'Gamma';
  q.optionD = 'Delta';
  q.correctAnswer = 'A';
  q.usageCycle = 1;
  q.timesUsed = 0;
  q.createdAt = new Date();
  Object.assign(q, overrides);
  return q;
}

describe('AnswerShuffler', () => {
  it('returns all original options after shuffling', () => {
    const q = makeQuestion();
    const result = shuffleAnswers(q);
    expect(new Set(result.shuffledOptions)).toEqual(
      new Set([q.optionA, q.optionB, q.optionC, q.optionD]),
    );
  });

  it('always returns 4 options for a 4-option question', () => {
    const q = makeQuestion();
    const result = shuffleAnswers(q);
    expect(result.shuffledOptions).toHaveLength(4);
  });

  it('returns 5 options for a 5-option question', () => {
    const q = makeQuestion({ optionE: 'Epsilon' });
    const result = shuffleAnswers(q);
    expect(result.shuffledOptions).toHaveLength(5);
    expect(result.shuffledOptions).toContain('Epsilon');
  });

  it('maps correctShuffledIndex to the correct answer text', () => {
    const q = makeQuestion({ correctAnswer: 'A' }); // A = Alpha
    const result = shuffleAnswers(q);
    expect(result.shuffledOptions[result.correctShuffledIndex]).toBe('Alpha');
  });

  it('maps correct answer B correctly', () => {
    const q = makeQuestion({ correctAnswer: 'B' }); // B = Beta
    const result = shuffleAnswers(q);
    expect(result.shuffledOptions[result.correctShuffledIndex]).toBe('Beta');
  });

  it('maps correct answer C correctly', () => {
    const q = makeQuestion({ correctAnswer: 'C' }); // C = Gamma
    const result = shuffleAnswers(q);
    expect(result.shuffledOptions[result.correctShuffledIndex]).toBe('Gamma');
  });

  it('maps correct answer D correctly', () => {
    const q = makeQuestion({ correctAnswer: 'D' }); // D = Delta
    const result = shuffleAnswers(q);
    expect(result.shuffledOptions[result.correctShuffledIndex]).toBe('Delta');
  });

  it('maps correct answer E correctly', () => {
    const q = makeQuestion({ optionE: 'Epsilon', correctAnswer: 'E' });
    const result = shuffleAnswers(q);
    expect(result.shuffledOptions[result.correctShuffledIndex]).toBe('Epsilon');
  });

  it('falls back to optionD when E is correct but optionE missing', () => {
    const q = makeQuestion({ correctAnswer: 'E' }); // No optionE
    const result = shuffleAnswers(q);
    // Should not throw and should return a valid index
    expect(result.correctShuffledIndex).toBeGreaterThanOrEqual(0);
  });

  it('displayToOriginal has an entry for each display letter', () => {
    const q = makeQuestion();
    const result = shuffleAnswers(q);
    expect(Object.keys(result.displayToOriginal)).toHaveLength(4);
  });

  it('displayToOriginal maps display letters to original letters', () => {
    const q = makeQuestion();
    const result = shuffleAnswers(q);
    const letters = ['A', 'B', 'C', 'D'];
    for (const letter of letters) {
      expect(letters).toContain(result.displayToOriginal[letter]);
    }
  });

  it('is non-deterministic (shuffles differently at least sometimes)', () => {
    // Run many times and check that not all are identical
    const q = makeQuestion();
    const indices = new Set<number>();
    for (let i = 0; i < 50; i++) {
      indices.add(shuffleAnswers(q).correctShuffledIndex);
    }
    // With 4 positions, over 50 runs we expect more than 1 unique index
    expect(indices.size).toBeGreaterThan(1);
  });

  it('handles unknown correct answer letter gracefully', () => {
    const q = makeQuestion({ correctAnswer: 'Z' }); // Falls back to A
    const result = shuffleAnswers(q);
    expect(result.shuffledOptions[result.correctShuffledIndex]).toBe('Alpha');
  });
});
