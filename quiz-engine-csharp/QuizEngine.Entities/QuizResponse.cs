using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace QuizEngine.Entities;

[Table("quiz_responses")]
public class QuizResponse
{
    [Key]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public int Id { get; set; }

    [Required]
    [MaxLength(36)]
    public string SessionId { get; set; } = string.Empty;

    [Required]
    public int QuestionId { get; set; }

    [MaxLength(1)]
    public string? UserAnswer { get; set; }

    [DefaultValue(0)]
    public int IsCorrect { get; set; } = 0;

    public int? TimeTakenSeconds { get; set; }

    [ForeignKey(nameof(SessionId))]
    public QuizSession Session { get; set; } = null!;

    [ForeignKey(nameof(QuestionId))]
    public Question Question { get; set; } = null!;
}
