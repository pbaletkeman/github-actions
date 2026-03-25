import 'package:test/test.dart';
import 'package:quiz_engine_dart/src/service/answer_shuffler.dart';
import 'package:quiz_engine_dart/src/models/question.dart';

Question _makeQuestion({
  String optionA = 'Alpha',
  String optionB = 'Beta',
  String optionC = 'Gamma',
  String optionD = 'Delta',
  String? optionE,
  String correctAnswer = 'A',
}) {
  return Question(
    id: 1,
    questionText: 'Test?',
    optionA: optionA,
    optionB: optionB,
    optionC: optionC,
    optionD: optionD,
    optionE: optionE,
    correctAnswer: correctAnswer,
    createdAt: DateTime.now(),
  );
}

void main() {
  late AnswerShuffler shuffler;

  setUp(() => shuffler = AnswerShuffler());

  group('shuffle', () {
    test('preserves all original options', () {
      final q = _makeQuestion();
      final result = shuffler.shuffle(q);
      final expected = {'Alpha', 'Beta', 'Gamma', 'Delta'};
      expect(result.shuffledOptions.toSet(), equals(expected));
    });

    test('returns correct number of options (4)', () {
      final q = _makeQuestion();
      final result = shuffler.shuffle(q);
      expect(result.shuffledOptions.length, equals(4));
    });

    test('returns 5 options when optionE is present', () {
      final q = _makeQuestion(optionE: 'Epsilon');
      final result = shuffler.shuffle(q);
      expect(result.shuffledOptions.length, equals(5));
    });

    test('maps correct answer to new shuffled position', () {
      final q = _makeQuestion(correctAnswer: 'A');
      final result = shuffler.shuffle(q);
      final correctIndex =
          result.labels.indexOf(result.correctLabel);
      expect(result.shuffledOptions[correctIndex], equals('Alpha'));
    });

    test('correctLabel is one of the valid labels', () {
      final q = _makeQuestion();
      final result = shuffler.shuffle(q);
      expect(result.labels, contains(result.correctLabel));
    });

    test('labels are A–D for 4 options', () {
      final q = _makeQuestion();
      final result = shuffler.shuffle(q);
      expect(result.labels, equals(['A', 'B', 'C', 'D']));
    });

    test('labels are A–E for 5 options', () {
      final q = _makeQuestion(optionE: 'Epsilon');
      final result = shuffler.shuffle(q);
      expect(result.labels, equals(['A', 'B', 'C', 'D', 'E']));
    });

    test('shuffles answer B correctly', () {
      final q = _makeQuestion(correctAnswer: 'B');
      final result = shuffler.shuffle(q);
      final correctIndex = result.labels.indexOf(result.correctLabel);
      expect(result.shuffledOptions[correctIndex], equals('Beta'));
    });

    test('shuffles answer C correctly', () {
      final q = _makeQuestion(correctAnswer: 'C');
      final result = shuffler.shuffle(q);
      final correctIndex = result.labels.indexOf(result.correctLabel);
      expect(result.shuffledOptions[correctIndex], equals('Gamma'));
    });

    test('shuffles answer D correctly', () {
      final q = _makeQuestion(correctAnswer: 'D');
      final result = shuffler.shuffle(q);
      final correctIndex = result.labels.indexOf(result.correctLabel);
      expect(result.shuffledOptions[correctIndex], equals('Delta'));
    });

    test('shuffles answer E correctly for 5-option question', () {
      final q = _makeQuestion(optionE: 'Epsilon', correctAnswer: 'E');
      final result = shuffler.shuffle(q);
      final correctIndex = result.labels.indexOf(result.correctLabel);
      expect(result.shuffledOptions[correctIndex], equals('Epsilon'));
    });
  });
}
