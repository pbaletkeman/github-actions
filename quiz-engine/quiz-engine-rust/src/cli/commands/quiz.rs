use clap::Args;
use sqlx::{Pool, Sqlite};

use crate::cli::formatter::{print_divider, print_result_box};
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

    for i in 0..engine.question_count() {
        let (question_text, options, correct_shuffled_index, section) = {
            let q = &engine.questions()[i];
            (
                q.question_text.clone(),
                q.options.clone(),
                q.correct_shuffled_index,
                q.section.clone(),
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

        if is_correct {
            println!("✓ Correct!\n");
        } else {
            let correct_letter = index_to_letter(correct_shuffled_index);
            println!("✗ Incorrect. The correct answer was: {correct_letter}) {}\n",
                options[correct_shuffled_index]);
        }
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

    Ok(())
}
