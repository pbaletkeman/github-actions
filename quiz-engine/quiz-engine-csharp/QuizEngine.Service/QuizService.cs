using QuizEngine.Data;
using QuizEngine.Entities;

namespace QuizEngine.Service;

public class ActiveQuizState
{
    public string SessionId { get; set; } = string.Empty;
    public List<Question> Questions { get; set; } = new();
    public List<ShuffleResult> ShuffleResults { get; set; } = new();
    public int CurrentIndex { get; set; } = 0;
    public DateTime StartedAt { get; set; } = DateTime.UtcNow;
}

public class AnswerResult
{
    public bool IsCorrect { get; set; }
    public string CorrectAnswer { get; set; } = string.Empty;
    public string? Explanation { get; set; }
    public int QuestionNumber { get; set; }
    public int TotalQuestions { get; set; }
}

public class QuizResult
{
    public string SessionId { get; set; } = string.Empty;
    public int NumQuestions { get; set; }
    public int NumCorrect { get; set; }
    public double PercentageCorrect { get; set; }
    public int TimeTakenSeconds { get; set; }
    public DateTime StartedAt { get; set; }
    public DateTime EndedAt { get; set; }
}

public class QuizService
{
    private readonly IQuestionRepository _questionRepo;
    private readonly ISessionRepository _sessionRepo;
    private readonly IResponseRepository _responseRepo;
    private readonly Dictionary<string, ActiveQuizState> _activeSessions = new();

    public QuizService(
        IQuestionRepository questionRepo,
        ISessionRepository sessionRepo,
        IResponseRepository responseRepo)
    {
        _questionRepo = questionRepo;
        _sessionRepo = sessionRepo;
        _responseRepo = responseRepo;
    }

    public async Task<ActiveQuizState> StartQuizAsync(int numQuestions, string? difficulty = null, string? section = null, bool noShuffle = false)
    {
        var questions = await _questionRepo.GetRandomQuestionsAsync(numQuestions, difficulty, section);

        if (questions.Count == 0)
            throw new InvalidOperationException("No questions available. Please import questions first.");

        var sessionId = Guid.NewGuid().ToString();
        var session = new QuizSession
        {
            SessionId = sessionId,
            NumQuestions = questions.Count,
            StartedAt = DateTime.UtcNow
        };

        await _sessionRepo.SaveAsync(session);

        // Shuffle answers for each question (unless --no-shuffle is set)
        var shuffleResults = questions
            .Select(q =>
            {
                var opts = AnswerShuffler.GetOptionsArray(q);
                if (noShuffle)
                    return AnswerShuffler.Identity(opts, q.CorrectAnswer);
                return AnswerShuffler.Shuffle(opts, q.CorrectAnswer);
            })
            .ToList();

        var state = new ActiveQuizState
        {
            SessionId = sessionId,
            Questions = questions,
            ShuffleResults = shuffleResults,
            StartedAt = session.StartedAt
        };

        _activeSessions[sessionId] = state;
        return state;
    }

    public async Task<AnswerResult> SubmitAnswerAsync(
        string sessionId, int questionIndex, string answer, int timeTakenSeconds)
    {
        if (!_activeSessions.TryGetValue(sessionId, out var state))
            throw new KeyNotFoundException($"Session {sessionId} not found.");

        if (questionIndex < 0 || questionIndex >= state.Questions.Count)
            throw new ArgumentOutOfRangeException(nameof(questionIndex));

        var question = state.Questions[questionIndex];
        var shuffle = state.ShuffleResults[questionIndex];

        // The answer is compared against the shuffled correct letter
        var isCorrect = string.Equals(answer.Trim(), shuffle.CorrectShuffledLetter, StringComparison.OrdinalIgnoreCase);

        var response = new QuizResponse
        {
            SessionId = sessionId,
            QuestionId = question.Id,
            UserAnswer = answer.ToUpper(),
            IsCorrect = isCorrect ? 1 : 0,
            TimeTakenSeconds = timeTakenSeconds
        };

        await _responseRepo.SaveAsync(response);

        return new AnswerResult
        {
            IsCorrect = isCorrect,
            CorrectAnswer = shuffle.CorrectShuffledLetter,
            Explanation = question.Explanation,
            QuestionNumber = questionIndex + 1,
            TotalQuestions = state.Questions.Count
        };
    }

    public async Task<QuizResult> FinalizeAsync(string sessionId)
    {
        if (!_activeSessions.TryGetValue(sessionId, out var state))
            throw new KeyNotFoundException($"Session {sessionId} not found.");

        // Mark all questions as used
        foreach (var q in state.Questions)
            await _questionRepo.MarkQuestionUsedAsync(q.Id);

        // Advance cycle if all questions are exhausted
        await _questionRepo.AdvanceCycleIfExhaustedAsync();

        var numCorrect = await _responseRepo.CountCorrectBySessionAsync(sessionId);
        var endedAt = DateTime.UtcNow;
        var timeTaken = (int)(endedAt - state.StartedAt).TotalSeconds;
        var percentage = state.Questions.Count > 0
            ? (numCorrect / (double)state.Questions.Count) * 100.0
            : 0.0;

        var session = new QuizSession
        {
            SessionId = sessionId,
            NumQuestions = state.Questions.Count,
            NumCorrect = numCorrect,
            PercentageCorrect = percentage,
            EndedAt = endedAt,
            TimeTakenSeconds = timeTaken,
            StartedAt = state.StartedAt
        };

        await _sessionRepo.SaveAsync(session);
        _activeSessions.Remove(sessionId);

        return new QuizResult
        {
            SessionId = sessionId,
            NumQuestions = state.Questions.Count,
            NumCorrect = numCorrect,
            PercentageCorrect = percentage,
            TimeTakenSeconds = timeTaken,
            StartedAt = state.StartedAt,
            EndedAt = endedAt
        };
    }

    public async Task<QuizSession?> GetSessionAsync(string sessionId)
    {
        return await _sessionRepo.GetByIdAsync(sessionId);
    }
}
