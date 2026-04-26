# Terraform Associate (004) — Question Bank Iter 4 Batch 4

**Iteration**: 4
**Iteration Style**: Error identification — spot the mistake in config, workflow, or claim
**Batch**: 4
**Objective**: 4a — Resources, Data & Dependencies
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 2 Easy / 9 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — `data` Block Claimed to Create Resources

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Identifying the error in claiming a `data` block provisions infrastructure

**Question**:
A developer comments on a pull request:

> "Adding `data "aws_vpc" "existing" { id = "vpc-0abc12345" }` to my configuration will look up the VPC **and** add it to the Terraform state file as a managed resource — just like a `resource` block would, except Terraform won't destroy it on `terraform destroy`."

What is the error in this comment?

- A) There is no error — a `data` block creates a resource just like a `resource` block, but with destroy protection
- B) The error is that `data` blocks require a `provider` meta-argument to be explicitly set; without one, the lookup cannot succeed
- C) The error is that a `data` block **never creates or manages resources** — it performs a read-only query against the provider API to retrieve information about an already-existing object; no resource is provisioned and the result is not added to state as a managed resource
- D) The error is that `id` is not a valid filter argument inside a `data "aws_vpc"` block — VPCs must be looked up by `cidr_block`

**Answer**: C

**Explanation**:
A `data` block (data source) is read-only — it queries the provider API for information about an existing object and exposes its attributes for use in the configuration. It does not create, update, or destroy any cloud resource, and the queried object is not tracked in state as a managed resource. Only `resource` blocks define infrastructure that Terraform creates and manages. The comparison to a `resource` block with destroy protection is fundamentally wrong.

---

### Question 2 — `create_before_destroy` Described as the Default Behaviour

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Identifying the error in claiming Terraform's default replacement order is create-then-destroy

**Question**:
A team's onboarding guide states:

> "When Terraform replaces a resource — for example, because an immutable attribute was changed — it creates the new resource first and only destroys the old one after the replacement is ready. This create-before-destroy strategy is Terraform's default behaviour. You can disable it by setting `create_before_destroy = false` in the `lifecycle` block."

What is the error in this guide?

- A) There is no error — Terraform always creates the replacement before destroying the original
- B) The error is that `create_before_destroy = false` is not a valid value; the argument is boolean and only `true` is accepted
- C) The error is that **Terraform's default replacement order is destroy-first, then create** — the original resource is destroyed before the replacement is provisioned; `create_before_destroy = true` must be explicitly set in the `lifecycle` block to reverse this order and avoid downtime
- D) The error is that `create_before_destroy` applies to all resource operations, not just replacements — it affects creates and updates as well

**Answer**: C

**Explanation**:
By default, when a resource must be replaced (e.g., changing the `ami` on an EC2 instance), Terraform destroys the old resource first and then creates the new one. This destroy-before-create default can cause downtime for resources that serve live traffic. Setting `create_before_destroy = true` in the `lifecycle` block reverses the order — the replacement is created first, and only after it succeeds is the old resource destroyed. The guide has the default behaviour backwards.

---

### Question 3 — `count` and `for_each` Claimed Usable Together

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Identifying TWO errors in claims about the `count` and `for_each` meta-arguments

**Question**:
A wiki article makes four claims about Terraform meta-arguments. Which TWO contain errors? (Select two.)

- A) "A resource block can use both `count` and `for_each` simultaneously — `count` controls how many copies exist while `for_each` provides a named key for each copy, allowing combined matrix-style resource creation"
- B) "With `count = 3`, the valid values of `count.index` are 0, 1, and 2 — indexing starts at zero, so the third instance has `count.index = 2`"
- C) "`for_each` accepts a plain `list(string)` value directly, without requiring any type conversion — Terraform iterates over the list elements natively"
- D) "When `for_each` is assigned a `map`, `each.key` holds the map key and `each.value` holds the corresponding map value for each iteration"

**Answer**: A, C

**Explanation**:
Option A is incorrect: `count` and `for_each` are **mutually exclusive** meta-arguments — they cannot be used on the same resource block. Using both causes a validation error. Option C is incorrect: `for_each` requires a `set(string)` or `map` value — it does not accept a plain `list`. A list must be converted first with `toset([...])` for a set or wrapped in a `tomap()` call for map-style iteration. Options B and D are both correct descriptions of `count.index` and `for_each` map behaviour.

---

### Question 4 — Implicit Dependency Described as Requiring `depends_on`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying the error in claiming `depends_on` is required for attribute-reference dependencies

**Question**:
During a code review, an engineer comments:

> "This configuration has a bug — `aws_instance.web` sets `subnet_id = aws_subnet.public.id`, but there's no `depends_on = [aws_subnet.public]` in the instance block. Without an explicit `depends_on`, Terraform has no way to know the subnet must be created first and may attempt to create the instance before the subnet exists."

What is the error in this review comment?

- A) There is no error — `depends_on` is always required when one resource references another resource's attribute
- B) The error is that `depends_on` must reference the VPC, not the subnet — Terraform only traces dependencies one level deep
- C) The error is that **Terraform automatically detects implicit dependencies through attribute references** — because `aws_instance.web` uses `aws_subnet.public.id` as an argument value, Terraform infers that the subnet must be created first without any explicit `depends_on`; adding it here is unnecessary and reduces parallelism
- D) The error is that dependency ordering is only enforced during `terraform destroy`, not `terraform apply`

**Answer**: C

**Explanation**:
Terraform builds a dependency graph by scanning resource attribute references. When `aws_instance.web` uses `aws_subnet.public.id`, Terraform detects the reference and automatically adds a dependency edge: the subnet must be created (and its `id` known) before the instance can be created. This is called an **implicit dependency**. `depends_on` is reserved for dependencies that cannot be expressed through attribute references — typically IAM policy attachments or other side-effects that Terraform cannot detect. Using `depends_on` unnecessarily introduces serialisation that limits Terraform's ability to run operations in parallel.

---

### Question 5 — `prevent_destroy` Claimed to Block `terraform state rm`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying the error in claiming `prevent_destroy` protects against `terraform state rm`

**Question**:
A senior engineer tells their team:

> "I've added `lifecycle { prevent_destroy = true }` to our production RDS instance. This is a complete safeguard — nobody can remove this resource from Terraform management through `terraform destroy`, a `terraform apply` that would replace it, **or** `terraform state rm`. It's the last line of defence against any accidental deletion."

What is the error in this claim?

- A) There is no error — `prevent_destroy = true` blocks all mechanisms that could remove the resource from Terraform management
- B) The error is that `prevent_destroy` does not block `terraform apply` replacements — it only prevents `terraform destroy`
- C) The error is that `prevent_destroy = true` **only prevents plan-time operations** that would destroy the resource (such as `terraform destroy` or config-driven replacements) — it does **not** protect against `terraform state rm`, which directly manipulates the state file and completely bypasses lifecycle hooks
- D) The error is that `prevent_destroy` requires a remote backend to function — it has no effect when state is stored locally

**Answer**: C

**Explanation**:
`prevent_destroy = true` causes Terraform to raise an error during the **plan** phase if any plan would destroy the resource — this blocks both `terraform destroy` and replacements triggered by configuration changes. However, `terraform state rm` operates **directly on the state file** and bypasses all plan-phase checks including lifecycle hooks. Running `terraform state rm aws_db_instance.prod` removes the resource from state without triggering any lifecycle evaluation, leaving the actual RDS instance running but unmanaged by Terraform. The engineer's claim that `prevent_destroy` is a complete safeguard is incorrect.

---

### Question 6 — `ignore_changes` Claimed to Hide Resource from `terraform plan`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying the error in claiming `ignore_changes = all` makes a resource invisible to plan

**Question**:
A team documents their use of `ignore_changes`:

> "We've applied `lifecycle { ignore_changes = all }` to our Auto Scaling Group. This makes the resource completely invisible to `terraform plan` — it won't appear in plan output at all, and Terraform treats it as unmanaged going forward."

What is the error in this documentation?

- A) There is no error — `ignore_changes = all` removes the resource from Terraform's awareness entirely
- B) The error is that `ignore_changes = all` is invalid syntax — the `all` keyword is not accepted; individual attribute names must be listed
- C) The error is that `ignore_changes = all` **does not hide the resource from plan or remove it from Terraform management** — Terraform still tracks the resource in state, still refreshes its current values, and still includes it in the plan; `ignore_changes = all` only means Terraform will not propose changes to update attribute values that have drifted, not that the resource is unmanaged
- D) The error is that `ignore_changes = all` must be combined with `prevent_destroy = true` to suppress all plan output for a resource

**Answer**: C

**Explanation**:
`ignore_changes = all` instructs Terraform to skip drift-based update proposals for all attributes of the resource — if the real-world state differs from the configuration, Terraform notes the difference but does not propose an update plan. The resource is still fully tracked in state, still refreshed during `terraform plan`, and still visible in plan output (for example, if a replacement is triggered by something outside `ignore_changes` scope). It does **not** make the resource unmanaged or invisible. To stop managing a resource entirely, the `removed` block with `lifecycle { destroy = false }` should be used instead.

---

### Question 7 — `count.index` Starting Value Claimed as 1

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying the error in a comment claiming `count.index` starts at 1

**Question**:
A developer adds this comment to their configuration:

```hcl
resource "aws_instance" "worker" {
  count         = 3
  ami           = "ami-0abc123"
  instance_type = "t3.micro"

  tags = {
    # count.index starts at 1 for the first instance
    Name = "worker-${count.index}"  # produces: "worker-1", "worker-2", "worker-3"
  }
}
```

What is the error in this comment?

- A) There is no error — `count.index` begins at 1, so with `count = 3` the tag values are `"worker-1"`, `"worker-2"`, and `"worker-3"`
- B) The error is that `count.index` cannot be used inside a `tags` block — it is only valid in top-level resource arguments such as `ami` or `instance_type`
- C) The error is that `count.index` **starts at 0**, not 1 — with `count = 3`, the three instances have `count.index` values of 0, 1, and 2, producing tag values `"worker-0"`, `"worker-1"`, and `"worker-2"`, not `"worker-1"`, `"worker-2"`, `"worker-3"`
- D) The error is that `count.index` returns a floating-point number by default and must be converted to a string with `tostring()` before use in string interpolation

**Answer**: C

**Explanation**:
`count.index` is zero-based — the first instance has index 0, the second has index 1, and so on. With `count = 3`, the valid index values are 0, 1, and 2. The comment incorrectly documents the tag values as `"worker-1"` through `"worker-3"`. The actual tags produced are `"worker-0"`, `"worker-1"`, and `"worker-2"`. This is a common off-by-one misconception, especially for engineers accustomed to 1-based indexing in other tools.

---

### Question 8 — Wrong Claims About `depends_on` on a Data Source

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Identifying TWO errors in claims about adding `depends_on` to a `data` block

**Question**:
A team wiki makes four claims about the following data source configuration:

```hcl
data "aws_s3_objects" "uploads" {
  bucket     = "my-app-uploads"
  depends_on = [aws_iam_role_policy.app_s3]
}
```

Which TWO claims contain errors? (Select two.)

- A) "Adding `depends_on` to a `data` block is invalid HCL syntax — only `resource` blocks support `depends_on`; using it inside a `data` block causes a validation error"
- B) "When `depends_on` is added to a data source, the data source read is deferred from the plan phase to the apply phase, even when no computed values are involved"
- C) "Adding `depends_on` to a data source with empty results will cause the data source to return `null` on every subsequent plan, permanently breaking the configuration"
- D) "`depends_on` on the data source ensures that `aws_iam_role_policy.app_s3` is fully created or updated before the data source queries the provider API"

**Answer**: A, C

**Explanation**:
Option A is incorrect: `depends_on` is valid on `data` blocks — it is a supported meta-argument for both `resource` and `data` blocks. Option C is incorrect: adding `depends_on` does not cause the data source to return `null` results permanently; it simply defers the read to the apply phase, where the data source queries the provider normally after the dependency is resolved. Options B and D are both accurate: `depends_on` on a data source does defer the read to apply (even with fully known values), and it ensures the listed dependency is applied before the query runs.

---

### Question 9 — Resource `id` Attribute Claimed to Be User-Defined

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying the error in claiming the `id` attribute of a managed resource can be set in HCL

**Question**:
A new team member asks how to reference the ID of `aws_instance.web`. A colleague responds:

> "Just add `id = "my-custom-instance-id"` to the `aws_instance.web` resource block in your HCL. Terraform will use that string as the resource's ID in both state and the cloud, and you can then reference it as `aws_instance.web.id` in other resources."

What is the error in this response?

- A) There is no error — the `id` argument can be set in any resource block to provide a stable identifier
- B) The error is that `id` can be set on `data` blocks but not on `resource` blocks
- C) The error is that the `id` attribute of a managed resource is a **read-only value assigned by the cloud provider** after the resource is created — it cannot be set in the resource block configuration; Terraform stores the provider-assigned ID in state, where it can be referenced as `aws_instance.web.id`
- D) The error is that `id` is a reserved keyword in Terraform HCL and will cause a parse error if used as an argument name in any block type

**Answer**: C

**Explanation**:
The `id` attribute is a **computed** value — it is assigned by the cloud provider (e.g., AWS assigns an instance ID like `i-0abc1234def`) when the resource is created and stored in state by Terraform. It cannot be set or overridden in the HCL configuration; Terraform does not expose it as a settable argument. After apply, `aws_instance.web.id` reads the provider-assigned value from state. Attempting to add `id = "custom-value"` to most resource blocks will produce a validation error because `id` is not a valid configurable argument for those resource types.

---

### Question 10 — `for_each` with a `list(string)` Variable

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying the error in using a list-type variable directly with `for_each`

**Question**:
A developer writes the following configuration and claims it is valid:

```hcl
variable "regions" {
  type    = list(string)
  default = ["us-east-1", "us-west-2", "eu-west-1"]
}

resource "aws_s3_bucket" "regional" {
  for_each = var.regions
  bucket   = "data-${each.value}"
}
```

What is the error in this configuration?

- A) There is no error — `for_each` natively accepts a `list(string)` variable and iterates over its elements
- B) The error is that `var.regions` must be wrapped in `tomap()` before it can be used with `for_each`
- C) The error is that `for_each` **does not accept a `list` type** — it requires a `set(string)` or `map`; the correct fix is `for_each = toset(var.regions)` to convert the list to a set before iterating
- D) The error is that `each.value` is the wrong reference when iterating a variable — `each.key` must be used instead

**Answer**: C

**Explanation**:
`for_each` accepts only `set(string)` or `map` types — passing a `list(string)` directly causes a validation error because lists allow duplicate values and have a defined order, which conflicts with `for_each`'s requirement for stable, unique keys. The fix is to convert the list with `toset(var.regions)`, which deduplicates the values and creates a set that `for_each` can iterate. When iterating a set, `each.key` and `each.value` both refer to the current set element (e.g., `"us-east-1"`).

---

### Question 11 — `depends_on` Claimed to Override Lifecycle Hooks

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Identifying the error in claiming `depends_on` can override `prevent_destroy`

**Question**:
An engineer explains to a junior colleague:

> "The `depends_on` meta-argument doesn't just control creation order — it can also override lifecycle protections. If resource B has `depends_on = [resource_A]`, and resource A needs to be destroyed, Terraform will temporarily bypass `prevent_destroy = true` on resource B to satisfy the dependency chain. This is how Terraform handles cascading destroys."

What is the error in this explanation?

- A) There is no error — `depends_on` does override `prevent_destroy` when cascading destroy operations require it
- B) The error is that `depends_on` only affects destroy ordering — it has no effect on create ordering
- C) The error is that `depends_on` **controls execution ordering only** — it has no effect on lifecycle hooks whatsoever; `prevent_destroy = true` continues to block any plan that would destroy the protected resource regardless of `depends_on` relationships; lifecycle hooks and dependency ordering are entirely independent mechanisms in Terraform's execution model
- D) The error is that `depends_on` cannot reference resources in different modules — it is only valid between resources declared in the same configuration file

**Answer**: C

**Explanation**:
`depends_on` affects the **order** in which resource operations are executed — it tells Terraform which resources must be applied or destroyed before others. Lifecycle hooks (`prevent_destroy`, `create_before_destroy`, `ignore_changes`, `replace_triggered_by`) are orthogonal controls that govern **how** operations are performed. They are completely independent mechanisms. `prevent_destroy = true` will raise a plan-time error on any destroy operation — no `depends_on` relationship overrides this. If Terraform needs to destroy resource B to satisfy a dependency chain but B has `prevent_destroy = true`, the plan fails with an error, and the engineer must manually remove `prevent_destroy` before the operation can proceed.

---

### Question 12 — Wrong Claims About the `removed` Block

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Identifying TWO errors in claims about the `removed` block's behaviour

**Question**:
A Terraform 1.7+ runbook makes four claims about the `removed` block. Which TWO contain errors? (Select two.)

- A) "The `removed` block with `lifecycle { destroy = false }` stops Terraform from managing the resource but leaves the real cloud resource running and unaffected in the provider account"
- B) "A `removed` block with `lifecycle { destroy = false }` deletes both the Terraform state entry **and** the real cloud resource — the `destroy = false` setting only suppresses the interactive confirmation prompt before deletion"
- C) "After `terraform apply` successfully processes a `removed` block, the block must remain in the configuration permanently to prevent future `terraform plan` runs from flagging the resource address as unknown and attempting to recreate it"
- D) "After `terraform apply` processes a `removed` block, the block can be safely deleted from the configuration — subsequent plans will not attempt to recreate or re-manage the resource because it is no longer in state"

**Answer**: B, C

**Explanation**:
Option B is incorrect: `lifecycle { destroy = false }` instructs Terraform to remove the resource from state **without** destroying the real cloud resource — the cloud resource continues to run unmanaged. The `destroy = false` flag controls whether the actual infrastructure is deleted, not whether a confirmation prompt is shown. Option C is incorrect: the `removed` block only needs to be present for the transitional apply that removes the resource from state; once apply succeeds and the resource is no longer in state, the `removed` block has served its purpose and should be deleted. Future plans will simply not reference the resource. Options A and D are both accurate descriptions of the `removed` block's behaviour.

---

### Question 13 — `replace_triggered_by` Claimed to Work on Data Sources

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying the error in claiming `replace_triggered_by` is valid in a `data` block's `lifecycle`

**Question**:
A developer proposes adding the following to a data source to ensure it re-reads when an IAM role changes:

```hcl
data "aws_iam_policy_document" "app" {
  statement {
    actions   = ["s3:GetObject"]
    resources = ["arn:aws:s3:::my-bucket/*"]
  }

  lifecycle {
    replace_triggered_by = [aws_iam_role.app]
  }
}
```

The developer claims: "`replace_triggered_by` in the `lifecycle` block will force this data source to be re-read whenever `aws_iam_role.app` changes."

What is the error in this configuration and claim?

- A) There is no error — `replace_triggered_by` is valid in the `lifecycle` block of both `resource` and `data` blocks
- B) The error is only in the reference syntax — `replace_triggered_by` inside a `data` block must use `data.` prefix references, not resource addresses
- C) The error is that `lifecycle` blocks — and all lifecycle meta-arguments including `replace_triggered_by` — are **not supported on `data` blocks**; lifecycle meta-arguments only apply to `resource` blocks; data sources are re-evaluated based on their argument values and dependency changes, not lifecycle hooks
- D) The error is that `replace_triggered_by` requires a list of attribute references (e.g., `aws_iam_role.app.arn`), not full resource addresses

**Answer**: C

**Explanation**:
`lifecycle` blocks are a `resource`-only feature in Terraform — they cannot be placed inside a `data` block. All four lifecycle meta-arguments (`create_before_destroy`, `prevent_destroy`, `ignore_changes`, and `replace_triggered_by`) are exclusive to `resource` blocks. Adding a `lifecycle` block to a `data` block causes a validation error. Data sources are re-evaluated during plan or apply based on their argument values and any `depends_on` declarations — not through lifecycle triggers. The correct way to ensure a data source re-reads after a related resource changes is to use `depends_on = [aws_iam_role.app]` in the `data` block, which defers the read to the apply phase.

---
