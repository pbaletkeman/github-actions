import { DataSource, Repository } from 'typeorm';
import { QuizResponse } from '../../models/QuizResponse';

export class ResponseRepository {
  private readonly repo: Repository<QuizResponse>;

  constructor(dataSource: DataSource) {
    this.repo = dataSource.getRepository(QuizResponse);
  }

  async save(response: Partial<QuizResponse>): Promise<QuizResponse> {
    const entity = this.repo.create(response);
    return this.repo.save(entity);
  }

  async findBySession(sessionId: string): Promise<QuizResponse[]> {
    return this.repo.findBy({ sessionId });
  }

  async findBySessionWithQuestion(sessionId: string): Promise<QuizResponse[]> {
    return this.repo
      .createQueryBuilder('r')
      .leftJoinAndSelect('r.question', 'q')
      .where('r.sessionId = :sessionId', { sessionId })
      .getMany();
  }

  async countBySession(sessionId: string): Promise<number> {
    return this.repo.countBy({ sessionId });
  }

  async countCorrectBySession(sessionId: string): Promise<number> {
    return this.repo.countBy({ sessionId, isCorrect: 1 });
  }

  async deleteBySession(sessionId: string): Promise<void> {
    await this.repo.delete({ sessionId });
  }

  async deleteAll(): Promise<void> {
    await this.repo.clear();
  }
}
