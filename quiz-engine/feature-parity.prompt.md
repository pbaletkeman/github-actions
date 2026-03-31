# Quiz Engine — Feature Parity Prompt

## Purpose

Use this prompt to bring a specific quiz-engine project up to full feature parity with the shared specification. Apply it one project at a time. **Add only what is missing — do not remove, rename, or refactor any existing functionality.**

---

## Feature Parity Specification

Every quiz-engine project must implement all of the following. Before making any change, verify whether the feature already exists in the target project.

### Required Features

| # | Feature | Acceptance Criteria |
|---|---------|---------------------|
| 1 | **Markdown import** | `import --file <path>` and `import --dir <path>` commands parse `.md` question files into the database |
| 2 | **Cycle-aware selection** | Questions are not repeated until all questions have been shown at least once in the current cycle |
| 3 | **Answer shuffling** | Answer options are randomised per question per session; original answer mapping is preserved for scoring |
| 4 | **SQLite persistence** | Questions, sessions, and responses are all stored in a local SQLite database across three tables |
| 5 | **Session history** | `history` command lists past sessions with score, date, and full UUID session ID; `--session-id` filter and `--review` flag to re-read answers |
| 6 | **Export JSON** | `history --export json` writes session data to a `.json` file |
| 7 | **Export CSV** | `history --export csv` writes session data to a `.csv` file |
| 8 | **`quiz` command** | Interactive quiz with `--questions N` (default 10) and `--no-shuffle` flag |
| 9 | **`import` command** | Parses both Format 1 (simple) and Format 2 (Answer-Key table) question file formats |
| 10 | **`history` command** | Lists sessions, supports `--session-id`, `--review`, `--export json`, `--export csv` |
| 11 | **`clear` command** | `clear --questions --confirm`, `clear --history --confirm`, `clear --all --confirm`; requires explicit `--confirm` to execute |
| 12 | **Test suite ≥90%** | All tests pass; coverage enforcement threshold is set to **90%** (not 80%) using the project's native coverage tool |

---

## Per-Project Gap List

Apply only the gaps listed for the chosen project. Do not change other projects.

### C# (`quiz-engine-csharp`)

**Gaps to fix:**

1. **Add `--export json` and `--export csv` to `HistoryCommand.cs`**
   - Add `--export` option accepting `json` or `csv`
   - In `HistoryService.cs`, add `ExportToJson(IEnumerable<QuizSession> sessions, string path)` and `ExportToCsv(...)` methods
   - Each exported file should include: session ID, date, score, total questions, and per-response question text / selected answer / correct answer / was-correct
   - Use `System.Text.Json` for JSON and manual CSV string building (no third-party library)
   - Document the new option in `docs/README.md` under the `history` command options table

2. **Add `--no-shuffle` flag to `QuizCommand.cs`**
   - Add boolean flag `--no-shuffle` (default: false)
   - Pass the flag value down to `QuizService` so it skips `AnswerShuffler` when true
   - Document in `docs/README.md` under the `quiz` command options table

---

### Go (`quiz-engine-golang`)

**Gaps to fix:**

1. **Implement `--export` flag in `cmd/history.go`**
   - Register a `--export` Cobra flag (`string`, default `""`, accepts `json` or `csv`)
   - In `internal/service/history_service.go`, add `ExportToJSON(sessions []Session, path string) error` and `ExportToCSV(sessions []Session, path string) error`
   - Each exported file should include: session ID, date, score, total questions, and per-response detail
   - Use `encoding/json` for JSON and `encoding/csv` for CSV — no third-party libraries
   - Update `docs/README.md` history command table to confirm `--export json|csv` is implemented

2. **Add coverage threshold enforcement**
   - Add a `scripts/check_coverage.sh` (and `scripts/check_coverage.bat` for Windows) that:
     - Runs `go test ./... -coverprofile=coverage.out`
     - Runs `go tool cover -func=coverage.out` and parses total coverage
     - Fails with exit code 1 if total is below **90%**
   - Update `docs/README.md` Testing section to document the threshold script
   - Update the Docker Compose test service to call this script instead of bare `go test -cover`

---

### Rust (`quiz-engine-rust`)

**Gaps to fix:**

1. **Implement `--export` in `src/cli/commands/history.rs`**
   - Add `export: Option<String>` field to `HistoryArgs` struct with Clap `long` attribute
   - In `src/service/history_service.rs`, add `export_json(sessions: &[QuizSession], path: &Path) -> Result<()>` and `export_csv(sessions: &[QuizSession], path: &Path) -> Result<()>`
   - JSON serialization via the existing `serde_json` dependency
   - CSV: write manually using `std::fs::File` + `std::io::Write` — no `csv` crate needed
   - Each export includes: session_id, started_at, score, total_questions, and per-response detail
   - Update `docs/README.md` history command table to confirm `--export json|csv` is implemented

2. **Add coverage threshold enforcement**
   - Add `scripts/check_coverage.sh` (and `scripts/check_coverage.bat`) that:
     - Runs `cargo llvm-cov --summary-only 2>&1`
     - Parses the `TOTAL` line for line coverage percentage
     - Fails with exit code 1 if below **90%**
   - If `cargo-llvm-cov` is not available, fall back to `cargo tarpaulin --fail-under 90`
   - Update `docs/README.md` Testing section to document the threshold and the script
   - Update the Docker Compose test service to call this script

---

### Java (`quiz-engine-java`)

**Gap to fix:**

1. **Raise JaCoCo coverage threshold from 80% to 90%**
   - In `build.gradle`, find the `jacocoTestCoverageVerification` task
   - Change the `minimum` value from `0.80` to `0.90`
   - Run `./gradlew jacocoTestCoverageVerification` to confirm the build still passes
   - If tests fail the new threshold, write additional unit tests to reach ≥90% before changing the threshold
   - Update `docs/README.md` Features section to say *"JaCoCo coverage enforcement (≥90% line coverage)"*

---

### Python (`quiz-engine-python`)

**Gap to fix:**

1. **Raise coverage threshold from 80% to 90%**
   - In `pyproject.toml`, find `[tool.pytest.ini_options]` or `[tool.coverage.report]` and change `fail_under` from `80` to `90`
   - In `docker-compose.yml`, change `--cov-fail-under=80` to `--cov-fail-under=90` in the test service command
   - If any existing scripts (`.bat`, `.ps1`, `.sh`) hardcode `--cov-fail-under=80`, update them too
   - Run the full test suite to confirm ≥90% is already met; if not, write additional tests before changing the threshold
   - Update `docs/README.md` Testing section to document `--cov-fail-under=90`

---

### Spring Boot (`quiz-engine-springboot`)

**Gaps to fix:**

1. **Add `clear` CLI command**
   - Create `src/main/java/com/quizengine/cli/ClearCommand.java` as a Picocli `@Command`
   - Sub-commands or options: `--questions --confirm`, `--history --confirm`, `--all --confirm`
   - `--questions` deletes all rows from the questions table via the question repository
   - `--history` deletes all sessions and responses via session/response repositories
   - `--all` runs both
   - `--confirm` is a required boolean flag; if absent, print a warning and exit without deleting
   - Register the command in `QuizCli.java`
   - Document in `docs/README.md` under "CLI Commands — `clear`"

2. **Add `--export csv` to the CLI `history` command**
   - The REST endpoint `GET /api/history?export=csv` may already exist; this gap is for the CLI
   - In the Picocli history command (inside `QuizCli.java` or a dedicated `HistoryCommand.java`), add `--export` option accepting `json` or `csv`
   - Reuse the same export logic that backs the REST endpoint if it exists, or add `exportToCsv(List<QuizSession> sessions, String path)` to `HistoryService.java`
   - Document `history --export csv` in `docs/README.md`

3. **Document the JaCoCo coverage threshold**
   - Open `build.gradle.kts` and find the `jacocoTestCoverageVerification` task
   - Confirm the `minimum` value; **if it is below 0.90, raise it to 0.90**
   - Update `docs/README.md` Features section to state the exact percentage: *"JaCoCo coverage — enforced minimum threshold: 90%"*

---

## Implementation Rules

1. **Read before writing.** Before editing any file, read its current content in full.
2. **Add, never remove.** Existing commands, flags, methods, tests, and configuration must remain intact.
3. **Match the project's idiom.** Use the same libraries, error-handling patterns, and code structure already present in the project.
4. **Update docs alongside code.** Every new flag or command must be reflected in `docs/README.md` under the appropriate section.
5. **Verify tests pass.** After each change, confirm the test suite builds and the new code is covered by at least one test.
6. **One project at a time.** Apply this prompt to a single project per session. Specify the target at the start.

---

## How to Use This Prompt

Start a new chat session and open this file as context. Then say:

```
Apply the feature parity changes for quiz-engine-<name>.
Target project: \quiz-engine\quiz-engine-<name>
```

Replace `<name>` with one of: `csharp`, `golang`, `rust`, `java`, `python`, `springboot`.

Do not target `dart` or `nodejs` — they are already fully spec-compliant and need no changes.
