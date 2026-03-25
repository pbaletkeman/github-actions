using QuizEngine.Data;
using QuizEngine.Entities;

namespace QuizEngine.Service;

public class HistoryService
{
    private readonly ISessionRepository _sessionRepo;
    private readonly IResponseRepository _responseRepo;

    public HistoryService(ISessionRepository sessionRepo, IResponseRepository responseRepo)
    {
        _sessionRepo = sessionRepo;
        _responseRepo = responseRepo;
    }

    public async Task<List<QuizSession>> GetRecentSessionsAsync(int count = 10)
    {
        return await _sessionRepo.GetAllAsync(0, count);
    }

    public async Task<(QuizSession? Session, List<QuizResponse> Responses)> GetSessionDetailAsync(string sessionId)
    {
        var session = await _sessionRepo.GetByIdAsync(sessionId);
        if (session == null)
            return (null, new List<QuizResponse>());

        var responses = await _responseRepo.GetBySessionIdAsync(sessionId);
        return (session, responses);
    }

    public async Task<int> GetTotalSessionsAsync()
    {
        return await _sessionRepo.CountAsync();
    }
}
