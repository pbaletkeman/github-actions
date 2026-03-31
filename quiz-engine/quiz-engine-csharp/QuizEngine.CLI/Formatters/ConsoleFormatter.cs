using Spectre.Console;
using QuizEngine.Entities;
using QuizEngine.Service;

namespace QuizEngine.CLI.Formatters;

public static class ConsoleFormatter
{
    public static void PrintQuestion(int num, int total, string questionText, string[] options)
    {
        AnsiConsole.WriteLine();
        var panel = new Panel($"[bold white]Question {num} of {total}[/]\n\n[yellow]{Markup.Escape(questionText)}[/]")
        {
            Border = BoxBorder.Rounded,
            BorderStyle = new Style(Color.Cyan1)
        };
        AnsiConsole.Write(panel);

        for (int i = 0; i < options.Length; i++)
        {
            var letter = (char)('A' + i);
            AnsiConsole.MarkupLine($"  [green]{letter})[/] {Markup.Escape(options[i])}");
        }
        AnsiConsole.WriteLine();
    }

    public static void PrintAnswerResult(AnswerResult result, bool showExplanation = true)
    {
        if (result.IsCorrect)
            AnsiConsole.MarkupLine("[bold green]✓ Correct![/]");
        else
            AnsiConsole.MarkupLine($"[bold red]✗ Incorrect. Correct answer: {result.CorrectAnswer}[/]");

        if (showExplanation && !string.IsNullOrEmpty(result.Explanation))
        {
            AnsiConsole.MarkupLine($"[dim italic]{Markup.Escape(result.Explanation)}[/]");
        }
    }

    public static void PrintQuizResult(QuizResult result)
    {
        AnsiConsole.WriteLine();
        AnsiConsole.Write(new Rule("[bold yellow]Quiz Complete![/]"));

        var table = new Table();
        table.AddColumn("[bold]Metric[/]");
        table.AddColumn("[bold]Value[/]");
        table.Border = TableBorder.Rounded;

        table.AddRow("Session ID", result.SessionId);
        table.AddRow("Questions", result.NumQuestions.ToString());
        table.AddRow("Correct", $"[green]{result.NumCorrect}[/]");
        table.AddRow("Incorrect", $"[red]{result.NumQuestions - result.NumCorrect}[/]");

        var scoreColor = result.PercentageCorrect >= 80 ? "green" : result.PercentageCorrect >= 60 ? "yellow" : "red";
        table.AddRow("Score", $"[{scoreColor}]{result.PercentageCorrect:F1}%[/]");
        table.AddRow("Time Taken", FormatTime(result.TimeTakenSeconds));

        AnsiConsole.Write(table);
        AnsiConsole.WriteLine();
    }

    public static void PrintSessionHistory(List<QuizSession> sessions)
    {
        if (sessions.Count == 0)
        {
            AnsiConsole.MarkupLine("[yellow]No quiz history found.[/]");
            return;
        }

        var table = new Table();
        table.AddColumn("[bold]#[/]");
        table.AddColumn("[bold]Session ID[/]");
        table.AddColumn("[bold]Date[/]");
        table.AddColumn("[bold]Questions[/]");
        table.AddColumn("[bold]Correct[/]");
        table.AddColumn("[bold]Score[/]");
        table.AddColumn("[bold]Time[/]");
        table.Border = TableBorder.Rounded;

        for (int i = 0; i < sessions.Count; i++)
        {
            var s = sessions[i];
            var scoreColor = s.PercentageCorrect >= 80 ? "green" : s.PercentageCorrect >= 60 ? "yellow" : "red";
            table.AddRow(
                (i + 1).ToString(),
                s.SessionId,
                s.StartedAt.ToLocalTime().ToString("yyyy-MM-dd HH:mm"),
                s.NumQuestions.ToString(),
                s.NumCorrect.ToString(),
                $"[{scoreColor}]{s.PercentageCorrect:F1}%[/]",
                s.TimeTakenSeconds.HasValue ? FormatTime(s.TimeTakenSeconds.Value) : "N/A"
            );
        }

        AnsiConsole.Write(table);
    }

    public static void PrintSessionDetail(QuizSession session, List<QuizResponse> responses)
    {
        AnsiConsole.MarkupLine($"\n[bold]Session:[/] {session.SessionId}");
        AnsiConsole.MarkupLine($"[bold]Date:[/] {session.StartedAt.ToLocalTime():yyyy-MM-dd HH:mm:ss}");
        AnsiConsole.MarkupLine($"[bold]Score:[/] {session.NumCorrect}/{session.NumQuestions} ({session.PercentageCorrect:F1}%)");

        if (responses.Count > 0)
        {
            var table = new Table();
            table.AddColumn("[bold]Q#[/]");
            table.AddColumn("[bold]Question[/]");
            table.AddColumn("[bold]Your Answer[/]");
            table.AddColumn("[bold]Result[/]");
            table.Border = TableBorder.Rounded;

            for (int i = 0; i < responses.Count; i++)
            {
                var r = responses[i];
                var qText = r.Question?.QuestionText ?? "Unknown";
                if (qText.Length > 50)
                    qText = qText[..47] + "...";

                var result = r.IsCorrect == 1 ? "[green]✓[/]" : "[red]✗[/]";
                table.AddRow(
                    (i + 1).ToString(),
                    Markup.Escape(qText),
                    r.UserAnswer ?? "-",
                    result
                );
            }

            AnsiConsole.Write(table);
        }
    }

    private static string FormatTime(int seconds)
    {
        if (seconds < 60) return $"{seconds}s";
        var mins = seconds / 60;
        var secs = seconds % 60;
        return $"{mins}m {secs}s";
    }
}
