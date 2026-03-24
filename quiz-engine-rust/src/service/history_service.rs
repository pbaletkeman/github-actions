use sqlx::{Pool, Sqlite};

use crate::db::repositories::{QuestionRepo, ResponseRepo, SessionRepo};
use crate::error::Result;
use crate::models::{Question, QuizResponse, QuizSession};

pub struct HistoryService;

impl HistoryService {
    /// List all quiz sessions.
    pub async fn list_sessions(pool: &Pool<Sqlite>) -> Result<Vec<QuizSession>> {
        SessionRepo::list_all(pool).await
    }

    /// Get a specific session by ID.
    pub async fn get_session(pool: &Pool<Sqlite>, session_id: &str) -> Result<QuizSession> {
        SessionRepo::get_by_id(pool, session_id).await
    }

    /// Get responses for a specific session.
    pub async fn get_responses(
        pool: &Pool<Sqlite>,
        session_id: &str,
    ) -> Result<Vec<QuizResponse>> {
        ResponseRepo::get_by_session(pool, session_id).await
    }

    /// Get the question associated with a specific response (for review).
    pub async fn get_question_for_response(
        pool: &Pool<Sqlite>,
        question_id: i64,
    ) -> Result<Question> {
        QuestionRepo::get_by_id(pool, question_id).await
    }

    /// Summarize all sessions: count, average score, best score.
    pub async fn summary(pool: &Pool<Sqlite>) -> Result<HistorySummary> {
        let sessions = SessionRepo::list_all(pool).await?;
        if sessions.is_empty() {
            return Ok(HistorySummary::default());
        }
        let count = sessions.len();
        let avg_pct = sessions.iter().map(|s| s.percentage_correct).sum::<f64>() / count as f64;
        let best = sessions
            .iter()
            .map(|s| s.percentage_correct)
            .fold(f64::NEG_INFINITY, f64::max);
        Ok(HistorySummary {
            session_count: count,
            average_score: avg_pct,
            best_score: best,
        })
    }
}

#[derive(Debug, Default)]
pub struct HistorySummary {
    pub session_count: usize,
    pub average_score: f64,
    pub best_score: f64,
}
