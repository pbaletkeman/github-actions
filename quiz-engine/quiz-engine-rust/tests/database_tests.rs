use quiz_engine::db::{create_test_pool, QuestionRepo};
use quiz_engine::models::NewQuestion;

fn sample_question(text: &str) -> NewQuestion {
    NewQuestion {
        question_text: text.to_string(),
        option_a: "Option A".to_string(),
        option_b: "Option B".to_string(),
        option_c: "Option C".to_string(),
        option_d: "Option D".to_string(),
        correct_answer: "A".to_string(),
        ..Default::default()
    }
}

#[tokio::test]
async fn test_insert_and_retrieve_question() {
    let pool = create_test_pool().await;
    let q = sample_question("What is CI?");
    let id = QuestionRepo::insert(&pool, &q).await.expect("insert failed");
    assert!(id > 0);

    let all = QuestionRepo::get_all(&pool).await.expect("get_all failed");
    assert_eq!(all.len(), 1);
    assert_eq!(all[0].question_text, "What is CI?");
}

#[tokio::test]
async fn test_insert_if_not_exists_skips_duplicate() {
    let pool = create_test_pool().await;
    let q = sample_question("Unique question text");

    let first = QuestionRepo::insert_if_not_exists(&pool, &q)
        .await
        .expect("insert failed");
    assert!(first.is_some());

    let second = QuestionRepo::insert_if_not_exists(&pool, &q)
        .await
        .expect("insert failed");
    assert!(second.is_none());

    let count = QuestionRepo::count(&pool).await.expect("count failed");
    assert_eq!(count, 1);
}

#[tokio::test]
async fn test_get_random_for_quiz_returns_correct_count() {
    let pool = create_test_pool().await;
    for i in 0..5 {
        let q = sample_question(&format!("Question {i}"));
        QuestionRepo::insert(&pool, &q).await.expect("insert");
    }
    let results = QuestionRepo::get_random_for_quiz(&pool, 3)
        .await
        .expect("get_random");
    assert_eq!(results.len(), 3);
}

#[tokio::test]
async fn test_mark_used_increments_times_used() {
    let pool = create_test_pool().await;
    let id = QuestionRepo::insert(&pool, &sample_question("Mark used?"))
        .await
        .expect("insert");

    QuestionRepo::mark_used(&pool, id).await.expect("mark_used");

    let q = QuestionRepo::get_by_id(&pool, id).await.expect("get_by_id");
    assert_eq!(q.times_used, 1);
    assert!(q.last_used_at.is_some());
}

#[tokio::test]
async fn test_mark_used_advances_usage_cycle() {
    let pool = create_test_pool().await;

    // Insert one question (cycle=1)
    let id = QuestionRepo::insert(&pool, &sample_question("Cycle test?"))
        .await
        .expect("insert");

    let cycle_before = QuestionRepo::get_current_cycle(&pool)
        .await
        .expect("get_cycle");
    assert_eq!(cycle_before, 1);

    // Mark it used — usage_cycle should increment to 2
    QuestionRepo::mark_used(&pool, id).await.expect("mark_used");

    // MIN(usage_cycle) should now be 2
    let cycle_after = QuestionRepo::get_current_cycle(&pool)
        .await
        .expect("get_cycle");
    assert_eq!(cycle_after, 2);

    // advance_cycle_if_exhausted when questions remain at current cycle returns false
    let was_exhausted = QuestionRepo::advance_cycle_if_exhausted(&pool)
        .await
        .expect("advance");
    assert!(!was_exhausted); // There IS a question at cycle 2
}

#[tokio::test]
async fn test_get_current_cycle_returns_one_initially() {
    let pool = create_test_pool().await;
    QuestionRepo::insert(&pool, &sample_question("Cycle question"))
        .await
        .expect("insert");
    let cycle = QuestionRepo::get_current_cycle(&pool)
        .await
        .expect("get_cycle");
    assert_eq!(cycle, 1);
}

#[tokio::test]
async fn test_count_returns_correct_number() {
    let pool = create_test_pool().await;
    let count_empty = QuestionRepo::count(&pool).await.expect("count");
    assert_eq!(count_empty, 0);

    QuestionRepo::insert(&pool, &sample_question("Q1"))
        .await
        .expect("insert");
    QuestionRepo::insert(&pool, &sample_question("Q2"))
        .await
        .expect("insert");

    let count = QuestionRepo::count(&pool).await.expect("count");
    assert_eq!(count, 2);
}

#[tokio::test]
async fn test_get_by_id_returns_correct_question() {
    let pool = create_test_pool().await;
    let id = QuestionRepo::insert(&pool, &sample_question("Find me"))
        .await
        .expect("insert");

    let q = QuestionRepo::get_by_id(&pool, id).await.expect("get_by_id");
    assert_eq!(q.question_text, "Find me");
    assert_eq!(q.id, id);
}

#[tokio::test]
async fn test_get_by_id_error_on_missing() {
    let pool = create_test_pool().await;
    let result = QuestionRepo::get_by_id(&pool, 9999).await;
    assert!(result.is_err());
}

#[tokio::test]
async fn test_delete_all_removes_questions() {
    let pool = create_test_pool().await;
    QuestionRepo::insert(&pool, &sample_question("Delete me"))
        .await
        .expect("insert");
    let deleted = QuestionRepo::delete_all(&pool).await.expect("delete_all");
    assert_eq!(deleted, 1);
    let count = QuestionRepo::count(&pool).await.expect("count");
    assert_eq!(count, 0);
}
