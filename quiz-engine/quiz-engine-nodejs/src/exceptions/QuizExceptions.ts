export class QuizException extends Error {
  constructor(message: string) {
    super(message);
    this.name = 'QuizException';
  }
}

export class QuestionNotFoundError extends QuizException {
  constructor(id: number) {
    super(`Question with id ${id} not found`);
    this.name = 'QuestionNotFoundError';
  }
}

export class SessionNotFoundError extends QuizException {
  constructor(sessionId: string) {
    super(`Quiz session '${sessionId}' not found`);
    this.name = 'SessionNotFoundError';
  }
}

export class InsufficientQuestionsError extends QuizException {
  constructor(requested: number, available: number) {
    super(`Requested ${requested} questions but only ${available} available`);
    this.name = 'InsufficientQuestionsError';
  }
}

export class InvalidAnswerError extends QuizException {
  constructor(answer: string) {
    super(`Invalid answer: '${answer}'. Must be A, B, C, D, or E`);
    this.name = 'InvalidAnswerError';
  }
}

export class ParseError extends QuizException {
  constructor(message: string) {
    super(`Parse error: ${message}`);
    this.name = 'ParseError';
  }
}

export class DatabaseError extends QuizException {
  constructor(message: string) {
    super(`Database error: ${message}`);
    this.name = 'DatabaseError';
  }
}
