using Microsoft.EntityFrameworkCore;
using QuizEngine.Data;
using QuizEngine.Entities;
using QuizEngine.Service;
using Xunit;

namespace QuizEngine.Tests;

public class ServiceTests
{
    private static (QuizEngineDbContext ctx, QuestionRepository qRepo, SessionRepository sRepo, ResponseRepository rRepo) CreateRepos()
    {
        var ctx = new QuizEngineDbContext(
            new DbContextOptionsBuilder<QuizEngineDbContext>()
                .UseInMemoryDatabase(Guid.NewGuid().ToString())
                .Options);
        ctx.Database.EnsureCreated();
        return (ctx, new QuestionRepository(ctx), new SessionRepository(ctx), new ResponseRepository(ctx));
    }

    // HistoryService tests
    [Fact]
    public async Task HistoryService_GetRecentSessions_ReturnsEmpty_WhenNoSessions()
    {
        var (ctx, _, sRepo, rRepo) = CreateRepos();
        var service = new HistoryService(sRepo, rRepo);

        var sessions = await service.GetRecentSessionsAsync(10);
        Assert.Empty(sessions);
    }

    [Fact]
    public async Task HistoryService_GetRecentSessions_ReturnsSessions()
    {
        var (ctx, _, sRepo, rRepo) = CreateRepos();
        var service = new HistoryService(sRepo, rRepo);

        var session = new QuizSession
        {
            SessionId = Guid.NewGuid().ToString(),
            NumQuestions = 5,
            NumCorrect = 3,
            PercentageCorrect = 60.0
        };
        await sRepo.SaveAsync(session);

        var sessions = await service.GetRecentSessionsAsync(10);
        Assert.Single(sessions);
    }

    [Fact]
    public async Task HistoryService_GetSessionDetail_ReturnsNull_WhenNotFound()
    {
        var (ctx, _, sRepo, rRepo) = CreateRepos();
        var service = new HistoryService(sRepo, rRepo);

        var (s, responses) = await service.GetSessionDetailAsync("nonexistent");
        Assert.Null(s);
        Assert.Empty(responses);
    }

    [Fact]
    public async Task HistoryService_GetTotalSessions_ReturnsCorrectCount()
    {
        var (ctx, _, sRepo, rRepo) = CreateRepos();
        var service = new HistoryService(sRepo, rRepo);

        var total0 = await service.GetTotalSessionsAsync();

        await sRepo.SaveAsync(new QuizSession
        {
            SessionId = Guid.NewGuid().ToString(),
            NumQuestions = 3
        });

        var total1 = await service.GetTotalSessionsAsync();
        Assert.Equal(total0 + 1, total1);
    }

    // SessionRepository tests
    [Fact]
    public async Task SessionRepository_GetAllAsync_ReturnsSortedByDate()
    {
        var (_, _, sRepo, _) = CreateRepos();

        var s1 = new QuizSession
        {
            SessionId = "session-1",
            StartedAt = DateTime.UtcNow.AddMinutes(-10),
            NumQuestions = 5
        };
        var s2 = new QuizSession
        {
            SessionId = "session-2",
            StartedAt = DateTime.UtcNow.AddMinutes(-5),
            NumQuestions = 5
        };

        await sRepo.SaveAsync(s1);
        await sRepo.SaveAsync(s2);

        var sessions = await sRepo.GetAllAsync(0, 10);
        Assert.True(sessions[0].StartedAt >= sessions[1].StartedAt);
    }

    [Fact]
    public async Task SessionRepository_SaveAsync_UpdatesExisting()
    {
        var (_, _, sRepo, _) = CreateRepos();
        var sessionId = Guid.NewGuid().ToString();
        var session = new QuizSession { SessionId = sessionId, NumQuestions = 5 };
        await sRepo.SaveAsync(session);

        // Update it
        session.NumCorrect = 4;
        session.PercentageCorrect = 80.0;
        await sRepo.SaveAsync(session);

        var loaded = await sRepo.GetByIdAsync(sessionId);
        Assert.NotNull(loaded);
        Assert.Equal(4, loaded.NumCorrect);
        Assert.Equal(80.0, loaded.PercentageCorrect, 2);
    }

    // ResponseRepository tests
    [Fact]
    public async Task ResponseRepository_SaveAsync_PersistsResponse()
    {
        var (ctx, qRepo, sRepo, rRepo) = CreateRepos();

        var question = await qRepo.InsertAsync(DatabaseFixture.BuildSampleQuestion(n: 2000));
        var session = new QuizSession
        {
            SessionId = Guid.NewGuid().ToString(),
            NumQuestions = 1
        };
        await sRepo.SaveAsync(session);

        var response = new QuizResponse
        {
            SessionId = session.SessionId,
            QuestionId = question.Id,
            UserAnswer = "A",
            IsCorrect = 1
        };
        await rRepo.SaveAsync(response);

        var responses = await rRepo.GetBySessionIdAsync(session.SessionId);
        Assert.Single(responses);
        Assert.Equal("A", responses[0].UserAnswer);
    }

    [Fact]
    public async Task ResponseRepository_CountCorrectBySession_ReturnsCorrectCount()
    {
        var (ctx, qRepo, sRepo, rRepo) = CreateRepos();

        var q1 = await qRepo.InsertAsync(DatabaseFixture.BuildSampleQuestion(n: 3000));
        var q2 = await qRepo.InsertAsync(DatabaseFixture.BuildSampleQuestion(n: 3001));
        var session = new QuizSession { SessionId = Guid.NewGuid().ToString(), NumQuestions = 2 };
        await sRepo.SaveAsync(session);

        await rRepo.SaveAsync(new QuizResponse { SessionId = session.SessionId, QuestionId = q1.Id, IsCorrect = 1, UserAnswer = "A" });
        await rRepo.SaveAsync(new QuizResponse { SessionId = session.SessionId, QuestionId = q2.Id, IsCorrect = 0, UserAnswer = "B" });

        var count = await rRepo.CountCorrectBySessionAsync(session.SessionId);
        Assert.Equal(1, count);
    }

    // ImportService tests
    [Fact]
    public async Task ImportService_ImportFromFile_ImportsQuestions()
    {
        var (_, qRepo, _, _) = CreateRepos();
        var service = new ImportService(qRepo);

        var content = @"
## Q1

**Q: What is GitHub Actions?**

- A) CI/CD Platform
- B) Database
- C) Storage
- D) Network

**Answer: A**
";
        var tmpFile = Path.GetTempFileName() + ".md";
        try
        {
            await File.WriteAllTextAsync(tmpFile, content);
            var (imported, _) = await service.ImportFromFileAsync(tmpFile);
            Assert.Equal(1, imported);
        }
        finally
        {
            File.Delete(tmpFile);
        }
    }

    [Fact]
    public async Task ImportService_ImportFromFile_ThrowsWhenFileNotFound()
    {
        var (_, qRepo, _, _) = CreateRepos();
        var service = new ImportService(qRepo);
        await Assert.ThrowsAsync<FileNotFoundException>(() =>
            service.ImportFromFileAsync("/nonexistent/file.md"));
    }

    [Fact]
    public async Task ImportService_ImportFromDirectory_ThrowsWhenDirNotFound()
    {
        var (_, qRepo, _, _) = CreateRepos();
        var service = new ImportService(qRepo);
        await Assert.ThrowsAsync<DirectoryNotFoundException>(() =>
            service.ImportFromDirectoryAsync("/nonexistent/directory"));
    }

    [Fact]
    public async Task ImportService_ImportFromDirectory_ImportsFromAllFiles()
    {
        var (_, qRepo, _, _) = CreateRepos();
        var service = new ImportService(qRepo);

        var tmpDir = Path.Combine(Path.GetTempPath(), Guid.NewGuid().ToString());
        Directory.CreateDirectory(tmpDir);

        try
        {
            var content1 = @"
## Q1
**Q: Question from file 1?**
- A) Answer A
- B) Answer B
- C) Answer C
- D) Answer D
**Answer: A**
";
            var content2 = @"
## Q2
**Q: Question from file 2?**
- A) Answer A
- B) Answer B
- C) Answer C
- D) Answer D
**Answer: B**
";
            await File.WriteAllTextAsync(Path.Combine(tmpDir, "file1.md"), content1);
            await File.WriteAllTextAsync(Path.Combine(tmpDir, "file2.md"), content2);

            var (imported, _) = await service.ImportFromDirectoryAsync(tmpDir);
            Assert.True(imported >= 2);
        }
        finally
        {
            Directory.Delete(tmpDir, recursive: true);
        }
    }
}
