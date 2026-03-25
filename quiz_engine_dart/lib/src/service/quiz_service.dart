import '../database/database.dart';
import '../models/question.dart';
import '../models/quiz_response.dart';
import '../models/quiz_session.dart';
import '../exceptions/quiz_exceptions.dart';

/// Provides high-level quiz business-logic operations.
class QuizService {
  final AppDatabase _db;

  QuizService(this._db);

  // ---------------------------------------------------------------------------
  // Questions
  // ---------------------------------------------------------------------------

  /// Returns [count] random questions from the current cycle.
  /// Throws [NoQuestionsException] if the database is empty.
  /// Throws [InsufficientQuestionsException] if fewer than [count] questions exist.
  List<Question> getRandomQuestions(int count) {
    final total = _db.countQuestions();
    if (total == 0) throw const NoQuestionsException();
    if (total < count) {
      throw InsufficientQuestionsException(
          requested: count, available: total);
    }
    return _db.getRandomQuestions(count);
  }

  /// Returns all questions.
  List<Question> getAllQuestions() => _db.getAllQuestions();

  /// Marks a question as used and potentially advances the cycle.
  void markQuestionUsed(int questionId) {
    _db.markQuestionUsed(questionId);
  }

  /// Advances the cycle if all questions in the current cycle have been used.
  void advanceCycleIfNeeded() => _db.advanceCycleIfExhausted();

  /// Returns the current usage cycle number.
  int getCurrentCycle() => _db.getCurrentCycle();

  /// Returns the total number of questions.
  int countQuestions() => _db.countQuestions();

  // ---------------------------------------------------------------------------
  // Sessions
  // ---------------------------------------------------------------------------

  /// Creates and persists a new quiz session.
  void createSession({
    required String sessionId,
    required int numQuestions,
  }) {
    _db.insertSession({
      'session_id': sessionId,
      'num_questions': numQuestions,
      'started_at': DateTime.now().millisecondsSinceEpoch ~/ 1000,
    });
  }

  /// Updates an existing session with final stats.
  void updateSession({
    required String sessionId,
    required int numCorrect,
    required double percentageCorrect,
    required DateTime endedAt,
    int? timeTakenSeconds,
  }) {
    _db.updateSession(
      sessionId: sessionId,
      numCorrect: numCorrect,
      percentageCorrect: percentageCorrect,
      endedAt: endedAt,
      timeTakenSeconds: timeTakenSeconds,
    );
  }

  /// Returns a session by id, or null.
  QuizSession? getSession(String sessionId) => _db.getSession(sessionId);

  /// Returns all sessions.
  List<QuizSession> getAllSessions() => _db.getAllSessions();

  // ---------------------------------------------------------------------------
  // Responses
  // ---------------------------------------------------------------------------

  /// Records an answer for a question.
  void saveResponse({
    required String sessionId,
    required int questionId,
    required String userAnswer,
    required int isCorrect,
    int? timeTaken,
  }) {
    _db.insertResponse({
      'session_id': sessionId,
      'question_id': questionId,
      'user_answer': userAnswer,
      'is_correct': isCorrect,
      'time_taken_seconds': timeTaken,
    });
  }

  /// Returns the number of correct answers for a session.
  int countCorrectAnswers(String sessionId) =>
      _db.countCorrectResponses(sessionId);

  /// Returns all responses for a session.
  List<QuizResponse> getResponsesForSession(String sessionId) =>
      _db.getResponsesForSession(sessionId);

  // ---------------------------------------------------------------------------
  // Bulk operations
  // ---------------------------------------------------------------------------

  /// Clears all quiz data (questions, sessions, responses).
  void clearAll() {
    _db.clearResponses();
    _db.clearSessions();
    _db.clearQuestions();
  }

  /// Clears only session and response data.
  void clearHistory() {
    _db.clearResponses();
    _db.clearSessions();
  }

  /// Clears only questions.
  void clearQuestions() => _db.clearQuestions();
}
