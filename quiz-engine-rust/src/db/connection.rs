use sqlx::{
    sqlite::{SqliteConnectOptions, SqlitePool},
    Pool, Sqlite,
};
use std::path::Path;
use std::str::FromStr;

use crate::error::{QuizError, Result};

/// Create a SQLite connection pool pointed at the given URL.
/// Creates the database file if it does not exist.
pub async fn create_pool(database_url: &str) -> Result<Pool<Sqlite>> {
    let options = SqliteConnectOptions::from_str(database_url)
        .map_err(QuizError::Database)?
        .create_if_missing(true);
    let pool = SqlitePool::connect_with(options)
        .await
        .map_err(QuizError::Database)?;
    Ok(pool)
}

/// Run embedded migrations to initialize the schema.
pub async fn run_migrations(pool: &Pool<Sqlite>) -> Result<()> {
    sqlx::migrate!("./migrations")
        .run(pool)
        .await
        .map_err(|e| QuizError::Other(format!("Migration failed: {e}")))?;
    Ok(())
}

/// Create an in-memory SQLite pool for testing.
pub async fn create_test_pool() -> Pool<Sqlite> {
    let options = SqliteConnectOptions::from_str("sqlite::memory:")
        .expect("invalid in-memory url")
        .create_if_missing(true);
    let pool = SqlitePool::connect_with(options)
        .await
        .expect("Failed to create test pool");
    run_migrations(&pool)
        .await
        .expect("Failed to run migrations in test pool");
    pool
}

/// Derive the default database path from DATABASE_URL env var or use a local file.
pub fn default_db_path() -> String {
    dotenvy::var("DATABASE_URL").unwrap_or_else(|_| "sqlite:./quiz_engine.db".to_string())
}

/// Ensure the parent directory for a file-based SQLite DB exists.
pub fn ensure_db_dir(db_url: &str) -> std::io::Result<()> {
    let path = db_url
        .trim_start_matches("sqlite:")
        .trim_start_matches("sqlite://");
    // Skip in-memory and relative paths without a parent dir
    if path == ":memory:" || path.starts_with(':') {
        return Ok(());
    }
    let path = Path::new(path);
    if let Some(parent) = path.parent() {
        if !parent.as_os_str().is_empty() {
            std::fs::create_dir_all(parent)?;
        }
    }
    Ok(())
}
