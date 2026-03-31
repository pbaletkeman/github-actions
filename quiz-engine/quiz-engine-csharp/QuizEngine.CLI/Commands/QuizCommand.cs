using System.CommandLine;
using Microsoft.Extensions.DependencyInjection;
using QuizEngine.CLI.Formatters;
using QuizEngine.Service;
using Spectre.Console;

namespace QuizEngine.CLI.Commands;

public static class QuizCommand
{
    public static Command Build(IServiceProvider services)
    {
        var questionsOption = new Option<int>(
            "--questions",
            getDefaultValue: () => 10,
            description: "Number of questions to ask");
        questionsOption.AddAlias("-n");

        var difficultyOption = new Option<string?>(
            "--difficulty",
            description: "Filter by difficulty (easy/medium/hard)");

        var sectionOption = new Option<string?>(
            "--section",
            description: "Filter by section/topic");

        var noExplanationOption = new Option<bool>(
            "--no-explanation",
            description: "Skip showing explanations after each answer");

        var noShuffleOption = new Option<bool>(
            "--no-shuffle",
            description: "Keep answer options in original order (do not randomise)");

        var command = new Command("quiz", "Take an interactive quiz");
        command.AddOption(questionsOption);
        command.AddOption(difficultyOption);
        command.AddOption(sectionOption);
        command.AddOption(noExplanationOption);
        command.AddOption(noShuffleOption);

        command.SetHandler(async (int numQuestions, string? difficulty, string? section, bool noExplanation, bool noShuffle) =>
        {
            await RunQuizAsync(services, numQuestions, difficulty, section, !noExplanation, noShuffle);
        }, questionsOption, difficultyOption, sectionOption, noExplanationOption, noShuffleOption);

        return command;
    }

    private static async Task RunQuizAsync(
        IServiceProvider services,
        int numQuestions,
        string? difficulty,
        string? section,
        bool showExplanation,
        bool noShuffle = false)
    {
        using var scope = services.CreateScope();
        var quizService = scope.ServiceProvider.GetRequiredService<QuizService>();

        try
        {
            AnsiConsole.Clear();
        }
        catch
        {
            // Clear may fail if console is not available (e.g., piped input)
        }

        AnsiConsole.Write(new FigletText("Quiz Engine").Color(Color.Cyan1));
        AnsiConsole.MarkupLine("[dim]GitHub Actions GH-200 Certification Prep[/]\n");

        ActiveQuizState state;
        try
        {
            state = await quizService.StartQuizAsync(numQuestions, difficulty, section, noShuffle);
        }
        catch (InvalidOperationException ex)
        {
            AnsiConsole.MarkupLine($"[red]Error:[/] {ex.Message}");
            AnsiConsole.MarkupLine("[yellow]Tip:[/] Run [bold]quiz import --file questions.md[/] to import questions first.");
            return;
        }

        AnsiConsole.MarkupLine($"[bold]Starting quiz:[/] {state.Questions.Count} questions");
        if (difficulty != null) AnsiConsole.MarkupLine($"[dim]Difficulty: {difficulty}[/]");
        if (section != null) AnsiConsole.MarkupLine($"[dim]Section: {section}[/]");
        AnsiConsole.WriteLine();

        var allResults = new List<(int Index, AnswerResult Result)>();

        for (int i = 0; i < state.Questions.Count; i++)
        {
            var question = state.Questions[i];
            var shuffle = state.ShuffleResults[i];

            ConsoleFormatter.PrintQuestion(i + 1, state.Questions.Count, question.QuestionText, shuffle.ShuffledOptions);

            var answer = ConsolePrompts.GetAnswer(i + 1, state.Questions.Count);

            if (string.IsNullOrEmpty(answer))
            {
                AnsiConsole.MarkupLine("[dim]Skipped.[/]");
                // Still record as wrong
                var skippedResult = await quizService.SubmitAnswerAsync(state.SessionId, i, "?", 0);
                allResults.Add((i, skippedResult));
                continue;
            }

            var result = await quizService.SubmitAnswerAsync(state.SessionId, i, answer, 0);
            // Don't show any feedback yet - just collect results
            allResults.Add((i, result));
        }

        // Show all results and explanations after quiz is complete
        AnsiConsole.WriteLine();
        AnsiConsole.Write(new Rule("[bold yellow]Quiz Results[/]"));

        foreach (var (index, result) in allResults)
        {
            AnsiConsole.WriteLine();
            AnsiConsole.MarkupLine($"[bold]Question {index + 1}:[/]");

            if (result.IsCorrect)
                AnsiConsole.MarkupLine("[bold green]✓ Correct![/]");
            else
                AnsiConsole.MarkupLine($"[bold red]✗ Incorrect. Correct answer: {result.CorrectAnswer}[/]");

            if (showExplanation && !string.IsNullOrEmpty(result.Explanation))
            {
                AnsiConsole.MarkupLine($"[dim italic]{Markup.Escape(result.Explanation)}[/]");
            }
        }

        var quizResult = await quizService.FinalizeAsync(state.SessionId);
        ConsoleFormatter.PrintQuizResult(quizResult);
    }
}
