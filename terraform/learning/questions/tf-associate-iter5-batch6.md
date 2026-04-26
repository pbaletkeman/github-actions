# Terraform Associate (004) — Question Bank Iter 5 Batch 6

**Iteration**: 5
**Iteration Style**: Comparison and contrast — distinguish between two similar concepts
**Batch**: 6
**Objective**: 4c — Custom Conditions & Sensitive Data
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 3 Easy / 8 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — `validation` Block vs `precondition`: When Each Runs

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contrast between the timing and triggering point of variable validation and lifecycle preconditions

**Question**:
Compare a `validation` block inside a `variable` declaration with a `precondition` block inside a resource's `lifecycle`. What is the key difference in when each runs during the Terraform workflow?

- A) Both run at the same point — they are two syntactic alternatives for the same assertion mechanism
- B) A `validation` block runs **before `terraform plan`** — it is evaluated during input variable processing and halts the run before any infrastructure analysis begins; a `precondition` runs **during `terraform apply`**, just before Terraform modifies the specific resource that contains it
- C) A `precondition` runs before `terraform plan`; a `validation` block runs after the plan is approved but before apply begins
- D) Both run after `terraform apply` completes — they are post-deployment verification tools

**Answer**: B

**Explanation**:
The timing difference is fundamental to understanding when each assertion catches a problem. `validation` runs at the earliest possible moment — during input variable processing, before planning, before any infrastructure is analysed. If the condition fails, no plan is generated. `precondition` runs mid-apply, immediately before Terraform makes changes to the resource that declares it. This means planning has already succeeded and other resources may have already been modified before the precondition is evaluated. Use `validation` for catching bad inputs early; use `precondition` for asserting conditions on resource-level prerequisites that can only be verified once planning is underway.

---

### Question 2 — `precondition` vs `postcondition`: Before vs After the Resource Change

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contrast between what precondition and postcondition assert and when each runs relative to the resource change

**Question**:
Compare a `precondition` and a `postcondition` declared in the same resource's `lifecycle` block. What is the key difference in their execution order and access to resource attributes?

- A) `precondition` and `postcondition` run at the same time — they differ only in whether the condition expression returns true or false
- B) A `precondition` runs **before** the resource is created or updated — it asserts that prerequisites are met and cannot use `self` to reference the resource's new attribute values; a `postcondition` runs **after** the resource change completes — it asserts that the created/updated resource meets requirements, and `self` references the resource's post-change attributes
- C) A `postcondition` runs before the resource change and uses `self` to reference the previous state; a `precondition` runs after and uses `self` to reference the new state
- D) Both can use `self`, but `precondition` uses the current (pre-change) `self` and `postcondition` uses the planned (post-change) `self`

**Answer**: B

**Explanation**:
`precondition` is a **gate before change** — it evaluates conditions that must be true before Terraform is allowed to touch the resource. Because the resource hasn't been modified yet, `self` cannot refer to the resource's new state, so `precondition` conditions typically reference data sources, other resources, or input variables. `postcondition` is a **gate after change** — it asserts that the resource, after being created or updated, meets expectations. The `self` keyword is valid here and refers to the resource as it exists after the apply operation. For example, a postcondition might assert `self.public_ip != null` to verify that the just-created instance has the expected attribute.

---

### Question 3 — `check` Block vs `precondition`: Blocking vs Non-Blocking Failures

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contrast between the failure behaviour of check blocks and preconditions

**Question**:
Compare a failing `check` block assertion with a failing `precondition`. What is the fundamental difference in how Terraform handles each failure?

- A) Both a failing `check` block and a failing `precondition` block apply and allow all resources to be created
- B) A failing `precondition` **blocks** the apply — Terraform halts before modifying the resource and exits with a non-zero status; a failing `check` block assertion is a **warning only** — Terraform displays the error message but all resource changes proceed normally and the apply exits successfully
- C) A failing `check` block blocks the apply; a failing `precondition` produces a warning only
- D) Both a failing `check` block and a failing `precondition` roll back any resources already created in the same apply run

**Answer**: B

**Explanation**:
This is one of the most important distinctions for the exam. `precondition` failures are **hard stops** — when the condition expression returns false, Terraform aborts the apply before modifying the resource, exits with a non-zero status code, and the error_message is displayed. `check` block failures are intentionally **soft** — the assertion failure is surfaced as a warning, but Terraform continues applying all planned changes and exits successfully. The `check` block was designed for continuous health monitoring (e.g., HTTP endpoint availability) where a transient failure should be visible to operators without blocking infrastructure deployment. Use `precondition` when a condition must be satisfied for the resource to be safe to create; use `check` for ongoing health assertions that should never block a deployment.

---

### Question 4 — `validation` Block Scope vs `precondition` Scope: What Each Can Reference

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between the expression scope allowed in validation conditions and precondition conditions

**Question**:
Compare what a `validation` block's `condition` and a `precondition`'s `condition` are each allowed to reference in their expressions. What is the key difference?

- A) Both are restricted to referencing only `var.<name>` — neither can reference data sources or resource attributes
- B) A `validation` block's `condition` can **only** reference `var.<variable_name>` — the specific variable being validated; a `precondition`'s `condition` can reference any value available at plan time, including data sources, other resource attributes, locals, and variables
- C) A `precondition`'s `condition` can only reference `self`; a `validation` block's `condition` can reference any expression including resources
- D) Both can reference any expression — the difference is only in when they are evaluated, not what they can reference

**Answer**: B

**Explanation**:
The scope restriction exists because of when each mechanism runs. `validation` runs before planning — at that point, only input variable values are available. Terraform enforces this by requiring that the `condition` only reference `var.<variable_name>`. Any attempt to reference a resource, data source, or local will produce a validation error at parse time. `precondition` runs during the apply phase, where the full configuration has been planned — resources, data sources, locals, and other variables are all resolved and available. This broader scope is what makes `precondition` suitable for checking things like whether a referenced AMI has the correct architecture (`data.aws_ami.ubuntu.architecture == "x86_64"`) or whether a dependency has the expected attribute value.

---

### Question 5 — `sensitive = true` on Variable: Terminal Masking vs State Protection

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between what sensitive=true on a variable actually protects vs what it does not protect

**Question**:
A variable is declared `sensitive = true` for a database password. Compare the protection this provides in two contexts: terminal output and the Terraform state file. What is the accurate description of each?

- A) `sensitive = true` both masks the value in terminal output AND encrypts it in the state file — it provides complete protection in both contexts
- B) `sensitive = true` **masks** the value in terminal output during `plan` and `apply` (showing `(sensitive value)` instead of the plaintext), but it provides **no protection** in the state file — the value is still stored in plaintext in `terraform.tfstate`; protecting it at rest requires an encrypted remote backend
- C) `sensitive = true` protects the value in the state file but has no effect on terminal output — the plaintext value is always shown during plan and apply
- D) `sensitive = true` removes the value from the state file entirely — it is never persisted anywhere

**Answer**: B

**Explanation**:
`sensitive = true` is a **display filter** — it tells Terraform to redact the value whenever it would appear in terminal output, plan files, or error messages, replacing it with `(sensitive value)`. This prevents the value from appearing in CI/CD logs or on-screen during operator runs. However, it provides absolutely no protection for the state file: the plaintext value is written to `terraform.tfstate` just like any other attribute. Anyone with read access to the state file can see the password. To protect sensitive values at rest, teams must use an encrypted remote backend such as HCP Terraform (which encrypts state) or S3 with server-side encryption, combined with strict IAM access controls.

---

### Question 6 — `validation` Block vs `check` Block: Where Each Is Declared

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between the structural location of a validation block and a check block in HCL

**Question**:
Compare where a `validation` block and a `check` block are declared in a Terraform configuration. How does their structural location differ, and what does that difference imply?

- A) Both `validation` and `check` blocks are top-level HCL blocks — they sit at the root of any `.tf` file alongside `resource` and `provider` blocks
- B) A `validation` block is declared **nested inside a `variable` block** — it is scoped to that specific variable and can only reference that variable; a `check` block is a **top-level block** — it sits at the root of a `.tf` file like a `resource` or `output` block, and it can reference any infrastructure value in scope, optionally including a scoped `data` source
- C) A `check` block is nested inside a `resource` block's `lifecycle`; a `validation` block is a top-level block
- D) Both are nested inside the `lifecycle` block of the resource they guard

**Answer**: B

**Explanation**:
The structural location of each block reflects its purpose and scope. A `validation` block is nested inside the `variable` block it validates — it is tightly scoped and can only reference `var.<variable_name>`. Multiple `validation` blocks can exist within a single variable. A `check` block is a top-level block — it stands independently at the root of any `.tf` file and can reference any value available in the module, including resource attributes and data source results. Optionally, a `check` block can declare its own scoped `data` source inside it (which is only used for the check's assertions and not available elsewhere in the module). This top-level placement reflects the `check` block's role as an independent infrastructure health assertion rather than an input constraint.

---

### Question 7 — `precondition` Failure vs `postcondition` Failure: Resource State After Each

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between the resource's state in AWS after a precondition failure vs a postcondition failure

**Question**:
An `aws_instance` resource has both a `precondition` and a `postcondition`. Compare the state of the EC2 instance in AWS after each type of failure:

- **Scenario A**: The `precondition` condition evaluates to `false` during apply
- **Scenario B**: The `postcondition` condition evaluates to `false` during apply

What is the difference in the real-world resource state between the two scenarios?

- A) In both scenarios, the EC2 instance is created in AWS — Terraform only removes it after the failure is acknowledged
- B) In Scenario A (precondition failure), the EC2 instance is **never created** in AWS — Terraform halts before making the API call; in Scenario B (postcondition failure), the EC2 instance **has been created** in AWS — Terraform only evaluates the postcondition after the API call succeeds, meaning the resource exists even though Terraform's apply failed
- C) In both scenarios, Terraform automatically destroys the resource and returns the infrastructure to its pre-apply state
- D) In Scenario A, the EC2 instance is created and then immediately destroyed; in Scenario B, the instance is never created

**Answer**: B

**Explanation**:
`precondition` acts as a **pre-flight check** — if it fails, Terraform aborts before making the API call to create or modify the resource. The resource does not exist in AWS. `postcondition` acts as a **post-creation check** — Terraform only evaluates it after successfully creating or updating the resource. If the postcondition then fails, the resource **already exists** in AWS, but the Terraform apply has exited with an error. This is an important operational distinction: a postcondition failure leaves real infrastructure behind. The resource may be in an unexpected state (e.g., an instance without a public IP), and the operator must investigate and potentially taint or destroy the resource manually before the configuration can be remediated.

---

### Question 8 — `sensitive` Variable Propagation vs Explicit `sensitive` Output

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between automatic sensitive propagation and explicit output sensitivity declaration

**Question**:
Compare two approaches for preventing a database password from appearing in terminal output:

- **Approach A**: Declare `variable "db_password" { sensitive = true }` and use `var.db_password` in a resource; do not declare any output
- **Approach B**: Declare the variable without `sensitive`, but declare `output "db_password" { value = var.db_password; sensitive = true }`

What is a key difference between these two approaches?

- A) Both approaches are exactly equivalent in all scenarios — sensitive propagation and explicit output sensitivity are interchangeable
- B) In Approach A, the `sensitive = true` on the variable causes Terraform to **automatically propagate** the sensitive marker to any expression that uses `var.db_password` — plan output, error messages, and any output that references it will be masked without needing explicit `sensitive = true` on the output; in Approach B, only the specific `output` block is masked — `var.db_password` itself is not marked sensitive, so if it appears in plan output or error messages elsewhere in the configuration, it may not be automatically redacted
- C) Approach B is more secure than Approach A because explicitly marking an output as sensitive also encrypts the value in the state file
- D) Approach A prevents the value from being stored in state; Approach B stores it in state but masks it in terminal output

**Answer**: B

**Explanation**:
When `sensitive = true` is set on the **variable**, Terraform marks the value itself as sensitive throughout the entire configuration. Any expression that references `var.db_password` — whether in a resource argument, a local, or an output — inherits the sensitive marker automatically. Plan output will show `(sensitive value)` for all such references, and if an undeclared output tries to expose the value, Terraform forces the user to acknowledge `sensitive = true`. In Approach B, only the declared output is masked — the variable value itself has no sensitive marker, so if it appears in a plan diff for a resource argument, it might be shown in plaintext. For comprehensive protection across the entire configuration, marking the variable as sensitive is the more thorough approach.

---

### Question 9 — `check` Block With Scoped Data Source vs `check` Block Without

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between a check block that embeds a scoped data source and one that references module-level values

**Question**:
Compare these two `check` block patterns:

**Pattern A** — scoped data source inside check:
```hcl
check "endpoint_health" {
  data "http" "ping" {
    url = "https://${aws_lb.web.dns_name}/health"
  }
  assert {
    condition     = data.http.ping.status_code == 200
    error_message = "Health endpoint returned ${data.http.ping.status_code}."
  }
}
```

**Pattern B** — no scoped data source:
```hcl
check "db_endpoint_set" {
  assert {
    condition     = can(tostring(aws_db_instance.main.endpoint))
    error_message = "Database endpoint attribute is not set."
  }
}
```

What is the key difference in scope between the two patterns?

- A) Pattern A is invalid — a `check` block cannot contain a `data` source; only `assert` blocks are allowed
- B) In Pattern A, the `data "http" "ping"` source is **scoped to the `check` block** — it is only evaluated during the check and its result is not accessible elsewhere in the module; in Pattern B, no data source is needed because the assert references an attribute directly from a resource already in state — the check block's assert can reference any module-level resource or data source without embedding a scoped source
- C) Pattern B is invalid because a `check` block must always contain a `data` source block
- D) The scoped data source in Pattern A is automatically added to the module's data sources and can be referenced from other resources

**Answer**: B

**Explanation**:
A `check` block optionally accepts a single scoped `data` source declaration inside it. This scoped data source is evaluated only during the check's execution — it is not accessible from any other part of the module. This is useful when the health assertion requires fetching external data (e.g., an HTTP call to a load balancer endpoint) that has no other use in the configuration. Pattern B omits the scoped data source entirely and instead references `aws_db_instance.main.endpoint` directly — this is valid because `check` block assert conditions can reference any resource or data source that is already in scope at the module level. The two patterns represent different approaches to sourcing the data being checked, not a difference in the check's blocking/non-blocking behaviour.

---

### Question 10 — Three Condition Mechanisms: Failure Behaviour Comparison

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Contrasting TWO failure behaviour differences across the three assertion mechanisms

**Question**:
Terraform provides three condition assertion mechanisms: `validation`, `precondition`/`postcondition`, and `check`. Which TWO statements correctly describe a contrast in their failure behaviour? (Select two.)

- A) A failing `validation` condition halts Terraform **before any plan is generated** — the run aborts with the error_message and no infrastructure analysis is performed; a failing `check` assertion produces a **warning** that is displayed but does not prevent the apply from completing
- B) A failing `precondition` halts the apply **before modifying the target resource** — the resource is never created or updated; a failing `postcondition` halts the apply **after the resource has been created or updated** — the resource already exists in the cloud provider when the failure is reported
- C) `validation`, `precondition`, and `postcondition` all produce warnings only — none of them block the apply
- D) A failing `check` assertion blocks the apply and triggers a rollback of any resources created during the run

**Answer**: A, B

**Explanation**:
(A) captures the contrast between `validation` (earliest possible failure — before plan) and `check` (non-blocking warning). `validation` prevents any planning from occurring; `check` allows full apply completion and only emits a warning. (B) captures the contrast between the two `lifecycle` conditions: `precondition` fails before the resource is touched (resource does not exist), while `postcondition` fails after the resource has been created/updated (resource exists in a potentially unexpected state). Options C and D are incorrect: `validation` and `precondition`/`postcondition` are hard stops that fail the run; `check` never blocks or rolls back anything.

---

### Question 11 — `validation` Block vs `check` Block: What Each Can Reference

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Deep contrast between the expression scope and structural rules governing validation conditions and check assertions

**Question**:
Both `validation` blocks and `check` block assertions use a `condition` expression that must evaluate to a boolean. Compare the expression scope rules for each — what can each reference, and what structural rule constrains the `check` block that does not apply to `validation`?

- A) Both `validation` and `check` block conditions are restricted to referencing only `var.<name>` — no other values are permitted in either context
- B) A `validation` block's `condition` can only reference `var.<variable_name>` — all other references are forbidden; a `check` block's `assert` condition can reference any module-level value (resources, data sources, locals, variables); however, if a `check` block includes a scoped `data` source, the data source reference in the assert must use the `data.<type>.<name>` form scoped to the check — and that scoped source cannot be referenced outside the `check` block
- C) A `check` block's condition is restricted to referencing only `data` source attributes; a `validation` block can reference any value including resource attributes
- D) Both can reference resource attributes and data source results — the only difference is that `validation` is evaluated earlier in the workflow

**Answer**: B

**Explanation**:
`validation` blocks enforce a strict **scope restriction** at parse time: the condition can only reference `var.<variable_name>`. This is enforced because validation runs before planning when no other values exist. `check` block assertions have a **broad scope** — they can reference any module-level resource, data source, or local that is available at plan/apply time. The key structural nuance for `check` blocks is the scoped `data` source option: a `check` block can optionally declare one `data` source block inside it, which is evaluated exclusively for the check. That scoped data source is addressed as `data.<type>.<name>` within the assert condition, but it is not part of the module's global namespace and cannot be referenced by any resource, output, or local outside the `check` block. This isolation prevents check-only data fetches from accidentally affecting resource dependencies.

---

### Question 12 — `postcondition` with `self` vs `precondition` with External References

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Contrasting TWO specific technical rules about expression references in preconditions and postconditions

**Question**:
Compare the expression reference rules for `precondition` and `postcondition` blocks. Which TWO statements correctly describe a difference? (Select two.)

- A) `self` is valid **only** in a `postcondition` — it refers to the resource after it has been created or updated; `self` is **not valid** in a `precondition` because the resource hasn't been created/modified yet when the precondition is evaluated
- B) A `precondition` can reference any value that is known at plan time — including other resource attributes, data sources, locals, and variables — but not `self`; a `postcondition` can reference all of those plus `self` to inspect the resource's own post-change attributes
- C) `self` is valid in both `precondition` and `postcondition`; the difference is that `self` in a `precondition` refers to the planned state while `self` in a `postcondition` refers to the applied state
- D) A `postcondition` can only reference `self` — it cannot reference other resources or module-level values

**Answer**: A, B

**Explanation**:
(A) `self` is reserved for `postcondition` — it refers to the resource or data source that contains the `lifecycle` block, as it exists after the create/update operation. Because `precondition` runs before the resource is touched, there is no "self" resource yet (or the resource exists in its previous state), so `self` is not permitted. (B) `precondition` has access to the full module scope at plan time — it can reference data sources, other resources' attributes, locals, and variables. This is what makes preconditions powerful for asserting cross-resource prerequisites (e.g., "the AMI I'm about to use must be x86_64"). After the resource change, `postcondition` adds `self` to that same scope, allowing assertions on the resource's own resulting attributes. (C) is incorrect — `self` is not valid in `precondition`. (D) is incorrect — `postcondition` can reference module-level values in addition to `self`.

---

### Question 13 — `sensitive = true` vs Vault Dynamic Secrets: Protection Depth

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Deep contrast between Terraform-native sensitive masking and HashiCorp Vault dynamic secrets as competing approaches to secret handling

**Question**:
Compare two approaches to managing a database password in Terraform:

- **Approach A**: Declare `variable "db_password" { sensitive = true }` and pass the password via `TF_VAR_db_password` at apply time
- **Approach B**: Use the Vault provider with a `data "vault_generic_secret"` data source to retrieve credentials dynamically during apply — no password variable in the Terraform configuration

What are the two most significant security differences between these approaches?

- A) Approach A is more secure because `sensitive = true` encrypts the value in state; Approach B stores the Vault token in state instead
- B) In Approach A, the password value **exists in the Terraform state file in plaintext** (despite the `sensitive = true` masking in terminal output) — anyone with read access to state can retrieve it; in Approach B, the credentials are **fetched dynamically** at apply time and are typically **short-lived** (dynamic secrets with a TTL) — they may never be stored in state as a sensitive value, and the Vault token used for authentication can be separately controlled and rotated independently of the database password
- C) Both approaches store the password in state — the only difference is that Approach B uses a second-factor Vault token alongside it
- D) Approach B is less secure because Vault data source results are never marked sensitive and will always appear in plaintext in terminal output and state

**Answer**: B

**Explanation**:
`sensitive = true` is a **display control** — it prevents the password from appearing in terminal output and plan files, but the value is written to `terraform.tfstate` in plaintext. Any user or system with read access to the state file (a CI agent, a developer with S3 bucket access, a leaked backup) can retrieve the password. The Vault dynamic secrets approach eliminates this problem in two ways: (1) the actual database credential never exists in the Terraform configuration at all — it is fetched from Vault at runtime; (2) Vault can issue **dynamic credentials** with a short TTL that are unique per apply run and automatically expire, so even if they were captured from a log, they would soon be invalid. The Vault provider does still store the data source results in state (data source results are always in state), but those credentials may be short-lived. The separation of concerns — Vault manages secrets, Terraform manages infrastructure — is the architectural benefit that goes beyond what `sensitive = true` alone can provide.

---
