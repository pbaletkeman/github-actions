use serde::{Deserialize, Serialize};

#[derive(Debug, Clone, Serialize, Deserialize, sqlx::FromRow)]
pub struct QuizResponse {
    pub id: i64,
    pub session_id: String,
    pub question_id: i64,
    pub user_answer: String,
    pub is_correct: i64,
    pub time_taken_seconds: Option<i64>,
}

impl QuizResponse {
    pub fn correct(&self) -> bool {
        self.is_correct != 0
    }
}
