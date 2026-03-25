use sqlx::{Pool, Row, Sqlite};

use crate::error::{QuizError, Result};
use crate::models::{NewQuestion, Question};

pub struct QuestionRepo;

impl QuestionRepo {
    /// Insert a new question, returning the rowid.
    pub async fn insert(pool: &Pool<Sqlite>, q: &NewQuestion) -> Result<i64> {
        let row = sqlx::query(
            r#"
            INSERT INTO questions
                (question_text, option_a, option_b, option_c, option_d,
                 option_e, correct_answer, explanation, section, difficulty, source_file)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id
            "#,
        )
        .bind(&q.question_text)
        .bind(&q.option_a)
        .bind(&q.option_b)
        .bind(&q.option_c)
        .bind(&q.option_d)
        .bind(&q.option_e)
        .bind(&q.correct_answer)
        .bind(&q.explanation)
        .bind(&q.section)
        .bind(&q.difficulty)
        .bind(&q.source_file)
        .fetch_one(pool)
        .await
        .map_err(QuizError::Database)?;
        Ok(row.get::<i64, _>("id"))
    }

    /// Insert only if a question with the same text does not already exist.
    pub async fn insert_if_not_exists(pool: &Pool<Sqlite>, q: &NewQuestion) -> Result<Option<i64>> {
        let existing: Option<i64> = sqlx::query_scalar(
            "SELECT id FROM questions WHERE question_text = ?",
        )
        .bind(&q.question_text)
        .fetch_optional(pool)
        .await
        .map_err(QuizError::Database)?;

        if existing.is_some() {
            return Ok(None);
        }
        let id = Self::insert(pool, q).await?;
        Ok(Some(id))
    }

    /// Get all questions.
    pub async fn get_all(pool: &Pool<Sqlite>) -> Result<Vec<Question>> {
        let questions = sqlx::query_as::<_, Question>(
            "SELECT id, question_text, option_a, option_b, option_c, option_d, option_e,
                    correct_answer, explanation, section, difficulty, source_file,
                    usage_cycle, times_used, last_used_at, created_at
             FROM questions ORDER BY id",
        )
        .fetch_all(pool)
        .await
        .map_err(QuizError::Database)?;
        Ok(questions)
    }

    /// Get the total number of questions.
    pub async fn count(pool: &Pool<Sqlite>) -> Result<i64> {
        let count: i64 = sqlx::query_scalar("SELECT COUNT(*) FROM questions")
            .fetch_one(pool)
            .await
            .map_err(QuizError::Database)?;
        Ok(count)
    }

    /// Return the current usage cycle (minimum cycle among all questions).
    pub async fn get_current_cycle(pool: &Pool<Sqlite>) -> Result<i64> {
        let cycle: Option<i64> =
            sqlx::query_scalar("SELECT MIN(usage_cycle) FROM questions")
                .fetch_optional(pool)
                .await
                .map_err(QuizError::Database)?
                .flatten();
        Ok(cycle.unwrap_or(1))
    }

    /// Select `count` random questions from the current cycle.
    pub async fn get_random_for_quiz(pool: &Pool<Sqlite>, count: i64) -> Result<Vec<Question>> {
        let cycle = Self::get_current_cycle(pool).await?;
        let questions = sqlx::query_as::<_, Question>(
            r#"
            SELECT id, question_text, option_a, option_b, option_c, option_d, option_e,
                   correct_answer, explanation, section, difficulty, source_file,
                   usage_cycle, times_used, last_used_at, created_at
            FROM questions
            WHERE usage_cycle = ?
            ORDER BY RANDOM()
            LIMIT ?
            "#,
        )
        .bind(cycle)
        .bind(count)
        .fetch_all(pool)
        .await
        .map_err(QuizError::Database)?;
        Ok(questions)
    }

    /// Mark a question as used — increments times_used and advances usage_cycle.
    pub async fn mark_used(pool: &Pool<Sqlite>, question_id: i64) -> Result<()> {
        sqlx::query(
            r#"
            UPDATE questions
            SET times_used = times_used + 1,
                last_used_at = datetime('now'),
                usage_cycle = usage_cycle + 1
            WHERE id = ?
            "#,
        )
        .bind(question_id)
        .execute(pool)
        .await
        .map_err(QuizError::Database)?;
        Ok(())
    }

    /// Check if there are any questions remaining in the current cycle.
    /// Returns true if the cycle was advanced (all questions have moved on).
    pub async fn advance_cycle_if_exhausted(pool: &Pool<Sqlite>) -> Result<bool> {
        let cycle = Self::get_current_cycle(pool).await?;
        let remaining: i64 = sqlx::query_scalar(
            "SELECT COUNT(*) FROM questions WHERE usage_cycle = ?",
        )
        .bind(cycle)
        .fetch_one(pool)
        .await
        .map_err(QuizError::Database)?;

        // If there are still questions at the current cycle, nothing to advance
        if remaining > 0 {
            return Ok(false);
        }

        // All questions have moved to the next cycle — this is handled automatically
        // by mark_used incrementing usage_cycle. No manual intervention needed.
        Ok(true)
    }

    /// Get a question by its id.
    pub async fn get_by_id(pool: &Pool<Sqlite>, question_id: i64) -> Result<Question> {
        sqlx::query_as::<_, Question>(
            r#"
            SELECT id, question_text, option_a, option_b, option_c, option_d, option_e,
                   correct_answer, explanation, section, difficulty, source_file,
                   usage_cycle, times_used, last_used_at, created_at
            FROM questions WHERE id = ?
            "#,
        )
        .bind(question_id)
        .fetch_optional(pool)
        .await
        .map_err(QuizError::Database)?
        .ok_or(QuizError::QuestionNotFound { question_id })
    }

    /// Delete all questions.
    pub async fn delete_all(pool: &Pool<Sqlite>) -> Result<u64> {
        let result = sqlx::query("DELETE FROM questions")
            .execute(pool)
            .await
            .map_err(QuizError::Database)?;
        Ok(result.rows_affected())
    }
}
