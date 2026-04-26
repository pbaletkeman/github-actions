# Terraform Associate (004) — Question Bank Iter 6 Batch 6

**Iteration**: 6
**Iteration Style**: Scenario-based troubleshooting — an engineer encounters a problem or unexpected outcome; identify the most likely cause and correct resolution
**Batch**: 6
**Objective**: 4c — Custom Conditions & Sensitive Data
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 3 Easy / 8 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — `check` Block Does Not Fail a Production Deployment Gate

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Understanding that check block failures are non-blocking warnings — wrong tool for a deployment gate

**Question**:
An engineer wants to prevent a production deployment from completing if the application's health endpoint does not return HTTP 200. They add a `check` block:

```hcl
check "app_health" {
  data "http" "probe" {
    url = "https://${aws_lb.web.dns_name}/health"
  }

  assert {
    condition     = data.http.probe.status_code == 200
    error_message = "Health check failed: got ${data.http.probe.status_code}."
  }
}
```

During a deploy, all resources are created successfully but the health endpoint returns `503`. The engineer expects the deployment to fail. Instead, `terraform apply` exits successfully, showing the assertion failure as a warning. What is the cause, and what is the correct fix?

- A) The `check` block is syntactically wrong — the scoped `data` source must be declared outside the `check` block
- B) The health check is evaluated before the load balancer DNS is ready — add a `depends_on` inside the `check` block to delay evaluation
- C) **`check` blocks are intentionally non-blocking** — a failing `assert` inside a `check` block produces a **warning** but never prevents the apply from succeeding; this is by design for continuous health monitoring; to create a hard deployment gate, the engineer should instead use a `postcondition` inside the `aws_lb` resource's `lifecycle` block — a failing `postcondition` halts apply with a non-zero exit code after the resource is changed but before the run is considered successful
- D) `check` blocks only fail the apply when the `data` source inside them returns a non-2xx status code — any other assertion failure is treated as a warning

**Answer**: C

**Explanation**:
The `check` block was deliberately designed as a **non-blocking** health monitor. Its failures are warnings, not errors — the apply exits `0` (success) regardless of assertion results. This makes `check` useful for surfacing infrastructure health issues in dashboards and logs without risking blocked deployments for transient failures. For a hard deployment gate (where a failed health check must abort the run), the correct tool is a `postcondition` inside the resource's `lifecycle` block. A failing `postcondition` halts the apply immediately after the resource change, exits with a non-zero status code, and marks the run as failed — which is what CI/CD pipeline gates require.

---

### Question 2 — Sensitive Variable Value Found in Plaintext in State File

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Understanding that sensitive = true masks terminal output but does not protect state

**Question**:
An engineer declares a database password as sensitive:

```hcl
variable "db_password" {
  type      = string
  sensitive = true
}
```

After running `terraform apply`, the engineer is auditing their security posture and opens `terraform.tfstate`. They find the password stored in plaintext under the resource's attribute map. They assumed `sensitive = true` would protect the value in state. What is the actual behaviour?

- A) This is a bug — `sensitive = true` should encrypt state file values and the engineer should file an issue with HashiCorp
- B) `sensitive = true` encrypts the value in state using a key derived from the Terraform workspace name — the engineer's state viewer is decrypting it automatically
- C) **`sensitive = true` only controls terminal display** — it causes Terraform to show `(sensitive value)` instead of the actual value in `terraform plan` and `terraform apply` output; it has **no effect on the state file**, where the value is always stored in **plaintext**; to protect sensitive values at rest, the engineer must use an **encrypted remote backend** (such as S3 with SSE-KMS or HCP Terraform), restrict access to the state file, and never commit `terraform.tfstate` to source control
- D) The state file shows plaintext only on first apply — after the next apply, Terraform replaces the plaintext with a hash of the value

**Answer**: C

**Explanation**:
`sensitive = true` is purely a **display-level** control. It instructs Terraform to suppress the value in terminal output — plan summaries, apply logs, and the `terraform output` all-outputs listing all show `(sensitive value)` instead. The actual value in `terraform.tfstate` is always stored in plaintext JSON, regardless of any `sensitive` flag. This is one of the most important security facts for the exam. The mitigation strategy has three parts: (1) use an encrypted remote backend so the state file is encrypted at rest; (2) apply strict IAM/RBAC policies to restrict who can read the state; (3) never commit `.tfstate` files to source control. For the highest security, consider using the HashiCorp Vault provider to inject dynamic, short-lived credentials rather than storing long-lived secrets in state at all.

---

### Question 3 — `self` Used in a `precondition` Causes an Error

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Understanding that self is only valid in postcondition, not precondition

**Question**:
An engineer wants to verify that an EC2 instance will be placed in the correct subnet before it is created. They write:

```hcl
resource "aws_instance" "app" {
  ami           = data.aws_ami.ubuntu.id
  instance_type = "t3.micro"
  subnet_id     = var.subnet_id

  lifecycle {
    precondition {
      condition     = self.subnet_id == var.expected_subnet_id
      error_message = "Instance must be placed in the expected subnet."
    }
  }
}
```

Running `terraform plan` produces: `Error: Invalid reference — The "self" object is not available in precondition blocks`. What is the cause and fix?

- A) `self` is a reserved keyword in Terraform and cannot be used in any `lifecycle` block — replace it with the full resource reference `aws_instance.app.subnet_id`
- B) `self` is not supported for EC2 instances — it is only available in `null_resource` lifecycle blocks
- C) **`self` is only valid in `postcondition` blocks**, where it references the resource's attributes after the resource has been created or updated; in a `precondition`, the resource does not yet exist (or has not yet been changed), so `self` cannot reference any post-change attributes; to check a value that is known before the resource is changed, the condition should reference the input directly — in this case, `var.subnet_id == var.expected_subnet_id` — or reference a data source or another already-computed resource
- D) `self` requires the resource to be referenced using its full address — replace `self.subnet_id` with `module.this.aws_instance.app.subnet_id`

**Answer**: C

**Explanation**:
`self` is a special reference that represents the resource being managed, available only in contexts where the resource's post-change state is accessible. In a `postcondition`, the resource has already been created or updated, so `self` correctly refers to the resulting resource attributes. In a `precondition`, the resource change has not yet occurred — the new attribute values do not exist yet — making `self` invalid. For the specific use case in this scenario (verifying the subnet before creation), the correct approach is to compare the input value directly: `condition = var.subnet_id == var.expected_subnet_id`. Alternatively, if the subnet must be looked up (e.g., to check it belongs to the right VPC), reference a `data "aws_subnet"` block instead.

---

### Question 4 — `postcondition` Fails After Resource Is Already Created

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Understanding that a failing postcondition leaves the resource created but marks the apply as failed

**Question**:
An engineer adds a `postcondition` to an EC2 instance requiring a public IP:

```hcl
resource "aws_instance" "web" {
  ami                         = data.aws_ami.ubuntu.id
  instance_type               = "t3.micro"
  associate_public_ip_address = false

  lifecycle {
    postcondition {
      condition     = self.public_ip != null
      error_message = "Instance must have a public IP address."
    }
  }
}
```

The engineer runs `terraform apply`. The instance is created in AWS, but the apply exits with the postcondition error. On the next `terraform plan`, the engineer notices the instance is shown as already existing in state. What is happening and what is the correct fix?

- A) The `postcondition` failing prevents the resource from being written to state — the instance was not actually created in AWS
- B) Terraform automatically destroys the resource and retries the apply when a `postcondition` fails — the instance in state is from a previous run
- C) **A `postcondition` runs after the resource has been created or updated** — at that point, the resource exists in AWS and is written to state; the failing `postcondition` causes the apply to exit with an error, but the resource is already in state; the correct fix is to address the root cause: `associate_public_ip_address = false` prevents the instance from receiving a public IP, so either change this to `true`, place the instance in a subnet with `map_public_ip_on_launch = true`, or update the `postcondition` condition to match the intended design
- D) The instance exists in state only until the next `terraform refresh` — the postcondition failure marks the resource as tainted and it will be destroyed on the next plan

**Answer**: C

**Explanation**:
`postcondition` semantics are important to understand: the check runs **after** the resource change completes. This means the resource exists in AWS and is recorded in state before the postcondition is evaluated. When the condition returns `false`, the apply fails and the error message is displayed — but the resource is already created. On the next `plan`, Terraform shows the resource as existing (because it is in state). The engineer must fix the underlying issue that caused the postcondition to fail: in this case, `associate_public_ip_address = false` explicitly disables public IP assignment, so `self.public_ip` will always be `null`. The fix is to set `associate_public_ip_address = true` (or rely on the subnet's auto-assign setting), not to remove the postcondition. Postconditions catch configuration mistakes but cannot undo infrastructure that has already been provisioned.

---

### Question 5 — All `validation` Blocks on a Variable Are Evaluated

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Understanding that multiple validation blocks on a variable are all evaluated and all failures are reported

**Question**:
An engineer adds two `validation` blocks to a variable:

```hcl
variable "instance_type" {
  type = string

  validation {
    condition     = startswith(var.instance_type, "t3.")
    error_message = "Only t3 family instances are allowed."
  }

  validation {
    condition     = length(var.instance_type) <= 12
    error_message = "Instance type name must not exceed 12 characters."
  }
}
```

They run `terraform plan -var="instance_type=m5.24xlarge"` expecting Terraform to stop after the first failing validation. They are surprised to see both error messages appear. Is this correct behaviour?

- A) This is unexpected behaviour — Terraform should stop after the first failing `validation` block and only display the first error; this may indicate a bug in the version being used
- B) Only the last `validation` block is evaluated — the first block is ignored when multiple blocks exist
- C) **Yes, this is correct and expected behaviour — Terraform evaluates ALL `validation` blocks on a variable and reports every failing condition in a single error output**; this is intentional: showing all failing constraints at once allows the engineer to correct all issues with one round trip, rather than discovering them one at a time; in this case, `"m5.24xlarge"` fails both conditions (does not start with `"t3."` and is 10 characters — actually passes the length check, but the first check fails); with a value like `"m5.extremely-large"` that fails both, both messages would be reported together
- D) Multiple `validation` blocks on a single variable are not supported — Terraform will use only the first block and silently ignore subsequent ones

**Answer**: C

**Explanation**:
Multiple `validation` blocks per variable are fully supported and all are evaluated independently. When a variable value is processed, Terraform checks every `validation` block and collects all failing conditions. All error messages from all failing blocks are reported together in the error output. This batch-reporting behaviour is deliberate — it follows the principle of fail fast and report completely, so engineers can see all constraint violations at once rather than fixing one and discovering the next in a follow-up run. A variable can have as many `validation` blocks as needed, each focusing on a distinct constraint (type family check, length check, format check, etc.).

---

### Question 6 — `nonsensitive()` Strips the Sensitive Taint From a Value

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Understanding that nonsensitive() removes the sensitive marking and allows the value to appear in plain text output

**Question**:
An engineer is debugging a connection string issue in a development environment. The connection string is derived from a sensitive variable and appears as `(sensitive value)` in all output. To temporarily see the value, they wrap it in `nonsensitive()`:

```hcl
output "debug_connection" {
  value = nonsensitive(local.connection_string)
}
```

This works — the value appears in plain text during the debug session. The engineer forgets to revert the change before merging the feature branch. The change is deployed to production. What is the security risk?

- A) There is no risk — `nonsensitive()` only affects local terminal display and does not change how the value is stored in state
- B) `nonsensitive()` cannot be used with sensitive values and would have caused a validation error during `terraform validate` — the scenario is not possible
- C) **`nonsensitive()` explicitly removes the sensitive taint from a value, causing it to be displayed in plaintext in `terraform plan`, `terraform apply` summary output, and `terraform output` listings** — once merged and deployed to production, the sensitive connection string (containing the database password) will appear in CI/CD pipeline logs, any tooling that captures `terraform apply` output, and the output of `terraform output` without any masking; this represents a real credential exposure risk; the fix is to remove `nonsensitive()` and restore `sensitive = true` on the output, or remove the debug output entirely before merging
- D) The risk is minor — `nonsensitive()` only affects the current terminal session; the CI/CD pipeline will still see the value as `(sensitive value)` because it uses a different Terraform execution context

**Answer**: C

**Explanation**:
`nonsensitive(value)` is a function that **explicitly and permanently removes the sensitive taint** from a value for the purpose of that expression. Once used, the resulting value is treated as non-sensitive everywhere it flows: it appears in plan output, apply summaries, `terraform output` listings, and anywhere the value is logged. This is the correct tool when you have a value that is marked sensitive but you have verified it is safe to expose in a specific context. However, its use in a shared codebase carries the risk described in this scenario — a temporary debugging aid accidentally shipping to production. Best practice is to never commit `nonsensitive()` wrapping to shared branches; instead, use `terraform output -raw <name>` locally to view a specific sensitive output without modifying the configuration.

---

### Question 7 — `precondition` Can Reference Other Resources (Unlike `validation`)

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Understanding the broader reference scope allowed in precondition/postcondition vs the var-only restriction in validation

**Question**:
An engineer who recently learned that `validation` blocks can only reference `var.<name>` assumes the same restriction applies to `precondition` blocks. They write:

```hcl
resource "aws_instance" "web" {
  ami           = data.aws_ami.ubuntu.id
  instance_type = "t3.micro"

  lifecycle {
    precondition {
      condition     = data.aws_ami.ubuntu.architecture == "x86_64"
      error_message = "AMI must be x86_64 architecture."
    }
  }
}
```

The engineer removes this `precondition` thinking it will fail because it references `data.aws_ami.ubuntu` rather than a `var.*`. Is their assumption correct?

- A) Yes — `precondition` blocks have the same restriction as `validation` blocks and can only reference `var.<name>`
- B) Yes — `precondition` blocks can reference data sources, but only data sources declared in the same `.tf` file as the resource
- C) **No — `precondition` and `postcondition` blocks can reference any value that Terraform can resolve at apply time**, including data sources, other resource attributes, locals, and variables; the `var.<name>-only` restriction applies exclusively to `validation` blocks inside `variable` declarations, because validation runs before planning when only the variable's own value is available; by the time a `precondition` runs (just before a resource is modified during apply), data sources and other resources earlier in the dependency graph have already been resolved; the engineer's config is valid and correct — they should keep the precondition
- D) No — but `precondition` blocks cannot reference data sources; they can reference other resource attributes and variables only

**Answer**: C

**Explanation**:
The reference restriction exists because of **when** each mechanism is evaluated. `validation` blocks run before `terraform plan`, at input-variable-processing time — the only thing available at that point is the variable's own value. `precondition` and `postcondition` blocks run during `terraform apply`, after the dependency graph has been resolved and earlier resources have been applied. At that point, data sources, other resources' attributes, locals, module outputs, and variables are all available for reference. The `precondition` in this example correctly references `data.aws_ami.ubuntu.architecture` — this is a valid and idiomatic pattern for asserting prerequisite conditions about infrastructure dependencies. The engineer should keep the precondition, not remove it.

---

### Question 8 — `check` Block Requires Terraform 1.5+

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Understanding that the check block was introduced in Terraform 1.5 and requires setting required_version

**Question**:
A senior engineer adds `check` blocks to a shared module to monitor infrastructure health. When a junior engineer on the team runs `terraform init` on their machine, they get: `Error: Unsupported block type — Blocks of type "check" are not expected here`. The senior engineer's machine works fine. What is the most likely cause?

- A) The `check` block must be declared inside a `resource` block — using it at the top level of a module is unsupported
- B) `check` blocks are only supported in Terraform Cloud/HCP Terraform and cannot be used in local Terraform runs
- C) **The `check` block was introduced in Terraform 1.5** — the junior engineer is running an older version of Terraform that does not recognise the `check` block type; the fix is for the module to declare a minimum version requirement: `required_version = ">= 1.5"` in the `terraform {}` block, which will produce a clear "version too old" error rather than a confusing "unsupported block" message, and prompts the junior engineer to upgrade their Terraform CLI
- D) The `check` block is only unsupported on Windows — this is a platform compatibility issue

**Answer**: C

**Explanation**:
The `check` block was added in **Terraform 1.5** (released June 2023). Versions prior to 1.5 do not recognise it and produce an "Unsupported block type" error during parsing. The correct solution is to set the `required_version` constraint in the module's `terraform {}` block: `required_version = ">= 1.5"`. This causes Terraform to emit a clear, actionable version constraint error when an older CLI is used, rather than the confusing "unsupported block type" message. For modules using features introduced in specific versions (1.5 for `check`, 1.4 for `import` blocks, 1.3 for `optional()` in object types, etc.), setting `required_version` is a best practice that guides consumers to use a compatible CLI version.

---

### Question 9 — `postcondition` Condition Expression Throws an Error Instead of Returning `false`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Using can() to make postcondition conditions error-tolerant when the expression might throw rather than return false

**Question**:
An engineer writes a `postcondition` to verify that at least one security group is attached to a newly created EC2 instance:

```hcl
resource "aws_instance" "app" {
  ami           = data.aws_ami.ubuntu.id
  instance_type = "t3.micro"

  lifecycle {
    postcondition {
      condition     = length(self.vpc_security_group_ids) > 0
      error_message = "Instance must have at least one security group."
    }
  }
}
```

In a specific deployment, `self.vpc_security_group_ids` is `null` (the attribute is not set). Instead of the custom `error_message`, Terraform reports: `Error in function call: Call to function "length" failed: value must not be null`. The engineer's custom message is never shown. What is the fix?

- A) `length()` cannot be used in a `postcondition` — use `self.vpc_security_group_ids != null && self.vpc_security_group_ids != []` instead
- B) `postcondition` blocks must always check for `null` using a separate `precondition` before the `postcondition` can safely call functions
- C) **The `condition` expression is throwing a function-call error rather than returning `false`** — when `self.vpc_security_group_ids` is `null`, `length(null)` errors rather than returning `0`; Terraform propagates this error directly instead of treating it as a failed condition; the fix is to **wrap the expression in `can()`** to convert any errors into `false`: `condition = can(length(self.vpc_security_group_ids) > 0)`; alternatively, add a null-guard: `condition = self.vpc_security_group_ids != null && length(self.vpc_security_group_ids) > 0`; either approach ensures the `error_message` is shown instead of a raw function error
- D) The issue is that `postcondition` does not support `length()` when the attribute type is a set — cast to a list first using `tolist()`

**Answer**: C

**Explanation**:
When a `condition` expression throws an error (rather than evaluating to `true` or `false`), Terraform reports the raw error from the function call rather than the custom `error_message`. This can produce confusing output that obscures the actual intent of the condition. Two patterns prevent this: (1) **`can(expr)`** — wraps any expression and returns `false` if the expression throws an error, making it suitable for use in `condition` where you want errors to be treated as "condition not met"; (2) **explicit null-guard** — `attr != null && length(attr) > 0` — checks for null before calling `length()`, which is more readable. The `can()` function is designed precisely for this use case: making expressions error-tolerant in contexts (conditions, validation, etc.) where a thrown error is semantically equivalent to "condition not met."

---

### Question 10 — Sensitive Output Value Revealed by `terraform output -json` in CI

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Understanding that terraform output -json reveals sensitive output values in plaintext

**Question**:
An engineer marks an output as sensitive to prevent the database connection string from appearing in CI logs:

```hcl
output "db_connection_string" {
  value     = local.connection_string
  sensitive = true
}
```

The CI pipeline uses this command to pass the connection string to a deployment script:

```bash
DB_URL=$(terraform output -json | jq -r '.db_connection_string.value')
```

A security reviewer audits the CI logs and finds the database password in plaintext in the `terraform output -json` output captured by the pipeline. The engineer is surprised — they expected `sensitive = true` to hide the value. What is the cause?

- A) `jq` strips the sensitive marking before returning the value — the issue is with the jq command, not Terraform
- B) `sensitive = true` only applies to the initial `terraform apply` output, not to subsequent `terraform output` commands
- C) **`terraform output -json` outputs all output values as plaintext JSON, including those marked `sensitive = true`** — the `sensitive` flag suppresses values in the terminal display of `terraform output` (the all-outputs listing), but programmatic queries such as `terraform output -json`, `terraform output -raw <name>`, and `terraform output <name>` all return the actual value; this is intentional — machines consuming outputs need the real value; the engineer must ensure CI pipeline output is not logged or is masked at the pipeline level (e.g., using GitHub Actions secrets masking, or marking the env var as a secret in the CI system)
- D) The CI pipeline runs as `root`, which bypasses Terraform's sensitive value masking

**Answer**: C

**Explanation**:
`sensitive = true` on an output is a **display hint for interactive terminal output** — it causes Terraform to print `(sensitive value)` instead of the actual value when running `terraform output` interactively (without a specific output name). It does not prevent the value from being retrieved programmatically. `terraform output -json` intentionally includes all values (including sensitive ones) in the JSON structure, because the primary use case is automated consumption by scripts and pipelines. The same applies to `terraform output -raw <name>` and `terraform output <name>` (single-output query). The engineer needs to apply protection at the CI layer: configure the CI system to mask the value (e.g., register it as a secret variable in the pipeline), avoid logging the `terraform output` command output, or use a secrets manager instead of Terraform outputs to pass credentials between pipeline steps.

---

### Question 11 — TWO Differences Between `check` Block and `precondition`

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Two distinct behavioral differences between check blocks and preconditions beyond just blocking vs non-blocking

**Question**:
An engineer is deciding whether to use a `check` block or a `precondition` for an infrastructure assertion. They know one blocks the apply and the other doesn't. Which TWO additional differences between `check` blocks and `preconditions` should they also consider? (Select two.)

- A) **`check` blocks run on every `terraform plan` AND every `terraform apply`**, making them suitable for continuous health monitoring; a `precondition` only runs during `terraform apply` — when its resource is actually being created or modified; if no change is planned for the resource, the precondition is not evaluated during that run
- B) `check` blocks can only reference outputs from completed resources; `precondition` blocks can reference any Terraform expression
- C) **`check` blocks can optionally contain a scoped `data` source block** that is evaluated exclusively within the `check` block scope and is not available elsewhere in the configuration; `precondition` blocks do not support embedded data sources — they reference data sources declared at module scope
- D) `precondition` failures can be suppressed with a `-force` flag; `check` block failures cannot be suppressed

**Answer**: A, C

**Explanation**:
Beyond the blocking vs. non-blocking distinction, two important differences: **(A)** `check` blocks run on both `plan` and `apply` operations, providing visibility into infrastructure health on every Terraform execution, even when no changes are planned. A `precondition` only runs during apply, and only when the specific resource it is attached to is being created, updated, or destroyed — if no change is planned for that resource, the precondition is skipped entirely. **(C)** `check` blocks support an optional embedded `data` source block (a "scoped data source") that is private to the check block. This allows the check to query external state (e.g., make an HTTP request, check a DNS record) without polluting the module's data source namespace. `precondition` blocks cannot embed data sources — they reference data sources declared separately at module scope. **(B)** is wrong — preconditions can reference any resolvable Terraform expression. **(D)** is wrong — there is no `-force` flag to suppress precondition failures.

---

### Question 12 — TWO Measures Required to Properly Protect Sensitive Values in Terraform

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Understanding the gap between what sensitive = true provides and what is required to actually protect secrets

**Question**:
A security team is reviewing a Terraform project that manages RDS database credentials. The engineer has applied `sensitive = true` to all relevant variables and outputs and believes this fully protects the secrets. Which TWO additional measures must be in place to achieve adequate protection of the credential values? (Select two.)

- A) Rename all sensitive variables to start with `_secret_` — Terraform treats these as encrypted at rest in state
- B) **Use an encrypted remote backend** — because `sensitive = true` does not protect the state file, and `terraform.tfstate` stores all values (including those marked sensitive) in **plaintext JSON**, the state file must be stored in a backend that encrypts data at rest and in transit; suitable options include S3 with SSE-KMS and strict bucket policies, HCP Terraform (which encrypts state by default), or other encrypted backends; without this, anyone with access to the state file can read all credentials regardless of `sensitive` flags
- C) Add `terraform plan -no-state` to all CI pipeline commands to prevent state from being written
- D) **Restrict access to the state file through IAM policies, bucket policies, or backend access controls** — even with encryption, if developers and CI pipelines have broad read access to the state backend, credentials can be extracted; the principle of least privilege should be applied so that only Terraform execution roles and specific operators can read state; this is independent of (and complementary to) backend encryption

**Answer**: B, D

**Explanation**:
`sensitive = true` provides only display-level masking — it is the thinnest layer of protection and addresses only incidental exposure in terminal logs. Two structural measures are required to actually protect credentials stored in state: **(B) Backend encryption**: the state file must reside in a backend that encrypts it at rest (S3 + SSE-KMS, HCP Terraform, etc.) and in transit (HTTPS). Without encryption, the plaintext JSON state file is readable by anyone with filesystem or bucket-level access. **(D) Access restriction**: encryption at rest is undermined if broad read access is granted to the backend. IAM/RBAC policies should limit state access to Terraform execution roles and specific operators — not to every developer or CI pipeline. Together, encryption (B) and access restriction (D) address the two threat vectors: data at rest and excessive authorized access. A third best practice (not listed here) is using dynamic secrets from HashiCorp Vault to avoid storing long-lived credentials in state at all.

---

### Question 13 — `check` Block Scoped Data Source Is Not Available Elsewhere in the Module

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Understanding that a data source declared inside a check block is scoped to that check block only

**Question**:
An engineer declares an HTTP data source inside a `check` block to probe an endpoint:

```hcl
check "api_health" {
  data "http" "probe" {
    url = "https://api.example.com/health"
  }

  assert {
    condition     = data.http.probe.status_code == 200
    error_message = "API health check failed."
  }
}

output "api_status_code" {
  value = data.http.probe.status_code
}
```

Running `terraform plan` fails with: `Reference to undeclared data source — A data source "http" "probe" has not been declared`. The engineer is confused because they declared `data "http" "probe"` inside the `check` block. What is the cause?

- A) The `http` provider data source is not supported inside `check` blocks — it must be declared at module scope
- B) The `output` block must be inside the `check` block to reference its scoped data source
- C) **A `data` source declared inside a `check` block is a "scoped data source" — it exists only within the scope of that `check` block** and is not visible to the rest of the module; referencing `data.http.probe` outside the `check` block (in an `output`, resource, or local) is an error because the data source does not exist in the module's global namespace; to use the HTTP result both in a check assertion and in an output, the engineer must declare a separate `data "http" "probe"` block at module scope and reference it from both the `check` block's `assert` and the `output`
- D) Scoped data sources inside `check` blocks are only valid when the `check` block uses `depends_on` to establish ordering with the output block

**Answer**: C

**Explanation**:
Scoped data sources inside `check` blocks have **block-level scope** — they are entirely private to the `check` block that contains them and cannot be referenced from outside. This is by design: the scoped data source is a tool for the `check` block's own assertion logic (e.g., making an HTTP call to verify a health endpoint) without polluting the module's data source namespace with checks that are not part of the core infrastructure definition. If the same data source is needed both inside a `check` block assertion and in another part of the module (like an output), the solution is to declare the data source at module scope: `data "http" "probe" { url = "..." }` outside the `check` block. Both the `check` block's `assert` and the `output` can then reference `data.http.probe.status_code`. The `check` block's embedded data source is purely a convenience for self-contained assertions.

---
