using QuizEngine.Entities;

namespace QuizEngine.Data;

public interface ISessionRepository
{
    Task<QuizSession?> GetByIdAsync(string sessionId);
    Task<List<QuizSession>> GetAllAsync(int skip = 0, int take = 10);
    Task SaveAsync(QuizSession session);
    Task<int> CountAsync();
}
