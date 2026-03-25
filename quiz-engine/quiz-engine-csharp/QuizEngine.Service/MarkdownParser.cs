using System.Text.RegularExpressions;
using QuizEngine.Entities;

namespace QuizEngine.Service;

public static class MarkdownParser
{
    // Matches a question block from markdown files
    // Expected format:
    // ## Question
    // **Q: <question text>**
    // - A) <option>
    // - B) <option>
    // ...
    // **Answer: X**
    // **Explanation:** <text>
    // Section: <section>
    // Difficulty: <difficulty>

    private static readonly Regex QuestionPattern = new(@"^\*\*Q:\s*(.+?)\*\*\s*$", RegexOptions.Multiline);
    private static readonly Regex OptionPattern = new(@"^-\s+([A-E])\)\s+(.+)$", RegexOptions.Multiline);
    private static readonly Regex AnswerPattern = new(@"\*\*Answer:\s*([A-E])\*\*", RegexOptions.Multiline);
    private static readonly Regex ExplanationPattern = new(@"\*\*Explanation:\*\*\s*(.+?)(?=\n\n|\n##|\z)", RegexOptions.Singleline);
    private static readonly Regex SectionPattern = new(@"(?:^|\n)Section:\s*(.+?)(?:\n|$)", RegexOptions.Multiline);
    private static readonly Regex DifficultyPattern = new(@"(?:^|\n)Difficulty:\s*(.+?)(?:\n|$)", RegexOptions.Multiline);

    public static List<Question> ParseFile(string filePath)
    {
        if (!File.Exists(filePath))
            throw new FileNotFoundException($"Markdown file not found: {filePath}");

        var content = File.ReadAllText(filePath);
        return Parse(content, filePath);
    }

    public static List<Question> Parse(string content, string? sourceFile = null)
    {
        var questions = new List<Question>();

        // Split into blocks separated by "##" headers
        var blocks = Regex.Split(content, @"(?=^##\s)", RegexOptions.Multiline)
            .Where(b => !string.IsNullOrWhiteSpace(b))
            .ToList();

        foreach (var block in blocks)
        {
            var question = ParseBlock(block, sourceFile);
            if (question != null)
                questions.Add(question);
        }

        return questions;
    }

    private static Question? ParseBlock(string block, string? sourceFile)
    {
        var questionMatch = QuestionPattern.Match(block);
        if (!questionMatch.Success)
            return null;

        var answerMatch = AnswerPattern.Match(block);
        if (!answerMatch.Success)
            return null;

        var optionMatches = OptionPattern.Matches(block);
        if (optionMatches.Count < 4)
            return null;

        var options = new Dictionary<string, string>();
        foreach (Match m in optionMatches)
            options[m.Groups[1].Value.ToUpper()] = m.Groups[2].Value.Trim();

        if (!options.ContainsKey("A") || !options.ContainsKey("B") ||
            !options.ContainsKey("C") || !options.ContainsKey("D"))
            return null;

        var explanationMatch = ExplanationPattern.Match(block);
        var sectionMatch = SectionPattern.Match(block);
        var difficultyMatch = DifficultyPattern.Match(block);

        return new Question
        {
            QuestionText = questionMatch.Groups[1].Value.Trim(),
            OptionA = options["A"],
            OptionB = options["B"],
            OptionC = options["C"],
            OptionD = options["D"],
            OptionE = options.TryGetValue("E", out var e) ? e : null,
            CorrectAnswer = answerMatch.Groups[1].Value.ToUpper(),
            Explanation = explanationMatch.Success ? explanationMatch.Groups[1].Value.Trim() : null,
            Section = sectionMatch.Success ? sectionMatch.Groups[1].Value.Trim() : null,
            Difficulty = difficultyMatch.Success ? difficultyMatch.Groups[1].Value.Trim() : null,
            SourceFile = sourceFile
        };
    }
}
