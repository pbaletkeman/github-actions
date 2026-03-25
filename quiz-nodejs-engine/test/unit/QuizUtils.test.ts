import { QuizUtils } from '../../src/service/QuizUtils';
import { QuizSession } from '../../src/models/QuizSession';

function makeSession(overrides: Partial<QuizSession> = {}): QuizSession {
  const s = new QuizSession();
  s.sessionId = 'test-session-id';
  s.numQuestions = 10;
  s.numCorrect = 8;
  s.percentageCorrect = 80;
  s.startedAt = new Date('2024-01-01T10:00:00Z');
  s.endedAt = new Date('2024-01-01T10:05:00Z');
  s.timeTakenSeconds = 300;
  Object.assign(s, overrides);
  return s;
}

describe('QuizUtils', () => {
  describe('calculatePercentage', () => {
    it('returns 0 for 0 questions', () => {
      expect(QuizUtils.calculatePercentage(0, 0)).toBe(0);
    });

    it('calculates 100% for all correct', () => {
      expect(QuizUtils.calculatePercentage(10, 10)).toBe(100);
    });

    it('calculates 50%', () => {
      expect(QuizUtils.calculatePercentage(5, 10)).toBe(50);
    });

    it('rounds to 2 decimal places', () => {
      expect(QuizUtils.calculatePercentage(1, 3)).toBe(33.33);
    });
  });

  describe('getGrade', () => {
    it('returns A for >= 90%', () => {
      expect(QuizUtils.getGrade(90)).toBe('A');
      expect(QuizUtils.getGrade(100)).toBe('A');
      expect(QuizUtils.getGrade(95)).toBe('A');
    });

    it('returns B for >= 80% and < 90%', () => {
      expect(QuizUtils.getGrade(80)).toBe('B');
      expect(QuizUtils.getGrade(89)).toBe('B');
    });

    it('returns C for >= 70% and < 80%', () => {
      expect(QuizUtils.getGrade(70)).toBe('C');
      expect(QuizUtils.getGrade(79)).toBe('C');
    });

    it('returns D for >= 60% and < 70%', () => {
      expect(QuizUtils.getGrade(60)).toBe('D');
      expect(QuizUtils.getGrade(69)).toBe('D');
    });

    it('returns F for < 60%', () => {
      expect(QuizUtils.getGrade(59)).toBe('F');
      expect(QuizUtils.getGrade(0)).toBe('F');
    });
  });

  describe('isPassing', () => {
    it('returns true for >= 70%', () => {
      expect(QuizUtils.isPassing(70)).toBe(true);
      expect(QuizUtils.isPassing(100)).toBe(true);
    });

    it('returns false for < 70%', () => {
      expect(QuizUtils.isPassing(69)).toBe(false);
      expect(QuizUtils.isPassing(0)).toBe(false);
    });
  });

  describe('formatDuration', () => {
    it('formats seconds only', () => {
      expect(QuizUtils.formatDuration(45)).toBe('45s');
      expect(QuizUtils.formatDuration(0)).toBe('0s');
    });

    it('formats minutes and seconds', () => {
      expect(QuizUtils.formatDuration(90)).toBe('1m 30s');
      expect(QuizUtils.formatDuration(60)).toBe('1m 0s');
    });

    it('formats hours, minutes, and seconds', () => {
      expect(QuizUtils.formatDuration(3661)).toBe('1h 1m 1s');
      expect(QuizUtils.formatDuration(7200)).toBe('2h 0m 0s');
    });
  });

  describe('formatDate', () => {
    it('returns a non-empty string', () => {
      const result = QuizUtils.formatDate(new Date('2024-01-01T10:00:00Z'));
      expect(typeof result).toBe('string');
      expect(result.length).toBeGreaterThan(0);
    });
  });

  describe('isValidAnswer', () => {
    it('accepts A-E uppercase', () => {
      ['A', 'B', 'C', 'D', 'E'].forEach((l) => {
        expect(QuizUtils.isValidAnswer(l)).toBe(true);
      });
    });

    it('accepts a-e lowercase', () => {
      ['a', 'b', 'c', 'd', 'e'].forEach((l) => {
        expect(QuizUtils.isValidAnswer(l)).toBe(true);
      });
    });

    it('rejects invalid letters', () => {
      ['F', 'Z', '1', '', 'AB'].forEach((l) => {
        expect(QuizUtils.isValidAnswer(l)).toBe(false);
      });
    });
  });

  describe('buildSessionSummary', () => {
    it('builds correct summary with all fields', () => {
      const session = makeSession();
      const summary = QuizUtils.buildSessionSummary(session);
      expect(summary.sessionId).toBe('test-session-id');
      expect(summary.numQuestions).toBe(10);
      expect(summary.numCorrect).toBe(8);
      expect(summary.percentage).toBe(80);
      expect(summary.grade).toBe('B');
      expect(summary.passed).toBe(true);
      expect(typeof summary.startedAt).toBe('string');
      expect(summary.duration).toBe('5m 0s');
    });

    it('shows "In progress" for sessions without endedAt', () => {
      const session = makeSession({ endedAt: undefined, timeTakenSeconds: undefined });
      const summary = QuizUtils.buildSessionSummary(session);
      expect(summary.endedAt).toBe('In progress');
      expect(summary.duration).toBe('N/A');
    });
  });
});
