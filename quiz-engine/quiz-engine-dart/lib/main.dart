import 'dart:io';

import 'package:args/args.dart';
import 'package:path/path.dart' as p;

import 'src/database/database.dart';
import 'src/cli/commands/quiz_command.dart';
import 'src/cli/commands/import_command.dart';
import 'src/cli/commands/history_command.dart';
import 'src/cli/commands/clear_command.dart';
import 'src/cli/formatter.dart';

void main(List<String> args) {
  final parser = ArgParser()
    ..addCommand('quiz', ArgParser.allowAnything())
    ..addCommand('import', ArgParser.allowAnything())
    ..addCommand('history', ArgParser.allowAnything())
    ..addCommand('clear', ArgParser.allowAnything())
    ..addOption('db',
        help: 'Path to SQLite database file',
        defaultsTo: p.join(
            p.dirname(Platform.resolvedExecutable), 'quiz_engine.db'))
    ..addFlag('help', abbr: 'h', negatable: false, help: 'Show help')
    ..addFlag('version', abbr: 'v', negatable: false, help: 'Show version');

  ArgResults results;
  try {
    results = parser.parse(args);
  } on FormatException catch (e) {
    stderr.writeln(Formatter.error('Invalid argument: ${e.message}'));
    stderr.writeln(parser.usage);
    exit(1);
  }

  if (results['version'] as bool) {
    print('quiz_engine_dart 1.0.0');
    exit(0);
  }

  final command = results.command;

  if (results['help'] as bool || command == null) {
    _printHelp(parser);
    exit(0);
  }

  final dbPath = results['db'] as String;
  final db = AppDatabase.open(dbPath);

  try {
    final exitCode = _dispatch(command.name!, command.rest, db);
    exit(exitCode);
  } finally {
    db.close();
  }
}

int _dispatch(String command, List<String> args, AppDatabase db) {
  switch (command) {
    case 'quiz':
      return quizCommand(args, db);
    case 'import':
      return importCommand(args, db);
    case 'history':
      return historyCommand(args, db);
    case 'clear':
      return clearCommand(args, db);
    default:
      stderr.writeln(Formatter.error('Unknown command: $command'));
      return 1;
  }
}

void _printHelp(ArgParser parser) {
  print('''
quiz_engine_dart — GitHub Actions certification quiz tool

Usage: quiz_engine <command> [options]

Commands:
  quiz      Start an interactive quiz session
  import    Import questions from markdown files
  history   View past quiz sessions
  clear     Remove data from the database

Global options:
${parser.usage}

Examples:
  quiz_engine import --file questions.md
  quiz_engine quiz --questions 20
  quiz_engine history
  quiz_engine clear --all --confirm
''');
}
