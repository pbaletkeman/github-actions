# Terraform Associate (004) — Question Bank Iter 5 Batch 4

**Iteration**: 5
**Iteration Style**: Comparison and contrast — distinguish between two similar concepts
**Batch**: 4
**Objective**: 4a — Resources, Data & Dependencies
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 3 Easy / 8 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — `resource` Block vs `data` Block: Management Model

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contrast between how Terraform treats a `resource` block and a `data` block

**Question**:
Compare a `resource` block and a `data` block in Terraform. What is the fundamental difference in how Terraform treats each one?

- A) Both blocks create and manage cloud infrastructure — `data` blocks are simply read-only after initial creation
- B) A `resource` block defines infrastructure that Terraform creates, updates, and destroys as part of its state-managed lifecycle; a `data` block queries existing infrastructure in read-only mode — it never creates, modifies, or destroys anything, and the queried object is not tracked as a managed resource in state
- C) `data` blocks are identical to `resource` blocks but execute during `terraform init` rather than `terraform apply`
- D) A `resource` block is used only for compute resources; a `data` block is used for networking and storage resources that already exist

**Answer**: B

**Explanation**:
`resource` blocks define the infrastructure Terraform owns — objects are created on first apply, updated when configuration changes, and destroyed when removed from configuration or when `terraform destroy` is run. These objects are tracked in the state file. `data` blocks perform read-only queries against the provider API to retrieve attributes of objects that exist outside Terraform's management (e.g., a shared VPC ID or the latest AMI). The provider fetches the data, Terraform makes the attributes available in the configuration, but no resource is provisioned and nothing is written to state as a managed object.

---

### Question 2 — `count` vs `for_each`: Iteration Model

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contrast between numeric `count` iteration and key-based `for_each` iteration

**Question**:
Compare the `count` and `for_each` meta-arguments for creating multiple resource instances. What is the key structural difference between how they identify each instance?

- A) `count` is used for map-based resources; `for_each` is used for list-based resources — they are interchangeable for all other scenarios
- B) `count` creates a fixed number of identical instances identified by a zero-based integer index (`count.index`); `for_each` creates one instance per key in a map or element in a set, with each instance identified by a string key (`each.key`) and accessible value (`each.value`)
- C) `count` supports string keys; `for_each` supports only numeric indexes — they serve opposite roles from what their names suggest
- D) Both meta-arguments use the same `each.key` / `each.value` syntax to reference the current iteration context

**Answer**: B

**Explanation**:
`count` is integer-based — you specify how many copies to create, and each instance is distinguished by `count.index` (0, 1, 2…). This works well for identical or near-identical resources but makes addressing fragile when items are added or removed (indexes shift). `for_each` is key-based — it accepts a `set(string)` or `map`, and each instance is keyed by a stable string identifier. Inside the block, `each.key` refers to the current key and `each.value` refers to the map value for that key. Because keys are stable strings, adding or removing one item from a `for_each` collection does not affect the addresses of other instances.

---

### Question 3 — Implicit Dependency vs `depends_on`

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contrast between when Terraform auto-detects dependencies and when `depends_on` is required

**Question**:
Compare implicit dependencies and the `depends_on` meta-argument. When does Terraform create an implicit dependency automatically, and when must `depends_on` be used instead?

- A) Implicit dependencies are created when two resources share the same provider; `depends_on` is used when resources are in different providers
- B) Terraform creates an implicit dependency when a resource references an attribute of another resource (e.g., `subnet_id = aws_subnet.public.id`) — no `depends_on` is needed for these; `depends_on` is required only when a dependency exists that Terraform cannot detect through attribute references, such as IAM permissions that must be active before a resource uses them
- C) `depends_on` is always required — Terraform never infers ordering automatically from attribute references
- D) Implicit dependencies are detected through `var.*` references; `depends_on` is used for resource attribute references

**Answer**: B

**Explanation**:
Terraform automatically builds its dependency graph by scanning resource attribute references. When `aws_subnet.public` sets `vpc_id = aws_vpc.main.id`, Terraform sees the reference and guarantees `aws_vpc.main` is created first — no explicit `depends_on` is needed. `depends_on` is reserved for relationships that exist in the real world but cannot be expressed as attribute references — the most common example is an IAM role policy that must be fully propagated before an EC2 instance can use the role. Because the instance doesn't reference any attribute of the policy, Terraform can't detect the dependency; `depends_on` makes it explicit. Overusing `depends_on` reduces parallelism and should be avoided when an attribute reference suffices.

---

### Question 4 — `create_before_destroy = true` vs Default Replacement Order

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between the default destroy-first replacement sequence and the reversed create-first sequence

**Question**:
An `aws_lb_target_group` must be replaced because an immutable argument changed. Compare the two replacement strategies below — what is the operational difference?

- **Strategy A**: No `lifecycle` block (default behaviour)
- **Strategy B**: `lifecycle { create_before_destroy = true }`

- A) Both strategies result in identical behaviour — the default has always been create-before-destroy
- B) Strategy A (default) destroys the existing target group first and then creates the replacement, which causes the load balancer to have no target group during the transition; Strategy B creates the replacement target group first and only destroys the old one after the new one is successfully provisioned, maintaining availability during the transition
- C) Strategy B is only relevant for resources that support zero-downtime updates; for stateless resources like target groups, both strategies are identical
- D) Strategy A is safer because destroying first ensures no duplicate resources exist in the cloud simultaneously

**Answer**: B

**Explanation**:
Terraform's default replacement order is **destroy then create** — the existing resource is removed before the new one is provisioned. For resources that serve live traffic or are referenced by other infrastructure (like a load balancer target group), this creates a gap during which the old resource is gone but the new one doesn't exist yet. Setting `create_before_destroy = true` reverses the order: the new resource is created and confirmed healthy first, then the old one is destroyed. The plan still shows a `-/+` symbol, but the actual execution order is reversed. This is the correct approach for any resource where a downtime window is unacceptable.

---

### Question 5 — `prevent_destroy` vs `ignore_changes`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between destruction protection and drift suppression in `lifecycle` blocks

**Question**:
Compare `prevent_destroy = true` and `ignore_changes = [tags]` in a `lifecycle` block. What does each protect against, and in what scenario would you use each?

- A) Both arguments prevent Terraform from making any changes to a resource — they serve the same purpose but one is for destroy operations and the other for update operations
- B) `prevent_destroy = true` causes Terraform to return an error if a plan includes the destruction of that resource — protecting against accidental deletion of critical resources like production databases; `ignore_changes = [tags]` tells Terraform to disregard drift in the listed attributes, so external changes to those attributes (e.g., tags added by an automation tool) do not appear as unwanted changes in future plans
- C) `prevent_destroy` ignores all planned changes; `ignore_changes` only prevents destruction — they are named in the reverse of their actual purpose
- D) `prevent_destroy` applies only during `terraform destroy` operations; it has no effect when a resource is destroyed as part of a replacement during `terraform apply`

**Answer**: B

**Explanation**:
`prevent_destroy = true` is a **destruction guard** — if any plan (whether from `terraform destroy`, a configuration change that implies replacement, or any other scenario) would destroy the protected resource, Terraform aborts with an error. It is commonly applied to production databases, DNS zones, and other resources where accidental deletion would cause data loss. `ignore_changes` is a **drift suppression** tool — it tells Terraform to accept that certain attribute values may be changed outside Terraform (e.g., by Auto Scaling groups, tagging automations, or other tools) and to not flag those changes as configuration drift. Removing or updating ignored attributes from the Terraform config won't trigger an update. The two serve orthogonal purposes: one guards deletion; the other suppresses noise from known external changes.

---

### Question 6 — `count` Instance Address vs `for_each` Instance Address

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between the bracket notation used for `count` instances and `for_each` instances

**Question**:
A configuration has two resources that create three web instances — one using `count = 3` and one using `for_each = toset(["us-east-1", "eu-west-1", "ap-southeast-1"])`. How does the state address format differ between these two approaches?

- A) Both use numeric indexes — `aws_instance.web[0]`, `aws_instance.web[1]`, etc.
- B) `count` instances are addressed with zero-based integer indexes (e.g., `aws_instance.web[0]`, `aws_instance.web[1]`, `aws_instance.web[2]`); `for_each` instances are addressed using their string key (e.g., `aws_instance.web["us-east-1"]`, `aws_instance.web["eu-west-1"]`)
- C) Both use string keys — `count` generates keys from `count.index` converted to a string
- D) `for_each` instances are addressed with integer indexes; `count` instances use string keys derived from each resource's `id` attribute

**Answer**: B

**Explanation**:
The state address format directly reflects the iteration mechanism used. `count` instances are identified by their zero-based integer index in square brackets — removing `aws_instance.web[1]` from a `count = 3` collection shifts `aws_instance.web[2]` to `aws_instance.web[1]`, which Terraform treats as a deletion and recreation. `for_each` instances are addressed by their string key from the set or map — `aws_instance.web["us-east-1"]` is a stable address that doesn't shift when other entries are added or removed. This stability of `for_each` addresses is a primary reason it is preferred over `count` for heterogeneous resource collections.

---

### Question 7 — `moved` Block vs `terraform state mv`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between the HCL-based `moved` block and the CLI `state mv` command for renaming resources

**Question**:
A team needs to rename a resource in their Terraform configuration from `aws_instance.server` to `aws_instance.api`. Compare using a `moved` block in configuration versus running `terraform state mv aws_instance.server aws_instance.api`. What is the key operational difference?

- A) Both approaches are identical in effect — the `moved` block is simply the newer syntax for `terraform state mv`
- B) A `moved` block is declared in HCL and processed during `terraform plan`/`apply` — it is version-controlled alongside the configuration, can be reviewed in pull requests, and is executed automatically for all team members on their next apply; `terraform state mv` is an imperative CLI command that manipulates the state file directly and immediately, bypassing the plan/apply workflow — the change takes effect for whoever runs the command but is not tracked in configuration
- C) `terraform state mv` is preferred because it provides rollback capabilities; the `moved` block permanently alters the state with no way to revert
- D) `moved` blocks can only rename resources within a module; `terraform state mv` is required for root module renames

**Answer**: B

**Explanation**:
The core difference is **declarative vs imperative**. A `moved` block is HCL — it is committed to version control, reviewed like any configuration change, and applied as part of the normal plan/apply workflow. Every team member who runs `terraform plan` will see the rename operation and can apply it safely. `terraform state mv` is a direct state manipulation command — it modifies the state file immediately without a plan and is typically not tracked in version control. It is useful for one-off manual fixes or emergency corrections but lacks the auditability and team coordination of the `moved` block approach. The `moved` block was introduced in Terraform 1.1 as the preferred declarative approach to state refactoring.

---

### Question 8 — `removed` Block (`destroy = false`) vs `terraform state rm`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between declaratively un-tracking a resource and imperatively removing it from state

**Question**:
An `aws_instance.legacy` resource was created by Terraform but should now be managed manually outside Terraform. The team wants Terraform to stop tracking it without destroying it in AWS. Compare these two approaches:

- **Approach A**: Add a `removed` block with `lifecycle { destroy = false }`
- **Approach B**: Run `terraform state rm aws_instance.legacy`

What is the key difference?

- A) Both approaches destroy the EC2 instance in AWS and remove it from state simultaneously
- B) Approach A (`removed` block) is declarative — it is committed to the configuration and applied through the plan/apply workflow, giving the team visibility and auditability; Approach B (`terraform state rm`) is an imperative CLI command that removes the resource from state immediately without going through a plan — effective immediately but not tracked in configuration
- C) `terraform state rm` keeps the resource in the state file as read-only; the `removed` block completely deletes the state entry
- D) The `removed` block with `destroy = false` is only valid in Terraform Enterprise; `terraform state rm` must be used in open-source Terraform

**Answer**: B

**Explanation**:
Both approaches achieve the same end result — the resource is removed from Terraform's state and Terraform stops managing it, leaving the AWS resource intact. The distinction is process: the `removed` block (introduced in Terraform 1.7) is a declarative HCL approach that is version-controlled, visible in the plan output, and applied consistently across the team. `terraform state rm` is a direct CLI state manipulation — it removes the entry immediately, making no API calls. It is not tracked in configuration and leaves no record of the decision in the codebase. For team environments and auditability, the `removed` block is the preferred approach.

---

### Question 9 — `ignore_changes = [specific_attrs]` vs `ignore_changes = all`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between targeted drift suppression and complete drift suppression

**Question**:
Compare `ignore_changes = [tags, user_data]` with `ignore_changes = all` in a `lifecycle` block. What is the key operational risk difference between these two forms?

- A) Both forms are equivalent — listing attributes is just a more verbose way of writing `all`
- B) `ignore_changes = [tags, user_data]` suppresses drift only for the explicitly listed attributes — changes to any unlisted attribute (e.g., `instance_type`) are still detected and shown in plan; `ignore_changes = all` suppresses drift detection for **every** attribute of the resource, meaning configuration changes to any argument will silently have no effect in future plans — this can cause Terraform to diverge permanently from the actual state of the resource
- C) `ignore_changes = all` is the safer option because it ensures no unintentional changes are applied to the resource
- D) `ignore_changes = [tags, user_data]` causes Terraform to import those attributes from the live resource on every plan; `ignore_changes = all` skips the refresh phase for the resource entirely

**Answer**: B

**Explanation**:
`ignore_changes` with a specific list is a **surgical** suppression — only the named attributes are excluded from drift detection. If an engineer changes `instance_type` in the Terraform config, that change is still detected and applied. `ignore_changes = all` is a **blanket** suppression — it tells Terraform to never report or apply any configuration changes to the resource after it is initially created. This is occasionally appropriate for resources where all attributes are externally managed after creation, but it carries significant risk: if a configuration change is made (e.g., to `security_groups`), Terraform will silently ignore it, and the actual resource will never reflect the intent of the configuration. The targeted form is strongly preferred; `all` should only be used when explicitly intended.

---

### Question 10 — Data Source Read at Plan vs Data Source Read at Apply

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Contrasting TWO scenarios that determine whether a data source is read during plan or deferred to apply

**Question**:
Compare when a data source is read during the `plan` phase versus when it is deferred to the `apply` phase. Which TWO statements correctly describe this difference? (Select two.)

- A) A data source whose filter arguments are all known static values or already-resolved references is read during `terraform plan` — the result is available in the plan output
- B) All data sources are always read during `terraform apply`, never during `terraform plan`
- C) A data source whose filter arguments depend on a value that is only known after another resource is created (e.g., filtering by the ID of a resource being created in the same apply) is deferred and read during `terraform apply` after its dependency is provisioned
- D) Data sources are read during `terraform init` — neither `plan` nor `apply` triggers data source queries

**Answer**: A, C

**Explanation**:
(A) When a data source's arguments are fully resolved — either static values like `owners = ["099720109477"]` or references to already-existing resources — Terraform reads it during the `plan` phase. The resolved value appears in the plan output, giving the operator visibility into what the data source returns. (C) When a data source's arguments depend on a computed value that doesn't exist yet (e.g., filtering by the ID of an `aws_vpc` that is being created in the same `terraform apply`), Terraform cannot query the data source during plan because the input doesn't exist. In this case, the data source read is deferred to the apply phase, after the dependency is created and its attributes are known. The plan output shows the data source result as `(known after apply)`.

---

### Question 11 — `replace_triggered_by` vs `depends_on`

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Contrasting TWO key differences between the `replace_triggered_by` lifecycle argument and the `depends_on` meta-argument

**Question**:
Compare `replace_triggered_by` in a `lifecycle` block with the `depends_on` meta-argument. Which TWO statements correctly describe a difference between these two? (Select two.)

- A) `depends_on` controls **execution ordering** — it ensures one resource is fully created before another begins, but it does not force any resource to be destroyed or recreated; `replace_triggered_by` forces the resource to be **destroyed and recreated** when a referenced resource or attribute changes
- B) Both `replace_triggered_by` and `depends_on` are interchangeable — they differ only in syntax
- C) `replace_triggered_by` creates a dependency that also acts as a **replacement trigger** — when the referenced resource changes, the resource with `replace_triggered_by` is scheduled for replacement in the next plan; `depends_on` only controls ordering and never triggers replacement
- D) `depends_on` causes a replacement; `replace_triggered_by` only controls creation order

**Answer**: A, C

**Explanation**:
(A and C together capture the complete contrast.) `depends_on` is an **ordering constraint** — it tells Terraform "resource B cannot be created until resource A is complete" but has no opinion on what happens to resource B when resource A changes in the future. `replace_triggered_by` is a **change-reactive replacement trigger** — it establishes both an ordering dependency and a rule that says "whenever the referenced resource or attribute changes, force this resource to be replaced." A common use case is coupling an `aws_autoscaling_group` to its `aws_launch_template` — when the launch template is updated, `replace_triggered_by` ensures the ASG is replaced with the new template, even if no ASG configuration attribute itself changed.

---

### Question 12 — `count` vs `for_each`: Accepted Input Types and Mutual Exclusivity

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Contrasting TWO technical constraints that differ between `count` and `for_each`

**Question**:
Compare `count` and `for_each` on two specific technical constraints. Which TWO statements are correct? (Select two.)

- A) `count` accepts any non-negative integer value; `for_each` accepts a `map` or a `set(string)` — it does NOT accept a plain `list(string)` without first converting it with `toset()`
- B) `for_each` accepts a plain `list(string)` directly — no type conversion is needed
- C) `count` and `for_each` are mutually exclusive — a single resource block cannot use both simultaneously; attempting to do so causes a Terraform validation error
- D) `count` and `for_each` can be combined on a single resource — `count` controls the number of copies and `for_each` assigns a key to each

**Answer**: A, C

**Explanation**:
(A) `for_each` is strict about input types — it accepts a `map(any)` or a `set(string)`, but not a raw `list`. Lists have ordered integer indexes and can contain duplicates, which conflicts with `for_each`'s requirement for unique stable keys. To use a list as a `for_each` input, convert it first: `for_each = toset(var.regions)`. `count` simply takes a non-negative integer. (C) The two meta-arguments are **mutually exclusive** — combining `count` and `for_each` on the same resource block is a validation error. Terraform allows only one instance-creation mechanism per resource. Attempting to use both causes `terraform validate` to fail with an error indicating that only one of `count` or `for_each` may be defined.

---

### Question 13 — `create_before_destroy` vs `replace_triggered_by`: Two Lifecycle Mechanisms Affecting Replacement

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Deep contrast between the two `lifecycle` arguments that both involve resource replacement but serve different purposes

**Question**:
Both `create_before_destroy = true` and `replace_triggered_by = [...]` involve resource replacement in Terraform. Compare them — what distinct problem does each solve, and can they be used together?

- A) They are synonyms — `replace_triggered_by` is the newer name for `create_before_destroy` introduced in Terraform 1.2+
- B) `create_before_destroy = true` controls **when in the replacement sequence** the new resource is provisioned relative to the old one (create-first vs destroy-first) — it does not change what triggers a replacement; `replace_triggered_by = [...]` controls **what events trigger a replacement** — it causes the resource to be replaced when a referenced resource or attribute changes, even if the resource's own configuration hasn't changed; the two are orthogonal and can be combined to get both a triggered replacement and a safe create-before-destroy sequence
- C) `replace_triggered_by` overrides `create_before_destroy` — if both are set, Terraform uses destroy-first ordering regardless
- D) `create_before_destroy` triggers replacements proactively; `replace_triggered_by` only changes the sequencing of a replacement that was already planned for other reasons

**Answer**: B

**Explanation**:
The two lifecycle arguments address completely different dimensions of replacement: **sequencing** vs **triggering**. `create_before_destroy = true` answers the question "in what order do the destroy and create happen?" — it reverses the default destroy-first sequence to create-first, minimising downtime during any replacement regardless of what caused it. `replace_triggered_by = [aws_launch_template.web]` answers the question "what changes should cause this resource to be replaced?" — it adds a dependency-based replacement trigger so that when the referenced resource changes, the current resource is also scheduled for replacement, even if no attribute of the current resource's configuration has changed. Because they control different aspects of replacement, they are orthogonal: a resource can use both to ensure that replacements triggered by upstream changes are also performed in a zero-downtime create-before-destroy order.

---
