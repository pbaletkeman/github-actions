using System.Text.RegularExpressions;
using QuizEngine.Entities;

namespace QuizEngine.Service;

public static class MarkdownParser
{
    // Supports two formats:
    //
    // Format 1 (original):
    // ## Question N
    // **Q: <question text>**
    // - A) <option>
    // - B) <option>
    // ...
    // **Answer: X**
    // **Explanation:** <text>
    // Section: <section>
    // Difficulty: <difficulty>
    //
    // Format 2 (gh-200-iteration-1.md):
    // ### Question N — <Topic>
    // **Difficulty**: <difficulty>
    // **Answer Type**: <one|many|none>
    // **Topic**: <topic>
    // **Question**:
    // <question text>
    // - A) <option>
    // - B) <option>
    // ...
    // (Answer and explanation extracted from answer key section at end)

    private static readonly Regex QuestionHeaderNewFormat = new(@"^###\s+Question\s+\d+\s+—\s+(.+?)\s*$", RegexOptions.Multiline);
    private static readonly Regex DifficultyNewFormat = new(@"^\*\*Difficulty\*\*:\s*(.+?)\s*$", RegexOptions.Multiline);
    private static readonly Regex TopicNewFormat = new(@"^\*\*Topic\*\*:\s*(.+?)\s*$", RegexOptions.Multiline);
    private static readonly Regex QuestionTextNewFormat = new(@"^\*\*Question\*\*:\s*\n(.+?)(?=\n-\s+[A-E]\)|$)", RegexOptions.Multiline | RegexOptions.Singleline);
    private static readonly Regex OptionPattern = new(@"^-\s+([A-E])\)\s+(.+)$", RegexOptions.Multiline);
    private static readonly Regex AnswerKeyPattern = new(@"^\|\s*(\d+)\s*\|\s*([A-E,\s]+?)\s*\|", RegexOptions.Multiline);

    // Old format patterns (backward compatibility)
    private static readonly Regex OldQuestionHeader = new(@"^##\s+Question", RegexOptions.Multiline);
    private static readonly Regex OldQuestionPattern = new(@"^\*\*Q:\s*(.+?)\*\*\s*$", RegexOptions.Multiline);
    private static readonly Regex OldAnswerPattern = new(@"\*\*Answer:\s*([A-E])\*\*", RegexOptions.Multiline);
    private static readonly Regex OldExplanationPattern = new(@"\*\*Explanation:\*\*\s*(.+?)(?=\n\n|\n##|\z)", RegexOptions.Singleline);
    private static readonly Regex OldSectionPattern = new(@"(?:^|\n)Section:\s*(.+?)(?:\n|$)", RegexOptions.Multiline);
    private static readonly Regex OldDifficultyPattern = new(@"(?:^|\n)Difficulty:\s*(.+?)(?:\n|$)", RegexOptions.Multiline);

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

        // Detect format by checking for format indicators
        bool isNewFormat = Regex.IsMatch(content, @"^###\s+Question\s+\d+\s+—", RegexOptions.Multiline) &&
                          Regex.IsMatch(content, @"^\*\*Difficulty\*\*:", RegexOptions.Multiline);

        if (isNewFormat)
        {
            // Build answer key from table at end of file
            var answerKey = ExtractAnswerKey(content);

            // Split into blocks separated by "###" headers (new format)
            var blocks = Regex.Split(content, @"(?=^###\s+Question\s+\d+)", RegexOptions.Multiline)
                .Where(b => !string.IsNullOrWhiteSpace(b))
                .ToList();

            foreach (var block in blocks)
            {
                var question = ParseBlockNewFormat(block, answerKey, sourceFile);
                if (question != null)
                    questions.Add(question);
            }
        }
        else
        {
            // Old format: split by "##" headers
            var blocks = Regex.Split(content, @"(?=^##\s)", RegexOptions.Multiline)
                .Where(b => !string.IsNullOrWhiteSpace(b))
                .ToList();

            foreach (var block in blocks)
            {
                var question = ParseBlockOldFormat(block, sourceFile);
                if (question != null)
                    questions.Add(question);
            }
        }

        return questions;
    }

    private static Dictionary<int, (string Answer, string? Explanation)> ExtractAnswerKey(string content)
    {
        var answerKey = new Dictionary<int, (string, string?)>();

        // Extract answer key table (lines starting with |)
        var lines = content.Split('\n');
        var inTable = false;

        foreach (var line in lines)
        {
            if (!line.StartsWith("|"))
                inTable = false;

            if (!inTable && line.Contains("| Q# | Answer"))
            {
                inTable = true;
                continue;
            }

            if (!inTable || !line.StartsWith("|"))
                continue;

            // Parse table row: | Q# | Answer(s) | Explanation | Source | Difficulty |
            var parts = line.Split('|').Select(p => p.Trim()).ToList();

            if (parts.Count < 3)
                continue;

            if (int.TryParse(parts[1], out int qNum))
            {
                var answer = parts[2]; // Answer(s)
                var explanation = parts.Count > 3 ? parts[3] : null;
                answerKey[qNum] = (answer, explanation);
            }
        }

        return answerKey;
    }

    private static Question? ParseBlockNewFormat(string block, Dictionary<int, (string Answer, string?)> answerKey, string? sourceFile)
    {
        // Extract question number
        var headerMatch = QuestionHeaderNewFormat.Match(block);
        if (!headerMatch.Success)
            return null;

        var topicMatch = TopicNewFormat.Match(block);
        var difficultyMatch = DifficultyNewFormat.Match(block);
        var questionTextMatch = QuestionTextNewFormat.Match(block);

        if (!questionTextMatch.Success)
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

        // Extract question number from header
        var qNumMatch = Regex.Match(headerMatch.Groups[1].Value, @"^Question\s+(\d+)");
        int? questionNum = null;
        if (qNumMatch.Success && int.TryParse(qNumMatch.Groups[1].Value, out int num))
            questionNum = num;

        // Try to get answer from answer key
        string correctAnswer = "A"; // default
        string? explanation = null;

        if (questionNum.HasValue && answerKey.TryGetValue(questionNum.Value, out var keyEntry))
        {
            // Parse answer(s) - could be "A" or "A, B, D" for multiple choice
            var answerLetters = keyEntry.Item1.Split(',').Select(s => s.Trim()).Where(s => s.Length == 1).FirstOrDefault();
            if (!string.IsNullOrEmpty(answerLetters))
                correctAnswer = answerLetters.ToUpper();

            explanation = keyEntry.Item2;
        }

        return new Question
        {
            QuestionText = questionTextMatch.Groups[1].Value.Trim(),
            OptionA = options["A"],
            OptionB = options["B"],
            OptionC = options["C"],
            OptionD = options["D"],
            OptionE = options.TryGetValue("E", out var e) ? e : null,
            CorrectAnswer = correctAnswer,
            Explanation = explanation,
            Section = topicMatch.Success ? topicMatch.Groups[1].Value.Trim() : null,
            Difficulty = difficultyMatch.Success ? difficultyMatch.Groups[1].Value.Trim() : null,
            SourceFile = sourceFile
        };
    }

    private static Question? ParseBlockOldFormat(string block, string? sourceFile)
    {
        var questionMatch = OldQuestionPattern.Match(block);
        if (!questionMatch.Success)
            return null;

        var answerMatch = OldAnswerPattern.Match(block);
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

        var explanationMatch = OldExplanationPattern.Match(block);
        var sectionMatch = OldSectionPattern.Match(block);
        var difficultyMatch = OldDifficultyPattern.Match(block);

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
