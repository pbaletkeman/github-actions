use std::path::PathBuf;

use clap::Args;
use sqlx::{Pool, Sqlite};

use crate::error::Result;
use crate::service::import_service::ImportService;

#[derive(Args, Debug)]
pub struct ImportArgs {
    /// Path to the markdown file containing questions
    #[arg(short, long)]
    pub file: PathBuf,
}

pub async fn run_import(pool: Pool<Sqlite>, args: ImportArgs) -> Result<()> {
    println!("Importing questions from: {}", args.file.display());

    let (imported, skipped) = ImportService::import_from_file(&pool, &args.file).await?;

    println!("Import complete:");
    println!("  Imported: {imported}");
    println!("  Skipped (duplicates): {skipped}");

    Ok(())
}
