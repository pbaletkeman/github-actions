use clap::Args;
use sqlx::{Pool, Sqlite};

use crate::db::repositories::{QuestionRepo, ResponseRepo, SessionRepo};
use crate::error::{QuizError, Result};

#[derive(Args, Debug)]
pub struct ClearArgs {
    /// Clear all questions
    #[arg(long)]
    pub questions: bool,

    /// Clear all session history (and responses)
    #[arg(long)]
    pub history: bool,

    /// Clear everything (questions + history)
    #[arg(long)]
    pub all: bool,

    /// Required confirmation flag
    #[arg(long)]
    pub confirm: bool,
}

pub async fn run_clear(pool: Pool<Sqlite>, args: ClearArgs) -> Result<()> {
    let do_questions = args.questions || args.all;
    let do_history = args.history || args.all;

    if !do_questions && !do_history {
        eprintln!("No action specified. Use --questions, --history, or --all.");
        return Ok(());
    }

    if !args.confirm {
        return Err(QuizError::ConfirmationRequired);
    }

    if do_history {
        let deleted_responses = ResponseRepo::delete_all(&pool).await?;
        let deleted_sessions = SessionRepo::delete_all(&pool).await?;
        println!(
            "Cleared {deleted_sessions} session(s) and {deleted_responses} response(s)."
        );
    }

    if do_questions {
        let deleted = QuestionRepo::delete_all(&pool).await?;
        println!("Cleared {deleted} question(s).");
    }

    Ok(())
}
