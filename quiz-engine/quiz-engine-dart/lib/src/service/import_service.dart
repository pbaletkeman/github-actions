import 'dart:io';

import '../database/database.dart';
import '../service/markdown_parser.dart';
import '../exceptions/quiz_exceptions.dart';

/// Handles bulk import of questions from markdown files.
class ImportService {
  final AppDatabase _db;
  final MarkdownParser _parser;

  ImportService(this._db, {MarkdownParser? parser})
      : _parser = parser ?? MarkdownParser();

  /// Imports all questions from [file].
  /// Returns the number of questions successfully imported.
  int importFile(File file) {
    final questions = _parser.parseFile(file);
    var count = 0;
    for (final q in questions) {
      _db.insertQuestionIfNotExists(q);
      count++;
    }
    return count;
  }

  /// Imports all markdown files from [directory].
  /// Returns the total number of questions imported.
  int importDirectory(Directory directory) {
    if (!directory.existsSync()) {
      throw ImportException(
        path: directory.path,
        reason: 'Directory does not exist',
      );
    }
    var total = 0;
    for (final entity in directory.listSync(recursive: false)) {
      if (entity is File &&
          (entity.path.endsWith('.md') ||
              entity.path.endsWith('.markdown'))) {
        total += importFile(entity);
      }
    }
    return total;
  }
}
