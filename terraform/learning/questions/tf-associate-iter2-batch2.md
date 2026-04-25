# Terraform Associate (004) — Question Bank Iter 2 Batch 2

**Iteration**: 2
**Iteration Style**: Behavioral prediction — what happens when code runs or a scenario occurs
**Batch**: 2
**Objective**: 2 — Terraform Fundamentals (Providers & State)
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 3 Easy / 8 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — Apply Without Init

**Difficulty**: Easy
**Answer Type**: one
**Topic**: What happens when terraform apply runs before terraform init

**Question**:
A developer clones a Terraform repository and immediately runs `terraform apply` without first running `terraform init`. What happens?

- A) Terraform applies the configuration successfully using the provider versions from the previous engineer's machine
- B) Terraform returns an error because the required provider plugins have not been downloaded yet
- C) Terraform downloads the providers on-the-fly during apply and then proceeds
- D) Terraform applies the configuration but skips any resources that require provider authentication

**Answer**: B

**Explanation**:
`terraform init` must be run before `terraform apply` because it downloads the required provider plugins into `.terraform/providers/` and sets up the dependency lock file. Without initialisation, Terraform does not have the provider binaries needed to communicate with cloud APIs, and it returns an error indicating that the working directory has not been initialised.

---

### Question 2 — Init with `~> 5.0` Constraint

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Which provider version gets installed when `~> 5.0` is specified and v5.31 is available

**Question**:
A `required_providers` block specifies `version = "~> 5.0"` for the AWS provider. The latest available versions are `5.31.0` and `6.0.0`. `terraform init` is run for the first time (no lock file exists). Which version is installed?

- A) `6.0.0` — the absolute latest version available
- B) `5.31.0` — the latest version within the `>= 5.0, < 6.0` range
- C) `5.0.0` — the minimum version satisfying the constraint
- D) The command fails because `~>` requires an exact version number

**Answer**: B

**Explanation**:
The `~>` pessimistic constraint operator with `~> 5.0` allows any version `>= 5.0` and `< 6.0`, which includes minor and patch updates within major version 5. Terraform selects the newest version satisfying the constraint — `5.31.0` in this case. Version `6.0.0` is excluded because it crosses the major version boundary.

---

### Question 3 — Lock File Not Committed

**Difficulty**: Easy
**Answer Type**: one
**Topic**: What happens when .terraform.lock.hcl is not committed to version control

**Question**:
A team does not commit `.terraform.lock.hcl` to their git repository. Engineer A runs `terraform init` on Monday and installs AWS provider `5.28.0`. On Tuesday, AWS provider `5.31.0` is released. Engineer B clones the repo and runs `terraform init`. What happens?

- A) Engineer B gets the same `5.28.0` version because the lock file is shared through Terraform's registry cache
- B) Engineer B gets `5.28.0` because `terraform init` always installs the minimum version satisfying the constraint
- C) Engineer B installs the latest version matching the constraint — potentially `5.31.0` — resulting in a different provider version than Engineer A
- D) `terraform init` fails because there is no lock file to verify against

**Answer**: C

**Explanation**:
The `.terraform.lock.hcl` lock file records the exact provider version and cryptographic hashes installed. Committing it to version control ensures all team members use the same provider version. Without it, each engineer's `terraform init` independently selects the newest version matching the constraint — which may differ as new versions are released. This is why HashiCorp recommends always committing the lock file to VCS.

---

### Question 4 — `terraform init -upgrade` Behaviour

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What terraform init -upgrade does when a newer provider version is available

**Question**:
The lock file records AWS provider `5.10.0`. The `required_providers` constraint is `~> 5.0`. AWS provider `5.31.0` has since been released. `terraform init -upgrade` is run. What happens?

- A) The command fails because the lock file must be deleted manually before upgrading
- B) The command ignores the upgrade request because `5.10.0` still satisfies the constraint
- C) Terraform updates the lock file to `5.31.0` and downloads the new provider binary
- D) Terraform upgrades to the newest available version — including `6.0.0` — ignoring the constraint

**Answer**: C

**Explanation**:
`terraform init -upgrade` instructs Terraform to re-evaluate provider version constraints and install the newest available version within the constraints. Because `5.31.0` satisfies `~> 5.0` (i.e., `>= 5.0, < 6.0`), Terraform downloads `5.31.0` and updates the lock file to record the new exact version and its hashes. Without `-upgrade`, Terraform uses the version already pinned in the lock file.

---

### Question 5 — Provider Alias Resource Assignment

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What happens when a resource omits a provider argument when multiple provider configs exist

**Question**:
Two AWS provider configurations are declared: a default (`region = "us-east-1"`) and an aliased one (`alias = "west", region = "us-west-2"`). An `aws_instance` resource block is written without a `provider` argument. In which region is the instance created?

- A) `us-west-2` — Terraform uses the most recently declared provider
- B) `us-east-1` — Terraform uses the default (unaliased) provider configuration
- C) Terraform returns an error because the `provider` argument is required when multiple configs exist
- D) Terraform creates an instance in both regions because it cannot determine which to use

**Answer**: B

**Explanation**:
When multiple configurations of the same provider exist (default + aliased), resources that do not specify a `provider` argument are automatically assigned to the **default (unaliased)** provider configuration. The aliased provider is only used by resources that explicitly reference it via `provider = aws.west` (or similar). This default assignment behaviour prevents ambiguity without requiring every resource to specify a provider.

---

### Question 6 — `terraform state rm` Effect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What happens to a cloud resource after terraform state rm is run

**Question**:
A team runs `terraform state rm aws_s3_bucket.legacy`. The S3 bucket exists in the cloud. What is the outcome?

- A) The S3 bucket is immediately deleted from AWS
- B) The S3 bucket is deleted from AWS on the next `terraform apply`
- C) The S3 bucket continues to exist in AWS but is no longer tracked or managed by Terraform
- D) Terraform marks the bucket as tainted and schedules it for replacement on the next apply

**Answer**: C

**Explanation**:
`terraform state rm` removes the resource from Terraform's state file but does **not** delete the actual cloud resource. After this command, the S3 bucket continues to exist in AWS, but Terraform no longer knows about it. On the next `terraform plan`, the resource will not appear in the diff because Terraform has no record of it. This command is commonly used when you want to stop managing a resource with Terraform without destroying it.

---

### Question 7 — Concurrent Local State Applies

**Difficulty**: Medium
**Answer Type**: many
**Topic**: What happens when two engineers run terraform apply simultaneously with local state

**Question**:
Two engineers working on the same project both have the local `terraform.tfstate` file on their laptops. They run `terraform apply` at the same time against the same cloud environment. Which TWO outcomes are likely? (Select two.)

- A) Terraform detects the simultaneous apply and queues them sequentially without any data loss
- B) Both applies may succeed independently, but their state files will now differ and be out of sync with each other
- C) One apply may overwrite the other's state file changes, leading to state corruption or lost resource tracking
- D) Local state automatically merges changes from both applies into a unified state file

**Answer**: B, C

**Explanation**:
Local state has no locking mechanism. When two engineers apply simultaneously, each reads the state file, makes changes, and writes it back independently. The second write can overwrite the first, causing one apply's resource changes to be lost from the state file — a condition known as state corruption. The resulting state files will diverge, meaning Terraform will no longer accurately reflect the true state of infrastructure. This is why **remote state with locking** is essential for team environments.

---

### Question 8 — Plan with Sensitive Output

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What happens to a sensitive output value in plan output vs state

**Question**:
An output is declared as:

```hcl
output "db_password" {
  value     = aws_db_instance.main.password
  sensitive = true
}
```

`terraform apply` completes successfully. What is the behaviour?

- A) The password is hidden from the terminal output and is also encrypted in `terraform.tfstate`
- B) The password is displayed in the terminal but marked with a warning; it is encrypted in state
- C) The password is hidden from the terminal output (`(sensitive value)`), but it is stored in plaintext in `terraform.tfstate`
- D) The password is hidden from both the terminal and state, requiring the engineer to retrieve it from the cloud provider

**Answer**: C

**Explanation**:
`sensitive = true` on an output only suppresses the value from being displayed in terminal output — it shows as `(sensitive value)`. However, Terraform state stores **all resource attributes in plaintext**, regardless of the `sensitive` flag. The password value is visible in the raw `terraform.tfstate` JSON. This is why encrypted remote backends and restricted access to state files are critical for production environments handling secrets.

---

### Question 9 — `terraform plan -refresh=false` After Manual Change

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What plan -refresh=false reports when cloud has drifted

**Question**:
An engineer manually changes an EC2 instance's type from `t3.micro` to `t3.large` through the AWS console. The Terraform configuration still declares `t3.micro`. A team member runs `terraform plan -refresh=false`. What does Terraform report?

- A) Terraform detects the drift and proposes reverting the instance type to `t3.micro`
- B) Terraform reports no changes because `-refresh=false` uses cached state, which still records `t3.micro`, and the configuration also declares `t3.micro`
- C) Terraform reports no changes and automatically updates the configuration to match the new instance type
- D) Terraform returns an error because `-refresh=false` cannot be used after manual changes

**Answer**: B

**Explanation**:
`terraform plan -refresh=false` skips the API refresh step and compares the configuration directly against the cached state file. Since the state file still records `instance_type = "t3.micro"` (the last known state from before the manual change) and the configuration also declares `t3.micro`, Terraform sees no difference and reports no changes. The drift is invisible. This illustrates why `-refresh=false` should be used with caution — it can mask real infrastructure drift.

---

### Question 10 — State After `terraform state mv`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What terraform state mv does to the resource in the cloud

**Question**:
An engineer runs `terraform state mv aws_instance.app aws_instance.web`. The EC2 instance is running in AWS. What is the immediate outcome?

- A) The EC2 instance is destroyed and recreated with the name `web`
- B) The state file is updated to reference the instance as `aws_instance.web`; the EC2 instance itself is untouched
- C) The command updates both the state file and the `.tf` configuration file to rename the resource
- D) The EC2 instance's Name tag is updated to `web` in AWS

**Answer**: B

**Explanation**:
`terraform state mv` renames a resource's address within the state file only — it does **not** modify, destroy, or recreate the actual cloud resource. After the command, Terraform treats the running EC2 instance as `aws_instance.web`. The configuration file must be updated separately to use `resource "aws_instance" "web"`. If the configuration is updated to match before running `terraform plan`, the plan shows no changes — no destroy and recreate occurs.

---

### Question 11 — Applying with Multiple Providers and No Authentication

**Difficulty**: Medium
**Answer Type**: many
**Topic**: What happens when a provider config has no credentials at apply time

**Question**:
A configuration declares an AWS provider and a Google provider but no authentication credentials are set in the provider blocks, environment variables, or credential files. `terraform apply` is run. Which TWO outcomes are most likely? (Select two.)

- A) Terraform skips resources belonging to providers with missing credentials and applies only the remaining resources
- B) Terraform begins the apply, but resource operations fail with authentication errors when provider API calls are made
- C) `terraform plan` and `terraform apply` may fail or produce errors during provider initialisation if the providers cannot authenticate
- D) Terraform uses anonymous access where available and creates public-only resources without authentication

**Answer**: B, C

**Explanation**:
Terraform providers require valid credentials to make API calls. Without credentials, the provider plugin cannot authenticate to the cloud API, and resource operations — or even provider initialisation during `plan` — will fail with authentication errors (such as `NoCredentialProviders` for AWS). Terraform does not silently skip providers or fall back to anonymous access for cloud infrastructure providers. Both AWS and GCP require explicit credentials.

---

### Question 12 — Deleting the State File Before Plan

**Difficulty**: Hard
**Answer Type**: one
**Topic**: What terraform plan proposes when the state file is missing

**Question**:
A Terraform configuration manages 15 cloud resources. The `terraform.tfstate` file is accidentally deleted. `terraform plan` is run immediately after. What does Terraform propose?

- A) Terraform returns an error because it cannot run without a state file
- B) Terraform detects the 15 existing resources via API and proposes no changes
- C) Terraform treats all 15 resources as non-existent and proposes creating all of them
- D) Terraform recreates the state file automatically by querying the cloud API

**Answer**: C

**Explanation**:
When no state file exists, Terraform has no record of any managed resources. During plan, it compares the configuration (desired state) against an empty state (no known resources). Because there is no record of any existing resource, Terraform proposes creating all 15 resources from scratch — it does not automatically query the cloud API to discover them. Running `terraform apply` in this state would likely fail or create duplicate resources. The correct remediation is to re-import the existing resources using `terraform import` or `import` blocks.

---

### Question 13 — gRPC Failure Between Core and Provider

**Difficulty**: Hard
**Answer Type**: one
**Topic**: What happens if the provider plugin crashes during an apply

**Question**:
During a `terraform apply`, the AWS provider plugin process crashes mid-operation while creating a resource. What is the most likely consequence?

- A) Terraform Core detects the crash, rolls back all changes already made, and returns the infrastructure to its pre-apply state
- B) Terraform Core reports an error; the resource may be partially created in AWS but not recorded in state, requiring manual reconciliation
- C) Terraform Core restarts the provider plugin automatically and retries the failed operation from the beginning
- D) The crash causes Terraform Core to also terminate, leaving state permanently corrupted

**Answer**: B

**Explanation**:
Because Terraform Core and provider plugins run as separate processes communicating over gRPC, a provider crash interrupts the apply mid-operation. Terraform cannot roll back cloud API changes already made — if a resource was partially created in AWS before the crash, it remains in that partial state. The state file may not record the resource because the operation never completed successfully. This results in a "real-but-untracked" resource that must be manually reconciled, typically via `terraform import`. Terraform Core itself does not crash — only the provider process does.

---
