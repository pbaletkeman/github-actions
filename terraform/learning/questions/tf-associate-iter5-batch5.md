# Terraform Associate (004) — Question Bank Iter 5 Batch 5

**Iteration**: 5
**Iteration Style**: Comparison and contrast — distinguish between two similar concepts
**Batch**: 5
**Objective**: 4b — Variables, Outputs, Types & Functions
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 3 Easy / 8 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — `var.*` vs `local.*`: External Input vs Internal Computed Value

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contrast between input variables and local values in purpose, scope, and mutability

**Question**:
Compare an input variable (`var.*`) and a local value (`local.*`) in Terraform. What is the fundamental difference between the two?

- A) Input variables and locals are interchangeable — locals are simply a shorthand for variables that have a default value
- B) An input variable (`var.*`) is an externally supplied value — it can be set by a caller via CLI flags, `.tfvars` files, or environment variables, and it forms part of the module's interface; a local value (`local.*`) is an internally computed value — it is calculated from other values within the module, cannot be set by callers, and is not part of the module's external interface
- C) Local values can be set from outside the module via `TF_LOCAL_*` environment variables; input variables cannot be overridden at runtime
- D) Input variables can reference resource attribute values in their `default` argument; locals cannot reference resource attributes

**Answer**: B

**Explanation**:
The core distinction is **who controls the value**. Input variables are the module's public interface — callers (or operators) supply their values through `.tfvars` files, CLI flags, environment variables, or module `var =` arguments. Locals are private — they are computed inside the module and cannot be set from outside. A local can reference variables, resource attributes, data sources, and other locals; an input variable's `default` is restricted to static values (it cannot reference resources or data sources). When designing a module, you expose variables for things callers need to customise and use locals for intermediate values that simplify expressions internally.

---

### Question 2 — Variable with `default` vs Required Variable

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contrast between optional variables (with default) and required variables (no default)

**Question**:
Compare a variable block that includes a `default` argument with one that omits it. What is the operational difference during `terraform apply`?

- A) Variables without a `default` are always invalid — Terraform requires a default on every variable
- B) A variable with a `default` is optional — if no value is provided through any input mechanism, Terraform uses the default and does not prompt the operator; a variable without a `default` is required — Terraform will interactively prompt the operator for a value if none is provided, or fail if running non-interactively without one
- C) Variables with `default` are evaluated at `terraform init`; variables without `default` are evaluated at `terraform apply`
- D) A variable without a `default` causes Terraform to use `null` if no value is supplied — there is no interactive prompt

**Answer**: B

**Explanation**:
The `default` argument determines whether a variable is optional or required. With a `default`, Terraform falls back to that value when no other input mechanism provides one — the operator is never prompted. Without a `default`, Terraform treats the variable as required. In an interactive terminal, Terraform will prompt the operator to type a value. In a non-interactive environment (CI/CD pipeline, automated apply), the absence of a value causes Terraform to fail with an error — it does not use `null` as an implicit default. This makes the `default` argument the mechanism for making variables optional.

---

### Question 3 — `list(string)` vs `set(string)`: Ordering and Uniqueness

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contrast between the `list` and `set` collection types in Terraform

**Question**:
Compare the `list(string)` and `set(string)` types in Terraform. What are the two key differences between them?

- A) `list(string)` is unordered and unique; `set(string)` is ordered and allows duplicates — they are opposites of their intuitive names
- B) A `list(string)` is ordered (elements have a defined position accessible by index) and allows duplicate values; a `set(string)` is unordered (no guaranteed element order) and enforces uniqueness — duplicate values are silently removed when a list is converted to a set
- C) Both types are ordered and unique — the only difference is that `set(string)` can hold mixed types
- D) `list(string)` and `set(string)` are interchangeable — Terraform accepts either type wherever a collection of strings is expected

**Answer**: B

**Explanation**:
`list(string)` preserves insertion order and permits duplicate values — elements are accessible by their zero-based index (e.g., `var.names[0]`). `set(string)` has no defined order and automatically removes duplicates — you cannot access set elements by index. This distinction matters practically: `for_each` does not accept a raw `list` (because lists can have duplicates, which conflict with the requirement for unique instance keys), so `toset(var.regions)` is used to convert a list to a set before passing it to `for_each`. Understanding which type a function or meta-argument expects is important for avoiding type errors.

---

### Question 4 — `terraform.tfvars` vs `*.auto.tfvars`: Auto-Loading Mechanics

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between the two automatically loaded variable file mechanisms and their precedence relationship

**Question**:
Compare `terraform.tfvars` and files matching `*.auto.tfvars` (e.g., `prod.auto.tfvars`). What are the two key differences between them?

- A) `terraform.tfvars` is automatically loaded; `*.auto.tfvars` files must be specified explicitly with `-var-file` — they are not auto-loaded
- B) `terraform.tfvars` is a single fixed filename that Terraform auto-loads; `*.auto.tfvars` is a naming pattern — any file ending in `.auto.tfvars` is auto-loaded automatically; additionally, values in `*.auto.tfvars` files have **higher precedence** than `terraform.tfvars`, so `prod.auto.tfvars` overrides the same variable set in `terraform.tfvars`
- C) Both are identical in mechanics and precedence — the `.auto.` in the filename has no special meaning to Terraform
- D) `*.auto.tfvars` has lower precedence than `terraform.tfvars` because it uses a wildcard pattern rather than a fixed filename

**Answer**: B

**Explanation**:
Both file types are automatically loaded without requiring a `-var-file` flag, but they differ in two ways. First, **naming**: `terraform.tfvars` is a single specific filename that Terraform always looks for; `*.auto.tfvars` is a pattern — any file whose name ends in `.auto.tfvars` (e.g., `network.auto.tfvars`, `prod.auto.tfvars`) is auto-loaded, allowing teams to organise variables across multiple files. Second, **precedence**: `*.auto.tfvars` files have higher precedence than `terraform.tfvars`. If `terraform.tfvars` sets `region = "us-east-1"` and `prod.auto.tfvars` sets `region = "eu-west-1"`, the value from `prod.auto.tfvars` wins. Multiple `.auto.tfvars` files are processed in lexical (alphabetical) order, with later files winning over earlier ones.

---

### Question 5 — `output` Block vs `local` Value: Cross-Module vs Intra-Module

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between the purpose of output values and local values in module composition

**Question**:
Compare an `output` block and a `locals` block value. What is the key difference in where each value is accessible?

- A) Both `output` blocks and `local` values are accessible by the parent module — `locals` are simply the private variant of outputs
- B) A `local` value is scoped to the module in which it is defined and cannot be accessed by any caller — it is an internal computed value for reducing repetition within one module; an `output` value is the module's public interface — it exposes a value to the parent module (via `module.<name>.<output>`) or to the operator after apply (via `terraform output`)
- C) `output` values are only accessible during `terraform plan`; `local` values persist and are accessible during `terraform apply`
- D) Both `output` and `local` values can be accessed cross-module; `output` values are simply re-exported locals with no functional difference

**Answer**: B

**Explanation**:
`locals` are private to the module — they are purely an internal DRY mechanism. No caller can reference `local.common_tags` from outside the module. `output` blocks are the module's export interface — a parent module consuming a child module accesses its outputs via `module.<name>.<output_name>`, and a root module's outputs are displayed after `terraform apply` and queryable with `terraform output`. When a child module computes a subnet ID that a parent module needs to assign to an EC2 instance, the subnet ID must be declared as an `output` in the child. If the subnet ID is only needed inside the child module itself, a `local` is sufficient.

---

### Question 6 — `map(string)` vs `object({...})`: Homogeneous vs Heterogeneous Attributes

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between the flexible but homogeneous `map` type and the structured but heterogeneous `object` type

**Question**:
Compare the `map(string)` type and the `object({ name = string, port = number })` type. What is the key structural difference between them?

- A) `map(string)` requires a fixed set of declared keys; `object({...})` allows any key to be added at runtime
- B) A `map(string)` is homogeneous — all values must be the same type (string in this case), but any key can be present and new keys can be added without changing the type definition; an `object({...})` is heterogeneous — each named attribute has its own individually declared type, the set of allowed attributes is fixed by the declaration, and different attributes can hold different types (e.g., `name` is a string while `port` is a number)
- C) Both types are identical — `map(string)` is simply shorthand for `object({})` with all string values
- D) `object({...})` accepts only string values for all attributes; `map(string)` allows values of any type

**Answer**: B

**Explanation**:
`map(string)` is a **homogeneous** collection — every value must be a string, but the set of keys is open-ended. You can add new keys to a `map(string)` variable without changing its type constraint. `object({name=string, port=number})` is a **heterogeneous** structured type — each declared attribute has its own type constraint, and only the declared attributes are valid (the schema is closed). This means `port` can be a number while `name` is a string — impossible in a `map(string)`. Choose `map(string)` when managing a variable-length collection of uniform values (e.g., resource tags); choose `object({...})` when modeling a structured configuration with named fields of specific types.

---

### Question 7 — `lookup()` vs Direct Map Indexing `map["key"]`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between safe key lookup with a default and direct bracket indexing on error behaviour

**Question**:
Compare `lookup(var.ami_ids, "us-west-2", "ami-default")` with `var.ami_ids["us-west-2"]`. What is the key difference in behaviour when the key `"us-west-2"` does not exist in the map?

- A) Both expressions throw a Terraform error if the key does not exist — `lookup()` provides no additional safety
- B) `lookup(var.ami_ids, "us-west-2", "ami-default")` returns the third argument (`"ami-default"`) when the key is absent — no error is raised; `var.ami_ids["us-west-2"]` causes a Terraform error at plan or apply time if the key does not exist in the map
- C) `var.ami_ids["us-west-2"]` silently returns `null` when the key is absent; `lookup()` returns an error
- D) Both expressions return `null` for missing keys — the difference is only in syntax style

**Answer**: B

**Explanation**:
`lookup(map, key, default)` is a **safe accessor** — its third argument is a fallback value returned when the specified key is not present in the map. This is useful for optional per-region or per-environment configuration where not every key may exist. Direct bracket indexing (`map["key"]`) is an **unsafe accessor** — if the key doesn't exist, Terraform raises a runtime error. Use `lookup()` when absence of a key is a valid and expected situation that should gracefully fall back to a default value; use direct indexing when the key is guaranteed to exist and an error on absence is the correct behaviour.

---

### Question 8 — `concat()` vs `flatten()`: Combining Lists vs Removing Nesting

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between joining multiple flat lists and flattening a single nested list structure

**Question**:
Compare `concat(list_a, list_b)` and `flatten([list_a, [list_b, list_c]])`. What problem does each function solve, and when would you choose one over the other?

- A) Both functions are identical — `flatten` simply uses different syntax to call the same underlying list-joining operation as `concat`
- B) `concat(list_a, list_b)` takes two or more already-flat lists as separate arguments and joins them into a single flat list; `flatten([list_a, [list_b, list_c]])` takes a single argument that is a list potentially containing nested sublists and recursively removes the nesting — use `concat` when joining known flat collections, and `flatten` when the input may contain lists nested within lists (e.g., a `for` expression that produces a list of lists)
- C) `concat` is for joining strings; `flatten` is for joining lists — they operate on different types
- D) `flatten` only removes one level of nesting; `concat` is required for deeply nested structures

**Answer**: B

**Explanation**:
`concat(a, b, c)` is a variadic function — it accepts multiple flat list arguments and joins them in order. It does not handle nesting: `concat(["a"], [["b", "c"]])` would produce `["a", ["b", "c"]]` — the inner list remains nested. `flatten` is designed specifically to **remove nesting** from a list that contains sublists. It recursively flattens all levels, so `flatten([["a"], [["b", "c"]]])` produces `["a", "b", "c"]`. A common real-world use case for `flatten` is in `for` expressions over modules or `for_each` resources that each return a list of objects — the result is a list of lists that needs flattening before being used in a subsequent `for_each`.

---

### Question 9 — `coalesce()` vs `try()`: Two Different Fallback Mechanisms

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between null/empty-string fallback and expression-error fallback

**Question**:
Compare `coalesce(var.region, var.fallback_region, "us-east-1")` and `try(var.config["region"], "us-east-1")`. What kind of fallback does each function provide?

- A) Both functions are interchangeable — they both return the first non-null value from their arguments
- B) `coalesce()` returns the first argument in its list that is neither `null` nor an empty string — it evaluates all arguments and returns the first one with a meaningful value; `try()` evaluates its first expression and returns the fallback only if the expression itself produces a **runtime error** — it does not treat `null` or empty string as a failure condition
- C) `try()` returns the first non-null value; `coalesce()` suppresses errors from invalid expressions
- D) `coalesce()` is for numeric fallbacks only; `try()` is for string fallbacks only

**Answer**: B

**Explanation**:
`coalesce(a, b, c)` is a **null/empty-string filter** — it scans its arguments in order and returns the first one that is not `null` and not an empty string `""`. It is used for optional configuration values where several inputs might be null and you want the first meaningful one. `try(expr, fallback)` is an **error suppressor** — it evaluates its first argument and, if that expression would throw a runtime error (e.g., accessing a map key that doesn't exist, or calling a function with an invalid input), it returns the fallback instead. If the first expression succeeds, `try()` returns its result regardless of whether the value is `null`. Use `coalesce()` for optional values with ordered defaults; use `try()` for defensive access to values that may not exist in a data structure.

---

### Question 10 — `[for ...]` vs `{for ...}`: List vs Map Output from `for` Expressions

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Contrasting TWO key differences between list-producing and map-producing `for` expressions

**Question**:
Compare `[for n in var.names : upper(n)]` and `{for n in var.names : n => upper(n)}`. Which TWO statements correctly describe a difference between these two forms? (Select two.)

- A) The `[for ...]` form with square brackets produces a **list** — elements are ordered and accessible by zero-based index; the `{for ...}` form with curly braces and `=>` produces a **map** — elements are accessible by their declared key
- B) Both forms always produce a list — the curly braces in `{for ...}` are purely cosmetic and have no effect on the output type
- C) The map form `{for ...}` requires that the key expression (left side of `=>`) produce a unique value for each iteration — duplicate keys cause a Terraform error; the list form `[for ...]` has no such uniqueness requirement
- D) The list form `[for ...]` requires specifying both a key and a value separated by `=>`; the map form only specifies a single transform expression

**Answer**: A, C

**Explanation**:
(A) The outer delimiter is what determines the output type — this is one of the most practically important distinctions in Terraform expressions. Square brackets produce a list; curly braces with `=>` produce a map. The downstream consumer of the value must receive the correct type (e.g., a `for_each` that expects a map would error if given a list). (C) The map form has a uniqueness constraint on keys — because a map cannot have duplicate keys, if the key expression produces the same value for two different iterations, Terraform raises an error. The list form has no such restriction — the same transformed value can appear multiple times in the resulting list. This is an important consideration when the input collection may have repeated values.

---

### Question 11 — `sensitive = true` on a Variable vs `sensitive = true` on an Output

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between how sensitive masking behaves differently for variables and outputs

**Question**:
Both input variables and output blocks support `sensitive = true`. What is a key difference in how the masking behaviour works for each?

- A) They are identical in every respect — `sensitive = true` on a variable and on an output both produce exactly the same masking behaviour in every context
- B) `sensitive = true` on an **input variable** hides the value in plan and apply terminal output for any expression that uses the variable; `sensitive = true` on an **output** hides the value in the `terraform apply` summary and in the general `terraform output` listing — however, querying a sensitive output directly by name (`terraform output db_password`) or using `terraform output -json` reveals the plaintext value, making direct name-based queries an intentional escape hatch
- C) `sensitive = true` on an output permanently encrypts the value so it can never be retrieved; sensitive variables are only masked in the terminal but remain retrievable
- D) Sensitive output values are excluded from the state file; sensitive variable values are not

**Answer**: B

**Explanation**:
Both settings suppress values in terminal output during plan and apply, but their behaviour diverges when outputs are accessed directly. A sensitive input variable's value is redacted wherever it appears in plan/apply output — the masking follows the value as it propagates through the configuration. A sensitive output is shown as `(sensitive value)` in the general `terraform output` listing (all outputs) and in the apply summary — this prevents accidental exposure in logs. However, `terraform output db_password` (querying by specific name) and `terraform output -json` both intentionally return the plaintext value, because the operator is explicitly requesting that value and is assumed to have authorised access. Neither setting encrypts the state file — sensitive values are still stored in plaintext in `terraform.tfstate`.

---

### Question 12 — `tuple` vs `list`: Fixed Mixed-Type vs Variable Homogeneous

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Contrasting TWO structural differences between the `tuple` type and the `list` type

**Question**:
Compare the `tuple([string, number, bool])` type and the `list(string)` type. Which TWO statements correctly describe a structural difference between them? (Select two.)

- A) A `tuple` has a **fixed length** — the number of elements is defined in the type declaration and cannot change; a `list` has a **variable length** — elements can be added or removed without changing the type definition
- B) A `tuple` and a `list` both enforce that all elements are the same type — the only difference is that `tuple` also enforces element count
- C) A `tuple` allows **different types at different positions** — e.g., `tuple([string, number, bool])` holds a string at position 0, a number at position 1, and a bool at position 2; a `list` requires **all elements to be the same type** — a `list(string)` can only hold strings at every position
- D) A `list` type allows mixed element types by default; a `tuple` restricts all elements to the same type declared in its definition

**Answer**: A, C

**Explanation**:
(A) Tuples are fixed-length — the type declaration specifies exactly how many elements exist and in what order. A `tuple([string, number, bool])` always has exactly three elements. Lists are variable-length — you can have zero, one, or many elements and the list type does not encode a count. (C) Tuples are per-position typed — element 0 is a string, element 1 is a number, element 2 is a bool; each position can hold a different type. Lists are uniformly typed — every element of a `list(string)` must be a string; you cannot mix strings and numbers. Together, tuples combine the per-position typing of objects with the ordered numeric indexing of lists, making them useful for representing structured records accessed by index rather than by name.

---

### Question 13 — Variable Precedence: Specific Ordering Contrasts

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Contrasting TWO specific precedence relationships in Terraform's variable input chain

**Question**:
Terraform resolves variable values from multiple sources using a defined precedence order. Which TWO statements correctly describe specific precedence relationships in that chain? (Select two.)

- A) `TF_VAR_*` environment variables have **higher** precedence than `*.auto.tfvars` files — if both set the same variable, the environment variable value is used
- B) `*.auto.tfvars` files have **higher** precedence than `terraform.tfvars` — if both set the same variable, the value from the `.auto.tfvars` file wins
- C) `terraform.tfvars` has **higher** precedence than `TF_VAR_*` environment variables — if both set the same variable, the `terraform.tfvars` value is used
- D) The `default` value in a variable block has **higher** precedence than `terraform.tfvars` — if both are present, the default is used

**Answer**: A, B

**Explanation**:
(A) The full precedence order from highest to lowest is: CLI `-var` flag → `TF_VAR_*` environment variables → `*.auto.tfvars` files → `terraform.tfvars` → `-var-file` flag → `default`. `TF_VAR_*` sits above `*.auto.tfvars`, so an environment variable overrides any auto-loaded file. (B) `*.auto.tfvars` files sit above `terraform.tfvars` in the precedence chain, so `prod.auto.tfvars` overrides a value set in `terraform.tfvars`. (C) is incorrect — `TF_VAR_*` is higher than `terraform.tfvars`, not the other way around. (D) is incorrect — the `default` is the **lowest** precedence fallback; any other input source overrides it.

---
