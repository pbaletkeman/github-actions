import { DataSource, Repository } from 'typeorm';
import { QuizSession } from '../../models/QuizSession';

export class SessionRepository {
  private readonly repo: Repository<QuizSession>;

  constructor(dataSource: DataSource) {
    this.repo = dataSource.getRepository(QuizSession);
  }

  async save(session: Partial<QuizSession>): Promise<QuizSession> {
    const entity = this.repo.create(session);
    return this.repo.save(entity);
  }

  async update(sessionId: string, data: Partial<QuizSession>): Promise<void> {
    await this.repo.update({ sessionId }, data);
  }

  async findById(sessionId: string): Promise<QuizSession | null> {
    return this.repo.findOneBy({ sessionId });
  }

  async findAll(): Promise<QuizSession[]> {
    return this.repo.find({ order: { startedAt: 'DESC' } });
  }

  async findRecent(limit: number): Promise<QuizSession[]> {
    return this.repo.find({ order: { startedAt: 'DESC' }, take: limit });
  }

  async count(): Promise<number> {
    return this.repo.count();
  }

  async deleteAll(): Promise<void> {
    await this.repo.clear();
  }

  async deleteById(sessionId: string): Promise<void> {
    await this.repo.delete({ sessionId });
  }
}
