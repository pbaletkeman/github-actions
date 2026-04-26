# Terraform Associate (004) — Question Bank Iter 4 Batch 7

**Iteration**: 4
**Iteration Style**: Error identification — spot the mistake in a claim, config, or workflow description
**Batch**: 7
**Objective**: 5 — Modules + 6 — State Backends
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 2 Easy / 9 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — Flawed Claim: `version` Argument Valid with GitHub Module Source

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Which module source types support the `version` argument

**Question**:
A developer writes:

```hcl
module "vpc" {
  source  = "github.com/acme/terraform-modules//modules/vpc"
  version = "~> 3.0"
  cidr    = "10.0.0.0/16"
}
```

and claims: "The `version = \"~> 3.0\"` constraint works exactly the same as it does for Terraform Registry modules — it restricts which tagged release `terraform init` downloads from GitHub."

What is wrong with this claim?

- A) Nothing — the `version` argument works with GitHub module sources using semantic versioning
- B) The claim is wrong: the `version` argument is only valid for Terraform Registry and private registry sources; using it with a GitHub URL causes a `terraform init` error — the correct way to pin a Git source is the `?ref=` query parameter (e.g., `?ref=v3.0.0`)
- C) The claim is wrong: `version` works with GitHub sources but uses the GitHub release API, not semantic version constraints
- D) The claim is wrong: the `version` argument is only valid when `source` begins with `git::https://` — bare GitHub URLs do not support versioning

**Answer**: B

**Explanation**:
The `version` argument in a `module` block is exclusively supported for **Terraform Registry** and **private registry** sources. For any Git-based source — including bare GitHub URLs, `git::https://`, and `git::ssh://` — the `version` argument is invalid and causes `terraform init` to raise an error. To pin a Git-based module to a specific version, use the `?ref=` query parameter with a tag, branch, or commit SHA: `"github.com/acme/terraform-modules//modules/vpc?ref=v3.0.0"`.

---

### Question 2 — Flawed HCL: Module Output Referenced with Wrong Syntax

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Correct syntax for referencing a child module's output in the root module

**Question**:
Read this root module configuration:

```hcl
module "networking" {
  source = "./modules/networking"
}

resource "aws_instance" "web" {
  ami       = "ami-0abc123"
  subnet_id = output.networking.public_subnet_id
}
```

What is wrong with this configuration?

- A) Nothing — `output.networking.public_subnet_id` is the correct syntax to reference a child module output
- B) The configuration is wrong: child module outputs are referenced as `module.<name>.<output_name>` — the correct reference here is `module.networking.public_subnet_id`; there is no `output.<module_name>` syntax
- C) The configuration is wrong: child module outputs cannot be used directly in resource arguments — they must first be assigned to a local value
- D) The configuration is wrong: the `subnet_id` argument requires an `aws_subnet` resource reference, not a module output

**Answer**: B

**Explanation**:
Child module outputs are accessed using the expression `module.<module_label>.<output_name>`. The label `"networking"` comes from the `module "networking"` block declaration, making the correct reference `module.networking.public_subnet_id`. The syntax `output.networking.public_subnet_id` does not exist and causes a Terraform error about an invalid reference. Module outputs can be used directly in any resource argument, local, or root output — no intermediate local assignment is needed.

---

### Question 3 — Flawed Claim: `.terraform/modules/` Should Be Committed to Git

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Whether the `.terraform/modules/` directory should be committed to version control

**Question**:
A new team member reads their company's Terraform onboarding guide, which states:

> "Commit the `.terraform/modules/` directory to version control. This eliminates the need for teammates to run `terraform init` when checking out the repository — the module cache is ready to use immediately."

What is wrong with this guidance?

- A) Nothing — committing `.terraform/modules/` is the recommended practice for improving team checkout speed
- B) The guidance is wrong: `.terraform/modules/` is a local cache directory that should be **excluded from version control** (via `.gitignore`); each developer and CI pipeline runs `terraform init` to populate it from the declared module sources — committing it creates unnecessary bloat and can cause version inconsistencies
- C) The guidance is wrong: `.terraform/modules/` is encrypted with a workspace-specific key and cannot be shared across machines even if committed
- D) The guidance is wrong: committing `.terraform/modules/` is not supported by GitHub because module caches exceed the 100 MB file size limit

**Answer**: B

**Explanation**:
`.terraform/modules/` is a **local cache directory** generated by `terraform init`. It is analogous to `node_modules/` in Node.js projects — each developer populates it by running `terraform init`, which downloads module sources according to the declared `source` and `version` constraints. Committing this directory adds unnecessary repository bloat, can commit platform-specific binaries or paths, and can cause teammates to use stale or inconsistent module code if the cache is not regenerated after source changes. The standard practice is to add `.terraform/` to `.gitignore` and always run `terraform init` after checkout.

---

### Question 4 — Flawed Claim: `terraform init` Is a One-Time Step

**Difficulty**: Medium
**Answer Type**: one
**Topic**: When `terraform init` must be re-run within an existing workspace

**Question**:
An experienced developer advises a teammate:

> "You only need to run `terraform init` once per workspace — after the initial setup, Terraform discovers module changes automatically during `terraform plan` and downloads any new modules it finds."

What is wrong with this advice?

- A) Nothing — `terraform init` is only required once per workspace; `terraform plan` handles module discovery automatically
- B) The advice is wrong: `terraform init` must be re-run whenever a new `module` block is added, an existing module's `source` is changed, or a module version is updated — `terraform plan` does not automatically install missing or updated modules and will raise an error if a new module has not been initialised
- C) The advice is wrong: `terraform init` must be re-run on every single `terraform plan` execution to ensure providers and modules are up to date
- D) The advice is wrong: modules are not downloaded by `terraform init` at all — they are resolved dynamically during `terraform plan`

**Answer**: B

**Explanation**:
`terraform init` must be re-run any time a module source is added, removed, or changed in the configuration. It is responsible for downloading and caching module source code into `.terraform/modules/`. If a new `module` block is added and `terraform init` has not been re-run, `terraform plan` raises an error: "Module not installed — run `terraform init`." The command does not need to be re-run on every single plan (option C is incorrect), but it is not a one-time-only step either. Re-running `init` is always safe — it only refreshes what has changed.

---

### Question 5 — Flawed HCL: DynamoDB Lock Table with Wrong `hash_key` Attribute Name

**Difficulty**: Medium
**Answer Type**: one
**Topic**: The required partition key attribute name for a DynamoDB table used for S3 backend state locking

**Question**:
A team provisions a DynamoDB table for Terraform state locking:

```hcl
resource "aws_dynamodb_table" "tf_lock" {
  name         = "terraform-state-locks"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "StateFileLock"

  attribute {
    name = "StateFileLock"
    type = "S"
  }
}

terraform {
  backend "s3" {
    bucket         = "company-tfstate"
    key            = "prod/terraform.tfstate"
    region         = "us-east-1"
    dynamodb_table = "terraform-state-locks"
    encrypt        = true
  }
}
```

What is wrong with this configuration?

- A) Nothing — the `hash_key` attribute can be any valid string name; `"StateFileLock"` is acceptable
- B) The configuration is wrong: the DynamoDB table's `hash_key` must be named exactly `"LockID"` — the Terraform S3 backend hardcodes this attribute name when writing and reading lock records; a different name causes locking to fail silently or raise errors
- C) The configuration is wrong: the DynamoDB table must use `"terraform.lock"` as the `hash_key` to match the format expected by the S3 backend
- D) The configuration is wrong: the `hash_key` type must be `"N"` (Number), not `"S"` (String), for the S3 backend locking to function correctly

**Answer**: B

**Explanation**:
The Terraform S3 backend writes lock acquisition entries to DynamoDB using a hardcoded partition key attribute named **`"LockID"`**. This is a fixed Terraform implementation requirement — the attribute name is not configurable. DynamoDB itself accepts any attribute name, so the table will be created successfully with `hash_key = "StateFileLock"`, but Terraform will fail when attempting to acquire or release a lock because it looks for an item with the `"LockID"` attribute. Every S3 backend locking example uses exactly `hash_key = "LockID"` with `type = "S"` for this reason.

---

### Question 6 — Flawed Claim: `terraform state rm` Deletes the Cloud Resource

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What `terraform state rm` does and does not do to the cloud resource

**Question**:
A developer on the team explains `terraform state rm`:

> "Use `terraform state rm aws_s3_bucket.logs` when you want to decommission a managed resource — it removes the bucket from state and also deletes it from AWS in a single atomic operation, which is safer than running `terraform destroy -target` because it doesn't risk cascading deletions."

What is wrong with this explanation?

- A) Nothing — `terraform state rm` removes the resource from both Terraform state and AWS in one step
- B) The explanation is wrong: `terraform state rm` removes the resource **only from the state file** — the actual AWS resource (the S3 bucket) is not touched in any way and continues to exist in AWS; Terraform simply stops managing it
- C) The explanation is wrong: `terraform state rm` is not valid for S3 resources — it can only be used with compute resources
- D) The explanation is wrong: `terraform state rm` sends a delete request to AWS but waits for manual confirmation before updating the state file

**Answer**: B

**Explanation**:
`terraform state rm` is a **state file manipulation command** — it removes the specified resource record from `terraform.tfstate` without making any API calls to the cloud provider. The AWS S3 bucket remains fully intact and operational. After the command runs, Terraform no longer manages the resource: it is "forgotten" from Terraform's perspective. On the next `terraform plan`, if the resource block still exists in configuration, Terraform will plan to **create** it (not destroy it), because it is no longer in state. The command is used to stop Terraform management, not to delete infrastructure.

---

### Question 7 — Flawed Claim: `terraform plan -refresh-only` Updates the State File

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Whether `terraform plan -refresh-only` writes any changes to the state file

**Question**:
An operator explains drift remediation to a new team member:

> "When someone manually modifies a resource in the AWS Console, running `terraform plan -refresh-only` is the correct command to safely sync the state file with the current cloud reality — it reads the live resource attributes and updates `terraform.tfstate` to match, without making any infrastructure changes."

What is wrong with this explanation?

- A) Nothing — `terraform plan -refresh-only` reads live cloud resources and updates the state file
- B) The explanation is wrong: `terraform plan -refresh-only` only **shows** the drift between the state file and the current cloud state — it does not write anything to the state file; to actually update the state, the operator must run `terraform apply -refresh-only`
- C) The explanation is wrong: `terraform plan -refresh-only` re-applies all infrastructure to match the configuration — it is not a safe read-only command
- D) The explanation is wrong: only `terraform refresh` (not `plan -refresh-only`) updates the state file

**Answer**: B

**Explanation**:
`terraform plan -refresh-only` is a **read-only drift report** — it queries live cloud resources, computes the difference from the current state, and displays what would change, but writes nothing to the state file. It is the drift-detection equivalent of `terraform plan` (which also never writes state). To actually update the state file to reflect cloud reality, the operator must run `terraform apply -refresh-only` and confirm the proposed state changes. The two commands are deliberately separated: `plan` always shows; `apply` always writes. Note: `terraform refresh` is deprecated and `apply -refresh-only` is its replacement.

---

### Question 8 — Flawed Claim: Every Child Module Requires Its Own `provider` Block

**Difficulty**: Medium
**Answer Type**: one
**Topic**: How provider configuration flows to child modules in Terraform

**Question**:
A team's Terraform style guide states:

> "Every child module that creates AWS resources must include its own `provider "aws" {}` block declaring the region and profile. Provider configuration is scoped to each module and is not automatically available to child modules from the root."

What is wrong with this style guide rule?

- A) Nothing — every child module must redeclare its provider configuration independently
- B) The rule is wrong: child modules **automatically inherit** the root module's default provider configuration — they do not need their own `provider "aws" {}` blocks; adding redundant provider blocks in child modules can cause "conflicting configurations" errors and is not a recommended practice
- C) The rule is wrong: child modules cannot declare `provider` blocks at all — only the root module is allowed to configure providers
- D) The rule is wrong: provider configuration is only required in leaf modules (modules with no children); intermediate modules inherit automatically but leaf modules do not

**Answer**: B

**Explanation**:
Terraform propagates the default (non-aliased) provider configuration from the root module to all child modules automatically. A child module that creates `aws_*` resources does not need its own `provider "aws" {}` block — it uses the configuration inherited from the root. The `providers` argument in a `module` block is only needed when using **aliased providers** (e.g., deploying the same module to multiple regions) or when the root module needs to explicitly override which provider configuration a child module receives. Redundant provider declarations in child modules can cause Terraform errors about conflicting configurations.

---

### Question 9 — Flawed HCL: `version` Constraint on Local Path Module Source

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying that `version` is invalid alongside a local path module source

**Question**:
An engineer pins a locally developed module with:

```hcl
module "compute" {
  source  = "./modules/compute"
  version = "~> 1.5"
  ami     = "ami-0abc123"
}
```

and says: "The `version = \"~> 1.5\"` constraint ensures we don't accidentally pick up breaking changes if a teammate bumps the module version."

What is wrong with this configuration?

- A) Nothing — `version` constraints are valid for local path module sources and work as expected
- B) The configuration is wrong: the `version` argument is not valid for local path sources — it causes `terraform init` to raise an error; local modules have no version registry to consult, and code changes are controlled via the file system (or, for git repos, via `?ref=` on the source URL)
- C) The configuration is wrong: local path modules require `version = "0.0.0"` as a placeholder — any other version string causes an error
- D) The configuration is wrong: the `~>` operator is only valid for provider version constraints, not for module version constraints

**Answer**: B

**Explanation**:
The `version` argument in a `module` block is only valid for modules sourced from the **Terraform Registry** or a private registry — sources that have an associated version registry to query. For local path sources (those beginning with `./` or `../`), the `version` argument is invalid and causes `terraform init` to fail with an error. Local module code is pinned by the state of the file system at the time of use. For Git-based sources, versioning is handled with `?ref=v1.5.0` in the URL. There is no mechanism to apply semantic version constraints to local paths.

---

### Question 10 — Two Errors in `for_each` Module Block

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Errors when using `count.index` in a `for_each` module and accessing instances with a numeric index

**Question**:
An engineer writes this configuration to deploy three server instances:

```hcl
module "server" {
  for_each     = toset(["web", "api", "worker"])
  source       = "./modules/server"
  service_name = count.index
  instance_type = "t3.micro"
}

output "web_instance_id" {
  value = module.server[0].instance_id
}
```

Which TWO statements correctly identify the errors? (Select two.)

- A) `count.index` is not available inside a `for_each` module block — it is only valid when the module uses `count`; the correct reference to the current element's key is `each.key`
- B) `toset()` is not a valid collection type for `for_each` on a module block — only maps are supported
- C) `module.server[0]` uses a numeric index, but `for_each` with a set of strings addresses instances using their string keys — the correct reference is `module.server["web"]`
- D) `for_each` cannot be used on a `module` block — it is only valid for `resource` blocks

**Answer**: A, C

**Explanation**:
Two distinct errors exist. First, `count.index` is only available inside blocks that use `count` — it is undefined when `for_each` is used instead. The correct reference to the current iteration's key in a `for_each` block is `each.key` (for the key) or `each.value` (for the value). Here, `service_name = each.key` would resolve to `"web"`, `"api"`, or `"worker"` for each instance. Second, `for_each` on a set of strings addresses each module instance using its string key in bracket notation: `module.server["web"]`, `module.server["api"]`, `module.server["worker"]`. Numeric indexing (`module.server[0]`) is only valid when `count` is used. Both `toset()` and `for_each` on module blocks are valid (options B and D are incorrect).

---

### Question 11 — Two Flawed Claims About `terraform.tfstate.backup`

**Difficulty**: Medium
**Answer Type**: many
**Topic**: The scope and content of `terraform.tfstate.backup` and when it is created

**Question**:
A developer documents local backend behaviour with two claims:

> **Claim 1**: "`terraform.tfstate.backup` maintains a rolling five-version history — each `terraform apply` appends a new snapshot, and the oldest is automatically pruned once the history exceeds five entries."
> **Claim 2**: "When using a remote S3 backend, `terraform.tfstate.backup` is automatically created in the working directory after every `terraform apply` as a local safety copy of the updated remote state."

Which TWO statements correctly identify the errors? (Select two.)

- A) Claim 1 is **wrong**: `terraform.tfstate.backup` is a **single file containing only one snapshot** — the state immediately before the most recent apply; it is completely overwritten on each apply with no history retention
- B) Claim 1 is **correct**: a rolling five-version history is maintained in `terraform.tfstate.backup`
- C) Claim 2 is **wrong**: `terraform.tfstate.backup` is only created by the **local backend**; when using a remote backend (such as S3), no local backup file is written to the working directory during apply
- D) Claim 2 is **correct**: remote backends always write a local `terraform.tfstate.backup` for disaster recovery purposes

**Answer**: A, C

**Explanation**:
`terraform.tfstate.backup` is a **single-file, single-snapshot** backup created only by the local backend. It is overwritten on each apply and holds no history beyond the immediately preceding state (Claim 1 is wrong — there is no rolling history). When using a remote backend such as S3, Terraform reads and writes state to the remote backend only — no local `terraform.tfstate.backup` file is created on apply (Claim 2 is wrong). Teams using remote backends rely on backend-native versioning (e.g., S3 object versioning) for state history, not local backup files.

---

### Question 12 — Flawed Claim: `terraform state push` Prompts Before Overwriting

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Whether `terraform state push` provides safety prompts before overwriting remote state

**Question**:
A platform engineer documents the state restoration workflow:

> "If a state file becomes corrupt, `terraform state push backup.tfstate` is a safe recovery tool — before overwriting the remote state, it displays a diff of what will change and requires the operator to type 'yes' to confirm, preventing accidental overwrites in shared environments."

What is wrong with this documentation?

- A) Nothing — `terraform state push` always prompts for confirmation and shows a diff before overwriting remote state
- B) The documentation is wrong: `terraform state push` **does not prompt for confirmation** and does not display a diff — it immediately overwrites the remote state file with the contents of the provided local file; it is a dangerous command that can silently destroy the remote state without any safety checks, and should only be used as a last resort by operators who have already verified the file to push is correct
- C) The documentation is wrong: `terraform state push` only works for the local backend — it cannot overwrite a remote S3 backend state
- D) The documentation is wrong: `terraform state push` requires the `-force` flag to be added before it will overwrite an existing remote state; without `-force` it is read-only

**Answer**: B

**Explanation**:
`terraform state push` is a low-level, **non-interactive command** — it writes the provided file directly to the remote backend without displaying a diff, requesting confirmation, or performing any safety validation. A simple `terraform state push stale-backup.tfstate` can immediately overwrite a production state file, removing all resource tracking for changes that occurred since the backup was created. Terraform does include a serial number check (it will warn if the remote serial is higher than the local serial), but it can be bypassed with `-force`. This is why `terraform state push` is documented as a dangerous escape hatch, not a routine recovery tool. State restoration should be done through backend-native mechanisms (S3 versioning, HCP Terraform history) whenever possible.

---

### Question 13 — Two Errors About `terraform state mv` and `moved` Blocks

**Difficulty**: Hard
**Answer Type**: many
**Topic**: What `terraform state mv` does and does not modify, and what `moved` blocks can address

**Question**:
A team's runbook contains two claims about renaming resources:

> **Claim 1**: "After running `terraform state mv aws_instance.old aws_instance.new`, Terraform automatically adds a `moved` block to `main.tf` to track the rename for teammates who apply from a fresh checkout — no manual HCL update is required."
> **Claim 2**: "`moved` blocks can only be used to rename resources at the root module level — they cannot move a resource into a module, rename a module call, or address resources inside a child module from the root."

Which TWO statements correctly identify the errors? (Select two.)

- A) Claim 1 is **wrong**: `terraform state mv` **only modifies the state file** — it never writes to any HCL files; teammates applying from a fresh state or importing the project will encounter a mismatch unless the engineer also manually updates the resource label in `main.tf` and (optionally) adds a `moved` block for history
- B) Claim 1 is **correct**: `terraform state mv` automatically adds a `moved` block to `main.tf`
- C) Claim 2 is **wrong**: `moved` blocks support a wide range of restructuring scenarios including moving a root-level resource into a module (`from = aws_instance.web` → `to = module.compute.aws_instance.web`), renaming a module call, and addressing resources within nested modules — they are not limited to root-level resource renames
- D) Claim 2 is **correct**: `moved` blocks only support root-level resource renames

**Answer**: A, C

**Explanation**:
`terraform state mv` is a **state file manipulation command** — it renames or relocates a resource's address within `terraform.tfstate` only; it does not touch any `.tf` configuration files (Claim 1 is wrong). The engineer must separately update the HCL resource label to match, or the next plan will propose destroying the old name and creating the new one. `moved` blocks (introduced in Terraform 1.1) are significantly more capable than Claim 2 suggests: they support renaming resources at any module level, moving resources from root into a module, moving resources between modules, and renaming module calls themselves (Claim 2 is wrong). `moved` blocks are the preferred declarative approach because they record the change in source control, helping teammates who apply from a fresh state.

---
