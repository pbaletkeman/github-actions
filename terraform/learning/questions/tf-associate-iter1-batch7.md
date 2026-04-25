# Terraform Associate (004) — Question Bank Iter 1 Batch 7

**Iteration**: 1
**Iteration Style**: Foundational recall — define terms, name commands, identify blocks
**Batch**: 7
**Objective**: 5 — Modules + 6 — State Backends
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 2 Easy / 9 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — Root Module Definition

**Difficulty**: Easy
**Answer Type**: one
**Topic**: What the root module is in a Terraform configuration

**Question**:
What is the Terraform **root module**?

- A) The first module listed in `modules.json` inside `.terraform/`
- B) The working directory from which you run `terraform apply`, containing the top-level configuration files
- C) A published module on the Terraform Registry that all other modules depend on
- D) The `main.tf` file specifically — variables.tf and outputs.tf belong to child modules

**Answer**: B

**Explanation**:
In Terraform, every directory containing `.tf` files is a module. The **root module** is the working directory from which you execute Terraform commands (`terraform init`, `terraform plan`, `terraform apply`). Modules called from the root are **child modules**. There is no special "root module" published separately — it is simply the local directory you are working in.

---

### Question 2 — Valid Module Source Types

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Which source types are valid for the `source` argument in a module block

**Question**:
Which TWO of the following are valid values for the `source` argument in a `module` block? (Select two.)

- A) `"./modules/networking"` — a relative local path to a subdirectory
- B) `"hashicorp/consul/aws"` — a Terraform Registry module reference
- C) `"provider::aws::vpc"` — a provider-namespaced module reference
- D) `"module.networking"` — a reference to another module in the same configuration

**Answer**: A, B

**Explanation**:
Terraform supports multiple module source types including local paths (starting with `./` or `../`), Terraform Registry references in the format `<NAMESPACE>/<MODULE>/<PROVIDER>`, GitHub URLs, generic Git URLs, HTTP archives, S3 buckets, and GCS buckets. Option C uses a fictional `provider::` syntax that does not exist. Option D is the syntax for *referencing* a module's outputs, not for sourcing a module definition.

---

### Question 3 — Double-Slash `//` in Module Source

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Meaning of `//` in a Git-based module source URL

**Question**:
In the module source `"github.com/org/infra-modules//modules/vpc"`, what does the `//` (double slash) signify?

- A) It is a URL comment indicator and the text after it is ignored
- B) It separates the repository root from a subdirectory path within the repository
- C) It indicates that the module should be downloaded twice for redundancy
- D) It is a typo — only a single slash is valid in module source paths

**Answer**: B

**Explanation**:
In Terraform module sources that point to a Git repository or archive, the `//` (double slash) is a separator that marks the boundary between the **repository root URL** and a **subdirectory path** within that repository. Everything before `//` identifies the repository; everything after identifies the specific folder to use as the module root. This convention is intentional and documented — it is not a typo.

---

### Question 4 — `version` Argument Restriction

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Which module source types support the `version` argument

**Question**:
For which module source type is the `version` argument valid?

- A) Local paths only (`"./modules/vpc"`)
- B) Git URLs only (`"git::https://..."`)
- C) Terraform Registry and private registry sources only
- D) All source types support the `version` argument

**Answer**: C

**Explanation**:
The `version` argument in a `module` block is only supported for modules sourced from the **Terraform Registry** or a **private registry**. For Git-based sources, you pin a version using the `?ref=` query parameter (e.g., `git::https://github.com/org/repo.git?ref=v1.2.0`). For local paths there is no versioning mechanism. Specifying `version` with a local or Git source causes an error.

---

### Question 5 — Module Cache Location

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Where `terraform init` caches downloaded module source code

**Question**:
Where does `terraform init` cache downloaded child module source code?

- A) `~/.terraform/modules/` in the user's home directory
- B) `terraform.tfstate` alongside the state data
- C) `.terraform/modules/` in the current working directory
- D) `/tmp/terraform-modules/` in a system temporary directory

**Answer**: C

**Explanation**:
`terraform init` downloads child module source code into the `.terraform/modules/` directory within the current working directory. This cache directory also contains a `modules.json` index file. You should re-run `terraform init` any time you add, remove, or change module sources in your configuration, so that the cache is updated.

---

### Question 6 — Variable Inheritance Between Modules

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Whether child modules inherit variables from the root module automatically

**Question**:
A root module declares `variable "region" { default = "us-east-1" }`. A child module also needs a `region` value. Which statement is correct?

- A) The child module automatically inherits `var.region` from the root module without any additional configuration
- B) The child module inherits the variable only if it is declared in both `variables.tf` files
- C) Variables are never automatically inherited — the root module must explicitly pass the value as an input argument in the `module` block
- D) Variables are inherited only for built-in types (string, number, bool); complex types must be passed explicitly

**Answer**: C

**Explanation**:
Terraform modules have **no implicit variable inheritance**. Even if the root module declares `variable "region"`, a child module cannot access `var.region` unless the root module explicitly passes it as an input argument in the `module` block (e.g., `region = var.region`). The child module must also have its own `variable "region"` block to declare the input. This explicit-passing requirement applies to all variable types.

---

### Question 7 — Standard Module File Structure

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Files that belong in a standard Terraform module structure

**Question**:
Which TWO files are part of the standard, recommended Terraform module file structure? (Select two.)

- A) `versions.tf` — declares required Terraform and provider versions
- B) `state.tf` — declares the backend configuration for the module's state
- C) `providers.tf` — required to re-declare the provider in every child module
- D) `outputs.tf` — declares the output values exposed by the module

**Answer**: A, D

**Explanation**:
The standard module structure includes: `main.tf` (core resources), `variables.tf` (input variable declarations), `outputs.tf` (output value declarations), `versions.tf` (required providers and version constraints), and `README.md`. Option B is incorrect — child modules do not have their own backend configuration; only the root module configures the backend. Option C is incorrect — child modules do not need to redeclare providers; they inherit the provider configuration from the root module unless an alias or explicit `providers` argument is used.

---

### Question 8 — Backend Block Location in HCL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Where the `backend` block is declared in Terraform configuration

**Question**:
Where must the `backend` block be declared in a Terraform configuration?

- A) In a dedicated `backend.tf` file at the root of the module
- B) Inside the `terraform {}` block, which is typically placed in `versions.tf` or `main.tf`
- C) Inside a `provider` block, alongside authentication credentials
- D) In `terraform.tfvars` as a backend-specific variable assignment

**Answer**: B

**Explanation**:
The `backend` block must be nested inside the `terraform {}` configuration block. While it can technically appear in any `.tf` file, it is conventionally placed in `versions.tf` or `main.tf`. The `backend` block cannot be placed inside a `provider` block or in `.tfvars` files — those files only accept variable value assignments.

---

### Question 9 — `terraform.tfstate.backup` Purpose

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What `terraform.tfstate.backup` contains and its limitations

**Question**:
What does `terraform.tfstate.backup` contain when using the local backend?

- A) A complete versioned history of all previous state files since the project was created
- B) The state from the most recent apply — a single snapshot of the previous state, not a full history
- C) A backup of the state from 24 hours ago, automatically updated on a daily schedule
- D) An encrypted copy of the current state file for disaster recovery

**Answer**: B

**Explanation**:
`terraform.tfstate.backup` stores only the **single most recent previous state** — the state as it was just before the last apply. It is overwritten on each apply and does not provide versioned history. To maintain a full history of state changes, you need a remote backend with versioning enabled (such as S3 with object versioning, or HCP Terraform which provides state history natively).

---

### Question 10 — `terraform init -migrate-state` vs `-reconfigure`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Difference between `terraform init -migrate-state` and `terraform init -reconfigure`

**Question**:
What is the difference between `terraform init -migrate-state` and `terraform init -reconfigure` when changing backends?

- A) `-migrate-state` only works with S3 backends; `-reconfigure` works with all backend types
- B) `-migrate-state` copies existing state to the new backend; `-reconfigure` reinitialises the backend without migrating existing state
- C) `-reconfigure` is for switching from local to remote backends; `-migrate-state` is for switching between two remote backends
- D) Both flags are identical — they are aliases for the same operation

**Answer**: B

**Explanation**:
When you change the `backend` block configuration and re-run `terraform init`, Terraform detects the change and requires you to choose a migration strategy. `terraform init -migrate-state` prompts Terraform to copy any existing state from the old backend into the new one. `terraform init -reconfigure` reinitialises the backend silently, discarding the migration prompt and leaving the old state in place — useful when the old state is no longer relevant or has already been handled manually.

---

### Question 11 — S3 Backend State Locking

**Difficulty**: Medium
**Answer Type**: one
**Topic**: How the S3 backend implements state locking

**Question**:
When using the S3 backend, which AWS service provides state locking, and what attribute name is required?

- A) S3 object tagging with a tag key of `TerraformLock`
- B) AWS Systems Manager Parameter Store with a parameter named `terraform-lock`
- C) DynamoDB with a table attribute named `LockID`
- D) CloudWatch Events with a rule named after the state file key

**Answer**: C

**Explanation**:
The S3 backend uses **DynamoDB** for state locking. You must create a DynamoDB table with a hash key (partition key) attribute named exactly `LockID` of type String. The table name is referenced in the backend block's `dynamodb_table` argument. When Terraform acquires a lock, it writes an item to this table; when the operation completes, the item is deleted. If the lock is not released (e.g., due to a crash), use `terraform force-unlock` with the lock ID.

---

### Question 12 — `terraform force-unlock` Usage

**Difficulty**: Hard
**Answer Type**: one
**Topic**: When and how to use `terraform force-unlock`

**Question**:
When should `terraform force-unlock <LOCK_ID>` be used, and what is the risk?

- A) It should be used routinely after every `terraform apply` to clean up lock artifacts; it carries no risk
- B) It should be used only when you are certain no other `terraform apply` or `plan` is actively running — using it while another operation holds the lock can corrupt state
- C) It should be used whenever `terraform plan` runs slowly, as it removes lock contention caused by stale read locks
- D) It can only be run by the user who created the lock; other users receive a permission error

**Answer**: B

**Explanation**:
`terraform force-unlock` manually releases a state lock that was not automatically released — typically because a process crashed or was interrupted while holding the lock. The lock ID to pass is shown in the error message Terraform displays when a lock is encountered. The **critical risk** is using it while another legitimate operation is actually running: if two applies run simultaneously against the same state, the state file can become corrupted. Only use `force-unlock` after confirming that no other operation is in progress.

---

### Question 13 — `plan -refresh-only` vs `apply -refresh-only`

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Distinguishing `terraform plan -refresh-only` from `terraform apply -refresh-only`

**Question**:
Which TWO statements correctly describe the behaviour of the `-refresh-only` flag? (Select two.)

- A) `terraform plan -refresh-only` shows what has drifted in the cloud compared to state, but proposes no infrastructure changes
- B) `terraform apply -refresh-only` modifies cloud resources to match the Terraform configuration, resolving drift by re-applying desired state
- C) `terraform apply -refresh-only` updates the state file to reflect the current actual state of cloud resources, without creating, modifying, or destroying any infrastructure
- D) `terraform plan -refresh-only` is equivalent to `terraform refresh`, which is a deprecated command that both refreshes state and applies the plan in one step

**Answer**: A, C

**Explanation**:
`terraform plan -refresh-only` reads the current state of cloud resources and shows what differs from the recorded state, but does not propose any resource creates, updates, or deletes — it is a read-only drift report. `terraform apply -refresh-only` performs the same drift detection and then **updates only the state file** to match the actual cloud resources; it does not make any changes to the infrastructure itself. Option B is incorrect — `-refresh-only` never modifies cloud resources. Option D is incorrect — `terraform refresh` is the deprecated equivalent of `apply -refresh-only`, not `plan -refresh-only`.

---
