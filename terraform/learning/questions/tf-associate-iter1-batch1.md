# Terraform Associate (004) — Question Bank Iter 1 Batch 1

**Iteration**: 1
**Iteration Style**: Foundational recall — define terms, name commands, identify blocks
**Batch**: 1
**Objective**: 1 — IaC Concepts
**Generated**: 2026-04-25
**Total Questions**: 12
**Difficulty Split**: 2 Easy / 8 Medium / 2 Hard
**Answer Types**: 9 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — IaC Definition

**Difficulty**: Easy
**Answer Type**: one
**Topic**: What Infrastructure as Code is

**Question**:
Which of the following best defines Infrastructure as Code (IaC)?

- A) A method of provisioning cloud infrastructure exclusively through graphical web consoles
- B) The practice of managing and provisioning infrastructure through machine-readable configuration files instead of manual processes
- C) A scripting approach where an operator runs CLI commands in sequence to build servers
- D) A monitoring strategy that detects when infrastructure deviates from a known baseline

**Answer**: B

**Explanation**:
IaC is the practice of defining infrastructure — servers, networks, databases, and so on — in machine-readable configuration files rather than through manual console clicks or ad-hoc commands. Terraform is a leading IaC tool that uses HCL configuration files to represent the desired state of infrastructure.

---

### Question 2 — Desired State vs Current State

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Desired state and current state concepts

**Question**:
In the context of Terraform, what does "desired state" refer to?

- A) The actual infrastructure resources that exist in the cloud provider right now
- B) The previous version of infrastructure before the last `terraform apply`
- C) What your `.tf` configuration files say the infrastructure should look like
- D) The contents of the `terraform.tfstate` file after the last successful apply

**Answer**: C

**Explanation**:
Desired state is what your Terraform configuration files declare the infrastructure should look like. Current state is what actually exists in the cloud. Terraform's job is to reconcile the gap between the two by planning and applying the minimal set of changes needed.

---

### Question 3 — Idempotency

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Idempotency in IaC

**Question**:
A Terraform configuration that creates three EC2 instances is applied successfully. An operator runs `terraform apply` a second time against the same unchanged configuration. What is the expected result?

- A) Terraform creates three additional EC2 instances, resulting in six total
- B) Terraform destroys the existing instances and recreates them
- C) Terraform reports that no changes are needed and makes no modifications
- D) Terraform returns an error because the resources already exist

**Answer**: C

**Explanation**:
Idempotency means applying the same configuration multiple times produces the same outcome. When the current state already matches the desired state, Terraform detects no difference and reports "No changes. Your infrastructure matches the configuration." No resources are created, modified, or destroyed.

---

### Question 4 — Declarative vs Imperative

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Declarative approach in Terraform

**Question**:
Which statement correctly describes the declarative approach used by Terraform?

- A) You write step-by-step instructions telling Terraform exactly how to create each resource
- B) You describe the desired end state of your infrastructure and Terraform determines the steps required to reach it
- C) You provide Terraform with shell scripts that it executes in order on the target cloud provider
- D) You specify only the resources to delete; Terraform infers what should be created from provider defaults

**Answer**: B

**Explanation**:
Terraform is declarative — you describe *what* you want (e.g., "three EC2 instances of type t3.micro"), and Terraform calculates *how* to get there by comparing desired state to current state. This contrasts with imperative tools such as Ansible playbooks or shell scripts, where you specify the exact steps to execute.

---

### Question 5 — IaC Benefits

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Benefits of using IaC

**Question**:
Which TWO of the following are recognized benefits of managing infrastructure with IaC tools like Terraform? (Select two.)

- A) Infrastructure changes require no code review since they are applied automatically
- B) Every infrastructure change is captured as a version-controlled commit, providing a full audit trail
- C) Provider-specific web consoles become unnecessary and are disabled by the IaC tool
- D) Identical environments can be reproduced reliably from the same configuration code

**Answer**: B, D

**Explanation**:
Two core benefits of IaC are repeatability (the same code deploys identical environments every time) and auditability (every change is a commit, creating a traceable history). IaC does not disable provider consoles, and infrastructure-as-code changes should still go through review processes like pull requests.

---

### Question 6 — Multi-Cloud Support

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Terraform multi-cloud capability

**Question**:
Which of the following best describes Terraform's approach to managing resources across multiple cloud providers?

- A) Terraform requires a separate state file and CLI installation for each cloud provider
- B) Terraform can manage resources across AWS, Azure, GCP, and other providers within a single workflow using provider plugins
- C) Terraform delegates cross-cloud provisioning to AWS CloudFormation, which acts as a broker
- D) Terraform supports only one cloud provider per workspace; multi-cloud requires separate root modules that cannot share state

**Answer**: B

**Explanation**:
Terraform's provider plugin model allows a single Terraform configuration to declare resources across multiple cloud providers simultaneously. A single `.tf` file can contain `aws_`, `azurerm_`, and `google_` resources. This multi-cloud capability is a key differentiator from single-cloud tools such as CloudFormation (AWS only) and ARM templates (Azure only).

---

### Question 7 — IaC Tooling Landscape

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Terraform vs other IaC tools

**Question**:
An operator wants to provision infrastructure on both AWS and Azure from a single set of configuration files. Which tool is best suited for this requirement?

- A) AWS CloudFormation
- B) Azure Resource Manager (ARM) templates
- C) Terraform
- D) Google Cloud Deployment Manager

**Answer**: C

**Explanation**:
AWS CloudFormation manages only AWS resources, ARM templates manage only Azure resources, and Google Cloud Deployment Manager manages only GCP resources. Terraform is provider-agnostic and can manage resources across all three — and many other providers — using a single unified workflow.

---

### Question 8 — Drift Detection

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Infrastructure drift and IaC

**Question**:
What is "infrastructure drift" in the context of IaC?

- A) The gradual increase in cost of cloud resources over time
- B) A discrepancy between the infrastructure described in configuration files and the actual infrastructure that exists in the cloud
- C) The latency introduced when Terraform communicates with a remote cloud API
- D) A versioning conflict between two different Terraform providers

**Answer**: B

**Explanation**:
Infrastructure drift occurs when the actual state of cloud resources diverges from the desired state expressed in configuration files — for example, when someone manually modifies a resource through the cloud console. Terraform can detect drift by comparing its state file with the real infrastructure, and remediate it by re-applying the configuration.

---

### Question 9 — IaC vs Manual Provisioning

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Drawbacks of manual (ClickOps) provisioning

**Question**:
Which TWO of the following are drawbacks of manually provisioning infrastructure through a cloud provider's web console instead of using IaC? (Select two.)

- A) Web consoles require a paid subscription that IaC tools eliminate
- B) Manual changes leave no automatic version-controlled audit trail of who changed what and when
- C) Manually provisioned environments are difficult to reproduce identically, leading to environment inconsistencies
- D) Web consoles do not support creating virtual machines or networking resources

**Answer**: B, C

**Explanation**:
Manual provisioning (sometimes called ClickOps) produces no automatic version history, making it hard to track changes over time. It also makes reproducing environments reliably nearly impossible since there is no single source of truth. IaC solves both problems by capturing infrastructure as code committed to version control.

---

### Question 10 — Declarative vs Imperative Tools

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Classifying IaC tools by approach

**Question**:
A team currently uses Ansible playbooks to configure servers step by step and is evaluating Terraform as an addition to their toolchain. Which statement correctly describes how the two tools differ in their primary approach?

- A) Ansible is declarative; Terraform is imperative
- B) Both Ansible and Terraform are declarative; they differ only in the cloud providers they support
- C) Ansible playbooks are primarily imperative — specifying steps to execute; Terraform is declarative — specifying the desired end state
- D) Terraform is imperative for resource creation but declarative for resource deletion

**Answer**: C

**Explanation**:
Ansible playbooks define ordered tasks to execute (imperative), while Terraform configuration declares the desired end state and lets the tool determine the necessary steps (declarative). In practice, Ansible can behave in a declarative manner for some modules, but its primary model is task-based and imperative, making it complementary to Terraform rather than a replacement.

---

### Question 11 — IaC and Disaster Recovery

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Disaster recovery with IaC

**Question**:
A production environment is completely lost due to a datacenter failure. The team has their Terraform configuration files and a backup of the Terraform state stored in a remote backend. Which statement best describes how IaC helps in this scenario?

- A) IaC cannot help because the cloud resources themselves are gone and must be rebuilt manually
- B) The team can run `terraform apply` using the existing configuration to recreate the entire environment reproducibly, with the state backup used to avoid recreating already-restored resources
- C) The team must first run `terraform import` for every resource before any apply can proceed
- D) IaC helps only if the same Terraform version that created the original infrastructure is installed

**Answer**: B

**Explanation**:
One of IaC's most valuable properties is disaster recovery — the entire infrastructure environment is encoded in configuration files and can be recreated by running `terraform apply`. Having a remote state backup allows Terraform to track which resources have already been restored during incremental recovery. There is no requirement to import resources or match exact Terraform versions for a basic apply.

---

### Question 12 — IaC Audit Trail

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Version control and auditability of IaC

**Question**:
Which TWO of the following capabilities does storing Terraform configuration in a version control system (such as Git) provide? (Select two.)

- A) Automatic application of infrastructure changes whenever a commit is pushed, without any additional tooling
- B) A historical record of every infrastructure change, including who made it and when
- C) The ability to review proposed infrastructure changes through pull requests before they are applied
- D) Real-time synchronization of cloud resources with the repository without needing `terraform apply`

**Answer**: B, C

**Explanation**:
Version-controlling Terraform configuration provides a full audit trail (who changed what and when, via commit history) and enables collaborative review of infrastructure changes through pull requests before they reach production. Automatic application on commit requires additional CI/CD tooling and does not happen by default; real-time resource synchronization is not a Git or Terraform feature.

---
