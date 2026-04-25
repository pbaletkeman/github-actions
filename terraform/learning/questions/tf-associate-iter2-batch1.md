# Terraform Associate (004) — Question Bank Iter 2 Batch 1

**Iteration**: 2
**Iteration Style**: Behavioral prediction — what happens when code runs or a scenario occurs
**Batch**: 1
**Objective**: 1 — IaC Concepts
**Generated**: 2026-04-25
**Total Questions**: 12
**Difficulty Split**: 2 Easy / 8 Medium / 2 Hard
**Answer Types**: 9 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — Second Apply with Unchanged Config

**Difficulty**: Easy
**Answer Type**: one
**Topic**: What Terraform reports when infrastructure already matches the configuration

**Question**:
A Terraform configuration that provisions three EC2 instances is applied successfully. No changes are made to the configuration files or the infrastructure. `terraform apply` is run again. What does Terraform report?

- A) `Error: 3 resources already exist. Run terraform import to manage them.`
- B) `Apply complete! Resources: 3 added, 0 changed, 0 destroyed.`
- C) `No changes. Your infrastructure matches the configuration.`
- D) `Warning: State drift detected. Run terraform refresh before applying.`

**Answer**: C

**Explanation**:
When Terraform compares the desired state (configuration files) to the current state (state file reflecting actual infrastructure) and finds no difference, it reports that no changes are needed. This idempotent behaviour — running the same configuration N times produces the same result — is fundamental to how declarative IaC tools operate. No resources are created, modified, or destroyed on the second run.

---

### Question 2 — Manual Deletion Followed by Plan

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Terraform's response when a resource is manually deleted from the cloud

**Question**:
A Terraform configuration declares three EC2 instances. All three are running. An administrator manually terminates one of the instances through the AWS console. A team member then runs `terraform plan`. What does Terraform propose?

- A) Terraform detects no changes because it only checks the state file, not the actual cloud
- B) Terraform proposes creating one EC2 instance to restore the desired count of three
- C) Terraform proposes destroying the remaining two instances to match the deleted resource
- D) Terraform returns an error because the state file is now out of sync

**Answer**: B

**Explanation**:
Terraform queries the cloud provider's API during `terraform plan` to refresh the real state of resources. When it detects that one instance no longer exists but the configuration still declares three, it proposes creating one new instance to reconcile the difference. This is drift detection in action — the current state (two instances) diverges from the desired state (three), and Terraform resolves it by planning the missing resource.

---

### Question 3 — Reducing Instance Count

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What happens when a declarative config reduces a resource count

**Question**:
A configuration currently manages five EC2 instances (`count = 5`). A developer updates the configuration to `count = 3` and runs `terraform apply`. What happens to the extra two instances?

- A) The two extra instances are left running; Terraform only creates new resources, never destroys
- B) Terraform returns an error because reducing count requires manual removal first
- C) Terraform destroys the two instances that are no longer described by the desired state
- D) Terraform marks the two instances as tainted so they are replaced on the next apply

**Answer**: C

**Explanation**:
In a declarative model, the configuration file represents the complete desired state. When `count` is reduced from 5 to 3, the desired state no longer includes the extra two instances, so Terraform plans to destroy them during apply. This is a key behavioural characteristic of declarative tools — you describe what you want to exist, and Terraform reconciles by both creating missing resources and destroying resources that should no longer exist.

---

### Question 4 — Increasing Instance Count

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What terraform plan shows when count is increased

**Question**:
A configuration currently manages two running EC2 instances (`count = 2`). A developer changes the configuration to `count = 5` and runs `terraform plan`. What does the plan output show?

- A) `Plan: 5 to add, 0 to change, 0 to destroy.` — Terraform creates all five from scratch
- B) `Plan: 3 to add, 0 to change, 0 to destroy.` — Terraform creates only the three additional instances
- C) `Plan: 2 to destroy, 5 to add` — Terraform destroys and recreates all instances
- D) `Error: count cannot be increased after initial apply`

**Answer**: B

**Explanation**:
Terraform compares the desired state (5 instances) to the current state (2 instances) and plans only the difference — creating the 3 additional instances needed to reach the desired count. The 2 existing instances remain unchanged. This minimal-change behaviour is a fundamental property of declarative IaC: Terraform plans only what is necessary, not a full rebuild.

---

### Question 5 — Out-of-Band Tag Change

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What happens when a manual change is made outside Terraform then apply runs

**Question**:
A Terraform configuration provisions an EC2 instance with the tag `Environment = "production"`. A cloud administrator manually changes the tag to `Environment = "legacy"` using the AWS console. A developer then runs `terraform apply`. What happens?

- A) Terraform ignores tag changes because tags are not tracked in state
- B) Terraform detects the drift and overwrites the tag back to `Environment = "production"` to match the desired state
- C) Terraform leaves the tag as `Environment = "legacy"` and updates the state file to record the new value
- D) Terraform destroys and recreates the instance to restore the original tag value

**Answer**: B

**Explanation**:
Terraform tracks all resource attributes — including tags — in its state file. During plan and apply, it refreshes the real resource state from the provider API and compares it to the desired state in configuration. Because the configuration still declares `Environment = "production"`, Terraform proposes an in-place update to overwrite the manually changed tag. The instance is not destroyed — only the tag attribute is updated.

---

### Question 6 — Terraform vs Ansible Idempotency

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Behavioural differences between Terraform (declarative) and Ansible (imperative) on repeated runs

**Question**:
A team uses Terraform to provision a server and also has an Ansible playbook that runs shell commands to provision an equivalent server. They run each tool a second time against the same environment. Which TWO statements correctly describe the difference in behaviour? (Select two.)

- A) Terraform reports no changes on the second run because the current state already matches the desired state
- B) The Ansible playbook may attempt to re-execute commands regardless of whether they have already been applied, depending on how the playbook is written
- C) Both Terraform and a typical imperative Ansible playbook behave identically — neither makes changes on the second run
- D) Terraform destroys and recreates the server on every run to ensure a fresh state

**Answer**: A, B

**Explanation**:
Terraform is declarative and idempotent by design — a second run with no configuration changes produces no actions, because the state already matches the desired state. An imperative Ansible playbook defines ordered tasks; without explicit idempotency guards, re-running it may re-execute commands (such as `apt install` or `systemctl restart`) even if they are already in effect. Ansible modules themselves can be idempotent, but the overall playbook behaviour depends on how it is written, unlike Terraform's built-in declarative idempotency.

---

### Question 7 — Console Change and Version History

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Audit trail behaviour when infrastructure is changed outside of code

**Question**:
An engineer modifies an RDS instance's storage size directly through the AWS console. The Terraform configuration file is never updated. Six months later, the team tries to audit what changed and when. What is the outcome?

- A) The change is captured automatically in Terraform's version history within the state file
- B) There is no record of the change in version control because it was not made through code — the audit trail has a gap
- C) AWS CloudTrail records the change, which Terraform automatically imports into its audit log
- D) The change appears in the Terraform plan output as a historical entry

**Answer**: B

**Explanation**:
One of the core benefits of IaC is that every infrastructure change becomes a version-controlled commit. When a change is made through the cloud console instead of updating the configuration and running Terraform, it bypasses the IaC audit trail entirely. There is no record in version control of who made the change, when, or why. This is one of the key drawbacks of mixing ClickOps with IaC workflows.

---

### Question 8 — Multi-Cloud Resources in One Config

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What happens when a config declares resources from multiple cloud providers

**Question**:
A Terraform configuration declares an `aws_s3_bucket` resource and an `azurerm_storage_account` resource in the same root module. The appropriate provider blocks for both AWS and Azure are configured. What happens when `terraform apply` runs?

- A) Terraform returns an error because only one cloud provider is allowed per configuration
- B) Terraform applies the AWS resource first, waits for confirmation, then applies the Azure resource separately
- C) Terraform provisions both resources using their respective provider plugins as part of a single apply operation
- D) Terraform applies only the AWS resource; Azure resources require a separate workspace

**Answer**: C

**Explanation**:
Terraform's provider plugin model supports multiple providers in a single configuration. During `terraform apply`, Terraform uses the appropriate provider plugin for each resource — AWS plugin for `aws_s3_bucket` and Azure plugin for `azurerm_storage_account` — and provisions them concurrently where there are no dependencies. This multi-cloud capability within a single workflow is a primary advantage over provider-specific tools.

---

### Question 9 — Disaster Recovery with Config and State

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What apply produces when recreating a lost environment from code and state backup

**Question**:
A cloud environment is completely destroyed by an incident. The team has their Terraform configuration files in a git repository and a recent backup of their state file. They restore the state file to their backend and run `terraform apply`. What does Terraform do?

- A) Terraform does nothing because the state file records resources as existing, even though they are gone
- B) Terraform detects that every resource recorded in state no longer exists in the cloud and proposes recreating the entire environment
- C) Terraform requires `terraform import` for every resource before any apply can succeed
- D) Terraform can only recreate resources created in the last 24 hours due to provider API limitations

**Answer**: B

**Explanation**:
During plan, Terraform refreshes the real state of each resource recorded in state by querying the provider API. If a resource no longer exists, Terraform marks it as needing creation. With a state backup, Terraform knows exactly which resources should exist and plans to recreate them all, producing an output such as `Plan: 47 to add, 0 to change, 0 to destroy`. This demonstrates IaC's disaster recovery benefit — the entire environment can be recreated from code.

---

### Question 10 — Zero Count Destroys All

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Behavioural prediction when count is set to zero and apply runs

**Question**:
A configuration currently manages four EC2 instances. A developer updates the configuration to `count = 0` and runs `terraform apply`. Which TWO statements correctly describe what happens? (Select two.)

- A) Terraform proposes destroying all four existing EC2 instances during the plan phase
- B) Terraform reports `Plan: 0 to add, 0 to change, 4 to destroy.` and prompts for confirmation before proceeding
- C) Terraform ignores the `count = 0` change and retains the existing four instances as unmanaged orphans
- D) Terraform creates zero instances without touching the existing four, because create and destroy are separate operations

**Answer**: A, B

**Explanation**:
Setting `count = 0` tells Terraform the desired state is zero instances of that resource. During plan, Terraform detects that four instances currently exist and proposes destroying all four. The plan output clearly shows `4 to destroy`, and Terraform prompts for interactive confirmation before applying the destroy (unless `-auto-approve` is passed). The declarative model means Terraform always reconciles to the desired state — including removing resources entirely.

---

### Question 11 — Applying Same Config to Three Environments

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Reproducibility across environments using identical IaC configuration

**Question**:
A team applies the exact same Terraform configuration to three separate workspaces representing development, staging, and production (each with its own state file and isolated cloud accounts). No manual changes are made in any environment. What is the expected state of each environment after apply?

- A) Each environment will differ because cloud providers assign different resource IDs, causing configuration drift
- B) Each environment will have identical infrastructure topology, resource types, and configuration — resource IDs differ but the structure is the same
- C) The production environment will differ because Terraform applies additional safety rules automatically in accounts tagged as production
- D) The staging and production environments will match, but development will differ because Terraform uses lower-cost defaults for development workspaces

**Answer**: B

**Explanation**:
Repeatability is a core IaC benefit — applying the same configuration to isolated environments produces identical infrastructure topology and configuration in each. Resource identifiers (such as instance IDs and ARNs) are assigned by the cloud provider and will differ, but the structure, resource types, counts, and settings will be identical. Terraform does not automatically apply environment-specific behaviour unless it is explicitly encoded in the configuration (e.g., through workspace variables or conditional expressions).

---

### Question 12 — Switching from ClickOps to IaC

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Behavioural change when a team adopts IaC after managing infrastructure manually

**Question**:
A team has been managing cloud infrastructure entirely through the AWS console (ClickOps) for two years. They decide to adopt Terraform. During the transition, an engineer imports all existing resources into Terraform state and creates matching configuration files. A second engineer later makes a change to a resource through the AWS console — not through Terraform. The first engineer then runs `terraform plan`. What does Terraform most likely report?

- A) No changes — Terraform only tracks resources that it originally created, not imported resources
- B) The console change is detected as drift, and Terraform proposes reverting the resource to match the configuration
- C) Terraform deletes all resources not originally created by Terraform, including the manually changed one
- D) Terraform updates its state file to match the console change and reports the configuration as out of date

**Answer**: B

**Explanation**:
Once resources are imported into Terraform state with corresponding configuration files, Terraform manages them like any other resource. When `terraform plan` runs, it refreshes the real resource state from the provider API and compares it to the configuration. The console change creates drift — a discrepancy between the desired state (configuration) and the current state (what exists in the cloud) — and Terraform reports it as a proposed change to revert. This demonstrates why mixing ClickOps with IaC creates ongoing reconciliation conflicts.

---
