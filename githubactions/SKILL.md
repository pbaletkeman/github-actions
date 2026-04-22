# Certification Exam Question Generator

## Purpose

Systematically generate, review, and optimize high-quality multiple-choice certification exam questions. This skill provides a reusable framework for creating rigorous, scenario-based questions with proper difficulty distribution, cognitive load balancing, and pedagogical rigor.

**Use this skill when you need to:**
- Generate 50–500 multiple-choice exam questions
- Review and improve existing question banks
- Ensure consistency in difficulty, format, and quality
- Create deduplication and variation strategies
- Design question distributions by topic, difficulty, and answer type
- Develop answer keys and rubrics

---

## Scope & Applicability

### ✅ Perfect For

- Certification exam preparation (AWS, Azure, GCP, GitHub Actions, Kubernetes, etc.)
- Internal knowledge assessments
- Technical interview prep
- Educational curriculum validation
- Learning analytics and competency testing

### ⚠️ Not For

- True/false quizzes (designed for multiple-choice only)
- Real-time assessment during exams
- Automated grading (focus is on question creation, not testing infrastructure)

---

## Core Workflow: 3 Modes

### Mode 1: **GENERATE** — Create New Questions from Scratch

**When to use**: Starting a new question bank or generating additional iterations.

**Input Requirements:**
- **Exam context**: Name, domain, certification body (e.g., "GitHub Actions GH-200")
- **Source material**: List of 19–30 topic reference files with brief descriptions
- **Question count**: Target (e.g., 100, 200)
- **Distribution parameters** (see section below)
- **Domain constraints**: Min questions on security/enterprise/advanced topics
- **Output format**: Markdown location and naming convention

**Process Steps:**

1. **Extract & Validate Source Material**
   - Read all reference files (topic 1–N)
   - Create file inventory with summaries
   - Identify coverage gaps and interdependencies
   - Generate confidence score (0–100%) based on material clarity

2. **Plan Distribution** _(see Distribution Parameters below)_
   - Allocate questions by topic (e.g., 5–7 per topic)
   - Confirm totals match target count
   - Ensure security/enterprise minimums are met
   - Verify cross-topic requirements

3. **Generate & Validate Each Question** (No Inline Answers)
   - Write scenario (if applicable; ~2–3 sentences)
   - Compose clear, unambiguous question
   - Create 4–5 plausible options (A, B, C, D, [E])
   - **DO NOT include inline answer markers** — answers go only in the centralized Answer Key section at EOF
   - Apply distractor strategy (see section below)
   - Verify against quality checklist
   - Save correct answer(s) separately for Step 5 (Answer Key generation)

4. **Deduplication Check** _(optional but recommended)_
   - If existing question bank exists, compare for semantic duplication
   - Refactor or retire redundant questions
   - Vary scenarios and answer positions to maximize diversity

5. **Generate Answer Key at EOF** (Always at End of File)
   - After all N questions are listed, add `---` section divider
   - Create `## Answer Key` section at end of file
   - Use markdown table format (see Answer Key Format below)
   - Write 2–3 sentence explanations per question
   - Include source file reference and difficulty rating
   - Ensure NO answers appear inline in question blocks above

6. **Output & Package**
   - Save as `[exam-name]-iteration-[N].md`
   - Verify structure: Questions section (Q1–QN with options only) → `---` divider → `## Answer Key` section at EOF
   - Include executive summary (parameters, totals, distribution) at top
   - Document any deviations from plan

---

### Mode 2: **REVIEW** — Improve & Optimize Existing Questions

**When to use**: Quality assurance, consistency alignment, or raising question difficulty.

**Input Requirements:**
- Existing question file (markdown)
- Review criteria (see Quality Checklist below)
- Target improvements (e.g., "raise difficulty to 30% Hard", "improve distractors")

**Process Steps:**

1. **Assess Current State**
   - Count questions, broken by difficulty and answer type
   - Identify formatting inconsistencies
   - Spot weak, vague, or ambiguous questions
   - Flag distractors that are implausible or too obvious

2. **Apply Quality Checklist**
   - Rewrite vague language (replace "usually", "might" → be specific)
   - Add missing scenarios (should be 70%+ scenario-based)
   - Fix double negatives and confusing phrasing
   - Verify each option is defensible

3. **Strengthen Distractors**
   - Rate each wrong answer on plausibility (would 50% knowledge pick it?)
   - Replace implausible distractors
   - Apply distractor strategy (partial knowledge, misconception, off-by-one, alternative tool, close-but-wrong)

4. **Rebalance Difficulty** _(if needed)_
   - Identify easy questions that should be medium (add complexity)
   - Find hard questions that are too obscure (refocus on core concepts)
   - Rewrite to match target distribution

5. **Consolidate Answers to EOF Answer Key**
   - Remove any inline answer markers from questions (search for `**Answer**:` or similar)
   - Move all answers to centralized `## Answer Key` section at EOF
   - Ensure explanations cite specific source sections
   - Verify no contradictions between questions
   - Add rationale for why distractors are wrong
   - Verify Answer Key is positioned at END OF FILE after all questions

6. **Package & Archive**
   - Rename to `[exam]-iteration-[N]-reviewed.md` (increment N)
   - Document change log (questions improved, distractors replaced, etc.)

---

### Mode 3: **HYBRID** — Generate + Review in Single Pass

**When to use**: Limited time, high quality bar, or expert refinement.

**Process**: Combine GENERATE steps 1–4, then immediately apply REVIEW steps 1–5. Output includes both raw and refined versions so differences are visible.

---

## Distribution Parameters Template

Copy and customize for your exam context:

```yaml
Exam Metadata:
  name: "[Exam Name & Code]"
  description: "[1–2 sentence overview]"
  reference_files: "[List topic files 1–N]"

Question Totals:
  target_count: 100
  estimate_scenarios_pct: 70
  min_security_questions: 11
  min_enterprise_questions: 9
  min_crossTopicQuestions: 5

Difficulty Distribution:
  easy: { pct: 20, count: 20 }
  medium: { pct: 60, count: 60 }
  hard: { pct: 20, count: 20 }

Answer Type Distribution:
  one: { pct: 55, count: 55 }
  many: { pct: 26, count: 26 }
  all: { pct: 12, count: 12 }
  none: { pct: 7, count: 7 }

Topic Allocation:
  Topic 1: 4 questions
  Topic 2: 6 questions
  ...
  Total: 100

Special Requirements:
  - Minimum 1 question on [concept X]
  - Minimum 1 question on [concept Y]
  - Special focus on [domain Z]
```

---

## Question Template

```markdown
### Question [N] — [Domain]

**Difficulty**: Easy | Medium | Hard
**Answer Type**: one | many | all | none
**Topic**: [e.g., "Topic 7: Default Environment Variables"]

**Scenario** _(if applicable)_:
[2–3 sentences setting up a realistic situation; omit if pure recall question]

**Question**:
[Clear, unambiguous phrasing; avoid "choose the best", "sometimes", "usually"]

- A) [Plausible option]
- B) [Plausible option or correct answer — marked only in Answer Key]
- C) [Plausible distractor]
- D) [Plausible distractor]
- E) [Optional fifth distractor]

---
```

**⚠️ Important:** Do NOT include `**Answer**: [X]` or `**Explanation**:` inline. All answers go in the `## Answer Key` section at EOF.

### Key Template Rules

- **For `many` type**: Add **(Select all that apply)** after question text
- **For `none` type**: Start with "Which is NOT true..." or equivalent
- **No double negatives**: Avoid "You should NOT avoid NOT using..."
- **Specific phrasing**: "You are required to..." instead of "You might..."
- **Code blocks**: Keep YAML/code snippets to 5–15 lines; highlight the tested concept

---

## Distractor Strategy

Create wrong answers that capture **realistic misconceptions**, not obvious mistakes.

| Type | Example | When to Use |
|------|---------|------------|
| **Partial Knowledge** | Correct concept, wrong context | Most distractors; captures ~50% understanding |
| **Common Misconception** | "Secrets are always in env vars" | Security concepts; address known pitfalls |
| **Off-by-One / Precedence** | Wrong behavior order or priority | Workflow structure, context availability |
| **Alternative but Wrong Tool** | "Use workflow_dispatch instead of push" | Event selection, tool choice scenarios |
| **Close but Not Best** | "This works but isn't recommended" | Best practices, optimization topics |

---

## Quality Assurance Checklist

✅ **Before finalizing each batch of 10–20 questions, verify:**

- [ ] **Clarity**: Each question has one unambiguous interpretation
- [ ] **Answerability**: Can be answered using only source material (no external research needed)
- [ ] **Realism**: Scenario reflects actual job responsibilities or real-world situations
- [ ] **Grammar**: No typos, awkward phrasing, or double negatives
- [ ] **Distractor Plausibility**: A candidate with 50% knowledge would consider each wrong answer
- [ ] **Difficulty Match**: Question cognitive demand matches stated difficulty level
- [ ] **No Patterns**: Correct answers not clustered (A, A, A...) — randomize across questions
- [ ] **Answer Type Distribution**: On track for target percentages
- [ ] **Deduplication**: No semantic overlap with previously generated questions
- [ ] **Formatting**: Consistent heading levels, code block syntax, spacing
- [ ] **Answer Placement**: NO inline answers in question blocks; all answers centralized in `## Answer Key` section at EOF ✅

---

## Cognitive Levels (Bloom's Taxonomy)

Align questions to cognitive demand:

| Level | % Expected | Example Task | Sample Phrasing |
|-------|-----------|--------------|-----------------|
| **Recall/Comprehension** (Easy) | 20% | "What does X contain?" | "Which default variable holds..." |
| **Application** (Medium) | 40% | "Use X to solve Y" | "You need to deploy 100 apps; which runner strategy..." |
| **Analysis** (Medium-Hard) | 20% | "Why does X fail in Y scenario?" | "A workflow times out; which factor is likely..." |
| **Synthesis/Evaluation** (Hard) | 20% | "Design/optimize solution" | "Your team runs 500 workflows/day; which approach minimizes..." |

---

## Answer Key Format (Always at EOF)

**Position**: The `## Answer Key` section must always appear at the END of the file, after all questions and a `---` divider.

**Structure**:

```markdown
---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | B | Explains why B is correct and why A, C, D are wrong (1–2 sentences); cite specific section | Topic 7 | Medium |
| 2 | B, D | Explains both B and D; contrasts against A, C | Topic 9 | Hard |
| 3 | All | Explains why all options are defensibly correct (rare; use sparingly) | Topic 4 | Easy |
| ... | ... | ... | ... | ... |
```

---

## Special Focus Areas (Domain-Specific Emphasis)

When generating, ensure **minimum representation**:

- **Security & Tokenization** (11+ questions): Token lifecycle, OIDC, SHA pinning, script injection, secret handling
- **Enterprise & Governance** (9+ questions): Access control, runner groups, secrets hierarchy, audit logging, deployment reviews
- **Real-World Scenarios** (37+ questions): Workflow design, matrix builds, artifacts, caching, debugging, troubleshooting
- **Advanced Synthesis** (14+ questions): Trade-off analysis, architecture decisions, optimization, error recovery

---

## Iteration & Variation Strategy

To generate **different question sets** on subsequent runs:

1. **Vary scenario context**: If one run uses "CI pipeline deploying microservices", next uses "mobile app release pipeline"
2. **Rotate answer positions**: Shuffle correct answer location (A, B, C, D vary across iterations)
3. **Swap distractors**: For same question scenario, replace some wrong answers with alternative misconceptions
4. **Adjust difficulty**: Raise medium → hard; simplify hard → medium
5. **Deduplication database**: If iterating, reference previous `quiz/[exam].md` to avoid semantic duplication

---

## Invocation Examples

### Example 1: Generate GH-200 Questions
```
User: "Generate 100 exam questions for GitHub Actions GH-200 certification.
Use the 19 reference files in github-actions/ folder.
Follow the distribution in question-prompt.md.
Output to quiz/gh-200-iteration-1.md"
```

**Skill Response**:
1. Extracts from 19 topic files
2. Plans distribution (4 questions × 19 topics + adjustments = 100)
3. Generates questions with scenarios, options, answers
4. Checks for deduplication
5. Validates answer key
6. Saves to specified location

---

### Example 2: Review & Improve Existing Bank
```
User: "Review quiz/gh-200.md. Improve weak distractors,
eliminate questions scoring <50% accuracy,
rebalance to 25% Hard instead of 20%.
Create az quiz/gh-200-iteration-2-reviewed.md"
```

**Skill Response**:
1. Assesses current state (distribution, quality metrics)
2. Applies quality checklist, rewrites vague language
3. Strengthens distractors (replace implausible ones)
4. Rebalances difficulty (move 5 medium → hard, rewrite to increase complexity)
5. Generates answer key with explanations
6. Documents changes (e.g., "12 distractors replaced, 3 questions rewritten")

---

### Example 3: Generic Exam (New Domain)
```
User: "I'm building a Kubernetes CKAD practice bank.
Create 150 questions covering 8 topics.
Use GENERATE mode.
60% scenario-based, 20% hard difficulty.
Output to ckad-practice-v1.md"
```

**Skill Response**: Adapts the framework for Kubernetes, validates source material, plans distribution, generates questions with Kubernetes-specific scenarios.

---

## Integration with Other Skills

- **`agent-customization`**: Customize your personal copy of this skill (e.g., add domain-specific rules, preferred distractors)
- **`specification-agent`**: Use to write detailed exam specifications before calling this skill
- **Data analysis tools**: Post-generation, analyze question performance if you have test result data

---

## Deliverables & Artifacts

Each mode produces:

1. **Question Bank** (markdown): All 100–500 questions formatted consistently
2. **Answer Key** (markdown table): Explanations, sources, difficulty ratings
3. **Metadata & Summary** (YAML or frontmatter): Distribution, parameters, deviations
4. **Change Log** (if review mode): Questions improved, distractors replaced, difficulty adjustments
5. **Quality Report** (optional): Pass/fail checklist, gaps, recommendations

---

## Best Practices & Tips

- **Start small**: Generate 20–30 questions, review, refine process, then scale to 100+
- **Batch validation**: After every 20 questions, spot-check against quality checklist
- **Reference discipline**: Always cite source material; trace every question back to a file/section
- **Scenario variety**: Mix 3–4 different narrative contexts (not all "you are a DevOps engineer...")
- **Distractor freshness**: When iterating, don't reuse same wrong answers; create new misconceptions
- **Answer key first**: Some experts draft the answer key *before* writing options—this ensures clarity
- **Peer review**: If possible, have a subject matter expert review 10% of questions for accuracy

---

## When to Escalate

- **Low confidence on source material** (<66%): Pause generation; research/spike first before creating questions
- **Domain expertise needed**: If you lack deep knowledge of the exam domain, collaborate with SMEs
- **Deduplication conflicts**: If 20%+ of questions conflict with existing bank, review scope/focus

---

## Success Criteria

✅ **Exam question bank is production-ready when:**
- All 100–N questions pass quality checklist
- Distribution meets targets (difficulty, answer types, topics)
- Answer key is complete and cited
- Distractors have >70% plausibility rating
- No semantic duplication with existing banks
- >70% scenario-based (real-world context)
- Security/enterprise/advanced minimums met
- Formatting is consistent
- Question clarity score: 9/10 or higher

---

## Related Customizations to Try Next

Once you've mastered question generation, consider creating:
- **Answer rubric evaluator**: Auto-grade candidate responses
- **Question performance tracker**: Measure which topics have high/low accuracy
- **Difficulty calibrator**: Adjust difficulty based on actual test performance data
- **Domain-specific variant**: Create a specialized version for your certification
