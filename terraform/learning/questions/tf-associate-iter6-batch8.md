# Terraform Associate (004) — Question Bank Iter 6 Batch 8

**Iteration**: 6
**Iteration Style**: Scenario-based troubleshooting — an engineer encounters a problem or unexpected outcome; identify the most likely cause and correct resolution
**Batch**: 8
**Objective**: 6/7/8 — State Backends, Locking, Importing & HCP Terraform
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 3 Easy / 8 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — DynamoDB Table Attribute Name Wrong — State Locking Fails

**Difficulty**: Easy
**Answer Type**: one
**Topic**: The DynamoDB lock table hash key must be exactly "LockID" — any other name causes locking failures

**Question**:
An engineer configures an S3 backend with state locking:

```hcl
terraform {
  backend "s3" {
    bucket         = "acme-tfstate"
    key            = "prod/terraform.tfstate"
    region         = "us-east-1"
    dynamodb_table = "terraform-state-lock"
    encrypt        = true
  }
}
```

The DynamoDB table was created with this Terraform resource:

```hcl
resource "aws_dynamodb_table" "lock" {
  name         = "terraform-state-lock"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "lockId"

  attribute {
    name = "lockId"
    type = "S"
  }
}
```

Running `terraform apply` fails with: `Error: Error acquiring the state lock: ... ValidationException: One or more parameter values were invalid: Type mismatch for key lockId expected: S got: NULL`. Concurrent runs are now possible without any locking. What is the cause?

- A) The `billing_mode = "PAY_PER_REQUEST"` is incompatible with Terraform state locking — use `PROVISIONED` mode instead
- B) DynamoDB state locking is not available in all AWS regions — the table must be in `us-east-1` specifically
- C) **The DynamoDB table's hash key must be named exactly `LockID`** (capital `L`, capital `I`, capital `D`) — Terraform's S3 backend writes and reads lock records using the hard-coded attribute name `LockID`; a table with a hash key named `lockId` (wrong casing) does not have the attribute Terraform expects, causing lock operations to fail with a validation error; the fix is to recreate the DynamoDB table with `hash_key = "LockID"` and a matching `attribute { name = "LockID" type = "S" }`
- D) The DynamoDB table must be in the same AWS account as the S3 bucket but a different region to prevent cross-service conflicts

**Answer**: C

**Explanation**:
The Terraform S3 backend expects a DynamoDB hash key with the **exact case-sensitive name `LockID`**. This is hard-coded in the S3 backend implementation. If the table uses any other attribute name — even `lockId`, `lock_id`, or `Lockid` — the backend cannot write or read lock entries and produces a `ValidationException`. The table name in the `dynamodb_table` argument can be anything, but the hash key attribute name must be `LockID`. The standard pattern from the Terraform documentation and AWS provider examples always uses `hash_key = "LockID"`. Recreating the table (or using `terraform import` to adopt a corrected table) with the correct attribute name resolves the issue.

---

### Question 2 — `-reconfigure` Used Instead of `-migrate-state` — Old State Abandoned

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Understanding the difference between terraform init -reconfigure and -migrate-state when changing backends

**Question**:
An engineer adds an S3 backend to a previously local-only configuration:

```hcl
terraform {
  backend "s3" {
    bucket = "acme-tfstate"
    key    = "prod/terraform.tfstate"
    region = "us-east-1"
  }
}
```

They run:

```bash
terraform init -reconfigure
```

The init succeeds. On the next `terraform plan`, all previously-managed resources appear as new resources to be created. The engineer is alarmed — their infrastructure already exists. What happened?

- A) The S3 bucket does not have versioning enabled, so the state was overwritten with an empty file during init
- B) `-reconfigure` triggers a state refresh that clears all resources older than 30 days
- C) **`terraform init -reconfigure` reinitialises the backend without migrating existing state** — it reconfigures the backend configuration but does not copy the old `terraform.tfstate` file to the new backend; the existing local state was left behind in the working directory; the new S3 backend starts with an empty state, so the next plan treats all previously-managed resources as unprovisioned; to migrate state to a new backend, the correct flag is `terraform init -migrate-state`, which prompts Terraform to copy the existing state to the new backend before switching
- D) The issue is that the S3 bucket did not exist before `terraform init` — the backend init silently created a new empty state when it could not find an existing file

**Answer**: C

**Explanation**:
Two flags serve different purposes when changing backend configuration. `terraform init -reconfigure` tells Terraform: "use this new backend configuration, but do NOT move any existing state — just reinitialise." The old state stays wherever it was (in this case, `terraform.tfstate` in the working directory). `terraform init -migrate-state` tells Terraform: "use this new backend configuration AND copy existing state from the previous backend to the new one." For any backend migration where existing infrastructure must be preserved, `-migrate-state` is the correct flag. After migration, verify with `terraform state list` that all resources appear, and then safely delete or archive the old local `terraform.tfstate` file.

---

### Question 3 — `terraform force-unlock` Runs While a Concurrent Apply Is Active

**Difficulty**: Easy
**Answer Type**: one
**Topic**: force-unlock is dangerous and should only be used when certain no other operation is running

**Question**:
An engineer runs `terraform apply` on a shared S3/DynamoDB-backed workspace. The apply is taking a long time and they suspect it has hung. A colleague runs `terraform force-unlock <LOCK_ID>` from their workstation to clear the lock so they can investigate. Two minutes later, the original engineer's apply completes successfully and writes its final state to S3. The colleague then immediately runs `terraform apply` — the plan shows a large number of unexpected changes. What is the risk that was realised?

- A) `terraform force-unlock` also rolls back any changes already made to cloud resources — the original apply's cloud changes were reverted
- B) There is no risk — `force-unlock` only releases the DynamoDB entry and has no effect on the state file itself
- C) **`terraform force-unlock` releases the DynamoDB lock record without any awareness of whether the locked operation is actually complete** — by unlocking while the original apply was still writing state, the colleague created a window where both operations could write state concurrently; the original apply wrote its final state, but a subsequent operation could read a stale state before that write landed, leading to conflicting state records; the safe use of `force-unlock` requires first **confirming with certainty** that no Terraform operation is running (e.g., the process has crashed, the CI job has been killed) — it must never be used as a shortcut to interrupt or "unstick" an operation that is still in progress
- D) The unexpected plan changes are caused by `force-unlock` incrementing the state serial number — the colleague's plan is reading a state with a higher serial than expected

**Answer**: C

**Explanation**:
`terraform force-unlock` removes the DynamoDB lock record unconditionally, with no check on whether the operation holding the lock is still running. It is designed for recovery from **crashed or orphaned processes** — situations where a process died without releasing its lock. Using it while an operation is still active is dangerous because it removes the mutual exclusion that prevents concurrent state writes. Two `terraform apply` operations writing state simultaneously can produce an inconsistent state (partial writes, serial conflicts, attribute mismatches) — which explains the unexpected plan changes the colleague saw. The correct procedure when a lock appears stuck: (1) check whether the locking process is truly dead (check CI job status, check Terraform Cloud run status); (2) only run `force-unlock` with the specific lock ID from the error message if you have confirmed the process is no longer running.

---

### Question 4 — `TF_LOG_CORE=DEBUG` Set But Provider API Calls Missing From Logs

**Difficulty**: Medium
**Answer Type**: one
**Topic**: TF_LOG_CORE captures only Terraform core logs — provider-level debugging requires TF_LOG_PROVIDER

**Question**:
An engineer is troubleshooting why an AWS resource is being re-created on every apply. They enable debug logging:

```bash
export TF_LOG_CORE=DEBUG
terraform apply 2>debug.log
```

They examine `debug.log` and find core planning logic but **no AWS API call details** — no HTTP requests, no responses, no provider plugin activity. They expected to see the full API conversation between Terraform and AWS. What is the cause and fix?

- A) Provider API calls are only visible when running `terraform apply` with the `-trace` flag, not via environment variables
- B) The `2>debug.log` redirection captures stderr but provider calls are written to a separate file in `.terraform/logs/`
- C) **`TF_LOG_CORE` controls logging only for the Terraform core binary** — it excludes provider plugin processes; AWS API calls, provider startup messages, and provider-level debug output are controlled by the separate `TF_LOG_PROVIDER` variable; to capture provider API calls, the engineer should set `TF_LOG_PROVIDER=TRACE` (or `DEBUG`); to capture both core and provider output in one go, set the unified `TF_LOG=TRACE` which applies to both; `TF_LOG_CORE` and `TF_LOG_PROVIDER` exist for cases where only one layer needs verbose output, avoiding the noise of the other
- D) Provider API calls are only logged when using HCP Terraform remote execution — local runs do not capture provider HTTP traffic regardless of log settings

**Answer**: C

**Explanation**:
Terraform's logging system distinguishes between two execution layers: (1) the **Terraform core binary** (planning, state management, graph resolution) — controlled by `TF_LOG_CORE`; (2) **provider plugins** (the actual API calls to AWS, Azure, GCP etc.) — controlled by `TF_LOG_PROVIDER`. Setting only `TF_LOG_CORE=DEBUG` produces planning and internal Terraform messages but no provider plugin output. For AWS API debugging (to see request/response payloads, error details, retry logic), `TF_LOG_PROVIDER=TRACE` is needed. The simplest approach when debugging unknown issues is to set `TF_LOG=TRACE`, which applies to both layers simultaneously. Use the split variables (`TF_LOG_CORE` and `TF_LOG_PROVIDER`) when the source of a problem is known and you want to reduce log noise from the unrelated layer.

---

### Question 5 — `terraform state mv` Done Without Updating HCL — Plan Shows Destroy and Create

**Difficulty**: Medium
**Answer Type**: one
**Topic**: terraform state mv moves the state address but if config is not updated to match, plan shows destroy+create at new/old addresses

**Question**:
An engineer renames an S3 bucket resource from `aws_s3_bucket.logs` to `aws_s3_bucket.access_logs` in the state only:

```bash
terraform state mv aws_s3_bucket.logs aws_s3_bucket.access_logs
```

The command succeeds. However, the engineer forgets to rename the resource block in `main.tf` — it still reads `resource "aws_s3_bucket" "logs"`. The next `terraform plan` shows:

- Destroy `aws_s3_bucket.access_logs` (exists in state, no config)
- Create `aws_s3_bucket.logs` (exists in config, no state)

What happened and what is the fix?

- A) `terraform state mv` always triggers a recreate — use `terraform state replace-provider` instead for safe renames
- B) The plan is correct — `state mv` only works permanently after `terraform apply` confirms the rename
- C) **`terraform state mv` moves the address in state but does not modify any `.tf` files** — after the command, state has `aws_s3_bucket.access_logs` but the configuration still has `resource "aws_s3_bucket" "logs"`; Terraform sees two mismatches: an orphaned state entry (`access_logs`) with no config, and a config resource (`logs`) with no state entry; to complete the rename, the engineer must also update `main.tf` to use `resource "aws_s3_bucket" "access_logs" { ... }`; after that change, state and config are aligned and `terraform plan` will show no changes; alternatively, the modern approach is to rename only the config resource and use a `moved` block, which avoids the need to manually run `state mv`
- D) The fix is to run `terraform state mv aws_s3_bucket.access_logs aws_s3_bucket.logs` to revert the state change, then rename only the HCL resource block

**Answer**: C

**Explanation**:
`terraform state mv` is a state-only operation — it modifies the state file's internal address mapping but leaves all `.tf` configuration files untouched. When you rename a resource in state without a matching rename in configuration, you create a mismatch on both sides: the new state address (`access_logs`) has no configuration counterpart, so Terraform plans to destroy it; the old configuration resource (`logs`) has no state entry, so Terraform plans to create it. Both the state AND the configuration must use the same address for Terraform to recognise them as the same resource. The correct two-step `state mv` workflow: (1) run `terraform state mv old_address new_address`; (2) rename the resource block in configuration to match. The `moved` block (Terraform 1.1+) is cleaner because it handles both the state address update and the configuration rename in a single declarative step during `terraform apply`.

---

### Question 6 — Sentinel `hard-mandatory` Policy Cannot Be Overridden

**Difficulty**: Medium
**Answer Type**: one
**Topic**: hard-mandatory enforcement level blocks a run unconditionally — even org owners cannot override it

**Question**:
An HCP Terraform organisation enforces a Sentinel policy that requires all EC2 instances to use approved AMI IDs. The policy is configured with `enforcement_level = "hard-mandatory"`. A team lead deploys a new configuration using a custom AMI not in the approved list. The policy check fails. The team lead is an organisation **Owner** (the highest HCP Terraform role) and clicks the "Override" button in the HCP Terraform UI — but the button is disabled. They escalate to HashiCorp support, believing this is a bug. Is it?

- A) Yes — organisation Owners should always be able to override any policy; a disabled override button is a bug
- B) No — the override button is disabled because the workspace must be in "admin" mode for owners to override Sentinel policies
- C) **No — `hard-mandatory` is the only Sentinel enforcement level that cannot be overridden by any user, regardless of role** — the three enforcement levels are: `advisory` (failure is a warning, run proceeds automatically); `soft-mandatory` (failure blocks the run, but an authorised user such as an Owner can click Override to allow the run to proceed); `hard-mandatory` (failure unconditionally blocks the run — the Override button is permanently disabled and no role has the ability to proceed); to resolve the team lead's situation, either the configuration must be updated to use an approved AMI, or a HashiCorp Sentinel policy administrator must change the enforcement level or update the approved AMI list in the policy
- D) No — only users with the Sentinel policy management permission can override `hard-mandatory` policies, and the team lead's Owner role does not include this permission by default

**Answer**: C

**Explanation**:
Sentinel provides three enforcement levels with a clear escalating hierarchy. `advisory` is purely informational — it logs a warning but never blocks a run. `soft-mandatory` blocks the run until an authorised user (a user with Override permission, typically an Owner) manually approves overriding the failing policy. `hard-mandatory` provides the strongest enforcement: **no user — not even organisation Owners, not HashiCorp support — can override it through the UI or API**. The run is permanently blocked until the configuration change itself satisfies the policy. This is by design for compliance mandates that must not allow exceptions (e.g., CIS benchmarks, regulatory requirements). Organisations should use `hard-mandatory` only for truly non-negotiable controls and `soft-mandatory` for policies that might need legitimate exceptions.

---

### Question 7 — HCP Terraform Workspace Variable Silently Overrides `terraform.tfvars`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Workspace variables in HCP Terraform take precedence over all file-based and CLI variable sources

**Question**:
A team uses HCP Terraform with a VCS-connected workspace. The repository contains a `terraform.tfvars` file setting `instance_type = "t3.large"`. Despite this, every apply deploys `t3.micro` instances. The engineer has confirmed the `terraform.tfvars` file is correct and in the right directory. Runs complete without errors. What is the most likely cause?

- A) HCP Terraform ignores `terraform.tfvars` in all cases — file-based variable files are not supported in remote runs
- B) The `terraform.tfvars` file must be named `override.tfvars` for HCP Terraform to load it during remote runs
- C) **A workspace variable named `instance_type` is set to `"t3.micro"` in the HCP Terraform workspace settings** — workspace variables have the **highest precedence** among all variable sources in HCP Terraform runs; they override `terraform.tfvars`, `.auto.tfvars`, `-var` flags, and `TF_VAR_` environment variables; the engineer should check the workspace's "Variables" tab in the HCP Terraform UI and remove or update the `instance_type` workspace variable; workspace variables are intentionally the highest-priority source so that operators can enforce environment-specific values without modifying VCS-tracked files
- D) HCP Terraform caches variable values from the first run — the workspace is using a stale variable value; run `terraform init -reconfigure` to clear the variable cache

**Answer**: C

**Explanation**:
In HCP Terraform runs, variable precedence follows a specific order (from lowest to highest): defaults in `variable` blocks → `terraform.tfvars` → `.auto.tfvars` → `TF_VAR_` env vars → workspace variables set in HCP Terraform UI/API. Workspace variables are intentionally the highest-priority source. This design allows operations teams to set environment-specific values (different regions, instance sizes, feature flags per environment) in the workspace UI without modifying tracked configuration files. When a workspace variable and a `tfvars` file set the same variable, the workspace variable always wins — silently. Engineers should always check the workspace's Variables tab when a value is not matching expectations, especially when the configuration files appear correct. This is a commonly misunderstood precedence issue on the exam.

---

### Question 8 — `terraform_remote_state` Errors Because Output Not Declared in Source Workspace

**Difficulty**: Medium
**Answer Type**: one
**Topic**: terraform_remote_state can only access values explicitly declared as outputs in the source workspace configuration

**Question**:
A team reads networking infrastructure outputs from a separate workspace:

```hcl
data "terraform_remote_state" "networking" {
  backend = "remote"
  config = {
    organization = "acme-corp"
    workspaces = {
      name = "networking-production"
    }
  }
}

resource "aws_instance" "app" {
  subnet_id = data.terraform_remote_state.networking.outputs.private_subnet_id
}
```

Running `terraform plan` fails: `Error: Unsupported attribute — This object does not have an attribute named "private_subnet_id"`. The engineer confirms the networking workspace exists and has a working state. What is the cause?

- A) The `terraform_remote_state` data source uses `backend = "remote"` but should use `backend = "cloud"` for HCP Terraform workspaces
- B) Cross-workspace state access requires a team token with Admin-level permissions on the networking workspace
- C) **`terraform_remote_state` only exposes values that are explicitly declared as `output` blocks in the source workspace's Terraform configuration** — the networking workspace's state may contain a `private_subnet_id` attribute on a resource, but unless the networking workspace declares `output "private_subnet_id" { value = ... }` in its configuration, the value is not exposed through the `outputs` object of `terraform_remote_state`; the fix is to add the required output to the networking workspace's configuration: `output "private_subnet_id" { value = aws_subnet.private.id }`, apply it, and the consuming workspace can then reference `data.terraform_remote_state.networking.outputs.private_subnet_id`
- D) The `outputs` attribute path is incorrect — use `data.terraform_remote_state.networking.state.private_subnet_id` to access resource attributes directly from remote state

**Answer**: C

**Explanation**:
`terraform_remote_state` only provides access to values that have been explicitly declared as `output` blocks in the source workspace. It does not allow arbitrary access to resource attributes in the remote state — the outputs act as a defined API between workspaces. This encapsulation is intentional: it forces workspace authors to be explicit about what they share, creating a stable interface. If `private_subnet_id` exists as a resource attribute in the networking workspace but has no corresponding `output` declaration, it is invisible to `terraform_remote_state` consumers. Adding the output, running `terraform apply` in the networking workspace to publish it, and then re-running the consuming workspace's plan resolves the error. This pattern is analogous to module output encapsulation — the `output` block is the deliberate contract.

---

### Question 9 — `terraform login` Token Not Available in CI — Env Var Alternative Needed

**Difficulty**: Medium
**Answer Type**: one
**Topic**: terraform login stores tokens in a local file not available in CI — TF_TOKEN_* env var is the correct CI approach

**Question**:
A developer successfully authenticates to HCP Terraform using `terraform login` on their laptop. They then add the same configuration to a CI/CD pipeline (GitHub Actions) and expect the pipeline to be authenticated automatically. The CI job fails: `Error: Required token could not be found: No credentials for app.terraform.io`. The developer is confused because their local machine works perfectly. What is the cause and fix?

- A) `terraform login` authentication tokens expire after 24 hours — the developer must re-run `terraform login` in the CI job before each run
- B) GitHub Actions blocks outbound connections to `app.terraform.io` for security — the organisation's GitHub Actions settings must whitelist the HCP Terraform domain
- C) **`terraform login` stores the authentication token in a local file on the developer's machine** (`~/.terraform.d/credentials.tfrc.json`) — this file exists only on that machine and is never available in CI runners, which start from a clean environment on every job; to authenticate Terraform in a CI/CD pipeline, the correct approach is to store the HCP Terraform API token as a CI secret and expose it via the `TF_TOKEN_app_terraform_io` environment variable (e.g., `TF_TOKEN_app_terraform_io: ${{ secrets.TF_API_TOKEN }}` in GitHub Actions); Terraform reads this environment variable automatically instead of the credentials file
- D) The CI pipeline must run `terraform init` with the `-token` flag pointing to a credentials file bundled in the repository

**Answer**: C

**Explanation**:
`terraform login` performs an interactive browser-based OAuth flow and writes the resulting API token to `~/.terraform.d/credentials.tfrc.json` — a user-specific file on the local machine. CI runners are ephemeral environments that start fresh on each job run, with no access to a developer's home directory. The standard CI authentication pattern is: (1) generate a team token (or user token) in the HCP Terraform UI; (2) store it as an encrypted secret in the CI system (GitHub Actions secret, GitLab CI variable, etc.); (3) expose it to the Terraform process via the `TF_TOKEN_app_terraform_io` environment variable. The environment variable format is `TF_TOKEN_<hostname_with_dots_replaced_by_underscores>`. Terraform checks for `TF_TOKEN_*` variables before the credentials file, so CI pipelines work without any file system setup. Using team tokens (rather than personal user tokens) for CI is also a security best practice.

---

### Question 10 — Run Triggers Not Configured — Workspace B Not Auto-Triggered

**Difficulty**: Medium
**Answer Type**: one
**Topic**: HCP Terraform run triggers must be explicitly configured — workspace B does not automatically follow workspace A

**Question**:
A team has two HCP Terraform workspaces: `networking` (manages VPCs and subnets) and `compute` (manages EC2 instances that depend on `networking` outputs). The team assumes that when `networking` completes an apply, `compute` will automatically queue a new run to pick up any networking changes. After updating security group rules in `networking`, the `compute` workspace does not receive a new run. The EC2 instances are still using stale subnet configurations. What is the cause?

- A) HCP Terraform only auto-triggers dependent workspaces when the source workspace is connected to a VCS repository — API-driven workspaces do not support automatic triggering
- B) Auto-triggering between workspaces requires both workspaces to be in the same HCP Terraform Project
- C) **HCP Terraform does not automatically detect dependencies between workspaces or trigger runs based on them** — run triggers between workspaces must be **explicitly configured** in the `compute` workspace settings under "Run Triggers"; once configured, when `networking` completes a successful apply, HCP Terraform queues a new plan-and-apply run in `compute`; without this explicit configuration, the two workspaces are entirely independent — no matter how many times `networking` applies, `compute` never receives an automatic trigger; the engineer must navigate to the `compute` workspace settings, add `networking` as a source workspace under Run Triggers, and re-apply
- D) Run triggers require the `terraform_remote_state` data source to be configured in the dependent workspace before HCP Terraform can detect the dependency and trigger automatically

**Answer**: C

**Explanation**:
HCP Terraform workspaces are isolated by default — there is no automatic dependency detection or cascading apply behaviour. Run triggers are an explicit, manually-configured feature. In the consuming workspace (`compute`), an operator adds the source workspace (`networking`) as a run trigger source in the workspace settings. After this configuration, whenever `networking` completes a successful apply, HCP Terraform automatically queues a new run in `compute`. This explicit configuration is intentional: implicit auto-triggering based on state-reading relationships could cause unexpected cascading applies in large organisations. The run trigger feature, combined with `terraform_remote_state` for data sharing, is the correct pattern for orchestrating dependent workspaces. Note: run triggers fire on successful apply completion, not on every plan.

---

### Question 11 — HCP Terraform Health Assessment Does Not Auto-Remediate Drift

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Health assessments detect and report drift on a schedule but never automatically apply changes to fix it

**Question**:
An operations team enables Health Assessments on their HCP Terraform workspace, setting it to run every 24 hours. After a cloud engineer manually modifies a security group in the AWS Console (creating drift), the health assessment runs and reports the drift. The operations team then waits, expecting HCP Terraform to automatically queue an apply to restore the configuration to the desired state. After several hours, no apply has run. Is this expected behaviour?

- A) No — Health Assessments are supposed to queue a remediation apply automatically; the delay indicates a workspace configuration issue such as auto-apply being disabled
- B) No — Health Assessments automatically apply corrections for drift on resources tagged with `health_assessment = "auto_remediate"` in their Terraform configuration
- C) **Yes — Health Assessments in HCP Terraform are a drift detection and reporting tool only; they never automatically queue or execute remediation applies** — when a health assessment detects drift (via an internal `terraform plan -refresh-only` equivalent), it surfaces the results in the HCP Terraform UI, sends notifications through configured channels (email, Slack), and updates the workspace's health status indicator; it is the responsibility of an operator to review the drift report and manually queue a new plan-and-apply run (or configure auto-apply) to remediate the drift; automatic remediation is not a health assessment feature because blindly re-applying in response to drift could mask legitimate manual changes or cause unintended infrastructure modifications
- D) No — Health Assessments in HCP Terraform trigger a speculative plan run that is automatically applied if the drift is smaller than a configurable threshold

**Answer**: C

**Explanation**:
Health Assessments are a **scheduled drift detection** feature — they run `terraform plan -refresh-only` on a configurable schedule (e.g., every 24 hours or every hour) and surface the results in the HCP Terraform UI and notification channels. They answer the question: "has the actual state of my infrastructure drifted from what Terraform expects?" They do not answer the question by fixing the drift. There is no auto-remediation capability. This is deliberate: not all drift is undesirable (e.g., a database's allocated storage might have been legitimately increased by a DBA), and automatically re-applying could revert intentional manual changes or trigger disruptive replacements. The correct response to a health assessment drift alert is for an operator to review the change, decide whether to remediate, and manually queue or approve a plan-and-apply run.

---

### Question 12 — TWO Safeguards Required Before Running `terraform state push`

**Difficulty**: Hard
**Answer Type**: many
**Topic**: terraform state push is destructive — two safeguards must be in place to prevent overwriting newer or correct state

**Question**:
Which TWO safeguards must be in place before running `terraform state push` against a remote backend to prevent irreversible state damage? (Select two.)

- A) **Verify that the serial number in the local state file being pushed is equal to or greater than the serial in the remote state** — `terraform state push` overwrites the remote state unconditionally; if the local file being pushed has a lower serial number than the current remote state, the push overwrites newer remote state with older data, potentially losing records of resources created, modified, or destroyed since the local file was saved; backends with versioning (such as S3 with versioning enabled) allow recovery, but the damage may not be noticed immediately
- B) Run `terraform validate` on the local state file before pushing to check for schema errors
- C) **Confirm that no Terraform operation is currently running against the remote backend** — if a plan or apply is in progress, pushing a state file mid-operation creates a race condition where two processes have divergent views of infrastructure truth; the running operation may complete and overwrite the pushed state, or the pushed state may cause the in-progress operation to act on stale resource information; always verify the workspace/backend is idle before using `state push`
- D) Run `terraform plan -refresh-only` after pushing to confirm the state matches cloud reality before running any apply

**Answer**: A, C

**Explanation**:
`terraform state push` is one of the most dangerous Terraform operations because it overwrites the authoritative remote state without a safety prompt or undo mechanism. Two safeguards are critical: **(A) Serial verification** — the state serial is an integer that increments with every state write; pushing a state with a lower serial than the current remote state overwrites newer data; before pushing, run `terraform state pull | jq '.serial'` to check the remote serial and compare it to the local file's serial; only push if the local serial is at least as high, or if you are deliberately rolling back a corrupt state (in which case you need to manually increment the serial before pushing); **(C) No concurrent operations** — pushing during an active operation creates state divergence; the workspace must be idle, the backend lock should be free, and ideally the push is performed during a maintenance window. **(B)** is wrong — `terraform validate` works on configuration, not state files. **(D)** is reasonable post-push verification but is not a safeguard before pushing.

---

### Question 13 — TWO HCP Terraform Mechanisms That Eliminate Storing Static Credentials

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Dynamic provider credentials (OIDC) and team tokens eliminate two different kinds of static credential storage

**Question**:
A security team reviews an HCP Terraform organisation and identifies two categories of static credentials that create security risks: (1) long-lived cloud provider credentials (AWS access keys) stored as sensitive workspace environment variables; (2) individual user API tokens used in CI/CD pipelines. Which TWO HCP Terraform mechanisms eliminate each of these risks respectively? (Select two.)

- A) **Dynamic Provider Credentials using OIDC (OpenID Connect)** — eliminates the need for static cloud provider credentials (AWS access keys, Azure service principal secrets, GCP service account keys) stored in workspace variables; instead, HCP Terraform workspaces present a signed OIDC token to the cloud provider's IAM system; the cloud provider validates the token against a configured OIDC trust relationship and returns short-lived credentials scoped to that specific run; credentials automatically expire and are never stored in HCP Terraform; this removes static `AWS_ACCESS_KEY_ID` / `AWS_SECRET_ACCESS_KEY` environment variables from workspace settings entirely
- B) Sentinel policies with `hard-mandatory` enforcement can prevent static credentials from being added to workspace variables — this is equivalent to eliminating them
- C) **Team Tokens** — eliminate the need for individual user API tokens in CI/CD pipelines; instead of each CI pipeline authenticating to HCP Terraform with a specific user's personal token (which is tied to an individual's account, expires with their employment, and may have excessive personal permissions), a Team Token represents the team's combined access to all assigned workspaces; the team token can be rotated independently of any individual, scoped to only the workspaces the team can access, and stored as a CI system secret without tying pipeline access to any employee's personal credentials
- D) Variable Sets can store cloud credentials centrally — this reduces credential duplication but does not eliminate the static credentials themselves

**Answer**: A, C

**Explanation**:
Two distinct credential categories with two distinct HCP Terraform solutions: **(A) Dynamic Provider Credentials / OIDC** addresses **cloud provider authentication** — the elimination of static AWS/Azure/GCP credentials stored as sensitive environment variables in workspace settings. With OIDC, HCP Terraform becomes a trusted identity provider; each run receives a short-lived, automatically-expiring token from the cloud provider with only the permissions needed for that run. This is significantly more secure than long-lived access keys. **(C) Team Tokens** address **HCP Terraform authentication** in automated systems — the elimination of individual user tokens in CI/CD pipelines. Team tokens are service-account-like credentials that are not tied to a specific user's lifecycle, can be scoped by team-level workspace access, and are the recommended authentication method for any non-interactive system. **(B)** is wrong — Sentinel can enforce policy but cannot eliminate credentials that are already stored. **(D)** is wrong — variable sets centralise credentials but do not eliminate them; the static values still exist, just in one place instead of many.

---
