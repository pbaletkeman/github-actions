import { createTestDataSource, sampleQuestion, sampleQuestion2, sampleQuestion3 } from '../setup';
import { QuizEngine } from '../../src/service/QuizEngine';
import { QuestionRepository } from '../../src/database/repositories/QuestionRepository';
import { DataSource } from 'typeorm';

describe('QuizEngine', () => {
  let dataSource: DataSource;
  let engine: QuizEngine;
  let questionRepo: QuestionRepository;

  beforeEach(async () => {
    dataSource = await createTestDataSource();
    engine = new QuizEngine(dataSource);
    questionRepo = new QuestionRepository(dataSource);
  });

  afterEach(async () => {
    await dataSource.destroy();
  });

  async function seedQuestions() {
    await questionRepo.save(sampleQuestion);
    await questionRepo.save(sampleQuestion2);
    await questionRepo.save(sampleQuestion3);
  }

  it('throws InsufficientQuestionsError when no questions exist', async () => {
    await expect(engine.startSession(5)).rejects.toThrow('0 available');
  });

  it('starts a session and returns quiz state', async () => {
    await seedQuestions();
    const state = await engine.startSession(2);
    expect(state.sessionId).toBeDefined();
    expect(state.questions).toHaveLength(2);
    expect(state.numCorrect).toBe(0);
    expect(state.isFinished).toBe(false);
  });

  it('quiz questions have shuffled options', async () => {
    await seedQuestions();
    const state = await engine.startSession(1);
    expect(state.questions[0].shuffledOptions).toHaveLength(4);
    expect(typeof state.questions[0].correctShuffledIndex).toBe('number');
  });

  it('scores correct answer', async () => {
    await seedQuestions();
    const state = await engine.startSession(1);
    const q = state.questions[0];
    const letters = ['A', 'B', 'C', 'D'];
    const correctLetter = letters[q.correctShuffledIndex];

    const isCorrect = await engine.submitAnswer(state, 0, correctLetter);
    expect(isCorrect).toBe(true);
    expect(state.numCorrect).toBe(1);
  });

  it('does not score wrong answer', async () => {
    await seedQuestions();
    const state = await engine.startSession(1);
    const q = state.questions[0];
    // Find a wrong letter
    const letters = ['A', 'B', 'C', 'D'];
    const wrongLetter = letters.find((l) => letters.indexOf(l) !== q.correctShuffledIndex) ?? 'A';

    const isCorrect = await engine.submitAnswer(state, 0, wrongLetter);
    expect(isCorrect).toBe(false);
    expect(state.numCorrect).toBe(0);
  });

  it('throws InvalidAnswerError for invalid answer letter', async () => {
    await seedQuestions();
    const state = await engine.startSession(1);
    await expect(engine.submitAnswer(state, 0, 'Z')).rejects.toThrow('Invalid answer');
  });

  it('throws error for invalid question index', async () => {
    await seedQuestions();
    const state = await engine.startSession(1);
    await expect(engine.submitAnswer(state, 99, 'A')).rejects.toThrow();
  });

  it('finalizes session and persists results', async () => {
    await seedQuestions();
    const state = await engine.startSession(2);

    const letters = ['A', 'B', 'C', 'D'];
    for (let i = 0; i < state.questions.length; i++) {
      const correctLetter = letters[state.questions[i].correctShuffledIndex];
      await engine.submitAnswer(state, i, correctLetter);
    }

    const session = await engine.finalizeSession(state);
    expect(session.numCorrect).toBe(2);
    expect(session.endedAt).toBeDefined();
    expect(Number(session.percentageCorrect)).toBe(100);
    expect(state.isFinished).toBe(true);
  });

  it('calculates percentage correct after finalization', async () => {
    await seedQuestions();
    const state = await engine.startSession(2);

    const letters = ['A', 'B', 'C', 'D'];
    // Answer first correctly, second incorrectly
    const q0Correct = letters[state.questions[0].correctShuffledIndex];
    await engine.submitAnswer(state, 0, q0Correct);

    const wrongIdx = (state.questions[1].correctShuffledIndex + 1) % 4;
    await engine.submitAnswer(state, 1, letters[wrongIdx]);

    const session = await engine.finalizeSession(state);
    expect(session.numCorrect).toBe(1);
    expect(Number(session.percentageCorrect)).toBe(50);
  });

  it('getSessionById returns the session', async () => {
    await seedQuestions();
    const state = await engine.startSession(1);
    await engine.finalizeSession(state);
    const found = await engine.getSessionById(state.sessionId);
    expect(found).not.toBeNull();
    expect(found!.sessionId).toBe(state.sessionId);
  });

  it('throws SessionNotFoundError when finalizing nonexistent session', async () => {
    await seedQuestions();
    const state = await engine.startSession(1);
    state.sessionId = 'nonexistent-id';
    await expect(engine.finalizeSession(state)).rejects.toThrow('not found');
  });

  it('marks questions as used after submitting answer', async () => {
    await seedQuestions();
    const state = await engine.startSession(1);
    const questionId = state.questions[0].questionId;

    const qBefore = await questionRepo.findById(questionId);
    expect(qBefore!.timesUsed).toBe(0);

    await engine.submitAnswer(state, 0, 'A');

    const qAfter = await questionRepo.findById(questionId);
    expect(qAfter!.timesUsed).toBe(1);
  });
});
