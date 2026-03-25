import 'package:test/test.dart';
import 'package:quiz_engine_dart/src/models/question.dart';
import 'package:quiz_engine_dart/src/models/quiz_session.dart';
import 'package:quiz_engine_dart/src/models/quiz_response.dart';

void main() {
  group('Question', () {
    test('fromMap round-trip preserves all fields', () {
      final now = DateTime.now();
      final q = Question(
        id: 42,
        questionText: 'What is CD?',
        optionA: 'Continuous Delivery',
        optionB: 'Code Deployment',
        optionC: 'Cloud Deploy',
        optionD: 'Concurrent Dev',
        optionE: 'None of the above',
        correctAnswer: 'A',
        explanation: 'CD = Continuous Delivery',
        section: 'DevOps',
        difficulty: 'medium',
        sourceFile: 'questions.md',
        usageCycle: 3,
        timesUsed: 7,
        lastUsedAt: now,
        createdAt: now,
      );

      final map = q.toInsertMap();
      expect(map['question_text'], equals('What is CD?'));
      expect(map['option_a'], equals('Continuous Delivery'));
      expect(map['option_e'], equals('None of the above'));
      expect(map['correct_answer'], equals('A'));
      expect(map['explanation'], equals('CD = Continuous Delivery'));
      expect(map['section'], equals('DevOps'));
      expect(map['usage_cycle'], equals(3));
      expect(map['times_used'], equals(7));
    });

    test('toInsertMap has null optionE when not provided', () {
      final q = Question(
        id: 1,
        questionText: 'Q?',
        optionA: 'A',
        optionB: 'B',
        optionC: 'C',
        optionD: 'D',
        correctAnswer: 'A',
        createdAt: DateTime.now(),
      );
      expect(q.toInsertMap()['option_e'], isNull);
    });

    test('fromMap parses unix timestamp for createdAt', () {
      final ts = DateTime(2024, 6, 15, 12, 0, 0).millisecondsSinceEpoch ~/ 1000;
      final map = {
        'id': 1,
        'question_text': 'Q?',
        'option_a': 'A',
        'option_b': 'B',
        'option_c': 'C',
        'option_d': 'D',
        'option_e': null,
        'correct_answer': 'A',
        'explanation': null,
        'section': null,
        'difficulty': null,
        'source_file': null,
        'usage_cycle': 1,
        'times_used': 0,
        'last_used_at': null,
        'created_at': ts,
      };
      final q = Question.fromMap(map);
      expect(q.createdAt.year, equals(2024));
      expect(q.createdAt.month, equals(6));
    });

    test('toString includes id and text', () {
      final q = Question(
        id: 99,
        questionText: 'Sample?',
        optionA: 'A',
        optionB: 'B',
        optionC: 'C',
        optionD: 'D',
        correctAnswer: 'A',
        createdAt: DateTime.now(),
      );
      expect(q.toString(), contains('99'));
      expect(q.toString(), contains('Sample?'));
    });
  });

  group('QuizSession', () {
    test('fromMap parses all fields', () {
      final now = DateTime.now();
      final ts = now.millisecondsSinceEpoch ~/ 1000;
      final map = {
        'session_id': 'abc-123',
        'started_at': ts,
        'ended_at': ts,
        'num_questions': 20,
        'num_correct': 18,
        'percentage_correct': 90.0,
        'time_taken_seconds': 600,
      };
      final s = QuizSession.fromMap(map);
      expect(s.sessionId, equals('abc-123'));
      expect(s.numQuestions, equals(20));
      expect(s.numCorrect, equals(18));
      expect(s.percentageCorrect, closeTo(90.0, 0.01));
    });

    test('copyWith updates fields correctly', () {
      final now = DateTime.now();
      final s = QuizSession(
        sessionId: 'copy-test',
        startedAt: now,
        numQuestions: 10,
      );
      final updated = s.copyWith(numCorrect: 7, percentageCorrect: 70.0);
      expect(updated.numCorrect, equals(7));
      expect(updated.percentageCorrect, closeTo(70.0, 0.01));
      expect(updated.sessionId, equals('copy-test'));
    });

    test('toInsertMap null endedAt is null', () {
      final s = QuizSession(
        sessionId: 's1',
        startedAt: DateTime.now(),
        numQuestions: 5,
      );
      expect(s.toInsertMap()['ended_at'], isNull);
    });

    test('toString contains sessionId', () {
      final s = QuizSession(
        sessionId: 'str-test',
        startedAt: DateTime.now(),
        numQuestions: 5,
      );
      expect(s.toString(), contains('str-test'));
    });
  });

  group('QuizResponse', () {
    test('fromMap sets correct field', () {
      final map = {
        'id': 1,
        'session_id': 'resp-session',
        'question_id': 42,
        'user_answer': 'C',
        'is_correct': 1,
        'time_taken_seconds': 30,
      };
      final r = QuizResponse.fromMap(map);
      expect(r.correct, isTrue);
      expect(r.userAnswer, equals('C'));
      expect(r.questionId, equals(42));
    });

    test('correct getter returns false for isCorrect=0', () {
      final r = QuizResponse(
        sessionId: 's',
        questionId: 1,
        userAnswer: 'B',
        isCorrect: 0,
      );
      expect(r.correct, isFalse);
    });

    test('toInsertMap includes all required fields', () {
      final r = QuizResponse(
        sessionId: 's1',
        questionId: 5,
        userAnswer: 'D',
        isCorrect: 1,
        timeTakenSeconds: 20,
      );
      final m = r.toInsertMap();
      expect(m['session_id'], equals('s1'));
      expect(m['question_id'], equals(5));
      expect(m['user_answer'], equals('D'));
      expect(m['is_correct'], equals(1));
      expect(m['time_taken_seconds'], equals(20));
    });

    test('toString is descriptive', () {
      final r = QuizResponse(
        sessionId: 'my-sess',
        questionId: 3,
        userAnswer: 'A',
        isCorrect: 1,
      );
      expect(r.toString(), contains('my-sess'));
    });
  });
}
