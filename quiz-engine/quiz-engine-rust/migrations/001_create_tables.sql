CREATE TABLE IF NOT EXISTS questions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    question_text TEXT NOT NULL CHECK(length(question_text) <= 2000),
    option_a TEXT NOT NULL CHECK(length(option_a) <= 500),
    option_b TEXT NOT NULL CHECK(length(option_b) <= 500),
    option_c TEXT NOT NULL CHECK(length(option_c) <= 500),
    option_d TEXT NOT NULL CHECK(length(option_d) <= 500),
    option_e TEXT CHECK(length(option_e) <= 500),
    correct_answer TEXT NOT NULL CHECK(length(correct_answer) = 1),
    explanation TEXT CHECK(length(explanation) <= 2000),
    section TEXT CHECK(length(section) <= 100),
    difficulty TEXT CHECK(length(difficulty) <= 50),
    source_file TEXT CHECK(length(source_file) <= 255),
    usage_cycle INTEGER NOT NULL DEFAULT 1,
    times_used INTEGER NOT NULL DEFAULT 0,
    last_used_at TEXT,
    created_at TEXT NOT NULL DEFAULT (datetime('now'))
);

CREATE INDEX IF NOT EXISTS idx_usage_cycle ON questions(usage_cycle);
CREATE INDEX IF NOT EXISTS idx_section ON questions(section);

CREATE TABLE IF NOT EXISTS quiz_sessions (
    session_id TEXT PRIMARY KEY CHECK(length(session_id) = 36),
    started_at TEXT NOT NULL DEFAULT (datetime('now')),
    ended_at TEXT,
    num_questions INTEGER NOT NULL,
    num_correct INTEGER NOT NULL DEFAULT 0,
    percentage_correct REAL NOT NULL DEFAULT 0.0,
    time_taken_seconds INTEGER
);

CREATE INDEX IF NOT EXISTS idx_started_at ON quiz_sessions(started_at);

CREATE TABLE IF NOT EXISTS quiz_responses (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    session_id TEXT NOT NULL CHECK(length(session_id) = 36),
    question_id INTEGER NOT NULL,
    user_answer TEXT NOT NULL CHECK(length(user_answer) = 1),
    is_correct INTEGER NOT NULL DEFAULT 0,
    time_taken_seconds INTEGER,
    FOREIGN KEY (session_id) REFERENCES quiz_sessions(session_id),
    FOREIGN KEY (question_id) REFERENCES questions(id),
    UNIQUE(session_id, question_id)
);

CREATE INDEX IF NOT EXISTS idx_responses_session_id ON quiz_responses(session_id);
CREATE INDEX IF NOT EXISTS idx_responses_question_id ON quiz_responses(question_id);
