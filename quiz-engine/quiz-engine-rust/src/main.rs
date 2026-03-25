use clap::{Parser, Subcommand};

use quiz_engine::cli::{
    run_clear, run_history, run_import, run_quiz, ClearArgs, HistoryArgs, ImportArgs, QuizArgs,
};
use quiz_engine::db::{create_pool, default_db_path, ensure_db_dir, run_migrations};
use quiz_engine::error::Result;

#[derive(Parser, Debug)]
#[command(name = "quiz_engine")]
#[command(about = "GH-200 Certification Quiz Engine — built with Rust, sqlx, and clap")]
#[command(version = "0.1.0")]
struct Cli {
    /// SQLite database URL (overrides DATABASE_URL env var)
    #[arg(long, global = true)]
    db: Option<String>,

    #[command(subcommand)]
    command: Commands,
}

#[derive(Subcommand, Debug)]
enum Commands {
    /// Take a quiz
    Quiz(QuizArgs),

    /// Import questions from a markdown file
    Import(ImportArgs),

    /// View quiz history
    History(HistoryArgs),

    /// Clear quiz data
    Clear(ClearArgs),
}

#[tokio::main]
async fn main() -> Result<()> {
    let cli = Cli::parse();

    let db_url = cli.db.clone().unwrap_or_else(default_db_path);

    ensure_db_dir(&db_url)?;

    let pool = create_pool(&db_url).await?;
    run_migrations(&pool).await?;

    match cli.command {
        Commands::Quiz(args) => run_quiz(pool, args).await?,
        Commands::Import(args) => run_import(pool, args).await?,
        Commands::History(args) => run_history(pool, args).await?,
        Commands::Clear(args) => run_clear(pool, args).await?,
    }

    Ok(())
}
