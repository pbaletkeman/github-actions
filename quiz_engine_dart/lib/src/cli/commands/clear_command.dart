import 'dart:io';

import 'package:args/args.dart';

import '../../database/database.dart';
import '../../service/quiz_service.dart';
import '../formatter.dart';
import '../prompts.dart';

/// Clears quiz data from the database.
int clearCommand(List<String> args, AppDatabase db) {
  final parser = ArgParser()
    ..addFlag('questions',
        defaultsTo: false, help: 'Delete all questions')
    ..addFlag('history',
        defaultsTo: false, help: 'Delete all sessions and responses')
    ..addFlag('all', defaultsTo: false, help: 'Delete all data')
    ..addFlag('confirm',
        defaultsTo: false,
        help: 'Skip confirmation prompt')
    ..addFlag('help', abbr: 'h', negatable: false, help: 'Show help');

  ArgResults results;
  try {
    results = parser.parse(args);
  } on FormatException catch (e) {
    stderr.writeln(Formatter.error('Invalid argument: ${e.message}'));
    return 1;
  }

  if (results['help'] as bool) {
    print('Usage: quiz_engine clear [options]\n${parser.usage}');
    return 0;
  }

  final clearAll = results['all'] as bool;
  final clearQuestions = results['questions'] as bool;
  final clearHistory = results['history'] as bool;
  final skipConfirm = results['confirm'] as bool;

  if (!clearAll && !clearQuestions && !clearHistory) {
    stderr.writeln(Formatter.error(
        'Specify at least one of --questions, --history, or --all.'));
    return 1;
  }

  final what = clearAll
      ? 'ALL data'
      : [
          if (clearQuestions) 'questions',
          if (clearHistory) 'history',
        ].join(' and ');

  if (!skipConfirm) {
    final ok = Prompts.confirm(
        Formatter.warning('This will permanently delete $what. Are you sure?'));
    if (!ok) {
      print(Formatter.info('Aborted.'));
      return 0;
    }
  }

  final service = QuizService(db);
  if (clearAll) {
    service.clearAll();
  } else {
    if (clearQuestions) service.clearQuestions();
    if (clearHistory) service.clearHistory();
  }

  print(Formatter.success('Cleared $what.'));
  return 0;
}
