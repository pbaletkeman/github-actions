import { createTestDataSource, sampleQuestion, sampleQuestion2, sampleQuestion3 } from '../setup';
import { HistoryService } from '../../src/service/HistoryService';
import { SessionRepository } from '../../src/database/repositories/SessionRepository';
import { ResponseRepository } from '../../src/database/repositories/ResponseRepository';
import { QuestionRepository } from '../../src/database/repositories/QuestionRepository';
import { DataSource } from 'typeorm';
import { v4 as uuidv4 } from 'uuid';

describe('HistoryService', () => {
  let dataSource: DataSource;
  let historyService: HistoryService;
  let sessionRepo: SessionRepository;
  let responseRepo: ResponseRepository;
  let questionRepo: QuestionRepository;

  beforeEach(async () => {
    dataSource = await createTestDataSource();
    historyService = new HistoryService(dataSource);
    sessionRepo = new SessionRepository(dataSource);
    responseRepo = new ResponseRepository(dataSource);
    questionRepo = new QuestionRepository(dataSource);
  });

  afterEach(async () => {
    await dataSource.destroy();
  });

  async function seedSession(numCorrect = 8, numQuestions = 10): Promise<string> {
    const sessionId = uuidv4();
    await sessionRepo.save({
      sessionId,
      numQuestions,
      numCorrect,
      percentageCorrect: (numCorrect / numQuestions) * 100,
      endedAt: new Date(),
      timeTakenSeconds: 300,
    });
    return sessionId;
  }

  it('returns empty array when no sessions exist', async () => {
    const sessions = await historyService.getAllSessions();
    expect(sessions).toHaveLength(0);
  });

  it('returns recent sessions', async () => {
    await seedSession(8, 10);
    await seedSession(5, 10);
    const sessions = await historyService.getRecentSessions(5);
    expect(sessions).toHaveLength(2);
  });

  it('limits recent sessions', async () => {
    for (let i = 0; i < 5; i++) {
      await seedSession();
    }
    const sessions = await historyService.getRecentSessions(3);
    expect(sessions).toHaveLength(3);
  });

  it('includes summary fields in session summaries', async () => {
    await seedSession(8, 10);
    const sessions = await historyService.getAllSessions();
    expect(sessions[0]).toHaveProperty('sessionId');
    expect(sessions[0]).toHaveProperty('percentage');
    expect(sessions[0]).toHaveProperty('grade');
    expect(sessions[0]).toHaveProperty('passed');
    expect(sessions[0]).toHaveProperty('duration');
  });

  it('returns null for nonexistent session review', async () => {
    const review = await historyService.getSessionReview('nonexistent-id');
    expect(review).toBeNull();
  });

  it('returns session review with response details', async () => {
    const q = await questionRepo.save(sampleQuestion);
    const sessionId = await seedSession(1, 1);
    await responseRepo.save({
      sessionId,
      questionId: q.id,
      userAnswer: 'A',
      isCorrect: 1,
    });

    const review = await historyService.getSessionReview(sessionId);
    expect(review).not.toBeNull();
    expect(review!.responses).toHaveLength(1);
    expect(review!.responses[0].isCorrect).toBe(true);
    expect(review!.responses[0].userAnswer).toBe('A');
    expect(review!.responses[0].correctAnswer).toBe('A');
  });

  it('exports sessions as JSON', async () => {
    await seedSession();
    const json = await historyService.exportSessions('json');
    const parsed = JSON.parse(json);
    expect(Array.isArray(parsed)).toBe(true);
    expect(parsed).toHaveLength(1);
  });

  it('exports sessions as CSV', async () => {
    await seedSession();
    const csv = await historyService.exportSessions('csv');
    expect(typeof csv).toBe('string');
    const lines = csv.split('\n');
    expect(lines.length).toBeGreaterThanOrEqual(2); // header + 1 row
  });

  it('exports empty CSV for no sessions', async () => {
    const csv = await historyService.exportSessions('csv');
    expect(csv).toBe('');
  });

  it('returns correct session count', async () => {
    expect(await historyService.getSessionCount()).toBe(0);
    await seedSession();
    expect(await historyService.getSessionCount()).toBe(1);
  });

  it('calculates percentage correctly in summary', async () => {
    await seedSession(7, 10);
    const sessions = await historyService.getAllSessions();
    expect(sessions[0].percentage).toBe(70);
    expect(sessions[0].passed).toBe(true);
    expect(sessions[0].grade).toBe('C');
  });
});
