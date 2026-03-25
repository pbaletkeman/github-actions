using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace QuizEngine.Entities;

[Table("quiz_sessions")]
public class QuizSession
{
    [Key]
    [MaxLength(36)]
    public string SessionId { get; set; } = Guid.NewGuid().ToString();

    public DateTime StartedAt { get; set; } = DateTime.UtcNow;

    public DateTime? EndedAt { get; set; }

    [Required]
    public int NumQuestions { get; set; }

    [DefaultValue(0)]
    public int NumCorrect { get; set; } = 0;

    [DefaultValue(0.0)]
    public double PercentageCorrect { get; set; } = 0.0;

    public int? TimeTakenSeconds { get; set; }

    public ICollection<QuizResponse> Responses { get; } = new List<QuizResponse>();
}
