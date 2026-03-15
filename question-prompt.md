# GitHub Actions (GH-200) Exam Question Generator — Revised

## Executive Summary

This prompt guides generation of **150–175 high-quality exam-style questions** for the GitHub Actions GH-200 certification. Questions will be scenario-based, cognitively rigorous, and aligned with the 19 topic files from the split GitHub Workflows Guide. **The prompt supports iteration**: run it multiple times to generate different question sets with varied scenarios and distractors.

---

## 1. Objective

Generate 150–175 multiple-choice certification exam questions that follows `exam-overview.md` GH-200 exam skill domain breakdown and weights

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

- **File**: `gh-200.md` (if exists in workspace; else skip deduplication check)
- **Purpose**: Avoid semantic duplication of previously generated questions

---

## 3. Question Generation Targets

### Total Output

- **Total Questions**: 162 questions (middle target within 150–175 range; can be adjusted 150–175 based on generation pass)
- **Distribution**: ~8–9 questions per topic file (allows for variable coverage by domain importance)

### Distribution by Topic (Recommended)

| Topic # | Topic Name | Questions | Rationale |
| ------- | ---------- | --------- | --------- |
| 1 | VS Code Extension | 5 | Tools & features (foundational) |
| 2 | Contextual Information | 11 | Core knowledge; 10 context types |
| 3 | Context Availability | 8 | Static vs. runtime; advanced concept |
| 4 | Workflow File Structure | 12 | Fundamental; many properties to test |
| 5 | Trigger Events | 12 | 26+ events; common exam focus |
| 6 | Custom Env Vars | 9 | Practical, scenario-heavy |
| 7 | Default Env Vars | 8 | Reference knowledge; practical use |
| 8 | Environment Protection | 9 | Enterprise/security focus |
| 9 | Artifacts | 11 | Practical; common workflows |
| 10 | Caching | 9 | Performance optimization; strategy |
| 11 | Workflow Sharing | 8 | Reusability; marketplace |
| 12 | Debugging | 9 | Troubleshooting; real-world scenarios |
| 13 | REST API | 8 | Automation; advanced use |
| 14 | Deployment Review | 6 | Enterprise/governance |
| 15 | Creating/Publishing Actions | 9 | Advanced; marketplace |
| 16 | Managing Runners | 9 | Operational; enterprise focus |
| 17 | Enterprise Features | 11 | Policy, groups, audit; high exam weight |
| 18 | Security & Optimization | 12 | OIDC, script injection, SHA pinning; critical |
| 19 | Troubleshooting | 9 | Problem-solving; real scenarios |
| **TOTAL** | | **162** | |

---

## 4. Question Difficulty & Cognitive Levels

### Distribution by Difficulty

| Difficulty | % | Count | Cognitive Level | Example Focus |
| ---------- | --- | ----- | --------------- | -------------- |
| **Easy** | 20% | 19 | Recall + Comprehension | "What does this context contain?" |
| **Medium** | 60% | 103 | Application + Analysis | "Which trigger event should you use for...?" |
| **Hard** | 20% | 40 | Synthesis + Evaluation | "Identify the scenario where this approach fails..." |

### Cognitive Level Definitions

- **Recall/Comprehension** (Easy): Direct from guide; minimal reasoning
- **Application** (Medium): Apply knowledge to new situation; select correct tool
- **Analysis** (Medium-Hard): Break apart scenario; identify root cause or best practice
- **Synthesis** (Hard): Combine multiple concepts; design/optimize a solution
- **Evaluation** (Hard): Judge trade-offs; identify pitfalls or best practice

---

## 5. Answer Type Distribution

Distribute answer types across the 162 questions:

| Answer Type | % of 162 | Count | Definition |
| ----------- | -------- | ----- | --------- |
| `one` | 55% | 89 | Exactly one correct answer |
| `many` | 26% | 42 | Multiple correct answers (2–4) |
| `all` | 12% | 19 | All options are correct (rare but valid) |
| `none` | 7% | 11 | "Which is NOT true...?" or no correct answer |

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

- **Check against `gh-200.md`** (if exists): Identify semantically similar questions
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

#### Minimum 18 questions on security topics

### Enterprise & Governance (Topics 17, 8, 14)

- Runner groups: permission model, scaling, cost optimization
- IP allow lists: use cases, configuration
- Secrets hierarchy: org vs. repo vs. environment scope
- Audit logging: event types, streaming, compliance
- Environment protection rules: required reviewers, wait timers, custom rules
- Deployment review workflows: pause, approve, monitor

#### Minimum 15 questions on enterprise topics**

### Real-World Scenarios (Topics 5, 9–12, 19)

- Workflow design: trigger selection, event payload
- Matrix builds: combinations, fail-fast, job indexing
- Artifact workflows: upload from build, download in another job
- Caching strategy: dependency selection, cache keys, miss handling
- Debugging: log streaming, RUNNER_DEBUG, slow step identification
- Troubleshooting: common errors, root cause, solutions

#### Minimum 60 questions scenario-based**

### Advanced & Synthesis (All topics)

- Trade-off decisions: "You need X, Y, Z. Which approach is best?"
- Architecture decisions: "Design a workflow that..."
- Optimization: "How can you reduce execution time?"
- Error recovery: "An artifact upload fails midway; what happens?"

#### Minimum 23 questions requiring synthesis/evaluation**

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

- **Primary**: `gh-200-new.md`
- **Alternative** (if appending): Append to `gh-200.md` with date marker and "NEW QUESTIONS (Generated [DATE])" section

### File Structure

```plaintext
# GH-200 Certification Exam — Practice Questions

## Questions (162 total — Iteration [N])

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

- **Total Questions**: 162 (within target 150–175 range)
- **Iteration Number**: [N] (1 for first run; increment for different question sets)
- **Difficulty Distribution**: 32 Easy, 97 Medium, 33 Hard
- **Answer Type Distribution**: 89 one, 42 many, 19 all, 11 none
- **Generation Date**: [Date]
- **Topics Covered**: All 19
- **Scenario-Based**: ~114 questions (70%+)
- **Average Question Length**: [X] words
- **Deduplication**: Checked against all previous iterations

```

---

## 14. Process Workflow (For Implementation)

1. **Preparation**

   - Load all 19 topic files
   - Load `gh-200.md` (if exists) for deduplication
   - Load/read ITERATION_NUMBER (default: 1) for question variation
   - Organize by topic; target 8–9 questions per topic

2. **Generation Pass 1: Easy (32 questions)**

   - Generate recall/comprehension questions
   - Direct from guide content
   - Distribute across topics
   - Vary scenarios by iteration number (seed RNG with iteration)

3. **Generation Pass 2: Medium (97 questions)**

   - Scenario-based; application/analysis
   - Mix of decision-making, bug-finding, best-practice selection
   - Prioritize enterprise, security, and real-world patterns
   - Randomize distractor order by iteration (shuffle wrong answers)

4. **Generation Pass 3: Hard (33 questions)**

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
   - Append to `gh-200-all-iterations.md` OR export to `gh-200-iteration-[N].md`
   - Document iteration number and generation timestamp

---

## 15. Defaults Applied & Assumptions

| Item | Default | Rationale |
| ---- | ------- | --------- |
| **Total Questions** | 162 (target 150–175) | Extended practice set; ~3 hours exam simulation; can adjust within range |
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
- **Deduplication**: Manual if `gh-200.md` doesn't exist in standard format
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

- `gh-200-iteration-1.md` (first run)
- `gh-200-iteration-2.md` (second run)
- `gh-200-iteration-3.md` (third run)
- Pros: Easy comparison; clear versioning; no duplicates
- Cons: Multiple files

#### Option B: Single Append File

- `gh-200-all-iterations.md` (combined)
- Format: `---` delimiter between iterations; metadata header for each iteration
- Pros: All questions in one place
- Cons: Harder to track which iteration a question came from

**Recommended**: Use **Option A**; include iteration info in output filename

### Deduplication Across Iterations

- **Before generating new iteration**: Check all previous `gh-200-iteration-*.md` files
- **Semantic comparison**: Avoid identical scenario structures
  - Example: "Your matrix is 3×2; how many jobs?" shouldn't repeat if Iteration 1 asked it
- **Variation measurement**: Spot-check 5–10 questions across iterations; confirm they're materially different

### Regeneration Workflow

1. **Increment iteration number**: `ITERATION_NUMBER += 1`
2. **Run generation process** with new iteration number
3. **Output to**: `gh-200-iteration-[N].md` (where N = iteration number)
4. **Verify differences**: Spot-check Q1, Q50, Q100+ across iterations for variation
5. **Archive previous**: Keep all iterations for test bank expansion

### Quality Assurance for Iterations

- **No semantic duplication**: Each iteration is meaningfully different
- **Difficulty distribution maintained**: Still 32 Easy / 97 Medium / 33 Hard per iteration
- **Answer type distribution maintained**: Still 55% one, 26% many, etc.
- **Topic coverage maintained**: Still 5–12 questions per topic
- **Focus area minimums met**: Still 18+ security, 15+ enterprise, 60+ scenarios

---

## 18. Success Criteria

✓ 162 questions generated per iteration (within 150–175 range)
✓ 0 duplicates within iteration; deduplication tracked across iterations
✓ 70%+ scenario-based (not pure recall)
✓ All questions answerable from guide alone
✓ Clear, unambiguous wording
✓ Plausible distractors for each question
✓ Balanced answer distribution (A/B/C/D ~40–41 each)
✓ Proper answer key with explanations
✓ Focus areas met (security: 18+, enterprise: 15+, scenarios: 60+, synthesis: 23+)
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
5. **Run deduplication** against `gh-200.md` before output
6. **Export statistics** automatically if possible (count by difficulty, answer type, etc.)
