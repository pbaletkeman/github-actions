# Terraform Associate (004) — Question Bank Iter 4 Batch 6

**Iteration**: 4
**Iteration Style**: Error identification — spot the mistake in a claim, config, or workflow description
**Batch**: 6
**Objective**: 4c — Custom Conditions & Sensitive Data
**Generated**: 2026-04-25
**Total Questions**: 12
**Difficulty Split**: 2 Easy / 8 Medium / 2 Hard
**Answer Types**: 9 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — Flawed Claim: `check` Block Failure Blocks Apply

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Whether a failing `check` block assertion blocks `terraform apply`

**Question**:
A colleague explains the three Terraform condition mechanisms and states:

> "A failed `check` block assertion works just like a failed `precondition` — it blocks `terraform apply` and prevents the infrastructure from being created or modified."

What is wrong with this explanation?

- A) Nothing — `check` block failures and `precondition` failures both block apply
- B) The explanation is wrong: `check` block failures are **warnings only** — they do not block apply; all resources are still created or updated and the apply exits successfully
- C) The explanation is wrong: `check` block failures are more severe than `precondition` failures — they abort the entire Terraform run including `init`
- D) The explanation is wrong: `check` block failures only block apply if they contain a scoped `data` source

**Answer**: B

**Explanation**:
The `check` block is intentionally **non-blocking**. A failing `assert` condition inside a `check` block produces a **warning** message but does not prevent the apply from completing. All resource changes proceed normally and the apply exits with a success status. This is the key design difference from `precondition`, which halts apply before the target resource is modified. `check` blocks are designed for continuous health monitoring where a failure should be surfaced to operators without risking blocked deployments.

---

### Question 2 — Flawed Claim: `validation` Runs During Apply

**Difficulty**: Easy
**Answer Type**: one
**Topic**: When variable `validation` blocks are evaluated in the Terraform workflow

**Question**:
A team's internal wiki states:

> "Variable `validation` blocks run during `terraform apply`, after the operator has reviewed and approved the execution plan. If a variable fails validation at this point, Terraform aborts the apply."

What is wrong with this description?

- A) Nothing — variable validation runs during `terraform apply` after plan approval
- B) The description is wrong: variable `validation` blocks run **before `terraform plan`** — they are evaluated during input variable processing, which happens before any planning; an invalid variable aborts before a plan is ever generated
- C) The description is wrong: variable `validation` blocks only run when explicitly invoked with `terraform validate`
- D) The description is wrong: variable `validation` blocks run at `terraform init` time when providers are registered

**Answer**: B

**Explanation**:
Variable `validation` blocks are evaluated as part of **input variable processing**, which occurs before `terraform plan` generates any execution plan. A validation failure halts Terraform immediately, displaying the `error_message` — no plan is generated, no diff is computed, and no apply can proceed. The team's wiki is incorrect in describing this as a post-plan, apply-time check. This early evaluation is precisely what makes `validation` useful: catching bad inputs before any infrastructure analysis occurs.

---

### Question 3 — Flawed HCL: `validation` Condition References a Local

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What a `validation` block's `condition` is allowed to reference

**Question**:
An engineer writes the following to validate that an environment variable matches an allowed list defined in a local:

```hcl
locals {
  allowed_envs = ["dev", "staging", "production"]
}

variable "environment" {
  type = string

  validation {
    condition     = contains(local.allowed_envs, var.environment)
    error_message = "environment must be one of: dev, staging, production."
  }
}
```

What is wrong with this configuration?

- A) Nothing — a `validation` block can reference any value available in the module
- B) The configuration is wrong: `validation` block conditions can only reference `var.<variable_name>`; referencing `local.allowed_envs` is not permitted and causes a Terraform error
- C) The configuration is wrong: `contains()` cannot be used inside a `validation` block condition
- D) The configuration is wrong: `locals` must be declared after all `variable` blocks in a Terraform file

**Answer**: B

**Explanation**:
A `validation` block's `condition` argument is restricted to referencing only `var.<variable_name>` — the specific variable being validated. This restriction exists because validation runs before planning, when local values, resources, and data sources have not yet been computed. Referencing `local.allowed_envs` inside a validation condition causes Terraform to raise an error about an invalid reference. The correct fix is to inline the list directly: `contains(["dev", "staging", "production"], var.environment)`.

---

### Question 4 — Flawed Claim: `self` Available in `precondition`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Which lifecycle condition blocks allow use of the `self` reference

**Question**:
An engineer documents lifecycle conditions and writes:

> "`self` is available in both `precondition` and `postcondition` blocks — you can use `self.<attribute>` in either to inspect the resource's current or desired state before or after a change."

What is wrong with this documentation?

- A) Nothing — `self` is available in both `precondition` and `postcondition`
- B) The documentation is wrong: `self` is only available in `postcondition` blocks; `precondition` blocks cannot use `self` because the resource has not yet been created or updated when the precondition is evaluated
- C) The documentation is wrong: `self` is only available in `precondition` blocks; `postcondition` blocks must reference the resource by its full address
- D) The documentation is wrong: `self` is not valid in either block; resource attributes must always be referenced by their full address

**Answer**: B

**Explanation**:
`self` is a special reference that points to the resource instance **after** it has been created or updated. This makes it available only in `postcondition` blocks, where the resource's resulting attributes can be inspected. In a `precondition`, the resource has not yet been modified — there are no new attribute values to reference via `self`. `precondition` conditions typically reference variables, data sources, or other already-known values instead. Using `self` in a `precondition` causes a Terraform error.

---

### Question 5 — Flawed Claim: `check` Block Introduced in Terraform 1.3

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Which Terraform version introduced the `check` block

**Question**:
A team's migration runbook contains the following statement:

> "The `check` block was introduced in Terraform 1.3 as part of the same release that added `moved` blocks, enabling non-blocking infrastructure health assertions."

Which two facts in this statement are incorrect?

- A) Nothing — the `check` block was introduced in 1.3 alongside `moved` blocks
- B) The `check` block was introduced in **Terraform 1.5**, not 1.3; and `moved` blocks were introduced in **Terraform 1.1**, not in the same release as `check` — the two features were added in different versions
- C) The `check` block was introduced in Terraform 1.7; `moved` blocks were introduced in 1.3
- D) The `check` block was introduced in Terraform 1.5; `moved` blocks were also introduced in Terraform 1.5 in the same release

**Answer**: B

**Explanation**:
The `check` block was introduced in **Terraform 1.5**, not 1.3. The `moved` block was introduced earlier, in **Terraform 1.1**, as a way to rename or restructure resources in state without destroying and recreating them. The `import` block (declarative resource import) was introduced in the **same 1.5 release** as `check`. The runbook contains two errors: the wrong version for `check` and the wrong claim that `check` and `moved` were released together.

---

### Question 6 — Flawed Claim: Failed `postcondition` Destroys Resource and Removes from State

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What happens to a resource in AWS and in state when its `postcondition` fails

**Question**:
A developer explains postcondition behaviour to their team:

> "When a `postcondition` fails after a resource is created, Terraform automatically destroys the resource to maintain a consistent, valid state — it removes the resource from AWS and from `terraform.tfstate` so the next apply starts clean."

What is wrong with this explanation?

- A) Nothing — Terraform does automatically destroy resources that fail postconditions
- B) The explanation is wrong: when a `postcondition` fails, the resource **remains in AWS and remains recorded in `terraform.tfstate`**; Terraform does not perform an automatic rollback, destroy, or state removal — the apply exits with a failure status and the team must investigate manually
- C) The explanation is wrong: when a `postcondition` fails, the resource is kept in AWS but is removed from state so the next plan treats it as a new resource to be imported
- D) The explanation is wrong: when a `postcondition` fails, the resource is destroyed in AWS but remains in state as an orphaned record

**Answer**: B

**Explanation**:
A `postcondition` runs **after** the resource change has already been applied. By the time the postcondition is evaluated, the resource exists in AWS and has been written to `terraform.tfstate`. Terraform does not roll back, destroy, or remove the resource on postcondition failure — it exits with a non-zero status and displays the `error_message`. The resource remains in both AWS and state with its actual attributes. This is the critical operational implication of postconditions: failure is caught after the fact, requiring manual investigation, unlike `precondition` which prevents the change entirely.

---

### Question 7 — Flawed HCL: `precondition` Placed Outside `lifecycle` Block

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Where `precondition` and `postcondition` blocks must be declared inside a resource

**Question**:
An engineer writes the following resource to prevent deployment to a test environment:

```hcl
resource "aws_instance" "web" {
  ami           = "ami-0abc123"
  instance_type = "t3.micro"

  precondition {
    condition     = var.environment != "test"
    error_message = "This resource cannot be deployed to the test environment."
  }
}
```

What is wrong with this configuration?

- A) Nothing — `precondition` blocks can be placed directly inside any resource block
- B) The configuration is wrong: `precondition` blocks must be declared **inside a `lifecycle` block** within the resource; placing `precondition` directly as a resource argument is invalid syntax
- C) The configuration is wrong: `precondition` cannot reference variables — it can only reference data source attributes
- D) The configuration is wrong: `precondition` blocks must be placed in a separate `conditions` file, not inside the resource block

**Answer**: B

**Explanation**:
`precondition` and `postcondition` are **nested inside the `lifecycle` block** of a resource or data source — they are not top-level resource arguments. The correct structure is:

```hcl
lifecycle {
  precondition {
    condition     = var.environment != "test"
    error_message = "This resource cannot be deployed to the test environment."
  }
}
```

Placing `precondition` directly in the resource block body is invalid HCL and will cause a parse error. The `lifecycle` block is the only place where `precondition`, `postcondition`, `create_before_destroy`, `prevent_destroy`, `ignore_changes`, and `replace_triggered_by` can be declared.

---

### Question 8 — Two Flawed Claims About `check` Block Structure

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Where `check` blocks can be placed and whether a scoped `data` source is required

**Question**:
A developer documents the `check` block and makes two structural claims:

> **Claim 1**: "A `check` block can be placed inside a resource block to validate that specific resource's post-deployment state, similar to `postcondition`."
> **Claim 2**: "A `check` block must always include both a `data` block and an `assert` block — the `data` block is required; an `assert` block alone is not valid."

Which TWO statements correctly identify what is wrong with these claims? (Select two.)

- A) Claim 1 is **wrong**: `check` blocks are **top-level configuration blocks** — they cannot be nested inside resource blocks; their placement is similar to `resource` or `data` blocks at the root of a module
- B) Claim 1 is **correct**: `check` blocks can be placed inside resource blocks
- C) Claim 2 is **wrong**: the `data` block inside a `check` block is **optional** — a `check` block with only an `assert` block is perfectly valid
- D) Claim 2 is **correct**: a `check` block with only an `assert` and no `data` block causes a Terraform parse error

**Answer**: A, C

**Explanation**:
`check` blocks are **top-level** configuration constructs — they are declared at the same level as `resource`, `data`, `variable`, and `output` blocks. They cannot be nested inside a resource block (Claim 1 is wrong). The scoped `data` block inside a `check` block is entirely optional — `check` blocks with only an `assert` block are valid and commonly used to assert conditions on already-known infrastructure values such as resource attribute lengths or computed expressions (Claim 2 is wrong). The scoped `data` block is used when the assertion requires fetching live external data, such as an HTTP health check.

---

### Question 9 — Flawed Claim: Sensitive Variable Propagates Automatically to Output

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Whether `sensitive = true` on a variable automatically marks referencing outputs as sensitive

**Question**:
A developer marks a variable as sensitive and then creates an output referencing it:

```hcl
variable "db_password" {
  type      = string
  sensitive = true
}

output "db_connection_string" {
  value = "postgresql://admin:${var.db_password}@db.example.com/prod"
}
```

The developer says: "Since I've marked `var.db_password` as sensitive, Terraform will automatically treat `db_connection_string` as sensitive too — no changes to the output block are needed."

What is wrong with this claim?

- A) Nothing — Terraform automatically marks outputs as sensitive when they reference sensitive variables
- B) The claim is wrong: Terraform **detects** that the output references a sensitive value and raises a **plan error** requiring `sensitive = true` to be explicitly added to the output block — automatic propagation does not occur without user action
- C) The claim is wrong: Terraform silently allows the output to expose the sensitive value in plaintext; no error or automatic protection is applied
- D) The claim is wrong: sensitive variables cannot be interpolated into output values at all — they must be referenced directly with `value = var.db_password`

**Answer**: B

**Explanation**:
Terraform does detect sensitivity propagation and will raise a **plan error** when a sensitive value flows into an output that is not marked `sensitive = true`. The error message is: *"Output refers to Terraform-sensitive values. To protect these values, mark the output as sensitive."* This is not automatic protection — it is an error requiring the developer to explicitly add `sensitive = true` to the output block. The requirement is deliberate: Terraform forces conscious acknowledgement rather than silently hiding values that might be important to expose in some contexts.

---

### Question 10 — Two Flawed Claims About Condition Mechanism Failure Behaviour

**Difficulty**: Hard
**Answer Type**: many
**Topic**: When and how `validation`, `precondition`, and `check` failures are raised

**Question**:
A team's study guide contains the following two statements about condition mechanism failure behaviour:

> **Claim 1**: "A failed `validation` block is treated as a non-fatal warning during `terraform plan` — the plan still generates and shows proposed changes, but an advisory message is displayed."
> **Claim 2**: "A failed `precondition` produces a warning and allows the resource to be created anyway — it is the operator's responsibility to act on the warning."

Which TWO statements correctly identify the errors? (Select two.)

- A) Claim 1 is **wrong**: a failed `validation` block is a **fatal error that halts Terraform before any plan is generated** — it is not a warning, and no execution plan is produced
- B) Claim 1 is **correct**: validation failures are non-fatal warnings and the plan proceeds
- C) Claim 2 is **wrong**: a failed `precondition` is a **fatal error that halts `terraform apply` before the resource is created or modified** — the resource is not created and the apply exits with a failure
- D) Claim 2 is **correct**: precondition failures are warnings that allow the resource to be created

**Answer**: A, C

**Explanation**:
Both claims mischaracterize fatal conditions as warnings. `validation` failures (Claim 1) abort Terraform **before plan** — no plan is generated and no changes are shown. It is not a warning. `precondition` failures (Claim 2) abort `terraform apply` **before the resource is changed** — the resource is not created and the apply exits with a non-zero status. It is not a warning. Only `check` block failures are true warnings that allow apply to complete. Confusing these three mechanisms on the exam is a common error; the key is: `validation` = fatal before plan, `precondition`/`postcondition` = fatal during apply, `check` = warning only.

---

### Question 11 — Flawed Claim: Scoped `data` Source in `check` Block Has Fatal Errors Like Top-Level `data`

**Difficulty**: Hard
**Answer Type**: one
**Topic**: How errors from a scoped `data` source inside a `check` block differ from top-level `data` source errors

**Question**:
A developer adds a `check` block with a scoped HTTP data source:

```hcl
check "api_health" {
  data "http" "probe" {
    url = "https://api.internal.example.com/health"
  }

  assert {
    condition     = data.http.probe.status_code == 200
    error_message = "API health check failed with status ${data.http.probe.status_code}."
  }
}
```

During `terraform apply`, the internal API is unreachable and the HTTP request times out. The developer claims: "This HTTP data source inside the `check` block will behave exactly like a top-level `data` source — a connection failure will cause a fatal error and abort the apply."

What is wrong with this claim?

- A) Nothing — a data source error inside a `check` block is fatal and aborts apply, just like a top-level `data` source error
- B) The claim is wrong: a data source error inside a `check` block is treated the same as a failing `assert` condition — it produces a **warning** and does not abort the apply; this is one of the key differences between scoped and top-level `data` sources
- C) The claim is wrong: the scoped `data` source inside a `check` block is never actually executed during apply — it is only evaluated during `terraform plan`
- D) The claim is wrong: `check` block data sources automatically retry three times before deciding whether to fail or warn

**Answer**: B

**Explanation**:
A `data` block scoped inside a `check` block does **not** behave like a top-level `data` block. Top-level data source failures are fatal and abort the apply. A scoped data source inside a `check` block is part of the check's non-blocking context — errors (including connection failures, timeouts, or invalid responses) are treated as check failures and produce a **warning** rather than a fatal error. All other resources in the apply continue unaffected. This is a critical distinction: the same HTTP provider `data "http"` block behaves differently depending on whether it is placed at the top level or inside a `check` block.

---

### Question 12 — Two Flawed Claims About Retrieving Sensitive Output Values

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Which `terraform output` command forms expose sensitive output values in plaintext

**Question**:
A developer sets `sensitive = true` on an output named `db_password` and makes two claims about retrieval:

> **Claim 1**: "Running `terraform output -json` will display sensitive outputs as `(sensitive value)` in the JSON, the same way `terraform output` (no arguments) does — sensitive values are always hidden in any output format."
> **Claim 2**: "Running `terraform output db_password` (querying the single output by name) will display `(sensitive value)` — there is no way to retrieve the actual value using the `terraform output` command."

Which TWO statements correctly identify the errors? (Select two.)

- A) Claim 1 is **wrong**: `terraform output -json` **reveals sensitive output values in plaintext** in the JSON response — it does not redact them as `(sensitive value)`
- B) Claim 1 is **correct**: `terraform output -json` always hides sensitive values
- C) Claim 2 is **wrong**: `terraform output db_password` (querying a single output by name) **displays the actual plaintext value** — only the aggregate `terraform output` (no arguments) listing redacts sensitive values
- D) Claim 2 is **correct**: querying a sensitive output by name always returns `(sensitive value)`

**Answer**: A, C

**Explanation**:
`sensitive = true` on an output suppresses the value in the aggregate `terraform output` listing — it appears as `(sensitive value)`. However, both `terraform output -json` and `terraform output <name>` (single output by name) **display the actual plaintext value**. `terraform output -raw <name>` also returns the raw plaintext. This is an important exam distinction: the sensitive flag is a display guard for the summary view, not a hard access control. It reinforces why the state file must be stored in an encrypted backend — the value is always retrievable by anyone with access to the state or to a terminal running these specific output commands.

---
