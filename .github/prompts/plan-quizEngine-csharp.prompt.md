# C# Entity Framework Core Quiz Engine for GH-200 Certification

## System Architecture Overview

### Directory Structure
```
QuizEngine/
├── QuizEngine.sln                          # Solution file
├── QuizEngine.Entities/
│   ├── Question.cs                         # Entity model
│   ├── QuizSession.cs                      # Entity model
│   └── QuizResponse.cs                     # Entity model
├── QuizEngine.Data/
│   ├── QuizEngineDbContext.cs              # DbContext (Entity Framework Core)
│   ├── IQuestionRepository.cs              # Repository interface
│   ├── QuestionRepository.cs               # Repository implementation
│   ├── ISessionRepository.cs               # Repository interface
│   ├── SessionRepository.cs                # Repository implementation
│   ├── IResponseRepository.cs              # Repository interface
│   └── ResponseRepository.cs               # Repository implementation
├── QuizEngine.Service/
│   ├── QuizEngine.cs                       # Core quiz logic
│   ├── QuizService.cs                      # Business logic
│   ├── HistoryService.cs                   # History operations
│   ├── ImportService.cs                    # Markdown import
│   ├── MarkdownParser.cs                   # MD file parser
│   ├── AnswerShuffler.cs                   # Answer randomization
│   └── QuizUtils.cs                        # Helper utilities
├── QuizEngine.CLI/
│   ├── QuizEngine.CLI.csproj               # CLI project
│   ├── Program.cs                          # Entry point (Spectre.Console)
│   ├── Commands/
│   │   ├── QuizCommand.cs                  # Interactive quiz command
│   │   ├── ImportCommand.cs                # Import questions command
│   │   ├── HistoryCommand.cs               # View history command
│   │   └── ClearCommand.cs                 # Clear data command
│   ├── Formatters/
│   │   ├── ConsoleFormatter.cs             # Table/box formatting
│   │   └── AnsiColors.cs                   # ANSI color constants
│   └── Prompts.cs                          # Interactive prompts
├── QuizEngine.Tests/
│   ├── RepositoryTests.cs
│   ├── QuizEngineTests.cs
│   └── AnswerShufflerTests.cs
└── README.md                               # Documentation
```

### Database Schema (Managed by EF Core Migrations)

#### Question Entity
```csharp
[Table("questions")]
public class Question
{
    [Key]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public int Id { get; set; }

    [Required]
    [MaxLength(500)]
    public string QuestionText { get; set; }

    [Required]
    [MaxLength(200)]
    public string OptionA { get; set; }

    [Required]
    [MaxLength(200)]
    public string OptionB { get; set; }

    [Required]
    [MaxLength(200)]
    public string OptionC { get; set; }

    [Required]
    [MaxLength(200)]
    public string OptionD { get; set; }

    [MaxLength(200)]
    public string OptionE { get; set; }

    [Required]
    [MaxLength(1)]
    public string CorrectAnswer { get; set; }

    [MaxLength(1000)]
    public string Explanation { get; set; }

    [MaxLength(100)]
    public string Section { get; set; }

    [MaxLength(50)]
    public string Difficulty { get; set; }

    [MaxLength(255)]
    public string SourceFile { get; set; }

    [Column("usage_cycle")]
    [DefaultValue(1)]
    public int UsageCycle { get; set; } = 1;

    [Column("times_used")]
    [DefaultValue(0)]
    public int TimesUsed { get; set; } = 0;

    [Column("last_used_at")]
    public DateTime? LastUsedAt { get; set; }

    [DatabaseGenerated(DatabaseGeneratedOption.Computed)]
    public DateTime CreatedAt { get; set; }

    public ICollection<QuizResponse> Responses { get; } = new List<QuizResponse>();
}
```

#### QuizSession Entity
```csharp
[Table("quiz_sessions")]
public class QuizSession
{
    [Key]
    [MaxLength(36)]
    public string SessionId { get; set; }

    public DateTime StartedAt { get; set; } = DateTime.UtcNow;

    public DateTime? EndedAt { get; set; }

    [Required]
    public int NumQuestions { get; set; }

    [DefaultValue(0)]
    public int NumCorrect { get; set; } = 0;

    [DefaultValue(0.0)]
    public double PercentageCorrect { get; set; } = 0.0;

    public int? TimeTakenSeconds { get; set; }

    public ICollection<QuizResponse> Responses { get; } = new List<QuizResponse>();
}
```

#### QuizResponse Entity
```csharp
[Table("quiz_responses")]
public class QuizResponse
{
    [Key]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public int Id { get; set; }

    [Required]
    [ForeignKey(nameof(Session))]
    [MaxLength(36)]
    public string SessionId { get; set; }

    [Required]
    [ForeignKey(nameof(Question))]
    public int QuestionId { get; set; }

    [MaxLength(1)]
    public string UserAnswer { get; set; }

    [DefaultValue(0)]
    public int IsCorrect { get; set; } = 0;

    public int? TimeTakenSeconds { get; set; }

    public QuizSession Session { get; set; }
    public Question Question { get; set; }
}
```

---

## Implementation Plan

### Phase 1: Project Setup & EF Core Configuration
**Timeline:** 1.5-2 hours

**Objective:** Create .NET solution, set up Entity Framework Core, define entities.

**Tasks:**

1. **Create Solution (CLI):**
   ```bash
   dotnet new sln -n QuizEngine
   dotnet new classlib -n QuizEngine.Entities
   dotnet new classlib -n QuizEngine.Data
   dotnet new classlib -n QuizEngine.Service
   dotnet new console -n QuizEngine.CLI
   dotnet new xunit -n QuizEngine.Tests
   dotnet sln add QuizEngine.*/QuizEngine.*.csproj
   ```

2. **Add NuGet Dependencies (to all projects):**
   ```bash
   # Project.csproj
   <ItemGroup>
     <PackageReference Include="Microsoft.EntityFrameworkCore" Version="8.0.0" />
     <PackageReference Include="Microsoft.EntityFrameworkCore.Sqlite" Version="8.0.0" />
     <PackageReference Include="Microsoft.EntityFrameworkCore.Design" Version="8.0.0" />
     <PackageReference Include="Spectre.Console" Version="0.47.0" />
     <PackageReference Include="System.CommandLine" Version="2.0.0-beta4.22272.1" />
   </ItemGroup>
   ```

3. **Create Entity Models in `QuizEngine.Entities/`:**
   - `Question.cs` with [Key], [Required], [Column] attributes
   - `QuizSession.cs` with relationships, timestamps
   - `QuizResponse.cs` with foreign keys
   - Include `[Table]` attributes for exact DB names

4. **Create `QuizEngineDbContext` in `QuizEngine.Data/`:**
   ```csharp
   public class QuizEngineDbContext : DbContext
   {
       public DbSet<Question> Questions { get; set; }
       public DbSet<QuizSession> QuizSessions { get; set; }
       public DbSet<QuizResponse> QuizResponses { get; set; }

       protected override void OnConfiguring(DbContextOptionsBuilder options)
           => options.UseSqlite("Data Source=quiz.db");

       protected override void OnModelCreating(ModelBuilder modelBuilder)
       {
           // Fluent API configurations
           modelBuilder.Entity<Question>()
               .HasIndex(q => q.Section);
           modelBuilder.Entity<Question>()
               .HasIndex(q => q.Difficulty);
           modelBuilder.Entity<Question>()
               .HasIndex(q => q.UsageCycle);

           // Unique constraint
           modelBuilder.Entity<Question>()
               .HasIndex(q => new { q.QuestionText, q.CorrectAnswer })
               .IsUnique();
       }
   }
   ```

5. **Create EF Core Migration:**
   ```bash
   dotnet ef migrations add InitialCreate --project QuizEngine.Data --startup-project QuizEngine.CLI
   dotnet ef database update --project QuizEngine.Data --startup-project QuizEngine.CLI
   ```

6. **Verify database creation:**
   - SQLite file created at workspace root
   - All tables and indexes created correctly

**Success Criteria:**
- Solution builds without errors
- EF Core migrations run cleanly
- SQLite database initialized with schema
- Entities properly mapped to tables
- All relationships configured

---

### Phase 2: Repository Layer & Queries
**Timeline:** 1-1.5 hours

**Objective:** Implement repository pattern for data access with cycle-aware queries.

**Tasks:**

1. **Create Repository Interfaces:**
   ```csharp
   public interface IQuestionRepository
   {
       Task<List<Question>> GetRandomQuestionsAsync(int count, string difficulty = null, string section = null);
       Task<int> GetCurrentCycleAsync();
       Task<Question> GetByIdAsync(int id);
       Task MarkQuestionUsedAsync(int questionId);
       Task AdvanceQuestionsToNextCycleAsync();
       Task<int> CountAsync();
   }

   public interface ISessionRepository
   {
       Task<QuizSession> GetByIdAsync(string sessionId);
       Task<List<QuizSession>> GetAllAsync(int skip = 0, int take = 10);
       Task SaveAsync(QuizSession session);
   }

   public interface IResponseRepository
   {
       Task SaveAsync(QuizResponse response);
       Task<List<QuizResponse>> GetBySessionIdAsync(string sessionId);
   }
   ```

2. **Implement Repository Classes:**
   ```csharp
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

       public async Task<List<Question>> GetRandomQuestionsAsync(
           int count, string difficulty = null, string section = null)
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
               .Select(q => new Question
               {
                   // Exclude CorrectAnswer and Explanation
                   Id = q.Id,
                   QuestionText = q.QuestionText,
                   OptionA = q.OptionA,
                   OptionB = q.OptionB,
                   OptionC = q.OptionC,
                   OptionD = q.OptionD,
                   OptionE = q.OptionE,
                   Section = q.Section,
                   Difficulty = q.Difficulty
               })
               .ToListAsync();
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

       public async Task AdvanceQuestionsToNextCycleAsync()
       {
           var cycle = await GetCurrentCycleAsync();
           var exhausted = await _context.Questions
               .Where(q => q.UsageCycle == cycle && q.TimesUsed > 0)
               .ToListAsync();

           foreach (var q in exhausted)
               q.UsageCycle++;

           await _context.SaveChangesAsync();
       }
   }
   ```

3. **Implement Session & Response Repositories** (similar pattern)

4. **register repositories in dependency injection:**
   ```csharp
   var services = new ServiceCollection();
   services.AddDbContext<QuizEngineDbContext>();
   services.AddScoped<IQuestionRepository, QuestionRepository>();
   services.AddScoped<ISessionRepository, SessionRepository>();
   services.AddScoped<IResponseRepository, ResponseRepository>();
   ```

5. **Test repositories:**
   - Unit tests with InMemory database
   - Verify cycle-aware queries
   - Test batch operations

**Success Criteria:**
- All repository methods working correctly
- Cycle-aware queries return correct subset
- Marked questions increment counter
- Cycle auto-advances properly
- All repository tests passing

---

### Phase 3: Service & Business Logic
**Timeline:** 2-2.5 hours

**Objective:** Implement QuizEngine, services, and utility classes.

**Tasks:**

1. **Create `QuizEngine.cs` (Core Logic):**
   ```csharp
   public class QuizEngine
   {
       private readonly string _sessionId;
       private readonly IQuestionRepository _questionRepo;
       private readonly ISessionRepository _sessionRepo;
       private readonly IResponseRepository _responseRepo;
       private List<Question> _questions;
       private QuizSession _session;

       public async Task LoadQuestionsAsync(int count)
       {
           _questions = await _questionRepo.GetRandomQuestionsAsync(count);
       }

       public async Task SubmitAnswerAsync(int questionIndex, string answer, int timeTaken)
       {
           var question = _questions[questionIndex];
           var response = new QuizResponse
           {
               SessionId = _sessionId,
               QuestionId = question.Id,
               UserAnswer = answer,
               IsCorrect = VerifyAnswer(answer, question) ? 1 : 0,
               TimeTakenSeconds = timeTaken
           };
           await _responseRepo.SaveAsync(response);
       }

       public async Task FinalizeAsync()
       {
           // Mark questions used, advance cycle if needed
           foreach (var q in _questions)
               await _questionRepo.MarkQuestionUsedAsync(q.Id);

           await _questionRepo.AdvanceQuestionsToNextCycleAsync();

           // Update session stats
           _session.EndedAt = DateTime.UtcNow;
           _session.NumCorrect = await CountCorrectAnswersAsync();
           _session.PercentageCorrect = (_session.NumCorrect / (double)_session.NumQuestions) * 100;
           await _sessionRepo.SaveAsync(_session);
       }

       private bool VerifyAnswer(string shuffledAnswer, Question question)
       {
           // Match shuffled letter to correct answer position
           return question.CorrectAnswer == shuffledAnswer;
       }
   }
   ```

2. **Create `AnswerShuffler.cs`:**
   - Randomize answer order
   - Track position of correct answer
   - Return mapping for verification

3. **Create `MarkdownParser.cs`:**
   - Parse markdown files for questions
   - Extract all fields including explanation
   - Validate structure

4. **Create `HistoryService.cs`, `ImportService.cs`:** with similar CRUD patterns

5. **Test service layer:**
   - Integration tests with InMemory DB
   - Full quiz flow: load → submit → finalize
   - Cycle mechanics verified

**Success Criteria:**
- QuizEngine orchestrates correct flow
- Answers shuffled and tracked properly
- Session saved with correct stats
- Import/history operations work
- All service tests passing

---

### Phase 4: CLI Layer with Spectre.Console
**Timeline:** 1.5-2 hours

**Objective:** Build interactive CLI using Spectre.Console and System.CommandLine.

**Tasks:**

1. **Create `Program.cs` (Entry Point):**
   ```csharp
   var builder = Host.CreateDefaultBuilder(args)
       .ConfigureServices((host, services) =>
       {
           services.AddDbContext<QuizEngineDbContext>();
           services.AddScoped<IQuestionRepository, QuestionRepository>();
           // ... register all services and repositories
       });

   var host = builder.Build();

   var rootCommand = new RootCommand("GitHub Actions Quiz Engine");
   rootCommand.AddCommand(BuildQuizCommand(host.Services));
   rootCommand.AddCommand(BuildImportCommand(host.Services));
   rootCommand.AddCommand(BuildHistoryCommand(host.Services));
   rootCommand.AddCommand(BuildClearCommand(host.Services));

   return rootCommand.InvokeAsync(args);
   ```

2. **Create `Commands/QuizCommand.cs`:**
   ```csharp
   public static Command BuildQuizCommand(IServiceProvider services)
   {
       var command = new Command("quiz", "Take a quiz");

       command.SetHandler(async () =>
       {
           var quizService = services.GetRequiredService<QuizService>();

           // Prompt for config
           var numQuestions = Prompt.GetInt("Number of questions? [100]: ", 100);

           // Run quiz interactively
           await quizService.StartQuizAsync(numQuestions);
       });

       return command;
   }
   ```

3. **Create `Prompts.cs` with Spectre.Console:**
   ```csharp
   public static class Prompt
   {
       public static int GetInt(string prompt, int defaultValue)
       {
           AnsiConsole.Markup($"[yellow]{prompt}[/]");
           var input = Console.ReadLine();
           return string.IsNullOrEmpty(input) ? defaultValue : int.Parse(input);
       }

       public static string PromptAnswer()
       {
           AnsiConsole.Markup("[cyan]Enter your answer (A-E) or press ENTER to skip: [/]");
           return Console.ReadLine()?.ToUpper() ?? "";
       }
   }
   ```

4. **Create `Formatters/ConsoleFormatter.cs`:**
   - Use Spectre.Console tables for results
   - Use boxes for question display
   - Use progress bars for timer

5. **Test CLI commands:**
   - `dotnet run -- quiz`
   - `dotnet run -- import --file questions.md`
   - All interactive flows work

**Success Criteria:**
- CLI builds as single executable
- All commands function correctly
- Interactive prompts work cleanly
- Pretty-printed output (tables, boxes)
- No unhandled exceptions

---

### Phase 5: Testing & Packaging
**Timeline:** 1-1.5 hours

**Objective:** Comprehensive tests, create release build.

**Tasks:**

1. **Write Unit Tests (`QuizEngine.Tests/`):**
   - `xUnit` for test framework
   - InMemory SQLite for data layer tests
   - Mock services for CLI tests

2. **Create Release Build:**
   ```bash
   dotnet publish -c Release -o ./release
   ```

3. **Write README:**
   - **Getting Started:** .NET 8 requirement, download/build
   - **Configuration:** appsettings.json options
   - **Running:** `dotnet run -- quiz` or release executable
   - **CLI Commands:** quiz, import, history, clear
   - **Architecture:** EF Core, Spectre.Console, repository pattern

4. **Final testing:**
   - Full end-to-end workflow
   - Create, take, review, retake quizzes
   - Verify cycle mechanics

**Success Criteria:**
- All tests passing
- Release build creates standalone executable
- Can run on Windows/Mac/Linux (.NET 8 installed)
- Documentation complete

---

## Dependencies Summary
- **Entity Framework Core 8.0** - ORM
- **Microsoft.EntityFrameworkCore.Sqlite** - SQLite provider
- **Spectre.Console** - Rich terminal output
- **System.CommandLine** - Modern CLI parsing
- **xUnit** - Test framework

---

## Core Design Decisions

### 1. Entity Framework Core
- **ORM:** Simplifies data access, type-safe queries
- **Migrations:** Version schema changes, rollback capability
- **DbContext:** Single per application instance

### 2. Repository Pattern
- **Abstraction:** Decouples data access from business logic
- **Testability:** Easy to mock in unit tests
- **Consistency:** Centralized query logic

### 3. Dependency Injection (IServiceProvider)
- **Built-in:** .NET includes DI container
- **Scope Management:** Automatic cleanup
- **Flexibility:** Easy to swap implementations

### 4. Spectre.Console for Rich TUI
- **Tables:** Formatted output for history
- **Boxes:** Question display with borders
- **Progress:** Timer visualization
- **Colors:** ANSI color support

### 5. System.CommandLine
- **Modern:** Declarative command structure
- **Help:** Auto-generated help and completions
- **Types:** Strong typing for arguments

---

## CLI Examples

```bash
# Take a quiz
dotnet run -- quiz

# Import questions
dotnet run -- import --file questions.md
dotnet run -- import --dir ./md/

# View history
dotnet run -- history
dotnet run -- history --session-id <uuid> --review
dotnet run -- history --export json

# Clear data
dotnet run -- clear --questions --confirm
dotnet run -- clear --history --all --confirm
```

---

## Success Criteria

### Functional
- ✓ Load 100+ random questions without showing answers
- ✓ NEVER repeat question until cycle exhausted
- ✓ Answers randomized and verified correctly
- ✓ Session persisted with full stats
- ✓ Import/history/clear operations work
- ✓ All CLI commands functional

### Non-Functional
- ✓ Performance: Load questions + display <1 second
- ✓ Usability: Full workflow <15 minutes
- ✓ Reliability: Graceful error handling, transactional integrity
- ✓ Maintainability: Clean architecture, testable
- ✓ Compatibility: .NET 8+, Windows/Mac/Linux

---

## Implementation Notes

- **Dependency Injection:** Use `IServiceProvider` throughout
- **Async/Await:** Async all data access (async repository methods)
- **DbContext Lifetime:** Create new context per command execution
- **Error Handling:** Use custom exceptions for quiz-specific errors
- **Testing:** Use InMemory provider for unit tests
- **Migrations:** Store migrations in `QuizEngine.Data` project
- **Future:** Add web API layer with ASP.NET Core, REST endpoints
