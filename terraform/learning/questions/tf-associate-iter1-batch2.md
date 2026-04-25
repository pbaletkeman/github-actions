# Terraform Associate (004) — Question Bank Iter 1 Batch 2

**Iteration**: 1
**Iteration Style**: Foundational recall — define terms, name commands, identify blocks
**Batch**: 2
**Objective**: 2 — Terraform Fundamentals (Providers & State)
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 2 Easy / 9 Medium / 2 Hard
**Answer Types**: 11 `one` / 2 `many` / 0 `none`

---

## Questions

---

### Question 1 — Provider Definition

**Difficulty**: Easy
**Answer Type**: one
**Topic**: What a Terraform provider is

**Question**:
Which of the following best describes a Terraform provider?

- A) A HashiCorp-managed cloud platform that stores Terraform state remotely
- B) A plugin that bridges Terraform's HCL configuration to a target cloud or service API
- C) A built-in Terraform block that defines authentication credentials in a `.tf` file
- D) A version of the Terraform CLI binary compiled for a specific operating system

**Answer**: B

**Explanation**:
A provider is a Terraform plugin responsible for authenticating to a cloud or service API, implementing CRUD operations for resources, exposing data sources, validating configuration, and mapping resource attributes to and from state. Every resource in Terraform belongs to a provider.

---

### Question 2 — Provider Tiers

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Provider tier classification

**Question**:
Terraform providers are classified into three tiers. Which tier is maintained directly by HashiCorp and carries the highest level of trust?

- A) Community
- B) Partner
- C) Official
- D) Verified

**Answer**: C

**Explanation**:
The three provider tiers are Official (maintained by HashiCorp — e.g., AWS, Azure, GCP), Partner (maintained by technology partners and reviewed/approved by HashiCorp), and Community (maintained by individuals or organisations with no HashiCorp verification). Official providers carry the highest trust level.

---

### Question 3 — Plugin Architecture Communication

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Terraform Core and provider communication protocol

**Question**:
How does Terraform Core communicate with a provider plugin during execution?

- A) Through direct function calls within the same process binary
- B) Via HTTP REST API calls to a locally running provider web server
- C) Via gRPC, with the provider running as a separate process from Terraform Core
- D) Through shared memory mapped files in the `.terraform/` directory

**Answer**: C

**Explanation**:
Terraform Core and provider plugins run as **separate processes** that communicate over **gRPC**. This separation allows providers to be upgraded independently of Terraform Core. The provider binary in turn makes HTTPS API calls to the target cloud service.

---

### Question 4 — Provider Source Address Format

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Provider source address and default registry hostname

**Question**:
A `required_providers` block declares `source = "hashicorp/aws"`. What is the fully qualified source address that Terraform resolves this to?

- A) `github.com/hashicorp/aws`
- B) `registry.terraform.io/hashicorp/aws`
- C) `releases.hashicorp.com/hashicorp/aws`
- D) `hashicorp.com/terraform/providers/aws`

**Answer**: B

**Explanation**:
The full source address format is `<hostname>/<namespace>/<type>`. When the hostname is omitted, Terraform defaults to `registry.terraform.io`. So `hashicorp/aws` resolves to `registry.terraform.io/hashicorp/aws`.

---

### Question 5 — `terraform init` Provider Download

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Where terraform init installs providers

**Question**:
When `terraform init` is run, where are provider plugins downloaded to?

- A) `~/.terraform.d/plugins/`
- B) `terraform.tfstate`
- C) `.terraform/providers/`
- D) `providers.lock/`

**Answer**: C

**Explanation**:
`terraform init` downloads required provider plugins into the `.terraform/providers/` directory within the working directory. It also creates or updates the dependency lock file `.terraform.lock.hcl` to record the exact provider versions and their checksums.

---

### Question 6 — Dependency Lock File

**Difficulty**: Medium
**Answer Type**: one
**Topic**: `.terraform.lock.hcl` purpose and version control

**Question**:
What is the purpose of the `.terraform.lock.hcl` file, and how should it be treated with respect to version control?

- A) It records the current Terraform state and must never be committed to version control
- B) It records exact provider versions and checksums installed; it must be committed to version control
- C) It is a temporary cache file generated on each `terraform init` run and should be added to `.gitignore`
- D) It stores encrypted provider credentials and must be committed but kept private

**Answer**: B

**Explanation**:
`.terraform.lock.hcl` records the exact provider version installed, the version constraint from `required_providers`, and cryptographic hashes for verification. It **must be committed** to version control so all team members and CI pipelines use identical provider versions. Use `terraform init -upgrade` to update versions within constraints.

---

### Question 7 — Pessimistic Constraint Operator

**Difficulty**: Medium
**Answer Type**: one
**Topic**: `~>` version constraint operator behaviour

**Question**:
A `required_providers` block specifies `version = "~> 5.0"` for the AWS provider. Which versions are permitted by this constraint?

- A) Exactly version 5.0.0 only
- B) Any version >= 5.0.0 with no upper limit
- C) Any version >= 5.0 and < 6.0 (minor and patch updates within major version 5)
- D) Any version >= 5.0.0 and < 5.1.0 (patch updates only within minor version 5.0)

**Answer**: C

**Explanation**:
The pessimistic constraint operator `~>` allows updates to the rightmost version component. `~> 5.0` allows >= 5.0 and < 6.0 (minor and patch updates within major version 5). By contrast, `~> 5.0.0` would allow only patch updates (>= 5.0.0 and < 5.1.0). This is one of the most commonly tested version constraint questions on the exam.

---

### Question 8 — Provider Alias

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Provider alias for multiple configurations

**Question**:
When is a provider `alias` required in a Terraform configuration?

- A) Whenever a provider block is declared inside a module rather than the root module
- B) When more than one configuration of the same provider is needed, such as managing resources in multiple regions
- C) Whenever a provider version constraint is specified in the `required_providers` block
- D) When Terraform is run with a non-default workspace

**Answer**: B

**Explanation**:
A provider alias is required when you need multiple configurations of the same provider — for example, deploying to both `us-east-1` and `us-west-2` using the AWS provider, or managing two separate AWS accounts. The aliased provider is referenced on a resource using `provider = aws.alias_name`.

---

### Question 9 — What `terraform.tfstate` Stores

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Content and purpose of the Terraform state file

**Question**:
What does the `terraform.tfstate` file contain?

- A) The raw HCL configuration for all resources managed by Terraform
- B) A log of every `terraform apply` command that has been executed
- C) A JSON mapping of Terraform resource addresses to their real-world resource IDs and attributes
- D) The compiled binary representation of a Terraform execution plan

**Answer**: C

**Explanation**:
`terraform.tfstate` is a JSON file that maps each HCL resource (e.g., `aws_instance.web`) to its real-world identifier (e.g., `i-0abcd1234ef567890`) and all tracked attributes. This mapping is what allows Terraform to compute diffs, track metadata, and avoid querying every resource on every plan.

---

### Question 10 — Three Sources During Plan

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Sources Terraform compares during `terraform plan`

**Question**:
When `terraform plan` runs, Terraform compares information from multiple sources to determine what changes are needed. Which TWO of the following are sources that Terraform uses during planning? (Select two.)

- A) The Terraform Registry API to check for provider updates
- B) The `.tf` configuration files representing the desired state
- C) The `terraform.tfstate` file representing the last-known resource state
- D) The `.terraform.lock.hcl` file representing installed provider versions

**Answer**: B, C

**Explanation**:
During `terraform plan`, Terraform compares three sources: the desired state (your `.tf` configuration), the known state (`terraform.tfstate`), and the actual state (live cloud resources queried via the provider API). The lock file and Registry are not part of the planning comparison — they are used during `terraform init`.

---

### Question 11 — Local vs Remote State

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Differences between local and remote Terraform state

**Question**:
Which of the following is a characteristic of local Terraform state that makes it unsuitable for team environments?

- A) Local state files use a binary format that cannot be read or backed up
- B) Local state provides no locking mechanism, meaning concurrent `terraform apply` runs by different team members can corrupt state
- C) Local state is automatically deleted after each `terraform apply` completes
- D) Local state cannot store resource attribute values, only resource addresses

**Answer**: B

**Explanation**:
Local state (`terraform.tfstate` in the working directory) has no locking, no sharing, and no encryption. Without locking, two operators running `terraform apply` simultaneously can overwrite each other's state changes, causing corruption. Remote backends such as S3, Azure Blob, or HCP Terraform provide state locking, shared access, and optional encryption — making them required for all team and production workloads.

---

### Question 12 — `sensitive = true` and State

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Behaviour of `sensitive = true` in outputs vs state storage

**Question**:
A Terraform output is declared with `sensitive = true`. What is the effect of this setting on how the value is handled?

- A) The value is encrypted with AES-256 before being written to `terraform.tfstate`
- B) The value is redacted from `terraform.tfstate` entirely and stored in a separate secrets file
- C) The value is masked in terminal output but is still stored in plaintext in `terraform.tfstate`
- D) The value is masked in terminal output and also removed from any remote backend storage

**Answer**: C

**Explanation**:
`sensitive = true` on an output value suppresses it from being displayed in terminal output during `terraform apply` or `terraform output`. However, it does **not** encrypt or remove the value from state — the attribute remains stored in plaintext in `terraform.tfstate`. To protect sensitive values at rest, you must use a remote backend with encryption (e.g., S3 with server-side encryption enabled).

---

### Question 13 — `terraform state` Commands

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Terraform state CLI subcommands

**Question**:
Which TWO `terraform state` subcommands would be appropriate for inspecting state without modifying it? (Select two.)

- A) `terraform state rm`
- B) `terraform state list`
- C) `terraform state push`
- D) `terraform state show`

**Answer**: B, D

**Explanation**:
`terraform state list` lists all resource addresses currently tracked in state, and `terraform state show <address>` displays all attributes of a specific resource — both are read-only operations. By contrast, `terraform state rm` removes a resource from state (destructive), and `terraform state push` uploads a local state file to a remote backend (potentially overwriting it).

---
