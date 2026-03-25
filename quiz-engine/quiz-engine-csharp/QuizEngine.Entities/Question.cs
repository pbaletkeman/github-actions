using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace QuizEngine.Entities;

[Table("questions")]
public class Question
{
    [Key]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public int Id { get; set; }

    [Required]
    [MaxLength(500)]
    public string QuestionText { get; set; } = string.Empty;

    [Required]
    [MaxLength(200)]
    public string OptionA { get; set; } = string.Empty;

    [Required]
    [MaxLength(200)]
    public string OptionB { get; set; } = string.Empty;

    [Required]
    [MaxLength(200)]
    public string OptionC { get; set; } = string.Empty;

    [Required]
    [MaxLength(200)]
    public string OptionD { get; set; } = string.Empty;

    [MaxLength(200)]
    public string? OptionE { get; set; }

    [Required]
    [MaxLength(1)]
    public string CorrectAnswer { get; set; } = string.Empty;

    [MaxLength(1000)]
    public string? Explanation { get; set; }

    [MaxLength(100)]
    public string? Section { get; set; }

    [MaxLength(50)]
    public string? Difficulty { get; set; }

    [MaxLength(255)]
    public string? SourceFile { get; set; }

    [Column("usage_cycle")]
    [DefaultValue(1)]
    public int UsageCycle { get; set; } = 1;

    [Column("times_used")]
    [DefaultValue(0)]
    public int TimesUsed { get; set; } = 0;

    [Column("last_used_at")]
    public DateTime? LastUsedAt { get; set; }

    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;

    public ICollection<QuizResponse> Responses { get; } = new List<QuizResponse>();
}
