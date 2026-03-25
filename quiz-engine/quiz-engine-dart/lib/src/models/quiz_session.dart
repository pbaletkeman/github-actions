/// Quiz session model class.
class QuizSession {
  final String sessionId;
  final DateTime startedAt;
  final DateTime? endedAt;
  final int numQuestions;
  final int numCorrect;
  final double percentageCorrect;
  final int? timeTakenSeconds;

  const QuizSession({
    required this.sessionId,
    required this.startedAt,
    this.endedAt,
    required this.numQuestions,
    this.numCorrect = 0,
    this.percentageCorrect = 0.0,
    this.timeTakenSeconds,
  });

  /// Creates a [QuizSession] from a database row map.
  factory QuizSession.fromMap(Map<String, dynamic> map) {
    return QuizSession(
      sessionId: map['session_id'] as String,
      startedAt: DateTime.fromMillisecondsSinceEpoch(
          (map['started_at'] as int) * 1000),
      endedAt: map['ended_at'] != null
          ? DateTime.fromMillisecondsSinceEpoch(
              (map['ended_at'] as int) * 1000)
          : null,
      numQuestions: map['num_questions'] as int,
      numCorrect: map['num_correct'] as int? ?? 0,
      percentageCorrect: (map['percentage_correct'] as num?)?.toDouble() ?? 0.0,
      timeTakenSeconds: map['time_taken_seconds'] as int?,
    );
  }

  /// Converts this [QuizSession] to a map suitable for database insertion.
  Map<String, dynamic> toInsertMap() {
    return {
      'session_id': sessionId,
      'started_at': startedAt.millisecondsSinceEpoch ~/ 1000,
      'ended_at': endedAt != null
          ? endedAt!.millisecondsSinceEpoch ~/ 1000
          : null,
      'num_questions': numQuestions,
      'num_correct': numCorrect,
      'percentage_correct': percentageCorrect,
      'time_taken_seconds': timeTakenSeconds,
    };
  }

  /// Returns a copy of this session with updated fields.
  QuizSession copyWith({
    DateTime? endedAt,
    int? numCorrect,
    double? percentageCorrect,
    int? timeTakenSeconds,
  }) {
    return QuizSession(
      sessionId: sessionId,
      startedAt: startedAt,
      endedAt: endedAt ?? this.endedAt,
      numQuestions: numQuestions,
      numCorrect: numCorrect ?? this.numCorrect,
      percentageCorrect: percentageCorrect ?? this.percentageCorrect,
      timeTakenSeconds: timeTakenSeconds ?? this.timeTakenSeconds,
    );
  }

  @override
  String toString() =>
      'QuizSession(id: $sessionId, correct: $numCorrect/$numQuestions)';
}
