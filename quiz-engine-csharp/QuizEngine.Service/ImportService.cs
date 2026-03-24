using QuizEngine.Data;
using QuizEngine.Entities;

namespace QuizEngine.Service;

public class ImportService
{
    private readonly IQuestionRepository _questionRepo;

    public ImportService(IQuestionRepository questionRepo)
    {
        _questionRepo = questionRepo;
    }

    public async Task<(int Imported, int Skipped)> ImportFromFileAsync(string filePath)
    {
        var questions = MarkdownParser.ParseFile(filePath);
        return await ImportQuestionsAsync(questions);
    }

    public async Task<(int Imported, int Skipped)> ImportFromDirectoryAsync(string dirPath)
    {
        if (!Directory.Exists(dirPath))
            throw new DirectoryNotFoundException($"Directory not found: {dirPath}");

        var files = Directory.GetFiles(dirPath, "*.md", SearchOption.AllDirectories);
        var allQuestions = new List<Question>();

        foreach (var file in files)
        {
            var questions = MarkdownParser.ParseFile(file);
            allQuestions.AddRange(questions);
        }

        return await ImportQuestionsAsync(allQuestions);
    }

    private async Task<(int Imported, int Skipped)> ImportQuestionsAsync(List<Question> questions)
    {
        int imported = 0;
        int skipped = 0;

        // Deduplicate within the batch by question text + correct answer
        var seen = new HashSet<string>();

        foreach (var q in questions)
        {
            var key = $"{q.QuestionText}|{q.CorrectAnswer}";
            if (seen.Contains(key))
            {
                skipped++;
                continue;
            }
            seen.Add(key);

            var countBefore = await _questionRepo.CountAsync();
            await _questionRepo.InsertAsync(q);
            var countAfter = await _questionRepo.CountAsync();

            if (countAfter > countBefore)
                imported++;
            else
                skipped++;
        }

        return (imported, skipped);
    }
}

