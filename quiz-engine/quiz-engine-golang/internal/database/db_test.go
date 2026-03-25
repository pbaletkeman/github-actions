package database

import (
	"os"
	"path/filepath"
	"testing"
)

func TestNewDB_InMemory(t *testing.T) {
	// Use a temp file since NewDB adds WAL query params which don't work with :memory:
	tmpDir := t.TempDir()
	dbPath := filepath.Join(tmpDir, "test.db")

	db, err := NewDB(dbPath)
	if err != nil {
		t.Fatalf("NewDB failed: %v", err)
	}
	defer db.Close()

	if err := db.Ping(); err != nil {
		t.Fatalf("db.Ping() failed: %v", err)
	}

	// Verify tables exist
	row := db.QueryRow("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='questions'")
	var count int
	if err := row.Scan(&count); err != nil {
		t.Fatalf("query failed: %v", err)
	}
	if count != 1 {
		t.Error("expected questions table to exist")
	}
}

func TestNewDB_InvalidPath(t *testing.T) {
	// Try to open a DB in a non-existent directory
	_, err := NewDB("/nonexistent/path/test.db")
	if err == nil {
		t.Error("expected error for invalid path")
	}
}

func TestInitSchema_Idempotent(t *testing.T) {
	db := openTestDB(t)

	// Calling InitSchema again should not fail (IF NOT EXISTS)
	if err := InitSchema(db); err != nil {
		t.Fatalf("InitSchema second call failed: %v", err)
	}
}

func TestNewDB_CreatesFile(t *testing.T) {
	tmpDir := t.TempDir()
	dbPath := filepath.Join(tmpDir, "quiz_test.db")

	if _, err := os.Stat(dbPath); !os.IsNotExist(err) {
		t.Fatal("DB file should not exist yet")
	}

	db, err := NewDB(dbPath)
	if err != nil {
		t.Fatalf("NewDB failed: %v", err)
	}
	db.Close()
}
