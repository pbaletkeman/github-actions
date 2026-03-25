use criterion::{criterion_group, criterion_main, Criterion};
use quiz_engine::db::{create_test_pool, QuestionRepo};
use quiz_engine::models::NewQuestion;
use quiz_engine::service::{
    answer_shuffler::shuffle_answers,
    quiz_utils::{calculate_percentage, grade_from_percentage},
};
use tokio::runtime::Runtime;

fn make_question(n: usize) -> NewQuestion {
    NewQuestion {
        question_text: format!("Benchmark question {n}?"),
        option_a: "Alpha".to_string(),
        option_b: "Beta".to_string(),
        option_c: "Gamma".to_string(),
        option_d: "Delta".to_string(),
        correct_answer: "A".to_string(),
        ..Default::default()
    }
}

fn bench_shuffle_answers(c: &mut Criterion) {
    let options: Vec<String> = vec![
        "Alpha".to_string(),
        "Beta".to_string(),
        "Gamma".to_string(),
        "Delta".to_string(),
    ];
    c.bench_function("shuffle_answers", |b| {
        b.iter(|| shuffle_answers(&options, "A"))
    });
}

fn bench_calculate_percentage(c: &mut Criterion) {
    c.bench_function("calculate_percentage", |b| {
        b.iter(|| calculate_percentage(85, 100))
    });
}

fn bench_grade_from_percentage(c: &mut Criterion) {
    c.bench_function("grade_from_percentage", |b| {
        b.iter(|| grade_from_percentage(85.0))
    });
}

fn bench_db_insert_questions(c: &mut Criterion) {
    let rt = Runtime::new().unwrap();
    let pool = rt.block_on(create_test_pool());

    let mut counter = 0usize;
    c.bench_function("db_insert_question", |b| {
        b.iter(|| {
            let q = make_question(counter);
            counter += 1;
            rt.block_on(QuestionRepo::insert(&pool, &q)).expect("insert");
        });
    });
}

fn bench_db_get_random_questions(c: &mut Criterion) {
    let rt = Runtime::new().unwrap();
    let pool = rt.block_on(create_test_pool());

    // Seed 100 questions
    rt.block_on(async {
        for i in 0..100 {
            QuestionRepo::insert(&pool, &make_question(i))
                .await
                .expect("seed");
        }
    });

    c.bench_function("db_get_random_10_questions", |b| {
        b.iter(|| {
            rt.block_on(QuestionRepo::get_random_for_quiz(&pool, 10))
                .expect("get_random");
        });
    });
}

criterion_group!(
    benches,
    bench_shuffle_answers,
    bench_calculate_percentage,
    bench_grade_from_percentage,
    bench_db_insert_questions,
    bench_db_get_random_questions,
);
criterion_main!(benches);
