import 'dart:io';

import 'package:args/args.dart';
import 'package:uuid/uuid.dart';

import '../../database/database.dart';
import '../../models/question.dart';
import '../../service/quiz_engine.dart';
import '../../service/quiz_service.dart';
import '../../service/answer_shuffler.dart';
import '../../exceptions/quiz_exceptions.dart';
import '../formatter.dart';
import '../prompts.dart';

/// Runs an interactive quiz session.
int quizCommand(List<String> args, AppDatabase db) {
  final parser = ArgParser()
    ..addOption('questions',
        abbr: 'q',
        defaultsTo: '10',
        help: 'Number of questions to ask (default: 10)')
    ..addFlag('no-shuffle',
        defaultsTo: false, help: 'Disable answer shuffling')
    ..addFlag('help', abbr: 'h', negatable: false, help: 'Show help');

  ArgResults results;
  try {
    results = parser.parse(args);
  } on FormatException catch (e) {
    stderr.writeln(Formatter.error('Invalid argument: ${e.message}'));
    return 1;
  }

  if (results['help'] as bool) {
    print('Usage: quiz_engine quiz [options]\n${parser.usage}');
    return 0;
  }

  int numQuestions;
  try {
    numQuestions = int.parse(results['questions'] as String);
    if (numQuestions < 1) throw const FormatException('Must be >= 1');
  } on FormatException {
    stderr.writeln(Formatter.error(
        '--questions must be a positive integer (got "${results['questions']}")'));
    return 1;
  }

  final service = QuizService(db);
  final sessionId = const Uuid().v4();
  final engine = QuizEngine(
    sessionId: sessionId,
    quizService: service,
    shuffler: results['no-shuffle'] as bool ? _NoShuffleAdapter() : null,
    numQuestions: numQuestions,
  );

  try {
    engine.loadQuestions();
  } on NoQuestionsException {
    stderr.writeln(Formatter.error(
        'No questions in database. Import some first with: quiz_engine import'));
    return 1;
  } on InsufficientQuestionsException catch (e) {
    stderr.writeln(Formatter.warning(
        'Only ${e.available} questions available (requested $numQuestions). '
        'Continuing with ${e.available}.'));
    // Re-create with available count
    final fallbackEngine = QuizEngine(
      sessionId: sessionId,
      quizService: service,
      numQuestions: e.available,
    );
    fallbackEngine.loadQuestions();
    return _runQuiz(fallbackEngine);
  }

  return _runQuiz(engine);
}

int _runQuiz(QuizEngine engine) {
  print(Formatter.boxed(
      'Quiz started — ${engine.questions.length} questions\n'
      'Type the letter of your answer and press ENTER.',
      title: 'GitHub Actions Quiz Engine'));

  final results = <_QuestionResult>[];

  for (var i = 0; i < engine.questions.length; i++) {
    final question = engine.questions[i];
    final shuffle = engine.getShuffleResult(i);

    print('');
    print(Formatter.info(
        'Q${i + 1}/${engine.questions.length}  '
        '[Section: ${question.section ?? "General"}]'));
    print('');
    print(question.questionText);
    print('');

    for (var j = 0; j < shuffle.shuffledOptions.length; j++) {
      print('  ${shuffle.labels[j]}) ${shuffle.shuffledOptions[j]}');
    }
    print('');

    final answer = Prompts.readAnswer(shuffle.labels);
    engine.submitAnswer(i, answer);

    results.add(_QuestionResult(
      number: i + 1,
      questionText: question.questionText,
      userAnswer: answer,
      correctLabel: shuffle.correctLabel,
      explanation: question.explanation,
    ));
  }

  // Display full review after all questions have been answered
  print('');
  print(Formatter.boxed('Review of all answers:', title: 'Answer Review'));
  for (final r in results) {
    print('');
    print(Formatter.info('Q${r.number}: ${r.questionText}'));
    if (r.isCorrect) {
      print(Formatter.success('  Your answer: ${r.userAnswer} — Correct!'));
    } else {
      print(Formatter.error(
          '  Your answer: ${r.userAnswer} — Incorrect. '
          'Correct answer: ${r.correctLabel}'));
      if (r.explanation != null) {
        print('  Explanation: ${r.explanation}');
      }
    }
  }

  final session = engine.finalizeQuiz();
  _printResults(session);
  return 0;
}

void _printResults(dynamic session) {
  print('');
  print(Formatter.boxed(
    'Score: ${session.numCorrect}/${session.numQuestions}\n'
    'Percentage: ${session.percentageCorrect.toStringAsFixed(1)}%\n'
    'Session ID: ${session.sessionId}',
    title: 'Quiz Complete',
  ));
}

class _QuestionResult {
  final int number;
  final String questionText;
  final String userAnswer;
  final String correctLabel;
  final String? explanation;

  _QuestionResult({
    required this.number,
    required this.questionText,
    required this.userAnswer,
    required this.correctLabel,
    this.explanation,
  });

  bool get isCorrect => userAnswer.toUpperCase() == correctLabel.toUpperCase();
}

/// A shuffler that leaves answers in their original order.
class _NoShuffleAdapter extends AnswerShuffler {
  _NoShuffleAdapter() : super();

  @override
  ShuffleResult shuffle(Question question) {
    final options = [
      question.optionA,
      question.optionB,
      question.optionC,
      question.optionD,
      if (question.optionE != null && question.optionE!.isNotEmpty)
        question.optionE!,
    ];
    final labels = List.generate(
        options.length,
        (i) => String.fromCharCode('A'.codeUnitAt(0) + i));
    return ShuffleResult(
      shuffledOptions: options,
      labels: labels,
      correctLabel: question.correctAnswer.toUpperCase(),
    );
  }
}
