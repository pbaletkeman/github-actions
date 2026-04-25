---
mode: agent
description: "Terraform Associate (004) question bank generator — produces 1000 unique questions across 10 iterations × 8 batches. Run once per (ITERATION, BATCH) pair."
---

# Terraform Associate Exam — Question Bank Generator

## How to Use This Prompt

This prompt is designed to be run **once per (ITERATION, BATCH) pair** to avoid response-size errors.

- **10 iterations × 8 batches = 80 total runs**
- **Each run generates ~12–13 questions**
- **Grand total: ~1000 unique questions**

**Set BOTH values before each run:**

> **CURRENT ITERATION**: `1`  ← change this to 1–10 before each run
> **CURRENT BATCH**: `1`  ← change this to 1–8 before each run

---

## Iteration Definitions — Question Style per Iteration

Each iteration applies a **different question style** to the same source material.
This guarantees 1000 unique questions with no conceptual overlap across iterations.

| Iteration | Question Style Focus | Example Framing |
|-----------|---------------------|-----------------|
| 1 | **Foundational recall** — define terms, name commands, identify blocks | "What is the purpose of…" / "Which block is used to…" |
| 2 | **Behavioral prediction** — what happens when code runs | "Given this config, what does `terraform plan` output?" |
| 3 | **HCL interpretation** — read a snippet and identify output/effect | "What is the value of `local.name` after this runs?" |
| 4 | **Error identification** — spot the mistake in config or workflow | "Which line causes a validation error?" |
| 5 | **Comparison and contrast** — distinguish between two similar concepts | "What is the difference between `count` and `for_each`?" |
| 6 | **Ordering and sequence** — correct workflow or dependency order | "Which step must occur before `terraform apply`?" |
| 7 | **Best practice and recommendation** — choose the correct approach | "Which approach is recommended when managing sensitive values?" |
| 8 | **Edge cases and gotchas** — unusual or less-obvious behaviour | "What happens if the state file is deleted before `plan`?" |
| 9 | **HCP Terraform focus** — platform features, workspaces, governance | "Which HCP Terraform feature prevents concurrent runs?" |
| 10 | **Exam-style scenarios** — multi-sentence real-world context questions | "A team notices drift after a manual change. Which command…?" |

---

## Batch Definitions — Objective and Source Files per Batch

| Batch | Exam Objective | Source Files | Target Q Count |
|-------|---------------|--------------|----------------|
| 1 | 1 — IaC Concepts | `terraform/learning/prompt01-what-is-iac.md` | 12 |
| 2 | 2 — Terraform Fundamentals | `terraform/learning/prompt02-providers-plugin-model.md`, `prompt03-terraform-state.md` | 13 |
| 3 | 3 — Core Workflow & CLI | `terraform/learning/prompt04-core-workflow-cli.md` | 13 |
| 4 | 4a — Resources, Data & Dependencies | `terraform/learning/prompt05-resource-data-blocks.md`, `prompt09-dependencies-lifecycle.md` | 13 |
| 5 | 4b — Variables, Outputs, Types & Functions | `terraform/learning/prompt06-variables-locals-outputs.md`, `prompt07-complex-types-collections.md`, `prompt08-builtin-functions-expressions.md` | 13 |
| 6 | 4c — Custom Conditions & Sensitive Data | `terraform/learning/prompt10-custom-conditions-sensitive-data.md` | 12 |
| 7 | 5 — Modules + 6 — State Backends | `terraform/learning/prompt11-terraform-modules.md`, `prompt12-state-backends-locking-remote-state.md` | 13 |
| 8 | 7 — Maintaining Infra + 8 — HCP Terraform | `terraform/learning/prompt13-importing-infrastructure-state-inspection.md`, `prompt14-hcp-terraform-workspaces-runs-state.md`, `prompt15-hcp-terraform-governance-security-advanced.md` | 13 |

---

## Instructions for the Current Run

1. **Identify** the current ITERATION (1–10) and BATCH (1–8) set at the top of this prompt.
2. **Read** only the source file(s) listed for the current batch in the Batch Definitions table above.
3. **Read** `terraform/plan-terraformAssociateExamOverview.prompt.md` to confirm objective scope and category weights.
4. **Scan** all existing files in `terraform/learning/questions/` to identify questions already written — **do not duplicate any question concept, wording, or scenario from any existing file**.
5. **Reference** `actions-source-material/gh-200-iteration-1.md` for the required output format (header metadata + per-question structure).
6. **Apply the iteration style** from the Iteration Definitions table — every question in this run must follow that style. This is the primary mechanism for ensuring 1000 unique questions.
7. **Generate exactly** the target question count shown in the Batch Definitions table — no more, no fewer.
8. **Apply the difficulty split** per run: ~20% Easy / ~60% Medium / ~20% Hard.
9. **Apply the answer-type split** per run: ~75% `one` / ~24% `many` / ~1% `none`.
10. **Save the output** as a new file:
    `terraform/learning/questions/tf-associate-iter{I}-batch{B}.md`
    where `{I}` = current iteration and `{B}` = current batch
    (e.g., iteration 3, batch 5 → `tf-associate-iter3-batch5.md`)

---

## Required Output File Format

Match the structure from `actions-source-material/gh-200-iteration-1.md` exactly:

```markdown
# Terraform Associate (004) — Question Bank Iter {I} Batch {B}

**Iteration**: {I}
**Iteration Style**: {style name from Iteration Definitions table}
**Batch**: {B}
**Objective**: {Objective name from Batch Definitions table}
**Generated**: {today's date}
**Total Questions**: {count}
**Difficulty Split**: {X} Easy / {Y} Medium / {Z} Hard
**Answer Types**: {a} `one` / {b} `many` / {c} `none`

---

## Questions

---

### Question {seq} — {Short Topic Label}

**Difficulty**: Easy | Medium | Hard
**Answer Type**: one | many | none
**Topic**: {specific concept being tested}

**Question**:
{Question text}

- A) {option}
- B) {option}
- C) {option}
- D) {option}

**Answer**: {letter(s)}

**Explanation**:
{1–3 sentence explanation referencing official Terraform behaviour or HCL syntax}

---
```

---

## Quality Rules

- Questions must test **exam-relevant knowledge** only — no trivial trivia.
- Every question must clearly reflect the **iteration style** — a "Behavioral prediction" question must not read like a "Foundational recall" question.
- Every `many` answer type must have **exactly 2 correct answers** unless the question stem specifies otherwise.
- All `none` answer types must include a clearly stated correct answer in the explanation.
- Distractors must be plausible — avoid obviously wrong options.
- HCL code snippets in questions must be **syntactically valid**.
- Do **not** repeat a concept already covered by another question in the same run.
- Do **not** duplicate any question from existing files in `terraform/learning/questions/`.
- The **iteration style is the uniqueness guarantee** — lean into it. A question testing the same concept as a prior iteration is acceptable only if the style/framing is genuinely different.

---

## Run Tracking Reference

Use this table to track completion. Each cell = one run (80 total).

|   | B1 | B2 | B3 | B4 | B5 | B6 | B7 | B8 |
|---|----|----|----|----|----|----|----|----|
| **I1** | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] |
| **I2** | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] |
| **I3** | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] |
| **I4** | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] |
| **I5** | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] |
| **I6** | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] |
| **I7** | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] |
| **I8** | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] |
| **I9** | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] |
| **I10** | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] |
