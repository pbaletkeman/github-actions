import 'package:test/test.dart';

import '../../helpers.dart';

void main() {
  late dynamic db;

  setUp(() => db = openTestDatabase());
  tearDown(() => db.close());

  group('insertQuestion / getAllQuestions', () {
    test('inserts and retrieves a single question', () {
      db.insertQuestion(sampleQuestion());
      final all = db.getAllQuestions();
      expect(all.length, equals(1));
      expect(all.first.questionText, equals('What is CI?'));
    });

    test('inserts multiple distinct questions', () {
      db.insertQuestion(sampleQuestion(questionText: 'Q1'));
      db.insertQuestion(sampleQuestion(questionText: 'Q2'));
      expect(db.getAllQuestions().length, equals(2));
    });

    test('question has correct default values', () {
      db.insertQuestion(sampleQuestion());
      final q = db.getAllQuestions().first;
      expect(q.usageCycle, equals(1));
      expect(q.timesUsed, equals(0));
    });
  });

  group('insertQuestionIfNotExists', () {
    test('does not create duplicate with same question_text', () {
      final data = sampleQuestion();
      db.insertQuestionIfNotExists(data);
      db.insertQuestionIfNotExists(data);
      expect(db.countQuestions(), equals(1));
    });

    test('inserts new question when text differs', () {
      db.insertQuestionIfNotExists(sampleQuestion(questionText: 'Q1'));
      db.insertQuestionIfNotExists(sampleQuestion(questionText: 'Q2'));
      expect(db.countQuestions(), equals(2));
    });

    test('returns the id of existing row on duplicate', () {
      final data = sampleQuestion();
      final id1 = db.insertQuestionIfNotExists(data);
      final id2 = db.insertQuestionIfNotExists(data);
      expect(id1, equals(id2));
    });
  });

  group('getRandomQuestions', () {
    test('returns requested number of questions', () {
      for (var i = 0; i < 5; i++) {
        db.insertQuestion(sampleQuestion(questionText: 'Q$i'));
      }
      final q = db.getRandomQuestions(3);
      expect(q.length, equals(3));
    });

    test('respects current cycle', () {
      final id = db.insertQuestion(sampleQuestion());
      db.markQuestionUsed(id);
      db.advanceCycleIfExhausted();
      final q = db.getRandomQuestions(1);
      expect(q.first.usageCycle, equals(2));
    });

    test('returns all questions when count equals total', () {
      for (var i = 0; i < 3; i++) {
        db.insertQuestion(sampleQuestion(questionText: 'Q$i'));
      }
      expect(db.getRandomQuestions(3).length, equals(3));
    });
  });

  group('markQuestionUsed', () {
    test('increments times_used', () {
      final id = db.insertQuestion(sampleQuestion());
      db.markQuestionUsed(id);
      final q = db.getAllQuestions().first;
      expect(q.timesUsed, equals(1));
    });

    test('sets last_used_at', () {
      final id = db.insertQuestion(sampleQuestion());
      db.markQuestionUsed(id);
      final q = db.getAllQuestions().first;
      expect(q.lastUsedAt, isNotNull);
    });
  });

  group('advanceCycleIfExhausted', () {
    test('advances cycle when all questions used', () {
      final id = db.insertQuestion(sampleQuestion());
      db.markQuestionUsed(id);
      db.advanceCycleIfExhausted();
      expect(db.getCurrentCycle(), equals(2));
    });

    test('does not advance cycle when questions remain', () {
      db.insertQuestion(sampleQuestion(questionText: 'Q1'));
      final id2 = db.insertQuestion(sampleQuestion(questionText: 'Q2'));
      db.markQuestionUsed(id2);
      db.advanceCycleIfExhausted();
      expect(db.getCurrentCycle(), equals(1));
    });

    test('resets times_used to 0 on cycle advance', () {
      final id = db.insertQuestion(sampleQuestion());
      db.markQuestionUsed(id);
      db.advanceCycleIfExhausted();
      final q = db.getAllQuestions().first;
      expect(q.timesUsed, equals(0));
    });
  });

  group('sessions', () {
    test('inserts and retrieves a session', () {
      db.insertSession({
        'session_id': 'test-session-1',
        'num_questions': 10,
        'started_at': DateTime.now().millisecondsSinceEpoch ~/ 1000,
      });
      final s = db.getSession('test-session-1');
      expect(s, isNotNull);
      expect(s!.numQuestions, equals(10));
    });

    test('returns null for non-existent session', () {
      expect(db.getSession('no-such-id'), isNull);
    });

    test('getAllSessions returns all sessions', () {
      db.insertSession({
        'session_id': 's1',
        'num_questions': 5,
        'started_at': DateTime.now().millisecondsSinceEpoch ~/ 1000,
      });
      db.insertSession({
        'session_id': 's2',
        'num_questions': 10,
        'started_at': DateTime.now().millisecondsSinceEpoch ~/ 1000,
      });
      expect(db.getAllSessions().length, equals(2));
    });

    test('updateSession persists stats', () {
      db.insertSession({
        'session_id': 'upd-session',
        'num_questions': 5,
        'started_at': DateTime.now().millisecondsSinceEpoch ~/ 1000,
      });
      db.updateSession(
        sessionId: 'upd-session',
        numCorrect: 4,
        percentageCorrect: 80.0,
        endedAt: DateTime.now(),
        timeTakenSeconds: 120,
      );
      final s = db.getSession('upd-session')!;
      expect(s.numCorrect, equals(4));
      expect(s.percentageCorrect, closeTo(80.0, 0.01));
    });
  });

  group('responses', () {
    setUp(() {
      db.insertSession({
        'session_id': 'resp-session',
        'num_questions': 2,
        'started_at': DateTime.now().millisecondsSinceEpoch ~/ 1000,
      });
    });

    test('inserts and retrieves responses', () {
      db.insertResponse({
        'session_id': 'resp-session',
        'question_id': 1,
        'user_answer': 'A',
        'is_correct': 1,
        'time_taken_seconds': 10,
      });
      final responses = db.getResponsesForSession('resp-session');
      expect(responses.length, equals(1));
      expect(responses.first.userAnswer, equals('A'));
      expect(responses.first.correct, isTrue);
    });

    test('countCorrectResponses counts correctly', () {
      db.insertResponse({
        'session_id': 'resp-session',
        'question_id': 1,
        'user_answer': 'A',
        'is_correct': 1,
      });
      db.insertResponse({
        'session_id': 'resp-session',
        'question_id': 2,
        'user_answer': 'B',
        'is_correct': 0,
      });
      expect(db.countCorrectResponses('resp-session'), equals(1));
    });

    test('UNIQUE constraint prevents duplicate responses', () {
      final data = {
        'session_id': 'resp-session',
        'question_id': 1,
        'user_answer': 'A',
        'is_correct': 1,
      };
      db.insertResponse(data);
      // INSERT OR REPLACE — should not throw
      db.insertResponse({...data, 'user_answer': 'B', 'is_correct': 0});
      final responses = db.getResponsesForSession('resp-session');
      expect(responses.length, equals(1));
      expect(responses.first.userAnswer, equals('B'));
    });
  });

  group('clear operations', () {
    test('clearQuestions removes all questions', () {
      db.insertQuestion(sampleQuestion());
      db.clearQuestions();
      expect(db.countQuestions(), equals(0));
    });

    test('clearSessions removes all sessions', () {
      db.insertSession({
        'session_id': 'clr-s',
        'num_questions': 1,
        'started_at': DateTime.now().millisecondsSinceEpoch ~/ 1000,
      });
      db.clearSessions();
      expect(db.getAllSessions(), isEmpty);
    });
  });
}
