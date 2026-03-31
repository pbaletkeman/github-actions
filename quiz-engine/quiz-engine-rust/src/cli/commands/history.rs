use std::path::Path;

use chrono::Local;
use clap::Args;
use sqlx::{Pool, Sqlite};

use crate::cli::formatter::print_table;
use crate::error::Result;
use crate::service::history_service::HistoryService;
use crate::service::quiz_utils::format_duration;

#[derive(Args, Debug)]
pub struct HistoryArgs {
    /// Show details for a specific session by ID
    #[arg(long)]
    pub session_id: Option<String>,

    /// Show the answer review for the specified session
    #[arg(long)]
    pub review: bool,

    /// Export history to a file: json or csv
    #[arg(long)]
    pub export: Option<String>,
}

pub async fn run_history(pool: Pool<Sqlite>, args: HistoryArgs) -> Result<()> {
    if let Some(ref id) = args.session_id {
        let session = HistoryService::get_session(&pool, id).await?;
        println!("Session: {}", session.session_id);
        println!("Started:    {}", session.started_at);
        println!(
            "Ended:      {}",
            session.ended_at.as_deref().unwrap_or("(in progress)")
        );
        println!(
            "Score:      {}/{} ({:.1}%)",
            session.num_correct, session.num_questions, session.percentage_correct
        );
        println!("Grade:      {}", session.grade());
        if let Some(t) = session.time_taken_seconds {
            println!("Duration:   {}", format_duration(t));
        }

        if args.review {
            println!("\nAnswer Review:");
            let responses = HistoryService::get_responses(&pool, id).await?;
            let headers = &["#", "Question", "Your Answer", "Correct?"];
            let mut rows: Vec<Vec<String>> = Vec::new();

            for (i, resp) in responses.iter().enumerate() {
                let question =
                    HistoryService::get_question_for_response(&pool, resp.question_id).await?;
                let correct_label = if resp.correct() { "✓" } else { "✗" }.to_string();
                let q_short = if question.question_text.len() > 40 {
                    format!("{}…", &question.question_text[..40])
                } else {
                    question.question_text.clone()
                };
                rows.push(vec![
                    (i + 1).to_string(),
                    q_short,
                    resp.user_answer.clone(),
                    correct_label,
                ]);
            }
            print_table(headers, &rows);
        }
    } else {
        let sessions = HistoryService::list_sessions(&pool).await?;
        if sessions.is_empty() {
            println!("No quiz sessions found. Take a quiz first with 'quiz --questions N'.");
            return Ok(());
        }

        if let Some(ref fmt) = args.export {
            let timestamp = Local::now().format("%Y%m%d%H%M%S");
            match fmt.as_str() {
                "json" => {
                    let path_str = format!("quiz-history-{}.json", timestamp);
                    let path = Path::new(&path_str);
                    HistoryService::export_json(&pool, &sessions, path).await?;
                    println!("Exported {} sessions to {}", sessions.len(), path_str);
                }
                "csv" => {
                    let path_str = format!("quiz-history-{}.csv", timestamp);
                    let path = Path::new(&path_str);
                    HistoryService::export_csv(&pool, &sessions, path).await?;
                    println!("Exported {} sessions to {}", sessions.len(), path_str);
                }
                other => {
                    eprintln!("Unknown export format '{}': use 'json' or 'csv'.", other);
                    return Err(crate::error::QuizError::Other(format!(
                        "Unknown export format: {}",
                        other
                    )));
                }
            }
            return Ok(());
        }

        let summary = HistoryService::summary(&pool).await?;
        println!(
            "Total sessions: {} | Average score: {:.1}% | Best score: {:.1}%",
            summary.session_count, summary.average_score, summary.best_score
        );
        println!();

        let headers = &[
            "Session ID",
            "Date",
            "Questions",
            "Correct",
            "Score %",
            "Grade",
        ];
        let rows: Vec<Vec<String>> = sessions
            .iter()
            .map(|s| {
                vec![
                    s.session_id.clone(),
                    s.started_at[..10].to_string(),
                    s.num_questions.to_string(),
                    s.num_correct.to_string(),
                    format!("{:.1}", s.percentage_correct),
                    s.grade().to_string(),
                ]
            })
            .collect();
        print_table(headers, &rows);
    }

    Ok(())
}
