use serde::{Deserialize, Serialize};

#[derive(Debug, Clone, Serialize, Deserialize, sqlx::FromRow)]
pub struct QuizSession {
    pub session_id: String,
    pub started_at: String,
    pub ended_at: Option<String>,
    pub num_questions: i64,
    pub num_correct: i64,
    pub percentage_correct: f64,
    pub time_taken_seconds: Option<i64>,
}

impl QuizSession {
    pub fn is_finalized(&self) -> bool {
        self.ended_at.is_some()
    }

    pub fn grade(&self) -> &str {
        match self.percentage_correct as u32 {
            90..=100 => "A",
            80..=89 => "B",
            70..=79 => "C",
            60..=69 => "D",
            _ => "F",
        }
    }

    pub fn passed(&self) -> bool {
        self.percentage_correct >= 70.0
    }
}
