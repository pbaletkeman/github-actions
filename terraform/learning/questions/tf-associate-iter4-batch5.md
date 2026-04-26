# Terraform Associate (004) — Question Bank Iter 4 Batch 5

**Iteration**: 4
**Iteration Style**: Error identification — spot the mistake in a claim, config, or workflow description
**Batch**: 5
**Objective**: 4b — Variables, Outputs, Types & Functions
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 3 Easy / 8 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — Flawed Claim: `default` Sets the Type

**Difficulty**: Easy
**Answer Type**: one
**Topic**: What `default` does vs what `type` does in a variable block

**Question**:
A colleague makes the following claim:

> "Setting a `default` value on a variable is all you need — it both makes the variable optional and automatically infers its type constraint, so you never need a separate `type` argument."

What is wrong with this claim?

- A) Nothing is wrong — `default` does make the variable optional and Terraform does infer the type from it
- B) The claim is wrong about type inference: Terraform does infer the type from the default, but the variable is still required
- C) The claim is wrong about type constraints: while `default` does make the variable optional, Terraform does **not** enforce a type constraint based on the default — an explicit `type` argument is needed to validate that callers provide the correct type
- D) The claim is wrong because `default` values are ignored if a `type` constraint is also declared

**Answer**: C

**Explanation**:
`default` does make a variable optional — if no value is provided through any input mechanism, the default is used. However, Terraform does not enforce a strict type constraint based solely on the default value. Without an explicit `type` argument, a caller could supply a value of a different type (e.g., pass a number to a variable whose default is a string). The `type` argument is what enforces type validation; `default` only controls whether the variable is required. Both serve distinct purposes and should be declared separately.

---

### Question 2 — Flawed Claim: `sensitive = true` Encrypts State

**Difficulty**: Easy
**Answer Type**: one
**Topic**: What `sensitive = true` on a variable actually does vs does not do

**Question**:
A new engineer reads the documentation and concludes:

> "If I set `sensitive = true` on a variable, its value will be encrypted in `terraform.tfstate`, so I don't need to worry about securing the state file."

Which part of this conclusion is incorrect?

- A) The conclusion is correct — `sensitive = true` causes Terraform to encrypt the value before writing it to state
- B) The conclusion is incorrect — `sensitive = true` hides the value in terminal output but the value is still stored in **plaintext** in the state file; securing the state file is still required
- C) The conclusion is incorrect — `sensitive = true` prevents the value from being stored in state at all
- D) The conclusion is incorrect — `sensitive = true` only applies to output blocks, not variable blocks

**Answer**: B

**Explanation**:
`sensitive = true` on a variable causes Terraform to redact the value from terminal output during `plan` and `apply`, displaying `(sensitive value)` instead. It does **not** encrypt or omit the value from `terraform.tfstate` — the plaintext value is written to state like any other attribute. Anyone with read access to the state file can view sensitive variable values. Protecting sensitive data at rest requires using an encrypted remote backend (such as HCP Terraform or an S3 bucket with server-side encryption).

---

### Question 3 — Flawed Claim: Locals Are Evaluated at Apply Time Only

**Difficulty**: Easy
**Answer Type**: one
**Topic**: When `locals` are evaluated in the Terraform workflow

**Question**:
An engineer explains to a junior colleague:

> "Locals in Terraform are lazy — they're only evaluated during `terraform apply`, not during `terraform plan`. This means a local that references an unknown resource attribute won't cause any issues at plan time."

What is wrong with this explanation?

- A) Nothing — locals are indeed evaluated only at apply time
- B) The explanation is wrong: locals are evaluated during `terraform plan` as well; if a local references a known value it resolves at plan time, and if it references an unknown value the local itself becomes unknown (shown as `(known after apply)`) but no error is raised for that reason alone
- C) The explanation is wrong: locals are evaluated at `terraform init` time, before plan or apply
- D) The explanation is wrong: locals that reference resource attributes cause an immediate error at plan time, regardless of whether the value is known

**Answer**: B

**Explanation**:
Terraform evaluates `locals` during the planning phase, not exclusively at apply time. When a local references a value that is already known (such as a variable default or a static string), it is fully resolved during plan. When a local references an attribute that will only be known after resource creation (such as `aws_instance.web.id`), the local's value is shown as `(known after apply)` in the plan — not deferred silently. This means plan output accurately reflects which values are computed and which are still unknown, which is important for understanding dependency chains.

---

### Question 4 — Flawed Claim: `output sensitive = true` Removes Value from State

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What `sensitive = true` on an output block does vs does not do

**Question**:
A configuration has the following output:

```hcl
output "db_password" {
  value     = aws_db_instance.main.password
  sensitive = true
}
```

A developer claims: "Setting `sensitive = true` on this output removes the password from Terraform state, so the state file is safe to store unencrypted."

What is wrong with this claim?

- A) Nothing — `sensitive = true` on an output block does remove the value from state
- B) The claim is wrong: `sensitive = true` on an output only suppresses the value in terminal display; the underlying resource attribute (`aws_db_instance.main.password`) is still stored in plaintext in state as part of the resource's own attributes
- C) The claim is wrong: `sensitive = true` on an output causes an error during apply because passwords cannot be exposed as outputs
- D) The claim is wrong: `sensitive = true` only affects `terraform output` commands; it has no effect on `terraform apply` output

**Answer**: B

**Explanation**:
`sensitive = true` on an output block suppresses the value from being displayed in `terraform apply` summary and in `terraform output` (all outputs listing). However, it does not remove the value from the state file — the resource attribute `aws_db_instance.main.password` is written to state as part of the resource's tracked attributes, independent of any output declaration. The output block itself also stores the value reference in state. Encrypting the state backend is always required when sensitive data is managed by Terraform.

---

### Question 5 — Flawed Variable Precedence Order

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Correct ordering of variable input precedence (highest to lowest)

**Question**:
A team's internal wiki documents the following variable input precedence order (highest to lowest):

> 1. `terraform.tfvars` (auto-loaded)
> 2. `*.auto.tfvars` files
> 3. `TF_VAR_*` environment variables
> 4. `-var` CLI flag
> 5. `default` in the variable block

Which error does this list contain?

- A) `terraform.tfvars` should be above `*.auto.tfvars`
- B) The `-var` CLI flag and `TF_VAR_*` environment variables are listed in the wrong order — `-var` has the highest precedence and both are above `.tfvars` files; the list also incorrectly places `.tfvars` files above environment variables
- C) `default` in the variable block should be listed first (highest priority)
- D) There are no errors — this is the correct precedence order

**Answer**: B

**Explanation**:
The correct Terraform variable precedence from highest to lowest is: (1) CLI `-var` flag and `TF_VAR_*` environment variables — these are at the **same** highest level and both outrank all file-based sources; (2) `*.auto.tfvars` files; (3) `terraform.tfvars`; (4) `-var-file` flag; (5) `default` in the variable block. The wiki incorrectly ranks `terraform.tfvars` and `*.auto.tfvars` above environment variables and the `-var` flag. In reality, any `TF_VAR_*` environment variable or `-var` flag overrides any `.tfvars` file value.

---

### Question 6 — Flawed Claim: `toset()` Preserves List Order

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What `toset()` does to ordering when converting a list

**Question**:
An engineer writes the following and comments on it:

```hcl
locals {
  regions_list = ["us-east-1", "eu-west-1", "ap-southeast-1"]
  regions_set  = toset(local.regions_list)
}
```

> "I'm using `toset()` here so that `for_each` can iterate over the regions. The set will maintain the same order as my original list — `us-east-1` first, `eu-west-1` second, `ap-southeast-1` third."

What is wrong with this comment?

- A) Nothing — `toset()` preserves insertion order from the source list
- B) The comment is wrong: `toset()` removes duplicates but **does not guarantee ordering** — sets in Terraform are unordered; iteration order over the resulting set is not guaranteed to match the original list order
- C) The comment is wrong: `toset()` converts the list to a map, not a set
- D) The comment is wrong: `toset()` can only be used with `count`, not `for_each`

**Answer**: B

**Explanation**:
Sets in Terraform are **unordered collections** — they do not have a defined element order. `toset()` converts a list to a set, which removes duplicates, but the resulting set does not preserve the original list's order. When `for_each` iterates over a set, the iteration order is not guaranteed to be alphabetical, insertion-order, or any other predictable sequence. If ordering matters, use a list with `count` or rely on stable map keys with `for_each`. The key benefit of `toset()` for `for_each` is stable key-based addressing, not ordering.

---

### Question 7 — Flawed Claim: `for` on a Map Always Produces a List

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What output type a `for` expression produces based on its delimiter

**Question**:
A developer reviewing HCL code states:

> "A `for` expression always produces a list. The outer delimiter is just a style choice — you can use `{...}` or `[...]` interchangeably and always get a list back."

Which of the following best describes the error in this statement?

- A) The statement is correct — `for` expressions always produce lists in Terraform
- B) The statement is wrong: the outer delimiter determines the output type — `[for ...]` (square brackets) produces a **list**, while `{for ... : key => value}` (curly braces with `=>`) produces a **map (object)**; they are not interchangeable
- C) The statement is wrong: `for` expressions always produce maps, not lists
- D) The statement is wrong: `{...}` produces a set, not a map

**Answer**: B

**Explanation**:
The outer delimiter of a `for` expression is not a style choice — it controls the output type. `[for item in collection : expr]` produces a **list**. `{for k, v in collection : key_expr => value_expr}` produces a **map**. These two forms have different syntax requirements (the map form requires the `=>` operator and typically two iteration variables) and produce incompatible types. Using a map output where a list is expected, or vice versa, will cause a type error. The distinction is fundamental to writing correct Terraform expressions.

---

### Question 8 — Flawed Claim: `lookup()` Default Argument Is Optional

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Whether the default argument to `lookup()` is required or optional

**Question**:
An engineer writes:

```hcl
locals {
  port = lookup(var.service_config, "port")
}
```

And explains: "I don't need to pass a default to `lookup()` — the third argument is optional, so if the key is missing, it just returns `null`."

What is wrong with this explanation?

- A) Nothing — the third argument to `lookup()` is optional and returns `null` when omitted
- B) The explanation is wrong: omitting the default from `lookup()` causes Terraform to raise an error if the key is not found; the default argument is required to make the call safe against missing keys
- C) The explanation is wrong: `lookup()` requires exactly four arguments
- D) The explanation is wrong: `lookup()` cannot be used with variables — only with literals

**Answer**: B

**Explanation**:
`lookup(map, key, default)` has a third argument that, while syntactically optional in some Terraform versions, is strongly recommended. When the key does not exist and no default is provided, Terraform raises an error. The safe pattern is always to provide a default value: `lookup(var.service_config, "port", 443)`. Omitting the default and assuming a `null` return is incorrect — Terraform does not silently return `null` for missing keys when `lookup()` is called without a default. Always supply a meaningful fallback to avoid plan-time failures.

---

### Question 9 — Flawed Claim: `merge()` Deep-Merges Nested Maps

**Difficulty**: Medium
**Answer Type**: one
**Topic**: How `merge()` handles nested map keys (shallow vs deep merge)

**Question**:
A developer uses the following to combine configuration maps:

```hcl
locals {
  base = {
    tags = { Environment = "dev", Team = "platform" }
    region = "us-east-1"
  }
  override = {
    tags = { Environment = "production" }
  }
  config = merge(local.base, local.override)
}
```

The developer expects `local.config.tags` to equal `{ Environment = "production", Team = "platform" }` — the `Team` key preserved from `base` and `Environment` overridden. What is wrong with this expectation?

- A) Nothing — `merge()` performs a deep recursive merge, so the `tags` maps are merged together
- B) The expectation is wrong: `merge()` performs a **shallow merge only** — when `override.tags` and `base.tags` share the same top-level key (`"tags"`), the entire `tags` map from `override` replaces the one from `base`; the result is `local.config.tags = { Environment = "production" }` with `Team` lost
- C) The expectation is wrong: `merge()` will raise an error because both maps contain the same top-level key `"tags"`
- D) The expectation is wrong: `merge()` only works on flat maps with string values; nested maps cause a type error

**Answer**: B

**Explanation**:
`merge()` performs a **shallow (top-level) merge only**. When two maps share the same key, the value from the last map wins — the entire value is replaced, not recursively merged. In this example, both `local.base` and `local.override` have a `"tags"` key. The `override` map is the last argument, so its `tags` value (`{ Environment = "production" }`) completely replaces the `base` tags map. The `Team` key is lost because Terraform does not recurse into nested maps to merge them. To achieve deep merging, you would need to explicitly merge the nested maps: `merge(local.base.tags, local.override.tags)`.

---

### Question 10 — Flawed Claim: `nonsensitive()` Permanently Removes Sensitive Marking from State

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What `nonsensitive()` does and does not affect

**Question**:
A developer uses `nonsensitive()` to unwrap a sensitive value before passing it to a resource argument and claims:

> "Once I wrap a value with `nonsensitive()`, Terraform permanently removes the sensitive marking from that value everywhere — including in state and in all downstream references."

What is wrong with this claim?

- A) Nothing — `nonsensitive()` permanently removes the sensitive marking from the value across all contexts
- B) The claim is wrong: `nonsensitive()` only removes the sensitive marking for the **specific expression** where it is used, allowing that value to be displayed in plan output; it does not change how the underlying source (the original sensitive variable or attribute) is marked, and the source value remains sensitive in other contexts
- C) The claim is wrong: `nonsensitive()` is not a valid Terraform function
- D) The claim is wrong: `nonsensitive()` removes the value from state entirely

**Answer**: B

**Explanation**:
`nonsensitive(value)` is a Terraform function that removes the sensitive marking from a value for the purpose of the current expression, allowing it to appear in plan output or be passed to arguments that don't accept sensitive values. It does **not** permanently alter the source variable or attribute's sensitive marking — the original source remains sensitive. Callers of the same sensitive variable elsewhere still see it as sensitive. `nonsensitive()` should be used with care and only when the developer is certain the value is safe to expose; it does not affect state storage.

---

### Question 11 — Flawed Claim: `coalesce()` and `coalescelist()` Are Interchangeable

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Distinguishing `coalesce()` from `coalescelist()` and their input types

**Question**:
An engineer writes the following and explains their reasoning:

```hcl
locals {
  fallback_region = coalesce(var.primary_region, var.secondary_region, "us-east-1")
  fallback_list   = coalesce(var.allowed_ports, [80, 443])
}
```

> "`coalesce()` works for both scalar values and lists — it just returns the first non-null, non-empty value regardless of type."

What is wrong with this code and explanation?

- A) Nothing — `coalesce()` accepts any type including lists
- B) The second usage is wrong: `coalesce()` is designed for **scalar values** (strings, numbers, bools) and returns the first non-null, non-empty string; for returning the first non-empty **list**, `coalescelist()` should be used instead
- C) The first usage is wrong: `coalesce()` only accepts exactly two arguments
- D) Both usages are wrong: `coalesce()` only works with boolean values

**Answer**: B

**Explanation**:
`coalesce(str1, str2, ...)` accepts scalar values and returns the first that is not null and not an empty string. It is designed for string/scalar fallback patterns. `coalescelist(list1, list2, ...)` is the collection counterpart — it accepts lists and returns the first non-empty list. Using `coalesce()` with a list argument (like `[80, 443]`) will cause a type error because `coalesce()` does not accept list inputs. The correct function for the second local is `coalescelist(var.allowed_ports, [80, 443])`.

---

### Question 12 — Flawed Claim: `can()` Returns the Value on Success

**Difficulty**: Hard
**Answer Type**: many
**Topic**: What `can()` returns vs what `try()` returns

**Question**:
A developer writes the following code and makes two claims about it:

```hcl
locals {
  settings = { timeout = 30 }
  a = can(local.settings["timeout"])
  b = try(local.settings["timeout"], 0)
}
```

> **Claim 1**: "`local.a` equals `30` — `can()` evaluates the expression and returns its value when it succeeds."
> **Claim 2**: "`local.b` equals `30` — `try()` evaluates `local.settings["timeout"]` successfully and returns its value."

Which TWO statements correctly identify errors or accuracies in these claims? (Select two.)

- A) Claim 1 is **wrong**: `can(expr)` always returns a **boolean** (`true` if the expression succeeds without error, `false` if it errors) — it never returns the expression's value; `local.a` equals `true`, not `30`
- B) Claim 1 is **correct**: `can()` returns the value of the expression when it succeeds
- C) Claim 2 is **correct**: `try()` evaluates the first expression successfully (`local.settings["timeout"]` = `30`) and returns `30`; the fallback `0` is never used
- D) Claim 2 is **wrong**: `try()` always returns the last fallback argument regardless of whether earlier expressions succeed

**Answer**: A, C

**Explanation**:
`can(expr)` is a predicate function — it always returns a **boolean**. It returns `true` if the expression can be evaluated without error, and `false` if it would error. It never returns the expression's value. Claim 1 incorrectly treats `can()` like `try()`. `try(expr1, fallback)` evaluates each argument in order and returns the **value** of the first one that succeeds without error. Since `local.settings["timeout"]` exists and equals `30`, `try()` returns `30` immediately without needing the fallback. Claim 2 is correct.

---

### Question 13 — Flawed Claim: `length()` on `null` Returns Zero

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What `length()` does when called with a `null` argument

**Question**:
A developer writes:

```hcl
variable "tags" {
  type    = map(string)
  default = null
  nullable = true
}

locals {
  tag_count = length(var.tags)
}
```

The developer claims: "If `var.tags` is `null`, `length()` will safely return `0` — it handles `null` gracefully just like an empty collection."

What is wrong with this claim?

- A) Nothing — `length()` returns `0` for `null` values
- B) The claim is wrong: `length()` does **not** accept `null` — calling `length(null)` raises an error; the developer should guard against null with a conditional expression such as `var.tags != null ? length(var.tags) : 0` or use `try(length(var.tags), 0)`
- C) The claim is wrong: `length()` returns `-1` for `null` values to indicate an undefined collection
- D) The claim is wrong: the `nullable = true` setting prevents `var.tags` from ever being `null`, so the scenario cannot occur

**Answer**: B

**Explanation**:
`length()` requires a non-null string, list, or map argument. Passing `null` to `length()` causes a Terraform error — it does not return `0` or any other default value. When a variable may be `null` (e.g., when `nullable = true` and no default is provided, or when the default is explicitly `null`), callers must guard against null before calling `length()`. Safe patterns include: `var.tags != null ? length(var.tags) : 0` (conditional expression) or `try(length(var.tags), 0)` (using `try()` to catch the error). The `nullable = true` setting explicitly allows `null` values — it does not prevent them.

---
