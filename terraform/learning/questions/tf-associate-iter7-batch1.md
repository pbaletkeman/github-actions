# Terraform Associate (004) — Question Bank Iter 7 Batch 1

**Iteration**: 7
**Iteration Style**: Fill-in-the-blank / complete the HCL — given a partial configuration or partial statement, select the term or argument that correctly fills the blank
**Batch**: 1
**Objective**: 1 — IaC Concepts
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 3 Easy / 8 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — IaC Benefit Name for Identical Environments

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Repeatability — the IaC benefit that guarantees the same code deploys identical environments across staging and production

**Question**:
Complete the following statement by selecting the term that fills the blank:

> "Running the same Terraform configuration in the staging and production environments guarantees identical infrastructure. This IaC benefit is called ___________."

- A) Atomicity
- B) Convergence
- C) Modularity
- D) **Repeatability**

**Answer**: D

**Explanation**:
Repeatability is one of the core IaC benefits listed in Terraform learning materials: "Same code → identical environment every time." Because infrastructure is described as version-controlled code, any environment — staging, production, QA, disaster-recovery — can be provisioned from the same configuration and will produce structurally identical results. Atomicity, convergence, and modularity are related software engineering concepts but are not the named IaC benefit for this property.

---

### Question 2 — `instance_type` Argument in an EC2 Resource Block

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Completing a basic `aws_instance` resource block — the argument that specifies the EC2 instance type

**Question**:
A developer is writing their first Terraform resource block and leaves one argument name incomplete:

```hcl
resource "aws_instance" "web" {
  ami           = "ami-0abcd1234"
  ___________   = "t3.micro"
}
```

What argument name fills the blank to specify the EC2 instance size?

- A) `machine_type`
- B) `instance_size`
- C) **`instance_type`**
- D) `type`

**Answer**: C

**Explanation**:
In the `aws_instance` resource, the argument `instance_type` sets the EC2 instance family and size (e.g., `"t3.micro"`, `"m5.large"`). The argument name is specific to the AWS provider. `machine_type` is the equivalent argument name in the `google_compute_instance` resource (GCP); `instance_size` does not exist in any major provider; `type` is a meta-argument used for resource type alias patterns but not for setting the instance family here.

---

### Question 3 — "Desired" End State in Declarative IaC

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Completing the definition of declarative IaC — configurations express the desired end state

**Question**:
Complete the following statement:

> "Terraform configurations describe the ___________ end state of infrastructure. The tool determines what steps are necessary to reach that state, whether that means creating, modifying, or destroying resources."

- A) procedural
- B) scripted
- C) **desired**
- D) imperative

**Answer**: C

**Explanation**:
The core characteristic of a declarative IaC tool like Terraform is that you describe the **desired** end state — what you want to exist — rather than the **procedural** or **imperative** sequence of commands to create it. Terraform then compares that desired state against the current tracked state and calculates the minimum set of actions (create, update, or destroy) to reconcile the difference. "Scripted" and "imperative" describe the opposite approach, where an operator writes step-by-step instructions.

---

### Question 4 — `count = 0` to Conditionally Disable a Resource

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Using `count = 0` to declare that no instances of a resource should exist, while keeping the block in configuration

**Question**:
An engineer wants the bastion host resource block to remain in the configuration file for documentation purposes, but needs to ensure **no instances are created in the current environment**. Complete the resource block:

```hcl
resource "aws_instance" "bastion" {
  ami           = "ami-0abc1234"
  instance_type = "t3.nano"
  count         = ___________
}
```

What value fills the blank?

- A) `null`
- B) `false`
- C) `"disabled"`
- D) **`0`**

**Answer**: D

**Explanation**:
Setting `count = 0` tells Terraform the desired number of instances of this resource is zero. If any instances currently exist in state, Terraform plans to destroy them; if none exist, no action is taken. The resource block remains in the configuration file as documentation of the resource's definition without affecting infrastructure. `null` and `false` are not valid values for `count`; `"disabled"` is a string and would cause a type error since `count` requires a non-negative integer.

---

### Question 5 — "Drift" — Infrastructure Deviation From Desired State

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Completing the definition of infrastructure drift — when actual cloud resources diverge from the Terraform configuration

**Question**:
Complete the following statement:

> "When a cloud engineer manually modifies an EC2 instance's security group through the AWS Console — without updating the Terraform configuration — the actual state of that resource diverges from what the `.tf` files describe. This divergence is called infrastructure ___________."

- A) contamination
- B) corruption
- C) divergence
- D) **drift**

**Answer**: D

**Explanation**:
**Drift** is the specific term used in Terraform (and IaC tooling in general) for the condition where the actual state of cloud resources no longer matches what the configuration declares as the desired state. Drift is caused by out-of-band changes: manual console modifications, other automation tools acting on the same resources, auto-scaling events, or provider-side changes. Terraform can detect drift by comparing the refreshed state (queried from the cloud API) against the configuration during `terraform plan`. "Divergence" is a synonymous English word but not the recognised technical IaC term used in Terraform documentation and exam objectives.

---

### Question 6 — "Audit Trail" Benefit of Version-Controlled IaC

**Difficulty**: Medium
**Answer Type**: one
**Topic**: The IaC benefit that provides a historical record of every infrastructure change via version control commits

**Question**:
Complete the following statement:

> "When infrastructure configuration files are stored in a version control system such as Git, every infrastructure change is captured as a commit — recording who made the change, when, and what was modified. This gives teams a complete ___________ of infrastructure changes."

- A) Terraform plan output
- B) `terraform.tfstate.backup`
- C) speculative plan history
- D) **audit trail**

**Answer**: D

**Explanation**:
An **audit trail** is one of the explicitly listed IaC benefits that addresses a key limitation of the manual ClickOps approach: "No audit trail — who changed what and when?" By defining infrastructure as code in a version-controlled repository, every change becomes a commit with a timestamp, author, and diff. This is valuable for security reviews, compliance audits, incident post-mortems, and team collaboration. `terraform.tfstate.backup` is the previous state file (not an audit history); plan output shows a proposed diff but is not a persistent change record.

---

### Question 7 — TWO Provisioning Tools in the IaC Landscape

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Distinguishing provisioning tools (Terraform category) from configuration management tools — CloudFormation and Pulumi are provisioning tools

**Question**:
An engineer is building a comparison table of IaC tools with a "Provisioning" vs "Configuration Management" category column. Terraform belongs in the **Provisioning** category. Which TWO other tools from the list below also belong in the **Provisioning** category? (Select two.)

- A) Ansible
- B) **AWS CloudFormation**
- C) Chef
- D) **Pulumi**

**Answer**: B, D

**Explanation**:
**Provisioning tools** create and manage cloud infrastructure resources (virtual machines, networks, storage, databases). **AWS CloudFormation** (B) and **Pulumi** (D) are both provisioning tools — they declare and manage cloud infrastructure, placing them in the same category as Terraform. **Ansible** (A) and **Chef** (C) are primarily **configuration management tools** — they manage the state of software, packages, files, and services on existing servers, rather than provisioning the servers themselves. (Note: tools like Ansible have some infrastructure provisioning modules, but their primary classification is configuration management.)

---

### Question 8 — "Documentation" Benefit of IaC Configuration Files

**Difficulty**: Medium
**Answer Type**: one
**Topic**: IaC configuration files serve as living documentation of infrastructure

**Question**:
Complete the following statement from Terraform's IaC benefits:

> "Unlike a ClickOps environment where the exact infrastructure setup exists only in someone's memory or informal runbooks, IaC configuration files serve as living ___________ of what has been provisioned — always up to date because they are the authoritative source used to apply infrastructure."

- A) backups
- B) **documentation**
- C) snapshots
- D) manifests

**Answer**: B

**Explanation**:
**Documentation** is one of the listed IaC benefits: "Config files serve as living documentation of infrastructure." Because the configuration file must exactly match the deployed infrastructure (otherwise Terraform would show a diff on the next plan), the `.tf` files are always a current, accurate description of what exists. This is qualitatively different from separate documentation files that can fall out of sync. New team members can read the configuration files to understand the entire infrastructure without needing knowledge transfer from the original authors. "Manifests" is used in Kubernetes contexts; "backups" and "snapshots" describe point-in-time copies.

---

### Question 9 — Declarative IaC Describes WHAT, Not HOW

**Difficulty**: Medium
**Answer Type**: one
**Topic**: In a declarative IaC approach, the engineer describes WHAT the infrastructure should look like — not the steps to create it

**Question**:
Complete the following statement that describes the Terraform configuration model:

> "In Terraform's declarative approach, the engineer describes ___________ the infrastructure should look like. Terraform determines the sequence of API calls and resource operations needed to produce that outcome."

- A) how
- B) why
- C) when
- D) **what**

**Answer**: D

**Explanation**:
The fundamental distinction between declarative and imperative tools is **what** vs **how**. Terraform's HCL configuration says "I want 3 EC2 instances of type `t3.micro` in `us-east-1`" — it declares the desired end state (**what**). Terraform then handles the **how**: calling the AWS EC2 `RunInstances` API, waiting for provisioning to complete, writing state, etc. An imperative tool (like a shell script) would require the engineer to write each of those steps explicitly. This "what not how" principle is why Terraform can detect changes and only apply the minimum necessary modifications — it always works toward the declared desired state, regardless of the current state.

---

### Question 10 — Conditional `count` Expression for Optional Resources

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Using a ternary conditional expression to set count = 1 when a boolean variable is true, otherwise count = 0

**Question**:
An engineer wants to conditionally create a bastion host only when the `create_bastion` variable is set to `true`. Complete the resource block:

```hcl
variable "create_bastion" {
  type    = bool
  default = false
}

resource "aws_instance" "bastion" {
  ami           = "ami-0abc1234"
  instance_type = "t3.nano"
  count         = var.create_bastion ? ___ : 0
}
```

What value fills the blank to create exactly one bastion instance when `create_bastion` is `true`?

- A) `true`
- B) **`1`**
- C) `"enabled"`
- D) `var.bastion_count`

**Answer**: B

**Explanation**:
The `count` meta-argument requires a non-negative integer value. The ternary expression `var.create_bastion ? 1 : 0` evaluates to `1` when `create_bastion` is `true` (creating one instance) and `0` when it is `false` (creating no instances). `true` is a boolean and would cause a type error since `count` expects a number. `"enabled"` is a string — also invalid. `var.bastion_count` would work if such a variable existed, but it is not declared and would be an error. The `bool ? 1 : 0` conditional count pattern is the canonical Terraform idiom for optional resource creation.

---

### Question 11 — TWO Accurate Statements About IaC Solving ClickOps Problems

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Identifying which IaC benefits directly address the reproducibility and drift problems of the manual ClickOps approach

**Question**:
A team is documenting why they are adopting Terraform to replace their ClickOps workflow. Which TWO of the following statements correctly complete the sentence: "We are adopting IaC because ___________"? (Select two.)

- A) **The same configuration can be applied to staging and production to guarantee identical environments, solving the problem of environments drifting apart over time through individual manual changes**
- B) IaC removes the need for cloud providers because infrastructure is simulated locally in configuration files
- C) IaC configurations run faster than manual provisioning because they use compiled binary formats
- D) **An IaC tool can compare the desired state in configuration files against the actual cloud state, detect manual changes made outside Terraform, and propose corrections**

**Answer**: A, D

**Explanation**:
**(A)** addresses the **repeatability** and **environment consistency** problem of ClickOps: manually provisioned environments inevitably diverge because different operators make different changes at different times. IaC solves this by using the same code as the single source of truth for all environments. **(D)** addresses the **drift detection** capability: IaC tools can query the cloud API to compare actual resource attributes against the configuration and flag deviations, a capability that is impossible with manual approaches. **(B)** is false — IaC still communicates with cloud providers; nothing is simulated. **(C)** is false — HCL is interpreted, not compiled, and provisioning speed is dominated by cloud API latency, not the configuration format.

---

### Question 12 — `resource` Keyword in HCL Block Declaration

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Completing an HCL block declaration — the `resource` keyword identifies a managed infrastructure object

**Question**:
A new engineer is writing their first Terraform file. They accidentally leave out the opening keyword of the block:

```hcl
___________ "aws_s3_bucket" "assets" {
  bucket = "acme-company-assets-2024"
}
```

What keyword fills the blank to correctly declare an S3 bucket that Terraform will create and manage?

- A) `data`
- B) `module`
- C) `provider`
- D) **`resource`**

**Answer**: D

**Explanation**:
In Terraform HCL, the `resource` keyword identifies a **managed resource** — infrastructure that Terraform creates, reads, updates, and destroys through the lifecycle of the configuration. The block structure is `resource "<TYPE>" "<NAME>" { ... }`. Each of the four available block keywords serves a distinct purpose: `data` declares a **data source** (read-only lookup of existing infrastructure or external data); `module` calls a **child module** (reusable configuration bundle); `provider` configures a **provider** (the plugin that communicates with a cloud API). Using `data "aws_s3_bucket" "assets" { ... }` would attempt to look up an existing bucket by name, not create one.

---

### Question 13 — TWO Advantages of Terraform Over AWS CloudFormation

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Terraform's multi-cloud support and open-source/non-vendor-locked nature versus CloudFormation's AWS-only scope

**Question**:
A platform engineering team is choosing between Terraform and AWS CloudFormation for a new infrastructure project. Which TWO of the following correctly complete the statement: "We should choose Terraform over CloudFormation because ___________"? (Select two.)

- A) **Terraform can manage resources across AWS, Azure, and GCP in a single configuration and workflow, whereas CloudFormation only manages AWS resources**
- B) CloudFormation uses an imperative scripting model, while Terraform uses a superior declarative model
- C) Terraform plan output includes cost estimation for every resource type, while CloudFormation does not support cost estimation
- D) **Terraform is not tied to a single cloud vendor — the same tool, workflow, and skills apply whether managing AWS, Azure, GCP, or on-premises resources**

**Answer**: A, D

**Explanation**:
**(A)** is accurate and is the primary technical advantage: Terraform's provider model supports multi-cloud management from a single tool. AWS CloudFormation exclusively manages AWS resources; there is no CloudFormation equivalent for Azure or GCP resources. **(D)** is also accurate and states the organisational implication: adopting Terraform means teams build skills and workflows that are reusable across any cloud provider — avoiding vendor lock-in at the tooling layer. **(B)** is false — CloudFormation is also declarative; you declare desired stacks in JSON or YAML templates, and CloudFormation reconciles them. **(C)** is false — cost estimation in Terraform is an HCP Terraform feature, not a CLI feature for all resource types; CloudFormation also has cost estimation through AWS Cost Calculator integrations.

---
