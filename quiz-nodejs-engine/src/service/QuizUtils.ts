import { QuizSession } from '../models/QuizSession';

export class QuizUtils {
  /**
   * Calculate percentage correct (0-100).
   */
  static calculatePercentage(numCorrect: number, numQuestions: number): number {
    if (numQuestions === 0) return 0;
    return Math.round((numCorrect / numQuestions) * 100 * 100) / 100;
  }

  /**
   * Get a pass/fail grade based on percentage (passing = 70%).
   */
  static getGrade(percentage: number): string {
    if (percentage >= 90) return 'A';
    if (percentage >= 80) return 'B';
    if (percentage >= 70) return 'C';
    if (percentage >= 60) return 'D';
    return 'F';
  }

  /**
   * Determine if a score is passing (>= 70%).
   */
  static isPassing(percentage: number): boolean {
    return percentage >= 70;
  }

  /**
   * Format elapsed time in seconds to a human-readable string.
   */
  static formatDuration(seconds: number): string {
    if (seconds < 60) return `${seconds}s`;
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    if (mins < 60) return `${mins}m ${secs}s`;
    const hours = Math.floor(mins / 60);
    const remainMins = mins % 60;
    return `${hours}h ${remainMins}m ${secs}s`;
  }

  /**
   * Format a date for display.
   */
  static formatDate(date: Date): string {
    return date.toLocaleString();
  }

  /**
   * Validate that an answer string is A-E.
   */
  static isValidAnswer(answer: string): boolean {
    return /^[A-Ea-e]$/.test(answer);
  }

  /**
   * Build a summary object for a session.
   */
  static buildSessionSummary(session: QuizSession): {
    sessionId: string;
    startedAt: string;
    endedAt: string;
    numQuestions: number;
    numCorrect: number;
    percentage: number;
    grade: string;
    passed: boolean;
    duration: string;
  } {
    const percentage = Number(session.percentageCorrect);
    return {
      sessionId: session.sessionId,
      startedAt: QuizUtils.formatDate(session.startedAt),
      endedAt: session.endedAt ? QuizUtils.formatDate(session.endedAt) : 'In progress',
      numQuestions: session.numQuestions,
      numCorrect: session.numCorrect,
      percentage,
      grade: QuizUtils.getGrade(percentage),
      passed: QuizUtils.isPassing(percentage),
      duration: session.timeTakenSeconds
        ? QuizUtils.formatDuration(session.timeTakenSeconds)
        : 'N/A',
    };
  }
}
