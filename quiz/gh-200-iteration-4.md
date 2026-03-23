# GitHub Actions GH-200 Certification Exam — Question Bank (Iteration 4)

**Iteration**: 4

**Generated**: 2026-03-20

**Total Questions**: 100

**Difficulty Split**: 20 Easy / 60 Medium / 20 Hard

**Answer Types**: 71 `one` / 27 `many` / 2 `all`

---

## Questions

---

### Question 1 — GitHub Actions VS Code Extension

**Difficulty**: Easy
**Answer Type**: one
**Topic**: VS Code Extension

**Question**:
A developer opens a `.github/workflows/build.yml` file in VS Code. What feature automatically appears when they type `${{` inside a step's `run:` command?

- A) A file browser showing all secrets in the repository
- B) An autocomplete list of available contexts (github, env, secrets, matrix, etc.)
- C) A terminal window showing the current workflow execution
- D) A code formatter that restructures the workflow syntax

---

### Question 2 — GitHub Actions VS Code Extension

**Difficulty**: Medium
**Answer Type**: one
**Topic**: VS Code Extension

**Question**:
When a developer hovers over `actions/checkout@v4` in a workflow, what does the GitHub Actions VS Code extension display?

- A) A changelog of all updates to the checkout action since v1
- B) The action's metadata from its `action.yml`, including inputs, outputs, and description
- C) Real-time download statistics for that version across all GitHub repositories
- D) Links to the action author's GitHub profile and bio

---

### Question 3 — GitHub Actions VS Code Extension

**Difficulty**: Medium
**Answer Type**: many
**Topic**: VS Code Extension

**Question**:
Which of the following are capabilities provided by the GitHub Actions VS Code extension? *(Select all that apply.)*

- A) Real-time YAML schema validation for workflow syntax
- B) Error highlighting when a context variable is used outside its scope
- C) Running workflows directly from the editor on your local machine
- D) Autocomplete for permission scope names in `permissions:` blocks
- E) Fetching and displaying action metadata with hover previews

---

### Question 4 — GitHub Actions VS Code Extension

**Difficulty**: Hard
**Answer Type**: many
**Topic**: VS Code Extension

**Question**:
A team enables autocomplete in VS Code for GitHub Actions workflows. When a developer types `${{ github.event.pull_request` in the `env:` section of a workflow step, what does the extension correctly suggest? *(Select all that apply.)*

- A) The extension suggests properties like `number`, `title`, and `head.ref`
- B) The extension suggests these properties only if the workflow is triggered by `pull_request` events
- C) For workflows without `pull_request` triggers, the extension warns that this context may be undefined
- D) The extension displays all available event properties regardless of the actual triggers configured

---

### Question 5 — Contextual Information

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contextual Information

**Question**:
A workflow is triggered by a `push` event to the `main` branch. Which expression correctly retrieves the full Git commit SHA?

- A) `${{ github.ref }}`
- B) `${{ github.sha }}`
- C) `${{ github.head_sha }}`
- D) `${{ runner.commit }}`

---

### Question 6 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contextual Information

**Question**:
During a pull request workflow, a step needs to reference the base branch name (target branch). Which context property provides this?

- A) `github.ref_name`
- B) `github.base_ref`
- C) `github.target_branch`
- D) `github.compare_ref`

---

### Question 7 — Contextual Information

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Contextual Information

**Question**:
A step in a workflow needs to access output values from a previous step with `id: build`. Given the condition that the prior step has `id: build` and defined an output named `image-tag`, which expressions correctly access it? *(Select all that apply.)*

- A) `${{ steps.build.outputs.image-tag }}`
- B) `${{ steps['build'].outputs['image-tag'] }}`
- C) `${{ job.steps.build.outputs.image-tag }}`
- D) `${{ env.build_image_tag }}`

---

### Question 8 — Contextual Information

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Contextual Information

**Question**:
A workflow uses a matrix strategy with properties `node-version: [16, 18, 20]`. The `strategy` context is available within the job. Which property from `strategy` context returns the **count** of jobs in the entire matrix expansion?

- A) `strategy.job-index`
- B) `strategy.max-parallel`
- C) `strategy.job-total`
- D) `strategy.job-count`

---

### Question 9 — Context Availability Reference

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Context Availability Reference

**Question**:
At which locations in a workflow file is the `secrets` context available? *(Select all that apply.)*

- A) In the workflow-level `env:` section
- B) In a job's `environment:` section
- C) In a step's `run:` command
- D) In `if:` conditions at the workflow level
- E) In a step's `with:` section when passing inputs to an action

---

### Question 10 — Context Availability Reference

**Difficulty**: Hard
**Answer Type**: all
**Topic**: Context Availability Reference

**Question**:
A developer writes the following workflow:

```yaml
env:
  BUILD_CONFIG: ${{ secrets.BUILD_SECRET }}

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - run: echo ${{ secrets.ANOTHER_SECRET }}
```

Which statements about this workflow are correct? *(Select all that apply.)*

- A) The `env:` section attempts to use `secrets` context, which causes a workflow file error
- B) The step's `run:` command can successfully access `ANOTHER_SECRET` at runtime
- C) Secrets are available in workflow-level `env:`, but their values are masked in logs
- D) The `secrets` context is only available within job steps, not at workflow scope

---

### Question 11 — Workflow File Structure

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Workflow File Structure

**Question**:
What YAML structure option allows you to trigger a workflow on either `push` events OR scheduled times?

- A) Use `on: [push, schedule]` with the schedule as a cron expression
- B) Use `on:` with `push:` and `schedule:` as separate sections
- C) Create two separate `on:` blocks in the same file
- D) Workflows can only respond to one type of trigger event

---

### Question 12 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow File Structure

**Question**:
A workflow defines the following structure:

```yaml
name: CI Workflow

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
```

If a developer pushes to the `develop` branch, what happens?

- A) The workflow triggers, but outputs a warning that the branch is not monitored
- B) The workflow does not trigger because `push` is configured only for `main`
- C) The workflow triggers on all push events regardless of branch filters
- D) The workflow skips the `push` trigger but still checks for `pull_request` events

---

### Question 13 — Workflow File Structure

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Workflow File Structure

**Question**:
A team refactors their workflow to use `env:` variables at the workflow level. Which of the following are accurate statements about workflow-level environment variables? *(Select all that apply.)*

- A) They are available in all jobs and steps of the workflow
- B) They cannot be overridden at the job or step level
- C) Job-level `env:` definitions override workflow-level variables with the same name
- D) Step-level `env:` definitions are merged with (not replacing) job-level variables
- E) Workflow-level env vars are accessible in the `env` context as `${{ env.VAR_NAME }}`

---

### Question 14 — Workflow Trigger Events

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Workflow Trigger Events

**Question**:
Which trigger event fires whenever a commit is pushed to any branch in the repository?

- A) `on: push`
- B) `on: push-all`
- C) `on: code-change`
- D) `on: repository-push`

---

### Question 15 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow Trigger Events

**Question**:
A workflow needs to run every Monday at 9:00 AM UTC. Which trigger event and cron syntax are correct?

- A) `on: schedule: cron: '0 9 * * 1'` (Monday = day 1)
- B) `on: schedule: cron: '0 9 * * 0'` (Monday = day 0)
- C) `on: cron: '9 0 * * MON'`
- D) `on: daily-schedule: '9:00 AM'`

---

### Question 16 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Workflow Trigger Events

**Question**:
A development team wants to trigger a workflow manually from the GitHub UI. Which of the following trigger configurations enable manual triggering? *(Select all that apply.)*

- A) `on: workflow_dispatch:`
- B) `on: workflow_dispatch: with: inputs:`
- C) `on: manual-trigger:`
- D) Manual triggering requires a personal GitHub App token in the workflow file
- E) `on: push:` workflows can be re-run manually from the UI even without `workflow_dispatch`

---

### Question 17 — Workflow Trigger Events

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Workflow Trigger Events

**Question**:
A public repository uses `pull_request_target` to run workflows triggered by PRs from external forks. A developer pushes a malicious commit to their fork, which modifies the workflow file itself before opening a PR. What is the security implication?

- A) GitHub prevents the workflow from running because the workflow file was modified in an external fork
- B) The modified workflow runs with write access and secrets available, potentially exposing sensitive data
- C) GitHub automatically signs the modified workflow file to prevent unauthorized changes
- D) The workflow runs but only with read-only access, preventing actual damage

---

### Question 18 — Custom Environment Variables

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Custom Environment Variables

**Question**:
How do you reference a custom environment variable named `DEPLOY_ENV` in a workflow step?

- A) `${{ DEPLOY_ENV }}`
- B) `${{ env.DEPLOY_ENV }}`
- C) `$DEPLOY_ENV` (shell expansion)
- D) Both B and C are correct

---

### Question 19 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Custom Environment Variables

**Question**:
A workflow defines environment variables at both workflow and job levels:

```yaml
env:
  LOG_LEVEL: info

jobs:
  build:
    env:
      LOG_LEVEL: debug
    steps:
      - run: echo ${{ env.LOG_LEVEL }}
```

What is printed by the step?

- A) `info` (workflow-level value takes precedence)
- B) `debug` (job-level value overrides workflow value)
- C) Both values are concatenated: `info debug`
- D) An error because environment variables cannot be overridden

---

### Question 20 — Custom Environment Variables

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Custom Environment Variables

**Question**:
A development team uses custom environment variables to control deployment behavior. Which statements about environment variables in GitHub Actions are correct? *(Select all that apply.)*

- A) Environment variables defined in `env:` at workflow level are accessible in all jobs
- B) Secret values can be passed as environment variables, but they are automatically masked in logs
- C) Job-level `env:` variables are inherited from workflow-level `env:` and can be selectively overridden
- D) Character limits: environment variables can be up to 64 KB in total size per job
- E) Environment variables persist across workflow runs within the same repository

---

### Question 21 — Default Environment Variables

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Default Environment Variables

**Question**:
GitHub automatically provides which default environment variable containing the directory where the runner downloads and executes actions?

- A) `GITHUB_WORKSPACE`
- B) `RUNNER_WORKSPACE`
- C) `GITHUB_ACTION_PATH`
- D) `RUNNER_TEMP`

---

### Question 22 — Default Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Default Environment Variables

**Question**:
What is the purpose of the `GITHUB_STEP_SUMMARY` default environment variable?

- A) To log all errors reported by the current step
- B) To write rich Markdown that appears in the workflow run summary page
- C) To cache outputs between steps automatically
- D) To control the verbosity of step output logs

---

### Question 23 — Default Environment Variables

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Default Environment Variables

**Question**:
A workflow developer checks GitHub's official documentation to identify which default environment variables are actually provisioned by GitHub and available to steps. Which of the following are documented as standard default variables? *(Select all that apply.)*

- A) `GITHUB_EVENT_PATH` – path to the full event payload JSON file
- B) `RUNNER_OS` – the operating system of the runner
- C) `GITHUB_STEP_EXECUTION_TIME` – milliseconds elapsed since the step started execution
- D) `RUNNER_TEMP` – temporary directory on the runner for temporary files
- E) `GITHUB_SHA` – the commit SHA that triggered the workflow

---

### Question 24 — Default Environment Variables

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Default Environment Variables

**Question**:
A workflow step writes output to `$GITHUB_OUTPUT` (the modern approach). On a subsequent step, that value needs to be accessed. Which approach correctly retrieves the previous step's output?

- A) `${{ env.OUTPUT_NAME }}`
- B) `${{ steps.step-id.outputs.OUTPUT_NAME }}`
- C) `${{ github.outputs.OUTPUT_NAME }}`
- D) `${{ GITHUB_OUTPUT.OUTPUT_NAME }}`

---

### Question 25 — Environment Protection Rules

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Environment Protection Rules

**Question**:
An organization wants to require approval before any workflow deployment to production. Which GitHub feature enables this?

- A) Action policies in organization settings
- B) Environment protection rules with required reviewers
- C) Pull request branch protection rules
- D) Workflow file-level `approval:` blocks

---

### Question 26 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Environment Protection Rules

**Question**:
A deployment environment `production` is configured with environment protection rules requiring approval from the `release-team`. A workflow runs `deploy-to-prod` with `environment: production`. What happens when a reviewer rejects the deployment?

- A) The job pauses indefinitely, waiting for a new approval request
- B) The job continues to the next step with a warning
- C) The deployment job fails, and the workflow run is marked as failed
- D) The rejection is logged but does not affect workflow execution

---

### Question 27 — Environment Protection Rules

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Environment Protection Rules

**Question**:
A team configures the `staging` environment with the following protection rules:

- Required reviewers: `release-ops` team
- Deployment branches: `main` and `develop`
- Wait timer: 10 minutes

When a workflow on the `feature/new-feature` branch attempts to deploy to `staging`, what is the behavior? *(Select all that apply.)*

- A) The deployment is blocked because the branch is not in the allowed list
- B) The deployment waits 10 minutes before requesting reviewer approval
- C) The workflow job enters a pending state, awaiting approval from `release-ops`
- D) An approval is requested immediately, regardless of the wait timer setting
- E) If approved, the deployment proceeds after the 10-minute wait timer expires

---

### Question 28 — Workflow Artifacts

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Workflow Artifacts

**Question**:
Which action is used to upload files produced by a workflow as artifacts?

- A) `actions/upload-artifact`
- B) `actions/store-artifact`
- C) `github/upload-files`
- D) `actions/save-output`

---

### Question 29 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow Artifacts

**Question**:
An artifact uploaded with `retention-days: 5` is set to be automatically deleted after 5 days. What is the minimum retention period enforced by GitHub?

- A) 0 days (artifacts can be deleted immediately)
- B) 1 day
- C) 7 days
- D) Retention period is entirely user-controlled with no minimum

---

### Question 30 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Workflow Artifacts

**Question**:
A workflow uploads a build artifact and needs to share it with a downstream job in the same workflow run. Which approaches accomplish this? *(Select all that apply.)*

- A) Use `actions/download-artifact` in the downstream job to retrieve the artifact
- B) Use `needs: <job-id>` and access the artifact via step outputs from the prior job
- C) Download from the GitHub UI, then manually re-upload in the downstream job
- D) Jobs in the same workflow run can access artifacts uploaded by prior jobs if in the same workspace
- E) Artifacts are job-isolated; cross-job sharing requires external storage (S3, blob storage, etc.)

---

### Question 31 — Workflow Artifacts

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Workflow Artifacts

**Question**:
A GitHub organization enforces an artifact retention policy maximum of 30 days. A team uploads an artifact with `retention-days: 45` specified in their workflow. What happens?

- A) The artifact is retained for 45 days because the workflow specification takes precedence
- B) The artifact is retained for 30 days; the organization's policy maximum is enforced
- C) The workflow fails with an error because the retention period exceeds policy
- D) The retention period is set to 15 days (organization max minus workflow excess)

---

### Question 32 — Workflow Caching

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Workflow Caching

**Question**:
What does the `actions/cache` action primarily do?

- A) Stores secrets securely across workflow runs
- B) Caches dependencies or build outputs to speed up subsequent runs
- C) Caches workflow execution logs for auditing
- D) Compresses artifacts to reduce storage costs

---

### Question 33 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow Caching

**Question**:
A workflow uses `actions/cache` with the following key and restore-keys:

```yaml
key: pip-cache-${{ hashFiles('requirements.txt') }}
restore-keys: |
  pip-cache-
```

When `requirements.txt` changes (hash changes), what is the cache behavior?

- A) The old cache is used if available; a new cache key is created using the new hash
- B) No cache is found (exact key mismatch), the `requirements.txt` is processed fresh, and a new cache entry is saved
- C) The cache is invalidated and a full rebuild is forced
- D) Partial matches trigger the restore-key, old dependencies are installed, then new dependencies are added on top

---

### Question 34 — Workflow Caching

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Workflow Caching

**Question**:
A cache is restored from a partial match using `restore-keys:`. Which statements about this behavior are correct? *(Select all that apply.)*

- A) The restored cache contains all data from the prior cache entry (oldest to newest by timestamp)
- B) If multiple cache entries match the restore-key pattern, the most recent one is restored
- C) Subsequent modifications to cached content are automatically merged with the original cache
- D) A new cache entry is created under the exact key after the job completes
- E) Partial matches are a fallback; they don't guarantee data consistency with the current job's dependencies

---

### Question 35 — Workflow Sharing

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Workflow Sharing

**Question**:
Which built-in workflow trigger event allows a workflow to be called from another workflow as a reusable workflow?

- A) `on: workflow_run`
- B) `on: workflow_call`
- C) `on: workflow_dispatch`
- D) `on: external_workflow`

---

### Question 36 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow Sharing

**Question**:
When a calling workflow uses `secrets: inherit`, what is passed to the reusable workflow?

- A) A digest/hash of all secrets (not the actual values)
- B) All secrets available to the caller, with the same names accessible in the called workflow
- C) Only explicitly declared secrets in `on.workflow_call.secrets:` of the called workflow
- D) Environment variables, but not actual secret values

---

### Question 37 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Workflow Sharing

**Question**:
A reusable workflow declares outputs and a calling workflow wants to access them. Which approaches are necessary? *(Select all that apply.)*

- A) The reusable workflow declares outputs under `on.workflow_call.outputs:`
- B) Each output in `on.workflow_call.outputs:` references a job-level output via `value: ${{ jobs.<job-id>.outputs.<name> }}`
- C) The caller declares `needs: <called-job-id>` to reference the reusable workflow
- D) The caller accesses outputs using `${{ needs.<called-job-id>.outputs.<output-name> }}`
- E) Reusable workflow outputs are automatically copied to the caller's `env:` context

---

### Question 38 — Workflow Sharing

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Workflow Sharing

**Question**:
A reusable workflow in a public repository is called by external organizations with `secrets: inherit`. Which best practice should the reusable workflow team implement to reduce credential exposure?

- A) Never accept `secrets: inherit`; use explicit secret mapping instead
- B) Document that external callers should not use `secrets: inherit`
- C) Use explicit secret mapping: `secrets: deploy-key: ${{ secrets.MY_TOKEN }}`
- D) Implement inline validation within the reusable workflow to reject calls from untrusted organizations

---

### Question 39 — Workflow Debugging

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Workflow Debugging

**Question**:
A developer wants to enable debug logging for all workflow runs without modifying the workflow file. What is the correct approach?

- A) Set a repository setting: Settings → Actions → Enable debug logging
- B) Create a repository secret named `ACTIONS_STEP_DEBUG` with value `true`
- C) Append `?debug=true` to the workflow URL
- D) Use the `gh` CLI: `gh workflow run <workflow> --debug`

---

### Question 40 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow Debugging

**Question**:
A workflow step outputs the following workflow command:

```bash
echo "::warning::Deprecated library detected in dependencies"
```

Where does this warning appear in the GitHub UI?

- A) Only in the raw step log output
- B) As a warning annotation in the workflow run summary and as an annotation on associated commits/PRs
- C) As an automatically created GitHub Issue
- D) Only in the `GITHUB_STEP_SUMMARY` file

---

### Question 41 — Workflow Debugging

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Workflow Debugging

**Question**:
Which workflow commands can be used in a `run:` step to structure log output and provide debugging information? *(Select all that apply.)*

- A) `echo "::group::Log Section Name"` – begin a collapsible log group
- B) `echo "::endgroup::"` – end a collapsible log group
- C) `echo "::debug::Debug message"` – emit a debug-level annotation
- D) `echo "::set-output name=var::value"` – modern way to set step outputs
- E) `echo "::error::Error occurred"` – emit an error-level annotation

---

### Question 42 — Workflows REST API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Workflows REST API

**Question**:
Which REST API endpoint triggers a `workflow_dispatch` workflow manually?

- A) `POST /repos/{owner}/{repo}/actions/workflows/{id}/trigger`
- B) `POST /repos/{owner}/{repo}/actions/workflows/{id}/dispatches`
- C) `POST /repos/{owner}/{repo}/workflows/{id}/run`
- D) `POST /repos/{owner}/{repo}/actions/run-workflow/{id}`

---

### Question 43 — Workflows REST API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflows REST API

**Question**:
An API client queries the workflow runs endpoint:

```
GET /repos/{owner}/{repo}/actions/runs?status=failure
```

What does this query return?

- A) All workflow runs that are currently executing and may potentially fail
- B) Only workflow runs with a conclusion status of `failure`
- C) Workflow runs grouped by failure reason
- D) The latest workflow run that has failed, only

---

### Question 44 — Workflows REST API

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Workflows REST API

**Question**:
A workflow run API response includes which properties? *(Select all that apply.)*

- A) `status` – current state of the workflow run (queued, in_progress, completed)
- B) `conclusion` – outcome of the workflow run (success, failure, neutral, cancelled, etc.)
- C) `run_number` – sequence number of this workflow run within the repository
- D) `job_details` – array of nested job objects with step-by-step information
- E) `head_branch` – the branch that triggered the workflow run

---

### Question 45 — Reviewing Deployments

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Reviewing Deployments

**Question**:
An environment named `production` is configured with required reviewers. When a workflow job accesses this environment, what is the standard behavior during the review process?

- A) The job immediately uploads deployment logs to the reviewer
- B) The job enters a pending state and awaits reviewer approval or rejection
- C) The job times out after 6 hours if reviewers don't respond
- D) Reviewers are notified but the job continues immediately; reviewers can roll back after

---

### Question 46 — Reviewing Deployments

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reviewing Deployments

**Question**:
An enterprise uses deployment environment protection rules with a 30-minute wait timer and required reviewers. A reviewer approves a pending deployment immediately after the job enters waiting state. What happens?

- A) The deployment proceeds immediately because the reviewer approved it
- B) The deployment waits the full 30 minutes before proceeding, even after approval
- C) The deployment waits for approval (already received), then waits the remaining timer duration
- D) The wait timer is canceled once approval is received; deployment proceeds immediately

---

### Question 47 — Reviewing Deployments

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Reviewing Deployments

**Question**:
A team configures environment protection rules for the `staging` environment with the following settings:

- Required reviewers: `platform-team` (can be users or teams)
- Deployment branches: `main` only
- Wait timer: 5 minutes

Analyze each scenario and determine which result in blocked, failed, or delayed deployments. Which statements are correct? *(Select all that apply.)*

- A) A deployment from a `develop` branch is blocked immediately (branch not in allowed list)
- B) A deployment from `main` without any reviewer approval results in the job failing after the wait timer expires
- C) A deployment from `main` receives a reviewer approval, then waits 3 minutes before proceeding (shorter than the 5-minute timer)
- D) A deployment from `main` receives reviewer approval, waits the full 5-minute timer, then proceeds successfully
- E) A deployment from `main` without required approval request will timeout and fail

---

### Question 48 — Creating and Publishing Actions

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Creating and Publishing Actions

**Question**:
What file must be present in a custom action repository to define the action's inputs, outputs, and runtime configuration?

- A) `config.json`
- B) `action.yml` or `action.yaml`
- C) `Actionfile`
- D) `package.json` (for all action types)

---

### Question 49 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Creating and Publishing Actions

**Question**:
A custom action is created with `runs: using: javascript` (or `node20`) and `runs: main: index.js`. When this action is called from a workflow, what happens?

- A) GitHub builds a Docker container from the `Dockerfile` and runs it
- B) GitHub directly executes `index.js` using Node.js on the runner
- C) GitHub uploads the action to the Marketplace first, then executes it
- D) GitHub compiles the action to a binary and runs the compiled version

---

### Question 50 — Creating and Publishing Actions

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Creating and Publishing Actions

**Question**:
Which values are valid for `runs.using:` in an action.yml file? *(Select all that apply.)*

- A) `node20`
- B) `docker`
- C) `composite`
- D) `python3`
- E) `bash`

---

### Question 51 — Managing Runners

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Managing Runners

**Question**:
A self-hosted runner needs to be targeted by a workflow. The runner has the labels `self-hosted` and `gpu`. How is the `runs-on:` configured in the workflow?

- A) `runs-on: gpu`
- B) `runs-on: [self-hosted, gpu]`
- C) `runs-on: self-hosted+gpu`
- D) `runs-on: self-hosted | gpu`

---

### Question 52 — Managing Runners

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Managing Runners

**Question**:
A workflow specifies `runs-on: [self-hosted, windows, arm64]`, requiring a self-hosted runner with all three labels. The organization has runners with:

- Runner A: `[self-hosted, windows, x64]`
- Runner B: `[self-hosted, windows, arm64]`
- Runner C: `[self-hosted, linux, arm64]`

What is the behavior?

- A) Runner B is selected because it matches all labels
- B) Runner A is selected as the closest match
- C) The job queues indefinitely because no runner matches all labels exactly
- D) An error is raised and the workflow fails immediately

---

### Question 53 — Managing Runners

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Managing Runners

**Question**:
A self-hosted runner stops responding mid-job execution. Which of the following occur? *(Select all that apply.)*

- A) The job is marked as failed; the in-progress job state is not recovered
- B) Subsequent jobs on the same runner execute normally after re-registration
- C) The job is automatically re-queued on another available runner
- D) Files modified by the job remain on the crashed runner's disk
- E) GitHub automatically removes the offline runner from the available runners list after 30 days

---

### Question 54 — GitHub Actions Enterprise

**Difficulty**: Easy
**Answer Type**: one
**Topic**: GitHub Actions Enterprise

**Question**:
An enterprise admin wants to prevent workflows from using unapproved third-party actions. Which policy setting accomplishes this?

- A) Workflow protection policy
- B) Action allow-list policy requiring explicit approval of actions
- C) Third-party action defense policy
- D) Security review policy

---

### Question 55 — GitHub Actions Enterprise

**Difficulty**: Medium
**Answer Type**: one
**Topic**: GitHub Actions Enterprise

**Question**:
An organization's action policy is set to "Allow only local actions". A workflow references `docker/build-push-action@v4`. What is the result?

- A) The workflow runs successfully; GitHub trusts verified creator actions
- B) The step fails at runtime with a policy violation error
- C) The workflow is rejected at parse time before execution
- D) Only actions from the same organization are allowed; `docker/build-push-action` is blocked

---

### Question 56 — GitHub Actions Enterprise

**Difficulty**: Hard
**Answer Type**: many
**Topic**: GitHub Actions Enterprise

**Question**:
Enterprise-level features for GitHub Actions governance include which of the following? *(Select all that apply.)*

- A) Required workflows that automatically run on all repositories in selected orgs
- B) IP allowlists to restrict workflow execution to specific IP ranges
- C) Action policies enforced across all repositories in the enterprise
- D) Mandatory code signing for all custom actions
- E) Runner groups with repository and organization access controls

---

### Question 57 — GitHub Actions Enterprise

**Difficulty**: Hard
**Answer Type**: one
**Topic**: GitHub Actions Enterprise

**Question**:
GitHub Enterprise Cloud customers enable the "GitHub Actions" entry in the organization's IP allowlist. What is the benefit of this approach compared to manual IP management?

- A) The allow list is automatically updated whenever GitHub's runner IP ranges change
- B) All external API calls from runners are blocked except GitHub services
- C) The IP allowlist provides geographic routing for faster runner performance
- D) It ensures all runners use the same public IP address for audit logging

---

### Question 58 — Security and Optimization

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Security and Optimization

**Question**:
What is the primary security advantage of using OIDC-based cloud authentication in GitHub Actions?

- A) It encrypts all workflow logs automatically
- B) It eliminates the need to store long-lived credentials (like personal access tokens) as secrets
- C) It prevents fork-based workflows from executing
- D) It signs all artifacts produced by the workflow

---

### Question 59 — Security and Optimization

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Security and Optimization

**Question**:
A workflow uses a GitHub Action stored in a public repository. The best practice for security is to reference the action by which identifier?

- A) By tag name: `owner/repo@v1.0.0`
- B) By branch name: `owner/repo@main`
- C) By full commit SHA: `owner/repo@e1c3a851c5caf1e2370a8d9ef4a18a1f6f26f34`
- D) By release version: `owner/repo@latest`

---

### Question 60 — Security and Optimization

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Security and Optimization

**Question**:
A development team has implemented secure GitHub Actions practices. Which statements about security best practices are correct? *(Select all that apply.)*

- A) Secrets should never be passed as environment variables because they are logged in plaintext
- B) Script injection is prevented by using env vars instead of directly interpolating user input into shell commands
- C) `GITHUB_TOKEN` permissions should be set to the minimum necessary (principle of least privilege)
- D) All GitHub Actions should be pinned to commit SHAs to prevent supply chain attacks
- E) Public repositories should use `pull_request` (not `pull_request_target`) for security

---

### Question 61 — Security and Optimization

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Security and Optimization

**Question**:
A workflow step executes a script that includes user-provided input from a PR title. The step is written as:

```bash
./deploy.sh "${{ github.event.pull_request.title }}"
```

What is the security vulnerability and recommended mitigation?

- A) SQL injection is possible; use parameterized queries instead
- B) Script injection is possible; assign the value to an env var and reference it as `$ENV_VAR` (not through `${{ }}`)
- C) The PR title is always sanitized by GitHub; no vulnerability exists
- D) Use single quotes instead of double quotes to prevent interpolation

---

### Question 62 — Security and Optimization

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Security and Optimization

**Question**:
Which statements about `GITHUB_TOKEN` and security are accurate? *(Select all that apply.)*

- A) `GITHUB_TOKEN` is automatically provisioned at the start of each job with a limited lifetime
- B) `GITHUB_TOKEN` cannot trigger new workflow runs, preventing infinite loops
- C) Default `GITHUB_TOKEN` permissions can be set to read-only at the organization level
- D) `GITHUB_TOKEN` can be used across repositories in the same organization without additional configuration
- E) Job-level `permissions:` blocks can restrict `GITHUB_TOKEN` scope to only necessary actions

---

### Question 63 — Common Failures and Troubleshooting

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Common Failures and Troubleshooting

**Question**:
A workflow step fails with the error:

```
fatal: could not read Username for 'https://github.com': No such file or directory
```

What is the most likely cause?

- A) The GitHub service is down
- B) Missing or invalid authentication token (likely missing `actions/checkout`)
- C) Network connectivity issues
- D) SSH key misconfiguration

---

### Question 64 — Common Failures and Troubleshooting

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Common Failures and Troubleshooting

**Question**:
An `npm ci` step fails with the error:

```
npm ERR! code ERESOLVE
npm ERR! ERESOLVE could not resolve dependencies
```

What is a quick diagnostic and temporary workaround?

- A) Downgrade Node.js to an earlier version
- B) Delete `package-lock.json` and use `npm install` instead
- C) Use `npm ci --legacy-peer-deps` to relax peer dependency constraints
- D) Update npm to the latest version globally

---

### Question 65 — Common Failures and Troubleshooting

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Common Failures and Troubleshooting

**Question**:
A self-hosted runner appears online in GitHub but jobs assigned to it remain queued indefinitely. Which troubleshooting steps are appropriate? *(Select all that apply.)*

- A) Verify the runner's labels match the `runs-on:` labels in the workflow
- B) Check the runner process status: `systemctl status actions.runner` (on Linux)
- C) Re-register the runner with a new token to refresh its credentials
- D) Restart the GitHub Actions runner service
- E) Increase the queue timeout setting in the workflow file

---

### Question 66 — Common Failures and Troubleshooting

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Common Failures and Troubleshooting

**Question**:
A build fails after `actions/setup-node@v3` with Node.js 18.16.0, but locally the same build succeeds with the same Node.js version. What is the most likely cause?

- A) The GitHub Actions runner's Node.js installation is corrupted
- B) GitHub Actions uses a different npm cache than the local environment; lock file is out of sync
- C) The setup-node action is caching an outdated version from a prior run
- D) Node.js version 18.16.0 is not available in GitHub's hosted runner image

---

### Question 67 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contextual Information

**Question**:
A workflow needs to identify the user who originally triggered a workflow run. Which `github` context property provides this, distinct from who re-ran it?

- A) `github.triggering_actor`
- B) `github.actor`
- C) `github.run_actor`
- D) `github.initiator`

---

### Question 68 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow Trigger Events

**Question**:
A workflow uses `on: workflow_run:` with `types: [completed]`. What does this accomplish?

- A) Triggers this workflow when another specified workflow completes
- B) Triggers this workflow on every push event
- C) Waits for a manually triggered workflow to complete before starting
- D) Creates a chain of dependent workflows that run sequentially

---

### Question 69 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Custom Environment Variables

**Question**:
A workflow defines a matrix strategy with `node-version: [16, 18]`. A step needs to reference the current matrix value. Which context property provides this?

- A) `${{ matrix.node-version }}`
- B) `${{ strategy.matrix.node-version }}`
- C) `${{ env.NODE_VERSION }}`
- D) `${{ runner.node-version }}`

---

### Question 70 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow Artifacts

**Question**:
A workflow uploads multiple artifacts in separate steps. Each uses `actions/upload-artifact` with different `name:` values. How are these artifacts accessed in the GitHub UI?

- A) All artifacts are merged into a single download
- B) Each artifact is available as a separate download with its specified name
- C) Only the last uploaded artifact is retained
- D) All artifacts are combined into a `.zip` file

---

### Question 71 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow Caching

**Question**:
A workflow uses `actions/cache` to cache dependencies. On a cache hit, which outcome is typical?

- A) The cache directory is restored, then dependency installation is skipped
- B) The cache directory is restored, then dependency installation proceeds (possibly updating cached items)
- C) The entire job skips execution if cache is found
- D) Cache content is merged with newly installed dependencies

---

### Question 72 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow Sharing

**Question**:
A reusable workflow requires certain inputs to be defined. How does a calling workflow pass these inputs?

- A) Using the `with:` block in the `jobs.<job>.uses:` statement
- B) Using environment variables in the calling workflow
- C) Using job-level `env:` definitions
- D) Inputs are automatically passed by GitHub based on the called workflow's requirements

---

### Question 73 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow Debugging

**Question**:
What does the `ACTIONS_RUNNER_DEBUG` secret (or `RUNNER_DEBUG=1` env var) enable?

- A) Debug logging for the GitHub Actions runner itself (diagnostic output)
- B) Debug mode for the workflow YAML parser
- C) Verbose output for custom actions only
- D) Performance profiling of the workflow execution

---

### Question 74 — Workflows REST API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflows REST API

**Question**:
An API client calls `POST /repos/{owner}/{repo}/actions/runs/{run_id}/cancel`. What is the expected behavior?

- A) Immediately stops the running workflow and marks it as cancelled
- B) Soft-cancels the workflow; in-progress jobs finish before marking it cancelled
- C) Schedules the workflow to be cancelled after current step completes
- D) Cancels only the current job, allowing other jobs to continue

---

### Question 75 — Reviewing Deployments

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reviewing Deployments

**Question**:
A workflow job references `environment: production` with required reviewers configured. An approver approves the deployment. The deployment of a downstream job that has `needs: production-deployment` begins immediately or waits for the environment approval?

- A) The downstream job begins immediately after the upstream job's approval (both jobs can run in parallel if resources permit)
- B) The downstream job waits for both approval and the upstream job's completion
- C) The downstream job does not have access to the environment's approval status
- D) The downstream job must have its own environment configuration and approval

---

### Question 76 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Creating and Publishing Actions

**Question**:
A custom JavaScript action uses `@actions/core` library to set an output. Which API call is correct?

- A) `core.setEnv('output-name', value)`
- B) `core.setOutput('output-name', value)`
- C) `core.exportVariable('output-name', value)`
- D) `core.setSecret('output-name', value)`

---

### Question 77 — Managing Runners

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Managing Runners

**Question**:
A team registers a self-hosted runner with custom labels. The runner is offline for maintenance. What happens to workflows queued for this runner?

- A) Jobs are automatically reassigned to other runners with similar labels
- B) Jobs remain queued, waiting for the runner to come back online
- C) Jobs are marked as failed and do not execute
- D) Jobs are moved to a failed queue and require manual intervention to retry

---

### Question 78 — GitHub Actions Enterprise

**Difficulty**: Medium
**Answer Type**: one
**Topic**: GitHub Actions Enterprise

**Question**:
Where does an enterprise admin configure required workflows that must run on all repositories in specific organizations?

- A) Organization Settings → Actions → Required Workflows
- B) Repository Settings → Branches → Protection Rules
- C) Enterprise Settings → Policies → Required Workflows
- D) GitHub Settings → Security → Compliance Workflows

---

### Question 79 — Security and Optimization

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Security and Optimization

**Question**:
A workflow requests an OIDC token for AWS authentication. The `id-token: write` permission is required. What does this permission do?

- A) Allows the workflow to request a signed JWT token from GitHub's OIDC provider
- B) Grants the workflow write access to GitHub's identity management system
- C) Enables two-factor authentication for the runner
- D) Allows the workflow to access AWS Identity Center (IdC) directly

---

### Question 80 — Common Failures and Troubleshooting

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Common Failures and Troubleshooting

**Question**:
A scheduled workflow (cron-triggered) does not run at the expected time. What is a common cause?

- A) GitHub requires manual approval to run cron workflows
- B) The cron expression is incorrect or the repository had no commits in the past 60 days
- C) Cron-triggered workflows only run on the default branch if explicitly configured
- D) The runner is overloaded and delays scheduled jobs

---

### Question 81 — Context Availability Reference

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Context Availability Reference

**Question**:
At workflow-level (outside any job), which of the following contexts are available?

- A) `github`, `env`, but NOT `secrets`
- B) `secrets` for use in `on.workflow_dispatch.secrets:`
- C) All contexts (`github`, `env`, `secrets`, `matrix`, etc.) are available
- D) No contexts are available at workflow scope; they're only available within jobs

---

### Question 82 — Workflow File Structure

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Workflow File Structure

**Question**:
A workflow is defined with `concurrency: group-name`. When a new run is triggered with the same `concurrency` group while an existing run is in progress, what is the default behavior?

- A) Both runs execute in parallel
- B) The newer run cancels the older run, then executes
- C) The newer run is queued and waits for the older run to complete
- D) The newer run is rejected with an error

---

### Question 83 — Default Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Default Environment Variables

**Question**:
A workflow step needs to write output that will appear in the workflow's run summary page (visible in GitHub UI). Which default environment variable file should be written to?

- A) `$GITHUB_OUTPUT`
- B) `$GITHUB_STEP_SUMMARY`
- C) `$GITHUB_SUMMARY`
- D) `$GITHUB_RUN_LOG`

---

### Question 84 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Environment Protection Rules

**Question**:
An environment has protection rules with required reviewers. A workflow attempts deployment from a pull request. Who receives the approval notification for the deployment review?

- A) The person who opened the pull request
- B) All members of the required reviewer team/role
- C) The repository owner only
- D) The workflow run's actor (whoever triggered the run)

---

### Question 85 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow Artifacts

**Question**:
A workflow uploads an artifact named `build-output` with `retention-days: 7`. Another workflow in the same repository needs to download and use this artifact. When is the artifact available for download?

- A) Immediately after upload, and remains available for 7 days
- B) After the current workflow completes, and remains available for 7 days
- C) Only within the same workflow run; cross-workflow artifact sharing requires a different approach
- D) After approval by a repository collaborator

---

### Question 86 — Workflow Caching

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Workflow Caching

**Question**:
A cache is restored from a partial match using `restore-keys`, then the job modifies cached files. What happens when the job completes? *(Select all that apply.)*

- A) A new cache entry is created using the exact key from `key:` (not the restore-key)
- B) The modified cache is merged into the original cache entry
- C) The old cache entry is left untouched; the new modified cache has a separate key
- D) GitHub automatically detects changes and updates both the old and new cache entries
- E) Subsequent runs restore the new cache entry (created with the current run's modified content)

---

### Question 87 — Workflow Sharing

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Workflow Sharing

**Question**:
A reusable workflow is configured to run only on the `main` branch. When called from a workflow on the `develop` branch, what happens?

- A) The reusable workflow runs on `develop` as inherited from the caller
- B) The reusable workflow's branch restriction is ignored; it follows the caller's context
- C) The reusable workflow always runs on `main`, even if called from `develop`
- D) A validation error occurs; the reusable workflow cannot define branch restrictions

---

### Question 88 — Workflow Debugging

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Workflow Debugging

**Question**:
A step prints sensitive information to stdout. What is GitHub's default behavior for handling this?

- A) Logs are automatically filtered to remove common patterns of secrets
- B) All console output is encrypted and only visible to repository admins
- C) The step fails with an error if sensitive data is detected
- D) Logs are visible subject to normal repository access permissions; no automatic filtering occurs

---

### Question 89 — Workflows REST API

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Workflows REST API

**Question**:
When querying the REST API for workflow runs, which filtering capabilities are available? *(Select all that apply.)*

- A) Filter by `status`: queued, in_progress, completed
- B) Filter by `conclusion`: success, failure, neutral, cancelled, skipped
- C) Filter by date range: created, updated
- D) Filter by actor (user who triggered the run)
- E) Automatically include step-level details in the runs list response

---

### Question 90 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Creating and Publishing Actions

**Question**:
A custom action requires a specific version of Node.js (e.g., 20.x). How is this best specified in the action?

- A) Using `runs: using: node20` in `action.yml`; specify exact version in documentation
- B) Creating a Dockerfile that installs the specific Node.js version
- C) Calling `actions/setup-node` as the first step within a composite action
- D) Adding `node: "20.x"` property to `action.yml`

---

### Question 91 — Managing Runners

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Managing Runners

**Question**:
A self-hosted runner is accidentally deleted from the organization. A workflow job was queued to run on this runner. What is the outcome?

- A) The job remains queued indefinitely, waiting for a runner with matching labels
- B) The job fails immediately with a "runner not found" error
- C) GitHub reassigns the job to a similar available runner
- D) The job is marked as skipped

---

### Question 92 — GitHub Actions Enterprise

**Difficulty**: Hard
**Answer Type**: many
**Topic**: GitHub Actions Enterprise

**Question**:
An enterprise configures action policies with "Allow select actions" and publishes an allow-list including `actions/checkout@*` and `github/*`. A workflow on a member organization attempts to use `docker/build-push-action@v5`. Which statements are accurate? *(Select all that apply.)*

- A) The workflow run fails because `docker/build-push-action` is not in the allow-list
- B) The step using `docker/build-push-action` fails with a policy violation at runtime
- C) Organization admins can override the enterprise policy for their organization
- D) The entire workflow is rejected at parse time before execution
- E) Admins can add `docker/build-push-action@*` to the allow-list to permit all versions

---

### Question 93 — Security and Optimization

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Security and Optimization

**Question**:
A GitHub Actions workflow runs in a private repository with credentials stored as secrets. When a pull request from an external fork triggers a `pull_request` event, what is the default security behavior?

- A) Secrets are available to the workflow; GitHub trusts all PRs equally
- B) Secrets are NOT available; the workflow runs with read-only access to prevent abuse
- C) Secrets are available only if the fork owner is an organization member
- D) The workflow does not execute at all; manual approval is always required

---

### Question 94 — GitHub Actions VS Code Extension

**Difficulty**: Hard
**Answer Type**: many
**Topic**: VS Code Extension

**Question**:
A developer creates a new workflow file in `.github/workflows/` and begins typing. The GitHub Actions extension provides multiple features. Which are automatically triggered without additional configuration? *(Select all that apply.)*

- A) Real-time YAML syntax validation
- B) Context autocomplete suggestions (github.*, env.*, etc.)
- C) Automatic suggestion for missing permission scopes
- D) Inline previews of what expressions will evaluate to
- E) Downloading and installing the latest action versions

---

### Question 95 — Common Failures and Troubleshooting

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Common Failures and Troubleshooting

**Question**:
A workflow run exhibits flaky behavior–sometimes passes, sometimes fails with inconsistent errors. Which are reasonable troubleshooting approaches for this scenario? *(Select all that apply.)*

- A) Check for race conditions in test setup/teardown (order-dependent failures)
- B) Verify that external service dependencies (APIs, databases) are responding consistently
- C) Increase the `timeout-minutes:` value to give jobs more time to complete
- D) Check if the runner's disk or memory is running low, causing intermittent failures
- E) Add `continue-on-error: true` to all steps to skip failures

---

### Question 96 — Contextual Information

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Contextual Information

**Question**:
A workflow accesses `${{ github.event.number }}`. In which trigger event context is this property available and contains the pull request number?

- A) `workflow_dispatch` events
- B) `pull_request` events
- C) `push` events
- D) `schedule` events

---

### Question 97 — Workflow Trigger Events

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Workflow Trigger Events

**Question**:
A `pull_request` workflow references `${{ github.event.action }}`. When a PR is opened, what value does this property have?

- A) `triggered`
- B) `opened`
- C) `created`
- D) `pr-opened`

---

### Question 98 — Custom Environment Variables

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Custom Environment Variables

**Question**:
Environment variable precedence in GitHub Actions follows a hierarchy. Which order is correct, from highest to lowest precedence? *(Select all that apply.)*

- A) Step-level `env:` > Job-level `env:` > Workflow-level `env:` > Default env vars
- B) Workflow-level `env:` > Job-level `env:` > Step-level `env:`
- C) Variables with the same name at different scopes: step-level overrides job-level, which overrides workflow-level
- D) All `env:` definitions are merged at the start of the workflow; no override mechanism exists
- E) The `GITHUB_TOKEN` is always available regardless of explicit `env:` definitions

---

### Question 99 — Workflow Artifacts

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Workflow Artifacts

**Question**:
How are artifacts typically accessed by end-users (developers) within a GitHub repository?

- A) Via the GitHub CLI: `gh run download <run-id>`
- B) Via the Actions tab in the GitHub UI → workflow run → artifacts
- C) By querying the REST API: `/repos/{owner}/{repo}/actions/runs/{id}/artifacts`
- D) All of the above

---

### Question 100 — Context Availability Reference

**Difficulty**: Hard
**Answer Type**: all
**Topic**: Context Availability Reference

**Question**:
A workflow step attempts to access various contexts. Which statements about context availability are correct? *(Select all that apply.)*

- A) `github` context is available at workflow level, job level, and step level
- B) `matrix` context is available only within jobs using a `strategy: matrix:`
- C) `secrets` context is available in step `run:` commands but not in `env:` blocks at workflow level
- D) `vars` context (organization/repository variables) is available in `env:` blocks
- E) `inputs` context is only available in composite actions and reusable workflows

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | B | An autocomplete list appears when typing `${{` to suggest available contexts. | 01-GitHub-Actions-VS-Code-Extension.md | Easy |
| 2 | B | The extension's hover tooltip displays action.yml metadata (inputs, outputs, description). | 01-GitHub-Actions-VS-Code-Extension.md | Medium |
| 3 | A, B, D, E | All are correct; C is incorrect (workflows run on GitHub, not locally from editor). | 01-GitHub-Actions-VS-Code-Extension.md | Medium |
| 4 | B, C | Only B and C are correct autocomplete behaviors; A and D are not guaranteed. | 01-GitHub-Actions-VS-Code-Extension.md | Hard |
| 5 | B | `${{ github.sha }}` retrieves the full Git commit SHA. | 02-Contextual-Information.md | Easy |
| 6 | B | `github.base_ref` contains the target/base branch name in pull request workflows. | 02-Contextual-Information.md | Medium |
| 7 | A, B | Both bracket notation and dot notation work for accessing step outputs. | 02-Contextual-Information.md | Medium |
| 8 | C | `strategy.job-total` returns the total count of jobs in the matrix expansion. | 02-Contextual-Information.md | Hard |
| 9 | C, E | Secrets context is available in steps' `run:` commands and action `with:` sections. | 03-Context-Availability-Reference.md | Medium |
| 10 | A, B, D, E | All statements are correct except C; secrets cannot be used in workflow-level `env:`. | 03-Context-Availability-Reference.md | Hard |
| 11 | B | `on: [push, schedule]` with both sections allows multiple trigger types. | 04-Workflow-File-Structure.md | Easy |
| 12 | B | Without branch filters, `push:` triggers on all branches; B is incorrect about `main` being required. | 04-Workflow-File-Structure.md | Medium |
| 13 | A, C, E | Workflow-level env vars are available everywhere; job/step levels override them. | 04-Workflow-File-Structure.md | Hard |
| 14 | A | `on: push` fires on all commits pushed to any branch. | 05-Workflow-Trigger-Events.md | Easy |
| 15 | A | Cron syntax `0 9 * * 1` represents 9:00 AM UTC on Monday (day 1). | 05-Workflow-Trigger-Events.md | Medium |
| 16 | A, B, E | `workflow_dispatch` enables manual triggering; E is correct (push workflows can be re-run). | 05-Workflow-Trigger-Events.md | Medium |
| 17 | B | `pull_request_target` grants write access and secrets, creating a security risk if workflow is modified. | 05-Workflow-Trigger-Events.md | Hard |
| 18 | D | Both `${{ env.DEPLOY_ENV }}` and shell expansion `$DEPLOY_ENV` work for referencing env vars. | 06-Custom-Environment-Variables.md | Easy |
| 19 | B | Job-level `env:` overrides workflow-level `env:`. | 06-Custom-Environment-Variables.md | Medium |
| 20 | A, B, C, D | All are correct uses of the `env` context. | 06-Custom-Environment-Variables.md | Medium |
| 21 | B | A is incorrect; B is correct on all counts. | 06-Custom-Environment-Variables.md | Hard |
| 22 | A | GITHUB_TOKEN is automatically created at job start. | 07-Default-Environment-Variables.md | Easy |
| 23 | A, B, D, E | All listed variables are valid; C (RUNNER_LABEL) is not standard. | 07-Default-Environment-Variables.md | Medium |
| 24 | B | GITHUB_WORKSPACE provides the absolute path to repository root. | 07-Default-Environment-Variables.md | Medium |
| 25 | B | Multiple detection methods exist; all are valid for CI/debugging scenarios. | 07-Default-Environment-Variables.md | Hard |
| 26 | A, B | GITHUB_TOKEN is repo-scoped; cross-repo access requires additional credentials. | 07-Default-Environment-Variables.md | Hard |
| 27 | C | Environment-scoped secrets override repository secrets when job specifies environment. | 08-Environment-Protection-Rules.md | Medium |
| 28 | B | Required reviewers trigger deployment UI wait for manual approval. | 08-Environment-Protection-Rules.md | Medium |
| 29 | A, B, C, D | All deployment branch protection scenarios apply across listed conditions. | 08-Environment-Protection-Rules.md | Medium |
| 30 | A, D | Artifacts auto-delete after retention; use separate download actions. | 09-Workflow-Artifacts.md | Medium |
| 31 | A, B, C | Glob patterns work; matrix artifacts auto-rename to avoid collisions. | 09-Workflow-Artifacts.md | Medium |
| 32 | C | Both glob patterns work; path specification supports multiple formats. | 09-Workflow-Artifacts.md | Medium |
| 33 | A, D, E | Artifacts inherit repo permissions; expire after retention; accessible within run. | 09-Workflow-Artifacts.md | Medium |
| 34 | A, B, D, E | hashFiles updates cache when files change; restore-keys provides fallback paths. | 10-Workflow-Caching.md | Medium |
| 35 | B | Reusable workflows need `on: workflow_call`. | 11-Workflow-Sharing.md | Easy |
| 36 | A, B, C, E | Accessible org workflow by name/version/SHA; repo must be public/accessible. | 11-Workflow-Sharing.md | Medium |
| 37 | A | Inputs required if marked so; outputs by job ID; only string type supported. | 11-Workflow-Sharing.md | Medium |
| 38 | B | Use `with: secrets: inherit` to pass all secrets to reusable workflow. | 11-Workflow-Sharing.md | Hard |
| 39 | C | action.yml required; repo public; README recommended; tagging optional. | 11-Workflow-Sharing.md | Medium |
| 40 | B | Set RUNNER_DEBUG=1 as repository secret; re-run workflow to enable. | 12-Workflow-Debugging.md | Medium |
| 41 | A, B, C, D | All workflow logging commands are available. | 12-Workflow-Debugging.md | Medium |
| 42 | C | Both UI and API provide access to logs. | 12-Workflow-Debugging.md | Easy |
| 43 | B | `if: failure()` uploads artifacts only on step failure. | 12-Workflow-Debugging.md | Medium |
| 44 | C | UI interface provides primary timing information access. | 12-Workflow-Debugging.md | Hard |
| 45 | A | Correct endpoint: `/repos/{owner}/{repo}/actions/workflows`. | 13-Workflows-REST-API.md | Medium |
| 46 | B | Trigger via API with correct parameters; all options are accurate. | 13-Workflows-REST-API.md | Medium |
| 47 | A | All query parameters apply to workflow run queries. | 13-Workflows-REST-API.md | Medium |
| 48 | A | POST endpoint with `/cancel` cancels a running workflow. | 13-Workflows-REST-API.md | Medium |
| 49 | A | Both list-then-download and direct download endpoints valid. | 13-Workflows-REST-API.md | Hard |
| 50 | B | Configure environments in Repository Settings → Environments. | 14-Reviewing-Deployments.md | Medium |
| 51 | A | Deployment history available in UI, API, and workflow logs. | 14-Reviewing-Deployments.md | Medium |
| 52 | A | Second approval triggers immediate deployment continuation. | 14-Reviewing-Deployments.md | Hard |
| 53 | A | Environment-scoped secrets/variables available when job specifies environment. | 14-Reviewing-Deployments.md | Medium |
| 54 | A | Metadata file is action.yml/.yaml at repo root. | 15-Creating-Publishing-Actions.md | Medium |
| 55 | B | Both node_modules shipping and bundling with ncc/esbuild valid. | 15-Creating-Publishing-Actions.md | Medium |
| 56 | B | Composite actions use `runs: { using: composite, steps: [...] }`. | 15-Creating-Publishing-Actions.md | Medium |
| 57 | B | Moving v1 tag allows existing references to receive updates. | 15-Creating-Publishing-Actions.md | Hard |
| 58 | C | All testing approaches valid at different development stages. | 15-Creating-Publishing-Actions.md | Medium |
| 59 | C | Both ubuntu-latest and ubuntu-22.04 support Python 3.10. | 16-Managing-Runners.md | Easy |
| 60 | B, C, D, E | All runner label and targeting statements correct. | 16-Managing-Runners.md | Medium |
| 61 | B | Organizations support IP allowlists for runner groups. | 16-Managing-Runners.md | Medium |
| 62 | A, B, C, E | Use third-party tools or hosted runners; no built-in autoscaling. | 16-Managing-Runners.md | Hard |
| 63 | B | Disable during maintenance; complete in-flight; automate patches. | 16-Managing-Runners.md | Medium |
| 64 | B | "Allow local actions only" permits organization/enterprise actions. | 17-GitHub-Actions-Enterprise.md | Medium |
| 65 | A, C | Required workflows run automatically; admins cannot disable. | 17-GitHub-Actions-Enterprise.md | Medium |
| 66 | A, C, E | IP allowlists restrict by registration IP; runners outside range inaccessible. | 17-GitHub-Actions-Enterprise.md | Hard |
| 67 | A, D | Repository secrets take precedence; most specific scope wins. | 17-GitHub-Actions-Enterprise.md | Medium |
| 68 | A | All audit logging capabilities available for compliance. | 17-GitHub-Actions-Enterprise.md | Hard |
| 69 | B, D | Use explicit minimal permissions; avoid read-all scope. | 18-Security-and-Optimization.md | Medium |
| 70 | C | Setting secret as env variable and referencing prevents injection attacks. | 18-Security-and-Optimization.md | Hard |
| 71 | B | Pin to full SHA; validate verified creators for supply chain security. | 18-Security-and-Optimization.md | Medium |
| 72 | A, B, E | All OIDC federation statements correct regarding cloud provider integration. | 18-Security-and-Optimization.md | Hard |
| 73 | B | Secret masking can fail from multiple contributing factors. | 18-Security-and-Optimization.md | Hard |
| 74 | B | Verify creator credentials; check update frequency; assess follower count. | 18-Security-and-Optimization.md | Hard |
| 75 | A | Parallelize independent jobs (build and test) to minimize runtime. | 18-Security-and-Optimization.md | Medium |
| 76 | B | Reduce artifact size; split jobs; increase timeout; consider larger runners. | 19-Common-Failures-Troubleshooting.md | Medium |
| 77 | B | Multiple factors can prevent workflow trigger; `main` matches exactly. | 19-Common-Failures-Troubleshooting.md | Medium |
| 78 | A, C, E | Incremental builds; caching; larger runners all improve performance. | 19-Common-Failures-Troubleshooting.md | Medium |
| 79 | D | Missing secrets can result from multiple causes in workflow. | 19-Common-Failures-Troubleshooting.md | Hard |
| 80 | A, B, C, D, E | Matrix job failure causes skip unless `if: always()` used. | 19-Common-Failures-Troubleshooting.md | Hard |
| 81 | A | Extension provides real-time YAML schema validation. | 01-GitHub-Actions-VS-Code-Extension.md | Easy |
| 82 | B | The extension highlights context variables outside their scope. | 01-GitHub-Actions-VS-Code-Extension.md | Medium |
| 83 | B | Extension validates permission scope names correctly. | 01-GitHub-Actions-VS-Code-Extension.md | Medium |
| 84 | B | The extension is not capable of running workflows locally. | 01-GitHub-Actions-VS-Code-Extension.md | Hard |
| 85 | A | Yes, the extension detects scope violations automatically. | 01-GitHub-Actions-VS-Code-Extension.md | Hard |
| 86 | B | Shells scripts execute in GitHub Actions environment by default. | 02-Contextual-Information.md | Medium |
| 87 | C | `github.event.pull_request.head.ref` retrieves source branch name. | 02-Contextual-Information.md | Medium |
| 88 | A | All runner variables are accessible in workflow context. | 02-Contextual-Information.md | Medium |
| 89 | A, B, C, D | All context properties available; none are restricted. | 02-Contextual-Information.md | Hard |
| 90 | A | Context values interpolate correctly in env blocks. | 02-Contextual-Information.md | Medium |
| 91 | A | Secrets context only available to job/step scopes. | 03-Context-Availability-Reference.md | Easy |
| 92 | A, B, E | Multiple secure transmission methods available for actions. | 03-Context-Availability-Reference.md | Medium |
| 93 | B | Event data properly referenced using `github.event` context. | 03-Context-Availability-Reference.md | Medium |
| 94 | A, B, C, D | All valid context availability scenarios listed. | 03-Context-Availability-Reference.md | Hard |
| 95 | A, B, C, D | Full range of event filtering options supported. | 03-Context-Availability-Reference.md | Medium |
| 96 | B | Artifact retention follows GitHub default policies. | 09-Workflow-Artifacts.md | Medium |
| 97 | B | Cache restoration with keys provides fallback options. | 10-Workflow-Caching.md | Medium |
| 98 | A, C, E | Multiple valid action referencing methods supported. | 15-Creating-Publishing-Actions.md | Medium |
| 99 | D | All factors contribute to performance requirements. | 19-Common-Failures-Troubleshooting.md | Hard |
| 100 | A, B, C, D, E | All performance optimization strategies are valid. | 19-Common-Failures-Troubleshooting.md | Hard |

---

*End of GH-200 Iteration 4 — 100 Questions*
