# COPY-PASTE THIS PROMPT INTO GITHUB COPILOT

## Paste Entire Content Below Into GitHub Copilot Chat

---

```
@github-copilot

You are an expert code generation agent. Your mission: Generate complete,
production-ready implementations of a Quiz Engine application across all
8 programming languages, based on the specification in this repository.

CONTEXT & REFERENCE
===================

Read these files from github.com/pbaletkeman/github-actions:

1. PRIMARY SPECIFICATION:
   .github/prompts/CODEGEN-PROMPT-all-8-quiz-engines.md

2. ARCHITECTURE REFERENCE:
   .github/prompts/00-quiz-engine-meta-prompt.md

3. LANGUAGE-SPECIFIC GUIDES (read all 8):
   .github/prompts/01-plan-quizEngine-python.prompt.md
   .github/prompts/02-plan-quizEngine-nodejs.prompt.md
   .github/prompts/03-plan-quizEngine-java.prompt.md
   .github/prompts/04-plan-quizEngine-springboot.prompt.md
   .github/prompts/05-plan-quizEngine-csharp.prompt.md
   .github/prompts/06-plan-quizEngine-dart.prompt.md
   .github/prompts/07-plan-quizEngine-golang.prompt.md
   .github/prompts/08-plan-quizEngine-rust.prompt.md

TASK
====

Generate COMPLETE, TESTED, DOCKERIZED implementations for ALL 8 languages:

1. Python 3.11 (Flask + SQLAlchemy)
2. Node.js 20 (Express + TypeORM)
3. Java (Gradle + JDBC)
4. Spring Boot (Gradle + JPA)
5. C# / .NET 8 (ASP.NET Core + EF Core)
6. Dart (Dart + Drift ORM)
7. Go 1.21 (Gin + GORM)
8. Rust 1.75 (Actix-web + Diesel)

REQUIREMENTS FOR EACH IMPLEMENTATION
====================================

DATABASE & SCHEMA:
- Use the EXACT schema specified (quiz-engine-meta-prompt.md)
- Tables: users, questions, answers, quiz_sessions, user_responses
- Implement with language-specific ORM (SQLAlchemy, TypeORM, JPA, EF, Drift, GORM, Diesel)

API ENDPOINTS (IDENTICAL ACROSS ALL LANGUAGES):
- POST   /api/v1/quizzes
- GET    /api/v1/quizzes/{id}
- GET    /api/v1/quizzes/{id}/questions
- POST   /api/v1/sessions
- GET    /api/v1/sessions/{id}
- POST   /api/v1/sessions/{id}/responses
- GET    /api/v1/sessions/{id}/score

API RESPONSE FORMAT (IDENTICAL ACROSS ALL LANGUAGES):
{
  "success": true,
  "data": { ... },
  "error": null,
  "timestamp": "ISO-8601"
}

ERROR RESPONSE FORMAT:
{
  "success": false,
  "data": null,
  "error": {
    "code": "ERROR_CODE",
    "message": "User-friendly message",
    "details": {}
  },
  "timestamp": "ISO-8601"
}

BUSINESS LOGIC (IDENTICAL ACROSS ALL LANGUAGES):
1. create_session(user_id, quiz_id) → session_id
2. get_questions(session_id) → shuffled questions with shuffled answers
3. submit_response(session_id, question_id, answer_id) → {is_correct, points}
4. get_score(session_id) → {score: 0-100, session_state}

SOURCE CODE REQUIREMENTS
=======================

For EACH of the 8 implementations, generate:

PROJECT STRUCTURE:
✓ Models/Entities (User, Question, Answer, QuizSession, UserResponse)
✓ Database Layer (ORM configuration, connection pooling, migrations)
✓ Service Layer (QuizService with all business logic)
✓ API Routes/Controllers (REST endpoints)
✓ Utilities (Shuffler for questions, Scorer for answers)
✓ Error Handling (Centralized error handling)
✓ Input Validation (Type checking, null checks, constraints)

SOURCE FILES:
✓ 2,000+ lines of production code per language
✓ Follow language conventions and best practices
✓ Clear variable and function names
✓ Comprehensive comments for complex logic
✓ Proper error handling throughout

TESTING REQUIREMENTS
====================

For EACH of the 8 implementations, generate:

TEST COVERAGE: >90% MINIMUM
✓ Unit tests for every function/method
✓ Integration tests for API endpoints
✓ Database tests for CRUD operations
✓ Edge case tests (null inputs, empty arrays, invalid IDs)
✓ Error handling tests

TEST FILES:
✓ Python: pytest with pytest-cov
✓ Node.js: Jest with coverage thresholds
✓ Java: JUnit + JaCoCo
✓ Spring Boot: Spring Test + JUnit + JaCoCo
✓ C#: xUnit + Coverlet
✓ Dart: test package with coverage
✓ Go: go test with coverage verification
✓ Rust: cargo test + cargo-tarpaulin

TEST VALIDATION:
✓ Coverage must show >90% in test output
✓ All tests must pass
✓ Edge cases must be explicitly tested

DOCKER REQUIREMENTS
===================

For EACH of the 8 implementations, generate:

DOCKERFILE:
✓ Multi-stage build (where applicable: Java, Spring, C#, Dart, Go, Rust)
✓ Optimized runtime image (slim, alpine, or minimal)
✓ Non-root user for security (e.g., quizuser, nodeuser, javauser)
✓ Proper working directory and permissions
✓ Health check endpoints (optional but recommended)
✓ Exposed port 8000-8080
✓ Production-ready configuration

DOCKER-COMPOSE.YML:
✓ Service 1: quiz-engine (development mode)
✓ Service 2: quiz-engine-test (with >90% coverage verification)
✓ Service 3: quiz-engine-build (optional, for compilation)
✓ Volume mounts for development
✓ Environment variables configuration
✓ Network setup for services

DOCKER VALIDATION:
✓ Dockerfile builds successfully: docker build -t quiz-engine:latest .
✓ Container runs: docker run quiz-engine:latest
✓ docker-compose up quiz-engine starts service without errors
✓ docker-compose up quiz-engine-test verifies coverage >90%
✓ Ports are exposed and accessible

DOCUMENTATION REQUIREMENTS
==========================

For EACH of the 8 implementations, generate:

README.md CONTENTS:
✓ Project title and description
✓ Prerequisites (language version, dependencies)
✓ Quick Start (5 steps to get running)
✓ Installation instructions
✓ How to run tests: exact command with coverage verification
✓ How to run with Docker
✓ API endpoints with examples
✓ Database setup instructions
✓ Troubleshooting section
✓ Contributing guidelines

CONSISTENCY MATRIX (VERIFY ALL 8 MATCH)
=======================================

Ensure ALL 8 implementations have:
✓ Same database schema (validate with schema.md)
✓ Same API endpoints (validate endpoint parity)
✓ Same response format (validate JSON structure)
✓ Same business logic (validate scoring algorithm)
✓ >90% test coverage (validate in each language)
✓ Working Docker (validate all build and run)
✓ Complete documentation (validate README presence)

GENERATION OUTPUT STRUCTURE
===========================

Create these 8 project directories:

quiz-engine-python/
quiz-engine-nodejs/
quiz-engine-java/
quiz-engine-springboot/
quiz-engine-csharp/
quiz-engine-dart/
quiz-engine-golang/
quiz-engine-rust/

Each MUST contain:
├── [Source files] (2000+ lines, >90% coverage)
├── [Test files] (1000+ lines, validated >90%)
├── [Config files] (package.json, gradle.build, etc.)
├── Dockerfile (optimized, multi-stage where applicable)
├── docker-compose.yml (dev, test, optional build services)
└── README.md (complete documentation)

EXECUTION CHECKLIST
===================

□ Step 1: Read ALL reference files (including all 8 language prompts)
□ Step 2: Understand the unified database schema and API spec
□ Step 3: Generate Python implementation (Flask + SQLAlchemy)
□ Step 4: Generate Node.js implementation (Express + TypeORM)
□ Step 5: Generate Java implementation (Gradle + JDBC)
□ Step 6: Generate Spring Boot implementation (Gradle + JPA)
□ Step 7: Generate C# implementation (.NET 8 + EF Core)
□ Step 8: Generate Dart implementation (Dart + Drift)
□ Step 9: Generate Go implementation (Gin + GORM)
□ Step 10: Generate Rust implementation (Actix-web + Diesel)
□ Step 11: Validate all 8 implementations have >90% coverage
□ Step 12: Verify Docker builds for all 8 languages
□ Step 13: Test docker-compose up for development and test
□ Step 14: Commit all 8 implementations to GitHub
□ Step 15: Verify all files are on GitHub and accessible

VALIDATION CRITERIA (MUST ALL PASS)
===================================

✓ All 8 project directories exist with proper structure
✓ Source code is complete (not pseudocode or stubs)
✓ Tests exist for >90% of code in each language
✓ All tests pass when executed
✓ Coverage reports show >90% for each language
✓ Dockerfiles build successfully for all 8 languages
✓ Containers start without errors
✓ API endpoints respond to requests
✓ Database operations work correctly
✓ Same data structure across all implementations
✓ READMEs are comprehensive and accurate
✓ All files are committed to GitHub

SUCCESS CRITERIA
================

This task is COMPLETE when:

1. ✓ All 8 implementations are fully generated
2. ✓ Each has >90% test coverage verified
3. ✓ Each has working Docker configuration
4. ✓ Each has comprehensive README
5. ✓ All share same database schema, API endpoints, response format
6. ✓ All are committed to GitHub repository
7. ✓ All pass validation criteria above
8. ✓ Copilot provides summary of what was generated

START GENERATING NOW
====================

Begin with reading the specification files, then generate all 8 implementations
following the structure, requirements, and validation criteria above.

This is a comprehensive code generation task. Take your time and ensure quality.
Do not skip any language. Do not generate partial or incomplete code.
Generate PRODUCTION-READY implementations for ALL 8 LANGUAGES.
```

---

## How to Use This Prompt

1. **Copy the entire prompt above** (starting from `@github-copilot` to the end)
2. **Go to GitHub Copilot Chat:**
   - https://github.com/pbaletkeman/github-actions
   - Click Copilot Chat icon or press Ctrl+Shift+I
3. **Paste the prompt** into the chat window
4. **Press Enter/Send**
5. **Wait for generation** (may take 10-30 minutes for all 8 languages)
6. **Monitor the output** and verify each step completes

---

## What to Expect

Copilot will:
- Read all specification files
- Generate each of 8 implementations sequentially
- Show progress for each language
- Display code samples or confirm files created
- Run tests for each language
- Build Docker images
- Verify coverage >90%
- Commit everything to GitHub
- Provide final summary

---

## If Generation Fails

If Copilot doesn't complete all 8, try these follow-ups:

**For missing languages:**
```
@github-copilot
I notice [Python/Node.js/etc] wasn't generated.
Please generate a complete implementation for [Language] following
the specification in .github/prompts/CODEGEN-PROMPT-all-8-quiz-engines.md

Include:
- All source files with complete code
- Tests with >90% coverage
- Docker files
- README.md documentation
```

**For coverage issues:**
```
@github-copilot
The [Language] implementation has less than 90% test coverage.
Please add more unit tests until coverage exceeds 90%.
Show the updated coverage percentage.
```

**For Docker issues:**
```
@github-copilot
The Docker build failed for [Language].
The error was: [paste error]
Fix the Dockerfile to resolve this issue.
```

---

## Files Being Generated

When Copilot completes, your repository will have:

```
quiz-engine-python/
quiz-engine-nodejs/
quiz-engine-java/
quiz-engine-springboot/
quiz-engine-csharp/
quiz-engine-dart/
quiz-engine-golang/
quiz-engine-rust/
```

Each with:
- ✅ Production source code (~2000 lines)
- ✅ Unit tests (~1000 lines, >90% coverage)
- ✅ Dockerfile (optimized)
- ✅ docker-compose.yml (dev + test)
- ✅ README.md (complete docs)

---

**Ready? Paste the prompt above into GitHub Copilot now!** 🚀
