# GitHub Actions GH-200 Certification Exam — Question Bank (Iteration 9)

**Iteration**: 9

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
**Topic**: Extension setup and file path requirement

**Question**:
For the GitHub Actions VS Code extension to activate its schema validation and IntelliSense features, workflow files must be located in which directory path?

- A) `workflows/` at the root of the repository
- B) `.github/workflows/` relative to the repository root
- C) `src/.github/actions/` relative to the project source
- D) Any directory ending in `/workflows/`

---

### Question 2 — VS Code Extension

**Difficulty**: Medium
**Answer Type**: one
**Topic**: SHA pinning security warnings

**Scenario**:
A developer references a third-party action using a branch tag like `uses: external-org/deploy-action@main` in their workflow. The VS Code extension immediately highlights this line with a warning.

**Question**:
What is the extension warning about and what is the recommended fix?

- A) The action is from an external organization; switch to a first-party GitHub action
- B) Branch references can change at any time; pin the action to a specific commit SHA for reproducibility and security
- C) The action must be referenced by a semver version tag, not a branch name
- D) The `@main` syntax is not valid YAML and will cause a parse error

---

### Question 3 — VS Code Extension

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Extension troubleshooting — schema not loading

**Scenario**:
A developer installs the GitHub Actions extension but notices that IntelliSense suggestions are not appearing and no syntax errors are highlighted, even for clearly broken workflow syntax.

**Question**:
Which troubleshooting step should be performed first?

- A) Uninstall and reinstall GitHub Actions extension, then restart VS Code
- B) Verify the YAML extension is installed, check that the file is in `.github/workflows/`, and try running "Reload Window"
- C) Manually add the GitHub Actions JSON schema URL to the VS Code `yaml.schemas` setting
- D) Check that the workflow file uses `.yml` extension instead of `.yaml`

---

### Question 4 — VS Code Extension

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Expression evaluation capability

**Scenario**:
A developer writes the expression `${{ github.event_name == 'push' && github.ref == 'refs/heads/main' }}` in a workflow `if:` condition and wants to verify the logic is correct before running the workflow.

**Question**:
Which GitHub Actions VS Code extension feature helps evaluate this expression without running a workflow?

- A) The extension connects to the GitHub API and evaluates the expression against the latest workflow run context
- B) The extension's expression evaluator highlights the expression and shows potential type mismatches or logical errors inline
- C) The extension has a local expression preview panel that simulates context values and shows the boolean result
- D) The extension flags the expression as too complex and suggests splitting it into two separate conditions

---

### Question 5 — VS Code Extension

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Verifying extension installation

**Question**:
Which command can a developer run in a terminal to verify whether the GitHub Actions VS Code extension is currently installed?

- A) `code --show-extensions github`
- B) `code --list-extensions | grep -i github`
- C) `gh extension list --vscode`
- D) `code --verify-extension github.vscode-github-actions`

---

### Question 6 — Contextual Information

**Difficulty**: Easy
**Answer Type**: one
**Topic**: github.token automatic provisioning

**Question**:
What does `github.token` represent in a GitHub Actions workflow, and how is it provisioned?

- A) A long-lived personal access token that must be manually stored as a secret before use
- B) An automatically provisioned short-lived token for the GitHub App installed on the repository, available throughout the workflow
- C) An environment variable that must be explicitly set under `env:` at the workflow level before it can be used
- D) A read-only token that only provides access to public repository metadata

---

### Question 7 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: github.base_ref vs github.head_ref

**Scenario**:
A workflow is triggered by a pull request from the branch `feature/login` targeting the branch `develop`. A developer needs to log both the source and target branch names.

**Question**:
Which expression correctly captures the **target** branch of the pull request?

- A) `${{ github.ref_name }}`
- B) `${{ github.head_ref }}`
- C) `${{ github.base_ref }}`
- D) `${{ github.event.pull_request.target }}`

---

### Question 8 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: secrets context auto-redaction

**Scenario**:
A developer accidentally echoes a secret value in a `run:` step: `echo "Token: ${{ secrets.API_TOKEN }}"`. The workflow runs successfully.

**Question**:
What will appear in the workflow logs for that echo command?

- A) The full secret value is printed because `echo` bypasses content filtering
- B) The step fails with a security violation error before executing the echo
- C) GitHub automatically replaces the secret value with `***` in the log output
- D) The secret value is printed but the log line is hidden from all viewers except repository admins

---

### Question 9 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: job.status context

**Scenario**:
A cleanup job needs to run at the end of a workflow regardless of whether earlier jobs succeeded or failed. The cleanup job also needs to report whether the preceding jobs passed.

**Question**:
Which context property provides the current job's completion status within a subsequent job, and which function ensures the cleanup job always runs?

- A) `github.run_status` combined with the `always()` status check function
- B) `job.status` accessed in the cleanup job combined with `needs.<job-id>.result`; use `if: always()` on the cleanup job
- C) `env.JOB_OUTCOME` set by the upstream job, accessed using the `env` context
- D) `runner.status` combined with `if: success() || failure()`

---

### Question 10 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: github.run_number vs github.run_id

**Question**:
A team wants to tag Docker images with a numeric build counter that increments for each run of a specific workflow. Which GitHub context property is most appropriate?

- A) `github.run_id` — a unique global identifier that is consistent across re-runs
- B) `github.run_number` — a sequential counter scoped to a specific workflow, starting at 1
- C) `github.run_attempt` — the attempt number for the current workflow run
- D) `github.sha` — the commit SHA that triggered the run

---

### Question 11 — Context Availability Reference

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Context availability in run-name

**Question**:
A developer wants to set a dynamic `run-name` for a workflow that includes a value from a workflow dispatch input. Which contexts are available in the `run-name:` key?

- A) All contexts including `env`, `secrets`, `job`, and `steps`
- B) Only `github`, `inputs`, and `vars`
- C) Only `github` and `env`
- D) Only `github`, `inputs`, `vars`, and `secrets`

---

### Question 12 — Context Availability Reference

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Context availability in jobs.<job_id>.runs-on

**Scenario**:
A workflow needs to dynamically select the runner OS using a value passed in through workflow_dispatch inputs. A developer writes `runs-on: ${{ inputs.runner-os }}`.

**Question**:
Is this a valid use of context in the `runs-on:` key, and which contexts are available there?

- A) Invalid — `runs-on:` only accepts literal string values, no context expressions are supported
- B) Valid — all contexts including `env`, `matrix`, and `secrets` are available in `runs-on:`
- C) Valid — `inputs`, `github`, and `vars` contexts are available in `runs-on:`
- D) Valid — only the `matrix` and `strategy` contexts are available in `runs-on:`

---

### Question 13 — Context Availability Reference

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Context availability in jobs.<job_id>.if

**Scenario**:
A developer wants to conditionally skip a deployment job based on whether the preceding test job succeeded. They write `if: needs.test.result == 'success'` on the deployment job.

**Question**:
Is the `needs` context available in `jobs.<job_id>.if:`, and what other context group is notable at this level that is NOT available in `steps[*].if`?

- A) No — `needs` context is not available in `jobs.<job_id>.if:`, only in step conditions
- B) Yes — `needs` is available at `jobs.<job_id>.if:`; notably `secrets` is also available here
- C) Yes — `needs` is available; and `env`, `job`, and `runner` contexts are also available at job-level `if:`
- D) No — job-level `if:` only supports `github` and `inputs` contexts

---

### Question 14 — Context Availability Reference

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Full context set in steps

**Question**:
Which statement correctly describes the context availability difference between `jobs.<job_id>.if:` and `jobs.<job_id>.steps[*].run:`?

- A) Both locations have identical context availability
- B) `steps[*].run:` has access to the full context set including `env`, `job`, `runner`, `steps`, `strategy`, and `matrix`; `jobs.<job_id>.if:` does not have `env`, `job`, or `runner` by default
- C) `jobs.<job_id>.if:` has more contexts available than `steps[*].run:` because it can see future step outputs
- D) `steps[*].run:` only has access to `github`, `env`, and `secrets` contexts

---

### Question 15 — Context Availability Reference

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Strategy matrix context availability

**Scenario**:
A developer attempts to use `${{ env.MATRIX_VALUE }}` inside the `jobs.<job_id>.strategy.matrix:` definition to dynamically populate matrix values from a workflow-level environment variable.

**Question**:
Why will this expression fail, and what contexts ARE available in `strategy.matrix:`?

- A) It will fail because `env` context is not available in `strategy.matrix:`; only `github`, `inputs`, and `vars` are available there
- B) It will fail because `strategy.matrix:` does not support any context expressions — only literal values
- C) It will succeed because the `env` context is always available in all workflow keys
- D) It will fail because `env` context requires `fromJSON()` wrapper when used in matrix definitions

---

### Question 16 — Workflow File Structure

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Top-level workflow keys

**Question**:
Which of the following is a valid top-level key in a GitHub Actions workflow YAML file?

- A) `triggers:`
- B) `pipeline:`
- C) `concurrency:`
- D) `stages:`

---

### Question 17 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: defaults.run configuration

**Scenario**:
A monorepo has all application source code in the `./app` subdirectory. Every step in every job must run commands from that directory. A developer wants to avoid adding `working-directory: ./app` to every individual step.

**Question**:
Which workflow-level configuration achieves this requirement?

- A) Set `env: WORKING_DIR: ./app` at the workflow level and reference it in each `run:` command
- B) Use `defaults: run: working-directory: ./app` at the workflow level
- C) Set `runs-on: ubuntu-latest` with a custom `entrypoint: ./app` option
- D) Use `on: workflow_dispatch: inputs: working-directory:` with a default value

---

### Question 18 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: concurrency group and cancel-in-progress

**Scenario**:
A CI workflow runs on every push to any branch. When a developer pushes multiple commits rapidly, several workflow runs queue up for the same branch, wasting runner minutes on outdated code.

**Question**:
Which concurrency configuration cancels older runs on the same branch when a newer run starts?

- A) `concurrency: cancel-old: true` at the workflow level with branch filtering
- B) `concurrency: group: ${{ github.workflow }}-${{ github.ref }}` with `cancel-in-progress: true`
- C) `concurrency: max-parallel: 1` combined with `strategy: fail-fast: true`
- D) `on: push: concurrency: cancel-previous: ${{ github.ref }}`

---

### Question 19 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: workflow_run trigger

**Scenario**:
Team A maintains a "Build" workflow. Team B wants their "Integration Test" workflow to automatically start after the Build workflow completes on the `main` branch, but only when the build was successful.

**Question**:
Which trigger configuration achieves this?

- A) `on: workflow_call:` with `needs: build` referencing Team A's workflow
- B) `on: workflow_run: workflows: ["Build"] branches: [main] types: [completed]` with an `if:` condition checking `github.event.workflow_run.conclusion == 'success'`
- C) `on: push: branches: [main]` with a step condition that checks the previous workflow status via API
- D) `on: repository_dispatch:` listening for a custom event dispatched by the Build workflow

---

### Question 20 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: schedule cron trigger

**Scenario**:
A security team wants to run a dependency vulnerability scan every weekday at 9:00 AM UTC.

**Question**:
Which `on:` configuration correctly schedules this workflow?

- A) `on: schedule: cron: "0 9 * * 1-5"`
- B) `on: schedule: cron: "9 0 * * MON-FRI"`
- C) `on: timer: interval: "daily" time: "09:00" days: "weekdays"`
- D) `on: schedule: cron: "0 9 * * 0-4"`

---

### Question 21 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: workflow_dispatch with input types

**Scenario**:
A deployment workflow needs a manual trigger with a dropdown to select the target environment (`staging` or `production`) and a boolean toggle to enable verbose logging.

**Question**:
Which `on: workflow_dispatch:` input configuration supports this?

- A) `inputs: environment: type: string` and `inputs: verbose: type: flag`
- B) `inputs: environment: type: choice options: [staging, production]` and `inputs: verbose: type: boolean`
- C) `inputs: environment: type: enum values: [staging, production]` and `inputs: verbose: type: toggle`
- D) `inputs: environment: type: select` and `inputs: verbose: type: checkbox`

---

### Question 22 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: defaults.run shell override

**Scenario**:
A workflow sets `defaults: run: shell: bash` at the workflow level. One specific step needs to run a PowerShell command on the same Ubuntu runner.

**Question**:
How can the developer override the default shell for that specific step?

- A) It cannot be overridden at the step level; the shell must be changed at the workflow level
- B) Add `shell: pwsh` to that specific step's configuration, which overrides the `defaults.run.shell` for that step only
- C) Use a separate job with `runs-on: windows-latest` to execute PowerShell commands
- D) Prefix the run command with `#!/usr/bin/env pwsh` as a shebang line

---

### Question 23 — Workflow Trigger Events

**Difficulty**: Easy
**Answer Type**: one
**Topic**: push trigger path filtering

**Question**:
Which `push` trigger configuration causes the workflow to run only when files in the `src/` directory are changed?

- A) `on: push: include: ["src/**"]`
- B) `on: push: paths: ["src/**"]`
- C) `on: push: filter: paths: ["src/"]`
- D) `on: push: watch: ["src/**"]`

---

### Question 24 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: pull_request types

**Scenario**:
A code review workflow should only run when a pull request is explicitly marked as "Ready for Review" (converted from Draft), not when new commits are pushed to an already-open PR.

**Question**:
Which `pull_request` trigger configuration achieves this?

- A) `on: pull_request: types: [ready_for_review]`
- B) `on: pull_request: types: [opened, synchronized]` with an `if:` condition checking draft status
- C) `on: pull_request_review: types: [ready]`
- D) `on: pull_request: filter: draft: false`

---

### Question 25 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: push branch AND path filter logic

**Scenario**:
A workflow has this trigger:
```yaml
on:
  push:
    branches: [main]
    paths: ["docs/**"]
```
A developer pushes a commit to the `main` branch that only modifies `src/app.js`.

**Question**:
Does the workflow run, and why?

- A) Yes — the push was to `main` which satisfies the branch filter, so the workflow runs
- B) No — both the branch filter AND the path filter must be satisfied; `src/app.js` does not match `docs/**`
- C) Yes — path filters are only applied when branch filters are not specified
- D) No — the workflow requires changes to both `main` branch files AND docs/**` simultaneously

---

### Question 26 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: pull_request_target security implications

**Scenario**:
A public repository uses `on: pull_request_target:` to run a workflow that posts a comment on PRs from forks. A security-conscious maintainer raises a concern about this trigger.

**Question**:
What is the primary security risk of `pull_request_target` compared to `pull_request`?

- A) `pull_request_target` runs in the context of the fork, so it cannot access secrets
- B) `pull_request_target` runs with write-level token and can access secrets; if the workflow also checks out fork code and runs it, attackers can exfiltrate secrets
- C) `pull_request_target` always runs with admin permissions regardless of repository settings
- D) `pull_request_target` sends the full repository contents to GitHub's external security scanning service

---

### Question 27 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: repository_dispatch trigger

**Scenario**:
An external deployment system wants to trigger a GitHub Actions workflow when a deployment completes in an on-premises environment. The external system can make outbound HTTP calls.

**Question**:
Which trigger type is designed for this use case, and what HTTP method does the external system use?

- A) `on: webhook:` — the external system registers a webhook in GitHub Settings
- B) `on: repository_dispatch:` — the external system sends a POST request to `/repos/{owner}/{repo}/dispatches`
- C) `on: external_trigger:` — the external system pushes a trigger file to a designated GitHub branch
- D) `on: api_call:` — the external system calls the GitHub Actions API to queue a workflow run

---

### Question 28 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: paths-ignore filter

**Scenario**:
A CI workflow should run on all pushes to `main` EXCEPT when only documentation files in `docs/` are changed — those changes shouldn't trigger expensive CI builds.

**Question**:
Which trigger configuration implements this?

- A) `on: push: branches: [main] paths-ignore: ["docs/**"]`
- B) `on: push: branches: [main] paths: ["!docs/**"]`
- C) `on: push: branches: [main] exclude: ["docs/**"]`
- D) `on: push: branches: [main] filter: ignore-paths: ["docs/"]`

---

### Question 29 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: tag push trigger

**Scenario**:
A release workflow should only trigger when a version tag following the pattern `v1.0.0` (semver with `v` prefix) is pushed to the repository.

**Question**:
Which trigger configuration uses correct glob syntax to match only these tags?

- A) `on: push: tags: ["v[0-9]+.[0-9]+.[0-9]+"]`
- B) `on: push: tags: ["v*.*.*"]`
- C) `on: create: refs: tags: pattern: "v*"`
- D) `on: release: types: [published] tags: ["v*"]`

---

### Question 30 — Custom Environment Variables

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Environment variable scope hierarchy

**Question**:
A workflow defines an environment variable `LOG_LEVEL: info` at the workflow level and `LOG_LEVEL: debug` at the job level. What value does `LOG_LEVEL` have inside that job?

- A) `info` — workflow-level variables always take precedence
- B) `debug` — the narrower job-level scope overrides the broader workflow-level value
- C) Both values are concatenated: `info:debug`
- D) An error is thrown because duplicate variable names are not allowed across scopes

---

### Question 31 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Persisting env vars between steps with GITHUB_ENV

**Scenario**:
Step 1 calculates a version string (`1.4.2-rc1`) and needs to make it available in all subsequent steps as an environment variable named `APP_VERSION`. Using `env:` on step 1 only makes it available within that step.

**Question**:
Which mechanism correctly persists the environment variable across steps in the same job?

- A) Export the variable using `export APP_VERSION=1.4.2-rc1` in a `run:` command
- B) Write `APP_VERSION=1.4.2-rc1` to the file referenced by `$GITHUB_ENV`
- C) Set `outputs: APP_VERSION: 1.4.2-rc1` in the step and access it via `${{ steps.step-id.outputs.APP_VERSION }}`
- D) Add the variable to the `secrets` context using the GitHub API from within the workflow

---

### Question 32 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Best practice for secrets in run commands

**Scenario**:
A developer needs to pass an API key to a shell command. They consider two approaches:
- Option A: `run: curl -H "Authorization: ${{ secrets.API_KEY }}" https://api.example.com`
- Option B: Map `secrets.API_KEY` to an environment variable and reference `$API_KEY` in the shell command

**Question**:
Why is Option B the recommended security practice?

- A) Option A causes the workflow to fail because context expressions are not evaluated inside `run:` commands
- B) Option A risks the secret value being interpolated into the GitHub Actions log before shell execution; Option B keeps the value in the process environment and reduces interpolation exposure
- C) Option A only works on Windows runners; Option B is cross-platform
- D) Option B encrypts the variable value at rest in the runner's temporary storage

---

### Question 33 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Job-level env with context expressions

**Scenario**:
A developer wants all steps in a job to have access to the repository name formatted as an env var. They write:
```yaml
jobs:
  build:
    env:
      REPO_NAME: ${{ github.repository }}
```

**Question**:
Is this valid, and where is `REPO_NAME` accessible?

- A) Invalid — context expressions cannot be used in `env:` values at the job level
- B) Valid — `REPO_NAME` is available to all steps within that job as an OS environment variable
- C) Valid — but only if the variable is also declared at the workflow level first
- D) Valid — but `REPO_NAME` is only accessible via `${{ env.REPO_NAME }}`, not as a shell variable

---

### Question 34 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Step-level env isolation

**Scenario**:
A workflow has three sequential steps. Step 2 defines `env: CACHE_BUST: "abc123"`. A developer expects to access `$CACHE_BUST` in Step 3.

**Question**:
What will happen when Step 3 tries to use `$CACHE_BUST`?

- A) Step 3 will see the value `abc123` because environment variables persist within the same job
- B) Step 3 will see an empty value because step-level `env:` variables are scoped to that step only; use `$GITHUB_ENV` to persist across steps
- C) Step 3 will error because using a variable from another step without declaring it causes a syntax error
- D) Step 3 will use the workflow-level value of `CACHE_BUST` if one exists, otherwise the step-level value from Step 2

---

### Question 35 — Default Environment Variables

**Difficulty**: Easy
**Answer Type**: one
**Topic**: GITHUB_REF_NAME vs GITHUB_REF

**Question**:
A script needs the simple branch or tag name (e.g., `main`) without the full ref path. Which default environment variable provides this?

- A) `GITHUB_REF` — always contains the short name
- B) `GITHUB_REF_NAME` — contains the short ref name (branch or tag without `refs/heads/` or `refs/tags/` prefix)
- C) `GITHUB_REF_SLUG` — a URL-safe version of the ref name
- D) `GITHUB_BRANCH` — the dedicated branch name variable

---

### Question 36 — Default Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: GITHUB_RUN_ATTEMPT

**Scenario**:
A build script generates an artifact filename that should be unique even when a workflow run is manually re-run. The developer uses `artifact-$GITHUB_RUN_ID.zip` but finds that a re-run produces the same filename and overwrites the previous artifact.

**Question**:
Which additional variable should be incorporated into the filename to ensure uniqueness across re-runs?

- A) `GITHUB_RUN_NUMBER` — increments on each re-run attempt
- B) `GITHUB_RUN_ATTEMPT` — increments with each re-run attempt of the same run ID
- C) `GITHUB_SHA` — guaranteed unique per commit
- D) `GITHUB_WORKFLOW` — the workflow name distinguishes between runs

---

### Question 37 — Default Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: GITHUB_STEP_SUMMARY

**Scenario**:
A test step produces a markdown-formatted test results table and the team wants it to appear prominently in the GitHub Actions run summary page for easy review.

**Question**:
Which mechanism correctly writes content to the workflow run summary?

- A) Use `::set-output name=summary::` workflow command to send the markdown to the UI
- B) Write markdown content to the file path stored in `$GITHUB_STEP_SUMMARY`
- C) Call the GitHub API endpoint `POST /repos/{owner}/{repo}/actions/runs/{run_id}/summary` with the markdown payload
- D) Use the `actions/upload-artifact` action with `name: step-summary` to publish the content

---

### Question 38 — Default Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: GITHUB_TRIGGERING_ACTOR vs GITHUB_ACTOR

**Scenario**:
A deployment workflow was originally triggered by user `alice` via a push. However, the workflow was later re-run by user `bob` using the "Re-run jobs" button.

**Question**:
What values do `GITHUB_ACTOR` and `GITHUB_TRIGGERING_ACTOR` hold during the re-run initiated by `bob`?

- A) Both `GITHUB_ACTOR` and `GITHUB_TRIGGERING_ACTOR` equal `bob`
- B) `GITHUB_ACTOR` equals `alice` (original trigger); `GITHUB_TRIGGERING_ACTOR` equals `bob` (who initiated this run/re-run)
- C) `GITHUB_ACTOR` equals `bob` (most recent action); `GITHUB_TRIGGERING_ACTOR` equals `alice`
- D) Both equal `alice` because re-runs always inherit the original actor

---

### Question 39 — Default Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: GITHUB_OUTPUT for step outputs

**Scenario**:
A step needs to set a value (`build-tag=1.4.2`) that a subsequent step in the same job can read using `${{ steps.tag-step.outputs.build-tag }}`. The old `set-output` workflow command is deprecated.

**Question**:
What is the correct modern way to set this step output?

- A) `echo "::set-output name=build-tag::1.4.2"` in the `run:` command
- B) `echo "build-tag=1.4.2" >> $GITHUB_OUTPUT` in the `run:` command
- C) `echo "build-tag=1.4.2" >> $GITHUB_ENV` and then reference it as a step output
- D) Set `outputs: build-tag: "1.4.2"` as a key in the step YAML configuration

---

### Question 40 — Environment Protection Rules

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Where to configure required reviewers

**Question**:
A DevOps engineer wants to require that two senior engineers approve any workflow deployment to the `production` environment. Where in GitHub is this configured?

- A) Repository → `.github/workflows/` → add `reviewers:` key to the workflow YAML
- B) Repository → Settings → Environments → select `production` → Required reviewers
- C) Organization → Settings → Actions → Deployment policies → add reviewer rules
- D) Repository → Settings → Branch protection rules → add reviewer requirement

---

### Question 41 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Deployment branch restriction options

**Scenario**:
A company policy states that only commits from the `main` branch and branches matching `release/*` should ever be deployable to production. All other branches must be blocked from deploying.

**Question**:
Which deployment branch restriction option in the Environment settings supports this requirement?

- A) "Protected Branches Only" — only allows branches with branch protection rules enabled
- B) "Named branches" with patterns `main` and `release/*` specified in the allowed branches list
- C) "All Branches" with a workflow-level `if:` condition filtering the branches
- D) "Restricted" with a CODEOWNERS file listing the allowed branches

---

### Question 42 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Wait timer behavior

**Scenario**:
The production environment has a 30-minute wait timer configured. A workflow run reaches the deployment step at 2:00 PM. Reviewer Alice approves the deployment at 2:05 PM.

**Question**:
When does the deployment actually start executing?

- A) Immediately at 2:05 PM when Alice approves — reviewer approval overrides the wait timer
- B) At 2:30 PM — the timer counts from when the job was dispatched (2:00 PM), so 30 minutes must elapse regardless of approval time
- C) At 2:35 PM — the 30-minute timer begins when approval is granted
- D) At 2:05 PM unless a second reviewer also approves within the 30-minute window

---

### Question 43 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Environment URL in deployments tab

**Scenario**:
After deploying to staging, the team wants the deployed application URL to appear clickable in the repository's Deployments section for easy access.

**Question**:
How is the deployment URL configured in the workflow?

- A) Add `url: https://staging.example.com` as a step output after the deploy step completes
- B) Set `environment: name: staging url: https://staging.example.com` in the job configuration
- C) Call the GitHub Deployments API to update the deployment record after the workflow completes
- D) Add `DEPLOYMENT_URL=https://staging.example.com` to `$GITHUB_ENV` and GitHub will automatically detect it

---

### Question 44 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reviewer notification and approval flow

**Scenario**:
A workflow targeting the `production` environment pauses awaiting approval. A designated reviewer sees the notification but wants to review the code changes and test results before deciding to approve or reject.

**Question**:
What information is available to the reviewer in the GitHub UI when reviewing a pending deployment?

- A) Only the workflow run status and the deploying user's name
- B) The job logs, linked PR/commit details, test results, and the reviewer can approve or reject with an optional comment
- C) Only a binary approve/reject choice with no access to workflow logs until after the decision
- D) The reviewer can only see the environment name and wait timer status; logs require separate navigation

---

### Question 45 — Workflow Artifacts

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Default artifact retention period

**Question**:
When uploading an artifact with `actions/upload-artifact@v3` without specifying `retention-days:`, how long is the artifact retained by default?

- A) 1 day
- B) 90 days (maximum allowed)
- C) 5 days (repository default, configurable by admins)
- D) 30 days

---

### Question 46 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Sharing artifacts between jobs

**Scenario**:
A workflow has a `build` job that compiles a binary and a `deploy` job that should use that binary. The `deploy` job runs on a different runner than `build`.

**Question**:
What is the correct approach to share the compiled binary between these two jobs?

- A) Use `outputs:` on the build job to pass the binary as a base64-encoded string to the deploy job
- B) Mount a shared network drive on both runners using the `runs-on:` `volumes:` key
- C) In `build`, upload the binary via `actions/upload-artifact`; in `deploy` (declared with `needs: build`), download it via `actions/download-artifact`
- D) Store the binary as a GitHub secret and reference it in the deploy job

---

### Question 47 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Artifact glob path patterns and exclusion

**Scenario**:
A build step generates files in a `dist/` directory including compiled outputs (`.js`, `.css`) and source maps (`.map`). The team wants to upload only the compiled outputs, excluding `.map` files.

**Question**:
Which `upload-artifact` path configuration achieves this?

- A) `path: dist/*.js\ndist/*.css` using line-separated glob patterns; source maps are excluded by default
- B) Using a multiline path with inclusion patterns and `!dist/**/*.map` on a separate line to exclude `.map` files
- C) Set `exclude: ["*.map"]` as a sibling key to `path:` in the `upload-artifact` step
- D) Source maps cannot be excluded; upload the full `dist/` and filter during the download step

---

### Question 48 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Artifact storage and repository limits

**Question**:
A team notices their repository is approaching its storage quota. They identify large artifacts from old workflow runs as the primary cause. What is true about artifact storage and cleanup?

- A) Artifact storage is entirely separate from repository storage and does not count toward any quota
- B) Artifacts count toward repository storage; they can be manually deleted via the GitHub UI or API, and expired artifacts are permanently deleted
- C) Artifacts automatically compress to a maximum of 1 GB per workflow run regardless of the original file sizes
- D) Only artifacts larger than 500 MB count toward repository storage limits

---

### Question 49 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: one
**Topic**: download-artifact across workflow runs

**Scenario**:
A nightly build workflow generates a large reference dataset artifact. A separate on-demand testing workflow needs to download this artifact without re-running the nightly build.

**Question**:
What does `actions/download-artifact@v3` require to download an artifact from a different workflow run?

- A) It is not possible — artifacts can only be downloaded within the same workflow run that uploaded them
- B) The artifact can be downloaded by specifying the `run-id` of the nightly build run and the artifact `name`
- C) The artifact must be transferred to a GitHub Release before it can be accessed by other workflows
- D) Use the `actions/cache` action instead; artifacts cannot be shared between workflow runs

---

### Question 50 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: one
**Topic**: cache-hit output from actions/cache

**Scenario**:
A workflow uses `actions/cache@v3` with a cache key based on `hashFiles('**/package-lock.json')`. A developer wants to skip the `npm ci` installation step entirely when a cache hit occurs.

**Question**:
How can the developer use the cache action's output to conditionally skip the install step?

- A) The `actions/cache` action automatically skips subsequent installation commands when a cache hit occurs
- B) Give the cache step an `id:`, then add `if: steps.<id>.outputs.cache-hit != 'true'` to the install step
- C) Use `continue-on-error: true` on the cache step; if it fails, run install; if it passes, skip install
- D) Check the `CACHE_HIT` environment variable that `actions/cache` automatically sets in the environment

---

### Question 51 — Workflow Caching

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Cache storage limit

**Question**:
What is the maximum cache storage limit per repository in GitHub Actions?

- A) 500 MB
- B) 2 GB
- C) 5 GB
- D) 10 GB

---

### Question 52 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Cache key with hashFiles

**Scenario**:
A Node.js project uses `npm`. A developer wants to cache `~/.npm` and ensure the cache is invalidated whenever `package-lock.json` changes anywhere in the repository.

**Question**:
Which cache key expression correctly implements this?

- A) `key: ${{ runner.os }}-npm-${{ hashFiles('package-lock.json') }}`
- B) `key: ${{ runner.os }}-npm-${{ hashFiles('**/package-lock.json') }}`
- C) `key: npm-cache-${{ github.sha }}`
- D) `key: ${{ runner.os }}-npm-${{ checksum('package-lock.json') }}`

---

### Question 53 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: restore-keys fallback behavior

**Scenario**:
A cache key is `ubuntu-npm-abc123` based on the current lock file hash. No exact match exists in the cache, but a prior cache entry with key `ubuntu-npm-xyz789` exists (partial prefix match).

**Question**:
If `restore-keys: ubuntu-npm-` is configured, what happens?

- A) The workflow fails because no exact key match was found
- B) The partial prefix match `ubuntu-npm-xyz789` is restored as a fallback; `cache-hit` output will be `false` indicating partial restore
- C) The `restore-keys` fallback restores the most recent matching cache but marks `cache-hit: true`
- D) GitHub downloads all caches with matching prefix and merges them

---

### Question 54 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Cache eviction and expiry policy

**Question**:
A repository has accumulated numerous cache entries and is approaching the 5 GB limit. Which statement correctly describes how GitHub manages cache eviction?

- A) GitHub deletes caches in FIFO order regardless of access frequency
- B) The oldest cache entries are evicted first when the total size exceeds 5 GB; entries not accessed for 7 days are also automatically removed
- C) GitHub notifies the repository admin and pauses all workflow runs until old caches are manually deleted
- D) Cache entries older than 30 days are archived to cold storage but still accessible with a 10-second delay

---

### Question 55 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Branch cache isolation

**Scenario**:
A developer working on `feature/dark-mode` installs a new npm package and generates a new cache entry. A colleague working on `bugfix/header` wants to know if they can use the cache from `feature/dark-mode`.

**Question**:
What is the correct behavior of cache access across branches?

- A) All caches are accessible to all branches within the same repository without restriction
- B) Cache entries from one branch are not accessible from other branches; each branch has its own isolated cache scope (except the default branch, which can be read by all branches as a fallback)
- C) Cache entries are global across the organization; any repository in the org can read them
- D) Caches are scoped to workflow files; two branches using the same workflow file share the same cache

---

### Question 56 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Cache for compiled assets

**Scenario**:
A Python ML project has a step that compiles C extensions during `pip install`. This step takes 8 minutes and the compiled `.so` files are stored under `.venv/`. The developer wants to cache the virtual environment.

**Question**:
Which configuration most appropriately caches the virtual environment with proper invalidation?

- A) `path: .venv key: python-venv-${{ github.sha }}` — cache is tied to exact commit
- B) `path: .venv key: ${{ runner.os }}-python-${{ hashFiles('requirements.txt', 'setup.py') }}` — cache invalidates when dependencies change
- C) `path: ~/.pip/cache key: pip-always` — caches the pip download cache with a static key
- D) `path: .venv key: ${{ github.workflow }}-venv` — scoped to this workflow only

---

### Question 57 — Workflow Sharing

**Difficulty**: Easy
**Answer Type**: one
**Topic**: workflow_call trigger

**Question**:
What trigger keyword is required in a workflow file to make it callable as a reusable workflow from another workflow?

- A) `on: workflow_dispatch:`
- B) `on: workflow_call:`
- C) `on: reusable: true`
- D) `on: external_call:`

---

### Question 58 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Calling a reusable workflow with inputs and secrets

**Scenario**:
An organization stores a shared deployment workflow at `org/shared-workflows/.github/workflows/deploy.yml@main`. A consuming repository needs to call it with `environment: production` and pass `secrets.DEPLOY_KEY`.

**Question**:
Which `jobs:` block syntax correctly calls this reusable workflow?

- A) `uses: org/shared-workflows/.github/workflows/deploy.yml@main with: environment: production secrets: deploy-key: ${{ secrets.DEPLOY_KEY }}`
- B) Jobs use `uses:` at the job level: `uses: org/shared-workflows/.github/workflows/deploy.yml@main` + `with: environment: production` + `secrets: deploy-key: ${{ secrets.DEPLOY_KEY }}`
- C) `steps: - uses: org/shared-workflows/.github/workflows/deploy.yml@main` — reusable workflows are called from a step's `uses:` key
- D) `needs: org/shared-workflows` with `env: ENVIRONMENT: production` — needs references external workflows directly

---

### Question 59 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: secrets: inherit

**Scenario**:
A reusable workflow needs access to many secrets from the calling workflow. Explicitly mapping each secret is tedious since the calling and called workflows share the same organization.

**Question**:
Which shorthand passes all calling workflow secrets to the reusable workflow without listing each one?

- A) `secrets: pass-all: true`
- B) `secrets: inherit`
- C) `secrets: "*"` (wildcard pattern)
- D) `secrets: forward: true`

---

### Question 60 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reusable workflow outputs

**Scenario**:
A reusable `build` workflow generates a `version-tag` output that the calling workflow needs in subsequent jobs.

**Question**:
How does the calling workflow access the `version-tag` output from the reusable workflow job named `build-job`?

- A) `${{ needs.build-job.outputs.version-tag }}` — reusable workflow outputs surface through the `needs` context like regular job outputs
- B) `${{ jobs.build-job.outputs.version-tag }}` — job outputs from called workflows are in the `jobs` context
- C) `${{ steps.build-job.outputs.version-tag }}` — the called workflow is treated as a step
- D) Outputs from reusable workflows cannot be accessed by the calling workflow

---

### Question 61 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Required workflows (enterprise)

**Scenario**:
An enterprise security team wants to ensure that every pull request across all organization repositories runs a mandatory security scan workflow, even for repositories where owners might disable Actions.

**Question**:
Which feature enables this enforcement?

- A) Branch protection rules with "Require status checks" and the security scan listed
- B) Enterprise-required workflows configured in Enterprise Settings → Policies → Required workflows
- C) Organization-level CODEOWNERS file listing the security scan workflow as a required reviewer
- D) Repository rulesets with a custom workflow enforcement action

---

### Question 62 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reusable workflow inputs definition

**Scenario**:
A reusable workflow exposes a `node-version` input. If the calling workflow doesn't provide a value, the reusable workflow should default to Node.js 18.

**Question**:
Which `workflow_call` inputs definition is correct?

- A) `inputs: node-version: type: string required: true` — caller must always provide the version
- B) `inputs: node-version: type: string required: false default: "18"` — optional with fallback to "18"
- C) `inputs: node-version: type: number optional: "18"` — number type with inline default
- D) `inputs: defaults: node-version: "18"` — defaults declared in a sibling `defaults` block

---

### Question 63 — Workflow Debugging

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Enabling step debug logging

**Question**:
How is step-level debug logging enabled for a GitHub Actions workflow run without modifying the workflow file?

- A) Append `?debug=true` to the GitHub Actions run URL in the browser
- B) Add the repository secret `ACTIONS_STEP_DEBUG` with value `true`
- C) Set `RUNNER_DEBUG=1` in the workflow's `env:` block at the workflow level
- D) Enable "Verbose logging" in Repository → Settings → Actions → Runner settings

---

### Question 64 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: one
**Topic**: ::debug:: workflow command

**Scenario**:
A developer wants to emit a custom debug message (`"Calculated checksum: abc123"`) that only appears in the logs when debug logging is enabled, keeping logs clean in normal runs.

**Question**:
Which `run:` command is correct?

- A) `echo "[DEBUG] Calculated checksum: abc123"`
- B) `echo "::debug::Calculated checksum: abc123"`
- C) `echo "ACTIONS_DEBUG=Calculated checksum: abc123"`
- D) `core.debug('Calculated checksum: abc123')` — in a `run:` step

---

### Question 65 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: one
**Topic**: RUNNER_DEBUG secret vs ACTIONS_STEP_DEBUG

**Scenario**:
A DevOps engineer wants to enable verbose runner-level diagnostic information (runner environment, file permissions, command execution details) in addition to step-level debug output.

**Question**:
Which additional secret enables runner-level diagnostic output, and what is its value?

- A) `ACTIONS_RUNNER_VERBOSE: true`
- B) `RUNNER_DEBUG: 1`
- C) `GITHUB_ACTIONS_DEBUG: true`
- D) `ACTIONS_RUNNER_DEBUG: true`

---

### Question 66 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow log access and step expansion

**Scenario**:
A workflow fails in a specific step but the error message is truncated in the summary view. A developer needs to see complete log output for that step.

**Question**:
What is the correct way to access full, expanded step logs in the GitHub UI?

- A) Download the log archive from the workflow run API and search locally
- B) Navigate to Repository → Actions → [workflow run] → click the failed job → click the failed step to expand its full log output
- C) Enable `ACTIONS_STEP_DEBUG` and re-run the workflow to get complete logs
- D) Use the GitHub CLI with `gh run view --log-failed` to retrieve full failure logs

---

### Question 67 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: one
**Topic**: ::error:: and ::warning:: workflow commands

**Scenario**:
A build step detects a configuration issue that should be surfaced as a prominent error annotation in the GitHub UI (not just a log line), referencing the problematic file and line number.

**Question**:
Which workflow command correctly creates an error annotation with file and line context?

- A) `echo "ERROR: config.yaml line 45: missing required field"`
- B) `echo "::error file=config.yaml,line=45::Missing required field 'api_url'"`
- C) `echo "::annotate level=error::config.yaml:45: missing required field"`
- D) `core.error('Missing required field', {file: 'config.yaml', line: 45})`

---

### Question 68 — Workflows REST API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: API authentication methods

**Question**:
Which authentication methods are accepted by the GitHub Workflows REST API for making requests?

- A) Only SAML SSO tokens with organization membership verification
- B) Personal Access Token (PAT), GitHub App token, or OAuth token
- C) Only the `GITHUB_TOKEN` automatically provisioned during workflow runs
- D) HTTP Basic Authentication with GitHub username and password

---

### Question 69 — Workflows REST API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Triggering workflow_dispatch via API

**Scenario**:
An external CI/CD system needs to trigger a GitHub Actions workflow named `deploy.yml` in the repository `myorg/myapp` on the `main` branch, passing an input `environment: production`.

**Question**:
Which API call achieves this?

- A) `GET /repos/myorg/myapp/actions/workflows/deploy.yml/runs?branch=main&input=environment:production`
- B) `POST /repos/myorg/myapp/actions/workflows/deploy.yml/dispatches` with body `{"ref": "main", "inputs": {"environment": "production"}}`
- C) `POST /repos/myorg/myapp/actions/runs` with body `{"workflow": "deploy.yml", "branch": "main"}`
- D) `PUT /repos/myorg/myapp/actions/workflows/deploy.yml` with body `{"trigger": true, "ref": "main"}`

---

### Question 70 — Workflows REST API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Listing workflow runs with status filter

**Scenario**:
A dashboard application needs to retrieve all failed workflow runs from the past 24 hours for a repository to display in a status panel.

**Question**:
Which API endpoint and parameter combination retrieves this data?

- A) `GET /repos/{owner}/{repo}/actions/workflows` with `filter: failed`
- B) `GET /repos/{owner}/{repo}/actions/runs?status=failure&created=>YYYY-MM-DD`
- C) `POST /repos/{owner}/{repo}/actions/runs/query` with a JSON filter body
- D) `GET /repos/{owner}/{repo}/actions/runs?conclusion=failure` only (no date filter supported)

---

### Question 71 — Workflows REST API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Canceling a workflow run

**Scenario**:
An automated monitoring system detects that a deployment workflow run is in progress but the deployment target is currently under maintenance. The system needs to cancel the running workflow programmatically.

**Question**:
Which API call cancels a specific workflow run identified by `run_id`?

- A) `DELETE /repos/{owner}/{repo}/actions/runs/{run_id}`
- B) `POST /repos/{owner}/{repo}/actions/runs/{run_id}/cancel`
- C) `PATCH /repos/{owner}/{repo}/actions/runs/{run_id}` with body `{"status": "cancelled"}`
- D) `PUT /repos/{owner}/{repo}/actions/runs/{run_id}/stop`

---

### Question 72 — Workflows REST API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Re-running failed jobs via API

**Scenario**:
A flaky network test caused a workflow run to fail. An engineer wants to re-run only the failed jobs (not the entire workflow) using the API.

**Question**:
Which API endpoint re-runs only failed jobs in a workflow run?

- A) `POST /repos/{owner}/{repo}/actions/runs/{run_id}/rerun`
- B) `POST /repos/{owner}/{repo}/actions/runs/{run_id}/rerun-failed-jobs`
- C) `POST /repos/{owner}/{repo}/actions/runs/{run_id}/retry?failed-only=true`
- D) `PATCH /repos/{owner}/{repo}/actions/runs/{run_id}` with body `{"rerun": "failed"}`

---

### Question 73 — Reviewing Deployments

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Deployment review navigation

**Question**:
Where does a designated reviewer go in GitHub to approve or reject a pending deployment that is waiting for review?

- A) Repository → Pull Requests → Reviews tab → Pending Deployments
- B) Repository → Actions → [workflow run] → Review Deployments button on the paused job
- C) Repository → Settings → Environments → Pending Approvals queue
- D) Personal notification email → click "Approve" link directly

---

### Question 74 — Reviewing Deployments

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What reviewers can see before deciding

**Scenario**:
A reviewer receives a notification that a production deployment is awaiting approval. Before clicking Approve or Reject, they want to verify that all tests passed and review which code changes are being deployed.

**Question**:
What information is directly available to the reviewer in the deployment review interface?

- A) Only the environment name and the requesting user; test results require separate navigation to the Pull Requests tab
- B) Job execution logs, the commit/PR context, test step results, and a comment field for the approval decision
- C) Only a binary Approve/Reject button; all other context must be gathered from separate repository views
- D) The deployment diff and reviewer checklist, but workflow logs are hidden until after the decision

---

### Question 75 — Reviewing Deployments

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Multiple reviewers and approval count

**Scenario**:
A production environment requires approval from at least 2 of the 5 designated reviewers. Reviewer A approves the deployment. What happens next?

**Question**:
Which statement correctly describes the workflow behavior after the first approval?

- A) The workflow immediately proceeds after the first approval since at least one reviewer approved
- B) The workflow remains paused until a second reviewer also approves (meeting the minimum of 2 approvals required)
- C) The workflow sends a final confirmation to the requester, who must click "Confirm Deployment" to proceed
- D) The first approval starts a 1-hour countdown timer; if no rejection arrives, deployment proceeds automatically

---

### Question 76 — Reviewing Deployments

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Deployment history and audit trail

**Scenario**:
After a production incident, a compliance officer needs to know who approved the deployment that occurred last Tuesday, what branch it came from, and what tests ran before deployment.

**Question**:
Where is this deployment audit information stored and accessible?

- A) Only in the organization's audit log under Security → Audit log; individual deployment history is not stored in GitHub
- B) In the repository's Deployments section showing history per environment, including the approver, branch, commit, and linked workflow run logs
- C) In the workflow YAML file as auto-generated comments added after each deployment
- D) Only accessible via the GitHub API; the UI does not retain historical deployment approval data

---

### Question 77 — Reviewing Deployments

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Environment-scoped secrets

**Scenario**:
A workflow deploys to both `staging` and `production` environments. The production database connection string is different from staging and must not be accessible to staging deployments.

**Question**:
How should environment-specific secrets be configured to achieve this isolation?

- A) Prefix staging secrets with `STG_` and production secrets with `PROD_` and use conditionals in the workflow to select the correct one
- B) Store the production connection string as a repository secret and use an `if:` condition to only use it in the production deployment step
- C) Store the connection string as an environment-scoped secret under the `production` environment; it will only be available to jobs that target that specific environment
- D) Use GitHub Vault integration to store environment-specific secrets with environment-based access policies

---

### Question 78 — Creating and Publishing Actions

**Difficulty**: Easy
**Answer Type**: one
**Topic**: action.yml required file

**Question**:
Every GitHub Action must include a metadata file that defines its name, description, inputs, outputs, and runtime. What is this file called?

- A) `workflow.yml`
- B) `action.yaml` or `action.yml`
- C) `manifest.json`
- D) `Dockerfile` (for Docker actions)

---

### Question 79 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Action types comparison

**Scenario**:
A team needs to create a custom action that wraps a complex Bash script, calls several other pre-existing GitHub Actions steps, and requires no compiled code or Docker. Startup speed is a priority.

**Question**:
Which action type is most appropriate?

- A) JavaScript action using `@actions/exec` to run the Bash script
- B) Docker container action using a Ubuntu-based image
- C) Composite action using `runs: using: composite` with `steps:` that include both shell commands and `uses:` references
- D) A reusable workflow instead of an action, since Docker and JavaScript are not suitable

---

### Question 80 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: @actions/core package usage

**Scenario**:
A JavaScript action needs to read an input named `deployment-environment`, log an informational message, set an output named `deployment-url`, and fail the action with a specific error message if validation fails.

**Question**:
Which `@actions/core` methods correctly handle these four operations in order?

- A) `core.input()`, `core.log()`, `core.output()`, `core.throw()`
- B) `core.getInput('deployment-environment')`, `core.info('message')`, `core.setOutput('deployment-url', value)`, `core.setFailed('error message')`
- C) `process.env.INPUT_DEPLOYMENT_ENVIRONMENT`, `console.log()`, `core.exportVariable()`, `process.exit(1)`
- D) `core.readInput()`, `core.print()`, `core.writeOutput()`, `core.error()`

---

### Question 81 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Branding in action.yml for Marketplace

**Scenario**:
A developer is publishing a custom GitHub Action to the GitHub Marketplace and wants it to display a distinctive icon and color in the Marketplace listing.

**Question**:
Where in `action.yml` is Marketplace branding (icon and color) configured?

- A) Under a top-level `marketplace:` key with `icon:` and `color:` child keys
- B) Under a top-level `branding:` key with `icon:` (Feather icon name) and `color:` child keys
- C) In the `README.md` using a special `<!-- marketplace: icon=send color=blue -->` HTML comment
- D) By naming the repository `action-icon-{color}` using a naming convention GitHub Marketplace reads

---

### Question 82 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Docker action entrypoint

**Scenario**:
A Docker container action runs a Python script `run.py` as its entrypoint. The `action.yml` defines `runs: using: docker image: Dockerfile`. The developer needs to specify that `python run.py` is the command to execute.

**Question**:
How is the Docker action entrypoint specified in `action.yml`?

- A) Add `entrypoint: python run.py` under the `runs:` key in `action.yml`
- B) Use `CMD ["python", "run.py"]` in the Dockerfile; the `action.yml` `runs:` key has no `entrypoint` override option for Docker actions
- C) Add `pre: python run.py` under the `runs:` key to specify the pre-execution command
- D) Set `command: "python run.py"` under the `runs:` key in `action.yml`

---

### Question 83 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Action versioning and SHA pinning

**Scenario**:
An organization's security policy requires that all third-party actions must be pinned to a specific immutable reference to prevent supply chain attacks. A developer is adding `actions/setup-node` to a workflow.

**Question**:
Which reference format meets this security requirement?

- A) `uses: actions/setup-node@v3` — major version tag
- B) `uses: actions/setup-node@v3.8.1` — full semver tag
- C) `uses: actions/setup-node@1234567890abcdef1234567890abcdef12345678` — full commit SHA
- D) `uses: actions/setup-node@main` — trunk branch reference

---

### Question 84 — Managing Runners

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Self-hosted runner registration location

**Question**:
Where in the GitHub UI does a repository administrator go to register a new self-hosted runner for a specific repository?

- A) Repository → Actions → Marketplace → Add Runner
- B) Repository → Settings → Actions → Runners → New self-hosted runner
- C) Organization → Settings → Runners → Register → assign to repository
- D) Repository → Settings → Environments → Runners → Add new

---

### Question 85 — Managing Runners

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Runner labels for job targeting

**Scenario**:
An organization has self-hosted runners tagged with custom labels: `gpu`, `high-memory`, and `linux`. A machine learning training job requires both GPU access and high memory. Other jobs should not run on these expensive machines.

**Question**:
Which `runs-on:` configuration correctly targets only runners with both the `gpu` and `high-memory` labels?

- A) `runs-on: gpu || high-memory` — using OR logic
- B) `runs-on: [self-hosted, gpu, high-memory]` — array syntax requires all labels to match
- C) `runs-on: labels: [gpu, high-memory]` — using the `labels:` key
- D) `runs-on: self-hosted` with `label-selector: gpu AND high-memory`

---

### Question 86 — Managing Runners

**Difficulty**: Medium
**Answer Type**: one
**Topic**: GITHUB_ACTIONS environment variable on self-hosted runners

**Scenario**:
A script running in a workflow step needs to detect whether it's executing inside GitHub Actions (vs. being run locally by a developer) to conditionally enable CI-specific behavior.

**Question**:
Which environment variable is always set to `true` when running inside GitHub Actions, on both hosted and self-hosted runners?

- A) `CI=true`
- B) `GITHUB_ACTIONS=true`
- C) `RUNNER_ENVIRONMENT=github-hosted`
- D) `IS_GITHUB_RUNNER=1`

---

### Question 87 — Managing Runners

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Self-hosted runner security considerations

**Scenario**:
A public repository wants to use self-hosted runners. A security engineer raises a concern about this configuration.

**Question**:
What is the primary security risk of using self-hosted runners with public repositories?

- A) Self-hosted runners cannot authenticate to GitHub API when used with public repositories
- B) Malicious actors can fork the repository and open PRs that trigger workflow runs executing arbitrary code on the self-hosted runner's host system
- C) Self-hosted runners do not support secrets, so sensitive configuration cannot be used
- D) Public repositories are rate-limited to 1 concurrent self-hosted runner job

---

### Question 88 — Managing Runners

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Runner groups for access control

**Scenario**:
An enterprise wants to ensure that only repositories belonging to the `payments` team can use the PCI-compliant self-hosted runners that have access to production payment systems. Other repositories must not be able to run jobs on these runners.

**Question**:
Which mechanism controls which repositories can access specific self-hosted runners?

- A) Runner labels — only repositories that match the label pattern can access the runner
- B) Runner groups — runners are assigned to a group, and the group's access policy controls which repositories or organizations can use those runners
- C) CODEOWNERS file — listing the runner's registration token controls access
- D) Repository secrets — storing the runner token as a secret limits access to authorized workflows only

---

### Question 89 — GitHub Actions Enterprise

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Enterprise policy hierarchy

**Question**:
In a GitHub Enterprise setup, which level has the highest authority to restrict GitHub Actions policies?

- A) Repository administrator settings
- B) Organization owner settings
- C) Enterprise administrator settings
- D) GitHub Support configuration

---

### Question 90 — GitHub Actions Enterprise

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Allow-list for actions policies

**Scenario**:
An enterprise administrator wants to permit only actions from GitHub itself (`actions/*` and `github/*`) and two specific trusted third-party actions, while blocking all other external actions.

**Question**:
Which Actions policy setting achieves this configuration?

- A) "Allow local actions only" — only allows actions from within the organization
- B) "Allow select actions" — configure an explicit allow list with patterns like `actions/*`, `github/*`, and specific full action references
- C) "Disable Actions for all organizations" then re-enable per-repository via exceptions
- D) "Allow all verified creator actions" — uses GitHub's verification status automatically

---

### Question 91 — GitHub Actions Enterprise

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Fork pull request workflow policies

**Scenario**:
A public organization repository receives many PRs from external contributors (outside collaborators). The security team wants to ensure that ALL outside collaborators must have their workflow runs approved by a maintainer before execution, not just first-time contributors.

**Question**:
Which fork PR workflow approval setting achieves this?

- A) "Require approval for first-time contributors who are new to GitHub"
- B) "Require approval for first-time contributors" (any first-time contributor to the repo)
- C) "Require approval for all outside collaborators"
- D) "Disable Actions for fork pull requests entirely"

---

### Question 92 — GitHub Actions Enterprise

**Difficulty**: Medium
**Answer Type**: one
**Topic**: pull_request_target safe usage pattern

**Scenario**:
A workflow uses `on: pull_request_target:` to post a comment with test results on PRs from forks. The workflow must check out the fork's code to run those tests. A security engineer flags this as dangerous.

**Question**:
What is the secure approach when using `pull_request_target` and needing to run code from the fork?

- A) Grant the workflow `write` permissions; the GITHUB_TOKEN's write access ensures safe execution
- B) Never check out fork code in a `pull_request_target` workflow; use a two-workflow pattern where a `pull_request` workflow uploads test results as artifacts, and a separate `pull_request_target` workflow downloads and posts them
- C) Add the fork owner as a repository collaborator before allowing their PRs to trigger the workflow
- D) Use `pull_request_target: safe: true` configuration flag to enable sandboxed fork code execution

---

### Question 93 — GitHub Actions Enterprise

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Required workflows enforcement behavior

**Scenario**:
An enterprise administrator configures a required workflow `org/compliance/.github/workflows/sec-scan.yml@main` to run on all repositories across 3 organizations. A repository owner in one of those organizations disables GitHub Actions for their repository.

**Question**:
What happens to the required workflow for that repository?

- A) The required workflow is also disabled since Actions is disabled for the repository
- B) The required workflow continues to run on that repository even though the owner disabled Actions — required workflows are enforced regardless of repository-level Actions settings
- C) The repository is automatically re-enabled for Actions by the enterprise policy enforcement
- D) The required workflow runs but reports a policy violation rather than the actual security scan results

---

### Question 94 — Security and Optimization

**Difficulty**: Medium
**Answer Type**: one
**Topic**: GITHUB_TOKEN permission scoping

**Scenario**:
A release workflow needs to create GitHub Releases and push packages to GitHub Packages. The security team wants to follow least-privilege principles and ensure no other permissions are granted.

**Question**:
Which `permissions:` block correctly scopes the token to only the required access?

- A) `permissions: write-all` — grants all write permissions as needed
- B) `permissions: contents: write packages: write` — grants only the required scopes
- C) `permissions: releases: write packages: write` — uses release-specific scope
- D) No `permissions:` block needed — GITHUB_TOKEN defaults to write for `contents` and `packages`

---

### Question 95 — Security and Optimization

**Difficulty**: Medium
**Answer Type**: one
**Topic**: OIDC cloud federation

**Scenario**:
A team deploys to AWS and currently stores `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` as long-lived repository secrets. A security architect recommends replacing this with OIDC-based federation.

**Question**:
What is the primary security advantage of using OIDC instead of long-lived AWS credentials?

- A) OIDC tokens are longer and harder to guess than standard AWS access keys
- B) OIDC eliminates the need to store any AWS credentials in GitHub secrets; the workflow requests short-lived credentials scoped to the specific run, which expire automatically
- C) OIDC requires two-factor authentication for each AWS API call, adding an extra verification layer
- D) OIDC tokens can only be used from GitHub-hosted runners, preventing credential exfiltration from self-hosted runners

---

### Question 96 — Security and Optimization

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Script injection via untrusted input

**Scenario**:
A workflow step uses a pull request title in a shell command:
```yaml
- name: Label PR
  run: gh pr edit ${{ github.event.pull_request.number }} --title "${{ github.event.pull_request.title }}"
```
A researcher identifies this as a security vulnerability.

**Question**:
What is the attack vector and what is the recommended mitigation?

- A) The PR number could overflow; use `parseInt()` to sanitize it before use
- B) The PR title is untrusted user input directly interpolated into the shell command; an attacker can inject arbitrary shell commands via a crafted PR title. Mitigation: assign to an intermediate environment variable and reference `$ENV_VAR` in the command instead
- C) The `gh` CLI does not support context expressions; use the REST API directly instead
- D) The vulnerability is the use of `github.event.pull_request.number` — use `github.event.number` instead

---

### Question 97 — Security and Optimization

**Difficulty**: Medium
**Answer Type**: one
**Topic**: GITHUB_TOKEN cannot trigger downstream workflows

**Scenario**:
A workflow that runs on push to `main` uses `git push` with `${{ secrets.GITHUB_TOKEN }}` to commit updated build artifacts back to the repository. The team expects this push to trigger another `on: push:` workflow, but the second workflow never runs.

**Question**:
Why does the second workflow not trigger, and what is the solution?

- A) The `git push` command needs the `--trigger` flag to activate downstream workflows
- B) `GITHUB_TOKEN` cannot trigger new workflow runs to prevent infinite loops; use a Personal Access Token (PAT) or GitHub App token for the push if triggering another workflow is intentional
- C) Workflows cannot push to the triggering branch; the push is blocked by branch protection
- D) The second workflow must use `on: push: actor: actions-bot` to listen for automated pushes

---

### Question 98 — Common Failures & Troubleshooting

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Authentication error cause identification

**Question**:
A workflow step fails with `fatal: could not read Username for 'https://github.com': No such file or directory`. What is the most likely cause?

- A) The `actions/checkout` step was skipped or the checkout action version is incompatible
- B) The `GITHUB_TOKEN` is missing, has insufficient permissions, or the checkout step is not configured to use an appropriate token
- C) The runner has no internet access and cannot reach github.com
- D) The repository is private and requires SSH keys configured on the runner

---

### Question 99 — Common Failures & Troubleshooting

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Timeout configuration for long-running jobs

**Scenario**:
A data processing workflow regularly takes 4 hours to complete. The team notices workflows are consistently failing with a timeout error after 6 hours (360 minutes). They want to set a job-level timeout of 5 hours.

**Question**:
Which configuration sets a 5-hour (300-minute) timeout for the job?

- A) Add `timeout: 300` at the workflow level under `on:`
- B) Add `timeout-minutes: 300` at the job level in the job definition
- C) Add `max-duration: 300m` as a runner label in `runs-on:`
- D) Set the `ACTIONS_JOB_TIMEOUT=300` environment variable in the job's `env:` block

---

### Question 100 — Common Failures & Troubleshooting

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Dependency lock file version mismatch

**Scenario**:
A Node.js workflow that was working fine for months suddenly starts failing on `npm ci` with:
```
npm ERR! code ERESOLVE
npm ERR! ERESOLVE could not resolve dependencies
```
No changes were made to `package.json`. The Node.js version in the workflow is pinned to `18.16.0`.

**Question**:
What is the most likely cause and the correct diagnostic/resolution path?

- A) The pinned Node.js version `18.16.0` is no longer available on GitHub-hosted runners; switch to `node-version: 18`
- B) A transitive dependency released a new version that conflicts with peer dependencies in the lock file; the lock file is now out of sync. Resolution: run `npm install` locally to regenerate the lock file, review the changes, and commit the updated `package-lock.json`
- C) The `npm ci` command requires `--legacy-peer-deps` flag on all modern Node.js versions; add this flag to the install command
- D) GitHub runner images updated their global npm version; run `npm install -g npm@8` before `npm ci` to downgrade npm

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | B | Workflow files must be in `.github/workflows/` for the extension to activate schema validation and IntelliSense. | 01-GitHub-Actions-VS-Code-Extension.md | Easy |
| 2 | B | Branch tag references like `@main` are mutable; the extension warns to pin to a full commit SHA for security and reproducibility. | 01-GitHub-Actions-VS-Code-Extension.md | Medium |
| 3 | B | First verify the YAML extension is installed, confirm the file path is `.github/workflows/`, and try "Reload Window" — the most common resolution steps. | 01-GitHub-Actions-VS-Code-Extension.md | Medium |
| 4 | B | The extension provides expression evaluation that highlights type mismatches and logical issues in `if:` conditions inline. | 01-GitHub-Actions-VS-Code-Extension.md | Medium |
| 5 | B | `code --list-extensions \| grep -i github` lists all installed extensions and filters for GitHub-related ones. | 01-GitHub-Actions-VS-Code-Extension.md | Easy |
| 6 | B | `github.token` is automatically provisioned each run as a short-lived token for the repository's GitHub App installation — no manual configuration needed. | 02-Contextual-Information.md | Easy |
| 7 | C | `github.base_ref` is the target branch of the PR (what the PR merges into); `github.head_ref` is the source branch. | 02-Contextual-Information.md | Medium |
| 8 | C | GitHub Actions automatically redacts known secret values in logs, replacing them with `***`. | 02-Contextual-Information.md | Medium |
| 9 | B | `job.status` reflects completion status; `needs.<job-id>.result` provides upstream results; `if: always()` ensures the cleanup job runs regardless of outcome. | 02-Contextual-Information.md | Medium |
| 10 | B | `github.run_number` is a sequential counter starting at 1 for each workflow, making it suitable for build numbering/image tagging. | 02-Contextual-Information.md | Medium |
| 11 | B | `run-name:` only has access to `github`, `inputs`, and `vars` contexts — not `env`, `secrets`, or job-level contexts. | 03-Context-Availability-Reference.md | Easy |
| 12 | C | `runs-on:` supports context expressions; available contexts are `github`, `inputs`, and `vars` only — not `env`, `matrix`, or `secrets`. | 03-Context-Availability-Reference.md | Medium |
| 13 | B | `needs` context IS available in `jobs.<job_id>.if:`; notably `secrets` is also available at job-level `if:`, unlike some other locations. | 03-Context-Availability-Reference.md | Medium |
| 14 | B | `steps[*].run:` has the full context set including `env`, `job`, `runner`, `steps`, `strategy`, and `matrix`; `jobs.<job_id>.if:` is more restricted. | 03-Context-Availability-Reference.md | Medium |
| 15 | A | `strategy.matrix:` only allows `github`, `inputs`, and `vars` contexts. The `env` context is not available there, causing the expression to fail. | 03-Context-Availability-Reference.md | Hard |
| 16 | C | `concurrency:` is a valid top-level workflow key. `triggers:`, `pipeline:`, and `stages:` are not valid GitHub Actions keys. | 04-Workflow-File-Structure.md | Easy |
| 17 | B | `defaults: run: working-directory: ./app` sets the default working directory for all `run:` steps across all jobs (overridable at job or step level). | 04-Workflow-File-Structure.md | Medium |
| 18 | B | `concurrency: group: ${{ github.workflow }}-${{ github.ref }}` creates a unique group per workflow+branch, and `cancel-in-progress: true` cancels older runs when a newer one starts. | 04-Workflow-File-Structure.md | Medium |
| 19 | B | `on: workflow_run:` triggers after a named workflow completes; the `if:` condition on `github.event.workflow_run.conclusion == 'success'` ensures only successful builds proceed. | 04-Workflow-File-Structure.md | Medium |
| 20 | A | Cron syntax is `minute hour day month weekday`. `0 9 * * 1-5` means minute 0, hour 9, every day of month, every month, Monday through Friday. | 04-Workflow-File-Structure.md | Medium |
| 21 | B | `type: choice` with an `options:` list creates a dropdown; `type: boolean` creates a toggle checkbox in the manual trigger UI. | 04-Workflow-File-Structure.md | Medium |
| 22 | B | `shell:` specified at the step level overrides the `defaults.run.shell` for that step only, allowing mixed shell usage within the same job. | 04-Workflow-File-Structure.md | Medium |
| 23 | B | `on: push: paths: ["src/**"]` is the correct syntax; paths filtering uses the `paths:` key under the event trigger. | 05-Workflow-Trigger-Events.md | Easy |
| 24 | A | `types: [ready_for_review]` fires only when a PR is converted from draft to ready for review, not on code pushes to the PR. | 05-Workflow-Trigger-Events.md | Medium |
| 25 | B | When both `branches:` and `paths:` filters are specified on a `push` trigger, BOTH conditions must be satisfied. `src/app.js` does not match `docs/**`, so the workflow does not run. | 05-Workflow-Trigger-Events.md | Medium |
| 26 | B | `pull_request_target` runs in the base repo context with write access and secrets. If fork code is checked out and executed, attackers can craft code to exfiltrate secrets. | 05-Workflow-Trigger-Events.md | Medium |
| 27 | B | `on: repository_dispatch:` is designed for external HTTP triggers; external systems send a `POST` to `/repos/{owner}/{repo}/dispatches` with an event type. | 05-Workflow-Trigger-Events.md | Medium |
| 28 | A | `paths-ignore: ["docs/**"]` excludes commits where only docs files changed from triggering the workflow; combined with `branches: [main]` it applies to main branch pushes. | 05-Workflow-Trigger-Events.md | Medium |
| 29 | B | `tags: ["v*.*.*"]` uses glob patterns matching `v` followed by any semver-like string. Option A uses regex-style syntax not supported in GitHub glob patterns. | 05-Workflow-Trigger-Events.md | Medium |
| 30 | B | In GitHub Actions, the narrower scope wins. Job-level `LOG_LEVEL: debug` overrides the workflow-level `LOG_LEVEL: info` within that job. | 06-Custom-Environment-Variables.md | Easy |
| 31 | B | Writing `APP_VERSION=1.4.2-rc1` to the file at `$GITHUB_ENV` persists the environment variable for all subsequent steps in the same job. | 06-Custom-Environment-Variables.md | Medium |
| 32 | B | Direct `${{ secrets.API_KEY }}` interpolation in shell commands risks the value appearing in pre-processed log output. Mapping to an env var keeps it in the process environment, reducing exposure risk. | 06-Custom-Environment-Variables.md | Medium |
| 33 | B | Context expressions are valid in job-level `env:` values; `REPO_NAME` becomes an OS environment variable accessible as `$REPO_NAME` in all steps of that job. | 06-Custom-Environment-Variables.md | Medium |
| 34 | B | Step-level `env:` variables are scoped to that step only. To persist across steps, write to `$GITHUB_ENV`. Step outputs (via `$GITHUB_OUTPUT`) are a different mechanism. | 06-Custom-Environment-Variables.md | Medium |
| 35 | B | `GITHUB_REF_NAME` provides the short ref name (e.g., `main`, `v1.2.3`) without the `refs/heads/` or `refs/tags/` prefix that `GITHUB_REF` includes. | 07-Default-Environment-Variables.md | Easy |
| 36 | B | `GITHUB_RUN_ATTEMPT` increments each time a run is re-run; combining `$GITHUB_RUN_ID-$GITHUB_RUN_ATTEMPT` creates a unique filename across all attempts. | 07-Default-Environment-Variables.md | Medium |
| 37 | B | Writing markdown to the file path in `$GITHUB_STEP_SUMMARY` causes that content to appear in the workflow run's summary page on GitHub. | 07-Default-Environment-Variables.md | Medium |
| 38 | B | `GITHUB_ACTOR` is set from the original event trigger (alice). `GITHUB_TRIGGERING_ACTOR` reflects who initiated the current run or re-run (bob). | 07-Default-Environment-Variables.md | Medium |
| 39 | B | The modern approach uses `echo "key=value" >> $GITHUB_OUTPUT`; the old `::set-output` command is deprecated and should no longer be used. | 07-Default-Environment-Variables.md | Medium |
| 40 | B | Required reviewers for deployment environments are configured at Repository → Settings → Environments → select the environment → Required reviewers section. | 08-Environment-Protection-Rules.md | Easy |
| 41 | B | "Named branches" with specific patterns (`main`, `release/*`) allows precise control; only commits from those branches can deploy to production. | 08-Environment-Protection-Rules.md | Medium |
| 42 | B | The wait timer counts from when the job was dispatched, not from when approval is granted. If dispatched at 2:00 PM with a 30-minute timer, deployment starts at 2:30 PM regardless of early approval. | 08-Environment-Protection-Rules.md | Medium |
| 43 | B | Setting `environment: name: staging url: https://staging.example.com` in the job configuration provides the URL that appears in the Deployments section. | 08-Environment-Protection-Rules.md | Medium |
| 44 | B | Reviewers have access to job logs, linked commits/PRs, test results, and can approve or reject with optional comments in the deployment review UI. | 08-Environment-Protection-Rules.md | Medium |
| 45 | C | The default retention period is 5 days (repository default, configurable by admins up to 90 days on free or up to 400 days on paid plans); no `retention-days:` key means 5-day default. | 09-Workflow-Artifacts.md | Easy |
| 46 | C | The standard cross-job artifact sharing pattern: upload in the producing job, declare `needs: build` in the consuming job, then download by name using `actions/download-artifact`. | 09-Workflow-Artifacts.md | Medium |
| 47 | B | Multiline `path:` with an exclusion line starting with `!` (e.g., `!dist/**/*.map`) uses the exclusion glob to skip `.map` files from the upload. | 09-Workflow-Artifacts.md | Medium |
| 48 | B | Artifacts count toward repository storage limits; they can be deleted manually via UI or API, and naturally expire based on the retention period. | 09-Workflow-Artifacts.md | Medium |
| 49 | B | `actions/download-artifact@v3` supports cross-run downloads when the `run-id` and artifact `name` are specified, making pre-built artifacts reusable across workflow runs. | 09-Workflow-Artifacts.md | Medium |
| 50 | B | Give the cache step an `id:`, then use `if: steps.<id>.outputs.cache-hit != 'true'` on the install step to skip installation when a full cache match was found. | 09-Workflow-Artifacts.md | Medium |
| 51 | C | GitHub Actions provides 5 GB of cache storage per repository. Caches exceeding this limit trigger LRU eviction. | 10-Workflow-Caching.md | Easy |
| 52 | B | `hashFiles('**/package-lock.json')` uses a glob pattern to hash ALL lock files anywhere in the repository; `runner.os` scopes the cache by OS. | 10-Workflow-Caching.md | Medium |
| 53 | B | With `restore-keys:`, the most recently accessed cache with the matching prefix is restored as a fallback. `cache-hit` is `false` (partial restore), prompting npm to install only new/changed packages. | 10-Workflow-Caching.md | Medium |
| 54 | B | GitHub evicts the least recently used caches when approaching 5 GB, and automatically removes caches not accessed for 7 days. | 10-Workflow-Caching.md | Medium |
| 55 | B | Caches are scoped per branch. Different branches cannot directly access each other's caches, except that branches can fall back to caches from the repository's default branch. | 10-Workflow-Caching.md | Medium |
| 56 | B | `hashFiles('requirements.txt', 'setup.py')` invalidates the cache when Python dependency files change; `runner.os` ensures OS-specific compiled extensions get separate caches. | 10-Workflow-Caching.md | Medium |
| 57 | B | `on: workflow_call:` is the trigger that designates a workflow as callable/reusable from other workflows. | 11-Workflow-Sharing.md | Easy |
| 58 | B | Reusable workflows are called at the job level using `uses:` (not in `steps:`); `with:` passes inputs and `secrets:` passes secrets. | 11-Workflow-Sharing.md | Medium |
| 59 | B | `secrets: inherit` passes all caller workflow secrets to the reusable workflow without explicit individual mapping — valid for same-org workflows. | 11-Workflow-Sharing.md | Medium |
| 60 | A | Reusable workflow outputs are surfaced through the `needs` context exactly like regular job outputs: `${{ needs.<job-id>.outputs.<output-name> }}`. | 11-Workflow-Sharing.md | Medium |
| 61 | B | Enterprise Required Workflows are configured in Enterprise Settings → Policies → Required workflows and run on all matching repositories regardless of repository-level Actions settings. | 11-Workflow-Sharing.md | Medium |
| 62 | B | `required: false` with `default: "18"` makes the input optional with a fallback value — callers can override or omit it. | 11-Workflow-Sharing.md | Medium |
| 63 | B | Adding repository secret `ACTIONS_STEP_DEBUG` with value `true` enables verbose step debug logging without modifying any workflow files. | 12-Workflow-Debugging.md | Easy |
| 64 | B | `echo "::debug::message"` emits a debug-level log entry that only appears when debug logging is enabled via `ACTIONS_STEP_DEBUG`. | 12-Workflow-Debugging.md | Medium |
| 65 | B | `RUNNER_DEBUG: 1` (as a repository secret) enables runner-level diagnostic output in addition to the step-level debug provided by `ACTIONS_STEP_DEBUG`. | 12-Workflow-Debugging.md | Medium |
| 66 | B | Expanded step logs are available by navigating to the workflow run in the Actions tab, selecting the job, and clicking the step to expand its full output. | 12-Workflow-Debugging.md | Medium |
| 67 | B | `echo "::error file=config.yaml,line=45::message"` creates a file-annotated error that appears prominently in the GitHub UI with file/line context. | 12-Workflow-Debugging.md | Medium |
| 68 | B | The GitHub REST API accepts Personal Access Tokens (PAT), GitHub App tokens, or OAuth tokens for authentication. | 13-Workflows-REST-API.md | Easy |
| 69 | B | `POST /repos/{owner}/{repo}/actions/workflows/{workflow_id}/dispatches` with `{"ref": "main", "inputs": {...}}` triggers a `workflow_dispatch` event via API. | 13-Workflows-REST-API.md | Medium |
| 70 | B | `GET /repos/{owner}/{repo}/actions/runs?status=failure` (or `conclusion=failure`) with date range filtering retrieves failed runs; the exact parameter is `status` for in-progress states and `conclusion` for completed runs. | 13-Workflows-REST-API.md | Medium |
| 71 | B | `POST /repos/{owner}/{repo}/actions/runs/{run_id}/cancel` sends a cancellation request for a specific workflow run. | 13-Workflows-REST-API.md | Medium |
| 72 | B | `POST /repos/{owner}/{repo}/actions/runs/{run_id}/rerun-failed-jobs` re-runs only the failed jobs in a completed workflow run without repeating successful jobs. | 13-Workflows-REST-API.md | Medium |
| 73 | B | Pending deployment reviews appear in Repository → Actions → [workflow run]; the paused job shows a "Review deployments" button for designated reviewers. | 14-Reviewing-Deployments.md | Easy |
| 74 | B | The deployment review interface shows job logs, commit/PR context, test results, and allows approving or rejecting with an optional comment. | 14-Reviewing-Deployments.md | Medium |
| 75 | B | With a requirement of 2 approvals, the workflow remains paused until the minimum reviewer count is reached; a single approval is insufficient. | 14-Reviewing-Deployments.md | Medium |
| 76 | B | The repository's Deployments section shows environment-specific deployment history including approver, branch, commit SHA, and links to the workflow run logs. | 14-Reviewing-Deployments.md | Medium |
| 77 | C | Environment-scoped secrets are stored under the specific environment (e.g., `production`) and are only injected into jobs that declare `environment: production`. | 14-Reviewing-Deployments.md | Medium |
| 78 | B | Every action repository must contain `action.yml` (or `action.yaml`) at the root — this metadata file defines the action's interface and runtime. | 15-Creating-Publishing-Actions.md | Easy |
| 79 | C | Composite actions use `runs: using: composite` with `steps:` that can include both `run:` shell commands and `uses:` references to other actions — no compilation or Docker needed, and startup is fast. | 15-Creating-Publishing-Actions.md | Medium |
| 80 | B | The `@actions/core` API: `getInput()` reads inputs, `info()` logs messages, `setOutput()` sets step outputs, and `setFailed()` fails the action with an error message. | 15-Creating-Publishing-Actions.md | Medium |
| 81 | B | `branding:` is a top-level `action.yml` key with `icon:` (Feather icon library name) and `color:` child keys for Marketplace appearance. | 15-Creating-Publishing-Actions.md | Medium |
| 82 | A | Docker action `action.yml` supports `entrypoint:` under the `runs:` key to override the Dockerfile's ENTRYPOINT or CMD for the action execution. | 15-Creating-Publishing-Actions.md | Medium |
| 83 | C | A full commit SHA is immutable — tags and branches can be moved, but a SHA always refers to exactly one commit, providing the strongest supply chain security guarantee. | 15-Creating-Publishing-Actions.md | Medium |
| 84 | B | Self-hosted runners are registered at Repository → Settings → Actions → Runners → New self-hosted runner, which provides the OS-specific setup commands. | 16-Managing-Runners.md | Easy |
| 85 | B | `runs-on: [self-hosted, gpu, high-memory]` requires the runner to have ALL listed labels; only runners tagged with both `gpu` and `high-memory` receive these jobs. | 16-Managing-Runners.md | Medium |
| 86 | B | `GITHUB_ACTIONS` is always set to `true` on both GitHub-hosted and self-hosted runners executing inside a workflow, making it the reliable detection variable. | 16-Managing-Runners.md | Medium |
| 87 | B | Public repositories allow anyone to fork and open PRs; `pull_request` workflows from forks execute on self-hosted runners, allowing untrusted fork code to run on the host system. | 16-Managing-Runners.md | Medium |
| 88 | B | Runner groups provide access control; runners assigned to a group are only accessible to repositories or organizations explicitly granted access to that group. | 16-Managing-Runners.md | Medium |
| 89 | C | Enterprise administrator settings have the highest authority; enterprise policies cascade down and override organization and repository settings. | 17-GitHub-Actions-Enterprise.md | Easy |
| 90 | B | "Allow select actions" enables an explicit allow list where specific patterns (`actions/*`, `github/*`, `specific-org/action@ref`) can be permitted while blocking everything else. | 17-GitHub-Actions-Enterprise.md | Medium |
| 91 | C | "Require approval for all outside collaborators" requires maintainer approval for ALL outside contributors' workflow runs, not just first-timers. | 17-GitHub-Actions-Enterprise.md | Medium |
| 92 | B | The secure two-workflow pattern keeps privileged `pull_request_target` context away from fork code entirely: fork code runs under `pull_request` (no secrets), results are uploaded as artifacts, then a separate `pull_request_target` workflow downloads and posts them. | 17-GitHub-Actions-Enterprise.md | Medium |
| 93 | B | Required workflows configured at the enterprise level run on all targeted repositories regardless of whether the repository owner has disabled Actions — this is by design for compliance enforcement. | 17-GitHub-Actions-Enterprise.md | Hard |
| 94 | B | `permissions: contents: write` grants release creation and pushes; `permissions: packages: write` grants package publishing. No other scopes are needed, following least privilege. | 18-Security-and-Optimization.md | Medium |
| 95 | B | OIDC eliminates long-lived static credentials; the workflow exchanges a GitHub-signed JWT for short-lived cloud credentials scoped to that run, which expire automatically after the job. | 18-Security-and-Optimization.md | Medium |
| 96 | B | PR titles are attacker-controlled inputs; interpolating them directly into shell commands enables injection (e.g., title `"; rm -rf / #`). Mitigation: assign to an env var first (`TITLE: ${{ github.event.pull_request.title }}`), then reference `$TITLE`. | 18-Security-and-Optimization.md | Hard |
| 97 | B | `GITHUB_TOKEN` is intentionally blocked from triggering new workflow runs to prevent infinite loops. When another workflow must be triggered, use a PAT or GitHub App token for the git operation. | 18-Security-and-Optimization.md | Medium |
| 98 | B | The error "could not read Username" indicates authentication failure — the `GITHUB_TOKEN` is missing, has insufficient permissions, or the `actions/checkout` step is not configured with an appropriate token. | 19-Common-Failures-Troubleshooting.md | Easy |
| 99 | B | `timeout-minutes:` is a job-level key that sets the maximum duration for a job; setting it to `300` allows 5 hours of execution. | 19-Common-Failures-Troubleshooting.md | Medium |
| 100 | B | `npm ci` uses the lock file exactly; when a transitive dependency releases a breaking version that conflicts with the lock file, `ERESOLVE` errors appear. Running `npm install` locally updates the lock file to resolve the conflict. | 19-Common-Failures-Troubleshooting.md | Hard |

---

*End of GH-200 Iteration 9 — 100 Questions*
