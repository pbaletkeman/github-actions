import { DataSource, Repository } from 'typeorm';
import { Question } from '../../models/Question';

export class QuestionRepository {
  private readonly repo: Repository<Question>;

  constructor(dataSource: DataSource) {
    this.repo = dataSource.getRepository(Question);
  }

  async save(question: Partial<Question>): Promise<Question> {
    const entity = this.repo.create(question);
    return this.repo.save(entity);
  }

  async findById(id: number): Promise<Question | null> {
    return this.repo.findOneBy({ id });
  }

  async findAll(): Promise<Question[]> {
    return this.repo.find();
  }

  async count(): Promise<number> {
    return this.repo.count();
  }

  async findBySection(section: string): Promise<Question[]> {
    return this.repo.findBy({ section });
  }

  /**
   * Get random questions for the current usage cycle.
   * Returns questions that have timesUsed < usageCycle (not yet used in this cycle).
   */
  async getRandomQuestions(limit: number): Promise<Question[]> {
    const currentCycle = await this.getCurrentCycle();
    const questions = await this.repo
      .createQueryBuilder('q')
      .where('q.timesUsed < q.usageCycle')
      .orderBy('RANDOM()')
      .take(limit)
      .getMany();

    if (questions.length < limit) {
      // Not enough questions in current cycle — advance cycle and retry
      await this.advanceCycleIfExhausted();
      return this.repo
        .createQueryBuilder('q')
        .where('q.timesUsed < q.usageCycle')
        .orderBy('RANDOM()')
        .take(limit)
        .getMany();
    }
    return questions;
  }

  /**
   * Mark a question as used in the current cycle.
   */
  async markUsed(id: number): Promise<void> {
    await this.repo
      .createQueryBuilder()
      .update(Question)
      .set({
        timesUsed: () => 'timesUsed + 1',
        lastUsedAt: new Date(),
      })
      .where('id = :id', { id })
      .execute();
  }

  /**
   * If all questions have been used in the current cycle, advance all to the next cycle.
   */
  async advanceCycleIfExhausted(): Promise<boolean> {
    const unusedCount = await this.repo
      .createQueryBuilder('q')
      .where('q.timesUsed < q.usageCycle')
      .getCount();

    if (unusedCount === 0) {
      const currentCycle = await this.getCurrentCycle();
      await this.repo
        .createQueryBuilder()
        .update(Question)
        .set({ usageCycle: currentCycle + 1 })
        .execute();
      return true;
    }
    return false;
  }

  async getCurrentCycle(): Promise<number> {
    const result = await this.repo
      .createQueryBuilder('q')
      .select('MAX(q.usageCycle)', 'maxCycle')
      .getRawOne<{ maxCycle: number }>();
    return result?.maxCycle ?? 1;
  }

  async existsByText(questionText: string): Promise<boolean> {
    const count = await this.repo.countBy({ questionText });
    return count > 0;
  }

  async deleteAll(): Promise<void> {
    await this.repo.clear();
  }
}
