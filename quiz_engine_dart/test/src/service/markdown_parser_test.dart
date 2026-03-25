import 'package:test/test.dart';
import 'package:quiz_engine_dart/src/service/markdown_parser.dart';

void main() {
  late MarkdownParser parser;

  setUp(() => parser = MarkdownParser());

  const sampleContent = '''
# GitHub Actions Basics

## Question 1
What does CI stand for?

A) Continuous Integration
B) Code Import
C) Compile
D) Configure

**Answer:** A
**Explanation:** CI stands for Continuous Integration.

## Question 2
What is a workflow in GitHub Actions?

A) A shell script
B) An automated process triggered by events
C) A Docker image
D) A repository branch

**Answer:** B
**Explanation:** Workflows are automated processes.
''';

  group('parseContent', () {
    test('parses correct number of questions', () {
      final questions = parser.parseContent(sampleContent);
      expect(questions.length, equals(2));
    });

    test('extracts question text', () {
      final questions = parser.parseContent(sampleContent);
      expect(questions.first['question_text'],
          equals('What does CI stand for?'));
    });

    test('extracts all four options', () {
      final questions = parser.parseContent(sampleContent);
      final q = questions.first;
      expect(q['option_a'], equals('Continuous Integration'));
      expect(q['option_b'], equals('Code Import'));
      expect(q['option_c'], equals('Compile'));
      expect(q['option_d'], equals('Configure'));
    });

    test('extracts correct answer', () {
      final questions = parser.parseContent(sampleContent);
      expect(questions.first['correct_answer'], equals('A'));
    });

    test('extracts explanation', () {
      final questions = parser.parseContent(sampleContent);
      expect(questions.first['explanation'],
          equals('CI stands for Continuous Integration.'));
    });

    test('extracts section from heading', () {
      final questions = parser.parseContent(sampleContent);
      expect(questions.first['section'], equals('GitHub Actions Basics'));
    });

    test('stores sourceFile when provided', () {
      final questions = parser.parseContent(sampleContent,
          sourceFile: 'questions.md');
      expect(questions.first['source_file'], equals('questions.md'));
    });

    test('sets default usage_cycle to 1', () {
      final questions = parser.parseContent(sampleContent);
      expect(questions.first['usage_cycle'], equals(1));
    });

    test('returns empty list when no questions found', () {
      final questions = parser.parseContent('# No questions here\nJust text.');
      expect(questions, isEmpty);
    });

    test('skips block with fewer than 4 options', () {
      const content = '''
## Question 1
Incomplete question?

A) Option A
B) Option B

**Answer:** A
''';
      final questions = parser.parseContent(content);
      expect(questions, isEmpty);
    });

    test('skips block with no answer marker', () {
      const content = '''
## Question 1
No answer question?

A) Option A
B) Option B
C) Option C
D) Option D
''';
      final questions = parser.parseContent(content);
      expect(questions, isEmpty);
    });

    test('parses multiple questions in sequence', () {
      final questions = parser.parseContent(sampleContent);
      expect(questions[1]['question_text'],
          equals('What is a workflow in GitHub Actions?'));
      expect(questions[1]['correct_answer'], equals('B'));
    });

    test('handles lowercase answer letter', () {
      const content = '''
## Question 1
Question?

A) Option A
B) Option B
C) Option C
D) Option D

**Answer:** c
''';
      final questions = parser.parseContent(content);
      expect(questions.first['correct_answer'], equals('C'));
    });

    test('parses E option when present', () {
      const content = '''
## Question 1
Five option question?

A) Option A
B) Option B
C) Option C
D) Option D
E) Option E

**Answer:** E
''';
      final questions = parser.parseContent(content);
      expect(questions.first['option_e'], equals('Option E'));
      expect(questions.first['correct_answer'], equals('E'));
    });
  });
}
