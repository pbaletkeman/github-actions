import 'dart:io';

import 'package:args/args.dart';

import '../../database/database.dart';
import '../../service/import_service.dart';
import '../../exceptions/quiz_exceptions.dart';
import '../formatter.dart';

/// Imports questions from markdown files into the database.
int importCommand(List<String> args, AppDatabase db) {
  final parser = ArgParser()
    ..addOption('file', abbr: 'f', help: 'Path to a markdown file to import')
    ..addOption('dir', abbr: 'd', help: 'Path to a directory of markdown files')
    ..addFlag('help', abbr: 'h', negatable: false, help: 'Show help');

  ArgResults results;
  try {
    results = parser.parse(args);
  } on FormatException catch (e) {
    stderr.writeln(Formatter.error('Invalid argument: ${e.message}'));
    return 1;
  }

  if (results['help'] as bool) {
    print('Usage: quiz_engine import [options]\n${parser.usage}');
    return 0;
  }

  final filePath = results['file'] as String?;
  final dirPath = results['dir'] as String?;

  if (filePath == null && dirPath == null) {
    stderr.writeln(Formatter.error('Specify --file or --dir.'));
    return 1;
  }

  final service = ImportService(db);
  var total = 0;

  try {
    if (filePath != null) {
      total += service.importFile(File(filePath));
    }
    if (dirPath != null) {
      total += service.importDirectory(Directory(dirPath));
    }
  } on ImportException catch (e) {
    stderr.writeln(Formatter.error(e.toString()));
    return 1;
  }

  print(Formatter.success('Imported $total question(s).'));
  return 0;
}
