using Microsoft.EntityFrameworkCore;
using QuizEngine.Entities;

namespace QuizEngine.Data;

public class ResponseRepository : IResponseRepository
{
    private readonly QuizEngineDbContext _context;

    public ResponseRepository(QuizEngineDbContext context) => _context = context;

    public async Task SaveAsync(QuizResponse response)
    {
        _context.QuizResponses.Add(response);
        await _context.SaveChangesAsync();
    }

    public async Task<List<QuizResponse>> GetBySessionIdAsync(string sessionId)
    {
        return await _context.QuizResponses
            .Where(r => r.SessionId == sessionId)
            .Include(r => r.Question)
            .AsNoTracking()
            .ToListAsync();
    }

    public async Task<int> CountCorrectBySessionAsync(string sessionId)
    {
        return await _context.QuizResponses
            .Where(r => r.SessionId == sessionId && r.IsCorrect == 1)
            .CountAsync();
    }
}
