import * as fs from 'fs';
import * as path from 'path';
import * as os from 'os';
import { createTestDataSource } from '../setup';
import { QuizEngine, QuizState } from '../../src/service/QuizEngine';
import { QuizService } from '../../src/service/QuizService';
import { ImportService } from '../../src/service/ImportService';
import { HistoryService } from '../../src/service/HistoryService';
import { QuestionRepository } from '../../src/database/repositories/QuestionRepository';
import { DataSource } from 'typeorm';

const SAMPLE_MD = `
## Q1
> What does CI stand for?
- A) Continuous Integration
- B) Code Import
- C) Compiler Install
- D) Content Index
**Answer: A**

## Q2
> What does CD stand for?
- A) Code Deploy
- B) Continuous Delivery
- C) Content Delivery
- D) Code Distribution
**Answer: B**

## Q3
> What is a GitHub Action?
- A) A reusable workflow unit
- B) A type of container
- C) A CI server
- D) A code review tool
**Answer: A**
`;

describe('Quiz Workflow Integration', () => {
  let dataSource: DataSource;
  let quizEngine: QuizEngine;
  let quizService: QuizService;
  let importService: ImportService;
  let historyService: HistoryService;
  let questionRepo: QuestionRepository;
  let tempFile: string;
  let completedSessionId: string;

  beforeAll(async () => {
    dataSource = await createTestDataSource();
    quizEngine = new QuizEngine(dataSource);
    quizService = new QuizService(dataSource);
    importService = new ImportService(dataSource);
    historyService = new HistoryService(dataSource);
    questionRepo = new QuestionRepository(dataSource);

    // Create a temp markdown file
    tempFile = path.join(os.tmpdir(), `integration-test-${Date.now()}.md`);
    fs.writeFileSync(tempFile, SAMPLE_MD, 'utf-8');
  });

  afterAll(async () => {
    if (fs.existsSync(tempFile)) {
      fs.unlinkSync(tempFile);
    }
    await dataSource.destroy();
  });

  it('Step 1: Import questions from markdown', async () => {
    const result = await importService.importFile(tempFile);
    expect(result.errors).toHaveLength(0);
    expect(result.imported).toBe(3);
    expect(result.skipped).toBe(0);
  });

  it('Step 2: Skips duplicate imports', async () => {
    const result = await importService.importFile(tempFile);
    expect(result.imported).toBe(0);
    expect(result.skipped).toBe(3);
  });

  it('Step 3: Quiz engine loads questions', async () => {
    const state = await quizEngine.startSession(2);
    expect(state.questions).toHaveLength(2);
    expect(state.sessionId).toBeTruthy();
    // Finalize without answering to prevent unfinished sessions affecting later tests
    await quizEngine.finalizeSession(state);
  });

  it('Step 4: Full quiz workflow — start, answer, finalize', async () => {
    const state = await quizEngine.startSession(3);
    expect(state.questions).toHaveLength(3);

    const letters = ['A', 'B', 'C', 'D'];
    let correctCount = 0;
    for (let i = 0; i < state.questions.length; i++) {
      const correctLetter = letters[state.questions[i].correctShuffledIndex];
      const isCorrect = await quizEngine.submitAnswer(state, i, correctLetter);
      expect(isCorrect).toBe(true);
      correctCount++;
    }

    const session = await quizEngine.finalizeSession(state);
    completedSessionId = session.sessionId;
    expect(session.numCorrect).toBe(correctCount);
    expect(Number(session.percentageCorrect)).toBe(100);
    expect(session.endedAt).toBeDefined();
  });

  it('Step 5: History service shows completed session by ID', async () => {
    const review = await historyService.getSessionReview(completedSessionId);
    expect(review).not.toBeNull();
    expect(review!.summary.passed).toBe(true);
    expect(review!.summary.percentage).toBe(100);
  });

  it('Step 6: Session review includes answer details', async () => {
    const review = await historyService.getSessionReview(completedSessionId);
    expect(review).not.toBeNull();
    expect(review!.responses.length).toBe(3);
    expect(review!.responses[0].isCorrect).toBe(true);
  });

  it('Step 7: Cycle mechanics — questions track usage', async () => {
    const count = await questionRepo.count();
    expect(count).toBe(3);

    const cycle = await questionRepo.getCurrentCycle();
    // After answering all 3 questions in steps 3 and 4, cycle should have advanced
    expect(cycle).toBeGreaterThanOrEqual(1);
  });

  it('Step 8: History service returns all sessions', async () => {
    const sessions = await historyService.getAllSessions();
    // Two sessions were finalized in steps 3 and 4
    expect(sessions.length).toBeGreaterThanOrEqual(2);
  });

  it('Step 9: Clear all data', async () => {
    await quizService.clearAll();
    expect(await questionRepo.count()).toBe(0);
    expect(await historyService.getSessionCount()).toBe(0);
  });

  it('Step 10: Import from directory', async () => {
    const tempDir = path.join(os.tmpdir(), `quiz-dir-${Date.now()}`);
    fs.mkdirSync(tempDir);
    fs.writeFileSync(path.join(tempDir, 'test1.md'), SAMPLE_MD);
    fs.writeFileSync(path.join(tempDir, 'test2.md'), SAMPLE_MD); // will be duplicates

    const results = await importService.importDirectory(tempDir);
    expect(results.length).toBe(2);

    const totalImported = results.reduce((sum, r) => sum + r.imported, 0);
    const totalSkipped = results.reduce((sum, r) => sum + r.skipped, 0);
    expect(totalImported).toBe(3); // First file imports all 3
    expect(totalSkipped).toBe(3); // Second file all duplicates

    // Cleanup
    fs.rmSync(tempDir, { recursive: true });
  });

  it('Step 11: Import from nonexistent directory', async () => {
    const results = await importService.importDirectory('/nonexistent/dir');
    expect(results).toHaveLength(1);
    expect(results[0].errors.length).toBeGreaterThan(0);
  });

  it('Step 12: Export sessions as JSON', async () => {
    const json = await historyService.exportSessions('json');
    // After clearing, should be empty array
    expect(JSON.parse(json)).toEqual([]);
  });

  it('Step 13: Export sessions as CSV', async () => {
    const csv = await historyService.exportSessions('csv');
    expect(csv).toBe(''); // no sessions after clear
  });
});
