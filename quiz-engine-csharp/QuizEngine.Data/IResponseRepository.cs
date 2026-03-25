using QuizEngine.Entities;

namespace QuizEngine.Data;

public interface IResponseRepository
{
    Task SaveAsync(QuizResponse response);
    Task<List<QuizResponse>> GetBySessionIdAsync(string sessionId);
    Task<int> CountCorrectBySessionAsync(string sessionId);
}
