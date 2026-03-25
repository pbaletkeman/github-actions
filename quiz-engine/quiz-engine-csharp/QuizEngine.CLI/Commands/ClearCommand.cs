using System.CommandLine;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using QuizEngine.Data;
using Spectre.Console;

namespace QuizEngine.CLI.Commands;

public static class ClearCommand
{
    public static Command Build(IServiceProvider services)
    {
        var questionsOption = new Option<bool>(
            "--questions",
            description: "Clear all questions from the database");

        var historyOption = new Option<bool>(
            "--history",
            description: "Clear all quiz session history");

        var allOption = new Option<bool>(
            "--all",
            description: "Clear all data (questions and history)");

        var confirmOption = new Option<bool>(
            "--confirm",
            description: "Confirm the operation (required)");

        var command = new Command("clear", "Clear data from the database");
        command.AddOption(questionsOption);
        command.AddOption(historyOption);
        command.AddOption(allOption);
        command.AddOption(confirmOption);

        command.SetHandler(async (bool questions, bool history, bool all, bool confirm) =>
        {
            if (!questions && !history && !all)
            {
                AnsiConsole.MarkupLine("[yellow]Specify --questions, --history, or --all[/]");
                return;
            }

            if (!confirm)
            {
                AnsiConsole.MarkupLine("[red]Warning:[/] This will permanently delete data. Add [bold]--confirm[/] to proceed.");
                return;
            }

            using var scope = services.CreateScope();
            var context = scope.ServiceProvider.GetRequiredService<QuizEngineDbContext>();

            if (all || (questions && history))
            {
                await context.QuizResponses.ExecuteDeleteAsync();
                await context.QuizSessions.ExecuteDeleteAsync();
                await context.Questions.ExecuteDeleteAsync();
                AnsiConsole.MarkupLine("[green]✓ All data cleared.[/]");
            }
            else if (questions)
            {
                await context.QuizResponses.ExecuteDeleteAsync();
                await context.Questions.ExecuteDeleteAsync();
                AnsiConsole.MarkupLine("[green]✓ Questions cleared.[/]");
            }
            else if (history)
            {
                await context.QuizResponses.ExecuteDeleteAsync();
                await context.QuizSessions.ExecuteDeleteAsync();
                AnsiConsole.MarkupLine("[green]✓ History cleared.[/]");
            }
        }, questionsOption, historyOption, allOption, confirmOption);

        return command;
    }
}
