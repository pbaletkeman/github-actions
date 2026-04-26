# Terraform Associate (004) — Question Bank Iter 6 Batch 1

**Iteration**: 6
**Iteration Style**: Scenario-based troubleshooting — an engineer encounters a problem or unexpected outcome; identify the most likely cause and correct resolution
**Batch**: 1
**Objective**: 1 — IaC Concepts
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 3 Easy / 8 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — Duplicate Resources From a Shell Script

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Understanding why an imperative provisioning script is not idempotent and how Terraform's declarative model avoids the same problem

**Question**:
An engineer has been provisioning S3 buckets by running a shell script that calls `aws s3api create-bucket --bucket my-data-bucket`. After running the script on Monday and again on Friday (forgetting the first run), a second bucket was attempted. The engineer asks: "Could I have avoided this by writing the bucket in a Terraform configuration instead?" What is the most accurate answer?

- A) No — Terraform would also attempt to create a second bucket because it has no memory of what was created before
- B) No — Terraform only manages EC2 and VPC resources natively; S3 requires a separate tool
- C) Yes — a Terraform resource block is **declarative**: on the second apply, Terraform compares desired state to current state, detects the bucket already exists, and makes no changes; the shell script is **imperative** and executes the create command unconditionally every time it is run
- D) Yes — but only if the engineer runs `terraform import` first to register the bucket with Terraform before either apply

**Answer**: C

**Explanation**:
The shell script is imperative — it issues a direct "create" command and has no awareness of whether the resource already exists. Running it twice attempts to create the resource twice. Terraform's declarative model operates differently: the configuration file describes *what should exist*, and Terraform queries the provider to determine whether it already does. If the S3 bucket is already present and matches the configuration, Terraform reports no changes needed. This idempotent behaviour — applying the same configuration N times always yields the same outcome — is one of the central reasons IaC tools like Terraform are preferred over ad-hoc scripting for infrastructure management.

---

### Question 2 — Manual Console Change Reverted by Terraform

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Understanding that Terraform enforces desired state and will revert manual changes on the next apply

**Question**:
An engineer manually changes an EC2 instance's instance type from `t3.micro` to `t3.large` using the AWS console. The Terraform configuration still declares `instance_type = "t3.micro"`. A colleague then runs `terraform apply`. The engineer is surprised to see the instance type change back to `t3.micro`. What explains this behaviour?

- A) Terraform detected the console change as a security violation and triggered an automatic rollback
- B) Terraform uses the **configuration file as the source of desired state** — the configuration still declares `t3.micro`, so Terraform plans and applies a change to bring the current state back in line with the desired state, effectively reverting the manual change
- C) Running `terraform apply` always rolls back the last 24 hours of changes as a safety measure
- D) The colleague accidentally ran `terraform destroy` and `terraform apply` in sequence

**Answer**: B

**Explanation**:
This is drift remediation in action. During `terraform plan` (which runs at the start of `terraform apply`), Terraform refreshes the current state by querying the AWS API and discovers the instance type is now `t3.large`. The configuration, however, declares `t3.micro`. Because the configuration is the authoritative desired state, Terraform plans to change the instance type back to `t3.micro` and applies it. This behaviour is intentional and desirable in a managed environment — the configuration is the single source of truth, and manual console changes will be overwritten on the next apply. Teams that use Terraform should commit to the discipline of making all infrastructure changes through configuration, not the console.

---

### Question 3 — Engineer Confused by "No Changes" Message

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Recognising that "No changes" is the correct idempotent outcome — not an error or a skipped step

**Question**:
A junior engineer runs `terraform apply` and receives the message: `No changes. Your infrastructure matches the configuration.` They open a support ticket reporting: "Terraform is broken — it skipped creating my resources." The configuration was applied successfully the previous day by another engineer. What should the support engineer explain?

- A) The junior engineer is correct — `No changes` indicates Terraform encountered an error and aborted before creating any resources
- B) Terraform requires a `--force` flag on subsequent applies to re-create resources that already exist
- C) "No changes" is the **expected, correct outcome** of running `terraform apply` when the current infrastructure already matches the configuration — Terraform detected that all declared resources already exist in the correct state and correctly took no action; this is called idempotency
- D) The message indicates the state file is corrupted — the engineer should delete `terraform.tfstate` and re-apply

**Answer**: C

**Explanation**:
`No changes. Your infrastructure matches the configuration.` is one of the most important messages in Terraform — it means everything is working exactly as designed. Terraform compared the desired state (configuration files) to the current state (state file, validated against the cloud provider) and found no differences. All declared resources already exist with the correct attributes. This idempotent property is fundamental to IaC: you can safely run `terraform apply` at any time and it will only make changes if the infrastructure has actually diverged from the configuration. The junior engineer's confusion is common among those new to declarative tools — they expect "apply" to always *do* something.

---

### Question 4 — Direct State File Edit

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Understanding why directly editing terraform.tfstate is dangerous and what the file's actual role is

**Question**:
An engineer wants to "manually add" a resource that was created outside of Terraform. They open `terraform.tfstate` in a text editor, copy an existing resource block, and paste a modified version with the new resource's attributes. They then run `terraform plan`. What is the most likely problem with this approach?

- A) There is no problem — editing the state file directly is the recommended way to onboard manually created resources
- B) The state file uses a format that Terraform regenerates automatically; any manual edits will be ignored on the next `terraform init`
- C) Editing the state file directly is **fragile and unsupported** — state is in a structured JSON format with checksums and schema versioning; an incorrect edit can corrupt state, causing Terraform to misidentify resources, plan unnecessary destroys, or fail entirely; the correct approach is to use the `import` block or `terraform import` CLI to bring the resource under management properly
- D) Terraform will accept the manual state edit but will immediately destroy the resource because there is no matching resource block in the `.tf` configuration files

**Answer**: C

**Explanation**:
`terraform.tfstate` is a structured JSON file that Terraform manages internally — it is not intended to be hand-edited. It contains checksums, serial numbers, schema versions, and provider metadata that must be consistent. A manual edit that misses any of these details can corrupt the file, potentially causing Terraform to plan destructive operations or fail to run at all. More importantly, the state file is not the *configuration* — adding an entry to state without a corresponding resource block in a `.tf` file means Terraform will see the state entry and either ignore it or (depending on the Terraform version and configuration) plan to remove it. The correct path for onboarding an existing resource is `terraform import` (CLI) or the declarative `import` block (Terraform 1.5+), which both validate the resource against provider schema and add it to state safely.

---

### Question 5 — Resource Deleted From Config Gets Planned for Destruction

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Understanding that Terraform owns resources it manages and plans to destroy them when removed from configuration

**Question**:
An engineer removes a `resource "aws_security_group" "allow_ssh"` block from their Terraform configuration. They did not intend to delete the security group — they just wanted to "stop managing it with Terraform." When they run `terraform plan`, they see `1 to destroy`. What explains this, and what should the engineer do instead?

- A) `terraform plan` is wrong — removing a block from configuration never causes a destroy in Terraform; the engineer should open a bug report
- B) Removing a resource block from configuration signals to Terraform that the resource should no longer exist — Terraform plans to destroy it; to **stop managing a resource without destroying it**, the engineer should instead run `terraform state rm aws_security_group.allow_ssh` to remove it from state, after which Terraform will no longer track it and the cloud resource will remain untouched
- C) The engineer should run `terraform apply -target=aws_security_group.allow_ssh` to skip the security group during apply
- D) Removing a resource block only causes a destroy if `prevent_destroy = false` is set in the lifecycle block; without a lifecycle block, removal is safe

**Answer**: B

**Explanation**:
When a resource block exists in state but is absent from the configuration, Terraform interprets this as intent to remove the resource — the desired state no longer includes it, so Terraform plans to destroy it. This is the correct behaviour for the common case (you genuinely want to delete the infrastructure). However, when the goal is to "hand off" management — keep the resource running but stop Terraform from tracking it — the right tool is `terraform state rm <address>`. This removes the resource from Terraform's state without touching the cloud resource. After this, the resource becomes unmanaged drift: Terraform no longer knows about it and will not create, update, or destroy it. The engineer should also remove the resource block from the configuration (or leave it out) to avoid Terraform trying to create a new one.

---

### Question 6 — Staging Drifted from Production

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying which IaC benefit directly addresses environment drift between staging and production

**Question**:
A company has a production environment and a staging environment. Both were originally set up manually via the AWS console. Over six months, engineers have made small ad-hoc changes to each independently. The production team is now seeing bugs that cannot be reproduced in staging because the two environments have diverged significantly. Which IaC benefit most directly addresses this class of problem?

- A) **Speed** — IaC tools provision resources faster than manual console clicks, so environments can be rebuilt more quickly
- B) **Audit trail** — storing infrastructure in version control lets engineers see a history of every change
- C) **Repeatability** — the same Terraform configuration applied to both environments produces identical infrastructure in each; any manual change that drifts an environment from the configuration will be detected and corrected on the next apply, preventing long-term divergence
- D) **Cost estimation** — IaC tools calculate the cost difference between environments so discrepancies can be identified

**Answer**: C

**Explanation**:
The root cause of the staging/production divergence is that neither environment has a shared, version-controlled definition of its desired state — each was built and modified manually and independently. **Repeatability** is the IaC benefit that solves this: a single Terraform configuration applied against both environments (with environment-specific variables for names, sizing, etc.) ensures both are built from the same specification. Any manual drift is detected on the next `terraform plan` and corrected on `terraform apply`. While an audit trail (B) would help trace *how* the divergence occurred, it does not prevent it. Repeatability — the ability to reproducibly create identical environments from code — is the fundamental fix.

---

### Question 7 — Plan Output Shows "1 to add" but the Resource Is in the Config

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Understanding why terraform plan shows a resource as "to add" even though it is declared in configuration

**Question**:
A Terraform configuration declares an `aws_instance` resource. The engineer runs `terraform plan` and sees `Plan: 1 to add, 0 to change, 0 to destroy.` They are confused — the instance already exists in AWS, they can see it in the console. What is the most likely explanation?

- A) Terraform always plans to add all declared resources on the first plan — the `1 to add` is a display artefact and no resource will actually be created on apply
- B) The instance exists in AWS but has **not been imported into Terraform state** — Terraform has no record of it in state, so from Terraform's perspective the desired state (the resource block) is unfulfilled; Terraform will create a second new instance unless the existing instance is first imported
- C) The `aws_instance` resource type is deprecated and Terraform is planning to recreate it using an updated resource type
- D) This means the state file is locked — Terraform is re-planning an earlier failed apply

**Answer**: B

**Explanation**:
Terraform's planning is based on its **state file**, not direct cloud observation alone. When an instance exists in AWS but has never been managed by Terraform (no entry in state), Terraform has no knowledge of it. The configuration declares it should exist, state says it doesn't, so Terraform plans to create it. Running `terraform apply` would create a second, duplicate instance alongside the existing one. To correct this, the engineer should import the existing instance using the `import` block or `terraform import aws_instance.<name> <instance-id>`, which adds the instance to state and allows Terraform to manage it going forward. After a successful import, `terraform plan` should show no changes if the configuration attributes match the actual instance.

---

### Question 8 — Colleague Claims CloudFormation Is Better for Multi-Cloud

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Recognising Terraform's multi-cloud advantage over provider-specific IaC tools

**Question**:
An engineer proposes using AWS CloudFormation to manage all company infrastructure because it is a "mature, native AWS service." A colleague argues that Terraform would be a better choice given that the company also has workloads on Azure and GCP. What is the most accurate reason to favour Terraform in this scenario?

- A) Terraform is faster than CloudFormation because it uses a compiled language under the hood, while CloudFormation uses interpreted JSON
- B) **CloudFormation is AWS-only** — it cannot provision Azure or GCP resources; Terraform uses provider plugins and a single unified workflow to manage resources across AWS, Azure, GCP, and many other platforms, making it the natural choice for organisations with multi-cloud or hybrid-cloud infrastructure
- C) Terraform replaces CloudFormation entirely — AWS itself recommends migrating all existing CloudFormation stacks to Terraform
- D) CloudFormation requires separate credentials for each AWS region; Terraform uses a single credential set for all regions and all cloud providers

**Answer**: B

**Explanation**:
CloudFormation is a highly capable IaC tool but is architecturally scoped to AWS. It cannot provision Azure virtual machines, GCP storage buckets, or resources in any non-AWS provider. Terraform's plugin-based architecture means providers exist for AWS, Azure, GCP, Kubernetes, Datadog, GitHub, and hundreds of other platforms — all managed through the same HCL syntax and workflow. A company with workloads on AWS and Azure would need two separate tools (CloudFormation + ARM/Bicep) if they chose provider-native tools, while Terraform provides a single unified workflow. This is one of Terraform's defining advantages. The same applies to Azure Bicep/ARM (Azure-only) — each provider-native tool is limited to its cloud.

---

### Question 9 — Three Benefits of Migrating From ClickOps to IaC

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Identifying valid IaC benefits over manual console-based provisioning

**Question**:
A company is evaluating a migration from "ClickOps" (provisioning via cloud console) to Terraform-based IaC. Which THREE of the following are genuine benefits they should cite in their proposal? (Select three.)

- A) **Repeatability** — the same Terraform configuration can be applied to create identical staging, production, and disaster-recovery environments without manual steps, eliminating environment drift
- B) **Audit trail** — every infrastructure change is a version-controlled commit with author, timestamp, and diff, enabling full change history and compliance traceability
- C) **Guaranteed zero downtime** — Terraform performs all changes in a blue-green pattern that ensures no service interruption during apply
- D) **Disaster recovery** — if an environment is destroyed, it can be recreated from the configuration files in minutes, without manually remembering every resource and setting
- E) **Elimination of all cloud provider costs** — IaC tools negotiate lower cloud rates by batching API calls to the provider

**Answer**: A, B, D

**Explanation**:
(A) Repeatability is a core IaC benefit — the same code produces the same infrastructure every time, across any environment. (B) Version-controlled commits are the IaC answer to "who changed what and when?" — something ClickOps cannot provide. (D) Disaster recovery is dramatically improved by IaC: an entire environment can be reconstructed from configuration files rather than reconstructed from memory and scattered documentation. (C) is incorrect — Terraform has no zero-downtime guarantee; some resource changes (e.g., replacing a database instance) cause downtime, and blue-green deployments must be designed explicitly. (E) is entirely false — Terraform is an orchestration tool with no influence over cloud pricing.

---

### Question 10 — Engineer Confused by "1 to change" vs "1 to add" in Plan

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Understanding what terraform plan's "to add", "to change", and "to destroy" counts mean for in-place vs replace operations

**Question**:
An engineer modifies an existing EC2 instance's `tags` attribute in the configuration and runs `terraform plan`. The plan shows `Plan: 0 to add, 1 to change, 0 to destroy.` The engineer expected to see `1 to add` because they were "adding a tag." What is the correct interpretation of `1 to change`?

- A) `1 to change` means Terraform will destroy the instance and create a new one with the updated tags
- B) `1 to change` means the **existing resource will be updated in place** — Terraform will call the provider API to modify the tag on the running instance without destroying or recreating it; this is an in-place update; `1 to add` would mean a brand new resource would be created that does not currently exist in state
- C) `1 to change` means the change is optional — the engineer can skip it by running `terraform apply -skip-changes`
- D) `1 to change` is shown only when `count` or `for_each` is used; for single resources, the plan always shows `1 to add` regardless of whether the resource exists

**Answer**: B

**Explanation**:
Terraform's plan output uses three categories that directly map to API operations. `to add` (or `to create`) means a resource does not exist and will be created. `to change` (or `to update`) means the resource exists and one or more of its attributes will be modified in place — the resource continues to exist throughout. `to destroy` means the resource will be deleted. The engineer was confused because "adding a tag" feels like creating something new, but from Terraform's perspective the *resource* (the EC2 instance) already exists and is simply being modified. The tag update is an attribute change, not a new resource — hence `1 to change`. A fourth category, `(replace)`, appears when a required attribute change forces destruction and recreation of the resource.

---

### Question 11 — Why Does Plan Re-Run on `terraform apply`?

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Understanding that terraform apply always performs a fresh plan to account for changes that may have occurred since the last plan

**Question**:
An engineer runs `terraform plan` and reviews the output carefully. They then run `terraform apply` immediately after. To their surprise, Terraform runs a planning phase again before asking for confirmation. The engineer asks: "Why is Terraform planning again? I just ran plan!" What is the correct explanation?

- A) This is a bug — `terraform apply` should use the output from the previous `plan` command without re-running
- B) `terraform apply` **always performs a fresh plan** before applying because infrastructure may have changed in the time between `terraform plan` and `terraform apply` — a colleague could have applied changes, a resource could have been manually modified, or a plan file could have been applied elsewhere; the fresh plan ensures Terraform acts on the actual current state, not a potentially stale snapshot
- C) The plan phase runs again only when the configuration files have been modified since the last `terraform plan`
- D) Terraform re-plans because `terraform plan` does not save its results — each command is stateless

**Answer**: B

**Explanation**:
By default, `terraform apply` always runs a fresh plan immediately before applying. The reason is safety: the state of cloud infrastructure can change between the time `terraform plan` was run and the time `terraform apply` executes. A teammate could have run their own apply, a resource could have been manually modified, or an automated process could have changed something. If Terraform used a stale plan from minutes (or hours) ago, it might apply changes that are no longer appropriate. To skip the re-plan and use an exact saved plan, the engineer should run `terraform plan -out=plan.tfplan` to save the plan to a file, then `terraform apply plan.tfplan` to apply that specific plan without re-planning. This two-step pattern is also recommended in CI/CD pipelines for deterministic applies.

---

### Question 12 — Renamed Resource Block Causes Destroy and Recreate

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Understanding that renaming a resource label causes plan to show destroy + create, and knowing TWO ways to handle this correctly

**Question**:
An engineer renames a resource block in their configuration from `resource "aws_instance" "web"` to `resource "aws_instance" "web_server"`. When they run `terraform plan`, they see `Plan: 1 to add, 0 to change, 1 to destroy.` The engineer does not want to destroy and recreate the instance. Which TWO approaches correctly resolve this? (Select two.)

- A) Run `terraform state mv aws_instance.web aws_instance.web_server` **before** running `terraform apply` — this updates the state entry to reference the new name, so Terraform sees the renamed block as the same resource and plans zero changes instead of destroy + create
- B) Add a `moved` block to the configuration declaring that `aws_instance.web` has moved to `aws_instance.web_server` — Terraform 1.1+ supports `moved` blocks as a declarative way to inform Terraform of refactors, causing it to update state during apply without destroying the resource
- C) Run `terraform apply -target=aws_instance.web_server` to create the new resource first, then manually delete the old one from the console
- D) Add `prevent_destroy = true` to the lifecycle block of `aws_instance.web_server` — this prevents the destroy and causes Terraform to skip the create as well

**Answer**: A, B

**Explanation**:
When a resource block is renamed, Terraform sees a new address (`aws_instance.web_server`) with no state entry and an old address (`aws_instance.web`) in state with no configuration block. This is interpreted as "destroy the old, create the new." To avoid destroy + recreate: **(A) `terraform state mv`** imperatively updates the state file to use the new address before apply runs — when Terraform then plans, it sees both the configuration block and state entry under `aws_instance.web_server` and recognises them as the same resource, planning no changes. This is the pre-1.1 approach and still valid. **(B) `moved` block** is the declarative equivalent introduced in Terraform 1.1: you add a `moved { from = aws_instance.web; to = aws_instance.web_server }` block to the configuration, and Terraform processes the refactor during the next apply, updating state without any destroy/create. After the moved block has been applied, it can be removed. (C) is a manual workaround that would still cause downtime. (D) is incorrect — `prevent_destroy` prevents destruction but does not suppress the create; it would cause the apply to error.

---

### Question 13 — IaC as Living Documentation

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Understanding the documentation benefit of IaC and the limits of that claim in a scenario where config and reality have diverged

**Question**:
A new engineer joins a team and is told: "Our Terraform configuration files are our infrastructure documentation — you can read the `.tf` files to understand exactly what we have deployed." Six months later, the engineer discovers that the actual cloud environment contains several resources not present in the configuration. What is the most precise statement about the "living documentation" claim of IaC, and what condition is required for it to remain true?

- A) The claim is always false — configuration files describe intent, not actual infrastructure; real documentation must be maintained separately
- B) Terraform configuration files are accurate documentation **only for the resources Terraform manages** — the claim holds when all infrastructure changes flow through Terraform with no manual console changes, no resources created outside Terraform, and no state manipulation that removes resources from tracking; when engineers bypass Terraform (ClickOps, other tools, manual `terraform state rm`), the configuration no longer reflects reality and the documentation value is lost
- C) The claim is fully accurate regardless of manual changes — Terraform automatically discovers all cloud resources during `terraform plan` and adds them to the configuration
- D) Configuration files are only reliable documentation after running `terraform refresh` — between refreshes, they may be outdated

**Answer**: B

**Explanation**:
"Infrastructure as living documentation" is a genuine and significant benefit of IaC — but it is **conditional**, not automatic. The configuration accurately documents the infrastructure exactly as long as Terraform is the *exclusive* method of change. The moment an engineer creates a resource via the console, modifies one manually, or uses another provisioning tool, the configuration and reality diverge. Terraform cannot retroactively discover and add unmanaged resources to configuration files (it can detect drift for *managed* resources, but cannot auto-import new ones). Maintaining the documentation value requires organisational discipline: all infrastructure changes go through version-controlled Terraform configuration, pull request review, and `terraform apply`. The scenario describes a team that violated this discipline, which is a common real-world challenge when adopting IaC in an existing environment.

---
