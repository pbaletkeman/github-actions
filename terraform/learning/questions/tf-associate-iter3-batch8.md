# Terraform Associate (004) — Question Bank Iter 3 Batch 8

**Iteration**: 3
**Iteration Style**: HCL interpretation — read a snippet and identify output/effect
**Batch**: 8
**Objective**: 7 — Maintaining Infra + 8 — HCP Terraform
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 3 Easy / 8 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — `import` Block `to` Argument

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Reading an `import` block to identify what the `to` argument specifies

**Question**:
Read this `import` block:

```hcl
import {
  to = aws_s3_bucket.media_assets
  id = "company-media-assets-2024"
}
```

What does the `to` argument specify?

- A) The S3 bucket name in AWS — `to` is an alias for the `id` argument that accepts human-readable names
- B) The Terraform resource address (type and label) that will manage the existing cloud resource once the import completes
- C) The destination file path where Terraform writes the generated HCL configuration for the imported resource
- D) The key path within the state file where the imported resource will be stored

**Answer**: B

**Explanation**:
In an `import` block, `to` specifies the **Terraform resource address** — the resource type and label that will own the imported resource in state. Here, `aws_s3_bucket.media_assets` means the existing S3 bucket will be managed by a `resource "aws_s3_bucket" "media_assets"` block. The `id` argument separately provides the cloud provider's identifier for the existing resource (`"company-media-assets-2024"` — the bucket name, which is the AWS S3 resource ID format). These two arguments together tell Terraform which config block maps to which real cloud resource.

---

### Question 2 — `terraform show plan.tfplan`

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Reading a two-command sequence to identify what `terraform show plan.tfplan` renders

**Question**:
Read this command sequence:

```bash
terraform plan -out=plan.tfplan
terraform show plan.tfplan
```

What does the second command render?

- A) A JSON document of the entire current state file, filtered to only the resources affected by the plan
- B) A summary of the most recent `terraform apply` output, sourced from the local operation log
- C) A human-readable view of the saved execution plan in `plan.tfplan`, showing the proposed creates, updates, and destroys
- D) An error — `terraform show` can only read the current state file and does not accept a plan file as an argument

**Answer**: C

**Explanation**:
`terraform plan -out=plan.tfplan` saves the execution plan to a binary file. `terraform show plan.tfplan` then reads that file and renders it as human-readable text showing the proposed infrastructure changes. This workflow is used in automated pipelines to separate the plan and apply steps: a human (or approval gate) reviews the output of `terraform show plan.tfplan` before the saved plan file is passed to `terraform apply plan.tfplan`. Use `terraform show -json plan.tfplan` for machine-readable output suitable for tools like Open Policy Agent.

---

### Question 3 — `TF_LOG=WARN` Message Filtering

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Reading a shell snippet with `TF_LOG=WARN` to identify which log messages appear

**Question**:
Read this shell snippet:

```bash
export TF_LOG=WARN
terraform apply
```

Which log messages does setting `TF_LOG=WARN` produce during the apply?

- A) WARN-level messages only — messages at all other levels are suppressed
- B) WARN and ERROR messages — the log level acts as a minimum severity threshold
- C) INFO, WARN, and ERROR messages — WARN is a mid-level filter that includes less-verbose levels above it
- D) All log levels — WARN enables logging but does not filter by severity

**Answer**: B

**Explanation**:
`TF_LOG` sets a **minimum severity threshold**. The levels from least to most severe are: `TRACE < DEBUG < INFO < WARN < ERROR`. Setting `TF_LOG=WARN` produces messages at WARN severity and above — meaning both WARN and ERROR messages appear. INFO, DEBUG, and TRACE messages are suppressed. This is useful when you want to see only warnings and errors without the noise of detailed operational or debugging output. To silence all logging, set `TF_LOG=OFF` or unset the variable entirely.

---

### Question 4 — `terraform plan -generate-config-out` Produced File

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reading a generated resource block to identify which workflow step produced it

**Question**:
Read this file:

```hcl
# generated.tf
# __generated__ by Terraform
# Please review these resources and move them into your main configuration files.

resource "aws_s3_bucket" "legacy_assets" {
  bucket              = "my-existing-bucket-2024"
  bucket_prefix       = null
  force_destroy       = null
  object_lock_enabled = false
  tags                = {}
  tags_all            = {}
}
```

Which command produced this file, and under what conditions?

- A) `terraform apply` — Terraform generates a record of newly created resources in a companion file after each apply
- B) `terraform plan -generate-config-out=generated.tf` — run after adding an `import` block for a resource that had no existing HCL configuration
- C) `terraform state show aws_s3_bucket.legacy_assets > generated.tf` — the state show output was redirected into a file
- D) `terraform init` — provider initialisation generates scaffold configuration files for each resource type the provider supports

**Answer**: B

**Explanation**:
The `# __generated__ by Terraform` comment and the presence of explicitly null attributes (like `bucket_prefix = null` and `force_destroy = null`) are hallmarks of Terraform's automatic config generation. This file is produced by `terraform plan -generate-config-out=generated.tf` when an `import` block references a resource that has no existing HCL configuration. Terraform calls the provider's Read function to retrieve all current attributes and writes a complete `resource` block. The team must then review, trim unnecessary nulls, and move the block into their main configuration before running `terraform apply` to complete the import.

---

### Question 5 — `cloud` Block with `tags` Workspace Selector

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reading a `cloud` block with a `tags` array to identify which HCP Terraform workspaces it selects

**Question**:
Read this `cloud` block:

```hcl
terraform {
  cloud {
    organization = "acme-corp"

    workspaces {
      tags = ["component=networking", "env=production"]
    }
  }
}
```

Which workspaces in the `acme-corp` organisation does this configuration connect to?

- A) All workspaces in the organisation — tags are stored as metadata but do not filter which workspace is selected
- B) Any workspace that has AT LEAST ONE of the listed tags: either `component=networking` OR `env=production`
- C) Any workspace that has ALL of the listed tags: both `component=networking` AND `env=production`
- D) Only the workspace whose name matches the first tag value: `"component=networking"`

**Answer**: C

**Explanation**:
When `workspaces { tags = [...] }` is used in the `cloud` block, Terraform connects to workspaces that possess **all** of the specified tags — it is an AND condition, not OR. This configuration connects to any workspace in `acme-corp` tagged with both `component=networking` and `env=production`. A workspace tagged only with `env=production` (but not `component=networking`) would not be selected. This selector is used for configurations that should run against a dynamic set of identically-tagged workspaces rather than a single named workspace.

---

### Question 6 — `terraform_remote_state` Output Reference Value

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reading a `terraform_remote_state` data source and root output to identify the evaluated value

**Question**:
Read this configuration:

```hcl
data "terraform_remote_state" "networking" {
  backend = "remote"
  config = {
    organization = "my-org"
    workspaces = {
      name = "networking-production"
    }
  }
}

output "app_subnet" {
  value = data.terraform_remote_state.networking.outputs.private_subnet_id
}
```

The `networking-production` workspace has this output declared and applied:

```hcl
output "private_subnet_id" {
  value = "subnet-0abc123def456"
}
```

What is the value of `output.app_subnet`?

- A) The entire `outputs` map from the remote state — the `.private_subnet_id` suffix selects a key within the map but is evaluated at apply time only
- B) `"subnet-0abc123def456"` — the string value of the `private_subnet_id` output from the remote workspace
- C) The ARN of the subnet — `terraform_remote_state` resolves subnet IDs to ARNs automatically
- D) `null` — cross-workspace remote state output references always evaluate to null during the first plan

**Answer**: B

**Explanation**:
`data.terraform_remote_state.<name>.outputs.<key>` evaluates to the **value stored in the remote workspace's state for the named output**. Here, the `networking-production` workspace exports `private_subnet_id = "subnet-0abc123def456"`, so `data.terraform_remote_state.networking.outputs.private_subnet_id` evaluates to the string `"subnet-0abc123def456"`. This value is then used as `output.app_subnet`. The `terraform_remote_state` data source reads outputs directly from the remote workspace's state file — no resolution or transformation occurs.

---

### Question 7 — `TF_LOG_PROVIDER=TRACE` Without `TF_LOG`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reading a shell snippet with only `TF_LOG_PROVIDER` set to identify the resulting log output

**Question**:
Read this shell snippet:

```bash
export TF_LOG_PROVIDER=TRACE
# TF_LOG is NOT set
terraform apply
```

What logging does Terraform produce?

- A) No logging — `TF_LOG` must be set to any level for any logging to occur; `TF_LOG_PROVIDER` has no effect without it
- B) TRACE-level logs for provider plugins only; Terraform core produces no logs
- C) TRACE-level logs for all components — setting `TF_LOG_PROVIDER=TRACE` implicitly enables `TF_LOG=TRACE` for core as well
- D) An error is raised — `TF_LOG_PROVIDER` requires `TF_LOG` to be configured first to establish a baseline level

**Answer**: B

**Explanation**:
`TF_LOG_PROVIDER` and `TF_LOG_CORE` are independent, component-scoped overrides. When only `TF_LOG_PROVIDER=TRACE` is set (with no global `TF_LOG`), Terraform produces TRACE-level logs for **provider plugin** activity only — including all API calls made to the cloud provider. The Terraform core engine (graph walking, state management, plan generation) produces no logs because neither `TF_LOG` nor `TF_LOG_CORE` is set. This is the intended use case: debugging a misbehaving provider without the noise of core infrastructure logging.

---

### Question 8 — Sentinel Policy Snippet Interpretation

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Reading a Sentinel policy to identify TWO correct statements about what it enforces and its override behaviour

**Question**:
Read this Sentinel policy, assigned at the `soft-mandatory` enforcement level:

```python
import "tfplan/v2" as tfplan

all_resources = filter tfplan.resource_changes as _, rc {
  rc.change.actions is not ["no-op"]
}

untagged = filter all_resources as _, r {
  not (r.change.after.tags else null) != null
}

main = rule {
  length(untagged) == 0
}
```

Which TWO statements correctly describe this policy? (Select two.)

- A) The `main` rule evaluates to `true` (passes) when every resource being modified has a non-null `tags` attribute — that is, when `untagged` is empty
- B) If a resource has `tags` explicitly set to an empty map `{}`, it is added to the `untagged` collection and causes a policy failure
- C) A policy failure at `soft-mandatory` can be overridden by a user who has the "Manage Policy Overrides" permission, allowing the run to proceed
- D) If the `main` rule returns `false`, the run is blocked and cannot proceed under any circumstances regardless of user permissions

**Answer**: A, C

**Explanation**:
The `untagged` filter collects resources where `r.change.after.tags` is `null` or absent — `(r.change.after.tags else null) != null` evaluates to `false` for null/missing tags, so `not false` adds them to the collection. The `main` rule passes only when `untagged` is empty, meaning all modified resources carry a non-null `tags` attribute (A is correct). Option B is incorrect: a resource with `tags = {}` has a non-null (empty) map — it passes the filter and is NOT added to `untagged`. Option C is correct: `soft-mandatory` failures can be overridden by a user with the "Manage Policy Overrides" permission. Option D describes `hard-mandatory` behaviour, not `soft-mandatory`.

---

### Question 9 — `workspaces.name` vs `workspaces.tags` in `cloud` Block

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reading two `cloud` block variants to identify the key behavioral difference between name and tags selectors

**Question**:
Read both configurations:

```hcl
# Config A
terraform {
  cloud {
    organization = "my-org"
    workspaces {
      name = "production"
    }
  }
}
```

```hcl
# Config B
terraform {
  cloud {
    organization = "my-org"
    workspaces {
      tags = ["env=production"]
    }
  }
}
```

What is the key behavioral difference between Config A and Config B?

- A) Config A connects this configuration directory to exactly one workspace named `"production"`; Config B allows the configuration to be used with any workspace in `my-org` tagged with `env=production`
- B) Config A creates the `production` workspace automatically if it does not exist; Config B only connects to workspaces that already exist and carry the specified tag
- C) Both configurations connect to the same workspace — `name = "production"` and `tags = ["env=production"]` are equivalent expressions when the workspace name matches the tag value
- D) Config B connects to the HCP Terraform default workspace; `tags` is a UI display filter and not a workspace selector

**Answer**: A

**Explanation**:
`workspaces { name = "..." }` pins the configuration to a **single, specific workspace** by name. `workspaces { tags = [...] }` enables the configuration to work with **any workspace** in the organisation carrying the specified tag(s), turning the working directory into a workspace-agnostic configuration that selects its target at `terraform init` time. The `tags` selector is used when teams want the same root module to deploy to multiple workspaces (e.g., one per region) without hardcoding a workspace name. Options B, C, and D mischaracterise both selectors.

---

### Question 10 — `import` Block vs CLI Import Differences

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Reading an `import` block and a CLI import command side-by-side to identify TWO structural differences

**Question**:
Read both import approaches for the same resource:

```hcl
# Method A — import block (Terraform 1.5+)
import {
  to = aws_instance.app
  id = "i-0abc1234567890"
}
```

```bash
# Method B — CLI import (legacy)
terraform import aws_instance.app i-0abc1234567890
```

Which TWO statements correctly describe a difference between Method A and Method B? (Select two.)

- A) Method A can be previewed with `terraform plan` before any state changes are made; Method B immediately writes to state when the command runs, with no plan preview
- B) Method A can optionally generate the HCL resource configuration using `terraform plan -generate-config-out=file.tf`; Method B only writes to state and never produces HCL configuration
- C) Method A requires a pre-existing `resource "aws_instance" "app"` block just like Method B — both methods require the resource block to already exist
- D) Method A and Method B produce identical results in every way — the `import` block is simply a declarative wrapper around the same imperative operation

**Answer**: A, B

**Explanation**:
The two key structural advantages of the `import` block (Method A) are: (A) it integrates into the standard plan/apply workflow, allowing `terraform plan` to preview the import before committing any state changes — Method B immediately and irrevocably modifies state; (B) it can pair with `terraform plan -generate-config-out=file.tf` to automatically generate the HCL resource block, whereas Method B only adds the resource to state and the practitioner must manually write and tune the configuration. Option C is incorrect — Method A can be used with `-generate-config-out` when no resource block exists yet. Option D is incorrect because of the plan preview and config-generation differences.

---

### Question 11 — `TF_TOKEN_app_terraform_io` vs `terraform login`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reading a CI/CD env-var authentication snippet to identify what it achieves and why it is preferred over `terraform login`

**Question**:
Read this CI/CD pipeline configuration snippet:

```bash
export TF_TOKEN_app_terraform_io="<hcp-terraform-api-token>"
terraform init
terraform plan
```

What does setting `TF_TOKEN_app_terraform_io` achieve, and why is it preferred over `terraform login` in this context?

- A) It is functionally identical to `terraform login` — both approaches store the token in `~/.terraform.d/credentials.tfrc.json` for use by subsequent Terraform commands
- B) It provides authentication to HCP Terraform via an environment variable, avoiding the need to store a token file on disk — the recommended approach for CI/CD pipelines where no persistent home directory exists
- C) It sets a short-lived session token scoped to the current shell; `terraform login` generates a longer-lived token with broader permissions
- D) The variable is consumed only by `terraform init`; subsequent `plan` and `apply` commands require re-exporting the variable in separate shell steps

**Answer**: B

**Explanation**:
`TF_TOKEN_<hostname>` (with dots in the hostname replaced by underscores) is the environment variable equivalent of the credentials stored by `terraform login`. Setting it provides HCP Terraform authentication without writing any file to disk. This is the **recommended pattern for CI/CD pipelines** and automation contexts where `terraform login` is impractical (it requires interactive browser-based authentication and writes to `~/.terraform.d/credentials.tfrc.json`). The environment variable is read by all Terraform commands — `init`, `plan`, `apply` — for the duration of the process without needing to be re-exported.

---

### Question 12 — `hard-mandatory` Sentinel Policy Override Attempt by Owner

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Reading a `hard-mandatory` Sentinel policy and identifying what happens when an Owner tries to override the failure

**Question**:
Read this Sentinel policy metadata and code fragment:

```
# Policy: require-s3-encryption.sentinel
# Enforcement level: hard-mandatory

main = rule {
  length(unencrypted_buckets) == 0
}
```

An engineer creates an `aws_s3_bucket` without encryption. The `main` rule evaluates to `false`. An organisation Owner attempts to use the HCP Terraform UI override button to allow the run to proceed. What happens?

- A) The override succeeds — Owners have unrestricted access and can override any policy failure regardless of enforcement level
- B) The override button is visible but is disabled with a tooltip explaining that `hard-mandatory` policies cannot be overridden via the UI
- C) There is no override option presented in the HCP Terraform UI — `hard-mandatory` policies display the failure with no mechanism to proceed
- D) The override succeeds only if the Owner is also a member of the team that manages the failing policy set

**Answer**: C

**Explanation**:
`hard-mandatory` is the strictest Sentinel enforcement level. When a `hard-mandatory` policy fails, the run is permanently blocked and **no override option is presented in the HCP Terraform UI** — not for Owners, not for any role. This is by design: `hard-mandatory` is used for compliance requirements that must never be bypassed (e.g., encryption, regulatory controls). The only way to unblock the run is to fix the Terraform configuration so the policy passes, or to change the policy's enforcement level (which requires policy admin access). This distinguishes it from `soft-mandatory`, where an authorised user can override the failure.

---

### Question 13 — Three Logging Variables in Combination

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Reading a shell snippet with `TF_LOG`, `TF_LOG_CORE`, `TF_LOG_PROVIDER`, and `TF_LOG_PATH` all set to identify TWO correct outcomes

**Question**:
Read this shell snippet:

```bash
export TF_LOG=TRACE
export TF_LOG_CORE=ERROR
export TF_LOG_PROVIDER=DEBUG
export TF_LOG_PATH=/var/log/terraform.log
terraform apply
```

Which TWO statements correctly describe the resulting logging behaviour? (Select two.)

- A) All log output is written to `/var/log/terraform.log` rather than stderr
- B) Terraform core logs appear at TRACE level — the global `TF_LOG=TRACE` takes precedence over `TF_LOG_CORE=ERROR` for core components
- C) Terraform core logs appear at ERROR level only — `TF_LOG_CORE=ERROR` overrides the global `TF_LOG=TRACE` for core components
- D) Provider plugin logs appear at TRACE level — the global `TF_LOG=TRACE` overrides `TF_LOG_PROVIDER=DEBUG` for provider components

**Answer**: A, C

**Explanation**:
Component-scoped variables (`TF_LOG_CORE`, `TF_LOG_PROVIDER`) take **precedence over the global `TF_LOG`** for their respective components. `TF_LOG_CORE=ERROR` overrides `TF_LOG=TRACE` for Terraform core — only ERROR-level core messages appear (C is correct). `TF_LOG_PROVIDER=DEBUG` overrides `TF_LOG=TRACE` for provider plugins — provider logs appear at DEBUG, not TRACE (so D is incorrect). `TF_LOG_PATH=/var/log/terraform.log` redirects **all** log output from stderr to the specified file (A is correct), regardless of which levels or components are active.

---
