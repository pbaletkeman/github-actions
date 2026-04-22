# GitHub Actions GH-200 Certification Exam — Question Bank (Iteration 7)

**Iteration**: 7

**Generated**: 2026-03-22

**Total Questions**: 100

**Difficulty Split**: 16 Easy / 80 Medium / 4 Hard

**Answer Types**: 100 `one`

---

## Questions

---

### Question 1 — VS Code Extension & Tooling

**Difficulty**: Easy
**Answer Type**: one
**Topic**: VS Code Extension primary benefit

**Question**:
What is the primary benefit of using the GitHub Actions VS Code extension?

- A) Syntax validation against GitHub's official schema
- B) Automatic deployment of workflows
- C) Real-time cost estimation
- D) Direct integration with GitHub Packages

---

### Question 2 — VS Code Extension & Tooling

**Difficulty**: Medium
**Answer Type**: one
**Topic**: VS Code Extension error indicators

**Scenario**:
You are authoring a workflow file in VS Code. The extension highlights a red squiggly line under `with:` inputs for an action you're using.

**Question**:
What does this indicate?

- A) The action syntax is correct but uses deprecated parameters
- B) The input name is invalid or not recognized by the action
- C) The workflow file has minor formatting issues that won't affect execution
- D) The extension requires a configuration update

---

### Question 3 — VS Code Extension & Tooling

**Difficulty**: Medium
**Answer Type**: one
**Topic**: VS Code Extension YAML schema validation setup

**Scenario**:
Your team is onboarding a new project and wants to enable schema validation for GitHub Actions workflows in their VS Code workspace. They need to add YAML schema validation manually.

**Question**:
What should they do?

- A) Install the "YAML" extension and add schema mapping in settings.json
- B) Commit a `.vscode/settings.json` to the repository
- C) Run `code --install-extension github.vscode-github-actions`
- D) Both A and B

---

### Question 4 — VS Code Extension & Tooling

**Difficulty**: Easy
**Answer Type**: one
**Topic**: VS Code Extension IntelliSense context completions

**Question**:
When you type `${{` in a VS Code workflow file with the GitHub Actions extension active, what suggestions does IntelliSense provide?

- A) Only `github.*` context variables
- B) `github.*`, `secrets.*`, `env.*`, `inputs.*`, `matrix.*`, and `vars.*`
- C) All available GitHub API endpoints
- D) Previous step outputs only

---

### Question 5 — VS Code Extension & Tooling

**Difficulty**: Medium
**Answer Type**: one
**Topic**: VS Code Extension troubleshooting

**Scenario**:
A developer reports that the GitHub Actions extension stopped validating their workflow file. The YAML extension is installed, and the file is in `.github/workflows/`.

**Question**:
What is the first troubleshooting step you should recommend?

- A) Uninstall and reinstall both YAML and GitHub Actions extensions
- B) Reload the VS Code window (Ctrl/Cmd+Shift+P → Developer: Reload Window)
- C) Delete the `.git` directory and reinitialize
- D) Switch to a different editor

---

### Question 6 — Contextual Information

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Secrets context availability scope

**Question**:
In what scope is the `secrets` context available in a GitHub workflow?

- A) Workflow level only
- B) Job and step scopes
- C) Workflow, job, and step scopes
- D) Not available anywhere (must be loaded via environment variables)

---

### Question 7 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: run-name context support

**Scenario**:
You want to dynamically set the workflow run name based on the event type and branch.

**Question**:
Which contexts can you use in the `run-name` workflow key?

- A) `github`, `secrets`, `env`
- B) `github`, `inputs`, `vars`
- C) `github`, `env`, `inputs`, `vars`, `needs`
- D) All contexts

---

### Question 8 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Pull request base branch context variable

**Scenario**:
Your workflow needs to access the PR base branch name in a conditional job step. You're using a `pull_request` trigger.

**Question**:
Which context variable should you reference?

- A) `${{ github.ref_name }}`
- B) `${{ github.base_ref }}`
- C) `${{ inputs.base-branch }}`
- D) `${{ steps.checkout.outputs.base }}`

---

### Question 9 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Organization-level variables access

**Scenario**:
Your enterprise policy requires that all deployments include approval from senior engineers. You need to store the list of approved environments in an organization-level variable. In a deployment job, you need to reference this org-level `APPROVED_ENVS` variable.

**Question**:
How do you reference this organization-level variable?

- A) `${{ env.APPROVED_ENVS }}`
- B) `${{ vars.APPROVED_ENVS }}`
- C) `${{ org.vars.APPROVED_ENVS }}`
- D) `${{ secrets.APPROVED_ENVS }}`

---

### Question 10 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Matrix context variable reference

**Scenario**:
You have a matrix build with three Node.js versions: [16, 18, 20]. In a conditional step, you need to run a special test only for version 18.

**Question**:
How would you reference the matrix value?

- A) `if: ${{ matrix == 18 }}`
- B) `if: ${{ matrix.node-version == '18' }}`
- C) `if: ${{ env.MATRIX_NODE_VERSION == '18' }}`
- D) `if: contains(matrix, '18')`

---

### Question 11 — Context Availability Reference

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contexts available at job-level if conditional

**Question**:
Which of the following contexts is available in the `jobs.<job_id>.if` conditional?

- A) `github`, `secrets`, `inputs`, `vars`, `needs`
- B) `github`, `env`, `secrets`, `inputs`, `vars`, `needs`, `job`
- C) All contexts
- D) `github` and `vars` only

---

### Question 12 — Context Availability Reference

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Secrets context restriction in job-level conditionals

**Scenario**:
You're writing a workflow that needs to access a secret inside a conditional `if: github.actor` check. However, you notice the secret isn't available.

**Question**:
Why is the secret not accessible in this context?

- A) `github` context is not available in conditionals
- B) `secrets` context is not available at the job-level `if` key — it's only available at step level
- C) You must wrap the conditional in a `hashFiles()` function
- D) Secrets are always available; this shouldn't happen

---

### Question 13 — Context Availability Reference

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Needs context for job output reference

**Scenario**:
A deployment workflow needs to use a job output as context. The previous job named `build` produced an output `image-tag`.

**Question**:
How do you reference this output in the current job?

- A) `${{ needs.build.outputs.image-tag }}`
- B) `${{ steps.build.outputs.image-tag }}`
- C) `${{ job.outputs.image-tag }}`
- D) `${{ workflow.build.image-tag }}`

---

### Question 14 — Context Availability Reference

**Difficulty**: Medium
**Answer Type**: one
**Topic**: run_number uniqueness scope

**Scenario**:
In your workflow, you use `${{ github.run_number }}` to tag Docker images. You want to confirm this value is unique across all workflow runs in the repository.

**Question**:
Is this correct?

- A) Yes, `github.run_number` is globally unique across all repositories
- B) Yes, `github.run_number` is unique per repository
- C) No, `github.run_id` should be used for uniqueness
- D) No, `github.sha` is the only guaranteed unique identifier

---

### Question 15 — Context Availability Reference

**Difficulty**: Medium
**Answer Type**: one
**Topic**: server_url in GitHub Enterprise environments

**Scenario**:
Your workflow references `${{ github.server_url }}` to construct API URLs. Your organization uses GitHub Enterprise Server.

**Question**:
What value might `github.server_url` contain in a GitHub Enterprise environment?

- A) Always `https://github.com`
- B) `https://github.com` for cloud, `https://your-ghe-instance.com` for enterprise
- C) The runner's local IP address
- D) The SSH key fingerprint

---

### Question 16 — Workflow File Structure

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Multiple trigger events YAML syntax

**Question**:
What is the correct YAML syntax for specifying multiple trigger events with filters in a workflow?

- A) `on: [push, pull_request]`
- B) `on: push, 'pull_request'`
- C)
```yaml
on:
  push:
  pull_request:
```
- D) Both A and C

---

### Question 17 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workflow-level env with context interpolation

**Scenario**:
You need to set a workflow-level environment variable that uses both a context value and a static string. Your intent is to build an image name like `ghcr.io/myorg/myapp:v1.0`.

**Question**:
How would you structure this?

- A)
```yaml
env:
  IMAGE_NAME: ghcr.io/${{ github.repository }}:v1.0
```
- B)
```yaml
env:
  IMAGE_NAME: '${{ ghcr.io/github.repository:v1.0 }}'
```
- C)
```yaml
env:
  REGISTRY: ghcr.io
  IMAGE_NAME: $REGISTRY/${{ github.repository }}:v1.0
```
- D) Both A and C work, but A is cleaner

---

### Question 18 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Overriding workflow-level shell default

**Scenario**:
Your workflow uses `defaults.run.shell: pwsh` to specify PowerShell as the default shell for all `run` steps. A specific job needs to override this to use `bash`.

**Question**:
How do you override the default shell for that one job?

- A) Set `defaults.run.shell: bash` at the job level
- B) Add `shell: bash` to the individual `run` step
- C) Both A and B work
- D) You cannot override a workflow-level default

---

### Question 19 — Workflow File Structure

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Maximum concurrent jobs per workflow

**Question**:
What is the maximum number of concurrent jobs that can run from a single workflow file?

- A) 5
- B) 25
- C) 256
- D) No hard limit; it depends on your GitHub plan

---

### Question 20 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Enterprise compliance variable enforcement

**Scenario**:
An enterprise has a policy that requires all workflows to include a pre-defined environment variable `COMPLIANCE_LEVEL: strict` at the workflow level. Your team creates a new workflow that omits this.

**Question**:
What happens when the workflow runs?

- A) The workflow is rejected by GitHub and cannot be committed
- B) The workflow runs but without the compliance checks
- C) Enterprise required policies are enforced separately; manually adding this env is not enforced at commit time
- D) The workflow fails to dispatch

---

### Question 21 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Conditional syntax for commit message filtering

**Scenario**:
You want to ensure a job runs only when the commit message contains the text "deploy".

**Question**:
Which conditional syntax is correct?

- A) `if: contains(${{ github.event.head_commit.message }}, 'deploy')`
- B) `if: contains(github.event.head_commit.message, 'deploy')`
- C) `if: ${{ contains(github.event.head_commit.message, 'deploy') }}`
- D) All three are equivalent

---

### Question 22 — Workflow File Structure

**Difficulty**: Medium
**Answer Type**: one
**Topic**: workflow_call trigger for reusable workflows

**Scenario**:
Your workflow has 50 lines and is becoming difficult to maintain. You want to split it into multiple reusable workflows.

**Question**:
What YAML key should you use in the new shared workflow file to make it callable from another workflow?

- A) `on: workflow_call`
- B) `on: [workflow_call]`
- C)
```yaml
on:
  workflow_call:
```
- D) Both A and C

---

### Question 23 — Workflow Trigger Events

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Invalid workflow trigger events

**Question**:
Which of the following GitHub events is NOT a valid trigger event for a workflow?

- A) `push`
- B) `workflow_approval`
- C) `schedule`
- D) `repository_dispatch`

---

### Question 24 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Branch and path filter combination

**Scenario**:
Your team wants a workflow to run only when code is pushed to `main` or `develop` branches AND when files in the `src/` directory are modified.

**Question**:
Which configuration is correct?

- A)
```yaml
on:
  push:
    branches: [main, develop]
    paths: ['src/**']
```
- B)
```yaml
on:
  push:
    branches: [main, develop]
OR
    paths: ['src/**']
```
- C) Both A and B
- D) This combination is not possible

---

### Question 25 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Filtering out forked pull requests

**Scenario**:
A pull request trigger workflow should NOT run when pull requests are opened from forked repositories, as a security measure.

**Question**:
How do you prevent this?

- A) Add `if: github.event.pull_request.head.repo.full_name == github.repository`
- B) Add `if: github.event.pull_request.head.repo.fork == false` to the job
- C) Use organization policy to disable Actions for forks
- D) Both A and B work

---

### Question 26 — Workflow Trigger Events

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Tag-based deployment trigger pattern

**Scenario**:
Your organization has a workflow that deploys to production. To prevent inadvertent deployments from feature branches, you want this workflow to trigger ONLY on tags matching the pattern `v*.*.*`.

**Question**:
Which trigger syntax is correct?

- A)
```yaml
on:
  push:
    tags: ['v*.*.*']
```
- B)
```yaml
on:
  push:
    tags:
      - 'v[0-9].[0-9].[0-9]'
```
- C) Both A and B
- D) This pattern cannot be specified; GitHub requires exact tag names

---

### Question 27 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Nightly cron schedule syntax

**Scenario**:
You want a nightly integration test workflow to run every day at 2:00 AM UTC.

**Question**:
What is the correct cron syntax?

- A) `cron: '2 * * * *'`
- B) `cron: '0 2 * * *'`
- C) `cron: '0 2 0 0 0'`
- D) `cron: 'nightly'`

---

### Question 28 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Cross-repository enterprise compliance trigger

**Scenario**:
Your enterprise has multiple repositories and wants to trigger a centralized compliance scan whenever ANY repository in the organization receives a push.

**Question**:
Which trigger event and configuration best enables this?

- A) Use `repository_dispatch` with a webhook from each repository
- B) Use `workflow_run` to trigger from another workflow
- C) Use `push` event with organization-level required workflows
- D) Both A and C

---

### Question 29 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: workflow_dispatch input access

**Scenario**:
A deployment workflow is triggered by `workflow_dispatch` with user-provided inputs: `environment` (production/staging) and `version`.

**Question**:
How do you access these inputs in a step?

- A) `${{ github.event.inputs.environment }}`
- B) `${{ inputs.environment }}`
- C) `${{ env.ENVIRONMENT }}`
- D) Both A and B are correct (different context access paths)

---

### Question 30 — Workflow Trigger Events

**Difficulty**: Medium
**Answer Type**: one
**Topic**: pull_request_target security implications

**Scenario**:
Your workflow uses `pull_request_target` (instead of `pull_request`) to give write access to workflow secrets on PRs from forks.

**Question**:
What major security implication does this have, and how would you mitigate it?

- A) No security issue; `pull_request_target` is safer than `pull_request`
- B) Untrusted code from forked PRs can access your secrets and write to the repo; always review PR code before running it
- C) `pull_request_target` cannot be used with secrets at all
- D) GitHub automatically blocks `pull_request_target` in open-source repositories

---

### Question 31 — Custom Environment Variables

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Levels for defining custom environment variables

**Question**:
At which levels can you define custom environment variables in a GitHub Actions workflow?

- A) Workflow level only
- B) Workflow, job, and step levels
- C) Job and step levels only
- D) Global level (affecting all workflows)

---

### Question 32 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Overriding workflow-level environment variable at job level

**Scenario**:
Your workflow defines `NODE_ENV: production` at the workflow level, but a specific job needs `NODE_ENV: development`.

**Question**:
How do you override this at the job level?

- A)
```yaml
jobs:
  test-job:
    env:
      NODE_ENV: development
```
- B) You cannot override workflow-level variables at the job level
- C) Use a conditional `if` to skip the workflow-level definition
- D) Job-level variables shadow workflow-level ones automatically; no explicit override needed

---

### Question 33 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Secure API key injection via environment variable

**Scenario**:
You need to pass a sensitive API key to a step.

**Question**:
Should you define it as an environment variable, and if so, how?

- A) Yes, define it as `env: API_KEY: my-secret-value` in the step
- B) No, use `${{ secrets.API_KEY }}` directly in commands instead
- C) Yes, use `env: API_KEY: ${{ secrets.MY_API_KEY }}`
- D) Environment variables cannot contain secrets

---

### Question 34 — Custom Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: github.repository context value format

**Scenario**:
You're using `${{ github.repository }}` in a workflow-level environment variable definition.

**Question**:
What value does this produce?

- A) The repository name only (e.g., `my-repo`)
- B) The full repository path (e.g., `myorg/my-repo`)
- C) The repository URL (e.g., `https://github.com/myorg/my-repo`)
- D) This is not allowed — context cannot be used in environment variable definitions

---

### Question 35 — Custom Environment Variables

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Default GitHub-provided environment variables

**Question**:
Which of the following is a default GitHub-provided environment variable?

- A) `GITHUB_WORKFLOW`
- B) `GITHUB_RUN_ID`
- C) `GITHUB_ACTOR`
- D) All of the above

---

### Question 36 — Default Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: run_number behavior on re-run

**Scenario**:
Your workflow uses `${{ github.run_number }}` to create a build tag. A workflow run is re-run from the GitHub Actions UI.

**Question**:
Does `run_number` change on a re-run?

- A) Yes, it increments even for re-runs
- B) No, it remains the same for re-runs of the same workflow run
- C) No, you must use `${{ github.run_id }}` for unique re-run identification
- D) It depends on whether you're re-running from the GitHub UI or API

---

### Question 37 — Default Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Branch protection detection via default variable

**Scenario**:
A step needs to know if the current branch is protected.

**Question**:
Which default environment variable contains this information?

- A) `$GITHUB_REF_PROTECTED`
- B) `$GITHUB_PROTECTED_BRANCH`
- C) This information is not available in default variables; query the API
- D) `$GITHUB_BRANCH_PROTECTION`

---

### Question 38 — Default Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Enterprise version detection in workflows

**Scenario**:
Your enterprise workflow needs different configuration based on which GitHub Enterprise version is running.

**Question**:
How would you detect this?

- A) Check `${{ github.server_url }}` to identify the instance
- B) Use `${{ github.enterprise }}` context variable
- C) Use the GitHub API to query instance metadata
- D) Both A and C are viable approaches

---

### Question 39 — Default Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Unique artifact name using run context variables

**Scenario**:
You want to create a unique artifact name using the run number and attempt number.

**Question**:
What should the format be?

- A) `artifact-${{ github.run_number }}-${{ github.run_attempt }}`
- B) `artifact-$GITHUB_RUN_NUMBER-$GITHUB_RUN_ATTEMPT`
- C) Both A and B work in shell commands
- D) Only B is correct; context expressions cannot be used in artifact names

---

### Question 40 — Default Environment Variables

**Difficulty**: Medium
**Answer Type**: one
**Topic**: runner.os reliability across platforms

**Scenario**:
Your workflow uses `${{ runner.os }}` to conditionally run platform-specific steps in a matrix build.

**Question**:
On which operating systems does this reliably work?

- A) Linux only
- B) Linux and Windows
- C) Linux, Windows, and macOS
- D) Any OS supported by the runner

---

### Question 41 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Configuring reviewer approvals and branch restrictions

**Scenario**:
Your enterprise requires that any deployment to production must be approved by at least 2 senior engineers. Additionally, deployments can only occur from the `main` branch.

**Question**:
How do you configure these rules in GitHub?

- A) In repository Settings → Environments → [production] → Deployment branches and reviewers
- B) Through enterprise policy → Actions → Deployment approvals
- C) Both A and B
- D) This level of granular control is not possible in GitHub

---

### Question 42 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Revoking a deployment approval already granted

**Scenario**:
A reviewer approves a production deployment in a GitHub Actions workflow. The approval is recorded in the audit log. If the reviewer changes their mind and wants to deny the deployment that's already in progress, can they do so?

**Question**:
What options does the reviewer have?

- A) Yes, by modifying the approval record
- B) No, once approved the deployment cannot be stopped in that phase
- C) Yes, by canceling the entire workflow run
- D) No, approvals are immutable

---

### Question 43 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reviewer removed from team behavior on pending deployments

**Scenario**:
You configure an environment with "Required reviewers" set to a specific user. Later, that user leaves the company and you remove them from the team.

**Question**:
What happens to in-progress deployments awaiting their approval?

- A) They proceed automatically since the reviewer is no longer valid
- B) They fail because the specified reviewer is no longer available
- C) GitHub prompts you to reassign the approval
- D) You must manually cancel or retry these deployments

---

### Question 44 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Wait timer interaction with reviewer approval

**Scenario**:
You set up a 24-hour wait timer on the production environment. A developer triggers a deployment, and it's approved by reviewers after 12 hours.

**Question**:
What happens at the 24-hour mark?

- A) The deployment automatically proceeds
- B) The deployment is canceled
- C) The system prompts for final confirmation
- D) The workflow waits indefinitely; the timer is advisory only

---

### Question 45 — Environment Protection Rules

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Deployment branch restriction enforcement

**Scenario**:
Your enterprise has governance rules that allow deployments only from branches named `main` or `release/*`. A developer tries to deploy from a `feature/new-ui` branch to production.

**Question**:
What stops the deployment?

- A) The workflow fails because the branch doesn't match environment protection rules
- B) A reviewer rejects the deployment
- C) GitHub automatically creates a PR to merge into `main` first
- D) The deployment is queued until the developer manually approves the branch switch

---

### Question 46 — Workflow Artifacts

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Default artifact retention period

**Question**:
What is the default retention period for GitHub Actions artifacts?

- A) 1 day
- B) 5 days
- C) 30 days
- D) 90 days

---

### Question 47 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Uploading artifacts with custom retention

**Scenario**:
Your build job creates a `dist/` directory with compiled assets. You want to upload this as an artifact and make it available for 60 days.

**Question**:
Which action and configuration is correct?

- A)
```yaml
- uses: actions/upload-artifact@v3
  with:
    name: build-assets
    path: dist/
    retention-days: 60
```
- B)
```yaml
- uses: actions/upload-artifact@v3
  with:
    path: dist/
    ttl: 60d
```
- C) Both A and B work
- D) Retention > 5 days requires an enterprise license

---

### Question 48 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Downloading an artifact by name between jobs

**Scenario**:
Your test job needs to download an artifact uploaded by a previous build job named `build`. The artifact is named `test-results`.

**Question**:
How do you download it in the test job?

- A)
```yaml
- uses: actions/download-artifact@v3
  with:
    name: test-results
```
- B)
```yaml
- uses: actions/download-artifact@v3
  with:
    job-id: build
```
- C) Both A and B work
- D) You cannot download artifacts between jobs; only between workflow runs

---

### Question 49 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Enterprise repository artifact storage limit

**Scenario**:
Your enterprise workflow uploads multiple artifacts: `logs`, `reports`, and `metrics`, each 500 MB. Your repository has a 5 GB artifact storage limit.

**Question**:
When does the storage limit take effect?

- A) After the first artifact is uploaded
- B) After the total of all artifacts exceeds 5 GB
- C) Each artifact has its own 5 GB limit
- D) There is no per-repository limit; it's per-organization

---

### Question 50 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Artifact recovery after deletion

**Scenario**:
You upload a large compiled binary as an artifact but forget to set retention days. Seven days later, the artifact is no longer available.

**Question**:
Can you recover it?

- A) Yes, GitHub keeps a backup for 30 days
- B) No, the artifact is permanently deleted after the default 5-day retention
- C) Yes, by running the workflow again
- D) Yes, if you have enterprise backup enabled

---

### Question 51 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Artifact deletion and data permanence

**Scenario**:
Your confidential business analysis is uploaded as an artifact. You realize you made a mistake and want to ensure the artifact is completely removed from GitHub's systems.

**Question**:
Can you manually delete it, and what does deletion guarantee?

- A) Yes, GitHub permanently deletes artifacts after their retention expires
- B) Yes, you can manually delete the artifact immediately via the UI or API
- C) Manual deletion removes it from access but GitHub retains archived copies internally
- D) Artifacts cannot be deleted manually; you must contact support

---

### Question 52 — Workflow Artifacts

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Downloading artifacts from a previous workflow run

**Scenario**:
A deployment job needs to download an artifact from a previous workflow run (not the current run) to deploy an older version.

**Question**:
How do you specify this?

- A) Use `actions/download-artifact` with the `run-id` parameter pointing to the previous workflow run
- B) Artifacts from previous runs are not accessible; you can only use artifacts from the current run
- C) Use the GitHub API to query and download from prior runs
- D) Both A and C

---

### Question 53 — Workflow Caching

**Difficulty**: Easy
**Answer Type**: one
**Topic**: npm cache key file reference

**Question**:
For npm dependencies, what file does the cache action use to compute the cache key?

- A) `package.json`
- B) `package-lock.json`
- C) `node_modules/.cache`
- D) `.npmrc`

---

### Question 54 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Cache key invalidation on lockfile change

**Scenario**:
Your cache key is `${{ runner.os }}-npm-${{ hashFiles('**/package-lock.json') }}`. The `package-lock.json` file changes.

**Question**:
What happens to the cache?

- A) The cache is automatically updated in place with the new key
- B) GitHub detects the change and invalidates the old cache entry
- C) A new cache is created with the new key; the old cache is unused
- D) The old cache remains and your job uses the new one going forward

---

### Question 55 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Cache encryption at rest

**Scenario**:
Your cache contains sensitive build artifacts. You want to confirm whether the cache is encrypted at rest in GitHub.

**Question**:
Is the cache encrypted by default?

- A) Yes, all caches are encrypted by default
- B) No, caches are accessible in plaintext
- C) Only if you enable encryption in organization settings
- D) Caches are encrypted only if they contain less than 100 MB

---

### Question 56 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Restore key prefix fallback risks

**Scenario**:
You set up a cache restore key fallback: `${{ runner.os }}-npm-`. This will restore any cache key matching that prefix even if the exact key doesn't exist.

**Question**:
In what scenario is this dangerous?

- A) It can restore caches from different branches, leading to inconsistent dependencies
- B) It slows down the restore process significantly
- C) It's never dangerous; fallbacks are always safe
- D) It requires additional permissions to execute

---

### Question 57 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Cross-platform cache key isolation

**Scenario**:
A macOS runner caches `~/Library/Caches` using key `macos-build-cache`. Later, a Linux runner also tries to use this same cache key.

**Question**:
What happens?

- A) The cache is shared and used by the Linux runner, even though paths are different
- B) The cache key is automatically namespaced by OS, so no conflict occurs
- C) The job fails because platform-specific caches cannot be shared
- D) The Linux runner creates a new cache overwriting the same key

---

### Question 58 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Cache storage limit behavior

**Scenario**:
Your cache storage usage is at 4.8 GB out of a 5 GB limit. A new workflow run tries to save a 300 MB cache.

**Question**:
What happens?

- A) The cache is saved, temporarily exceeding the 5 GB limit
- B) GitHub automatically deletes the least-recently-used caches to make room
- C) The cache save fails, but the job continues
- D) The job fails due to insufficient cache storage

---

### Question 59 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Single vs separate cache actions for multiple paths

**Scenario**:
You want to cache both npm dependencies AND build output in the same workflow.

**Question**:
Should you use a single cache action with multiple paths or separate cache actions?

- A) A single cache action is more efficient
- B) Use separate cache actions for different paths; they have different invalidation needs
- C) Both approaches are equivalent
- D) You cannot cache multiple paths in a single workflow

---

### Question 60 — Workflow Caching

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Branch-isolated cache key strategy for enterprise

**Scenario**:
Your enterprise has decided that caches should be isolated per branch for security reasons, to prevent feature branches from using production build caches.

**Question**:
How do you implement branch-isolated caches?

- A) Include the branch name in the cache key: `${{ runner.os }}-${{ github.ref_name }}-npm-${{ hashFiles(...) }}`
- B) Use an organization policy to enforce cache isolation
- C) Caches are automatically isolated per branch by default
- D) This cannot be enforced; GitHub doesn't support per-branch cache isolation

---

### Question 61 — Workflow Sharing

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Reusable workflow call syntax

**Question**:
When you call a reusable workflow using `uses:`, what syntax should you use?

- A) `uses: { owner/repo/path/to/workflow.yml@ref }`
- B) `uses: owner/repo/.github/workflows/workflow-name.yml@ref`
- C) Both A and B
- D) The syntax depends on whether the workflow is internal or external

---

### Question 62 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Calling a reusable workflow with custom inputs

**Scenario**:
Your organization maintains a shared reusable workflow at `shared-workflows/.github/workflows/test.yml`. Another team wants to use it with custom inputs (`nodejs-version` and `test-command`).

**Question**:
How do you call it?

- A)
```yaml
jobs:
  test:
    uses: shared-workflows/.github/workflows/test.yml@main
    with:
      nodejs-version: '18'
      test-command: 'npm run test:e2e'
```
- B)
```yaml
jobs:
  test:
    uses: shared-workflows/test@main
    with:
      nodejs-version: '18'
```
- C) Both A and B
- D) You cannot pass custom inputs; reusable workflows use only defaults

---

### Question 63 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Passing secrets to a reusable workflow

**Scenario**:
Your reusable workflow needs to access a secret (`NPM_TOKEN`) that's defined in the calling repository.

**Question**:
How do you pass it to the reusable workflow?

- A)
```yaml
jobs:
  build:
    uses: org/shared-workflows/.github/workflows/build.yml@main
    secrets:
      NPM_TOKEN: ${{ secrets.NPM_TOKEN }}
```
- B) Secrets are automatically inherited by reusable workflows
- C) You cannot pass secrets between workflows; each must define its own
- D) Use `inherit: true` to pass all secrets automatically

---

### Question 64 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Private repository reusable workflow access conditions

**Scenario**:
Your team created a reusable workflow in a private repository. Other repositories in your organization want to call it.

**Question**:
What condition must be met?

- A) The private repository must allow cross-repository workflow access in its settings
- B) Calling repositories must use a GITHUB_TOKEN with sufficient permissions
- C) Access is restricted; private repository workflows can only be used in the same repo
- D) Both A and B

---

### Question 65 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Missing required input to a reusable workflow

**Scenario**:
You define a reusable workflow with a required input `environment` (production or staging) with no default value. A calling workflow forgets to provide this input.

**Question**:
What happens?

- A) The reusable workflow fails with an error about the missing required input
- B) The input defaults to the first value in the enum
- C) The workflow uses an empty string as the input value
- D) GitHub prompts for manual input at runtime

---

### Question 66 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Enterprise enforcement of shared reusable workflows

**Scenario**:
Your enterprise has 100 repositories. You create a centralized reusable workflow for compliance scanning in a shared repository and want all 100 repos to use it.

**Question**:
How do you enforce this?

- A) Each repository manually adds the `uses:` call (not scalable)
- B) Use enterprise required workflows to enforce the shared workflow on all repositories
- C) Create a GitHub App that automatically adds the workflow to all repos
- D) Both B and C are valid enforcement mechanisms

---

### Question 67 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Consuming reusable workflow outputs in subsequent jobs

**Scenario**:
A reusable workflow outputs a deployment URL. The calling workflow needs to use this output in a subsequent job.

**Question**:
How do you reference it?

- A) `${{ jobs.call-deploy.outputs.deployment-url }}`
- B) `${{ steps.call-deploy.outputs.deployment-url }}`
- C) Both A and B based on job naming
- D) Outputs from reusable workflows cannot be consumed by other jobs

---

### Question 68 — Workflow Sharing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Documenting required secrets in reusable workflows

**Scenario**:
Your reusable workflow accepts secrets but doesn't document what secrets it expects. Another team calls it and experiences failures.

**Question**:
What should you do to prevent similar issues in the future?

- A) Document required secrets in the reusable workflow's README
- B) Define a `secrets:` section in the reusable workflow definition with descriptions
- C) Both A and B
- D) Nothing; teams should read the source code

---

### Question 69 — Workflow Debugging

**Difficulty**: Easy
**Answer Type**: one
**Topic**: RUNNER_DEBUG effect on workflow output

**Question**:
What does enabling the `RUNNER_DEBUG` environment variable (or secret) do?

- A) Stops the workflow and opens an interactive debugger
- B) Produces verbose log output for each step execution
- C) Enables debug output only for failing steps
- D) Disables logs to improve performance

---

### Question 70 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: one
**Topic**: ACTIONS_STEP_DEBUG secret activation

**Scenario**:
Your GitHub Actions workflow fails mysteriously. To enable debug logging, you want to add a secret named `ACTIONS_STEP_DEBUG` set to `true`. The next workflow run should produce verbose output.

**Question**:
Is this correct?

- A) Yes, setting the secret and re-running the workflow enables debug mode
- B) No, you must set `ACTIONS_STEP_DEBUG` as an environment variable in the workflow, not a secret
- C) No, you must manually add debug output using `echo` commands
- D) Yes, but it only works if you re-run with the `--debug` flag

---

### Question 71 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Cross-platform environment variable listing

**Scenario**:
You want to diagnose why a specific step is failing. You add a diagnostic step that prints all environment variables. You need this to work across all operating systems in your matrix build.

**Question**:
Which command correctly does this while working across all operating systems?

- A) `echo $GITHUB_CONTEXT`
- B) `printenv` on Linux/macOS — or — `set` on Windows
- C) Use OS-specific conditionals with different commands for each platform
- D) GitHub does not support cross-platform environment variable debugging

---

### Question 72 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Automatic secret redaction in logs

**Scenario**:
Your workflow logs are producing verbose output. Accidentally, a secret value appears in plaintext in the logs. GitHub logs show it as `***`.

**Question**:
How does GitHub know to redact this value?

- A) GitHub automatically redacts any value that matches a defined secret
- B) GitHub uses pattern matching to detect likely secrets
- C) Secrets are not automatically redacted; you must use GitHub's masking functions
- D) This cannot happen; secrets are always automatically redacted at the platform level

---

### Question 73 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Conditional debug message output

**Scenario**:
You want to add a debug message that appears only when `RUNNER_DEBUG` is enabled.

**Question**:
How do you use the GitHub Actions logging functions?

- A) `echo "::debug::My debug message"`
- B) `echo "DEBUG: My debug message"`
- C) Use `core.debug("My debug message")` in a JavaScript action
- D) Both A and C

---

### Question 74 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Cleanup step that runs on cancellation

**Scenario**:
A step fails with a cryptic error. You add a cleanup step that should always run to capture the state. You also need it to run if the job is canceled.

**Question**:
How do you ensure the cleanup step runs even if the job is canceled?

- A) Use `if: always()`
- B) Use `if: failure()`
- C) Use `if: cancelled() || failure()`
- D) Use a `finally:` block (not valid GitHub Actions YAML)

---

### Question 75 — Workflow Debugging

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Auditing debug activity for enterprise compliance

**Scenario**:
Your organization needs to audit all workflow debugging activities for compliance. You need to determine when `RUNNER_DEBUG` was enabled.

**Question**:
Where should you look?

- A) Workflow run logs only
- B) Secret modification audit log in organization settings only
- C) Both A and B
- D) Debugging activities are not logged

---

### Question 76 — Workflows REST API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: REST API endpoint to list workflows

**Question**:
To list all workflows in a GitHub repository using the REST API, what endpoint should you use?

- A) `GET /repos/{owner}/{repo}/actions/workflows`
- B) `GET /repos/{owner}/{repo}/workflows`
- C) `GET /actions/workflows`
- D) `GET /repos/{owner}/{repo}/actions`

---

### Question 77 — Workflows REST API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: REST API workflow dispatch endpoint

**Scenario**:
Your automation script needs to trigger a workflow using the REST API. The workflow accepts inputs like `environment` and `version`.

**Question**:
What HTTP method and endpoint should you use?

- A) `POST /repos/{owner}/{repo}/actions/workflows/{workflow_id}/dispatches`
- B) `POST /repos/{owner}/{repo}/actions/workflows/{workflow_name}/trigger`
- C) `PUT /repos/{owner}/{repo}/actions/workflows/{workflow_id}`
- D) The REST API cannot trigger workflows; use GitHub CLI instead

---

### Question 78 — Workflows REST API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Querying workflow runs by branch and status

**Scenario**:
Your CI/CD automation script needs to query all workflow runs for a specific workflow file to find the last successful run on the `main` branch.

**Question**:
Which REST API endpoint and parameters should you use?

- A) `GET /repos/{owner}/{repo}/actions/workflows/{workflow_id}/runs` with `?branch=main&status=success`
- B) `GET /repos/{owner}/{repo}/actions/runs?workflow_id={workflow_id}&status=success`
- C) `GET /repos/{owner}/{repo}/actions/workflows/{workflow_id}?runs=true&branch=main`
- D) `GET /repos/{owner}/{repo}/actions/runs/{run_id}/workflow`

---

### Question 79 — Workflows REST API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: REST API endpoint to cancel a workflow run

**Scenario**:
A deployment workflow was triggered accidentally and is currently queued. Your automated monitoring system needs to cancel it via the REST API before it starts.

**Question**:
What is the correct HTTP method and endpoint to cancel a queued or in-progress workflow run?

- A) `DELETE /repos/{owner}/{repo}/actions/runs/{run_id}`
- B) `POST /repos/{owner}/{repo}/actions/runs/{run_id}/cancel`
- C) `PUT /repos/{owner}/{repo}/actions/runs/{run_id}` with `{"status": "cancelled"}`
- D) `PATCH /repos/{owner}/{repo}/actions/runs/{run_id}` with `{"status": "cancelled"}`

---

### Question 80 — Workflows REST API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Minimum token permissions for Actions REST API operations

**Scenario**:
Your enterprise automation uses a GitHub App token to interact with the Actions REST API. The script needs to list artifacts, cancel runs, and trigger workflows via `workflow_dispatch`.

**Question**:
What is the minimum set of permissions required for the GitHub App token?

- A) `actions: write` permission is sufficient for all three operations
- B) `actions: read` for listing; `actions: write` for cancel and trigger
- C) `workflow: write` for triggering; `actions: write` for cancel and list
- D) `repo: write` (full repository access) is required for all Actions API operations

---

### Question 81 — Reviewing Deployments

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Required reviewer behavior on job execution

**Question**:
What happens when a GitHub Actions job targets a deployment environment that has required reviewers configured?

- A) The job runs immediately and notifies the reviewers after completion
- B) The job pauses in a waiting state until a required reviewer approves or rejects the deployment
- C) The job fails immediately with a permissions error
- D) The job runs but the deployment is held in a pending state in the Deployments tab

---

### Question 82 — Reviewing Deployments

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Multiple independent required reviewers configuration

**Scenario**:
Your enterprise production environment requires sign-off from both a security engineer and a deployment engineer before any deployment proceeds. They must each independently approve.

**Question**:
How do you configure this requirement in GitHub environment settings?

- A) Add both the security engineer and deployment engineer as required reviewers; GitHub requires approval from each one
- B) Create two separate environments chained in sequence, each requiring one reviewer
- C) Use a composite action that calls a manual approval step before deploying
- D) Both B and C; option A does not support requiring multiple independent reviewers

---

### Question 83 — Reviewing Deployments

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Wait timer start time behavior with early reviewer approval

**Scenario**:
You configure a deployment environment with a wait timer set to 30 minutes. A workflow run is triggered and immediately passes all reviewers' approvals.

**Question**:
When can the deployment job actually start executing?

- A) 30 minutes after the workflow run was triggered, regardless of when approval was given
- B) 30 minutes after all required reviewers approve
- C) Immediately; wait timers only apply when there are no reviewers configured
- D) 30 minutes after the first deployment to that environment within the day

---

### Question 84 — Reviewing Deployments

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Restricting deployments to a specific branch

**Scenario**:
Your team wants to ensure only code from the `main` branch can be deployed to the production environment. A developer attempts to deploy from a `hotfix/urgent` branch.

**Question**:
How do you enforce branch restrictions for your production environment?

- A) Configure "Deployment branches" in environment settings to allow only `main`
- B) Add `if: github.ref == 'refs/heads/main'` to the deployment job
- C) Use a branch protection rule that prevents deployment from non-main branches
- D) Both A and B together are required to enforce the restriction

---

### Question 85 — Reviewing Deployments

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Defense-in-depth against deployment privilege escalation

**Scenario**:
A developer with write access to your repository creates a branch named `main-2` and modifies a workflow to bypass the production environment's required reviewers by targeting a less-restricted environment first.

**Question**:
Which combination of controls best mitigates this privilege escalation risk?

- A) Enable required reviewers on all environments
- B) Restrict deployment branches to `main` only on the production environment
- C) Audit workflow changes using branch protection rules that require reviews on workflow files
- D) All of the above: required reviewers, branch restrictions, and protected workflow file changes via branch protection

---

### Question 86 — Creating and Publishing Actions

**Difficulty**: Easy
**Answer Type**: one
**Topic**: GitHub Actions types

**Question**:
Which of the following is NOT one of the three types of GitHub Actions?

- A) JavaScript action
- B) Python action
- C) Docker container action
- D) Composite action

---

### Question 87 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Required action metadata file

**Scenario**:
You are creating a new JavaScript action for the GitHub Marketplace. The action accepts an `input-file` input and produces a `summary` output.

**Question**:
What file is required to define the action's metadata (name, description, inputs, outputs, and entry point)?

- A) `package.json`
- B) `action.yml` or `action.yaml`
- C) `index.js`
- D) `.github/action-definition.json`

---

### Question 88 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Publishing an action to the GitHub Marketplace

**Scenario**:
Your team has built a custom GitHub Action and wants to publish it to the GitHub Marketplace so others can use it.

**Question**:
What are the requirements to submit an action to the Marketplace?

- A) Store the action in a public repository with an `action.yml`, a README, and submit from the repository's Releases page
- B) Submit the action through GitHub CLI using `gh marketplace publish`
- C) Actions are automatically available on the Marketplace when pushed to a public repository
- D) You must have a paid GitHub plan to publish to the Marketplace

---

### Question 89 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Secure action version pinning to prevent supply chain attacks

**Scenario**:
Your organization uses several third-party GitHub Actions. You're reviewing workflow security and want to ensure that a third-party action cannot be silently updated by its author with malicious code.

**Question**:
Which referencing strategy best protects against this supply chain attack?

- A) Pin the action to a specific commit SHA (e.g., `uses: actions/checkout@abc123def456...`)
- B) Use a major version tag (e.g., `uses: actions/checkout@v3`)
- C) Always use `@latest` to get the most recent, actively maintained version
- D) Clone and fork the action into your own repository without pinning

---

### Question 90 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Setting action output values with @actions/core

**Scenario**:
You are writing a JavaScript action using `@actions/core`. Your action needs to produce an output value named `deployment-url` for use by subsequent jobs.

**Question**:
Which API call correctly sets this output?

- A) `core.setOutput('deployment-url', url)`
- B) `core.exportVariable('deployment-url', url)`
- C) `process.env['OUTPUT_DEPLOYMENT_URL'] = url`
- D) `core.info('deployment-url=' + url)`

---

### Question 91 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Passing data between steps in a composite action

**Scenario**:
You are building a composite action with multiple steps. Step 1 runs a shell script that produces a value you need to use in Step 3 of the same composite action.

**Question**:
How do you pass the Step 1 output to Step 3 within the composite action?

- A) Use a workflow-level environment variable set in `$GITHUB_ENV`
- B) Outputs are automatically available between steps within composite actions
- C) Give Step 1 an `id:` and reference `${{ steps.step1-id.outputs.output-name }}` in Step 3
- D) Composite actions cannot pass data between their internal steps

---

### Question 92 — Creating and Publishing Actions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Enterprise policy settings to restrict action usage

**Scenario**:
Your enterprise administrator needs to control which external actions can be used across all organization repositories for security compliance.

**Question**:
Which enterprise-level policy settings are available to restrict action usage?

- A) Allow all actions from any source
- B) Allow only actions created by GitHub (github.com/actions and github.com/github)
- C) Allow actions from specific verified creators and specific action repositories by pattern
- D) All of the above are valid policy settings that can be configured

---

### Question 93 — Managing Runners

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Key advantage of self-hosted runners

**Question**:
What is the key advantage of self-hosted runners over GitHub-hosted runners?

- A) They are free and provided by GitHub with no setup required
- B) They can access private networks, use custom hardware, and maintain persistent storage between jobs
- C) They automatically scale based on workflow demand
- D) They support more operating systems than GitHub-hosted runners

---

### Question 94 — Managing Runners

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Runner choice for private network database access

**Scenario**:
Your workflow jobs need to connect to an internal PostgreSQL database that is only reachable within your company's private network. Using GitHub-hosted runners, these jobs always fail with a connection timeout.

**Question**:
What is the recommended solution?

- A) Configure a VPN GitHub Action to connect the hosted runner to your private network at the start of each job
- B) Deploy self-hosted runners inside your private network that have direct access to the internal database
- C) Expose the internal database to the internet with IP allowlisting for GitHub's IP ranges
- D) Use environment variables to pass the database connection string to the GitHub-hosted runner

---

### Question 95 — Managing Runners

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Runner groups for team-level access control

**Scenario**:
Your enterprise has three teams with different security clearances: General, Finance, and Security. Finance and Security runners have access to sensitive credentials. Any team in the enterprise can currently access any runner.

**Question**:
How should you configure self-hosted runners to enforce team-level access control?

- A) Create separate GitHub organizations for each team
- B) Use runner groups at the organization or enterprise level to assign runners to specific teams or repositories
- C) Use labels to segregate runners; teams can only use labeled runners
- D) Self-hosted runner access cannot be restricted below the organization level

---

### Question 96 — Managing Runners

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Job queuing behavior when all labeled runners are busy

**Scenario**:
You assign the custom label `gpu-runner` to two self-hosted runners. A workflow specifies `runs-on: [self-hosted, gpu-runner]`. Both labeled runners are currently busy processing other jobs.

**Question**:
What happens to new workflow jobs with this `runs-on` specification?

- A) The job immediately fails with "no runners with matching labels found"
- B) The job is dispatched to any available runner, ignoring the label
- C) The job fails after a 10-minute timeout
- D) The job is queued and waits until one of the `gpu-runner` labeled runners becomes available

---

### Question 97 — Managing Runners

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Actions Runner Controller maximum runner cap

**Scenario**:
Your enterprise uses Actions Runner Controller (ARC) on Kubernetes for auto-scaling self-hosted runners. During peak CI usage, the runner pool scales up but you want to cap costs by limiting the maximum number of runners.

**Question**:
What configuration setting controls the maximum number of runners that can be provisioned by ARC?

- A) `maxRunners` in the `AutoscalingRunnerSet` resource specification
- B) `maxConcurrency` at the enterprise Actions policy settings
- C) `concurrency.group` in the workflow file
- D) Runner scale sets scale infinitely by design; a maximum cannot be set

---

### Question 98 — GitHub Actions Enterprise

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Enterprise-enforced required workflows

**Question**:
Which GitHub Actions feature allows enterprise administrators to mandate that a specific workflow runs on every repository in the enterprise, regardless of what the repository's own workflows define?

- A) Required workflows (enterprise-enforced workflows)
- B) Mandatory action policies
- C) Workflow enforcement via GitHub Apps
- D) Required status checks combined with branch protection

---

### Question 99 — GitHub Actions Enterprise

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Restricting secret access to main branch workflows only

**Scenario**:
An enterprise security audit reveals that workflow secrets from production repositories are being accessed by workflows triggered from experimental feature branches, bypassing intended access controls.

**Question**:
Which configuration combination best enforces that secrets are only accessible to workflows running from the `main` branch?

- A) Use GitHub Apps instead of secrets for all sensitive credentials
- B) Configure environment protection rules to restrict the `production` environment to the `main` branch, and store sensitive secrets in that environment rather than at the repository level
- C) Add `if: github.ref == 'refs/heads/main'` checks before every step that accesses secrets
- D) Enable IP allow lists on the enterprise to restrict runner access

---

### Question 100 — GitHub Actions Enterprise

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Consolidated enterprise-level Actions usage reporting

**Scenario**:
Your enterprise spans multiple GitHub organizations. Finance, Engineering, and Operations each have their own organization but all use GitHub Actions extensively. The CTO wants a consolidated view of Actions usage, billing costs per organization, and runner utilization for the entire enterprise.

**Question**:
Where is this consolidated enterprise-level GitHub Actions data available?

- A) Each organization must be viewed separately in GitHub.com; there is no consolidated enterprise view
- B) Enterprise-level usage data, including Actions minutes and storage per organization, is available in the enterprise account's "Usage" and "Billing" sections
- C) Use `GET /enterprises/{enterprise}/actions/cache/usage` REST API endpoint for all metrics
- D) Both B and C provide complete consolidated enterprise Actions reporting

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | A | The VS Code GitHub Actions extension's primary feature is real-time YAML schema validation against GitHub's official workflow schema, catching syntax and configuration errors before committing. | 01-GitHub-Actions-VS-Code-Extension.md | Easy |
| 2 | B | A red squiggly under a `with:` input indicates the parameter name does not match any of the action's defined inputs in its `action.yml`. Check the action's documentation for valid input names. | 01-GitHub-Actions-VS-Code-Extension.md | Medium |
| 3 | D | Schema validation requires both the YAML extension (for YAML language support) and a schema mapping configuration. Committing `.vscode/settings.json` to the repository shares this configuration with the team. Both steps together complete the setup. | 01-GitHub-Actions-VS-Code-Extension.md | Medium |
| 4 | B | With the GitHub Actions extension active, typing `${{` triggers IntelliSense completions for all accessible contexts: `github.*`, `secrets.*`, `env.*`, `inputs.*`, `matrix.*`, and `vars.*`. | 01-GitHub-Actions-VS-Code-Extension.md | Easy |
| 5 | B | Reloading the VS Code window (`Developer: Reload Window`) refreshes all extension processes and schema loading. This resolves most transient extension state issues without requiring reinstallation. | 01-GitHub-Actions-VS-Code-Extension.md | Medium |
| 6 | B | The `secrets` context is available at the job level and step level. It is not available at the workflow-level `env:` block or in `run-name`, as secrets are resolved per-job execution context. | 02-Contextual-Information.md | Easy |
| 7 | B | The `run-name` workflow key supports only `github`, `inputs`, and `vars` contexts. Sensitive contexts like `secrets` and dependency contexts like `needs` are not permitted in `run-name`. | 02-Contextual-Information.md | Medium |
| 8 | B | For `pull_request` events, `github.base_ref` contains the name of the base branch (the branch the PR targets). `github.ref_name` refers to the merge ref, not the base branch. | 02-Contextual-Information.md | Medium |
| 9 | B | Organization-level configuration variables are accessed via the `vars` context: `${{ vars.VARIABLE_NAME }}`. The `env` context only reflects values explicitly set in the current workflow's `env:` blocks. | 02-Contextual-Information.md | Medium |
| 10 | B | Matrix dimension values are accessed via `${{ matrix.KEY_NAME }}` where `KEY_NAME` matches the dimension key defined in `strategy.matrix`. The property name must match exactly. | 02-Contextual-Information.md | Medium |
| 11 | A | At the `jobs.<job_id>.if` conditional, the available contexts are `github`, `secrets`, `inputs`, `vars`, and `needs`. The `env`, `job`, and `steps` contexts are only available within job steps, not at the job-level `if`. | 02-Contextual-Information.md | Easy |
| 12 | B | The `secrets` context is restricted to step-level evaluation only. It cannot be used in job-level `if:` conditions. This is a documented GitHub Actions scoping limitation to control where secrets are evaluated. | 02-Contextual-Information.md | Hard |
| 13 | A | Job outputs from a dependency named `build` are referenced as `${{ needs.build.outputs.<output-name> }}`. The `needs` context holds outputs from all jobs the current job depends on. | 02-Contextual-Information.md | Medium |
| 14 | B | `github.run_number` is a monotonically incrementing integer unique within a single repository. It is not globally unique across repositories. Use it for per-repo build tagging. | 02-Contextual-Information.md | Medium |
| 15 | B | On GitHub.com, `github.server_url` returns `https://github.com`. On GitHub Enterprise Server, it returns the custom enterprise hostname (e.g., `https://ghe.mycompany.com`), enabling instance-aware workflows. | 02-Contextual-Information.md | Medium |
| 16 | D | Trigger events can be specified using compact array notation `on: [push, pull_request]` or the expanded YAML map form. Both are valid and commonly used; the map form allows adding event-specific filters. | 04-Workflow-File-Structure.md | Easy |
| 17 | A | Workflow-level `env:` values support context expression interpolation. Option A correctly embeds `${{ github.repository }}` inside a string to construct the full image name. | 04-Workflow-File-Structure.md | Medium |
| 18 | C | Shell defaults can be overridden at both the job level (`defaults.run.shell:`) and at individual step level (`shell:`). Both approaches are valid and override the workflow-level default for the targeted scope. | 04-Workflow-File-Structure.md | Medium |
| 19 | D | GitHub does not impose a fixed per-workflow maximum on concurrent jobs. The practical limit depends on the GitHub plan's included concurrency, available runners, and any configured concurrency groups. | 04-Workflow-File-Structure.md | Easy |
| 20 | C | Enterprise compliance requirements like mandatory environment variables are enforced through enterprise required workflows. Individual repository workflow files are not pre-validated for compliance at commit time. | 04-Workflow-File-Structure.md | Medium |
| 21 | C | The `if:` conditional requires the `${{ }}` expression wrapper for function calls like `contains()`. Without the wrapper, the expression is treated as a literal string and not evaluated as a workflow expression. | 04-Workflow-File-Structure.md | Medium |
| 22 | D | A reusable workflow is exposed by adding `workflow_call` to the `on:` trigger. Both the compact form (`on: workflow_call`) and the expanded map form are valid YAML and produce the same behavior. | 04-Workflow-File-Structure.md | Medium |
| 23 | B | `workflow_approval` is not a valid GitHub Actions trigger event. Related valid events include `workflow_dispatch` (manual trigger), `workflow_call` (reusable), `workflow_run`, and `repository_dispatch`. | 05-Workflow-Trigger-Events.md | Easy |
| 24 | A | When `branches` and `paths` filters appear under the same event key, GitHub applies AND logic: both the branch condition AND the path condition must be satisfied for the workflow to trigger. | 05-Workflow-Trigger-Events.md | Medium |
| 25 | D | Both approaches work: checking full repo name equality (`head.repo.full_name == github.repository`) or directly checking the fork boolean (`head.repo.fork == false`). Either condition correctly filters out forked PR triggers. | 05-Workflow-Trigger-Events.md | Medium |
| 26 | A | The glob pattern `v*.*.*` correctly matches semantic version tags like `v1.2.3`, `v10.0.1`, etc. This is the standard and most readable approach for version tag matching. Option B is unnecessarily restrictive (single digits only). | 05-Workflow-Trigger-Events.md | Hard |
| 27 | B | Cron syntax `0 2 * * *` means "at minute 0, hour 2, any day, any month, any weekday" = 2:00 AM UTC daily. The five fields are: minute, hour, day-of-month, month, day-of-week. | 05-Workflow-Trigger-Events.md | Medium |
| 28 | D | Both `repository_dispatch` (triggered via webhook from each repository) and enterprise-level required workflows (centrally enforced push-triggered scans) can achieve cross-repository compliance scanning. | 05-Workflow-Trigger-Events.md | Medium |
| 29 | B | `workflow_dispatch` inputs are best accessed via the `inputs` context: `${{ inputs.environment }}`. Both `inputs` context and `github.event.inputs` work, but the `inputs` context is the modern recommended approach introduced in 2021. | 05-Workflow-Trigger-Events.md | Medium |
| 30 | B | `pull_request_target` runs with write permissions and secrets access from the base repository. Untrusted code from forks can access secrets and write to the repo. Always manually review fork PR code before running any code with this trigger. | 05-Workflow-Trigger-Events.md | Medium |
| 31 | B | Environment variables can be defined at three scopes: workflow root (`env:`), job (`env:` under a job), and step (`env:` under a step). Step-level overrides job-level, which overrides workflow-level for the same variable name. | 06-Custom-Environment-Variables.md | Easy |
| 32 | A | Defining `NODE_ENV: development` in the job's `env:` section overrides the workflow-level value for that job only. The override respects the scope hierarchy: step > job > workflow. | 06-Custom-Environment-Variables.md | Medium |
| 33 | C | The correct pattern maps a secret to an environment variable using `env: API_KEY: ${{ secrets.MY_API_KEY }}`. This securely exposes the secret as an environment variable within the step without hardcoding sensitive values. | 06-Custom-Environment-Variables.md | Medium |
| 34 | B | `github.repository` returns the full repository path in the format `owner/repository-name` (e.g., `octocat/my-repo`). It does not return just the repo name or the full URL. | 06-Custom-Environment-Variables.md | Medium |
| 35 | D | `GITHUB_WORKFLOW`, `GITHUB_RUN_ID`, and `GITHUB_ACTOR` are all automatically set default environment variables available in every GitHub Actions workflow run. Many more exist in this default set. | 06-Custom-Environment-Variables.md | Easy |
| 36 | B | The `run_number` is tied to the workflow run, not the attempt. Re-running a workflow increments `github.run_attempt` while `github.run_number` stays the same. Use `run_attempt` to distinguish re-runs. | 06-Custom-Environment-Variables.md | Medium |
| 37 | A | `$GITHUB_REF_PROTECTED` is a default environment variable set to `true` when the workflow is running against a protected ref (branch or tag with branch protection rules). It enables protection-aware workflow logic. | 06-Custom-Environment-Variables.md | Medium |
| 38 | D | Both checking `github.server_url` (identifies the instance hostname) and querying the GitHub API metadata endpoint are viable approaches to detect enterprise instance specifics in a workflow. | 06-Custom-Environment-Variables.md | Medium |
| 39 | A | Context expressions are valid in most string fields including artifact names. `artifact-${{ github.run_number }}-${{ github.run_attempt }}` creates a unique artifact identifier per run attempt without shell variable dependencies. | 06-Custom-Environment-Variables.md | Medium |
| 40 | D | `runner.os` returns `Linux`, `Windows`, or `macOS` correctly across all supported GitHub-hosted and self-hosted runner operating systems, making it reliable for cross-platform conditional logic. | 06-Custom-Environment-Variables.md | Medium |
| 41 | A | Environment protection rules, including required reviewers and allowed deployment branches, are configured per-environment in repository Settings → Environments → [environment name] → Protection rules. | 08-Environment-Protection-Rules.md | Medium |
| 42 | C | Once a deployment approval is granted and the deployment job is running, the approval itself cannot be revoked. However, the entire workflow run (including the active deployment job) can be canceled via the GitHub Actions UI. | 08-Environment-Protection-Rules.md | Medium |
| 43 | B | Required reviewer protection rules reference specific users or teams. If the designated reviewer is removed from the organization or team, pending deployments awaiting that reviewer's approval will fail. The environment protection rules must be updated. | 08-Environment-Protection-Rules.md | Medium |
| 44 | A | Wait timers count from when the deployment job was triggered (first dispatched). The deployment automatically proceeds once the full timer duration elapses, assuming all other protection rules are also satisfied. | 08-Environment-Protection-Rules.md | Medium |
| 45 | A | When a deployment branch restriction is configured on an environment, any workflow job targeting that environment from a non-matching branch fails automatically with a protection rule violation error. | 08-Environment-Protection-Rules.md | Medium |
| 46 | B | GitHub Actions artifacts have a default retention period of 5 days for many account types. After this period, artifacts are permanently deleted. The retention period can be extended up to 400 days via `retention-days` on paid plans. | 09-Workflow-Artifacts.md | Easy |
| 47 | A | The `actions/upload-artifact` action with `retention-days: 60` correctly uploads the `dist/` directory with a 60-day retention. Option B uses an invalid `ttl` parameter; the `retention-days` key is the correct one. | 09-Workflow-Artifacts.md | Medium |
| 48 | A | `actions/download-artifact` with the `name:` parameter downloads a specific artifact by name from the current workflow run. Artifacts are scoped to the workflow run by default; no job-id is needed. | 09-Workflow-Artifacts.md | Medium |
| 49 | B | The repository-level artifact storage limit applies to the total cumulative size of all artifacts stored simultaneously. Individual artifacts are not separately limited — it is the grand total that counts toward the quota. | 09-Workflow-Artifacts.md | Medium |
| 50 | B | Artifacts are permanently deleted after their retention period with no recovery option. Running the workflow again creates a new artifact but cannot restore the previously deleted one's content. | 09-Workflow-Artifacts.md | Medium |
| 51 | B | You can manually delete an artifact immediately via the GitHub UI or REST API, making it instantly inaccessible. The artifact is removed from all access paths upon manual deletion. | 09-Workflow-Artifacts.md | Medium |
| 52 | D | Both methods work for accessing artifacts from previous runs: `actions/download-artifact` with the `run-id` parameter, and the GitHub REST API (`GET /repos/{owner}/{repo}/actions/artifacts`) with run filtering. | 09-Workflow-Artifacts.md | Medium |
| 53 | B | The `actions/cache` action uses `package-lock.json` (not `package.json`) for npm cache key computation via `hashFiles()`. The lock file contains exact pinned dependency versions, making it the correct cache invalidation signal. | 10-Workflow-Caching.md | Easy |
| 54 | C | When `package-lock.json` changes, the `hashFiles()` hash changes, causing a cache miss. GitHub creates a new cache entry with the updated key. The old cache entry remains but is no longer matched by the new key. | 10-Workflow-Caching.md | Medium |
| 55 | A | All GitHub Actions caches are encrypted at rest using AES-256 by default. No additional configuration is required. Cache contents cannot be accessed as plaintext by anyone. | 10-Workflow-Caching.md | Medium |
| 56 | A | A prefix-based restore key matches caches from other branches that share the prefix. A feature branch could restore a cache built on `main`, causing dependency mismatches that are subtle and difficult to debug. | 10-Workflow-Caching.md | Medium |
| 57 | B | Including `runner.os` in cache keys (as is recommended practice) ensures OS-specific isolation. The same literal key string cannot be shared across different operating systems when properly namespaced by runner OS. | 10-Workflow-Caching.md | Medium |
| 58 | B | When the per-repository cache storage limit is reached, GitHub automatically evicts the least-recently-used (LRU) caches to make space for new cache entries. The current job continues normally after eviction. | 10-Workflow-Caching.md | Medium |
| 59 | B | npm dependencies and build output have fundamentally different invalidation triggers. Dependencies change when `package-lock.json` changes; build output changes with source code. Separate cache actions give precise, independent invalidation control. | 10-Workflow-Caching.md | Medium |
| 60 | A | Including `github.ref_name` in the cache key creates branch-specific cache entries. Each branch generates its own unique cache key, preventing cross-branch cache contamination from shared prefix keys. | 10-Workflow-Caching.md | Medium |
| 61 | B | The correct reusable workflow call syntax is `uses: owner/repo/.github/workflows/workflow-name.yml@ref`. The `.github/workflows/` path prefix and the `@ref` suffix are both required. | 11-Workflow-Sharing.md | Easy |
| 62 | A | To call a reusable workflow with custom inputs, use the full workflow path with `uses:` and pass values in the `with:` block. The full path format `owner/repo/.github/workflows/name.yml@ref` is required. | 11-Workflow-Sharing.md | Medium |
| 63 | A | Secrets must be explicitly passed to reusable workflows via the `secrets:` key in the calling job. They are not automatically inherited. Each secret to be shared must be listed individually with its value. | 11-Workflow-Sharing.md | Medium |
| 64 | D | Accessing a private repository's reusable workflow from another repository requires both enabling cross-repository workflow access in the private repo's settings AND the calling repository having appropriate token permissions. | 11-Workflow-Sharing.md | Medium |
| 65 | A | If a required input with no default value is omitted by the calling workflow, the reusable workflow immediately fails with a validation error. GitHub enforces required input constraints at dispatch time. | 11-Workflow-Sharing.md | Medium |
| 66 | D | Both enterprise required workflows (automatic enforcement across all repos) and a GitHub App that programmatically adds the workflow to repositories are valid mechanisms for achieving organization-wide adoption. | 11-Workflow-Sharing.md | Medium |
| 67 | A | Outputs from a called reusable workflow job are referenced as `${{ jobs.job-name.outputs.output-name }}` in subsequent jobs that list the called job in their `needs:`. The `steps` context is scoped to the current job only. | 11-Workflow-Sharing.md | Medium |
| 68 | C | Both approaches together are best practice: define a `secrets:` section in the reusable workflow definition with descriptions (enforced at the API level) AND document in the README (human-readable reference for callers). | 11-Workflow-Sharing.md | Medium |
| 69 | B | When `ACTIONS_RUNNER_DEBUG` (or the `RUNNER_DEBUG` secret/variable) is set to `true`, the runner emits verbose diagnostic log output for each step execution, including runner setup details and command traces. | 12-Workflow-Debugging.md | Easy |
| 70 | A | Setting the repository secret `ACTIONS_STEP_DEBUG` to `true` enables step-level debug logging for subsequent workflow runs. This is the correct and documented method; no workflow file changes are needed. | 12-Workflow-Debugging.md | Medium |
| 71 | C | No single cross-platform command lists all environment variables. The correct approach is OS-conditional steps: use `printenv` on Linux/macOS and `set` on Windows, gated with `if: runner.os == 'Windows'` conditions. | 12-Workflow-Debugging.md | Medium |
| 72 | A | GitHub automatically redacts any value that exactly matches a defined repository or organization secret from workflow log output, replacing it with `***`. This happens transparently without special masking configuration. | 12-Workflow-Debugging.md | Medium |
| 73 | D | Both `echo "::debug::message"` (workflow command syntax) and `core.debug("message")` (JavaScript `@actions/core` SDK) correctly emit debug-level messages that are only visible when debug logging is enabled. | 12-Workflow-Debugging.md | Medium |
| 74 | A | `if: always()` ensures a step executes regardless of the outcome of all previous steps, including both failures and job cancellation. `if: failure()` does not catch cancellation; `if: cancelled()` does not catch step failures. | 12-Workflow-Debugging.md | Medium |
| 75 | C | Debugging audit trails exist in two locations: workflow run logs (showing verbose output from debug-enabled runs) and the organization's secret modification audit log (recording when `ACTIONS_STEP_DEBUG` or `RUNNER_DEBUG` secrets were set or changed). | 12-Workflow-Debugging.md | Medium |
| 76 | A | The correct REST API endpoint to list all workflow files in a repository is `GET /repos/{owner}/{repo}/actions/workflows`. This returns a paginated list of workflow objects. | 13-Workflows-REST-API.md | Easy |
| 77 | A | To trigger a workflow via REST API, use `POST /repos/{owner}/{repo}/actions/workflows/{workflow_id}/dispatches` with a JSON body containing `ref` and optionally `inputs`. The workflow must have the `workflow_dispatch` event trigger defined. | 13-Workflows-REST-API.md | Medium |
| 78 | A | To list runs for a specific workflow, use `GET /repos/{owner}/{repo}/actions/workflows/{workflow_id}/runs`. Append query parameters `?branch=main&status=success` to filter by branch and completion status. | 13-Workflows-REST-API.md | Medium |
| 79 | B | To cancel a workflow run, use `POST /repos/{owner}/{repo}/actions/runs/{run_id}/cancel`. This sends a cancellation request to the queued or in-progress run. DELETE would remove the run record; PUT/PATCH are not used for this operation. | 13-Workflows-REST-API.md | Medium |
| 80 | A | A GitHub App token needs the `actions: write` permission to list artifacts, cancel runs, and trigger workflow dispatches. This single permission scope covers all three read and write operations against the Actions API. | 13-Workflows-REST-API.md | Medium |
| 81 | B | When a job targets an environment with required reviewers, GitHub Actions places the job in a "waiting" state and sends approval request notifications. The job does not proceed until a required reviewer explicitly approves or until the pending request times out. | 14-Reviewing-Deployments.md | Medium |
| 82 | A | Adding multiple users or teams as required reviewers in the environment protection rules requires ALL listed reviewers to independently approve before deployment proceeds. This is natively supported without chaining environments. | 14-Reviewing-Deployments.md | Medium |
| 83 | A | The wait timer starts when the deployment job is first triggered (dispatched). It counts elapsed real time, not time since reviewer approval. The deployment job starts after the full wait duration elapses AND all reviewer approvals are received. | 14-Reviewing-Deployments.md | Medium |
| 84 | A | Configuring "Deployment branches and tags" in environment settings to allow only the `main` branch is the primary enforcement mechanism. This is enforced by GitHub at the environment level, not just by workflow conditional logic. | 14-Reviewing-Deployments.md | Medium |
| 85 | D | Defense-in-depth requires all three controls: required reviewers (prevent unauthorized approvals), deployment branch restrictions (block non-main branch deployments), and branch protection requiring PR reviews on workflow file changes (prevent bypass via workflow modification). | 14-Reviewing-Deployments.md | Hard |
| 86 | B | GitHub Actions supports three first-class action types: JavaScript actions, Docker container actions, and composite actions. Python is not a discrete action type — it can be used inside Docker or composite actions but is not its own type. | 15-Creating-Publishing-Actions.md | Easy |
| 87 | B | `action.yml` (or `action.yaml`) is the required metadata file for any GitHub Action. It defines the action's name, description, inputs, outputs, `runs` configuration (entry point), and optional branding. | 15-Creating-Publishing-Actions.md | Medium |
| 88 | A | To publish to the GitHub Marketplace, the action must be in a public repository with a valid `action.yml`, a README, and be submitted via a tagged release from the repository's Releases page. No CLI tool or special plan is required. | 15-Creating-Publishing-Actions.md | Medium |
| 89 | A | Pinning a third-party action to a full commit SHA (e.g., `uses: actions/checkout@abc123def456...`) prevents a supply chain attack where the action author pushes malicious code under an existing tag. Tags are mutable; commit SHAs are not. | 15-Creating-Publishing-Actions.md | Medium |
| 90 | A | `core.setOutput('name', value)` from the `@actions/core` library is the correct method to set an action output value. `exportVariable` sets an environment variable, not an output; `info` only logs a message. | 15-Creating-Publishing-Actions.md | Medium |
| 91 | C | Within a composite action, the standard step output mechanism applies: assign an `id:` to the producing step and reference its output as `${{ steps.step-id.outputs.output-name }}` in subsequent steps, identical to regular workflow jobs. | 15-Creating-Publishing-Actions.md | Medium |
| 92 | D | Enterprise and organization Actions policies support all listed restriction levels: allow all, allow only GitHub-owned actions, or allow specific owners and repositories by pattern. Administrators choose the appropriate level for their security posture. | 15-Creating-Publishing-Actions.md | Medium |
| 93 | B | Self-hosted runners' key advantages are: access to private networks without VPN, use of custom or specialized hardware (GPUs, high-memory machines), and persistent tool caches or state between job runs. | 16-Managing-Runners.md | Easy |
| 94 | B | Self-hosted runners deployed inside a private network have direct connectivity to internal resources with no public exposure. This is the recommended architecture for workflows requiring access to internal services. | 16-Managing-Runners.md | Medium |
| 95 | B | Runner groups at the organization or enterprise level allow administrators to assign specific self-hosted runners to specific repositories or teams. Runners in restricted groups are inaccessible to repositories not granted access. | 16-Managing-Runners.md | Medium |
| 96 | D | When all runners matching the `runs-on` label combination are busy, GitHub queues the job and it waits until a matching runner becomes available. Jobs do not fail immediately on runner unavailability — they wait in the queue. | 16-Managing-Runners.md | Medium |
| 97 | A | Actions Runner Controller uses the `AutoscalingRunnerSet` Kubernetes CRD to manage runner lifecycle. The `maxRunners` field in this resource sets the upper bound for the number of runner pods that can be auto-provisioned. | 16-Managing-Runners.md | Medium |
| 98 | A | "Required workflows" is the GitHub Enterprise feature enabling administrators to mandate that specific workflows execute on all repositories in the enterprise, supplementing (not replacing) repository-defined workflows. | 17-GitHub-Actions-Enterprise.md | Medium |
| 99 | B | Storing sensitive production secrets in environment-scoped secrets (rather than repo-level secrets) and configuring the environment to restrict deployments to `main` only ensures that only workflows triggered from `main` can access those secrets. | 17-GitHub-Actions-Enterprise.md | Hard |
| 100 | B | Enterprise accounts have a centralized "Billing & plans" section with GitHub Actions usage data (minutes consumed, storage used) broken down per organization, providing the consolidated view needed for cross-org reporting. | 17-GitHub-Actions-Enterprise.md | Medium |

---

*End of GH-200 Iteration 7 — 100 Questions*
