# How to Use Quiz Engine Prompts with GitHub Copilot (Computer Off)

## Overview

You now have three new files that enable GitHub Copilot to work with your 8 quiz engine implementations even when your computer is off. Here's how to use them:

---

## Files Created

### 1. **push-prompts-to-github.ps1**
- PowerShell script that pushes all 8 prompt files to GitHub
- Location: `c:\Users\Pete\Desktop\github-actions\push-prompts-to-github.ps1`
- **Run this FIRST** to sync files to GitHub

### 2. **00-quiz-engine-meta-prompt.md**
- Comprehensive reference document for GitHub Copilot
- Location: `.github/prompts/00-quiz-engine-meta-prompt.md`
- Use this when chatting with GitHub Copilot via the web interface
- It gives Copilot a complete understanding of all 8 implementations

### 3. **GITHUB-COPILOT-USAGE.md** (this file)
- Usage instructions and examples
- Helps you understand the workflow

---

## Step 1: Push Files to GitHub

### Option A: Using the PowerShell Script (Recommended)

```powershell
# Open PowerShell in the repository directory
cd c:\Users\Pete\Desktop\github-actions

# Run the script
.\push-prompts-to-github.ps1
```

**What it does:**
1. ✅ Verifies all 8 prompt files exist
2. ✅ Stages all files for commit
3. ✅ Creates a git commit with a descriptive message
4. ✅ Pushes everything to `pbaletkeman/github-actions` on GitHub
5. ✅ Shows confirmation with GitHub repository URL

### Option B: Manual Git Commands

```bash
# Navigate to repository
cd c:\Users\Pete\Desktop\github-actions

# Stage all prompt files
git add .github/prompts/*.prompt.md
git add .github/prompts/00-quiz-engine-meta-prompt.md

# Commit
git commit -m "Add meta-prompt and documentation for Github Copilot collaboration"

# Push to GitHub
git push origin main
```

---

## Step 2: Access GitHub Copilot Web Interface

Once files are pushed to GitHub, you can use GitHub Copilot from anywhere (computer off, mobile, etc.):

### Access Methods

**Method 1: GitHub Web (Copilot Chat)**
1. Go to: https://github.com/pbaletkeman/github-actions
2. Look for the **Copilot Chat** icon or use `Ctrl + Shift + I` (if available)
3. Reference the prompt files in your questions

**Method 2: GitHub Copilot Web UI**
1. ⚠️ Requires GitHub Copilot Pro or Team subscription
2. Go to: https://github.com/copilot
3. Create a new chat session
4. Reference your prompt files from the repository

**Method 3: GitHub Pull Request / Issue Comments**
1. Create an issue or PR in your repository
2. Use `@github-copilot` to invoke Copilot
3. Reference the prompt files or meta-prompt

---

## Step 3: Using the Meta-Prompt with GitHub Copilot

### Example Workflow

#### Question 1: "Understand My Quiz Engine Implementations"
```
@github-copilot

Please read the file `.github/prompts/00-quiz-engine-meta-prompt.md`
and summarize what implementations I have and their key features.
```

**Copilot will:**
- Understand all 8 languages
- Know the testing requirements (>90% coverage)
- Understand Docker standards
- See API conventions across all implementations

#### Question 2: "Generate a New Feature"
```
@github-copilot

Based on the Quiz Engine implementations in `.github/prompts/`,
please generate code for a new endpoint: `/api/v1/quizzes/{id}/attempts`

The endpoint should:
- Return all previous quiz attempts for a user
- Include score, duration, and timestamp
- Be consistent with existing API patterns

Generate this for: Python, Node.js, and Rust
```

**Copilot will:**
- Reference your existing patterns for all 3 languages
- Ensure API response format matches your spec
- Maintain consistency with testing approach

#### Question 3: "Create Docker Deployment Instructions"
```
@github-copilot

Reference `.github/prompts/00-quiz-engine-meta-prompt.md`
and the Docker & Containerization phase.

Create a step-by-step guide for deploying all 8 quiz engine implementations
to Docker Hub with multi-architecture support (linux/amd64, linux/arm64).
```

**Copilot will:**
- Know the base images for each language
- Understand your non-root user strategy
- Reference the correct coverage tools

---

## Reference the Actual Implementation Prompts

When you need language-specific details, reference the actual implementation files:

### Python
```
Read `.github/prompts/01-plan-quizEngine-python.prompt.md`
```

### Node.js
```
Read `.github/prompts/02-plan-quizEngine-nodejs.prompt.md`
```

### Java
```
Read `.github/prompts/03-plan-quizEngine-java.prompt.md`
```

### Spring Boot
```
Read `.github/prompts/04-plan-quizEngine-springboot.prompt.md`
```

### C#
```
Read `.github/prompts/05-plan-quizEngine-csharp.prompt.md`
```

### Dart
```
Read `.github/prompts/06-plan-quizEngine-dart.prompt.md`
```

### Go
```
Read `.github/prompts/07-plan-quizEngine-golang.prompt.md`
```

### Rust
```
Read `.github/prompts/08-plan-quizEngine-rust.prompt.md`
```

---

## Common Use Cases

### Use Case 1: Cross-Language API Design
```
@github-copilot

From the Quiz Engine implementations, what is the consistent API response
format used across all 8 languages? Show me an example response for creating
a new quiz session.
```

### Use Case 2: Testing Standards
```
@github-copilot

Reference the implementations in `.github/prompts/`.
Show me the testing approach for [Language] that ensures >90% code coverage.
Include the exact commands I need to run.
```

### Use Case 3: Database Schema
```
@github-copilot

From all 8 implementations, what is the quiz engine database schema?
Show the ERD (Entity Relationship Diagram) and explain how each language
uses its ORM (SQLAlchemy, TypeORM, JPA, etc.) to interact with it.
```

### Use Case 4: Docker Deployment
```
@github-copilot

Based on `.github/prompts/00-quiz-engine-meta-prompt.md`,
create a docker-compose.yml that spins up all 8 quiz engine implementations
simultaneously with their test suites. Include coverage verification for each.
```

---

## Offline Workflow (Computer Off)

### Scenario: You're away from your computer

**What you CAN do:**
- ✅ Access GitHub via web browser from any device
- ✅ Chat with GitHub Copilot about your implementations
- ✅ Get code generation help
- ✅ Ask for architectural guidance
- ✅ Review implementations side-by-side
- ✅ Design new features
- ✅ Get testing strategies

**What you CANNOT do (requires your computer):**
- ❌ Run build/test commands locally
- ❌ Execute Docker builds
- ❌ Deploy to production
- ❌ Edit files directly in VS Code

**Workaround:**
- **GitHub Codespaces** (if you have Copilot Pro)
  - Spin up cloud-based VS Code environment
  - Run commands, build, test, and deploy
  - Continue working exactly as if your computer was on
  - Reference: https://github.com/codespaces

---

## Best Practices

### 1. Always Reference the Meta-Prompt First
```
Don't:
"How do I test Python code?"

Do:
"From the Quiz Engine implementations in `.github/prompts/00-quiz-engine-meta-prompt.md`,
what is the Python testing approach with >90% coverage?"
```

### 2. Be Specific About Which Implementation
```
Don't:
"Generate an API endpoint"

Do:
"For the Java Quiz Engine implementation (reference 03-plan-quizEngine-java.prompt.md),
generate a new REST endpoint for quiz analytics."
```

### 3. Include Consistency Requirements
```
Don't:
"Create database migration code"

Do:
"Create a database migration for adding a 'difficulty_level' column to questions.
Make it consistent across all 8 implementations (see `.github/prompts/`)
using each language's migration framework (Alembic, Flyway, Entity Framework, etc.)"
```

### 4. Ask for Cross-Language Comparison
```
"Compare how each of the 8 implementations handle error responses.
Reference the API design patterns in `.github/prompts/00-quiz-engine-meta-prompt.md`"
```

---

## File Structure After Push

```
pbaletkeman/github-actions (GitHub)
├── .github/
│   ├── prompts/
│   │   ├── 00-quiz-engine-meta-prompt.md           ← Start here
│   │   ├── 01-plan-quizEngine-python.prompt.md
│   │   ├── 02-plan-quizEngine-nodejs.prompt.md
│   │   ├── 03-plan-quizEngine-java.prompt.md
│   │   ├── 04-plan-quizEngine-springboot.prompt.md
│   │   ├── 05-plan-quizEngine-csharp.prompt.md
│   │   ├── 06-plan-quizEngine-dart.prompt.md
│   │   ├── 07-plan-quizEngine-golang.prompt.md
│   │   ├── 08-plan-quizEngine-rust.prompt.md
│   │   └── GITHUB-COPILOT-USAGE.md                ← You are here
│   └── ...
├── push-prompts-to-github.ps1                       ← Run once to sync
└── ...
```

---

## Troubleshooting

### Problem: "Authentication failed" when running push script

**Solution:**
1. Ensure you're logged into GitHub: `git config --global user.name "Your Name"`
2. Use SSH keys or personal access tokens
3. Try: `git push -v` to see detailed error messages

### Problem: GitHub Copilot doesn't see the files

**Solution:**
1. Verify files are pushed: `git push --verify`
2. Wait 1-2 minutes for GitHub to index the changes
3. Hard refresh your browser (`Ctrl + F5`)
4. Access: https://github.com/pbaletkeman/github-actions/blob/main/.github/prompts/

### Problem: Copilot gives inconsistent answers

**Solution:**
1. Always reference the meta-prompt first in your question
2. Ask Copilot to: "Reference the implementations in `.github/prompts/`"
3. Provide specific file names: "From `01-plan-quizEngine-python.prompt.md`..."

---

## Security Notes

⚠️ **Before pushing to GitHub:**
1. Review all 8 prompt files for any sensitive information
2. Ensure no API keys, passwords, or credentials are in the files
3. These files are now public (if your repo is public)
4. GitHub Copilot will have full access to all content

✅ **Safe to share:**
- Architecture decisions
- Algorithm explanations
- Language-specific patterns
- Testing strategies
- Docker configurations

---

## Next Steps

1. **Push files to GitHub** using the script
2. **Access GitHub Copilot** from any device
3. **Reference the meta-prompt** when asking questions
4. **Collaborate efficiently** even when your computer is off!

---

## Questions?

Refer to:
- `.github/prompts/00-quiz-engine-meta-prompt.md` - Comprehensive reference
- Individual implementation files for language-specific details
- GitHub Copilot documentation: https://docs.github.com/en/copilot

**Last Updated:** March 23, 2026
