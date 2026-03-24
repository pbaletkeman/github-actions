import '../database/database.dart';
import '../models/quiz_session.dart';
import '../models/quiz_response.dart';
import '../models/question.dart';
import '../service/quiz_service.dart';

/// Provides quiz history queries.
class HistoryService {
  final QuizService _quizService;
  final AppDatabase _db;

  HistoryService(this._quizService, this._db);

  /// Returns all recorded sessions, newest first.
  List<QuizSession> getSessions() => _quizService.getAllSessions();

  /// Returns all responses for [sessionId].
  List<QuizResponse> getResponses(String sessionId) =>
      _quizService.getResponsesForSession(sessionId);

  /// Returns a map of question id → Question for the given list of ids.
  Map<int, Question> getQuestionsByIds(List<int> ids) {
    if (ids.isEmpty) return {};
    final rows = _db.db.select(
      'SELECT * FROM questions WHERE id IN (${ids.map((_) => '?').join(',')})',
      ids,
    );
    return {for (final row in rows) row['id'] as int: Question.fromMap(row)};
  }

  /// Exports all sessions to a list of maps (JSON-serialisable).
  List<Map<String, dynamic>> exportSessions() {
    return getSessions().map((s) => s.toInsertMap()).toList();
  }

  /// Exports all responses for [sessionId] to a list of maps.
  List<Map<String, dynamic>> exportResponses(String sessionId) {
    return getResponses(sessionId).map((r) => r.toInsertMap()).toList();
  }
}
