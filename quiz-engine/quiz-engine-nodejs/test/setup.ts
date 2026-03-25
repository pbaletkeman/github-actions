import 'reflect-metadata';
import { DataSource } from 'typeorm';
import { Question } from '../src/models/Question';
import { QuizSession } from '../src/models/QuizSession';
import { QuizResponse } from '../src/models/QuizResponse';

/**
 * Create an in-memory SQLite DataSource for testing.
 */
export async function createTestDataSource(): Promise<DataSource> {
  const ds = new DataSource({
    type: 'sqlite',
    database: ':memory:',
    synchronize: true,
    logging: false,
    entities: [Question, QuizSession, QuizResponse],
  });
  await ds.initialize();
  return ds;
}

export const sampleQuestion = {
  questionText: 'What is CI/CD?',
  optionA: 'Continuous Integration / Continuous Delivery',
  optionB: 'Code Import / Code Deployment',
  optionC: 'Compiler Install / Compiler Deploy',
  optionD: 'Content Index / Content Delivery',
  correctAnswer: 'A',
  difficulty: 'Easy',
  section: 'DevOps',
};

export const sampleQuestion2 = {
  questionText: 'What does YAML stand for?',
  optionA: "YAML Ain't Markup Language",
  optionB: 'Yet Another Markup Language',
  optionC: 'Your Awesome Markup Language',
  optionD: 'Yes A Markup Language',
  correctAnswer: 'A',
  difficulty: 'Easy',
  section: 'Syntax',
};

export const sampleQuestion3 = {
  questionText: 'Which GitHub Actions trigger fires on a push?',
  optionA: 'on: push',
  optionB: 'on: pull_request',
  optionC: 'on: schedule',
  optionD: 'on: workflow_dispatch',
  correctAnswer: 'A',
  difficulty: 'Medium',
  section: 'Triggers',
};
