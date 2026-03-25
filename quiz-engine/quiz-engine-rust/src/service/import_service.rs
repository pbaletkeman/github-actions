use std::path::Path;

use sqlx::{Pool, Sqlite};

use crate::db::repositories::QuestionRepo;
use crate::error::Result;
use crate::service::markdown_parser::parse_markdown_file;

pub struct ImportService;

impl ImportService {
    /// Import questions from a markdown file, skipping duplicates.
    ///
    /// Returns `(imported, skipped)` counts.
    pub async fn import_from_file(
        pool: &Pool<Sqlite>,
        path: &Path,
    ) -> Result<(usize, usize)> {
        let questions = parse_markdown_file(path)?;
        let mut imported = 0;
        let mut skipped = 0;

        for q in &questions {
            match QuestionRepo::insert_if_not_exists(pool, q).await? {
                Some(_) => imported += 1,
                None => skipped += 1,
            }
        }

        Ok((imported, skipped))
    }
}
