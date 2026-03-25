import 'package:quiz_engine_dart/src/database/database.dart';

/// Opens an in-memory database for test isolation.
AppDatabase openTestDatabase() => AppDatabase.inMemory();

/// Returns a map suitable for inserting a sample question.
Map<String, dynamic> sampleQuestion({
  String questionText = 'What is CI?',
  String optionA = 'Continuous Integration',
  String optionB = 'Code Import',
  String optionC = 'Compile',
  String optionD = 'Configure',
  String? optionE,
  String correctAnswer = 'A',
  String? explanation = 'CI stands for Continuous Integration.',
  String? section = 'GitHub Actions',
  String? difficulty = 'easy',
}) {
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
    'source_file': null,
    'usage_cycle': 1,
    'times_used': 0,
    'last_used_at': null,
  };
}
