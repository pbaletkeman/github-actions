import {
  QuizException,
  QuestionNotFoundError,
  SessionNotFoundError,
  InsufficientQuestionsError,
  InvalidAnswerError,
  ParseError,
  DatabaseError,
} from '../../src/exceptions/QuizExceptions';

describe('QuizExceptions', () => {
  it('QuizException is an Error', () => {
    const err = new QuizException('test message');
    expect(err).toBeInstanceOf(Error);
    expect(err.message).toBe('test message');
    expect(err.name).toBe('QuizException');
  });

  it('QuestionNotFoundError includes id in message', () => {
    const err = new QuestionNotFoundError(42);
    expect(err).toBeInstanceOf(QuizException);
    expect(err.message).toContain('42');
    expect(err.name).toBe('QuestionNotFoundError');
  });

  it('SessionNotFoundError includes sessionId in message', () => {
    const err = new SessionNotFoundError('abc-123');
    expect(err).toBeInstanceOf(QuizException);
    expect(err.message).toContain('abc-123');
    expect(err.name).toBe('SessionNotFoundError');
  });

  it('InsufficientQuestionsError includes counts in message', () => {
    const err = new InsufficientQuestionsError(10, 3);
    expect(err).toBeInstanceOf(QuizException);
    expect(err.message).toContain('10');
    expect(err.message).toContain('3');
    expect(err.name).toBe('InsufficientQuestionsError');
  });

  it('InvalidAnswerError includes answer in message', () => {
    const err = new InvalidAnswerError('X');
    expect(err).toBeInstanceOf(QuizException);
    expect(err.message).toContain('X');
    expect(err.name).toBe('InvalidAnswerError');
  });

  it('ParseError includes message prefix', () => {
    const err = new ParseError('invalid format');
    expect(err).toBeInstanceOf(QuizException);
    expect(err.message).toContain('invalid format');
    expect(err.name).toBe('ParseError');
  });

  it('DatabaseError includes message prefix', () => {
    const err = new DatabaseError('connection failed');
    expect(err).toBeInstanceOf(QuizException);
    expect(err.message).toContain('connection failed');
    expect(err.name).toBe('DatabaseError');
  });
});
