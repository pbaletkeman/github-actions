# Terraform Associate (004) — Question Bank Iter 2 Batch 8

**Iteration**: 2
**Iteration Style**: Behavioral prediction — what happens when code runs or a scenario occurs
**Batch**: 8
**Objective**: 7 — Maintaining Infra + 8 — HCP Terraform
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 3 Easy / 8 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — TF_LOG Without TF_LOG_PATH

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Where Terraform writes log output when `TF_LOG` is set but `TF_LOG_PATH` is not

**Question**:
An engineer sets the following before running a command:

```bash
export TF_LOG=TRACE
terraform apply
```

No `TF_LOG_PATH` variable is set. Where does Terraform write the log output?

- A) To a file named `terraform.log` created automatically in the current working directory
- B) To stderr — all log output is written to the standard error stream
- C) To stdout, interleaved with the normal plan and apply output
- D) Logging is disabled when `TF_LOG_PATH` is not configured

**Answer**: B

**Explanation**:
When `TF_LOG` is set without `TF_LOG_PATH`, Terraform writes all log output to **stderr**. This means log lines appear in the terminal alongside normal output but can be separated using standard shell redirection (e.g., `terraform apply 2>terraform.log`). Setting `TF_LOG_PATH` redirects stderr log output to a file instead. Option D is incorrect — `TF_LOG_PATH` is optional; the absence of it does not disable logging.

---

### Question 2 — VCS Pull Request Triggers a Run

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Which HCP Terraform run type is automatically triggered when a pull request is opened

**Question**:
A team has connected an HCP Terraform workspace to a GitHub repository. A developer opens a pull request targeting the `main` branch. What does HCP Terraform automatically do?

- A) It queues a plan-and-apply run that immediately applies changes to the workspace
- B) It queues a plan-only run that waits for manual approval before it can be applied
- C) It triggers a speculative plan and posts the result as a status check on the pull request
- D) Nothing happens until the pull request is merged; HCP Terraform only reacts to merge events

**Answer**: C

**Explanation**:
In a VCS-connected HCP Terraform workspace, opening a pull request automatically triggers a **speculative plan** — a read-only plan that can never progress to apply. The results are posted back to the pull request as a status check, giving reviewers visibility into what infrastructure changes the PR would cause. The apply-triggering event is a merge to the target branch, not PR creation. This workflow supports code review with infrastructure change visibility before any changes are made.

---

### Question 3 — terraform state show Output

**Difficulty**: Easy
**Answer Type**: one
**Topic**: What `terraform state show` outputs for a specific resource

**Question**:
An engineer runs:

```bash
terraform state show aws_instance.web
```

What does this command output?

- A) A JSON document representing the entire state file
- B) The HCL resource block exactly as it appears in the Terraform configuration file
- C) All recorded attributes of the `aws_instance.web` resource as stored in the state file
- D) An execution plan showing what Terraform would change about `aws_instance.web`

**Answer**: C

**Explanation**:
`terraform state show <address>` outputs all attributes stored in the state for a specific resource, including values that are not present in the configuration file (such as the resource's `id`, computed attributes like `public_ip`, and all provider-tracked metadata). It is formatted in a human-readable, attribute-value style. This is the primary tool for inspecting a resource's current tracked state and is especially useful after a `terraform import` to see all attributes that need to be replicated in the configuration.

---

### Question 4 — CLI Import When Resource Already in State

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What happens when `terraform import` targets a resource address already tracked in state

**Question**:
A resource `aws_s3_bucket.logs` already exists in the Terraform state file and is actively managed. An engineer runs:

```bash
terraform import aws_s3_bucket.logs existing-log-bucket
```

What happens?

- A) The import succeeds and refreshes the state entry with the latest attributes from the cloud
- B) Terraform raises an error indicating that `aws_s3_bucket.logs` is already managed in state
- C) Terraform creates a second, duplicate state entry for the same cloud resource
- D) The existing state entry is silently overwritten with the newly imported attributes

**Answer**: B

**Explanation**:
`terraform import` will not overwrite or update an existing state entry. If the target resource address already exists in state, Terraform raises an error and aborts the import without modifying state. This prevents accidental state corruption. If you need to update tracked attributes for an existing resource, use `terraform refresh` (deprecated) or `terraform apply -refresh-only` to synchronise state with the current cloud resource attributes.

---

### Question 5 — import Block With an Invalid Cloud Resource ID

**Difficulty**: Medium
**Answer Type**: one
**Topic**: When Terraform surfaces an error if the `import` block's `id` does not match any real cloud resource

**Question**:
An engineer writes this configuration:

```hcl
import {
  to = aws_instance.app
  id = "i-DOESNOTEXIST"
}

resource "aws_instance" "app" {
  ami           = "ami-0abc123"
  instance_type = "t3.micro"
}
```

The instance ID `i-DOESNOTEXIST` does not correspond to any real EC2 instance. What happens when `terraform plan` is run?

- A) The import is silently skipped and Terraform plans to create a new `aws_instance.app`
- B) The plan succeeds but Terraform logs a warning about the invalid ID
- C) Terraform raises a provider error during the planning phase because the resource with that ID was not found in the cloud
- D) The error only surfaces at `terraform apply` time; the plan phase cannot validate cloud resource IDs

**Answer**: C

**Explanation**:
When `terraform plan` processes an `import` block, it calls the provider's Read function to retrieve the current state of the resource using the supplied `id`. If no resource with that ID exists in the cloud, the provider returns a "not found" error during the planning phase. This early error detection is one advantage of the declarative `import` block over the legacy CLI import command — problems with the import configuration are caught before any changes are applied.

---

### Question 6 — generate-config-out When Resource Block Already Exists

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What happens when `-generate-config-out` is used but the target resource block already exists

**Question**:
An engineer has this configuration in `main.tf`:

```hcl
import {
  to = aws_vpc.main
  id = "vpc-0abc1234"
}

resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"
}
```

The engineer then runs:

```bash
terraform plan -generate-config-out=generated.tf
```

What happens?

- A) The existing `resource "aws_vpc" "main"` block in `main.tf` is overwritten with the generated version
- B) Terraform raises an error: `-generate-config-out` cannot be used when the resource block already exists in configuration
- C) A second copy of the resource block is written to `generated.tf`, and Terraform warns about the duplication
- D) The `-generate-config-out` flag is silently ignored since a resource block already exists; the plan proceeds normally

**Answer**: B

**Explanation**:
`terraform plan -generate-config-out=file.tf` is designed for the initial bootstrap of missing HCL configuration for an imported resource. If the target resource block already exists in the configuration, Terraform raises an error because it cannot write a duplicate block — duplicate `resource` addresses in HCL are invalid. The correct workflow is to use `-generate-config-out` only when no resource block exists yet, review the generated file, and then run `terraform apply` to complete the import.

---

### Question 7 — Run Trigger From Upstream Workspace Apply

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What HCP Terraform does to a downstream workspace when an upstream apply completes via a run trigger

**Question**:
Workspace B (`compute`) is configured in HCP Terraform with Workspace A (`networking`) set as a **run trigger** source. Workspace A manages the VPC and subnets that Workspace B depends on. Workspace A's `terraform apply` completes successfully. What does HCP Terraform do next?

- A) Workspace B immediately begins executing without a separate planning phase
- B) Workspace B is automatically queued for a new plan-and-apply run
- C) Workspace B receives a notification in the UI but requires a developer to manually trigger a run
- D) Workspace B triggers a run only if Workspace A's apply produced at least one resource change

**Answer**: B

**Explanation**:
HCP Terraform run triggers automatically queue a new **plan-and-apply** run in the downstream workspace (B) when the upstream workspace (A) completes a successful apply. This is unconditional — it triggers regardless of whether any resources actually changed in Workspace A. The intent is to propagate upstream infrastructure changes (such as new subnet IDs exposed via outputs) through dependent workspaces without manual intervention. Teams use this to model workspace dependency graphs.

---

### Question 8 — hard-mandatory Policy Failure Outcomes

**Difficulty**: Medium
**Answer Type**: many
**Topic**: What happens when a `hard-mandatory` Sentinel policy fails

**Question**:
A `hard-mandatory` Sentinel policy assigned to a workspace fails during a plan. Which TWO statements correctly describe what happens next? (Select two.)

- A) The run is blocked and cannot proceed to apply
- B) An organisation Owner can override the failure from the HCP Terraform UI to allow the run to proceed
- C) No user or role can override the failure — the policy code must pass before the run can continue
- D) The run proceeds to apply but records the policy failure in the audit log as a warning

**Answer**: A, C

**Explanation**:
`hard-mandatory` is the strictest policy enforcement level. When it fails: (A) the run is definitively blocked and cannot progress to apply. (C) unlike `soft-mandatory`, there is no override mechanism available to any user or team regardless of their permission level — the policy must actually pass (or the policy assignment must be removed/changed by a policy admin). Option B describes the behaviour of `soft-mandatory`, and Option D describes `advisory` enforcement.

---

### Question 9 — soft-mandatory Override Attempt by Write-Level User

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Whether a user with Write workspace permission can override a soft-mandatory policy failure

**Question**:
A `soft-mandatory` Sentinel policy fails during a run in an HCP Terraform workspace. A developer with **Write** workspace permission attempts to override the policy failure using the override button in the HCP Terraform UI. What happens?

- A) The override succeeds — Write permission includes the ability to override soft-mandatory policy failures
- B) The override fails — overriding soft-mandatory policies requires the "Manage Policy Overrides" permission, which is not included in Write access
- C) The override button is not visible to the developer — it is only shown to users with Admin workspace permission
- D) The override succeeds only if the developer is also a member of the team that manages the failing policy set

**Answer**: B

**Explanation**:
Overriding a `soft-mandatory` policy failure requires the **"Manage Policy Overrides"** workspace permission, which is a separate capability not bundled into the Write permission level. Write permission grants the ability to trigger and approve runs but does not extend to policy governance. This separation ensures that policy enforcement is not trivially bypassed by developers with standard run access. A user needs either explicit "Manage Policy Overrides" access or Admin workspace permission to override soft-mandatory failures.

---

### Question 10 — Health Assessment Detects Drift

**Difficulty**: Medium
**Answer Type**: many
**Topic**: What HCP Terraform does when a scheduled health assessment detects configuration drift

**Question**:
HCP Terraform health assessments are enabled for a workspace. On its next scheduled run, the assessment detects that an EC2 instance's `instance_type` was manually changed in the AWS Console from `t3.micro` to `t3.small`. Which TWO things happen as a result? (Select two.)

- A) HCP Terraform automatically queues a `terraform apply` run to revert the instance type back to `t3.micro`
- B) The workspace's health status is marked as drifted in the HCP Terraform UI
- C) Notifications are sent to configured notification destinations alerting the team to the detected drift
- D) The workspace is locked to prevent further runs until the team manually resolves the drift

**Answer**: B, C

**Explanation**:
HCP Terraform health assessments run `terraform plan -refresh-only` on a configurable schedule. When drift is detected: (B) the workspace health status in the UI is updated to show the workspace as drifted, giving teams passive visibility into infrastructure drift. (C) notifications are dispatched to configured channels (Slack, email, webhooks). Critically, (A) is incorrect — health assessments are read-only operations that never auto-apply; the team must decide how to respond. (D) is also incorrect — the workspace is not locked.

---

### Question 11 — terraform_remote_state Output That Does Not Exist

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What happens at plan time when a `terraform_remote_state` reference targets a nonexistent output

**Question**:
A root module uses this configuration:

```hcl
data "terraform_remote_state" "network" {
  backend = "remote"
  config = {
    organization = "my-org"
    workspaces = { name = "networking" }
  }
}

resource "aws_instance" "app" {
  subnet_id = data.terraform_remote_state.network.outputs.private_subnet_id
}
```

The `networking` workspace state exists and is accessible, but it does **not** export an output named `private_subnet_id`. What happens during `terraform plan`?

- A) `subnet_id` is set to `null` — Terraform treats missing remote state outputs as null values
- B) Terraform raises an error during planning because `private_subnet_id` does not exist in the remote workspace outputs
- C) `terraform plan` succeeds but `aws_instance.app` is omitted from the plan until the output exists
- D) Terraform substitutes an empty string `""` and emits a warning about the missing output

**Answer**: B

**Explanation**:
`data.terraform_remote_state.<name>.outputs.<key>` must reference an output that is actually declared and present in the remote state. If `private_subnet_id` is not exported by the `networking` workspace, Terraform raises a plan-time error indicating the attribute path does not exist. There is no automatic null substitution or fallback. The fix is to add `output "private_subnet_id" {}` to the networking workspace configuration, apply it, and then re-run the dependent workspace's plan.

---

### Question 12 — Workspace Variable Overrides Variable Set

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Variable precedence when the same key is defined in both a variable set and a workspace variable

**Question**:
An HCP Terraform organisation has a variable set named "Shared Defaults" containing:

```
environment = "default"
```

This variable set is assigned to all workspaces. The `production` workspace also has a workspace-level variable defined directly:

```
environment = "production"
```

When Terraform runs in the `production` workspace, what value does `var.environment` have?

- A) `"default"` — variable sets take precedence over workspace-level variables
- B) `"production"` — workspace-level variables take precedence over variable sets
- C) An error is raised — HCP Terraform does not allow the same variable key to be defined in both a variable set and a workspace variable
- D) The run prompts the user to choose which value to use before planning begins

**Answer**: B

**Explanation**:
In HCP Terraform's variable resolution hierarchy, **workspace-level variables take precedence over variable sets** when the same key is defined in both. This design allows teams to use variable sets as shared defaults across many workspaces while overriding specific values for individual workspaces that require different behaviour (e.g., all workspaces default to `"default"` but `production` explicitly uses `"production"`). No error is raised — the workspace-level value silently wins.

---

### Question 13 — TF_LOG_CORE and TF_LOG Interaction

**Difficulty**: Hard
**Answer Type**: many
**Topic**: How `TF_LOG_CORE` overrides `TF_LOG` and the resulting log output per component

**Question**:
An engineer configures these environment variables before running `terraform apply`:

```bash
export TF_LOG=DEBUG
export TF_LOG_CORE=OFF
```

Which TWO statements correctly describe the resulting logging behaviour? (Select two.)

- A) No logs are produced at all — `TF_LOG_CORE=OFF` disables all Terraform logging globally
- B) Provider plugin logs are produced at `DEBUG` level
- C) Terraform core logs are suppressed
- D) `TF_LOG=DEBUG` overrides `TF_LOG_CORE=OFF` for core components because it was set earlier

**Answer**: B, C

**Explanation**:
`TF_LOG_CORE` and `TF_LOG_PROVIDER` take component-level precedence over the global `TF_LOG` setting. `TF_LOG_CORE=OFF` disables Terraform core logging regardless of the `TF_LOG=DEBUG` setting — they are not additive, and `TF_LOG_CORE` wins for core components. Since `TF_LOG_PROVIDER` is not set, provider plugin logging falls back to the global `TF_LOG=DEBUG` value. Result: (B) provider logs appear at DEBUG level, and (C) core logs are suppressed. This granular control is useful for debugging provider API interactions without the noise of Terraform core debug output.

---
