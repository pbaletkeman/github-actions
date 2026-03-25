import { DataSource } from 'typeorm';
import { QuizSession } from '../models/QuizSession';
import { QuizResponse } from '../models/QuizResponse';
import { SessionRepository } from '../database/repositories/SessionRepository';
import { ResponseRepository } from '../database/repositories/ResponseRepository';
import { QuizUtils } from './QuizUtils';

export interface SessionSummary {
  sessionId: string;
  startedAt: string;
  endedAt: string;
  numQuestions: number;
  numCorrect: number;
  percentage: number;
  grade: string;
  passed: boolean;
  duration: string;
}

export interface SessionReview {
  summary: SessionSummary;
  responses: ResponseReview[];
}

export interface ResponseReview {
  questionNumber: number;
  questionText: string;
  userAnswer: string;
  correctAnswer: string;
  isCorrect: boolean;
  explanation?: string;
}

export class HistoryService {
  private readonly sessionRepo: SessionRepository;
  private readonly responseRepo: ResponseRepository;

  constructor(dataSource: DataSource) {
    this.sessionRepo = new SessionRepository(dataSource);
    this.responseRepo = new ResponseRepository(dataSource);
  }

  async getRecentSessions(limit = 10): Promise<SessionSummary[]> {
    const sessions = await this.sessionRepo.findRecent(limit);
    return sessions.map((s) => QuizUtils.buildSessionSummary(s));
  }

  async getAllSessions(): Promise<SessionSummary[]> {
    const sessions = await this.sessionRepo.findAll();
    return sessions.map((s) => QuizUtils.buildSessionSummary(s));
  }

  async getSessionReview(sessionId: string): Promise<SessionReview | null> {
    const session = await this.sessionRepo.findById(sessionId);
    if (!session) return null;

    const responses = await this.responseRepo.findBySessionWithQuestion(sessionId);

    const responseReviews: ResponseReview[] = await Promise.all(
      responses.map(async (r, idx) => {
        const question = await r.question;
        return {
          questionNumber: idx + 1,
          questionText: question.questionText,
          userAnswer: r.userAnswer,
          correctAnswer: question.correctAnswer,
          isCorrect: r.isCorrect === 1,
          explanation: question.explanation,
        };
      }),
    );

    return {
      summary: QuizUtils.buildSessionSummary(session),
      responses: responseReviews,
    };
  }

  async exportSessions(format: 'json' | 'csv'): Promise<string> {
    const sessions = await this.getAllSessions();

    if (format === 'json') {
      return JSON.stringify(sessions, null, 2);
    }

    // CSV format
    if (sessions.length === 0) return '';

    const headers = Object.keys(sessions[0]).join(',');
    const rows = sessions.map((s) =>
      Object.values(s)
        .map((v) => `"${String(v).replace(/"/g, '""')}"`)
        .join(','),
    );
    return [headers, ...rows].join('\n');
  }

  async getSessionCount(): Promise<number> {
    return this.sessionRepo.count();
  }
}
