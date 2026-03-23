# GitHub Actions GH-200 Certification Exam — Question Bank (Iteration 5)

**Iteration**: 5

**Generated**: 2026-03-20

**Total Questions**: 100

**Difficulty Split**: 20 Easy / 60 Medium / 20 Hard

**Answer Types**: 56 `one` / 26 `many` / 11 `all` / 7 `none`

---

## Questions

---

### Question 1 — GitHub Actions VS Code Extension

**Difficulty**: Easy
**Answer Type**: one
**Topic**: GitHub Actions VS Code Extension

**Question**:
A developer wants to manually add the GitHub Actions YAML schema to VS Code because the extension is not auto-detecting their workflow files. They open `settings.json` to add it. Which configuration is correct?

- A) `"github.actions.schema": ".github/workflows/*.yml"`
- B) `"yaml.schemas": { "https://json.schemastore.org/github-workflow.json": ".github/workflows/*.yml" }`
- C) `"github.actions.validate": { "pattern": "*.yml", "schema": "github-workflow" }`
- D) `"files.associations": { ".github/workflows/*.yml": "github-actions" }`

---

### Question 2 — GitHub Actions VS Code Extension

**Difficulty**: Medium
**Answer Type**: one
**Topic**: GitHub Actions VS Code Extension

**Question**:
A workflow developer hovers over `uses: actions/setup-node@v4` in VS Code and sees action metadata. What specific information does the GitHub Actions extension display in this hover preview?

- A) The GitHub Marketplace download statistics and star rating for the action
- B) The action's `action.yml` content, including available inputs, outputs, and description
- C) A diff showing all changes introduced between the previous and current version
- D) The action's test coverage percentage and CI build status badge

---

### Question 3 — GitHub Actions VS Code Extension

**Difficulty**: Medium
**Answer Type**: many
**Topic**: GitHub Actions VS Code Extension

**Question**:
A developer is using the GitHub Actions VS Code extension while writing a new workflow. Which of the following problems will the extension detect and highlight before the workflow is pushed? *(Select all that apply.)*

- A) Using `${{ secrets.MY_TOKEN }}` in the workflow-level `env:` block, where `secrets` context is not supported
- B) Referencing the output of a step using `${{ steps.build.outputs.result }}` in a step that appears before the `build` step
- C) An invalid YAML indentation caused by mixing tabs and spaces
- D) A permission scope typo such as `content: write` instead of `contents: write` in a `permissions:` block
- E) Whether `secrets.MY_TOKEN` has been defined in the GitHub repository settings

---

### Question 4 — GitHub Actions VS Code Extension

**Difficulty**: Medium
**Answer Type**: none
**Topic**: GitHub Actions VS Code Extension

**Question**:
A developer lists several advanced features they believe the GitHub Actions VS Code extension provides. Which of the following does the extension actually support?

- A) Running workflow jobs locally inside a Docker container with results streamed back to the editor
- B) Automatically committing and pushing workflow changes to the remote repository on file save
- C) Displaying real-time streaming logs from in-progress workflow runs in the editor sidebar
- D) Synchronizing repository secrets between a local `.env` file and GitHub's secret settings

---

### Question 5 — GitHub Actions VS Code Extension

**Difficulty**: Hard
**Answer Type**: all
**Topic**: GitHub Actions VS Code Extension

**Question**:
A platform engineering team standardizes on the GitHub Actions VS Code extension. A senior engineer validates the team's understanding of the extension's behavior. Which of the following statements are all correct?

- A) The extension identifies workflow files using path patterns like `.github/workflows/*.yml` and applies GitHub's JSON schema for validation
- B) IntelliSense for `${{ github.* }}` shows all available properties from GitHub's official context documentation when typed inside a workflow expression
- C) The extension validates that `permissions:` blocks contain only recognized scope names and warns when unknown scopes are used
- D) If the YAML Language Support extension is not installed, schema validation features in the GitHub Actions extension may not function correctly

---

### Question 6 — Contextual Information

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contextual Information

**Question**:
A workflow step needs to print the unique numeric identifier for the current workflow run — one that remains constant across re-runs of the same run. Which expression provides this value?

- A) `${{ github.run_number }}`
- B) `${{ github.run_id }}`
- C) `${{ github.run_attempt }}`
- D) `${{ github.workflow_id }}`

---

### Question 7 — Contextual Information

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Contextual Information

**Question**:
A workflow builds a Docker image and tags it for a registry. The team needs to compose the image tag using both the branch name and the short commit SHA. Which context expressions provide these values correctly? *(Select all that apply.)*

- A) `${{ github.ref_name }}` — provides the branch or tag name without the `refs/heads/` prefix
- B) `${{ github.sha }}` — provides the full 40-character commit SHA
- C) `${{ github.ref_type }}` — provides whether the trigger was a `branch` or `tag`
- D) `${{ github.branch }}` — provides the branch name for all trigger events
- E) `${{ github.head_commit.id }}` — provides the short 7-character commit SHA

---

### Question 8 — Contextual Information

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Contextual Information

**Question**:
A DevOps engineer writes a step that prints comprehensive runner metadata to a job log for auditing. Which `runner` context properties are valid and available? *(Select all that apply.)*

- A) `runner.name` — the display name of the runner executing the job
- B) `runner.os` — the operating system (Linux, Windows, or macOS)
- C) `runner.arch` — the CPU architecture (X64, ARM64, etc.)
- D) `runner.memory` — the total RAM available on the runner in megabytes
- E) `runner.temp` — the path to the runner's temporary directory

---

### Question 9 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contextual Information

**Question**:
A workflow has two dependent jobs. The first job (`build`) produces an output named `version` via `$GITHUB_OUTPUT`. The second job (`deploy`) needs to consume that value. Which expression in the `deploy` job correctly accesses the `version` output from `build`?

- A) `${{ outputs.build.version }}`
- B) `${{ needs.build.outputs.version }}`
- C) `${{ jobs.build.outputs.version }}`
- D) `${{ steps.build.outputs.version }}`

---

### Question 10 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contextual Information

**Question**:
During a `pull_request` event, a step references `${{ github.base_ref }}`. What value does this expression return?

- A) The branch the pull request is being merged **into** (e.g., `main`)
- B) The branch the pull request was created **from** (e.g., `feature/login`)
- C) The full ref path of the base branch (e.g., `refs/heads/main`)
- D) The latest commit SHA of the base branch at the time the PR was opened

---

### Question 11 — Contextual Information

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Contextual Information

**Question**:
A matrix job uses `strategy.matrix` with `os: [ubuntu-latest, windows-latest]` and `node: [18, 20]`. Which expressions are valid and return expected values within a step of that matrix job? *(Select all that apply.)*

- A) `${{ matrix.os }}` — returns `ubuntu-latest` or `windows-latest` depending on the current combination
- B) `${{ strategy.job-total }}` — returns the total number of jobs generated by the matrix expansion (4 in this case)
- C) `${{ strategy.job-index }}` — returns the zero-based index of the current job in the matrix
- D) `${{ matrix.node }}` — returns `18` or `20` depending on the current combination
- E) `${{ matrix.os.version }}` — unpacks the OS string into major version components

---

### Question 12 — Context Availability Reference

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Context Availability Reference

**Question**:
In a multi-job workflow, the `needs` context allows a downstream job to access outputs from upstream jobs. At which workflow key is the `needs` context available for use?

- A) Only in `jobs.<job_id>.steps[*].run`
- B) In `jobs.<job_id>.if`, `jobs.<job_id>.steps[*].run`, and `jobs.<job_id>.outputs`
- C) Only at the top-level workflow `env:` block
- D) In `jobs.<job_id>.runs-on` for dynamic runner selection

---

### Question 13 — Context Availability Reference

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Context Availability Reference

**Question**:
A workflow author is writing a `jobs.<job_id>.if:` condition that must evaluate whether a secret is set and whether a previous job succeeded. Which contexts are available in a job-level `if:` condition? *(Select all that apply.)*

- A) `github` — to check event names, branch, actor, etc.
- B) `secrets` — to check whether specific secrets are defined
- C) `needs` — to check the outcome of jobs listed in `needs:`
- D) `runner` — to check the operating system of the assigned runner
- E) `vars` — to access repository and organization variables

---

### Question 14 — Context Availability Reference

**Difficulty**: Medium
**Answer Type**: all
**Topic**: Context Availability Reference

**Question**:
A workflow author needs to understand where `env` context and `secrets` context are and are not available. Which of the following statements are all correct regarding context availability restrictions?

- A) The `secrets` context is not available at the workflow-level `env:` block
- B) The `env` context is not available in the workflow-level `env:` block itself (to prevent circular references)
- C) The `matrix` context is only available within the job that defines the matrix strategy
- D) The `steps` context is only available within the job where those steps execute

---

### Question 15 — Context Availability Reference

**Difficulty**: Medium
**Answer Type**: none
**Topic**: Context Availability Reference

**Question**:
A workflow author reviews a list of contexts and claims all of them are available in the `jobs.<job_id>.steps[*].uses:` key for dynamic action selection. Which of these contexts is actually available in the `uses:` key?

- A) `secrets` — to select a private action stored in a secret variable
- B) `env` — to dynamically select an action based on an environment variable
- C) `matrix` — to choose a different action per matrix combination
- D) `needs` — to select an action based on the output of a dependency job

---

### Question 16 — Context Availability Reference

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Context Availability Reference

**Question**:
Which special functions are available in `jobs.<job_id>.if:` conditions but not in most other workflow keys?

- A) `hashFiles()` and `toJSON()`
- B) `always()`, `success()`, `failure()`, and `cancelled()`
- C) `format()` and `join()`
- D) `contains()` and `startsWith()`

---

### Question 17 — Workflow File Structure

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Workflow File Structure

**Question**:
A workflow uses `strategy.matrix` with `fail-fast` not explicitly set. A developer asks what happens when one matrix job fails. Which behavior is the default?

- A) All other matrix jobs continue running regardless of failures
- B) All in-progress and queued matrix jobs are cancelled when any matrix job fails
- C) Only jobs sharing the same `os` value as the failing job are cancelled
- D) The failed job is automatically retried three times before other jobs are affected

---

### Question 18 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Workflow File Structure

**Question**:
A workflow defines a `concurrency:` group set to `${{ github.workflow }}-${{ github.ref }}` with `cancel-in-progress: true`. Three developers push to the same branch in rapid succession. Which statements correctly describe the resulting behavior? *(Select all that apply.)*

- A) The first run begins executing immediately
- B) The second run cancels the first run and begins executing
- C) The third run cancels the second run and begins executing
- D) All three runs queue up and execute sequentially
- E) Runs on different branches are not affected by this concurrency group

---

### Question 19 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: all
**Topic**: Workflow File Structure

**Question**:
A team is reviewing the `defaults:` section of a workflow. Which of the following statements about `defaults.run` are all correct?

- A) `defaults.run.shell` sets the default shell for all `run:` steps in the workflow unless overridden at the job or step level
- B) `defaults.run.working-directory` sets the default working directory for all `run:` steps
- C) An individual step can override `defaults.run.shell` by specifying its own `shell:` key
- D) `defaults.run` applies only to steps that use `run:`, not to `uses:` action steps

---

### Question 20 — Workflow File Structure

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Workflow File Structure

**Question**:
A matrix job includes `include:` entries to add extra combinations. The base matrix is `os: [ubuntu-latest, windows-latest]` and `node: [18, 20]`. An `include:` entry specifies `os: ubuntu-latest, node: 20, experimental: true`. Which behavior correctly describes what `include:` produces in this case?

- A) A new combination is added: `{os: ubuntu-latest, node: 20, experimental: true}` — replacing the existing `{ubuntu-latest, 20}` combination
- B) The properties from the `include:` entry are merged into the existing `{ubuntu-latest, 20}` combination, adding `experimental: true` to it
- C) The `include:` entry is rejected because `ubuntu-latest + node:20` already exists in the base matrix
- D) The `experimental` key is ignored unless it is declared in the base matrix

---

### Question 21 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow File Structure

**Question**:
A workflow has jobs `build`, `test`, and `deploy`. The `deploy` job specifies `needs: [build, test]`. What must be true before `deploy` starts executing?

- A) Both `build` and `test` must have completed successfully
- B) Either `build` or `test` must have completed successfully
- C) `build` must succeed, but `test` can be skipped
- D) The jobs run in sequence based on the order they appear in the file

---

### Question 22 — Workflow Trigger Events

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Workflow Trigger Events

**Question**:
A team wants to allow manually triggering a workflow from the GitHub Actions UI while requiring the user to select a deployment target (dev, staging, or prod). Which trigger configuration enables this?

- A) `on: push` with a branch filter
- B) `on: workflow_dispatch` with `inputs:` of type `choice`
- C) `on: repository_dispatch` with event type filtering
- D) `on: schedule` with a cron pattern

---

### Question 23 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow Trigger Events

**Question**:
A workflow is triggered by `pull_request_target`. How does this event differ from using `pull_request` when a fork submits a PR?

- A) `pull_request_target` runs with read-only token and no access to secrets, identical to `pull_request`
- B) `pull_request_target` runs in the context of the base repository with write token and access to secrets, even for fork PRs
- C) `pull_request_target` only triggers for PRs from the same repository, while `pull_request` also handles forks
- D) `pull_request_target` automatically rejects PRs from forked repositories

---

### Question 24 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Workflow Trigger Events

**Question**:
A developer is configuring path-based filtering for workflow triggers. Which trigger events support the `paths:` or `paths-ignore:` filter? *(Select all that apply.)*

- A) `push`
- B) `pull_request`
- C) `workflow_dispatch`
- D) `pull_request_target`
- E) `schedule`

---

### Question 25 — Workflow Trigger Events

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Workflow Trigger Events

**Question**:
A repository has two workflow files. Workflow A triggers on `push` to `main`. Workflow B triggers on `push` to `main` with `paths-ignore: ['*.md', 'docs/**']`. A developer pushes a commit that changes only `README.md`. Which workflows run?

- A) Both Workflow A and Workflow B run
- B) Only Workflow A runs; Workflow B is skipped because of the path filter
- C) Neither workflow runs because markdown changes are excluded by default
- D) Both workflows are queued but Workflow B is auto-cancelled before it starts

---

### Question 26 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow Trigger Events

**Question**:
A DevOps team has a deployment workflow that should only trigger after another workflow named "Build and Test" completes successfully. Which trigger event and configuration achieves this?

- A) `on: push` with a branch filter for `main`
- B) `on: workflow_run` with `workflows: ["Build and Test"]` and `types: [completed]`, plus a job-level `if:` checking `github.event.workflow_run.conclusion == 'success'`
- C) `on: workflow_call` referencing the build workflow as a dependency
- D) `on: repository_dispatch` with `event-types: [build-completed]`

---

### Question 27 — Workflow Trigger Events

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Workflow Trigger Events

**Question**:
A developer wants to understand which trigger event types support the `types:` activity filter. Which events can be filtered using `types:`? *(Select all that apply.)*

- A) `pull_request`
- B) `push`
- C) `issues`
- D) `workflow_dispatch`
- E) `release`
- F) `label`

---

### Question 28 — Custom Environment Variables

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Custom Environment Variables

**Question**:
A workflow defines `MY_VAR: global` at the workflow level, and a specific job re-defines `MY_VAR: job-level`. Inside that job, a step also defines `MY_VAR: step-level`. When the step's `run:` command echoes `$MY_VAR`, what value is printed?

- A) `global`
- B) `job-level`
- C) `step-level`
- D) An error because the variable is defined multiple times

---

### Question 29 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Custom Environment Variables

**Question**:
A workflow step appends a value to the `$GITHUB_ENV` file to share a variable with subsequent steps. What is the correct syntax for setting `DEPLOY_VERSION=1.2.3` for all following steps?

- A) `echo "export DEPLOY_VERSION=1.2.3" >> $GITHUB_ENV`
- B) `echo "DEPLOY_VERSION=1.2.3" >> $GITHUB_ENV`
- C) `echo "set DEPLOY_VERSION=1.2.3" >> $GITHUB_ENV`
- D) `GITHUB_ENV["DEPLOY_VERSION"]="1.2.3"`

---

### Question 30 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: all
**Topic**: Custom Environment Variables

**Question**:
An organization wants to pass a secret into a container-based deployment step securely. A DevOps engineer proposes several approaches. Which of the following are all valid methods for making a secret available to a `run:` step?

- A) Defining it under the step's `env:` block: `env: { API_KEY: "${{ secrets.API_KEY }}" }`
- B) Defining it under the job's `env:` block: `env: { API_KEY: "${{ secrets.API_KEY }}" }` (available to all steps in the job)
- C) Defining it under the workflow-level `env:` block: `env: { API_KEY: "${{ secrets.API_KEY }}" }`
- D) Passing it directly to a shell command using `run: ./deploy.sh ${{ secrets.API_KEY }}`

---

### Question 31 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Custom Environment Variables

**Question**:
A workflow step needs to mask a dynamically computed value from appearing in future log output. The value is retrieved from an external API at runtime. Which workflow command achieves this?

- A) `echo "::set-secret::$DYNAMIC_VALUE"`
- B) `echo "::add-mask::$DYNAMIC_VALUE"`
- C) `echo "::redact::$DYNAMIC_VALUE"`
- D) `echo "MASK=$DYNAMIC_VALUE" >> $GITHUB_ENV`

---

### Question 32 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: all
**Topic**: Custom Environment Variables

**Question**:
An engineering team is reviewing environment variable scope rules in GitHub Actions. Which of the following statements about env var scoping are all correct?

- A) A step-level `env:` variable is not visible to other steps in the same job
- B) A job-level `env:` variable is visible to all steps within that job but not to other jobs
- C) A workflow-level `env:` variable is visible to all jobs and steps in that workflow
- D) When the same variable name is defined at multiple levels, the most specific (innermost) scope wins

---

### Question 33 — Default Environment Variables

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Default Environment Variables

**Question**:
A script running inside a GitHub Actions workflow needs to read the full path to the JSON file containing the webhook event payload that triggered the current run. Which default environment variable provides this path?

- A) `GITHUB_WORKSPACE`
- B) `GITHUB_EVENT_PATH`
- C) `RUNNER_TEMP`
- D) `GITHUB_EVENT_NAME`

---

### Question 34 — Default Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Default Environment Variables

**Question**:
A step uses `$GITHUB_OUTPUT` to pass a value to subsequent steps, while another step uses `$GITHUB_ENV` to set a variable. What is the fundamental difference between these two mechanisms?

- A) `$GITHUB_OUTPUT` sets variables only for the current step; `$GITHUB_ENV` sets variables for all subsequent steps
- B) `$GITHUB_OUTPUT` creates step outputs accessible via `steps.<id>.outputs.<name>` in the same job; `$GITHUB_ENV` creates environment variables available as `$VAR_NAME` to all subsequent steps in the same job
- C) `$GITHUB_OUTPUT` is available across jobs using `needs.outputs`; `$GITHUB_ENV` is only available within the same step
- D) They are interchangeable; both achieve the same result through different file formats

---

### Question 35 — Default Environment Variables

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Default Environment Variables

**Question**:
A CI script audits which GitHub-provided default environment variables are available for repository and commit identification. Which of the following are valid GitHub-provided default variables? *(Select all that apply.)*

- A) `GITHUB_REPOSITORY` — the full owner/repo name
- B) `GITHUB_SHA` — the commit SHA that triggered the workflow
- C) `GITHUB_BRANCH` — the current branch name without the refs/heads/ prefix
- D) `GITHUB_REF_NAME` — the branch or tag name
- E) `GITHUB_REPOSITORY_OWNER` — the repository owner's login

---

### Question 36 — Default Environment Variables

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Default Environment Variables

**Question**:
A workflow step writes a multi-line value to `$GITHUB_OUTPUT` using a heredoc delimiter to avoid parsing issues with embedded newlines and special characters. Which is the correct syntax?

**A.**
```
echo "NOTES<<EOF" >> $GITHUB_OUTPUT
echo "Line 1" >> $GITHUB_OUTPUT
echo "Line 2" >> $GITHUB_OUTPUT
echo "EOF" >> $GITHUB_OUTPUT
```
**B.**
```
echo "NOTES=Line 1\nLine 2" >> $GITHUB_OUTPUT
```
**C.**
```
GITHUB_OUTPUT+="NOTES=Line 1\nLine 2"
```
**D.**
```
printf "NOTES|Line 1|Line 2" >> $GITHUB_OUTPUT
```

---

### Question 37 — Default Environment Variables

**Difficulty**: Medium
**Answer Type**: none
**Topic**: Default Environment Variables

**Question**:
A workflow developer lists default environment variables they believe GitHub automatically provides in every run. Which of the following is actually a GitHub-provided default environment variable?

- A) `GITHUB_STEP_NUMBER` — the sequential number of the current step within the job
- B) `GITHUB_JOB_STATUS` — the current exit status of the job (success or failure)
- C) `RUNNER_MEMORY_MB` — the total RAM available on the runner in megabytes
- D) `GITHUB_COMMIT_MESSAGE` — the commit message of the commit that triggered the workflow

---

### Question 38 — Environment Protection Rules

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Environment Protection Rules

**Question**:
An organization configures a `wait timer` of 60 minutes on the `production` environment. A deployment workflow is triggered and the `deploy` job references `environment: production`. What happens when the job reaches the environment gate?

- A) The job fails immediately with a timeout error
- B) The job pauses for 60 minutes before proceeding, even if a reviewer has already approved
- C) GitHub emails the reviewers and the job starts executing while waiting for their response
- D) The 60-minute wait timer applies only after a reviewer approves the deployment

---

### Question 39 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Environment Protection Rules

**Question**:
A production deployment workflow is paused awaiting approval. A required reviewer logs into GitHub, examines the deployment logs and diff, and clicks **Reject**. Which outcome occurs?

- A) The workflow is paused indefinitely until another reviewer approves
- B) The workflow run fails and the deployment does not proceed
- C) The deployment is skipped but the workflow continues with remaining steps
- D) The workflow run is retried automatically with a fresh approval request

---

### Question 40 — Environment Protection Rules

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Environment Protection Rules

**Question**:
An enterprise wants only commits from the `main` branch or branches matching `release/*` to be deployable to the `production` environment. A developer on a `hotfix/login-bug` branch triggers a deployment workflow. Given these deployment branch restrictions, what happens?

- A) The deployment proceeds because hotfix branches are implicitly trusted
- B) The deployment pauses and waits for an admin to manually override the branch restriction
- C) The entire workflow run fails when it reaches the job referencing `environment: production`
- D) GitHub redirects the deployment to the `staging` environment instead

---

### Question 41 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Environment Protection Rules

**Question**:
A team defines environment protection rules for a `staging` environment. Which statements about how environment secrets differ from repository secrets are correct? *(Select all that apply.)*

- A) Environment secrets are only accessible to jobs that reference the specific environment
- B) If a repository secret and an environment secret share the same name, the environment secret takes precedence for jobs using that environment
- C) Environment secrets are available to all jobs in the workflow regardless of which environment they reference
- D) Environment secrets can only be accessed after a required reviewer approves the deployment
- E) Repository-level secrets are available to all jobs that do not reference a protected environment

---

### Question 42 — Environment Protection Rules

**Difficulty**: Hard
**Answer Type**: all
**Topic**: Environment Protection Rules

**Question**:
A compliance officer reviews GitHub environment protection rules for an enterprise deployment pipeline. Which of the following statements about required reviewers are all correct?

- A) Up to 6 users or teams can be configured as required reviewers for an environment
- B) If multiple reviewers are configured, any one of them can approve the deployment (it is not an all-must-approve requirement by default)
- C) A reviewer who triggered the workflow cannot approve their own deployment
- D) The required reviewer setting prevents the deployment job from executing until the review is completed

---

### Question 43 — Workflow Artifacts

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Workflow Artifacts

**Question**:
A CI workflow uploads a build artifact without specifying `retention-days`. How long will GitHub retain the artifact by default?

- A) 1 day
- B) 5 days
- C) 30 days
- D) 90 days

---

### Question 44 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow Artifacts

**Question**:
A `build` job uploads an artifact named `app-package` containing the compiled `dist/` directory. A downstream `deploy` job needs to access the compiled files. Which action and configuration correctly retrieves the artifact?

**A.**
```yaml
uses: actions/download-artifact@v3
with:
  name: app-package
  path: ./downloaded/
```
**B.**
```yaml
uses: actions/checkout@v3
with:
  artifact: app-package
```
**C.**
```yaml
uses: actions/restore-artifact@v1
with:
  artifact-name: app-package
```
**D.**
```yaml
uses: actions/get-artifact@v3
with:
  artifact-id: ${{ needs.build.outputs.artifact-id }}
```

---

### Question 45 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Workflow Artifacts

**Question**:
A workflow produces multiple test result files in different directories. An engineer configures `actions/upload-artifact` with path patterns. Which patterns are valid for the `path:` input? *(Select all that apply.)*

- A) `dist/**/*.js` — uploads all `.js` files recursively under `dist/`
- B) A multi-line YAML block listing multiple paths, one per line
- C) `!dist/**/*.map` — excludes all source map files from the uploaded artifact
- D) `results/*.xml` — uploads all XML files in the `results/` directory
- E) `*` — uploads all files matching any name in any directory

---

### Question 46 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow Artifacts

**Question**:
A developer wants to download an artifact that was produced by a different workflow run (not the current run) — specifically run ID `9876543210`. Which `actions/download-artifact` configuration achieves this?

**A.**
```yaml
uses: actions/download-artifact@v3
with:
  name: build-dist
  run-id: 9876543210
```
**B.**
```yaml
uses: actions/download-artifact@v3
with:
  name: build-dist
  github-token: ${{ secrets.GITHUB_TOKEN }}
  run-id: 9876543210
```
**C.**
```yaml
uses: actions/download-artifact@v3
with:
  artifact-id: 9876543210
```
**D.**
```yaml
uses: actions/fetch-artifact@v1
with:
  workflow-run-id: 9876543210
```

---

### Question 47 — Workflow Artifacts

**Difficulty**: Hard
**Answer Type**: none
**Topic**: Workflow Artifacts

**Question**:
A workflow engineer lists artifact behaviors they believe are accurate. Which of the following is actually a correct statement about GitHub Actions artifacts?

- A) Artifacts are automatically available to all jobs in the same workflow run without any download step
- B) Two artifact upload steps can use the same artifact name in the same workflow run without any issue
- C) Artifacts created in one repository are automatically shared with all repositories in the same organization
- D) Artifacts are retained indefinitely unless the repository is deleted

---

### Question 48 — Workflow Caching

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Workflow Caching

**Question**:
A Node.js workflow uses `actions/cache` with a key that includes `hashFiles('**/package-lock.json')`. When does the cache miss and trigger a fresh dependency installation?

- A) Every time the workflow runs, because cache is never hit on first run
- B) When `package-lock.json` changes (producing a different hash)
- C) Every 24 hours, because cache keys include a timestamp
- D) Only when the `node_modules` directory is manually deleted from the runner

---

### Question 49 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow Caching

**Question**:
A `actions/cache` step is configured with a primary key and two `restore-keys`. On a run where the primary key produces a cache miss, what occurs?

- A) The workflow fails because the cache is required and unavailable
- B) The `restore-keys` entries are tried in order; if a partial match is found, that cache is restored as a starting point and the new cache is saved at the end of the run with the primary key
- C) The `restore-keys` are ignored; only an exact primary key match triggers a cache restore
- D) The step silently skips and the job proceeds without any cache, with nothing saved at the end

---

### Question 50 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Workflow Caching

**Question**:
A platform engineer audits the caching strategy for a large monorepo. Which statements about GitHub Actions caching behavior are correct? *(Select all that apply.)*

- A) The cache storage limit is 5 GB per repository
- B) Caches expire and are evicted after 7 days without access
- C) A cache key created on a feature branch is accessible by workflows running on any other branch
- D) `actions/setup-node` with `cache: 'npm'` provides built-in caching without a separate `actions/cache` step
- E) Multiple jobs within the same workflow run can read from the same cache key simultaneously

---

### Question 51 — Workflow Caching

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Workflow Caching

**Question**:
A workflow caches Python dependencies using the key `${{ runner.os }}-pip-${{ hashFiles('**/requirements.txt') }}`. A new dependency is added to `requirements.txt`. On the next run, the cache step produces a miss. The `restore-keys` is `${{ runner.os }}-pip-`. Describe the complete cache behavior for this run.

- A) The run fails because the dependency cache is stale and cannot be partially restored
- B) The previous cache (matched by `restore-keys`) is restored, `pip install` runs and downloads only new packages, and at job end a new cache entry is saved with the updated primary key
- C) The entire `pip install` runs from scratch downloading all packages, and the new cache is saved with the updated key, but no partial restore occurs
- D) The `restore-keys` match causes a full cache hit; no new packages are installed and no new cache is saved

---

### Question 52 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow Caching

**Question**:
A `build` job uses `actions/cache` and needs to check whether the cache was restored (cache hit) to decide whether to skip dependency installation. Which step output is used to check this?

- A) `steps.<cache-step-id>.outputs.cache-restored`
- B) `steps.<cache-step-id>.outputs.cache-hit`
- C) `steps.<cache-step-id>.conclusion`
- D) `env.CACHE_HIT`

---

### Question 53 — Workflow Sharing

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Workflow Sharing

**Question**:
A DevOps team wants to create a reusable workflow that other teams can call from their own workflows. Which `on:` trigger must be added to make a workflow callable?

- A) `on: workflow_dispatch`
- B) `on: workflow_call`
- C) `on: repository_dispatch`
- D) `on: workflow_run`

---

### Question 54 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow Sharing

**Question**:
A calling workflow passes secrets to a reusable workflow using `secrets: inherit`. What behavior does this produce?

- A) Only secrets explicitly declared in the reusable workflow's `on.workflow_call.secrets:` block are forwarded
- B) All secrets from the calling workflow's context are automatically available to the reusable workflow using their original names
- C) The reusable workflow gains access to organizational secrets only, not repository secrets
- D) `secrets: inherit` is equivalent to passing `secrets: {}` — no secrets are forwarded

---

### Question 55 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: all
**Topic**: Workflow Sharing

**Question**:
A reusable workflow author is designing the `on.workflow_call.inputs:` section. Which input type definitions are all valid and supported?

- A) `type: string` — for text values passed from the caller
- B) `type: boolean` — for true/false flags
- C) `type: number` — for numeric values
- D) `type: environment` — for selecting a deployment environment

---

### Question 56 — Workflow Sharing

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Workflow Sharing

**Question**:
A reusable workflow needs to expose an output value so the calling workflow can use it in a downstream job. The value originates in a step (`id: compute`) within the reusable workflow's job (`job_id: build`). Tracing the complete output chain, which declarations are required in the correct order?

- A) Step writes to `$GITHUB_OUTPUT` ? job declares `outputs.version: ${{ steps.compute.outputs.value }}` ? `on.workflow_call.outputs.version: value: ${{ jobs.build.outputs.version }}`
- B) Step writes to `$GITHUB_OUTPUT` ? the caller accesses it directly via `needs.<job_id>.steps.compute.outputs.value`
- C) Step writes to `$GITHUB_ENV` ? job exports it ? caller reads it via `env.version`
- D) Step writes to `$GITHUB_OUTPUT` ? the reusable workflow exposes it via `outputs.version: ${{ steps.compute.outputs.value }}` (no job intermediary needed)

---

### Question 57 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow Sharing

**Question**:
An enterprise configures a **required workflow** that must run on all repositories matching a specific filter. If the target repository has GitHub Actions disabled by a repository admin, what happens to the required workflow?

- A) The required workflow is also disabled and does not run
- B) The required workflow still runs because enterprise-level required workflows override repository-level Actions settings
- C) The required workflow runs but its results are not surfaced in PR checks
- D) GitHub displays an error and the PR cannot be merged until Actions is re-enabled

---

### Question 58 — Workflow Sharing

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Workflow Sharing

**Question**:
An organization is evaluating whether to use `secrets: inherit` or explicit secret mapping when calling reusable workflows. Which statements correctly describe the trade-offs? *(Select all that apply.)*

- A) `secrets: inherit` passes ALL caller secrets automatically without requiring them to be declared in the called workflow's `on.workflow_call.secrets:` block
- B) Explicit secret mapping offers higher security because only named secrets flow from caller to called workflow
- C) `secrets: inherit` is recommended for public or third-party reusable workflows
- D) With explicit mapping, the called workflow must declare each secret in its `on.workflow_call.secrets:` block to receive it
- E) Explicit mapping requires more maintenance because secrets must be updated in the calling workflow whenever the called workflow adds new secrets

---

### Question 59 — Workflow Debugging

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Workflow Debugging

**Question**:
A developer re-runs a failed workflow and wants to enable verbose step-level debug logging for diagnostic output. Which repository secret must be set to enable this behavior?

- A) `ACTIONS_STEP_DEBUG: true`
- B) `RUNNER_DEBUG: 1`
- C) `DEBUG_MODE: enabled`
- D) `GITHUB_ACTIONS_DEBUG: true`

---

### Question 60 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Workflow Debugging

**Question**:
A developer uses GitHub Actions workflow commands inside `run:` steps for diagnostic output. Which commands produce output visible as annotations in the GitHub PR checks interface or workflow summary? *(Select all that apply.)*

- A) `echo "::notice::Deployment succeeded to staging"`
- B) `echo "::warning::Deprecated API used in module X"`
- C) `echo "::debug::Processing file $FILE"`
- D) `echo "::error::Build failed due to missing dependency"`
- E) `echo "::group::Installation steps"`

---

### Question 61 — Workflow Debugging

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Workflow Debugging

**Question**:
A CI engineer troubleshoots a workflow that behaves differently in CI than on the developer's local machine. Which debugging techniques are available natively in GitHub Actions without installing third-party tools? *(Select all that apply.)*

- A) Setting the `ACTIONS_STEP_DEBUG` secret to `true` to enable verbose runner and step-level output
- B) Using `::group::` and `::endgroup::` commands to collapse and expand output sections in the log viewer
- C) SSH-ing into the runner during job execution using an interactive tmate session intrinsically built into GitHub-hosted runners
- D) Adding a step that prints `env` and `set` to display all environment variables available at that point
- E) Using `echo "::debug::message"` to emit messages that appear only when debug logging is enabled

---

### Question 62 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: all
**Topic**: Workflow Debugging

**Question**:
A senior engineer reviews debugging best practices with a junior team. Which statements about GitHub Actions workflow commands are all correct?

- A) `::debug::` messages are hidden in standard log output and only visible when `ACTIONS_STEP_DEBUG=true` is set
- B) `::notice::` messages create a visible annotation in the GitHub UI and appear in the PR checks detail view
- C) `::set-output::` has been deprecated in favor of writing to `$GITHUB_OUTPUT`
- D) `::group::` and `::endgroup::` create collapsible log sections in the Actions run view

---

### Question 63 — Workflows REST API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Workflows REST API

**Question**:
An external monitoring tool needs to retrieve all workflow runs for a specific repository, filtering only for runs that are currently in progress. Which API endpoint and query parameter accomplishes this?

- A) `GET /repos/{owner}/{repo}/actions/workflows` with `?status=in_progress`
- B) `GET /repos/{owner}/{repo}/actions/runs` with `?status=in_progress`
- C) `GET /repos/{owner}/{repo}/actions/jobs` with `?filter=active`
- D) `GET /repos/{owner}/{repo}/actions/runs` with `?conclusion=in_progress`

---

### Question 64 — Workflows REST API

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Workflows REST API

**Question**:
A CI/CD script uses the REST API to trigger a `workflow_dispatch` workflow. Which of the following are required in the request body? *(Select all that apply.)*

- A) `ref` — the branch or tag name to run the workflow on
- B) `inputs` — the workflow input values (required only if the workflow defines required inputs)
- C) `workflow_id` — included in the URL path, not the request body
- D) `event_type` — required for `workflow_dispatch` events
- E) `sha` — the specific commit SHA to run the workflow against

---

### Question 65 — Workflows REST API

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Workflows REST API

**Question**:
A team audits API permissions for a workflow management script. Which REST API operations require write permissions on the `actions` scope? *(Select all that apply.)*

- A) Listing workflow runs (`GET /repos/{owner}/{repo}/actions/runs`)
- B) Cancelling a workflow run (`POST /repos/{owner}/{repo}/actions/runs/{run_id}/cancel`)
- C) Re-running a failed workflow (`POST /repos/{owner}/{repo}/actions/runs/{run_id}/rerun`)
- D) Triggering a workflow dispatch (`POST /repos/{owner}/{repo}/actions/workflows/{workflow_id}/dispatches`)
- E) Getting a specific workflow run (`GET /repos/{owner}/{repo}/actions/runs/{run_id}`)

---

### Question 66 — Workflows REST API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Workflows REST API

**Question**:
A deployment automation script must list only workflow runs that failed due to a test error (conclusion = `failure`) and were triggered by the `push` event, paginating 5 results per page. Which URL correctly constructs this query against the GitHub REST API?

- A) `GET /repos/{owner}/{repo}/actions/runs?status=failure&event=push&per_page=5`
- B) `GET /repos/{owner}/{repo}/actions/runs?conclusion=failure&event=push&per_page=5`
- C) `GET /repos/{owner}/{repo}/actions/runs?result=failure&trigger=push&page_size=5`
- D) `GET /repos/{owner}/{repo}/actions/runs?outcome=failure&on=push&limit=5`

---

### Question 67 — Workflows REST API

**Difficulty**: Medium
**Answer Type**: none
**Topic**: Workflows REST API

**Question**:
A developer lists workflow run `status` values they believe are valid for filtering via the GitHub REST API. Which of the following is actually a valid workflow run `status` query value?

- A) `running`
- B) `pending`
- C) `paused`
- D) `scheduled`

---

### Question 68 — Reviewing Deployments

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Reviewing Deployments

**Question**:
A production deployment to the `production` environment is awaiting review. Which users can approve or reject this deployment?

- A) Any GitHub user with write access to the repository
- B) Only the users or teams explicitly configured as required reviewers for the `production` environment
- C) Only repository admins and organization owners
- D) The person who triggered the workflow run, plus any designated reviewer

---

### Question 69 — Reviewing Deployments

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reviewing Deployments

**Question**:
A reviewer receives a notification to approve a deployment. They navigate to the GitHub Actions run view. What information is available to the reviewer to help make the approval decision?

- A) Only the deployment environment name and the branch that triggered the workflow
- B) The job execution logs (up to the point of the approval gate), the git diff of the triggering commit, and the actor who initiated the run
- C) A rendered diff of all changes from the last approved deployment to the current commit
- D) The test coverage report and performance benchmarks from the CI jobs

---

### Question 70 — Reviewing Deployments

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Reviewing Deployments

**Question**:
An environment protection rule for `production` requires one reviewer from the `platform-team` group and has a 10-minute wait timer. A reviewer approves the deployment within 2 minutes of it being queued. Which outcomes correctly describe what happens? *(Select all that apply.)*

- A) The deployment proceeds immediately after the reviewer's approval since the approval is the primary gate
- B) After the reviewer approves, the workflow still waits the full 10-minute timer before deploying
- C) The reviewer's approval and the wait timer are independent; both must be satisfied before deployment can proceed
- D) The wait timer begins counting down from the moment the workflow run starts, not from when the reviewer approves
- E) If the reviewer approves before the timer expires, the deployment begins as soon as both the approval and the timer requirement are met

---

### Question 71 — Reviewing Deployments

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Reviewing Deployments

**Question**:
An organization uses deployment reviews for multiple environments. Which statements about the deployment review process are correct? *(Select all that apply.)*

- A) The workflow run is paused — no steps in the deployment job execute until the review is approved
- B) Reviewers can see the workflow run logs from the jobs completed before the approval gate
- C) If the reviewer rejects the deployment, the workflow run status is set to `failure`
- D) A reviewer who initiated the workflow cannot approve their own deployment
- E) Deployment reviews apply only to GitHub Enterprise Cloud customers

---

### Question 72 — Reviewing Deployments

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Reviewing Deployments

**Question**:
A team configures the `production` environment to require 2 approvals (two different users from `platform-team`). The first reviewer approves the deployment. The second reviewer reviews and rejects it with a comment. What is the final state of the workflow run?

- A) The deployment proceeds because the first approval is already recorded and satisfies half the requirement
- B) The workflow pauses and requests re-approval from a different reviewer to replace the rejection
- C) The workflow run is marked as failed; the deployment does not proceed
- D) The deployment is put into a `pending` state awaiting admin intervention

---

### Question 73 — Creating and Publishing Actions

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Creating and Publishing Actions

**Question**:
A developer creates a new custom GitHub Action. Which file is required at the root of the action's repository to define the action's metadata, inputs, outputs, and runtime?

- A) `workflow.yml`
- B) `action.yml` (or `action.yaml`)
- C) `Dockerfile`
- D) `package.json`

---

### Question 74 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: 74

**Question**:
A developer creates a composite action that chains multiple shell and action steps together. Which `runs:` configuration is required in `action.yml` to define it as a composite action?

**A.**
```yaml
runs:
  using: composite
  steps:
    - run: echo "Hello"
      shell: bash
```
**B.**
```yaml
runs:
  using: node20
  main: index.js
```
**C.**
```yaml
runs:
  using: docker
  image: Dockerfile
```
**D.**
```yaml
runs:
  using: shell
  steps:
    - run: echo "Hello"
```

---

### Question 75 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Creating and Publishing Actions

**Question**:
A team compares JavaScript actions to Docker container actions for a new internal action they are building. Which statements correctly distinguish them? *(Select all that apply.)*

- A) JavaScript actions execute directly on the runner without a container and start faster than Docker actions
- B) Docker container actions can use any language or tool that can run inside a Linux container
- C) JavaScript actions require specifying `using: node20` (or another Node.js LTS version) in `action.yml`
- D) Docker container actions always run on Ubuntu runners and cannot be used on Windows or macOS GitHub-hosted runners
- E) JavaScript actions use the `@actions/core` and `@actions/github` npm packages to interact with GitHub

---

### Question 76 — Creating and Publishing Actions

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Creating and Publishing Actions

**Question**:
A JavaScript action encounters a critical validation error and needs to immediately mark the workflow job as failed. Which `@actions/core` function achieves this and halts further step execution?

- A) `core.error("Validation failed")`
- B) `core.setFailed("Validation failed")`
- C) `core.abort("Validation failed")`
- D) `process.exit(1)` called after `core.warning("Validation failed")`

---

### Question 77 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: none
**Topic**: Creating and Publishing Actions

**Question**:
A developer writes `action.yml` for a new custom GitHub Action and specifies a `runs.using:` value. Which of the following is an actual valid value for the `runs.using:` field?

- A) `python3`
- B) `bash`
- C) `go`
- D) `java`

---

### Question 78 — Managing Runners

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Managing Runners

**Question**:
A self-hosted runner machine loses network connectivity and goes offline. Where in the GitHub UI does an admin check the runner's current online/offline status?

- A) `Repository ? Actions ? Workflows ? Runner Status`
- B) `Repository ? Settings ? Actions ? Runners`
- C) `Repository ? Insights ? Actions ? Runner Health`
- D) `Organization ? Billing ? Actions Minutes ? Runner Status`

---

### Question 79 — Managing Runners

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Managing Runners

**Question**:
A workflow job must run on a self-hosted runner that has both the `gpu` and `linux` labels. Which `runs-on:` configurations correctly target such a runner? *(Select all that apply.)*

**A.**
```yaml
runs-on: [self-hosted, gpu, linux]
```
**B.**
```yaml
runs-on:
  labels: [self-hosted, gpu, linux]
```
**C.**
```yaml
runs-on: self-hosted
  labels:
    - gpu
    - linux
```
**D.**
```yaml
runs-on: gpu
```

---

### Question 80 — Managing Runners

**Difficulty**: Medium
**Answer Type**: all
**Topic**: Managing Runners

**Question**:
A team moves from self-hosted runners to GitHub-hosted runners. Which of the following statements about GitHub-hosted runners are all correct?

- A) GitHub-hosted runners are automatically provisioned for each job and destroyed after the job completes
- B) GitHub-hosted runners include preinstalled tools such as Node.js, Python, Java, Docker, and the GitHub CLI
- C) The `RUNNER_TOOL_CACHE` environment variable points to the directory containing preinstalled tool versions
- D) GitHub-hosted runners come in Ubuntu, Windows, and macOS variants

---

### Question 81 — Managing Runners

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Managing Runners

**Question**:
An organization creates a runner group with `visibility: selected` and assigns it to two specific repositories. A workflow in a third repository (not in the selected list) includes `runs-on: group: production-runners`. What happens?

- A) The job queues and waits indefinitely for a runner in that group to become available
- B) The job fails immediately because the repository is not authorized to use the runner group
- C) GitHub falls back to the nearest matching labeled runner outside the group
- D) The job runs on a GitHub-hosted runner as a fallback when no group runner is available

---

### Question 82 — Managing Runners

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Managing Runners

**Question**:
An organization wants jobs to run on a specific runner group while also requiring specific labels within that group. Which `runs-on:` syntax supports specifying both a group and required labels? *(Select all that apply.)*

**A.**
```yaml
runs-on:
  group: production-runners
  labels: [self-hosted, linux]
```
**B.**
```yaml
runs-on: [group:production-runners, self-hosted, linux]
```
**C.**
```yaml
runs-on:
  group: production-runners
```
**D.**
```yaml
runs-on: production-runners
```

---

### Question 83 — Managing Runners

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Managing Runners

**Question**:
A security-conscious organization evaluates self-hosted runners for use with public repositories. Which security risks and considerations apply? *(Select all that apply.)*

- A) Fork-based pull requests can trigger workflows on self-hosted runners, potentially running untrusted code on the runner machine
- B) Self-hosted runners persist between jobs, meaning a previous job's files and processes may remain on the runner
- C) GitHub-hosted runners are destroyed after each job, making them inherently safer for public repositories
- D) Self-hosted runners are completely isolated from the host network by default
- E) Organizations can mitigate fork PR risks by requiring approval before running workflows from first-time contributors

---

### Question 84 — GitHub Actions Enterprise

**Difficulty**: Easy
**Answer Type**: one
**Topic**: GitHub Actions Enterprise

**Question**:
An enterprise administrator configures a **required workflow** for all repositories in the organization. Under what condition does the required workflow run for a given repository?

- A) Only when the repository explicitly opts in by adding a `required_workflows:` key to their workflow file
- B) Automatically on matching repositories regardless of whether repository admins have enabled or disabled Actions
- C) Only on public repositories within the organization
- D) Only when triggered by a push to the default branch

---

### Question 85 — GitHub Actions Enterprise

**Difficulty**: Medium
**Answer Type**: one
**Topic**: GitHub Actions Enterprise

**Question**:
An enterprise administrator sets a policy that restricts all repositories in the organization to use only actions from the organization's own repos plus verified GitHub actions. A repository admin tries to change this restriction to allow all third-party actions. What happens?

- A) The repository admin can override the enterprise policy for their own repository
- B) The repository admin's change is blocked; enterprise-level policies always take precedence over organization and repository level
- C) The organization admin must first grant permission before the repository admin can override the enterprise policy
- D) The change applies only to that repository, while the organization policy remains in effect for others

---

### Question 86 — GitHub Actions Enterprise

**Difficulty**: Medium
**Answer Type**: many
**Topic**: GitHub Actions Enterprise

**Question**:
A security engineer reviews how GitHub handles workflows triggered by pull requests from **forked repositories**. Which statements correctly describe the default behavior for the `pull_request` event from a fork? *(Select all that apply.)*

- A) The `GITHUB_TOKEN` is read-only for `pull_request` workflows triggered by forks
- B) Secrets defined in the repository are not available to `pull_request` workflows from forks
- C) `pull_request_target` runs with full write access and secrets even from forks, which creates a security risk
- D) Fork PRs from first-time contributors are automatically approved and proceed without any friction
- E) Organizations can configure GitHub to require manual approval before running workflows from first-time contributors

---

### Question 87 — GitHub Actions Enterprise

**Difficulty**: Hard
**Answer Type**: one
**Topic**: GitHub Actions Enterprise

**Question**:
An enterprise organization creates a runner group at the enterprise level and sets it to span multiple organizations within the enterprise. A repository in `org-A` tries to use this enterprise runner group but the runner group is configured with access only to `org-B`. What is the result?

- A) The job runs on any available runner in the enterprise, ignoring the group restriction
- B) The job fails because the repository in `org-A` is not authorized to use the enterprise runner group configured for `org-B`
- C) GitHub routes the job to an equivalent runner group in `org-A` with matching labels
- D) The enterprise admin is notified, and the job queues until they approve access for `org-A`

---

### Question 88 — GitHub Actions Enterprise

**Difficulty**: Hard
**Answer Type**: all
**Topic**: GitHub Actions Enterprise

**Question**:
An enterprise administrator reviews the action allow-list policies to enforce supply-chain security. Which of the following statements about enterprise action allow-list configuration are all correct?

- A) Setting policy to "Allow local actions only" restricts all repositories to using only actions from within the same organization or enterprise
- B) Wildcard patterns like `actions/*` allow all actions published by the `actions` organization
- C) Enterprise-level policies take precedence over organization-level policies for all repositories in the enterprise
- D) Full commit SHA pinning requirement can be enforced at the organization level, causing workflows that use mutable tags like `@v4` to fail

---

### Question 89 — Security and Optimization

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Security and Optimization

**Question**:
The `GITHUB_TOKEN` is automatically provisioned at the start of each workflow job. When is this token automatically revoked?

- A) When the step that uses the token completes
- B) When the job that received the token finishes
- C) 24 hours after the token was issued, regardless of job status
- D) When the workflow run completes (not per-job)

---

### Question 90 — Security and Optimization

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Security and Optimization

**Question**:
A workflow contains the following step, which is vulnerable to script injection:

```yaml
- name: Greet contributor
  run: |
    echo "Thank you for PR: ${{ github.event.pull_request.title }}"
```

What is the correct mitigation to prevent a malicious PR title from injecting shell commands?

- A) Wrap the expression in single quotes: `echo 'Thank you for PR: ${{ github.event.pull_request.title }}'`
- B) Pass the PR title through an environment variable and reference the env var in the script, avoiding direct expression interpolation
- C) Use `${{ toJSON(github.event.pull_request.title) }}` to escape the title before interpolation
- D) Only allow `push` events in the workflow trigger to avoid PR-based injection vectors

---

### Question 91 — Security and Optimization

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Security and Optimization

**Question**:
A workflow needs to request an OIDC token from GitHub to authenticate against AWS without storing an AWS secret in GitHub. Which conditions must be met for OIDC token federation to work? *(Select all that apply.)*

- A) The job must have `permissions: id-token: write` to request the OIDC JWT
- B) The AWS IAM role trust policy must include the GitHub OIDC provider URL `https://token.actions.githubusercontent.com`
- C) The workflow must store an `AWS_SECRET_ACCESS_KEY` secret that the OIDC action uses internally
- D) The trust policy should be scoped to specific repositories, branches, or environments to prevent abuse
- E) The OIDC token request uses `contents: read` permission, not `id-token: write`

---

### Question 92 — Security and Optimization

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Security and Optimization

**Question**:
An AWS IAM role trust policy is configured to only allow OIDC-based access for workflows running from the `main` branch of `myorg/myapp`. A workflow running on the `feature/login` branch of the same repository attempts to assume the role. What is the outcome?

- A) The trust policy allows the request because the repository (`myorg/myapp`) matches, regardless of branch
- B) The role assumption is denied because the OIDC subject claim `repo:myorg/myapp:ref:refs/heads/feature/login` does not match the allowed subject `repo:myorg/myapp:ref:refs/heads/main`
- C) The role assumption succeeds but AWS grants only read-only permissions for non-main branches
- D) GitHub intercepts the request and automatically adjusts the subject claim to allow branch-based matching

---

### Question 93 — Security and Optimization

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Security and Optimization

**Question**:
A security team mandates that all action references must be pinned to full commit SHAs. A workflow uses `actions/checkout@v4`. The team asks why this is insufficient. What is the correct reason?

- A) `@v4` references the latest minor version and can silently change when a new patch is released
- B) `@v4` is a tag, which is mutable — the tag can be moved to point to a different commit at any time, meaning the action code could change without any modification to the workflow file
- C) `@v4` only works for public GitHub repositories; private repositories require SHA pinning
- D) The `v4` tag is deprecated and should be replaced with `v4.x.x` for stability

---

### Question 94 — Security and Optimization

**Difficulty**: Medium
**Answer Type**: all
**Topic**: Security and Optimization

**Question**:
A security engineer compares `GITHUB_TOKEN` to a Personal Access Token (PAT) for use in automated workflows. Which of the following statements about the differences are all correct?

- A) `GITHUB_TOKEN` is automatically provisioned per job and revoked when the job ends; PATs are manually created and have user-configured expiry
- B) `GITHUB_TOKEN` is scoped to a single repository; PATs can be granted access to multiple repositories
- C) `GITHUB_TOKEN` cannot trigger new workflow runs in the same repository; PATs can trigger new workflow runs
- D) PATs carry a higher security risk than `GITHUB_TOKEN` because they are long-lived credentials

---

### Question 95 — Security and Optimization

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Security and Optimization

**Question**:
An organization enforces maximum privilege reduction. A job only needs to push a Docker image to GitHub Container Registry, with no other operations. Which is the minimal `permissions:` block that should be specified for this job?

**A.**
```yaml
permissions:
  contents: read
  packages: write
```
**B.**
```yaml
permissions:
  contents: write
  packages: write
  id-token: write
```
**C.**
```yaml
permissions: write-all
```
**D.**
```yaml
permissions:
  packages: write
  issues: write
```

---

### Question 96 — Common Failures and Troubleshooting

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Common Failures and Troubleshooting

**Question**:
A workflow job is stuck showing `Waiting for a runner to pick up this job` and never starts. The workflow uses `runs-on: [self-hosted, linux, gpu]`. What is the most likely root cause?

- A) The workflow file has invalid YAML syntax
- B) No self-hosted runner with all three labels (`self-hosted`, `linux`, `gpu`) is online and available
- C) GitHub-hosted runners do not support custom labels
- D) The `gpu` label is reserved and cannot be used in `runs-on`

---

### Question 97 — Common Failures and Troubleshooting

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Common Failures and Troubleshooting

**Question**:
A workflow job intermittently fails with `The operation timed out after 360 minutes`. The job runs integration tests that normally complete in under an hour. Which configuration change should be investigated first?

- A) Increase the repository-level `default_timeout` setting in GitHub Organization settings
- B) Set `timeout-minutes:` at the job or step level to a value appropriate for expected runtime, and investigate what is causing the test to hang
- C) Switch to a GitHub-hosted runner because self-hosted runners have a shorter timeout limit
- D) Set `continue-on-error: true` on the failing step to bypass the timeout

---

### Question 98 — Common Failures and Troubleshooting

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Common Failures and Troubleshooting

**Question**:
A workflow's `npm ci` step frequently fails with `ERESOLVE` dependency errors in CI despite working locally. Which troubleshooting steps are appropriate? *(Select all that apply.)*

- A) Pin the Node.js version in the workflow using `actions/setup-node` with a specific `node-version:` value matching the local development environment
- B) Run `npm ci --legacy-peer-deps` or `npm ci --force` if there are known peer dependency conflicts
- C) Set `ACTIONS_STEP_DEBUG: true` to view verbose npm install output and identify conflicting packages
- D) Delete and regenerate `package-lock.json` locally, verify the resolution works, and commit the updated lock file
- E) Downgrade GitHub Actions to a previous version that did not have this issue

---

### Question 99 — Common Failures and Troubleshooting

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Common Failures and Troubleshooting

**Question**:
A workflow step uses `GITHUB_TOKEN` to push a tag to the repository:

```yaml
- name: Push tag
  run: git push origin $TAG_NAME
  env:
    GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

The step fails with `Permission denied`. The default organization permission for `GITHUB_TOKEN` is `restricted` (read-only). How is this fixed correctly?

- A) Replace `secrets.GITHUB_TOKEN` with `secrets.PERSONAL_ACCESS_TOKEN` which always has write access
- B) Add `permissions: contents: write` to the job or workflow level to grant the required write access to the repository contents scope
- C) Change the push command to use `GITHUB_ACTOR` authentication instead of the token
- D) Set the `actions.default_token_permissions` setting to `permissive` for the entire organization

---

### Question 100 — Common Failures and Troubleshooting

**Difficulty**: Medium
**Answer Type**: none
**Topic**: Common Failures and Troubleshooting

**Question**:
A developer lists common causes of "your workflow did not run" situations in GitHub Actions. Which of the following is actually a valid documented reason why a workflow may silently skip execution?

- A) The workflow file has more than 10 job definitions
- B) The workflow trigger uses `pull_request` and the push event also fires on the same commit
- C) The repository has received more than 1,000 workflow runs in the current billing period
- D) The workflow is triggered by `GITHUB_TOKEN` which cannot trigger new workflow runs

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | B | Correct YAML schema configuration in `settings.json` using yaml.schemas with the GitHub workflow URL. | 01-GitHub-Actions-VS-Code-Extension.md | Medium |
| 2 | B | The GitHub Actions extension displays action.yml metadata (inputs, outputs, description) in hover previews. | 01-GitHub-Actions-VS-Code-Extension.md | Medium |
| 3 | A, B, C, D | Extension detects invalid context usage, scoping issues, permission typos, and step ordering problems. | 01-GitHub-Actions-VS-Code-Extension.md | Medium |
| 4 | None | The extension cannot run workflows locally, commit changes automatically, or stream real-time logs. | 01-GitHub-Actions-VS-Code-Extension.md | Hard |
| 5 | A, B, C, D | Extension identifies workflow paths, provides IntelliSense context, validates permission scopes, and requires yaml extension. | 01-GitHub-Actions-VS-Code-Extension.md | Hard |
| 6 | B | `github.run_id` provides the unique numeric identifier for the current workflow run that persists across re-runs. | 02-Contextual-Information.md | Easy |
| 7 | A, B, C | `github.ref_name` for branch name, `github.sha` for full commit SHA, and context properties for tag detection. | 02-Contextual-Information.md | Medium |
| 8 | A, B, C, E | Runner context includes name, OS, architecture, and temp path; memory is not a standard property. | 02-Contextual-Information.md | Medium |
| 9 | B | Use `${{ needs.build.outputs.version }}` to access job outputs; needs context only available in downstream jobs. | 02-Contextual-Information.md | Medium |
| 10 | A | `github.base_ref` returns the target branch (main) that the PR is being merged into. | 02-Contextual-Information.md | Easy |
| 11 | A, B, C, D | Matrix strategies provide `matrix.os`, `matrix.node`, `strategy.job-total`, and `strategy.job-index` variables. | 02-Contextual-Information.md | Hard |
| 12 | B | The `needs` context is available in job-level `if:`, step `run:`, and job-level `outputs`. | 03-Context-Availability-Reference.md | Easy |
| 13 | A, B, C, D, E | Job-level `if:` conditions can access github, secrets, needs, runner, and vars contexts. | 03-Context-Availability-Reference.md | Medium |
| 14 | A, B, C, D | All statements about context availability are correct (secrets not in workflow env, scope restrictions). | 03-Context-Availability-Reference.md | Hard |
| 15 | None | None of these contexts are actually available in the `uses:` key for action selection. | 03-Context-Availability-Reference.md | Medium |
| 16 | B | Functions like `always()`, `success()`, `failure()`, and `cancelled()` are only available in job-level `if:` conditions. | 03-Context-Availability-Reference.md | Easy |
| 17 | B | By default, `fail-fast` is true; in-progress and queued matrix jobs are cancelled when any matrix job fails. | 04-Workflow-File-Structure.md | Easy |
| 18 | A, B, C, D, E | Concurrency group with `cancel-in-progress: true` cancels previous runs and allows sequential execution per branch. | 04-Workflow-File-Structure.md | Medium |
| 19 | A, B, C, D | All statements about `defaults.run` are correct regarding shell, working directory, and scope precedence. | 04-Workflow-File-Structure.md | Hard |
| 20 | B | Job-level defaults override workflow-level defaults for run shell and working directory settings. | 04-Workflow-File-Structure.md | Hard |
| 21 | A | Scheduled workflows don't run on repositories with no commits in 60 days (inactivity threshold). | 05-Workflow-Trigger-Events.md | Easy |
| 22 | B | `workflow_dispatch` enables manual triggering; access inputs via `github.event.inputs` with string types only. | 05-Workflow-Trigger-Events.md | Medium |
| 23 | B | Use `on: workflow_run` to trigger after another workflow completes with status/conclusion filters. | 05-Workflow-Trigger-Events.md | Medium |
| 24 | A, B, D | Multiple deployment branch protection scenarios all apply; IP allowlists restrict by registration IP. | 05-Workflow-Trigger-Events.md | Hard |
| 25 | B | Scheduled workflows check activity status for inactivity before triggering; no explicit override available. | 05-Workflow-Trigger-Events.md | Easy |
| 26 | B | Repository-scoped secrets take precedence over organization-scoped; most specific scope always wins. | 06-Custom-Environment-Variables.md | Medium |
| 27 | A, C, E, F | The range of valid secret scope and access scenarios described in the options. | 06-Custom-Environment-Variables.md | Medium |
| 28 | C | Both ${{ }} context syntax and shell variable expansion work for environment variable interpolation. | 06-Custom-Environment-Variables.md | Hard |
| 29 | B | Masking fails when secrets are transformed (encoded, hashed); automatic masking is incomplete for variants. | 06-Custom-Environment-Variables.md | Hard |
| 30 | A, B, C | Workflow-level env is globally available; job/step levels override specific values down the scope chain. | 06-Custom-Environment-Variables.md | Easy |
| 31 | B | Job-level `env:` completely overrides workflow-level `env:` for that job's execution scope. | 06-Custom-Environment-Variables.md | Medium |
| 32 | A, B, C, D | GITHUB_OUTPUT allows setting and retrieving step outputs; steps must have ID for proper output access. | 06-Custom-Environment-Variables.md | Medium |
| 33 | B | GitHub automatically redacts secrets 3+ characters from logs using exact string matching. | 06-Custom-Environment-Variables.md | Medium |
| 34 | B | Contexts evaluate correctly in workflow-level env; `github.sha` returns full 40-character commit SHA. | 06-Custom-Environment-Variables.md | Hard |
| 35 | A, B, D, E | All listed runner variables are standard; RUNNER_LABEL is not a recognized default variable. | 07-Default-Environment-Variables.md | Medium |
| 36 | A | GITHUB_TOKEN auto-created per job; scoped to current repository only. | 07-Default-Environment-Variables.md | Easy |
| 37 | None | Workflow runs locally, commits automatically, and dynamic action selection are all unsupported. | 07-Default-Environment-Variables.md | Hard |
| 38 | B | Required reviewers in deployment environments require manual approval before continuation. | 08-Environment-Protection-Rules.md | Medium |
| 39 | B | All deployment branch protection scenarios correctly apply across listed conditions. | 08-Environment-Protection-Rules.md | Medium |
| 40 | C | Environment-scoped secrets and variables available when job specifies environment context. | 08-Environment-Protection-Rules.md | Medium |
| 41 | A, B, C, D | All are valid default runner environment variables available in workflow context. | 07-Default-Environment-Variables.md | Medium |
| 42 | A, B, C, D | Artifacts auto-delete after retention; require explicit download; use glob patterns; matrix auto-renames. | 09-Workflow-Artifacts.md | Medium |
| 43 | B | Artifacts are automatically deleted after the retention period expires. | 09-Workflow-Artifacts.md | Easy |
| 44 | A | hashFiles() updates cache key when dependency files change, correctly invalidating stale cache. | 10-Workflow-Caching.md | Medium |
| 45 | A, B, C, D | hashFiles updates cache; restore-keys provides fallback; cache restores most recent match. | 10-Workflow-Caching.md | Medium |
| 46 | B | Composite actions use `runs: { using: composite, steps: [...] }` syntax. | 11-Workflow-Sharing.md | Easy |
| 47 | None | Workflows cannot be run locally from VS Code; this is a UI-only viewing option. | 01-GitHub-Actions-VS-Code-Extension.md | Hard |
| 48 | B | GitHub renames matrix artifacts with matrix identifiers to prevent collisions automatically. | 09-Workflow-Artifacts.md | Hard |
| 49 | B | After 24 hours from start time (Tuesday 2 PM to Wednesday 2 PM approximately). | 08-Environment-Protection-Rules.md | Medium |
| 50 | A, B, D, E | Artifacts inherit repo permissions; accessible during the run; expire after retention period. | 09-Workflow-Artifacts.md | Medium |
| 51 | B | List-then-download and direct download endpoints are both valid approaches for artifact retrieval. | 09-Workflow-Artifacts.md | Hard |
| 52 | B | Use `with: secrets: inherit` to pass all repository secrets to reusable workflows. | 11-Workflow-Sharing.md | Hard |
| 53 | B | `cache-hit` output uses underscore notation: check `steps.cache.outputs.cache_hit`. | 10-Workflow-Caching.md | Medium |
| 54 | B | action.yml required at repo root; GitHub Marketplace optional; README with examples recommended. | 15-Creating-Publishing-Actions.md | Medium |
| 55 | A, B, C, D | Reusable workflows require `on: workflow_call`; can reference by name/version/SHA. | 11-Workflow-Sharing.md | Hard |
| 56 | A | Reusable workflow metadata file is action.yml or action.yaml at repository root. | 15-Creating-Publishing-Actions.md | Easy |
| 57 | B | Shipping node_modules or using ncc/esbuild for bundling are both valid approaches. | 15-Creating-Publishing-Actions.md | Medium |
| 58 | A, B, D, E | All reusable workflow and composite action features listed are correct implementations. | 11-Workflow-Sharing.md | Hard |
| 59 | A | Setting RUNNER_DEBUG=1 as a secret and re-running enables debug logging for the workflow. | 12-Workflow-Debugging.md | Medium |
| 60 | A, B, D | All workflow logging commands (notice, warning, error) are available; debug via RUNNER_DEBUG. | 12-Workflow-Debugging.md | Medium |
| 61 | A, B, C, D | All logging workflow commands are available for structured output. | 12-Workflow-Debugging.md | Medium |
| 62 | C | Both UI interface and API provide access to workflow logs; UI is primary interface. | 12-Workflow-Debugging.md | Easy |
| 63 | B | Use `if: failure()` to upload artifacts only on step failure; if: always() uploads regardless. | 12-Workflow-Debugging.md | Medium |
| 64 | A, B | Environment-scoped variables override lower scopes; both secrets and variables are scope-managed. | 08-Environment-Protection-Rules.md | Medium |
| 65 | B, C, D | Any of these factors can cause deployment branch protection time windows to be complex. | 08-Environment-Protection-Rules.md | Hard |
| 66 | B | Correct endpoint: `/repos/{owner}/{repo}/actions/workflows` for workflow list queries. | 13-Workflows-REST-API.md | Medium |
| 67 | None | No built-in autoscaling exists; use third-party tools or hosted runners for capacity. | 16-Managing-Runners.md | Hard |
| 68 | B | POST to `/repos/{owner}/{repo}/actions/runs/{run_id}/cancel` to cancel workflow runs. | 13-Workflows-REST-API.md | Medium |
| 69 | B | Lack of verification, infrequent updates, and high follower counts all merit careful assessment. | 18-Security-and-Optimization.md | Hard |
| 70 | B, C, D, E | Multiple factors can prevent trigger; branch filter `main` requires exact match. | 19-Common-Failures-Troubleshooting.md | Medium |
| 71 | A, B, C, D | All statements about triggering workflows via API are correct. | 13-Workflows-REST-API.md | Medium |
| 72 | C | All approaches (UI, API, logs) provide deployment timing information. | 12-Workflow-Debugging.md | Hard |
| 73 | B | GitHub doesn't offer built-in time-window deployment protection; use custom logic or apps. | 08-Environment-Protection-Rules.md | Hard |
| 74 | A | Environments are configured in Repository Settings ? Environments. | 14-Reviewing-Deployments.md | Easy |
| 75 | A, B, C, D, E | All optimization strategies (parallelization, caching, artifacts, runners, incremental builds) are valid. | 19-Common-Failures-Troubleshooting.md | Hard |
| 76 | B | Deployment history is available in UI, API, and workflow logs. | 14-Reviewing-Deployments.md | Medium |
| 77 | None | Moving v1 tag automatically is optional; version management requires deliberate planning. | 15-Creating-Publishing-Actions.md | Hard |
| 78 | B | All testing approaches are valid at different development and deployment stages. | 15-Creating-Publishing-Actions.md | Medium |
| 79 | A, B | Both ubuntu-latest and ubuntu-22.04 support Python 3.10. | 16-Managing-Runners.md | Easy |
| 80 | A, B, C, D | All statements about runner labels and targeting are correct. | 16-Managing-Runners.md | Medium |
| 81 | B | Organizations support IP allowlists for runner group restrictions; IP-based access control. | 16-Managing-Runners.md | Medium |
| 82 | A, C | Required workflows run automatically and appear in PR checks; admins cannot disable. | 17-GitHub-Actions-Enterprise.md | Medium |
| 83 | A, B, C, E | Various valid context availability and scope scenarios listed. | 03-Context-Availability-Reference.md | Hard |
| 84 | B | "Allow local actions only" permits all actions within organization/enterprise; not just local. | 17-GitHub-Actions-Enterprise.md | Medium |
| 85 | B | After second approval, deployment is approved and continues immediately. | 14-Reviewing-Deployments.md | Hard |
| 86 | A, B, C, E | Multiple valid action referencing and permission configuration methods listed. | 13-Workflows-REST-API.md | Medium |
| 87 | B | IP allowlists restrict runners by registration IP; runners outside range cannot be accessed. | 17-GitHub-Actions-Enterprise.md | Hard |
| 88 | A, B, C, D | All audit logging capabilities available for compliance and security auditing. | 17-GitHub-Actions-Enterprise.md | Hard |
| 89 | B | Use explicit minimal permissions; avoid read-all scope for security best practices. | 18-Security-and-Optimization.md | Medium |
| 90 | B | Setting secrets as env variables and referencing them prevents code injection attacks. | 18-Security-and-Optimization.md | Hard |
| 91 | A, B, D | Pin to full SHA; validate verified creators; monitor updates for supply chain security. | 18-Security-and-Optimization.md | Medium |
| 92 | B | OIDC federation enables workflowless credentials; all statements about federation are correct. | 18-Security-and-Optimization.md | Hard |
| 93 | B | Masking can fail from multiple causes; exact string matching has edge case limitations. | 18-Security-and-Optimization.md | Hard |
| 94 | A, B, C, D | Assessment criteria for action verification include creator status, update frequency, and followers. | 18-Security-and-Optimization.md | Hard |
| 95 | A | Parallelizing independent jobs (build and test) minimizes overall runtime. | 18-Security-and-Optimization.md | Medium |
| 96 | B | Reduce artifact size, split jobs, increase timeout, or consider larger runners for space limits. | 19-Common-Failures-Troubleshooting.md | Medium |
| 97 | B | Branch filter `main` requires exact match; omitting path filters matches all paths. | 19-Common-Failures-Troubleshooting.md | Medium |
| 98 | A, B, C, D | Incremental builds, caching, larger runners, and workflow optimization improve performance. | 19-Common-Failures-Troubleshooting.md | Medium |
| 99 | B | Missing secrets result from scoping issues, workflow errors, or incorrect references. | 19-Common-Failures-Troubleshooting.md | Hard |
| 100 | None | Matrix job failure skips dependent jobs by default; requires if: always() to override. | 19-Common-Failures-Troubleshooting.md | Hard |

---

*End of GH-200 Iteration 5 — 100 Questions*
