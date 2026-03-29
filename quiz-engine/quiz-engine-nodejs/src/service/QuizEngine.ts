import { DataSource } from 'typeorm';
import { v4 as uuidv4 } from 'uuid';
import { Question } from '../models/Question';
import { QuizSession } from '../models/QuizSession';
import { QuizResponse } from '../models/QuizResponse';
import { QuestionRepository } from '../database/repositories/QuestionRepository';
import { SessionRepository } from '../database/repositories/SessionRepository';
import { ResponseRepository } from '../database/repositories/ResponseRepository';
import { shuffleAnswers, ShuffleResult } from './AnswerShuffler';
import { QuizUtils } from './QuizUtils';
import {
  InsufficientQuestionsError,
  InvalidAnswerError,
  SessionNotFoundError,
} from '../exceptions/QuizExceptions';

export interface QuizQuestion {
  index: number;
  questionId: number;
  questionText: string;
  shuffledOptions: string[];
  correctShuffledIndex: number;
  displayToOriginal: Record<string, string>;
  explanation?: string;
  answered: boolean;
  userAnswer?: string;
  isCorrect?: boolean;
  timeTakenSeconds?: number;
}

export interface QuizState {
  sessionId: string;
  questions: QuizQuestion[];
  currentIndex: number;
  startTime: Date;
  numCorrect: number;
  isFinished: boolean;
}

export class QuizEngine {
  private readonly questionRepo: QuestionRepository;
  private readonly sessionRepo: SessionRepository;
  private readonly responseRepo: ResponseRepository;

  constructor(dataSource: DataSource) {
    this.questionRepo = new QuestionRepository(dataSource);
    this.sessionRepo = new SessionRepository(dataSource);
    this.responseRepo = new ResponseRepository(dataSource);
  }

  /**
   * Start a new quiz session with the given number of questions.
   */
  async startSession(numQuestions: number): Promise<QuizState> {
    const availableCount = await this.questionRepo.count();
    if (availableCount === 0) {
      throw new InsufficientQuestionsError(numQuestions, 0);
    }

    const questions = await this.questionRepo.getRandomQuestions(numQuestions);
    if (questions.length < numQuestions) {
      throw new InsufficientQuestionsError(numQuestions, questions.length);
    }

    const sessionId = uuidv4();
    await this.sessionRepo.save({
      sessionId,
      numQuestions,
      numCorrect: 0,
      percentageCorrect: 0,
    });

    const quizQuestions: QuizQuestion[] = questions.map((q, idx) => {
      const shuffle: ShuffleResult = shuffleAnswers(q);
      return {
        index: idx,
        questionId: q.id,
        questionText: q.questionText,
        shuffledOptions: shuffle.shuffledOptions,
        correctShuffledIndex: shuffle.correctShuffledIndex,
        displayToOriginal: shuffle.displayToOriginal,
        explanation: q.explanation,
        answered: false,
      };
    });

    return {
      sessionId,
      questions: quizQuestions,
      currentIndex: 0,
      startTime: new Date(),
      numCorrect: 0,
      isFinished: false,
    };
  }

  /**
   * Submit an answer for the current question in the quiz state.
   * @param state - The current quiz state (mutated in place)
   * @param questionIndex - Index of the question being answered
   * @param displayAnswer - The user's answer letter (A-E) in shuffled display order
   * @param timeTakenSeconds - Optional time taken to answer
   */
  async submitAnswer(
    state: QuizState,
    questionIndex: number,
    displayAnswer: string,
    timeTakenSeconds?: number,
  ): Promise<boolean> {
    const upperAnswer = displayAnswer.toUpperCase();
    if (!QuizUtils.isValidAnswer(upperAnswer)) {
      throw new InvalidAnswerError(displayAnswer);
    }

    const quizQ = state.questions[questionIndex];
    if (!quizQ) {
      throw new Error(`No question at index ${questionIndex}`);
    }

    const displayLetters = ['A', 'B', 'C', 'D', 'E'];
    const answerIndex = displayLetters.indexOf(upperAnswer);
    const isCorrect = answerIndex === quizQ.correctShuffledIndex;

    quizQ.answered = true;
    quizQ.userAnswer = upperAnswer;
    quizQ.isCorrect = isCorrect;
    quizQ.timeTakenSeconds = timeTakenSeconds;

    if (isCorrect) {
      state.numCorrect++;
    }

    // Persist response
    await this.responseRepo.save({
      sessionId: state.sessionId,
      questionId: quizQ.questionId,
      userAnswer: upperAnswer,
      isCorrect: isCorrect ? 1 : 0,
      timeTakenSeconds,
    });

    // Mark the question as used
    await this.questionRepo.markUsed(quizQ.questionId);

    return isCorrect;
  }

  /**
   * Finalize the quiz session and compute final stats.
   */
  async finalizeSession(state: QuizState): Promise<QuizSession> {
    const session = await this.sessionRepo.findById(state.sessionId);
    if (!session) {
      throw new SessionNotFoundError(state.sessionId);
    }

    const endTime = new Date();
    const timeTakenSeconds = Math.round((endTime.getTime() - state.startTime.getTime()) / 1000);
    const percentage = QuizUtils.calculatePercentage(state.numCorrect, state.questions.length);

    await this.sessionRepo.update(state.sessionId, {
      endedAt: endTime,
      numCorrect: state.numCorrect,
      percentageCorrect: percentage,
      timeTakenSeconds,
    });

    state.isFinished = true;

    // Advance cycle if all questions have been used
    await this.questionRepo.advanceCycleIfExhausted();

    return (await this.sessionRepo.findById(state.sessionId)) as QuizSession;
  }

  async getSessionById(sessionId: string): Promise<QuizSession | null> {
    return this.sessionRepo.findById(sessionId);
  }
}
