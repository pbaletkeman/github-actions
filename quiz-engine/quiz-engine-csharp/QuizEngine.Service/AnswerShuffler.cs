namespace QuizEngine.Service;

public class ShuffleResult
{
    public string[] ShuffledOptions { get; set; } = Array.Empty<string>();
    public int CorrectShuffledIndex { get; set; }
    public string CorrectShuffledLetter { get; set; } = string.Empty;
    public Dictionary<string, string> LetterMap { get; set; } = new();
}

public static class AnswerShuffler
{
    private static readonly string[] Letters = { "A", "B", "C", "D", "E" };

    public static ShuffleResult Shuffle(string[] options, string correctLetter, Random? rng = null)
    {
        rng ??= Random.Shared;

        var letterIndex = Array.IndexOf(Letters, correctLetter.ToUpper());
        if (letterIndex < 0 || letterIndex >= options.Length)
            throw new ArgumentException($"Invalid correct answer letter: {correctLetter}");

        var correctText = options[letterIndex];

        // Create indexed list and shuffle
        var indexed = options
            .Select((opt, i) => (opt, i))
            .ToList();

        // Fisher-Yates shuffle
        for (int i = indexed.Count - 1; i > 0; i--)
        {
            int j = rng.Next(i + 1);
            (indexed[i], indexed[j]) = (indexed[j], indexed[i]);
        }

        var shuffled = indexed.Select(x => x.opt).ToArray();
        var correctShuffledIndex = Array.IndexOf(shuffled, correctText);
        var correctShuffledLetter = Letters[correctShuffledIndex];

        // Build letter map: new letter -> original letter
        var letterMap = new Dictionary<string, string>();
        for (int i = 0; i < shuffled.Length; i++)
        {
            var originalIndex = Array.IndexOf(options, shuffled[i]);
            letterMap[Letters[i]] = Letters[originalIndex];
        }

        return new ShuffleResult
        {
            ShuffledOptions = shuffled,
            CorrectShuffledIndex = correctShuffledIndex,
            CorrectShuffledLetter = correctShuffledLetter,
            LetterMap = letterMap
        };
    }

    public static string[] GetOptionsArray(QuizEngine.Entities.Question q)
    {
        var opts = new List<string> { q.OptionA, q.OptionB, q.OptionC, q.OptionD };
        if (!string.IsNullOrWhiteSpace(q.OptionE))
            opts.Add(q.OptionE);
        return opts.ToArray();
    }

    public static ShuffleResult Identity(string[] options, string correctLetter)
    {
        var letterIndex = Array.IndexOf(Letters, correctLetter.ToUpper());
        if (letterIndex < 0 || letterIndex >= options.Length)
            throw new ArgumentException($"Invalid correct answer letter: {correctLetter}");

        var letterMap = new Dictionary<string, string>();
        for (int i = 0; i < options.Length; i++)
            letterMap[Letters[i]] = Letters[i];

        return new ShuffleResult
        {
            ShuffledOptions = options,
            CorrectShuffledIndex = letterIndex,
            CorrectShuffledLetter = correctLetter.ToUpper(),
            LetterMap = letterMap
        };
    }
}
