use sqlx::{Pool, Sqlite};
use uuid::Uuid;

use crate::db::repositories::{QuestionRepo, ResponseRepo, SessionRepo};
use crate::error::{QuizError, Result};
use crate::models::{Question, QuizQuestion, QuizSession};
use crate::service::answer_shuffler::shuffle_answers;
use crate::service::quiz_service::QuizService;

/// A running quiz session.
pub struct QuizEngine {
    pool: Pool<Sqlite>,
    pub session_id: String,
    questions: Vec<QuizQuestion>,
    num_correct: usize,
    finalized: bool,
}

impl QuizEngine {
    /// Create a new quiz session with `count` random questions.
    pub async fn new(pool: Pool<Sqlite>, count: usize) -> Result<Self> {
        let total = QuizService::question_count(&pool).await?;
        if total == 0 {
            return Err(QuizError::NoQuestionsFound);
        }
        let available = total as usize;
        if count > available {
            return Err(QuizError::NotEnoughQuestions {
                requested: count,
                available,
            });
        }

        let raw_questions = QuizService::get_random_questions(&pool, count).await?;
        let session_id = Uuid::new_v4().to_string();

        SessionRepo::create(&pool, &session_id, count as i64).await?;

        let questions: Vec<QuizQuestion> = raw_questions
            .iter()
            .map(|q| build_quiz_question(q))
            .collect();

        Ok(QuizEngine {
            pool,
            session_id,
            questions,
            num_correct: 0,
            finalized: false,
        })
    }

    /// Return the list of questions (without correct answers).
    pub fn questions(&self) -> &[QuizQuestion] {
        &self.questions
    }

    /// Return number of questions.
    pub fn question_count(&self) -> usize {
        self.questions.len()
    }

    /// Return current correct count.
    pub fn num_correct(&self) -> usize {
        self.num_correct
    }

    /// Submit an answer for the question at the given index.
    ///
    /// `answer_index` is the 0-based index into the shuffled options.
    /// `time_taken_seconds` is optional elapsed time.
    pub async fn submit_answer(
        &mut self,
        question_index: usize,
        answer_index: usize,
        time_taken_seconds: Option<i64>,
    ) -> Result<bool> {
        if self.finalized {
            return Err(QuizError::SessionAlreadyFinalized);
        }
        if question_index >= self.questions.len() {
            return Err(QuizError::InvalidQuestionIndex {
                index: question_index,
                count: self.questions.len(),
            });
        }

        let q = &self.questions[question_index];
        let is_correct = answer_index == q.correct_shuffled_index;
        if is_correct {
            self.num_correct += 1;
        }

        let letter = index_to_answer_letter(answer_index);
        ResponseRepo::record(
            &self.pool,
            &self.session_id,
            q.id,
            letter,
            is_correct,
            time_taken_seconds,
        )
        .await?;

        Ok(is_correct)
    }

    /// Finalize the session, compute stats, and persist.
    pub async fn finalize(
        &mut self,
        time_taken_seconds: Option<i64>,
    ) -> Result<QuizSession> {
        if self.finalized {
            return Err(QuizError::SessionAlreadyFinalized);
        }

        // Mark all questions used
        for q in &self.questions {
            QuestionRepo::mark_used(&self.pool, q.id).await?;
        }
        QuestionRepo::advance_cycle_if_exhausted(&self.pool).await?;

        let num_questions = self.questions.len();
        let pct = if num_questions > 0 {
            (self.num_correct as f64 / num_questions as f64) * 100.0
        } else {
            0.0
        };

        let session = SessionRepo::finalize(
            &self.pool,
            &self.session_id,
            self.num_correct as i64,
            pct,
            time_taken_seconds,
        )
        .await?;

        self.finalized = true;
        Ok(session)
    }
}

fn build_quiz_question(q: &Question) -> QuizQuestion {
    let options: Vec<String> = q.options().iter().map(|s| s.to_string()).collect();
    let shuffle = shuffle_answers(&options, &q.correct_answer);
    QuizQuestion {
        id: q.id,
        question_text: q.question_text.clone(),
        options: shuffle.shuffled_options,
        correct_shuffled_index: shuffle.correct_shuffled_index,
        section: q.section.clone(),
        difficulty: q.difficulty.clone(),
        explanation: q.explanation.clone(),
    }
}

fn index_to_answer_letter(index: usize) -> &'static str {
    match index {
        0 => "A",
        1 => "B",
        2 => "C",
        3 => "D",
        4 => "E",
        _ => "A",
    }
}
