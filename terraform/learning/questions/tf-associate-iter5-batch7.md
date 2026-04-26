# Terraform Associate (004) — Question Bank Iter 5 Batch 7

**Iteration**: 5
**Iteration Style**: Comparison and contrast — distinguish between two similar concepts
**Batch**: 7
**Objective**: 5 — Terraform Modules
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 3 Easy / 8 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — Root Module vs Child Module: Role and Location

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contrast between what the root module and a child module are in a Terraform configuration

**Question**:
Compare the **root module** with a **child module** in Terraform. What is the key difference between the two?

- A) The root module is always stored on the Terraform Registry; a child module is always a local directory
- B) The root module is the working directory from which you run Terraform commands — it contains the top-level `.tf` files you execute; a child module is any module called via a `module` block from the root or from another module — it may be a local subdirectory, a registry module, or a Git source
- C) The root module can only call one child module at a time; child modules can call unlimited other child modules
- D) A child module is the first module Terraform processes; the root module is processed last after all child modules complete

**Answer**: B

**Explanation**:
Every Terraform configuration directory is a module. The **root module** is specifically the working directory from which you run `terraform init`, `terraform plan`, and `terraform apply`. It is the entry point of execution. A **child module** is any module instantiated by a `module` block — whether sourced from a local relative path (`./modules/vpc`), the Terraform Registry (`hashicorp/consul/aws`), or a Git URL. A single root module can call many child modules, and child modules can in turn call further child modules. The distinction is structural: root is the top of the call hierarchy; child is anything below it.

---

### Question 2 — Local Path Module Source vs Terraform Registry Module Source

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contrast between how local path and Terraform Registry module sources are specified and what constraints each supports

**Question**:
Compare a local path module source with a Terraform Registry module source. What is the key structural and capability difference?

- A) A local path source uses `http://` as the prefix; a registry source uses `registry://` as the prefix
- B) A local path source begins with `./` or `../` and references a directory on the local filesystem — the `version` argument is **not supported** for local paths; a Terraform Registry source uses the three-part `<NAMESPACE>/<MODULE>/<PROVIDER>` format and **does support** the `version` argument for version constraints
- C) Both local path and registry sources support the `version` argument — the difference is that local paths require a full absolute path while registry sources use a relative namespace
- D) A registry source begins with `./registry/` to distinguish it from a local path; a local path omits the prefix entirely

**Answer**: B

**Explanation**:
Local path module sources point to a directory on the local filesystem. They require a relative path starting with `./` or `../` and do not support the `version` argument — there is no versioning concept for a local directory. Terraform Registry sources use the standardised `<NAMESPACE>/<MODULE>/<PROVIDER>` format (e.g., `terraform-aws-modules/vpc/aws`) and support `version` with constraint syntax (`~> 5.0`, `>= 4.0, < 6.0`). Registry modules are downloaded by `terraform init` and cached in `.terraform/modules/`. Local path modules are read directly from the filesystem; no downloading occurs, but `terraform init` must still be run to register them.

---

### Question 3 — `version` Argument vs `?ref=` Query Parameter: Registry Pin vs Git Pin

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contrast between how registry modules and Git-sourced modules are pinned to a specific version

**Question**:
Compare pinning a Terraform Registry module to a specific version versus pinning a Git-sourced module to a specific version. What is the correct mechanism for each?

- A) Both registry modules and Git-sourced modules use the `version` argument in the `module` block — the syntax is identical for both source types
- B) Terraform Registry modules are pinned using the `version` argument in the `module` block (e.g., `version = "~> 5.0"`); Git-sourced modules are pinned using the `?ref=` query parameter in the `source` URL (e.g., `?ref=v2.1.0`) — the `version` argument is **not valid** for Git sources and causes a `terraform init` error
- C) Git-sourced modules use the `version` argument; registry modules use the `?ref=` query parameter in their registry URL
- D) Both source types accept the `version` argument, but for Git sources it must contain a full commit SHA rather than a semantic version string

**Answer**: B

**Explanation**:
The versioning mechanism differs depending on where the module is sourced. For **Terraform Registry** and **private registry** sources, the `version` argument in the `module` block accepts constraint expressions (e.g., `"~> 5.0"`) and `terraform init` resolves the constraint against the registry's published versions. For **Git-based sources** (GitHub URLs, `git::https://`, `git::ssh://`), the `version` argument is invalid — using it causes a `terraform init` error. Instead, the `?ref=` query parameter is appended to the source URL to pin the checkout to a specific git tag, branch, or commit SHA (e.g., `"github.com/org/repo//modules/vpc?ref=v2.1.0"`).

---

### Question 4 — Child Module Variable Inheritance vs Explicit Input Passing

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between the assumption that child modules inherit parent variables and the actual explicit-passing requirement

**Question**:
A root module declares `variable "environment" {}` and its value is `"production"`. A child module also needs the environment name. Compare these two approaches:

- **Approach A**: Assume the child module automatically inherits `var.environment` from the root module — no input assignment in the `module` block
- **Approach B**: Explicitly pass the value in the `module` block: `environment = var.environment`

Which approach is correct, and why?

- A) Approach A is correct — child modules automatically inherit all variables declared in the calling module
- B) Approach B is correct — child modules do **not** inherit variables from their caller; every input a child module needs must be explicitly passed as an argument in the `module` block; if a variable is not passed, the child module uses its `default` value or fails if the variable has no default
- C) Both approaches work — Approach A uses implicit inheritance while Approach B uses explicit passing; the result is the same
- D) Approach A is correct for `string` variables; Approach B is only required for `list` and `map` types

**Answer**: B

**Explanation**:
Child modules have **complete scope isolation** from their callers. A variable defined in the root module is not visible inside a child module unless it is explicitly passed as an argument in the `module` block. This is by design — it enforces explicit contracts between modules and makes data flow clear and auditable. If the child module declares `variable "environment" {}` with no default, and the caller does not pass `environment = var.environment`, Terraform will raise an error: "No value for required variable." If the child has a `default`, that default is used. The explicit-passing requirement is fundamental to Terraform module design.

---

### Question 5 — Referencing a Child Module Output vs Referencing a Resource Attribute

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between the syntax for accessing a child module's output and accessing a managed resource's attribute directly

**Question**:
Compare the syntax for referencing a value from a child module versus referencing an attribute from a managed resource directly. Given a module named `networking` with an output `vpc_id`, and a resource `aws_vpc` named `main` with attribute `id`, what is the correct syntax for each?

- A) Both use the same `resource.<name>.<attribute>` pattern — there is no distinction between module outputs and resource attributes at the reference level
- B) A child module output is referenced as `module.<module_name>.<output_name>` (e.g., `module.networking.vpc_id`); a managed resource attribute is referenced as `<resource_type>.<resource_name>.<attribute>` (e.g., `aws_vpc.main.id`) — the `module.` prefix clearly identifies a module output while the resource type prefix identifies a direct resource reference
- C) A child module output is referenced as `output.<module_name>.<output_name>`; a resource attribute uses `resource.<type>.<name>.<attr>`
- D) Both use `var.<name>` — module outputs and resource attributes are both treated as variable references in HCL

**Answer**: B

**Explanation**:
Terraform uses distinct namespaces for different reference types. **Module outputs** are accessed via `module.<module_name>.<output_name>` — the `module.` prefix identifies that the value comes from a child module's declared `output` block. **Resource attributes** are accessed via `<resource_type>.<resource_name>.<attribute>` — no `resource.` prefix is needed; the resource type itself serves as the namespace. For example, `module.networking.vpc_id` retrieves the `vpc_id` output from the `networking` module, while `aws_vpc.main.id` retrieves the `id` attribute directly from the `aws_vpc.main` resource. Using the wrong syntax (e.g., `output.networking.vpc_id`) is invalid and causes a plan-time error.

---

### Question 6 — `.terraform/modules/` Cache vs `.terraform/providers/` Cache

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between the purpose of the modules cache directory and the providers cache directory inside .terraform/

**Question**:
After running `terraform init`, two subdirectories are created inside `.terraform/`: `modules/` and `providers/`. Compare what each directory contains and why each needs to be refreshed.

- A) Both directories cache the same content — `modules/` stores a backup copy of the provider binaries and `providers/` stores a backup of the module source code
- B) `.terraform/modules/` contains the downloaded or locally-registered **module source code** for all `module` blocks in the configuration — it must be refreshed (by re-running `terraform init`) when a module `source` or `version` changes; `.terraform/providers/` contains the **provider plugin binaries** — it must be refreshed when the `required_providers` configuration changes or a new provider version is needed
- C) `.terraform/modules/` caches provider binaries; `.terraform/providers/` caches module source code — the names are inverted from what most developers expect
- D) Both directories are updated automatically on every `terraform plan` — no manual `terraform init` re-run is required after source changes

**Answer**: B

**Explanation**:
`terraform init` populates both directories with distinct content. `.terraform/modules/` contains the source code for every module referenced by a `module` block — for local path modules this is a reference; for registry and Git sources this is downloaded content. The file `.terraform/modules/modules.json` tracks the mapping. `.terraform/providers/` contains the binary executables for every provider declared in `required_providers`. If you add a new module `source` or change a `version` constraint, you must re-run `terraform init` to update `.terraform/modules/`. Similarly, adding or changing a provider requires re-running `terraform init` to download the new binary into `.terraform/providers/`. Neither directory is updated automatically during `plan` or `apply`.

---

### Question 7 — Registry Module Source Format vs GitHub URL Format

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between the structural format of a Terraform Registry module source and a GitHub URL module source

**Question**:
Compare the format of a Terraform Registry module source and a GitHub module source. What are the key structural differences, and what capability does only one of them support?

- A) A Terraform Registry source uses `registry://` as a URL scheme; a GitHub source uses `github://` — they are otherwise structurally identical
- B) A Terraform Registry source uses the three-part `<NAMESPACE>/<MODULE>/<PROVIDER>` format (e.g., `"terraform-aws-modules/vpc/aws"`) and supports the `version` argument; a GitHub source uses a URL beginning with `github.com/` or `git::https://github.com/` and does **not** support the `version` argument — version pinning is done with `?ref=` in the URL
- C) A GitHub source uses the three-part namespace format; a Terraform Registry source uses a full HTTPS URL
- D) Both formats are valid for the `version` argument — the only difference is the URL protocol used

**Answer**: B

**Explanation**:
These two source types look different and behave differently. A **Terraform Registry** source is a compact, slash-delimited identifier: `<NAMESPACE>/<MODULE>/<PROVIDER>`. It integrates with the registry API, supports semantic `version` constraints, and is the format for publicly published modules on `registry.terraform.io`. A **GitHub** source is a URL — `github.com/org/repo` or `git::https://github.com/org/repo.git` — and it interacts with the Git protocol. It does not support the `version` argument; pinning is done via `?ref=v2.0.0` in the URL. The `//` double-slash separator is used with GitHub sources to specify a subdirectory: `github.com/org/repo//modules/vpc?ref=v1.0.0`.

---

### Question 8 — `//` Double-Slash Subdirectory Separator vs `?ref=` Query Parameter in Git URLs

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between the two URL annotation mechanisms used in Git-based module sources

**Question**:
A module source reads:
```hcl
source = "github.com/acme/terraform-modules//modules/vpc?ref=v3.1.0"
```

Compare what `//modules/vpc` and `?ref=v3.1.0` each control. What is the difference?

- A) `//modules/vpc` pins the git checkout to a specific ref; `?ref=v3.1.0` selects a subdirectory
- B) `//modules/vpc` is the **subdirectory separator** — it tells Terraform to use the `modules/vpc` folder within the repository as the module root rather than the repository root; `?ref=v3.1.0` is the **git ref pin** — it specifies which tag, branch, or commit to check out; they are two independent mechanisms that can be combined in the same URL
- C) Both `//modules/vpc` and `?ref=v3.1.0` are version constraints — `//` specifies the major version and `?ref=` specifies the patch version
- D) `?ref=v3.1.0` is required when using `//` — without `?ref=`, the `//` separator is ignored by Terraform

**Answer**: B

**Explanation**:
These are two independent URL annotation mechanisms for Git-based module sources. The **double-slash `//`** is a path separator that tells Terraform: "the module root is inside a subdirectory of this repository." Everything after `//` (up to `?`) is the path to the module's root within the checked-out repository. Without `//`, Terraform uses the repository root as the module root. The **`?ref=` query parameter** is a git checkout instruction: it tells `terraform init` which git ref (tag, branch, or SHA) to check out. Without `?ref=`, `terraform init` checks out the default branch. Both mechanisms can be combined in a single source URL and serve entirely different purposes — one selects the directory, the other selects the commit.

---

### Question 9 — Child Module `output` Block vs Root Module `output` Block: Declaration and Access

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between how child module outputs and root module outputs are declared and how they are accessed differently

**Question**:
Compare an `output` block declared in a **child module** with an `output` block declared in the **root module**. How does the declaration differ, and how is each accessed?

- A) Child module `output` blocks use a different syntax than root module `output` blocks — child modules must use `export` instead of `output`
- B) The `output` block syntax is **identical** in both child and root modules — both use `output "<name>" { value = ... }`; however, their access pattern differs: a child module output is accessed from the caller as `module.<module_name>.<output_name>`, while a root module output is displayed in the terminal after `terraform apply` and is accessible via `terraform output <name>` — it is not referenced from a parent (the root has no caller)
- C) Child module `output` blocks must include `export = true` to be visible from the calling module; root module outputs are always visible without any extra argument
- D) Root module outputs are declared in a separate `outputs.tf` file; child module outputs must be declared in `main.tf`

**Answer**: B

**Explanation**:
The `output` block uses the same HCL syntax regardless of whether it is in a child or root module. The difference is in how the value flows. A **child module output** exposes a value from inside the module to its caller — the caller accesses it as `module.<module_name>.<output_name>`. If the child's output is not declared, the value is private to that module. A **root module output** exposes a value to the operator — it is printed to the terminal after `terraform apply` and can be queried with `terraform output`. Root module outputs cannot be referenced as `module.root.*` because the root module has no caller. Both use exactly the same declaration syntax: `output "name" { value = ... }`.

---

### Question 10 — Two Differences Between Registry Module and Local Module

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Contrasting TWO key differences between a published Terraform Registry module and a local path module

**Question**:
Compare a module sourced from the Terraform Registry with a module sourced from a local path. Which TWO statements correctly describe a difference? (Select two.)

- A) A Terraform Registry module supports the `version` argument and is downloaded by `terraform init` into `.terraform/modules/`; a local path module does **not** support `version` and is read directly from the filesystem — no downloading occurs during `terraform init`
- B) A Terraform Registry module must follow the naming convention `terraform-<PROVIDER>-<NAME>` to be publicly published; a local path module has no naming convention requirements — it can be named anything
- C) Both registry modules and local path modules require the `version` argument — without it, `terraform init` raises an error for both source types
- D) Local path modules support the `?ref=` query parameter for version pinning; registry modules use the `version` argument

**Answer**: A, B

**Explanation**:
(A) `version` is only supported for registry sources. When `terraform init` processes a registry module with a `version` constraint, it queries the registry API, resolves the version, and downloads the source code to `.terraform/modules/`. For a local path module, `terraform init` simply registers the path in `.terraform/modules/modules.json` — no download occurs and `version` is not a valid argument. (B) Published Terraform Registry modules must follow the naming pattern `terraform-<PROVIDER>-<NAME>` (e.g., `terraform-aws-vpc`) for discoverability and registry compliance. Local modules in your own directory have no such naming requirement. (C) is wrong — `version` is optional even for registry modules (though recommended). (D) is wrong — `?ref=` applies to Git sources, not local paths.

---

### Question 11 — `terraform init` Re-run for New Module Source vs New Provider

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Contrast between why terraform init must be re-run after adding a new module source vs adding a new provider

**Question**:
Both adding a new `module` block with a new `source` and adding a new `required_providers` entry require re-running `terraform init`. Compare the specific reason `terraform init` is needed in each case — what does it do differently for modules vs providers?

- A) `terraform init` does the same thing in both cases — it validates syntax and writes a lock file
- B) When a new **module source** is added, `terraform init` needs to **download or register the module source code** into `.terraform/modules/` — without this, Terraform cannot find the module's `.tf` files and `terraform plan` fails with "module not installed"; when a new **provider** is added, `terraform init` needs to **download the provider plugin binary** into `.terraform/providers/` and update `.terraform.lock.hcl` with the provider's version and hash — without this, Terraform cannot make API calls to the provider
- C) When a new module source is added, `terraform init` updates `.terraform.lock.hcl` with the module's hash; when a new provider is added, it updates `.terraform/modules/modules.json`
- D) For new module sources, `terraform init` is optional if the source is a local path; for new providers, it is always required — there is no scenario where a provider can be used without running `terraform init`

**Answer**: B

**Explanation**:
`terraform init` performs distinct operations for modules and providers. For **modules**: it resolves the `source` URL, downloads remote module code (or registers local paths), and writes the mapping to `.terraform/modules/modules.json`. Running `terraform plan` without doing this for a new `module` block produces: "Module not installed. Run `terraform init`." For **providers**: it queries the provider registry, downloads the provider binary executable for the target platform, and records the version and cryptographic hash in `.terraform.lock.hcl`. Running `terraform plan` without installing a new provider produces: "Provider not installed." The two operations affect different directories (`.terraform/modules/` vs `.terraform/providers/`) and different metadata files (`modules.json` vs `.terraform.lock.hcl`). Note that for local path modules, the init step is still required to register the module even though no download occurs.

---

### Question 12 — Two Differences Between Module Input via Literal and via Expression

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Contrasting TWO differences between hardcoding a module input as a literal value vs passing it as an expression referencing a root variable

**Question**:
Compare these two `module` blocks:

**Block A** — literal values:
```hcl
module "networking" {
  source      = "./modules/networking"
  environment = "production"
  vpc_cidr    = "10.0.0.0/16"
}
```

**Block B** — expression references:
```hcl
module "networking" {
  source      = "./modules/networking"
  environment = var.environment
  vpc_cidr    = var.vpc_cidr
}
```

Which TWO statements correctly describe a difference between the two approaches? (Select two.)

- A) Block B allows the same module instantiation to receive different values depending on how the root configuration is called or what `.tfvars` values are supplied — the module behaviour can vary across environments without modifying the `module` block; Block A hardcodes the values and requires editing `main.tf` to change the environment or CIDR
- B) Block A creates a **plan-time constant** dependency for the module inputs — Terraform can resolve the values during the graph construction phase without needing to evaluate variable expressions; Block B introduces a dependency on root variable values that must be resolved before the module inputs are finalised, but this adds no meaningful performance overhead in practice
- C) Block A is always more secure than Block B — hardcoded values cannot be overridden by malicious `.tfvars` files
- D) Passing expressions as in Block B causes Terraform to re-create the module's resources on every `terraform apply`, while Block A keeps resources stable because the values never change

**Answer**: A, B

**Explanation**:
(A) is the primary practical difference: Block B makes the module reusable and environment-agnostic. By passing `var.environment` and `var.vpc_cidr`, the same module block can represent different infrastructure configurations simply by changing the input variables — via `-var` flags, `.tfvars` files, or workspace-specific variable sets. Block A bakes `"production"` and `"10.0.0.0/16"` directly into the configuration; changing them requires editing the source file. (B) describes a real but practically insignificant technical distinction: literal values in Block A are graph-time constants whereas Block B requires variable evaluation first. In any real Terraform run, this difference is imperceptible. (C) is a false security claim — the `source` argument and module identity provide no protection against malicious inputs in either case. (D) is incorrect — Terraform plans changes based on the resolved value compared to state, not based on whether the value came from a literal or variable.

---

### Question 13 — Standard Module File Structure vs Monolithic Single-File Module

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Deep contrast between the recommended standard module file layout and collapsing everything into a single main.tf

**Question**:
The Terraform community recommends a standard module file structure:

```
module/
├── main.tf       — core resource definitions
├── variables.tf  — input variable declarations
├── outputs.tf    — output value declarations
└── versions.tf   — required_providers and terraform version constraint
```

Compare this against putting all declarations in a single `main.tf`. What are the two most significant practical differences?

- A) Using multiple files is mandatory — Terraform raises a validation error if variable declarations appear in `main.tf`
- B) Terraform is **file-agnostic** within a module directory — all `.tf` files in a directory are merged into a single namespace at parse time, so splitting across files versus using one file makes **no functional difference to Terraform itself**; the practical differences are entirely for **human maintainability**: the standard layout makes it immediately clear where to find variable definitions (`variables.tf`) and exported values (`outputs.tf`), while a monolithic `main.tf` requires scanning the entire file; for published or shared modules, the standard layout is a community convention that sets user expectations about where to look for the module's interface
- C) Using the standard layout allows Terraform to parse files in parallel, improving plan performance for large modules; a single `main.tf` forces sequential parsing
- D) `versions.tf` must be a separate file — it cannot be placed in `main.tf` because `terraform init` reads `versions.tf` before any other file during provider installation

**Answer**: B

**Explanation**:
Terraform treats all `.tf` files in a directory as part of a **single flat namespace** — there is no concept of file-level scoping or execution order. A `resource` block in `variables.tf` is just as valid as one in `main.tf`; an `output` block in `main.tf` works identically to one in `outputs.tf`. Splitting declarations across files versus keeping them in one file makes zero functional difference to how Terraform plans or applies. The standard layout is entirely a **convention for humans**: it makes modules navigable, reduces the cognitive load of contributors, and matches the expectations of anyone who has worked with public Terraform modules. For published modules on the Terraform Registry, following the standard structure is especially important because users will look in `variables.tf` for the module's interface and `outputs.tf` for its exported values. Options A, C, and D all incorrectly imply that Terraform's internal processing depends on the file structure.

---
