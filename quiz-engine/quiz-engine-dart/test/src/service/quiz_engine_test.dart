import 'package:test/test.dart';
import 'package:quiz_engine_dart/src/service/quiz_service.dart';
import 'package:quiz_engine_dart/src/service/quiz_engine.dart';
import 'package:quiz_engine_dart/src/exceptions/quiz_exceptions.dart';

import '../../helpers.dart';

void main() {
  late dynamic db;
  late QuizService service;

  setUp(() {
    db = openTestDatabase();
    service = QuizService(db);
  });

  tearDown(() => db.close());

  void addQuestions(int count) {
    for (var i = 0; i < count; i++) {
      db.insertQuestion(sampleQuestion(
        questionText: 'Question $i',
        correctAnswer: 'A',
      ));
    }
  }

  group('QuizService.getRandomQuestions', () {
    test('throws NoQuestionsException when database is empty', () {
      expect(() => service.getRandomQuestions(5),
          throwsA(isA<NoQuestionsException>()));
    });

    test('throws InsufficientQuestionsException when not enough', () {
      addQuestions(2);
      expect(() => service.getRandomQuestions(5),
          throwsA(isA<InsufficientQuestionsException>()));
    });

    test('returns correct number of questions', () {
      addQuestions(10);
      expect(service.getRandomQuestions(5).length, equals(5));
    });

    test('returns all questions when count equals total', () {
      addQuestions(3);
      expect(service.getRandomQuestions(3).length, equals(3));
    });
  });

  group('QuizService.cycle operations', () {
    test('getCurrentCycle returns 1 for empty db', () {
      expect(service.getCurrentCycle(), equals(1));
    });

    test('advanceCycleIfNeeded advances when all used', () {
      final id = db.insertQuestion(sampleQuestion());
      service.markQuestionUsed(id);
      service.advanceCycleIfNeeded();
      expect(service.getCurrentCycle(), equals(2));
    });
  });

  group('QuizService bulk operations', () {
    test('clearAll removes questions, sessions, and responses', () {
      addQuestions(2);
      db.insertSession({
        'session_id': 'bulk-s1',
        'num_questions': 2,
        'started_at': DateTime.now().millisecondsSinceEpoch ~/ 1000,
      });
      service.clearAll();
      expect(service.countQuestions(), equals(0));
      expect(service.getAllSessions(), isEmpty);
    });

    test('clearHistory keeps questions', () {
      addQuestions(2);
      db.insertSession({
        'session_id': 'bulk-s2',
        'num_questions': 2,
        'started_at': DateTime.now().millisecondsSinceEpoch ~/ 1000,
      });
      service.clearHistory();
      expect(service.countQuestions(), equals(2));
      expect(service.getAllSessions(), isEmpty);
    });
  });

  group('QuizEngine.loadQuestions', () {
    test('loads correct number of questions', () {
      addQuestions(5);
      final engine = QuizEngine(
        sessionId: 'eng-session-1',
        quizService: service,
        numQuestions: 3,
      );
      engine.loadQuestions();
      expect(engine.questions.length, equals(3));
    });

    test('creates a session record after loadQuestions', () {
      addQuestions(5);
      final engine = QuizEngine(
        sessionId: 'eng-session-2',
        quizService: service,
        numQuestions: 2,
      );
      engine.loadQuestions();
      expect(service.getSession('eng-session-2'), isNotNull);
    });

    test('pre-shuffles answers for all questions', () {
      addQuestions(3);
      final engine = QuizEngine(
        sessionId: 'eng-session-3',
        quizService: service,
        numQuestions: 3,
      );
      engine.loadQuestions();
      for (var i = 0; i < 3; i++) {
        final sr = engine.getShuffleResult(i);
        expect(sr.shuffledOptions.length, greaterThanOrEqualTo(4));
      }
    });
  });

  group('QuizEngine.submitAnswer', () {
    late QuizEngine engine;

    setUp(() {
      addQuestions(2);
      engine = QuizEngine(
        sessionId: 'submit-session',
        quizService: service,
        numQuestions: 2,
      );
      engine.loadQuestions();
    });

    test('increments numCorrect on correct answer', () {
      final correctLabel = engine.getShuffleResult(0).correctLabel;
      engine.submitAnswer(0, correctLabel);
      expect(engine.numCorrect, equals(1));
    });

    test('does not increment numCorrect on wrong answer', () {
      final sr = engine.getShuffleResult(0);
      final wrong = sr.labels.firstWhere((l) => l != sr.correctLabel);
      engine.submitAnswer(0, wrong);
      expect(engine.numCorrect, equals(0));
    });

    test('saves response to database', () {
      final correctLabel = engine.getShuffleResult(0).correctLabel;
      engine.submitAnswer(0, correctLabel, timeTaken: 15);
      final responses = service.getResponsesForSession('submit-session');
      expect(responses.length, equals(1));
    });
  });

  group('QuizEngine.finalizeQuiz', () {
    test('persists session to database', () {
      addQuestions(2);
      final engine = QuizEngine(
        sessionId: 'fin-session',
        quizService: service,
        numQuestions: 2,
      );
      engine.loadQuestions();
      final correctLabel0 = engine.getShuffleResult(0).correctLabel;
      final correctLabel1 = engine.getShuffleResult(1).correctLabel;
      engine.submitAnswer(0, correctLabel0, timeTaken: 5);
      engine.submitAnswer(1, correctLabel1, timeTaken: 5);
      final session = engine.finalizeQuiz();
      expect(session.sessionId, equals('fin-session'));
      expect(session.numCorrect, equals(2));
      expect(session.percentageCorrect, closeTo(100.0, 0.01));
    });

    test('marks questions as used after finalize', () {
      // Add 3 questions but only quiz on 2 so the cycle does NOT advance,
      // leaving times_used=1 on the 2 questions that were used.
      addQuestions(3);
      final engine = QuizEngine(
        sessionId: 'fin-session-2',
        quizService: service,
        numQuestions: 2,
      );
      engine.loadQuestions();
      engine.finalizeQuiz();
      final usedIds =
          engine.questions.map((q) => q.id).toSet();
      final allQuestions = service.getAllQuestions();
      for (final q in allQuestions) {
        if (usedIds.contains(q.id)) {
          expect(q.timesUsed, equals(1),
              reason: 'Used question ${q.id} should have timesUsed=1');
        } else {
          expect(q.timesUsed, equals(0),
              reason: 'Unused question ${q.id} should have timesUsed=0');
        }
      }
    });

    test('advances cycle after finalize when all questions used', () {
      addQuestions(2);
      final engine = QuizEngine(
        sessionId: 'fin-session-3',
        quizService: service,
        numQuestions: 2,
      );
      engine.loadQuestions();
      engine.finalizeQuiz();
      // All 2 questions were in this session, so cycle should advance
      expect(service.getCurrentCycle(), equals(2));
    });
  });
}
