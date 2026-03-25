import { createTestDataSource, sampleQuestion, sampleQuestion2, sampleQuestion3 } from '../setup';
import { QuizService } from '../../src/service/QuizService';
import { DataSource } from 'typeorm';

describe('QuizService', () => {
  let dataSource: DataSource;
  let service: QuizService;

  beforeEach(async () => {
    dataSource = await createTestDataSource();
    service = new QuizService(dataSource);
  });

  afterEach(async () => {
    await dataSource.destroy();
  });

  it('returns question count', async () => {
    expect(await service.getQuestionCount()).toBe(0);
  });

  it('imports a question', async () => {
    const q = await service.importQuestion(sampleQuestion, 'test.md');
    expect(q).not.toBeNull();
    expect(q!.questionText).toBe(sampleQuestion.questionText);
    expect(await service.getQuestionCount()).toBe(1);
  });

  it('skips duplicate question (returns null)', async () => {
    await service.importQuestion(sampleQuestion);
    const result = await service.importQuestion(sampleQuestion);
    expect(result).toBeNull();
    expect(await service.getQuestionCount()).toBe(1);
  });

  it('imports multiple questions', async () => {
    const { imported, skipped } = await service.importQuestions([
      sampleQuestion,
      sampleQuestion2,
      sampleQuestion,  // duplicate
    ]);
    expect(imported).toBe(2);
    expect(skipped).toBe(1);
  });

  it('returns random questions', async () => {
    await service.importQuestion(sampleQuestion);
    await service.importQuestion(sampleQuestion2);
    const questions = await service.getRandomQuestions(2);
    expect(questions).toHaveLength(2);
  });

  it('marks question as used', async () => {
    const q = await service.importQuestion(sampleQuestion);
    await service.markQuestionUsed(q!.id);
    // No assertion needed — just checking it doesn't throw
  });

  it('advanceCycleIfNeeded returns false when questions remain', async () => {
    await service.importQuestion(sampleQuestion);
    const advanced = await service.advanceCycleIfNeeded();
    expect(advanced).toBe(false);
  });

  it('getCurrentCycle returns 1 initially', async () => {
    const cycle = await service.getCurrentCycle();
    expect(cycle).toBe(1);
  });

  it('deleteAllQuestions clears questions', async () => {
    await service.importQuestion(sampleQuestion);
    await service.deleteAllQuestions();
    expect(await service.getQuestionCount()).toBe(0);
  });

  it('deleteAllSessions works without error', async () => {
    await service.deleteAllSessions();
  });

  it('deleteAllResponses works without error', async () => {
    await service.deleteAllResponses();
  });

  it('clearAll removes everything', async () => {
    await service.importQuestion(sampleQuestion);
    await service.clearAll();
    expect(await service.getQuestionCount()).toBe(0);
  });
});
