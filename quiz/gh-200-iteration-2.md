# GitHub Actions GH-200 Certification Exam — Question Bank (Iteration 2)

**Iteration**: 2

**Generated**: 2026-03-19

**Total Questions**: 100

**Difficulty Split**: 20 Easy / 60 Medium / 20 Hard

**Answer Types**: 72 `one` / 28 `many`

---

## Questions

---

### Question 1 — VS Code Extension

**Difficulty**: Easy
**Answer Type**: one
**Topic**: VS Code Extension

**Question**:
A developer opens a workflow YAML file in VS Code and notices no syntax validation or autocomplete is offered for GitHub Actions keywords. What is the most likely cause?

- A) The workflow file uses tabs instead of spaces for indentation.
- B) The YAML language extension is not installed.
- C) The VS Code GitHub Actions extension requires an internet connection to validate and it is disconnected.
- D) The workflow file has no `on:` trigger defined.

---

### Question 2 — VS Code Extension

**Difficulty**: Medium
**Answer Type**: one
**Topic**: VS Code Extension

**Question**:
A developer types `${{` in the `env:` section of a workflow step and the VS Code GitHub Actions extension shows an autocomplete list. Which of the following statement about that autocomplete list is accurate?

- A) It shows only context names available in `steps[*].env`, such as `github`, `env`, and `secrets`.
- B) It shows all context names regardless of where in the file the cursor is.
- C) It only appears when the workflow has been saved to a `.github/workflows/` directory.
- D) It queries the GitHub API to retrieve the live secret names for the repository.

---

### Question 3 — VS Code Extension

**Difficulty**: Medium
**Answer Type**: one
**Topic**: VS Code Extension

**Question**:
A team member hovers over `uses: actions/setup-node@v4` in their workflow file using the VS Code GitHub Actions extension. What information does the hover tooltip display?

- A) The last three releases of the action from the GitHub Marketplace.
- B) The runtime logs from the most recent execution of that action.
- C) The action metadata from its `action.yml` file, including its inputs, outputs, and description.
- D) A security advisory report showing known CVEs for the action version.

---

### Question 4 — VS Code Extension

**Difficulty**: Hard
**Answer Type**: many
**Topic**: VS Code Extension

**Question**:
Which of the following capabilities does the VS Code GitHub Actions extension provide? *(Select all that apply.)*

- A) Validating that permission scope names used in `permissions:` blocks are recognized scopes.
- B) Highlighting when a context variable is referenced in a workflow key where that context is not available.
- C) Executing the workflow on a local Docker daemon when triggered from the editor.
- D) Providing autocomplete suggestions for trigger event names under the `on:` key.
- E) Displaying the CI/CD status badge for the workflow in the editor gutter.

---

### Question 5 — Contextual Information

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contextual Information

**Question**:
A workflow is triggered by a `push` event. Which expression correctly retrieves the full Git SHA of the commit that triggered the run?

- A) `${{ github.ref_name }}`
- B) `${{ github.sha }}`
- C) `${{ runner.workspace }}`
- D) `${{ github.run_id }}`

---

### Question 6 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contextual Information

**Question**:
A workflow step needs to determine who originally triggered the workflow run versus who re-ran it (these can be different people). Which two context properties, respectively, provide the original actor and the re-run actor?

- A) `github.actor` and `github.triggering_actor`
- B) `github.sender` and `github.actor`
- C) `github.actor` and `github.workflow`
- D) `github.triggering_actor` and `github.run_id`

---

### Question 7 — Contextual Information

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Contextual Information

**Question**:
A step needs to reference output values produced by a previous step in the same job. Given that the prior step has `id: build-step`, which of the following expressions correctly accesses its output named `artifact-name`? *(Select all that apply.)*

- A) `${{ steps.build-step.outputs.artifact-name }}`
- B) `${{ steps['build-step'].outputs['artifact-name'] }}`
- C) `${{ job.steps.build-step.artifact-name }}`
- D) `${{ env.build-step_artifact-name }}`

---

### Question 8 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contextual Information

**Question**:
The `strategy` context is available inside jobs that define a `strategy:` block. Which property from that context tells you the **total** number of jobs in the current matrix expansion?

- A) `strategy.job-index`
- B) `strategy.max-parallel`
- C) `strategy.job-total`
- D) `strategy.fail-fast`

---

### Question 9 — Contextual Information

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Contextual Information

**Question**:
A workflow uses `workflow_call` and receives several inputs from the calling workflow. Which of the following statements about the `inputs` context in called workflows are correct? *(Select all that apply.)*

- A) Inputs declared as `type: boolean` are accessible as the string `"true"` or `"false"`, not as native booleans, in expression comparisons.
- B) `inputs` context properties are available in `jobs.<id>.if` conditions.
- C) `inputs` context is available in the `run-name` key.
- D) The caller can pass any undeclared input and the called workflow will receive it via `inputs.extraData`.

---

### Question 10 — Contextual Information

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Contextual Information

**Question**:
A job output is defined as:

```yaml
outputs:
  version: ${{ steps.tag-step.outputs.version }}
```

A downstream job that depends on this job via `needs: release` wants to reference this value. Which expression is correct?

- A) `${{ steps.tag-step.outputs.version }}`
- B) `${{ needs.release.outputs.version }}`
- C) `${{ jobs.release.outputs.version }}`
- D) `${{ needs.outputs.release.version }}`

---

### Question 11 — Context Availability Reference

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Context Availability Reference

**Question**:
A workflow engineer wants to set `run-name` dynamically to show the branch that triggered the run. Which expression is valid in the `run-name` key?

- A) `${{ runner.os }} run on ${{ github.ref_name }}`
- B) `${{ github.ref_name }} – ${{ github.workflow }}`
- C) `${{ secrets.DEPLOY_ENV }} deployment`
- D) `${{ matrix.os }} run`

---

### Question 12 — Context Availability Reference

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Context Availability Reference

**Question**:
A job conditional is written as:

```yaml
jobs:
  deploy:
    if: ${{ runner.arch == 'X64' }}
```

What happens when this workflow is evaluated?

- A) The conditional evaluates correctly, allowing the job to run only on x64 runners.
- B) The expression fails validation because `runner` context is not available at `jobs.<id>.if`.
- C) The expression is silently ignored and the job always runs.
- D) GitHub replaces `runner.arch` with an empty string, causing the comparison to return false.

---

### Question 13 — Context Availability Reference

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Context Availability Reference

**Question**:
Which of the following contexts are available in `jobs.<id>.steps[*].run`? *(Select all that apply.)*

- A) `secrets`
- B) `runner`
- C) `matrix`
- D) `strategy`
- E) `needs`

---

### Question 14 — Context Availability Reference

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Context Availability Reference

**Question**:
A developer writes the following workflow snippet:

```yaml
jobs:
  build:
    container:
      image: node:${{ matrix.node-version }}
    strategy:
      matrix:
        node-version: [18, 20]
```

Is the `matrix` context available in `jobs.<id>.container`?

- A) Yes – `matrix` context is available everywhere inside a job definition.
- B) No – `jobs.<id>.container` only supports `github`, `inputs`, and `vars` contexts; `matrix` is not available there.
- C) Yes – but only when `fail-fast: false` is set in the strategy.
- D) No – container images must be hardcoded strings; no expression evaluation is supported.

---

### Question 15 — Context Availability Reference

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Context Availability Reference

**Question**:
Which of the following contexts are **not** available in `jobs.<id>.outputs`? *(Select all that apply.)*

- A) `secrets`
- B) `runner`
- C) `job`
- D) `strategy`
- E) `matrix`

---

### Question 16 — Workflow File Structure

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Workflow File Structure

**Question**:
What is the purpose of `defaults.run.working-directory` at the workflow level?

- A) It sets the default shell for all run steps.
- B) It sets the working directory used by all `run:` steps that do not override it.
- C) It specifies the repository directory that `actions/checkout` will clone into.
- D) It restricts which directories a step can read or write.

---

### Question 17 — Workflow File Structure

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Workflow File Structure

**Question**:
By default, jobs in a workflow run in which manner?

- A) Sequentially, in the order they are listed in the file.
- B) Sequentially, but only after a manual approval gate is passed.
- C) In parallel, unless a `needs:` dependency is specified.
- D) In parallel, sharing a single runner to reduce cost.

---

### Question 18 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow File Structure

**Question**:
A workflow uses this concurrency block:

```yaml
concurrency:
  group: ${{ github.workflow }}-${{ github.ref }}
  cancel-in-progress: true
```

What happens when a new push is made to `main` while a workflow triggered by the previous push is still running?

- A) The new run waits in a queue until the old run completes.
- B) Both runs execute in parallel under the same concurrency group key.
- C) The currently running workflow is cancelled and the new run starts.
- D) The new run is rejected with an error about concurrent execution.

---

### Question 19 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Workflow File Structure

**Question**:
A matrix strategy is defined as:

```yaml
strategy:
  matrix:
    os: [ubuntu-latest, windows-latest, macos-latest]
    node: [18, 20]
  fail-fast: false
  max-parallel: 2
```

Which of the following statements are accurate? *(Select all that apply.)*

- A) With `fail-fast: false`, if one matrix combination fails, all remaining combinations continue to run.
- B) No more than 2 matrix job instances will run simultaneously.
- C) The total number of matrix jobs generated is 5 (3 OSes + 2 node versions).
- D) Setting `fail-fast: false` means failed jobs are automatically retried.

---

### Question 20 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow File Structure

**Question**:
A matrix configuration includes this `exclude` entry:

```yaml
matrix:
  os: [ubuntu-latest, windows-latest, macos-latest]
  node: [18, 20]
  exclude:
    - os: windows-latest
      node: 18
```

How many total job instances will be created?

- A) 4
- B) 5
- C) 6
- D) 3

---

### Question 21 — Workflow File Structure

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Workflow File Structure

**Question**:
A job is configured with `continue-on-error: ${{ matrix.experimental == true }}`. An `include` entry adds `experimental: true` to one specific combination. Which of the following statements correctly describe behavior? *(Select all that apply.)*

- A) The experimental matrix combination will not cause the overall run to fail if it fails by itself.
- B) Non-experimental combinations will still cancel if `fail-fast: true` and a non-experimental job fails.
- C) `continue-on-error: true` at the job level causes the job to be re-run automatically on failure.
- D) The `continue-on-error` expression is evaluated per-job, receiving the specific `matrix.experimental` value for that combination.

---

### Question 22 — Workflow File Structure

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Workflow File Structure

**Question**:
A developer wants to re-run only the failed matrix combinations after a partial failure, without re-running the combinations that succeeded. How is this accomplished?

- A) Use the `retry:` key in the matrix strategy block.
- B) Navigate to the failed workflow run in the GitHub UI, select the failed job, and choose "Re-run failed jobs."
- C) Trigger a new `workflow_dispatch` run with the specific matrix values as inputs.
- D) Set `fail-fast: false` and push an empty commit to trigger re-evaluation.

---

### Question 23 — Workflow Trigger Events

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Workflow Trigger Events

**Question**:
Which `on:` configuration triggers a workflow only when a push is made to branches matching `release/**`?

**A.**
```yaml
on:
  push:
    branches: release
```
**B.**
```yaml
on:
  push:
    paths: release/**
```
**C.**
```yaml
on:
  push:
    branches:
      - 'release/**'
```
**D.**
```yaml
on:
  push:
    tags:
      - 'release/**'
```

---

### Question 24 — Workflow Trigger Events

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Workflow Trigger Events

**Question**:
A developer wants a workflow to run every Monday at 9 AM UTC. Which cron expression is correct?

- A) `0 9 * * 1`
- B) `9 0 * * MON`
- C) `0 9 1 * *`
- D) `9 * * * 1`

---

### Question 25 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow Trigger Events

**Question**:
A workflow uses `pull_request` and targets the `main` branch. A fork contributor opens a PR. By default, what permissions does the `GITHUB_TOKEN` have in this workflow run?

- A) Write access – the same as any internal contributor's workflow run.
- B) Read-only – fork `pull_request` workflows always receive a read-only token.
- C) No token is provisioned – external fork runs cannot access `GITHUB_TOKEN`.
- D) Read-only on code but write access on issues and pull requests.

---

### Question 26 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Workflow Trigger Events

**Question**:
Which of the following `pull_request` activity types must be explicitly listed under `types:` to trigger the workflow on those events? *(Select all that apply.)*

- A) `ready_for_review`
- B) `synchronize`
- C) `opened`
- D) `converted_to_draft`
- E) `labeled`

---

### Question 27 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow Trigger Events

**Question**:
A workflow uses the `workflow_run` trigger:

```yaml
on:
  workflow_run:
    workflows: ["Build"]
    types:
      - completed
```

The job inside this workflow contains `if: github.event.workflow_run.conclusion == 'success'`. What does this condition achieve?

- A) It causes the job to run regardless of whether the "Build" workflow succeeded or failed.
- B) It ensures this job only runs when the "Build" workflow completed with a success conclusion.
- C) It is invalid syntax – `workflow_run.conclusion` is not a valid property.
- D) It causes GitHub to cancel the "Build" workflow if it failed.

---

### Question 28 — Workflow Trigger Events

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Workflow Trigger Events

**Question**:
A public repository uses `pull_request_target` to run CI when external contributors submit PRs. The workflow checks out the PR head commit and runs the contributor's test scripts. What is the primary security risk of this configuration?

- A) External contributors cannot trigger `pull_request_target` workflows at all.
- B) The workflow runs with write permissions and access to secrets, and the contributor-controlled code could exfiltrate those secrets.
- C) The checkout action is incompatible with `pull_request_target` and will always fail.
- D) Organization secrets are automatically redacted when `pull_request_target` is used for forks.

---

### Question 29 — Workflow Trigger Events

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Workflow Trigger Events

**Question**:
A `workflow_dispatch` input is declared with `type: choice` and a list of options. Which of the following statements about `workflow_dispatch` inputs are correct? *(Select all that apply.)*

- A) Inputs with `required: true` must be provided when the workflow is triggered via the GitHub UI or API.
- B) `workflow_dispatch` inputs can be accessed via the `inputs` context inside the workflow.
- C) The `environment` input type automatically links the selected environment's protection rules to the workflow run.
- D) `workflow_dispatch` workflows can only be triggered via the GitHub UI, not via the REST API.

---

### Question 30 — Custom Environment Variables

**Difficulty**: Easy
**Answer Type**: one
**Topic**: 06

**Question**:
A developer defines a variable at both the workflow level and the job level with the same name. Which value takes precedence within that job?

- A) The workflow-level definition always wins.
- B) The job-level definition overrides the workflow-level definition within that job.
- C) Both values are concatenated with a colon separator.
- D) GitHub raises a validation error for duplicate variable names.

---

### Question 31 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: 06

**Question**:
A step writes a value to the `GITHUB_OUTPUT` file:

```bash
echo "version=1.2.3" >> $GITHUB_OUTPUT
```

How does a subsequent step in the same job access this value?

- A) `${{ env.version }}`
- B) `${{ steps.<step-id>.outputs.version }}`
- C) `${{ github.output.version }}`
- D) `$GITHUB_OUTPUT_version`

---

### Question 32 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: many
**Topic**: 06

**Question**:
Which of the following are valid methods to make an environment variable available to all steps within a specific job, but not to other jobs in the same workflow? *(Select all that apply.)*

- A) Define it under `jobs.<job-id>.env:`
- B) Define it at the top-level `env:` and reference it only within that job
- C) Write it to `$GITHUB_ENV` in the first step of that job
- D) Define it at the top-level `env:` and use `unset` in jobs where it should not apply

---

### Question 33 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: 06

**Question**:
A workflow uses:

```yaml
env:
  CONFIG: ${{ vars.NODE_ENV || 'development' }}
```

What does the `||` operator achieve in this expression?

- A) It concatenates the string value of `vars.NODE_ENV` with the string `'development'`.
- B) It uses `'development'` as a fallback value if `vars.NODE_ENV` is empty or undefined.
- C) It evaluates `vars.NODE_ENV` as a boolean and runs a shell OR operation.
- D) It is invalid YAML syntax and will cause the workflow to fail.

---

### Question 34 — Custom Environment Variables

**Difficulty**: Hard
**Answer Type**: one
**Topic**: 06

**Question**:
A developer writes a step that passes a secret directly via expression interpolation into a `run:` shell command:

```yaml
run: ./deploy.sh ${{ secrets.DEPLOY_TOKEN }}
```

What security risk does this pattern introduce?

- A) No risk – GitHub encrypts all secrets before they reach the runner.
- B) The secret value is script-injected into the shell command, potentially exposing it through process listings or shell history.
- C) The step will fail because `secrets` context is unavailable in `run:` steps.
- D) Only a risk when using Windows runners; Linux runners handle secret injection safely.

---

### Question 35 — Default Environment Variables

**Difficulty**: Easy
**Answer Type**: one
**Topic**: 07

**Question**:
Which default environment variable always evaluates to `true` in every GitHub Actions workflow run, indicating that the job is running in a CI environment?

- A) `GITHUB_CI`
- B) `CI`
- C) `GITHUB_ACTIONS`
- D) `RUNNER_CI`

---

### Question 36 — Default Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: 07

**Question**:
A step needs the full Git ref of the current run (e.g., `refs/heads/main`). Which default environment variable provides this?

- A) `GITHUB_REF_NAME`
- B) `GITHUB_REF`
- C) `GITHUB_HEAD_REF`
- D) `GITHUB_SHA`

---

### Question 37 — Default Environment Variables

**Difficulty**: Medium
**Answer Type**: many
**Topic**: 07

**Question**:
Which of the following default environment variables are available in every GitHub Actions run regardless of the event type? *(Select all that apply.)*

- A) `GITHUB_REPOSITORY`
- B) `GITHUB_BASE_REF`
- C) `GITHUB_RUN_ID`
- D) `GITHUB_SHA`
- E) `GITHUB_HEAD_REF`

---

### Question 38 — Default Environment Variables

**Difficulty**: Hard
**Answer Type**: one
**Topic**: 07

**Question**:
A step uses `$GITHUB_STEP_SUMMARY` to write a markdown table. Where does this content appear in the GitHub UI?

- A) In the job's log output for the specific step that wrote to it.
- B) On the workflow run summary page as a rendered markdown section.
- C) In the repository's Wiki as a new page.
- D) As a comment on the commit that triggered the workflow.

---

### Question 39 — Environment Protection Rules

**Difficulty**: Easy
**Answer Type**: one
**Topic**: 08

**Question**:
An organization wants all deployments to the `production` environment to pause and wait for approval from a senior engineer before proceeding. Which environment protection rule achieves this?

- A) Deployment branches – restrict deployments to protected branches only.
- B) Wait timer – set a 60-minute delay before the deployment job starts.
- C) Required reviewers – specify the senior engineer's GitHub account as a reviewer.
- D) IP allow list – restrict the runner IPs that can deploy to production.

---

### Question 40 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: one
**Topic**: 08

**Question**:
A `production` environment is configured with `Required reviewers`. A deployment job targeting this environment is triggered. Which statement correctly describes what happens at runtime?

- A) The workflow fails immediately with "insufficient permissions" if the reviewer is not online.
- B) The deployment job is queued and the workflow pauses; once a required reviewer approves, the job proceeds.
- C) The job skips the reviewer check and runs immediately if it was triggered by the repository owner.
- D) All jobs in the workflow are paused, including those not targeting the protected environment.

---

### Question 41 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: many
**Topic**: 08

**Question**:
Which of the following are valid settings under "Deployment branches" for a GitHub environment? *(Select all that apply.)*

- A) All branches
- B) Protected branches only
- C) Selected branches (using patterns like `main`, `release/*`)
- D) Only branches owned by the repository admin
- E) No branches (prevents all deployments)

---

### Question 42 — Environment Protection Rules

**Difficulty**: Hard
**Answer Type**: one
**Topic**: 08

**Question**:
An environment's wait timer is set to 30 minutes. A deployment job targeting that environment is triggered at 10:00 AM. When can a reviewer approve the deployment?

- A) Immediately – the wait timer only applies after approval.
- B) A reviewer can click "Approve" at any point, but the job won't actually start until 10:30 AM.
- C) No reviewer action is needed; the job automatically proceeds at 10:30 AM without approval.
- D) Not until 10:30 AM – the approval prompt does not appear until the wait timer expires.

---

### Question 43 — Workflow Artifacts

**Difficulty**: Easy
**Answer Type**: one
**Topic**: 09

**Question**:
A build job uploads a compiled binary using `actions/upload-artifact`. A separate deploy job in the same workflow needs to use that binary. What must the deploy job specify to ensure it runs after the build job and can access the artifact?

- A) `uses: actions/checkout@v3` with the artifact name as the repository.
- B) `needs: build` and then use `actions/download-artifact` to retrieve the binary.
- C) Both jobs must share the same runner via a `runs-on` label.
- D) The artifact is automatically available to all jobs in the workflow without downloading.

---

### Question 44 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: one
**Topic**: 09

**Question**:
By default, how long are GitHub Actions artifacts retained before being automatically deleted?

- A) 1 day
- B) 5 days
- C) 30 days
- D) 90 days

---

### Question 45 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: many
**Topic**: 09

**Question**:
A step uploads artifacts with the following configuration:

```yaml
- uses: actions/upload-artifact@v3
  with:
    name: test-results
    path: |
      coverage/
      reports/**/*.xml
      !reports/**/temp-*.xml
```

Which of the following statements about this upload are correct? *(Select all that apply.)*

- A) The `!` prefix excludes files matching `reports/**/temp-*.xml` from the upload.
- B) All `.xml` files under `reports/` including those matching `temp-*.xml` are uploaded.
- C) The `coverage/` directory and all its contents are included.
- D) The artifact is named `test-results` and can be downloaded by downstream jobs using that name.

---

### Question 46 — Workflow Artifacts

**Difficulty**: Hard
**Answer Type**: one
**Topic**: 09

**Question**:
A developer wants to download an artifact from a **different** workflow run (not the current run). Their step uses:

```yaml
- uses: actions/download-artifact@v3
  with:
    name: build-dist
    github-token: ${{ secrets.GITHUB_TOKEN }}
    run-id: 9876543210
```

Which of the following is required for this cross-run download to succeed?

- A) The artifact must have been uploaded during the current branch's most recent run.
- B) The artifact named `build-dist` must exist in the specified run, the token must have `actions: read` permission, and the artifact must not have expired.
- C) The `GITHUB_TOKEN` secret must be replaced with a PAT because cross-run downloads require elevated permissions automatically.
- D) The workflow file must be in the same repository as the run that created the artifact.

---

### Question 47 — Workflow Caching

**Difficulty**: Easy
**Answer Type**: one
**Topic**: 10

**Question**:
A cache is configured with:

```yaml
key: ${{ runner.os }}-npm-${{ hashFiles('**/package-lock.json') }}
restore-keys: |
  ${{ runner.os }}-npm-
```

If the exact key is not found, what happens next?

- A) The workflow fails with a "cache miss" error.
- B) The workflow skips the cache step entirely and continues.
- C) GitHub searches for a cache that matches the `restore-keys` prefix and restores the most recent partial match.
- D) The `restore-keys` are ignored; only exact key matches are used.

---

### Question 48 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: 10

**Question**:
A team notices that their npm cache is not being reused across pull requests from different branches. What is the most likely explanation?

- A) Caches can only be accessed from the same branch or the repository's default branch.
- B) npm caches require a PAT to be shared across branches.
- C) Caches are cleared automatically every 24 hours regardless of access.
- D) Changing the matrix adds an extra identifier to the cache key.

---

### Question 49 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: many
**Topic**: 10

**Question**:
Which of the following statements about GitHub Actions cache behavior are correct? *(Select all that apply.)*

- A) Cache entries that have not been accessed for 7 days are automatically evicted.
- B) The maximum cache storage per repository is 5 GB; oldest entries are evicted when this limit is exceeded.
- C) A cache hit means the `actions/cache` step's `cache-hit` output equals `"true"`.
- D) Caches are shared across all repositories in an organization by default.

---

### Question 50 — Workflow Caching

**Difficulty**: Hard
**Answer Type**: one
**Topic**: 10

**Question**:
A workflow caches Python pip packages using:

```yaml
key: ${{ runner.os }}-pip-${{ hashFiles('**/requirements.txt') }}
restore-keys:
  ${{ runner.os }}-pip-
```

A developer adds a new package to `requirements.txt`. What happens on the next run?

- A) The old cache is restored and the new package is installed on top; the updated cache is saved under the old key.
- B) The exact key doesn't match (hash changed); the restore-key partial match is used to restore the old cache; the new package is installed; a new cache entry is saved under the new exact key.
- C) No cache is found because the key changed; pip downloads all packages from scratch, then saves a new cache.
- D) The cache system detects the change in `requirements.txt` and automatically updates the cached packages.

---

### Question 51 — Workflow Sharing

**Difficulty**: Easy
**Answer Type**: one
**Topic**: 11

**Question**:
What trigger event must a reusable workflow file declare in order to be callable from another workflow?

- A) `on: workflow_run`
- B) `on: workflow_dispatch`
- C) `on: workflow_call`
- D) `on: repository_dispatch`

---

### Question 52 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: 11

**Question**:
A calling workflow uses `secrets: inherit` when calling a reusable workflow. What does this mean?

- A) Only secrets explicitly declared in `on.workflow_call.secrets:` of the called workflow are forwarded.
- B) All secrets available to the caller are automatically passed to the called workflow, accessible by the same names.
- C) The called workflow receives a copy of the caller's environment variables but not actual secrets.
- D) `secrets: inherit` is only valid when both workflows are in the same repository.

---

### Question 53 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: many
**Topic**: 11

**Question**:
A reusable workflow declares outputs under `on.workflow_call.outputs`. Which of the following statements about exposing outputs from a reusable workflow to the caller are correct? *(Select all that apply.)*

- A) Reusable workflow outputs must ultimately reference a job-level output, not a step output directly.
- B) The caller accesses the output using `needs.<job-id>.outputs.<output-name>`, where `<job-id>` is the job in the calling workflow that used `uses:`.
- C) A step output can be promoted to a job output with `$GITHUB_OUTPUT`, and then the job output can be promoted to a workflow output in `on.workflow_call.outputs`.
- D) Reusable workflow outputs are available to the caller in the `inputs` context.

---

### Question 54 — Workflow Sharing

**Difficulty**: Hard
**Answer Type**: one
**Topic**: 54

**Question**:
Two reusable workflow secret-passing strategies are compared:

| Strategy | Description |
|---|---|
| Explicit mapping | `secrets: deploy-token: ${{ secrets.DEPLOY_TOKEN }}` |
| `secrets: inherit` | All caller secrets are forwarded automatically |

An organization manages a **public** shared workflow repository used by external teams. Which strategy is recommended and why?

- A) `secrets: inherit` – because it minimizes boilerplate and all public repos should share secrets freely.
- B) Explicit mapping – because only named secrets flow to the called workflow, reducing unintended credential exposure.
- C) Explicit mapping – because `secrets: inherit` is only supported for workflows in private repositories.
- D) `secrets: inherit` – because explicit mapping requires the called workflow to re-declare each secret, which is not possible in public workflows.

---

### Question 55 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: 11

**Question**:
A workflow badge URL follows the format: `https://github.com/<owner>/<repo>/workflows/<workflow-name>/badge.svg`. What does this badge display?

- A) The code coverage percentage from the most recent workflow run.
- B) The pass/fail status for the most recent run of that workflow.
- C) The total number of workflow runs in the last 30 days.
- D) The number of open pull requests associated with the workflow.

---

### Question 56 — Workflow Debugging

**Difficulty**: Easy
**Answer Type**: one
**Topic**: 12

**Question**:
A developer wants to enable verbose debug logging for all steps in a workflow run without editing the workflow file. What is the correct way to enable this?

- A) Add `debug: true` to the `jobs` section of the workflow.
- B) Create a repository secret named `ACTIONS_STEP_DEBUG` with value `true` and re-run the workflow.
- C) Set the `RUNNER_DEBUG=1` environment variable in the first step of the workflow.
- D) Append `?debug=true` to the URL when navigating to the workflow run.

---

### Question 57 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: one
**Topic**: 12

**Question**:
A step uses the following workflow command:

```bash
echo "::warning::The configuration file is using deprecated settings."
```

Where does this warning appear?

- A) Only in the step's raw log output with no special formatting.
- B) In the workflow run log with a warning annotation, and as a workflow annotation that may appear on the associated commit or PR.
- C) As a GitHub Issue filed against the repository automatically.
- D) Only in the `GITHUB_STEP_SUMMARY` file.

---

### Question 58 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: many
**Topic**: 12

**Question**:
Which workflow commands can be used within a `run:` step to structure log output? *(Select all that apply.)*

- A) `echo "::group::My Group Name"` – starts a collapsible log group
- B) `echo "::endgroup::"` – closes a collapsible log group
- C) `echo "::debug::My debug message"` – emits a debug-level log entry
- D) `echo "::set-output name=myvar::myvalue"` – the recommended modern way to set step outputs
- E) `echo "::notice::Something important happened"` – creates a notice annotation

---

### Question 59 — Workflow Debugging

**Difficulty**: Hard
**Answer Type**: one
**Topic**: 12

**Question**:
A step produces significant output and a developer needs to capture all stdout into a step output variable for later use. Given this step:

```yaml
- name: Capture output
  id: capture
  run: |
    RESULT=$(./generate-report.sh)
    echo "report=$RESULT" >> $GITHUB_OUTPUT
```

What limitation must the developer be aware of?

- A) Step outputs cannot contain newlines; multiline values require a heredoc syntax with `<<EOF` when writing to `$GITHUB_OUTPUT`.
- B) The `$GITHUB_OUTPUT` file is cleared between jobs, so this value will not persist beyond the current job.
- C) The maximum size of a single step output value is 1 MB.
- D) Step outputs are only available in subsequent steps if the step has `continue-on-error: true` set.

---

### Question 60 — Workflows REST API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: 13

**Question**:
A script needs to trigger a `workflow_dispatch` workflow via the REST API. Which HTTP method and endpoint are correct?

- A) `GET /repos/{owner}/{repo}/actions/workflows/{workflow_id}/dispatches`
- B) `POST /repos/{owner}/{repo}/actions/workflows/{workflow_id}/dispatches`
- C) `PUT /repos/{owner}/{repo}/actions/runs/{run_id}/trigger`
- D) `PATCH /repos/{owner}/{repo}/actions/workflows/{workflow_id}/dispatches`

---

### Question 61 — Workflows REST API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: 13

**Question**:
An external system needs to list only the failed workflow runs for a repository. Which query parameter combination achieves this?

- A) `?conclusion=failure`
- B) `?status=failure`
- C) `?result=failed`
- D) `?event=failure`

---

### Question 62 — Workflows REST API

**Difficulty**: Medium
**Answer Type**: many
**Topic**: 13

**Question**:
A developer calls `GET /repos/{owner}/{repo}/actions/runs/{run_id}`. Which of the following fields are included in the response? *(Select all that apply.)*

- A) `status` – indicates whether the run is queued, in_progress, or completed
- B) `conclusion` – indicates the final result (success, failure, cancelled, etc.)
- C) `head_branch` – the branch that triggered the run
- D) `workflow_steps` – an inline list of all step names and their results
- E) `run_number` – a sequential counter for this workflow's runs

---

### Question 63 — Workflows REST API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: 13

**Question**:
A CI/CD platform wants to cancel a running workflow via the REST API. Which endpoint is used?

- A) `DELETE /repos/{owner}/{repo}/actions/runs/{run_id}`
- B) `POST /repos/{owner}/{repo}/actions/runs/{run_id}/cancel`
- C) `PATCH /repos/{owner}/{repo}/actions/runs/{run_id}` with `{"status": "cancelled"}`
- D) `POST /repos/{owner}/{repo}/actions/workflows/{workflow_id}/disable`

---

### Question 64 — Reviewing Deployments

**Difficulty**: Easy
**Answer Type**: one
**Topic**: 64

**Question**:
When a deployment reviewer **rejects** a pending deployment in the GitHub Actions UI, what happens to the workflow?

- A) The deployment job is retried automatically after a 10-minute delay.
- B) The workflow stops and the deployment does not proceed; the run is marked as rejected.
- C) The workflow skips the deployment job but continues executing subsequent jobs.
- D) The rejection is logged but the deployment proceeds anyway after a 30-minute wait.

---

### Question 65 — Reviewing Deployments

**Difficulty**: Medium
**Answer Type**: one
**Topic**: 65

**Question**:
A deployment review workflow has multiple downstream jobs that depend on the protected environment job. A reviewer approves the deployment. What happens to those downstream jobs?

- A) They were already running in parallel while waiting for the approval.
- B) They proceed normally after the protected environment job completes successfully.
- C) They require separate review approvals of their own even if they do not target a protected environment.
- D) They are skipped because only the protected environment job can run after an approval.

---

### Question 66 — Reviewing Deployments

**Difficulty**: Hard
**Answer Type**: many
**Topic**: 66

**Question**:
When configuring deployment reviewers in GitHub environment settings, which of the following are true? *(Select all that apply.)*

- A) Both individual GitHub users and GitHub Teams can be designated as required reviewers.
- B) The workflow actor (the person who triggered the workflow) can serve as a required reviewer for their own deployment.
- C) Up to 6 reviewers (users or teams combined) can be configured per environment.
- D) A required reviewer receives a notification to review the deployment when the workflow reaches that environment.

---

### Question 67 — Creating and Publishing Actions

**Difficulty**: Easy
**Answer Type**: one
**Topic**: 15

**Question**:
A developer wants to reference a custom action stored in the **same repository** as the calling workflow. Which `uses:` syntax is correct?

- A) `uses: ./my-custom-action`
- B) `uses: self/my-custom-action@main`
- C) `uses: local:my-custom-action`
- D) `uses: ${{ github.repository }}/my-custom-action@${{ github.sha }}`

---

### Question 68 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: 15

**Question**:
What file is required in the root of a custom action repository to define its inputs, outputs, and runtime?

- A) `workflow.yml`
- B) `action.yml` (or `action.yaml`)
- C) `package.json`
- D) `Dockerfile` (only for Docker-based actions)

---

### Question 69 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: many
**Topic**: 15

**Question**:
Which of the following are valid action types supported by the `runs.using` key in `action.yml`? *(Select all that apply.)*

- A) `node20`
- B) `docker`
- C) `composite`
- D) `python3`
- E) `node16`

---

### Question 70 — Creating and Publishing Actions

**Difficulty**: Hard
**Answer Type**: one
**Topic**: 15

**Question**:
A JavaScript action uses `@actions/core` to set a step output. Which method call is used?

- A) `core.setEnv('output-name', value)`
- B) `core.exportVariable('output-name', value)`
- C) `core.setOutput('output-name', value)`
- D) `core.setSecret('output-name', value)`

---

### Question 71 — Managing Runners

**Difficulty**: Easy
**Answer Type**: one
**Topic**: 16

**Question**:
A workflow needs to run on a self-hosted runner that has a label `gpu`. Which `runs-on:` configuration targets this runner?

- A) `runs-on: self-hosted-gpu`
- B) `runs-on: gpu`
- C) `runs-on: [self-hosted, gpu]`
- D) `runs-on: {type: self-hosted, label: gpu}`

---

### Question 72 — Managing Runners

**Difficulty**: Medium
**Answer Type**: one
**Topic**: 16

**Question**:
A self-hosted runner shows status `online` but `busy: false` in the API, yet jobs targeting it remain queued indefinitely. What is the most likely cause?

- A) The runner's registration token has expired and needs to be rotated.
- B) The `runs-on` label in the workflow does not exactly match all labels on the runner.
- C) The runner is on a different operating system than specified in `runs-on`.
- D) Self-hosted runners cannot pick up jobs unless `allow_public_repositories: true` is set.

---

### Question 73 — Managing Runners

**Difficulty**: Medium
**Answer Type**: many
**Topic**: 16

**Question**:
Which of the following are security risks associated with using self-hosted runners on public repositories? *(Select all that apply.)*

- A) Malicious fork pull requests could trigger workflows that execute arbitrary code on the self-hosted runner.
- B) Self-hosted runners are always accessible to any internet client without authentication.
- C) Environment variables and files on the runner from a previous job may be accessible to subsequent jobs if the runner is not ephemeral.
- D) GITHUB_TOKEN permissions cannot be restricted on self-hosted runners, giving workflows unbounded write access.

---

### Question 74 — Managing Runners

**Difficulty**: Hard
**Answer Type**: one
**Topic**: 16

**Question**:
A self-hosted runner needs to be restarted because it crashed mid-job. After the service is restarted, what happens to the job that was in progress when the runner crashed?

- A) The job is automatically resumed from where it stopped.
- B) The job is marked as failed; a new run must be triggered or the failed job re-run manually.
- C) GitHub retries the job on another available runner with the same labels.
- D) The job is queued again with the same run ID and picks up where it left off.

---

### Question 75 — GitHub Actions Enterprise

**Difficulty**: Easy
**Answer Type**: one
**Topic**: 17

**Question**:
At what level can an enterprise administrator enforce that a specific compliance workflow runs on all repositories in all organizations in the enterprise, regardless of each repository's own workflow configuration?

- A) Repository level via a required status check.
- B) Organization level via a branch protection rule.
- C) Enterprise level via Required Workflows policy.
- D) Repository level via the Actions permissions setting.

---

### Question 76 — GitHub Actions Enterprise

**Difficulty**: Medium
**Answer Type**: one
**Topic**: 17

**Question**:
An organization policy is set to "Allow select actions and reusable workflows." A workflow in the organization tries to use `docker/build-push-action@v5`, which is not in the allow list. What happens?

- A) The action runs but its outputs are quarantined for review.
- B) The workflow fails when it reaches that step because the action is blocked by the policy.
- C) The action is automatically added to the allow list after the first successful run.
- D) The workflow skips the step silently and continues.

---

### Question 77 — GitHub Actions Enterprise

**Difficulty**: Medium
**Answer Type**: many
**Topic**: 17

**Question**:
Which of the following statements about runner groups in a GitHub organization are correct? *(Select all that apply.)*

- A) A runner can belong to only one runner group at a time.
- B) Runner groups can be configured to allow access only to specific repositories.
- C) Enterprise-level runner groups can span multiple organizations.
- D) Workflow files can directly specify a runner group name in addition to runner labels.

---

### Question 78 — GitHub Actions Enterprise

**Difficulty**: Hard
**Answer Type**: one
**Topic**: 17

**Question**:
An enterprise has GitHub-hosted runner IP ranges that change frequently. Services behind a firewall need to allow inbound traffic from these runners. Which option provides automatic synchronization of the allowed IP ranges?

- A) Manually update the firewall rule weekly by running `curl https://api.github.com/meta`.
- B) Subscribe to the GitHub `meta` webhook event, which fires when IP ranges change.
- C) Use a self-hosted runner inside the network perimeter instead.
- D) Enable the "GitHub Actions" item in the organization's IP allow list in GitHub Enterprise Cloud, which automatically handles the IP ranges.

---

### Question 79 — Security and Optimization

**Difficulty**: Medium
**Answer Type**: one
**Topic**: 18

**Question**:
A workflow needs to upload Docker images to Amazon ECR without storing long-lived AWS credentials as repository secrets. Which approach achieves this?

- A) Store an IAM user access key/secret in repository secrets and reference them in the workflow.
- B) Use GitHub OIDC federation: grant `id-token: write` permission, configure an AWS IAM role with a trust policy pointing to GitHub's OIDC provider, and use `aws-actions/configure-aws-credentials` with `role-to-assume`.
- C) Use the `GITHUB_TOKEN` secret to authenticate with AWS ECR.
- D) Create a GitHub App and use its installation token to push images to ECR.

---

### Question 80 — Security and Optimization

**Difficulty**: Medium
**Answer Type**: one
**Topic**: 18

**Question**:
A developer pins an action to a full commit SHA:

```yaml
- uses: actions/checkout@11bd71901bbe5b1630ceea73d27597364c9af683 # v4.2.2
```

What security benefit does this provide compared to `actions/checkout@v4`?

- A) It enables GitHub to scan the action's code for vulnerabilities automatically.
- B) It guarantees that the exact same action code runs every time, even if the `v4` tag is moved or the action is compromised.
- C) It prevents the action from accessing repository secrets.
- D) It restricts the action from making outbound network calls.

---

### Question 81 — Security and Optimization

**Difficulty**: Hard
**Answer Type**: one
**Topic**: 18

**Question**:
A workflow step directly interpolates `${{ github.event.pull_request.title }}` into a `run:` shell script. An attacker submits a pull request with a title containing shell metacharacters. What is the attack vector and correct mitigation?

- A) Attack: The expression evaluates in the YAML parser before reaching the shell; mitigation: wrap in double quotes in YAML.
- B) Attack: Script injection – the PR title value is substituted into the shell command, potentially being interpreted as shell commands; mitigation: assign to an environment variable and reference it as `$ENV_VAR` in the script.
- C) Attack: The expression triggers YAML deserialization of arbitrary code; mitigation: use `fromJSON()` to safely parse the value.
- D) Attack: None – GitHub sanitizes all `github.event` context values before they reach the runner.

---

### Question 82 — Security and Optimization

**Difficulty**: Hard
**Answer Type**: many
**Topic**: 18

**Question**:
Which of the following are accurate statements about the `GITHUB_TOKEN`? *(Select all that apply.)*

- A) `GITHUB_TOKEN` is automatically created by GitHub at the start of each **job** and revoked when the job finishes.
- B) A workflow triggered by `GITHUB_TOKEN` push events cannot trigger additional workflow runs to prevent infinite loops.
- C) The default permission level of `GITHUB_TOKEN` can be set to read-only across all scopes at the organization level.
- D) `GITHUB_TOKEN` can be used to trigger `workflow_dispatch` events in the same repository.

---

### Question 83 — Security and Optimization

**Difficulty**: Hard
**Answer Type**: one
**Topic**: 18

**Question**:
The OIDC subject claim for a workflow running in a deployment to the `production` environment follows which format?

- A) `repo:ORG/REPO:environment:production`
- B) `ORG/REPO:env:production`
- C) `ref:refs/heads/main:environment:production`
- D) `repo:ORG/REPO:ref:refs/heads/main`

---

### Question 84 — Common Failures and Troubleshooting

**Difficulty**: Easy
**Answer Type**: one
**Topic**: 19

**Question**:
A workflow fails with:

```
fatal: could not read Username for 'https://github.com': terminal prompts disabled
```

What is the most likely cause?

- A) The runner does not have Git installed.
- B) The `actions/checkout` step is missing or the `GITHUB_TOKEN` was not passed to it.
- C) The repository is private and requires a deploy key.
- D) The workflow YAML file has a syntax error in the `on:` trigger.

---

### Question 85 — Common Failures and Troubleshooting

**Difficulty**: Medium
**Answer Type**: one
**Topic**: 19

**Question**:
A Node.js workflow fails with `npm ERR! code ERESOLVE`. Without understanding the exact conflict, what workflow change is the quickest way to diagnose whether the issue is a `package-lock.json` sync problem?

- A) Delete `package-lock.json` from the repository and re-push.
- B) Replace `npm ci` with `npm install --legacy-peer-deps` in the workflow.
- C) Add `--verbose` to the `npm ci` command to see the full dependency tree in the log.
- D) Switch to `yarn` in the workflow to avoid npm resolution errors.

---

### Question 86 — Common Failures and Troubleshooting

**Difficulty**: Medium
**Answer Type**: many
**Topic**: 19

**Question**:
A self-hosted runner shows `offline` status in Repository Settings → Actions → Runners. Which of the following are valid troubleshooting steps? *(Select all that apply.)*

- A) Verify the runner process is running on the host machine (`ps aux | grep Runner.Listener`).
- B) Check outbound connectivity from the runner to `github.com` and `api.github.com`.
- C) Re-register the runner with a new token if the existing registration is corrupted.
- D) Increase the runner's `timeout-minutes` setting in the workflow file.

---

### Question 87 — Common Failures and Troubleshooting

**Difficulty**: Hard
**Answer Type**: one
**Topic**: 19

**Question**:
A matrix workflow has 12 job combinations. One combination fails after 45 minutes. `fail-fast` is set to `true` (the default). How many of the remaining in-progress or queued jobs are affected?

- A) None – `fail-fast` only cancels queued jobs, not in-progress jobs.
- B) All remaining in-progress and queued matrix jobs are cancelled.
- C) Only jobs that have not yet started are cancelled; in-progress jobs run to completion.
- D) The next 5 jobs are cancelled; the rest continue to run.

---

### Question 88 — Common Failures and Troubleshooting

**Difficulty**: Medium
**Answer Type**: one
**Topic**: 19

**Question**:
A secret named `API_KEY` is available in the repository but when a step prints `echo "Key: $API_KEY"` to the log, the output shows `Key: ***`. What does this behavior indicate?

- A) The secret is not set and GitHub replaces missing secret values with `***`.
- B) GitHub's automatic secret masking is working correctly – the actual value is redacted in the log.
- C) The step does not have permission to access `API_KEY` and GitHub masks missing values.
- D) The secret was rotated; the `***` indicates an expired value.

---

### Question 89 — Common Failures and Troubleshooting

**Difficulty**: Hard
**Answer Type**: many
**Topic**: 19

**Question**:
A job is stuck in "queued" status and never starts. Which of the following are potential root causes that should be investigated? *(Select all that apply.)*

- A) No runner with labels matching `runs-on:` is currently available or registered.
- B) A concurrency group with `cancel-in-progress: false` has another run already occupying the slot.
- C) The job's `if:` condition evaluated to `false`, keeping it out of the execution queue.
- D) The required environment reviewer has not approved, causing the job to wait.
- E) The repository has hit its GitHub Actions usage limit for the billing period.

---

### Question 90 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow File Structure

**Question**:
A step uses `continue-on-error: true`. The step fails. What is the behavior of the job and workflow?

- A) The job is marked as failed and the remaining steps in the job are skipped.
- B) The failed step is reported as a warning; the job continues to run subsequent steps and is ultimately reported as successful if all other steps pass.
- C) The job is marked as failed, but subsequent steps still run because `continue-on-error` propagates.
- D) The workflow is cancelled immediately.

---

### Question 91 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow Trigger Events

**Question**:
A workflow configured with `on: schedule` uses `cron: '0 */6 * * *'`. How often does this workflow run?

- A) Every 6 minutes
- B) Every 6 hours
- C) On the 6th day of every month
- D) At 6:00 AM UTC every day

---

### Question 92 — Workflow Sharing

**Difficulty**: Hard
**Answer Type**: one
**Topic**: 11

**Question**:
A reusable workflow needs to output the version string built during its execution back to the caller. The three-layer chain is: step output → job output → workflow output. In the reusable workflow file, which key under `on.workflow_call` is used to declare this output?

- A) `returns:`
- B) `outputs:`
- C) `exports:`
- D) `artifacts:`

---

### Question 93 — GitHub Actions Enterprise

**Difficulty**: Hard
**Answer Type**: many
**Topic**: 17

**Question**:
Which of the following are accurate behaviors of GitHub-required workflows at the enterprise level? *(Select all that apply.)*

- A) Required workflows run even if the targeted repository has GitHub Actions disabled.
- B) Required workflow results appear in the PR checks list alongside the repository's own workflow results.
- C) Repository owners can override or skip required workflow runs using `workflow_dispatch`.
- D) Required workflows are enforced for all repositories matching the configured filter, regardless of each repository's branch protection rules.

---

### Question 94 — Workflow Artifacts

**Difficulty**: Hard
**Answer Type**: one
**Topic**: 09

**Question**:
A workflow uploads an artifact with `retention-days: 7`. The repository-level default retention is 30 days and the organization policy sets a maximum of 90 days. Which value actually applies?

- A) 90 days – organization maximum always wins.
- B) 30 days – repository default overrides per-artifact settings.
- C) 7 days – the per-artifact `retention-days` value is honored when it is less than the organization maximum.
- D) 1 day – when multiple retention policies conflict, GitHub uses the most restrictive value.

---

### Question 95 — Security and Optimization

**Difficulty**: Hard
**Answer Type**: one
**Topic**: 18

**Question**:
A `permissions:` block at the **workflow level** sets `contents: read`. A job inside the workflow does not declare its own `permissions:` block. What permissions does that job's `GITHUB_TOKEN` have?

- A) The job inherits the workflow-level `contents: read` restriction.
- B) The job receives the repository's default permissions, ignoring the workflow-level block.
- C) The job has no permissions because a workflow-level `permissions:` block removes all defaults for jobs that don't override it.
- D) The job receives write permissions automatically because jobs without explicit `permissions:` are treated as trusted.

---

### Question 96 — Cross-Topic – Environment Rules and Enterprise

**Difficulty**: Hard
**Answer Type**: many
**Topic**: 08+17

**Question**:
An enterprise security team wants to ensure that no production deployment can happen unless (a) the commit is on the `main` branch and (b) at least one member of the `senior-engineers` team approves. Which combination of GitHub features achieves this? *(Select all that apply.)*

- A) Configure the production environment's "Deployment branches" to `main`.
- B) Add the `senior-engineers` GitHub Team as required reviewers on the production environment.
- C) Use a required workflow at the enterprise level that validates the branch before allowing deployment.
- D) Set `fail-fast: true` in the matrix strategy of the deployment job.

---

### Question 97 — Cross-Topic – Triggers and Security

**Difficulty**: Hard
**Answer Type**: one
**Topic**: 05+18

**Question**:
A public repository uses `pull_request_target` to post auto-comments on PRs. The workflow checks out code with:

```yaml
- uses: actions/checkout@v4
  with:
    ref: ${{ github.event.pull_request.head.sha }}
```

and then runs `npm run test`. Why is this configuration dangerous and what should be done?

- A) Using `pull_request_target` with checkout of the fork's head SHA and subsequent execution of untrusted scripts runs attacker-controlled code with write permissions and access to secrets. The fix is to never checkout and execute fork code in a `pull_request_target` context, or to restrict execution to labeled PRs after manual review.
- B) The `pull_request_target` event does not provide `github.event.pull_request.head.sha`, so the checkout silently fails.
- C) `npm run test` is blocked by GitHub's code scanning when triggered from fork PRs.
- D) Using `pull_request_target` from a fork is safe because GitHub automatically sandboxes the runner.

---

### Question 98 — Cross-Topic – Workflow Structure and Sharing

**Difficulty**: Hard
**Answer Type**: many
**Topic**: 04+11

**Question**:
A reusable workflow declares three inputs and one output. A calling workflow job uses this reusable workflow and a downstream job needs the output. Which of the following are required? *(Select all that apply.)*

- A) The reusable workflow must promote the step output to a job output via `$GITHUB_OUTPUT` and the job's `outputs:` key.
- B) The reusable workflow must declare the output in `on.workflow_call.outputs:` referencing the job output.
- C) The calling workflow's downstream job must declare `needs:` pointing to the job that called the reusable workflow.
- D) The output is accessed in the downstream job using `${{ needs.<calling-job-id>.outputs.<output-name> }}`.

---

### Question 99 — Cross-Topic – Caching and Runners

**Difficulty**: Hard
**Answer Type**: one
**Topic**: 10+16

**Question**:
A team uses self-hosted runners for their CI pipeline. They configure `actions/cache` to cache npm dependencies. A developer notices that cache hits never occur even when dependencies haven't changed. What is the most likely explanation?

- A) `actions/cache` only works with GitHub-hosted runners; it is not compatible with self-hosted runners.
- B) The cache key includes `${{ runner.os }}`, but the self-hosted runner reports a different OS string than when the cache was saved.
- C) Self-hosted runners require a separate cache backend configuration (Redis or S3) before `actions/cache` will function.
- D) The default 5 GB cache limit is lower for self-hosted runners.

---

### Question 100 — Cross-Topic – Env Vars, Artifacts, and Security

**Difficulty**: Hard
**Answer Type**: many
**Topic**: 06+09+18

**Question**:
A CI pipeline builds a Docker image, uploads the image digest as an artifact, and deploys to production. The team wants to follow security best practices throughout. Which of the following statements describe correct practices? *(Select all that apply.)*

- A) Pass secrets to Docker build steps using environment variables (`--build-arg` with `${{ secrets.* }}`) rather than hardcoding them in the Dockerfile.
- B) Reference third-party actions used in the pipeline by full commit SHA rather than mutable version tags.
- C) Use `GITHUB_TOKEN` to authenticate to AWS ECR for the Docker push, since it provides cloud provider access.
- D) Set `permissions: contents: read` at the workflow level and only grant additional scopes in jobs that need them.

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | B | Incorrect YAML extension setup is most likely; the GitHub Actions extension requires YAML language support. | 01-GitHub-Actions-VS-Code-Extension.md | Easy |
| 2 | A | Autocomplete shows currently available contexts; context availability depends on workflow location. | 01-GitHub-Actions-VS-Code-Extension.md | Medium |
| 3 | C | Hover preview displays action.yml metadata (inputs, outputs, description) from the referenced action. | 01-GitHub-Actions-VS-Code-Extension.md | Medium |
| 4 | A, B, D | Extension validates permission scopes, highlights unavailable contexts, provides event autocomplete, but cannot execute locally. | 01-GitHub-Actions-VS-Code-Extension.md | Hard |
| 5 | B | `github.sha` retrieves the full 40-character commit SHA from push events. | 02-Contextual-Information.md | Easy |
| 6 | A | `github.actor` identifies the triggering user; `github.triggering_actor` distinguishes original vs. re-run actor. | 02-Contextual-Information.md | Medium |
| 7 | A, B | Both bracket and dot notation work for accessing step outputs via `steps.<id>.outputs.<name>`. | 02-Contextual-Information.md | Medium |
| 8 | C | `strategy.job-total` returns the total number of matrix job combinations. | 02-Contextual-Information.md | Medium |
| 9 | B, C | Inputs are only string type; available in job `if:` and `run-name`; cannot pass undeclared inputs. | 02-Contextual-Information.md | Hard |
| 10 | B | Use `${{ needs.<job-id>.outputs.<output-name> }}` for accessing job outputs in downstream jobs. | 02-Contextual-Information.md | Hard |
| 11 | B | `run-name` supports only `github`, `inputs`, and `vars` contexts; `runner` context is unavailable. | 03-Context-Availability-Reference.md | Medium |
| 12 | B | Job-level `if:` is evaluated before runner assignment; `runner` context is not available at that stage. | 03-Context-Availability-Reference.md | Medium |
| 13 | A, B, C, D, E | All listed contexts (secrets, runner, matrix, strategy, needs) are available in `jobs.<id>.steps[*].run`. | 03-Context-Availability-Reference.md | Hard |
| 14 | B | `matrix` context is not available in `jobs.<id>.container`; only static strings or `github`/`inputs`/`vars` contexts supported. | 03-Context-Availability-Reference.md | Hard |
| 15 | A, B, C, D, E | None of these contexts are available in `jobs.<id>.outputs`; only `needs` and job-level properties can be referenced. | 03-Context-Availability-Reference.md | Hard |
| 16 | B | `defaults.run.working-directory` sets the default working directory for all `run:` steps in the workflow. | 04-Workflow-File-Structure.md | Easy |
| 17 | C | Jobs run in parallel by default unless `needs:` dependencies specify sequential execution. | 04-Workflow-File-Structure.md | Easy |
| 18 | C | `cancel-in-progress: true` cancels the running workflow when a new push to the same branch occurs. | 04-Workflow-File-Structure.md | Medium |
| 19 | A, B | `fail-fast: false` allows remaining jobs to continue on failure; `max-parallel: 2` limits concurrent job execution. | 04-Workflow-File-Structure.md | Medium |
| 20 | B | The `exclude` array removes specific matrix combinations; 6 base combinations minus 1 excluded = 5 total jobs. | 04-Workflow-File-Structure.md | Medium |
| 21 | A, B, D | `continue-on-error` is evaluated per job with matrix context; experimental jobs don't fail the run; non-experimental jobs still cancel with fail-fast. | 04-Workflow-File-Structure.md | Hard |
| 22 | B | Use "Re-run failed jobs" in the GitHub UI to retry only failed matrix combinations. | 04-Workflow-File-Structure.md | Hard |
| 23 | C | `on: push: branches:` with pattern filter triggers on matching branch names. | 05-Workflow-Trigger-Events.md | Easy |
| 24 | A | Cron expression `0 9 * * 1` runs at 9 AM UTC on Mondays (day 1 = Monday). | 05-Workflow-Trigger-Events.md | Medium |
| 25 | B | Fork pull_request workflows receive read-only `GITHUB_TOKEN` for security. | 05-Workflow-Trigger-Events.md | Medium |
| 26 | A, D, E | `ready_for_review`, `converted_to_draft`, and `labeled` must be explicitly listed; `opened`/`synchronize` are default. | 05-Workflow-Trigger-Events.md | Medium |
| 27 | B | Condition checks that triggered workflow completed successfully; does not affect the trigger workflow. | 05-Workflow-Trigger-Events.md | Medium |
| 28 | B | `pull_request_target` grants write permissions and secrets to external contributors; PR code could exfiltrate secrets. | 05-Workflow-Trigger-Events.md | Hard |
| 29 | A, B, C | Required inputs must be provided; inputs accessible via `inputs` context; choice type supports environment linking. | 05-Workflow-Trigger-Events.md | Hard |
| 30 | B | Job-level environment variable definition overrides workflow-level definition within that job's scope. | 06-Custom-Environment-Variables.md | Easy |
| 31 | B | Step outputs accessed via `${{ steps.<step-id>.outputs.<name> }}` after writing to `GITHUB_OUTPUT`. | 06-Custom-Environment-Variables.md | Medium |
| 32 | A, C | Both secrets and env variables can be accessed via context expressions; both have scope hierarchy. | 06-Custom-Environment-Variables.md | Medium |
| 33 | B | GitHub automatically masks exact string matches of secrets 3+ characters in logs. | 06-Custom-Environment-Variables.md | Medium |
| 34 | B | Workflow-level `env:` can reference `github` context; `github.sha` evaluates as full 40-char commit SHA. | 06-Custom-Environment-Variables.md | Hard |
| 35 | B | Only `GITHUB_TOKEN` is auto-created per job; other secrets must be explicitly defined. | 07-Default-Environment-Variables.md | Easy |
| 36 | B | GITHUB_TOKEN scoped to current repository; cross-repo access denied by default. | 07-Default-Environment-Variables.md | Hard |
| 37 | A, C, D | `CI=true`, `GITHUB_ACTIONS=true`, and `RUNNER_DEBUG=true` all enable detection; DEBUG_MODE is not automatic. | 07-Default-Environment-Variables.md | Medium |
| 38 | B | Use required reviewers deployment protection rule to require manual approval before job continuation. | 08-Environment-Protection-Rules.md | Medium |
| 39 | C | All listed deployment branch protection scenarios are enforced consistently. | 08-Environment-Protection-Rules.md | Hard |
| 40 | B | 24-hour threshold from first job execution (Tuesday 2 PM to Wednesday 2 PM). | 08-Environment-Protection-Rules.md | Medium |
| 41 | A, B, C | Artifacts auto-delete after retention, require explicit downloads, and support glob patterns. | 09-Workflow-Artifacts.md | Medium |
| 42 | D | Multiple factors can prevent workflow trigger including branch filters and event configurations. | 05-Workflow-Trigger-Events.md | Hard |
| 43 | B | Use `if: failure()` to upload artifacts conditionally on step failure. | 09-Workflow-Artifacts.md | Medium |
| 44 | B | hashFiles updates cache key when dependency files change, invalidating stale cache. | 10-Workflow-Caching.md | Medium |
| 45 | A, C, D | hashFiles for key updates, restore-keys for fallback, most recent cache restored. | 10-Workflow-Caching.md | Medium |
| 46 | B | Composite actions use `runs: { using: composite, steps: [...] }` syntax. | 11-Workflow-Sharing.md | Medium |
| 47 | C | Workflows cannot be executed locally from VS Code; viewing is UI-only. | 01-GitHub-Actions-VS-Code-Extension.md | Hard |
| 48 | A | GitHub automatically renames matrix artifacts with matrix identifiers to prevent collisions. | 09-Workflow-Artifacts.md | Hard |
| 49 | A, B, C | All statements about `if: always()` uploading artifacts regardless of outcome are correct. | 09-Workflow-Artifacts.md | Medium |
| 50 | B | Workflows triggered manually; inputs and optional typed parameters supported. | 05-Workflow-Trigger-Events.md | Easy |
| 51 | C | `hashFiles()` syntax and multiple path syntax both valid for cache key generation. | 10-Workflow-Caching.md | Medium |
| 52 | B | Use `with: secrets: inherit` to pass all repository secrets to reusable workflows. | 11-Workflow-Sharing.md | Hard |
| 53 | A, B, C | All artifact management statements regarding storage, retention, and API access are correct. | 09-Workflow-Artifacts.md | Medium |
| 54 | B | action.yml required at repo root; README with examples recommended; GitHub Marketplace optional. | 15-Creating-Publishing-Actions.md | Medium |
| 55 | B | Reusable workflows require `on: workflow_call` trigger definition. | 11-Workflow-Sharing.md | Easy |
| 56 | B | Reusable workflow metadata file is `action.yml` or `action.yaml` at repository root. | 15-Creating-Publishing-Actions.md | Easy |
| 57 | B | Shipping node_modules or using ncc/esbuild bundling are both valid distribution methods. | 15-Creating-Publishing-Actions.md | Medium |
| 58 | A, B, C, E | All reusable workflow definition and invocation patterns are valid. | 11-Workflow-Sharing.md | Hard |
| 59 | A | Set RUNNER_DEBUG=1 as repository secret and re-run workflow to enable debug output. | 12-Workflow-Debugging.md | Medium |
| 60 | B | Workflow logging commands (notice, warning, error) available; workflow_run events support logging. | 12-Workflow-Debugging.md | Medium |
| 61 | B | Multiple logging approaches available (UI, API, annotations) for different debugging scenarios. | 12-Workflow-Debugging.md | Medium |
| 62 | A, B, C, E | All listed runner environment variables are valid and available in workflows. | 07-Default-Environment-Variables.md | Medium |
| 63 | B | `if: failure()` uploads only on failure; `if: always()` uploads regardless of outcome. | 09-Workflow-Artifacts.md | Medium |
| 64 | B | Required reviewers protection rule; all deployment scenarios listed are enforced. | 08-Environment-Protection-Rules.md | Medium |
| 65 | B | Repository settings environment configuration location. | 14-Reviewing-Deployments.md | Easy |
| 66 | A, C, D | All workflow triggering API statements correct; only string type inputs supported. | 13-Workflows-REST-API.md | Medium |
| 67 | A | Correct API endpoint for workflow list and details retrieval. | 13-Workflows-REST-API.md | Easy |
| 68 | B | POST to cancel endpoint `/repos/{owner}/{repo}/actions/runs/{run_id}/cancel`. | 13-Workflows-REST-API.md | Medium |
| 69 | A, B, C, E | All context scoping and access statements correct regarding environment-scoped secrets. | 08-Environment-Protection-Rules.md | Hard |
| 70 | C | All approaches (UI, API, step annotations) provide workflow timing information. | 12-Workflow-Debugging.md | Hard |
| 71 | C | GitHub doesn't provide built-in deployment time-window protection; custom logic required. | 08-Environment-Protection-Rules.md | Hard |
| 72 | B | Deployment history available through UI, API, and workflow logs. | 14-Reviewing-Deployments.md | Medium |
| 73 | A, C | Required workflows run automatically; admins cannot disable; appear in PR checks. | 17-GitHub-Actions-Enterprise.md | Medium |
| 74 | B | "Allow local actions only" permits organization/enterprise actions within scope. | 17-GitHub-Actions-Enterprise.md | Medium |
| 75 | C | Moving v1 tag to new release allows existing references to receive updates. | 15-Creating-Publishing-Actions.md | Hard |
| 76 | B | All testing approaches valid at different development and validation stages. | 15-Creating-Publishing-Actions.md | Medium |
| 77 | A, B, C, D | All runner label and targeting configuration options are valid. | 16-Managing-Runners.md | Medium |
| 78 | D | All performance improvement strategies listed are valid and proven effective. | 19-Common-Failures-Troubleshooting.md | Hard |
| 79 | B | ubuntu-latest and ubuntu-22.04 both support Python 3.10. | 16-Managing-Runners.md | Easy |
| 80 | B | Disable runners during maintenance; complete in-flight jobs; automate patching. | 16-Managing-Runners.md | Medium |
| 81 | B | IP allowlist restricts runners by registration IP at organization/enterprise level. | 16-Managing-Runners.md | Medium |
| 82 | A, B, C | Required workflows automatic; cannot be disabled; PR check integration. | 17-GitHub-Actions-Enterprise.md | Medium |
| 83 | A | All valid context availability and scope combinations listed. | 03-Context-Availability-Reference.md | Hard |
| 84 | B | "Allow local actions only" permits organization/enterprise actions. | 17-GitHub-Actions-Enterprise.md | Medium |
| 85 | B | Second approval triggers immediate deployment continuation without additional wait. | 08-Environment-Protection-Rules.md | Hard |
| 86 | A, B, C | Multiple valid action referencing and permission configuration methods supported. | 13-Workflows-REST-API.md | Medium |
| 87 | B | IP allowlist restricts by registration IP; runners outside range inaccessible. | 17-GitHub-Actions-Enterprise.md | Hard |
| 88 | B | All audit logging capabilities available for security and compliance auditing. | 17-GitHub-Actions-Enterprise.md | Hard |
| 89 | A, B, D, E | Multiple valid OIDC federation and secret handling scenarios. | 18-Security-and-Optimization.md | Hard |
| 90 | B | Setting secrets as env vars and referencing prevents code injection attacks. | 18-Security-and-Optimization.md | Hard |
| 91 | B | Artifact retention follows GitHub immutability and cleanup policies. | 09-Workflow-Artifacts.md | Medium |
| 92 | B | All OIDC federation statements regarding cloud provider integration correct. | 18-Security-and-Optimization.md | Hard |
| 93 | A, B, D | Multiple factors can cause secret masking failures or missing secrets. | 18-Security-and-Optimization.md | Hard |
| 94 | C | All factors contribute to action security assessment and verification. | 18-Security-and-Optimization.md | Hard |
| 95 | A | Parallelizing independent jobs (build and test) minimizes overall runtime. | 19-Common-Failures-Troubleshooting.md | Medium |
| 96 | A, B | Multiple valid approaches for artifact storage and workflow optimization. | 19-Common-Failures-Troubleshooting.md | Medium |
| 97 | A | Branch filter `main` requires exact match; no pattern wildcards by default. | 19-Common-Failures-Troubleshooting.md | Medium |
| 98 | A, B, C, D | All workflow optimization strategies listed are valid and effective. | 19-Common-Failures-Troubleshooting.md | Medium |
| 99 | B | Missing secrets result from scope misconfiguration or incorrect reference syntax. | 19-Common-Failures-Troubleshooting.md | Hard |
| 100 | A, B, D | Matrix job failure skips dependent jobs; use `if: always()` to override default behavior. | 19-Common-Failures-Troubleshooting.md | Hard |

---

*End of GH-200 Iteration 2 — 100 Questions*
