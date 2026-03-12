# GitHub Actions (GH-200) Exam Question Generator

## Objective

Generate exam-like multiple-choice questions for the GH-200 certification using content from the GitHub-Workflows-Guide.md and exam-overview.md.

## Input Files

- Comprehensive reference material:
    - 01-GitHub-Actions-VS-Code-Extension.md
    - Contextual-Information.md
    - 03-Context-Availability-Reference.md
    - 04-Workflow-File-Structure.md
    - 05-Workflow-Trigger-Events.md
    - 06-Custom-Environment-Variables.md
    - 07-Default-Environment-Variables.md
    - 08-Environment-Protection-Rules.md
    - 09-Workflow-Artifacts.md
    - 10-Workflow-Caching.md
    - 11-Workflow-Sharing.md
    - 12-Workflow-Debugging.md
    - 13-Workflows-REST-API.md
    - 14-Reviewing-Deployments.md
    - 15-Creating-Publishing-Actions.md
    - 16-Managing-Runners.md
    - 17-GitHub-Actions-Enterprise.md
    - 18-Security-and-Optimization.md
    - 19-Common-Failures-Troubleshooting.md

- `gh-200.md` — Existing question database (for deduplication)

## Output Requirements

- **File**: Save as `gh-200-new.md` (or append to `gh-200.md` if preferred)
- **Format**: Use the standard question template (see below)
- **Layout**: All questions first, then all answers in a separate section

## Question Specifications

### Quantity

8 - 10 questions per a 100 lines of referance material.

### Difficulty & Depth

- **Cognitive Level**: Mix of application, analysis, and synthesis (avoid pure recall)
- **Scenario-based**: 70% should present real-world workflows or scenarios
- **Trick elements**: Include 1–2 plausible distractors in each question

### Answer Type Distribution

- `one` — 55% of questions (single correct answer)
- `many` — 26% of questions (multiple correct answers)
- `all` — 12% of questions (all options are correct)
- `none` — 7% of questions (no correct answer; "Which is NOT...?" style)

### Quality Rules

- **Deduplication**: Check `gh-200.md` for semantic duplicates; do not repeat concepts
- **Clarity**: Each question should be unambiguous; avoid double negatives
- **Plausibility**: All distractors should be realistically chosen by someone with partial knowledge
- **Exam alignment**: Mirror exam language and focus areas

## Question Template

```markdown
### Question [N] — [Domain]

**Difficulty**: Easy | Medium | Hard
**Answer Type**: one | many | all | none

[Question text with scenario if applicable]

- A) [Option A]
- B) [Option B]
- C) [Option C]
- D) [Option D]
- E) [Option E] _(optional if needed)_

**Topic Area**: [e.g., "Security & GITHUB_TOKEN", "Matrix Strategies", "Runner Groups"]

---

```

## Answers Section Format

```markdown
---

## Answer Key

| Q   | Answer(s) | Explanation                            | Difficulty |
| --- | --------- | -------------------------------------- | ---------- |
| 1   | B, D      | [Brief rationale citing guide section] | Medium     |
| 2   | All       | [Explanation]                          | Hard       |
| ... | ...       | ...                                    | ...        |
```

## Special Instructions

- **SHA Pinning**: Include at least two questions about immutable action SHAs
- **Enterprise**: Prioritize runner groups, IP allow lists, secrets hierarchy
- **Security**: Emphasize OIDC, script injection, trustworthy actions, attestations
- **Real scenarios**: When possible, base questions on actual workflow patterns (matrix, caching, artifacts)
- **Avoid trivial**: Don't ask "What does $GITHUB_WORKSPACE contain?" — ask how to leverage it
