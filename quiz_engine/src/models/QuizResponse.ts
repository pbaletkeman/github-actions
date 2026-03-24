import 'reflect-metadata';
import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  ManyToOne,
  JoinColumn,
  CreateDateColumn,
} from 'typeorm';
import { QuizSession } from './QuizSession';
import { Question } from './Question';

@Entity('quiz_responses')
export class QuizResponse {
  @PrimaryGeneratedColumn()
  id!: number;

  @Column({ type: 'varchar', length: 36 })
  sessionId!: string;

  @Column({ type: 'integer' })
  questionId!: number;

  @Column({ type: 'varchar', length: 1 })
  userAnswer!: string;

  @Column({ type: 'integer', default: 0 })
  isCorrect!: number;

  @Column({ type: 'integer', nullable: true })
  timeTakenSeconds?: number;

  @CreateDateColumn()
  answeredAt!: Date;

  @ManyToOne(() => QuizSession, (session) => session.responses, { lazy: true })
  @JoinColumn({ name: 'sessionId' })
  session!: Promise<QuizSession>;

  @ManyToOne(() => Question, (question) => question.responses, { lazy: true })
  @JoinColumn({ name: 'questionId' })
  question!: Promise<Question>;
}
