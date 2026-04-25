# Terraform Associate (004) — Question Bank Iter 2 Batch 5

**Iteration**: 2
**Iteration Style**: Behavioral prediction — what happens when code runs or a scenario occurs
**Batch**: 5
**Objective**: 4b — Variables, Outputs, Types & Functions
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 3 Easy / 8 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — -var Flag Overrides auto.tfvars

**Difficulty**: Easy
**Answer Type**: one
**Topic**: What value Terraform uses when the same variable is set in auto.tfvars and via -var

**Question**:
A variable `instance_type` has `default = "t3.micro"` in its declaration. A file named `prod.auto.tfvars` sets `instance_type = "t3.large"`. The engineer runs:

```bash
terraform apply -var="instance_type=t3.xlarge"
```

What value does Terraform use for `instance_type`?

- A) `"t3.micro"` — the `default` in the variable block is the authoritative source
- B) `"t3.large"` — `.auto.tfvars` files override CLI flags
- C) `"t3.xlarge"` — the `-var` flag has the highest precedence and overrides all other sources
- D) Terraform returns an error because the variable is set in multiple places

**Answer**: C

**Explanation**:
The `-var` CLI flag has the highest precedence in Terraform's variable resolution order, overriding all `.tfvars` files and environment variables. When `-var="instance_type=t3.xlarge"` is passed, it wins over both `prod.auto.tfvars` and the `default`. Terraform does not error on multiple sources — it silently uses the highest-precedence value.

---

### Question 2 — sensitive = true on an Output

**Difficulty**: Easy
**Answer Type**: one
**Topic**: What terraform output displays for a sensitive output

**Question**:
An output is declared as:

```hcl
output "db_password" {
  value     = aws_db_instance.main.password
  sensitive = true
}
```

An engineer runs `terraform output db_password`. What does the terminal display?

- A) The actual database password in plaintext
- B) `(sensitive value)` — the value is redacted in the terminal
- C) An error: sensitive outputs cannot be queried directly
- D) An empty string — sensitive outputs always return `""`

**Answer**: A

**Explanation**:
Running `terraform output <name>` for a specific output by name returns the **raw value in plaintext**, even when `sensitive = true`. The `sensitive` flag suppresses the value in the summary output of `terraform apply` and in `terraform output` (all outputs), but when a single output is queried directly by name, the plaintext value is shown. To retrieve it without display, use `terraform output -raw db_password` in a script. Note: `terraform output -json` also reveals sensitive values.

---

### Question 3 — toset() Removes Duplicates Before for_each

**Difficulty**: Easy
**Answer Type**: one
**Topic**: How many resource instances for_each creates when toset() is applied to a list with duplicates

**Question**:
A resource is declared as:

```hcl
resource "aws_iam_user" "team" {
  for_each = toset(["alice", "bob", "alice", "carol"])
  name     = each.key
}
```

How many IAM user resources does Terraform create?

- A) 4 — Terraform creates one resource per list element, including duplicates
- B) 3 — `toset()` removes the duplicate `"alice"`, leaving `{"alice", "bob", "carol"}`
- C) 1 — `for_each` only creates the first unique value
- D) Terraform returns an error because the input list contains duplicate values

**Answer**: B

**Explanation**:
`toset()` converts a list to a set, and sets cannot contain duplicate values. The duplicate `"alice"` is silently removed, leaving the set `{"alice", "bob", "carol"}`. `for_each` then creates exactly 3 resource instances — one for each unique element. The resulting resource addresses are `aws_iam_user.team["alice"]`, `aws_iam_user.team["bob"]`, and `aws_iam_user.team["carol"]`.

---

### Question 4 — for_each Map Creates Named Addresses

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What resource instance addresses are created in state when for_each uses a map

**Question**:
A resource is declared as:

```hcl
variable "servers" {
  default = {
    web = "t3.micro"
    app = "t3.small"
  }
}

resource "aws_instance" "servers" {
  for_each      = var.servers
  ami           = "ami-0abc123"
  instance_type = each.value
}
```

After `terraform apply`, which resource addresses appear in Terraform state?

- A) `aws_instance.servers[0]` and `aws_instance.servers[1]`
- B) `aws_instance.servers["web"]` and `aws_instance.servers["app"]`
- C) `aws_instance.servers.web` and `aws_instance.servers.app`
- D) `aws_instance.servers` — `for_each` creates a single resource with multiple attributes

**Answer**: B

**Explanation**:
When `for_each` iterates over a map, each resource instance is addressed in state using the map key in square brackets: `resource_type.name["key"]`. The two instances are tracked as `aws_instance.servers["web"]` and `aws_instance.servers["app"]`. This key-based addressing is stable — removing the `"app"` entry only destroys that one instance without affecting `"web"`.

---

### Question 5 — count Renumbers on Middle Removal

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What terraform plan shows when the middle element is removed from a count list

**Question**:
A resource is created with `count = length(var.names)` where `var.names = ["alice", "bob", "carol"]`. The index assignments are: `[0]=alice`, `[1]=bob`, `[2]=carol`. The engineer removes `"bob"` from the middle, making `var.names = ["alice", "carol"]`. What does `terraform plan` propose?

- A) Destroy only `aws_iam_user.users[1]` (bob) — the other two are untouched
- B) Destroy `aws_iam_user.users[1]` (bob) and update `aws_iam_user.users[2]` to use `"carol"` shifted to index 1 — effectively destroying and recreating carol's resource
- C) Destroy all three instances and recreate only two
- D) Terraform returns an error because `count` does not allow removing elements

**Answer**: B

**Explanation**:
With `count`, instances are identified by their numeric index. Removing `"bob"` at index 1 shifts `"carol"` from index 2 to index 1. Terraform sees `users[1]` changing from `"bob"` to `"carol"` (an update or destroy/recreate depending on whether the name attribute is immutable) and `users[2]` (previously `"carol"`) as needing to be destroyed. This is the key drawback of `count` with non-identical instances — removals from the middle cause downstream resources to be recreated. `for_each` avoids this problem entirely.

---

### Question 6 — dynamic Block Generates Blocks per Collection Element

**Difficulty**: Medium
**Answer Type**: one
**Topic**: How many nested blocks a dynamic block generates from a 3-element list

**Question**:
A security group resource uses a `dynamic` block:

```hcl
variable "ports" {
  default = [80, 443, 8080]
}

resource "aws_security_group" "web" {
  name = "web-sg"

  dynamic "ingress" {
    for_each = var.ports
    content {
      from_port   = ingress.value
      to_port     = ingress.value
      protocol    = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
    }
  }
}
```

How many `ingress` blocks does Terraform generate for this security group?

- A) 1 — `dynamic` blocks always produce a single merged block
- B) 2 — `dynamic` only iterates over the first two elements
- C) 3 — one `ingress` block is generated per element in `var.ports`
- D) Terraform returns an error because `dynamic` blocks cannot iterate over a list of numbers

**Answer**: C

**Explanation**:
A `dynamic` block generates one instance of the nested block for each element in the `for_each` collection. With `var.ports = [80, 443, 8080]`, three elements are present, so three separate `ingress` blocks are produced — equivalent to writing three static `ingress` blocks with `from_port`/`to_port` set to 80, 443, and 8080 respectively. `dynamic` blocks work with any collection type including lists, maps, and sets.

---

### Question 7 — merge() Later Key Wins

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What value merge() produces when both maps share the same key

**Question**:
A local is defined as:

```hcl
locals {
  defaults = { environment = "dev", region = "us-east-1", debug = false }
  overrides = { environment = "production", debug = true }
  config = merge(local.defaults, local.overrides)
}
```

What is the value of `local.config`?

- A) `{ environment = "dev", region = "us-east-1", debug = false }` — the first map's values are preserved
- B) `{ environment = "production", region = "us-east-1", debug = true }` — keys from `overrides` win; unique keys from `defaults` are kept
- C) Terraform returns an error because `environment` exists in both maps
- D) `{ environment = "production", region = "us-east-1", debug = false }` — `debug` is not overridden because it is a boolean

**Answer**: B

**Explanation**:
`merge()` combines all provided maps into a single map. When the same key exists in multiple maps, the value from the **last map** that contains that key wins. Here, `local.overrides` is the second argument, so its values for `environment` (`"production"`) and `debug` (`true`) override those from `local.defaults`. The key `region` only exists in `local.defaults`, so it is preserved as-is. The result is `{ environment = "production", region = "us-east-1", debug = true }`.

---

### Question 8 — Conditional Expression Selects Value

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What value a conditional expression returns based on a variable

**Question**:
A resource is declared with:

```hcl
variable "environment" {
  default = "staging"
}

resource "aws_instance" "app" {
  ami           = "ami-0abc123"
  instance_type = var.environment == "production" ? "t3.large" : "t3.micro"
}
```

`terraform apply` is run without any variable overrides. What `instance_type` is used?

- A) `"t3.large"` — because `environment` has a value and the condition is evaluated
- B) `"t3.micro"` — because `var.environment` is `"staging"`, which does not equal `"production"`
- C) Terraform returns an error because conditional expressions are not allowed in resource arguments
- D) `null` — because `var.environment` has not been explicitly set by the user

**Answer**: B

**Explanation**:
The conditional expression evaluates `var.environment == "production"`. Since no override is provided, `var.environment` uses its default value of `"staging"`. `"staging" == "production"` is `false`, so the false branch `"t3.micro"` is returned. The instance is created with `instance_type = "t3.micro"`. Conditional expressions are fully supported in resource arguments and are evaluated during plan.

---

### Question 9 — try() Returns Fallback When Key Missing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What try() returns when the primary expression produces an error

**Question**:
A local is defined as:

```hcl
variable "settings" {
  default = { timeout = 30 }
}

locals {
  retry_count = try(var.settings["retry_count"], 3)
}
```

What is the value of `local.retry_count`?

- A) `null` — `try()` returns null when a key is missing
- B) An error — accessing a missing map key in Terraform always raises a fatal error
- C) `3` — `try()` evaluates `var.settings["retry_count"]`, gets an error because the key doesn't exist, and returns the fallback value `3`
- D) `0` — Terraform defaults numeric values to `0` when a key is not found

**Answer**: C

**Explanation**:
`try(expr1, expr2, ...)` evaluates each expression in order and returns the result of the **first one that does not produce an error**. Accessing a non-existent key in a map raises an error in Terraform. Because `var.settings["retry_count"]` fails (the key is absent), `try()` moves to the next argument, `3`, which evaluates successfully and is returned. This is a clean pattern for optional map keys with defaults.

---

### Question 10 — for Map Comprehension Result

**Difficulty**: Medium
**Answer Type**: many
**Topic**: What a for expression with map output produces

**Question**:
A local is defined as:

```hcl
locals {
  sizes = { web = "t3.micro", app = "t3.small", db = "t3.medium" }
  labels = { for k, v in local.sizes : k => "instance-type: ${v}" }
}
```

Which TWO statements correctly describe `local.labels`? (Select two.)

- A) `local.labels` is a list of strings
- B) `local.labels` is a map where each key matches the original key and each value is a formatted string
- C) `local.labels["web"]` equals `"instance-type: t3.micro"`
- D) `local.labels` contains only one entry because `for` expressions return a single value

**Answer**: B, C

**Explanation**:
The `for` expression uses `{ for k, v in ... : k => expr }` syntax to produce a **map** (not a list). For each entry in `local.sizes`, the output map keeps the original key and transforms the value into a formatted string. The result is `{ web = "instance-type: t3.micro", app = "instance-type: t3.small", db = "instance-type: t3.medium" }`. Accessing `local.labels["web"]` returns `"instance-type: t3.micro"`. A list `for` would use `[ for ... ]` syntax instead.

---

### Question 11 — nullable = false with No Value Provided

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What happens when nullable = false is set and no value is provided for the variable

**Question**:
A variable is declared as:

```hcl
variable "app_name" {
  type     = string
  nullable = false
}
```

No `default` is provided, no `.tfvars` file sets the variable, and no `-var` flag is passed. `terraform plan` is run. What happens?

- A) Terraform uses `""` (empty string) as the value because `nullable = false` implies a non-null default
- B) Terraform prompts the user interactively for a value
- C) Terraform returns an error because the variable has no default and no value was provided, making it required
- D) Terraform automatically sets the value to `null` because no source provides a value

**Answer**: C

**Explanation**:
A variable with no `default` value is **required** — Terraform will error during plan if no value is supplied through any input method. The `nullable = false` setting means the value cannot be `null` once provided, but it does not supply a default. Because the variable has no default and receives no input, Terraform treats it as missing and raises an error asking the user to provide a value. `nullable = false` does not make the variable optional.

---

### Question 12 — element() Wraps Around the List

**Difficulty**: Hard
**Answer Type**: one
**Topic**: What element() returns when the index exceeds the list length

**Question**:
The following expression is evaluated in `terraform console`:

```
element(["a", "b", "c"], 4)
```

What does it return?

- A) An error — the index `4` is out of bounds for a 3-element list
- B) `null` — out-of-bounds indices return null in Terraform
- C) `"a"` — `element()` wraps around using modulo, so index 4 mod 3 = 1... wait, 4 mod 3 = 1 → `"b"`
- D) `"b"` — `element()` wraps around the list using modulo arithmetic: `4 mod 3 = 1`, returning the element at index 1

**Answer**: D

**Explanation**:
`element(list, index)` wraps around the list using modulo arithmetic when the index exceeds the list length. With a 3-element list `["a", "b", "c"]` and index `4`: `4 mod 3 = 1`, so the element at index 1 is returned, which is `"b"`. This wrapping behaviour makes `element()` useful for distributing resources across a fixed set of availability zones or other options. Unlike standard list indexing, `element()` never raises an out-of-bounds error.

---

### Question 13 — Sensitive Variable Value in Plan Output

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Where a sensitive variable's value is and is not visible

**Question**:
A variable and resource are declared as:

```hcl
variable "api_key" {
  type      = string
  sensitive = true
}

resource "aws_ssm_parameter" "key" {
  name  = "/app/api_key"
  type  = "SecureString"
  value = var.api_key
}
```

`terraform apply` is run. Which TWO statements correctly describe where `var.api_key`'s value appears? (Select two.)

- A) The value is redacted as `(sensitive value)` in the `terraform apply` terminal output
- B) The value is encrypted automatically before being written to the state file
- C) The value is stored in plaintext in `terraform.tfstate` under the resource's attributes
- D) The value is permanently deleted after apply and cannot be retrieved from state

**Answer**: A, C

**Explanation**:
`sensitive = true` on a variable causes Terraform to redact it in terminal output — plan and apply both show `(sensitive value)` wherever the variable is referenced. However, the `sensitive` flag provides **no encryption or omission** in the state file. The actual value is written to `terraform.tfstate` in plaintext under the resource's `value` attribute. This is why encrypted remote backends (such as S3 with server-side encryption or HCP Terraform) are essential when handling secrets in Terraform configurations.

---
