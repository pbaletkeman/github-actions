using System.CommandLine;
using Microsoft.Extensions.DependencyInjection;
using QuizEngine.CLI.Formatters;
using QuizEngine.Service;
using Spectre.Console;

namespace QuizEngine.CLI.Commands;

public static class HistoryCommand
{
    public static Command Build(IServiceProvider services)
    {
        var countOption = new Option<int>(
            "--count",
            getDefaultValue: () => 10,
            description: "Number of recent sessions to show");
        countOption.AddAlias("-n");

        var sessionIdOption = new Option<string?>(
            "--session-id",
            description: "Show details for a specific session");

        var command = new Command("history", "View quiz history");
        command.AddOption(countOption);
        command.AddOption(sessionIdOption);

        command.SetHandler(async (int count, string? sessionId) =>
        {
            using var scope = services.CreateScope();
            var historyService = scope.ServiceProvider.GetRequiredService<HistoryService>();

            if (sessionId != null)
            {
                var (session, responses) = await historyService.GetSessionDetailAsync(sessionId);
                if (session == null)
                {
                    AnsiConsole.MarkupLine($"[red]Session not found:[/] {sessionId}");
                    return;
                }
                ConsoleFormatter.PrintSessionDetail(session, responses);
            }
            else
            {
                var sessions = await historyService.GetRecentSessionsAsync(count);
                var total = await historyService.GetTotalSessionsAsync();
                AnsiConsole.MarkupLine($"\n[bold]Quiz History[/] (showing {sessions.Count} of {total} sessions)\n");
                ConsoleFormatter.PrintSessionHistory(sessions);
            }
        }, countOption, sessionIdOption);

        return command;
    }
}
