import { DataSource } from 'typeorm';
import { Question } from '../models/Question';
import { QuestionRepository } from '../database/repositories/QuestionRepository';
import { SessionRepository } from '../database/repositories/SessionRepository';
import { ResponseRepository } from '../database/repositories/ResponseRepository';
import { ParsedQuestion } from './MarkdownParser';

export class QuizService {
  private readonly questionRepo: QuestionRepository;
  private readonly sessionRepo: SessionRepository;
  private readonly responseRepo: ResponseRepository;

  constructor(dataSource: DataSource) {
    this.questionRepo = new QuestionRepository(dataSource);
    this.sessionRepo = new SessionRepository(dataSource);
    this.responseRepo = new ResponseRepository(dataSource);
  }

  async getRandomQuestions(count: number): Promise<Question[]> {
    return this.questionRepo.getRandomQuestions(count);
  }

  async getQuestionCount(): Promise<number> {
    return this.questionRepo.count();
  }

  async importQuestion(parsed: ParsedQuestion, sourceFile?: string): Promise<Question | null> {
    // Skip duplicates
    const exists = await this.questionRepo.existsByText(parsed.questionText);
    if (exists) return null;

    return this.questionRepo.save({
      ...parsed,
      sourceFile,
      usageCycle: 1,
      timesUsed: 0,
    });
  }

  async importQuestions(
    parsedList: ParsedQuestion[],
    sourceFile?: string,
  ): Promise<{ imported: number; skipped: number }> {
    let imported = 0;
    let skipped = 0;

    for (const parsed of parsedList) {
      const result = await this.importQuestion(parsed, sourceFile);
      if (result) {
        imported++;
      } else {
        skipped++;
      }
    }

    return { imported, skipped };
  }

  async markQuestionUsed(id: number): Promise<void> {
    await this.questionRepo.markUsed(id);
  }

  async advanceCycleIfNeeded(): Promise<boolean> {
    return this.questionRepo.advanceCycleIfExhausted();
  }

  async getCurrentCycle(): Promise<number> {
    return this.questionRepo.getCurrentCycle();
  }

  async deleteAllQuestions(): Promise<void> {
    await this.questionRepo.deleteAll();
  }

  async deleteAllSessions(): Promise<void> {
    await this.sessionRepo.deleteAll();
  }

  async deleteAllResponses(): Promise<void> {
    await this.responseRepo.deleteAll();
  }

  async clearAll(): Promise<void> {
    await this.responseRepo.deleteAll();
    await this.sessionRepo.deleteAll();
    await this.questionRepo.deleteAll();
  }
}
