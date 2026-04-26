# Terraform Associate (004) — Question Bank Iter 3 Batch 6

**Iteration**: 3
**Iteration Style**: HCL interpretation — read a snippet and identify output/effect
**Batch**: 6
**Objective**: 4c — Custom Conditions & Sensitive Data
**Generated**: 2026-04-25
**Total Questions**: 12
**Difficulty Split**: 3 Easy / 7 Medium / 2 Hard
**Answer Types**: 9 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — `validation` Condition with `startswith()`

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Reading a `validation` block using `startswith()` to identify which value passes the condition

**Question**:
Read this variable declaration:

```hcl
variable "instance_type" {
  type = string

  validation {
    condition     = startswith(var.instance_type, "t3.")
    error_message = "Only t3 family instances are allowed."
  }
}
```

Which value satisfies this `validation` condition?

- A) `"t2.micro"` — starts with `"t"`, which is within the `"t3."` family
- B) `"T3.micro"` — the prefix `"T3."` is equivalent to `"t3."` in Terraform comparisons
- C) `"t3.small"` — the value begins with `"t3."`, satisfying the `startswith()` condition
- D) `"m5.t3.large"` — contains `"t3."` somewhere in the string

**Answer**: C

**Explanation**:
`startswith(string, prefix)` returns `true` only when the string begins with the exact prefix — the check is case-sensitive and must match at position 0. `"t3.small"` begins with `"t3."` and passes. `"t2.micro"` begins with `"t2."` — close, but not `"t3."`. `"T3.micro"` fails because Terraform string comparisons are case-sensitive. `"m5.t3.large"` contains `"t3."` but does not start with it, so `startswith()` returns `false`.

---

### Question 2 — `self` Reference in a `postcondition`

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Reading a `postcondition` block to identify what `self` refers to and when the block is evaluated

**Question**:
Read this resource block:

```hcl
resource "aws_instance" "web" {
  ami           = "ami-0abc123"
  instance_type = "t3.micro"

  lifecycle {
    postcondition {
      condition     = self.public_ip != null
      error_message = "Instance must have a public IP address."
    }
  }
}
```

What does `self` refer to inside the `postcondition`, and when is this block evaluated?

- A) `self` refers to the previous state of the instance; the block runs before any changes are applied
- B) `self` refers to the `aws_instance.web` resource after it has been created or updated; the block runs after the resource change completes
- C) `self` refers to the entire Terraform module containing the resource; it is evaluated during `terraform validate`
- D) `self` is a shorthand for `this` and refers to the variable block declaring the resource type

**Answer**: B

**Explanation**:
In a `postcondition` block, `self` is a special reference to the resource instance that was just created or updated during the current apply operation. It allows the condition to inspect the resulting real-world attribute values — in this case, checking that `public_ip` was actually assigned. The `postcondition` is evaluated after the resource change completes. This is why `self` is only available in `postcondition`, not `precondition` — at precondition time, the resource does not yet have new attribute values to inspect.

---

### Question 3 — `check` Block `error_message` Interpolation

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Reading a `check` block's interpolated `error_message` to identify the exact message displayed when the assertion fails

**Question**:
Read this `check` block:

```hcl
check "endpoint_healthy" {
  data "http" "probe" {
    url = "https://api.example.com/health"
  }

  assert {
    condition     = data.http.probe.status_code == 200
    error_message = "Health check failed: got ${data.http.probe.status_code}, expected 200."
  }
}
```

The health endpoint returns HTTP status `503`. What does Terraform display?

- A) `"Health check failed: got 503, expected 200."` — the interpolation resolves the actual status code into the message
- B) No message — `check` blocks produce only a generic "assertion failed" notice without custom text
- C) `"Health check failed: got 200, expected 200."` — the message always interpolates the expected value
- D) Terraform raises a fatal error instead of a warning because the endpoint is unreachable

**Answer**: A

**Explanation**:
The `error_message` string supports interpolation — `${data.http.probe.status_code}` resolves to the actual value returned at evaluation time. Because the endpoint returns `503`, the interpolated message is `"Health check failed: got 503, expected 200."`. This message is displayed as a **warning** (not an error) since `check` blocks never block apply. The custom `error_message` is always used when an assertion fails — Terraform does not substitute a generic message.

---

### Question 4 — Variable with Two `validation` Blocks

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reading a variable with two separate `validation` blocks to identify which value satisfies both conditions

**Question**:
Read this variable declaration:

```hcl
variable "port" {
  type = number

  validation {
    condition     = var.port >= 1024
    error_message = "Port must be >= 1024 (unprivileged ports only)."
  }

  validation {
    condition     = var.port <= 65535
    error_message = "Port must be <= 65535."
  }
}
```

Which value satisfies **both** validation conditions?

- A) `80` — a well-known HTTP port
- B) `1023` — one below the unprivileged port boundary
- C) `8080` — an unprivileged port within the valid range
- D) `70000` — above the maximum valid port number

**Answer**: C

**Explanation**:
Both validation blocks must pass simultaneously for the variable to be accepted. `80` fails the first condition (`80 >= 1024` is false). `1023` also fails the first condition (`1023 >= 1024` is false — it is one below the threshold). `70000` fails the second condition (`70000 <= 65535` is false). `8080` satisfies both: `8080 >= 1024` is true, and `8080 <= 65535` is true. When a variable has multiple `validation` blocks, all conditions must pass; any single failure displays that block's `error_message` and halts processing.

---

### Question 5 — `precondition` Referencing a Data Source Attribute

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reading a `precondition` that checks a data source attribute to identify when and how it triggers a failure

**Question**:
Read this configuration:

```hcl
data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"]
}

resource "aws_instance" "web" {
  ami           = data.aws_ami.ubuntu.id
  instance_type = "t3.micro"

  lifecycle {
    precondition {
      condition     = data.aws_ami.ubuntu.architecture == "x86_64"
      error_message = "AMI must be x86_64 architecture, found: ${data.aws_ami.ubuntu.architecture}."
    }
  }
}
```

The data source resolves to an AMI with `architecture = "arm64"`. What happens during `terraform apply`?

- A) The instance is created with `arm64` architecture; Terraform logs a warning about the architecture mismatch
- B) The precondition condition evaluates to `false`; apply halts and `aws_instance.web` is NOT created in AWS
- C) Terraform silently skips the architecture check and creates the instance because the type constraint is satisfied
- D) The plan succeeds but apply pauses for interactive confirmation before creating the instance

**Answer**: B

**Explanation**:
`precondition` blocks are evaluated before the resource is changed during apply. The condition `data.aws_ami.ubuntu.architecture == "x86_64"` evaluates to `false` when the AMI returns `"arm64"`. Terraform displays the `error_message` (with the interpolated architecture value) and halts apply immediately — `aws_instance.web` is never sent to the AWS API. No instance is created and no partial state is recorded. This contrasts with `postcondition`, which would only fire after the instance already exists.

---

### Question 6 — `check` Block Without a Scoped Data Source

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reading a `check` block that uses only an `assert` (no scoped `data` source) to identify its effect on apply

**Question**:
Read this `check` block:

```hcl
check "ha_check" {
  assert {
    condition     = length(aws_autoscaling_group.web.availability_zones) >= 2
    error_message = "Auto-scaling group should span at least 2 AZs for high availability."
  }
}
```

After `terraform apply`, the ASG is created spanning only one availability zone, so `length(aws_autoscaling_group.web.availability_zones)` equals `1`. What does Terraform do?

- A) Apply fails with: "Auto-scaling group should span at least 2 AZs for high availability."
- B) Terraform emits a warning with the `error_message` and completes apply successfully — the ASG is created
- C) Terraform destroys the ASG and recreates it across 2 AZs to satisfy the assertion
- D) The `check` block is ignored because it does not contain a scoped `data` source

**Answer**: B

**Explanation**:
`check` blocks are always **non-blocking**. A failing `assert` condition produces a warning message — the apply does not fail, no resources are rolled back, and no resources are modified to satisfy the assertion. The ASG is created with one AZ as declared in the configuration; the `check` block simply flags the HA concern to the operator. A scoped `data` source inside a `check` block is optional — the block is valid and evaluated with only an `assert`.

---

### Question 7 — Sensitivity Propagation Through `locals` to Two Outputs

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reading a configuration with two outputs — one referencing a sensitive local and one not — to identify which requires `sensitive = true`

**Question**:
Read this configuration:

```hcl
variable "password" {
  type      = string
  sensitive = true
}

locals {
  greeting = "Hello, infrastructure!"
  db_url   = "postgresql://admin:${var.password}@db.example.com/prod"
}

output "greeting_output" {
  value = local.greeting
}

output "db_url_output" {
  value = local.db_url
}
```

Which output requires `sensitive = true` to be explicitly added, and why?

- A) Both outputs — all values that flow through `locals` must be marked sensitive in their outputs
- B) Neither output — `sensitive = true` on the `password` variable is sufficient to protect it everywhere
- C) Only `db_url_output` — `local.db_url` interpolates the sensitive variable, making it sensitive; `local.greeting` does not reference any sensitive value
- D) Only `greeting_output` — it is the non-sensitive output and must be explicitly marked to prevent accidental exposure

**Answer**: C

**Explanation**:
Sensitivity propagates in Terraform: any expression that includes a reference to a sensitive value is itself treated as sensitive. `local.db_url` interpolates `var.password`, so the resulting string is sensitive. When `db_url_output` references this sensitive local without `sensitive = true`, Terraform raises a plan error requiring the flag to be added. `local.greeting` does not reference any sensitive value, so `greeting_output` requires no special marking and displays normally. This selective propagation allows non-sensitive values to remain visible while protecting sensitive ones.

---

### Question 8 — Lifecycle Block with Both `precondition` and `postcondition`

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Reading a lifecycle block with both condition types to identify TWO correct statements about their timing

**Question**:
Read this resource block:

```hcl
resource "aws_db_instance" "prod" {
  identifier     = "prod-db"
  engine         = "postgres"
  instance_class = var.db_instance_class

  lifecycle {
    precondition {
      condition     = var.db_instance_class != "db.t3.micro"
      error_message = "Production databases must not use db.t3.micro."
    }

    postcondition {
      condition     = self.endpoint != ""
      error_message = "Database did not receive a valid endpoint."
    }
  }
}
```

Which TWO statements correctly describe when each condition is evaluated? (Select two.)

- A) The `precondition` is evaluated before `aws_db_instance.prod` is created or modified in AWS
- B) The `postcondition` is evaluated at the same time as the `precondition`, before the database is created
- C) If `var.db_instance_class` is `"db.t3.micro"`, the `precondition` fails and the database is never created
- D) `self.endpoint` in the `postcondition` refers to the endpoint from the previous state, not the newly provisioned instance

**Answer**: A, C

**Explanation**:
`precondition` runs before the resource is changed — if `var.db_instance_class` is `"db.t3.micro"`, the condition evaluates to `false` and apply halts before any AWS API call is made (C is correct). `postcondition` runs after the resource change completes (B is incorrect). `self` in a `postcondition` refers to the newly created or updated resource's attributes, not the previous state (D is incorrect). This means if the `precondition` passes but the DB is created without a valid endpoint, the `postcondition` catches it — but only after the DB already exists in AWS.

---

### Question 9 — Sensitive Variable Through `local` to Resource Argument

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reading a sensitive variable referenced in a `locals` interpolation used as a resource argument — identifying what `terraform plan` shows

**Question**:
Read this configuration:

```hcl
variable "api_secret" {
  type      = string
  sensitive = true
}

locals {
  connection_string = "Server=db.example.com;Password=${var.api_secret}"
}

resource "aws_ssm_parameter" "conn" {
  name  = "/app/connection_string"
  type  = "SecureString"
  value = local.connection_string
}
```

What does `terraform plan` display for the `value` argument of `aws_ssm_parameter.conn`?

- A) The full connection string with the actual password substituted in plaintext
- B) `(sensitive value)` — the local interpolates a sensitive variable, so the result is also treated as sensitive
- C) `"Server=db.example.com;Password=(sensitive value)"` — Terraform partially redacts only the sensitive portion
- D) An error — locals cannot interpolate sensitive variables; they must be referenced directly in resource arguments

**Answer**: B

**Explanation**:
Sensitivity propagates through Terraform expressions. When `local.connection_string` interpolates `var.api_secret` (which is sensitive), the resulting string is also sensitive. When this sensitive local is used in a resource argument, `terraform plan` redacts the entire value as `(sensitive value)`. Terraform does not perform partial redaction — the entire expression result is treated as sensitive. The value is still stored in plaintext in `terraform.tfstate`, so an encrypted remote backend is required to protect it at rest.

---

### Question 10 — `validation` with `&&` — Identifying Values That Pass

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Reading a `validation` block with a compound `&&` condition to identify TWO values that satisfy both sub-conditions

**Question**:
Read this variable declaration:

```hcl
variable "cidr_block" {
  type = string

  validation {
    condition     = startswith(var.cidr_block, "10.") && length(split(".", var.cidr_block)) == 4
    error_message = "Must start with 10. and have exactly 4 dot-separated segments."
  }
}
```

Which TWO values satisfy **both** parts of the `&&` condition? (Select two.)

- A) `"10.0.0.0/16"` — starts with `"10."` and `split(".", "10.0.0.0/16")` produces 4 segments
- B) `"192.168.1.0/24"` — standard private CIDR
- C) `"10.0.1.0"` — starts with `"10."` and `split(".", "10.0.1.0")` produces 4 segments
- D) `"10.0.0"` — starts with `"10."` but only 3 dot-separated segments

**Answer**: A, C

**Explanation**:
The condition requires both sub-expressions to be `true`. `"10.0.0.0/16"` starts with `"10."` (✓) and `split(".", "10.0.0.0/16")` returns `["10", "0", "0", "0/16"]` — 4 elements (✓). `"10.0.1.0"` starts with `"10."` (✓) and `split(".", "10.0.1.0")` returns `["10", "0", "1", "0"]` — 4 elements (✓). `"192.168.1.0/24"` fails the first sub-condition (does not start with `"10."`). `"10.0.0"` starts with `"10."` but `split(".", "10.0.0")` returns only 3 elements, failing the length check.

---

### Question 11 — `precondition` with `can(regex())` Pattern

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Reading a `precondition` that uses `can(regex())` to validate a data source attribute format

**Question**:
Read this resource block:

```hcl
data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"]
}

resource "aws_instance" "web" {
  ami           = data.aws_ami.ubuntu.id
  instance_type = "t3.micro"

  lifecycle {
    precondition {
      condition     = can(regex("^ami-[0-9a-f]{8,17}$", data.aws_ami.ubuntu.id))
      error_message = "AMI ID must match the format ami-xxxxxxxx (8-17 hex chars)."
    }
  }
}
```

What does this `precondition` check, and what is the effect of using `can()` around `regex()`?

- A) It validates the `ami` input variable format before planning; `can()` makes the check optional so it never blocks
- B) It checks that the AMI ID returned by the data source matches the expected `ami-xxxxxxxx` format before the instance is created; `can()` catches a regex error (e.g., if the AMI ID is null) and returns `false` instead of propagating the error
- C) It runs after the instance is created and inspects `self.ami`; `can()` is required to access `self` attributes safely
- D) It runs during `terraform validate` and does not require provider credentials; `can()` provides the null-safety needed for offline validation

**Answer**: B

**Explanation**:
This `precondition` runs before `aws_instance.web` is created during apply. It uses `regex()` to confirm the AMI ID matches the expected `ami-xxxxxxxx` format. `can(expr)` wraps the `regex()` call — if `data.aws_ami.ubuntu.id` is `null` or not a string, `regex()` would raise an error; `can()` catches that error and returns `false` instead of propagating it, causing the precondition to fail cleanly with the custom `error_message` rather than a cryptic Terraform runtime error. This pattern is a common idiom for safe format validation in lifecycle conditions.

---

### Question 12 — Three Condition Mechanisms for `var.env = "dev"`

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Reading three parallel snippets — one `validation`, one `precondition`, one `check` — to identify TWO correct statements when the variable value is `"dev"`

**Question**:
Read these three configuration snippets. Assume `var.env = "dev"` is provided.

```hcl
# Snippet 1 — validation block
variable "env" {
  type = string
  validation {
    condition     = contains(["dev", "prod"], var.env)
    error_message = "env must be dev or prod."
  }
}

# Snippet 2 — resource precondition
resource "aws_instance" "web" {
  ami           = "ami-0abc123"
  instance_type = "t3.micro"

  lifecycle {
    precondition {
      condition     = var.env == "prod"
      error_message = "This resource may only be created in the prod environment."
    }
  }
}

# Snippet 3 — check block
check "env_check" {
  assert {
    condition     = var.env != ""
    error_message = "env must not be empty."
  }
}
```

With `var.env = "dev"`, which TWO statements correctly describe Terraform's behaviour? (Select two.)

- A) Snippet 1's `validation` passes — `"dev"` is in `["dev", "prod"]`, so the condition is `true`
- B) Snippet 2's `precondition` passes — `var.env` has a value, and preconditions only fail when the variable is `null`
- C) Snippet 2's `precondition` fails — `"dev" == "prod"` is `false`; apply halts before `aws_instance.web` is created
- D) Snippet 3's `check` block fails — `"dev" != ""` is `false`, so a warning is emitted

**Answer**: A, C

**Explanation**:
Snippet 1: `contains(["dev", "prod"], "dev")` returns `true` — the validation passes and planning proceeds. Snippet 2: `"dev" == "prod"` is `false`, making the `precondition` condition `false`. A false condition causes the precondition to fail; apply is halted before `aws_instance.web` is created. The error is not about `null` — any false condition fails a precondition. Snippet 3: `"dev" != ""` is `true` (dev is not an empty string), so the check assertion passes — no warning is emitted. Option D incorrectly states the check fails; `"dev" != ""` evaluates to `true`.

---
