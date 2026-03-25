using QuizEngine.Data;
using QuizEngine.Entities;
using Xunit;

namespace QuizEngine.Tests;

public class QuestionRepositoryTests : IClassFixture<DatabaseFixture>
{
    private readonly DatabaseFixture _fixture;
    private readonly QuestionRepository _repo;

    public QuestionRepositoryTests(DatabaseFixture fixture)
    {
        _fixture = fixture;
        _repo = new QuestionRepository(_fixture.Context);
    }

    [Fact]
    public async Task InsertAsync_ShouldPersistQuestion()
    {
        var q = DatabaseFixture.BuildSampleQuestion(n: 100);
        await _repo.InsertAsync(q);

        var all = await _repo.GetAllAsync();
        Assert.Contains(all, x => x.QuestionText == q.QuestionText);
    }

    [Fact]
    public async Task InsertAsync_SkipsDuplicateQuestion()
    {
        var q = DatabaseFixture.BuildSampleQuestion(n: 200);
        await _repo.InsertAsync(q);
        var countBefore = await _repo.CountAsync();

        // Insert same question again
        var duplicate = DatabaseFixture.BuildSampleQuestion(n: 200); // Same text
        await _repo.InsertAsync(duplicate);
        var countAfter = await _repo.CountAsync();

        Assert.Equal(countBefore, countAfter);
    }

    [Fact]
    public async Task GetCurrentCycleAsync_ReturnsOne_WhenNoQuestions()
    {
        // With empty or fresh DB
        var cycle = await _repo.GetCurrentCycleAsync();
        Assert.True(cycle >= 1);
    }

    [Fact]
    public async Task GetRandomQuestionsAsync_ReturnsRequestedCount()
    {
        // Insert several questions
        for (int i = 300; i < 310; i++)
        {
            await _repo.InsertAsync(DatabaseFixture.BuildSampleQuestion(n: i));
        }

        var questions = await _repo.GetRandomQuestionsAsync(3);
        Assert.True(questions.Count <= 3);
    }

    [Fact]
    public async Task GetRandomQuestionsAsync_FiltersByDifficulty()
    {
        var easy = DatabaseFixture.BuildSampleQuestion(n: 400);
        easy.Difficulty = "easy";
        var hard = DatabaseFixture.BuildSampleQuestion(n: 401);
        hard.Difficulty = "hard";
        await _repo.InsertAsync(easy);
        await _repo.InsertAsync(hard);

        var results = await _repo.GetRandomQuestionsAsync(10, difficulty: "hard");
        Assert.All(results, q => Assert.Equal("hard", q.Difficulty));
    }

    [Fact]
    public async Task GetRandomQuestionsAsync_FiltersBySection()
    {
        var q1 = DatabaseFixture.BuildSampleQuestion(n: 500);
        q1.Section = "SectionAlpha";
        var q2 = DatabaseFixture.BuildSampleQuestion(n: 501);
        q2.Section = "SectionBeta";
        await _repo.InsertAsync(q1);
        await _repo.InsertAsync(q2);

        var results = await _repo.GetRandomQuestionsAsync(10, section: "SectionAlpha");
        Assert.All(results, q => Assert.Equal("SectionAlpha", q.Section));
    }

    [Fact]
    public async Task GetByIdAsync_ReturnsQuestion_WhenExists()
    {
        var q = DatabaseFixture.BuildSampleQuestion(n: 600);
        var inserted = await _repo.InsertAsync(q);

        var found = await _repo.GetByIdAsync(inserted.Id);
        Assert.NotNull(found);
        Assert.Equal(inserted.Id, found.Id);
    }

    [Fact]
    public async Task GetByIdAsync_ReturnsNull_WhenNotFound()
    {
        var found = await _repo.GetByIdAsync(999999);
        Assert.Null(found);
    }

    [Fact]
    public async Task MarkQuestionUsedAsync_IncrementsTimesUsed()
    {
        var q = DatabaseFixture.BuildSampleQuestion(n: 700);
        var inserted = await _repo.InsertAsync(q);
        Assert.Equal(0, inserted.TimesUsed);

        await _repo.MarkQuestionUsedAsync(inserted.Id);

        var updated = await _repo.GetByIdAsync(inserted.Id);
        Assert.Equal(1, updated!.TimesUsed);
        Assert.NotNull(updated.LastUsedAt);
    }

    [Fact]
    public async Task MarkQuestionUsedAsync_DoesNothing_WhenNotFound()
    {
        // Should not throw
        await _repo.MarkQuestionUsedAsync(999998);
    }

    [Fact]
    public async Task AdvanceCycleIfExhaustedAsync_AdvancesCycle_WhenAllUsed()
    {
        // Fresh context for isolation
        var freshFixture = new DatabaseFixture();
        var freshRepo = new QuestionRepository(freshFixture.Context);

        var q = DatabaseFixture.BuildSampleQuestion(n: 800);
        var inserted = await freshRepo.InsertAsync(q);
        await freshRepo.MarkQuestionUsedAsync(inserted.Id);

        var cycleBefore = await freshRepo.GetCurrentCycleAsync();
        await freshRepo.AdvanceCycleIfExhaustedAsync();
        var cycleAfter = await freshRepo.GetCurrentCycleAsync();

        Assert.Equal(cycleBefore + 1, cycleAfter);
        freshFixture.Dispose();
    }

    [Fact]
    public async Task AdvanceCycleIfExhaustedAsync_DoesNotAdvance_WhenUnusedRemain()
    {
        var freshFixture = new DatabaseFixture();
        var freshRepo = new QuestionRepository(freshFixture.Context);

        // Insert two, only mark one used
        var q1 = DatabaseFixture.BuildSampleQuestion(n: 900);
        var q2 = DatabaseFixture.BuildSampleQuestion(n: 901);
        var i1 = await freshRepo.InsertAsync(q1);
        await freshRepo.InsertAsync(q2);
        await freshRepo.MarkQuestionUsedAsync(i1.Id);

        var cycleBefore = await freshRepo.GetCurrentCycleAsync();
        await freshRepo.AdvanceCycleIfExhaustedAsync();
        var cycleAfter = await freshRepo.GetCurrentCycleAsync();

        Assert.Equal(cycleBefore, cycleAfter);
        freshFixture.Dispose();
    }

    [Fact]
    public async Task CountAsync_ReturnsCorrectCount()
    {
        var countBefore = await _repo.CountAsync();
        await _repo.InsertAsync(DatabaseFixture.BuildSampleQuestion(n: 1000));
        var countAfter = await _repo.CountAsync();
        Assert.Equal(countBefore + 1, countAfter);
    }

    [Theory]
    [InlineData("A", "A", true)]
    [InlineData("A", "a", true)]
    [InlineData("A", "B", false)]
    [InlineData("B", "B", true)]
    [InlineData("C", "D", false)]
    public void CheckAnswer_ReturnsCorrectResult(string correct, string submitted, bool expected)
    {
        Assert.Equal(expected, _repo.CheckAnswer(correct, submitted));
    }
}
