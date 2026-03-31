# Inspect and Recreate Quiz Engine Projects

Inspect each of the projects listed below. For each project, create a dedicated prompt file that contains enough detail to fully recreate the project from scratch.

## Projects to Inspect

- `quiz-engine/quiz-engine-python`
- `quiz-engine/quiz-engine-nodejs`
- `quiz-engine/quiz-engine-java`
- `quiz-engine/quiz-engine-springboot`
- `quiz-engine/quiz-engine-csharp`
- `quiz-engine/quiz-engine-dart`
- `quiz-engine/quiz-engine-golang`
- `quiz-engine/quiz-engine-rust`

## Prompt File Requirements

- Save each file to `.github/prompts/` following the existing naming convention: `10-recreate-quizEngine-<language>.prompt.md`
- Do not overwrite or modify any existing files in that directory
- Each prompt must be self-contained — someone with no prior context must be able to recreate the project from it alone

## What to Capture for Each Project

### 1. Project Structure
Every directory and file, with a brief description of each file's purpose.

### 2. Language, Runtime, and Dependencies
Exact versions from the build file (`pom.xml`, `build.gradle`, `package.json`, `Cargo.toml`, `pubspec.yaml`, `go.mod`, `*.csproj`, etc.).

### 3. Database Schema
All tables, columns, types, constraints, and relationships as they exist in the migration or ORM model files.

### 4. CLI Commands
Every command and subcommand with all flags, default values, and example invocations including expected output.

### 5. Documentation
Capture the structure and content of `docs/README.md` including every section heading, table, and code block so the docs can be regenerated alongside the code.

### 6. Question File Formats
Both supported formats (simple and Answer-Key table), with at least two complete sample questions per format taken from actual files in the repo.

### 7. Unit Test Coverage
The enforced threshold value, the tool used to measure it (pytest-cov, JaCoCo, llvm-cov, etc.), and where that threshold is configured (file name and property name).

### 8. Scripts
Every script file (`.sh`, `.bat`, `.ps1`) with its location, purpose, and the exact command to invoke it from the project root.

### 9. Docker Setup
Full content of `Dockerfile` and `docker-compose.yml`, including all service definitions, environment variables, and volume mounts.

### 10. Architecture Decisions
Any notable patterns (repository pattern, service layer, ORM vs raw SQL, dependency injection approach) so they are reproduced faithfully.
