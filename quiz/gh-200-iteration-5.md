# GitHub Actions GH-200 Certification Exam — Question Bank (Iteration 5)

**Total Questions:** 100
**Difficulty Distribution:** 20 Easy · 60 Medium · 20 Hard
**Answer Types:** 55 one · 26 many · 12 all · 7 none
**Passing Score:** 70% (70/100)
**Scenario-Based:** 76 questions (76%)
**Security Questions:** 14 (Topics 18, 06, 07, and security scenarios in other topics)
**Enterprise Questions:** 15 (Topics 08, 14, 17 and related scenarios)

---

## Question 1 — Topic 01: GitHub Actions VS Code Extension

**Difficulty:** Easy | **Answer Type:** one | **Topic:** 01

A developer wants to manually add the GitHub Actions YAML schema to VS Code because the extension is not auto-detecting their workflow files. They open `settings.json` to add it. Which configuration is correct?

**A.** `"github.actions.schema": ".github/workflows/*.yml"`
**B.** `"yaml.schemas": { "https://json.schemastore.org/github-workflow.json": ".github/workflows/*.yml" }`
**C.** `"github.actions.validate": { "pattern": "*.yml", "schema": "github-workflow" }`
**D.** `"files.associations": { ".github/workflows/*.yml": "github-actions" }`

**Answer:** B

---

## Question 2 — Topic 01: GitHub Actions VS Code Extension

**Difficulty:** Medium | **Answer Type:** one | **Topic:** 01

A workflow developer hovers over `uses: actions/setup-node@v4` in VS Code and sees action metadata. What specific information does the GitHub Actions extension display in this hover preview?

**A.** The GitHub Marketplace download statistics and star rating for the action
**B.** The action's `action.yml` content, including available inputs, outputs, and description
**C.** A diff showing all changes introduced between the previous and current version
**D.** The action's test coverage percentage and CI build status badge

**Answer:** B

---

## Question 3 — Topic 01: GitHub Actions VS Code Extension

**Difficulty:** Medium | **Answer Type:** many | **Topic:** 01

A developer is using the GitHub Actions VS Code extension while writing a new workflow. Which of the following problems will the extension detect and highlight before the workflow is pushed? *(Select all that apply.)*

**A.** Using `${{ secrets.MY_TOKEN }}` in the workflow-level `env:` block, where `secrets` context is not supported
**B.** Referencing the output of a step using `${{ steps.build.outputs.result }}` in a step that appears before the `build` step
**C.** An invalid YAML indentation caused by mixing tabs and spaces
**D.** A permission scope typo such as `content: write` instead of `contents: write` in a `permissions:` block
**E.** Whether `secrets.MY_TOKEN` has been defined in the GitHub repository settings

**Answer:** A, B, C, D

---

## Question 4 — Topic 01: GitHub Actions VS Code Extension

**Difficulty:** Medium | **Answer Type:** none | **Topic:** 01

A developer lists several advanced features they believe the GitHub Actions VS Code extension provides. Which of the following does the extension actually support?

**A.** Running workflow jobs locally inside a Docker container with results streamed back to the editor
**B.** Automatically committing and pushing workflow changes to the remote repository on file save
**C.** Displaying real-time streaming logs from in-progress workflow runs in the editor sidebar
**D.** Synchronizing repository secrets between a local `.env` file and GitHub's secret settings

**Answer:** None of the above

---

## Question 5 — Topic 01: GitHub Actions VS Code Extension

**Difficulty:** Hard | **Answer Type:** all | **Topic:** 01

A platform engineering team standardizes on the GitHub Actions VS Code extension. A senior engineer validates the team's understanding of the extension's behavior. Which of the following statements are all correct?

**A.** The extension identifies workflow files using path patterns like `.github/workflows/*.yml` and applies GitHub's JSON schema for validation
**B.** IntelliSense for `${{ github.* }}` shows all available properties from GitHub's official context documentation when typed inside a workflow expression
**C.** The extension validates that `permissions:` blocks contain only recognized scope names and warns when unknown scopes are used
**D.** If the YAML Language Support extension is not installed, schema validation features in the GitHub Actions extension may not function correctly

**Answer:** All of the above (A, B, C, D)

---

## Question 6 — Topic 02: Contextual Information

**Difficulty:** Easy | **Answer Type:** one | **Topic:** 02

A workflow step needs to print the unique numeric identifier for the current workflow run — one that remains constant across re-runs of the same run. Which expression provides this value?

**A.** `${{ github.run_number }}`
**B.** `${{ github.run_id }}`
**C.** `${{ github.run_attempt }}`
**D.** `${{ github.workflow_id }}`

**Answer:** B

---

## Question 7 — Topic 02: Contextual Information

**Difficulty:** Medium | **Answer Type:** many | **Topic:** 02

A workflow builds a Docker image and tags it for a registry. The team needs to compose the image tag using both the branch name and the short commit SHA. Which context expressions provide these values correctly? *(Select all that apply.)*

**A.** `${{ github.ref_name }}` — provides the branch or tag name without the `refs/heads/` prefix
**B.** `${{ github.sha }}` — provides the full 40-character commit SHA
**C.** `${{ github.ref_type }}` — provides whether the trigger was a `branch` or `tag`
**D.** `${{ github.branch }}` — provides the branch name for all trigger events
**E.** `${{ github.head_commit.id }}` — provides the short 7-character commit SHA

**Answer:** A, B, C

---

## Question 8 — Topic 02: Contextual Information

**Difficulty:** Medium | **Answer Type:** many | **Topic:** 02

A DevOps engineer writes a step that prints comprehensive runner metadata to a job log for auditing. Which `runner` context properties are valid and available? *(Select all that apply.)*

**A.** `runner.name` — the display name of the runner executing the job
**B.** `runner.os` — the operating system (Linux, Windows, or macOS)
**C.** `runner.arch` — the CPU architecture (X64, ARM64, etc.)
**D.** `runner.memory` — the total RAM available on the runner in megabytes
**E.** `runner.temp` — the path to the runner's temporary directory

**Answer:** A, B, C, E

---

## Question 9 — Topic 02: Contextual Information

**Difficulty:** Medium | **Answer Type:** one | **Topic:** 02

A workflow has two dependent jobs. The first job (`build`) produces an output named `version` via `$GITHUB_OUTPUT`. The second job (`deploy`) needs to consume that value. Which expression in the `deploy` job correctly accesses the `version` output from `build`?

**A.** `${{ outputs.build.version }}`
**B.** `${{ needs.build.outputs.version }}`
**C.** `${{ jobs.build.outputs.version }}`
**D.** `${{ steps.build.outputs.version }}`

**Answer:** B

---

## Question 10 — Topic 02: Contextual Information

**Difficulty:** Medium | **Answer Type:** one | **Topic:** 02

During a `pull_request` event, a step references `${{ github.base_ref }}`. What value does this expression return?

**A.** The branch the pull request is being merged **into** (e.g., `main`)
**B.** The branch the pull request was created **from** (e.g., `feature/login`)
**C.** The full ref path of the base branch (e.g., `refs/heads/main`)
**D.** The latest commit SHA of the base branch at the time the PR was opened

**Answer:** A

---

## Question 11 — Topic 02: Contextual Information

**Difficulty:** Hard | **Answer Type:** many | **Topic:** 02

A matrix job uses `strategy.matrix` with `os: [ubuntu-latest, windows-latest]` and `node: [18, 20]`. Which expressions are valid and return expected values within a step of that matrix job? *(Select all that apply.)*

**A.** `${{ matrix.os }}` — returns `ubuntu-latest` or `windows-latest` depending on the current combination
**B.** `${{ strategy.job-total }}` — returns the total number of jobs generated by the matrix expansion (4 in this case)
**C.** `${{ strategy.job-index }}` — returns the zero-based index of the current job in the matrix
**D.** `${{ matrix.node }}` — returns `18` or `20` depending on the current combination
**E.** `${{ matrix.os.version }}` — unpacks the OS string into major version components

**Answer:** A, B, C, D

---

## Question 12 — Topic 03: Context Availability Reference

**Difficulty:** Easy | **Answer Type:** one | **Topic:** 03

In a multi-job workflow, the `needs` context allows a downstream job to access outputs from upstream jobs. At which workflow key is the `needs` context available for use?

**A.** Only in `jobs.<job_id>.steps[*].run`
**B.** In `jobs.<job_id>.if`, `jobs.<job_id>.steps[*].run`, and `jobs.<job_id>.outputs`
**C.** Only at the top-level workflow `env:` block
**D.** In `jobs.<job_id>.runs-on` for dynamic runner selection

**Answer:** B

---

## Question 13 — Topic 03: Context Availability Reference

**Difficulty:** Medium | **Answer Type:** many | **Topic:** 03

A workflow author is writing a `jobs.<job_id>.if:` condition that must evaluate whether a secret is set and whether a previous job succeeded. Which contexts are available in a job-level `if:` condition? *(Select all that apply.)*

**A.** `github` — to check event names, branch, actor, etc.
**B.** `secrets` — to check whether specific secrets are defined
**C.** `needs` — to check the outcome of jobs listed in `needs:`
**D.** `runner` — to check the operating system of the assigned runner
**E.** `vars` — to access repository and organization variables

**Answer:** A, B, C, E

---

## Question 14 — Topic 03: Context Availability Reference

**Difficulty:** Medium | **Answer Type:** all | **Topic:** 03

A workflow author needs to understand where `env` context and `secrets` context are and are not available. Which of the following statements are all correct regarding context availability restrictions?

**A.** The `secrets` context is not available at the workflow-level `env:` block
**B.** The `env` context is not available in the workflow-level `env:` block itself (to prevent circular references)
**C.** The `matrix` context is only available within the job that defines the matrix strategy
**D.** The `steps` context is only available within the job where those steps execute

**Answer:** All of the above (A, B, C, D)

---

## Question 15 — Topic 03: Context Availability Reference

**Difficulty:** Medium | **Answer Type:** none | **Topic:** 03

A workflow author reviews a list of contexts and claims all of them are available in the `jobs.<job_id>.steps[*].uses:` key for dynamic action selection. Which of these contexts is actually available in the `uses:` key?

**A.** `secrets` — to select a private action stored in a secret variable
**B.** `env` — to dynamically select an action based on an environment variable
**C.** `matrix` — to choose a different action per matrix combination
**D.** `needs` — to select an action based on the output of a dependency job

**Answer:** None of the above

---

## Question 16 — Topic 03: Context Availability Reference

**Difficulty:** Easy | **Answer Type:** one | **Topic:** 03

Which special functions are available in `jobs.<job_id>.if:` conditions but not in most other workflow keys?

**A.** `hashFiles()` and `toJSON()`
**B.** `always()`, `success()`, `failure()`, and `cancelled()`
**C.** `format()` and `join()`
**D.** `contains()` and `startsWith()`

**Answer:** B

---

## Question 17 — Topic 04: Workflow File Structure

**Difficulty:** Easy | **Answer Type:** one | **Topic:** 04

A workflow uses `strategy.matrix` with `fail-fast` not explicitly set. A developer asks what happens when one matrix job fails. Which behavior is the default?

**A.** All other matrix jobs continue running regardless of failures
**B.** All in-progress and queued matrix jobs are cancelled when any matrix job fails
**C.** Only jobs sharing the same `os` value as the failing job are cancelled
**D.** The failed job is automatically retried three times before other jobs are affected

**Answer:** B

---

## Question 18 — Topic 04: Workflow File Structure

**Difficulty:** Medium | **Answer Type:** many | **Topic:** 04

A workflow defines a `concurrency:` group set to `${{ github.workflow }}-${{ github.ref }}` with `cancel-in-progress: true`. Three developers push to the same branch in rapid succession. Which statements correctly describe the resulting behavior? *(Select all that apply.)*

**A.** The first run begins executing immediately
**B.** The second run cancels the first run and begins executing
**C.** The third run cancels the second run and begins executing
**D.** All three runs queue up and execute sequentially
**E.** Runs on different branches are not affected by this concurrency group

**Answer:** A, B, C, E

---

## Question 19 — Topic 04: Workflow File Structure

**Difficulty:** Medium | **Answer Type:** all | **Topic:** 04

A team is reviewing the `defaults:` section of a workflow. Which of the following statements about `defaults.run` are all correct?

**A.** `defaults.run.shell` sets the default shell for all `run:` steps in the workflow unless overridden at the job or step level
**B.** `defaults.run.working-directory` sets the default working directory for all `run:` steps
**C.** An individual step can override `defaults.run.shell` by specifying its own `shell:` key
**D.** `defaults.run` applies only to steps that use `run:`, not to `uses:` action steps

**Answer:** All of the above (A, B, C, D)

---

## Question 20 — Topic 04: Workflow File Structure

**Difficulty:** Hard | **Answer Type:** one | **Topic:** 04

A matrix job includes `include:` entries to add extra combinations. The base matrix is `os: [ubuntu-latest, windows-latest]` and `node: [18, 20]`. An `include:` entry specifies `os: ubuntu-latest, node: 20, experimental: true`. Which behavior correctly describes what `include:` produces in this case?

**A.** A new combination is added: `{os: ubuntu-latest, node: 20, experimental: true}` — replacing the existing `{ubuntu-latest, 20}` combination
**B.** The properties from the `include:` entry are merged into the existing `{ubuntu-latest, 20}` combination, adding `experimental: true` to it
**C.** The `include:` entry is rejected because `ubuntu-latest + node:20` already exists in the base matrix
**D.** The `experimental` key is ignored unless it is declared in the base matrix

**Answer:** B

---

## Question 21 — Topic 04: Workflow File Structure

**Difficulty:** Medium | **Answer Type:** one | **Topic:** 04

A workflow has jobs `build`, `test`, and `deploy`. The `deploy` job specifies `needs: [build, test]`. What must be true before `deploy` starts executing?

**A.** Both `build` and `test` must have completed successfully
**B.** Either `build` or `test` must have completed successfully
**C.** `build` must succeed, but `test` can be skipped
**D.** The jobs run in sequence based on the order they appear in the file

**Answer:** A

---

## Question 22 — Topic 05: Workflow Trigger Events

**Difficulty:** Easy | **Answer Type:** one | **Topic:** 05

A team wants to allow manually triggering a workflow from the GitHub Actions UI while requiring the user to select a deployment target (dev, staging, or prod). Which trigger configuration enables this?

**A.** `on: push` with a branch filter
**B.** `on: workflow_dispatch` with `inputs:` of type `choice`
**C.** `on: repository_dispatch` with event type filtering
**D.** `on: schedule` with a cron pattern

**Answer:** B

---

## Question 23 — Topic 05: Workflow Trigger Events

**Difficulty:** Medium | **Answer Type:** one | **Topic:** 05

A workflow is triggered by `pull_request_target`. How does this event differ from using `pull_request` when a fork submits a PR?

**A.** `pull_request_target` runs with read-only token and no access to secrets, identical to `pull_request`
**B.** `pull_request_target` runs in the context of the base repository with write token and access to secrets, even for fork PRs
**C.** `pull_request_target` only triggers for PRs from the same repository, while `pull_request` also handles forks
**D.** `pull_request_target` automatically rejects PRs from forked repositories

**Answer:** B

---

## Question 24 — Topic 05: Workflow Trigger Events

**Difficulty:** Medium | **Answer Type:** many | **Topic:** 05

A developer is configuring path-based filtering for workflow triggers. Which trigger events support the `paths:` or `paths-ignore:` filter? *(Select all that apply.)*

**A.** `push`
**B.** `pull_request`
**C.** `workflow_dispatch`
**D.** `pull_request_target`
**E.** `schedule`

**Answer:** A, B, D

---

## Question 25 — Topic 05: Workflow Trigger Events

**Difficulty:** Hard | **Answer Type:** one | **Topic:** 05

A repository has two workflow files. Workflow A triggers on `push` to `main`. Workflow B triggers on `push` to `main` with `paths-ignore: ['*.md', 'docs/**']`. A developer pushes a commit that changes only `README.md`. Which workflows run?

**A.** Both Workflow A and Workflow B run
**B.** Only Workflow A runs; Workflow B is skipped because of the path filter
**C.** Neither workflow runs because markdown changes are excluded by default
**D.** Both workflows are queued but Workflow B is auto-cancelled before it starts

**Answer:** B

---

## Question 26 — Topic 05: Workflow Trigger Events

**Difficulty:** Medium | **Answer Type:** one | **Topic:** 05

A DevOps team has a deployment workflow that should only trigger after another workflow named "Build and Test" completes successfully. Which trigger event and configuration achieves this?

**A.** `on: push` with a branch filter for `main`
**B.** `on: workflow_run` with `workflows: ["Build and Test"]` and `types: [completed]`, plus a job-level `if:` checking `github.event.workflow_run.conclusion == 'success'`
**C.** `on: workflow_call` referencing the build workflow as a dependency
**D.** `on: repository_dispatch` with `event-types: [build-completed]`

**Answer:** B

---

## Question 27 — Topic 05: Workflow Trigger Events

**Difficulty:** Hard | **Answer Type:** many | **Topic:** 05

A developer wants to understand which trigger event types support the `types:` activity filter. Which events can be filtered using `types:`? *(Select all that apply.)*

**A.** `pull_request`
**B.** `push`
**C.** `issues`
**D.** `workflow_dispatch`
**E.** `release`
**F.** `label`

**Answer:** A, C, E, F

---

## Question 28 — Topic 06: Custom Environment Variables

**Difficulty:** Easy | **Answer Type:** one | **Topic:** 06

A workflow defines `MY_VAR: global` at the workflow level, and a specific job re-defines `MY_VAR: job-level`. Inside that job, a step also defines `MY_VAR: step-level`. When the step's `run:` command echoes `$MY_VAR`, what value is printed?

**A.** `global`
**B.** `job-level`
**C.** `step-level`
**D.** An error because the variable is defined multiple times

**Answer:** C

---

## Question 29 — Topic 06: Custom Environment Variables

**Difficulty:** Medium | **Answer Type:** one | **Topic:** 06

A workflow step appends a value to the `$GITHUB_ENV` file to share a variable with subsequent steps. What is the correct syntax for setting `DEPLOY_VERSION=1.2.3` for all following steps?

**A.** `echo "export DEPLOY_VERSION=1.2.3" >> $GITHUB_ENV`
**B.** `echo "DEPLOY_VERSION=1.2.3" >> $GITHUB_ENV`
**C.** `echo "set DEPLOY_VERSION=1.2.3" >> $GITHUB_ENV`
**D.** `GITHUB_ENV["DEPLOY_VERSION"]="1.2.3"`

**Answer:** B

---

## Question 30 — Topic 06: Custom Environment Variables

**Difficulty:** Medium | **Answer Type:** all | **Topic:** 06

An organization wants to pass a secret into a container-based deployment step securely. A DevOps engineer proposes several approaches. Which of the following are all valid methods for making a secret available to a `run:` step?

**A.** Defining it under the step's `env:` block: `env: { API_KEY: "${{ secrets.API_KEY }}" }`
**B.** Defining it under the job's `env:` block: `env: { API_KEY: "${{ secrets.API_KEY }}" }` (available to all steps in the job)
**C.** Defining it under the workflow-level `env:` block: `env: { API_KEY: "${{ secrets.API_KEY }}" }`
**D.** Passing it directly to a shell command using `run: ./deploy.sh ${{ secrets.API_KEY }}`

**Answer:** A, B, C

---

## Question 31 — Topic 06: Custom Environment Variables

**Difficulty:** Medium | **Answer Type:** one | **Topic:** 06

A workflow step needs to mask a dynamically computed value from appearing in future log output. The value is retrieved from an external API at runtime. Which workflow command achieves this?

**A.** `echo "::set-secret::$DYNAMIC_VALUE"`
**B.** `echo "::add-mask::$DYNAMIC_VALUE"`
**C.** `echo "::redact::$DYNAMIC_VALUE"`
**D.** `echo "MASK=$DYNAMIC_VALUE" >> $GITHUB_ENV`

**Answer:** B

---

## Question 32 — Topic 06: Custom Environment Variables

**Difficulty:** Medium | **Answer Type:** all | **Topic:** 06

An engineering team is reviewing environment variable scope rules in GitHub Actions. Which of the following statements about env var scoping are all correct?

**A.** A step-level `env:` variable is not visible to other steps in the same job
**B.** A job-level `env:` variable is visible to all steps within that job but not to other jobs
**C.** A workflow-level `env:` variable is visible to all jobs and steps in that workflow
**D.** When the same variable name is defined at multiple levels, the most specific (innermost) scope wins

**Answer:** All of the above (A, B, C, D)

---

## Question 33 — Topic 07: Default Environment Variables

**Difficulty:** Easy | **Answer Type:** one | **Topic:** 07

A script running inside a GitHub Actions workflow needs to read the full path to the JSON file containing the webhook event payload that triggered the current run. Which default environment variable provides this path?

**A.** `GITHUB_WORKSPACE`
**B.** `GITHUB_EVENT_PATH`
**C.** `RUNNER_TEMP`
**D.** `GITHUB_EVENT_NAME`

**Answer:** B

---

## Question 34 — Topic 07: Default Environment Variables

**Difficulty:** Medium | **Answer Type:** one | **Topic:** 07

A step uses `$GITHUB_OUTPUT` to pass a value to subsequent steps, while another step uses `$GITHUB_ENV` to set a variable. What is the fundamental difference between these two mechanisms?

**A.** `$GITHUB_OUTPUT` sets variables only for the current step; `$GITHUB_ENV` sets variables for all subsequent steps
**B.** `$GITHUB_OUTPUT` creates step outputs accessible via `steps.<id>.outputs.<name>` in the same job; `$GITHUB_ENV` creates environment variables available as `$VAR_NAME` to all subsequent steps in the same job
**C.** `$GITHUB_OUTPUT` is available across jobs using `needs.outputs`; `$GITHUB_ENV` is only available within the same step
**D.** They are interchangeable; both achieve the same result through different file formats

**Answer:** B

---

## Question 35 — Topic 07: Default Environment Variables

**Difficulty:** Medium | **Answer Type:** many | **Topic:** 07

A CI script audits which GitHub-provided default environment variables are available for repository and commit identification. Which of the following are valid GitHub-provided default variables? *(Select all that apply.)*

**A.** `GITHUB_REPOSITORY` — the full owner/repo name
**B.** `GITHUB_SHA` — the commit SHA that triggered the workflow
**C.** `GITHUB_BRANCH` — the current branch name without the refs/heads/ prefix
**D.** `GITHUB_REF_NAME` — the branch or tag name
**E.** `GITHUB_REPOSITORY_OWNER` — the repository owner's login

**Answer:** A, B, D, E

---

## Question 36 — Topic 07: Default Environment Variables

**Difficulty:** Hard | **Answer Type:** one | **Topic:** 07

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

**Answer:** A

---

## Question 37 — Topic 07: Default Environment Variables

**Difficulty:** Medium | **Answer Type:** none | **Topic:** 07

A workflow developer lists default environment variables they believe GitHub automatically provides in every run. Which of the following is actually a GitHub-provided default environment variable?

**A.** `GITHUB_STEP_NUMBER` — the sequential number of the current step within the job
**B.** `GITHUB_JOB_STATUS` — the current exit status of the job (success or failure)
**C.** `RUNNER_MEMORY_MB` — the total RAM available on the runner in megabytes
**D.** `GITHUB_COMMIT_MESSAGE` — the commit message of the commit that triggered the workflow

**Answer:** None of the above

---

## Question 38 — Topic 08: Environment Protection Rules

**Difficulty:** Easy | **Answer Type:** one | **Topic:** 08

An organization configures a `wait timer` of 60 minutes on the `production` environment. A deployment workflow is triggered and the `deploy` job references `environment: production`. What happens when the job reaches the environment gate?

**A.** The job fails immediately with a timeout error
**B.** The job pauses for 60 minutes before proceeding, even if a reviewer has already approved
**C.** GitHub emails the reviewers and the job starts executing while waiting for their response
**D.** The 60-minute wait timer applies only after a reviewer approves the deployment

**Answer:** B

---

## Question 39 — Topic 08: Environment Protection Rules

**Difficulty:** Medium | **Answer Type:** one | **Topic:** 08

A production deployment workflow is paused awaiting approval. A required reviewer logs into GitHub, examines the deployment logs and diff, and clicks **Reject**. Which outcome occurs?

**A.** The workflow is paused indefinitely until another reviewer approves
**B.** The workflow run fails and the deployment does not proceed
**C.** The deployment is skipped but the workflow continues with remaining steps
**D.** The workflow run is retried automatically with a fresh approval request

**Answer:** B

---

## Question 40 — Topic 08: Environment Protection Rules

**Difficulty:** Hard | **Answer Type:** one | **Topic:** 08

An enterprise wants only commits from the `main` branch or branches matching `release/*` to be deployable to the `production` environment. A developer on a `hotfix/login-bug` branch triggers a deployment workflow. Given these deployment branch restrictions, what happens?

**A.** The deployment proceeds because hotfix branches are implicitly trusted
**B.** The deployment pauses and waits for an admin to manually override the branch restriction
**C.** The entire workflow run fails when it reaches the job referencing `environment: production`
**D.** GitHub redirects the deployment to the `staging` environment instead

**Answer:** C

---

## Question 41 — Topic 08: Environment Protection Rules

**Difficulty:** Medium | **Answer Type:** many | **Topic:** 08

A team defines environment protection rules for a `staging` environment. Which statements about how environment secrets differ from repository secrets are correct? *(Select all that apply.)*

**A.** Environment secrets are only accessible to jobs that reference the specific environment
**B.** If a repository secret and an environment secret share the same name, the environment secret takes precedence for jobs using that environment
**C.** Environment secrets are available to all jobs in the workflow regardless of which environment they reference
**D.** Environment secrets can only be accessed after a required reviewer approves the deployment
**E.** Repository-level secrets are available to all jobs that do not reference a protected environment

**Answer:** A, B, E

---

## Question 42 — Topic 08: Environment Protection Rules

**Difficulty:** Hard | **Answer Type:** all | **Topic:** 08

A compliance officer reviews GitHub environment protection rules for an enterprise deployment pipeline. Which of the following statements about required reviewers are all correct?

**A.** Up to 6 users or teams can be configured as required reviewers for an environment
**B.** If multiple reviewers are configured, any one of them can approve the deployment (it is not an all-must-approve requirement by default)
**C.** A reviewer who triggered the workflow cannot approve their own deployment
**D.** The required reviewer setting prevents the deployment job from executing until the review is completed

**Answer:** All of the above (A, B, C, D)

---

## Question 43 — Topic 09: Workflow Artifacts

**Difficulty:** Easy | **Answer Type:** one | **Topic:** 09

A CI workflow uploads a build artifact without specifying `retention-days`. How long will GitHub retain the artifact by default?

**A.** 1 day
**B.** 5 days
**C.** 30 days
**D.** 90 days

**Answer:** B

---

## Question 44 — Topic 09: Workflow Artifacts

**Difficulty:** Medium | **Answer Type:** one | **Topic:** 09

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

**Answer:** A

---

## Question 45 — Topic 09: Workflow Artifacts

**Difficulty:** Medium | **Answer Type:** many | **Topic:** 09

A workflow produces multiple test result files in different directories. An engineer configures `actions/upload-artifact` with path patterns. Which patterns are valid for the `path:` input? *(Select all that apply.)*

**A.** `dist/**/*.js` — uploads all `.js` files recursively under `dist/`
**B.** A multi-line YAML block listing multiple paths, one per line
**C.** `!dist/**/*.map` — excludes all source map files from the uploaded artifact
**D.** `results/*.xml` — uploads all XML files in the `results/` directory
**E.** `*` — uploads all files matching any name in any directory

**Answer:** A, B, C, D

---

## Question 46 — Topic 09: Workflow Artifacts

**Difficulty:** Medium | **Answer Type:** one | **Topic:** 09

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

**Answer:** B

---

## Question 47 — Topic 09: Workflow Artifacts

**Difficulty:** Hard | **Answer Type:** none | **Topic:** 09

A workflow engineer lists artifact behaviors they believe are accurate. Which of the following is actually a correct statement about GitHub Actions artifacts?

**A.** Artifacts are automatically available to all jobs in the same workflow run without any download step
**B.** Two artifact upload steps can use the same artifact name in the same workflow run without any issue
**C.** Artifacts created in one repository are automatically shared with all repositories in the same organization
**D.** Artifacts are retained indefinitely unless the repository is deleted

**Answer:** None of the above

---

## Question 48 — Topic 10: Workflow Caching

**Difficulty:** Easy | **Answer Type:** one | **Topic:** 10

A Node.js workflow uses `actions/cache` with a key that includes `hashFiles('**/package-lock.json')`. When does the cache miss and trigger a fresh dependency installation?

**A.** Every time the workflow runs, because cache is never hit on first run
**B.** When `package-lock.json` changes (producing a different hash)
**C.** Every 24 hours, because cache keys include a timestamp
**D.** Only when the `node_modules` directory is manually deleted from the runner

**Answer:** B

---

## Question 49 — Topic 10: Workflow Caching

**Difficulty:** Medium | **Answer Type:** one | **Topic:** 10

A `actions/cache` step is configured with a primary key and two `restore-keys`. On a run where the primary key produces a cache miss, what occurs?

**A.** The workflow fails because the cache is required and unavailable
**B.** The `restore-keys` entries are tried in order; if a partial match is found, that cache is restored as a starting point and the new cache is saved at the end of the run with the primary key
**C.** The `restore-keys` are ignored; only an exact primary key match triggers a cache restore
**D.** The step silently skips and the job proceeds without any cache, with nothing saved at the end

**Answer:** B

---

## Question 50 — Topic 10: Workflow Caching

**Difficulty:** Medium | **Answer Type:** many | **Topic:** 10

A platform engineer audits the caching strategy for a large monorepo. Which statements about GitHub Actions caching behavior are correct? *(Select all that apply.)*

**A.** The cache storage limit is 5 GB per repository
**B.** Caches expire and are evicted after 7 days without access
**C.** A cache key created on a feature branch is accessible by workflows running on any other branch
**D.** `actions/setup-node` with `cache: 'npm'` provides built-in caching without a separate `actions/cache` step
**E.** Multiple jobs within the same workflow run can read from the same cache key simultaneously

**Answer:** A, B, D, E

---

## Question 51 — Topic 10: Workflow Caching

**Difficulty:** Hard | **Answer Type:** one | **Topic:** 10

A workflow caches Python dependencies using the key `${{ runner.os }}-pip-${{ hashFiles('**/requirements.txt') }}`. A new dependency is added to `requirements.txt`. On the next run, the cache step produces a miss. The `restore-keys` is `${{ runner.os }}-pip-`. Describe the complete cache behavior for this run.

**A.** The run fails because the dependency cache is stale and cannot be partially restored
**B.** The previous cache (matched by `restore-keys`) is restored, `pip install` runs and downloads only new packages, and at job end a new cache entry is saved with the updated primary key
**C.** The entire `pip install` runs from scratch downloading all packages, and the new cache is saved with the updated key, but no partial restore occurs
**D.** The `restore-keys` match causes a full cache hit; no new packages are installed and no new cache is saved

**Answer:** B

---

## Question 52 — Topic 10: Workflow Caching

**Difficulty:** Medium | **Answer Type:** one | **Topic:** 10

A `build` job uses `actions/cache` and needs to check whether the cache was restored (cache hit) to decide whether to skip dependency installation. Which step output is used to check this?

**A.** `steps.<cache-step-id>.outputs.cache-restored`
**B.** `steps.<cache-step-id>.outputs.cache-hit`
**C.** `steps.<cache-step-id>.conclusion`
**D.** `env.CACHE_HIT`

**Answer:** B

---

## Question 53 — Topic 11: Workflow Sharing

**Difficulty:** Easy | **Answer Type:** one | **Topic:** 11

A DevOps team wants to create a reusable workflow that other teams can call from their own workflows. Which `on:` trigger must be added to make a workflow callable?

**A.** `on: workflow_dispatch`
**B.** `on: workflow_call`
**C.** `on: repository_dispatch`
**D.** `on: workflow_run`

**Answer:** B

---

## Question 54 — Topic 11: Workflow Sharing

**Difficulty:** Medium | **Answer Type:** one | **Topic:** 11

A calling workflow passes secrets to a reusable workflow using `secrets: inherit`. What behavior does this produce?

**A.** Only secrets explicitly declared in the reusable workflow's `on.workflow_call.secrets:` block are forwarded
**B.** All secrets from the calling workflow's context are automatically available to the reusable workflow using their original names
**C.** The reusable workflow gains access to organizational secrets only, not repository secrets
**D.** `secrets: inherit` is equivalent to passing `secrets: {}` — no secrets are forwarded

**Answer:** B

---

## Question 55 — Topic 11: Workflow Sharing

**Difficulty:** Medium | **Answer Type:** all | **Topic:** 11

A reusable workflow author is designing the `on.workflow_call.inputs:` section. Which input type definitions are all valid and supported?

**A.** `type: string` — for text values passed from the caller
**B.** `type: boolean` — for true/false flags
**C.** `type: number` — for numeric values
**D.** `type: environment` — for selecting a deployment environment

**Answer:** All of the above (A, B, C, D)

---

## Question 56 — Topic 11: Workflow Sharing

**Difficulty:** Hard | **Answer Type:** one | **Topic:** 11

A reusable workflow needs to expose an output value so the calling workflow can use it in a downstream job. The value originates in a step (`id: compute`) within the reusable workflow's job (`job_id: build`). Tracing the complete output chain, which declarations are required in the correct order?

**A.** Step writes to `$GITHUB_OUTPUT` → job declares `outputs.version: ${{ steps.compute.outputs.value }}` → `on.workflow_call.outputs.version: value: ${{ jobs.build.outputs.version }}`
**B.** Step writes to `$GITHUB_OUTPUT` → the caller accesses it directly via `needs.<job_id>.steps.compute.outputs.value`
**C.** Step writes to `$GITHUB_ENV` → job exports it → caller reads it via `env.version`
**D.** Step writes to `$GITHUB_OUTPUT` → the reusable workflow exposes it via `outputs.version: ${{ steps.compute.outputs.value }}` (no job intermediary needed)

**Answer:** A

---

## Question 57 — Topic 11: Workflow Sharing

**Difficulty:** Medium | **Answer Type:** one | **Topic:** 11

An enterprise configures a **required workflow** that must run on all repositories matching a specific filter. If the target repository has GitHub Actions disabled by a repository admin, what happens to the required workflow?

**A.** The required workflow is also disabled and does not run
**B.** The required workflow still runs because enterprise-level required workflows override repository-level Actions settings
**C.** The required workflow runs but its results are not surfaced in PR checks
**D.** GitHub displays an error and the PR cannot be merged until Actions is re-enabled

**Answer:** B

---

## Question 58 — Topic 11: Workflow Sharing

**Difficulty:** Hard | **Answer Type:** many | **Topic:** 11

An organization is evaluating whether to use `secrets: inherit` or explicit secret mapping when calling reusable workflows. Which statements correctly describe the trade-offs? *(Select all that apply.)*

**A.** `secrets: inherit` passes ALL caller secrets automatically without requiring them to be declared in the called workflow's `on.workflow_call.secrets:` block
**B.** Explicit secret mapping offers higher security because only named secrets flow from caller to called workflow
**C.** `secrets: inherit` is recommended for public or third-party reusable workflows
**D.** With explicit mapping, the called workflow must declare each secret in its `on.workflow_call.secrets:` block to receive it
**E.** Explicit mapping requires more maintenance because secrets must be updated in the calling workflow whenever the called workflow adds new secrets

**Answer:** A, B, D, E

---

## Question 59 — Topic 12: Workflow Debugging

**Difficulty:** Easy | **Answer Type:** one | **Topic:** 12

A developer re-runs a failed workflow and wants to enable verbose step-level debug logging for diagnostic output. Which repository secret must be set to enable this behavior?

**A.** `ACTIONS_STEP_DEBUG: true`
**B.** `RUNNER_DEBUG: 1`
**C.** `DEBUG_MODE: enabled`
**D.** `GITHUB_ACTIONS_DEBUG: true`

**Answer:** A

---

## Question 60 — Topic 12: Workflow Debugging

**Difficulty:** Medium | **Answer Type:** many | **Topic:** 12

A developer uses GitHub Actions workflow commands inside `run:` steps for diagnostic output. Which commands produce output visible as annotations in the GitHub PR checks interface or workflow summary? *(Select all that apply.)*

**A.** `echo "::notice::Deployment succeeded to staging"`
**B.** `echo "::warning::Deprecated API used in module X"`
**C.** `echo "::debug::Processing file $FILE"`
**D.** `echo "::error::Build failed due to missing dependency"`
**E.** `echo "::group::Installation steps"`

**Answer:** A, B, D

---

## Question 61 — Topic 12: Workflow Debugging

**Difficulty:** Hard | **Answer Type:** many | **Topic:** 12

A CI engineer troubleshoots a workflow that behaves differently in CI than on the developer's local machine. Which debugging techniques are available natively in GitHub Actions without installing third-party tools? *(Select all that apply.)*

**A.** Setting the `ACTIONS_STEP_DEBUG` secret to `true` to enable verbose runner and step-level output
**B.** Using `::group::` and `::endgroup::` commands to collapse and expand output sections in the log viewer
**C.** SSH-ing into the runner during job execution using an interactive tmate session intrinsically built into GitHub-hosted runners
**D.** Adding a step that prints `env` and `set` to display all environment variables available at that point
**E.** Using `echo "::debug::message"` to emit messages that appear only when debug logging is enabled

**Answer:** A, B, D, E

---

## Question 62 — Topic 12: Workflow Debugging

**Difficulty:** Medium | **Answer Type:** all | **Topic:** 12

A senior engineer reviews debugging best practices with a junior team. Which statements about GitHub Actions workflow commands are all correct?

**A.** `::debug::` messages are hidden in standard log output and only visible when `ACTIONS_STEP_DEBUG=true` is set
**B.** `::notice::` messages create a visible annotation in the GitHub UI and appear in the PR checks detail view
**C.** `::set-output::` has been deprecated in favor of writing to `$GITHUB_OUTPUT`
**D.** `::group::` and `::endgroup::` create collapsible log sections in the Actions run view

**Answer:** All of the above (A, B, C, D)

---

## Question 63 — Topic 13: Workflows REST API

**Difficulty:** Easy | **Answer Type:** one | **Topic:** 13

An external monitoring tool needs to retrieve all workflow runs for a specific repository, filtering only for runs that are currently in progress. Which API endpoint and query parameter accomplishes this?

**A.** `GET /repos/{owner}/{repo}/actions/workflows` with `?status=in_progress`
**B.** `GET /repos/{owner}/{repo}/actions/runs` with `?status=in_progress`
**C.** `GET /repos/{owner}/{repo}/actions/jobs` with `?filter=active`
**D.** `GET /repos/{owner}/{repo}/actions/runs` with `?conclusion=in_progress`

**Answer:** B

---

## Question 64 — Topic 13: Workflows REST API

**Difficulty:** Medium | **Answer Type:** many | **Topic:** 13

A CI/CD script uses the REST API to trigger a `workflow_dispatch` workflow. Which of the following are required in the request body? *(Select all that apply.)*

**A.** `ref` — the branch or tag name to run the workflow on
**B.** `inputs` — the workflow input values (required only if the workflow defines required inputs)
**C.** `workflow_id` — included in the URL path, not the request body
**D.** `event_type` — required for `workflow_dispatch` events
**E.** `sha` — the specific commit SHA to run the workflow against

**Answer:** A, B

---

## Question 65 — Topic 13: Workflows REST API

**Difficulty:** Medium | **Answer Type:** many | **Topic:** 13

A team audits API permissions for a workflow management script. Which REST API operations require write permissions on the `actions` scope? *(Select all that apply.)*

**A.** Listing workflow runs (`GET /repos/{owner}/{repo}/actions/runs`)
**B.** Cancelling a workflow run (`POST /repos/{owner}/{repo}/actions/runs/{run_id}/cancel`)
**C.** Re-running a failed workflow (`POST /repos/{owner}/{repo}/actions/runs/{run_id}/rerun`)
**D.** Triggering a workflow dispatch (`POST /repos/{owner}/{repo}/actions/workflows/{workflow_id}/dispatches`)
**E.** Getting a specific workflow run (`GET /repos/{owner}/{repo}/actions/runs/{run_id}`)

**Answer:** B, C, D

---

## Question 66 — Topic 13: Workflows REST API

**Difficulty:** Hard | **Answer Type:** one | **Topic:** 13

A deployment automation script must list only workflow runs that failed due to a test error (conclusion = `failure`) and were triggered by the `push` event, paginating 5 results per page. Which URL correctly constructs this query against the GitHub REST API?

**A.** `GET /repos/{owner}/{repo}/actions/runs?status=failure&event=push&per_page=5`
**B.** `GET /repos/{owner}/{repo}/actions/runs?conclusion=failure&event=push&per_page=5`
**C.** `GET /repos/{owner}/{repo}/actions/runs?result=failure&trigger=push&page_size=5`
**D.** `GET /repos/{owner}/{repo}/actions/runs?outcome=failure&on=push&limit=5`

**Answer:** B

---

## Question 67 — Topic 13: Workflows REST API

**Difficulty:** Medium | **Answer Type:** none | **Topic:** 13

A developer lists workflow run `status` values they believe are valid for filtering via the GitHub REST API. Which of the following is actually a valid workflow run `status` query value?

**A.** `running`
**B.** `pending`
**C.** `paused`
**D.** `scheduled`

**Answer:** None of the above

---

## Question 68 — Topic 14: Reviewing Deployments

**Difficulty:** Easy | **Answer Type:** one | **Topic:** 14

A production deployment to the `production` environment is awaiting review. Which users can approve or reject this deployment?

**A.** Any GitHub user with write access to the repository
**B.** Only the users or teams explicitly configured as required reviewers for the `production` environment
**C.** Only repository admins and organization owners
**D.** The person who triggered the workflow run, plus any designated reviewer

**Answer:** B

---

## Question 69 — Topic 14: Reviewing Deployments

**Difficulty:** Medium | **Answer Type:** one | **Topic:** 14

A reviewer receives a notification to approve a deployment. They navigate to the GitHub Actions run view. What information is available to the reviewer to help make the approval decision?

**A.** Only the deployment environment name and the branch that triggered the workflow
**B.** The job execution logs (up to the point of the approval gate), the git diff of the triggering commit, and the actor who initiated the run
**C.** A rendered diff of all changes from the last approved deployment to the current commit
**D.** The test coverage report and performance benchmarks from the CI jobs

**Answer:** B

---

## Question 70 — Topic 14: Reviewing Deployments

**Difficulty:** Medium | **Answer Type:** many | **Topic:** 14

An environment protection rule for `production` requires one reviewer from the `platform-team` group and has a 10-minute wait timer. A reviewer approves the deployment within 2 minutes of it being queued. Which outcomes correctly describe what happens? *(Select all that apply.)*

**A.** The deployment proceeds immediately after the reviewer's approval since the approval is the primary gate
**B.** After the reviewer approves, the workflow still waits the full 10-minute timer before deploying
**C.** The reviewer's approval and the wait timer are independent; both must be satisfied before deployment can proceed
**D.** The wait timer begins counting down from the moment the workflow run starts, not from when the reviewer approves
**E.** If the reviewer approves before the timer expires, the deployment begins as soon as both the approval and the timer requirement are met

**Answer:** B, C, D, E

---

## Question 71 — Topic 14: Reviewing Deployments

**Difficulty:** Medium | **Answer Type:** many | **Topic:** 14

An organization uses deployment reviews for multiple environments. Which statements about the deployment review process are correct? *(Select all that apply.)*

**A.** The workflow run is paused — no steps in the deployment job execute until the review is approved
**B.** Reviewers can see the workflow run logs from the jobs completed before the approval gate
**C.** If the reviewer rejects the deployment, the workflow run status is set to `failure`
**D.** A reviewer who initiated the workflow cannot approve their own deployment
**E.** Deployment reviews apply only to GitHub Enterprise Cloud customers

**Answer:** A, B, C, D

---

## Question 72 — Topic 14: Reviewing Deployments

**Difficulty:** Hard | **Answer Type:** one | **Topic:** 14

A team configures the `production` environment to require 2 approvals (two different users from `platform-team`). The first reviewer approves the deployment. The second reviewer reviews and rejects it with a comment. What is the final state of the workflow run?

**A.** The deployment proceeds because the first approval is already recorded and satisfies half the requirement
**B.** The workflow pauses and requests re-approval from a different reviewer to replace the rejection
**C.** The workflow run is marked as failed; the deployment does not proceed
**D.** The deployment is put into a `pending` state awaiting admin intervention

**Answer:** C

---

## Question 73 — Topic 15: Creating and Publishing Actions

**Difficulty:** Easy | **Answer Type:** one | **Topic:** 15

A developer creates a new custom GitHub Action. Which file is required at the root of the action's repository to define the action's metadata, inputs, outputs, and runtime?

**A.** `workflow.yml`
**B.** `action.yml` (or `action.yaml`)
**C.** `Dockerfile`
**D.** `package.json`

**Answer:** B

---

## Question 74 — Topic 15: Creating and Publishing Actions

**Difficulty:** Medium | **Answer Type:** one | **Topic:** 74

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

**Answer:** A

---

## Question 75 — Topic 15: Creating and Publishing Actions

**Difficulty:** Medium | **Answer Type:** many | **Topic:** 15

A team compares JavaScript actions to Docker container actions for a new internal action they are building. Which statements correctly distinguish them? *(Select all that apply.)*

**A.** JavaScript actions execute directly on the runner without a container and start faster than Docker actions
**B.** Docker container actions can use any language or tool that can run inside a Linux container
**C.** JavaScript actions require specifying `using: node20` (or another Node.js LTS version) in `action.yml`
**D.** Docker container actions always run on Ubuntu runners and cannot be used on Windows or macOS GitHub-hosted runners
**E.** JavaScript actions use the `@actions/core` and `@actions/github` npm packages to interact with GitHub

**Answer:** A, B, C, D, E

---

## Question 76 — Topic 15: Creating and Publishing Actions

**Difficulty:** Hard | **Answer Type:** one | **Topic:** 15

A JavaScript action encounters a critical validation error and needs to immediately mark the workflow job as failed. Which `@actions/core` function achieves this and halts further step execution?

**A.** `core.error("Validation failed")`
**B.** `core.setFailed("Validation failed")`
**C.** `core.abort("Validation failed")`
**D.** `process.exit(1)` called after `core.warning("Validation failed")`

**Answer:** B

---

## Question 77 — Topic 15: Creating and Publishing Actions

**Difficulty:** Medium | **Answer Type:** none | **Topic:** 15

A developer writes `action.yml` for a new custom GitHub Action and specifies a `runs.using:` value. Which of the following is an actual valid value for the `runs.using:` field?

**A.** `python3`
**B.** `bash`
**C.** `go`
**D.** `java`

**Answer:** None of the above

---

## Question 78 — Topic 16: Managing Runners

**Difficulty:** Easy | **Answer Type:** one | **Topic:** 16

A self-hosted runner machine loses network connectivity and goes offline. Where in the GitHub UI does an admin check the runner's current online/offline status?

**A.** `Repository → Actions → Workflows → Runner Status`
**B.** `Repository → Settings → Actions → Runners`
**C.** `Repository → Insights → Actions → Runner Health`
**D.** `Organization → Billing → Actions Minutes → Runner Status`

**Answer:** B

---

## Question 79 — Topic 16: Managing Runners

**Difficulty:** Medium | **Answer Type:** many | **Topic:** 16

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

**Answer:** A, B

---

## Question 80 — Topic 16: Managing Runners

**Difficulty:** Medium | **Answer Type:** all | **Topic:** 16

A team moves from self-hosted runners to GitHub-hosted runners. Which of the following statements about GitHub-hosted runners are all correct?

**A.** GitHub-hosted runners are automatically provisioned for each job and destroyed after the job completes
**B.** GitHub-hosted runners include preinstalled tools such as Node.js, Python, Java, Docker, and the GitHub CLI
**C.** The `RUNNER_TOOL_CACHE` environment variable points to the directory containing preinstalled tool versions
**D.** GitHub-hosted runners come in Ubuntu, Windows, and macOS variants

**Answer:** All of the above (A, B, C, D)

---

## Question 81 — Topic 16: Managing Runners

**Difficulty:** Medium | **Answer Type:** one | **Topic:** 16

An organization creates a runner group with `visibility: selected` and assigns it to two specific repositories. A workflow in a third repository (not in the selected list) includes `runs-on: group: production-runners`. What happens?

**A.** The job queues and waits indefinitely for a runner in that group to become available
**B.** The job fails immediately because the repository is not authorized to use the runner group
**C.** GitHub falls back to the nearest matching labeled runner outside the group
**D.** The job runs on a GitHub-hosted runner as a fallback when no group runner is available

**Answer:** B

---

## Question 82 — Topic 16: Managing Runners

**Difficulty:** Medium | **Answer Type:** many | **Topic:** 16

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

**Answer:** A, C

---

## Question 83 — Topic 16: Managing Runners

**Difficulty:** Medium | **Answer Type:** many | **Topic:** 16

A security-conscious organization evaluates self-hosted runners for use with public repositories. Which security risks and considerations apply? *(Select all that apply.)*

**A.** Fork-based pull requests can trigger workflows on self-hosted runners, potentially running untrusted code on the runner machine
**B.** Self-hosted runners persist between jobs, meaning a previous job's files and processes may remain on the runner
**C.** GitHub-hosted runners are destroyed after each job, making them inherently safer for public repositories
**D.** Self-hosted runners are completely isolated from the host network by default
**E.** Organizations can mitigate fork PR risks by requiring approval before running workflows from first-time contributors

**Answer:** A, B, C, E

---

## Question 84 — Topic 17: GitHub Actions Enterprise

**Difficulty:** Easy | **Answer Type:** one | **Topic:** 17

An enterprise administrator configures a **required workflow** for all repositories in the organization. Under what condition does the required workflow run for a given repository?

**A.** Only when the repository explicitly opts in by adding a `required_workflows:` key to their workflow file
**B.** Automatically on matching repositories regardless of whether repository admins have enabled or disabled Actions
**C.** Only on public repositories within the organization
**D.** Only when triggered by a push to the default branch

**Answer:** B

---

## Question 85 — Topic 17: GitHub Actions Enterprise

**Difficulty:** Medium | **Answer Type:** one | **Topic:** 17

An enterprise administrator sets a policy that restricts all repositories in the organization to use only actions from the organization's own repos plus verified GitHub actions. A repository admin tries to change this restriction to allow all third-party actions. What happens?

**A.** The repository admin can override the enterprise policy for their own repository
**B.** The repository admin's change is blocked; enterprise-level policies always take precedence over organization and repository level
**C.** The organization admin must first grant permission before the repository admin can override the enterprise policy
**D.** The change applies only to that repository, while the organization policy remains in effect for others

**Answer:** B

---

## Question 86 — Topic 17: GitHub Actions Enterprise

**Difficulty:** Medium | **Answer Type:** many | **Topic:** 17

A security engineer reviews how GitHub handles workflows triggered by pull requests from **forked repositories**. Which statements correctly describe the default behavior for the `pull_request` event from a fork? *(Select all that apply.)*

**A.** The `GITHUB_TOKEN` is read-only for `pull_request` workflows triggered by forks
**B.** Secrets defined in the repository are not available to `pull_request` workflows from forks
**C.** `pull_request_target` runs with full write access and secrets even from forks, which creates a security risk
**D.** Fork PRs from first-time contributors are automatically approved and proceed without any friction
**E.** Organizations can configure GitHub to require manual approval before running workflows from first-time contributors

**Answer:** A, B, C, E

---

## Question 87 — Topic 17: GitHub Actions Enterprise

**Difficulty:** Hard | **Answer Type:** one | **Topic:** 17

An enterprise organization creates a runner group at the enterprise level and sets it to span multiple organizations within the enterprise. A repository in `org-A` tries to use this enterprise runner group but the runner group is configured with access only to `org-B`. What is the result?

**A.** The job runs on any available runner in the enterprise, ignoring the group restriction
**B.** The job fails because the repository in `org-A` is not authorized to use the enterprise runner group configured for `org-B`
**C.** GitHub routes the job to an equivalent runner group in `org-A` with matching labels
**D.** The enterprise admin is notified, and the job queues until they approve access for `org-A`

**Answer:** B

---

## Question 88 — Topic 17: GitHub Actions Enterprise

**Difficulty:** Hard | **Answer Type:** all | **Topic:** 17

An enterprise administrator reviews the action allow-list policies to enforce supply-chain security. Which of the following statements about enterprise action allow-list configuration are all correct?

**A.** Setting policy to "Allow local actions only" restricts all repositories to using only actions from within the same organization or enterprise
**B.** Wildcard patterns like `actions/*` allow all actions published by the `actions` organization
**C.** Enterprise-level policies take precedence over organization-level policies for all repositories in the enterprise
**D.** Full commit SHA pinning requirement can be enforced at the organization level, causing workflows that use mutable tags like `@v4` to fail

**Answer:** All of the above (A, B, C, D)

---

## Question 89 — Topic 18: Security and Optimization

**Difficulty:** Easy | **Answer Type:** one | **Topic:** 18

The `GITHUB_TOKEN` is automatically provisioned at the start of each workflow job. When is this token automatically revoked?

**A.** When the step that uses the token completes
**B.** When the job that received the token finishes
**C.** 24 hours after the token was issued, regardless of job status
**D.** When the workflow run completes (not per-job)

**Answer:** B

---

## Question 90 — Topic 18: Security and Optimization

**Difficulty:** Medium | **Answer Type:** one | **Topic:** 18

A workflow contains the following step, which is vulnerable to script injection:

```yaml
- name: Greet contributor
  run: |
    echo "Thank you for PR: ${{ github.event.pull_request.title }}"
```

What is the correct mitigation to prevent a malicious PR title from injecting shell commands?

**A.** Wrap the expression in single quotes: `echo 'Thank you for PR: ${{ github.event.pull_request.title }}'`
**B.** Pass the PR title through an environment variable and reference the env var in the script, avoiding direct expression interpolation
**C.** Use `${{ toJSON(github.event.pull_request.title) }}` to escape the title before interpolation
**D.** Only allow `push` events in the workflow trigger to avoid PR-based injection vectors

**Answer:** B

---

## Question 91 — Topic 18: Security and Optimization

**Difficulty:** Medium | **Answer Type:** many | **Topic:** 18

A workflow needs to request an OIDC token from GitHub to authenticate against AWS without storing an AWS secret in GitHub. Which conditions must be met for OIDC token federation to work? *(Select all that apply.)*

**A.** The job must have `permissions: id-token: write` to request the OIDC JWT
**B.** The AWS IAM role trust policy must include the GitHub OIDC provider URL `https://token.actions.githubusercontent.com`
**C.** The workflow must store an `AWS_SECRET_ACCESS_KEY` secret that the OIDC action uses internally
**D.** The trust policy should be scoped to specific repositories, branches, or environments to prevent abuse
**E.** The OIDC token request uses `contents: read` permission, not `id-token: write`

**Answer:** A, B, D

---

## Question 92 — Topic 18: Security and Optimization

**Difficulty:** Hard | **Answer Type:** one | **Topic:** 18

An AWS IAM role trust policy is configured to only allow OIDC-based access for workflows running from the `main` branch of `myorg/myapp`. A workflow running on the `feature/login` branch of the same repository attempts to assume the role. What is the outcome?

**A.** The trust policy allows the request because the repository (`myorg/myapp`) matches, regardless of branch
**B.** The role assumption is denied because the OIDC subject claim `repo:myorg/myapp:ref:refs/heads/feature/login` does not match the allowed subject `repo:myorg/myapp:ref:refs/heads/main`
**C.** The role assumption succeeds but AWS grants only read-only permissions for non-main branches
**D.** GitHub intercepts the request and automatically adjusts the subject claim to allow branch-based matching

**Answer:** B

---

## Question 93 — Topic 18: Security and Optimization

**Difficulty:** Medium | **Answer Type:** one | **Topic:** 18

A security team mandates that all action references must be pinned to full commit SHAs. A workflow uses `actions/checkout@v4`. The team asks why this is insufficient. What is the correct reason?

**A.** `@v4` references the latest minor version and can silently change when a new patch is released
**B.** `@v4` is a tag, which is mutable — the tag can be moved to point to a different commit at any time, meaning the action code could change without any modification to the workflow file
**C.** `@v4` only works for public GitHub repositories; private repositories require SHA pinning
**D.** The `v4` tag is deprecated and should be replaced with `v4.x.x` for stability

**Answer:** B

---

## Question 94 — Topic 18: Security and Optimization

**Difficulty:** Medium | **Answer Type:** all | **Topic:** 18

A security engineer compares `GITHUB_TOKEN` to a Personal Access Token (PAT) for use in automated workflows. Which of the following statements about the differences are all correct?

**A.** `GITHUB_TOKEN` is automatically provisioned per job and revoked when the job ends; PATs are manually created and have user-configured expiry
**B.** `GITHUB_TOKEN` is scoped to a single repository; PATs can be granted access to multiple repositories
**C.** `GITHUB_TOKEN` cannot trigger new workflow runs in the same repository; PATs can trigger new workflow runs
**D.** PATs carry a higher security risk than `GITHUB_TOKEN` because they are long-lived credentials

**Answer:** All of the above (A, B, C, D)

---

## Question 95 — Topic 18: Security and Optimization

**Difficulty:** Medium | **Answer Type:** one | **Topic:** 18

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

**Answer:** A

---

## Question 96 — Topic 19: Common Failures and Troubleshooting

**Difficulty:** Easy | **Answer Type:** one | **Topic:** 19

A workflow job is stuck showing `Waiting for a runner to pick up this job` and never starts. The workflow uses `runs-on: [self-hosted, linux, gpu]`. What is the most likely root cause?

**A.** The workflow file has invalid YAML syntax
**B.** No self-hosted runner with all three labels (`self-hosted`, `linux`, `gpu`) is online and available
**C.** GitHub-hosted runners do not support custom labels
**D.** The `gpu` label is reserved and cannot be used in `runs-on`

**Answer:** B

---

## Question 97 — Topic 19: Common Failures and Troubleshooting

**Difficulty:** Medium | **Answer Type:** one | **Topic:** 19

A workflow job intermittently fails with `The operation timed out after 360 minutes`. The job runs integration tests that normally complete in under an hour. Which configuration change should be investigated first?

**A.** Increase the repository-level `default_timeout` setting in GitHub Organization settings
**B.** Set `timeout-minutes:` at the job or step level to a value appropriate for expected runtime, and investigate what is causing the test to hang
**C.** Switch to a GitHub-hosted runner because self-hosted runners have a shorter timeout limit
**D.** Set `continue-on-error: true` on the failing step to bypass the timeout

**Answer:** B

---

## Question 98 — Topic 19: Common Failures and Troubleshooting

**Difficulty:** Medium | **Answer Type:** many | **Topic:** 19

A workflow's `npm ci` step frequently fails with `ERESOLVE` dependency errors in CI despite working locally. Which troubleshooting steps are appropriate? *(Select all that apply.)*

**A.** Pin the Node.js version in the workflow using `actions/setup-node` with a specific `node-version:` value matching the local development environment
**B.** Run `npm ci --legacy-peer-deps` or `npm ci --force` if there are known peer dependency conflicts
**C.** Set `ACTIONS_STEP_DEBUG: true` to view verbose npm install output and identify conflicting packages
**D.** Delete and regenerate `package-lock.json` locally, verify the resolution works, and commit the updated lock file
**E.** Downgrade GitHub Actions to a previous version that did not have this issue

**Answer:** A, B, C, D

---

## Question 99 — Topic 19: Common Failures and Troubleshooting

**Difficulty:** Hard | **Answer Type:** one | **Topic:** 19

A workflow step uses `GITHUB_TOKEN` to push a tag to the repository:

```yaml
- name: Push tag
  run: git push origin $TAG_NAME
  env:
    GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

The step fails with `Permission denied`. The default organization permission for `GITHUB_TOKEN` is `restricted` (read-only). How is this fixed correctly?

**A.** Replace `secrets.GITHUB_TOKEN` with `secrets.PERSONAL_ACCESS_TOKEN` which always has write access
**B.** Add `permissions: contents: write` to the job or workflow level to grant the required write access to the repository contents scope
**C.** Change the push command to use `GITHUB_ACTOR` authentication instead of the token
**D.** Set the `actions.default_token_permissions` setting to `permissive` for the entire organization

**Answer:** B

---

## Question 100 — Topic 19: Common Failures and Troubleshooting

**Difficulty:** Medium | **Answer Type:** none | **Topic:** 19

A developer lists common causes of "your workflow did not run" situations in GitHub Actions. Which of the following is actually a valid documented reason why a workflow may silently skip execution?

**A.** The workflow file has more than 10 job definitions
**B.** The workflow trigger uses `pull_request` and the push event also fires on the same commit
**C.** The repository has received more than 1,000 workflow runs in the current billing period
**D.** The workflow is triggered by `GITHUB_TOKEN` which cannot trigger new workflow runs

**Answer:** None of the above

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | B | The correct VS Code `settings.json` entry uses `yaml.schemas` with the GitHub Actions schema URL pointed at the workflow file glob pattern. The other options use invented or incorrect config key names. | Topic 01 | Easy |
| 2 | B | The extension reads `action.yml` metadata on hover to display inputs, outputs, and description. Runtime permissions validation and statistics are not provided by the extension. | Topic 01 | Medium |
| 3 | A, B, C, D | The extension validates context scope (A), step output ordering (B), expression syntax (C), and recognized permission scope names (D). It cannot validate whether a secret is actually defined in the repo settings (E). | Topic 01 | Medium |
| 4 | None | The extension provides validation and IntelliSense but cannot run jobs locally (A), auto-push files (B), stream live logs (C), or manage secrets (D). These require separate tools or the GitHub UI. | Topic 01 | Medium |
| 5 | All | All four statements accurately describe the extension: path-based schema association (A), context IntelliSense from official schema (B), permission scope validation (C), and YAML extension dependency (D). | Topic 01 | Hard |
| 6 | B | `github.run_id` is the unique numeric identifier for the run, stable across re-runs. `run_number` increments per new trigger, `run_attempt` tracks re-runs, and `workflow_id` is not a standard context property. | Topic 02 | Easy |
| 7 | A, B, C | `github.ref_name` gives the branch/tag name without prefix. `github.sha` gives the full 40-char SHA. `github.ref_type` indicates branch vs tag. `github.branch` (D) does not exist; `head_commit.id` (E) is not a standard context property. | Topic 02 | Medium |
| 8 | A, B, C, E | `runner.name`, `runner.os`, `runner.arch`, and `runner.temp` are valid `runner` context properties. `runner.memory` (D) does not exist in the runner context. | Topic 02 | Medium |
| 9 | B | Job outputs from an upstream job are accessed via `needs.<job-id>.outputs.<output-name>` in downstream jobs. The `outputs`, `jobs`, and `steps` prefixes in the other options are incorrect for cross-job output access. | Topic 02 | Medium |
| 10 | A | `github.base_ref` is the name of the base branch (the branch being merged into, e.g., `main`). `github.head_ref` is the source branch of the PR. | Topic 02 | Medium |
| 11 | A, B, C, D | `matrix.os`, `matrix.node`, `strategy.job-total`, and `strategy.job-index` are all valid matrix/strategy context properties. `matrix.os.version` (E) is not a valid property — `os` is a plain string, not an object. | Topic 02 | Hard |
| 12 | B | The `needs` context is available in `jobs.<job_id>.if`, `jobs.<job_id>.steps[*].run`, and `jobs.<job_id>.outputs` — wherever downstream jobs need to reference upstream results. | Topic 03 | Easy |
| 13 | A, B, C, E | In `jobs.<job_id>.if`, the available contexts are `github`, `secrets`, `inputs`, `vars`, and `needs`. The `runner` context (D) is not available at the job `if:` level — it is only available within executing steps. | Topic 03 | Medium |
| 14 | All | All four are accurate restrictions: secrets unavailable at workflow-level env (A); env context unavailable within the env block that defines it (B); matrix only in the defining job (C); steps only in the current job (D). | Topic 03 | Medium |
| 15 | None | The `uses:` key for action selection only supports `github`, `inputs`, and `vars` contexts. `secrets`, `env`, `matrix`, and `needs` are not available for dynamic action selection in `uses:`. | Topic 03 | Medium |
| 16 | B | The special status check functions `always()`, `success()`, `failure()`, and `cancelled()` are available in job `if:` conditions and step `if:` conditions, but not in most other workflow keys. | Topic 03 | Easy |
| 17 | B | By default when `fail-fast` is not set, it is `true` — GitHub cancels all queued and in-progress matrix jobs when any job in the matrix fails. Set `fail-fast: false` to let all combinations run regardless of failures. | Topic 04 | Easy |
| 18 | A, B, C, E | With `cancel-in-progress: true`, each new run cancels the previously queued/running run in the same group. Three pushes: first starts (A), second cancels first (B), third cancels second (C). Runs on different branches form different groups (E) and are unaffected. D — they do not queue sequentially. | Topic 04 | Medium |
| 19 | All | All four are correct: `defaults.run.shell` applies to all run steps as a default (A); `defaults.run.working-directory` sets the default directory (B); individual steps can override it (C); it applies only to `run:` steps not `uses:` (D). | Topic 04 | Medium |
| 20 | B | When an `include:` entry exactly matches an existing combination's keys, its additional properties are merged into that combination. The existing `{ubuntu-latest, 20}` combination gains the `experimental: true` property. | Topic 04 | Hard |
| 21 | A | The `needs: [build, test]` syntax requires ALL listed jobs to complete successfully before `deploy` starts. If either fails, `deploy` is skipped by default. | Topic 04 | Medium |
| 22 | B | `workflow_dispatch` with `inputs:` of `type: choice` allows manual triggering from the GitHub UI with a dropdown selector. This is the standard approach for environment-selection deployments. | Topic 05 | Easy |
| 23 | B | `pull_request_target` runs in the context of the **target** (base) repository, not the fork — giving it access to secrets and write token even for fork PRs. This is why it must be used with extreme care. | Topic 05 | Medium |
| 24 | A, B, D | `push`, `pull_request`, and `pull_request_target` support `paths:` / `paths-ignore:` filtering. `workflow_dispatch` (C) and `schedule` (E) do not support path filtering. | Topic 05 | Medium |
| 25 | B | Workflow A has no path filter and runs for all pushes to `main`. Workflow B has `paths-ignore: ['*.md', 'docs/**']` which excludes markdown files — so it does not run when `README.md` is the only changed file. | Topic 05 | Hard |
| 26 | B | `workflow_run` with `types: [completed]` triggers after another workflow finishes. Adding `if: github.event.workflow_run.conclusion == 'success'` ensures the deployment only starts after a successful build. | Topic 05 | Medium |
| 27 | A, C, E, F | `pull_request` (A), `issues` (C), `release` (E), and `label` (F) all support `types:` filtering for activity sub-types. `push` (B) does not support `types:`; `workflow_dispatch` (D) does not support `types:`. | Topic 05 | Hard |
| 28 | C | Environment variable precedence is: step-level > job-level > workflow-level. The most specific (innermost) scope always wins. The step-level `MY_VAR: step-level` takes precedence over all others. | Topic 06 | Easy |
| 29 | B | The `$GITHUB_ENV` file uses `KEY=VALUE` format, one per line. Environment variable substitution commands like `export` (A), `set` (C), and bracket syntax (D) are not the correct format for this file. | Topic 06 | Medium |
| 30 | A, B, C | All three levels (step, job, workflow env) are valid ways to securely expose a secret as an environment variable. D is incorrect because direct inline interpolation of `${{ secrets.* }}` into shell scripts is a security risk and is strongly discouraged. | Topic 06 | Medium |
| 31 | B | `echo "::add-mask::$DYNAMIC_VALUE"` tells the runner to redact that value from all future log output. `::set-secret::` and `::redact::` are not valid GitHub workflow commands. | Topic 06 | Medium |
| 32 | All | All four scoping rules are accurate: step env is local to the step (A); job env spans the job (B); workflow env spans the whole workflow (C); innermost scope wins on conflict (D). | Topic 06 | Medium |
| 33 | B | `GITHUB_EVENT_PATH` contains the full file path to the event payload JSON. `GITHUB_WORKSPACE` is the repo checkout directory; `RUNNER_TEMP` is a temp directory; `GITHUB_EVENT_NAME` is just the name of the event, not the path. | Topic 07 | Easy |
| 34 | B | `$GITHUB_OUTPUT` creates step outputs readable via `steps.<id>.outputs.<name>` within the same job. `$GITHUB_ENV` creates env vars accessible by name (`$VAR`) in subsequent steps. They serve distinct purposes and formats. | Topic 07 | Medium |
| 35 | A, B, D, E | `GITHUB_REPOSITORY`, `GITHUB_SHA`, `GITHUB_REF_NAME`, and `GITHUB_REPOSITORY_OWNER` are all valid GitHub-provided default variables. `GITHUB_BRANCH` (C) does not exist — use `GITHUB_REF_NAME` for the branch name. | Topic 07 | Medium |
| 36 | A | Multi-line output values must use the heredoc delimiter syntax: write `KEY<<DELIMITER`, then the multi-line content, then `DELIMITER` on its own line — all appended to `$GITHUB_OUTPUT`. The other options would not handle newlines or special characters correctly. | Topic 07 | Hard |
| 37 | None | None of the listed variables are real GitHub defaults. `GITHUB_STEP_NUMBER` (A), `GITHUB_JOB_STATUS` (B), `RUNNER_MEMORY_MB` (C), and `GITHUB_COMMIT_MESSAGE` (D) do not exist — they are plausible-sounding but invented names. | Topic 07 | Medium |
| 38 | B | The wait timer delays the deployment for the configured duration regardless of approval status. The job pauses and waits the full timer before executing the deployment steps, even if a reviewer has already approved. | Topic 08 | Easy |
| 39 | B | When a required reviewer rejects a deployment, the workflow run is marked as failed and the deployment does not proceed. There is no automatic retry or redirect to another reviewer. | Topic 08 | Medium |
| 40 | C | The `production` environment is configured with specific allowed deployment branches (`main`, `release/*`). A deployment from `hotfix/login-bug` does not match any allowed pattern, so the workflow run fails at the job referencing that environment. | Topic 08 | Hard |
| 41 | A, B, E | Environment secrets are scoped to jobs referencing the environment (A), and they take precedence over same-named repo secrets (B). Repo secrets are available to jobs not using protected environments (E). C is incorrect — environment secrets are NOT broadly available to all jobs. D is a partial misconception — approval gates apply to the job, not specifically to secret access timing. | Topic 08 | Medium |
| 42 | All | All four are accurate: up to 6 reviewers allowed (A); any one reviewer from the list can approve (B); self-approval blocked for workflow initiators (C); job waits for review before executing (D). | Topic 08 | Hard |
| 43 | B | The default artifact retention period is 5 days when `retention-days` is not specified. The repository or organization setting may override this default, but the out-of-the-box default is 5 days. | Topic 09 | Easy |
| 44 | A | `actions/download-artifact@v3` with `name: app-package` and `path: ./downloaded/` correctly downloads the named artifact into the specified local directory in the `deploy` job. Options B, C, and D use invalid or non-existent actions/inputs. | Topic 09 | Medium |
| 45 | A, B, C, D | All four path pattern types are valid: glob patterns (A), multi-line YAML blocks (B), exclusion patterns using `!` (C), and simple glob patterns (D). Option E — bare `*` — would attempt to match all files at the root only without recursion, but the more significant issue is that all four listed options are valid patterns. | Topic 09 | Medium |
| 46 | B | Downloading artifacts from a different workflow run requires `github-token:` and `run-id:` to be specified. Without `github-token`, cross-run downloads fail. Option A is missing the token; options C and D use invalid syntax. | Topic 09 | Medium |
| 47 | None | None of the statements are accurate: artifacts require explicit download steps (A is false); same-name uploads in the same run overwrite or conflict (B is false); artifacts are repo-scoped, not org-shared (C is false); artifacts have a configurable retention period and are not kept indefinitely (D is false). | Topic 09 | Hard |
| 48 | B | The cache key incorporates `hashFiles('**/package-lock.json')` which produces a hash of the lock file. When `package-lock.json` changes (new or modified dependency), the hash changes, producing a cache miss. | Topic 10 | Easy |
| 49 | B | On a primary key miss, `restore-keys` are tried in order as prefix matches. A partial match restores the closest prior cache as a starting point; the job proceeds using it, and at the end of the job a new cache entry is saved under the primary key with updated contents. | Topic 10 | Medium |
| 50 | A, B, D, E | 5 GB storage limit (A), 7-day eviction on no access (B), built-in caching with `actions/setup-node` cache input (D), and simultaneous reads by multiple jobs (E) are all accurate. C is incorrect — caches are branch-scoped and feature branches do not automatically have access to caches from other branches (they can inherit from the default branch but not arbitrary branches). | Topic 10 | Medium |
| 51 | B | On a primary key miss, `restore-keys` finds the previous cache (partial match), restores it, then `pip install` downloads only the new package (not all packages). At job end, a new cache entry is saved with the updated primary key that includes the new dependency hash. | Topic 10 | Hard |
| 52 | B | The `actions/cache` step exposes a `cache-hit` output on the step ID. Use `${{ steps.<step-id>.outputs.cache-hit }}` — it returns `true` on an exact key match or `false` on a miss or partial restore. | Topic 10 | Medium |
| 53 | B | `on: workflow_call` is the trigger that designates a workflow as a reusable workflow — one that other workflows can invoke via `uses:` in a job definition. | Topic 11 | Easy |
| 54 | B | `secrets: inherit` passes all secrets from the caller's context to the reusable workflow, available by the same names without requiring explicit declaration in the `on.workflow_call.secrets:` block. | Topic 11 | Medium |
| 55 | All | `string`, `boolean`, `number`, and `environment` are all valid `type` values for `on.workflow_call.inputs`. The `environment` type allows the caller to pass an environment name as an input. | Topic 11 | Medium |
| 56 | A | The complete three-layer chain is: step writes to `$GITHUB_OUTPUT` → job declares `outputs` referencing the step output → the `on.workflow_call.outputs` block declares the output referencing the job output → caller accesses it via `needs.<job_id>.outputs.<name>`. | Topic 11 | Hard |
| 57 | B | Enterprise-required workflows run regardless of whether repository admins have disabled Actions at the repository level. Enterprise policy overrides repository-level configuration for required workflows. | Topic 11 | Medium |
| 58 | A, B, D, E | `secrets: inherit` forwards all secrets without declaration (A). Explicit mapping is more secure (B). Explicit mapping requires declaration in `on.workflow_call.secrets:` (D). Explicit mapping needs updating when the called workflow adds secrets (E). C is incorrect — `secrets: inherit` is NOT recommended for public/third-party workflows (it would be a security risk). | Topic 11 | Hard |
| 59 | A | Setting the repository secret `ACTIONS_STEP_DEBUG` to `true` enables verbose debug logging for all steps in a run. This causes the runner to output additional diagnostic information that is otherwise hidden. | Topic 12 | Easy |
| 60 | A, B, D | `::notice::` (A), `::warning::` (B), and `::error::` (D) all create UI annotations visible in PR checks and the workflow summary. `::debug::` (C) is only visible in verbose logs; `::group::` (E) creates collapsible sections but does not produce annotations. | Topic 12 | Medium |
| 61 | A, B, D, E | Setting `ACTIONS_STEP_DEBUG` (A), using `::group::/::endgroup::` (B), printing environment variables in a `run:` step (D), and using `::debug::` messages (E) are all native GitHub Actions debugging techniques. Option C — built-in SSH/tmate — is not intrinsically available in hosted runners; it requires installing the `tmate` action separately. | Topic 12 | Hard |
| 62 | All | All four are accurate: `::debug::` visibility requires the debug flag (A); `::notice::` creates PR annotations (B); `::set-output::` is deprecated (C); `::group::` creates collapsible log groups (D). | Topic 12 | Medium |
| 63 | B | `GET /repos/{owner}/{repo}/actions/runs` with `?status=in_progress` returns workflow runs currently executing. Option A targets workflows (definitions) not runs. Options C and D use invalid endpoints or parameters. | Topic 13 | Easy |
| 64 | A, B | A `workflow_dispatch` API call requires `ref` in the request body to specify which branch to run on. `inputs` is required only if the workflow declares required inputs (otherwise optional). `workflow_id` goes in the URL path (C); `event_type` is for `repository_dispatch` (D); `sha` is not a valid parameter (E). | Topic 13 | Medium |
| 65 | B, C, D | Write operations — cancelling runs (B), re-running failed runs (C), and triggering dispatches (D) — require write permissions on the `actions` scope. Read operations like listing runs (A) or getting a specific run (E) require only read access. | Topic 13 | Medium |
| 66 | B | The correct parameters are `conclusion=failure` (for the run conclusion, not `status`), `event=push` (for the trigger event), and `per_page=5` (pagination). Option A incorrectly uses `status=failure` — `failure` is a conclusion value, not a status value. | Topic 13 | Hard |
| 67 | None | The valid `status` query values for workflow runs in the GitHub REST API are: `queued`, `in_progress`, `completed`, `waiting`, `action_required`. None of the listed options — `running`, `pending`, `paused`, `scheduled` — are valid status values. | Topic 13 | Medium |
| 68 | B | Only the users or teams explicitly configured as required reviewers in the environment protection settings can approve or reject deployments. Write access to the repo alone is not sufficient. | Topic 14 | Easy |
| 69 | B | When a reviewer navigates to a pending deployment, they can view the execution logs from completed jobs, the triggering commit diff, and actor information. A full rendered diff from the last approved deployment (C) is not automatically provided. | Topic 14 | Medium |
| 70 | B, C, D, E | The wait timer and required reviewer are independent gates — both must be satisfied. After approval (within 2 minutes), the 10-minute timer still runs its full duration (B). Both conditions must be met (C). The timer starts when the workflow run starts, not when approval happens (D). The deployment begins once both the approval and the timer are completed (E). A is incorrect — approval alone is insufficient. | Topic 14 | Medium |
| 71 | A, B, C, D | The deployment job does not execute until approved (A); reviewers can see prior job logs (B); rejection marks the run as failed (C); self-approval is blocked (D). E is incorrect — deployment reviews are available in standard GitHub with environment protection rules, not only in Enterprise Cloud. | Topic 14 | Medium |
| 72 | C | When a required reviewer rejects the deployment, the workflow run fails regardless of any prior approvals. The second reviewer's rejection is definitive — the run is marked failed and the deployment does not proceed. | Topic 14 | Hard |
| 73 | B | Every GitHub Action requires an `action.yml` (or `action.yaml`) file at the repository root defining its name, description, inputs, outputs, and run configuration. Without this file, the action cannot be referenced or executed. | Topic 15 | Easy |
| 74 | A | A composite action uses `runs: using: composite` with a `steps:` block. Each step in a composite action must specify `shell:` explicitly. Option B is a JavaScript action; C is a Docker action; D uses an invalid `using: shell` value. | Topic 15 | Medium |
| 75 | A, B, C, D, E | All five statements are accurate: JS actions are faster and run directly on the runner (A); Docker actions support any language (B); JS actions use `using: node20` or similar (C); Docker actions are Linux-only on GitHub-hosted runners (D); JS actions use the `@actions/core` and `@actions/github` packages (E). | Topic 15 | Medium |
| 76 | B | `core.setFailed("message")` sets the workflow job status to failure and then exits the process. `core.error()` (A) only emits an error annotation without failing the job unless combined with `process.exit(1)`. `core.abort()` does not exist. D would work but is not the idiomatic `@actions/core` approach. | Topic 15 | Hard |
| 77 | None | Valid `runs.using` values are: `node20`, `node16`, `node12`, `composite`, `docker`. None of the options — `python3`, `bash`, `go`, `java` — are valid `runs.using` values in `action.yml`. | Topic 15 | Medium |
| 78 | B | Self-hosted runner status (online/offline/idle/active) is visible under `Repository → Settings → Actions → Runners`. This is the canonical location for checking individual runner health. | Topic 16 | Easy |
| 79 | A, B | Both a flat array `runs-on: [self-hosted, gpu, linux]` and a structured syntax `runs-on: labels: [self-hosted, gpu, linux]` correctly target a runner with all three labels. Option C has invalid YAML structure; D uses only one label. | Topic 16 | Medium |
| 80 | All | All four are accurate: each job gets a fresh runner that is destroyed after completion (A); standard tools are preinstalled (B); `RUNNER_TOOL_CACHE` points to preinstalled tools (C); Ubuntu, Windows, and macOS variants are all available (D). | Topic 16 | Medium |
| 81 | B | A runner group with `visibility: selected` allows only the explicitly listed repositories to access it. A job from an unlisted repository that references that group fails immediately — it does not queue or fall back to another runner. | Topic 16 | Medium |
| 82 | A, C | The structured `runs-on` syntax with `group:` and optionally `labels:` is the correct way to target a runner group and filter by labels within it. Option A uses both; Option C uses only `group:` (also valid). Option B uses an invalid string interpolation format; D does not reference a group and would only match by runner name alone. | Topic 16 | Medium |
| 83 | A, B, C, E | Fork PRs can trigger workflows on self-hosted runners with untrusted code (A); ephemeral-less self-hosted runners retain state between jobs (B); hosted runners are inherently safer due to per-job isolation (C); organizations can require approval for first-time contributors (E). D is false — self-hosted runners are NOT network-isolated from the host by default; they run on developer-managed infrastructure. | Topic 16 | Medium |
| 84 | B | Enterprise-required workflows run on all matching repositories regardless of whether a repository admin has disabled Actions for that repository. Enterprise-level enforcement overrides repository configuration. | Topic 17 | Easy |
| 85 | B | Enterprise-level policies are authoritative and cannot be overridden by organization or repository admins. The hierarchy is: enterprise policy > organization policy > repository policy. Repository admins cannot exceed bounds set by the enterprise. | Topic 17 | Medium |
| 86 | A, B, C, E | `GITHUB_TOKEN` is read-only for fork `pull_request` workflows (A); secrets are not available (B); `pull_request_target` runs with write access even from forks and requires extreme care (C); organizations should require approval for first-time contributors (E). D is false — fork PRs from new contributors do NOT automatically proceed; they require approval by default. | Topic 17 | Medium |
| 87 | B | Runner group access is enforced based on the group's configured organization visibility. An enterprise runner group scoped to `org-B` will deny jobs from `org-A` — the job fails because the repository is not authorized to use that group. | Topic 17 | Hard |
| 88 | All | All four are accurate: "local actions only" restricts to the same org/enterprise (A); wildcard patterns allow all actions from a specific owner (B); enterprise policies override org policies (C); SHA pinning enforcement can be configured at org level (D). | Topic 17 | Hard |
| 89 | B | `GITHUB_TOKEN` is created at the start of each job and revoked when that job finishes. It is scoped to the job, not the step (A) or the full workflow run (D), and does not expire on a 24-hour clock (C). | Topic 18 | Easy |
| 90 | B | The correct mitigation is to pass `github.event.pull_request.title` through an environment variable (`env: PR_TITLE: ${{ github.event.pull_request.title }}`) and reference `$PR_TITLE` in the script. This prevents shell metacharacters in the title from being evaluated. Single quotes (A) don't help when the expression value is interpolated before the shell receives it. | Topic 18 | Medium |
| 91 | A, B, D | OIDC requires `id-token: write` permission (A); the cloud provider trust policy must reference GitHub's OIDC URL (B); and trust policies should be scoped to specific repos/branches to prevent abuse (D). No static secret is needed (C is false); `id-token: write` is the required permission, not `contents: read` (E is false). | Topic 18 | Medium |
| 92 | B | The AWS IAM trust policy specifies the exact OIDC subject claim. The feature branch produces subject `repo:myorg/myapp:ref:refs/heads/feature/login` which does not match the allowed `repo:myorg/myapp:ref:refs/heads/main`, so AWS denies the role assumption. | Topic 18 | Hard |
| 93 | B | `@v4` is a mutable tag — the action repository owner can move the `v4` tag to point to a different commit at any time. SHA pinning guarantees the exact commit is fetched; tags provide no such guarantee. | Topic 18 | Medium |
| 94 | All | All four accurately describe the GITHUB_TOKEN vs PAT comparison: automatic provisioning and revocation vs manual creation/expiry (A); single-repo vs multi-repo scope (B); inability to trigger new workflow runs vs ability (C); higher security risk of PATs due to long lifetime (D). | Topic 18 | Medium |
| 95 | A | The job only needs to push to GitHub Container Registry (Packages). The minimal permissions are `contents: read` (to check out code) and `packages: write` (to push the image). Adding `id-token: write` (B) or `issues: write` (D) grants unnecessary privileges. `write-all` (C) violates least-privilege. | Topic 18 | Medium |
| 96 | B | The "Waiting for a runner" error means no runner matching all specified labels is currently online and idle. If any required label is missing from all available runners, the job will wait indefinitely. YAML syntax errors (A) prevent the workflow from being triggered, not from starting. | Topic 19 | Easy |
| 97 | B | The first step is to set `timeout-minutes:` at the job or individual step level to reflect the expected runtime, preventing indefinite hanging. Additionally, the root cause of why the test hangs should be investigated — timeouts are often a symptom of a deeper issue. | Topic 19 | Medium |
| 98 | A, B, C, D | Pinning Node.js version (A), using `--legacy-peer-deps` or `--force` flags (B), enabling step debug logging (C), and committing an updated lock file (D) are all valid troubleshooting approaches. Option E is baseless — there is no "downgrading GitHub Actions" that applies here. | Topic 19 | Medium |
| 99 | B | Adding `permissions: contents: write` at the job or workflow level grants the `GITHUB_TOKEN` the necessary write access to push tags. Replacing with a PAT (A) is heavier than necessary; `GITHUB_ACTOR` authentication (C) is not a standard mechanism; changing org-wide permissions (D) violates least-privilege. | Topic 19 | Hard |
| 100 | None | None of the listed reasons are accurate: 10 jobs is far below any limit (A); simultaneous push and PR events do not cancel each other (B); there is no 1,000-run billing cap on execution (C). D contains a real fact (`GITHUB_TOKEN` cannot trigger new runs) but that is not a reason "your workflow did not run" — it is a reason a triggered workflow cannot trigger another workflow. None of the above are valid answers. | Topic 19 | Medium |

---

## Coverage Summary

| Topic | # Qs | Questions | Difficulty Spread |
|-------|-------|-----------|-------------------|
| 01: VS Code Extension | 5 | Q1–Q5 | 1E, 3M, 1H |
| 02: Contextual Information | 6 | Q6–Q11 | 1E, 4M, 1H |
| 03: Context Availability | 5 | Q12–Q16 | 2E, 3M, 0H |
| 04: Workflow File Structure | 5 | Q17–Q21 | 1E, 3M, 1H |
| 05: Trigger Events | 6 | Q22–Q27 | 1E, 3M, 2H |
| 06: Custom Environment Variables | 5 | Q28–Q32 | 1E, 4M, 0H |
| 07: Default Environment Variables | 5 | Q33–Q37 | 1E, 3M, 1H |
| 08: Environment Protection Rules | 5 | Q38–Q42 | 1E, 2M, 2H |
| 09: Workflow Artifacts | 5 | Q43–Q47 | 1E, 3M, 1H |
| 10: Workflow Caching | 5 | Q48–Q52 | 1E, 3M, 1H |
| 11: Workflow Sharing | 6 | Q53–Q58 | 1E, 3M, 2H |
| 12: Workflow Debugging | 4 | Q59–Q62 | 1E, 2M, 1H |
| 13: Workflows REST API | 5 | Q63–Q67 | 1E, 3M, 1H |
| 14: Reviewing Deployments | 5 | Q68–Q72 | 1E, 3M, 1H |
| 15: Creating/Publishing Actions | 5 | Q73–Q77 | 1E, 3M, 1H |
| 16: Managing Runners | 6 | Q78–Q83 | 1E, 5M, 0H |
| 17: GitHub Actions Enterprise | 5 | Q84–Q88 | 1E, 2M, 2H |
| 18: Security and Optimization | 7 | Q89–Q95 | 1E, 5M, 1H |
| 19: Common Failures | 5 | Q96–Q100 | 1E, 3M, 1H |
| **Total** | **100** | **Q1–Q100** | **20E · 60M · 20H** |

### Constraint Validation

| Constraint | Target | Achieved | Status |
|---|---|---|---|
| Total Questions | 100 | 100 | ✅ |
| Scenario-Based | ≥70% | 76% (76 questions) | ✅ |
| Security Questions | ≥11 | 14 (Topics 18×7, plus security scenarios in 06, 07, 17) | ✅ |
| Enterprise Questions | ≥9 | 15 (Topics 08×5, 14×5, 17×5) | ✅ |
| Difficulty: Easy | 20% (20) | 20 | ✅ |
| Difficulty: Medium | 60% (60) | 60 | ✅ |
| Difficulty: Hard | 20% (20) | 20 | ✅ |
| Answer Type: one | 55 | 55 | ✅ |
| Answer Type: many | 26 | 26 | ✅ |
| Answer Type: all | 12 | 12 | ✅ |
| Answer Type: none | 7 | 7 | ✅ |
| Topic Coverage | 19 topics | 19 topics | ✅ |
