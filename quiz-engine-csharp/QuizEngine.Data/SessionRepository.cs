using Microsoft.EntityFrameworkCore;
using QuizEngine.Entities;

namespace QuizEngine.Data;

public class SessionRepository : ISessionRepository
{
    private readonly QuizEngineDbContext _context;

    public SessionRepository(QuizEngineDbContext context) => _context = context;

    public async Task<QuizSession?> GetByIdAsync(string sessionId)
    {
        return await _context.QuizSessions
            .Include(s => s.Responses)
            .ThenInclude(r => r.Question)
            .FirstOrDefaultAsync(s => s.SessionId == sessionId);
    }

    public async Task<List<QuizSession>> GetAllAsync(int skip = 0, int take = 10)
    {
        return await _context.QuizSessions
            .OrderByDescending(s => s.StartedAt)
            .Skip(skip)
            .Take(take)
            .AsNoTracking()
            .ToListAsync();
    }

    public async Task SaveAsync(QuizSession session)
    {
        var existing = await _context.QuizSessions
            .FirstOrDefaultAsync(s => s.SessionId == session.SessionId);

        if (existing == null)
            _context.QuizSessions.Add(session);
        else
        {
            existing.EndedAt = session.EndedAt;
            existing.NumCorrect = session.NumCorrect;
            existing.PercentageCorrect = session.PercentageCorrect;
            existing.TimeTakenSeconds = session.TimeTakenSeconds;
        }

        await _context.SaveChangesAsync();
    }

    public async Task<int> CountAsync()
    {
        return await _context.QuizSessions.CountAsync();
    }
}
