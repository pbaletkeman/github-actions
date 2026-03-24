import { createTestDataSource, sampleQuestion } from '../setup';
import { ResponseRepository } from '../../src/database/repositories/ResponseRepository';
import { SessionRepository } from '../../src/database/repositories/SessionRepository';
import { QuestionRepository } from '../../src/database/repositories/QuestionRepository';
import { DataSource } from 'typeorm';
import { v4 as uuidv4 } from 'uuid';

describe('ResponseRepository', () => {
  let dataSource: DataSource;
  let responseRepo: ResponseRepository;
  let sessionRepo: SessionRepository;
  let questionRepo: QuestionRepository;

  beforeEach(async () => {
    dataSource = await createTestDataSource();
    responseRepo = new ResponseRepository(dataSource);
    sessionRepo = new SessionRepository(dataSource);
    questionRepo = new QuestionRepository(dataSource);
  });

  afterEach(async () => {
    await dataSource.destroy();
  });

  async function setupSessionAndQuestion() {
    const sessionId = uuidv4();
    await sessionRepo.save({ sessionId, numQuestions: 1 });
    const q = await questionRepo.save(sampleQuestion);
    return { sessionId, questionId: q.id };
  }

  it('saves a response', async () => {
    const { sessionId, questionId } = await setupSessionAndQuestion();
    const resp = await responseRepo.save({
      sessionId,
      questionId,
      userAnswer: 'A',
      isCorrect: 1,
    });
    expect(resp.id).toBeDefined();
    expect(resp.sessionId).toBe(sessionId);
  });

  it('finds responses by session', async () => {
    const { sessionId, questionId } = await setupSessionAndQuestion();
    await responseRepo.save({ sessionId, questionId, userAnswer: 'A', isCorrect: 1 });
    const responses = await responseRepo.findBySession(sessionId);
    expect(responses).toHaveLength(1);
    expect(responses[0].isCorrect).toBe(1);
  });

  it('finds responses with question join', async () => {
    const { sessionId, questionId } = await setupSessionAndQuestion();
    await responseRepo.save({ sessionId, questionId, userAnswer: 'A', isCorrect: 1 });
    const responses = await responseRepo.findBySessionWithQuestion(sessionId);
    expect(responses).toHaveLength(1);
    const q = await responses[0].question;
    expect(q.questionText).toBe(sampleQuestion.questionText);
  });

  it('counts responses by session', async () => {
    const { sessionId, questionId } = await setupSessionAndQuestion();
    await responseRepo.save({ sessionId, questionId, userAnswer: 'A', isCorrect: 1 });
    expect(await responseRepo.countBySession(sessionId)).toBe(1);
  });

  it('counts correct responses by session', async () => {
    const { sessionId, questionId } = await setupSessionAndQuestion();
    await responseRepo.save({ sessionId, questionId, userAnswer: 'A', isCorrect: 1 });
    expect(await responseRepo.countCorrectBySession(sessionId)).toBe(1);
  });

  it('deletes responses by session', async () => {
    const { sessionId, questionId } = await setupSessionAndQuestion();
    await responseRepo.save({ sessionId, questionId, userAnswer: 'A', isCorrect: 1 });
    await responseRepo.deleteBySession(sessionId);
    expect(await responseRepo.countBySession(sessionId)).toBe(0);
  });

  it('deletes all responses', async () => {
    const { sessionId, questionId } = await setupSessionAndQuestion();
    await responseRepo.save({ sessionId, questionId, userAnswer: 'A', isCorrect: 1 });
    await responseRepo.deleteAll();
    expect(await responseRepo.countBySession(sessionId)).toBe(0);
  });
});
