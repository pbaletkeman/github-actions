# Terraform Associate (004) — Study Prompts
## AI-Generated Study Material Prompts

Use each prompt below with an AI tool (GitHub Copilot, ChatGPT, Claude) to generate detailed study content for the HashiCorp Certified: Terraform Associate (004) exam.

---

### OBJECTIVE 1: Infrastructure as Code (IaC) with Terraform

> **Exam objective: Understand what IaC is, its advantages, and how Terraform fits the multi-cloud landscape.**

#### Prompt 1: What is Infrastructure as Code?

You are not done until you save the response and files  to `terraform/learning/`

"Create a comprehensive study guide on Infrastructure as Code (IaC) concepts tested on the Terraform Associate exam:
- Define IaC: managing and provisioning infrastructure through machine-readable configuration files rather than manual processes
- Core IaC principles: idempotency, declarative vs imperative approaches — explain the difference with examples
- Advantages of IaC over manual provisioning: consistency, repeatability, version control, peer review, automation, documentation-as-code, disaster recovery speed
- Declarative vs imperative IaC tools — where Terraform fits (declarative) vs tools like Ansible scripts (imperative)
- How IaC enables collaboration: storing configuration in Git, code review workflows, branching strategies for infrastructure changes
- Terraform's approach: desired state declaration, Terraform computes the diff and creates an execution plan
- Multi-cloud and service-agnostic advantages: one workflow for AWS, Azure, GCP, and hundreds of other providers using the same HCL syntax
- Hybrid cloud use cases: managing on-premises VMware alongside public cloud in one workspace
- Real-world scenario: describe how IaC would let a team rebuild an entire environment in minutes vs days manually
- Include 3 exam-style questions with answers on IaC concepts; for each question, wrap the answer letter and full explanation inside <details><summary>Answer</summary> ... </details> HTML tags so answers are collapsed by default
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant HCL configuration or CLI commands, the expected outcome, and which exam sub-objective is being demonstrated."

---

### OBJECTIVE 2: Terraform Fundamentals

> **Exam objective: Understand providers, the plugin model, state purpose, and how to install/version providers.**

#### Prompt 2: Terraform Providers and the Plugin Model

You are not done until you save the response and files  to `terraform/learning/`

"Write a detailed guide on Terraform providers for the Associate exam:
- What a provider is: a plugin that translates Terraform HCL into API calls for a specific platform (AWS, Azure, GCP, GitHub, Kubernetes, etc.)
- How Terraform interacts with providers: the plugin model — provider binary downloaded separately from Terraform core
- The `provider` block: purpose, required_providers block, `source` and `version` constraints
- Provider source address format: `registry.terraform.io/hashicorp/aws` — namespace/type
- Provider tiers on the Terraform Public Registry:
  - **Official**: authored and maintained by HashiCorp (e.g., `hashicorp/aws`, `hashicorp/azurerm`, `hashicorp/google`)
  - **Partner**: built and maintained by a technology partner, verified by HashiCorp (e.g., `datadog/datadog`, `cloudflare/cloudflare`)
  - **Community**: published by individual or community contributors — use with caution
  - **Archived**: previously published providers no longer actively maintained
- Version constraint operators: `=`, `!=`, `>`, `>=`, `<`, `<=`, `~>` (pessimistic constraint — know this one)
- How `terraform init` downloads providers and populates `.terraform/providers/`
- The dependency lock file (`.terraform.lock.hcl`): purpose, what it records, when to commit it to version control, and why
- `terraform providers lock` and `terraform providers mirror` commands
- Multiple provider configurations using `alias` — when and why you'd use two configurations of the same provider
- Writing configuration that uses two different providers in one workspace
- Code examples: `required_providers` block, aliased provider, provider in a module
- 3 exam-style questions with answers on provider configuration and versioning; for each question, wrap the answer letter and full explanation inside <details><summary>Answer</summary> ... </details> HTML tags so answers are collapsed by default
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant HCL configuration or CLI commands, the expected outcome, and which exam sub-objective is being demonstrated."

#### Prompt 3: Terraform State — Purpose and Management

You are not done until you save the response and files  to `terraform/learning/`

"Create a guide on Terraform state fundamentals for the exam:
- What the state file is: `terraform.tfstate` — maps Terraform configuration to real-world resources
- Why state is required: Terraform needs to know what it manages to compute diffs, understand ordering, and track metadata
- What the state file contains: resource IDs, attributes, dependencies, provider metadata
- State and performance: state allows Terraform to skip API calls when planning unchanged resources (`-refresh=false`)
- Local state: default behavior, stored in `terraform.tfstate` in the working directory — limitations for teams
- Why you should **never** manually edit the state file directly
- Sensitive data in state: the state file may contain secrets in plaintext — implications for storage security
- The `terraform state list` command: list all resources tracked in state
- The `terraform state show` command: show attributes of a specific resource
- The `terraform show` command: show human-readable state or plan
- How Terraform determines whether a resource needs to be created, updated, or destroyed by comparing desired state (config) to current state (state file) to actual state (real infrastructure)
- Code examples: sample `terraform.tfstate` JSON snippet annotated to explain each field
- 3 exam-style questions with answers; for each question, wrap the answer letter and full explanation inside <details><summary>Answer</summary> ... </details> HTML tags so answers are collapsed by default
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant HCL configuration or CLI commands, the expected outcome, and which exam sub-objective is being demonstrated."

---

### OBJECTIVE 3: Core Terraform Workflow

> **Exam objective: Know every CLI command in the write → plan → apply → destroy cycle and what each does.**

#### Prompt 4: The Core Terraform Workflow — All CLI Commands

You are not done until you save the response and files  to `terraform/learning/`

"Write a comprehensive guide on the Terraform CLI workflow for the Associate exam. For each command include: purpose, key flags, what it reads/writes, and a practical example:

- **terraform init**:
  - Initializes working directory, downloads providers and modules
  - Creates/updates `.terraform/` directory and `.terraform.lock.hcl`
  - Flags: `-upgrade` (upgrade providers to latest matching constraints), `-backend=false`, `-reconfigure`
  - Safe to run repeatedly — idempotent

- **terraform fmt**:
  - Formats `.tf` files to canonical HCL style (indentation, alignment)
  - Flags: `-recursive` (process subdirectories), `-check` (exit non-zero if changes needed, for CI), `-diff` (show diff)
  - Does NOT change functionality — purely cosmetic

- **terraform validate**:
  - Validates HCL syntax and internal consistency (references, required arguments)
  - Does NOT contact APIs — does not verify that resources exist
  - Requires `terraform init` to have been run first (to know provider schema)

- **terraform plan**:
  - Creates an execution plan showing what Terraform will do
  - Reads current state, calls provider APIs to refresh, computes diff
  - Output symbols: `+` create, `-` destroy, `~` update in-place, `-/+` destroy and recreate, `<=` read data source
  - Flags: `-out=plan.tfplan` (save plan to file), `-target=resource.address`, `-refresh-only`, `-destroy`
  - A saved plan file guarantees `apply` will execute exactly what was planned

- **terraform apply**:
  - Executes the plan to create/update/destroy resources
  - Prompts for confirmation unless `-auto-approve` is passed
  - Updates state file after each resource change
  - Can apply a saved plan file: `terraform apply plan.tfplan`
  - `-replace=<resource_address>`: force replacement (destroy and recreate) of a specific resource — replaces the deprecated `terraform taint` command

- **terraform destroy**:
  - Destroys all resources managed in the current workspace
  - Equivalent to `terraform apply -destroy`
  - Prompts for confirmation

- **terraform output**: show output values from state
- **terraform refresh**: update state to match real infrastructure (deprecated — use `-refresh-only` plan instead)

- **terraform workspace**: manage multiple named workspaces (separate state files) within a single configuration directory
  - `terraform workspace list`: list all workspaces; active workspace marked with `*`
  - `terraform workspace new <name>`: create and switch to a new workspace
  - `terraform workspace select <name>`: switch to an existing workspace
  - `terraform workspace show`: print the name of the current workspace
  - `terraform workspace delete <name>`: delete a workspace (cannot delete the active workspace or the `default` workspace)
  - State for non-default workspaces stored at `terraform.tfstate.d/<workspace_name>/terraform.tfstate`
  - The `default` workspace always exists and cannot be deleted
  - Use `terraform.workspace` in expressions to branch configuration by workspace name (e.g., different instance sizes per environment)
  - Key distinction: CLI workspaces share the same configuration and backend but have separate state files; HCP Terraform workspaces are fully independent environments with separate config, state, variables, and team access

Include a flowchart description of the full workflow and 4 exam-style questions covering command behavior edge cases — include at least one question on `terraform workspace` commands and one on `terraform apply -replace`; for each question, wrap the answer letter and full explanation inside <details><summary>Answer</summary> ... </details> HTML tags so answers are collapsed by default
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant HCL configuration or CLI commands, the expected outcome, and which exam sub-objective is being demonstrated."

---

### OBJECTIVE 4: Terraform Configuration (HCL)

> **Exam objective: The largest topic area. Write and interpret HCL — resources, data, variables, outputs, functions, expressions, dependencies, lifecycle, custom conditions, sensitive data.**

#### Prompt 5: Resource and Data Blocks

You are not done until you save the response and files  to `terraform/learning/`

"Create a guide on `resource` and `data` blocks for the exam:
- **Resource block**: declares a piece of infrastructure to manage
  - Syntax: `resource \"<provider_type>\" \"<name>\" { ... }` — resource address is `<provider_type>.<name>`
  - Required vs optional arguments — where to find what arguments a resource accepts (provider docs)
  - Resource behaviors: create, read (refresh), update, destroy — not all resources support all behaviors
  - Resource addressing for references and targeting: `aws_instance.web`, `aws_instance.web.id`
- **Data block**: reads existing infrastructure not managed by this workspace
  - Syntax: `data \"<type>\" \"<name>\" { ... }` — referenced as `data.<type>.<name>.<attribute>`
  - Key difference from resource: data sources are read-only; they never create or destroy anything
  - When to use data vs resource: looking up an existing VPC ID, getting the latest AMI, reading a secret from Vault
  - Data sources and plan timing: most data sources read during `terraform plan`; some read during apply
- **Lifecycle meta-arguments** in resource blocks:
  - `create_before_destroy`: create replacement before destroying original — use for zero-downtime
  - `prevent_destroy`: prevents `terraform destroy` from removing this resource — useful for databases
  - `ignore_changes`: tell Terraform to ignore changes to specific attributes made outside Terraform
  - `replace_triggered_by`: force replacement when another resource changes
- Code examples: resource with lifecycle block, data source lookup used in a resource argument
- 3 exam-style questions with answers; for each question, wrap the answer letter and full explanation inside <details><summary>Answer</summary> ... </details> HTML tags so answers are collapsed by default
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant HCL configuration or CLI commands, the expected outcome, and which exam sub-objective is being demonstrated."

#### Prompt 6: Variables, Locals, and Outputs

You are not done until you save the response and files  to `terraform/learning/`

"Write a comprehensive guide on input variables, local values, and output values for the exam:
- **Input variables** (`variable` block):
  - Declaring variables: `type`, `default`, `description`, `validation`, `sensitive` arguments
  - Setting variable values: CLI flag `-var`, `.tfvars` files, `TF_VAR_` environment variables, HCP Terraform variable sets — **precedence order** (exam topic)
  - `terraform.tfvars` and `*.auto.tfvars`: automatically loaded
  - `sensitive = true`: masks value in plan/apply output and state file UI
  - Variable type constraints: string, number, bool, list(type), map(type), set(type), object({...}), tuple([...])
  - `validation` block inside a variable: `condition` expression and `error_message`

- **Local values** (`locals` block):
  - Purpose: named expressions to avoid repetition — computed once, referenced as `local.<name>`
  - When to use locals vs variables: locals are internal to the module; variables are inputs from outside
  - Code example: constructing a common tag map using locals

- **Output values** (`output` block):
  - Purpose: expose values from a module/workspace to the caller or the CLI
  - `value`, `description`, `sensitive` arguments
  - Outputs in root module: displayed after `terraform apply`, accessible with `terraform output`
  - Outputs in child modules: accessed by caller as `module.<module_name>.<output_name>`
  - `sensitive = true` on outputs: value hidden in terminal but still in state file

- Variable precedence list (from lowest to highest priority): default → `.tfvars` file → `*.auto.tfvars` → `-var-file` flag → `-var` flag → environment variable
- Code examples for each block type with explanations
- 4 exam-style questions covering variable scoping, precedence, and output access; for each question, wrap the answer letter and full explanation inside <details><summary>Answer</summary> ... </details> HTML tags so answers are collapsed by default
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant HCL configuration or CLI commands, the expected outcome, and which exam sub-objective is being demonstrated."

#### Prompt 7: Complex Types and Collections in Terraform

You are not done until you save the response and files  to `terraform/learning/`

"Create a guide on Terraform complex types — commonly tested on the exam:
- **Primitive types**: string, number, bool — conversions between them
- **Collection types**:
  - `list(type)`: ordered sequence of values of one type; accessed by index `var.my_list[0]`
  - `map(type)`: key-value pairs where all values are one type; accessed by key `var.tags[\"env\"]`
  - `set(type)`: unordered unique values — cannot be accessed by index; often used with `for_each`
- **Structural types**:
  - `object({key = type, ...})`: like a map but each attribute can have a different type
  - `tuple([type, type, ...])`: like a list but each element can have a different type
- **any** type: disables type checking for a variable — use sparingly
- Converting between types: `tolist()`, `toset()`, `tomap()` functions
- **`count` meta-argument**: create multiple instances of a resource; `count.index` for uniqueness; limitations with order sensitivity
- **`for_each` meta-argument**: create one instance per element of a map or set; `each.key` and `each.value`; preferred over count for named resources
- **`for` expressions**: transform collections: `[for s in var.list : upper(s)]`, `{for k, v in var.map : k => upper(v)}`
- **Splat expressions**: `aws_instance.web[*].id` — shorthand for extracting a list of attribute values from count-based resources
- **`dynamic` block**: generate repeated nested blocks programmatically based on a collection
- Code examples: resource with `for_each` over a map, `dynamic` block for security group rules, `for` expression to transform a list
- 4 exam-style questions with answers; for each question, wrap the answer letter and full explanation inside <details><summary>Answer</summary> ... </details> HTML tags so answers are collapsed by default
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant HCL configuration or CLI commands, the expected outcome, and which exam sub-objective is being demonstrated."

#### Prompt 8: Built-in Functions and Expressions

You are not done until you save the response and files  to `terraform/learning/`

"Write a reference guide on Terraform built-in functions required for the exam:
- How to test functions interactively: `terraform console` — run expressions without applying
- **String functions**: `upper()`, `lower()`, `trimspace()`, `split()`, `join()`, `replace()`, `substr()`, `format()`, `formatlist()`, `regex()`, `regexall()`, `startswith()`, `endswith()`
- **Numeric functions**: `abs()`, `ceil()`, `floor()`, `max()`, `min()`, `parseint()`, `signum()`
- **Collection functions**: `length()`, `contains()`, `keys()`, `values()`, `lookup()`, `merge()`, `flatten()`, `distinct()`, `concat()`, `element()`, `index()`, `range()`, `toset()`, `tolist()`, `tomap()`, `zipmap()`
- **Type conversion functions**: `tostring()`, `tonumber()`, `tobool()`
- **Encoding functions**: `base64encode()`, `base64decode()`, `jsonencode()`, `jsondecode()`, `yamlencode()`, `yamldecode()`
- **Filesystem functions**: `file()`, `filebase64()`, `templatefile()` — read local file contents into configuration
- **Date/time functions**: `timestamp()`, `formatdate()`, `timeadd()`
- **IP networking functions**: `cidrhost()`, `cidrnetmask()`, `cidrsubnet()` — useful for VPC/subnet calculations
- **Conditional expression**: `condition ? true_val : false_val` — inline if/else
- **`can()` function**: returns true if an expression evaluates without error — used in validation conditions
- **`try()` function**: evaluates expressions in order and returns the first that succeeds without error
- Code examples showing each function category with input and output
- Emphasis on functions most likely to appear: `lookup()`, `merge()`, `flatten()`, `jsonencode()`, `templatefile()`, `cidrsubnet()`
- 3 exam-style questions with answers; for each question, wrap the answer letter and full explanation inside <details><summary>Answer</summary> ... </details> HTML tags so answers are collapsed by default
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant HCL configuration or CLI commands, the expected outcome, and which exam sub-objective is being demonstrated."

#### Prompt 9: Resource Dependencies and Lifecycle

You are not done until you save the response and files  to `terraform/learning/`

"Create a guide on Terraform dependency management for the exam:
- **Implicit dependencies**: Terraform automatically determines order when one resource references another's attribute — `resource.type.name.attribute` creates a dependency edge
- **Explicit dependencies** with `depends_on`: use when a dependency exists that Terraform cannot detect from configuration alone (e.g., a resource depends on a side effect of another)
- How Terraform builds a **dependency graph** (DAG): explains why resources are created/destroyed in a specific order
- **Parallel execution**: resources with no dependency relationship are created/destroyed in parallel
- **`-target` flag**: apply only to a specific resource — use cases and cautions (can leave state inconsistent)
- **`terraform graph`**: output a DOT-format dependency graph for visualization
- **Resource lifecycle**:
  - Default lifecycle: plan diff → destroy old → create new (for resources that must be replaced)
  - `create_before_destroy = true`: create replacement first, then destroy old — required for some resources
  - `prevent_destroy = true`: Terraform will refuse to plan any operation that would destroy the resource
  - `ignore_changes = [attribute_name]`: Terraform will not compute a diff for these attributes
  - `replace_triggered_by`: force replacement of this resource when a referenced resource or attribute changes
- **`provider` meta-argument**: select which provider configuration a resource uses — required when multiple configurations of the same provider exist via `alias`
  - Syntax: `provider = aws.us-east-1` inside the resource block
  - If omitted, Terraform uses the default (non-aliased) provider configuration for that resource type
  - Use case: deploy resources to two AWS regions in the same workspace by aliasing the `aws` provider twice and using `provider =` on each resource
- **`moved` block**: refactor resource addresses without destroying and recreating — used when renaming resources or moving them into/out of modules
- **`removed` block**: remove a resource from state without destroying the actual infrastructure
- Code examples: implicit reference creating order, `depends_on` for hidden dependency, lifecycle with `create_before_destroy`, resource using `provider` meta-argument with an aliased provider
- 3 exam-style questions; for each question, wrap the answer letter and full explanation inside <details><summary>Answer</summary> ... </details> HTML tags so answers are collapsed by default
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant HCL configuration or CLI commands, the expected outcome, and which exam sub-objective is being demonstrated."

#### Prompt 10: Custom Conditions and Sensitive Data

You are not done until you save the response and files  to `terraform/learning/`

"Write a guide on validation, custom conditions, and sensitive data handling for the exam:

- **Variable `validation` block**:
  - Syntax: nested inside a `variable` block with `condition` and `error_message`
  - `condition` must be a boolean expression using the variable's value
  - Use `can()` in conditions to handle type conversion errors gracefully
  - Example: validate that a string variable is one of an allowed set of values

- **`check` block** (Terraform 1.5+):
  - Assert conditions about infrastructure state after apply
  - Not a hard error — generates a warning, does not fail the plan/apply
  - Use case: verify an HTTP endpoint returns 200 after creating a server
  - `data` block inside `check`: read data that only applies to the assertion

- **`postcondition` and `precondition` blocks** inside resource lifecycle:
  - `precondition`: checked before the resource is created/updated — hard error if fails
  - `postcondition`: checked after the resource is created/updated — hard error if fails
  - Useful for documenting assumptions and invariants in modules

- **Sensitive data management**:
  - `sensitive = true` on variable: value masked in plan/apply output
  - `sensitive = true` on output: value masked in output display
  - Sensitive values are still stored in state file in plaintext — secure your state storage
  - `nonsensitive()` function: explicitly mark a value as non-sensitive (use with caution)
  - `sensitive()` function: mark a value as sensitive

- **HashiCorp Vault integration**:
  - The Vault provider allows reading secrets from Vault into Terraform as data sources
  - Benefits: secrets never stored in `.tfvars` files or version control
  - Dynamic credentials: HCP Terraform can use dynamic credentials to authenticate to cloud providers via Vault

- Code examples: variable with validation, check block with data source, sensitive variable with output
- 3 exam-style questions with answers; for each question, wrap the answer letter and full explanation inside <details><summary>Answer</summary> ... </details> HTML tags so answers are collapsed by default
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant HCL configuration or CLI commands, the expected outcome, and which exam sub-objective is being demonstrated."

---

### OBJECTIVE 5: Terraform Modules

> **Exam objective: Understand how to use and create modules, source types, variable scope, and versioning.**

#### Prompt 11: Terraform Modules — Using, Creating, and Versioning

You are not done until you save the response and files  to `terraform/learning/`

"Create a comprehensive guide on Terraform modules for the exam:
- **What a module is**: a container for multiple resources used together; every Terraform configuration is a module (the root module)
- **Module types**:
  - Root module: the working directory where `terraform init` is run
  - Child module: a module called from another module using a `module` block
  - Published module: a module in the Terraform Public Registry or a private registry

- **Module sources** — the `source` argument accepts:
  - Local path: `source = \"./modules/vpc\"` — relative path, `./` required
  - Terraform Registry: `source = \"hashicorp/consul/aws\"` — format: `namespace/module/provider`
  - GitHub: `source = \"github.com/org/repo\"` or `source = \"github.com/org/repo//subdir\"`
  - Generic Git: `source = \"git::https://...\"` or `source = \"git::ssh://...\"`
  - HTTP archive, S3 bucket, GCS bucket

- **`module` block**:
  - `source` and `version` arguments
  - Input variable values passed as arguments to the module block
  - `depends_on` and `providers` meta-arguments for modules

- **Module versioning**:
  - `version` argument — only supported for registry sources (not local paths)
  - Same constraint operators as provider versions: `~>`, `>=`, `=`, etc.
  - `terraform init -upgrade` to pull newer compatible versions

- **Variable scope and encapsulation**:
  - Module inputs: declared as `variable` blocks inside the module — callers pass values via module block arguments
  - Module outputs: declared as `output` blocks inside the module — accessed by caller as `module.<name>.<output>`
  - Resources inside a module are NOT directly addressable from outside: `module.<name>.<resource_type>.<resource_name>` for targeting only
  - No automatic inheritance of root module variables — must pass explicitly

- **Standard module structure** (exam topic):
  - `main.tf`: main resources
  - `variables.tf`: input variable declarations
  - `outputs.tf`: output declarations
  - `README.md`: documentation
  - `versions.tf`: required_providers block

- **Module composition**: passing outputs of one module as inputs to another
- Code examples: calling a registry module with version pinning, local module call, accessing module output
- 4 exam-style questions on module sources, scoping, and versioning; for each question, wrap the answer letter and full explanation inside <details><summary>Answer</summary> ... </details> HTML tags so answers are collapsed by default
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant HCL configuration or CLI commands, the expected outcome, and which exam sub-objective is being demonstrated."

---

### OBJECTIVE 6: Terraform State Management

> **Exam objective: Understand state backends, state locking, remote state, and managing drift.**

#### Prompt 12: State Backends, Locking, and Remote State

You are not done until you save the response and files  to `terraform/learning/`

"Write an in-depth state management guide for the exam:
- **Local backend**:
  - Default backend — stores state in `terraform.tfstate` in the working directory
  - Not suitable for teams: no locking, no sharing, no encryption at rest
  - `terraform.tfstate.backup`: created automatically before each apply

- **The `backend` block**:
  - Configured inside the `terraform {}` block in configuration
  - Specifies where state is stored (S3, GCS, Azure Blob, HCP Terraform, HTTP, etc.)
  - `terraform init` must be re-run after changing backend configuration — prompts to migrate state
  - Backend configuration cannot contain variable references (evaluated before variables)

- **State locking**:
  - Prevents concurrent state modifications — only one operation at a time per workspace
  - Not all backends support locking — know which ones do (S3 + DynamoDB, GCS, Azure Blob, HCP Terraform)
  - `terraform force-unlock <lock-id>`: manually release a stuck lock — use with extreme caution
  - Lock timeout: configurable per backend

- **Remote backend with S3 + DynamoDB** (exam example):
  - S3 bucket stores state file; DynamoDB table provides locking
  - `encrypt = true` for S3 backend
  - `dynamodb_table` argument for locking

- **Managing resource drift**:
  - Drift: real infrastructure differs from Terraform state (changed outside Terraform)
  - `terraform plan -refresh-only`: create a plan that only updates state to match reality — does NOT apply config changes
  - `terraform apply -refresh-only`: update state to match actual infrastructure without changing resources
  - Regular drift detection is a best practice; HCP Terraform does this automatically

- **`moved` block**: update state when you rename a resource or move it into/out of a module — no destroy/recreate
- **`removed` block**: tell Terraform to stop managing a resource without destroying it — removes from state
- **`terraform state mv`**: imperatively move resources in state (older approach; `moved` block is preferred)
- **`terraform state rm`**: remove a resource from state without destroying real infrastructure
- **`terraform state pull`** and **`terraform state push`**: download/upload raw state

- Code examples: `backend` block for S3, `moved` block, `removed` block, `-refresh-only` plan output
- 4 exam-style questions on state locking, drift, and backend configuration; for each question, wrap the answer letter and full explanation inside <details><summary>Answer</summary> ... </details> HTML tags so answers are collapsed by default
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant HCL configuration or CLI commands, the expected outcome, and which exam sub-objective is being demonstrated."

---

### OBJECTIVE 7: Maintaining Infrastructure with Terraform

> **Exam objective: Import existing resources, inspect state, and use verbose logging to debug.**

#### Prompt 13: Importing Existing Infrastructure and Inspecting State

You are not done until you save the response and files  to `terraform/learning/`

"Create a guide on infrastructure import and state inspection for the exam:
- **Why import is needed**: infrastructure created outside Terraform (manually, by another tool, or before Terraform adoption) must be imported before Terraform can manage it
- **The `import` block** (Terraform 1.5+ — preferred method):
  - Declare an `import` block with `to` (resource address) and `id` (cloud resource ID)
  - Run `terraform plan` to see the import and generate configuration
  - `terraform apply` executes the import and writes to state
  - `terraform generate config for` flag to have Terraform write the resource config for you

- **CLI import command** (`terraform import <address> <id>`): older approach
  - Does NOT generate HCL configuration — you must write the resource block yourself before importing
  - Writes the resource directly into state

- **When to use each approach**: `import` block is preferred; generates config and shows in plan; CLI command is still supported

- **Inspecting state with CLI commands**:
  - `terraform state list`: list all resources in state (optionally filter by address prefix)
  - `terraform state show <resource_address>`: show all attributes of a specific resource from state
  - `terraform show`: show human-readable state or the contents of a saved plan file
  - `terraform output`: show output values; `terraform output <name>` for a specific output; `-json` flag
  - `terraform console`: interactive expression evaluation — test functions, inspect variables

- **Verbose logging**:
  - `TF_LOG` environment variable: levels are TRACE, DEBUG, INFO, WARN, ERROR
  - `TF_LOG=TRACE terraform apply` for maximum detail — shows all provider API calls
  - `TF_LOG_PATH=./terraform.log` to write logs to a file
  - Use DEBUG for troubleshooting provider issues; use TRACE for deep debugging
  - `TF_LOG_CORE` and `TF_LOG_PROVIDER`: separately control core and provider log levels

- Code examples: `import` block with `terraform plan` output, `state list` and `state show` outputs
- 3 exam-style questions on import and logging; for each question, wrap the answer letter and full explanation inside <details><summary>Answer</summary> ... </details> HTML tags so answers are collapsed by default
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant HCL configuration or CLI commands, the expected outcome, and which exam sub-objective is being demonstrated."

---

### OBJECTIVE 8: HCP Terraform

> **Exam objective: The second largest topic. Know workspaces, runs, state storage, variable sets, governance, dynamic credentials, drift detection.**

#### Prompt 14: HCP Terraform — Workspaces, Runs, and State

You are not done until you save the response and files  to `terraform/learning/`

"Write a guide on HCP Terraform core functionality for the exam:
- **What HCP Terraform is**: HashiCorp's hosted service for Terraform — manages state, runs, variables, and collaboration in the cloud (formerly Terraform Cloud)
- **Difference from Terraform Community Edition**: Community Edition is a local CLI tool; HCP Terraform adds remote state, team collaboration, policy enforcement, audit logging, and SSO
- **HCP Terraform workspaces**:
  - Each workspace has its own state file, variable store, and run history
  - Not the same as local Terraform workspaces (`terraform workspace` command)
  - Execution modes: remote (HCP runs the plan/apply on managed workers), local (CLI drives execution, state stored remotely), agent (runs on customer-managed agent)
  - Workspace settings: Terraform version, execution mode, auto-apply, run triggers

- **HCP Terraform run lifecycle**: trigger → plan → policy check → apply confirmation → apply
- **CLI-driven workflow**: configure `cloud` block in `terraform.tf` → `terraform login` → `terraform init` → normal CLI commands run remotely
- **`cloud` block vs `backend "remote"`**: the `cloud` block (available since Terraform 1.1) is the preferred way to connect to HCP Terraform; the older `backend "remote"` block still works but is deprecated for HCP Terraform usage; the `cloud` block supports workspace tag-based filtering and is more feature-complete
- **VCS-driven workflow**: connect workspace to a GitHub/GitLab/etc. repo → push to branch triggers speculative plan; merge to default branch triggers apply
- **`terraform login`**: authenticates CLI to HCP Terraform, stores token in `~/.terraform.d/credentials.tfrc.json`
- **Migrating state to HCP Terraform**: add `cloud` block to backend config → `terraform init` → follow migration prompts

- **Variable sets**: reusable collections of variables applied to multiple workspaces — avoid repeating common variables (e.g., cloud credentials)
- **Remote state data source**: `terraform_remote_state` data source — read outputs from another HCP Terraform workspace
- **Run triggers**: automatically queue a run in workspace B when workspace A applies successfully

- **Projects**: organize multiple workspaces — control access at the project level; `project_id` on workspace

- Code examples: `cloud` block configuration, variable set usage, `terraform_remote_state` data source
- 4 exam-style questions on HCP Terraform workflows and workspace management; for each question, wrap the answer letter and full explanation inside <details><summary>Answer</summary> ... </details> HTML tags so answers are collapsed by default
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant HCL configuration or CLI commands, the expected outcome, and which exam sub-objective is being demonstrated."

#### Prompt 15: HCP Terraform Governance, Security, and Advanced Features

You are not done until you save the response and files  to `terraform/learning/`

"Create a guide on HCP Terraform collaboration and governance features for the exam:
- **Teams and permissions**:
  - Organizations contain teams; teams have workspace-level permissions
  - Workspace permissions: read, plan, write, admin
  - Organization-level roles: owner, member
  - Team tokens vs user tokens for API access

- **Policy enforcement**:
  - Sentinel: HashiCorp's policy-as-code framework — write policies that govern Terraform plans
  - OPA (Open Policy Agent): alternative policy engine supported in HCP Terraform
  - Policy sets applied to workspaces or organization-wide
  - Enforcement levels: advisory (warn), soft mandatory (can override), hard mandatory (cannot override)
  - Policies run between `plan` and `apply` in the run lifecycle

- **Health assessments**:
  - HCP Terraform automatically runs drift detection on a schedule
  - Reports when real infrastructure differs from Terraform state
  - Also checks for failed continuous validation (`check` blocks)
  - Workspace health dashboard shows assessment status

- **Dynamic provider credentials**:
  - Eliminates long-lived static credentials (API keys) in workspace variables
  - Uses OIDC token issued by HCP Terraform to authenticate to cloud providers (AWS, Azure, GCP)
  - Vault also supported for dynamic credentials injection
  - More secure: credentials scoped to the run, not stored persistently

- **Private Registry**:
  - Publish private modules and providers visible only to your organization
  - Same module consumption syntax as public registry — just different source address
  - Module versioning via Git tags

- **Audit logging**: organization-level log of all actions — who ran what, when
- **Change requests**: structured workflow for proposing infrastructure changes with approval gates
- **Explorer**: organization-wide visibility into resources and workspace health

- Code examples: sentinel policy example (advisory level), dynamic credentials configuration
- 4 exam-style questions on policy enforcement, teams, and health features; for each question, wrap the answer letter and full explanation inside <details><summary>Answer</summary> ... </details> HTML tags so answers are collapsed by default
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant HCL configuration or CLI commands, the expected outcome, and which exam sub-objective is being demonstrated."

---

### EXAM PRACTICE

#### Prompt 16: Practice Exam Questions — All Objectives

You are not done until you save the response and files  to `terraform/learning/`

"Generate 25 realistic multiple-choice and true/false practice questions for the HashiCorp Certified Terraform Associate (004) exam. Format each question as:

**Q{N}: [Question text]**
A) ...
B) ...
C) ...
D) ...
<details>
<summary>Answer</summary>

**Answer: [Letter]**
**Explanation**: [2–3 sentences explaining why the answer is correct and why the others are wrong]

</details>

Distribution matching all 8 exam objectives:
- 3 questions on IaC concepts (Objective 1)
- 3 questions on providers, state, plugin model (Objective 2)
- 4 questions on CLI commands and core workflow (Objective 3)
- 6 questions on HCL configuration — resources, variables, functions, types, lifecycle (Objective 4)
- 2 questions on modules (Objective 5)
- 3 questions on state management and backends (Objective 6)
- 2 questions on import and logging (Objective 7)
- 2 questions on HCP Terraform (Objective 8)

Include a mix of:
- Conceptual questions (what does X do, what is the difference between A and B)
- HCL reading questions (what will this configuration do, what error will occur, what value is produced)
- Command behavior questions (what does `terraform plan -refresh-only` do, what happens when you run `terraform init -upgrade`)
- Best-practice questions (which approach is preferred for X scenario)
- True/false questions on common misconceptions (e.g., 'terraform validate contacts the cloud provider API — true or false')
- Generate 5 practical real-world scenarios — each presenting a realistic developer situation where understanding the correct answer to an exam-style question is critical. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, the question it raises, the correct answer, and a full explanation of why the other options are wrong."

#### Prompt 17: Hands-On Capstone Project

You are not done until you save the response and files  to `terraform/learning/`

"Create a complete end-to-end Terraform project that exercises all 8 exam objectives. Do not use a real cloud provider — use the `random`, `local`, and `null` providers which require no credentials.

**Project goal**: Provision a simulated application environment with multiple resources, a reusable module, remote state, and validation.

**Required tasks — implement each with full Terraform HCL**:
1. Create a root module with `required_providers` block pinning `hashicorp/random` and `hashicorp/local` with version constraints using `~>`
2. Declare 3 input variables: `environment` (string, validated to be 'dev', 'staging', or 'prod'), `app_name` (string), `instance_count` (number, default 2)
3. Create a `locals` block that builds a name prefix: `\"\${var.app_name}-\${var.environment}\"`
4. Use `random_pet` resource with `for_each` over a `toset` of instance names to create multiple uniquely-named resources
5. Write a child module in `./modules/config-file` that accepts inputs and uses the `local_file` resource to write a config file to disk — demonstrate variable scope and output values
6. Call the child module from the root module and access its output
7. Declare 2 output values: one sensitive (mark it `sensitive = true`), one that uses a `for` expression to collect all random pet names into a list
8. Add a `validation` block to the `environment` variable and a `check` block that verifies the config file was created
9. Add a `lifecycle` block with `prevent_destroy = true` to one resource
10. Show the `backend` block for an S3 remote backend (write the HCL even though it won't execute without credentials — explain state locking configuration)
11. Write a `moved` block to simulate renaming a resource from `random_pet.old_name` to `random_pet.new_name`
12. Run through the complete workflow: `terraform init`, `terraform fmt`, `terraform validate`, `terraform plan -out=plan.tfplan`, `terraform apply plan.tfplan`, `terraform state list`, `terraform state show`, `terraform output`, then `terraform destroy`

For each task: include the HCL code, the expected CLI output at key steps, and a note identifying which exam objective it demonstrates
- After all 12 tasks, generate 5 practical real-world scenarios that extend the capstone project — for example, recovering from a stuck state lock, handling a `prevent_destroy` error, or migrating the local backend to S3. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the problem description, the HCL or CLI steps to resolve it, the expected outcome, and which exam sub-objective is being reinforced."

---

## Usage Instructions

**Coverage Map — Prompt to Exam Objective:**

| Exam Objective | Prompts | # Prompts |
|---|---|---|
| 1 — IaC with Terraform | 1 | 1 |
| 2 — Terraform Fundamentals (providers, state) | 2, 3 | 2 |
| 3 — Core Terraform Workflow | 4 | 1 |
| 4 — Terraform Configuration (HCL) | 5, 6, 7, 8, 9, 10 | 6 |
| 5 — Terraform Modules | 11 | 1 |
| 6 — State Management | 12 | 1 |
| 7 — Maintaining Infrastructure | 13 | 1 |
| 8 — HCP Terraform | 14, 15 | 2 |
| Practice | 16, 17 | 2 |

**Suggested Study Sequence:**

| Week | Focus | Prompts |
|---|---|---|
| 1 | IaC concepts, providers, state, core workflow | 1–4 |
| 2 | HCL deep dive — resources, variables, functions, types | 5–8 |
| 3 | Dependencies, lifecycle, custom conditions, sensitive data, modules | 9–11 |
| 4 | State management, import, logging, HCP Terraform | 12–15 |
| 5 | Practice exam and capstone project | 16–17 |

**How to Use Each Prompt:**
1. Feed the prompt to an AI tool to generate detailed study content
2. For all HCL examples, run them in a local Terraform installation using the `random`, `local`, or `null` providers — no cloud credentials needed
3. Use `terraform console` to interactively test functions from Prompt 8
4. After reading each topic, write a 1-paragraph summary in your own words before moving on
5. Use Prompt 16 to self-test after completing each week; filter questions by objective
6. Complete Prompt 17 capstone as your final review before the exam date

**Key Facts to Memorize:**
- `terraform init` → downloads providers/modules, initializes backend
- `terraform fmt` → formats only, `-check` for CI, `-recursive` for subdirectories
- `terraform validate` → syntax check only, does NOT call cloud APIs
- `terraform plan -refresh-only` → update state only, no infrastructure changes
- `.terraform.lock.hcl` → provider dependency lock file — commit to version control
- `~>` → pessimistic constraint operator (allows patch/minor, not major)
- Variable precedence (low → high): default → `.tfvars` → `*.auto.tfvars` → `-var-file` → `-var` → env var
- `count` → ordered, use index; `for_each` → named keys, preferred for most resources
- `sensitive = true` → masks in CLI output, still plaintext in state file
- `moved` block → refactor resource names without destroy/recreate
- HCP Terraform workspaces ≠ `terraform workspace` CLI workspaces
- `terraform apply -replace=<address>` → force resource replacement (replaces deprecated `terraform taint`)
- `terraform workspace` → CLI workspaces share config directory, separate state files; reference current workspace in config with `terraform.workspace`
- `provider` meta-argument on a resource → selects which aliased provider configuration that resource uses
- Provider tiers: Official (HashiCorp-authored) > Partner (HashiCorp-verified) > Community > Archived

---
