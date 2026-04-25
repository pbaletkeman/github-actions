# Terraform Associate (004) — Question Bank Iter 1 Batch 4

**Iteration**: 1
**Iteration Style**: Foundational recall — define terms, name commands, identify blocks
**Batch**: 4
**Objective**: 4a — Resources, Data & Dependencies
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 2 Easy / 9 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — Resource Block Syntax

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Resource block syntax and components

**Question**:
Which of the following correctly shows the required structure of a Terraform resource block?

- A) `resource { type = "aws_instance" name = "web" }`
- B) `resource "aws_instance" "web" { ... }`
- C) `resource aws_instance web { ... }`
- D) `provider "aws_instance" "web" { ... }`

**Answer**: B

**Explanation**:
A resource block requires two labels after the `resource` keyword: the provider type (e.g., `aws_instance`) and the local name (e.g., `web`), both in double quotes, followed by a configuration block in `{ }`. The local name is used to reference this resource elsewhere in the configuration as `aws_instance.web`.

---

### Question 2 — Data Source Purpose

**Difficulty**: Easy
**Answer Type**: one
**Topic**: What a data source does

**Question**:
What is the purpose of a `data` block in a Terraform configuration?

- A) To define a new cloud resource that Terraform will create and manage
- B) To query an existing infrastructure object in read-only mode without creating or managing it
- C) To store sensitive values such as passwords and API keys securely
- D) To declare default values for input variables used across modules

**Answer**: B

**Explanation**:
A `data` block (data source) queries existing infrastructure in read-only mode. It retrieves information about a pre-existing resource — such as an AMI ID or a VPC ID — that Terraform did not create. Data sources never create, update, or destroy resources; they only read and expose attributes for use in the configuration.

---

### Question 3 — Data Source Reference Format

**Difficulty**: Medium
**Answer Type**: one
**Topic**: How to reference a data source attribute

**Question**:
A data source is declared as `data "aws_ami" "ubuntu" { ... }`. What is the correct syntax to reference its `id` attribute in a resource block?

- A) `aws_ami.ubuntu.id`
- B) `data.ubuntu.aws_ami.id`
- C) `data.aws_ami.ubuntu.id`
- D) `source.aws_ami.ubuntu.id`

**Answer**: C

**Explanation**:
Data source attributes are referenced using `data.<TYPE>.<NAME>.<ATTRIBUTE>`. For a data source declared as `data "aws_ami" "ubuntu"`, the `id` attribute is accessed as `data.aws_ami.ubuntu.id`. This is distinct from resource references, which omit the `data.` prefix (e.g., `aws_instance.web.id`).

---

### Question 4 — Meta-Arguments Available to All Resources

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Resource meta-arguments

**Question**:
Which TWO of the following are valid meta-arguments that can be added to any Terraform resource block? (Select two.)

- A) `source`
- B) `depends_on`
- C) `backend`
- D) `lifecycle`

**Answer**: B, D

**Explanation**:
Meta-arguments are special arguments supported by all resource blocks regardless of provider. The five meta-arguments are: `count`, `for_each`, `depends_on`, `provider`, and `lifecycle`. `source` is used in `required_providers` and `module` blocks, not resource blocks. `backend` is a root-level configuration block, not a resource argument.

---

### Question 5 — Implicit vs Explicit Dependencies

**Difficulty**: Medium
**Answer Type**: one
**Topic**: How implicit dependencies are created

**Question**:
What causes Terraform to automatically create an implicit dependency between two resources?

- A) Both resources sharing the same provider configuration
- B) One resource's argument referencing an attribute of another resource (e.g., `aws_vpc.main.id`)
- C) Both resources being declared in the same `.tf` file
- D) A `var.*` or `local.*` value being used in both resource blocks

**Answer**: B

**Explanation**:
Terraform builds its dependency graph by scanning for attribute references between resources. When resource B uses `aws_vpc.main.id` as an argument value, Terraform infers that B depends on A and ensures A is created first. References to `var.*` and `local.*` do **not** create dependency edges because variables and locals are not resources.

---

### Question 6 — `depends_on` Use Case

**Difficulty**: Medium
**Answer Type**: one
**Topic**: When to use `depends_on`

**Question**:
In which situation should `depends_on` be used in a Terraform resource block?

- A) To define the order in which output values are displayed after `terraform apply`
- B) To force a resource to be replaced instead of updated in-place
- C) To express a dependency that Terraform cannot detect through attribute references, such as an IAM policy attachment
- D) To ensure a resource is created before any data sources in the configuration are read

**Answer**: C

**Explanation**:
`depends_on` is used for dependencies that exist in the real world but are not expressed through attribute references. The canonical example is an IAM policy: an EC2 instance may need an IAM role policy to be fully attached before the instance starts, but Terraform cannot detect this relationship through HCL references alone. Overusing `depends_on` reduces Terraform's ability to parallelise operations.

---

### Question 7 — `lifecycle` `create_before_destroy`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: `create_before_destroy` lifecycle argument

**Question**:
What is the effect of setting `create_before_destroy = true` in a resource's `lifecycle` block?

- A) Terraform creates a backup of the resource's state before applying any destroy operation
- B) Terraform provisions the replacement resource first and only destroys the old resource after the new one is ready, minimising downtime
- C) Terraform creates the resource before any `terraform plan` is executed
- D) Terraform ensures the resource is created before all other resources in the configuration, regardless of dependencies

**Answer**: B

**Explanation**:
By default, when a resource must be replaced (e.g., changing an immutable attribute), Terraform destroys the old resource first, then creates the new one. Setting `create_before_destroy = true` reverses this order — the replacement is created first, and only after it is successfully provisioned is the old resource destroyed. This is commonly used for load balancer target groups, certificates, and other resources where downtime must be avoided.

---

### Question 8 — `lifecycle` `prevent_destroy`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: `prevent_destroy` lifecycle argument

**Question**:
What happens when `prevent_destroy = true` is set in a resource's `lifecycle` block and a plan includes destroying that resource?

- A) Terraform skips the destroy step and leaves the resource unchanged without any error
- B) Terraform prompts the user for a special override passphrase before allowing the destroy
- C) Terraform returns an error and refuses to create a plan that includes destroying the resource
- D) Terraform creates a backup of the resource in a separate workspace before destroying it

**Answer**: C

**Explanation**:
`prevent_destroy = true` instructs Terraform to raise an error if any execution plan would destroy that resource. This acts as a guardrail against accidental deletion of critical infrastructure like production databases or networking resources. To destroy such a resource intentionally, you must first remove the `prevent_destroy` setting from the configuration.

---

### Question 9 — `lifecycle` `ignore_changes`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: `ignore_changes` lifecycle argument

**Question**:
What is the purpose of the `ignore_changes` argument in a resource's `lifecycle` block?

- A) It prevents Terraform from reading the resource's current state during `terraform plan`
- B) It tells Terraform to ignore drift on specified attributes, so changes to those attributes outside Terraform do not trigger an update
- C) It suppresses `terraform plan` output for the listed attributes to keep logs concise
- D) It forces Terraform to skip validation of the listed arguments during `terraform validate`

**Answer**: B

**Explanation**:
`ignore_changes` accepts a list of attribute names. When Terraform detects drift on a listed attribute (i.e., the real-world value differs from the state value), it ignores the difference and does not plan an update. This is useful for attributes managed externally — for example, an Auto Scaling Group's `desired_capacity` that is adjusted by AWS automatically and should not be reverted by Terraform on each apply.

---

### Question 10 — `replace_triggered_by` Lifecycle Argument

**Difficulty**: Medium
**Answer Type**: one
**Topic**: `replace_triggered_by` lifecycle argument

**Question**:
What does the `replace_triggered_by` argument in a `lifecycle` block do?

- A) It replaces the resource's provider with the one specified in the argument list
- B) It causes the resource to be destroyed and recreated whenever any resource or attribute listed in the argument changes
- C) It triggers a `terraform plan` automatically when a listed resource is modified outside Terraform
- D) It forces the resource to be replaced on every `terraform apply` regardless of any changes

**Answer**: B

**Explanation**:
`replace_triggered_by` accepts a list of resource references or resource attribute references. When any of the referenced items change (even if the current resource's own arguments have not changed), Terraform marks the resource for replacement (destroy + create). A common use case is replacing an Auto Scaling Group when its launch template is updated.

---

### Question 11 — `moved` Block Purpose

**Difficulty**: Hard
**Answer Type**: one
**Topic**: What the `moved` block does in Terraform state

**Question**:
What is the purpose of the `moved` block in Terraform?

- A) It migrates a resource to a different cloud provider without destroying and recreating it
- B) It renames or relocates a resource address in the state file without destroying and recreating the real infrastructure
- C) It moves a resource's configuration from one `.tf` file to another and updates imports automatically
- D) It transfers a resource's state to a different Terraform workspace while keeping the resource in the current cloud account

**Answer**: B

**Explanation**:
The `moved` block tells Terraform that a resource previously tracked under one address (e.g., `aws_instance.old_name`) is now known under a different address (e.g., `aws_instance.new_name`). Terraform updates the state file to reflect the new address without issuing any API calls to destroy or recreate the real resource. This is essential when refactoring configuration or moving resources into modules.

---

### Question 12 — Default Parallelism

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Terraform default parallelism and the `-parallelism` flag

**Question**:
What is the default number of concurrent operations Terraform uses during `terraform apply`, and how can it be changed?

- A) Default is 5; change with `terraform apply -concurrent=10`
- B) Default is 10; change with `terraform apply -parallelism=<n>`
- C) Default is unlimited; Terraform always maximises concurrency up to available CPU cores
- D) Default is 1 (sequential); change with `terraform apply -parallel=true`

**Answer**: B

**Explanation**:
Terraform applies resource operations in parallel for resources with no inter-dependencies, with a default concurrency of 10. This can be tuned with `-parallelism=<n>` on `plan`, `apply`, or `destroy`. The dependency graph (DAG) determines which operations can safely run in parallel; only independent resources are processed concurrently.

---

### Question 13 — When Data Sources Are Read

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Timing of data source reads during plan and apply

**Question**:
During which TWO phases can a Terraform data source be read? (Select two.)

- A) During `terraform init`, when providers are downloaded
- B) During the `plan` phase, when the data source's query arguments are fully known
- C) During the `apply` phase, when the data source's query arguments depend on a value not known until apply
- D) During `terraform validate`, when syntax is checked

**Answer**: B, C

**Explanation**:
Data sources are typically read during the **plan** phase so their results can inform the plan output. However, if a data source's arguments depend on a value that is only computed during apply (such as the ID of a resource being created in the same run), the data source read is deferred to the **apply** phase. `terraform init` and `terraform validate` do not read data sources.

---
