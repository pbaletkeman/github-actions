using Microsoft.EntityFrameworkCore;
using QuizEngine.Entities;

namespace QuizEngine.Data;

public class QuizEngineDbContext : DbContext
{
    public QuizEngineDbContext(DbContextOptions<QuizEngineDbContext> options) : base(options)
    {
    }

    public DbSet<Question> Questions { get; set; } = null!;
    public DbSet<QuizSession> QuizSessions { get; set; } = null!;
    public DbSet<QuizResponse> QuizResponses { get; set; } = null!;

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        base.OnModelCreating(modelBuilder);

        modelBuilder.Entity<Question>(entity =>
        {
            entity.HasIndex(q => q.Section);
            entity.HasIndex(q => q.Difficulty);
            entity.HasIndex(q => q.UsageCycle);
            entity.HasIndex(q => new { q.QuestionText, q.CorrectAnswer }).IsUnique();
        });

        modelBuilder.Entity<QuizSession>(entity =>
        {
            entity.HasMany(s => s.Responses)
                  .WithOne(r => r.Session)
                  .HasForeignKey(r => r.SessionId)
                  .OnDelete(DeleteBehavior.Cascade);
        });

        modelBuilder.Entity<QuizResponse>(entity =>
        {
            entity.HasOne(r => r.Question)
                  .WithMany(q => q.Responses)
                  .HasForeignKey(r => r.QuestionId)
                  .OnDelete(DeleteBehavior.Restrict);
        });
    }
}
