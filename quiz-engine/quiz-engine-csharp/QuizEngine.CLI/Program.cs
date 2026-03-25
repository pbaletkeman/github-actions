using System.CommandLine;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using QuizEngine.CLI.Commands;
using QuizEngine.Data;
using QuizEngine.Service;

// Configure dependency injection
var services = new ServiceCollection();

var dbPath = Environment.GetEnvironmentVariable("QUIZ_DB_PATH") ?? "quiz.db";

services.AddDbContext<QuizEngineDbContext>(options =>
    options.UseSqlite($"Data Source={dbPath}"));

services.AddScoped<IQuestionRepository, QuestionRepository>();
services.AddScoped<ISessionRepository, SessionRepository>();
services.AddScoped<IResponseRepository, ResponseRepository>();
services.AddScoped<QuizService>();
services.AddScoped<HistoryService>();
services.AddScoped<ImportService>();

var serviceProvider = services.BuildServiceProvider();

// Ensure database is created and migrations applied
using (var scope = serviceProvider.CreateScope())
{
    var context = scope.ServiceProvider.GetRequiredService<QuizEngineDbContext>();
    context.Database.EnsureCreated();
}

// Build CLI root command
var rootCommand = new RootCommand("GitHub Actions Quiz Engine - GH-200 Certification Prep");

rootCommand.AddCommand(QuizCommand.Build(serviceProvider));
rootCommand.AddCommand(ImportCommand.Build(serviceProvider));
rootCommand.AddCommand(HistoryCommand.Build(serviceProvider));
rootCommand.AddCommand(ClearCommand.Build(serviceProvider));

return await rootCommand.InvokeAsync(args);
