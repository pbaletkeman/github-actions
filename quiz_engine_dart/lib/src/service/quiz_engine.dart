import '../models/question.dart';
import '../models/quiz_session.dart';
import '../service/quiz_service.dart';
import '../service/answer_shuffler.dart';

/// Orchestrates a single quiz session end-to-end.
class QuizEngine {
  final String sessionId;
  final QuizService _quizService;
  final AnswerShuffler _shuffler;
  final int numQuestionsRequested;

  List<Question> _questions = [];
  final Map<int, ShuffleResult> _shuffleResults = {};
  int _numCorrect = 0;
  final DateTime _startedAt = DateTime.now();

  QuizEngine({
    required this.sessionId,
    required QuizService quizService,
    AnswerShuffler? shuffler,
    int numQuestions = 10,
  })  : _quizService = quizService,
        _shuffler = shuffler ?? AnswerShuffler(),
        numQuestionsRequested = numQuestions;

  /// Loaded question list (available after [loadQuestions]).
  List<Question> get questions => List.unmodifiable(_questions);

  /// Number of correct answers recorded so far.
  int get numCorrect => _numCorrect;

  /// Loads [numQuestionsRequested] questions and creates the session record.
  void loadQuestions() {
    _questions = _quizService.getRandomQuestions(numQuestionsRequested);
    _quizService.createSession(
      sessionId: sessionId,
      numQuestions: _questions.length,
    );
    // Pre-shuffle all answers
    for (var i = 0; i < _questions.length; i++) {
      _shuffleResults[i] = _shuffler.shuffle(_questions[i]);
    }
  }

  /// Returns the shuffle result for question at [index].
  ShuffleResult getShuffleResult(int index) {
    assert(index >= 0 && index < _questions.length,
        'Index out of range: $index');
    return _shuffleResults[index]!;
  }

  /// Records the user's answer for question at [index].
  /// [userAnswer] must be the *shuffled* label (e.g. 'A', 'B', …).
  void submitAnswer(int index, String userAnswer, {int timeTaken = 0}) {
    final question = _questions[index];
    final shuffleResult = _shuffleResults[index]!;
    final isCorrect =
        userAnswer.toUpperCase() == shuffleResult.correctLabel ? 1 : 0;
    if (isCorrect == 1) _numCorrect++;

    _quizService.saveResponse(
      sessionId: sessionId,
      questionId: question.id,
      userAnswer: userAnswer,
      isCorrect: isCorrect,
      timeTaken: timeTaken,
    );
  }

  /// Finalises the session and persists statistics.
  /// Returns the updated [QuizSession].
  QuizSession finalizeQuiz() {
    final endedAt = DateTime.now();
    final total = _questions.length;
    final correct = _quizService.countCorrectAnswers(sessionId);
    final percentage =
        total > 0 ? (correct / total) * 100.0 : 0.0;
    final timeTaken = endedAt.difference(_startedAt).inSeconds;

    // Mark each question as used and advance cycle if needed
    for (final q in _questions) {
      _quizService.markQuestionUsed(q.id);
    }
    _quizService.advanceCycleIfNeeded();

    _quizService.updateSession(
      sessionId: sessionId,
      numCorrect: correct,
      percentageCorrect: percentage,
      endedAt: endedAt,
      timeTakenSeconds: timeTaken,
    );

    return _quizService.getSession(sessionId)!;
  }
}
