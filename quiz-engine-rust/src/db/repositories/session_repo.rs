use sqlx::{Pool, Sqlite};

use crate::error::{QuizError, Result};
use crate::models::QuizSession;

pub struct SessionRepo;

impl SessionRepo {
    /// Create a new quiz session.
    pub async fn create(pool: &Pool<Sqlite>, session_id: &str, num_questions: i64) -> Result<()> {
        sqlx::query(
            "INSERT INTO quiz_sessions (session_id, num_questions) VALUES (?, ?)",
        )
        .bind(session_id)
        .bind(num_questions)
        .execute(pool)
        .await
        .map_err(QuizError::Database)?;
        Ok(())
    }

    /// Finalize a session with results.
    pub async fn finalize(
        pool: &Pool<Sqlite>,
        session_id: &str,
        num_correct: i64,
        percentage_correct: f64,
        time_taken_seconds: Option<i64>,
    ) -> Result<QuizSession> {
        sqlx::query(
            r#"
            UPDATE quiz_sessions
            SET ended_at = datetime('now'),
                num_correct = ?,
                percentage_correct = ?,
                time_taken_seconds = ?
            WHERE session_id = ?
            "#,
        )
        .bind(num_correct)
        .bind(percentage_correct)
        .bind(time_taken_seconds)
        .bind(session_id)
        .execute(pool)
        .await
        .map_err(QuizError::Database)?;

        Self::get_by_id(pool, session_id).await
    }

    /// Fetch a session by its ID.
    pub async fn get_by_id(pool: &Pool<Sqlite>, session_id: &str) -> Result<QuizSession> {
        sqlx::query_as::<_, QuizSession>(
            r#"
            SELECT session_id, started_at, ended_at,
                   num_questions, num_correct,
                   percentage_correct, time_taken_seconds
            FROM quiz_sessions WHERE session_id = ?
            "#,
        )
        .bind(session_id)
        .fetch_optional(pool)
        .await
        .map_err(QuizError::Database)?
        .ok_or_else(|| QuizError::SessionNotFound {
            session_id: session_id.to_string(),
        })
    }

    /// List all sessions ordered by start time (newest first).
    pub async fn list_all(pool: &Pool<Sqlite>) -> Result<Vec<QuizSession>> {
        let sessions = sqlx::query_as::<_, QuizSession>(
            r#"
            SELECT session_id, started_at, ended_at,
                   num_questions, num_correct,
                   percentage_correct, time_taken_seconds
            FROM quiz_sessions ORDER BY started_at DESC
            "#,
        )
        .fetch_all(pool)
        .await
        .map_err(QuizError::Database)?;
        Ok(sessions)
    }

    /// Delete all sessions.
    pub async fn delete_all(pool: &Pool<Sqlite>) -> Result<u64> {
        let result = sqlx::query("DELETE FROM quiz_sessions")
            .execute(pool)
            .await
            .map_err(QuizError::Database)?;
        Ok(result.rows_affected())
    }
}
