import 'reflect-metadata';
import { DataSource } from 'typeorm';
import { Question } from '../models/Question';
import { QuizSession } from '../models/QuizSession';
import { QuizResponse } from '../models/QuizResponse';

const dbPath = process.env.DATABASE_PATH ?? './quiz_engine.db';

export const AppDataSource = new DataSource({
  type: 'sqlite',
  database: dbPath,
  synchronize: false,
  logging: process.env.NODE_ENV === 'development',
  entities: [Question, QuizSession, QuizResponse],
  migrations: [__dirname + '/migrations/*.{ts,js}'],
  migrationsRun: false,
});

export async function initializeDatabase(): Promise<void> {
  if (!AppDataSource.isInitialized) {
    await AppDataSource.initialize();
  }
  await AppDataSource.runMigrations();
}
