import 'dart:math';

import '../models/question.dart';

/// Result of shuffling a question's answer options.
class ShuffleResult {
  /// The shuffled list of option texts.
  final List<String> shuffledOptions;

  /// The letter labels for the shuffled options (A, B, C, D, [E]).
  final List<String> labels;

  /// The label that corresponds to the correct answer after shuffling.
  final String correctLabel;

  const ShuffleResult({
    required this.shuffledOptions,
    required this.labels,
    required this.correctLabel,
  });
}

/// Shuffles a question's answer options while tracking the correct answer.
class AnswerShuffler {
  final Random _random;

  AnswerShuffler({Random? random}) : _random = random ?? Random();

  /// Returns a [ShuffleResult] with randomised option order.
  ShuffleResult shuffle(Question question) {
    final options = _buildOptions(question);
    final correctIndex = _labelToIndex(question.correctAnswer);
    final correctText = options[correctIndex];

    // Shuffle the list
    final shuffled = List<String>.from(options);
    for (var i = shuffled.length - 1; i > 0; i--) {
      final j = _random.nextInt(i + 1);
      final tmp = shuffled[i];
      shuffled[i] = shuffled[j];
      shuffled[j] = tmp;
    }

    final labels = _labelsForCount(shuffled.length);
    final newCorrectIndex = shuffled.indexOf(correctText);
    final correctLabel = labels[newCorrectIndex];

    return ShuffleResult(
      shuffledOptions: shuffled,
      labels: labels,
      correctLabel: correctLabel,
    );
  }

  List<String> _buildOptions(Question question) {
    final opts = [
      question.optionA,
      question.optionB,
      question.optionC,
      question.optionD,
    ];
    if (question.optionE != null && question.optionE!.isNotEmpty) {
      opts.add(question.optionE!);
    }
    return opts;
  }

  static int _labelToIndex(String label) {
    return label.toUpperCase().codeUnitAt(0) - 'A'.codeUnitAt(0);
  }

  static List<String> _labelsForCount(int count) {
    return List.generate(count, (i) => String.fromCharCode('A'.codeUnitAt(0) + i));
  }
}
