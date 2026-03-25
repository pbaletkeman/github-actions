using QuizEngine.Service;
using Xunit;

namespace QuizEngine.Tests;

public class AnswerShufflerTests
{
    private static readonly string[] FourOptions = { "Alpha", "Beta", "Gamma", "Delta" };
    private static readonly string[] FiveOptions = { "Alpha", "Beta", "Gamma", "Delta", "Epsilon" };

    [Fact]
    public void Shuffle_PreservesAllOptions()
    {
        var result = AnswerShuffler.Shuffle(FourOptions, "A");
        Assert.Equal(new HashSet<string>(FourOptions), new HashSet<string>(result.ShuffledOptions));
    }

    [Fact]
    public void Shuffle_MapsCorrectAnswerToNewPosition()
    {
        var result = AnswerShuffler.Shuffle(FourOptions, "A");
        // "Alpha" is at index 0 (A) in original
        Assert.Equal("Alpha", result.ShuffledOptions[result.CorrectShuffledIndex]);
    }

    [Theory]
    [InlineData("A", "Alpha")]
    [InlineData("B", "Beta")]
    [InlineData("C", "Gamma")]
    [InlineData("D", "Delta")]
    public void Shuffle_CorrectAnswerTextPreserved(string letter, string expectedText)
    {
        var result = AnswerShuffler.Shuffle(FourOptions, letter);
        Assert.Equal(expectedText, result.ShuffledOptions[result.CorrectShuffledIndex]);
    }

    [Fact]
    public void Shuffle_CorrectShuffledLetterIsValid()
    {
        var result = AnswerShuffler.Shuffle(FourOptions, "A");
        var validLetters = new[] { "A", "B", "C", "D" };
        Assert.Contains(result.CorrectShuffledLetter, validLetters);
    }

    [Fact]
    public void Shuffle_WithFiveOptions_PreservesAllOptions()
    {
        var result = AnswerShuffler.Shuffle(FiveOptions, "E");
        Assert.Equal(new HashSet<string>(FiveOptions), new HashSet<string>(result.ShuffledOptions));
    }

    [Fact]
    public void Shuffle_WithFiveOptions_EOption()
    {
        var result = AnswerShuffler.Shuffle(FiveOptions, "E");
        // "Epsilon" is at index 4 (E) in original
        Assert.Equal("Epsilon", result.ShuffledOptions[result.CorrectShuffledIndex]);
    }

    [Fact]
    public void Shuffle_InvalidLetter_Throws()
    {
        Assert.Throws<ArgumentException>(() => AnswerShuffler.Shuffle(FourOptions, "Z"));
    }

    [Fact]
    public void Shuffle_WithFixedRng_IsReproducible()
    {
        var rng = new Random(42);
        var result1 = AnswerShuffler.Shuffle(FourOptions, "A", rng);

        rng = new Random(42);
        var result2 = AnswerShuffler.Shuffle(FourOptions, "A", rng);

        Assert.Equal(result1.ShuffledOptions, result2.ShuffledOptions);
        Assert.Equal(result1.CorrectShuffledIndex, result2.CorrectShuffledIndex);
    }

    [Fact]
    public void Shuffle_LetterMapContainsAllShuffledLetters()
    {
        var result = AnswerShuffler.Shuffle(FourOptions, "B");
        Assert.Equal(FourOptions.Length, result.LetterMap.Count);
        for (int i = 0; i < FourOptions.Length; i++)
        {
            var letter = ((char)('A' + i)).ToString();
            Assert.True(result.LetterMap.ContainsKey(letter));
        }
    }

    [Fact]
    public void GetOptionsArray_ReturnsFourOptions_WhenNoE()
    {
        var q = DatabaseFixture.BuildSampleQuestion();
        var opts = AnswerShuffler.GetOptionsArray(q);
        Assert.Equal(4, opts.Length);
        Assert.Equal("Continuous Integration/Continuous Delivery", opts[0]);
    }

    [Fact]
    public void GetOptionsArray_ReturnsFiveOptions_WhenEExists()
    {
        var q = DatabaseFixture.BuildSampleQuestion();
        q.OptionE = "Something Else";
        var opts = AnswerShuffler.GetOptionsArray(q);
        Assert.Equal(5, opts.Length);
    }

    [Fact]
    public void Shuffle_CorrectShuffledLetterMatchesIndex()
    {
        var result = AnswerShuffler.Shuffle(FourOptions, "C");
        var expectedLetter = ((char)('A' + result.CorrectShuffledIndex)).ToString();
        Assert.Equal(expectedLetter, result.CorrectShuffledLetter);
    }
}
