using QuizEngine.Entities;

namespace QuizEngine.Data;

public interface IQuestionRepository
{
    Task<List<Question>> GetRandomQuestionsAsync(int count, string? difficulty = null, string? section = null);
    Task<List<Question>> GetAllAsync();
    Task<int> GetCurrentCycleAsync();
    Task<Question?> GetByIdAsync(int id);
    Task<Question> InsertAsync(Question question);
    Task MarkQuestionUsedAsync(int questionId);
    Task AdvanceCycleIfExhaustedAsync();
    Task<int> CountAsync();
    bool CheckAnswer(string correctAnswer, string submittedAnswer);
}
