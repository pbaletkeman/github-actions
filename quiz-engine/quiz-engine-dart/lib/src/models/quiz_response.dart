/// Quiz response model class.
class QuizResponse {
  final int? id;
  final String sessionId;
  final int questionId;
  final String userAnswer;
  final int isCorrect;
  final int? timeTakenSeconds;

  const QuizResponse({
    this.id,
    required this.sessionId,
    required this.questionId,
    required this.userAnswer,
    required this.isCorrect,
    this.timeTakenSeconds,
  });

  /// Creates a [QuizResponse] from a database row map.
  factory QuizResponse.fromMap(Map<String, dynamic> map) {
    return QuizResponse(
      id: map['id'] as int?,
      sessionId: map['session_id'] as String,
      questionId: map['question_id'] as int,
      userAnswer: map['user_answer'] as String,
      isCorrect: map['is_correct'] as int,
      timeTakenSeconds: map['time_taken_seconds'] as int?,
    );
  }

  /// Converts this [QuizResponse] to a map suitable for database insertion.
  Map<String, dynamic> toInsertMap() {
    return {
      'session_id': sessionId,
      'question_id': questionId,
      'user_answer': userAnswer,
      'is_correct': isCorrect,
      'time_taken_seconds': timeTakenSeconds,
    };
  }

  bool get correct => isCorrect == 1;

  @override
  String toString() =>
      'QuizResponse(session: $sessionId, question: $questionId, answer: $userAnswer, correct: $correct)';
}
