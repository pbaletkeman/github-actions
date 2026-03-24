use sqlx::{Pool, Sqlite};

use crate::error::{QuizError, Result};
use crate::models::QuizResponse;

pub struct ResponseRepo;

impl ResponseRepo {
    /// Record a user's answer.
    pub async fn record(
        pool: &Pool<Sqlite>,
        session_id: &str,
        question_id: i64,
        user_answer: &str,
        is_correct: bool,
        time_taken_seconds: Option<i64>,
    ) -> Result<i64> {
        let is_correct_int: i64 = if is_correct { 1 } else { 0 };
        let row: (i64,) = sqlx::query_as(
            r#"
            INSERT INTO quiz_responses
                (session_id, question_id, user_answer, is_correct, time_taken_seconds)
            VALUES (?, ?, ?, ?, ?)
            RETURNING id
            "#,
        )
        .bind(session_id)
        .bind(question_id)
        .bind(user_answer)
        .bind(is_correct_int)
        .bind(time_taken_seconds)
        .fetch_one(pool)
        .await
        .map_err(QuizError::Database)?;
        Ok(row.0)
    }

    /// Fetch all responses for a given session.
    pub async fn get_by_session(
        pool: &Pool<Sqlite>,
        session_id: &str,
    ) -> Result<Vec<QuizResponse>> {
        let responses = sqlx::query_as::<_, QuizResponse>(
            "SELECT id, session_id, question_id, user_answer, is_correct, time_taken_seconds
             FROM quiz_responses WHERE session_id = ? ORDER BY id",
        )
        .bind(session_id)
        .fetch_all(pool)
        .await
        .map_err(QuizError::Database)?;
        Ok(responses)
    }

    /// Delete all responses for a given session.
    pub async fn delete_by_session(pool: &Pool<Sqlite>, session_id: &str) -> Result<u64> {
        let result = sqlx::query("DELETE FROM quiz_responses WHERE session_id = ?")
            .bind(session_id)
            .execute(pool)
            .await
            .map_err(QuizError::Database)?;
        Ok(result.rows_affected())
    }

    /// Delete all responses.
    pub async fn delete_all(pool: &Pool<Sqlite>) -> Result<u64> {
        let result = sqlx::query("DELETE FROM quiz_responses")
            .execute(pool)
            .await
            .map_err(QuizError::Database)?;
        Ok(result.rows_affected())
    }
}
