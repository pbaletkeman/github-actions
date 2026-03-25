using QuizEngine.Service;
using Xunit;

namespace QuizEngine.Tests;

public class MarkdownParserTests
{
    private const string ValidMarkdown = @"
## Question 1

**Q: What does CI stand for?**

- A) Continuous Integration
- B) Code Integration
- C) Complete Infrastructure
- D) Cloud Infrastructure

**Answer: A**

**Explanation:** CI stands for Continuous Integration.

Section: GitHub Actions
Difficulty: easy

## Question 2

**Q: What is GitHub Actions?**

- A) A database service
- B) A CI/CD platform
- C) A version control system
- D) A container registry

**Answer: B**

Section: Workflows
Difficulty: medium
";

    [Fact]
    public void Parse_ValidMarkdown_ReturnsTwoQuestions()
    {
        var questions = MarkdownParser.Parse(ValidMarkdown);
        Assert.Equal(2, questions.Count);
    }

    [Fact]
    public void Parse_ValidMarkdown_ParsesQuestionText()
    {
        var questions = MarkdownParser.Parse(ValidMarkdown);
        Assert.Equal("What does CI stand for?", questions[0].QuestionText);
    }

    [Fact]
    public void Parse_ValidMarkdown_ParsesOptions()
    {
        var questions = MarkdownParser.Parse(ValidMarkdown);
        Assert.Equal("Continuous Integration", questions[0].OptionA);
        Assert.Equal("Code Integration", questions[0].OptionB);
        Assert.Equal("Complete Infrastructure", questions[0].OptionC);
        Assert.Equal("Cloud Infrastructure", questions[0].OptionD);
    }

    [Fact]
    public void Parse_ValidMarkdown_ParsesCorrectAnswer()
    {
        var questions = MarkdownParser.Parse(ValidMarkdown);
        Assert.Equal("A", questions[0].CorrectAnswer);
        Assert.Equal("B", questions[1].CorrectAnswer);
    }

    [Fact]
    public void Parse_ValidMarkdown_ParsesExplanation()
    {
        var questions = MarkdownParser.Parse(ValidMarkdown);
        Assert.NotNull(questions[0].Explanation);
        Assert.Contains("Continuous Integration", questions[0].Explanation);
    }

    [Fact]
    public void Parse_ValidMarkdown_ParsesSection()
    {
        var questions = MarkdownParser.Parse(ValidMarkdown);
        Assert.Equal("GitHub Actions", questions[0].Section);
        Assert.Equal("Workflows", questions[1].Section);
    }

    [Fact]
    public void Parse_ValidMarkdown_ParsesDifficulty()
    {
        var questions = MarkdownParser.Parse(ValidMarkdown);
        Assert.Equal("easy", questions[0].Difficulty);
        Assert.Equal("medium", questions[1].Difficulty);
    }

    [Fact]
    public void Parse_ValidMarkdown_SetsSourceFile()
    {
        var questions = MarkdownParser.Parse(ValidMarkdown, "test.md");
        Assert.All(questions, q => Assert.Equal("test.md", q.SourceFile));
    }

    [Fact]
    public void Parse_EmptyContent_ReturnsEmpty()
    {
        var questions = MarkdownParser.Parse(string.Empty);
        Assert.Empty(questions);
    }

    [Fact]
    public void Parse_ContentWithoutQuestions_ReturnsEmpty()
    {
        var content = "# Just a heading\n\nSome text without questions.";
        var questions = MarkdownParser.Parse(content);
        Assert.Empty(questions);
    }

    [Fact]
    public void Parse_QuestionWithoutAnswer_SkipsBlock()
    {
        var content = @"
## Bad Question

**Q: What is this?**

- A) Option A
- B) Option B
- C) Option C
- D) Option D

No answer provided.
";
        var questions = MarkdownParser.Parse(content);
        Assert.Empty(questions);
    }

    [Fact]
    public void Parse_QuestionWithFiveOptions()
    {
        var content = @"
## Q5

**Q: Which are valid GitHub Actions events?**

- A) push
- B) pull_request
- C) schedule
- D) workflow_dispatch
- E) all of the above

**Answer: E**
";
        var questions = MarkdownParser.Parse(content);
        Assert.Single(questions);
        Assert.Equal("E", questions[0].CorrectAnswer);
        Assert.NotNull(questions[0].OptionE);
        Assert.Equal("all of the above", questions[0].OptionE);
    }

    [Fact]
    public void ParseFile_ThrowsWhenFileNotFound()
    {
        Assert.Throws<FileNotFoundException>(() =>
            MarkdownParser.ParseFile("/nonexistent/path/file.md"));
    }

    [Fact]
    public async Task ParseFile_ReadsFromDisk()
    {
        var tmpFile = Path.GetTempFileName() + ".md";
        try
        {
            await File.WriteAllTextAsync(tmpFile, ValidMarkdown);
            var questions = MarkdownParser.ParseFile(tmpFile);
            Assert.Equal(2, questions.Count);
        }
        finally
        {
            File.Delete(tmpFile);
        }
    }
}
