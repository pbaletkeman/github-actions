use sqlx::{Pool, Sqlite};

use crate::db::repositories::QuestionRepo;
use crate::error::Result;
use crate::models::Question;

pub struct QuizService;

impl QuizService {
    /// Get N random questions from the current quiz cycle.
    pub async fn get_random_questions(
        pool: &Pool<Sqlite>,
        count: usize,
    ) -> Result<Vec<Question>> {
        QuestionRepo::get_random_for_quiz(pool, count as i64).await
    }

    /// Mark a question as used and advance the cycle if all questions have been used.
    pub async fn mark_question_used(pool: &Pool<Sqlite>, question_id: i64) -> Result<()> {
        QuestionRepo::mark_used(pool, question_id).await?;
        QuestionRepo::advance_cycle_if_exhausted(pool).await?;
        Ok(())
    }

    /// Return the total number of questions in the database.
    pub async fn question_count(pool: &Pool<Sqlite>) -> Result<i64> {
        QuestionRepo::count(pool).await
    }

    /// Return the current usage cycle number.
    pub async fn current_cycle(pool: &Pool<Sqlite>) -> Result<i64> {
        QuestionRepo::get_current_cycle(pool).await
    }
}
