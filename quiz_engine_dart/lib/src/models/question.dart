/// Question model class.
class Question {
  final int id;
  final String questionText;
  final String optionA;
  final String optionB;
  final String optionC;
  final String optionD;
  final String? optionE;
  final String correctAnswer;
  final String? explanation;
  final String? section;
  final String? difficulty;
  final String? sourceFile;
  final int usageCycle;
  final int timesUsed;
  final DateTime? lastUsedAt;
  final DateTime createdAt;

  const Question({
    required this.id,
    required this.questionText,
    required this.optionA,
    required this.optionB,
    required this.optionC,
    required this.optionD,
    this.optionE,
    required this.correctAnswer,
    this.explanation,
    this.section,
    this.difficulty,
    this.sourceFile,
    this.usageCycle = 1,
    this.timesUsed = 0,
    this.lastUsedAt,
    required this.createdAt,
  });

  /// Creates a [Question] from a database row map.
  factory Question.fromMap(Map<String, dynamic> map) {
    return Question(
      id: map['id'] as int,
      questionText: map['question_text'] as String,
      optionA: map['option_a'] as String,
      optionB: map['option_b'] as String,
      optionC: map['option_c'] as String,
      optionD: map['option_d'] as String,
      optionE: map['option_e'] as String?,
      correctAnswer: map['correct_answer'] as String,
      explanation: map['explanation'] as String?,
      section: map['section'] as String?,
      difficulty: map['difficulty'] as String?,
      sourceFile: map['source_file'] as String?,
      usageCycle: map['usage_cycle'] as int? ?? 1,
      timesUsed: map['times_used'] as int? ?? 0,
      lastUsedAt: map['last_used_at'] != null
          ? DateTime.fromMillisecondsSinceEpoch(
              (map['last_used_at'] as int) * 1000)
          : null,
      createdAt: DateTime.fromMillisecondsSinceEpoch(
          (map['created_at'] as int) * 1000),
    );
  }

  /// Converts this [Question] to a map suitable for database insertion.
  Map<String, dynamic> toInsertMap() {
    return {
      'question_text': questionText,
      'option_a': optionA,
      'option_b': optionB,
      'option_c': optionC,
      'option_d': optionD,
      'option_e': optionE,
      'correct_answer': correctAnswer,
      'explanation': explanation,
      'section': section,
      'difficulty': difficulty,
      'source_file': sourceFile,
      'usage_cycle': usageCycle,
      'times_used': timesUsed,
      'last_used_at':
          lastUsedAt != null ? lastUsedAt!.millisecondsSinceEpoch ~/ 1000 : null,
      'created_at': createdAt.millisecondsSinceEpoch ~/ 1000,
    };
  }

  @override
  String toString() => 'Question(id: $id, text: $questionText)';
}
