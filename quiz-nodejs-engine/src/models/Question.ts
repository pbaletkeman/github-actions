import 'reflect-metadata';
import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  CreateDateColumn,
  OneToMany,
} from 'typeorm';
import { QuizResponse } from './QuizResponse';

@Entity('questions')
export class Question {
  @PrimaryGeneratedColumn()
  id!: number;

  @Column({ type: 'varchar', length: 2000 })
  questionText!: string;

  @Column({ type: 'varchar', length: 500 })
  optionA!: string;

  @Column({ type: 'varchar', length: 500 })
  optionB!: string;

  @Column({ type: 'varchar', length: 500 })
  optionC!: string;

  @Column({ type: 'varchar', length: 500 })
  optionD!: string;

  @Column({ type: 'varchar', length: 500, nullable: true })
  optionE?: string;

  @Column({ type: 'varchar', length: 1 })
  correctAnswer!: string;

  @Column({ type: 'varchar', length: 2000, nullable: true })
  explanation?: string;

  @Column({ type: 'varchar', length: 100, nullable: true })
  section?: string;

  @Column({ type: 'varchar', length: 50, nullable: true })
  difficulty?: string;

  @Column({ type: 'varchar', length: 255, nullable: true })
  sourceFile?: string;

  @Column({ type: 'integer', default: 1 })
  usageCycle!: number;

  @Column({ type: 'integer', default: 0 })
  timesUsed!: number;

  @Column({ type: 'datetime', nullable: true })
  lastUsedAt?: Date;

  @CreateDateColumn()
  createdAt!: Date;

  @OneToMany(() => QuizResponse, (response) => response.question, { lazy: true })
  responses!: Promise<QuizResponse[]>;
}
