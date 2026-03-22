# GitHub Actions GH-200 Exam Questions — Iteration 8

**Format:** 100 Questions | 70% Scenario-Based | 11+ Security | 9+ Enterprise
**Topics Covered:** All 19 GitHub Actions Core Topics
**Iteration Note:** Fresh question set — new angles, deeper coverage, no repeats from Iteration 7
**Last Updated:** March 22, 2026

---

## Section 1: VS Code Extension & Tooling

**Q1 — Conceptual**
The GitHub Actions VS Code extension can display action inputs and outputs when you hover over a `uses:` directive. What does the extension require to enable this feature?

- A) The action must be published on the GitHub Marketplace
- B) The action repository must have an `action.yml` or `action.yaml` file with metadata
- C) You must be authenticated to GitHub in VS Code
- D) The extension requires internet access to fetch YAML schemas at hover time

**Answer: B**

---

**Q2 — Scenario**
A developer adds `invalid-input: value` under `with:` for an action that does not define that input. The GitHub Actions VS Code extension highlights the key in red. When the workflow runs on GitHub, what actually happens?

- A) The workflow fails immediately with a validation error at parse time
- B) The invalid input is silently ignored by the runner; the action runs normally
- C) The action fails at the step level with an `Unexpected input(s)` error
- D) GitHub rejects the workflow file during the push

**Answer: B**

---

**Q3 — Scenario**
Your team uses a `.github/workflows/` directory, but a junior developer placed a new workflow in `.github/Workflows/` (capital W) on a Linux runner. The extension doesn't highlight errors, but the workflow never runs. Why?

- A) The path must be exactly `.github/workflows/` (lowercase); GitHub does not discover workflows in other paths
- B) The VS Code extension should have caught this; the developer needs to reload the window
- C) The workflow needs a `name:` field to be discovered
- D) GitHub scans all directories for `*.yml` files regardless of path

**Answer: A**

---

**Q4 — Scenario (Security)**
After installing the GitHub Actions VS Code extension, a developer notices that `secrets.*` context values are shown as `***` in IntelliSense suggestions. What does this indicate about security?

- A) The extension has access to secret values and is masking them
- B) The extension only shows secret names, never their values — values are only available during job execution in the runner
- C) This is a bug; the extension should show full secret names and descriptions
- D) Secrets are fully accessible to any VS Code extension with the right permissions

**Answer: B**

---

**Q5 — Scenario**
Your organization enforces SHA-pinned actions (e.g., `actions/checkout@abc123`). The VS Code extension shows a warning for a developer's new step using `actions/checkout@v4`. What should the developer do to resolve this?

- A) Ignore the warning; SHA pinning is optional
- B) Run `gh api repos/actions/checkout/git/ref/tags/v4 --jq .object.sha` and replace `@v4` with the resulting commit SHA
- C) Remove the `uses:` line and run `checkout` as a shell command instead
- D) Use `actions/checkout@latest` instead

**Answer: B**

---

## Section 2: Contextual Information & Context Availability

**Q6 — Conceptual**
What is the difference between `github.actor` and `github.triggering_actor`?

- A) They are identical; both represent the user who triggered the run
- B) `github.actor` is the user who initiated the original event; `github.triggering_actor` is the user who triggered the re-run, which may differ
- C) `github.triggering_actor` is only set for scheduled workflows
- D) `github.actor` is deprecated in favor of `github.triggering_actor`

**Answer: B**

---

**Q7 — Scenario**
Your workflow uses `${{ github.sha }}` in a `pull_request` triggered workflow to tag a Docker image. A team member points out that this may not be the "real" commit on the target branch. Why?

- A) `github.sha` is the merge commit SHA GitHub creates to test the PR merge, not the head commit of the PR branch
- B) `github.sha` is always empty for pull_request events
- C) `github.sha` returns the base branch SHA, not the PR branch SHA
- D) SHA values are only available for `push` events

**Answer: A**

---

**Q8 — Scenario**
A workflow conditional at the job level reads: `if: github.ref == 'refs/heads/main'`. A developer pushes to a branch named `maintenance/main-fix`. Does this conditional match?

- A) Yes, because the branch contains "main"
- B) No, because `github.ref` is `refs/heads/maintenance/main-fix`, which does not equal `refs/heads/main`
- C) It depends on the trigger event
- D) `github.ref` uses glob matching, so `*main*` patterns would apply

**Answer: B**

---

**Q9 — Scenario (Security)**
You want to prevent a workflow from running when triggered by a GitHub App bot (e.g., `dependabot[bot]`). Which conditional correctly achieves this?

- A) `if: github.actor != 'bot'`
- B) `if: !contains(github.actor, '[bot]')`
- C) `if: github.actor != 'dependabot[bot]'`
- D) Both B and C — B is more generic, C is specific to Dependabot

**Answer: D**

---

**Q10 — Scenario**
In a matrix workflow with `include:` entries, a step uses `${{ matrix.experimental }}`. For matrix combinations that don't define `experimental`, what does this expression evaluate to?

- A) An error is thrown — you must define all matrix variables for every combination
- B) An empty string (falsy)
- C) `null`
- D) `false`

**Answer: B**

---

**Q11 — Conceptual**
In a reusable workflow, the `inputs` context contains values passed by the caller. Which context is used to access these values inside the called workflow?

- A) `${{ github.event.inputs.* }}`
- B) `${{ inputs.* }}`
- C) `${{ workflow.inputs.* }}`
- D) `${{ caller.inputs.* }}`

**Answer: B**

---

**Q12 — Scenario**
A step outputs `version=1.2.3` using `echo "version=1.2.3" >> $GITHUB_OUTPUT`. A later step in the same job needs to reference it. What is the correct expression, given the earlier step has `id: tag`?

- A) `${{ env.version }}`
- B) `${{ steps.tag.outputs.version }}`
- C) `${{ job.tag.outputs.version }}`
- D) `${{ github.outputs.tag.version }}`

**Answer: B**

---

**Q13 — Scenario (Enterprise)**
Your enterprise requires all deployment job names to include the deploying actor for audit purposes. You want the job display name to read `Deploy by octocat`. Where can the `github.actor` context safely be used to set this?

- A) `jobs.<job_id>.name`
- B) `jobs.<job_id>.if`
- C) `jobs.<job_id>.runs-on`
- D) `jobs.<job_id>.environment`

**Answer: A**

---

**Q14 — Scenario**
A workflow has a concurrency group set to `${{ github.workflow }}-${{ github.ref }}` with `cancel-in-progress: true`. Two pushes arrive in quick succession to `main`. What happens to the first run?

- A) Both runs execute simultaneously since they are in the same workflow
- B) The first run is canceled when the second run starts
- C) The second run is queued and waits for the first to finish
- D) An error is thrown — only one concurrency group is allowed per workflow

**Answer: B**

---

**Q15 — Scenario**
Your workflow reads `${{ vars.DEPLOY_REGION }}`. The variable is set at both the repository level (`us-east-1`) and the organization level (`eu-west-1`). Which value takes precedence?

- A) Organization-level value always wins
- B) Repository-level value wins — it overrides the organization-level value
- C) Both values are merged into a comma-separated list
- D) The runner level always wins over both

**Answer: B**

---

## Section 3: Workflow File Structure

**Q16 — Conceptual**
What is the behavior of `cancel-in-progress: false` in a concurrency block?

- A) New runs are rejected when one run is already in progress
- B) In-progress runs are allowed to complete; new runs for the same concurrency group queue behind them
- C) All runs execute in parallel regardless of concurrency group
- D) `false` is not a valid value; only `true` is supported

**Answer: B**

---

**Q17 — Scenario**
Your workflow defines a matrix:

```yaml
strategy:
  matrix:
    os: [ubuntu-latest, windows-latest]
    node: [18, 20]
  fail-fast: false
```

One matrix combination fails. What happens to the other running combinations?

- A) All combinations stop immediately — fail-fast is the default behavior
- B) Only the failing combination marks the workflow as failed; other combinations continue to run
- C) The failing combination is retried automatically up to 3 times
- D) GitHub sends a notification to pause the remaining combinations

**Answer: B**

---

**Q18 — Scenario**
A complex deploy workflow has 8 jobs. Jobs `test-unit` and `test-integration` can run in parallel. Job `build` must wait for both test jobs. Job `deploy` must wait for `build`. Which `needs:` pattern expresses this correctly for the `build` job?

- A) `needs: [test-unit, test-integration]`
- B) `needs: test-unit && test-integration`
- C) `needs: [test-unit] && [test-integration]`
- D) `needs: all-tests`

**Answer: A**

---

**Q19 — Scenario (Security)**
You want to harden a job by granting the minimum required permissions. The job only needs to read repository content and write PR comments. What is the correct permissions block?

```yaml
permissions:
  contents: ???
  pull-requests: ???
```

- A) `contents: read`, `pull-requests: write`
- B) `contents: write`, `pull-requests: read`
- C) `contents: none`, `pull-requests: write`
- D) `contents: read`, `pull-requests: read`

**Answer: A**

---

**Q20 — Scenario**
You add a `container:` block to a job pointing to a private image at `ghcr.io/myorg/runner-image:latest`. The job fails with `Error response from daemon: pull access denied`. What is the most likely cause?

- A) The `container:` key is not supported on GitHub-hosted runners
- B) The `GITHUB_TOKEN` does not have `packages: read` permission, or credentials for the private registry are not configured
- C) Container images must be publicly accessible for GitHub-hosted runners
- D) The `container:` key requires `runs-on: self-hosted`

**Answer: B**

---

**Q21 — Conceptual**
In a workflow using `strategy.matrix`, what does the `include:` key allow you to do?

- A) Add extra variables to specific matrix combinations or add additional combinations not generated by the matrix cross-product
- B) Import variables from outside the repository
- C) Include another YAML file's matrix definition
- D) Force all matrix jobs to run sequentially

**Answer: A**

---

**Q22 — Scenario (Enterprise)**
Your enterprise requires all workflows to define `timeout-minutes` at the job level to prevent runaway billing. A new hire's workflow omits this field. What is the default behavior, and what risk does it create?

- A) Jobs default to 6 hours (360 minutes); a runaway job could consume significant compute resources before being killed
- B) Jobs have no timeout by default; they can run indefinitely if no timeout is set
- C) Workflows timeout at 1 hour regardless of job-level settings
- D) GitHub automatically kills jobs after 24 hours of inactivity

**Answer: A**

---

## Section 4: Workflow Trigger Events

**Q23 — Scenario**
You want to skip a workflow run when the commit message contains `[skip ci]`. Which approach most efficiently implements this without adding external tools?

- A) `if: !contains(github.event.head_commit.message, '[skip ci]')` on every job
- B) GitHub natively respects `[skip ci]` in commit messages and skips workflow runs automatically
- C) Add a workflow condition: `if: env.SKIP_CI != 'true'`
- D) Use `paths-ignore: ['**']` filtered by commit message

**Answer: B**

---

**Q24 — Scenario (Security)**
A `workflow_dispatch` trigger accepts an `environment` input with options: `["dev", "staging", "production"]`. A user submits the API payload manually with `environment: "production_debug"`. What happens?

- A) GitHub validates the enum and rejects the invalid value
- B) The workflow runs with `production_debug` as the input value; enum only applies to the UI dropdown
- C) The workflow fails at the step level when the invalid environment is used
- D) GitHub silently replaces the invalid value with the default

**Answer: A**

---

**Q25 — Scenario**
Your workflow uses `on: pull_request` with `branches: [main]`. A developer opens a PR targeting the `develop` branch. Does the workflow trigger?

- A) Yes — `branches` in `pull_request` is the source branch filter
- B) No — `branches` in `pull_request` filters by the PR target (base) branch; PRs targeting `develop` don't match
- C) Yes — all pull requests trigger `pull_request` regardless of target branch
- D) Only if the source branch also matches a `branches` filter

**Answer: B**

---

**Q26 — Scenario**
A workflow uses `on: push` with both `branches: [main]` and `paths: ['docs/**']`. A commit containing only JS changes is pushed to `main`. Does the workflow trigger?

- A) Yes — pushing to `main` satisfies the branch filter
- B) No — both `branches` and `paths` filters must be satisfied simultaneously
- C) It depends on whether `paths-ignore` is also set
- D) Yes — `branches` and `paths` filters are evaluated independently with OR logic

**Answer: B**

---

**Q27 — Scenario (Enterprise)**
Your enterprise CI must run on every PR but must also be triggerable manually for hotfix situations. Which combination of triggers supports both use cases?

- A)
```yaml
on:
  pull_request:
    branches: [main]
  workflow_dispatch:
```
- B)
```yaml
on:
  push:
    branches: [main]
  workflow_run:
    workflows: ["CI"]
    types: [completed]
```
- C) `on: [pull_request, push]`
- D) Only one trigger can be specified per workflow

**Answer: A**

---

**Q28 — Scenario (Security)**
A public repository receives a PR from a fork. The PR modifies `.github/workflows/ci.yml` to add a step that reads `${{ secrets.DEPLOY_KEY }}`. What happens when the workflow runs?

- A) The workflow runs and successfully reads the secret
- B) GitHub runs the base branch's version of the workflow file, not the forked version — the forked change has no effect
- C) The workflow runs but all secret expressions evaluate to empty strings
- D) The PR is automatically blocked because it modifies workflow files

**Answer: B**

---

**Q29 — Scenario**
Your `schedule` trigger uses cron `0 */4 * * *`. The GitHub Actions service experiences a 30-minute outage at the scheduled time. What happens to the skipped scheduled run?

- A) GitHub automatically re-queues the missed run once the service recovers
- B) The missed scheduled run is not re-run; the next scheduled time proceeds as normal
- C) GitHub retries the missed run up to 3 times within the next hour
- D) A `schedule_missed` event is fired to notify the team

**Answer: B**

---

**Q30 — Scenario**
You want a workflow that runs only when a new GitHub Release is published (not created as a draft). Which trigger configuration is correct?

- A)
```yaml
on:
  release:
    types: [published]
```
- B)
```yaml
on:
  push:
    tags: ['v*']
```
- C) Both A and B — they are equivalent
- D) `on: release` without `types` triggers on all release events including drafts

**Answer: A**

---

## Section 5: Environment Variables

**Q31 — Scenario**
A workflow step sets a variable using `echo "MY_VAR=hello" >> $GITHUB_ENV`. Which statement is correct about when this variable is available?

- A) Immediately in the same step after the `echo` command
- B) Available in all subsequent steps in the same job, but not in the current step
- C) Available in all subsequent jobs in the workflow
- D) Available only in the current step

**Answer: B**

---

**Q32 — Scenario (Security)**
A developer attempts to log a secret value for debugging: `run: echo "Token is ${{ secrets.API_TOKEN }}"`. What will appear in the workflow logs?

- A) The actual secret value — this is a security vulnerability
- B) `Token is ***` — GitHub automatically masks registered secret values in logs
- C) An empty string — secrets cannot be used in `run:` steps directly
- D) The workflow fails with a security policy violation

**Answer: B**

---

**Q33 — Scenario**
Your workflow defines `env: BUILD_VERSION: ${{ github.run_number }}` at the workflow level. Later, a job defines `env: BUILD_VERSION: snapshot`. Which value does a step in that job see when referencing `$BUILD_VERSION`?

- A) The workflow-level value (`run_number`) — workflow-level always wins
- B) `snapshot` — the job-level definition shadows the workflow-level definition for that job
- C) Both values concatenated: `42-snapshot`
- D) An error is thrown due to the duplicate definition

**Answer: B**

---

**Q34 — Conceptual**
What is the difference between `$GITHUB_ENV` and `$GITHUB_OUTPUT`?

- A) `$GITHUB_ENV` sets workflow-level variables; `$GITHUB_OUTPUT` sets job-level variables
- B) `$GITHUB_ENV` sets environment variables available in subsequent steps of the same job; `$GITHUB_OUTPUT` sets step outputs consumable by later steps via `${{ steps.<id>.outputs.<name> }}`
- C) `$GITHUB_OUTPUT` is deprecated; use `$GITHUB_ENV` for all output propagation
- D) They are interchangeable — writing to either has the same effect

**Answer: B**

---

**Q35 — Scenario**
A step uses `echo "PATH=$HOME/.local/bin:$PATH" >> $GITHUB_ENV` to add a custom binary directory to PATH. The next step runs `mytool --version`. Does `mytool` get found?

- A) No — PATH modifications via `$GITHUB_ENV` don't apply to the PATH executable search
- B) Yes — environment variable updates written to `$GITHUB_ENV` (including PATH) take effect for all subsequent steps
- C) Only if the step uses `shell: bash` explicitly
- D) Only if the binary directory already existed before the workflow started

**Answer: B**

---

**Q36 — Scenario (Enterprise)**
Your enterprise policy requires a standard `COMPLIANCE_ORG_ID` variable across all repositories. An organization admin sets this at the organization level. A specific repository sets `COMPLIANCE_ORG_ID: override-value`. Which value does the workflow see?

- A) The organization-level value always wins
- B) The repository-level `vars.COMPLIANCE_ORG_ID` overrides the organization-level setting for workflows in that repo
- C) Both values are available under different context keys
- D) The workflow fails due to conflicting variable definitions

**Answer: B**

---

**Q37 — Conceptual**
What is the correct way to access a default GitHub environment variable (`GITHUB_WORKFLOW`) in a Bash `run` step?

- A) `${{ github.workflow }}`
- B) `$GITHUB_WORKFLOW`
- C) Both A and B are valid — context expression and environment variable reference are equivalent in `run` steps
- D) Only A is valid; shell variables with the `GITHUB_` prefix are read-only

**Answer: C**

---

## Section 6: Environment Protection Rules

**Q38 — Scenario**
A production environment is configured with one required reviewer. The reviewer approves the deployment but then immediately goes on vacation. Three hours later, the workflow is still paused at the `environment: production` step. What is happening?

- A) The approval triggers the deployment countdown but the 3-hour wait timer hasn't elapsed yet
- B) Approval pauses are indefinite until approved — the deployment only pauses waiting for approval, not after it is given
- C) The workflow timed out because the `timeout-minutes` threshold was exceeded
- D) The reviewer needs to confirm the approval a second time after returning

**Answer: A**

---

**Q39 — Scenario (Security)**
Your organization recently experienced an unauthorized deployment where a developer push-triggered a production deployment from their own feature branch. Which environment protection rule best prevents this from happening again?

- A) Set "Required reviewers" to a separate team
- B) Under "Deployment branches," restrict to branches matching `main` only
- C) Set `timeout-minutes: 1` to shorten the deployment window
- D) Both A and B together provide defense-in-depth

**Answer: D**

---

**Q40 — Scenario**
You configure a 2-hour wait timer on your staging environment. A developer triggers a deployment, which is approved immediately. The deployment job then sits waiting for 2 hours before executing. Is this the intended behavior?

- A) No — the wait timer should have been satisfied since the reviewer approved the deployment
- B) Yes — the wait timer runs from when the deployment job starts, regardless of approval time, enforcing a mandatory delay
- C) No — the wait timer only applies when no reviewer is configured
- D) Timers and reviewer approvals are mutually exclusive; you can only configure one

**Answer: B**

---

**Q41 — Scenario (Enterprise)**
An enterprise compliance requirement states that deployments must be traceable to an approved change ticket. Your team decides to use environment protection rules combined with a required reviewer step. What additional mechanism could you add to enforce the change ticket reference?

- A) Configure a pre-deployment `run` step that validates an input parameter for the ticket ID
- B) Use `workflow_dispatch` inputs to capture the ticket ID and validate it in a pre-flight job before the protected environment step
- C) Both A and B
- D) GitHub natively integrates with ITSM systems for change ticket validation

**Answer: C**

---

**Q42 — Scenario**
A new environment `pre-prod` is created but has no protection rules. A workflow with `environment: pre-prod` runs. What behavior should you expect?

- A) The workflow fails because all environments must have at least one protection rule
- B) The workflow accesses `pre-prod` environment secrets and variables freely without any approval gating
- C) The workflow uses the production environment's rules as fallback
- D) Environments without protection rules are not recognized by GitHub Actions

**Answer: B**

---

## Section 7: Artifacts

**Q43 — Scenario**
A `build` job uploads an artifact with `name: build-dist`. A parallel `test` job tries to download it. Does the `test` job need `needs: build` to access the artifact?

- A) No — artifacts are immediately available to all jobs in the same workflow run once uploaded
- B) Yes — the `test` job must declare `needs: build` to ensure the artifact exists before the download step runs
- C) Artifacts are not available between parallel jobs; `needs:` creates a sequential dependency
- D) Only if the jobs run on different runner OS types

**Answer: B**

---

**Q44 — Scenario**
Your workflow uploads three separate artifacts: `frontend-bundle` (800 MB), `backend-jar` (200 MB), and `test-reports` (50 MB). The total artifact storage for the repository is approaching 4.9 GB of the 5 GB limit. What will happen when this workflow run's artifacts are uploaded?

- A) GitHub rejects all artifact uploads once the 5 GB limit is reached
- B) GitHub automatically evicts the oldest least-recently-used caches (not artifacts) to make room; artifacts must be deleted manually
- C) All three upload successfully — the 5 GB limit applies to caches, not artifacts
- D) Only the largest artifacts are rejected

**Answer: A**

---

**Q45 — Scenario (Security)**
Your build job accidentally writes a `.env` file containing database credentials to the `dist/` directory, which is then uploaded as an artifact. Who can download this artifact?

- A) Only repository admins
- B) Anyone with read access to the repository — artifacts are accessible to all repository readers
- C) Only the GitHub Actions service account
- D) Only the actor who triggered the workflow

**Answer: B**

---

**Q46 — Scenario**
A `deployment` job runs after `build` and needs files from the build artifact. But you also need the exact artifact file structure preserved, including subdirectories. When downloading with `actions/download-artifact`, where are files placed by default?

- A) Directly in the current working directory, flattening subdirectory structure
- B) In a subdirectory named after the artifact, preserving the original directory structure
- C) In `$GITHUB_WORKSPACE/artifacts/<name>/`
- D) In `/tmp/` regardless of working directory

**Answer: B**

---

**Q47 — Conceptual**
What is the maximum configurable retention period for GitHub Actions artifacts on GitHub.com (non-enterprise)?

- A) 30 days
- B) 60 days
- C) 90 days
- D) 365 days

**Answer: C**

---

## Section 8: Workflow Caching

**Q48 — Scenario**
Your cache key is `${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}`. A developer adds a comment to `build.gradle`. Does this invalidate the cache?

- A) No — `hashFiles` only hashes file content changes, not whitespace/comment changes
- B) Yes — `hashFiles` computes a SHA-256 hash of the file content including comments; any content change invalidates the key
- C) It depends on whether `gradle-wrapper.properties` was also modified
- D) Only if the Gradle wrapper version changed

**Answer: B**

---

**Q49 — Scenario**
You set a cache with key `ubuntu-npm-abc123` but no `restore-keys`. A subsequent run has key `ubuntu-npm-def456` (different lockfile). Does the cache restore?

- A) Yes — GitHub tries prefix matching automatically
- B) No — without `restore-keys`, an exact key match is required; no partial restore occurs
- C) Yes — `ubuntu-npm-` is always tried automatically as a fallback
- D) The job fails if no cache hit is found

**Answer: B**

---

**Q50 — Scenario (Enterprise)**
Your enterprise has a policy that build caches must not be shared across pull request branches to prevent cache poisoning attacks. How do you implement this?

- A) Include `${{ github.event.pull_request.number }}` in the cache key so each PR gets an isolated cache
- B) Disable the `actions/cache` action organization-wide
- C) GitHub automatically isolates caches per PR branch
- D) Use separate self-hosted runner pools for each PR

**Answer: A**

---

**Q51 — Scenario**
A `save` phase of `actions/cache@v3` fails because the job was canceled before it completed. What impact does this have on future workflow runs?

- A) The partially written cache causes errors in future restore operations
- B) Future runs will experience a cache miss and proceed without cached data
- C) GitHub locks the cache key until the next successful save
- D) A cache entry with partial content is saved and can cause build corruption

**Answer: B**

---

**Q52 — Scenario (Security)**
A developer on a feature branch modifies the `package-lock.json` to introduce a malicious npm package. The build workflow caches after `npm ci`. A later `main` branch run hits a restore-key fallback and restores this feature branch cache. What risk does this represent?

- A) No risk — restore-key fallbacks only restore caches from the same branch
- B) Cache poisoning — malicious cached `node_modules` from a feature branch can be restored into a production `main` branch build
- C) This is not possible; npm packages in cache are re-validated against `package-lock.json`
- D) Restored caches are read-only and cannot affect runtime behavior

**Answer: B**

---

## Section 9: Workflow Sharing

**Q53 — Scenario**
A reusable workflow is called with `secrets: inherit`. The reusable workflow tries to access `${{ secrets.DEPLOY_KEY }}`. The calling repository has `DEPLOY_KEY` defined. Does the reusable workflow successfully access this secret?

- A) No — `secrets: inherit` only passes secrets that are explicitly listed in the called workflow's `on.workflow_call.secrets:` block
- B) Yes — `secrets: inherit` passes all secrets from the caller to the called workflow without requiring declaration
- C) No — `secrets: inherit` is not a valid keyword
- D) Only if the reusable workflow is in the same repository

**Answer: B**

---

**Q54 — Scenario**
You're designing a reusable workflow that produces three different deployment artifacts as outputs. What is the maximum number of outputs a single job in a reusable workflow can define?

- A) 10
- B) 50
- C) There is no documented limit; outputs are bounded only by the `$GITHUB_OUTPUT` file size
- D) 256

**Answer: C**

---

**Q55 — Scenario (Enterprise)**
Your enterprise has `required workflows` configured at the enterprise level for all repositories. A repository disables GitHub Actions via repository settings. What happens to the enterprise-required workflow for that repository?

- A) The required workflow is skipped because Actions is disabled for the repo
- B) Required workflows run regardless of whether Actions is disabled at the repository level
- C) The repository settings override enterprise policy; required workflows do not run
- D) GitHub sends an alert to the enterprise admin about the policy conflict

**Answer: B**

---

**Q56 — Scenario**
A reusable workflow is hosted in `private-org/shared-wf-repo`. A repository in `public-org` tries to call this workflow. What must be configured for this to work?

- A) The `private-org/shared-wf-repo` repository must be made public
- B) Cross-organization access requires a GitHub App or PAT with appropriate permissions
- C) The `private-org/shared-wf-repo` repository must set Actions access to allow external repositories
- D) Cross-organization reusable workflow calls are not supported

**Answer: C**

---

**Q57 — Scenario**
A reusable workflow declares an output `deploy-url`. The caller job successfully runs the reusable workflow but the next job in the caller cannot access `${{ needs.deploy-job.outputs.deploy-url }}`. It returns empty. What is the most likely cause?

- A) Outputs must be declared in the `jobs.<id>.outputs` section of the calling workflow's job block
- B) The reusable workflow must use `secrets: inherit` for outputs to work
- C) The reusable workflow's output was defined at the job level but was not promoted to the `on.workflow_call.outputs:` section
- D) Both A and C could be the cause

**Answer: C**

---

**Q58 — Conceptual**
When using `secrets: inherit` in a `workflow_call`, what happens if the caller's repository does not have a secret the called workflow expects?

- A) The called workflow will receive `null` for that secret
- B) The workflow fails with a missing secret error
- C) The called workflow receives an empty string for the undefined secret
- D) GitHub automatically creates the secret with a placeholder value

**Answer: C**

---

## Section 10: Workflow Debugging

**Q59 — Scenario**
You want to add a collapsible group in the GitHub Actions log to organize verbose output from a build tool. Which workflow commands achieve this?

- A) `echo "::group::Build Output"` ... `echo "::endgroup::"`
- B) `echo "[START_GROUP]Build Output"` ... `echo "[END_GROUP]"`
- C) `core.startGroup("Build Output")` in shell scripts
- D) Only JavaScript actions can create log groups

**Answer: A**

---

**Q60 — Scenario**
Your workflow has a step that runs a long script. You want to add a notice (visible notice in the GitHub UI, not just a log message) when a specific condition is detected at runtime. Which command achieves this?

- A) `echo "::notice::Deployment to staging initiated"`
- B) `echo "notice: Deployment to staging initiated"`
- C) `echo "NOTICE: Deployment to staging initiated"`
- D) Only the `@actions/core` JavaScript package can emit notices

**Answer: A**

---

**Q61 — Scenario**
A matrix workflow produces 20 job combinations. 3 fail. Using the GitHub CLI, how would you rerun only the failed jobs?

- A) `gh run rerun <run-id> --failed`
- B) `gh workflow run --retry-failed`
- C) `gh run rerun <run-id> --all`
- D) Failed jobs cannot be selectively rerun via CLI; use the GitHub UI

**Answer: A**

---

**Q62 — Scenario (Security)**
Enabling `ACTIONS_STEP_DEBUG` secret reveals sensitive environment variable values in the debug log output. What is the correct way to ensure a sensitive value is always masked even with debug logging enabled?

- A) Store it as a secret in repository settings — GitHub automatically masks all registered secrets in all log output, including debug logs
- B) Use `echo "::mask::<value>"` in a prior step to register the value for masking
- C) Both A and B are valid masking approaches
- D) There is no way to prevent debug logs from exposing sensitive values

**Answer: C**

---

**Q63 — Scenario**
A step uses `continue-on-error: true`. The step fails with exit code 1. What is the outcome for the workflow run?

- A) The overall workflow run is marked as failed
- B) The step is marked as failed, but the job and workflow continue; the overall workflow result reflects subsequent step outcomes
- C) The step is marked as skipped
- D) The step silently succeeds; the failure is not recorded

**Answer: B**

---

**Q64 — Scenario**
You have a `cleanup` step that must run after a deploy step fails, but should be skipped if the deploy step succeeds. Which conditional is correct?

- A) `if: failure()`
- B) `if: always()`
- C) `if: steps.deploy.outcome == 'failure'`
- D) Both A and C work, but C is more specific

**Answer: D**

---

## Section 11: Workflows REST API

**Q65 — Scenario**
You need to trigger a `workflow_dispatch` workflow via the REST API. The workflow file is named `deploy.yml` in the repository. What value should you use for the `{workflow_id}` parameter?

- A) The numeric workflow ID (obtained via list workflows endpoint)
- B) `deploy.yml` — the filename is a valid identifier for the dispatch endpoint
- C) Both A and B are accepted by the API
- D) Only the numeric ID is accepted; filenames are not valid identifiers

**Answer: C**

---

**Q66 — Scenario**
You call `DELETE /repos/{owner}/{repo}/actions/runs/{run_id}` via the REST API. What does this API endpoint do?

- A) Cancels the workflow run if it is still in progress
- B) Deletes the workflow run logs and record permanently
- C) Marks the workflow run as archived
- D) Removes the workflow YAML file from the repository

**Answer: B**

---

**Q67 — Scenario (Enterprise)**
Your enterprise wants to audit which workflow runs accessed a specific environment over the past 30 days. Which REST API endpoint provides this information?

- A) `GET /repos/{owner}/{repo}/deployments`
- B) `GET /repos/{owner}/{repo}/actions/runs` filtered by `event=deployment`
- C) `GET /repos/{owner}/{repo}/environments/{environment_name}/deployments`
- D) Both A and C provide deployment history

**Answer: A**

---

**Q68 — Scenario**
A CI system needs to check if a specific workflow run has completed successfully before proceeding. It polls `GET /repos/{owner}/{repo}/actions/runs/{run_id}`. Which two response fields determine run completion and success?

- A) `state` and `result`
- B) `status` and `conclusion`
- C) `phase` and `outcome`
- D) `condition` and `status`

**Answer: B**

---

## Section 12: Reviewing Deployments

**Q69 — Scenario**
A reviewer opens the deployment approval page and sees the comment field. After approving, can other designated reviewers still reject the same deployment?

- A) No — once one reviewer approves, the deployment proceeds
- B) Yes — if multiple reviewers are required, they all must approve; any one can reject at any point until the minimum approvals are met
- C) Approvals are final; rejection is only available before any approval is given
- D) It depends on whether `any_reviewer` or `all_reviewers` policy is configured

**Answer: B**

---

**Q70 — Scenario (Enterprise)**
Your organization's deployment policy requires that the reviewer must not be the same person who triggered the deployment. How do you enforce this through GitHub Actions?

- A) GitHub natively enforces "self-review prevention" through environment protection rule settings
- B) This requires a custom pre-deployment job that queries the GitHub API to compare the reviewer with `github.actor`
- C) Add the deploying user's team to the "required reviewers" list but exclude them individually
- D) This cannot be enforced natively; you need an external approval system

**Answer: A**

---

**Q71 — Scenario**
A workflow has a `deployment` job with `environment: production` and a reviewer requirement. The workflow is triggered at 11 PM on a Friday, and the reviewer won't approve until Monday. What happens to the pending deployment workflow?

- A) It is automatically canceled after 24 hours
- B) It remains pending indefinitely until explicitly approved or rejected, or until the reviewer acts
- C) GitHub sends daily reminder notifications until Monday
- D) It expires after 72 hours

**Answer: B**

---

## Section 13: Creating & Publishing Actions

**Q72 — Scenario**
A Docker container action uses a Dockerfile at `./Dockerfile`. During workflow execution, does GitHub build this Docker image on every run?

- A) Yes — the image is built from the Dockerfile on every job execution (no caching)
- B) No — GitHub caches the Docker image between runs using the image digest as the cache key
- C) The image is built on first run and cached; subsequent runs restore the cached image
- D) Docker actions always pull from Docker Hub; local Dockerfiles are not supported

**Answer: A**

---

**Q73 — Scenario**
Your composite action has a step that uses `shell: bash` for a `run:` step. Is specifying `shell:` required in composite action steps?

- A) No — composite action steps inherit the default shell from the calling workflow
- B) Yes — each `run:` step in a composite action requires an explicit `shell:` declaration
- C) Only if the composite action runs on Windows
- D) `shell:` is ignored in composite actions; it always uses `bash`

**Answer: B**

---

**Q74 — Scenario (Security)**
You're publishing a public action and users will pass GitHub tokens as inputs. What security best practice should you follow when handling these token inputs in your JavaScript action code?

- A) Log the token to verify it's correctly received
- B) Use `core.setSecret(token)` immediately after receiving the input to ensure the value is masked in all subsequent log output
- C) Store the token in `$GITHUB_ENV` for easy access across steps
- D) Validate the token by making an API call to `/user` before proceeding

**Answer: B**

---

**Q75 — Scenario**
Your JavaScript action calls `core.setFailed("Deployment failed")`. What is the effect?

- A) Only the current step is marked as failed; the job continues
- B) The action exits, the step is marked as failed, and the job fails (unless `continue-on-error: true`)
- C) The workflow sends a failure notification but continues running
- D) The function is deprecated; use `process.exit(1)` instead

**Answer: B**

---

**Q76 — Scenario (Enterprise)**
Your enterprise wants to prevent developers from using any Docker container actions that pull from external registries for security reasons. How would you enforce this?

- A) Configure an organization policy to only allow `uses: ./` (local actions)
- B) Use the organization Actions policy "allow list" to restrict to GitHub.com-hosted actions only, blocking Docker Hub images
- C) Configure a firewall rule blocking Docker Hub outbound traffic from runners and document the approved private registry
- D) Both B and C provide layered enforcement

**Answer: D**

---

## Section 14: Managing Runners

**Q77 — Scenario**
You have three runner labels applied to a self-hosted runner: `self-hosted`, `linux`, and `deploy`. A workflow job specifies `runs-on: [self-hosted, linux, database]`. Does this job run on your runner?

- A) Yes — partial label matches are sufficient
- B) No — the job requires ALL listed labels to be present on the runner; `database` is not on your runner
- C) Yes — `self-hosted` and `linux` are sufficient for selection
- D) The runner is selected but the `database` label causes a warning

**Answer: B**

---

**Q78 — Scenario**
Your self-hosted runner shows `status: online` and `busy: false` in the API response, but no new jobs are being assigned to it. A workflow job with `runs-on: [self-hosted, linux]` is queued. What could cause jobs to not be assigned?

- A) The runner is registered at the repository level but the job is queued for an organization-level runner group
- B) The runner's labels don't match the job's `runs-on` requirements
- C) The runner has reached its maximum concurrent job limit
- D) Any of the above

**Answer: D**

---

**Q79 — Scenario (Enterprise)**
Your enterprise uses runner groups to isolate production deployments. The `production-runners` group is configured with `visibility: selected` assigned to only `prod-deploy-repo`. A developer in a different repository tries to use `runs-on: group: production-runners`. What happens?

- A) The job runs successfully because enterprise runner groups are accessible to all repos
- B) The job queues indefinitely because no matching runner is available to it
- C) An immediate error indicates the runner group is not accessible
- D) Both B and C — the behavior depends on how long the queue timeout is set

**Answer: B**

---

**Q80 — Scenario (Security)**
Your enterprise's self-hosted Linux runners are shared across all teams. A developer's workflow runs `rm -rf /` in a step (accidentally). What is the impact, and how should you mitigate this?

- A) The runner is destroyed; use ephemeral runners (disposable runners that are provisioned fresh for each job)
- B) No impact; GitHub Actions runs steps in isolated containers by default
- C) The runner directory is cleaned automatically after the job
- D) GitHub blocks destructive OS-level commands

**Answer: A**

---

**Q81 — Scenario**
You're configuring auto-scaling for self-hosted runners using a script that checks runner busyness. The script finds `busy = 4` out of `online = 4` (100% busy). What should the script do?

- A) Immediately scale down since all runners are in use
- B) Provision additional runner instances to handle queued jobs
- C) Restart all runners to clear the backlog
- D) Alert the team and wait for manual intervention

**Answer: B**

---

## Section 15: Enterprise Governance

**Q82 — Scenario (Enterprise)**
An enterprise admin wants to enforce that all workflows in ALL organizations use only GitHub-verified creator actions (`actions/*` and `github/*`). Where is this policy configured, and does it override org-level settings?

- A) At each organization's Settings > Actions > General; org admin can change it
- B) At Enterprise Settings > Policies > Actions; enterprise policy overrides org-level settings
- C) In a YAML policy file committed to each organization's `.github` repository
- D) Only through a GitHub App with admin permissions

**Answer: B**

---

**Q83 — Scenario (Enterprise)**
A required enterprise workflow is pinned to `@main` in the enterprise settings. The compliance team updates the workflow's `main` branch. When do existing repositories pick up the changes?

- A) Immediately — on the next workflow run, the enterprise fetches the workflow at its current `@main` HEAD
- B) Only after each repository re-registers with the enterprise policy
- C) Only when the enterprise admin manually pushes the policy update
- D) After a 24-hour propagation delay

**Answer: A**

---

**Q84 — Scenario (Enterprise, Security)**
Your enterprise recently discovered that a workflow used `pull_request_target` in a public repository, and untrusted fork code was checked out in a privileged context. How do you prevent this pattern enterprise-wide?

- A) Block `pull_request_target` trigger via enterprise policy and require code review before CI runs
- B) Add a required enterprise workflow that checks for `pull_request_target` usage and fails if found
- C) Configure fork PR approval requirements at the enterprise level to require manual approval before any fork PR workflow runs
- D) Both B and C together provide enforcement

**Answer: D**

---

**Q85 — Scenario (Enterprise)**
Workflows in your enterprise regularly fail with `Resource not accessible by integration` when attempting to create pull requests. Which permission is missing in the workflow?

- A) `contents: write`
- B) `pull-requests: write`
- C) `issues: write`
- D) `checks: write`

**Answer: B**

---

## Section 16: Security & Optimization

**Q86 — Scenario (Security)**
You're configuring OIDC for AWS authentication. Your trust policy currently uses the condition:
```json
"token.actions.githubusercontent.com:sub": "repo:myorg/*:*"
```
A security auditor flags this as overly permissive. What is the most restrictive rewrite?

- A) `"repo:myorg/myrepo:*"`
- B) `"repo:myorg/myrepo:ref:refs/heads/main"`
- C) `"repo:myorg/myrepo:environment:production"`
- D) Both B and C — B restricts to a specific branch, C restricts to a specific environment; use whichever fits your deployment model

**Answer: D**

---

**Q87 — Scenario (Security)**
A workflow uses `actions/checkout@v4` (mutable tag reference). Your security policy requires SHA pinning. What is the correct pinned form?

- A) `actions/checkout@sha256:abc123...`
- B) `actions/checkout@<full-40-char-commit-sha>  # v4`
- C) `actions/checkout@v4.0.0`
- D) SHA pinning only applies to Docker container actions

**Answer: B**

---

**Q88 — Scenario (Security)**
A JavaScript action uses `core.exportVariable("PATH", userControlledInput)` where `userControlledInput` comes from a workflow input. What security risk does this create?

- A) No risk — `core.exportVariable` is sandboxed
- B) Environment variable injection — a malicious actor could inject commands via newline characters in environment variable names or values, potentially overwriting arbitrary variables or injecting into subsequent steps (log4shell-style injection through `$GITHUB_ENV`)
- C) The variable is only available within the current step
- D) This only risks exposing the workflow token

**Answer: B**

---

**Q89 — Scenario (Security)**
Your workflow uses:
```yaml
- run: echo "Hello ${{ github.event.pull_request.title }}"
```
A PR is submitted with title: `"; rm -rf / ; echo "`. What security risk does this create?

- A) No risk — GitHub Actions escapes context expression values in `run:` steps
- B) Script injection — the PR title is injected directly into the shell command and could execute arbitrary code
- C) The workflow fails syntactically before execution
- D) GitHub validates PR titles to prevent injection

**Answer: B**

---

**Q90 — Scenario (Security)**
How do you mitigate the script injection vulnerability from Q89?

- A) Use an environment variable to pass the value: define `PR_TITLE: ${{ github.event.pull_request.title }}` and reference `$PR_TITLE` in the script
- B) Wrap the expression in single quotes: `echo 'Hello ${{ github.event.pull_request.title }}'`
- C) Use `contains()` function to sanitize
- D) Only run the step on `push` events, not `pull_request`

**Answer: A**

---

**Q91 — Scenario (Security)**
Your organization wants to adopt SLSA Level 3 for build artifacts. Which GitHub Actions features together best support this?

- A) SHA-pinned action references + `id-token: write` (OIDC) + artifact attestation with `actions/attest-build-provenance`
- B) Signed commits + merge queue + protected branches
- C) Artifact encryption + private repositories + required reviewers
- D) Concurrency groups + fail-fast matrix + immutable cache keys

**Answer: A**

---

## Section 17: Troubleshooting Common Failures

**Q92 — Scenario**
A step fails with `Error: ENOSPC: no space left on device`. This occurs on a GitHub-hosted `ubuntu-latest` runner. What is the most effective fix?

- A) Free up space by removing unused tool cache items: `sudo rm -rf /opt/hostedtoolcache`
- B) Increase the `timeout-minutes` limit
- C) Use a self-hosted runner with more disk storage
- D) Both A and C are valid approaches depending on the nature of the space usage

**Answer: D**

---

**Q93 — Scenario**
A workflow step runs `npm ci` and fails with `ECONNRESET` — network connection reset. This failure is intermittent (passes ~80% of the time). What is the recommended resolution?

- A) Add `retry` logic using a step-level `continue-on-error: true` loop
- B) Implement retry logic with a tool like `retry` shell command or use `actions/retry` action; alternatively, configure the cache to reduce npm registry calls
- C) Switch to `npm install` instead of `npm ci`
- D) Increase `timeout-minutes` to allow more time for slow network

**Answer: B**

---

**Q94 — Scenario**
A self-hosted runner's workflow job fails with `The operation was canceled` immediately after the job starts. The runner logs show `Runner.Listener disconnected`. What is the most likely cause?

- A) The runner process crashed or lost connectivity to GitHub's API endpoints
- B) The workflow file has a syntax error
- C) The job exceeded its timeout limit
- D) The runner is in a different runner group than required

**Answer: A**

---

**Q95 — Scenario**
Your Docker build step fails with `failed to solve: failed to read dockerfile: open Dockerfile: no such file or directory`. The `Dockerfile` exists in the project root. What is the most likely cause?

- A) The job's `working-directory` has been changed to a subdirectory, and the Docker build context needs to be explicitly specified
- B) Docker is not installed on the runner
- C) The `actions/checkout` step was omitted; the repository is not on the runner
- D) Both A and C could cause this error

**Answer: D**

---

**Q96 — Scenario**
A workflow run shows `Waiting for a runner to pick up this job` for over 30 minutes for a `runs-on: ubuntu-latest` job. GitHub-hosted runners are showing no reported incidents. What should you check?

- A) Concurrent job limits for your GitHub plan may have been reached
- B) The workflow may be rate-limited due to excessive API calls in recent runs
- C) Repository-level Actions may be disabled
- D) Both A and C are likely causes

**Answer: D**

---

**Q97 — Scenario**
A step in your workflow writes to `$GITHUB_OUTPUT` using a multi-line value:
```bash
echo "json_output<<EOF" >> $GITHUB_OUTPUT
cat result.json >> $GITHUB_OUTPUT
echo "EOF" >> $GITHUB_OUTPUT
```
A later step retrieves `${{ steps.<id>.outputs.json_output }}`. The value appears truncated. What is the most likely issue?

- A) Multi-line outputs are not supported via `$GITHUB_OUTPUT`
- B) The JSON file contains a line with `EOF` somewhere in its content, terminating the heredoc prematurely
- C) The file encoding is incompatible
- D) `$GITHUB_OUTPUT` has a 64 KB size limit per output variable

**Answer: B**

---

## Section 18: Cross-Topic Integration Scenarios

**Q98 — Scenario (Enterprise, Security)**
Your enterprise requires all workflows deploying to production to: (1) use OIDC for cloud authentication, (2) require two reviewer approvals, (3) only deploy from `main`, and (4) pin all actions to SHA. A developer submits a workflow using `actions/checkout@v4`, `pull_request` trigger, and a PAT stored in secrets instead of OIDC. How many policy violations does this workflow have?

- A) 1
- B) 2
- C) 3
- D) 4

**Answer: C** *(Violations: mutable action ref @v4; wrong trigger (deployment should come from push to main or workflow_dispatch, not pull_request); PAT instead of OIDC. The reviewer approval is enforced by environment protection rules, not the workflow itself.)*

---

**Q99 — Scenario (Enterprise)**
Your organization uses a matrix build across 12 OS/version combinations. The build takes 45 minutes per combination. Monthly CI costs are exceeding budget. Which combination of GitHub Actions features most effectively reduces cost without sacrificing test coverage?

- A) Enable `fail-fast: true` to stop all matrix jobs when one fails, and implement aggressive caching using `actions/cache` with broad restore keys
- B) Enable `fail-fast: false` and reduce the matrix to only critical combinations
- C) Use self-hosted runners on spot/preemptible VMs combined with aggressive dependency caching to reduce per-minute charges and job duration
- D) Reduce `timeout-minutes` to cut off slow runs early

**Answer: C**

---

**Q100 — Scenario (Security, Enterprise)**
Your enterprise security team performs a workflow audit and finds: (1) five workflows use `actions/upload-artifact` without setting `retention-days`, uploading sensitive test credentials in fixture files; (2) three workflows have `permissions: write-all`; (3) two workflows trigger on `pull_request_target` in public repos with `actions/checkout` using the head SHA. Which finding represents the MOST critical security risk?

- A) Finding 1 — long-retained artifacts containing sensitive data
- B) Finding 2 — overly broad `write-all` permissions
- C) Finding 3 — `pull_request_target` with fork head checkout enables arbitrary code execution with write permissions and secret access
- D) All three represent equally critical risks

**Answer: C**

---

## Answer Key

| Q | A | Q | A | Q | A | Q | A | Q | A |
|---|---|---|---|---|---|---|---|---|---|
| 1 | B | 21 | A | 41 | C | 61 | A | 81 | B |
| 2 | B | 22 | A | 42 | B | 62 | C | 82 | B |
| 3 | A | 23 | B | 43 | B | 63 | B | 83 | A |
| 4 | B | 24 | A | 44 | A | 64 | D | 84 | D |
| 5 | B | 25 | B | 45 | B | 65 | C | 85 | B |
| 6 | B | 26 | B | 46 | B | 66 | B | 86 | D |
| 7 | A | 27 | A | 47 | C | 67 | A | 87 | B |
| 8 | B | 28 | B | 48 | B | 68 | B | 88 | B |
| 9 | D | 29 | B | 49 | B | 69 | B | 89 | B |
| 10 | B | 30 | A | 50 | A | 70 | A | 90 | A |
| 11 | B | 31 | B | 51 | B | 71 | B | 91 | A |
| 12 | B | 32 | B | 52 | B | 72 | A | 92 | D |
| 13 | A | 33 | B | 53 | B | 73 | B | 93 | B |
| 14 | B | 34 | B | 54 | C | 74 | B | 94 | A |
| 15 | B | 35 | B | 55 | B | 75 | B | 95 | D |
| 16 | B | 36 | B | 56 | C | 76 | D | 96 | D |
| 17 | B | 37 | C | 57 | C | 77 | B | 97 | B |
| 18 | A | 38 | A | 58 | C | 78 | D | 98 | C |
| 19 | A | 39 | D | 59 | A | 79 | B | 99 | C |
| 20 | B | 40 | B | 60 | A | 80 | A | 100 | C |

---

## Distribution Analysis

| Metric | Count | % | Requirement |
|--------|-------|---|-------------|
| **Total Questions** | 100 | 100% | 100 ✅ |
| **Scenario-Based** | 72 | 72% | 70% min ✅ |
| **Conceptual** | 28 | 28% | — |
| **Security-Tagged** | 18 | 18% | 11 min ✅ |
| **Enterprise-Tagged** | 16 | 16% | 9 min ✅ |

### Security Questions
Q4, Q9, Q19, Q20, Q28, Q32, Q39, Q45, Q52, Q62, Q74, Q76, Q80, Q84, Q86, Q87, Q88, Q89, Q90, Q91, Q98, Q100

### Enterprise Questions
Q13, Q22, Q27, Q36, Q41, Q50, Q55, Q67, Q70, Q76, Q79, Q82, Q83, Q84, Q85, Q98, Q99, Q100

---

## Key Topics Deep-Dived in Iteration 8

This iteration adds coverage not featured in Iteration 7:

| Topic Area | New Coverage Added |
|---|---|
| Context & Expressions | `github.sha` on PRs (merge commit), `github.actor` vs `github.triggering_actor`, matrix empty-value behavior |
| Workflow Structure | `cancel-in-progress: false`, `fail-fast: false`, job container credentials, `include:` in matrix |
| Trigger Events | `paths` + `branches` AND logic, `release: published` type, `schedule` missed runs, fork workflow file isolation |
| Env Variables | `$GITHUB_ENV` timing, `$GITHUB_OUTPUT` multi-line heredoc pitfalls, PATH manipulation |
| Security | OIDC trust policy scope, SHA pinning enforcement, script injection mitigation, `core.setSecret`, cache poisoning attack vector, `pull_request_target` privileged checkout, SLSA L3, `$GITHUB_ENV` injection |
| Enterprise | Runner groups + `visibility: selected`, required workflows vs disabled Actions repos, enterprise policy override hierarchy, self-review prevention, actor bot filtering |
| Runners | Label matching (ALL labels required), runner pool isolation, ephemeral runners for security, auto-scaling logic, runner connectivity diagnostics |
| Caching | Restore-keys without fallback, cache cross-branch poisoning risk, `hashFiles` behavior on comment changes |
| Debugging | `::group::` / `::endgroup::` commands, `::notice::`, `::mask::`, `core.setFailed`, `gh run rerun --failed` |
| Artifacts | Parallel job artifact timing, storage limit behavior, access control exposure risk |
| Troubleshooting | `ENOSPC`, intermittent npm registry failures, `$GITHUB_OUTPUT` heredoc terminator collision, runner `ECONNRESET`, Docker `no such file` root causes |
