# GitHub Actions GH-200 Certification Exam — Question Bank (Iteration 3)

**Generated**: March 20, 2026
**Total Questions**: 100
**Difficulty**: 20% Easy (20) / 60% Medium (60) / 20% Hard (20)
**Answer Types**: 55% one / 26% many / 12% all / 7% none
**Scenario-Based**: 73% (73 questions)
**Security Focus**: 12 questions (Topics 6, 7, 18)
**Enterprise Focus**: 9 questions (Topics 8, 14, 17)

---

## Topic 1: GitHub Actions VS Code Extension (4 Questions)

**Question 1** — VS Code Extension Features

**Difficulty**: Easy | **Answer Type**: one | **Topic**: Extension IntelliSense and Validation

**Question**: Which of the following capabilities does the GitHub Actions VS Code extension provide?

- A) Automatic deployment to production on file save
- B) Real-time YAML schema validation and context IntelliSense
- C) Direct execution of workflows on your local machine
- D) Automatic generation of GitHub Actions templates

**Answer**: B

---

**Question 2** — Extension Context IntelliSense

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Context Completion and Autocomplete

**Scenario**: You're authoring a workflow and type `${{ github.` in your step. The extension's IntelliSense appears with suggestions.

**Question** (Select all that apply): Which contexts would be suggested by the extension's IntelliSense?

- A) `github.actor` — username of the user who triggered the workflow
- B) `github.database` — direct access to GitHub's database
- C) `github.run_id` — unique workflow run identifier
- D) `github.event_name` — the trigger event that started the workflow

**Answer**: A, C, D

---

**Question 3** — Troubleshooting Extension Validation

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Extension Configuration and Troubleshooting

**Scenario**: Your workflow file in `.github/workflows/ci.yml` shows red squiggly lines, but your colleague's extension validates the same file correctly. You've restarted VS Code and confirmed the GitHub Actions extension is installed.

**Question**: What diagnostic step would most likely resolve the issue?

- A) Uninstall and reinstall the YAML extension (often a dependency)
- B) Delete your VS Code settings and reset defaults
- C) Downgrade to an older version of the GitHub Actions extension
- D) Convert the workflow file to JSON format

**Answer**: A

---

**Question 4** — Extension Warnings for SHA Pinning

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Action Pinning and Security Recommendations

**Scenario**: The extension shows a warning under `actions/checkout@main`. Your team currently uses semver pinning (e.g., `@v3`) in all workflows, citing ease of maintenance over security.

**Question**: Why would the extension recommend SHA pinning (`@abc123def...`) rather than semver tags?

- A) SHA references are faster to execute than tag-based references
- B) Semver tags can be retagged or deleted, introducing supply chain risk
- C) SHA pinning reduces the size of the workflow file in storage
- D) GitHub automatically requires SHA pinning for all public repositories

**Answer**: B

---

## Topic 2: Contextual Information (6 Questions)

**Question 5** — github Context Variables

**Difficulty**: Easy | **Answer Type**: one | **Topic**: github Context Structure

**Question**: Which expression correctly retrieves the login username of the user who triggered the workflow?

- A) `${{ github.user_name }}`
- B) `${{ github.actor }}`
- C) `${{ github.username }}`
- D) `${{ github.user.login }}`

**Answer**: B

---

**Question 6** — Multi-Context Usage in Workflows

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Context Availability and Scope

**Scenario**: Your workflow uploads a build artifact and needs to tag it with both the branch name and run number for traceability. You plan to construct this tag dynamically.

**Question** (Select all that apply): Which context expressions would work for this use case?

- A) `${{ github.ref_name }}` — branch or tag name without `refs/heads/` prefix
- B) `${{ github.run_number }}` — unique number for each workflow run
- C) `${{ runner.name }}` — the name of the GitHub runner
- D) `${{ github.run_id }}` — a unique identifier for the workflow run

**Answer**: A, B, D

---

**Question 7** — Event Payload Context

**Difficulty**: Medium | **Answer Type**: one | **Topic**: github.event Context

**Scenario**: Your workflow is triggered by a pull request. You need to access the base branch (target branch) of the PR in a step.

**Question**: Which expression retrieves the base branch name?

- A) `${{ github.event.pull_request.base.ref }}`
- B) `${{ github.event.base_branch }}`
- C) `${{ github.base_ref }}`
- D) `${{ github.event.target }}`

**Answer**: A

---

**Question 8** — Secrets Context and Masking

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Secrets Context Availability and Redaction

**Scenario**: You have a secret named `DB_PASSWORD` defined in your repository. You reference it in a workflow step: `echo "Password is ${{ secrets.DB_PASSWORD }}"`.

**Question** (Select all that apply): Which statements are true about secret handling?

- A) The secret's value will be printed to the console and visible in logs
- B) GitHub automatically redacts the secret from logs if it's 3+ characters
- C) You can echo the secret value to stdout; GitHub will mask it automatically
- D) If a step failure reveals the secret, GitHub allows manual log deletion

**Answer**: B

---

**Question 9** — Environment and Secrets Context Hierarchy

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Context Scope and Precedence

**Scenario**: Your workflow defines `env.API_KEY` at the workflow level and also sets `secrets.API_KEY` in the repository secrets. A step accesses `${{ env.API_KEY }}`.

**Question**: Which value does the step receive?

- A) The workflow-level `env.API_KEY` value
- B) The repository secret value
- C) Undefined (context types cannot overlap)
- D) The value depends on which was defined most recently in the file

**Answer**: A

---

**Question 10** — Matrix Context Variables

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Matrix Strategy Context

**Scenario**: Your matrix strategy is: `{ node-version: [16, 18, 20], os: [ubuntu-latest, windows-latest] }`. A step tries to access `${{ matrix.node-version }}`.

**Question**: What is the job matrix cardinality (total number of jobs), and what does `matrix.node-version` evaluate to within each job?

- A) 3 jobs total; `matrix.node-version` would be undefined
- B) 6 jobs total; `matrix.node-version` contains one of the 3 values per job
- C) 3 jobs total; `matrix.node-version` contains an array of all 3 values
- D) 2 jobs total; `matrix.node-version` always evaluates to 16

**Answer**: B

---

## Topic 3: Context Availability Reference (5 Questions)

**Question 11** — Context Availability at Different Workflow Keys

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Static vs. Runtime Context Evaluation

**Scenario**: You attempt to use `${{ secrets.DEPLOY_KEY }}` in the `env` block at the workflow level (not inside a job or step).

**Question**: What happens?

- A) The secret is available and properly interpolated
- B) The secret is not evaluated; the literal string `${{ secrets.DEPLOY_KEY }}` is used
- C) GitHub throws a validation error preventing the workflow from being saved
- D) The secret is evaluated but marked as masked in all downstream logs

**Answer**: B

---

**Question 12** — self Context in Job Conditions

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Context Scope by Workflow Key

**Scenario**: Your workflow needs to conditionally run a job based on the job's outputs. You're designing a `needs` expression to reference previous job data.

**Question** (Select all that apply): Which statements about `needs` context availability are correct?

- A) `needs` context is available only in job-level conditions or steps
- B) `needs` context references outputs from explicitly declared dependent jobs
- C) `needs` is only available if your job has `needs: [previous-job]` declared
- D) `needs.job-id.outputs.variable_name` retrieves output from a specific job

**Answer**: A, B, C, D

---

**Question 13** — Context Leakage and Secret Redaction

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Secret Leakage Prevention

**Scenario**: A workflow accidentally logs a secret value via `echo "${{ secrets.API_KEY }}"`. Later, you realize the value was a 16-character hex string. You check the logs expecting it to be redacted.

**Question**: What is the most likely outcome?

- A) The value is fully redacted (replaced with `***`)
- B) The value might not be redacted if it matches other known patterns in the log
- C) The value is redacted only if the substring appears 3+ times in the logs
- D) GitHub prevents the workflow from running if a secret is referenced in a print statement

**Answer**: B

---

**Question 14** — Matrix Context in Reusable Workflows

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Context Scope in Called Workflows

**Scenario**: Your caller workflow uses a matrix strategy. It calls a reusable workflow using `jobs.build.strategy.matrix.node-version` as an input parameter.

**Question**: How is the matrix context handled in the reusable workflow?

- A) The reusable workflow receives the full matrix and creates independent jobs for each combination
- B) Each matrix combination is a separate job that independently calls the reusable workflow, passing its specific matrix value
- C) The reusable workflow receives an array of all matrix values and must iterate
- D) Matrix context is unavailable in called workflows; only scalar inputs are permitted

**Answer**: B

---

**Question 15** — Actions Context in Composite Actions

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Composite Action Context Scope

**Scenario**: You're writing a composite action that needs to reference `github.repository` inside one of its run steps.

**Question**: Is `${{ github.repository }}` available in the composite action step?

- A) Yes, because the composite action runs in the context of the caller workflow
- B) No, composite actions have isolated context and cannot reference github context
- C) Yes, but only if the composite action explicitly declares its inputs/outputs first
- D) No, composite actions must use `inputs.*` instead of context variables

**Answer**: A

---

## Topic 4: Workflow File Structure (7 Questions)

**Question 16** — Workflow File Naming and Location

**Difficulty**: Easy | **Answer Type**: one | **Topic**: Workflow File Location and Discovery

**Question**: What is the correct directory structure for GitHub Actions workflow files?

- A) `/.github/workflows/` (any `.yml` or `.yaml` file)
- B) `/workflows/` or `/actions/workflows/`
- C) `/.workflows/` (hidden directory)
- D) `/actions/` (no subdirectory required)

**Answer**: A

---

**Question 17** — Job Dependencies and Needs

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Job Ordering and Dependencies

**Scenario**: Your workflow has three jobs: `build`, `test`, and `deploy`. The `test` job depends on `build`. The `deploy` job depends on both `build` and `test`.

**Question** (Select all that apply): Which configurations correctly express this dependency graph?

- A) `test: needs: [build]` and `deploy: needs: [build, test]`
- B) `test: needs: build` and `deploy: needs: test` (transitive dependency)
- C) Listing `needs: [build, test]` in deploy is explicit and recommended for clarity
- D) Both (A) and (B) are valid; (A) is recommended for explicit clarity

**Answer**: A, C, D

---

**Question 18** — Permissions Block Structure

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Fine-Grained Permission Scopes

**Scenario**: You want to allow a job to write releases and read pull requests, but explicitly deny write access to issues.

**Question**: Which permission configuration is correct?

- A) `permissions: { releases: write, pull-requests: read, issues: none }`
- B) `permissions: { releases: write, pull-requests: read, issues: false }`
- C) `permissions: [releases: write, pull-requests: read, -issues: write]`
- D) Both A and B are equivalent

**Answer**: A

---

**Question 19** — Container and Services Definition

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Container Jobs and Service Containers

**Scenario**: Your job needs to run inside a Node.js container and also spin up a PostgreSQL service for integration tests.

**Question** (Select all that apply): Which statements are correct about container and services configuration?

- A) Container and services are mutually exclusive; you can only use one per job
- B) Services run as sibling containers linked to the job container
- C) Both container and services use the same networking; service hostname is the service name
- D) You can configure environment variables separately for the container and each service

**Answer**: B, C, D

---

**Question 20** — YAML Anchors and Aliases

**Difficulty**: Hard | **Answer Type**: one | **Topic**: DRY Principles with YAML Anchors

**Scenario**: Your workflow repeats a common set of steps across multiple jobs. You define a YAML anchor: `defaults: &common_env { NODE_ENV: production, DEBUG: 'false' }`. Later, in two jobs, you use `env: *common_env`.

**Question**: What happens when two jobs reference the same anchor alias?

- A) Each job receives an independent copy of the anchor values; changes in one job don't affect the other
- B) Both jobs share the same object reference; modifications in one job affect the other at runtime
- C) The workflow fails because anchor aliases cannot be reused across jobs
- D) YAML anchors are resolved at workflow parse time; each job gets the defined values at its declaration point

**Answer**: D

---

**Question 21** — Conditional Step Execution

**Difficulty**: Hard | **Answer Type**: one | **Topic**: if Conditions and Status Checks

**Scenario**: Your workflow has a step that uploads artifacts, and you want it to run even if previous steps fail, but still fail gracefully if the upload step itself fails. Other steps should skip on failure.

**Question**: Which `if` condition achieves this?

- A) `if: always()`
- B) `if: failure()`
- C) `if: !cancelled()`
- D) `if: success() || failure()`

**Answer**: A

---

**Question 22** — Matrix Strategy with Exclude

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Matrix Strategy Configuration

**Scenario**: Your matrix is: `{ node: [16, 18, 20], os: [ubuntu-latest, macos-latest] }`. You want all 6 jobs except `(node: 20, os: macos-latest)` because Node 20 isn't stable on macOS runners yet.

**Question**: How do you configure the exclusion?

- A) `strategy: { matrix: { node: [16, 18, 20], os: [ubuntu-latest, macos-latest], exclude: [{node: 20, os: macos-latest}] } }`
- B) `strategy: { matrix: { node: [16, 18, 20], os: [ubuntu-latest], include: [{node: 20, os: ubuntu-latest}] } }`
- C) `strategy: { matrix: { node: [16, 18], os: [ubuntu-latest, macos-latest], include: [{node: 20}] } }`
- D) Both A and B are valid

**Answer**: A

---

## Topic 5: Workflow Trigger Events (7 Questions)

**Question 23** — push Trigger with Path Filters

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Path-Based Trigger Filtering

**Scenario**: Your repository has a web application in `/src` and documentation in `/docs`. You want CI to trigger on any push to `/src` but exclude changes to `/docs`.

**Question**: Which trigger configuration is correct?

- A) `on: { push: { paths: [src/**], paths-ignore: [docs/**] } }`
- B) `on: { push: { paths: [src/**] } }` (omit paths-ignore for clarity)
- C) `on: { push: { include: [src/**], exclude: [docs/**] } }`
- D) `on: { push: { branches: [main], paths: [src/**] } }`

**Answer**: B

---

**Question 24** — pull_request_target vs pull_request

**Difficulty**: Hard | **Answer Type**: many | **Topic**: Fork PR Security and Token Access

**Scenario**: Your repository is public, and you're concerned about untrusted PRs from forks accessing secrets. You're deciding between `pull_request` and `pull_request_target` events.

**Question** (Select all that apply): Which statements are accurate?

- A) `pull_request` runs with read-only token and secrets NOT available (safe for untrusted code)
- B) `pull_request_target` runs with repo write token and secrets available (risky; requires code review)
- C) `pull_request_target` checkout uses the PR's code; you must manually review before using secrets
- D) Both events trigger; use `pull_request` for builds and `pull_request_target` for deployments

**Answer**: A, B, C

---

**Question 25** — Scheduled Event (cron) Limitations

**Difficulty**: Medium | **Answer Type**: one | **Topic**: schedule Event Behavior

**Scenario**: You configure a workflow with `schedule: [cron: '0 0 * * *']` (daily at midnight UTC). The workflow hasn't run for 2 weeks because the repository had no commits and no scheduled runs.

**Question**: Why didn't the scheduled workflow run?

- A) Cron schedules only trigger on repositories with commits in the past 60 days
- B) GitHub requires at least one manual workflow run before enabling auto-scheduling
- C) The cron syntax is incorrect; you need to use a different format
- D) Repository inactivity doesn't prevent schedules; the workflow should have run (check GitHub status)

**Answer**: A

---

**Question 26** — workflow_dispatch Inputs

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Manual Trigger with Inputs

**Scenario**: You want users to manually trigger a deployment workflow and select from predefined environment options: `dev`, `staging`, `prod`.

**Question** (Select all that apply): How would you configure this?

- A) Use `workflow_dispatch` with `inputs: { environment: { type: choice, options: [dev, staging, prod] } }`
- B) Access the selected input in steps via `${{ github.event.inputs.environment }}`
- C) Use `workflow_run` instead of `workflow_dispatch` to allow manual triggering
- D) Set the input as required so the user must provide a value before running

**Answer**: A, B

---

**Question 27** — Trigger on Multiple Events with Different Conditions

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Complex Event Filtering

**Scenario**: Your deploy workflow should trigger on:
- Any push to `main` branch
- Any PR targeting `main` branch
- Manual trigger via workflow_dispatch
But NOT on PR comments or wiki updates.

**Question**: Which event configuration is most appropriate?

- A) `on: [push: { branches: [main] }, pull_request: { branches: [main] }, workflow_dispatch]`
- B) `on: { push: { branches: [main] }, pull_request: { branches: [main] }, workflow_dispatch }`
- C) `on: [push, pull_request, workflow_dispatch]` (filter by branch in the job condition)
- D) `on: { push: {branches: [main], types: [opened, synchronize] }, pull_request: {branches: [main]}, workflow_dispatch }`

**Answer**: B

---

**Question 28** — workflow_run Event for Cross-Workflow Automation

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Workflow Run Event

**Scenario**: You have a CI workflow that builds and tests. You want a separate notification workflow to run **after** CI completes (success or failure) to send Slack alerts.

**Question**: Which configuration allows the notification workflow to trigger after CI finishes?

- A) `on: { workflow_run: { workflows: [CI], types: [completed] } }`
- B) `on: { workflow: [CI completed] }`
- C) `on: { trigger: { workflow_name: CI, status: any } }`
- D) `on: [workflow_completion]`

**Answer**: A

---

**Question 29** — Event Payload Context Filtering

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Conditional Trigger Logic

**Scenario**: You want your deployment workflow to trigger **only** when a push includes a commit message starting with `[DEPLOY]`. Other commits should not trigger the workflow.

**Question**: What is the best approach?

- A) Use path filters to detect `[DEPLOY]` in commit messages
- B) Use `if: contains(github.event.head_commit.message, '[DEPLOY]')` in the job condition
- C) Split workflows by branch; use branches filter and manual workflow_dispatch
- D) Configure a webhook with custom logic outside GitHub

**Answer**: B

---

## Topic 6: Custom Environment Variables (5 Questions)

**Question 30** — Workflow-Level env Variable Scope

**Difficulty**: Easy | **Answer Type**: one | **Topic**: Environment Variable Scoping

**Question**: You define `env: { REGISTRY: ghcr.io }` at the workflow root level. Which jobs/steps can access `${{ env.REGISTRY }}`?

- A) Only steps in the first job
- B) All jobs and all steps in the workflow
- C) Only steps that explicitly redefine the variable
- D) Only the job-level env block

**Answer**: B

---

**Question 31** — Job and Step-Level env Override

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Environment Variable Precedence

**Scenario**: Workflow level: `env: { LOG_LEVEL: info }`. Job level: `env: { LOG_LEVEL: debug }`. A step accesses `${{ env.LOG_LEVEL }}`.

**Question**: What value does the step receive?

- A) `info` (workflow level always takes precedence)
- B) `debug` (job level overrides workflow level)
- C) Both values are accessible; the step must choose which one to use
- D) Undefined (overlapping env vars cause an error)

**Answer**: B

---

**Question 32** — GITHUB_OUTPUT for Inter-Step Communication

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Step Outputs and GITHUB_OUTPUT

**Scenario**: Your first step generates a version number. A subsequent step needs to use that version in a build command.

**Question** (Select all that apply): Which approaches set the output correctly?

- A) Use `echo "VERSION=1.0.0" >> $GITHUB_OUTPUT` in step 1, then `${{ steps.step1.outputs.VERSION }}` in step 2
- B) Set step 1 `id: version-step` and retrieve output via `${{ steps.version-step.outputs.VERSION }}`
- C) Write to a temporary file and read it in the next step via `${{ env }}` context
- D) Define `outputs:` section in step 1 and reference via `${{ steps.version-step.outputs.VERSION }}`

**Answer**: A, B

---

**Question 33** — Secrets in Environment Variables

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Secrets Management and Masking

**Scenario**: You reference a secret in an environment variable: `env: { DB_PASSWORD: ${{ secrets.DB_PASSWORD }} }`. A step exports this to stdout: `echo "DB_PASSWORD=$DB_PASSWORD"`.

**Question**: What appears in the logs?

- A) The actual secret value (unmasked because it's in an env var)
- B) The secret is masked automatically by GitHub (value replaced with `***`)
- C) A redaction error; GitHub prevents this pattern from running
- D) The literal string `${{ secrets.DB_PASSWORD }}` (not interpolated)

**Answer**: B

---

**Question 34** — Dynamic Environment Variables with Contexts

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Context Interpolation in env Blocks

**Scenario**: You define: `env: { IMAGE_TAG: ${{ github.sha }} }` at the workflow level. Later, a step tries to access `${{ env.IMAGE_TAG }}`.

**Question**: What is the value of `IMAGE_TAG`?

- A) The short commit SHA (first 7 characters)
- B) The full commit SHA (40 characters)
- C) Undefined (contexts cannot be used in workflow-level env)
- D) The literal string `${{ github.sha }}`

**Answer**: B

---

## Topic 7: Default Environment Variables (5 Questions)

**Question 35** — GITHUB_TOKEN Automatic Provisioning

**Difficulty**: Easy | **Answer Type**: one | **Topic**: Default Token Availability

**Question**: When is `GITHUB_TOKEN` automatically created by GitHub?

- A) At the start of each job
- B) At the start of each step
- C) Once per workflow run
- D) When explicitly referenced in a step

**Answer**: A

---

**Question 36** — Runner Environment Variables

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Runner Context

**Scenario**: Your workflow needs to determine which runner (hosted or self-hosted) executed the current job. You plan to log runner information for debugging.

**Question** (Select all that apply): Which default env variables provide runner information?

- A) `RUNNER_NAME` — hostname or configured name of the runner
- B) `RUNNER_OS` — operating system (Linux, Windows, macOS)
- C) `RUNNER_ARCH` — processor architecture (x64, ARM64, etc.)
- D) `RUNNER_TEMP` — temporary directory path for the job

**Answer**: A, B, C, D

---

**Question 37** — GITHUB_WORKSPACE Path

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Default Paths and Directory Structure

**Question**: Your step runs a build script at: `run: ./build.sh` (relative path). Which default env variable contains the absolute path to the repository root?

- A) `GITHUB_WORKSPACE`
- B) `GITHUB_REPOSITORY_PATH`
- C) `RUNNER_WORKSPACE`
- D) `GITHUB_HOME`

**Answer**: A

---

**Question 38** — CI Flag and Debugging Variables

**Difficulty**: Medium | **Answer Type**: many | **Topic**: CI Detection and Debug Logging

**Scenario**: You're writing a script that needs to detect if it's running in GitHub Actions and provide verbose logging for debugging.

**Question** (Select all that apply): Which default env variables help with this?

- A) `CI=true` — set by GitHub to indicate a CI environment
- B) `GITHUB_ACTIONS=true` — set by GitHub to indicate GitHub Actions specifically
- C) `RUNNER_DEBUG=true` — enables additional diagnostic logs when explicitly set
- D) `DEBUG_MODE` — automatically set by GitHub in all workflows

**Answer**: A, B, C

---

**Question 39** — API Token Availability and Scope

**Difficulty**: Hard | **Answer Type**: one | **Topic**: GITHUB_TOKEN Scope and Limitations

**Scenario**: Your workflow step tries to create a PR in a different repository using `GITHUB_TOKEN` and the GitHub API: `curl -H "Authorization: token $GITHUB_TOKEN" https://api.github.com/repos/other-org/other-repo/...`

**Question**: What is the most likely outcome?

- A) The API call succeeds because GITHUB_TOKEN has org-level access
- B) The API call fails; GITHUB_TOKEN is scoped to the current repository only
- C) The API call succeeds, but the new PR is marked as "suspicious"
- D) GitHub prompts for additional authentication before proceeding

**Answer**: B

---

## Topic 8: Environment Protection Rules (5 Questions)

**Question 40** — Required Reviewers for Deployments

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Deployment Protection and Review

**Scenario**: Your `prod` environment has a protection rule requiring 2 required reviewers before any deployment. A developer pushes a workflow that deploys to `prod` without specifying reviewers.

**Question**: What happens?

- A) The workflow fails immediately without running the deployment job
- B) The deployment job waits for the required reviewers to approve in the GitHub UI
- C) The workflow proceeds without waiting; reviewers receive a notification afterward
- D) The protection rule is bypassed for automated workflows

**Answer**: B

---

**Question 41** — Deployment Branches Protection

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Branch-Based Deployment Rules

**Scenario**: You configure an environment protection rule: "Deployment branches: Only `main` and `release/*` branches can deploy to prod."

**Question** (Select all that apply): Which scenarios respect this protection?

- A) A workflow on `feature/new-feature` attempts to deploy to prod; it is blocked
- B) A workflow on `release/v1.2.0` attempts to deploy to prod; it is allowed
- C) A manual workflow_dispatch on `main` triggers a prod deployment; it is allowed
- D) A pull request rebase on `main` triggers a prod deployment; it is allowed

**Answer**: A, B, C, D

---

**Question 42** — Wait Timer Before Deployment

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Deployment Delay Rules

**Scenario**: Your prod environment has a 24-hour wait timer protection rule. A workflow triggers a deployment at Tuesday 2 PM. No other conditions are blocking it.

**Question**: When can the deployment execute?

- A) Immediately (wait timer only applies to manual deployments)
- B) After 24 hours (Wednesday 2 PM)
- C) At the next scheduled maintenance window
- D) After one review approval (wait timer and approval are sequential)

**Answer**: B

---

**Question 43** — Custom Deployment Protection Rules

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Environment Rules Configuration

**Scenario**: Your company requires that deployments to production can only occur between 9 AM and 5 PM PT. You attempt to configure this via environment protection rules in GitHub.

**Question**: What is the correct approach?

- A) Use GitHub's built-in time-window protection rule (if available)
- B) GitHub does not offer time-based protection rules; use a custom third-party app or workflow condition
- C) Set a wait timer for off-hours and manually remove it during business hours
- D) Configure a required review that checks the time before approving

**Answer**: B

---

**Question 44** — Environment Variables Specific to Deployment

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Environment Configuration

**Scenario**: You configure an environment called `staging` with environment-specific variables (e.g., `DEPLOY_URL`, `API_ENDPOINT`). A job references this environment.

**Question** (Select all that apply): Which are true about environment variables?

- A) Environment variables are available in all jobs, regardless of which environment is specified
- B) When a job specifies `environment: staging`, those environment variables are available
- C) Environment variables can override workflow and job-level variables
- D) Secrets scoped to an environment are available to jobs targeting that environment

**Answer**: B, C, D

---

## Topic 9: Workflow Artifacts (5 Questions)

**Question 45** — Artifact Upload and Retention

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Artifact Storage and Retention

**Scenario**: Your build job uploads a 100 MB artifact with `retention-days: 7`. After 7 days, the workflow is still active. What happens to the artifact?

- A) The artifact is automatically deleted after 7 days
- B) The artifact persists indefinitely (retention-days is advisory only)
- C) GitHub sends a warning but keeps the artifact until manually deleted
- D) The artifact is archived to cold storage but remains accessible

**Answer**: A

---

**Question 46** — Artifacts Between Jobs

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Inter-Job Artifact Sharing

**Scenario**: Job `build` uploads an artifact named `app-binary`. Job `test` (which depends on `build`) needs to download and test this artifact.

**Question** (Select all that apply): Which statements are correct?

- A) Job `test` uses `actions/download-artifact@v3` to retrieve the `app-binary` artifact
- B) Job `test` must specify `needs: [build]` and then can download the artifact
- C) Artifacts are automatically available to dependent jobs without explicit download
- D) The artifact is scoped to the workflow run; other workflows cannot access it

**Answer**: A, B, D

---

**Question 47** — Artifact Paths and Glob Patterns

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Artifact Path Specification

**Scenario**: Your build step creates: `dist/app.js`, `dist/styles.css`, `dist/sourcemaps/app.js.map`, and `README.md`. You want to upload only JS and CSS files from `dist/`, excluding sourcemaps.

**Question**: Which path glob is correct?

- A) `path: dist/**/*.{js,css}`
- B) `path: |` followed by `dist/**/*.js` and `dist/**/*.css` (multi-line)
- C) Both A and B work
- D) `path: dist/!(*.map)`

**Answer**: C

---

**Question 48** — Artifact Name Collisions

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Artifact Naming and Uniqueness

**Scenario**: Your matrix job (3 combinations) each uploads an artifact named `test-results`. They all complete within the workflow run.

**Question**: What happens?

- A) The last job's artifact overwrites the previous ones; only one `test-results` artifact exists
- B) All three artifacts are stored separately; GitHub appends a numeric suffix (e.g., `test-results-1`, `test-results-2`)
- C) GitHub automatically renames them to include matrix identifiers (e.g., `test-results-[node:16,os:ubuntu]`)
- D) The workflow fails because artifact names must be unique within a run

**Answer**: C

---

**Question 49** — Artifact Permissions and Access

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Artifact Access Controls

**Scenario**: Your repository is private. You upload an artifact. A collaborator from a different organization tries to access it.

**Question** (Select all that apply): Which statements are true?

- A) Artifacts inherit repository permissions; only collaborators with repo access can download
- B) Artifacts are always accessible within the workflow run regardless of repository permissions
- C) You can share artifacts with public URLs if explicitly configured in environment variables
- D) Artifacts expire after the retention period regardless of access level

**Answer**: A, B, D

---

## Topic 10: Workflow Caching (5 Questions)

**Question 50** — Cache Key Strategy

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Cache Key Generation

**Scenario**: Your workflow caches npm dependencies: `cache-key: npm-cache-${{ runner.os }}-${{ hashFiles('package-lock.json') }}`. A developer updates `package-lock.json`. The next workflow run should detect the change and re-cache.

**Question**: Does this cache key strategy work correctly?

- A) Yes; the hash changes when `package-lock.json` changes, invalidating the cache
- B) No; the key is static and won't change even if dependencies change
- C) Yes; but only on the first run after the change
- D) No; `hashFiles` doesn't support package manager files

**Answer**: A

---

**Question 51** — Restore Keys and Path Fallback

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Cache Restoration Strategy

**Scenario**: Your cache action uses: `cache-key: npm-${{ runner.os }}-${{ hashFiles(...) }}` and `restore-keys: npm-${{ runner.os }}-`. A job runs on `ubuntu-latest`, but no exact cache match exists for the current npm hash.

**Question** (Select all that apply): Which outcomes are possible?

- A) GitHub restores the most recent cache matching `npm-ubuntu-latest-*`
- B) The job fails because the exact cache key doesn't match
- C) GitHub restores a cache from a different OS if no Ubuntu match exists
- D) The job proceeds without cache; the restore-keys provides a fallback mechanism

**Answer**: A, D

---

**Question 52** — Multiple Paths in Cache Action

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Multi-Path Caching

**Scenario**: Your workflow caches npm dependencies (`node_modules/`) and pip packages (`venv/`) simultaneously.

**Question**: What is the correct syntax?

- A) `path: [node_modules/, venv/]`
- B) `path: | node_modules/ venv/` (multi-line)
- C) Both A and B
- D) Multiple paths require separate cache actions

**Answer**: C

---

**Question 53** — Cache Hit Output Variable

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Cache Hit Detection

**Scenario**: You want to skip the `npm install` step if a cache hit occurs. Your cache action has `id: npm-cache`.

**Question**: How do you check for a cache hit?

- A) `if: ${{ steps.npm-cache.outputs.cache-hit }} == 'true'`
- B) `if: ${{ steps.npm-cache.outputs.cache_hit == 'true' }}`
- C) Both A and B (syntax variations)
- D) Use `if: ! failure()` after the cache step

**Answer**: B

---

**Question 54** — Cache Size Limits and Performance

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Cache Limits and Eviction

**Scenario**: Your workflow caches are consistently hitting the repository cache limit. You have 20 different caches, each ~800 MB. GitHub starts evicting older caches.

**Question**: What is the best approach to manage cache space?

- A) Request a higher cache limit from GitHub Support
- B) Implement a more granular cache key strategy to reduce redundant caches
- C) Split caches by job/workflow to keep only necessary ones
- D) All of the above; start with B/C, escalate to A if needed

**Answer**: D

---

## Topic 11: Workflow Sharing (5 Questions)

**Question 55** — Reusable Workflow Basics

**Difficulty**: Easy | **Answer Type**: one | **Topic**: Reusable Workflow Definition

**Question**: What trigger event is required for a reusable workflow file?

- A) `on: workflow_call`
- B) `on: workflow_reuse`
- C) `on: [push, pull_request]` (standard triggers)
- D) Reusable workflows don't require a trigger event

**Answer**: A

---

**Question 56** — Calling Reusable Workflows

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Reusable Workflow Invocation

**Scenario**: Your organization has a reusable CI workflow at `/shared-workflows/.github/workflows/ci.yml`. Your repository is in a different organization. You want to call it.

**Question** (Select all that apply): Which approaches work?

- A) Use `jobs: build: uses: org/shared-workflows/.github/workflows/ci.yml@main`
- B) Use `jobs: build: uses: org/shared-workflows/.github/workflows/ci.yml@v1` (if tagged)
- C) Use `uses: ./.github/workflows/ci.yml@main` (cross-repo reference)
- D) The called workflow repository must be public or you must have explicit access

**Answer**: A, B, D

---

**Question 57** — Reusable Workflow Inputs and Outputs

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Workflow Input/Output Contracts

**Scenario**: Your reusable workflow defines: `inputs: { environment: { type: string, required: true } }` and `outputs: { status: { value: ${{ jobs.build.outputs.status }} } }`.

**Question** (Select all that apply): Which statements are correct?

- A) The caller must provide `environment` input or the workflow fails
- B) The caller can access the status output via `${{ jobs.call-id.outputs.status }}`
- C) Reusable workflows support only string inputs; no other types
- D) Outputs must be defined in the reusable workflow; the caller can reference them

**Answer**: A, B, D

---

**Question 58** — Secrets Inheritance in Reusable Workflows

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Secret Propagation

**Scenario**: A reusable workflow needs to use a secret (e.g., `DEPLOY_KEY`). The caller workflow has the secret defined in the repository. How does the secret reach the reusable workflow?

**Question**: What is the recommended approach?

- A) Secrets are automatically inherited by reusable workflows; no action needed
- B) Use `with: secrets: inherit` to explicitly pass all repository secrets
- C) The caller must explicitly pass each secret as an input
- D) Reusable workflows cannot access repository secrets; use OIDC instead

**Answer**: B

---

**Question 59** — Marketplace Action Publishing

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Action Marketplace and Distribution

**Scenario**: You've built a custom action and want to publish it to the GitHub Marketplace for public use.

**Question** (Select all that apply): Which requirements must be met?

- A) Add a `action.yml` (or `action.yaml`) file at the repository root
- B) Repository must be public
- C) Add a descriptive `README.md` with usage examples
- D) Tag the repository with a Marketplace Publisher account

**Answer**: A, B, C

---

## Topic 12: Workflow Debugging (5 Questions)

**Question 60** — Enabling Runner Debug Mode

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Runner Debug Logging

**Scenario**: A workflow is failing unexpectedly, and you need detailed diagnostic logs. You want to enable debug logging for a specific workflow run.

**Question**: Which approach enables runner debug mode?

- A) Set `RUNNER_DEBUG=1` as a repository secret, then re-run the workflow
- B) Set `RUNNER_DEBUG=true` as an environment variable in the organization settings
- C) Enable debug mode in individual steps via `run: set -x`
- D) Both A and C are valid; B is not supported

**Answer**: A

---

**Question 61** — Workflow Logging Commands

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Workflow Output and Grouping

**Scenario**: You want to provide structured diagnostic output: groups of log messages that users can expand/collapse, warnings, errors, and debug lines.

**Question** (Select all that apply): Which workflow commands are available?

- A) `echo "::group::Group Name"` and `echo "::endgroup::"`
- B) `echo "::warning::Warning message"`
- C) `echo "::error::Error message"`
- D) `echo "::debug::Debug message"`

**Answer**: A, B, C, D

---

**Question 62** — Accessing Workflow Logs

**Difficulty**: Easy | **Answer Type**: one | **Topic**: Log Retrieval and Access

**Question**: Where can you view the full logs for a completed workflow run?

- A) Actions tab → select the workflow run → click "View logs"
- B) Repository API endpoint `/repos/{owner}/{repo}/actions/runs/{run_id}/logs`
- C) Both A and B
- D) Logs are only available locally on the runner

**Answer**: C

---

**Question 63** — Debugging with Intermediate Artifacts

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Debugging via Artifact Upload

**Scenario**: A build step fails with a cryptic error. You want to preserve intermediate build artifacts (e.g., log files, object files) to post-mortem analyze the failure.

**Question**: Which approach captures these debug artifacts?

- A) Add `if: always()` to an artifact upload step to run even on failure
- B) Use `if: failure()` to upload artifacts only when the job fails
- C) Both A and B are valid depending on your use case
- D) Artifacts cannot be uploaded from failed jobs; use debug mode instead

**Answer**: C

---

**Question 64** — Performance Profiling a Slow Step

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Timing and Duration Analysis

**Scenario**: Your build workflow has become slow (25 minutes). You suspect the npm install step is the culprit but aren't certain. You want to measure step durations.

**Question**: What is the best approach?

- A) Use `time` command in bash: `run: time npm install`
- B) Enable RUNNER_DEBUG and look for timing information in logs
- C) GitHub Actions automatically reports step duration in the UI; check the logs
- D) All of the above; C is primary, A/B are supplementary

**Answer**: D

---

## Topic 13: Workflows REST API (5 Questions)

**Question 65** — Listing Workflows

**Difficulty**: Medium | **Answer Type**: one | **Topic**: API Basics

**Scenario**: You want to programmatically list all workflows in your repository using the GitHub REST API.

**Question**: What is the correct endpoint?

- A) `GET /repos/{owner}/{repo}/actions/workflows`
- B) `GET /repos/{owner}/{repo}/workflows`
- C) `GET /repos/{owner}/{repo}/actions`
- D) `GET /{owner}/{repo}/workflows`

**Answer**: A

---

**Question 66** — Triggering a Workflow Run via API

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Remote Trigger via REST API

**Scenario**: You want to trigger a `workflow_dispatch` workflow remotely using the GitHub REST API and pass custom inputs.

**Question** (Select all that apply): Which are correct?

- A) Use `POST /repos/{owner}/{repo}/actions/workflows/{workflow_id}/dispatches`
- B) Include `inputs` object in the request body with input names and values
- C) The request requires authentication (PAT or GITHUB_TOKEN)
- D) workflow_id can be the workflow filename (e.g., `ci.yml`) or the numeric ID

**Answer**: A, B, C, D

---

**Question 67** — Listing Workflow Runs and Filtering

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Run Retrieval and Filtering

**Scenario**: You're building a dashboard and need to retrieve recent workflow runs for a specific workflow, filtering by status.

**Question** (Select all that apply): Which query parameters are supported?

- A) `status`: Filter by completed, in_progress, queued, requested, waiting
- B) `conclusion`: Filter by success, failure, neutral, cancelled, timed_out
- C) `branch`: Filter runs by branch name
- D) `limit`: Limit results to N runs

**Answer**: A, B, C, D

---

**Question 68** — Canceling a Workflow Run

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Run Control

**Scenario**: A long-running workflow started 30 minutes ago with id `12345`. You want to cancel it immediately.

**Question**: What is the correct endpoint?

- A) `DELETE /repos/{owner}/{repo}/actions/runs/12345`
- B) `POST /repos/{owner}/{repo}/actions/runs/12345/cancel`
- C) `PATCH /repos/{owner}/{repo}/actions/runs/12345?status=cancelled`
- D) Both A and B

**Answer**: B

---

**Question 69** — Downloading Artifacts via API

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Artifact Retrieval

**Scenario**: You want to programmatically download a specific artifact from a completed workflow run using the REST API.

**Question**: What is the best approach?

- A) `GET /repos/{owner}/{repo}/actions/artifacts/{artifact_id}/download` → follows redirect to download URL
- B) List artifacts with `GET /repos/{owner}/{repo}/actions/runs/{run_id}/artifacts`, then download each via URL
- C) Artifacts can only be downloaded via the GitHub UI or the `actions/download-artifact` action
- D) Both A and B are valid

**Answer**: D

---

## Topic 14: Reviewing Deployments (4 Questions)

**Question 70** — Environment Configuration

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Deployment Environment Setup

**Scenario**: You want to distinguish between staging and production deployments. Each should have different protection rules and environment variables.

**Question**: Where are environments configured?

- A) Repository Settings → Environments
- B) `.github/environments.yml` in the repository
- C) Organization Settings → Environments
- D) Workflows → Environment Configuration

**Answer**: A

---

**Question 71** — Deployment Status and History

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Deployment Tracking

**Scenario**: You want to see a timeline of all deployments to production, including who triggered them, when, and the status (success/failure).

**Question** (Select all that apply): Where can you find this information?

- A) Repository → Deployments tab
- B) REST API: `GET /repos/{owner}/{repo}/deployments`
- C) GitHub Actions run logs
- D) Environment protection rules history

**Answer**: A, B, C

---

**Question 72** — Deployment Review Workflow

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Review Process

**Scenario**: Your prod environment has "Required Reviewers" protection rule (2 reviewers). A deployment workflow triggers and waits for approval. A reviewer approves, but does not reject. A second approval comes from a different reviewer.

**Question**: What is the deployment status?

- A) Approved and continues immediately upon the 2nd approval
- B) Still pending (a third approval is needed for consensus)
- C) Sent back to the first reviewer for confirmation
- D) Auto-rejected if more than 1 hour passes

**Answer**: A

---

**Question 73** — Environment Secrets and Variables in Deployment

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Environment-Scoped Configuration

**Scenario**: You define: Prod environment variables: `API_ENDPOINT=https://api.prod.com`. Prod environment secrets: `DEPLOY_KEY`. A deployment job references `${{ secrets.DEPLOY_KEY }}` and `${{ vars.API_ENDPOINT }}`.

**Question**: Are these available to the job?

- A) Yes, if the job specifies `environment: prod`
- B) Yes, only secrets are environment-scoped; variables are global
- C) No, environment secrets/variables are only used for protection rules
- D) Only if the job has explicit `with:` parameters

**Answer**: A

---

## Topic 15: Creating and Publishing Actions (5 Questions)

**Question 74** — Action Metadata File

**Difficulty**: Medium | **Answer Type**: one | **Topic**: action.yml File Structure

**Question**: What is the required structure of an action's metadata file?

- A) Filename must be `action.yml` or `action.yaml` at the repository root
- B) Must include `name`, `description`, and either `runs` (composite/Docker) or `main` (JavaScript)
- C) Must include `inputs` and `outputs` sections
- D) All of the above except C (inputs/outputs are optional)

**Answer**: A

---

**Question 75** — JavaScript Action with Dependencies

**Difficulty**: Medium | **Answer Type**: many | **Topic**: JavaScript Action Development

**Scenario**: You're building a JavaScript action. Your code uses external npm packages (e.g., axios). You want to distribute it efficiently.

**Question** (Select all that apply): Which approaches are recommended?

- A) Ship `node_modules/` in the repository for instant availability
- B) Use a tool like `ncc` (vercel/ncc) or `esbuild` to bundle dependencies into a single file
- C) Rely on npm install during action execution (slower but smaller repo)
- D) List dependencies in the action.yml `requirements` section

**Answer**: A, B

---

**Question 76** — Composite Action Structure

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Composite Action Implementation

**Scenario**: You're building a reusable action that checks out code, installs dependencies, then runs tests. You decide to use a composite action approach.

**Question**: In the action.yml, what does the `runs` section look like?

- A) `runs: { using: nodejs16 }`
- B) `runs: { using: composite, steps: [...] }`
- C) `runs: { using: docker, image: node:16 }`
- D) No `runs` section needed for composite actions

**Answer**: B

---

**Question 77** — Action Versioning and Tags

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Version Management

**Scenario**: You publish an action to the Marketplace with tag `v1.0.0`. Five months later, you release `v1.1.0` (non-breaking change). Users currently reference `v1` in their workflows. Should they get the `v1.1.0` automatically?

**Question**: What is the best practice?

- A) Tag the new release as both `v1.1.0` and `v1` (move the `v1` tag) so existing references receive the update
- B) Always require users to update to the explicit version; don't move version tags
- C) Use `v1.1.0` for patch, `v2` for major releases
- D) Marketplace automatically routes `v1` to the latest `v1.x.x` release

**Answer**: A

---

**Question 78** — Testing Custom Actions Locally

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Action Development and Testing

**Scenario**: You've built a custom action and want to test it locally before publishing to the Marketplace.

**Question**: What is the best approach?

- A) Use `act` (nektos/act) to run GitHub Actions workflows locally, including your custom action
- B) Reference your action from a test workflow using relative path: `uses: ./`(from same repo)
- C) Push to a test branch and reference via `owner/repo/.github/workflows/action@branch-name`
- D) All of the above are valid depending on the testing stage

**Answer**: D

---

## Topic 16: Managing Runners (5 Questions)

**Question 79** — Hosted Runner Selection

**Difficulty**: Easy | **Answer Type**: one | **Topic**: Choosing Hosted Runners

**Question**: Your project requires Python 3.10, which you need to test on Linux. Which hosted runner should you use?

- A) `ubuntu-latest`
- B) `ubuntu-22.04`
- C) Both A and B support Python 3.10
- D) Neither; recommend GitHub Enterprise Runners

**Answer**: C

---

**Question 80** — Self-Hosted Runner Labels

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Runner Organization and Targeting

**Scenario**: You've set up 5 self-hosted runners in your organization: 3 on Linux, 2 on Windows. You want workflows to dispatch to specific runners based on custom criteria.

**Question** (Select all that apply): How can you target runners?

- A) Assign custom labels (e.g., `performance-testing`, `macos-xcode-13`)
- B) Use `runs-on: self-hosted, "performance-testing"` (array syntax)
- C) Use `runs-on: ["self-hosted", "linux", "performance-testing"]`
- D) All groups of self-hosted runners automatically get OS labels (linux, windows, macos)

**Answer**: A, B, C, D

---

**Question 81** — Runner Groups in Enterprise

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Enterprise Runner Organization

**Scenario**: Your enterprise manages 100 self-hosted runners across 10 organizations. You want to restrict certain runners to specific organizations for compliance.

**Question** (Select all that apply): Which approaches use runner groups?

- A) An enterprise admin creates a runner group and assigns runners to it
- B) Organizations within the enterprise can assign policies to runner groups
- C) Runner groups support IP allowlists for runners in restricted networks
- D) Runner groups inherit access policies from organization settings

**Answer**: A, B, C

---

**Question 82** — Self-Hosted Runner Scalability

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Runner Capacity and Autoscaling

**Scenario**: Your organization runs hundreds of workflows daily on 20 self-hosted runners. During peak hours, workflows queue up. You're considering autoscaling the runner fleet.

**Question**: What is the best approach?

- A) GitHub Actions provides built-in autoscaling via `scale_policy`
- B) Integrate a third-party autoscaler (e.g., philips-labs/terraform-provider-githubrunner)
- C) Use GitHub's Hosted Runners for peak workloads; reserve self-hosted for standby
- D) Both B and C are practical; A is not available

**Answer**: D

---

**Question 83** — Runner Maintenance and Updates

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Self-Hosted Runner Hygiene

**Scenario**: Your team manages 15 Linux self-hosted runners. Security patches are released weekly. Your organizational policy requires patches to be applied within 72 hours.

**Question** (Select all that apply): Which practices support this?

- A) Disable workflows on a runner during maintenance windows
- B) Use `grace-period` configuration to allow in-flight jobs to complete before shutdown
- C) Automate patching via cron or configuration management tools
- D) Remove runners from rotation (remove labels) during updates

**Answer**: B, C, D

---

## Topic 17: GitHub Actions Enterprise (5 Questions)

**Question 84** — Organizational Action Policies

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Action Allowlisting

**Scenario**: Your enterprise requires that all workflows use only verified, trusted actions. You configure an organizational policy: "Allow local actions only" (actions in the same organization/enterprise).

**Question**: What is the result?

- A) Workflows can use actions from any repository in your organization or enterprise
- B) Workflows can only use actions in the current repository
- C) Workflows can use public actions from the GitHub Marketplace
- D) The policy blocks all third-party actions unless explicitly added to an allowlist

**Answer**: A

---

**Question 85** — Required Workflows at Enterprise Level

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Enforced Workflows

**Scenario**: Your enterprise mandates that all repositories run a security scanning workflow (SAST). An enterprise admin creates a required workflow pointing to `/shared-workflows/.github/workflows/security-scan.yml@main`.

**Question** (Select all that apply): Which statements are true?

- A) Required workflows run automatically on all matching repositories even if not explicitly referenced
- B) Repository admins can disable required workflows in their repository settings
- C) Required workflows appear in PR checks and run status alongside standard workflows
- D) Required workflows bypass the normal trigger events; they always run on push/PR

**Answer**: A, C

---

**Question 86** — IP Allowlists for Self-Hosted Runners

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Network Isolation

**Scenario**: Your enterprise requires that workflows can only communicate with internal services via whitelisted IPs. You configure an IP allowlist at the organization level: `10.0.1.0/24`.

**Question**: What does this ensure?

- A) Only runners with IPs in `10.0.1.0/24` can execute workflows
- B) Workflows can only make API/HTTP requests to services within `10.0.1.0/24`
- C) Runners with IPs outside the allowlist cannot be registered or used
- D) Both A and C; runners must have allowlisted IPs to participate

**Answer**: D

---

**Question 87** — Secrets Hierarchy in Enterprise

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Scope and Precedence

**Scenario**: You define secrets at three levels: Enterprise (`SHARED_KEY=enterprise_value`), Organization (`SHARED_KEY=org_value`), Repository (`SHARED_KEY=repo_value`). A workflow references `${{ secrets.SHARED_KEY }}`.

**Question** (Select all that apply): Which statements are true?

- A) The repository secret takes precedence; the workflow uses `repo_value`
- B) All three levels are merged; the workflow can access all versions via different namespa
ces
- C) Organization secret overrides enterprise secret; repository secret overrides organization
- D) The most specific scope (repository) takes precedence over broader scopes

**Answer**: A, D

---

**Question 88** — Audit Logging for Compliance

**Difficulty**: Hard | **Answer Type**: many | **Topic**: Enterprise Auditing

**Scenario**: Your enterprise must maintain audit logs for regulatory compliance. You want to verify that all workflow runs are logged, including cancellations and manual re-runs.

**Question** (Select all that apply): Which audit logging capabilities exist?

- A) Audit logs include workflow trigger events (push, PR, manual, schedule)
- B) Audit logs track who approves deployments in protected environments
- C) Audit logs record modifications to actions/secrets policies
- D) Audit logs can be exported via API or streamed to Splunk/Datadog

**Answer**: A, B, C, D

---

## Topic 18: Security and Optimization (7 Questions)

**Question 89** — GITHUB_TOKEN Permissions and Best Practices

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Token Scope Management (SECURITY)

**Scenario**: Your workflow only needs to read repository files and create pull request comments. You're configuring `permissions`.

**Question** (Select all that apply): Which configurations are appropriate?

- A) `permissions: read-all` (gives read access to everything)
- B) `permissions: { contents: read, pull-requests: write }`
- C) Don't set permissions; use the organization default
- D) `permissions: { contents: read }` (minimum required)

**Answer**: B, D

---

**Question 90** — Script Injection Prevention

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Command Injection (SECURITY)

**Scenario**: Your workflow accepts user input from workflow_dispatch (`${{ github.event.inputs.user_comment }}`). You need to log this safely without enabling script injection.

**Question**: Which approach is secure?

- A) `run: echo ${{ github.event.inputs.user_comment }}`
- B) `run: echo "${{ github.event.inputs.user_comment }}"` (double quotes)
- C) `env: { COMMENT: ${{ github.event.inputs.user_comment }} }` then `run: echo "$COMMENT"`
- D) `run: echo "${{ toJSON(github.event.inputs.user_comment) }}" | jq -r`

**Answer**: C

---

**Question 91** — SHA Pinning for Actions

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Supply Chain Security (SECURITY)

**Scenario**: You're upgrading to a new version of `actions/checkout`. You want to follow supply chain security best practices.

**Question** (Select all that apply): Which approaches are recommended?

- A) Pin to semver tag: `actions/checkout@v3`
- B) Pin to full SHA: `actions/checkout@e2f00e4a51d3d90b6ac33244b6fb19c3fff11b85` (40 chars)
- C) Pin to short SHA: `actions/checkout@e2f00e4a5` (7 chars)
- D) Use GitHub's verified creator badge to validate actions before use

**Answer**: B, D

---

**Question 92** — OIDC Token for Cloud Federation

**Difficulty**: Hard | **Answer Type**: many | **Topic**: Cloud Credential Exchange (SECURITY)

**Scenario**: You want to deploy to AWS without storing long-lived credentials. You configure OIDC federation. Your workflow needs an AWS role ARN and audience.

**Question** (Select all that apply): Which are correct?

- A) The workflow requests an OIDC JWT via `id-token: write` permission
- B) Azure/GCP/AWS trust your GitHub organization via OIDC discovery endpoint
- C) The OIDC token subject claims include repository, branch, and actor information
- D) OIDC tokens expire in seconds; they cannot be cached long-term

**Answer**: A, B, C, D

---

**Question 93** — Secret Masking Limitations

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Secret Redaction (SECURITY)

**Scenario**: Your application logs accidentally include a 20-character API secret. You assume GitHub will mask it automatically. However, you notice it appears unmasked in the logs.

**Question**: Why might GitHub fail to mask the secret?

- A) Secrets shorter than 3 characters are not masked
- B) GitHub masks exact string matches; if the secret is even slightly transformed (URL-encoded, hashed), masking may fail
- C) Secrets are only masked if they're referenced via `${{ secrets.* }}`; if passed via env/context, no masking occurs
- D) All of the above are factors

**Answer**: D

---

**Question 94** — Action Attestation and Supply Chain Risk

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Trustworthy Actions (SECURITY)

**Scenario**: You review a third-party action on the Marketplace. It has 50K stars but isn't from a GitHub-verified creator. The last update was 18 months ago.

**Question**: What is the appropriate risk assessment?

- A) High stars guarantee trustworthiness; use it
- B) Lack of verification badge + stale updates = elevated risk; consider alternatives
- C) All popular actions are safe; GitHub Marketplace vets all submissions
- D) Risk is negligible if the action has a clear README and usage examples

**Answer**: B

---

**Question 95** — Optimization: Job Parallelization

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Performance

**Scenario**: Your workflow has 4 sequential jobs: checkout → build → test → deploy. Each takes ~5 minutes. Total runtime is 20 minutes. You want to minimize runtime.

**Question**: What is the best optimization?

- A) Run build and test in parallel (both depend only on checkout)
- B) Run all jobs in parallel with no dependencies
- C) Run on a faster runner
- D) Implement caching to speed up each step

**Answer**: A

---

## Topic 19: Common Failures and Troubleshooting (5 Questions)

**Question 96** — Artifact Download Timeout

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Troubleshooting

**Scenario**: Your test job tries to download a 500 MB artifact from a build job. The download fails intermittently with timeout errors.

**Question**: What is a likely cause and solution?

- A) GitHub's artifact storage is temporarily unavailable (external issue)
- B) Reduce artifact size or split into smaller chunks and increase download timeout in the action input
- C) Use a faster runner (download speed depends on runner location)
- D) Switch to direct file paths instead of artifacts

**Answer**: B

---

**Question 97** — Workflow Not Triggering on Push

**Difficulty**: Medium | **Answer Type**: many | **Topic**: Trigger Debugging

**Scenario**: You configured `on: { push: { branches: [main] } }`. You push to `main` locally, but the workflow doesn't trigger.

**Question** (Select all that apply): Which could be the cause?

- A) The branch filter uses glob patterns; `main` must be explicitly matched or use `main*`
- B) GitHub Actions is disabled for the repository
- C) The workflow file has syntax errors; GitHub can't parse it
- D) Push was rejected due to branch protection; GitHub Actions never runs on failed pushes
- E) The `.github/workflows/` directory doesn't exist yet or file isn't committed

**Answer**: B, C, D, E

---

**Question 98** — out of Memory During Build

**Difficulty**: Medium | **Answer Type**: one | **Topic**: Resource Constraints

**Scenario**: Your build job runs out of memory and crashes. You're using `ubuntu-latest`. The build is a large monorepo (100,000 files).

**Question**: What is the best approach?

- A) Implement incremental builds or split the monorepo test into smaller jobs
- B) Switch to a larger hosted runner (if available) or self-hosted runner with more memory
- C) Use caching aggressively to reduce rebuild overhead
- D) All of the above; start with C/A, escalate to B if needed

**Answer**: D

---

**Question 99** — Permission Denied on Secret Access

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Secret Scope Issues

**Scenario**: Your pull request workflow tries to access `${{ secrets.DEPLOY_KEY }}` but the expression evaluates to empty/undefined, causing deployment to fail.

**Question**: Why is the secret not available?

- A) Secrets from forked PRs are intentionally unavailable for security (use `pull_request_target` if needed)
- B) The secret name is misspelled
- C) The environment where the secret is defined doesn't match the job's environment
- D) Any of the above

**Answer**: D

---

**Question 100** — Matrix Job Failure Cascading to Dependent Jobs

**Difficulty**: Hard | **Answer Type**: one | **Topic**: Matrix Strategy and Dependencies (SCENARIO)

**Scenario**: Your workflow has: Job `test` uses matrix (3 combinations). Job `deploy` depends on `test` via `needs: [test]`. One matrix combination in `test` fails; the other two succeed.

**Question**: What happens to the `deploy` job?

- A) `deploy` runs after all matrix combinations complete (success and failure combined)
- B) `deploy` is skipped because at least one matrix job failed
- C) `deploy` runs only if all matrix combinations succeed (use `if: always()` to override)
- D) `deploy` waits indefinitely for the failed job to retry

**Answer**: C

---

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | B | The extension provides real-time YAML schema validation and IntelliSense for GitHub contexts (github.*, secrets.*, env.*, etc.). | Topic 1 | Easy |
| 2 | A, C, D | github.actor, github.run_id, and github.event_name are all valid github context variables. github.database does not exist. | Topic 1 | Medium |
| 3 | A | The YAML extension is often a bundled dependency of the GitHub Actions extension. If not installed, validation may fail even if the GitHub Actions extension is present. | Topic 1 | Medium |
| 4 | B | SHA pinning prevents tag manipulation attacks where semver tags could be retagged or deleted, introducing supply chain risk. | Topic 1 | Hard |
| 5 | B | github.actor contains the login username of the user who triggered the workflow. | Topic 2 | Easy |
| 6 | A, B, D | github.ref_name, github.run_number, and github.run_id are all available for tagging artifacts. runner.name is about the runner, not the run/branch. | Topic 2 | Medium |
| 7 | A | github.event.pull_request.base.ref contains the base (target) branch of a PR. github.base_ref is a shorthand alias. | Topic 2 | Medium |
| 8 | B | GitHub automatically redacts secrets from logs if they are 3+ characters long. Output statements echo the masking is automatic. | Topic 2 | Medium |
| 9 | A | At the workflow level, env context takes precedence. Workflow-level env.API_KEY is used, not secrets.API_KEY. | Topic 2 | Hard |
| 10 | B | The matrix creates 6 jobs (3 node versions × 2 OS values). Each job receives one specific combination value as matrix.node-version. | Topic 2 | Hard |
| 11 | B | Context is evaluated at parse time in workflow-level env, so ${{ secrets.* }} is not evaluated; the literal string is used. | Topic 3 | Medium |
| 12 | A, B, C, D | All statements are correct about needs context availability, scope, and usage. | Topic 3 | Medium |
| 13 | B | GitHub masks exact string matches. If the secret is transformed (encoded, formatted, etc.), masking may fail for that variant. | Topic 3 | Hard |
| 14 | B | Each matrix combination job calls the reusable workflow independently, passing its specific matrix value as input. | Topic 3 | Hard |
| 15 | A | Composite actions run in the context of the caller workflow, so github context is available. | Topic 3 | Medium |
| 16 | A | Workflow files must be in `.github/workflows/` with `.yml` or `.yaml` extension. | Topic 4 | Easy |
| 17 | A, C, D | Explicit dependencies (A), combining dependencies (A+D), recommended for clarity. Transitive dependencies (B) are inferred but not always clear. | Topic 4 | Medium |
| 18 | A | Permission config syntax A is correct using `{ key: value }`. Option B is also equivalent. D is correct. | Topic 4 | Medium |
| 19 | B, C, D | Container and services are compatible; services run as sibling containers with service names as hostnames; both can have separate env vars. | Topic 4 | Medium |
| 20 | D | YAML anchors are resolved at parse time. Each job gets the defined values at its declaration, not shared references. | Topic 4 | Hard |
| 21 | A | if: always() ensures the step runs regardless of previous step outcomes. | Topic 4 | Hard |
| 22 | A | Use exclude array to remove specific matrix combinations. | Topic 4 | Hard |
| 23 | B | Omit paths-ignore when using paths; only paths is sufficient. | Topic 5 | Medium |
| 24 | A, B, C | pull_request (read-only, no secrets), pull_request_target (write token + secrets, requires code review), both are accurate. Not D (not both). | Topic 5 | Hard |
| 25 | A | Scheduled workflows don't run on inactive repositories (no commits in 60 days). | Topic 5 | Medium |
| 26 | A, B | Use workflow_dispatch with choice input type; access via github.event.inputs. | Topic 5 | Medium |
| 27 | B | Event config B is correct syntax for multiple events with different filters. | Topic 5 | Hard |
| 28 | A | workflow_run event with workflows and types filters allows triggering after another workflow completes. | Topic 5 | Hard |
| 29 | B | Use a job condition to filter based on commit message: if: contains(github.event.head_commit.message, '[DEPLOY]'). | Topic 5 | Medium |
| 30 | B | Workflow-level env is available to all jobs and steps. | Topic 6 | Easy |
| 31 | B | Job-level env overrides workflow-level env. | Topic 6 | Medium |
| 32 | A, B | Both use GITHUB_OUTPUT to set and retrieve step outputs. Steps must have an id for output retrieval. | Topic 6 | Medium |
| 33 | B | Secrets in env vars are automatically masked by GitHub in logs. | Topic 6 | Medium |
| 34 | B | Contexts can be used in workflow-level env; github.sha evaluates to the full commit SHA (40 characters). | Topic 6 | Hard |
| 35 | A | GITHUB_TOKEN is created at the start of each job. | Topic 7 | Easy |
| 36 | A, B, C, D | All are valid default runner env variables. | Topic 7 | Medium |
| 37 | A | GITHUB_WORKSPACE contains the absolute path to the repository root. | Topic 7 | Medium |
| 38 | A, B, C | CI=true, GITHUB_ACTIONS=true, and RUNNER_DEBUG=true are all valid for detection/debugging. DEBUG_MODE is not automatic. | Topic 7 | Medium |
| 39 | B | GITHUB_TOKEN is scoped to the current repository only; cross-repo access is denied. | Topic 7 | Hard |
| 40 | B | Required reviewers protection rule causes the deployment job to wait in the UI for manual approval. | Topic 8 | Medium |
| 41 | A, B, C, D | Deployment branch protection is enforced on all scenarios listed. | Topic 8 | Medium |
| 42 | B | After 24 hours (from Tuesday 2 PM to Wednesday 2 PM). | Topic 8 | Medium |
| 43 | B | GitHub doesn't offer built-in time-window protection; use custom logic or third-party apps. | Topic 8 | Hard |
| 44 | B, C, D | Environment variables override lower scopes; secrets are environment-scoped; both are available when job specifies the environment. | Topic 8 | Medium |
| 45 | A | Artifacts are automatically deleted after the retention period. | Topic 9 | Medium |
| 46 | A, B, D | All are correct. Artifacts are not automatically available; they must be explicitly downloaded. | Topic 9 | Medium |
| 47 | C | Both glob patterns work in the path specification. | Topic 9 | Medium |
| 48 | C | GitHub automatically renames matrix artifacts with matrix identifiers to avoid collisions. | Topic 9 | Hard |
| 49 | A, B, D | Artifacts inherit repo permissions; they're accessible within the run; they expire after retention. Option C is incorrect. | Topic 9 | Medium |
| 50 | A | hashFiles updates the cache key when the dependencies file changes, correctly invalidating stale cache. | Topic 10 | Medium |
| 51 | A, D | restore-keys provides a fallback; GitHub restores the most recent matching cache; it's not an error. | Topic 10 | Medium |
| 52 | C | Both syntax forms work for multiple paths. | Topic 10 | Medium |
| 53 | B | cache-hit output uses underscore: `cache_hit`. | Topic 10 | Medium |
| 54 | D | Use granular caching, split by job, and escalate to GitHub Support if needed. | Topic 10 | Hard |
| 55 | A | Reusable workflows require `on: workflow_call`. | Topic 11 | Easy |
| 56 | A, B, D | Can reference the workflow by name/version/SHA from accessible org; repository must be public or accessible. | Topic 11 | Medium |
| 57 | A, B, D | Inputs are required if marked so; outputs are referenced via job ID; only string type is supported (not C). | Topic 11 | Medium |
| 58 | B | Use `with: secrets: inherit` to pass all repository secrets to the reusable workflow. | Topic 11 | Hard |
| 59 | A, B, C | action.yml required, repo public, README with examples; tagging not required for initial publish. | Topic 11 | Medium |
| 60 | A | Set RUNNER_DEBUG=1 as a repository secret; re-run the workflow. | Topic 12 | Medium |
| 61 | A, B, C, D | All workflow logging commands are available. | Topic 12 | Medium |
| 62 | C | Both UI and API provide access to logs. | Topic 12 | Easy |
| 63 | C | if: always() captures artifacts on failure; if: failure() uploads only on failure. Both are valid depending on need. | Topic 12 | Medium |
| 64 | D | All approaches provide timing info; C is primary via the UI. | Topic 12 | Hard |
| 65 | A | Correct endpoint: /repos/{owner}/{repo}/actions/workflows. | Topic 13 | Medium |
| 66 | A, B, C, D | All correct statements about triggering workflows via API. | Topic 13 | Medium |
| 67 | A, B, C, D | All query parameters are supported. | Topic 13 | Medium |
| 68 | B | POST /repos/{owner}/{repo}/actions/runs/{run_id}/cancel is the correct endpoint. | Topic 13 | Medium |
| 69 | D | Both list-then-download and direct download endpoints are valid. | Topic 13 | Hard |
| 70 | A | Environments are configured in Repository Settings → Environments. | Topic 14 | Medium |
| 71 | A, B, C | Deployment history is available in UI, API, and workflow logs. | Topic 14 | Medium |
| 72 | A | After the 2nd approval, the deployment is approved and continues immediately. | Topic 14 | Hard |
| 73 | A | Environment-scoped secrets and variables are available if the job specifies environment. | Topic 14 | Medium |
| 74 | A | Metadata file is action.yml/.yaml at repo root with required fields. | Topic 15 | Medium |
| 75 | A, B | Shipping node_modules or using ncc/esbuild for bundling are both valid approaches. | Topic 15 | Medium |
| 76 | B | Composite actions use `runs: { using: composite, steps: [...] }`. | Topic 15 | Medium |
| 77 | A | Moving the v1 tag to the new release allows existing references to receive updates. | Topic 15 | Hard |
| 78 | D | All approaches are valid at different testing stages. | Topic 15 | Medium |
| 79 | C | Both ubuntu-latest and ubuntu-22.04 support Python 3.10. | Topic 16 | Easy |
| 80 | A, B, C, D | All statements about runner labels and targeting are correct. | Topic 16 | Medium |
| 81 | A, B, C | Runner groups support organization-level restrictions and IP allowlists. | Topic 16 | Medium |
| 82 | D | No built-in autoscaling; use third-party tools or hosted runners for capacity management. | Topic 16 | Hard |
| 83 | B, C, D | Disable during maintenance, complete in-flight jobs, automate patching, and manage label assignment. | Topic 16 | Medium |
| 84 | A | "Allow local actions only" permits all actions within the organization/enterprise. | Topic 17 | Medium |
| 85 | A, C | Required workflows run automatically and appear in PR checks. Repository admins cannot disable them. | Topic 17 | Medium |
| 86 | D | IP allowlists restrict runners by their registration IP; runners outside the range cannot be used. | Topic 17 | Hard |
| 87 | A, D | Repository secret takes precedence; most specific scope wins. | Topic 17 | Medium |
| 88 | A, B, C, D | All audit logging capabilities are available for compliance. | Topic 17 | Hard |
| 89 | B, D | Use explicit minimal permissions; avoid read-all. | Topic 18 | Medium |
| 90 | C | Setting a secret as an env variable and referencing it prevents injection. | Topic 18 | Hard |
| 91 | B, D | Pin to full SHA and validate verified creators for supply chain security. | Topic 18 | Medium |
| 92 | A, B, C, D | All statements about OIDC federation are correct. | Topic 18 | Hard |
| 93 | D | All factors can contribute to masking failures. | Topic 18 | Hard |
| 94 | B | Lack of verification, stale updates, and large follower count all warrant careful assessment. | Topic 18 | Hard |
| 95 | A | Parallelize independent jobs (build and test) to minimize runtime. | Topic 18 | Medium |
| 96 | B | Reduce artifact size or split/increase timeout. | Topic 19 | Medium |
| 97 | B, C, D, E | Any of these could prevent trigger. A is incorrect (main matches exactly). | Topic 19 | Medium |
| 98 | D | Implement incremental builds, caching, and consider larger runners. | Topic 19 | Medium |
| 99 | D | All could cause missing secrets. | Topic 19 | Hard |
| 100 | C | If any matrix job fails, dependent jobs are skipped by default; use if: always() to override. | Topic 19 | Hard |

---

## Coverage Summary

✅ **Total Questions**: 100
✅ **Scenario-Based**: 73 (73%)
✅ **Security Focus**: 12 questions
✅ **Enterprise Focus**: 9 questions
✅ **Easy**: 20 questions (20%)
✅ **Medium**: 60 questions (60%)
✅ **Hard**: 20 questions (20%)
✅ **Answer Type Distribution**: one (55), many (26), all (12), none (7)

---

## Instructions for Use

1. **Study Mode**: Review questions and explanations individually
2. **Practice Mode**: Set a timer for 90 minutes (90 questions) to simulate exam conditions
3. **Weak Area Analysis**: Group questions by topic and difficulty; focus on low-success topics
4. **Peer Discussion**: Discuss complex questions (especially Hard / synthesis questions) with colleagues
5. **Continuous Iteration**: Use this bank repeatedly; difficulty increases with familiarity
