import 'dart:io';

import '../exceptions/quiz_exceptions.dart';

/// Parses markdown files that contain quiz questions.
///
/// Expected format:
/// ```
/// ## Question 1
/// What is CI?
///
/// A) Continuous Integration
/// B) Code Import
/// C) Compile
/// D) Configure
///
/// **Answer:** A
/// **Explanation:** CI stands for Continuous Integration.
/// ```
class MarkdownParser {
  static final _questionHeaderRe = RegExp(
    r'##\s+Question\s+\d+',
    multiLine: true,
  );
  static final _optionRe = RegExp(r'^([A-Ea-e])[).]\s*(.+)$', multiLine: true);
  static final _answerRe = RegExp(r'\*\*Answer:\*\*\s*([A-Ea-e])', multiLine: true);
  static final _explanationRe =
      RegExp(r'\*\*Explanation:\*\*\s*(.+)', multiLine: true);
  static final _sectionRe = RegExp(r'^#\s+(.+)', multiLine: true);

  /// Parses [file] and returns the list of question data maps.
  List<Map<String, dynamic>> parseFile(File file) {
    if (!file.existsSync()) {
      throw ImportException(path: file.path, reason: 'File does not exist');
    }
    final content = file.readAsStringSync();
    return parseContent(content, sourceFile: file.path);
  }

  /// Parses [content] string and returns question data maps.
  List<Map<String, dynamic>> parseContent(
    String content, {
    String? sourceFile,
  }) {
    final questions = <Map<String, dynamic>>[];
    String? currentSection;

    // Extract top-level section heading if present (single # headings)
    for (final m in _sectionRe.allMatches(content)) {
      final heading = m.group(1)!.trim();
      if (!heading.startsWith('Question')) {
        currentSection = heading;
        break;
      }
    }

    // Split on question headers to get individual blocks
    final headerMatches = _questionHeaderRe.allMatches(content).toList();
    for (var i = 0; i < headerMatches.length; i++) {
      final start = headerMatches[i].end;
      final end =
          i + 1 < headerMatches.length ? headerMatches[i + 1].start : content.length;
      final block = content.substring(start, end);
      final questionMap =
          _parseBlock(block, section: currentSection, sourceFile: sourceFile);
      if (questionMap != null) {
        questions.add(questionMap);
      }
    }
    return questions;
  }

  Map<String, dynamic>? _parseBlock(
    String block, {
    String? section,
    String? sourceFile,
  }) {
    final lines = block.split('\n').map((l) => l.trim()).toList();

    // First non-empty line is the question text
    final textLines =
        lines.where((l) => l.isNotEmpty && !l.startsWith(RegExp(r'[A-Ea-e][).]'))).toList();

    String questionText = '';
    for (final l in textLines) {
      if (!l.startsWith('**')) {
        questionText = l;
        break;
      }
    }
    if (questionText.isEmpty) return null;

    final options = <String, String>{};
    for (final m in _optionRe.allMatches(block)) {
      options[m.group(1)!.toUpperCase()] = m.group(2)!.trim();
    }

    if (options.length < 4) return null;

    final answerMatch = _answerRe.firstMatch(block);
    if (answerMatch == null) return null;
    final correctAnswer = answerMatch.group(1)!.toUpperCase();

    final explanationMatch = _explanationRe.firstMatch(block);
    final explanation = explanationMatch?.group(1)?.trim();

    return {
      'question_text': questionText,
      'option_a': options['A'] ?? '',
      'option_b': options['B'] ?? '',
      'option_c': options['C'] ?? '',
      'option_d': options['D'] ?? '',
      'option_e': options['E'],
      'correct_answer': correctAnswer,
      'explanation': explanation,
      'section': section,
      'difficulty': null,
      'source_file': sourceFile,
      'usage_cycle': 1,
      'times_used': 0,
    };
  }
}
