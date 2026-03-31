using System.Text;
using System.Text.Json;
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

    public async Task<List<QuizSession>> GetRecentSessionsAsync(int count = 10, string sortBy = "date", string order = "desc")
    {
        var sessions = await _sessionRepo.GetAllAsync(0, count);

        sessions = (sortBy.ToLower(), order.ToLower()) switch
        {
            ("score", "asc") => sessions.OrderBy(s => s.PercentageCorrect).ToList(),
            ("score", _) => sessions.OrderByDescending(s => s.PercentageCorrect).ToList(),
            ("questions", "asc") => sessions.OrderBy(s => s.NumQuestions).ToList(),
            ("questions", _) => sessions.OrderByDescending(s => s.NumQuestions).ToList(),
            ("time", "asc") => sessions.OrderBy(s => s.TimeTakenSeconds ?? int.MaxValue).ToList(),
            ("time", _) => sessions.OrderByDescending(s => s.TimeTakenSeconds ?? 0).ToList(),
            ("date", "asc") => sessions.OrderBy(s => s.StartedAt).ToList(),
            (_, _) => sessions.OrderByDescending(s => s.StartedAt).ToList(),
        };

        return sessions;
    }

    public async Task<(QuizSession? Session, List<QuizResponse> Responses)> GetSessionDetailAsync(string sessionId)
    {
        // Try exact match first
        var session = await _sessionRepo.GetByIdAsync(sessionId);
        if (session == null)
        {
            // Try prefix match
            var allSessions = await _sessionRepo.GetAllAsync(0, 1000);
            var matches = allSessions.Where(s => s.SessionId.StartsWith(sessionId, StringComparison.OrdinalIgnoreCase)).ToList();

            if (matches.Count == 1)
            {
                session = matches[0];
            }
            else if (matches.Count > 1)
            {
                // Multiple matches - return null with error message
                return (null, new List<QuizResponse>());
            }
            else
            {
                // No matches at all
                return (null, new List<QuizResponse>());
            }
        }

        var responses = await _responseRepo.GetBySessionIdAsync(session.SessionId);
        return (session, responses);
    }

    public async Task<int> GetTotalSessionsAsync()
    {
        return await _sessionRepo.CountAsync();
    }

    public async Task ExportToJsonAsync(IEnumerable<QuizSession> sessions, string path)
    {
        var records = new List<object>();
        foreach (var session in sessions)
        {
            var responses = await _responseRepo.GetBySessionIdAsync(session.SessionId);
            records.Add(new
            {
                session_id = session.SessionId,
                date = session.StartedAt,
                score = session.NumCorrect,
                total_questions = session.NumQuestions,
                percentage = session.PercentageCorrect,
                responses = responses.Select(r => new
                {
                    question_id = r.QuestionId,
                    selected_answer = r.UserAnswer,
                    was_correct = r.IsCorrect == 1
                })
            });
        }

        var json = JsonSerializer.Serialize(records, new JsonSerializerOptions { WriteIndented = true });
        await File.WriteAllTextAsync(path, json);
    }

    public async Task ExportToCsvAsync(IEnumerable<QuizSession> sessions, string path)
    {
        var sb = new StringBuilder();
        sb.AppendLine("session_id,date,score,total_questions,percentage,question_id,selected_answer,was_correct");

        foreach (var session in sessions)
        {
            var responses = await _responseRepo.GetBySessionIdAsync(session.SessionId);
            if (!responses.Any())
            {
                sb.AppendLine($"{session.SessionId},{session.StartedAt:o},{session.NumCorrect},{session.NumQuestions},{session.PercentageCorrect:F2},,, ");
            }
            else
            {
                foreach (var r in responses)
                {
                    sb.AppendLine($"{session.SessionId},{session.StartedAt:o},{session.NumCorrect},{session.NumQuestions},{session.PercentageCorrect:F2},{r.QuestionId},{r.UserAnswer},{r.IsCorrect == 1}");
                }
            }
        }

        await File.WriteAllTextAsync(path, sb.ToString());
    }
}
