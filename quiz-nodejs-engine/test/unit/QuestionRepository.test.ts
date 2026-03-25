import { createTestDataSource, sampleQuestion, sampleQuestion2 } from '../setup';
import { QuestionRepository } from '../../src/database/repositories/QuestionRepository';
import { DataSource } from 'typeorm';

describe('QuestionRepository', () => {
  let dataSource: DataSource;
  let repo: QuestionRepository;

  beforeEach(async () => {
    dataSource = await createTestDataSource();
    repo = new QuestionRepository(dataSource);
  });

  afterEach(async () => {
    await dataSource.destroy();
  });

  it('inserts a question and retrieves it by id', async () => {
    const q = await repo.save(sampleQuestion);
    expect(q.id).toBeDefined();
    expect(q.id).toBeGreaterThan(0);

    const found = await repo.findById(q.id);
    expect(found).not.toBeNull();
    expect(found!.questionText).toBe(sampleQuestion.questionText);
  });

  it('retrieves all questions', async () => {
    await repo.save(sampleQuestion);
    await repo.save(sampleQuestion2);
    const all = await repo.findAll();
    expect(all.length).toBe(2);
  });

  it('counts questions correctly', async () => {
    expect(await repo.count()).toBe(0);
    await repo.save(sampleQuestion);
    expect(await repo.count()).toBe(1);
  });

  it('finds questions by section', async () => {
    await repo.save(sampleQuestion); // section: DevOps
    await repo.save(sampleQuestion2); // section: Syntax
    const devops = await repo.findBySection('DevOps');
    expect(devops).toHaveLength(1);
    expect(devops[0].section).toBe('DevOps');
  });

  it('detects existing question text', async () => {
    await repo.save(sampleQuestion);
    expect(await repo.existsByText(sampleQuestion.questionText)).toBe(true);
    expect(await repo.existsByText('nonexistent question')).toBe(false);
  });

  it('marks a question as used', async () => {
    const q = await repo.save(sampleQuestion);
    expect(q.timesUsed).toBe(0);
    await repo.markUsed(q.id);
    const updated = await repo.findById(q.id);
    expect(updated!.timesUsed).toBe(1);
    expect(updated!.lastUsedAt).not.toBeNull();
  });

  it('returns questions within current cycle', async () => {
    const q = await repo.save({ ...sampleQuestion, usageCycle: 1, timesUsed: 0 });
    const questions = await repo.getRandomQuestions(1);
    expect(questions).toHaveLength(1);
    expect(questions[0].id).toBe(q.id);
  });

  it('excludes questions already used in current cycle', async () => {
    // Save a question and mark it as fully used in this cycle
    await repo.save({ ...sampleQuestion, usageCycle: 1, timesUsed: 1 });
    await repo.save({ ...sampleQuestion2, usageCycle: 1, timesUsed: 0 });

    const questions = await repo.getRandomQuestions(1);
    expect(questions).toHaveLength(1);
    expect(questions[0].questionText).toBe(sampleQuestion2.questionText);
  });

  it('advances cycle when all questions are used', async () => {
    await repo.save({ ...sampleQuestion, usageCycle: 1, timesUsed: 1 });
    const advanced = await repo.advanceCycleIfExhausted();
    expect(advanced).toBe(true);
    const cycle = await repo.getCurrentCycle();
    expect(cycle).toBe(2);
  });

  it('does not advance cycle when questions remain unused', async () => {
    await repo.save({ ...sampleQuestion, usageCycle: 1, timesUsed: 0 });
    const advanced = await repo.advanceCycleIfExhausted();
    expect(advanced).toBe(false);
    const cycle = await repo.getCurrentCycle();
    expect(cycle).toBe(1);
  });

  it('returns cycle 1 when no questions exist', async () => {
    const cycle = await repo.getCurrentCycle();
    expect(cycle).toBe(1);
  });

  it('auto-advances cycle and retries when insufficient questions in current cycle', async () => {
    // All questions used in cycle 1 → will advance to cycle 2 and return 1 question
    await repo.save({ ...sampleQuestion, usageCycle: 1, timesUsed: 1 });
    const questions = await repo.getRandomQuestions(1);
    expect(questions).toHaveLength(1);
    const cycle = await repo.getCurrentCycle();
    expect(cycle).toBe(2);
  });

  it('deletes all questions', async () => {
    await repo.save(sampleQuestion);
    await repo.deleteAll();
    expect(await repo.count()).toBe(0);
  });

  it('findById returns null for nonexistent id', async () => {
    const result = await repo.findById(999999);
    expect(result).toBeNull();
  });
});
