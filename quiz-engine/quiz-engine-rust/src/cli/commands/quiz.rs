use clap::Args;
use sqlx::{Pool, Sqlite};

use crate::cli::formatter::{print_divider, print_result_box, print_review};
use crate::cli::prompts::prompt_choice;
use crate::error::{QuizError, Result};
use crate::service::answer_shuffler::index_to_letter;
use crate::service::quiz_engine::QuizEngine;
use crate::service::quiz_utils::format_duration;

#[derive(Args, Debug)]
pub struct QuizArgs {
    /// Number of questions to answer (default: 10)
    #[arg(short, long, default_value = "10")]
    pub questions: usize,
}

/// Data captured per question for the end-of-quiz review.
struct ReviewItem {
    number: usize,
    question_text: String,
    user_letter: String,
    correct_letter: String,
    correct_answer: String,
    is_correct: bool,
    explanation: Option<String>,
}

pub async fn run_quiz(pool: Pool<Sqlite>, args: QuizArgs) -> Result<()> {
    let count = args.questions;

    println!("Starting quiz with {count} questions...");
    println!("Press Ctrl+C at any time to exit.\n");

    let mut engine = QuizEngine::new(pool, count).await.map_err(|e| match e {
        QuizError::NoQuestionsFound => {
            eprintln!("No questions found. Import questions first using 'import --file <file>'.");
            e
        }
        QuizError::NotEnoughQuestions {
            requested,
            available,
        } => {
            eprintln!(
                "Not enough questions: requested {requested}, only {available} available."
            );
            QuizError::NotEnoughQuestions {
                requested,
                available,
            }
        }
        other => other,
    })?;

    let start = std::time::Instant::now();
    let mut review_items: Vec<ReviewItem> = Vec::new();

    for i in 0..engine.question_count() {
        let (question_text, options, correct_shuffled_index, section, explanation) = {
            let q = &engine.questions()[i];
            (
                q.question_text.clone(),
                q.options.clone(),
                q.correct_shuffled_index,
                q.section.clone(),
                q.explanation.clone(),
            )
        };

        print_divider(60);
        println!("Question {} of {}", i + 1, engine.question_count());
        if let Some(ref sec) = section {
            println!("Section: {sec}");
        }
        println!();

        let chosen = prompt_choice(&question_text, &options);
        let is_correct = engine.submit_answer(i, chosen, None).await?;

        review_items.push(ReviewItem {
            number: i + 1,
            question_text: question_text.clone(),
            user_letter: index_to_letter(chosen).to_string(),
            correct_letter: index_to_letter(correct_shuffled_index).to_string(),
            correct_answer: options[correct_shuffled_index].clone(),
            is_correct,
            explanation,
        });
    }

    let elapsed = start.elapsed().as_secs() as i64;
    let session = engine.finalize(Some(elapsed)).await?;

    print_divider(60);
    let result_lines = vec![
        format!("Score:    {}/{}", session.num_correct, session.num_questions),
        format!("Percent:  {:.1}%", session.percentage_correct),
        format!("Grade:    {}", session.grade()),
        format!("Duration: {}", format_duration(elapsed)),
        format!(
            "Result:   {}",
            if session.passed() { "PASSED ✓" } else { "FAILED ✗" }
        ),
        format!("Session:  {}", session.session_id),
    ];
    print_result_box("Quiz Complete", &result_lines);

    // Show deferred answer review after the score
    let review_rows: Vec<(usize, &str, &str, &str, &str, bool, Option<&str>)> = review_items
        .iter()
        .map(|r| (
            r.number,
            r.question_text.as_str(),
            r.user_letter.as_str(),
            r.correct_letter.as_str(),
            r.correct_answer.as_str(),
            r.is_correct,
            r.explanation.as_deref(),
        ))
        .collect();
    print_review(&review_rows);

    Ok(())
}
