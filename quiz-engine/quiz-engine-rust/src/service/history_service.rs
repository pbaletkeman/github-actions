use std::fs::File;
use std::io::Write;
use std::path::Path;

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

    /// Export sessions to a JSON file.
    pub async fn export_json(
        pool: &Pool<Sqlite>,
        sessions: &[QuizSession],
        path: &Path,
    ) -> Result<()> {
        use serde_json::{json, Value};

        let mut records: Vec<Value> = Vec::new();
        for s in sessions {
            let responses = ResponseRepo::get_by_session(pool, &s.session_id).await?;
            let resp_arr: Vec<Value> = responses
                .iter()
                .map(|r| {
                    json!({
                        "question_id": r.question_id,
                        "user_answer": r.user_answer,
                        "is_correct": r.correct()
                    })
                })
                .collect();

            records.push(json!({
                "session_id": s.session_id,
                "started_at": s.started_at,
                "score": s.num_correct,
                "total_questions": s.num_questions,
                "percentage_correct": s.percentage_correct,
                "responses": resp_arr
            }));
        }

        let json_str = serde_json::to_string_pretty(&records)
            .map_err(|e| crate::error::QuizError::Other(e.to_string()))?;

        let mut file = File::create(path)?;
        file.write_all(json_str.as_bytes())?;

        Ok(())
    }

    /// Export sessions to a CSV file.
    pub async fn export_csv(
        pool: &Pool<Sqlite>,
        sessions: &[QuizSession],
        path: &Path,
    ) -> Result<()> {
        let mut file = File::create(path)?;

        writeln!(
            file,
            "session_id,started_at,score,total_questions,percentage_correct,question_id,user_answer,is_correct"
        )?;

        for s in sessions {
            let responses = ResponseRepo::get_by_session(pool, &s.session_id).await?;
            if responses.is_empty() {
                writeln!(
                    file,
                    "{},{},{},{},{:.2},,, ",
                    s.session_id, s.started_at, s.num_correct, s.num_questions, s.percentage_correct
                )?;
            } else {
                for r in &responses {
                    writeln!(
                        file,
                        "{},{},{},{},{:.2},{},{},{}",
                        s.session_id,
                        s.started_at,
                        s.num_correct,
                        s.num_questions,
                        s.percentage_correct,
                        r.question_id,
                        r.user_answer,
                        r.correct()
                    )?;
                }
            }
        }

        Ok(())
    }
}

#[derive(Debug, Default)]
pub struct HistorySummary {
    pub session_count: usize,
    pub average_score: f64,
    pub best_score: f64,
}
