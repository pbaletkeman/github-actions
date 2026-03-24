using System.CommandLine;
using Microsoft.Extensions.DependencyInjection;
using QuizEngine.Service;
using Spectre.Console;

namespace QuizEngine.CLI.Commands;

public static class ImportCommand
{
    public static Command Build(IServiceProvider services)
    {
        var fileOption = new Option<string?>(
            "--file",
            description: "Path to a markdown file containing questions");
        fileOption.AddAlias("-f");

        var dirOption = new Option<string?>(
            "--dir",
            description: "Path to a directory containing markdown files");
        dirOption.AddAlias("-d");

        var command = new Command("import", "Import questions from markdown files");
        command.AddOption(fileOption);
        command.AddOption(dirOption);

        command.SetHandler(async (string? file, string? dir) =>
        {
            if (file == null && dir == null)
            {
                AnsiConsole.MarkupLine("[red]Error:[/] Specify --file or --dir");
                return;
            }

            using var scope = services.CreateScope();
            var importService = scope.ServiceProvider.GetRequiredService<ImportService>();

            try
            {
                if (file != null)
                {
                    AnsiConsole.MarkupLine($"[yellow]Importing from file:[/] {file}");
                    var (imported, skipped) = await importService.ImportFromFileAsync(file);
                    AnsiConsole.MarkupLine($"[green]✓ Imported:[/] {imported} questions, [dim]Skipped (duplicates):[/] {skipped}");
                }
                else if (dir != null)
                {
                    AnsiConsole.MarkupLine($"[yellow]Importing from directory:[/] {dir}");
                    var (imported, skipped) = await importService.ImportFromDirectoryAsync(dir);
                    AnsiConsole.MarkupLine($"[green]✓ Imported:[/] {imported} questions, [dim]Skipped (duplicates):[/] {skipped}");
                }
            }
            catch (FileNotFoundException ex)
            {
                AnsiConsole.MarkupLine($"[red]Error:[/] {ex.Message}");
            }
            catch (DirectoryNotFoundException ex)
            {
                AnsiConsole.MarkupLine($"[red]Error:[/] {ex.Message}");
            }
        }, fileOption, dirOption);

        return command;
    }
}
