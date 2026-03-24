# How to Generate All 8 Quiz Engines with GitHub Copilot Agent

**Status:** ✅ All generation prompts now on GitHub  
**Files Pushed:** CODEGEN-PROMPT-all-8-quiz-engines.md  
**Repository:** https://github.com/pbaletkeman/github-actions

---

## Quick Start: Generate All 8 Implementations

### Option 1: Using GitHub Copilot Web Chat (Easiest)

1. **Navigate to your repository:**
   ```
   https://github.com/pbaletkeman/github-actions
   ```

2. **Open GitHub Copilot Chat** (Ctrl+Shift+I or click Copilot icon)

3. **Send this prompt to Copilot:**
   ```
   @github-copilot
   
   You are a code generation expert. Your task is to generate all 8 quiz engine 
   implementations based on the specification in this repository.
   
   1. Read: .github/prompts/CODEGEN-PROMPT-all-8-quiz-engines.md
   2. Read: .github/prompts/00-quiz-engine-meta-prompt.md
   3. Reference: Each individual prompt file (0[1-8]-plan-quizEngine-*.prompt.md)
   
   Then generate COMPLETE, PRODUCTION-READY code for all 8 languages:
   1. Python (Flask + SQLAlchemy)
   2. Node.js (Express + TypeORM)
   3. Java (Gradle + JDBC)
   4. Spring Boot (Gradle + JPA)
   5. C# (.NET 8 + EF Core)
   6. Dart (Dart + Drift)
   7. Go (Gin + GORM)
   8. Rust (Actix-web + Diesel)
   
   For each implementation:
   - Create project structure following the specification
   - Generate all source files with complete code
   - Add comprehensive unit tests (>90% coverage)
   - Generate Docker and docker-compose files
   - Create README.md documentation
   - Ensure all implementations use the SAME database schema and API format
   
   Success criteria:
   - All 8 implementations have >90% test coverage
   - All APIs follow the same endpoint patterns
   - All responses use the same JSON envelope format
   - All Docker images build and run successfully
   - Code is production-ready and follows language best practices
   
   Create the implementations and put them into respective directories:
   quiz-engine-python/
   quiz-engine-nodejs/
   quiz-engine-java/
   quiz-engine-springboot/
   quiz-engine-csharp/
   quiz-engine-dart/
   quiz-engine-golang/
   quiz-engine-rust/
   
   Then commit to GitHub.
   ```

4. **Copilot will:**
   - Read all specification files
   - Generate all 8 implementations
   - Create test files with >90% coverage
   - Generate Docker files
   - Commit everything to GitHub

---

### Option 2: Using Copilot Agent via VS Code

If you have VS Code with Copilot extension:

1. **Open the CODEGEN prompt file:**
   ```
   .github/prompts/CODEGEN-PROMPT-all-8-quiz-engines.md
   ```

2. **Launch Copilot Agent:**
   - Click Copilot icon
   - Select "Use Agent Mode"
   - Or use: Ctrl+Shift+I (Agent)

3. **Send agent prompt:**
   ```
   Read the current file and generate all 8 quiz engine implementations 
   according to the specification. Create complete, tested, and dockerized code 
   for all languages. Commit to GitHub when complete.
   ```

---

### Option 3: Programmatic API Call (If Using Copilot API)

```python
import requests

prompt = """
Read these files from github.com/pbaletkeman/github-actions:
- .github/prompts/CODEGEN-PROMPT-all-8-quiz-engines.md
- .github/prompts/00-quiz-engine-meta-prompt.md
- .github/prompts/0[1-8]-plan-quizEngine-*.prompt.md

Generate complete implementations for all 8 languages following the specification.
Ensure all implementations have:
- >90% test coverage
- Same database schema
- Identical API endpoints and response formats
- Working Docker configurations
- Production-ready code

Create directories quiz-engine-[language] and commit to GitHub.
"""

response = requests.post(
    "https://api.github.com/copilot/generate",
    json={"prompt": prompt, "repository": "pbaletkeman/github-actions"}
)
```

---

## What Copilot Will Generate

### For Each of 8 Languages:

**Source Code (~1500-2000 lines per language):**
- ✅ Models/entities
- ✅ Database layer with ORM
- ✅ Core business logic
- ✅ REST API endpoints
- ✅ Error handling
- ✅ Input validation

**Testing (~1000-1500 lines per language):**
- ✅ Unit tests for all functions
- ✅ Integration tests for API
- ✅ Database tests
- ✅ Edge case coverage
- ✅ >90% code coverage verification

**Docker & Deployment:**
- ✅ Multi-stage Dockerfile
- ✅ Optimized runtime image
- ✅ Non-root user security
- ✅ docker-compose.yml with dev/test services
- ✅ Coverage verification in test service

**Documentation:**
- ✅ README.md with quick start
- ✅ Setup instructions
- ✅ How to run tests
- ✅ How to run with Docker
- ✅ API documentation

---

## Expected Output Structure

```
quiz-engine-python/
  ├── quiz_engine/
  ├── tests/
  ├── requirements.txt
  ├── Dockerfile
  ├── docker-compose.yml
  └── README.md

quiz-engine-nodejs/
  ├── src/
  ├── tests/
  ├── package.json
  ├── Dockerfile
  ├── docker-compose.yml
  └── README.md

quiz-engine-java/
  ├── src/
  ├── build.gradle
  ├── Dockerfile
  ├── docker-compose.yml
  └── README.md

# ... and 5 more for Spring Boot, C#, Dart, Go, Rust
```

---

## Validating Generated Code

After Copilot generates everything, verify:

### 1. **All 8 directories exist:**
```bash
ls -la quiz-engine-*/
```

### 2. **Each has source code:**
```bash
find quiz-engine-* -name "*.py" -o -name "*.java" -o -name "*.go" -o -name "*.ts" | wc -l
# Should show 100+ files
```

### 3. **Each has tests:**
```bash
find quiz-engine-*/test* -type f | wc -l
# Should show 40+ test files
```

### 4. **Each has Docker files:**
```bash
find quiz-engine-* -name "Dockerfile" -o -name "docker-compose.yml"
# Should show 16 files (2 per language)
```

### 5. **Each has README:**
```bash
find quiz-engine-*/README.md
# Should show 8 files
```

### 6. **Verify coverage requirements in README:**
```bash
grep -r ">90%" quiz-engine-*/README.md
# Should show coverage requirements for each language
```

---

## Testing Generated Implementations

### Build & Test Each Implementation:

**Python:**
```bash
cd quiz-engine-python
python -m venv venv
source venv/bin/activate
pip install -r requirements.txt
pytest --cov=quiz_engine --cov-fail-under=90
```

**Node.js:**
```bash
cd quiz-engine-nodejs
npm install
npm test  # Should verify Jest coverage >90%
```

**Docker:**
```bash
cd quiz-engine-[language]
# Test development container
docker-compose up quiz-engine &
sleep 2
curl http://localhost:8000/api/v1/quizzes  # Should return 200
docker-compose down

# Test with coverage
docker-compose up quiz-engine-test
# Should verify coverage >90%
```

---

## Benefits of This Approach

✅ **Complete Implementation**
- All 8 languages generated simultaneously
- Consistent architecture across all
- Production-ready code quality

✅ **Testing Included**
- >90% coverage in each language
- Automated validation in Docker
- CI/CD ready

✅ **Containerized**
- Ready for deployment
- Multi-stage builds for optimization
- Security hardened (non-root users)

✅ **Documented**
- Each has complete README
- API patterns consistent
- Database schema unified

✅ **Scalable**
- Can extend with new features
- Consistent patterns for maintenance
- Easy to understand and modify

---

## GitHub Repository Structure After Generation

```
pbaletkeman/github-actions/
├── .github/prompts/
│   ├── 00-quiz-engine-meta-prompt.md
│   ├── CODEGEN-PROMPT-all-8-quiz-engines.md      ← Code generation spec
│   ├── GITHUB-COPILOT-USAGE.md
│   ├── 01-plan-quizEngine-python.prompt.md
│   ├── 02-plan-quizEngine-nodejs.prompt.md
│   ├── ... (8 language prompts)
│   └── GITHUB-COPILOT-SETUP-COMPLETE.md
├── quiz-engine-python/                          ← Generated implementations
├── quiz-engine-nodejs/
├── quiz-engine-java/
├── quiz-engine-springboot/
├── quiz-engine-csharp/
├── quiz-engine-dart/
├── quiz-engine-golang/
├── quiz-engine-rust/
└── README.md (main)
```

---

## Troubleshooting

### Issue: "Copilot didn't generate all 8 languages"

**Solution:**
```
Re-send with more specific prompt:

@github-copilot

I need you to generate implementations for ALL 8 languages:
1. Python - Flask app
2. Node.js - Express app
3. Java - Gradle project
4. Spring Boot - Spring Data JPA
5. C# - .NET 8 Minimal APIs
6. Dart - Shelf HTTP server
7. Go - Gin framework
8. Rust - Actix-web framework

Generate COMPLETE code for EACH language, not abstractions or pseudocode.
Include tests with >90% coverage for each.
```
```

### Issue: "Tests don't have >90% coverage"

**Solution:**
Ask Copilot to:
```
Add more unit tests to each implementation until coverage exceeds 90%.
Verify with: pytest --cov, npm test, gradle test, dotnet test, etc.
```

### Issue: "Docker doesn't build"

**Solution:**
```
@github-copilot

The Dockerfile for [language] won't build. 
The error is: [paste error]

Fix it to:
1. Use appropriate base image
2. Install all dependencies
3. Copy source and build
4. Create non-root user
5. Expose correct port
```

---

## Next Steps

1. **Send the prompt** to GitHub Copilot using Option 1, 2, or 3 above
2. **Wait for generation** (may take 5-10 minutes for all 8)
3. **Test each implementation** using the Docker command
4. **Verify coverage** meets >90% requirement
5. **Commit to GitHub** when all tests pass
6. **Deploy** to your production environment

---

## Reference Files

All files needed by Copilot to generate the implementations:

| File | Purpose |
|------|---------|
| `CODEGEN-PROMPT-all-8-quiz-engines.md` | ← **USE THIS** Spec for generating all 8 |
| `00-quiz-engine-meta-prompt.md` | Architecture overview (reference) |
| `01-plan-quizEngine-python.prompt.md` | Python-specific design (reference) |
| `02-plan-quizEngine-nodejs.prompt.md` | Node.js-specific design (reference) |
| ... | ... etc for all 8 languages |

---

**Ready to Generate?**

Use the prompt from Option 1 above and send it to GitHub Copilot now! 🚀

All 8 quiz engines will be generated with complete code, tests, Docker, and documentation.
