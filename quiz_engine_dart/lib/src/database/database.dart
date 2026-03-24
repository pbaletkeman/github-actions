import 'package:sqlite3/sqlite3.dart';

import '../models/question.dart';
import '../models/quiz_response.dart';
import '../models/quiz_session.dart';

/// Manages the SQLite database connection and schema.
class AppDatabase {
  final Database _db;

  AppDatabase(this._db) {
    _initialize();
  }

  /// Opens a database at [path], creating it if it does not exist.
  factory AppDatabase.open(String path) {
    final db = sqlite3.open(path);
    return AppDatabase(db);
  }

  /// Opens an in-memory database suitable for testing.
  factory AppDatabase.inMemory() {
    final db = sqlite3.openInMemory();
    return AppDatabase(db);
  }

  Database get db => _db;

  void _initialize() {
    _db.execute('PRAGMA journal_mode=WAL;');
    _db.execute('PRAGMA foreign_keys=ON;');
    _createTables();
  }

  void _createTables() {
    _db.execute('''
      CREATE TABLE IF NOT EXISTS questions (
        id              INTEGER PRIMARY KEY AUTOINCREMENT,
        question_text   TEXT    NOT NULL CHECK(length(question_text) >= 1),
        option_a        TEXT    NOT NULL,
        option_b        TEXT    NOT NULL,
        option_c        TEXT    NOT NULL,
        option_d        TEXT    NOT NULL,
        option_e        TEXT,
        correct_answer  TEXT    NOT NULL CHECK(length(correct_answer) = 1),
        explanation     TEXT,
        section         TEXT,
        difficulty      TEXT,
        source_file     TEXT,
        usage_cycle     INTEGER NOT NULL DEFAULT 1,
        times_used      INTEGER NOT NULL DEFAULT 0,
        last_used_at    INTEGER,
        created_at      INTEGER NOT NULL DEFAULT (strftime('%s','now'))
      );
    ''');

    _db.execute('''
      CREATE TABLE IF NOT EXISTS quiz_sessions (
        session_id          TEXT    PRIMARY KEY,
        started_at          INTEGER NOT NULL DEFAULT (strftime('%s','now')),
        ended_at            INTEGER,
        num_questions       INTEGER NOT NULL,
        num_correct         INTEGER NOT NULL DEFAULT 0,
        percentage_correct  REAL    NOT NULL DEFAULT 0.0,
        time_taken_seconds  INTEGER
      );
    ''');

    _db.execute('''
      CREATE TABLE IF NOT EXISTS quiz_responses (
        id                  INTEGER PRIMARY KEY AUTOINCREMENT,
        session_id          TEXT    NOT NULL,
        question_id         INTEGER NOT NULL,
        user_answer         TEXT    NOT NULL,
        is_correct          INTEGER NOT NULL DEFAULT 0,
        time_taken_seconds  INTEGER,
        UNIQUE(session_id, question_id)
      );
    ''');
  }

  /// Closes the database connection.
  void close() => _db.dispose();

  // ---------------------------------------------------------------------------
  // Questions DAO
  // ---------------------------------------------------------------------------

  /// Inserts a question. Returns the new row id.
  int insertQuestion(Map<String, dynamic> data) {
    _db.execute('''
      INSERT INTO questions
        (question_text, option_a, option_b, option_c, option_d, option_e,
         correct_answer, explanation, section, difficulty, source_file,
         usage_cycle, times_used, last_used_at, created_at)
      VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
    ''', [
      data['question_text'],
      data['option_a'],
      data['option_b'],
      data['option_c'],
      data['option_d'],
      data['option_e'],
      data['correct_answer'],
      data['explanation'],
      data['section'],
      data['difficulty'],
      data['source_file'],
      data['usage_cycle'] ?? 1,
      data['times_used'] ?? 0,
      data['last_used_at'],
      data['created_at'] ??
          (DateTime.now().millisecondsSinceEpoch ~/ 1000),
    ]);
    return _db.lastInsertRowId;
  }

  /// Inserts a question only if no row with the same text already exists.
  /// Returns the id of the existing or newly inserted row.
  int insertQuestionIfNotExists(Map<String, dynamic> data) {
    final existing = _db.select(
      'SELECT id FROM questions WHERE question_text = ?',
      [data['question_text']],
    );
    if (existing.isNotEmpty) {
      return existing.first['id'] as int;
    }
    return insertQuestion(data);
  }

  /// Returns all questions.
  List<Question> getAllQuestions() {
    final rows = _db.select('SELECT * FROM questions ORDER BY id');
    return rows.map(Question.fromMap).toList();
  }

  /// Returns [count] random questions from the current cycle.
  List<Question> getRandomQuestions(int count) {
    final cycle = _getCurrentCycle();
    final rows = _db.select('''
      SELECT * FROM questions
      WHERE usage_cycle = ? AND times_used = 0
      ORDER BY RANDOM()
      LIMIT ?
    ''', [cycle, count]);
    if (rows.length < count) {
      // Fall back to any questions in cycle (already used ones)
      final fallback = _db.select('''
        SELECT * FROM questions
        WHERE usage_cycle = ?
        ORDER BY RANDOM()
        LIMIT ?
      ''', [cycle, count]);
      return fallback.map(Question.fromMap).toList();
    }
    return rows.map(Question.fromMap).toList();
  }

  /// Marks a question as used.
  void markQuestionUsed(int id) {
    _db.execute('''
      UPDATE questions
      SET times_used = times_used + 1,
          last_used_at = strftime('%s','now')
      WHERE id = ?
    ''', [id]);
  }

  /// Advances the cycle for ALL questions if none remain unused in the current cycle.
  void advanceCycleIfExhausted() {
    final cycle = _getCurrentCycle();
    final remaining = _db.select('''
      SELECT COUNT(*) as cnt FROM questions
      WHERE usage_cycle = ? AND times_used = 0
    ''', [cycle]);
    final count = remaining.first['cnt'] as int;
    if (count == 0) {
      final newCycle = cycle + 1;
      _db.execute('''
        UPDATE questions
        SET usage_cycle = ?, times_used = 0
      ''', [newCycle]);
    }
  }

  /// Returns the current usage cycle (max cycle across all questions).
  int getCurrentCycle() => _getCurrentCycle();

  int _getCurrentCycle() {
    final row = _db.select('SELECT MAX(usage_cycle) as c FROM questions');
    return (row.first['c'] as int?) ?? 1;
  }

  /// Returns the total number of questions.
  int countQuestions() {
    final row =
        _db.select('SELECT COUNT(*) as cnt FROM questions').first;
    return row['cnt'] as int;
  }

  /// Deletes all questions.
  void clearQuestions() => _db.execute('DELETE FROM questions');

  // ---------------------------------------------------------------------------
  // Sessions DAO
  // ---------------------------------------------------------------------------

  /// Inserts a new quiz session.
  void insertSession(Map<String, dynamic> data) {
    _db.execute('''
      INSERT INTO quiz_sessions
        (session_id, started_at, ended_at, num_questions,
         num_correct, percentage_correct, time_taken_seconds)
      VALUES (?,?,?,?,?,?,?)
    ''', [
      data['session_id'],
      data['started_at'] ??
          (DateTime.now().millisecondsSinceEpoch ~/ 1000),
      data['ended_at'],
      data['num_questions'],
      data['num_correct'] ?? 0,
      data['percentage_correct'] ?? 0.0,
      data['time_taken_seconds'],
    ]);
  }

  /// Updates session statistics after quiz completion.
  void updateSession({
    required String sessionId,
    required int numCorrect,
    required double percentageCorrect,
    required DateTime endedAt,
    int? timeTakenSeconds,
  }) {
    _db.execute('''
      UPDATE quiz_sessions
      SET num_correct = ?,
          percentage_correct = ?,
          ended_at = ?,
          time_taken_seconds = ?
      WHERE session_id = ?
    ''', [
      numCorrect,
      percentageCorrect,
      endedAt.millisecondsSinceEpoch ~/ 1000,
      timeTakenSeconds,
      sessionId,
    ]);
  }

  /// Returns a session by its id, or null if not found.
  QuizSession? getSession(String sessionId) {
    final rows = _db.select(
      'SELECT * FROM quiz_sessions WHERE session_id = ?',
      [sessionId],
    );
    if (rows.isEmpty) return null;
    return QuizSession.fromMap(rows.first);
  }

  /// Returns all sessions ordered by start time descending.
  List<QuizSession> getAllSessions() {
    final rows = _db.select(
        'SELECT * FROM quiz_sessions ORDER BY started_at DESC');
    return rows.map(QuizSession.fromMap).toList();
  }

  /// Deletes all sessions.
  void clearSessions() => _db.execute('DELETE FROM quiz_sessions');

  // ---------------------------------------------------------------------------
  // Responses DAO
  // ---------------------------------------------------------------------------

  /// Inserts a quiz response.
  void insertResponse(Map<String, dynamic> data) {
    _db.execute('''
      INSERT OR REPLACE INTO quiz_responses
        (session_id, question_id, user_answer, is_correct, time_taken_seconds)
      VALUES (?,?,?,?,?)
    ''', [
      data['session_id'],
      data['question_id'],
      data['user_answer'],
      data['is_correct'] ?? 0,
      data['time_taken_seconds'],
    ]);
  }

  /// Returns the number of correct responses for a session.
  int countCorrectResponses(String sessionId) {
    final row = _db.select('''
      SELECT COUNT(*) as cnt FROM quiz_responses
      WHERE session_id = ? AND is_correct = 1
    ''', [sessionId]).first;
    return row['cnt'] as int;
  }

  /// Returns all responses for a session.
  List<QuizResponse> getResponsesForSession(String sessionId) {
    final rows = _db.select('''
      SELECT * FROM quiz_responses WHERE session_id = ?
    ''', [sessionId]);
    return rows.map(QuizResponse.fromMap).toList();
  }

  /// Deletes all responses.
  void clearResponses() =>
      _db.execute('DELETE FROM quiz_responses');
}
