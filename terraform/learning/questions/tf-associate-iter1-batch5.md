# Terraform Associate (004) — Question Bank Iter 1 Batch 5

**Iteration**: 1
**Iteration Style**: Foundational recall — define terms, name commands, identify blocks
**Batch**: 5
**Objective**: 4b — Variables, Outputs, Types & Functions
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 2 Easy / 9 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — Variable Input Precedence

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Order of variable value precedence (highest wins)

**Question**:
A Terraform variable named `region` has a `default` of `"us-east-1"` in its declaration, a value of `"eu-west-1"` in `terraform.tfvars`, and is also set via `TF_VAR_region=ap-southeast-1` in the shell environment. Which value does Terraform use?

- A) `"us-east-1"` — the `default` in the variable block always wins
- B) `"eu-west-1"` — `terraform.tfvars` overrides environment variables
- C) `"ap-southeast-1"` — `TF_VAR_*` environment variables have higher precedence than `.tfvars` files
- D) Terraform raises an error because the variable is set in multiple places

**Answer**: C

**Explanation**:
Terraform's variable precedence order from highest to lowest is: CLI `-var` flag and `TF_VAR_*` environment variables (tied at top) → `*.auto.tfvars` files → `terraform.tfvars` → `-var-file` flag → `default` in the variable block. Because `TF_VAR_region` and CLI `-var` share the highest level, `TF_VAR_region=ap-southeast-1` overrides the value in `terraform.tfvars`. Terraform does not error when a variable is set from multiple sources.

---

### Question 2 — Variable Block Argument: `sensitive`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Effect of `sensitive = true` on variables vs state

**Question**:
What is the effect of declaring `sensitive = true` on an input variable?

- A) The variable's value is encrypted before being stored in `terraform.tfstate`
- B) The variable's value is hidden in terminal output during `plan` and `apply` but is still stored in plaintext in the state file
- C) The variable's value is excluded from the state file entirely
- D) The variable cannot be passed via environment variables — only via `-var` flag

**Answer**: B

**Explanation**:
`sensitive = true` causes Terraform to redact the variable's value from terminal output (showing `(sensitive value)` in plans and applies). It does **not** encrypt or omit the value from `terraform.tfstate` — the value is still stored in plaintext. Protecting sensitive values at rest requires an encrypted remote backend.

---

### Question 3 — Validation Block Scope

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What a variable `validation` block can reference

**Question**:
Which of the following expressions is valid inside a variable's `validation` block `condition` argument?

- A) `length(aws_instance.web.id) > 0`
- B) `local.environment != ""`
- C) `contains(["dev", "staging", "prod"], var.environment)`
- D) `data.aws_ami.ubuntu.id != ""`

**Answer**: C

**Explanation**:
A `validation` block's `condition` argument can **only** reference `var.<name>` — the variable being validated. It cannot reference resources, data sources, locals, or other variables. This is because validation runs before planning, at a point when no infrastructure state or computed values are available. Option C is valid because it only uses `var.environment`.

---

### Question 4 — Locals vs Input Variables

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Difference between `locals` and `variable` blocks

**Question**:
What is the key difference between a `locals` block and a `variable` block in Terraform?

- A) Locals can only hold string values; variables can hold any type
- B) Variables are external inputs that can be set by the caller; locals are internal computed values that cannot be set from outside the module
- C) Locals are shared across all modules in a configuration; variables are scoped to a single module
- D) Variables must always have a `default` value; locals do not require a value expression

**Answer**: B

**Explanation**:
`variable` blocks define the external interface of a module — their values can be set by the user via CLI, `.tfvars` files, or environment variables. `locals` are internal to the module; they compute intermediate values from other expressions and cannot be overridden by the module's callers. Locals are referenced as `local.<name>` and appear in neither the module's input nor output interface.

---

### Question 5 — Module Output Reference Syntax

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Referencing a child module's output value

**Question**:
A root configuration uses a child module declared as `module "network" { ... }`. The child module has an output named `vpc_id`. What is the correct way to reference this output in the root module?

- A) `network.vpc_id`
- B) `output.network.vpc_id`
- C) `module.network.vpc_id`
- D) `var.network.vpc_id`

**Answer**: C

**Explanation**:
Child module output values are referenced from the parent using `module.<module_label>.<output_name>`. For a module declared as `module "network"`, the `vpc_id` output is accessed as `module.network.vpc_id`. This creates an implicit dependency: any resource using this reference will not be created until the `network` module has applied successfully.

---

### Question 6 — `count` vs `for_each` — Preferred Usage

**Difficulty**: Easy
**Answer Type**: one
**Topic**: When to prefer `for_each` over `count`

**Question**:
Why is `for_each` generally preferred over `count` when creating multiple resource instances in Terraform?

- A) `for_each` supports more resource types than `count`
- B) `for_each` uses named keys to identify instances, so removing one item does not force recreation of all subsequent instances
- C) `for_each` is faster to apply because it uses parallel execution; `count` is sequential
- D) `for_each` allows resources to span multiple providers; `count` is limited to a single provider

**Answer**: B

**Explanation**:
With `count`, resource instances are addressed by numeric index (e.g., `resource[0]`, `resource[1]`). If an element is removed from the middle of the list, all instances after it are renumbered and recreated. `for_each` addresses instances by stable string keys (e.g., `resource["web"]`), so removing one key only affects that single instance. This makes `for_each` the recommended choice whenever instances have a meaningful identity.

---

### Question 7 — `count.index` and Splat Expressions

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Accessing `count`-based resource instances

**Question**:
A resource is declared with `count = 3`. What does the splat expression `aws_instance.web[*].id` return?

- A) The ID of the first instance only (`aws_instance.web[0].id`)
- B) A map of index-to-ID pairs: `{ 0 = "i-aaa", 1 = "i-bbb", 2 = "i-ccc" }`
- C) A list containing the `id` attribute of all three instances
- D) An error — splat expressions only work with `for_each`, not `count`

**Answer**: C

**Explanation**:
The splat expression `[*]` extracts a named attribute from every element in a list-like collection and returns a list. For a resource with `count = 3`, `aws_instance.web[*].id` returns a list of all three IDs: `["i-aaa", "i-bbb", "i-ccc"]`. Splat works with `count`-based resources; `for_each`-based resources use `values(resource_type.name)[*].attribute` or a `for` expression instead.

---

### Question 8 — `for_each` with a Set — `each.key` vs `each.value`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Behaviour of `each.key` and `each.value` when `for_each` uses a set

**Question**:
When `for_each` is assigned a **set** of strings (e.g., `toset(["alice", "bob"])`), what is the relationship between `each.key` and `each.value` inside the resource block?

- A) `each.key` is the zero-based index; `each.value` is the string element
- B) `each.key` and `each.value` are both equal to the set element string
- C) `each.key` is the set element string; `each.value` is always `null` for sets
- D) Sets do not support `each.key` — only `each.value` is available

**Answer**: B

**Explanation**:
For a map, `each.key` is the map key and `each.value` is the corresponding value. For a **set**, because set elements have no separate key and value, both `each.key` and `each.value` are set to the element itself. This means you can use either `each.key` or `each.value` interchangeably when iterating over a set with `for_each`.

---

### Question 9 — `for` Expression with Filter

**Difficulty**: Medium
**Answer Type**: one
**Topic**: `for` expression syntax with an `if` filter clause

**Question**:
Which `for` expression correctly produces a list of names from `var.names` that are longer than 4 characters?

- A) `[for name in var.names : name where length(name) > 4]`
- B) `[for name in var.names : name if length(name) > 4]`
- C) `[for name in var.names | length(name) > 4 : name]`
- D) `filter(var.names, n => length(n) > 4)`

**Answer**: B

**Explanation**:
Terraform's `for` expression supports an optional `if` clause to filter elements: `[for <item> in <collection> : <value> if <condition>]`. The `if` keyword appears after the value expression and filters out elements where the condition is false. Options A, C, and D use invalid syntax — Terraform's `for` expressions do not use `where`, pipe filtering, or a `filter()` function.

---

### Question 10 — `lookup` Function

**Difficulty**: Medium
**Answer Type**: one
**Topic**: `lookup()` function signature and default value

**Question**:
What does `lookup({a = 1, b = 2}, "c", 99)` return?

- A) `null` — the key `"c"` does not exist in the map
- B) An error — `lookup` raises an exception when the key is not found
- C) `99` — the third argument is returned as the default when the key is not found
- D) `0` — `lookup` always returns 0 for missing numeric map entries

**Answer**: C

**Explanation**:
`lookup(map, key, default)` retrieves the value for `key` from `map`, or returns `default` if the key does not exist. In this case, the key `"c"` is absent from `{a = 1, b = 2}`, so the function returns the default value `99`. Always providing a default to `lookup` is recommended to prevent errors from missing keys.

---

### Question 11 — `cidrsubnet` Function

**Difficulty**: Hard
**Answer Type**: one
**Topic**: `cidrsubnet()` function parameters and result

**Question**:
What does `cidrsubnet("10.0.0.0/16", 8, 2)` return?

- A) `"10.0.0.0/24"`
- B) `"10.0.1.0/24"`
- C) `"10.0.2.0/24"`
- D) `"10.2.0.0/24"`

**Answer**: C

**Explanation**:
`cidrsubnet(prefix, newbits, netnum)` divides a CIDR block into subnets. It borrows `newbits` (8) additional bits from the host portion, making the new prefix length 16 + 8 = /24. Then it selects the `netnum`-th (2nd) subnet in that space. The subnets in order are: `10.0.0.0/24` (netnum 0), `10.0.1.0/24` (netnum 1), `10.0.2.0/24` (netnum 2). So the result is `"10.0.2.0/24"`.

---

### Question 12 — `flatten` vs `compact` Functions

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Distinguishing `flatten()` and `compact()` use cases

**Question**:
Which TWO statements correctly describe the difference between `flatten()` and `compact()`? (Select two.)

- A) `flatten(["a", ["b", "c"]])` returns `["a", "b", "c"]` by removing nested list structure
- B) `compact(["a", "", null, "b"])` returns `["a", "b"]` by removing empty strings and null values
- C) `flatten()` removes duplicate values from a flat list
- D) `compact()` recursively unwraps nested lists into a single flat list

**Answer**: A, B

**Explanation**:
`flatten()` takes a list that may contain nested lists and returns a single flat list — it collapses nesting, not duplicates. `compact()` takes a flat list of strings and removes any empty string (`""`) or `null` elements, returning only non-empty values. `flatten` does not deduplicate (use `distinct()` for that), and `compact` does not unwrap nesting.

---

### Question 13 — `templatefile` vs `file` Functions

**Difficulty**: Medium
**Answer Type**: many
**Topic**: When to use `templatefile()` instead of `file()`

**Question**:
Which TWO statements correctly describe the difference between `file()` and `templatefile()`? (Select two.)

- A) `file(path)` reads the file content as a raw string with no variable substitution
- B) `templatefile(path, vars)` renders the file as a template, substituting `${var_name}` placeholders with values from the `vars` map
- C) `file()` supports `.tpl` template files; `templatefile()` only supports `.txt` files
- D) `templatefile()` can only reference Terraform input variables; it cannot reference locals or computed values

**Answer**: A, B

**Explanation**:
`file(path)` reads and returns the raw content of a file exactly as-is, with no template processing. `templatefile(path, vars)` reads the file and renders it as a template, replacing `${var_name}` placeholders with the values provided in the `vars` map — useful for generating EC2 user-data scripts or configuration files. Both functions can work with any file extension; the `.tpl` convention is a naming practice, not a requirement.

---
