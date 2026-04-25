# Terraform Associate (004) — Question Bank Iter 2 Batch 4

**Iteration**: 2
**Iteration Style**: Behavioral prediction — what happens when code runs or a scenario occurs
**Batch**: 4
**Objective**: 4a — Resources, Data & Dependencies
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 3 Easy / 8 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — Attribute Reference Ordering

**Difficulty**: Easy
**Answer Type**: one
**Topic**: What Terraform does when a resource references another resource's attribute

**Question**:
A configuration contains the following two resources:

```hcl
resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"
}

resource "aws_subnet" "public" {
  vpc_id     = aws_vpc.main.id
  cidr_block = "10.0.1.0/24"
}
```

`terraform apply` is run. In what order does Terraform create these resources, and why?

- A) Both resources are created concurrently because Terraform always maximises parallelism
- B) `aws_subnet.public` is created first because it is declared last in the file
- C) `aws_vpc.main` is created first because `aws_subnet.public` references its `id` attribute, creating an implicit dependency
- D) The order is undefined; Terraform randomises creation order for performance

**Answer**: C

**Explanation**:
Terraform builds a dependency graph by scanning attribute references. Because `aws_subnet.public` uses `aws_vpc.main.id` as an argument, Terraform infers that the subnet depends on the VPC and creates `aws_vpc.main` first. The subnet is only created after the VPC apply completes and its `id` attribute is known. File declaration order has no effect on execution order.

---

### Question 2 — var.* Reference Between Resources

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Whether a shared variable reference creates a dependency between resources

**Question**:
Two resource blocks both use `var.region` in their configurations:

```hcl
resource "aws_instance" "web" {
  ami           = "ami-0abc123"
  instance_type = "t3.micro"
  availability_zone = var.region
}

resource "aws_s3_bucket" "logs" {
  bucket = "logs-${var.region}"
}
```

Does Terraform create a dependency between `aws_instance.web` and `aws_s3_bucket.logs`? What happens during apply?

- A) Yes — sharing `var.region` creates an implicit dependency; Terraform creates `aws_instance.web` before `aws_s3_bucket.logs`
- B) No — `var.*` references do not create dependency edges; Terraform creates both resources in parallel
- C) Yes — all resources sharing any common value are always serialised
- D) No — but Terraform still creates them sequentially to avoid API rate limits

**Answer**: B

**Explanation**:
Variable references (`var.*`) and local references (`local.*`) do **not** create dependency edges in Terraform's graph. They are just values — not resources. Because neither resource references an attribute of the other, Terraform treats them as independent and creates them concurrently. Only direct resource attribute references (e.g., `aws_vpc.main.id`) establish implicit dependencies.

---

### Question 3 — Data Source Read Timing

**Difficulty**: Easy
**Answer Type**: one
**Topic**: When a data source is read relative to plan and apply phases

**Question**:
A data source is declared as:

```hcl
data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"]
}

resource "aws_instance" "web" {
  ami           = data.aws_ami.ubuntu.id
  instance_type = "t3.micro"
}
```

When does Terraform read the data source and retrieve the AMI ID?

- A) When `terraform init` runs — data sources are resolved during initialisation
- B) During `terraform plan` — Terraform queries the provider API to fetch the AMI ID before generating the execution plan
- C) Only after `aws_instance.web` is created — the data source is read at the end of apply
- D) Never — data sources only use values already present in state

**Answer**: B

**Explanation**:
Data sources with no dependencies on computed resource values (values not known until apply) are read during the **plan** phase. Terraform calls the provider API, retrieves the latest Ubuntu AMI ID, and uses it in the plan output. If the data source result depended on a resource attribute that is only known after apply (such as a newly created resource's ID), reading would be deferred to the apply phase.

---

### Question 4 — prevent_destroy Blocks a Destroy Plan

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What happens when terraform destroy runs against a resource with prevent_destroy = true

**Question**:
A production RDS instance is declared with:

```hcl
resource "aws_db_instance" "prod" {
  # ... config
  lifecycle {
    prevent_destroy = true
  }
}
```

An engineer runs `terraform destroy`. What happens?

- A) Terraform destroys the database after displaying an extra confirmation prompt
- B) Terraform destroys all other resources but skips `aws_db_instance.prod` without error
- C) Terraform returns an error during planning and refuses to generate a destroy plan for that resource
- D) Terraform creates a snapshot of the database before proceeding with the destroy

**Answer**: C

**Explanation**:
`prevent_destroy = true` causes Terraform to raise an error during the **plan** phase — not the apply phase — if any plan would result in destroying that resource. The error message indicates that the resource cannot be destroyed. To intentionally destroy it, the engineer must first remove the `prevent_destroy = true` line from the configuration, re-run `terraform apply` to update state, then run `terraform destroy`. The protection cannot be bypassed with `-force` or `-auto-approve`.

---

### Question 5 — ignore_changes and Subsequent Drift

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What terraform plan reports when a listed attribute has drifted with ignore_changes set

**Question**:
An EC2 instance is declared with:

```hcl
resource "aws_instance" "web" {
  ami           = "ami-0abc123"
  instance_type = "t3.micro"
  tags          = { Name = "web" }

  lifecycle {
    ignore_changes = [tags]
  }
}
```

An administrator manually adds a tag `Owner = "ops"` via the AWS console. `terraform plan` is run. What does Terraform report regarding the tags?

- A) Terraform proposes removing `Owner = "ops"` to revert to the declared tag set
- B) Terraform reports no changes to the `tags` attribute — drift on `tags` is ignored
- C) Terraform returns an error because `ignore_changes` cannot be used with the `tags` attribute
- D) Terraform updates the state file to include `Owner = "ops"` and reports the config as out of date

**Answer**: B

**Explanation**:
`ignore_changes = [tags]` instructs Terraform to skip drift detection for the `tags` attribute entirely. When `terraform plan` runs, it detects the extra tag but does not propose any change — it simply ignores the discrepancy. This is commonly used for resources managed by auto-scaling services or external systems that modify tags programmatically. The state file is not updated to include the manually added tag either.

---

### Question 6 — replace_triggered_by on Launch Template Change

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What happens to the dependent resource when replace_triggered_by fires

**Question**:
An Auto Scaling Group is configured with:

```hcl
resource "aws_autoscaling_group" "web" {
  # ... config

  lifecycle {
    replace_triggered_by = [aws_launch_template.web]
  }
}
```

The `aws_launch_template.web` resource is updated in the configuration (e.g., the AMI ID changes). `terraform apply` is run. What happens to `aws_autoscaling_group.web`?

- A) Terraform updates `aws_autoscaling_group.web` in-place without destroying it
- B) Terraform destroys and recreates `aws_autoscaling_group.web`, even if its own configuration has not changed
- C) Terraform ignores the launch template change until `aws_autoscaling_group.web` is explicitly updated
- D) Terraform returns an error because `replace_triggered_by` requires both resources to be in the same module

**Answer**: B

**Explanation**:
`replace_triggered_by` forces the resource to be replaced (destroyed and recreated) whenever the referenced resource or attribute changes. When `aws_launch_template.web` is updated, Terraform detects the change and marks `aws_autoscaling_group.web` for replacement — regardless of whether the ASG's own configuration changed. This ensures the ASG picks up the new launch template version. The plan shows a `-/+` symbol for the ASG.

---

### Question 7 — moved Block Prevents Destroy and Recreate

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What happens when a moved block is used to rename a resource

**Question**:
A running EC2 instance is declared as `resource "aws_instance" "app"`. An engineer renames it to `resource "aws_instance" "web"` in the configuration and adds:

```hcl
moved {
  from = aws_instance.app
  to   = aws_instance.web
}
```

`terraform apply` is run. What happens to the EC2 instance?

- A) The EC2 instance is destroyed and a new one is created with the name `web`
- B) The state file is updated to track the instance as `aws_instance.web`; the running EC2 instance is not touched
- C) Terraform returns an error because renaming resources requires manual state manipulation
- D) Terraform creates a second EC2 instance named `web` and leaves the original `app` instance running

**Answer**: B

**Explanation**:
The `moved` block tells Terraform that `aws_instance.app` has been renamed to `aws_instance.web` in the configuration. During apply, Terraform updates the state file to record the resource under its new address — without destroying or recreating the actual EC2 instance. The plan shows no resource changes (just the state rename). After a successful apply, the `moved` block can be removed from the configuration.

---

### Question 8 — depends_on Reduces Parallelism

**Difficulty**: Medium
**Answer Type**: one
**Topic**: How explicit depends_on affects apply parallelism

**Question**:
A configuration has 10 independent EC2 instances (no attribute references between them) and Terraform's default parallelism of 10. An engineer adds `depends_on = [aws_instance.web_0]` to `aws_instance.web_1` through `aws_instance.web_9`. `terraform apply` is run. What is the behaviour?

- A) All 10 instances are still created concurrently because `depends_on` is only advisory
- B) Only `aws_instance.web_0` is created first; the remaining 9 instances wait for it to complete before being created
- C) Terraform returns an error because `depends_on` is not permitted between resources of the same type
- D) The instances are created two at a time because `depends_on` halves the default parallelism

**Answer**: B

**Explanation**:
`depends_on` introduces an explicit dependency that Terraform enforces during execution. Even though no attribute reference exists, adding `depends_on = [aws_instance.web_0]` to the other 9 instances tells Terraform they must wait for `aws_instance.web_0` to be created before they can proceed. All 9 remaining instances can then be created concurrently (within the parallelism limit) once `web_0` completes. This demonstrates why `depends_on` should be used sparingly — it can significantly serialise what would otherwise be parallel operations.

---

### Question 9 — Data Source Dependent on Computed Value

**Difficulty**: Medium
**Answer Type**: one
**Topic**: When a data source is deferred to apply because it depends on an unknown value

**Question**:
A data source depends on the ID of a resource that does not yet exist:

```hcl
resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"
}

data "aws_subnets" "available" {
  filter {
    name   = "vpc-id"
    values = [aws_vpc.main.id]
  }
}
```

`terraform plan` is run for the first time. When does Terraform read the `data.aws_subnets.available` data source?

- A) During `terraform plan` — Terraform reads all data sources before generating the plan
- B) During `terraform apply` — after `aws_vpc.main` is created and its `id` is known
- C) During `terraform init` — data sources with filters are resolved at initialisation
- D) Terraform never reads this data source because the VPC does not exist yet

**Answer**: B

**Explanation**:
When a data source's filter or argument depends on a resource attribute that is not yet known (a "computed" value — here, the VPC's `id` before the VPC is created), Terraform cannot read the data source during plan because the required value doesn't exist yet. Terraform defers the data source read to the **apply** phase, after `aws_vpc.main` is created and its `id` is available. In the plan output, the data source result appears as `(known after apply)`.

---

### Question 10 — removed Block with destroy = false

**Difficulty**: Medium
**Answer Type**: many
**Topic**: What happens when a removed block with destroy = false is applied

**Question**:
An engineer adds the following block to a configuration that previously managed an EC2 instance:

```hcl
removed {
  from = aws_instance.legacy

  lifecycle {
    destroy = false
  }
}
```

`terraform apply` is run. Which TWO statements correctly describe the outcome? (Select two.)

- A) The EC2 instance is deleted from AWS
- B) The EC2 instance continues to run in AWS unaffected
- C) Terraform removes `aws_instance.legacy` from the state file so it is no longer managed
- D) Future `terraform plan` runs will propose recreating `aws_instance.legacy` from the configuration

**Answer**: B, C

**Explanation**:
The `removed` block (Terraform 1.7+) with `lifecycle { destroy = false }` stops Terraform from managing the resource without destroying it. During apply, Terraform removes the resource from state — but the actual EC2 instance continues to run in AWS untouched. Because the resource is no longer in state or configuration, future plan runs will not reference it at all. The `removed` block itself can be deleted from the configuration after apply completes.

---

### Question 11 — create_before_destroy with Immutable Attribute

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Plan output when an immutable attribute changes with create_before_destroy set

**Question**:
An EC2 instance is declared with `create_before_destroy = true`. The `ami` attribute (which forces replacement when changed) is updated in the configuration. `terraform plan` is run. What does the plan output show for this resource?

- A) `~ aws_instance.web` — the instance will be updated in-place
- B) `-/+ aws_instance.web` — the instance will be replaced, with the new instance created before the old one is destroyed
- C) `+ aws_instance.web` — a second instance will be added; the original is not touched
- D) The plan returns an error because changing `ami` requires setting `prevent_destroy = false` first

**Answer**: B

**Explanation**:
When an immutable attribute like `ami` changes, Terraform must replace the resource (destroy old, create new). The `-/+` plan symbol indicates replacement. With `create_before_destroy = true`, the execution order is reversed: the new instance is created first, then the old one is destroyed. The `-/+` symbol in the plan output is the same regardless of `create_before_destroy` — the flag affects execution order, not the plan symbol displayed.

---

### Question 12 — apply -parallelism=1 Effect

**Difficulty**: Hard
**Answer Type**: one
**Topic**: What terraform apply -parallelism=1 does to resource creation order

**Question**:
A configuration creates 8 independent resources (no dependencies between them). An engineer runs `terraform apply -parallelism=1`. What is the behaviour compared to the default?

- A) Terraform creates all 8 resources in a single API call to improve efficiency
- B) Terraform creates one resource at a time sequentially, rather than up to 10 concurrently — the apply takes longer but reduces API concurrency
- C) Terraform creates 2 resources at a time instead of the default 10
- D) The command fails because `-parallelism=1` is not a valid value

**Answer**: B

**Explanation**:
`-parallelism=N` controls the maximum number of concurrent resource operations Terraform performs. The default is 10. Setting `-parallelism=1` forces Terraform to create (or destroy) resources one at a time — fully sequential execution. This is occasionally used when cloud provider API rate limits are being hit during large applies, or when debugging to observe operations in order. The apply completes correctly but takes longer than with the default parallelism.

---

### Question 13 — depends_on on a Data Source

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Behaviour when depends_on is added to a data source

**Question**:
A data source has `depends_on = [aws_iam_role_policy.app]` added to it:

```hcl
data "aws_s3_objects" "uploads" {
  bucket = "my-app-uploads"

  depends_on = [aws_iam_role_policy.app]
}
```

Which TWO statements correctly describe the impact of this `depends_on`? (Select two.)

- A) The data source will not be read during `terraform plan` — it is deferred to apply after `aws_iam_role_policy.app` is applied
- B) The data source will still be read during plan, but only after `aws_iam_role_policy.app` is created
- C) Adding `depends_on` to a data source forces it to be read during apply instead of plan, even if no computed values are involved
- D) The `depends_on` on a data source has no effect — data sources always read during plan regardless

**Answer**: A, C

**Explanation**:
When `depends_on` is added to a data source, Terraform treats the data source as having an explicit dependency. Because the dependency (`aws_iam_role_policy.app`) may not yet exist, Terraform cannot safely read the data source during plan. This causes the data source read to be **deferred to the apply phase** — even if the data source would otherwise have been readable during plan. This is a gotcha: `depends_on` on data sources intentionally defers their read to apply, ensuring the dependency is in place before the data source query runs.

---
