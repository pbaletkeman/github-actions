# Terraform Associate (004) — Question Bank Iter 6 Batch 4

**Iteration**: 6
**Iteration Style**: Scenario-based troubleshooting — an engineer encounters a problem or unexpected outcome; identify the most likely cause and correct resolution
**Batch**: 4
**Objective**: 4a — Resources, Data & Dependencies
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 3 Easy / 8 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — Subnet Created Before VPC

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Understanding that implicit dependency from attribute reference controls creation order

**Question**:
An engineer writes a Terraform configuration with an `aws_vpc` and an `aws_subnet`. They expect Terraform to create the subnet first because it is declared first in the file. Instead, Terraform creates the VPC first. The engineer asks why. What is the correct explanation?

- A) Terraform creates resources in alphabetical order by resource type — `aws_subnet` comes before `aws_vpc` alphabetically, but Terraform uses the reverse alphabetical order as a safety measure
- B) Terraform creates resources in the order they are declared in configuration files — the VPC being created first means it must have been declared first
- C) Terraform infers creation order from the **dependency graph, not file declaration order** — because the subnet's `vpc_id` argument references `aws_vpc.main.id`, Terraform detects an implicit dependency and ensures the VPC is created before the subnet; file order is irrelevant to execution order
- D) The VPC is created first only because it uses the default provider; resources using aliased providers are always created last

**Answer**: C

**Explanation**:
Terraform builds a Directed Acyclic Graph (DAG) by scanning attribute references in the configuration. When `aws_subnet.public` uses `vpc_id = aws_vpc.main.id`, Terraform detects that the subnet's `vpc_id` argument depends on the VPC's `id` attribute — an attribute that is only known after the VPC is created. This creates an implicit dependency edge in the graph: VPC → subnet. Terraform respects this ordering during apply regardless of how the blocks are arranged in the `.tf` files. Declaration order in HCL has no effect on execution order; only the dependency graph does.

---

### Question 2 — Data Source Returns Stale AMI

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Understanding when data sources are evaluated and how to force a refresh

**Question**:
An engineer uses a data source to look up the latest Ubuntu AMI and passes it to an EC2 instance. Six months later, a new Ubuntu AMI is released. The engineer runs `terraform apply` but the EC2 instance is not updated — it still uses the old AMI. The engineer assumes the data source is refreshing automatically. What explains this behaviour?

- A) Data sources are evaluated only during `terraform init` — after initialisation, the result is cached permanently
- B) The data source is re-evaluated on every `terraform plan` and `terraform apply`; if the AMI has changed, Terraform will detect the new value and plan a replacement for the EC2 instance automatically
- C) Data sources **are** re-evaluated on each plan, but changing the AMI ID returned by the data source does not automatically cause the EC2 instance to be replaced unless the resource has a `ForceNew` constraint on `ami` — and `aws_instance.ami` is indeed `ForceNew`; if the plan shows no changes, the most likely explanation is that the data source filter is still returning the same AMI (e.g., the `most_recent = true` filter resolved to the same ID), not that the data source is stale; the engineer should verify by running `terraform plan` and examining the data source result
- D) Data sources cache their results in `.terraform/` for 30 days to avoid repeated API calls during planning

**Answer**: C

**Explanation**:
Data sources are re-evaluated on every `terraform plan` and `terraform apply` — they are not permanently cached. If the data source filter (e.g., `most_recent = true` for Ubuntu AMIs) resolves to a new AMI ID, Terraform will detect that the `aws_instance.ami` attribute would change and, because `ami` is a `ForceNew` attribute, will plan a replacement (`-/+`) for the EC2 instance. If the plan shows no changes, the most likely reason is that the data source is still returning the same AMI ID it always has — perhaps the filter is not matching the newest AMI, or the newer AMI hasn't appeared under the queried owner or name pattern. The engineer should examine the data source output in `terraform plan` to confirm which AMI ID is being returned.

---

### Question 3 — `prevent_destroy` Blocks Teardown Pipeline

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Understanding that prevent_destroy causes terraform apply/destroy to error and how to override it

**Question**:
A team has `lifecycle { prevent_destroy = true }` on their production RDS instance. When a junior engineer runs `terraform destroy` on the environment teardown pipeline, it fails with: `Error: Instance cannot be destroyed`. The engineer asks: "How do we destroy this resource when we're ready to decommission the environment?" What is the correct answer?

- A) Run `terraform destroy -force` — this flag overrides `prevent_destroy` for a single destroy operation
- B) The only way to destroy a `prevent_destroy`-protected resource is to use the AWS console directly
- C) `prevent_destroy = true` is a **configuration-level guard** — to destroy the resource, the engineer must **remove or change the `prevent_destroy` setting** in the configuration file and then run `terraform apply` (or `terraform destroy`) again; there is no runtime flag to override it; this is by design — the guard forces a deliberate code change before destruction can proceed
- D) Set `TF_PREVENT_DESTROY=false` in the environment before running `terraform destroy`

**Answer**: C

**Explanation**:
`prevent_destroy = true` is a safeguard that causes `terraform plan` to error if the plan includes destroying the protected resource — whether triggered by `terraform destroy` or by removing the resource block. It is intentionally impossible to override at runtime with a flag, because the purpose is to require a human to make a deliberate, version-controlled change to the configuration before destruction can proceed. The process for decommissioning a `prevent_destroy`-protected resource is: (1) remove `prevent_destroy = true` (or set it to `false`) in the `.tf` file, (2) commit and merge the change, (3) run `terraform apply` or `terraform destroy`. This ensures the removal of the guard is tracked in version history and reviewed.

---

### Question 4 — Two Resources Created in Wrong Order Despite `depends_on`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Diagnosing why depends_on alone on a data source may not be enough to control ordering with IAM

**Question**:
An engineer creates an EC2 instance with an IAM instance profile. The instance starts but immediately fails because the attached IAM role does not yet have the S3 read policy attached — the policy attachment resource is not complete by the time the instance boots and attempts to use it. The engineer added `aws_instance.app` to depend on `aws_iam_role.app`, but forgot to include the policy resource in `depends_on`. What is the most precise diagnosis and fix?

- A) IAM propagation is always instant — the instance failure is caused by a misconfigured security group blocking S3 access, not a missing dependency
- B) The implicit dependency on `aws_iam_role.app` ensures the role exists before the instance, but does not guarantee the **policy attachment** (`aws_iam_role_policy` or `aws_iam_role_policy_attachment`) is complete; the engineer must add the policy attachment resource to the `depends_on` list on `aws_instance.app` so Terraform waits for the policy to be attached before creating the instance
- C) Adding any `depends_on` entry to a resource causes Terraform to serialize all subsequent resource creations — the issue must be something else
- D) `depends_on` cannot reference `aws_iam_role_policy` resources — it only accepts module references

**Answer**: B

**Explanation**:
This is the canonical use case for `depends_on`. When an EC2 instance uses an IAM instance profile, Terraform can detect implicit dependencies on the role and profile objects through attribute references — but it cannot detect that the instance's startup behaviour depends on a policy being attached to that role, because there is no attribute reference from the instance to the policy resource. Terraform creates the instance after the role exists but potentially before the policy attachment completes. The instance then tries to call the S3 API using permissions that haven't propagated yet. The fix is explicit: add the policy attachment to `depends_on` on the `aws_instance` resource — `depends_on = [aws_iam_role_policy.s3_read]`. This forces Terraform to wait for the policy attachment to complete before provisioning the instance, ensuring IAM permissions are in place before the instance attempts to use them.

---

### Question 5 — `count` Removal Causes Unexpected Destroy+Create

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Understanding that removing a count item by index causes all subsequent instances to shift and be replaced

**Question**:
An engineer uses `count = 3` to create three S3 buckets, named `bucket-0`, `bucket-1`, and `bucket-2`. They later need to remove `bucket-1`. They remove element 1 from the list and set `count = 2`. On running `terraform plan`, they see that `bucket-1` will be destroyed and `bucket-2` will also be destroyed and recreated as a different bucket. Why does `bucket-2` get recreated?

- A) `count` always destroys and recreates all instances when the count value is reduced
- B) Removing element 1 from the list **shifts the index of the remaining elements** — what was index 2 (`bucket-2`) becomes index 1 (`bucket-1`) in the new count; Terraform sees `aws_s3_bucket.this[1]` as a different instance in state vs the new configuration, triggering a destroy+create; this index-shifting problem is why `for_each` with stable string keys is preferred over `count` when the set of instances may change
- C) S3 bucket names are globally unique — Terraform must destroy all buckets with sequential names when any one is removed
- D) This only happens when `count` is reduced from 3 to 2 — reducing to 1 or 0 would not trigger the shift

**Answer**: B

**Explanation**:
`count` identifies each resource instance by its zero-based integer index. When an item in the middle of the list is removed and `count` is reduced, all indexes above the removed item shift down by one. Terraform compares state (which has instances at indexes 0, 1, and 2) to the new configuration (which has instances at indexes 0 and 1 with updated names or attributes). Index 2 in state no longer matches index 1 in the new config — Terraform plans to destroy the old index-2 instance and create a new index-1 instance. This is a well-known pain point of `count` for managing collections that may have items removed from the middle. The idiomatic solution is to switch to `for_each` with a `set(string)` or `map`, where each instance is keyed by a stable string (e.g., the bucket name). Removing one key from a `for_each` collection only destroys that specific instance — all others retain their stable keys and are unaffected.

---

### Question 6 — `ignore_changes` Not Preventing Drift Detection

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Understanding the correct syntax for ignore_changes and common mistakes

**Question**:
An engineer uses Auto Scaling Groups and wants Terraform to ignore the `desired_capacity` attribute (which the ASG manages automatically). They add this lifecycle block:

```hcl
lifecycle {
  ignore_changes = "desired_capacity"
}
```

On the next `terraform plan`, Terraform still shows a change to `desired_capacity`. What is the error?

- A) `desired_capacity` cannot be ignored — it is a required attribute that Terraform always manages
- B) `ignore_changes` is only available in Terraform Enterprise — the community edition always manages all attributes
- C) The **`ignore_changes` argument requires a list value**, not a bare string — the correct syntax is `ignore_changes = [desired_capacity]` (square brackets, no quotes around the attribute name); using a string instead of a list causes Terraform to silently ignore the argument or return a validation error, leaving the attribute tracked normally
- D) The engineer must also add `create_before_destroy = true` alongside `ignore_changes` for the latter to take effect

**Answer**: C

**Explanation**:
`ignore_changes` takes a **list of attribute references** (not strings). The correct syntax is:
```hcl
lifecycle {
  ignore_changes = [desired_capacity]
}
```
The attribute names inside the list are written without quotes — they are symbolic references to the resource's schema attributes, not string literals. Using a bare quoted string (`ignore_changes = "desired_capacity"`) is a type error: the argument expects a list and receives a string, which Terraform either rejects with a validation error or silently ignores (depending on the version), with the result that the attribute is still tracked and drift is still detected. The square bracket list syntax is required. To ignore all attributes, use the special value `ignore_changes = all`.

---

### Question 7 — `for_each` Fails on a `list(string)`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Understanding that for_each does not accept a plain list and requires a set or map

**Question**:
An engineer writes:

```hcl
variable "bucket_names" {
  type    = list(string)
  default = ["logs", "backups", "assets"]
}

resource "aws_s3_bucket" "this" {
  for_each = var.bucket_names
  bucket   = each.key
}
```

`terraform plan` returns an error: `The given "for_each" argument value is unsuitable: must be a map, or set of strings, and you have provided a value of type list of string`. What is the fix?

- A) Change `for_each = var.bucket_names` to `for_each = count(var.bucket_names)` — `for_each` requires a count value, not the list directly
- B) `for_each` does not support string resources — it only works with numeric types; use `count = length(var.bucket_names)` instead
- C) Wrap the list in the `toset()` function: `for_each = toset(var.bucket_names)` — `for_each` requires a `set(string)` or `map`; `toset()` converts a `list(string)` to a `set(string)` by deduplicating values and removing ordering; after this conversion, `each.key` and `each.value` will both hold the bucket name for each iteration
- D) Change the variable type to `set(string)` in the variable declaration and leave `for_each` as-is — `for_each` accepts sets only from variable declarations, not inline values

**Answer**: C

**Explanation**:
`for_each` requires a value of type `map` or `set(string)` — it does not accept `list(string)` directly. This is because lists are ordered and can contain duplicates, while `for_each` needs stable, unique keys for each instance. The standard fix is to convert the list to a set using `toset()`: `for_each = toset(var.bucket_names)`. After this, `each.key` and `each.value` both equal the bucket name for each iteration (in a set, the key and value are the same). Note that `toset()` deduplicates the input — if the list contained duplicate names, the set would have fewer elements. Alternatively, changing the variable type declaration to `type = set(string)` also works and is the cleaner long-term approach if duplicates are not meaningful.

---

### Question 8 — Resource Recreated Every Apply Due to `replace_triggered_by`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Diagnosing unintended constant replacement caused by replace_triggered_by referencing a frequently changing resource

**Question**:
An engineer adds `replace_triggered_by = [aws_launch_template.web]` to an Auto Scaling Group resource to ensure the ASG is recycled when the launch template changes. After deploying, the ASG is replaced on every `terraform apply` — even when nothing has changed. What is the most likely cause?

- A) `replace_triggered_by` always forces a replacement on every apply — this is the expected behaviour
- B) The `aws_launch_template.web` resource itself has **`create_before_destroy = true`**, causing it to be replaced on every apply, which then triggers the ASG replacement through `replace_triggered_by`; the fix is to remove `create_before_destroy` from the launch template
- C) The `aws_launch_template.web` resource **has an attribute that changes on every apply** — for example, if the launch template has `latest_version` or similar computed attribute that increments on each apply even with no configuration changes, `replace_triggered_by` detects this as a change and triggers ASG replacement; the engineer should scope `replace_triggered_by` to a specific stable attribute rather than the entire resource, or investigate why the launch template resource reports changes on every apply
- D) `replace_triggered_by` is incompatible with Auto Scaling Groups — it should only be used with stateless resources like EC2 instances

**Answer**: C

**Explanation**:
`replace_triggered_by` monitors the referenced resource (or a specific attribute of it) and forces a replacement of the current resource whenever a change is detected. If the referenced resource itself changes on every apply — for example, because it has a computed attribute like `latest_version` that increments whenever the template is touched, or because some attribute is not stable across plan cycles — then `replace_triggered_by` will trigger the ASG replacement every time. To fix this, the engineer can scope the trigger to a specific, stable attribute rather than the whole resource: `replace_triggered_by = [aws_launch_template.web.id]` rather than the entire resource object. Alternatively, investigate why the launch template is reporting changes on every apply and stabilise it. The pattern `replace_triggered_by = [resource]` triggers on any change to any attribute of that resource — scoping to a specific attribute gives more control.

---

### Question 9 — `removed` Block Destroys Resource Unexpectedly

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Understanding that removed block destroy=false is needed to stop managing without destroying

**Question**:
An engineer wants to stop managing an EC2 instance with Terraform without deleting it from AWS. They have heard about the `removed` block (Terraform 1.7+) and add it to their configuration. On the next `terraform apply`, the instance is deleted. What did the engineer most likely forget?

- A) The `removed` block always stops Terraform from managing a resource without any cloud-side effect — the instance deletion must have been triggered by something else
- B) The engineer forgot to run `terraform state rm` before using the `removed` block — the two must be used together
- C) The `removed` block has a **`lifecycle { destroy = false }` sub-block** that must be explicitly set — without it, the default behaviour of `removed` is to **destroy** the resource and remove it from state; setting `destroy = false` tells Terraform to stop tracking the resource but leave it running in the cloud; the engineer forgot this sub-block and got the default destroy behaviour
- D) `removed` blocks are only supported in HCP Terraform — in the open-source CLI, they always destroy the resource

**Answer**: C

**Explanation**:
The `removed` block (introduced in Terraform 1.7) is the declarative way to stop managing a resource. Its `lifecycle` sub-block controls what happens to the cloud resource: `destroy = true` (the default) destroys the resource and removes it from state; `destroy = false` removes it from state only, leaving the cloud resource intact and unmanaged. The engineer who wanted to "stop managing without deleting" needed:
```hcl
removed {
  from = aws_instance.old_server
  lifecycle {
    destroy = false
  }
}
```
Without the `destroy = false` sub-block, Terraform uses the default (`destroy = true`) and deletes the instance. This is analogous to `terraform state rm` (which also removes from state without destroying) but in a declarative, version-controlled form.

---

### Question 10 — Two Scenarios Where `depends_on` Is Required

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Identifying TWO scenarios where implicit dependency detection is insufficient and depends_on is needed

**Question**:
Which TWO of the following scenarios require `depends_on` because Terraform cannot detect the dependency through attribute references alone? (Select two.)

- A) An EC2 instance references `aws_subnet.public.id` in its `subnet_id` argument — Terraform must be told explicitly via `depends_on` that the instance depends on the subnet
- B) An EC2 instance uses an IAM instance profile, and the instance's startup script reads from an S3 bucket that the attached IAM role must have permission to access — the policy attachment has no attribute referenced by the instance, so Terraform has no way to detect the dependency automatically
- C) A `null_resource` with a `local-exec` provisioner runs a script that calls an API exposed by an `aws_api_gateway_rest_api` resource — no attribute of the API gateway is referenced in the `null_resource` block, so `depends_on` is needed to ensure the API is deployed before the script runs
- D) An `aws_db_instance` is referenced by its endpoint in an `aws_instance` user_data script using string interpolation — Terraform must be told via `depends_on` that the instance depends on the database

**Answer**: B, C

**Explanation**:
`depends_on` is for dependencies that Terraform **cannot** detect through attribute references. **(A)** is incorrect — `subnet_id = aws_subnet.public.id` is an explicit attribute reference, which Terraform automatically resolves into a dependency edge; no `depends_on` is needed. **(D)** is also incorrect — using `aws_db_instance.main.endpoint` in string interpolation is also an attribute reference that creates an implicit dependency. **(B)** is a genuine `depends_on` use case: the IAM policy attachment resource has no attribute referenced by the EC2 instance, yet the instance's runtime behaviour depends on the policy being attached before it starts. Terraform cannot infer this. **(C)** is the other canonical case: a `null_resource` provisioner that calls an external API has no Terraform attribute reference to that API resource, so `depends_on` is required to guarantee ordering.

---

### Question 11 — Circular Dependency Error

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Diagnosing a cycle in the dependency graph and knowing how to resolve it

**Question**:
An engineer receives the error: `Error: Cycle: aws_security_group.app, aws_instance.web`. Their configuration has `aws_instance.web` referencing `aws_security_group.app.id` in its `vpc_security_group_ids`, and `aws_security_group.app` referencing `aws_instance.web.private_ip` in an ingress rule. What explains the error and how should it be resolved?

- A) The error means both resources have `prevent_destroy = true` — removing those lifecycle blocks will resolve the cycle
- B) A **dependency cycle** exists: `aws_instance.web` depends on `aws_security_group.app` (for the security group ID), and simultaneously `aws_security_group.app` depends on `aws_instance.web` (for the private IP in the ingress rule); Terraform's DAG cannot resolve circular dependencies and errors immediately; the resolution is to break the cycle — for example, remove the private IP reference from the security group ingress rule and use a CIDR block instead, or restructure the ingress rule to reference a different stable value
- C) Cycles are resolved automatically on `terraform apply` — the error only appears during `terraform plan` and can be ignored
- D) The cycle is caused by both resources being in the same `.tf` file — moving one to a separate file resolves the dependency conflict

**Answer**: B

**Explanation**:
Terraform constructs a Directed Acyclic Graph (DAG) to determine creation and destruction order. A cycle occurs when resource A depends on resource B, and resource B also depends on resource A — creating a circular chain that cannot be ordered. In this case: the instance needs the security group ID to be created → but the security group needs the instance's private IP to be created → deadlock. Terraform detects this during graph construction and errors with a "Cycle" message rather than attempting an impossible ordering. The fix requires breaking the cycle by removing one of the circular references. Common approaches: (1) remove the private IP from the security group ingress and use a broader CIDR; (2) use a separate `aws_security_group_rule` resource for the instance-IP-based rule, applied after the instance is created; (3) redesign the architecture so the circular reference is not needed.

---

### Question 12 — `moved` Block Applied But Resource Still Recreated

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Diagnosing why a moved block did not prevent destroy+create — common mistakes in moved block syntax

**Question**:
An engineer renames `resource "aws_instance" "web"` to `resource "aws_instance" "web_server"` and adds:

```hcl
moved {
  from = "aws_instance.web"
  to   = "aws_instance.web_server"
}
```

Running `terraform plan` still shows `1 to destroy, 1 to add`. What is the error in the `moved` block?

- A) The `moved` block is in the wrong file — it must be in `main.tf` and cannot be in any other `.tf` file
- B) The `moved` block requires a `provider` argument to be specified alongside the `from` and `to` addresses
- C) The **`from` and `to` values in a `moved` block must be bare references, not quoted strings** — the correct syntax is `from = aws_instance.web` and `to = aws_instance.web_server` (without quotes); using quoted strings causes Terraform to treat the values as string literals rather than resource address references, which fails validation or is ignored; without a valid `moved` block, Terraform does not know about the rename and plans a destroy+create as it would without any `moved` block
- D) `moved` blocks can only be used when the resource is being moved between modules — renaming within the same module requires `terraform state mv` instead

**Answer**: C

**Explanation**:
`moved` block `from` and `to` arguments are **resource address references**, not strings. The correct syntax does not use quotes:
```hcl
moved {
  from = aws_instance.web
  to   = aws_instance.web_server
}
```
Quoting the addresses — `from = "aws_instance.web"` — is a syntax error or causes Terraform to misinterpret the block, depending on the version. When the `moved` block is invalid or unrecognised, Terraform falls back to the default behaviour: it sees a new address (`aws_instance.web_server`) with no state entry and an old address (`aws_instance.web`) with no configuration block, and plans a destroy+create. The `moved` block is valid for renaming within a module, moving into or out of a module, and refactoring `count`/`for_each` addresses — `terraform state mv` remains a valid imperative alternative.

---

### Question 13 — `data` Source Reads Stale Value During Apply

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Understanding the known-after-apply problem for data sources that depend on resources being created in the same apply

**Question**:
An engineer creates an `aws_security_group` resource and a `data "aws_security_group"` data source that filters by the security group's name in the same Terraform configuration. They expect the data source to return the newly created security group. During `terraform plan`, the data source shows `(known after apply)` instead of the security group's attributes. The engineer asks why. What is the correct explanation?

- A) Data sources can never reference resources created in the same Terraform configuration — they are always evaluated before any resources are created
- B) The data source must use the resource's `id` attribute, not the `name` filter — `name`-based filters only work against pre-existing resources
- C) The data source **depends on the security group being created**, but during `terraform plan` the security group does not exist yet (it will be created on apply); Terraform detects this dependency and defers the data source read until `terraform apply`, when the security group will exist — this is the "known after apply" behaviour; the attributes are shown as `(known after apply)` in the plan; after apply completes, the data source will have been read and its attributes will be available; the preferred alternative is to reference the resource's attributes directly (`aws_security_group.app.id`) rather than re-querying via a data source for a resource Terraform itself is creating
- D) The `(known after apply)` message means the data source is misconfigured — it cannot be used in the same configuration as a resource of the same type

**Answer**: C

**Explanation**:
When a data source's filter depends on a value that is only known after a resource is created (e.g., the name of a security group being created in the same apply), Terraform cannot evaluate the data source during planning — the value hasn't been set yet. Terraform defers the data source read to the apply phase and shows `(known after apply)` in the plan for any attributes that depend on the deferred result. This is correct behaviour and not an error. However, using a data source to look up a resource that Terraform itself manages is generally unnecessary and adds complexity. The idiomatic approach is to reference the managed resource's attributes directly — `aws_security_group.app.id` — rather than creating a data source that queries back for the same resource. Data sources are most valuable for referencing resources that exist outside the current Terraform configuration.

---
