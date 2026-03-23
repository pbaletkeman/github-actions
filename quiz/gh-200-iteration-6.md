# GitHub Actions GH-200 Certification Exam — Question Bank (Iteration 6)

**Iteration**: 6

**Generated**: 2026-03-21

**Total Questions**: 100

**Difficulty Split**: 21 Easy / 56 Medium / 23 Hard

**Answer Types**: 61 `one` / 20 `many` / 12 `all` / 7 `none`

---

## Questions

---

### Question 1 — VS Code Extension

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Schema validation configuration

**Question**:
A developer opens VS Code `settings.json` to manually associate GitHub's official workflow JSON schema with all `.yml` files under `.github/workflows/`. Which settings key accomplishes this?

- A) `"yaml.schemas": { "https://json.schemastore.org/github-workflow.json": ".github/workflows/*.yml" }`
- B) `"github.actions.schema": ".github/workflows/*.yml"`
- C) `"files.associations": { ".github/workflows/*.yml": "github-workflow" }`
- D) `"editor.validation.schema": "github-workflow"`

---

### Question 2 — VS Code Extension

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Extension execution limitations

**Scenario**:
A developer expects the GitHub Actions VS Code extension to include a "Run Workflow" button that queues a run on GitHub directly from the editor. They cannot find this button anywhere in the extension UI.

**Question**:
Which statement best describes why this capability is unavailable in the extension?

- A) The extension can trigger `workflow_dispatch` runs, but only from the Command Palette, not a UI button
- B) The extension is an authoring tool — it provides schema validation, IntelliSense, and hover metadata, but does not initiate workflow runs; runs must be triggered via push events, the GitHub UI, or the REST API
- C) The extension requires the GitHub CLI (`gh`) to be installed to enable remote run functionality
- D) Remote run capabilities were removed in VS Code 1.85 due to a security policy change

---

### Question 3 — VS Code Extension

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Real-time validation capabilities

**Scenario**:
A developer is editing a new workflow before pushing it to the repository. They want to understand exactly what the GitHub Actions extension validates in the editor.

**Question**:
**(Select all that apply)** Which categories of issues does the extension detect and highlight inline?

- A) Incorrect YAML structure such as wrong indentation or duplicate mapping keys
- B) A `permissions:` block containing an unrecognized scope name (e.g., `release: write` instead of `releases: write`)
- C) A `jobs.<job_id>.needs:` reference to a job ID that is not defined elsewhere in the same workflow file
- D) A `uses:` reference to a third-party action whose specified version tag does not exist on GitHub

---

### Question 4 — VS Code Extension

**Difficulty**: Hard
**Answer Type**: all
**Topic**: IntelliSense and hover behavior

**Question**:
Which of the following statements about the GitHub Actions VS Code extension's authoring experience are **all** correct?

- A) Typing `${{ github.` in a workflow expression triggers autocomplete that lists available properties from GitHub's official `github` context documentation
- B) The extension does not show the actual values of `secrets.*` properties in IntelliSense — only that the `secrets` context exists — because secret values are never available to editor tooling
- C) Hovering over `uses: actions/setup-python@v5` displays the action's declared inputs, outputs, and description sourced from its `action.yml` metadata
- D) The extension validates expression syntax errors such as unclosed `${{` delimiters and highlights them before the file is saved

---

### Question 5 — VS Code Extension

**Difficulty**: Medium
**Answer Type**: none
**Topic**: Extension feature boundaries

**Question**:
A developer lists features they believe the GitHub Actions VS Code extension provides natively (without additional tools or plugins). Which of the following does the extension actually support?

- A) Running a workflow locally inside a Docker container to simulate execution on a GitHub-hosted runner
- B) Automatically committing and pushing a workflow file to the remote repository when the developer saves the file
- C) Streaming live logs from an in-progress workflow run into a VS Code output panel
- D) Syncing repository secrets from a local `.env` file directly into GitHub's repository secret settings

---

### Question 6 — Contextual Information

**Difficulty**: Easy
**Answer Type**: one
**Topic**: github context event name

**Question**:
Which context expression returns the name of the event that triggered the current workflow run (for example, `push`, `pull_request`, or `schedule`)?

- A) `${{ github.trigger }}`
- B) `${{ github.event_name }}`
- C) `${{ github.event.type }}`
- D) `${{ runner.trigger }}`

---

### Question 7 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Conditional expression using ref_type and ref_name

**Scenario**:
A release workflow must only execute when triggered by a push to a tag whose name starts with `v` (such as `v1.0.0` or `v2.4.1-rc`). All other events — including pushes to branches — must be skipped.

**Question**:
Which `if:` condition expression correctly restricts execution to matching version tags?

- A) `${{ github.ref_type == 'tag' && startsWith(github.ref_name, 'v') }}`
- B) `${{ github.event_name == 'push' && github.ref starts_with 'refs/tags/v' }}`
- C) `${{ github.tag_name starts_with('v') }}`
- D) `${{ startsWith(github.event.tag.name, 'v') }}`

---

### Question 8 — Contextual Information

**Difficulty**: Medium
**Answer Type**: many
**Topic**: pull_request event context properties

**Scenario**:
A workflow triggered by `pull_request` events needs to log both the name of the branch the PR was opened from and the name of the branch it targets (the base).

**Question**:
**(Select all that apply)** Which context expressions correctly provide these two branch names?

- A) `${{ github.head_ref }}` — the source (feature) branch the PR was opened from
- B) `${{ github.base_ref }}` — the target branch the PR is being merged into
- C) `${{ github.ref_name }}` — equivalent to `head_ref` and always contains the source branch name
- D) `${{ github.event.pull_request.base.ref }}` — an alternative expression for the target branch name

---

### Question 9 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Job outputs and needs context

**Scenario**:
A `build` job writes a version string using `echo "version=2.5.0" >> $GITHUB_OUTPUT`. A downstream `deploy` job has `needs: [build]` and references `${{ needs.build.outputs.version }}` in a step, but the expression always evaluates to an empty string.

**Question**:
What is missing from the `build` job definition?

- A) A `permissions: outputs: write` declaration at the job level
- B) An `outputs:` block at the job level that maps the output name to the step output expression (e.g., `version: ${{ steps.build-step.outputs.version }}`)
- C) An `export-outputs: true` flag on the step that writes to `GITHUB_OUTPUT`
- D) A `uses: actions/cache-outputs@v1` step to persist outputs across jobs

---

### Question 10 — Contextual Information

**Difficulty**: Hard
**Answer Type**: all
**Topic**: vars context availability and behavior

**Scenario**:
Your organization stores non-sensitive configuration values (feature flags, API endpoint URLs) as repository and organization variables. You want to reference them via the `vars` context throughout workflows.

**Question**:
**(Select all that apply)** Which statements about the `vars` context are correct?

- A) `vars` context values are available in the workflow-level `env:` block
- B) `vars` context values are available in `jobs.<job_id>.runs-on` to dynamically select a runner
- C) `vars` context values are **not** masked in logs — they are treated as non-sensitive configuration, unlike `secrets`
- D) Organization-level variables are accessible via `vars` in repositories belonging to that organization
- E) `vars` is available in the `run-name:` top-level workflow key

---

### Question 11 — Context Availability Reference

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contexts available in run-name

**Question**:
A workflow author wants to set the `run-name:` key to include the triggering actor and the input value. Which contexts are available for use in the `run-name` workflow key?

- A) `github`, `env`, `secrets`, `vars`
- B) `github`, `inputs`, `vars`
- C) `github`, `runner`, `job`, `steps`
- D) `github`, `needs`, `matrix`, `strategy`

---

### Question 12 — Context Availability Reference

**Difficulty**: Medium
**Answer Type**: one
**Topic**: runner context not available in job-level if

**Scenario**:
A developer wants to skip a job on macOS runners and writes the following:

```yaml
jobs:
  lint:
    if: ${{ runner.os != 'macOS' }}
    runs-on: macos-latest
```

**Question**:
Why does this `if` condition not work as intended?

- A) The `runner.os` property uses the value `macOS` but the comparison should be against `Darwin`
- B) The `runner` context is provisioned only after a runner is assigned to the job; `jobs.<job_id>.if` is evaluated before runner assignment, so `runner` is not available there
- C) Job-level `if:` conditions only support the functions `always()`, `success()`, `failure()`, and `cancelled()`
- D) The `runner` context is not populated when using GitHub-hosted runners; it only exists for self-hosted runners

---

### Question 13 — Context Availability Reference

**Difficulty**: Medium
**Answer Type**: one
**Topic**: needs context not available in strategy.matrix

**Scenario**:
A developer wants to dynamically populate a matrix using an output from a previous job:

```yaml
jobs:
  config:
    runs-on: ubuntu-latest
    outputs:
      targets: ${{ steps.read.outputs.targets }}
    steps:
      - id: read
        run: echo "targets=[\"us-east\",\"eu-west\"]" >> $GITHUB_OUTPUT

  deploy:
    needs: config
    strategy:
      matrix:
        region: ${{ fromJSON(needs.config.outputs.targets) }}
```

**Question**:
This pattern uses `needs` inside `strategy.matrix`. Is this valid?

- A) No — `needs` context is not available in `jobs.<job_id>.strategy.matrix`; only `github`, `inputs`, and `vars` are allowed there
- B) Yes — this is a documented pattern for dynamic matrix generation and works as written
- C) No — `fromJSON()` can only be used in step `run:` blocks, not in matrix definitions
- D) Yes — but only when the upstream job is in the same file and the output is a valid JSON array string

---

### Question 14 — Context Availability Reference

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Identifying invalid context usages

**Scenario**:
You are auditing a workflow for context availability violations against GitHub's documented rules.

**Question**:
**(Select all that apply)** Which of the following context usages violate GitHub's context availability specification?

- A) Using `${{ secrets.API_KEY }}` in `jobs.<job_id>.runs-on`
- B) Using `${{ runner.arch }}` in `jobs.<job_id>.steps[*].with`
- C) Using `${{ matrix.os }}` inside the `jobs.<job_id>.strategy.matrix` definition itself
- D) Using `${{ needs.setup.outputs.version }}` in `jobs.<job_id>.steps[*].env`

---

### Question 15 — Context Availability Reference

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Security risk of secret interpolation in run steps

**Scenario**:
A workflow step constructs a `curl` command that includes an authorization token:

```yaml
- name: Call internal API
  run: |
    curl -H "Authorization: Bearer ${{ secrets.API_TOKEN }}" \
         https://internal.example.com/deploy
```

**Question**:
What security concern does this approach introduce, even if GitHub masks `secrets.API_TOKEN` in the workflow log output?

- A) The `curl` command will fail because `${{ }}` expressions are not evaluated inside multi-line `run:` blocks
- B) The secret value is interpolated directly into the shell command string before execution; it may be exposed through process listings (`ps aux`), shell debug traces (`set -x`), or error messages that GitHub's log masking does not cover
- C) Secrets used in `run:` steps are automatically stored in GITHUB_OUTPUT, making them accessible to downstream jobs
- D) Combining two secrets in the same `run:` step causes a fatal workflow error in secure organizations

---

### Question 16 — Workflow File Structure

**Difficulty**: Easy
**Answer Type**: one
**Topic**: concurrency key purpose

**Question**:
What is the function of the `concurrency:` key in a GitHub Actions workflow?

- A) It controls how many matrix jobs are allowed to run at the same time
- B) It prevents multiple runs of the same concurrency group from executing simultaneously, with an option to cancel in-progress runs when a newer run is queued
- C) It sets the number of CPU cores allocated to the runner for the workflow
- D) It limits the total number of jobs that can be created per workflow file

---

### Question 17 — Workflow File Structure

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Default job timeout

**Question**:
A job does not specify `timeout-minutes`. How long will GitHub Actions allow the job to run before canceling it?

- A) 30 minutes
- B) 60 minutes
- C) 360 minutes (6 hours)
- D) 720 minutes (12 hours)

---

### Question 18 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: continue-on-error at job level in a matrix

**Scenario**:
A matrix strategy launches 6 jobs. One job has `continue-on-error: true` set at the job level. That specific job fails. `fail-fast` is not set (defaults to `true`).

**Question**:
What is the behavior when this matrix job fails?

- A) Because `fail-fast` defaults to `true`, all other in-progress matrix jobs are immediately canceled regardless of the job's `continue-on-error` setting
- B) The failing job is marked as failed, but `continue-on-error: true` prevents the failure from triggering `fail-fast` cancellation; the overall workflow can still succeed
- C) The failing job is retried up to 3 times automatically before `fail-fast` is applied
- D) `continue-on-error: true` at the job level is equivalent to setting `fail-fast: false` at the strategy level

---

### Question 19 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Required permissions for multi-operation workflows

**Scenario**:
A release workflow performs three operations using the default `GITHUB_TOKEN`:
1. Pushes a Git tag (writes to repository contents)
2. Publishes a package to GitHub Packages
3. Posts a comment on the triggering pull request

**Question**:
**(Select all that apply)** Which `permissions:` scopes must be explicitly granted for all three operations to succeed?

- A) `contents: write`
- B) `packages: write`
- C) `pull-requests: write`
- D) `deployments: write`

---

### Question 20 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Service containers

**Scenario**:
An integration test job requires both a PostgreSQL database and a Redis cache to be running before any test steps execute. Both services need to be accessible from within job steps using predictable hostnames.

**Question**:
Which workflow feature is the correct approach for providing both services?

- A) Define two prerequisite jobs that each start a container and export the connection string as an output via `needs:`
- B) Use `jobs.<job_id>.services:` to declare both containers; GitHub starts them before job steps begin and assigns each a hostname matching its service key name
- C) Add `docker run` steps for each service at the top of the job before the test steps
- D) Specify both images in a list under `jobs.<job_id>.container:` to run them as co-located containers

---

### Question 21 — Workflow File Structure

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Matrix include and exclude combinatorics

**Scenario**:
A workflow defines the following matrix:

```yaml
strategy:
  matrix:
    os: [ubuntu-latest, windows-latest]
    node: [18, 20, 22]
    include:
      - os: ubuntu-latest
        node: 22
        experimental: true
    exclude:
      - os: windows-latest
        node: 18
```

**Question**:
How many total jobs does this matrix configuration produce?

- A) 4
- B) 5
- C) 6
- D) 7

---

### Question 22 — Workflow Trigger Events

**Difficulty**: Easy
**Answer Type**: one
**Topic**: workflow_dispatch trigger

**Question**:
Which trigger event must be configured to allow a workflow to accept user-provided typed inputs when started manually from the GitHub Actions UI or via the REST API?

- A) `repository_dispatch`
- B) `workflow_call`
- C) `workflow_dispatch`
- D) `manual`

---

### Question 23 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: pull_request_target security risk

**Scenario**:
A team configures a workflow to use `pull_request_target` so it can access repository secrets when triggered by pull requests from forks. A security reviewer raises a concern about this configuration.

**Question**:
Which statement correctly explains the security risk of `pull_request_target` when it checks out and executes PR code?

- A) `pull_request_target` runs with write permissions and access to secrets from the **base** repository, but it executes the workflow from the base branch; if the workflow checks out and runs code from the PR branch, an attacker can run arbitrary code with those elevated privileges
- B) `pull_request_target` is deprecated and may execute workflows from outdated YAML files, causing unpredictable behavior
- C) `pull_request_target` fires on every push to the PR branch, creating excessive API quota usage that can exhaust rate limits
- D) `pull_request_target` does not receive the `github.event.pull_request` data, so secrets cannot be reliably scoped to the triggering PR

---

### Question 24 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: workflow_run trigger and conclusion filtering

**Scenario**:
You need a deployment workflow to trigger automatically, but only after a workflow named "Build and Test" completes **successfully** — not when it fails or is canceled.

**Question**:
Which `on:` configuration correctly implements this requirement?

```yaml
on:
  workflow_run:
    workflows: ["Build and Test"]
    types: [completed]
```

- A) Change `types: [completed]` to `types: [success]` — the `success` type only fires on successful conclusions
- B) Keep `types: [completed]` (which fires on any conclusion) and add a job-level `if: ${{ github.event.workflow_run.conclusion == 'success' }}` condition
- C) Add `conclusion: success` as a sibling key to `types:` to filter by conclusion
- D) Use `types: [completed, success]` — listing both types ensures only successful runs trigger the workflow

---

### Question 25 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: many
**Topic**: push trigger with branch and path filtering

**Scenario**:
A CI workflow must trigger only when:
1. A push targets branches matching `feature/**` or the branch `main`
2. The pushed changes include files under `src/` or the root-level file `package.json`

**Question**:
**(Select all that apply)** Which filter keys are required under `on.push:` to enforce both constraints simultaneously?

- A) `branches:` listing `feature/**` and `main`
- B) `paths:` listing `src/**` and `package.json`
- C) `branches-ignore:` to exclude all branches that do not match the patterns
- D) `paths-ignore:` to exclude non-source files from consideration

---

### Question 26 — Workflow Trigger Events

**Difficulty**: Hard
**Answer Type**: one
**Topic**: schedule event behavior and payload

**Scenario**:
A nightly audit workflow runs on `schedule` at `cron: '0 3 * * *'`. A developer inspects `${{ github.event }}` in a step and finds it empty. They also notice the workflow sometimes starts 20–40 minutes after 03:00 UTC.

**Question**:
Which statement correctly explains both observations?

- A) The `schedule` event does not produce an event payload, so `github.event` is empty for scheduled runs; additionally, scheduled workflows may be delayed by GitHub's infrastructure load and are not guaranteed to start at the exact cron time
- B) `github.event` is only populated after `actions/checkout` runs because the event data is stored in the repository
- C) The cron expression `0 3 * * *` uses UTC+3 by default, causing an apparent 3-hour offset in UTC
- D) Scheduled workflows run in a read-only mode that prevents the event payload from being accessible in step contexts

---

### Question 27 — Custom Environment Variables

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Setting env vars for subsequent steps via GITHUB_ENV

**Question**:
A step needs to define an environment variable `DEPLOY_ENV=production` that will be available to all steps that follow it within the same job. Which command achieves this?

- A) `export DEPLOY_ENV=production`
- B) `echo "DEPLOY_ENV=production" >> $GITHUB_ENV`
- C) `echo "::set-env name=DEPLOY_ENV::production"`
- D) `set DEPLOY_ENV=production`

---

### Question 28 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Environment variable scope precedence

**Scenario**:
A workflow defines `LOG_LEVEL: info` at the workflow level. Job B overrides it with `LOG_LEVEL: debug` at the job level. A specific step inside Job B defines `LOG_LEVEL: warning` in its own `env:` block.

**Question**:
What value does `LOG_LEVEL` have during the execution of that step?

- A) `info` — workflow-level declarations always take priority
- B) `debug` — job-level overrides workflow-level for all steps in the job
- C) `warning` — step-level `env:` takes the highest precedence, overriding both job and workflow levels
- D) The variable is undefined because conflicting declarations cause a parse error

---

### Question 29 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: none
**Topic**: GITHUB_ENV cross-job and current-step misconceptions

**Question**:
A developer lists statements they believe to be true about environment variables written to `$GITHUB_ENV`. Which of the following is an actual documented behavior of `$GITHUB_ENV`?

- A) A value written to `$GITHUB_ENV` in Job A is automatically readable in Job B via the `env` context because jobs in the same workflow share an environment namespace
- B) A step can immediately read a variable it just wrote to `$GITHUB_ENV` within the same `run:` block (before the step finishes)
- C) Values written to `$GITHUB_ENV` are automatically masked in logs the same way that `secrets` values are
- D) Variables written to `$GITHUB_ENV` persist across workflow runs and are available in the next triggered run of the same workflow

---

### Question 30 — Custom Environment Variables

**Difficulty**: Hard
**Answer Type**: one
**Topic**: GITHUB_ENV injection via untrusted API response

**Scenario**:
A workflow step fetches a version number from an external API and writes it to `$GITHUB_ENV`:

```yaml
- name: Set version
  run: |
    VERSION=$(curl -s https://api.example.com/version)
    echo "APP_VERSION=$VERSION" >> $GITHUB_ENV
```

**Question**:
What security vulnerability does this introduce if the API response is not trusted?

- A) The `curl` command runs as root and can modify system files on the runner
- B) If the API response contains a newline followed by an `ENVNAME=VALUE` string, it can inject additional arbitrary environment variables into the job's execution context — a known `GITHUB_ENV` injection attack
- C) The variable value is limited to 256 characters; longer values from the API silently truncate and corrupt downstream steps
- D) External `curl` calls are blocked by GitHub's network policy for hosted runners, causing the step to hang indefinitely

---

### Question 31 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Writing env vars and step outputs in different scenarios

**Scenario**:
A step produces two pieces of data: (1) a status message to display in subsequent step names, and (2) a build artifact path to be consumed by a downstream job.

**Question**:
**(Select all that apply)** Which commands correctly produce the appropriate outputs for each use case?

- A) `echo "STATUS=Build succeeded" >> $GITHUB_ENV` — sets an env var readable in subsequent steps of the same job
- B) `echo "artifact-path=/dist/app.tar.gz" >> $GITHUB_OUTPUT` — sets a step output accessible via `steps.<id>.outputs.artifact-path` and exportable as a job output
- C) `echo "::set-output name=artifact-path::/dist/app.tar.gz"` — the current recommended way to set a step output
- D) `echo "STATUS=Build succeeded" >> $GITHUB_STEP_SUMMARY` — writes the status to the job summary UI panel

---

### Question 32 — Default Environment Variables

**Difficulty**: Easy
**Answer Type**: one
**Topic**: GITHUB_WORKSPACE path

**Question**:
Which default environment variable contains the absolute path to the directory on the runner where the repository is checked out when using `actions/checkout`?

- A) `GITHUB_REPOSITORY_PATH`
- B) `RUNNER_WORKSPACE`
- C) `GITHUB_WORKSPACE`
- D) `GITHUB_CHECKOUT_PATH`

---

### Question 33 — Default Environment Variables

**Difficulty**: Hard
**Answer Type**: all
**Topic**: Default environment variable facts

**Question**:
Which of the following statements about GitHub Actions default environment variables are **all** correct?

- A) `GITHUB_RUN_ID` is unique per workflow run within the repository and remains the same across re-run attempts of the same run
- B) `GITHUB_RUN_NUMBER` increments by 1 for each new run of a particular workflow in a repository; re-runs do not increment this counter
- C) `GITHUB_RUN_ATTEMPT` starts at `1` for the first attempt and increments by 1 for each re-run of the same workflow run
- D) `GITHUB_SHA` contains the commit SHA that triggered the workflow; for `pull_request` events, it is the SHA of the merge commit

---

### Question 34 — Default Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: GITHUB_TOKEN default permission restrictions

**Scenario**:
An organization administrator has configured the organization's `GITHUB_TOKEN` default permissions to `restricted` (read-only). A new workflow step that attempts to create a GitHub Release fails with HTTP 403.

**Question**:
What is the minimal change required to fix this without using a PAT?

- A) Add `allow-releases: true` to the workflow's top-level `on:` configuration
- B) Add `permissions: contents: write` at the job or workflow level to grant the `GITHUB_TOKEN` write access to repository contents
- C) Replace `${{ secrets.GITHUB_TOKEN }}` with `${{ github.token }}` — the `github.token` alias always has write access
- D) Set `default_workflow_permissions: write` in the individual workflow file's `permissions:` block

---

### Question 35 — Default Environment Variables

**Difficulty**: Hard
**Answer Type**: all
**Topic**: GITHUB_REF vs GITHUB_REF_NAME across event types

**Question**:
Which of the following statements about `GITHUB_REF` and `GITHUB_REF_NAME` are **all** correct?

- A) For a `push` to branch `main`, `GITHUB_REF` is `refs/heads/main` and `GITHUB_REF_NAME` is `main`
- B) For a `push` to tag `v2.0.0`, `GITHUB_REF` is `refs/tags/v2.0.0` and `GITHUB_REF_NAME` is `v2.0.0`
- C) For a `pull_request` event, `GITHUB_REF` is `refs/pull/<PR_NUMBER>/merge` and `GITHUB_REF_NAME` is `<PR_NUMBER>/merge`
- D) `GITHUB_REF_TYPE` will be either `branch` or `tag`, never `pull_request`, even for `pull_request` events

---

### Question 36 — Default Environment Variables

**Difficulty**: Easy
**Answer Type**: one
**Topic**: GITHUB_OUTPUT vs GITHUB_ENV

**Question**:
A developer needs to pass a computed value from one step to be consumed by a **different step in the same job**. They also need to pass a different value to a **downstream job** that lists this job in its `needs:`. Which files should be used for each purpose?

- A) Use `$GITHUB_ENV` for both — it handles all inter-step and inter-job communication
- B) Use `$GITHUB_ENV` to set variables for subsequent steps in the same job; use `$GITHUB_OUTPUT` to set step outputs that can be mapped to job outputs and consumed by downstream jobs
- C) Use `$GITHUB_OUTPUT` for both — it automatically promotes values to job-level outputs
- D) Use `$GITHUB_STEP_SUMMARY` for inter-job data sharing; `$GITHUB_ENV` is only for intra-step communication

---

### Question 37 — Environment Protection Rules

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Required reviewers purpose

**Question**:
What is the primary function of the "Required reviewers" setting on a GitHub environment?

- A) It requires a code reviewer to approve every pull request before the workflow can trigger at all
- B) It pauses a workflow run when it reaches a job that references the protected environment, waiting for designated reviewers to approve before job steps execute
- C) It sends a notification to reviewers after a deployment completes for post-deployment verification
- D) It prevents the workflow from running if any of the designated reviewers has not yet committed code to the branch

---

### Question 38 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Wait timer behavior

**Scenario**:
A team configures a `wait-timer` of 15 minutes on the `staging` environment. A deployment workflow references this environment.

**Question**:
**(Select all that apply)** Which statements correctly describe the wait timer's behavior?

- A) The workflow pauses for 15 minutes at the job that references `staging` before any steps in that job execute
- B) The wait timer runs concurrently with required reviewer approval — both the timer and any required approvals must complete before execution resumes
- C) The wait timer can be set to a value between 0 and 43,200 minutes (30 days)
- D) If a required reviewer approves before the timer expires, the job starts immediately without waiting for the timer
- E) The wait timer only applies to deployment jobs, not to any other jobs in the same workflow

---

### Question 39 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Deployment branch and tag restrictions

**Scenario**:
An enterprise team configures the `production` environment to only allow deployments from branches matching `main` or `release/**`. A developer on a branch named `hotfix/db-migration` triggers the deployment workflow.

**Question**:
What happens when the workflow reaches the job that references `environment: production`?

- A) The job pauses and waits for an admin to manually override the branch restriction before proceeding
- B) The entire workflow run fails when it reaches the job referencing the protected environment, because the triggering branch does not match the allowed patterns
- C) GitHub automatically redirects the deployment to the `staging` environment as a fallback
- D) The deployment proceeds because `hotfix/**` branches are implicitly trusted for emergency deployments

---

### Question 40 — Environment Protection Rules

**Difficulty**: Hard
**Answer Type**: all
**Topic**: Environment secrets vs repository secrets

**Question**:
Which of the following statements about environment secrets compared to repository secrets are **all** correct?

- A) Environment secrets are only accessible to jobs that explicitly reference the specific environment via `jobs.<job_id>.environment:`
- B) If an environment secret and a repository secret share the same name, the environment secret value takes precedence for jobs that reference that environment
- C) Environment secrets can only be accessed by a job after any required reviewer approval (and wait timer) for that environment has been satisfied
- D) Repository-level secrets are available to all jobs in the workflow that do not reference a protected environment with conflicting secret names

---

### Question 41 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Environment secret precedence over repository secrets

**Scenario**:
A repository has a secret named `DB_PASSWORD` set at the repository level. The `production` environment also has a secret named `DB_PASSWORD` with a different value. A job references `environment: production` and accesses `${{ secrets.DB_PASSWORD }}`.

**Question**:
Which value does `${{ secrets.DB_PASSWORD }}` resolve to in this job?

- A) The repository-level secret value, because repository secrets have higher precedence than environment secrets
- B) The environment-level secret value, because environment secrets override repository secrets for jobs referencing that environment
- C) An error is raised because duplicate secret names across scopes are not permitted
- D) The value set most recently takes precedence, regardless of whether it is a repository or environment secret

---

### Question 42 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Maximum required reviewers

**Scenario**:
An enterprise team wants to require multiple approval levels for their production environment. A team lead asks how many users or teams can be configured as required reviewers.

**Question**:
What is the maximum number of users or teams that can be configured as required reviewers for a single GitHub environment?

- A) 3
- B) 5
- C) 6
- D) 10

---

### Question 43 — Workflow Artifacts

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Default artifact retention period

**Question**:
An artifact is uploaded using `actions/upload-artifact` without specifying `retention-days`. What is the default artifact retention period for public repositories on GitHub.com?

- A) 7 days
- B) 30 days
- C) 90 days
- D) 180 days

---

### Question 44 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: many
**Topic**: upload-artifact configuration options

**Scenario**:
A build job produces a compiled binary at `./dist/app`, a test coverage report at `./coverage/`, and multiple log files matching `./logs/*.log`. The team wants to upload all outputs but keep them organized in separate artifacts.

**Question**:
**(Select all that apply)** Which `actions/upload-artifact` configurations would correctly create separate, named artifacts for each output?

- A) A step with `name: binary` and `path: ./dist/app`; another step with `name: coverage` and `path: ./coverage/`; another with `name: logs` and `path: ./logs/*.log`
- B) A single step with all three paths listed under `path:` using a multi-line value, and a single `name:` — this creates one artifact containing all outputs
- C) Using `if-no-files-found: error` on the logs artifact step to fail the job if no log files match the glob pattern
- D) Setting `retention-days: 14` on the binary artifact to retain it for a shorter period than the default

---

### Question 45 — Workflow Artifacts

**Difficulty**: Hard
**Answer Type**: all
**Topic**: Artifact behavior and availability

**Question**:
Which of the following statements about GitHub Actions artifacts are **all** correct?

- A) Artifacts uploaded by one job can be downloaded and used by a subsequent job in the same workflow run using `actions/download-artifact`
- B) An artifact with `retention-days: 0` is never stored — it is treated as a transient inter-job file transfer only available within the current run
- C) When two `upload-artifact` steps in the same job upload artifacts with the same `name`, the second upload merges its files with the first artifact's contents rather than replacing them
- D) Artifact names are unique within a workflow run; if two jobs upload artifacts with the same name, the latter upload overwrites the former

---

### Question 46 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Artifact download across jobs

**Scenario**:
A `build` job uploads an artifact named `compiled-app`. A `deploy` job (which `needs: [build]`) must download this artifact before running the deployment script.

**Question**:
Which step correctly downloads the artifact in the `deploy` job?

- A) `uses: actions/download-artifact@v4` with `with: name: compiled-app`
- B) `uses: actions/fetch-artifact@v1` since `download-artifact` only works within the same job
- C) The artifact is automatically available at the checkout path; no explicit download step is needed
- D) `uses: actions/checkout@v4` with `artifact: compiled-app` in the `with:` block

---

### Question 47 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: none
**Topic**: Artifact misconceptions

**Question**:
A developer lists statements they believe are true about GitHub Actions artifacts. Which of the following is an actual documented behavior?

- A) Artifacts uploaded in one workflow run are automatically available to subsequent workflow runs on the same branch without any explicit download step
- B) The `actions/upload-artifact` action fails silently if the specified path does not exist, uploading an empty artifact
- C) Artifact download is possible across repositories — any workflow can download an artifact from any public repository using its run ID
- D) Artifacts are accessible from within the same job that uploaded them without needing a separate `download-artifact` step, because they are written to a shared cache on the runner

---

### Question 48 — Workflow Caching

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Repository cache size limit

**Question**:
What is the maximum total cache storage size per repository in GitHub Actions?

- A) 2 GB
- B) 5 GB
- C) 10 GB
- D) 25 GB

---

### Question 49 — Workflow Caching

**Difficulty**: Hard
**Answer Type**: all
**Topic**: Cache key behavior and hashFiles function

**Question**:
Which of the following statements about cache keys in `actions/cache` are **all** correct?

- A) The `hashFiles('**/package-lock.json')` function computes a hash over all files matching the glob pattern; if any file changes, the hash changes and the cache key does not match
- B) When an exact cache key match is found, the cache is restored before the step that follows the `actions/cache` step
- C) Cache keys are case-sensitive and must be unique within the repository; two different workflows can share a cache if they use the same key
- D) If no exact cache key match is found, `restore-keys:` are tried in order from most specific to least specific as prefix matches

---

### Question 50 — Workflow Caching

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Cache branch access scope

**Scenario**:
A feature branch `feature/auth` runs a workflow that creates a cache with key `node-18-auth-abc123`. The `main` branch runs the same workflow but has never created a matching cache.

**Question**:
Which statement correctly describes which caches the `main` branch workflow can access from the `feature/auth` branch workflow?

- A) The `main` branch can access any cache created by `feature/auth` as long as it uses the same key
- B) Cache keys are scoped to the branch that created them; `main` cannot access caches created by `feature/auth`, but `feature/auth` can access caches created by `main` (the base branch)
- C) Caches in GitHub Actions are shared across all branches in the repository with no branch scoping
- D) Only the default branch (`main`) creates shareable caches; feature branch caches are always private to that branch

---

### Question 51 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: many
**Topic**: restore-keys fallback behavior

**Scenario**:
A workflow configures the following cache step:

```yaml
- uses: actions/cache@v4
  with:
    path: ~/.npm
    key: ${{ runner.os }}-npm-${{ hashFiles('**/package-lock.json') }}
    restore-keys: |
      ${{ runner.os }}-npm-
      ${{ runner.os }}-
```

A new run occurs where `package-lock.json` has changed, so the exact key no longer matches any cached entry.

**Question**:
**(Select all that apply)** Which statements correctly describe the cache restore behavior?

- A) GitHub searches for the most recently saved cache whose key starts with `${{ runner.os }}-npm-` as a prefix match
- B) If a `restore-keys` prefix match is found and restored, the exact key match is still saved as a new cache entry after the job completes
- C) `restore-keys` matches are always exact matches; changing `package-lock.json` in any way results in no cache restoration
- D) If the first `restore-keys` entry (`${{ runner.os }}-npm-`) finds no match, the second entry (`${{ runner.os }}-`) is tried next

---

### Question 52 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: setup-node built-in caching

**Scenario**:
A Node.js build workflow uses `actions/setup-node@v4`. A developer wants to cache npm dependencies automatically without writing a separate `actions/cache` step.

**Question**:
Which `actions/setup-node` input enables built-in dependency caching?

- A) `enable-cache: true`
- B) `cache: 'npm'`
- C) `auto-cache: npm`
- D) `cache-dependencies: package-lock.json`

---

### Question 53 — Workflow Sharing

**Difficulty**: Easy
**Answer Type**: one
**Topic**: workflow_call trigger for reusable workflows

**Question**:
Which event trigger must a workflow file declare to allow it to be called as a reusable workflow from another workflow?

- A) `on: workflow_dispatch`
- B) `on: workflow_call`
- C) `on: workflow_run`
- D) `on: repository_dispatch`

---

### Question 54 — Workflow Sharing

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Job outputs from reusable workflows

**Scenario**:
A reusable workflow produces an output named `build-version`. The calling workflow needs to access this value in a subsequent job after the reusable workflow completes.

**Question**:
What is required in the reusable workflow's definition for the calling workflow to access the output?

- A) The reusable workflow must declare the output in an `outputs:` block at the workflow level, mapping the output name to a job output expression
- B) The reusable workflow writes the value to `$GITHUB_ENV` and the calling workflow reads it via the `env` context
- C) Job outputs in reusable workflows are automatically promoted and no explicit declaration is needed
- D) The calling workflow must use `actions/download-artifact` to retrieve any data produced by the reusable workflow

---

### Question 55 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Secrets in workflow_call

**Scenario**:
A reusable workflow requires a secret (`NPM_TOKEN`) to publish a package. The calling workflow must pass this secret to the reusable workflow.

**Question**:
**(Select all that apply)** Which approaches are valid for passing secrets to a `workflow_call` reusable workflow?

- A) Declaring `secrets: NPM_TOKEN: required: true` in the reusable workflow's `workflow_call` trigger and passing `secrets: NPM_TOKEN: ${{ secrets.NPM_TOKEN }}` in the calling workflow
- B) Using `secrets: inherit` in the calling workflow's `uses:` step to automatically pass all secrets from the caller to the reusable workflow
- C) Writing the secret to `$GITHUB_ENV` in a step before calling the reusable workflow so the reusable workflow can read it from the environment
- D) Passing secrets as `inputs:` with `type: string` — this is the same as secrets but without masking

---

### Question 56 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Composite actions vs reusable workflows differences

**Scenario**:
A platform team is deciding whether to implement shared CI logic as a composite action or a reusable workflow.

**Question**:
**(Select all that apply)** Which statements correctly distinguish composite actions from reusable workflows?

- A) Composite actions run within the caller's job on the same runner; reusable workflows run in their own separate job(s) on potentially different runners
- B) Composite actions can use `jobs:` to define parallel execution; reusable workflows cannot
- C) Reusable workflows support job-level parallelism and can define multiple jobs; composite actions cannot define jobs
- D) Composite actions can reference other actions with `uses:` in their steps; reusable workflows can also call other reusable workflows (up to nesting depth limits)

---

### Question 57 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: none
**Topic**: Reusable workflow input misconceptions

**Question**:
A developer lists statements about `inputs:` in `workflow_call` triggers. Which of the following is an actual documented `workflow_call` input behavior?

- A) Inputs defined in `workflow_call` are automatically available as environment variables in all steps of the reusable workflow without any explicit reference
- B) `workflow_call` inputs support a `secret: true` flag that masks the input value in logs, making it equivalent to a secret
- C) `workflow_call` inputs with `type: boolean` are passed as string literals (`"true"`/`"false"`) from the calling workflow and automatically cast to boolean internally
- D) Inputs defined in `workflow_call` can have default values specified in the calling workflow's `with:` block that differ from the defaults in the reusable workflow definition

---

### Question 58 — Workflow Debugging

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Enabling step debug logging

**Question**:
A developer needs to enable verbose step-level debug logging for a workflow run without modifying the workflow YAML file. Which repository secret or variable must be set to `true` to enable this?

- A) `ACTIONS_DEBUG`
- B) `ACTIONS_STEP_DEBUG`
- C) `GITHUB_ACTIONS_DEBUG`
- D) `RUNNER_DEBUG`

---

### Question 59 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow command for debug messages

**Scenario**:
A developer wants to add diagnostic messages to a `run:` step that only appear in the workflow log when step debug logging is enabled, without always printing to the log.

**Question**:
Which workflow command outputs a debug message only when debug logging is active?

- A) `echo "::log level=debug::message"`
- B) `echo "::debug::message"`
- C) `echo "##[debug]message"`
- D) `echo "ACTIONS_DEBUG_MSG=message" >> $GITHUB_ENV`

---

### Question 60 — Workflow Debugging

**Difficulty**: Hard
**Answer Type**: one
**Topic**: ACTIONS_RUNNER_DEBUG vs ACTIONS_STEP_DEBUG

**Scenario**:
A DevOps engineer enables both `ACTIONS_RUNNER_DEBUG: true` and `ACTIONS_STEP_DEBUG: true` for a failing workflow, hoping to get maximum log output.

**Question**:
What is the difference between these two debug variables?

- A) `ACTIONS_RUNNER_DEBUG` and `ACTIONS_STEP_DEBUG` are aliases for the same functionality; setting either one enables both runner and step debug log output
- B) `ACTIONS_STEP_DEBUG` enables verbose logging within individual step executions (shell traces, command outputs); `ACTIONS_RUNNER_DEBUG` enables additional diagnostic logging from the runner agent itself (runner lifecycle, job setup, network activity)
- C) `ACTIONS_RUNNER_DEBUG` applies only to self-hosted runners; `ACTIONS_STEP_DEBUG` applies only to GitHub-hosted runners
- D) `ACTIONS_STEP_DEBUG` only affects steps that use `run:`; it has no impact on `uses:` action steps

---

### Question 61 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Methods to add diagnostic output to workflow logs

**Scenario**:
A developer is troubleshooting a failed deployment workflow and wants to add structured diagnostic information to the logs to help narrow down the issue.

**Question**:
**(Select all that apply)** Which approaches produce visible diagnostic output in the GitHub Actions workflow log?

- A) `echo "::notice::Deployment target: $TARGET_ENV"` — creates a notice annotation visible in the log and on the PR
- B) `echo "::group::Diagnostics" && env | sort && echo "::endgroup::"` — creates a collapsible log group showing all environment variables
- C) `echo "DIAG_INFO=value" >> $GITHUB_STEP_SUMMARY` — appends text to the workflow summary page
- D) `echo "::warning::Cache miss detected"` — creates a warning annotation in the log and workflow summary

---

### Question 62 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Log group syntax

**Scenario**:
A developer wants to group a series of diagnostic echo statements under a collapsible section in the workflow log titled "Environment Details".

**Question**:
Which pair of commands correctly creates and closes a collapsible log group?

- A) `echo "::group::Environment Details"` ... `echo "::endgroup::"`
- B) `echo "##[group]Environment Details"` ... `echo "##[endgroup]"`
- C) `echo "::begin-group::Environment Details"` ... `echo "::end-group::"`
- D) `echo "::section::Environment Details"` ... `echo "::endsection::"`

---

### Question 63 — Workflows REST API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: REST API endpoint to trigger a workflow_dispatch

**Question**:
Which REST API endpoint is used to programmatically trigger a workflow that has `on: workflow_dispatch` configured?

- A) `POST /repos/{owner}/{repo}/actions/runs`
- B) `POST /repos/{owner}/{repo}/actions/workflows/{workflow_id}/dispatches`
- C) `POST /repos/{owner}/{repo}/dispatches`
- D) `POST /repos/{owner}/{repo}/actions/triggers/{workflow_id}`

---

### Question 64 — Workflows REST API

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Operations available via the Workflows REST API

**Scenario**:
An operations team wants to build an internal dashboard and automation system that manages GitHub Actions workflows programmatically.

**Question**:
**(Select all that apply)** Which operations are available through the GitHub Actions REST API?

- A) Listing all workflow runs with filters for branch, status, event type, and date range
- B) Canceling an in-progress workflow run
- C) Modifying the YAML content of a workflow file directly via the API without a commit
- D) Re-running a failed workflow run or only its failed jobs
- E) Downloading workflow run logs as a zip archive

---

### Question 65 — Workflows REST API

**Difficulty**: Hard
**Answer Type**: all
**Topic**: Authentication methods for the REST API

**Question**:
Which of the following authentication methods are **all** valid for making GitHub Actions REST API calls?

- A) A Personal Access Token (PAT) with `repo` or `workflow` scope passed in the `Authorization: Bearer <token>` header
- B) The `GITHUB_TOKEN` from within a running workflow job, passed in the `Authorization: Bearer ${{ secrets.GITHUB_TOKEN }}` header
- C) A GitHub App installation access token with appropriate permissions, passed in the `Authorization: Bearer <token>` header
- D) An OAuth app token authorized by a user with the necessary repository permissions

---

### Question 66 — Workflows REST API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Filtering workflow runs by conclusion

**Scenario**:
An operations engineer needs to list only the failed workflow runs for a specific workflow in the `main` branch to populate an incident report.

**Question**:
Which query parameters should be added to the `GET /repos/{owner}/{repo}/actions/workflows/{workflow_id}/runs` request?

- A) `?branch=main&status=failed`
- B) `?branch=main&conclusion=failure`
- C) `?ref=main&result=failure`
- D) `?branch=main&state=failure`

---

### Question 67 — Workflows REST API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Pagination for workflow run listings

**Scenario**:
A team uses the REST API to list workflow runs and builds a pagination loop to retrieve all results.

**Question**:
When listing workflow runs via the REST API, what is the maximum number of results that can be returned in a single page?

- A) 30
- B) 50
- C) 100
- D) 250

---

### Question 68 — Reviewing Deployments

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Behavior after reviewer approval

**Question**:
A deployment workflow is paused awaiting required reviewer approval for the `production` environment. A designated reviewer logs in and clicks **Approve**. What happens next?

- A) The reviewer must also click a separate "Release" button in the deployment history before execution resumes
- B) GitHub resumes the workflow run; the job that was waiting for approval begins executing its steps
- C) All jobs in the workflow are retried from the beginning to ensure a clean execution context
- D) The approval is queued and the next scheduled maintenance window is used for the actual deployment

---

### Question 69 — Reviewing Deployments

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What reviewers can examine during approval

**Scenario**:
A required reviewer receives a notification to approve a production deployment. Before approving, the reviewer wants to understand what context is available to inform their decision.

**Question**:
Which of the following can a reviewer examine from the deployment approval screen?

- A) The full diff of code changes included in this deployment compared to the last production deployment
- B) The workflow run log output, the deployment context (branch, commit SHA, triggering actor), and any environment-specific review comments
- C) The complete contents of all repository secrets used by the deployment job
- D) The test coverage percentage and lint score automatically computed from the workflow artifacts

---

### Question 70 — Reviewing Deployments

**Difficulty**: Hard
**Answer Type**: all
**Topic**: Deployment protection rules for enterprise environments

**Question**:
Which of the following statements about GitHub environment deployment protection rules are **all** correct?

- A) Required reviewer counts can include both individual GitHub users and GitHub teams
- B) A reviewer who initiated the workflow run that triggered the deployment cannot approve their own deployment — GitHub enforces this restriction
- C) The `wait-timer` runs independently of required reviewer approval; both must complete before the protected environment job executes
- D) Environment protection rules apply to any job in any workflow that references the environment via `jobs.<job_id>.environment:`, regardless of the calling repository

---

### Question 71 — Reviewing Deployments

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Preventing self-approval security concern

**Scenario**:
An organization wants to ensure that the developer who triggered a production deployment cannot also approve it, maintaining separation of duties.

**Question**:
How does GitHub enforce this requirement with environment protection rules?

- A) There is no built-in restriction — organizations must implement this via a custom webhook that validates reviewer identity against the triggering actor
- B) GitHub prevents the person who triggered the workflow run from approving their own deployment when the environment has required reviewers configured
- C) Organizations must configure an `approver-exclusion-list` variable on the environment naming the actors who cannot approve
- D) This restriction only applies to environments with more than 3 required reviewers

---

### Question 72 — Reviewing Deployments

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Wait timer interaction with reviewer approval

**Scenario**:
An environment has both a 10-minute `wait-timer` and one required reviewer. A reviewer approves the deployment after only 3 minutes.

**Question**:
When does the deployment job begin executing its steps?

- A) Immediately when the reviewer approves, because reviewer approval takes priority over the wait timer
- B) After the full 10-minute wait timer completes, because the timer and approval are both required independently — the job starts after the longer condition is satisfied
- C) After 5 minutes — the wait timer is halved when a reviewer approves early
- D) The next time the workflow is re-triggered, since an early approval resets the run state

---

### Question 73 — Reviewing Deployments

**Difficulty**: Medium
**Answer Type**: none
**Topic**: Deployment history misconceptions

**Question**:
A compliance officer lists statements they believe are true about GitHub's deployment history and audit trail for protected environments. Which of the following is an actual documented behavior?

- A) GitHub permanently stores the full content of all approval comments and decisions in an immutable audit log that cannot be deleted by repository administrators
- B) Rejected deployments are automatically re-queued after 24 hours so deployment delays do not block releases indefinitely
- C) Deployment approvals are recorded with who approved and when, but the log is only retained for 30 days before automatic purge
- D) When a reviewer approves a deployment via the GitHub mobile app, the approval is not recognized and the workflow remains paused until a web browser approval is received

---

### Question 74 — Creating and Publishing Actions

**Difficulty**: Easy
**Answer Type**: one
**Topic**: action.yml required fields

**Question**:
When creating a custom GitHub Action, which field is **required** in the `action.yml` metadata file?

- A) `author:`
- B) `description:`
- C) `branding:`
- D) `outputs:`

---

### Question 75 — Creating and Publishing Actions

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Composite action capabilities and limitations

**Scenario**:
A platform team is evaluating composite actions as an alternative to shell scripts for encapsulating build steps.

**Question**:
**(Select all that apply)** Which statements about composite actions are correct?

- A) Composite action steps can use `uses:` to call other GitHub Actions, enabling composition of multiple upstream actions
- B) Composite actions can define `jobs:` blocks to run steps in parallel on different runners
- C) Composite action steps support `if:` conditions on individual steps just like workflow steps
- D) Composite actions can accept `inputs:` and produce `outputs:` that are accessible to the calling workflow step

---

### Question 76 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Versioning actions with tags and SHAs

**Scenario**:
A team publishes a JavaScript action and wants consumers to be able to reference the latest stable version using a mutable tag (`v2`) that always points to the latest `2.x.x` release.

**Question**:
Which versioning approach correctly implements this pattern?

- A) Use `npm version major` to update `package.json` — GitHub automatically creates a corresponding `v2` tag
- B) After releasing `v2.1.0`, move the `v2` tag forward to the same commit using `git tag -fa v2 -m "Update v2 tag"` and `git push origin v2 --force`
- C) Create a branch named `v2` from the release commit; consumers use `@v2` to reference this branch
- D) Set `default_version: v2` in the action's `action.yml` file to redirect `@v2` references automatically

---

### Question 77 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Docker container actions vs JavaScript actions

**Scenario**:
A team needs to create a custom action and is choosing between a Docker container action and a JavaScript action.

**Question**:
**(Select all that apply)** Which statements correctly describe trade-offs between Docker container actions and JavaScript actions?

- A) JavaScript actions execute directly on the runner and start faster than Docker container actions; Docker container actions require pulling or building an image
- B) Docker container actions can only run on Linux GitHub-hosted runners; they do not run on Windows or macOS runners
- C) JavaScript actions must be written in Node.js; Docker container actions can be implemented in any language
- D) Docker container actions provide a more consistent and isolated execution environment because the container image controls all dependencies

---

### Question 78 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: GitHub Marketplace publishing requirements

**Scenario**:
A developer has created a useful GitHub Action and wants to publish it to the GitHub Marketplace so others can discover and use it.

**Question**:
Which requirement must be met for an action to be listed on the GitHub Marketplace?

- A) The action repository must have at least 100 stars before it can be submitted for Marketplace listing
- B) The action must be in a public repository, the `action.yml` (or `action.yaml`) file must be present in the root of the repository, and the repository must not have another Marketplace listing
- C) The action must pass a GitHub security review that validates the action does not access secrets or make external network calls
- D) The action must include a Docker image published to GitHub Container Registry as part of the Marketplace submission

---

### Question 79 — Managing Runners

**Difficulty**: Medium
**Answer Type**: one
**Topic**: runs-on with multiple labels

**Scenario**:
A workflow job requires a self-hosted runner that has three specific labels: `self-hosted`, `linux`, and `gpu`. Only runners with all three labels should be eligible to pick up the job.

**Question**:
Which `runs-on:` configuration correctly requires all three labels?

- A) `runs-on: self-hosted, linux, gpu` (comma-separated string)
- B) `runs-on: [self-hosted, linux, gpu]` (YAML array)
- C) `runs-on: self-hosted` with a separate `runner-labels: [linux, gpu]` key
- D) `runs-on: self-hosted` and `labels: [linux, gpu]` at the job level

---

### Question 80 — Managing Runners

**Difficulty**: Hard
**Answer Type**: all
**Topic**: Self-hosted runner security for public repositories

**Question**:
Which of the following security considerations about self-hosted runners are **all** correct and apply specifically to public repositories?

- A) Using self-hosted runners for public repositories is risky because any fork can submit a pull request that triggers a workflow and potentially executes malicious code on the self-hosted runner
- B) Ephemeral (just-in-time) self-hosted runners mitigate the persistence risk because the runner host is destroyed after each job, preventing sensitive data from persisting between workflow runs
- C) Using `pull_request` (not `pull_request_target`) for fork PR workflows is safer because it runs with limited permissions and does not have access to secrets on self-hosted runners
- D) GitHub recommends configuring runner groups with access restricted to specific repositories to limit which workflows can use a given self-hosted runner

---

### Question 81 — Managing Runners

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Ephemeral (just-in-time) runner registration

**Scenario**:
An organization wants to provision self-hosted runners that are automatically deprovisioned after each workflow job to eliminate the risk of a compromised workflow affecting subsequent runs.

**Question**:
Which runner configuration achieves this ephemeral behavior?

- A) Configure the runner with `--ephemeral` flag so it deregisters itself after completing one job
- B) Set `cleanup: after-each-job: true` in the runner configuration file
- C) Create a separate runner registration token for each job via the REST API
- D) Set `max-jobs: 1` in GitHub runner group settings to limit each runner to one concurrent job

---

### Question 82 — Managing Runners

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Runner group concepts

**Question**:
What is the primary purpose of runner groups in GitHub Actions?

- A) To balance CI load by distributing jobs round-robin across runners with identical labels
- B) To control access to self-hosted runners by grouping them and configuring which organizations and repositories can use runners in each group
- C) To configure billing groups for tracking runner usage costs per team
- D) To apply shared labels to multiple runners simultaneously without configuring each runner individually

---

### Question 83 — Managing Runners

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Runner group access control

**Scenario**:
An enterprise organization has high-security self-hosted runners with specialized hardware. They want to restrict these runners to specific repositories and prevent their use by forked repositories.

**Question**:
**(Select all that apply)** Which runner group settings should be configured to enforce this?

- A) Restrict runner group access to "Selected repositories" and explicitly add only the approved repositories
- B) Disable the "Allow public repositories" setting on the runner group to prevent fork-sourced workflows from using these runners
- C) Set the runner group to "Organization-level" visibility with no further restrictions — organization membership is sufficient security for internal runners
- D) Enable the "Require workflow approval for fork pull requests" setting at the organization level to add an approval gate before fork workflows can run

---

### Question 84 — GitHub Actions Enterprise

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Required workflows enforcement

**Scenario**:
An enterprise security team wants to ensure that a specific compliance-scanning workflow runs on every repository in the organization, even if repository owners configure their own workflows.

**Question**:
Which GitHub Actions enterprise feature enables this enforcement?

- A) Workflow templates, which suggest starter workflows to new repositories
- B) Required workflows, which enterprise administrators can configure to run on all repositories matching a filter, regardless of the repository's own workflow configuration
- C) Organization-level secrets, which ensure compliance tools can access necessary credentials
- D) Protected branches, which prevent repositories from disabling the compliance workflow

---

### Question 85 — GitHub Actions Enterprise

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Enterprise policy hierarchy

**Scenario**:
An enterprise administrator sets a policy that restricts all organizations to only use actions from within the enterprise (no third-party actions). An organization administrator wants to allow one specific third-party action for their organization.

**Question**:
**(Select all that apply)** Which statements correctly describe how the enterprise policy hierarchy works?

- A) Enterprise-level policies set the outer bounds; organization administrators can only grant permissions within what the enterprise policy allows
- B) An organization administrator can override an enterprise-level restriction for their organization if they have organization Owner role
- C) Repository administrators can further restrict (but not expand) what the organization policy permits for their repository
- D) If the enterprise policy prohibits third-party actions, the organization administrator cannot add exceptions without the enterprise administrator changing the enterprise policy

---

### Question 86 — GitHub Actions Enterprise

**Difficulty**: Medium
**Answer Type**: many
**Topic**: GitHub Enterprise Server (GHES) specific considerations

**Scenario**:
A company runs GitHub Enterprise Server (GHES) in a self-hosted data center with no direct internet access. The team needs to run GitHub Actions workflows.

**Question**:
**(Select all that apply)** Which statements reflect GHES-specific constraints or requirements for GitHub Actions?

- A) GHES requires GitHub-connected or self-hosted runners; GitHub-hosted runners (ubuntu-latest, etc.) in GitHub.com infrastructure are not available to GHES instances
- B) Actions from the GitHub Marketplace must be manually synced to GHES using the `actions-sync` tool or GitHub Connect if the instance has no internet access
- C) GHES supports all the same trigger events as GitHub.com, with no feature gaps
- D) Large-file storage for artifacts and caches on GHES requires a separately configured blob storage backend (Azure Blob Storage, AWS S3, or MinIO)

---

### Question 87 — GitHub Actions Enterprise

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Internal repository visibility for sharing actions

**Scenario**:
A platform team in a GitHub Enterprise organization creates a custom action in a private repository. They want all other repositories in the **same organization** to be able to use this action without making it public.

**Question**:
What repository visibility setting and configuration enables this cross-repository action sharing within the organization?

- A) Keep the repository private and set `allow-actions-from: organization` in each consuming repository's settings
- B) Change the repository visibility to `internal` — internal repositories in the same enterprise can be referenced by workflows in any repository in the same organization (with appropriate access settings)
- C) Set the repository to private with "Allow other repositories in this org" enabled under Actions → General → Access
- D) Publish the action to the GitHub Marketplace with `internal` visibility so only organization members can discover it

---

### Question 88 — GitHub Actions Enterprise

**Difficulty**: Easy
**Answer Type**: none
**Topic**: GITHUB_TOKEN triggering new workflow runs

**Question**:
A developer lists scenarios where they believe actions performed by `GITHUB_TOKEN` during a workflow run will automatically trigger additional GitHub Actions workflow runs. Which of the following is an actual case where `GITHUB_TOKEN`-initiated events trigger new workflow runs?

- A) A workflow pushes a commit using `GITHUB_TOKEN` — this commit triggers the repository's `push`-event workflows
- B) A workflow creates a pull request using `GITHUB_TOKEN` — this creation triggers `pull_request: [opened]` workflows
- C) A workflow creates a release using `GITHUB_TOKEN` — this triggers `release: [published]` workflows
- D) A workflow calls the REST API with `GITHUB_TOKEN` to dispatch a `workflow_dispatch` event — this triggers the target workflow

---

### Question 89 — GitHub Actions Enterprise

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Enterprise audit log for Actions

**Scenario**:
An enterprise compliance team needs to audit which workflows ran, who triggered them, when they ran, and what secrets were accessed across all organizations in the enterprise.

**Question**:
How can enterprise administrators access this comprehensive audit information?

- A) Each repository's Actions tab shows historical data; compliance teams must manually aggregate across all repositories
- B) The enterprise-level audit log captures Actions events (workflow runs, secret access, permission changes) and is queryable via the GitHub Enterprise audit log API or exported to a SIEM for aggregation
- C) GitHub provides a pre-built compliance dashboard in the Enterprise overview page that automatically generates weekly reports
- D) Audit log data for Actions is only available via webhook delivery to an external logging system configured at organization level

---

### Question 90 — Security and Optimization

**Difficulty**: Hard
**Answer Type**: all
**Topic**: OIDC token configuration for cloud authentication

**Question**:
Which of the following statements about using OpenID Connect (OIDC) tokens for cloud provider authentication in GitHub Actions are **all** correct?

- A) The workflow job must have `permissions: id-token: write` to request an OIDC token — without this permission, the `getIDToken()` function in the cloud auth action will fail
- B) OIDC eliminates the need to store long-lived cloud credentials in GitHub secrets; the workflow exchanges a short-lived OIDC token for a temporary cloud provider credential
- C) The cloud provider trust policy must be configured to accept OIDC tokens issued by `token.actions.githubusercontent.com` and can be scoped to specific repositories, branches, or environments
- D) OIDC tokens are valid for 24 hours after issuance, giving workflows a full day to complete their cloud operations using the exchanged credentials

---

### Question 91 — Security and Optimization

**Difficulty**: Medium
**Answer Type**: one
**Topic**: SHA pinning for third-party actions

**Scenario**:
A security-conscious team uses several third-party actions. A security engineer recommends pinning all third-party action references to full commit SHAs instead of mutable version tags.

**Question**:
Why is SHA pinning considered more secure than using a mutable tag like `@v3`?

- A) SHA references are shorter and load faster from the GitHub actions cache
- B) A mutable tag can be moved to point to a different commit by the action author (including a malicious commit); a full commit SHA is immutable and guarantees the exact code that was previously reviewed will be executed
- C) GitHub scans SHA-pinned actions for known vulnerabilities automatically, but does not scan tag-pinned actions
- D) SHA pinning prevents the action from being updated by Dependabot, eliminating automated supply-chain risks

---

### Question 92 — Security and Optimization

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Script injection via untrusted context properties

**Scenario**:
A workflow step constructs a shell command using an expression from the `github` context:

```yaml
- name: Greet contributor
  run: echo "Thanks for contributing, ${{ github.event.pull_request.title }}!"
```

**Question**:
What security vulnerability does this introduce?

- A) The `github.event.pull_request.title` property is encrypted and cannot be decoded in a shell command
- B) A PR title is attacker-controlled input; if it contains shell metacharacters (e.g., `$(malicious_command)`), they will be interpolated directly into the shell command, enabling command injection
- C) This works correctly for trusted collaborators but fails silently when triggered by external contributors
- D) This approach is fine because GitHub sanitizes all `github.event.*` properties before they are used in expressions

---

### Question 93 — Security and Optimization

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Dependabot version updates for Actions dependencies

**Scenario**:
A security team wants to ensure that GitHub Actions referenced in their workflows (e.g., `actions/checkout@v4`) are regularly updated to include security patches.

**Question**:
**(Select all that apply)** Which statements about using Dependabot for GitHub Actions version updates are correct?

- A) Adding a Dependabot `package-ecosystem: "github-actions"` entry in `.github/dependabot.yml` enables automated PR creation for action version updates
- B) Dependabot can update both actions pinned to version tags (e.g., `@v4`) and actions pinned to full commit SHAs
- C) Dependabot will automatically merge all Actions update PRs without human review if the repository has auto-merge enabled
- D) Dependabot's update schedule for Actions can be configured with the same `schedule.interval` options (daily, weekly, monthly) as other package ecosystems

---

### Question 94 — Security and Optimization

**Difficulty**: Hard
**Answer Type**: one
**Topic**: GITHUB_TOKEN vs PAT for cross-repository operations

**Scenario**:
A workflow in Repository A needs to trigger a workflow in Repository B by dispatching a `repository_dispatch` event. The team considers using `GITHUB_TOKEN` for authentication.

**Question**:
Why will using `GITHUB_TOKEN` fail for this cross-repository trigger, and what is the recommended alternative?

- A) `GITHUB_TOKEN` fails because it expires after 10 minutes; a PAT with a 90-day expiry is the minimum required for cross-repository calls
- B) `GITHUB_TOKEN` is scoped to the repository where the workflow is running and cannot authenticate against a different repository's API endpoints; a PAT, a GitHub App installation token, or `secrets.GITHUB_TOKEN` from a centralized permissions model is required for cross-repository operations
- C) `GITHUB_TOKEN` lacks the `dispatch` permission scope; adding `permissions: dispatch: write` at the job level resolves this
- D) Cross-repository `repository_dispatch` events require an OAuth token — PATs are not supported for this operation

---

### Question 95 — Security and Optimization

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Fork pull request security model

**Scenario**:
A public repository uses a CI workflow that builds and tests code. A contributor from a fork submits a pull request. The workflow is triggered by `on: pull_request`.

**Question**:
Which statement correctly describes the security model applied to this fork-triggered `pull_request` workflow run?

- A) Fork PR workflows run with full write access to the base repository because the `pull_request` event grants elevated permissions needed for the CI workflow to post status checks
- B) Fork PR workflows run with read-only permissions and do not have access to repository secrets; this is a deliberate security isolation designed to prevent untrusted fork code from accessing sensitive data
- C) Fork PR workflows are automatically blocked until a maintainer approves them, regardless of whether the contributor is a first-time contributor
- D) Fork PR workflows have access to environment secrets but not repository secrets, balancing functionality with security

---

### Question 96 — Common Failures and Troubleshooting

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Workflow not triggering after push

**Scenario**:
A developer pushes code to a branch but the expected workflow does not appear in the Actions tab. The workflow is triggered by `on: push` with `branches: [main]`. The push was to a branch named `feature/login`.

**Question**:
What is the most likely reason the workflow did not trigger?

- A) The workflow file has a YAML syntax error that prevents parsing
- B) The push was to `feature/login`, which does not match the `branches: [main]` filter; the workflow only triggers on pushes to `main`
- C) GitHub requires a 60-second delay after a push before workflow triggers become active
- D) The `on: push` trigger requires the `contents: read` permission to be explicitly declared

---

### Question 97 — Common Failures and Troubleshooting

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Diagnosing 403 permission errors in workflows

**Scenario**:
A workflow step that calls the GitHub API fails with HTTP 403. The step uses `${{ secrets.GITHUB_TOKEN }}` for authentication. The organization uses restricted default permissions for `GITHUB_TOKEN`.

**Question**:
**(Select all that apply)** Which approaches should be investigated to resolve the 403 error?

- A) Check whether the required permission scope (e.g., `issues: write`, `pull-requests: write`) is declared in the workflow's `permissions:` block
- B) Verify that the workflow file is in the default branch — workflows in non-default branches do not receive `GITHUB_TOKEN` by default
- C) Check if the organization's `GITHUB_TOKEN` default permissions are set to `restricted`; if so, the required scope must be explicitly granted
- D) Ensure the `GITHUB_TOKEN` secret is not expired — it refreshes every 30 days and operations fail during the refresh window
- E) Confirm that the operation is within the scope of what `GITHUB_TOKEN` can do; some operations (like cross-repository triggers) require a PAT or GitHub App token regardless of permissions

---

### Question 98 — Common Failures and Troubleshooting

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Exit code and step failure behavior

**Scenario**:
A `run:` step executes a shell script. The script exits with code 1 (indicating failure). The step does not have `continue-on-error: true` set.

**Question**:
What is the default behavior when a `run:` step exits with a non-zero exit code?

- A) The step is retried up to 2 times before being marked as failed
- B) The step is marked as failed, subsequent steps in the job are skipped (unless they have `if: always()`), and the job is marked as failed
- C) The step is marked as failed but the job continues normally with the next step
- D) GitHub interprets exit code 1 as a warning; only exit codes 2 and above cause step failure

---

### Question 99 — Common Failures and Troubleshooting

**Difficulty**: Hard
**Answer Type**: all
**Topic**: Understanding workflow queue and concurrency failures

**Question**:
Which of the following statements about GitHub Actions workflow queuing and concurrency behavior are **all** correct?

- A) A workflow with `concurrency: group: ${{ github.ref }}` and `cancel-in-progress: true` will cancel any in-progress run for the same ref when a new run is queued
- B) If `cancel-in-progress: false` (the default when `cancel-in-progress` is omitted), a new run for the same concurrency group waits in a pending state — only one pending run is queued; additional runs replace the pending one
- C) Job-level concurrency groups can be different from the workflow-level concurrency group, allowing fine-grained control at the job level without affecting the whole workflow
- D) A workflow run stuck in "Queued" status that never progresses to "In progress" may indicate that no eligible runner (matching the `runs-on` labels) was available within the queue timeout window

---

### Question 100 — Common Failures and Troubleshooting

**Difficulty**: Medium
**Answer Type**: none
**Topic**: Cancelled workflow behavior misconceptions

**Question**:
A developer lists statements they believe are true about cancelled GitHub Actions workflow runs. Which of the following is an actual documented behavior when a workflow run is cancelled?

- A) Steps with `if: always()` continue to execute even after the workflow is cancelled, allowing cleanup operations to complete
- B) Canceling a workflow run causes GitHub to automatically roll back any deployments that were initiated during the cancelled run
- C) A cancelled workflow run counts against the repository's monthly Actions minutes exactly like a successful run, billed to the full timeout-minutes duration
- D) If a matrix workflow run is cancelled, only the currently executing matrix jobs are stopped; queued matrix jobs that have not yet started are also automatically started and run to completion before cancellation takes effect

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | A | `yaml.schemas` in `settings.json` associates a JSON schema URL with file path patterns; this is the standard way to apply the GitHub workflow schema in VS Code with the YAML extension. | 01-GitHub-Actions-VS-Code-Extension.md | Easy |
| 2 | B | The extension is an authoring-assistance tool and does not execute remote workflow runs. Runs are triggered via push events, the GitHub UI Actions tab, or the REST API. | 01-GitHub-Actions-VS-Code-Extension.md | Medium |
| 3 | A, B, C | The extension validates YAML structure, checks permission scope names, and validates `needs:` job references. C: Extension does validate that referenced jobs exist. D: Live tag existence on GitHub requires a network call the extension does not make. | 01-GitHub-Actions-VS-Code-Extension.md | Medium |
| 4 | A, B, C, D | All four statements are correct: `github.` autocomplete uses the official schema, secrets names are suggested but never values, hover shows `action.yml` metadata, and unclosed delimiters are flagged. | 01-GitHub-Actions-VS-Code-Extension.md | Hard |
| 5 | None | None of the listed capabilities (local Docker execution, auto-push on save, live log streaming, secret sync) are provided by the extension. The extension is a static authoring tool. | 01-GitHub-Actions-VS-Code-Extension.md | Medium |
| 6 | B | `github.event_name` contains the name of the event that triggered the workflow (e.g., `push`, `pull_request`). `github.trigger` and `github.event.type` do not exist. | 02-Contextual-Information.md | Easy |
| 7 | A | `github.ref_type == 'tag'` checks that the trigger was a tag push; `startsWith(github.ref_name, 'v')` checks the tag name prefix. The other options use non-existent properties or invalid expression syntax. | 02-Contextual-Information.md | Medium |
| 8 | A, B, D | `github.head_ref` is the source branch and `github.base_ref` is the target. D: `github.event.pull_request.base.ref` is also valid. C is incorrect: `github.ref_name` for pull_request events is `<PR_NUMBER>/merge`, not the head branch name. | 02-Contextual-Information.md | Medium |
| 9 | B | Job outputs must be declared in an `outputs:` map at the job level, mapping names to step output expressions (`${{ steps.<id>.outputs.<name> }}`). Without this declaration, downstream jobs cannot access the values via `needs.<job>.outputs`. | 02-Contextual-Information.md | Medium |
| 10 | A, B, C, D, E | All five options are correct: `vars` is available in workflow-level `env:` (A); `vars` IS available in `runs-on` per GitHub's context availability docs (B); `vars` values are never masked unlike secrets (C); org-level variables flow down to repositories (D); `vars` is available in the `run-name:` key (E). | 02-Contextual-Information.md | Hard |
| 11 | B | `run-name` only supports `github`, `inputs`, and `vars` contexts. `env`, `secrets`, `runner`, `job`, `needs`, `matrix`, and `strategy` are not available at this evaluation point. | 03-Context-Availability-Reference.md | Easy |
| 12 | B | `jobs.<job_id>.if` is evaluated before a runner is assigned; the `runner` context is a runtime context that requires an active runner and is therefore not available at the job-level conditional evaluation stage. | 03-Context-Availability-Reference.md | Medium |
| 13 | B | This pattern IS valid — `fromJSON(needs.config.outputs.targets)` in `strategy.matrix` is a documented pattern for dynamic matrix generation. The `needs` context is available in `strategy.matrix` when listing outputs from a dependency. | 03-Context-Availability-Reference.md | Medium |
| 14 | A, C | A: `secrets` is not available in `jobs.<job_id>.runs-on` — attempting to use it there violates context availability rules. C: The `matrix` context is not available within `jobs.<job_id>.strategy.matrix` itself (circular reference). B is valid: `runner.arch` IS available in step-level `with:`. D is valid: `needs` IS available in step-level contexts. | 03-Context-Availability-Reference.md | Hard |
| 15 | B | Direct secret interpolation into `run:` commands embeds the value in the shell command string. Process listings, shell debug traces (`set -x`), and error output can expose the value even when GitHub's log masking is active. | 03-Context-Availability-Reference.md | Hard |
| 16 | B | `concurrency:` defines a group name; only one run per group executes at a time. With `cancel-in-progress: true`, the in-progress run is canceled when a new run is queued for the same group. | 04-Workflow-File-Structure.md | Easy |
| 17 | C | The default job timeout in GitHub Actions is 360 minutes (6 hours). A job exceeding this limit is automatically canceled. | 04-Workflow-File-Structure.md | Easy |
| 18 | B | `continue-on-error: true` at the job level marks the individual job's failure as non-fatal — it does not cancel other matrix jobs and does not cause the overall workflow to fail, even when `fail-fast` defaults to `true`. | 04-Workflow-File-Structure.md | Medium |
| 19 | A, B, C | `contents: write` is needed to push tags; `packages: write` to publish to GitHub Packages; `pull-requests: write` to post comments. D (`deployments: write`) is not required for these three operations. | 04-Workflow-File-Structure.md | Medium |
| 20 | B | `jobs.<job_id>.services:` is the correct mechanism. GitHub starts containers defined there before any job steps run and assigns each service a hostname matching its service key name, making them network-accessible to steps. | 04-Workflow-File-Structure.md | Medium |
| 21 | B | Base matrix: 2 × 3 = 6 combinations. The `exclude` removes `windows-latest + node 18`, leaving 5. The `include` matches an existing combination (`ubuntu-latest + node 22`) and adds a property to it without creating a new job. Total: 5 jobs. | 04-Workflow-File-Structure.md | Hard |
| 22 | C | `workflow_dispatch` enables manual triggering from the GitHub UI or REST API with user-provided inputs defined under the trigger. `repository_dispatch` is for external systems; `workflow_call` is for reusable workflow invocation. | 05-Workflow-Trigger-Events.md | Easy |
| 23 | A | `pull_request_target` runs with write permissions and secrets of the base repo, but the workflow file executes from the base branch. If the workflow checks out and runs PR branch code, an attacker can execute arbitrary code with elevated privileges. | 05-Workflow-Trigger-Events.md | Medium |
| 24 | B | `workflow_run` with `types: [completed]` fires on any conclusion. To restrict to only successful runs, add `if: ${{ github.event.workflow_run.conclusion == 'success' }}` at the job or workflow level. `types: [success]` is not a valid type value. | 05-Workflow-Trigger-Events.md | Medium |
| 25 | A, B | `branches:` is required to restrict to matching branches; `paths:` is required to restrict to matching file changes. `branches-ignore:` and `paths-ignore:` would accomplish the opposite and cannot coexist with `branches:` or `paths:` respectively. | 05-Workflow-Trigger-Events.md | Medium |
| 26 | A | The `schedule` event produces no event payload (`github.event` is empty). Scheduled workflows are added to GitHub's job queue at the specified time but may be delayed due to infrastructure load and are not guaranteed to start at the exact cron time. | 05-Workflow-Trigger-Events.md | Hard |
| 27 | B | `echo "DEPLOY_ENV=production" >> $GITHUB_ENV` writes to the GitHub environment file in the correct format. A (`export`) only sets the variable for the current step process. C uses the deprecated `::set-env` command. D is Windows syntax. | 06-Custom-Environment-Variables.md | Easy |
| 28 | C | Step-level `env:` takes the highest precedence, overriding both job-level and workflow-level values for that specific step's execution context. | 06-Custom-Environment-Variables.md | Medium |
| 29 | None | A is wrong — `$GITHUB_ENV` values do not cross job boundaries. B is wrong — the current step cannot immediately read what it just wrote; values are available to **subsequent** steps only. C is wrong — `$GITHUB_ENV` values are not masked. D is wrong — values do not persist between runs. | 06-Custom-Environment-Variables.md | Medium |
| 30 | B | If the API response contains a newline followed by `ADDITIONAL_VAR=malicious_value`, that string is appended to `$GITHUB_ENV` and the injected variable becomes available to all subsequent steps — a `GITHUB_ENV` injection attack. Always validate or sanitize untrusted external data before writing it to `$GITHUB_ENV`. | 06-Custom-Environment-Variables.md | Hard |
| 31 | A, B, D | A: `$GITHUB_ENV` correctly sets variables for subsequent steps. B: `$GITHUB_OUTPUT` sets step outputs that can be promoted to job outputs. D: `$GITHUB_STEP_SUMMARY` writes to the UI summary panel. C uses the deprecated `::set-output` command, which is no longer recommended. | 06-Custom-Environment-Variables.md | Medium |
| 32 | C | `GITHUB_WORKSPACE` contains the absolute path to the directory where the repository is checked out on the runner. | 07-Default-Environment-Variables.md | Easy |
| 33 | A, B, C, D | All four statements are correct: `GITHUB_RUN_ID` is stable across re-runs; `GITHUB_RUN_NUMBER` increments per new run without incrementing on re-runs; `GITHUB_RUN_ATTEMPT` increments per re-run; `GITHUB_SHA` for `pull_request` is the merge commit SHA. | 07-Default-Environment-Variables.md | Hard |
| 34 | B | Adding `permissions: contents: write` grants the `GITHUB_TOKEN` the write scope needed to create a GitHub Release. The other options either don't exist as valid settings or don't correctly address the issue. | 07-Default-Environment-Variables.md | Medium |
| 35 | A, B, C, D | All four statements are correct per GitHub's documentation on `GITHUB_REF`, `GITHUB_REF_NAME`, and `GITHUB_REF_TYPE` behavior across push, tag, and pull_request events. | 07-Default-Environment-Variables.md | Hard |
| 36 | B | `$GITHUB_ENV` sets variables available to subsequent steps in the same job. `$GITHUB_OUTPUT` sets step outputs that can be declared as job outputs and consumed by downstream jobs via `needs.<job>.outputs`. | 07-Default-Environment-Variables.md | Easy |
| 37 | B | Required reviewers pause the workflow when it reaches a job referencing the protected environment. The job's steps do not execute until a designated reviewer approves through the GitHub UI. | 08-Environment-Protection-Rules.md | Easy |
| 38 | A, B, C | The timer pauses the job before any steps execute (A). Timer and required reviews are independent — both must complete (B). The maximum timer value is 43,200 minutes / 30 days (C). D is incorrect — the reviewer approval does not cancel the wait timer when both are configured; both must complete. E is incorrect — the wait timer applies to any job referencing the environment. | 08-Environment-Protection-Rules.md | Medium |
| 39 | B | When deployment branch restrictions are configured and the triggering branch does not match any allowed pattern, the workflow run fails at the job that references the protected environment. There is no fallback or override mechanism at the job level. | 08-Environment-Protection-Rules.md | Medium |
| 40 | A, B, C, D | All four statements correctly describe environment secret behavior: scoping to the referencing job (A), precedence over repo secrets (B), availability only after protection rules are satisfied (C), and repo secrets being the fallback for jobs without environment references (D). | 08-Environment-Protection-Rules.md | Hard |
| 41 | B | When a job references an environment, environment-level secrets take precedence over repository-level secrets with the same name. The environment secret value is used, not the repository secret value. | 08-Environment-Protection-Rules.md | Medium |
| 42 | C | GitHub allows a maximum of 6 users or teams to be configured as required reviewers for a single environment. | 08-Environment-Protection-Rules.md | Medium |
| 43 | C | The default retention period for artifacts in public repositories on GitHub.com is 90 days. For private repositories, the organization or enterprise limit applies. | 09-Workflow-Artifacts.md | Easy |
| 44 | A, C, D | A correctly creates separate named artifacts. C: `if-no-files-found: error` is a valid option to fail if no files match. D: `retention-days:` is a valid per-artifact setting. B describes creating a single combined artifact, not separate ones. | 09-Workflow-Artifacts.md | Medium |
| 45 | A, C | A is correct: `download-artifact` in a downstream job retrieves what `upload-artifact` stored. C is correct: uploading multiple times with the same name in the same job merges files into the artifact. B is incorrect: `retention-days: 0` is not valid — minimum is 1. D is incorrect: same-name uploads in the same job merge, not overwrite. | 09-Workflow-Artifacts.md | Hard |
| 46 | A | `actions/download-artifact@v4` with `name: compiled-app` downloads the specified artifact to the working directory in the downstream job. The other options describe non-existent actions or incorrect behavior. | 09-Workflow-Artifacts.md | Medium |
| 47 | None | A is wrong — artifacts are not automatically available to future runs. B is wrong — `upload-artifact` can be configured to fail or warn on missing files with `if-no-files-found`. C is wrong — cross-repository artifact downloads require explicit permissions and the artifacts API. D is wrong — the uploading job itself cannot download its own artifacts without an explicit download step. | 09-Workflow-Artifacts.md | Medium |
| 48 | C | GitHub Actions allows up to 10 GB of cache storage per repository. Caches exceeding this limit cause older, less-recently-used caches to be evicted. | 10-Workflow-Caching.md | Easy |
| 49 | A, B, C, D | All four statements are correct: `hashFiles` computes content-based hashes (A); cache is restored before subsequent steps (B); keys are case-sensitive and cross-workflow shareable (C); `restore-keys` are used as ordered prefix fallbacks (D). | 10-Workflow-Caching.md | Hard |
| 50 | B | Cache scope in GitHub Actions: feature branches can access caches from their base branch (e.g., `main`), but `main` cannot access caches created by `feature/auth`. Caches are NOT globally shared across branches. | 10-Workflow-Caching.md | Hard |
| 51 | A, B, D | A: The first `restore-keys` entry is tried as a prefix match. B: Even with a partial restore, the exact new key is saved as a fresh cache entry at job end. D: If the first restore-key fails, the second is tried. C is incorrect — `restore-keys` are prefix matches, not exact matches. | 10-Workflow-Caching.md | Medium |
| 52 | B | `actions/setup-node@v4` supports a `cache: 'npm'` (or `'yarn'`, `'pnpm'`) input that automates dependency caching without a separate `actions/cache` step. | 10-Workflow-Caching.md | Medium |
| 53 | B | `on: workflow_call` is the trigger that designates a workflow as reusable, allowing other workflows to invoke it using `uses:`. | 11-Workflow-Sharing.md | Easy |
| 54 | A | Reusable workflows must declare a top-level `outputs:` block in the `workflow_call` trigger, mapping output names to job output expressions. Without this declaration, calling workflows cannot access values via `jobs.<job>.outputs`. | 11-Workflow-Sharing.md | Hard |
| 55 | A, B | A: Explicitly declaring and passing named secrets is the primary method. B: `secrets: inherit` passes all secrets from the caller automatically. C is incorrect — `$GITHUB_ENV` values do not cross workflow boundaries. D is incorrect — secrets cannot be passed as plain `inputs:` because they would be visible and unmasked. | 11-Workflow-Sharing.md | Medium |
| 56 | A, C, D | A: Composite actions run in the caller's job; reusable workflows run in their own separate jobs (correct). B: Composite actions cannot define `jobs:` — only steps. C: Reusable workflows support multi-job parallelism. D: Both types can use `uses:` in their steps and call other reusable workflows. | 11-Workflow-Sharing.md | Medium |
| 57 | None | A is wrong — inputs are available as `${{ inputs.name }}`, not automatic environment variables. B is wrong — there is no `secret: true` flag on inputs (that is what `secrets:` is for). C is wrong — boolean inputs remain as strings and require explicit conversion. D is wrong — default values are defined only in the reusable workflow's `workflow_call` declaration, not overridden from the caller. | 11-Workflow-Sharing.md | Medium |
| 58 | B | Setting the repository secret `ACTIONS_STEP_DEBUG` to `true` enables verbose debug logging for all steps in the workflow run without modifying the workflow YAML. | 12-Workflow-Debugging.md | Easy |
| 59 | B | `echo "::debug::message"` is the workflow command that writes a debug message. Debug messages only appear in the log when `ACTIONS_STEP_DEBUG` is enabled. | 12-Workflow-Debugging.md | Medium |
| 60 | B | `ACTIONS_STEP_DEBUG` enables verbose logging within step executions (shell traces, detailed command output). `ACTIONS_RUNNER_DEBUG` enables additional diagnostic logging from the runner agent itself (lifecycle events, network, plugin activity). The two are complementary and independent. | 12-Workflow-Debugging.md | Hard |
| 61 | A, B, D | A: `::notice::` creates a notice annotation. B: `::group::` / `::endgroup::` creates collapsible sections. D: `::warning::` creates a warning annotation. C is incorrect — `$GITHUB_STEP_SUMMARY` renders a Markdown job summary page, not log output. | 12-Workflow-Debugging.md | Medium |
| 62 | A | `echo "::group::Title"` opens a collapsible section and `echo "::endgroup::"` closes it. Option B uses the older `##[group]` syntax that is no longer the current standard. C and D use non-existent commands. | 12-Workflow-Debugging.md | Medium |
| 63 | B | The endpoint `POST /repos/{owner}/{repo}/actions/workflows/{workflow_id}/dispatches` triggers a `workflow_dispatch` event. `repository_dispatch` uses a different endpoint (`/dispatches` without the `actions/workflows` path). | 13-Workflows-REST-API.md | Easy |
| 64 | A, B, D, E | A: Listing runs with filters is supported. B: Canceling a run is supported (`DELETE /actions/runs/{run_id}`). D: Re-running runs and failed jobs is supported. E: Log download is supported. C is incorrect — workflow YAML content cannot be modified via the Actions API; that requires the Contents API and a commit. | 13-Workflows-REST-API.md | Medium |
| 65 | A, B, C, D | All four authentication methods are valid for GitHub REST API calls. PATs (A), `GITHUB_TOKEN` (B), GitHub App tokens (C), and OAuth tokens (D) are all accepted in the `Authorization: Bearer` header. | 13-Workflows-REST-API.md | Hard |
| 66 | B | The `conclusion` query parameter filters runs by conclusion value. Valid values include `failure`, `success`, `cancelled`, `skipped`, etc. The `status` parameter filters by run state (queued, in_progress, completed), not conclusion. | 13-Workflows-REST-API.md | Medium |
| 67 | C | The GitHub REST API supports a maximum of 100 results per page for workflow run listings (using `per_page=100`). Pagination via the `Link` header or `page` parameter is required for larger result sets. | 13-Workflows-REST-API.md | Medium |
| 68 | B | After a required reviewer approves the deployment, GitHub resumes the workflow run. The job that was paused at the environment protection gate begins executing its steps immediately. | 14-Reviewing-Deployments.md | Easy |
| 69 | B | From the approval screen, reviewers can see the workflow run log, the deployment context (branch, commit SHA, triggering actor), and leave review comments. They cannot see secret values, CI metrics, or code diffs directly in the approval interface. | 14-Reviewing-Deployments.md | Medium |
| 70 | A, B, C, D | All four statements are correct: reviewers can include users and teams (A); self-approval is prevented (B); wait timer and reviewer approval are independent conditions both required (C); protection rules apply to all referencing jobs regardless of repo (D). | 14-Reviewing-Deployments.md | Hard |
| 71 | B | GitHub enforces separation of duties for environment protection: the user who triggered the workflow run cannot serve as a reviewer for their own deployment. This is built into the environment protection rules mechanism. | 14-Reviewing-Deployments.md | Medium |
| 72 | B | Both the wait timer and required reviewer approval must independently complete. Even if a reviewer approves after 3 minutes, the job does not start until the full 10-minute timer also expires, whichever ends later. | 14-Reviewing-Deployments.md | Medium |
| 73 | None | A is incorrect — audit log data can be influenced by retention policies and is not guaranteed immutable from admin deletion at the repo level. B is incorrect — rejected deployments are not automatically re-queued. C is incorrect — the documented retention limit is not 30 days. D is incorrect — mobile approvals are recognized by GitHub. | 14-Reviewing-Deployments.md | Medium |
| 74 | B | The `description:` field is required in `action.yml`. `name:` is also required. `author:`, `branding:`, `inputs:`, and `outputs:` are all optional. | 15-Creating-Publishing-Actions.md | Easy |
| 75 | A, C, D | A: Composite action steps can use `uses:`. B is incorrect: composite actions cannot define `jobs:` blocks. C: Step-level `if:` conditions work in composite actions. D: Composite actions support `inputs:` and `outputs:`. | 15-Creating-Publishing-Actions.md | Hard |
| 76 | B | Moving the `v2` tag forward to the latest release commit (`git tag -fa v2`) and force-pushing it is the standard pattern for maintaining a mutable major version tag. This allows consumers using `@v2` to always get the latest 2.x.x release. | 15-Creating-Publishing-Actions.md | Medium |
| 77 | A, B, C, D | All four statements are correct trade-offs: JS actions start faster (A); Docker actions only run on Linux (B); Docker actions support any language (C); Docker provides environment consistency (D). | 15-Creating-Publishing-Actions.md | Medium |
| 78 | B | To publish to the Marketplace, the action must be in a public repository with a valid `action.yml` (or `action.yaml`) at the root, and the repository must not already have a different Marketplace listing. No star count, security review, or GHCR requirement exists. | 15-Creating-Publishing-Actions.md | Medium |
| 79 | B | `runs-on: [self-hosted, linux, gpu]` as a YAML array requires the runner to have ALL listed labels. A comma-separated string is not valid YAML for this key. C and D describe non-existent configuration keys. | 16-Managing-Runners.md | Medium |
| 80 | A, B, C, D | All four statements are correct security considerations: fork PRs risk code execution on self-hosted runners (A); ephemeral runners prevent persistence between runs (B); `pull_request` (not `pull_request_target`) limits permissions (C); runner groups restrict access by repository (D). | 16-Managing-Runners.md | Hard |
| 81 | A | Registering a runner with the `--ephemeral` flag configures it to accept a single job and then deregister itself. This prevents any sensitive data from the previous job from persisting to the next. | 16-Managing-Runners.md | Medium |
| 82 | B | Runner groups allow administrators to control which organizations and repositories can use specific self-hosted runners by grouping runners and setting access policies on each group. | 16-Managing-Runners.md | Easy |
| 83 | A, B, D | A: Restricting to "Selected repositories" is the primary access control. B: Disabling "Allow public repositories" prevents fork workflows from using these runners. D: The fork PR approval setting adds a gate before fork workflows execute. C is incorrect — org-level visibility without restrictions allows all repos in the org to use the runners, which is not sufficiently restrictive. | 16-Managing-Runners.md | Medium |
| 84 | B | Required workflows is an enterprise feature that allows enterprise administrators to define workflows that must run on all repositories (or a filtered subset) in all organizations under the enterprise, regardless of individual repository configuration. | 17-GitHub-Actions-Enterprise.md | Easy |
| 85 | A, C, D | A: Enterprise sets the outer bounds; orgs operate within those bounds (correct). B: Organization administrators cannot override enterprise-level restrictions — they can only work within the allowed bounds. C: Repo admins can further restrict but not expand org allowances (correct). D: The enterprise policy must be changed by the enterprise admin to allow exceptions (correct). | 17-GitHub-Actions-Enterprise.md | Medium |
| 86 | A, B, D | A: GHES must use self-hosted or GitHub-connected runners. B: Actions must be synced to air-gapped GHES. D: GHES requires external blob storage for artifacts and caches. C is incorrect — GHES may lag behind GitHub.com feature parity, including trigger events. | 17-GitHub-Actions-Enterprise.md | Medium |
| 87 | C | For private repositories in the same organization, setting the repository to allow access from other repositories in the organization (Settings → Actions → General → Access) enables cross-repo workflows to use the action without changing visibility to internal or public. For cross-org sharing, `internal` visibility is recommended. | 17-GitHub-Actions-Enterprise.md | Medium |
| 88 | None | `GITHUB_TOKEN`-initiated events do NOT trigger new workflow runs. This is a deliberate security design to prevent infinite workflow loops. Commits pushed by `GITHUB_TOKEN` do not trigger push workflows; PRs, releases, or dispatches created by `GITHUB_TOKEN` do not trigger their respective event workflows. | 17-GitHub-Actions-Enterprise.md | Easy |
| 89 | B | Enterprise-level audit logs capture GitHub Actions events (workflow runs, secret access, permission changes, runner activity) across all organizations. These are queryable via the audit log API and can be streamed to SIEM tools for compliance aggregation. | 17-GitHub-Actions-Enterprise.md | Hard |
| 90 | A, B, C | A: `id-token: write` is required to request OIDC tokens. B: OIDC eliminates long-lived secret storage by exchanging short-lived tokens. C: The cloud trust policy must be configured to accept GitHub's OIDC issuer and can be scoped granularly. D is incorrect — OIDC tokens are short-lived (minutes), not 24-hour tokens; the exchanged cloud credential duration depends on the cloud provider's configuration. | 18-Security-and-Optimization.md | Hard |
| 91 | B | A mutable version tag (e.g., `@v3`) can be moved to a different commit by the action author, including a malicious one. A full commit SHA (e.g., `@abc1234...`) is immutable — it guarantees the exact code reviewed during adoption will always run. | 18-Security-and-Optimization.md | Medium |
| 92 | B | `github.event.pull_request.title` is attacker-controlled (anyone can title a PR). Interpolating it directly into a `run:` command enables shell injection if the title contains metacharacters like `$(...)`, backticks, or semicolons. Use environment variables instead: `PR_TITLE=${{ github.event.pull_request.title }}` and reference `$PR_TITLE` in the shell. | 18-Security-and-Optimization.md | Medium |
| 93 | A, B, D | A: `package-ecosystem: "github-actions"` enables Actions update PRs. B: Dependabot updates both tag references and SHA pins. D: The same scheduling options (daily, weekly, monthly) apply. C is incorrect — Dependabot creates PRs but does not auto-merge; auto-merge requires a separate configuration and human/bot approval policy. | 18-Security-and-Optimization.md | Medium |
| 94 | B | `GITHUB_TOKEN` is scoped to the repository where the workflow runs and cannot authenticate API calls to a different repository. Cross-repository operations require a PAT with appropriate scope, a GitHub App installation token, or another credential with access to the target repository. | 18-Security-and-Optimization.md | Hard |
| 95 | B | For `on: pull_request` triggered from a fork, GitHub runs the workflow with read-only `GITHUB_TOKEN` permissions and no access to repository secrets. This prevents fork contributors from accessing sensitive data or performing write operations. | 18-Security-and-Optimization.md | Medium |
| 96 | B | The `branches: [main]` filter restricts the `push` trigger to only pushes targeting the `main` branch. A push to `feature/login` does not match this filter and therefore does not trigger the workflow. | 19-Common-Failures-Troubleshooting.md | Easy |
| 97 | A, C, E | A: A missing `permissions:` scope is the most common cause of 403 errors. C: Restricted default permissions require explicit scope grants. E: Some operations (cross-repo) are outside `GITHUB_TOKEN`'s capability regardless of scopes. B is incorrect — `GITHUB_TOKEN` is available regardless of branch. D is incorrect — `GITHUB_TOKEN` is provisioned per-job and does not have a refresh window that causes failures. | 19-Common-Failures-Troubleshooting.md | Medium |
| 98 | B | When a `run:` step exits with a non-zero exit code, the step is marked failed. Subsequent steps are skipped unless they have `if: always()` or `if: failure()`. The job is then marked as failed, which propagates to the overall workflow. | 19-Common-Failures-Troubleshooting.md | Medium |
| 99 | A, B, C, D | A: `cancel-in-progress: true` cancels the current run for the same concurrency group. B: Without `cancel-in-progress`, only one pending run is held; additional triggers replace the single pending slot. C: Job-level concurrency groups provide finer control. D: "Queued" runs stuck permanently usually indicate no matching runner is available. | 19-Common-Failures-Troubleshooting.md | Hard |
| 100 | None | A is incorrect — steps with `if: always()` do NOT execute when a workflow is cancelled; `always()` evaluates to `true` for success, failure, and cancelled states, but the runner does not process new steps after cancellation. B is incorrect — GitHub does not auto-rollback deployments. C is incorrect — cancelled jobs are billed only for the time that actually ran, not the full timeout. D is incorrect — queued matrix jobs are not started after cancellation; they are dropped. | 19-Common-Failures-Troubleshooting.md | Medium |

---

*End of GH-200 Iteration 6 — 100 Questions*
