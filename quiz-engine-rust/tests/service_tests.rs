use quiz_engine::db::{create_test_pool, QuestionRepo, ResponseRepo, SessionRepo};
use quiz_engine::models::NewQuestion;
use quiz_engine::service::quiz_engine::QuizEngine;
use quiz_engine::service::history_service::HistoryService;
use quiz_engine::service::quiz_service::QuizService;
use quiz_engine::service::quiz_utils::{calculate_percentage, format_duration, grade_from_percentage};

fn make_question(n: usize) -> NewQuestion {
    NewQuestion {
        question_text: format!("Service question {n}?"),
        option_a: "Alpha".to_string(),
        option_b: "Beta".to_string(),
        option_c: "Gamma".to_string(),
        option_d: "Delta".to_string(),
        correct_answer: "A".to_string(),
        ..Default::default()
    }
}

async fn seed_questions(pool: &sqlx::Pool<sqlx::Sqlite>, count: usize) {
    for i in 0..count {
        QuestionRepo::insert(pool, &make_question(i))
            .await
            .expect("insert");
    }
}

// ──────────────────────────── QuizEngine tests ────────────────────────────

#[tokio::test]
async fn test_quiz_engine_new_returns_correct_count() {
    let pool = create_test_pool().await;
    seed_questions(&pool, 5).await;

    let engine = QuizEngine::new(pool.clone(), 3).await.expect("new engine");
    assert_eq!(engine.question_count(), 3);
}

#[tokio::test]
async fn test_quiz_engine_fails_with_no_questions() {
    let pool = create_test_pool().await;
    let result = QuizEngine::new(pool, 5).await;
    assert!(result.is_err());
}

#[tokio::test]
async fn test_quiz_engine_fails_when_not_enough_questions() {
    let pool = create_test_pool().await;
    seed_questions(&pool, 2).await;
    let result = QuizEngine::new(pool, 5).await;
    assert!(result.is_err());
}

#[tokio::test]
async fn test_submit_answer_correct_increases_score() {
    let pool = create_test_pool().await;
    seed_questions(&pool, 5).await;

    let mut engine = QuizEngine::new(pool.clone(), 1).await.expect("engine");
    let correct_idx = engine.questions()[0].correct_shuffled_index;
    let is_correct = engine.submit_answer(0, correct_idx, Some(5)).await.expect("submit");
    assert!(is_correct);
    assert_eq!(engine.num_correct(), 1);
}

#[tokio::test]
async fn test_submit_answer_wrong_does_not_increase_score() {
    let pool = create_test_pool().await;
    seed_questions(&pool, 5).await;

    let mut engine = QuizEngine::new(pool.clone(), 1).await.expect("engine");
    let correct_idx = engine.questions()[0].correct_shuffled_index;
    // Choose the wrong answer deliberately
    let wrong_idx = (correct_idx + 1) % engine.questions()[0].options.len();
    let is_correct = engine.submit_answer(0, wrong_idx, None).await.expect("submit");
    assert!(!is_correct);
    assert_eq!(engine.num_correct(), 0);
}

#[tokio::test]
async fn test_finalize_persists_session() {
    let pool = create_test_pool().await;
    seed_questions(&pool, 5).await;

    let mut engine = QuizEngine::new(pool.clone(), 2).await.expect("engine");
    let idx0 = engine.questions()[0].correct_shuffled_index;
    engine.submit_answer(0, idx0, None).await.expect("submit 0");
    engine.submit_answer(1, 0, None).await.expect("submit 1");

    let session = engine.finalize(Some(30)).await.expect("finalize");
    assert!(!session.session_id.is_empty());
    assert_eq!(session.num_questions, 2);
    assert!(session.ended_at.is_some());
}

#[tokio::test]
async fn test_finalize_cannot_be_called_twice() {
    let pool = create_test_pool().await;
    seed_questions(&pool, 5).await;

    let mut engine = QuizEngine::new(pool.clone(), 1).await.expect("engine");
    engine.finalize(None).await.expect("first finalize");
    let result = engine.finalize(None).await;
    assert!(result.is_err());
}

#[tokio::test]
async fn test_submit_after_finalize_returns_error() {
    let pool = create_test_pool().await;
    seed_questions(&pool, 5).await;

    let mut engine = QuizEngine::new(pool.clone(), 1).await.expect("engine");
    engine.finalize(None).await.expect("finalize");
    let result = engine.submit_answer(0, 0, None).await;
    assert!(result.is_err());
}

#[tokio::test]
async fn test_submit_answer_invalid_index_returns_error() {
    let pool = create_test_pool().await;
    seed_questions(&pool, 5).await;

    let mut engine = QuizEngine::new(pool.clone(), 1).await.expect("engine");
    let result = engine.submit_answer(99, 0, None).await;
    assert!(result.is_err());
}

// ──────────────────────────── HistoryService tests ────────────────────────

#[tokio::test]
async fn test_history_list_empty() {
    let pool = create_test_pool().await;
    let sessions = HistoryService::list_sessions(&pool).await.expect("list");
    assert!(sessions.is_empty());
}

#[tokio::test]
async fn test_history_list_after_session() {
    let pool = create_test_pool().await;
    seed_questions(&pool, 5).await;

    let mut engine = QuizEngine::new(pool.clone(), 1).await.expect("engine");
    engine.finalize(None).await.expect("finalize");

    let sessions = HistoryService::list_sessions(&pool).await.expect("list");
    assert_eq!(sessions.len(), 1);
}

#[tokio::test]
async fn test_history_summary_statistics() {
    let pool = create_test_pool().await;
    seed_questions(&pool, 10).await;

    // Two sessions
    let mut e1 = QuizEngine::new(pool.clone(), 2).await.expect("e1");
    let idx = e1.questions()[0].correct_shuffled_index;
    e1.submit_answer(0, idx, None).await.expect("s1");
    e1.submit_answer(1, idx, None).await.expect("s1b");
    e1.finalize(None).await.expect("f1");

    let mut e2 = QuizEngine::new(pool.clone(), 2).await.expect("e2");
    e2.submit_answer(0, 0, None).await.expect("s2");
    e2.submit_answer(1, 0, None).await.expect("s2b");
    e2.finalize(None).await.expect("f2");

    let summary = HistoryService::summary(&pool).await.expect("summary");
    assert_eq!(summary.session_count, 2);
    assert!(summary.average_score >= 0.0);
    assert!(summary.best_score >= 0.0);
}

#[tokio::test]
async fn test_history_get_session_by_id() {
    let pool = create_test_pool().await;
    seed_questions(&pool, 5).await;

    let mut engine = QuizEngine::new(pool.clone(), 1).await.expect("engine");
    let session_id = engine.session_id.clone();
    engine.finalize(None).await.expect("finalize");

    let session = HistoryService::get_session(&pool, &session_id)
        .await
        .expect("get_session");
    assert_eq!(session.session_id, session_id);
}

#[tokio::test]
async fn test_history_get_session_not_found() {
    let pool = create_test_pool().await;
    let result = HistoryService::get_session(&pool, "00000000-0000-0000-0000-000000000000").await;
    assert!(result.is_err());
}

#[tokio::test]
async fn test_history_get_responses() {
    let pool = create_test_pool().await;
    seed_questions(&pool, 5).await;

    let mut engine = QuizEngine::new(pool.clone(), 2).await.expect("engine");
    let session_id = engine.session_id.clone();
    engine.submit_answer(0, 0, None).await.expect("s0");
    engine.submit_answer(1, 0, None).await.expect("s1");
    engine.finalize(None).await.expect("finalize");

    let responses = HistoryService::get_responses(&pool, &session_id)
        .await
        .expect("responses");
    assert_eq!(responses.len(), 2);
}

// ──────────────────────────── QuizService tests ────────────────────────────

#[tokio::test]
async fn test_quiz_service_question_count() {
    let pool = create_test_pool().await;
    seed_questions(&pool, 7).await;

    let count = QuizService::question_count(&pool).await.expect("count");
    assert_eq!(count, 7);
}

#[tokio::test]
async fn test_quiz_service_current_cycle() {
    let pool = create_test_pool().await;
    seed_questions(&pool, 3).await;

    let cycle = QuizService::current_cycle(&pool).await.expect("cycle");
    assert_eq!(cycle, 1);
}

#[tokio::test]
async fn test_quiz_service_mark_question_used() {
    let pool = create_test_pool().await;
    let id = QuestionRepo::insert(&pool, &make_question(0))
        .await
        .expect("insert");

    QuizService::mark_question_used(&pool, id)
        .await
        .expect("mark_used");

    let q = QuestionRepo::get_by_id(&pool, id).await.expect("get_by_id");
    assert_eq!(q.times_used, 1);
}

// ──────────────────────────── Session/Response repos ────────────────────────

#[tokio::test]
async fn test_session_create_and_retrieve() {
    let pool = create_test_pool().await;
    let id = "12345678-1234-1234-1234-123456789012";
    SessionRepo::create(&pool, id, 10).await.expect("create");

    let session = SessionRepo::get_by_id(&pool, id).await.expect("get_by_id");
    assert_eq!(session.session_id, id);
    assert_eq!(session.num_questions, 10);
    assert!(session.ended_at.is_none());
}

#[tokio::test]
async fn test_session_finalize_updates_stats() {
    let pool = create_test_pool().await;
    let id = "12345678-1234-1234-1234-123456789012";
    SessionRepo::create(&pool, id, 10).await.expect("create");

    let session = SessionRepo::finalize(&pool, id, 8, 80.0, Some(120))
        .await
        .expect("finalize");
    assert_eq!(session.num_correct, 8);
    assert!((session.percentage_correct - 80.0).abs() < 0.1);
    assert!(session.ended_at.is_some());
}

#[tokio::test]
async fn test_response_record_and_fetch() {
    let pool = create_test_pool().await;
    let session_id = "12345678-1234-1234-1234-123456789012";
    let q_id = QuestionRepo::insert(&pool, &make_question(0))
        .await
        .expect("insert");
    SessionRepo::create(&pool, session_id, 1).await.expect("create");

    ResponseRepo::record(&pool, session_id, q_id, "A", true, Some(5))
        .await
        .expect("record");

    let responses = ResponseRepo::get_by_session(&pool, session_id)
        .await
        .expect("get_by_session");
    assert_eq!(responses.len(), 1);
    assert_eq!(responses[0].user_answer, "A");
    assert!(responses[0].correct());
}

#[tokio::test]
async fn test_response_delete_all() {
    let pool = create_test_pool().await;
    let session_id = "12345678-1234-1234-1234-123456789012";
    let q_id = QuestionRepo::insert(&pool, &make_question(0))
        .await
        .expect("insert");
    SessionRepo::create(&pool, session_id, 1).await.expect("create");
    ResponseRepo::record(&pool, session_id, q_id, "A", true, None)
        .await
        .expect("record");

    let deleted = ResponseRepo::delete_all(&pool).await.expect("delete_all");
    assert_eq!(deleted, 1);
}

// ──────────────────────────── Utils tests ────────────────────────────────

#[test]
fn test_percentage_calculation() {
    assert!((calculate_percentage(9, 10) - 90.0).abs() < f64::EPSILON);
}

#[test]
fn test_grade_passing() {
    assert_eq!(grade_from_percentage(92.0), "A");
    assert_eq!(grade_from_percentage(75.0), "C");
}

#[test]
fn test_format_duration_helper() {
    assert_eq!(format_duration(3661), "1h 1m 1s");
}
