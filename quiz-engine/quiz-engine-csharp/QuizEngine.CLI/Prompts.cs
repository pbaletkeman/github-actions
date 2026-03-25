using Spectre.Console;

namespace QuizEngine.CLI;

public static class ConsolePrompts
{
    public static int GetInt(string prompt, int defaultValue)
    {
        var input = AnsiConsole.Ask<string>($"[yellow]{prompt}[/] [[{defaultValue}]]:");
        if (string.IsNullOrWhiteSpace(input))
            return defaultValue;
        return int.TryParse(input, out var result) ? result : defaultValue;
    }

    public static string GetAnswer(int questionNum, int totalQuestions)
    {
        AnsiConsole.Markup($"[cyan]Q{questionNum}/{totalQuestions} - Enter your answer (A-E) or ENTER to skip: [/]");
        var input = Console.ReadLine()?.Trim().ToUpper() ?? string.Empty;
        return input;
    }

    public static string? GetOptionalString(string prompt)
    {
        var input = AnsiConsole.Ask<string>($"[yellow]{prompt}[/] [[leave blank to skip]]:");
        return string.IsNullOrWhiteSpace(input) ? null : input.Trim();
    }

    public static bool Confirm(string prompt)
    {
        return AnsiConsole.Confirm($"[yellow]{prompt}[/]");
    }
}
