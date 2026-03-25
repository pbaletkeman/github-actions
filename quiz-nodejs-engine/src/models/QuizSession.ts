import 'reflect-metadata';
import {
  Entity,
  PrimaryColumn,
  Column,
  CreateDateColumn,
  OneToMany,
} from 'typeorm';
import { QuizResponse } from './QuizResponse';

@Entity('quiz_sessions')
export class QuizSession {
  @PrimaryColumn({ type: 'varchar', length: 36 })
  sessionId!: string;

  @CreateDateColumn()
  startedAt!: Date;

  @Column({ type: 'datetime', nullable: true })
  endedAt?: Date;

  @Column({ type: 'integer' })
  numQuestions!: number;

  @Column({ type: 'integer', default: 0 })
  numCorrect!: number;

  @Column({ type: 'decimal', precision: 5, scale: 2, default: 0 })
  percentageCorrect!: number;

  @Column({ type: 'integer', nullable: true })
  timeTakenSeconds?: number;

  @OneToMany(() => QuizResponse, (response) => response.session, { lazy: true })
  responses!: Promise<QuizResponse[]>;
}
