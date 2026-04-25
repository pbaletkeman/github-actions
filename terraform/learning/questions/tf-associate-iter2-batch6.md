# Terraform Associate (004) — Question Bank Iter 2 Batch 6

**Iteration**: 2
**Iteration Style**: Behavioral prediction — what happens when code runs or a scenario occurs
**Batch**: 6
**Objective**: 4c — Custom Conditions & Sensitive Data
**Generated**: 2026-04-25
**Total Questions**: 12
**Difficulty Split**: 2 Easy / 8 Medium / 2 Hard
**Answer Types**: 9 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — Validation Failure Halts Plan Before Infrastructure

**Difficulty**: Easy
**Answer Type**: one
**Topic**: What happens when a variable validation condition fails during terraform plan

**Question**:
A variable is declared with a validation block:

```hcl
variable "environment" {
  type    = string

  validation {
    condition     = contains(["dev", "staging", "production"], var.environment)
    error_message = "environment must be one of: dev, staging, production."
  }
}
```

An engineer runs `terraform apply -var="environment=test"`. What happens?

- A) The apply proceeds but logs a warning about the unrecognised value
- B) Terraform evaluates the plan and fails only when the invalid value reaches a resource argument
- C) Terraform fails before generating any plan and displays: "environment must be one of: dev, staging, production."
- D) The apply succeeds because `"test"` satisfies the `string` type constraint

**Answer**: C

**Explanation**:
Variable `validation` blocks are evaluated as part of input variable processing, which occurs before `terraform plan` evaluates any infrastructure. When the `condition` expression returns `false`, Terraform immediately displays the `error_message` and halts — no plan is generated and no resource changes are proposed. The engineer must supply a value from the allowed list before any planning can proceed.

---

### Question 2 — Failing check Block Assertion Does Not Block Apply

**Difficulty**: Easy
**Answer Type**: one
**Topic**: What happens to terraform apply when a check block assertion evaluates to false

**Question**:
A `check` block asserts that a load balancer's HTTP health endpoint returns status code 200:

```hcl
check "lb_health" {
  data "http" "endpoint" {
    url = "https://${aws_lb.web.dns_name}/health"
  }

  assert {
    condition     = data.http.endpoint.status_code == 200
    error_message = "Health check returned ${data.http.endpoint.status_code}, expected 200."
  }
}
```

During `terraform apply`, all resources are created successfully but the health endpoint returns `503`. What is the exit behaviour of the apply?

- A) The apply exits with a non-zero status code because the `check` assertion failed
- B) Terraform rolls back the resources created during this apply
- C) Terraform displays a warning about the failed assertion and the apply exits successfully
- D) The `aws_lb.web` resource is marked as tainted for replacement on the next run

**Answer**: C

**Explanation**:
`check` blocks are intentionally **non-blocking**. A failing `assert` condition inside a `check` block produces a warning message in the output but does **not** cause the apply to fail or roll back. All resources remain created and the apply exits with a zero status code. This design allows continuous health monitoring without risking accidental apply failures that would block infrastructure changes.

---

### Question 3 — Precondition Prevents Resource Modification

**Difficulty**: Medium
**Answer Type**: one
**Topic**: State of the resource when a precondition fails during apply

**Question**:
A resource has a `precondition` in its `lifecycle` block:

```hcl
resource "aws_instance" "web" {
  ami           = data.aws_ami.ubuntu.id
  instance_type = var.instance_type

  lifecycle {
    precondition {
      condition     = data.aws_ami.ubuntu.architecture == "x86_64"
      error_message = "The selected AMI must be x86_64 architecture."
    }
  }
}
```

During `terraform apply`, the AMI is resolved as `arm64`. Has the `aws_instance` been created or modified in AWS when the error is reported?

- A) Yes — Terraform creates the instance first and then evaluates the precondition, marking it failed
- B) No — the precondition is evaluated before the resource is changed; no instance is created or modified
- C) The instance is partially provisioned — Terraform starts the API call and rolls back on failure
- D) The instance is created but Terraform removes it from state because the precondition failed

**Answer**: B

**Explanation**:
A `precondition` runs **before** the resource change is applied. If the condition is false, Terraform raises the error and halts the apply without making any API call to create or modify the resource. The instance does not exist in AWS and there is no partial state. This contrasts with a `postcondition`, which runs after the resource has already been changed.

---

### Question 4 — Postcondition Failure: Resource Exists in Cloud and State

**Difficulty**: Medium
**Answer Type**: many
**Topic**: What is true about a resource after its postcondition fails

**Question**:
An `aws_instance` resource has a `postcondition` checking that a public IP was assigned:

```hcl
lifecycle {
  postcondition {
    condition     = self.public_ip != null
    error_message = "Instance must have a public IP address."
  }
}
```

The instance is provisioned in a subnet without auto-assign public IP, so `self.public_ip` is `null`. Which TWO statements are true after `terraform apply` runs? (Select two.)

- A) The `aws_instance` does not exist in AWS — Terraform rolls back resource creation when a postcondition fails
- B) The `aws_instance` exists in AWS with its actual attributes
- C) The `aws_instance` IS recorded in `terraform.tfstate`
- D) The `aws_instance` is NOT recorded in `terraform.tfstate` because the postcondition invalidated the create

**Answer**: B, C

**Explanation**:
A `postcondition` runs **after** the resource change completes. By the time the postcondition is evaluated, the instance has already been created — it exists in AWS and Terraform has already written it to state. The apply exits with a non-zero status (failure) and the error message is displayed, but the resource is tracked in state with its actual attributes. The team must investigate the failure; the resource is not automatically destroyed or removed from state.

---

### Question 5 — Sensitive Variable Redacted in Plan Output

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What terraform plan displays for a sensitive variable value in a resource argument

**Question**:
A variable and resource are declared as:

```hcl
variable "db_password" {
  type      = string
  sensitive = true
}

resource "aws_db_instance" "main" {
  username = "admin"
  password = var.db_password
}
```

`terraform plan` is run. What does the plan output show for the `password` argument of `aws_db_instance.main`?

- A) The actual password value in plaintext
- B) `(known after apply)` — sensitive values are deferred to apply time
- C) `(sensitive value)` — the value is redacted in the plan display
- D) The `password` field is omitted entirely from the plan output

**Answer**: C

**Explanation**:
When a sensitive variable is used in a resource argument, `terraform plan` displays `(sensitive value)` in place of the actual value. This redaction applies consistently across `plan` output, `apply` output, and in logs. It does not prevent the value from being used — the plan is still valid and apply will use the correct value. The redaction is purely cosmetic; the value exists in the state file in plaintext.

---

### Question 6 — Validation Condition References a Data Source

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What happens when a validation condition references a data source instead of var.<name>

**Question**:
An engineer writes a validation block that references a data source:

```hcl
variable "availability_zone" {
  type = string

  validation {
    condition     = contains(data.aws_availability_zones.available.names, var.availability_zone)
    error_message = "Must be a valid availability zone."
  }
}
```

What happens when `terraform plan` is run with this configuration?

- A) Terraform reads the data source first and then evaluates the validation condition successfully
- B) Terraform raises an error because validation conditions can only reference `var.<variable_name>`
- C) The validation is silently skipped because the data source dependency cannot be resolved at variable evaluation time
- D) Terraform prompts for the data source to be initialised before validation can proceed

**Answer**: B

**Explanation**:
Variable `validation` blocks are restricted to referencing only `var.<variable_name>` — the specific variable being validated. They cannot reference resources, data sources, locals, or other variables. This restriction exists because validation runs before planning, when data sources have not yet been read. Attempting to reference `data.aws_availability_zones.available.names` inside a validation condition causes Terraform to raise an error identifying the invalid reference.

---

### Question 7 — Scoped Data Source in check Block Errors

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Effect on terraform apply when a scoped data source inside a check block fails

**Question**:
A `check` block contains a scoped `data "http"` source that makes an HTTP request to a monitoring endpoint. During `terraform apply`, the HTTP request fails with a connection timeout. How does this affect the rest of the apply?

- A) The entire apply is aborted because the `check` block's data source returned an error
- B) Terraform marks the check as failed with a warning and continues applying all other resources normally
- C) The data source failure is silently ignored and the `check` block assertion is treated as passing
- D) Terraform retries the HTTP request three times before failing the entire apply

**Answer**: B

**Explanation**:
`check` blocks are non-blocking by design. A data source error inside a `check` block's scope is treated the same as a failing `assert` condition — it produces a **warning**, not a fatal error. All other resources in the configuration continue to be created or updated normally. This distinguishes the scoped `data` block inside a `check` block from regular top-level `data` sources, which would abort the apply on error.

---

### Question 8 — Consuming Sensitive Output Without Marking It Sensitive

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What happens when a sensitive module output is referenced in an unmarked output block

**Question**:
A child module exposes a sensitive output:

```hcl
# modules/database/outputs.tf
output "db_password" {
  value     = aws_db_instance.main.password
  sensitive = true
}
```

The root module references this output in its own output block **without** marking it sensitive:

```hcl
# root outputs.tf
output "exposed_password" {
  value = module.database.db_password
}
```

What happens when `terraform plan` runs?

- A) Terraform automatically propagates the `sensitive` flag and redacts the value without requiring any change
- B) Terraform raises an error requiring `exposed_password` to also be marked `sensitive = true`
- C) The plan succeeds with a deprecation warning advising the team to add `sensitive = true`
- D) The value is shown in plaintext in the plan output because the root module does not inherit the sensitive flag

**Answer**: B

**Explanation**:
When a sensitive value flows into an output block that is **not** marked `sensitive = true`, Terraform raises a plan error: *"Output refers to Terraform-sensitive values. To protect these values, mark the output as sensitive."* Terraform does not automatically propagate the `sensitive` flag across module boundaries — the developer must explicitly declare `sensitive = true` on the consuming output. This deliberate requirement prevents accidental exposure of sensitive values through output chains.

---

### Question 9 — check Block Runs During terraform plan

**Difficulty**: Medium
**Answer Type**: one
**Topic**: When check block assertions are evaluated — plan, apply, or both

**Question**:
A configuration contains a `check` block with an `assert` that evaluates to `false` at the time `terraform plan` is run. What does the plan output include?

- A) The plan fails with an error and no proposed changes are shown — the assertion must pass before planning
- B) The plan succeeds; the assertion failure is displayed as a warning alongside the proposed infrastructure changes
- C) The plan output omits the `check` block results — assertions are only evaluated during `terraform apply`
- D) Terraform skips `check` block evaluation during plan if there are pending infrastructure changes

**Answer**: B

**Explanation**:
`check` blocks run on **every** `terraform plan` **and** `terraform apply`. A failing `assert` during plan produces a warning in the output — it does not block the plan, suppress proposed changes, or cause a non-zero exit. This allows teams to see continuous health assertion results alongside planned infrastructure changes during normal development workflow. The same non-blocking behaviour applies during apply.

---

### Question 10 — Interactive Prompt Then Validation Evaluation

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Sequence of events when a variable has no default and requires interactive input

**Question**:
A required variable has a validation block but no `default`:

```hcl
variable "environment" {
  type = string

  validation {
    condition     = contains(["dev", "staging", "production"], var.environment)
    error_message = "environment must be one of: dev, staging, production."
  }
}
```

No `.tfvars` file, environment variable, or `-var` flag provides a value. `terraform plan` is run interactively. Which TWO statements correctly describe what Terraform does? (Select two.)

- A) Terraform prompts the user interactively at the terminal to enter a value for `environment`
- B) Terraform fails immediately with the validation error because no value was provided
- C) After the user enters a value, the `validation` condition is evaluated against it
- D) The validation block is permanently skipped when the value comes from an interactive prompt

**Answer**: A, C

**Explanation**:
When a required variable (no `default`) has no value from any source, Terraform interactively prompts the user at the terminal. After the user provides input, Terraform applies all validation blocks to the supplied value. If the entered value does not satisfy the condition — for example, entering `"test"` — the `error_message` is displayed and the plan fails. Interactive input does not bypass validation.

---

### Question 11 — Precondition Passes, Postcondition Fails

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Final state of a resource after precondition passes and postcondition fails

**Question**:
A resource has both a `precondition` and a `postcondition` in its `lifecycle` block. During `terraform apply`:
1. The `precondition` evaluates to `true` — the resource creation proceeds
2. The resource is successfully created in AWS
3. The `postcondition` evaluates to `false` — the condition is not met

What is the final state of the resource after apply completes?

- A) The resource is destroyed in AWS — Terraform automatically cleans up resources that fail postconditions
- B) The resource exists in AWS and IS recorded in `terraform.tfstate`; the apply exits with a failure status
- C) The resource exists in AWS but is NOT recorded in `terraform.tfstate` — the postcondition failure marks it as unmanaged
- D) The resource is recorded in `terraform.tfstate` as `tainted` so it will be replaced on the next plan

**Answer**: B

**Explanation**:
When a `postcondition` fails, the resource has already been created and Terraform has already written it to state. Terraform does not roll back, destroy the resource, or remove it from state. The apply exits with a non-zero status and the postcondition `error_message` is shown. The resource remains in the cloud and in state with its actual attributes — the team must investigate and resolve the issue manually or by adjusting the configuration. This is the critical contrast with `precondition`, which prevents the change from happening at all.

---

### Question 12 — Mixed Sensitive and Non-Sensitive Outputs via terraform output

**Difficulty**: Hard
**Answer Type**: many
**Topic**: What terraform output (no arguments) shows for sensitive vs non-sensitive outputs

**Question**:
A configuration defines two outputs:

```hcl
output "instance_id" {
  value = aws_instance.web.id
}

output "db_password" {
  value     = aws_db_instance.main.password
  sensitive = true
}
```

An engineer runs `terraform output` (with no arguments) after apply. Which TWO statements correctly describe what is displayed? (Select two.)

- A) Both `instance_id` and `db_password` display their actual values
- B) `instance_id` is shown with its actual value
- C) `db_password` is shown as `(sensitive value)` rather than the actual password
- D) All outputs are hidden — `terraform output` without arguments only lists output names, not values

**Answer**: B, C

**Explanation**:
`terraform output` (no arguments) lists all outputs with their values. Non-sensitive outputs such as `instance_id` are displayed with their actual values. Sensitive outputs such as `db_password` are displayed as `(sensitive value)` — the flag suppresses the value in aggregate display. To retrieve the actual sensitive value directly, the engineer would use `terraform output db_password` (single output by name) or `terraform output -json`, both of which reveal the plaintext value — reinforcing why encrypted backend storage is necessary even when `sensitive = true` is set.

---
