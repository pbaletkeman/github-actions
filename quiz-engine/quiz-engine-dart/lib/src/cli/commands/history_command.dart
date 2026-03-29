import 'dart:convert';
import 'dart:io';

import 'package:args/args.dart';

import '../../database/database.dart';
import '../../service/quiz_service.dart';
import '../../service/history_service.dart';
import '../formatter.dart';

/// Displays quiz session history.
int historyCommand(List<String> args, AppDatabase db) {
  final parser = ArgParser()
    ..addOption('session-id',
        abbr: 's', help: 'Show details for a specific session')
    ..addFlag('review',
        abbr: 'r',
        defaultsTo: false,
        help: 'Show full answer key for a session (requires --session-id)')
    ..addOption('export',
        abbr: 'e',
        allowed: ['json', 'csv'],
        help: 'Export history as json or csv')
    ..addFlag('help', abbr: 'h', negatable: false, help: 'Show help');

  ArgResults results;
  try {
    results = parser.parse(args);
  } on FormatException catch (e) {
    stderr.writeln(Formatter.error('Invalid argument: ${e.message}'));
    return 1;
  }

  if (results['help'] as bool) {
    print('Usage: quiz_engine history [options]\n${parser.usage}');
    return 0;
  }

  final service = QuizService(db);
  final history = HistoryService(service, db);

  final sessionId = results['session-id'] as String?;
  final exportFormat = results['export'] as String?;
  final review = results['review'] as bool;

  if (sessionId != null) {
    final session = service.getSession(sessionId);
    if (session == null) {
      stderr.writeln(Formatter.error('Session "$sessionId" not found.'));
      return 1;
    }

    if (review) {
      _printSessionReview(history, sessionId);
    } else {
      _printSessionDetail(session);
    }
    return 0;
  }

  final sessions = history.getSessions();
  if (sessions.isEmpty) {
    print(Formatter.info('No quiz sessions recorded yet.'));
    return 0;
  }

  if (exportFormat == 'json') {
    final data = history.exportSessions();
    print(const JsonEncoder.withIndent('  ').convert(data));
    return 0;
  }

  if (exportFormat == 'csv') {
    _exportCsv(sessions);
    return 0;
  }

  // Default: table view
  print(Formatter.table(
    ['Session ID', 'Date', 'Score', '%'],
    sessions
        .map((s) => [
              s.sessionId,
              s.startedAt.toLocal().toString().substring(0, 16),
              '${s.numCorrect}/${s.numQuestions}',
              '${s.percentageCorrect.toStringAsFixed(1)}%',
            ])
        .toList(),
  ));
  return 0;
}

void _printSessionDetail(dynamic session) {
  print(Formatter.boxed(
    'Session:    ${session.sessionId}\n'
    'Started:    ${session.startedAt.toLocal()}\n'
    'Ended:      ${session.endedAt?.toLocal() ?? "in progress"}\n'
    'Score:      ${session.numCorrect}/${session.numQuestions}\n'
    'Percentage: ${session.percentageCorrect.toStringAsFixed(1)}%\n'
    'Time taken: ${session.timeTakenSeconds != null ? "${session.timeTakenSeconds}s" : "N/A"}',
    title: 'Session Details',
  ));
}

void _printSessionReview(HistoryService history, String sessionId) {
  final responses = history.getResponses(sessionId);
  if (responses.isEmpty) {
    print(Formatter.info('No responses found for session.'));
    return;
  }
  final ids = responses.map((r) => r.questionId).toList();
  final questions = history.getQuestionsByIds(ids);

  print(Formatter.boxed('Answer review for session $sessionId',
      title: 'Review'));
  for (final r in responses) {
    final q = questions[r.questionId];
    final status = r.correct ? Formatter.success('✓') : Formatter.error('✗');
    print('$status  Q${r.questionId}: ${q?.questionText ?? "(unknown)"}');
    print(
        '     Your answer: ${r.userAnswer}  |  Correct: ${q?.correctAnswer ?? "?"}');
    if (!r.correct && q?.explanation != null) {
      print('     Explanation: ${q!.explanation}');
    }
    print('');
  }
}

void _exportCsv(List<dynamic> sessions) {
  print('session_id,started_at,ended_at,num_questions,num_correct,percentage');
  for (final s in sessions) {
    print([
      s.sessionId,
      s.startedAt.toIso8601String(),
      s.endedAt?.toIso8601String() ?? '',
      s.numQuestions,
      s.numCorrect,
      s.percentageCorrect.toStringAsFixed(2),
    ].join(','));
  }
}
