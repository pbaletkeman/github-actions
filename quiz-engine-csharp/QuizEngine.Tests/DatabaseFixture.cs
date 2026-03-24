using Microsoft.EntityFrameworkCore;
using QuizEngine.Data;
using QuizEngine.Entities;

namespace QuizEngine.Tests;

public class DatabaseFixture : IDisposable
{
    public QuizEngineDbContext Context { get; }

    public DatabaseFixture()
    {
        var options = new DbContextOptionsBuilder<QuizEngineDbContext>()
            .UseInMemoryDatabase(databaseName: Guid.NewGuid().ToString())
            .Options;
        Context = new QuizEngineDbContext(options);
        Context.Database.EnsureCreated();
    }

    public void Dispose()
    {
        Context.Dispose();
        GC.SuppressFinalize(this);
    }

    public static Question BuildSampleQuestion(string? correctAnswer = "A", int n = 1) => new()
    {
        QuestionText = $"What is CI/CD? (Q{n})",
        OptionA = "Continuous Integration/Continuous Delivery",
        OptionB = "Code Integration/Code Deployment",
        OptionC = "Continuous Improvement/Continuous Development",
        OptionD = "Code Intelligence/Code Distribution",
        CorrectAnswer = correctAnswer ?? "A",
        Explanation = "CI/CD stands for Continuous Integration/Continuous Delivery.",
        Section = "GitHub Actions",
        Difficulty = "easy",
        SourceFile = "test.md"
    };
}
