using Microsoft.EntityFrameworkCore;
using QuizEngine.Data;
using QuizEngine.Service;
using Xunit;

namespace QuizEngine.Tests;

public class QuizEngineTests
{
    private static QuizEngineDbContext CreateContext() =>
        new(new DbContextOptionsBuilder<QuizEngineDbContext>()
            .UseInMemoryDatabase(Guid.NewGuid().ToString())
            .Options);

    private static (QuizService service, QuizEngineDbContext ctx) CreateService()
    {
        var ctx = CreateContext();
        ctx.Database.EnsureCreated();
        var questionRepo = new QuestionRepository(ctx);
        var sessionRepo = new SessionRepository(ctx);
        var responseRepo = new ResponseRepository(ctx);
        var service = new QuizService(questionRepo, sessionRepo, responseRepo);
        return (service, ctx);
    }

    private static async Task SeedQuestionsAsync(QuizEngineDbContext ctx, int count)
    {
        var repo = new QuestionRepository(ctx);
        for (int i = 0; i < count; i++)
        {
            await repo.InsertAsync(DatabaseFixture.BuildSampleQuestion(n: i + 1));
        }
    }

    [Fact]
    public async Task StartQuizAsync_ThrowsWhenNoQuestions()
    {
        var (service, _) = CreateService();
        await Assert.ThrowsAsync<InvalidOperationException>(() =>
            service.StartQuizAsync(5));
    }

    [Fact]
    public async Task StartQuizAsync_ReturnsStateWithQuestions()
    {
        var (service, ctx) = CreateService();
        await SeedQuestionsAsync(ctx, 5);

        var state = await service.StartQuizAsync(3);
        Assert.NotNull(state);
        Assert.Equal(3, state.Questions.Count);
        Assert.NotEmpty(state.SessionId);
    }

    [Fact]
    public async Task StartQuizAsync_ReturnsAllAvailable_WhenFewerThanRequested()
    {
        var (service, ctx) = CreateService();
        await SeedQuestionsAsync(ctx, 2);

        var state = await service.StartQuizAsync(10);
        Assert.Equal(2, state.Questions.Count);
    }

    [Fact]
    public async Task StartQuizAsync_ShufflesAnswers()
    {
        var (service, ctx) = CreateService();
        await SeedQuestionsAsync(ctx, 5);

        var state = await service.StartQuizAsync(5);
        Assert.Equal(state.Questions.Count, state.ShuffleResults.Count);
        Assert.All(state.ShuffleResults, sr =>
        {
            Assert.NotEmpty(sr.ShuffledOptions);
            Assert.NotEmpty(sr.CorrectShuffledLetter);
        });
    }

    [Fact]
    public async Task SubmitAnswerAsync_ThrowsWhenSessionNotFound()
    {
        var (service, _) = CreateService();
        await Assert.ThrowsAsync<KeyNotFoundException>(() =>
            service.SubmitAnswerAsync("nonexistent-session", 0, "A", 10));
    }

    [Fact]
    public async Task SubmitAnswerAsync_ThrowsWhenIndexOutOfRange()
    {
        var (service, ctx) = CreateService();
        await SeedQuestionsAsync(ctx, 2);
        var state = await service.StartQuizAsync(2);

        await Assert.ThrowsAsync<ArgumentOutOfRangeException>(() =>
            service.SubmitAnswerAsync(state.SessionId, 99, "A", 10));
    }

    [Fact]
    public async Task SubmitAnswerAsync_CorrectAnswer_ReturnsIsCorrectTrue()
    {
        var (service, ctx) = CreateService();
        await SeedQuestionsAsync(ctx, 1);
        var state = await service.StartQuizAsync(1);

        var correctLetter = state.ShuffleResults[0].CorrectShuffledLetter;
        var result = await service.SubmitAnswerAsync(state.SessionId, 0, correctLetter, 5);

        Assert.True(result.IsCorrect);
        Assert.Equal(1, result.QuestionNumber);
        Assert.Equal(1, result.TotalQuestions);
    }

    [Fact]
    public async Task SubmitAnswerAsync_WrongAnswer_ReturnsIsCorrectFalse()
    {
        var (service, ctx) = CreateService();
        await SeedQuestionsAsync(ctx, 1);
        var state = await service.StartQuizAsync(1);

        // Find a wrong letter
        var correctLetter = state.ShuffleResults[0].CorrectShuffledLetter;
        var wrongLetter = correctLetter == "A" ? "B" : "A";

        var result = await service.SubmitAnswerAsync(state.SessionId, 0, wrongLetter, 5);
        Assert.False(result.IsCorrect);
        Assert.Equal(correctLetter, result.CorrectAnswer);
    }

    [Fact]
    public async Task FinalizeAsync_ThrowsWhenSessionNotFound()
    {
        var (service, _) = CreateService();
        await Assert.ThrowsAsync<KeyNotFoundException>(() =>
            service.FinalizeAsync("nonexistent-session"));
    }

    [Fact]
    public async Task FinalizeAsync_PersistsSessionToDatabase()
    {
        var (service, ctx) = CreateService();
        await SeedQuestionsAsync(ctx, 2);
        var state = await service.StartQuizAsync(2);

        // Submit answers
        var correct0 = state.ShuffleResults[0].CorrectShuffledLetter;
        var correct1 = state.ShuffleResults[1].CorrectShuffledLetter;
        await service.SubmitAnswerAsync(state.SessionId, 0, correct0, 5);
        await service.SubmitAnswerAsync(state.SessionId, 1, correct1, 5);

        var result = await service.FinalizeAsync(state.SessionId);

        Assert.Equal(2, result.NumCorrect);
        Assert.Equal(100.0, result.PercentageCorrect, 2);
        Assert.True(result.TimeTakenSeconds >= 0);

        // Verify persisted
        var saved = await service.GetSessionAsync(state.SessionId);
        Assert.NotNull(saved);
        Assert.Equal(2, saved.NumCorrect);
    }

    [Fact]
    public async Task FinalizeAsync_RemovesSessionFromActiveState()
    {
        var (service, ctx) = CreateService();
        await SeedQuestionsAsync(ctx, 1);
        var state = await service.StartQuizAsync(1);

        await service.FinalizeAsync(state.SessionId);

        // Session no longer active
        await Assert.ThrowsAsync<KeyNotFoundException>(() =>
            service.SubmitAnswerAsync(state.SessionId, 0, "A", 5));
    }

    [Fact]
    public async Task GetSessionAsync_ReturnsNull_WhenNotFound()
    {
        var (service, _) = CreateService();
        var result = await service.GetSessionAsync("nonexistent");
        Assert.Null(result);
    }

    [Fact]
    public async Task FullQuizFlow_ZeroCorrect_ReturnsZeroPercentage()
    {
        var (service, ctx) = CreateService();
        await SeedQuestionsAsync(ctx, 3);
        var state = await service.StartQuizAsync(3);

        for (int i = 0; i < 3; i++)
        {
            var correct = state.ShuffleResults[i].CorrectShuffledLetter;
            var wrong = correct == "A" ? "B" : "A";
            await service.SubmitAnswerAsync(state.SessionId, i, wrong, 5);
        }

        var result = await service.FinalizeAsync(state.SessionId);
        Assert.Equal(0, result.NumCorrect);
        Assert.Equal(0.0, result.PercentageCorrect, 2);
    }
}
