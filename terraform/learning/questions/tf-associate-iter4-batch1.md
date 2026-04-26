# Terraform Associate (004) — Question Bank Iter 4 Batch 1

**Iteration**: 4
**Iteration Style**: Error identification — spot the mistake in config, workflow, or claim
**Batch**: 1
**Objective**: 1 — IaC Concepts
**Generated**: 2026-04-25
**Total Questions**: 12
**Difficulty Split**: 2 Easy / 8 Medium / 2 Hard
**Answer Types**: 9 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — Incorrect Idempotency Definition

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Identifying the error in a definition of idempotency

**Question**:
A team lead explains idempotency to a new hire:

> "Idempotency means Terraform is thorough — every time you run `terraform apply`, it destroys the existing infrastructure and recreates it from scratch to ensure everything matches the configuration exactly."

What is the error in this explanation?

- A) There is no error — destroy-and-recreate on every apply is exactly how Terraform ensures consistency
- B) Idempotency means the *opposite* — applying the same configuration multiple times produces the same end state without unnecessary changes; if the infrastructure already matches, no actions are taken
- C) The error is that idempotency only applies to the first apply — subsequent applies always trigger a full rebuild
- D) The error is that `terraform apply` never destroys resources — only `terraform destroy` can do that

**Answer**: B

**Explanation**:
Idempotency means applying the same configuration N times produces the same result. After the first successful apply, running `terraform apply` again against an unchanged configuration reports "No changes. Your infrastructure matches the configuration." — no destroy-and-recreate cycle occurs. Terraform plans only the *difference* between desired and current state; if there is no difference, it takes no action. The team lead has confused "thorough" with idempotent.

---

### Question 2 — Incorrect Claim About `count = 0`

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Identifying the error in a claim that `count = 0` is invalid syntax

**Question**:
During a code review, an engineer comments:

> "This is a syntax error — `count = 0` is not allowed in a `resource` block. The `count` meta-argument must be at least `1` or it should be removed entirely."

What is the error in this comment?

- A) The comment is correct — `count = 0` raises a Terraform validation error
- B) The comment is correct, but `count = 0` is allowed only in module blocks, not resource blocks
- C) The comment is incorrect — `count = 0` is valid and useful; it tells Terraform the desired number of instances is zero, so any existing instances will be planned for destruction
- D) The comment is partially correct — `count = 0` is allowed but only when combined with a `lifecycle` block

**Answer**: C

**Explanation**:
`count = 0` is perfectly valid Terraform syntax and is a deliberate pattern used to conditionally disable a resource without removing its block from the configuration. When `count = 0`, Terraform's desired state for that resource is zero instances, so if any currently exist in state, they will be planned for destruction. This is equivalent to saying "this resource should not exist." There is no minimum value requirement for `count`.

---

### Question 3 — State File Described as Desired State

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying the error in conflating the state file with desired state

**Question**:
A senior engineer is onboarding new team members and says:

> "The `terraform.tfstate` file is the source of truth for your desired infrastructure. Edit it directly when you want to change what Terraform will build on the next apply."

What is the error in this statement?

- A) There is no error — `terraform.tfstate` is both the desired state and the current state record
- B) The error is that the state file records the **current** tracked state of infrastructure (what Terraform believes exists), not the desired state; desired state is expressed in the `.tf` configuration files
- C) The error is that `terraform.tfstate` is read-only and cannot be edited under any circumstances
- D) The error is that the state file is not a source of truth at all — Terraform queries the cloud provider API every time without using it

**Answer**: B

**Explanation**:
The `terraform.tfstate` file records **current state** — what Terraform believes currently exists in the cloud, based on the last successful apply. Desired state is expressed in `.tf` configuration files. The two are compared during `terraform plan` to determine what changes to make. Directly editing the state file bypasses Terraform's safety checks and is discouraged for making configuration changes; `.tf` files are the correct place to declare desired infrastructure.

---

### Question 4 — Drift Defined as "Unrun Apply"

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying the error in a definition of infrastructure drift

**Question**:
A developer writes this entry in a team wiki:

> "Infrastructure drift occurs when a developer updates the `.tf` configuration files but forgets to run `terraform apply`. The infrastructure has 'drifted' away from what the code says."

What is the error in this definition?

- A) There is no error — this is the correct definition of infrastructure drift
- B) The error is minor — the developer should have written `terraform plan` instead of `terraform apply`
- C) The error is that this scenario describes an **unapplied change**, not drift; drift specifically refers to when **real cloud infrastructure** diverges from the desired or tracked state — typically caused by manual out-of-band changes to the cloud, not unrun applies
- D) The error is that drift can only occur after `terraform destroy` has been run

**Answer**: C

**Explanation**:
Infrastructure drift is when the actual state of cloud resources diverges from what Terraform tracks — for example, a resource modified directly through the cloud console, resized by an autoscaler outside Terraform's control, or deleted manually. An engineer updating `.tf` files without running `terraform apply` has simply made an unapplied configuration change, not caused drift. The infrastructure hasn't changed — the configuration has. These are distinct scenarios that require different responses.

---

### Question 5 — Terraform and Ansible Both Called "Config Management"

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying the error in classifying Terraform as a configuration management tool

**Question**:
A job posting reads:

> "We use Terraform and Ansible as our configuration management stack. Both tools install and configure software on our servers."

What is the error in this job posting?

- A) There is no error — Terraform and Ansible both fall under the configuration management category
- B) The error is that Ansible is a provisioning tool, not a configuration management tool
- C) The error is that Terraform is a **provisioning** tool used to create and manage infrastructure — not a configuration management tool; Ansible is the configuration management tool that installs and configures software on existing servers
- D) The error is that Terraform and Ansible cannot be used together in the same toolchain

**Answer**: C

**Explanation**:
Terraform and Ansible serve complementary but distinct roles. Terraform is a **provisioning** tool — it creates, modifies, and destroys cloud infrastructure resources (VMs, networks, storage). Ansible is a **configuration management** tool — it installs packages, manages services, and configures software on existing servers. Calling Terraform a "configuration management" tool is incorrect and conflates two separate IaC categories. Both tools together form a complete infrastructure automation stack.

---

### Question 6 — CloudFormation Described as Multi-Cloud

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying the error in a claim that CloudFormation supports multi-cloud

**Question**:
An architect presents the following to their team:

> "We'll use AWS CloudFormation as our IaC tool because it supports multi-cloud — we can manage both our AWS RDS databases and our Azure blob storage containers from a single CloudFormation template."

What is the error in this statement?

- A) There is no error — CloudFormation supports Azure resources through an AWS-Azure partnership feature
- B) The error is that CloudFormation is AWS-only and cannot manage Azure resources; Terraform is the tool that supports managing resources across multiple cloud providers from a single configuration
- C) The error is that CloudFormation uses YAML/JSON templates which are less capable than HCL for multi-cloud scenarios
- D) The error is that Azure blob storage cannot be managed by any IaC tool

**Answer**: B

**Explanation**:
AWS CloudFormation is an AWS-native service that can only provision and manage AWS resources. It has no native capability to manage Azure, GCP, or other cloud provider resources. Terraform, by contrast, uses a provider plugin model that supports 3,000+ providers, enabling management of AWS and Azure (and many other) resources from a single root module configuration. An architect requiring multi-cloud IaC should evaluate Terraform, not CloudFormation.

---

### Question 7 — Two Incorrect Claims About IaC Benefits

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Identifying TWO incorrect statements from a list of IaC benefit claims

**Question**:
A team writes four statements about the benefits of IaC in their documentation. Which TWO statements contain errors? (Select two.)

- A) "IaC enables disaster recovery — if an environment is lost, it can be recreated by applying the configuration files"
- B) "IaC eliminates infrastructure drift entirely — once infrastructure is managed by Terraform, no drift can ever occur"
- C) "Storing Terraform configuration in Git provides a version-controlled audit trail of every infrastructure change"
- D) "IaC is slower than manual console provisioning because every change must go through code review and a CI/CD pipeline"

**Answer**: B, D

**Explanation**:
Option B is incorrect: IaC *detects and can remediate* drift, but it does not eliminate it. Drift still occurs whenever someone makes manual out-of-band changes to cloud resources. Option D is incorrect: IaC is faster than manual provisioning at scale — automated pipelines can provision dozens of resources in minutes, while manual console work is slow and error-prone. Options A and C are correct statements about IaC benefits.

---

### Question 8 — IaC Said to Remove the Need for Audit Trails

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying the error in claiming IaC removes the need for auditing

**Question**:
A compliance officer is told by a developer:

> "Now that we use Terraform, we don't need to worry about audit trails anymore — IaC tools handle everything automatically and there's nothing to track."

What is the error in this statement?

- A) There is no error — IaC tools built-in auditing eliminates the need for external audit mechanisms
- B) The error is that the statement inverts the relationship: IaC *creates* and *enhances* the audit trail — every infrastructure change committed to version control is traceable with author, timestamp, and message; IaC does not remove the need for auditing
- C) The error is that `terraform plan` creates the audit trail, not version control
- D) The error is that audit trails are only relevant for security resources, not infrastructure

**Answer**: B

**Explanation**:
One of IaC's primary benefits *is* audit trail creation. When configuration changes are committed to version control (Git), every infrastructure modification becomes a traceable record: who changed it, when, what changed, and why (via commit message). This is a significant improvement over ClickOps, which leaves no automatic version history. The developer's statement is backwards — IaC improves audit capability, it does not eliminate the concept of auditing.

---

### Question 9 — Two Incorrect Claims About Declarative vs Imperative

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Identifying TWO incorrect statements about declarative and imperative IaC approaches

**Question**:
Four statements are made about declarative and imperative IaC approaches. Which TWO contain errors? (Select two.)

- A) "In a declarative approach, you describe the desired end state and the tool determines how to reach it"
- B) "Ansible playbooks are primarily imperative — they define ordered tasks to execute on target systems"
- C) "Terraform is imperative because it ultimately makes sequential API calls to cloud providers to create resources"
- D) "The declarative model guarantees idempotency — re-applying the same desired state configuration produces no changes if the current state already matches"

**Answer**: C, D

**Explanation**:
Option C is incorrect: the fact that Terraform uses API calls internally does not make it imperative. The *programming model* is what determines declarative vs imperative. Terraform's model is declarative — you describe desired state in HCL and Terraform determines the steps. Executing API calls is the implementation detail, not the model. Option D is also incorrect as stated: while declarative tools are designed to be idempotent, the guarantee is specifically about repeated applies with an *unchanged* configuration — it does not mean the same config is always a no-op in every scenario (e.g., the first apply always creates resources). The statement's absolute phrasing ("guarantees") without that qualifier makes it misleading. Options A and B are correct statements.

---

### Question 10 — Current State Described as Configuration Files

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying the error in swapping "current state" and "desired state" definitions

**Question**:
A team's onboarding guide contains this paragraph:

> "In Terraform, current state refers to what your `.tf` files say the infrastructure should look like. Desired state is what actually exists in the cloud right now. Terraform's job is to make the desired state match the current state."

What is the error in this paragraph?

- A) There is no error — this correctly describes the relationship between current state and desired state
- B) The error is that `.tf` files represent **desired state**, not current state; what actually exists in the cloud (as tracked by the state file) is **current state**; Terraform reconciles by making current state match desired state — not the reverse
- C) The error is that the state file, not `.tf` files, represents desired state
- D) The error is minor — "current state" and "desired state" are interchangeable terms in Terraform documentation

**Answer**: B

**Explanation**:
The paragraph has the definitions completely swapped. **Desired state** is what `.tf` configuration files declare the infrastructure should look like. **Current state** is what actually exists in the cloud (tracked in the Terraform state file). Terraform's reconciliation direction is always: *bring current state toward desired state*. Making current state match desired state means creating, updating, or destroying resources until the cloud matches what the configuration declares. The paragraph's reversed definitions would cause serious conceptual confusion.

---

### Question 11 — Idempotency Applied to a First-Time Apply

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Identifying the error in applying idempotency to mean "zero changes always"

**Question**:
A developer argues:

> "Idempotency means `terraform apply` is always safe to run because it will never make any changes — it's idempotent, so the result is always zero resources added, zero changed, and zero destroyed."

What is the specific error in this reasoning?

- A) There is no error — an idempotent operation always produces zero changes
- B) The error is that idempotency means the same **end state** is produced when applying the same configuration multiple times — the first apply *does* create resources; subsequent applies against an unchanged configuration with unchanged infrastructure produce no changes; "zero changes" is only the outcome when the infrastructure already matches the desired state
- C) The error is that idempotency is a property of `terraform plan`, not `terraform apply`
- D) The error is that the developer used the word "safe" — idempotent operations can still be dangerous

**Answer**: B

**Explanation**:
Idempotency does not mean "no changes ever." It means applying the same input multiple times produces the same result — the same desired end state. The *first* `terraform apply` on a new configuration creates all declared resources (many adds, zero changes). A *subsequent* apply with the same unchanged configuration and unchanged infrastructure produces no changes. If the infrastructure has drifted (e.g., a resource was deleted manually), apply *will* make changes to restore the desired state. "Always zero changes" is a fundamental misunderstanding of idempotency.

---

### Question 12 — Two Errors in a Declarative IaC Description

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Identifying TWO incorrect claims in a developer's description of how declarative Terraform works

**Question**:
A developer writes a blog post about Terraform with these four claims. Which TWO contain errors? (Select two.)

- A) "When you write `resource \"aws_instance\" \"web\" { instance_type = \"t3.micro\" }`, you are describing what you want to exist — Terraform figures out how to create, update, or skip it based on current state"
- B) "Terraform's declarative model means you write step-by-step API calls in HCL that Terraform executes in order — like a shell script but with better syntax"
- C) "Because Terraform is declarative, running the same configuration against an already-provisioned environment makes no changes — the desired state is already satisfied"
- D) "Terraform is multi-cloud because its declarative model means the same HCL syntax works natively for all providers without any provider-specific blocks"

**Answer**: B, D

**Explanation**:
Option B is incorrect: HCL is not a sequence of API calls. Declarative HCL describes the desired end state — Terraform internally determines what API calls to make. Writing "step-by-step API calls in HCL" describes an imperative model, not a declarative one. Option D is incorrect: while Terraform supports multiple cloud providers, each provider requires its own `provider` block and uses provider-specific resource types (e.g., `aws_instance` vs `azurerm_virtual_machine`). The "same HCL works natively for all providers without provider-specific blocks" is false. Options A and C are correct descriptions of declarative Terraform behaviour.

---
