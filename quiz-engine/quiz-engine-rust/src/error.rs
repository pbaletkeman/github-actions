use thiserror::Error;

#[derive(Debug, Error)]
pub enum QuizError {
    #[error("Database error: {0}")]
    Database(#[from] sqlx::Error),

    #[error("No questions found in the database. Use 'import' to load questions.")]
    NoQuestionsFound,

    #[error("Not enough questions available: requested {requested}, available {available}")]
    NotEnoughQuestions { requested: usize, available: usize },

    #[error("Session not found: {session_id}")]
    SessionNotFound { session_id: String },

    #[error("Question not found: id={question_id}")]
    QuestionNotFound { question_id: i64 },

    #[error("Invalid answer: '{answer}'. Must be A, B, C, D, or E.")]
    InvalidAnswer { answer: String },

    #[error("Invalid question index: {index} (session has {count} questions)")]
    InvalidQuestionIndex { index: usize, count: usize },

    #[error("Parse error in file '{file}': {message}")]
    ParseError { file: String, message: String },

    #[error("IO error: {0}")]
    Io(#[from] std::io::Error),

    #[error("Quiz session already finalized")]
    SessionAlreadyFinalized,

    #[error("Confirmation required. Use --confirm flag to proceed.")]
    ConfirmationRequired,

    #[error("{0}")]
    Other(String),
}

pub type Result<T> = std::result::Result<T, QuizError>;
