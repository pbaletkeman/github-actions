# Terraform Associate (004) — Question Bank Iter 1 Batch 8

**Iteration**: 1
**Iteration Style**: Foundational recall — define terms, name commands, identify blocks
**Batch**: 8
**Objective**: 7 — Maintaining Infra + 8 — HCP Terraform
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 2 Easy / 9 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — `import` Block vs CLI Import

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Which import method is preferred in Terraform 1.5+ and why

**Question**:
What is the primary advantage of using an `import` block (Terraform 1.5+) over the legacy `terraform import` CLI command?

- A) The `import` block is faster because it uses parallel imports; the CLI command is sequential
- B) The `import` block can be previewed with `terraform plan` before changes are applied, and can optionally generate HCL configuration automatically
- C) The `import` block supports all resource types; the CLI command only supports AWS resources
- D) The `import` block requires no existing resource block in configuration; the CLI command requires one

**Answer**: B

**Explanation**:
The `import` block (introduced in Terraform 1.5) is the preferred method because it is declarative and integrates into the standard plan/apply workflow — you can run `terraform plan` to preview what will be imported before committing. It also supports `terraform plan -generate-config-out=file.tf` to automatically generate the HCL resource configuration for the imported resource. The legacy `terraform import` CLI command imperatively modifies state immediately with no plan preview and does not generate configuration.

---

### Question 2 — `import` Block Syntax

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Required arguments in an `import` block

**Question**:
Which two arguments are required in an `import` block?

- A) `source` and `destination`
- B) `resource` and `cloud_id`
- C) `to` and `id`
- D) `address` and `provider_id`

**Answer**: C

**Explanation**:
An `import` block requires exactly two arguments: `to` specifies the Terraform resource address that will manage the resource (e.g., `aws_instance.web`), and `id` specifies the cloud provider's identifier for the existing resource (e.g., `"i-0abcd1234ef567890"`). The syntax is: `import { to = resource_type.name  id = "provider-id" }`.

---

### Question 3 — CLI Import Pre-Requisite

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What must exist in configuration before running `terraform import` CLI command

**Question**:
What must exist in the Terraform configuration before running `terraform import aws_s3_bucket.assets my-bucket-name`?

- A) Nothing — `terraform import` creates both the state entry and the HCL resource block automatically
- B) An `import` block referencing the same resource address
- C) A `resource "aws_s3_bucket" "assets" {}` block must already exist in the configuration files
- D) A `data "aws_s3_bucket" "assets" {}` block must exist to look up the bucket first

**Answer**: C

**Explanation**:
The legacy `terraform import` CLI command only writes the resource to state — it does not generate HCL. The corresponding resource block (e.g., `resource "aws_s3_bucket" "assets" {}`) must already exist in the configuration files before running the command. After importing, you use `terraform state show aws_s3_bucket.assets` to view all attributes and then manually update the resource block to match the actual resource configuration.

---

### Question 4 — `terraform plan -generate-config-out`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Purpose of the `-generate-config-out` flag

**Question**:
What does `terraform plan -generate-config-out=generated.tf` do?

- A) Saves the execution plan to a file named `generated.tf` that can be passed to `terraform apply`
- B) Generates HCL resource configuration for resources referenced in `import` blocks and writes it to `generated.tf`
- C) Exports all existing resources in state as HCL configuration into `generated.tf`
- D) Generates a Terraform provider configuration block and appends it to `generated.tf`

**Answer**: B

**Explanation**:
`terraform plan -generate-config-out=generated.tf` is used in conjunction with `import` blocks. When Terraform encounters an `import` block for a resource that has no existing HCL configuration, it generates the resource block based on the live cloud resource's attributes and writes it to the specified output file. You then review, adjust, and commit the generated file before running `terraform apply` to complete the import.

---

### Question 5 — `TF_LOG` Verbosity Order

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Order of `TF_LOG` levels from most to least verbose

**Question**:
Which ordering correctly lists Terraform's `TF_LOG` levels from **most verbose** to **least verbose**?

- A) `ERROR > WARN > INFO > DEBUG > TRACE > OFF`
- B) `TRACE > DEBUG > INFO > WARN > ERROR > OFF`
- C) `DEBUG > TRACE > INFO > WARN > ERROR > OFF`
- D) `INFO > DEBUG > TRACE > WARN > ERROR > OFF`

**Answer**: B

**Explanation**:
Terraform's logging levels in order from most to least verbose are: `TRACE` (all API calls and responses), `DEBUG` (detailed debugging), `INFO` (general operational messages), `WARN` (warning conditions), `ERROR` (error conditions only), and `OFF` (logging disabled). Setting `TF_LOG=TRACE` produces the maximum amount of output and is used for deep debugging of provider and core behaviour.

---

### Question 6 — `TF_LOG_PATH` and Separate Core/Provider Logging

**Difficulty**: Medium
**Answer Type**: many
**Topic**: `TF_LOG_PATH`, `TF_LOG_CORE`, and `TF_LOG_PROVIDER` environment variables

**Question**:
Which TWO statements correctly describe Terraform's logging environment variables? (Select two.)

- A) `TF_LOG_PATH=/tmp/tf.log` causes Terraform to write log output to that file instead of stderr
- B) `TF_LOG_CORE=DEBUG` and `TF_LOG_PROVIDER=TRACE` allow setting different log levels for Terraform core and provider plugins independently
- C) `TF_LOG_PATH` must be set to an absolute path; relative paths are not supported
- D) Setting `TF_LOG_PROVIDER=TRACE` also automatically sets `TF_LOG_CORE=TRACE`

**Answer**: A, B

**Explanation**:
`TF_LOG_PATH` redirects log output from stderr to a specified file path, making it easy to capture logs for review. `TF_LOG_CORE` and `TF_LOG_PROVIDER` allow independent log level control for Terraform's core engine and provider plugins respectively — useful when you need detailed provider API traces without the noise of core debug messages. Options C and D describe nonexistent restrictions; the variables are independent of each other.

---

### Question 7 — HCP Terraform `cloud` Block

**Difficulty**: Medium
**Answer Type**: one
**Topic**: The `cloud` block and when it was introduced

**Question**:
What is the preferred way to connect a Terraform configuration to HCP Terraform (Terraform 1.1+), and which two arguments does it require?

- A) `backend "remote"` block with `hostname` and `token` arguments
- B) `cloud` block with `organization` and `workspaces` arguments
- C) `cloud` block with `hostname` and `workspace_id` arguments
- D) `provider "tfe"` block with `organization` and `token` arguments

**Answer**: B

**Explanation**:
Starting with Terraform 1.1, the `cloud` block inside the `terraform {}` block is the preferred way to connect to HCP Terraform. It requires `organization` (the HCP Terraform organisation name) and `workspaces` (specifying either a workspace `name` or `tags` to select workspaces dynamically). The legacy `backend "remote"` block remains valid but is no longer the recommended approach.

---

### Question 8 — `terraform login` Token Storage

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Where `terraform login` stores the API token

**Question**:
Where does `terraform login` store the HCP Terraform API token after a successful browser-based authentication?

- A) `terraform.tfvars` in the current working directory
- B) `.terraform/credentials.json` in the current working directory
- C) `~/.terraform.d/credentials.tfrc.json` in the user's home directory
- D) The token is never stored on disk — it is only held in memory for the current session

**Answer**: C

**Explanation**:
`terraform login` opens a browser to authenticate with HCP Terraform and stores the resulting API token in `~/.terraform.d/credentials.tfrc.json` in the user's home directory. This file persists across sessions and is used automatically by subsequent Terraform commands. The token can alternatively be provided via the `TF_TOKEN_app_terraform_io` environment variable, which is preferred for CI/CD pipelines to avoid storing credentials on disk.

---

### Question 9 — HCP Terraform Run Types

**Difficulty**: Medium
**Answer Type**: one
**Topic**: The three HCP Terraform run types

**Question**:
Which HCP Terraform run type runs a plan but **never applies** — typically triggered by a pull request to show what would change?

- A) Plan-only run
- B) Speculative plan
- C) Plan-and-apply run
- D) Dry-run

**Answer**: B

**Explanation**:
A **speculative plan** is a read-only plan that never progresses to apply. It is typically triggered automatically when a pull request is opened against a VCS-connected workspace, giving reviewers visibility into what infrastructure changes the PR would cause. It cannot be approved for apply — it is informational only. A **plan-only** run performs a full plan that can later be approved for apply. A **plan-and-apply** run combines both steps.

---

### Question 10 — HCP Terraform Policy Enforcement Levels

**Difficulty**: Medium
**Answer Type**: one
**Topic**: The three Sentinel/OPA policy enforcement levels

**Question**:
Which HCP Terraform policy enforcement level **blocks a run** when a policy fails but **allows an authorised user to override** the failure and proceed?

- A) `advisory`
- B) `soft-mandatory`
- C) `hard-mandatory`
- D) `blocking`

**Answer**: B

**Explanation**:
HCP Terraform supports three policy enforcement levels: `advisory` (failure shows a warning but the run continues), `soft-mandatory` (failure blocks the run, but a user with sufficient permissions can override and allow it to proceed), and `hard-mandatory` (failure blocks the run with no override possible — the policy must pass for the run to continue). `blocking` is not a valid enforcement level.

---

### Question 11 — Variable Sets in HCP Terraform

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What variable sets are and how they are used

**Question**:
What is the purpose of a **variable set** in HCP Terraform?

- A) A set of required variables that must be declared in every workspace's `variables.tf`
- B) A reusable collection of Terraform or environment variables that can be assigned to multiple workspaces or an entire organisation
- C) A list of sensitive variable names whose values are automatically redacted from run logs
- D) A JSON file that defines default variable values, similar to `terraform.tfvars`

**Answer**: B

**Explanation**:
Variable sets allow teams to define a collection of variables (such as cloud provider credentials) once and assign them to multiple workspaces or an entire organisation, avoiding duplication. For example, a "AWS Credentials" variable set containing `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` environment variables can be applied to all workspaces that deploy to AWS. Variables in a set can be marked sensitive to hide their values in the UI.

---

### Question 12 — HCP Terraform Workspace Permissions

**Difficulty**: Hard
**Answer Type**: many
**Topic**: The four workspace-level permission tiers in HCP Terraform

**Question**:
Which TWO statements correctly describe HCP Terraform workspace-level permissions? (Select two.)

- A) The **Write** permission allows a user to trigger runs and approve applies, but not to manage workspace settings or team access
- B) The **Plan** permission allows a user to trigger full plan-and-apply runs without any additional approval
- C) The **Read** permission allows a user to view run history, state, and variables but not trigger any runs
- D) The **Admin** permission is required to change workspace variables — the Write permission does not allow variable changes

**Answer**: A, C

**Explanation**:
HCP Terraform's four workspace permission levels are: **Read** (view runs, state, variables — no triggering), **Plan** (trigger speculative plans only), **Write** (trigger and approve runs — the standard developer permission), and **Admin** (manage all workspace settings including team access and variables). Option B is incorrect — **Plan** only allows speculative plans, not full applies. Option D is incorrect — Write permission does allow variable management; Admin is required for workspace settings and team access control.

---

### Question 13 — Dynamic Provider Credentials (OIDC)

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Purpose and benefit of dynamic provider credentials using OIDC in HCP Terraform

**Question**:
What is the primary security benefit of using **dynamic provider credentials** (OIDC) in HCP Terraform workspaces?

- A) OIDC credentials are stored in HCP Terraform's vault and rotated every 24 hours automatically
- B) OIDC eliminates the need to store long-lived static cloud credentials in HCP Terraform — each run receives a short-lived token that expires after the run completes
- C) OIDC allows multiple cloud providers to share a single set of credentials, reducing the number of secrets to manage
- D) OIDC enables HCP Terraform to bypass multi-factor authentication requirements for cloud providers

**Answer**: B

**Explanation**:
Dynamic provider credentials use OpenID Connect (OIDC) to allow HCP Terraform workspaces to authenticate to cloud providers (AWS, Azure, GCP) without storing long-lived static access keys. Instead, each run requests a short-lived identity token from HCP Terraform, which the cloud provider validates against its configured trust relationship with HCP Terraform as the OIDC issuer. The credentials are scoped to a single run and expire automatically, significantly reducing the risk of credential leakage compared to storing static keys.

---
