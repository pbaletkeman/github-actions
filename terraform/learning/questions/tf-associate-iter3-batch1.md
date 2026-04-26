# Terraform Associate (004) — Question Bank Iter 3 Batch 1

**Iteration**: 3
**Iteration Style**: HCL interpretation — read a snippet and identify output/effect
**Batch**: 1
**Objective**: 1 — IaC Concepts
**Generated**: 2026-04-25
**Total Questions**: 12
**Difficulty Split**: 2 Easy / 8 Medium / 2 Hard
**Answer Types**: 9 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — State Addresses Created by count

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Reading a `count` argument to determine how many state resource addresses Terraform tracks

**Question**:
Given the following configuration:

```hcl
resource "aws_instance" "app" {
  ami           = "ami-0abc1234"
  instance_type = "t3.micro"
  count         = 3
}
```

How many resource addresses does Terraform record in state after a successful apply?

- A) 1 — `aws_instance.app`
- B) 3 — `aws_instance.app[0]`, `aws_instance.app[1]`, `aws_instance.app[2]`
- C) 3 — `aws_instance.app.0`, `aws_instance.app.1`, `aws_instance.app.2`
- D) 1 — Terraform groups all instances under a single `aws_instance.app` state entry

**Answer**: B

**Explanation**:
When `count` is used, Terraform creates one state entry per instance using zero-based bracket notation: `aws_instance.app[0]`, `aws_instance.app[1]`, and `aws_instance.app[2]`. Dot notation (option C) is an older format occasionally seen in legacy documentation but is not the canonical address format for `count`-based resources in current Terraform versions.

---

### Question 2 — count = 0 Effect on Resource

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Interpreting what `count = 0` means for a resource block

**Question**:
Read this configuration:

```hcl
resource "aws_instance" "bastion" {
  ami           = "ami-0abc1234"
  instance_type = "t3.nano"
  count         = 0
}
```

What does this configuration tell Terraform about the `aws_instance.bastion` resource?

- A) The resource is disabled — Terraform will create it on the next run when count is unset
- B) Zero instances of this resource should exist; if any currently exist, Terraform plans to destroy them
- C) This is a syntax error — `count` must be at least 1
- D) Terraform creates the resource but does not assign it an AMI or instance type

**Answer**: B

**Explanation**:
Setting `count = 0` is a valid and intentional pattern in Terraform. It declares that zero instances of the resource should exist. If any instances are currently tracked in state, Terraform will plan to destroy them on the next apply. This technique is commonly used to conditionally enable or disable a resource without removing its block from the configuration.

---

### Question 3 — Identifying Declarative vs Imperative from Snippets

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Classifying HCL vs shell script by the IaC approach each represents

**Question**:
Examine the two snippets below:

**Snippet A:**
```bash
aws ec2 run-instances \
  --image-id ami-0abc1234 \
  --instance-type t3.micro \
  --count 1
```

**Snippet B:**
```hcl
resource "aws_instance" "web" {
  ami           = "ami-0abc1234"
  instance_type = "t3.micro"
}
```

What is the correct characterisation of each snippet's approach?

- A) Both are declarative — they both describe a single EC2 instance
- B) Snippet A is declarative; Snippet B is imperative
- C) Snippet A is imperative; Snippet B is declarative
- D) Both are imperative — they both invoke AWS APIs to create resources

**Answer**: C

**Explanation**:
Snippet A is an **imperative** approach — it issues a CLI command that tells AWS exactly what steps to take to create an instance right now. Snippet B is **declarative** — it describes the desired end state (an EC2 instance of type `t3.micro`) and lets Terraform determine whether to create, update, or take no action based on the current state. Terraform HCL is always declarative; shell scripts issuing API calls are imperative.

---

### Question 4 — Multi-Provider Config Provider Count

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reading a multi-cloud config and identifying how many provider plugins are required

**Question**:
Read this configuration:

```hcl
provider "aws" {
  region = "us-east-1"
}

provider "azurerm" {
  features {}
}

resource "aws_s3_bucket" "data" {
  bucket = "my-data-bucket"
}

resource "azurerm_storage_account" "backup" {
  name                     = "backupstorage"
  resource_group_name      = "rg-backup"
  location                 = "eastus"
  account_tier             = "Standard"
  account_replication_type = "LRS"
}
```

How many provider plugins must `terraform init` download for this configuration to work?

- A) 1 — Terraform uses a single universal provider plugin for all cloud resources
- B) 2 — one AWS provider plugin and one Azure provider plugin
- C) 2 — one provider plugin shared between AWS and Azure resources
- D) 4 — one plugin per resource block

**Answer**: B

**Explanation**:
Each distinct `provider` block requires its own separate provider plugin. This configuration declares two providers — `"aws"` and `"azurerm"` — so `terraform init` must download and install two provider plugins: one from the AWS provider registry and one from the AzureRM provider registry. Resources are associated with provider plugins based on their type prefix, not on a one-per-resource basis.

---

### Question 5 — Desired vs Current State Table Interpretation

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reading a desired vs current state comparison to predict what terraform plan proposes

**Question**:
A team's Terraform configuration and current cloud state can be summarised as:

| Resource | Desired State (config) | Current State (cloud) |
|----------|----------------------|----------------------|
| `aws_instance.web[0]` | `t3.micro` | `t3.micro` |
| `aws_instance.web[1]` | `t3.micro` | Does not exist |
| `aws_instance.web[2]` | Does not exist | `t3.micro` |

What does `terraform plan` propose?

- A) No changes — all three instances are accounted for
- B) Create `aws_instance.web[1]`, destroy `aws_instance.web[2]`
- C) Destroy all three existing instances and recreate them
- D) Create `aws_instance.web[1]` only; `aws_instance.web[2]` is kept as unmanaged

**Answer**: B

**Explanation**:
Terraform reconciles differences between desired and current state. `aws_instance.web[0]` is already in the desired state — no action. `aws_instance.web[1]` exists in configuration but not in the cloud — Terraform plans to **create** it. `aws_instance.web[2]` exists in the cloud (and in state) but is not in the configuration — Terraform plans to **destroy** it. This targeted reconciliation is characteristic of declarative IaC.

---

### Question 6 — What "Idempotent" Means for This Apply Output

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Interpreting an apply summary line to confirm idempotent behaviour

**Question**:
After a `terraform apply` completes, the final summary line reads:

```
Apply complete! Resources: 0 added, 0 changed, 0 destroyed.
```

What does this output tell you about the relationship between the configuration and the infrastructure?

- A) The apply failed silently — Terraform could not reach the provider API
- B) Terraform destroyed existing resources before recreating them with zero net change
- C) The infrastructure already matched the desired state — no actions were needed, demonstrating idempotency
- D) The configuration is empty — no resource blocks are declared

**Answer**: C

**Explanation**:
An apply summary of `0 added, 0 changed, 0 destroyed` confirms that the current state of the infrastructure already matched the desired state expressed in the configuration. No modifications were required. This is Terraform's idempotent behaviour in action — running apply multiple times against an unchanged configuration is always safe and produces this output once the desired state is reached.

---

### Question 7 — Provider Constraint on Cloud Scope

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Reading a provider block list and identifying correct claims about each tool's cloud scope

**Question**:
Read this excerpt from a comparison table:

```
Tool A: provider block required — "aws" only
Tool B: provider block required — "aws", "azurerm", "google", and 3000+ others
Tool C: template-based — no explicit provider concept, AWS only
```

Which TWO statements correctly identify the tools described? (Select two.)

- A) Tool A is Terraform — it only supports the AWS provider
- B) Tool B is Terraform — it is provider-agnostic with plugins for 3000+ providers
- C) Tool C is AWS CloudFormation — it is AWS-only and uses templates without explicit provider blocks
- D) Tool C is Azure ARM templates — it uses templates but supports multi-cloud via a provider concept

**Answer**: B, C

**Explanation**:
Tool B is Terraform — its provider plugin ecosystem spans 3000+ providers (AWS, Azure, GCP, Kubernetes, and more), making it the canonical multi-cloud IaC tool. Tool C matches AWS CloudFormation — it uses JSON/YAML templates (not HCL) and is AWS-only with no explicit provider concept. Tool A does not match Terraform; Terraform is not limited to AWS.

---

### Question 8 — Documentation Role of Config Files

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Interpreting a configuration file as living documentation of infrastructure

**Question**:
A new engineer joins a team and is given read access to the Git repository containing all Terraform `.tf` files. The repository has no separate architecture diagrams or runbooks. Which statement best describes what the engineer can determine from the Terraform files alone?

- A) Nothing useful — Terraform files only contain syntax, not meaningful infrastructure information
- B) Only the list of resource types — attributes and values are hidden by variable references
- C) The complete intended infrastructure topology: resource types, configurations, dependencies, and relationships — because configuration files serve as living documentation
- D) Only the most recently applied state — older configurations require the state file to reconstruct

**Answer**: C

**Explanation**:
One of the key benefits of IaC is that configuration files serve as living documentation. A well-written Terraform repository describes the full desired infrastructure topology — resource types, attribute values, inter-resource references, and dependency relationships — in human-readable HCL. An engineer who can read HCL can understand the intended architecture without separate runbooks or diagrams, because the code is the authoritative description of the system.

---

### Question 9 — Interpreting Tool Type from Snippet

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying whether a snippet represents a config management tool or a provisioning tool

**Question**:
Read the following snippet:

```yaml
- name: Install nginx
  apt:
    name: nginx
    state: present

- name: Start nginx
  service:
    name: nginx
    state: started
```

And this snippet:

```hcl
resource "aws_instance" "web" {
  ami           = "ami-0abc1234"
  instance_type = "t3.micro"
}
```

What category does each snippet belong to?

- A) Both are provisioning tools — they both create server-level resources
- B) The YAML snippet is from a **configuration management** tool (Ansible); the HCL snippet is from a **provisioning** tool (Terraform)
- C) The YAML snippet is from Terraform's YAML provider; the HCL snippet is from CloudFormation
- D) Both are declarative configuration management tools in the Ansible/Terraform ecosystem

**Answer**: B

**Explanation**:
The YAML snippet uses Ansible's task syntax (`apt` module to install packages, `service` module to manage daemons) — a **configuration management** tool focused on what runs *inside* a server. The HCL snippet uses Terraform's `resource` block syntax to declare cloud infrastructure — a **provisioning** tool focused on creating the server itself. Terraform and Ansible are complementary in the toolchain: Terraform provisions the VM; Ansible configures it.

---

### Question 10 — Audit Trail Interpretation from Git Log

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Interpreting what a Git log of `.tf` file changes provides as an IaC benefit

**Question**:
A team stores their Terraform configuration in Git. The following `git log` excerpt exists for `main.tf`:

```
abc1234 2026-03-10  alice  chore: increase web tier to t3.medium
def5678 2026-02-28  bob    feat: add RDS instance for prod
ghi9012 2026-01-15  carol  fix: reduce bastion count to 0
```

What IaC benefit does this log directly demonstrate?

- A) Repeatability — the same configuration can deploy identical environments
- B) Speed — automated provisioning is faster than manual console changes
- C) Audit trail — every infrastructure change is a version-controlled commit with author and timestamp
- D) Idempotency — running the same configuration multiple times produces the same result

**Answer**: C

**Explanation**:
The `git log` shows that every infrastructure change — resizing instances, adding databases, disabling resources — was committed with the author's name and a timestamp. This is the **audit trail** benefit of IaC: all changes are traceable, reviewable, and attributable without relying on cloud provider activity logs alone. Repeatability and idempotency are also IaC benefits, but they are not directly demonstrated by a commit history.

---

### Question 11 — CloudFormation vs Terraform Multi-Cloud Snippet

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Interpreting a snippet that declares both AWS and Azure resources to identify which tool it must be written in

**Question**:
A colleague shares this configuration snippet and claims it is valid for a popular IaC tool:

```hcl
resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"
}

resource "azurerm_virtual_network" "vnet" {
  name                = "prod-vnet"
  address_space       = ["10.1.0.0/16"]
  location            = "East US"
  resource_group_name = "rg-prod"
}
```

The snippet simultaneously manages resources in AWS (`aws_vpc`) and Azure (`azurerm_virtual_network`). Which tool could this configuration be written for?

- A) AWS CloudFormation — it supports multi-cloud resources via cross-account stacks
- B) Azure ARM templates — they natively support AWS resources through an Azure bridge provider
- C) Terraform — its provider plugin model supports multiple cloud providers in a single configuration
- D) Neither — no IaC tool supports declaring AWS and Azure resources in the same configuration file

**Answer**: C

**Explanation**:
Only Terraform's provider plugin model supports declaring resources from multiple cloud providers within a single root module configuration. AWS CloudFormation is AWS-only and cannot manage Azure resources. Azure ARM templates (and Bicep) are Azure-only. This multi-cloud capability — using `aws_*` and `azurerm_*` resources together in one configuration — is a defining characteristic of Terraform.

---

### Question 12 — Interpreting "Drift" from a State vs Cloud Comparison

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Reading a state-vs-actual comparison and identifying which attributes represent drift

**Question**:
The Terraform state file records the following for `aws_db_instance.primary`:

```
instance_class = "db.t3.medium"
backup_retention_period = 7
multi_az = false
```

A cloud administrator ran `terraform plan` and the output shows:

```
~ aws_db_instance.primary
    ~ instance_class           = "db.t3.medium" -> "db.t3.large"
    ~ backup_retention_period  = 7 -> 14
      multi_az                 = false
```

Which TWO statements correctly interpret this plan output? (Select two.)

- A) `instance_class` and `backup_retention_period` were changed outside of Terraform and represent infrastructure drift
- B) `multi_az = false` was changed outside of Terraform — Terraform plans to update it to `true`
- C) The `~` symbol before `aws_db_instance.primary` means the resource will be destroyed and recreated
- D) Terraform plans to update `instance_class` and `backup_retention_period` in-place to match the desired state in the configuration

**Answer**: A, D

**Explanation**:
The `~` prefix on an attribute line means an in-place update — the resource will be modified without being destroyed. The plan shows `instance_class` changing from `db.t3.medium` to `db.t3.large` and `backup_retention_period` from `7` to `14` — these attributes were either changed outside Terraform (drift) or updated in the configuration. `multi_az = false` appears without a `~` or change arrow, meaning no change is proposed for that attribute. Option B is incorrect; option C is incorrect — `~` on the resource line means update, not replace. Option D correctly describes the update semantics.

---
