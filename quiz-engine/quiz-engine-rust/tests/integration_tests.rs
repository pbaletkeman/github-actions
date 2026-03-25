use std::io::Write;

use quiz_engine::db::{create_test_pool, QuestionRepo};
use quiz_engine::models::NewQuestion;
use quiz_engine::service::{parse_markdown_content, ImportService};
use quiz_engine::service::quiz_engine::QuizEngine;
use quiz_engine::service::history_service::HistoryService;

fn make_question(n: usize) -> NewQuestion {
    NewQuestion {
        question_text: format!("Integration question {n}?"),
        option_a: "Alpha".to_string(),
        option_b: "Beta".to_string(),
        option_c: "Gamma".to_string(),
        option_d: "Delta".to_string(),
        correct_answer: "A".to_string(),
        ..Default::default()
    }
}

/// Helper: seed N questions into a pool.
async fn seed(pool: &sqlx::Pool<sqlx::Sqlite>, count: usize) {
    for i in 0..count {
        QuestionRepo::insert(pool, &make_question(i))
            .await
            .expect("seed insert");
    }
}

// ─────────────────── Full workflow: Import → Quiz → History ────────────────

#[tokio::test]
async fn test_full_workflow_import_quiz_history() {
    let pool = create_test_pool().await;

    // Import via inline markdown
    let content = "## Q1\n> What is CI?\n- A) Continuous Integration\n- B) Code Import\n- C) Compile\n- D) Configure\n**Answer: A**\n\
                   ## Q2\n> What is CD?\n- A) Continuous Delivery\n- B) Code Deploy\n- C) Compile\n- D) Configure\n**Answer: A**\n";
    let questions = parse_markdown_content(content, "test.md").expect("parse");
    for q in &questions {
        QuestionRepo::insert(&pool, q)
            .await
            .expect("insert");
    }

    assert_eq!(
        QuestionRepo::count(&pool).await.expect("count"),
        2
    );

    // Quiz
    let mut engine = QuizEngine::new(pool.clone(), 2).await.expect("engine");
    let session_id = engine.session_id.clone();
    let idx0 = engine.questions()[0].correct_shuffled_index;
    let idx1 = engine.questions()[1].correct_shuffled_index;

    let r0 = engine.submit_answer(0, idx0, Some(5)).await.expect("s0");
    let r1 = engine.submit_answer(1, idx1, Some(3)).await.expect("s1");
    assert!(r0);
    assert!(r1);

    let session = engine.finalize(Some(8)).await.expect("finalize");
    assert_eq!(session.num_correct, 2);
    assert!((session.percentage_correct - 100.0).abs() < 0.01);
    assert!(session.passed());

    // History
    let loaded = HistoryService::get_session(&pool, &session_id)
        .await
        .expect("history");
    assert_eq!(loaded.session_id, session_id);
    assert!(loaded.is_finalized());

    let responses = HistoryService::get_responses(&pool, &session_id)
        .await
        .expect("responses");
    assert_eq!(responses.len(), 2);
    assert!(responses.iter().all(|r| r.correct()));
}

#[tokio::test]
async fn test_import_from_markdown_file() {
    let pool = create_test_pool().await;

    let mut file = tempfile::NamedTempFile::new().expect("tempfile");
    writeln!(
        file,
        "## Q1\n> What is CI?\n- A) Continuous Integration\n- B) Code\n- C) Compile\n- D) Configure\n**Answer: A**"
    )
    .expect("write");

    let (imported, skipped) = ImportService::import_from_file(&pool, file.path())
        .await
        .expect("import");
    assert_eq!(imported, 1);
    assert_eq!(skipped, 0);

    // Import same file again → should skip
    let (imported2, skipped2) = ImportService::import_from_file(&pool, file.path())
        .await
        .expect("import again");
    assert_eq!(imported2, 0);
    assert_eq!(skipped2, 1);
}

#[tokio::test]
async fn test_cycle_advances_after_all_questions_used() {
    let pool = create_test_pool().await;
    seed(&pool, 3).await;

    // Play a quiz with all 3 questions
    let mut engine = QuizEngine::new(pool.clone(), 3).await.expect("engine");
    for i in 0..3 {
        engine.submit_answer(i, 0, None).await.expect("submit");
    }
    engine.finalize(None).await.expect("finalize");

    // After finalization, all questions should have been marked used.
    // The cycle should have advanced (all questions usage_cycle moved).
    // The new current cycle should still return a consistent value.
    let cycle = QuestionRepo::get_current_cycle(&pool).await.expect("cycle");
    assert!(cycle >= 1);
}

#[tokio::test]
async fn test_multiple_sessions_history_summary() {
    let pool = create_test_pool().await;
    seed(&pool, 10).await;

    for _ in 0..3 {
        let mut engine = QuizEngine::new(pool.clone(), 2).await.expect("engine");
        engine.submit_answer(0, 0, None).await.expect("s0");
        engine.submit_answer(1, 0, None).await.expect("s1");
        engine.finalize(None).await.expect("finalize");
    }

    let summary = HistoryService::summary(&pool).await.expect("summary");
    assert_eq!(summary.session_count, 3);
    assert!(summary.best_score >= 0.0);
}

#[tokio::test]
async fn test_quiz_session_grade_and_pass_fail() {
    let pool = create_test_pool().await;
    seed(&pool, 10).await;

    let mut engine = QuizEngine::new(pool.clone(), 10).await.expect("engine");

    // Answer all correctly
    for i in 0..10 {
        let idx = engine.questions()[i].correct_shuffled_index;
        engine.submit_answer(i, idx, None).await.expect("submit");
    }
    let session = engine.finalize(None).await.expect("finalize");
    assert!(session.passed());
    assert_eq!(session.grade(), "A");
    assert!((session.percentage_correct - 100.0).abs() < 0.01);
}
