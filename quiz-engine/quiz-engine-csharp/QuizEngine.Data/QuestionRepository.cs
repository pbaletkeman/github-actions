using Microsoft.EntityFrameworkCore;
using QuizEngine.Entities;

namespace QuizEngine.Data;

public class QuestionRepository : IQuestionRepository
{
    private readonly QuizEngineDbContext _context;

    public QuestionRepository(QuizEngineDbContext context) => _context = context;

    public async Task<int> GetCurrentCycleAsync()
    {
        var minCycle = await _context.Questions
            .MinAsync(q => (int?)q.UsageCycle) ?? 1;
        return minCycle;
    }

    public async Task<List<Question>> GetAllAsync()
    {
        return await _context.Questions.AsNoTracking().ToListAsync();
    }

    public async Task<List<Question>> GetRandomQuestionsAsync(
        int count, string? difficulty = null, string? section = null)
    {
        var cycle = await GetCurrentCycleAsync();

        var query = _context.Questions
            .Where(q => q.UsageCycle == cycle)
            .AsNoTracking();

        if (!string.IsNullOrEmpty(difficulty))
            query = query.Where(q => q.Difficulty == difficulty);

        if (!string.IsNullOrEmpty(section))
            query = query.Where(q => q.Section == section);

        return await query
            .OrderBy(q => EF.Functions.Random())
            .Take(count)
            .ToListAsync();
    }

    public async Task<Question?> GetByIdAsync(int id)
    {
        return await _context.Questions.FindAsync(id);
    }

    public async Task<Question> InsertAsync(Question question)
    {
        // Check for duplicate by QuestionText + CorrectAnswer
        var existing = await _context.Questions
            .FirstOrDefaultAsync(q =>
                q.QuestionText == question.QuestionText &&
                q.CorrectAnswer == question.CorrectAnswer);

        if (existing != null)
            return existing;

        _context.Questions.Add(question);
        await _context.SaveChangesAsync();
        return question;
    }

    public async Task MarkQuestionUsedAsync(int questionId)
    {
        var question = await _context.Questions.FindAsync(questionId);
        if (question != null)
        {
            question.TimesUsed++;
            question.LastUsedAt = DateTime.UtcNow;
            await _context.SaveChangesAsync();
        }
    }

    public async Task AdvanceCycleIfExhaustedAsync()
    {
        var cycle = await GetCurrentCycleAsync();
        var remaining = await _context.Questions
            .Where(q => q.UsageCycle == cycle && q.TimesUsed == 0)
            .CountAsync();

        if (remaining == 0)
        {
            var exhausted = await _context.Questions
                .Where(q => q.UsageCycle == cycle)
                .ToListAsync();

            foreach (var q in exhausted)
            {
                q.UsageCycle++;
                q.TimesUsed = 0;
            }

            await _context.SaveChangesAsync();
        }
    }

    public async Task<int> CountAsync()
    {
        return await _context.Questions.CountAsync();
    }

    public bool CheckAnswer(string correctAnswer, string submittedAnswer)
    {
        return string.Equals(correctAnswer.Trim(), submittedAnswer.Trim(), StringComparison.OrdinalIgnoreCase);
    }
}
