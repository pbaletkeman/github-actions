use serde::{Deserialize, Serialize};

#[derive(Debug, Clone, Serialize, Deserialize, sqlx::FromRow)]
pub struct Question {
    pub id: i64,
    pub question_text: String,
    pub option_a: String,
    pub option_b: String,
    pub option_c: String,
    pub option_d: String,
    pub option_e: Option<String>,
    pub correct_answer: String,
    pub explanation: Option<String>,
    pub section: Option<String>,
    pub difficulty: Option<String>,
    pub source_file: Option<String>,
    pub usage_cycle: i64,
    pub times_used: i64,
    pub last_used_at: Option<String>,
    pub created_at: String,
}

/// Used when inserting a new question (no id/timestamps)
#[derive(Debug, Clone, Serialize, Deserialize, Default)]
pub struct NewQuestion {
    pub question_text: String,
    pub option_a: String,
    pub option_b: String,
    pub option_c: String,
    pub option_d: String,
    pub option_e: Option<String>,
    pub correct_answer: String,
    pub explanation: Option<String>,
    pub section: Option<String>,
    pub difficulty: Option<String>,
    pub source_file: Option<String>,
}

/// Question presented to user during quiz (answer hidden)
#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct QuizQuestion {
    pub id: i64,
    pub question_text: String,
    pub options: Vec<String>,
    pub correct_shuffled_index: usize,
    pub section: Option<String>,
    pub difficulty: Option<String>,
    pub explanation: Option<String>,
}

impl Question {
    pub fn options(&self) -> Vec<&str> {
        let mut opts = vec![
            self.option_a.as_str(),
            self.option_b.as_str(),
            self.option_c.as_str(),
            self.option_d.as_str(),
        ];
        if let Some(ref e) = self.option_e {
            opts.push(e.as_str());
        }
        opts
    }

    pub fn correct_index(&self) -> usize {
        match self.correct_answer.to_uppercase().as_str() {
            "A" => 0,
            "B" => 1,
            "C" => 2,
            "D" => 3,
            "E" => 4,
            _ => 0,
        }
    }
}
