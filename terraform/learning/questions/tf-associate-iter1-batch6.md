# Terraform Associate (004) — Question Bank Iter 1 Batch 6

**Iteration**: 1
**Iteration Style**: Foundational recall — define terms, name commands, identify blocks
**Batch**: 6
**Objective**: 4c — Custom Conditions & Sensitive Data
**Generated**: 2026-04-25
**Total Questions**: 12
**Difficulty Split**: 2 Easy / 8 Medium / 2 Hard
**Answer Types**: 9 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — Three Condition Mechanisms Overview

**Difficulty**: Easy
**Answer Type**: one
**Topic**: The three Terraform condition mechanisms and where each is declared

**Question**:
Which of the following correctly lists all three Terraform mechanisms for asserting conditions on infrastructure?

- A) `assert` block, `check` block, `guard` block
- B) `validation` block, `precondition`/`postcondition` in `lifecycle`, `check` block
- C) `validation` block, `check` block, `enforce` block
- D) `precondition` block, `postcondition` block, `verify` block

**Answer**: B

**Explanation**:
Terraform provides three condition assertion mechanisms: (1) `validation` blocks inside `variable` declarations for validating input values before planning, (2) `precondition` and `postcondition` blocks inside a resource's `lifecycle` block for asserting conditions before and after resource changes, and (3) top-level `check` blocks for continuous infrastructure health assertions that produce warnings without blocking applies.

---

### Question 2 — `validation` Block — When It Runs

**Difficulty**: Easy
**Answer Type**: one
**Topic**: When variable `validation` blocks are evaluated

**Question**:
When does a `validation` block inside a `variable` declaration run?

- A) After `terraform apply` completes, to verify the applied configuration
- B) During `terraform init`, when providers are downloaded
- C) Before `terraform plan`, as part of input variable evaluation
- D) Only when explicitly triggered by `terraform validate`

**Answer**: C

**Explanation**:
Variable `validation` blocks run before `terraform plan` evaluates any infrastructure — they are evaluated as part of input variable processing. If the `condition` expression returns false, Terraform displays the `error_message` and halts before generating the plan. This early failure prevents invalid inputs from reaching the planning stage.

---

### Question 3 — `validation` Block Condition Constraints

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What a `validation` condition can reference

**Question**:
Which restriction applies to the `condition` expression inside a variable `validation` block?

- A) It must return a number, which is compared to zero to determine pass/fail
- B) It can only reference `var.<variable_name>` — not resources, data sources, or locals
- C) It must use only built-in string functions; numeric comparisons are not allowed
- D) It can reference any variable in the same module but not variables from child modules

**Answer**: B

**Explanation**:
A `validation` block's `condition` is restricted to referencing only `var.<variable_name>` — the variable being validated. This restriction exists because validation runs before planning, when no resource, data source, or local values are available. Options C and D describe nonexistent restrictions; numeric comparisons are perfectly valid in validation conditions.

---

### Question 4 — `precondition` vs `postcondition` — Timing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: When `precondition` and `postcondition` run relative to resource changes

**Question**:
What is the key timing difference between a `precondition` and a `postcondition` in a resource's `lifecycle` block?

- A) `precondition` runs during `terraform plan`; `postcondition` runs during `terraform validate`
- B) `precondition` runs before the resource is changed during apply; `postcondition` runs after the resource change completes
- C) `precondition` validates input variables; `postcondition` validates output values
- D) Both run at the same time — the distinction is only in the error message text

**Answer**: B

**Explanation**:
`precondition` is evaluated before Terraform applies a change to the resource. If it fails, the apply is halted before the resource is modified. `postcondition` is evaluated after the resource change completes and can reference `self` to inspect the resulting resource attributes. If a postcondition fails, the apply is marked as failed even though the resource was already changed.

---

### Question 5 — `self` in `postcondition`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: The `self` reference available in `postcondition` blocks

**Question**:
What does `self` refer to in a `postcondition` block within a resource's `lifecycle`?

- A) The Terraform module that contains the resource
- B) The provider that manages the resource
- C) The resource instance that was just created or updated
- D) The previous state of the resource before the change

**Answer**: C

**Explanation**:
In a `postcondition` block, `self` is a special reference that points to the resource instance that was just created or updated during the current apply. This allows the postcondition to inspect the actual resulting attributes — for example, checking that `self.public_ip != null` to confirm a public IP was assigned. `self` is only available in `postcondition`, not in `precondition`.

---

### Question 6 — `check` Block Failure Behaviour

**Difficulty**: Medium
**Answer Type**: one
**Topic**: How a failing `check` block assertion affects `terraform apply`

**Question**:
What happens when an `assert` condition inside a `check` block evaluates to `false` during `terraform apply`?

- A) The apply is immediately halted and all changes are rolled back
- B) Terraform pauses and prompts the user to confirm whether to continue despite the failed assertion
- C) A warning is displayed but the apply continues and completes successfully
- D) The resource associated with the `check` block is marked as tainted for replacement on the next run

**Answer**: C

**Explanation**:
The `check` block is intentionally designed to be non-blocking. When an `assert` condition fails, Terraform emits a warning message but does **not** fail the apply. This makes `check` blocks suitable for continuous health monitoring — for example, verifying an HTTP endpoint returns a 200 status code after deployment — without risking accidental apply failures that would block infrastructure changes.

---

### Question 7 — `check` Block Introduction Version

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Which Terraform version introduced the `check` block

**Question**:
In which version of Terraform was the `check` block introduced?

- A) Terraform 1.0
- B) Terraform 1.3
- C) Terraform 1.5
- D) Terraform 1.7

**Answer**: C

**Explanation**:
The `check` block was introduced in **Terraform 1.5**. It provides a top-level mechanism for asserting infrastructure health conditions on every plan and apply, producing warnings rather than errors when assertions fail. The same release also introduced the HCL `import` block for managing existing resource imports declaratively.

---

### Question 8 — `check` Block Optional Data Source

**Difficulty**: Medium
**Answer Type**: one
**Topic**: The optional scoped data source inside a `check` block

**Question**:
What is the purpose of including a `data` block inside a `check` block?

- A) To import an existing resource into state as part of the health check
- B) To provide a scoped, read-only data source that is used only within that `check` block's assertions
- C) To declare a dependency so the check runs after a specific resource is created
- D) To override the provider used for the assertion data retrieval

**Answer**: B

**Explanation**:
A `check` block can optionally contain a nested `data` block that is scoped exclusively to that check. This allows the assertion to query live infrastructure (for example, making an HTTP request to a health endpoint) using data that is not needed anywhere else in the configuration. The scoped `data` block does not affect the rest of the configuration's dependency graph.

---

### Question 9 — Sensitive Output `sensitive = true`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Effect of `sensitive = true` on an `output` block

**Question**:
An output block is declared with `sensitive = true`. What is the effect on how Terraform handles this output?

- A) The output is encrypted with a provider-managed key before being stored in state
- B) The output value is shown in plan output but hidden during `terraform output` commands
- C) The output value is redacted in terminal display but still stored in plaintext in the state file
- D) The output is excluded from `terraform output` and cannot be retrieved programmatically

**Answer**: C

**Explanation**:
Marking an output as `sensitive = true` causes Terraform to display `(sensitive value)` instead of the actual value in terminal output during `plan`, `apply`, and `terraform output`. However, the value is still written in plaintext to `terraform.tfstate`. To actually protect sensitive output values, you must use a remote backend with encryption at rest and restrict access to the state file.

---

### Question 10 — Sensitive Data in State — Critical Fact

**Difficulty**: Hard
**Answer Type**: one
**Topic**: How `sensitive = true` interacts with `terraform.tfstate` storage

**Question**:
A team marks their database password variable and output as `sensitive = true`. A new engineer asks why the password is still visible when they open `terraform.tfstate` in a text editor. What is the correct explanation?

- A) The password is only encrypted if the team has enabled KMS integration in the provider block
- B) `sensitive = true` protects values in transit between modules but not in the final state file
- C) `sensitive = true` only controls terminal display — all attribute values are always stored in plaintext in the state file regardless of this setting
- D) The state file should be unreadable — the engineer must have the wrong version of Terraform

**Answer**: C

**Explanation**:
`sensitive = true` is purely a display control. It prevents the value from appearing in `terraform plan` and `terraform apply` terminal output. It has no effect on how the value is stored in `terraform.tfstate` — all resource and output attribute values, including those marked sensitive, are stored in **plaintext JSON** in the state file. Protecting secrets at rest requires using an encrypted remote backend (such as S3 with SSE, or HCP Terraform) and restricting who can access the state file.

---

### Question 11 — Mitigations for Sensitive Data in State

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Recommended approaches to protect sensitive values stored in Terraform state

**Question**:
Which TWO of the following are correct mitigations for the risk that sensitive values are stored in plaintext in Terraform state? (Select two.)

- A) Set `sensitive = true` on all variables and outputs containing secrets
- B) Use a remote backend that supports encryption at rest, such as S3 with server-side encryption or HCP Terraform
- C) Never commit `terraform.tfstate` to source control and restrict access to the state storage location
- D) Use the `prevent_destroy` lifecycle argument to prevent state files from being accidentally deleted

**Answer**: B, C

**Explanation**:
`sensitive = true` (option A) only masks terminal output and does not protect data in state — it is not a mitigation for state exposure. `prevent_destroy` (option D) protects resources from being destroyed, not state files. The correct mitigations are: using an **encrypted remote backend** (B) to protect data at rest, and **restricting access to the state file** and never committing it to version control (C).

---

### Question 12 — Comparing the Three Condition Mechanisms

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Failure behaviour of `validation`, `precondition`/`postcondition`, and `check`

**Question**:
Which TWO of the following statements correctly describe how Terraform condition mechanisms handle failures? (Select two.)

- A) A failed `validation` block halts execution before `terraform plan` generates an execution plan
- B) A failed `check` block assertion causes `terraform apply` to roll back all changes made in the current run
- C) A failed `precondition` block halts `terraform apply` before the resource is modified
- D) A failed `postcondition` block converts the failing resource to a warning and allows subsequent resources to continue applying

**Answer**: A, C

**Explanation**:
`validation` failures (A) stop Terraform before planning — invalid variable values are caught immediately. `precondition` failures (C) abort the apply before the target resource is modified, protecting the resource from being created in an invalid state. By contrast, `check` block failures (B) never abort the apply — they are warnings only. `postcondition` failures (D) do fail the apply; they do not produce warnings and do not allow the rest of the run to continue unaffected.

---
