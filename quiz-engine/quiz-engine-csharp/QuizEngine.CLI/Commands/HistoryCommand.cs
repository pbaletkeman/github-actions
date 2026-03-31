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

        var sortOption = new Option<string>(
            "--sort",
            getDefaultValue: () => "date",
            description: "Sort by: date, score, questions, or time");

        var orderOption = new Option<string>(
            "--order",
            getDefaultValue: () => "desc",
            description: "Sort order: asc or desc");

        var exportOption = new Option<string?>(
            "--export",
            description: "Export history to file: json or csv");

        var command = new Command("history", "View quiz history");
        command.AddOption(countOption);
        command.AddOption(sessionIdOption);
        command.AddOption(sortOption);
        command.AddOption(orderOption);
        command.AddOption(exportOption);

        command.SetHandler(async (int count, string? sessionId, string sort, string order, string? export) =>
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
            else if (export != null)
            {
                var sessions = await historyService.GetRecentSessionsAsync(int.MaxValue, sort, order);
                var format = export.ToLower();
                if (format == "json")
                {
                    var path = $"quiz-history-{DateTime.UtcNow:yyyyMMddHHmmss}.json";
                    await historyService.ExportToJsonAsync(sessions, path);
                    AnsiConsole.MarkupLine($"[green]Exported {sessions.Count} sessions to[/] {path}");
                }
                else if (format == "csv")
                {
                    var path = $"quiz-history-{DateTime.UtcNow:yyyyMMddHHmmss}.csv";
                    await historyService.ExportToCsvAsync(sessions, path);
                    AnsiConsole.MarkupLine($"[green]Exported {sessions.Count} sessions to[/] {path}");
                }
                else
                {
                    AnsiConsole.MarkupLine($"[red]Unknown export format:[/] {export}. Use 'json' or 'csv'.");
                }
            }
            else
            {
                var sessions = await historyService.GetRecentSessionsAsync(count, sort, order);
                var total = await historyService.GetTotalSessionsAsync();
                AnsiConsole.MarkupLine($"\n[bold]Quiz History[/] (showing {sessions.Count} of {total} sessions)\n");
                ConsoleFormatter.PrintSessionHistory(sessions);
            }
        }, countOption, sessionIdOption, sortOption, orderOption, exportOption);

        return command;
    }
}
