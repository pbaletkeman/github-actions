# GH-200 Question Generator — Reference Material

This companion file supplements `question-prompt.md`. Attach it when fine-tuning, scaling to different question counts, diagnosing iteration issues, or performing maintenance on the question bank.

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

**Option B: Single Append File**

- `quiz\gh-200-all-iterations.md` (combined)
- Format: `---` delimiter between iterations; metadata header for each iteration
- Pros: All questions in one place
- Cons: Harder to track which iteration a question came from

**Recommended**: Use **Option A**; include iteration info in output filename.

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
- **Difficulty distribution maintained**: 20 Easy / 60 Medium / 20 Hard per iteration
- **Answer type distribution maintained**: 55% one, 26% many, 12% all, 7% none
- **Topic coverage maintained**: ~5 questions per topic (4–7 for hotspots)
- **Focus area minimums met**: 11+ security, 9+ enterprise, 37+ scenarios

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

## 25. Answer Distribution & Topic Breakdown

### Recommended Distribution by Topic & Answer Type

> **Note**: Based on 100-question target. Per-topic counts use the 55%/26%/12%/7% distribution ratios, rounded to produce valid integer allocations. Column totals are approximate guides — the exact split across all 100 questions is what matters (55 one, 26 many, 12 all, 7 none).

| Topic # | Topic Name | Total | `one` | `many` | `all` | `none` |
| ------- | ---------- | ----- | ----- | ------ | ----- | ------ |
| 1 | VS Code Extension | 4 | 2 | 1 | 0 | 1 |
| 2 | Contextual Information | 6 | 3 | 2 | 1 | 0 |
| 3 | Context Availability | 5 | 3 | 1 | 1 | 0 |
| 4 | Workflow File Structure | 7 | 4 | 2 | 1 | 0 |
| 5 | Trigger Events | 7 | 4 | 2 | 1 | 0 |
| 6 | Custom Env Vars | 5 | 3 | 1 | 1 | 0 |
| 7 | Default Env Vars | 5 | 3 | 1 | 1 | 0 |
| 8 | Environment Protection | 5 | 3 | 1 | 1 | 0 |
| 9 | Artifacts | 5 | 3 | 1 | 1 | 0 |
| 10 | Caching | 5 | 3 | 1 | 1 | 0 |
| 11 | Workflow Sharing | 5 | 3 | 1 | 1 | 0 |
| 12 | Debugging | 5 | 3 | 1 | 1 | 0 |
| 13 | REST API | 5 | 3 | 1 | 1 | 0 |
| 14 | Deployment Review | 4 | 2 | 1 | 0 | 1 |
| 15 | Creating/Publishing Actions | 5 | 3 | 1 | 1 | 0 |
| 16 | Managing Runners | 5 | 3 | 1 | 1 | 0 |
| 17 | Enterprise Features | 5 | 3 | 1 | 1 | 0 |
| 18 | Security & Optimization | 7 | 4 | 2 | 1 | 0 |
| 19 | Troubleshooting | 5 | 3 | 1 | 1 | 0 |
| **TOTAL** | | **100** | **~58** | **~24** | **~16** | **~2** |

> **Adjustment note**: The per-topic rounding above produces ~58 `one`, ~24 `many`, ~16 `all`, ~2 `none`. Adjust to hit the exact targets of 55 `one`, 26 `many`, 12 `all`, 7 `none` by shifting a few questions:
> - Convert ~3 excess `one` answers to `many` in Topics 3–16
> - Convert ~4 excess `all` answers to `none` in Topics 3–16
> - This balances the final 100-question distribution correctly.

---

## 26. Glossary & Key Terminology

| Term | Definition |
| ---- | ---------- |
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

| Target | Simple Adjustments | Difficulty Split |
| ------ | ------------------ | ---------------- |
| **200 questions** | ×2 per topic; keep 20/60/20 split | 40 Easy / 120 Medium / 40 Hard |
| **50 questions** | ×0.5 per topic; round hotspots up | 10 Easy / 30 Medium / 10 Hard |
| **300 questions** | ×3 per topic; reduce lowest-impact scenarios | 60 Easy / 180 Medium / 60 Hard |

### Adjusting Difficulty Distribution

**Use case**: Organization prefers harder questions for advanced practitioners.

- **Default**: 20% Easy / 60% Medium / 20% Hard
- **Advanced Variant**: 10% Easy / 50% Medium / 40% Hard (shift 10 questions up one tier)

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

## 29. Integration & Downstream Use Cases

### Exam Platform Integration

```
Quiz → Export → LMS (Canvas, Moodle) → Scoring/Analytics
  ↓
Question metadata (topic, difficulty, answer type) → Platform dashboard
  ↓
Candidate performance by topic → Identify knowledge gaps → Recommend remediation
```

### Learning Path Mapping

- Easy questions → Onboarding learners (Week 1–2)
- Medium questions → Intermediate labs (Week 3–4)
- Hard questions → Capstone project (Week 5–6)

### Pre-Assessment & Diagnostic

- Run 20-question subset (random mix) → Quick readiness assessment
- If <50% pass: recommend foundational course first
- If >80% pass: skip to advanced topics

### Version Control & Archival

**File Naming Strategy**:

```
quiz/gh-200-iteration-1.md          (Initial generation)
quiz/gh-200-iteration-2.md          (Second run, different questions)
quiz/gh-200-iteration-2-refined.md  (After feedback refinement)
quiz/gh-200-live-v1.0.md            (Production release)
```

**Archive & Audit Trail**:

- Keep all iterations for historical reference
- Document when questions were added/removed and why
- Maintain copy of `exam-overview.md` used for generation (in case spec changes)

---

## 30. Maintenance & Evolution

### Ongoing Maintenance Schedule

| Frequency | Task | Owner |
| --------- | ---- | ----- |
| **Monthly** | Monitor candidate performance metrics; identify outlier questions | QA Lead |
| **Quarterly** | Update questions for new GitHub Actions features; retire outdated scenarios | Content SME |
| **Semi-annually** | Full review; incorporate user feedback; generate new iteration | Exam Committee |
| **Annually** | Validate against updated `exam-overview.md`; confirm topic weights still accurate | Certification Board |

### Deprecation & Sunset

- Questions become stale when a feature changes significantly (e.g., OIDC becomes default, old secret-based scenarios obsolete)
- Mark questions with `deprecated: true` tag; exclude from live exams but keep for reference
- Replacement questions should be created before the sunset date
