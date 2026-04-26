# Terraform Associate (004) — Question Bank Iter 4 Batch 3

**Iteration**: 4
**Iteration Style**: Error identification — spot the mistake in config, workflow, or claim
**Batch**: 3
**Objective**: 3 — Core Workflow & CLI
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 2 Easy / 9 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — `terraform validate` Claimed to Require Credentials

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Identifying the error in claiming `terraform validate` needs cloud provider credentials

**Question**:
A team's onboarding guide states:

> "Before running `terraform validate`, ensure your AWS credentials are configured in your environment. The command connects to the provider API to verify that referenced resources — such as AMI IDs and IAM role ARNs — actually exist."

What is the error in this guidance?

- A) There is no error — `terraform validate` requires provider credentials to verify resource attribute values
- B) The error is that `terraform validate` does not check resource references at all — only `terraform plan` reads `.tf` files
- C) The error is that `terraform validate` performs **no network calls and requires no provider credentials** — it checks only HCL syntax and static configuration consistency (undefined references, type errors, invalid arguments) without querying any cloud API
- D) The error is that AMI IDs must be validated with `terraform plan`, not `terraform validate`, because `validate` only handles provider block syntax

**Answer**: C

**Explanation**:
`terraform validate` is a fully offline command — it checks HCL syntax, verifies that all variable and resource references are declared, confirms argument names are valid for each resource type, and detects type mismatches. It makes zero API calls and requires no credentials. Verifying that a referenced AMI ID or IAM role actually exists in the cloud only occurs during `terraform plan` or `terraform apply`, when the provider plugin queries the live API.

---

### Question 2 — `-` Plan Symbol Described as Update

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Identifying the error in claiming the `-` plan symbol means an in-place update

**Question**:
A developer reads a `terraform plan` summary that includes this line:

```
  # aws_security_group.old will be removed
- resource "aws_security_group" "old" {
    - id   = "sg-0abc123def" -> null
    - name = "legacy-sg"    -> null
  }
```

The developer comments: "The `-` prefix means this security group will be updated in-place — the attributes shown in red are being modified."

What is the error in this interpretation?

- A) There is no error — the `-` prefix indicates an in-place update of the resource's attributes
- B) The error is that `-` indicates a **destroy** (deletion) operation — the security group will be permanently removed from the cloud; an in-place update is shown with the `~` prefix
- C) The error is that `-` means the resource is being moved to a different Terraform workspace
- D) The error is that `-` next to individual attribute lines always indicates a replacement, not a standalone destroy

**Answer**: B

**Explanation**:
The four `terraform plan` symbols are: `+` (create), `~` (update in-place), `-` (destroy), and `-/+` (destroy then recreate — replacement). The `-` prefix on the resource block means the resource will be **destroyed** — removed from the cloud entirely. Individual attribute lines also prefixed with `-` show the current values being removed. An in-place modification — where the resource continues to exist with changed attribute values — uses the `~` prefix on the resource block.

---

### Question 3 — `terraform fmt -check` Claimed to Reformat Files

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying the error in claiming `terraform fmt -check` writes formatting changes to disk

**Question**:
A CI pipeline script includes this step with the following comment:

```bash
# Reformat all .tf files to canonical style, then fail the build if any were changed
terraform fmt -check
```

What is the error in this script?

- A) There is no error — `terraform fmt -check` reformats files and exits with code 1 if any were changed
- B) The error is that `-check` is not a valid flag for `terraform fmt`
- C) The error is that `terraform fmt -check` is **read-only** — it checks whether files need reformatting and exits with code 1 if they do, but it does **not** write any changes to disk; to actually reformat files and then check, run `terraform fmt` first (no flags)
- D) The error is that the CI script should use `terraform fmt -diff` instead of `terraform fmt -check` to write and verify changes simultaneously

**Answer**: C

**Explanation**:
`terraform fmt -check` is a **non-destructive gate**: it scans all `.tf` files, exits with code 1 if any file is not in canonical format, and exits with code 0 if all files are already correctly formatted. It makes no changes to any file. The comment in the script is therefore misleading — `-check` does not reformat. The correct CI pattern is either: (1) run `terraform fmt` (writes changes, exits 0) and then `terraform fmt -check` to verify, or (2) simply run `terraform fmt -check` as a gate that requires developers to run `terraform fmt` locally before pushing.

---

### Question 4 — `terraform init -reconfigure` Claimed to Migrate State

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying the error in using `-reconfigure` when state migration is required

**Question**:
A team's runbook says:

> "To change from local state to an S3 remote backend, add the `backend \"s3\"` block to your configuration and run `terraform init -reconfigure`. This will reconfigure the backend and migrate your existing local state to S3 automatically."

What is the error in this runbook?

- A) There is no error — `terraform init -reconfigure` migrates state from the old backend to the new one
- B) The error is that backend changes require running `terraform state push` before `terraform init`
- C) The error is that `terraform init -reconfigure` forces Terraform to reconfigure the backend but **does not migrate existing state** — it may discard the current state location and start fresh; to copy existing state to the new backend, `terraform init -migrate-state` is the correct flag
- D) The error is that state migration from local to S3 requires running `terraform apply -migrate-state`, not an `init` flag

**Answer**: C

**Explanation**:
`terraform init -reconfigure` ignores any existing backend state and reconfigures the working directory to use the new backend from scratch — it does not migrate existing state. `terraform init -migrate-state` is the correct flag when switching backends: it detects the existing local state and prompts to copy it to the new backend (S3) before completing initialization. Using `-reconfigure` in this scenario risks leaving the existing state behind in the local file while the working directory points to an empty remote backend.

---

### Question 5 — `terraform plan -target` Claims About Dependencies

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Identifying TWO errors in claims about how `-target` handles dependencies

**Question**:
A wiki article makes four claims about `terraform plan -target`. Which TWO contain errors? (Select two.)

- A) "Using `-target` emits a warning that the resulting plan may be incomplete and is not a reliable representation of the full configuration"
- B) "`terraform plan -target=aws_instance.web` plans only `aws_instance.web` exactly — its upstream dependencies (such as a security group or subnet it references) are excluded from the plan entirely"
- C) "`terraform plan -target` is the recommended approach for routine production applies because it limits the blast radius to only the intended resource"
- D) "The targeted resource and any resources it transitively depends on are included in the plan"

**Answer**: B, C

**Explanation**:
Option B is incorrect: `-target` includes the targeted resource **and all of its dependencies** — resources that the target depends on are also planned because Terraform must resolve the full dependency chain to safely plan the targeted resource. Excluding dependencies would produce an invalid plan. Option C is incorrect: HashiCorp explicitly discourages routine use of `-target` in production. It is intended for exceptional circumstances (e.g., breaking a circular dependency or recovering from a partial failure), not standard workflows. Using it routinely can cause the state to diverge from the intended configuration. Options A and D are both correct descriptions.

---

### Question 6 — `terraform graph` Output Described as JSON

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying the error in claiming `terraform graph` produces JSON output

**Question**:
A developer writes this shell script step in their documentation:

```bash
# Generate a dependency graph in JSON format and pipe it to jq for processing
terraform graph | jq '.'
```

What is the error in this approach?

- A) There is no error — `terraform graph` outputs JSON and can be piped directly to `jq`
- B) The error is that `terraform graph` requires the `-json` flag to produce machine-readable output, just like `terraform show`
- C) The error is that `terraform graph` outputs **DOT format** — a plain-text graph description language used by Graphviz — not JSON; piping it to `jq` will fail because DOT text is not valid JSON
- D) The error is that `terraform graph` outputs XML and must be converted to JSON before processing

**Answer**: C

**Explanation**:
`terraform graph` produces output in **DOT language** — a plain-text format understood by Graphviz for rendering directed graphs. DOT output looks like `digraph { ... }` with node and edge declarations, which is structurally incompatible with JSON. Piping it to `jq` will produce a parse error. The standard usage is `terraform graph | dot -Tsvg > graph.svg` to render the dependency graph as an SVG image using the Graphviz `dot` tool.

---

### Question 7 — `terraform destroy` Claimed to Delete the State File

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying the error in claiming `terraform destroy` removes the state file after execution

**Question**:
An engineer documents a cleanup procedure:

> "After running `terraform destroy` to remove all cloud resources, Terraform also deletes the `terraform.tfstate` file since there is nothing left to track — this keeps the working directory clean."

What is the error in this documentation?

- A) There is no error — `terraform destroy` removes all resources and then deletes `terraform.tfstate`
- B) The error is that `terraform destroy` deletes `terraform.tfstate.backup` but not `terraform.tfstate`
- C) The error is that `terraform destroy` **does not delete the state file** — after all resources are destroyed, `terraform.tfstate` remains on disk but contains an empty resources list; the state file records that no resources are currently managed
- D) The error is that `terraform destroy` always leaves one backup copy of `terraform.tfstate` per resource destroyed

**Answer**: C

**Explanation**:
`terraform destroy` updates the state file to reflect that all managed resources have been removed — it does not delete `terraform.tfstate`. After a successful destroy, the state file exists on disk but contains an empty (or near-empty) resources array, indicating that no infrastructure is currently tracked. This behaviour is intentional: preserving the state file means the backend configuration and workspace context remain intact for future use, even if no resources are currently managed.

---

### Question 8 — `terraform apply` Without Saved Plan Claimed to Guarantee Reviewed Changes

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying the error in claiming `terraform apply` without a plan file applies exactly what `terraform plan` showed

**Question**:
A team's deployment procedure states:

> "Step 1: Run `terraform plan` and review the output carefully. Step 2: Run `terraform apply`. This guarantees that exactly the changes shown in Step 1 will be applied — no more, no fewer."

What is the error in this procedure?

- A) There is no error — `terraform apply` always applies exactly the changes shown by the most recent `terraform plan`
- B) The error is that `terraform apply` must be run before `terraform plan` in the correct workflow order
- C) The error is that `terraform apply` without a saved plan file **re-runs the plan internally** at apply time — if infrastructure has changed between the plan and apply steps, the applied changes may differ from what was reviewed; to guarantee the exact plan is applied, use `terraform plan -out=file` followed by `terraform apply file`
- D) The error is that `terraform plan` output is only valid for 60 seconds — after that, `terraform apply` automatically refreshes and re-plans

**Answer**: C

**Explanation**:
`terraform apply` without a saved plan file performs an **implicit plan** at apply time, right before requesting confirmation. If any infrastructure changed between the explicit `terraform plan` in Step 1 and the apply in Step 2 — due to a concurrent change, a manual modification, or a resource timing issue — the implicit plan will reflect the updated state, potentially differing from what was reviewed. The only way to guarantee that the exact reviewed plan is applied is to save the plan with `terraform plan -out=release.tfplan` and apply it with `terraform apply release.tfplan`.

---

### Question 9 — `terraform fmt` Claimed to Recurse by Default

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying the error in claiming `terraform fmt` includes subdirectories without a flag

**Question**:
A documentation page states:

> "`terraform fmt` automatically formats all `.tf` files in the current directory **and all subdirectories** recursively. No additional flags are needed for a multi-module repository where modules live in subdirectories."

What is the error in this statement?

- A) There is no error — `terraform fmt` recurses into subdirectories by default
- B) The error is that `terraform fmt` can only format a single file at a time — the directory scope requires a separate tool
- C) The error is that `terraform fmt` **only processes `.tf` files in the current directory by default** — it does not recurse into subdirectories unless the `-recursive` flag is explicitly provided; for a multi-module repository, `terraform fmt -recursive` is required to format all modules
- D) The error is that `terraform fmt` does not support subdirectories at all, even with `-recursive`

**Answer**: C

**Explanation**:
`terraform fmt` without flags scans and formats `.tf` files in the **current working directory only**. Subdirectories are ignored unless the `-recursive` flag is provided. In a typical Terraform repository where modules reside in subdirectories (e.g., `modules/networking/`, `modules/compute/`), running `terraform fmt` from the root would leave module files unformatted. The correct command to format an entire repository is `terraform fmt -recursive` run from the repository root.

---

### Question 10 — Wrong Conditions Claimed to Require `terraform init` Re-run

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Identifying TWO conditions incorrectly listed as requiring `terraform init` to be re-run

**Question**:
A team's wiki lists five actions that it claims require re-running `terraform init`. Which TWO are **incorrectly** listed as requiring a re-run? (Select two.)

- A) Adding a new provider to `required_providers` in `main.tf`
- B) Adding a new `output` block to an existing module that the root configuration already sources
- C) Changing the `backend` block from local to a remote S3 backend
- D) Updating the `default` value of an existing `variable` block in `variables.tf`

**Answer**: B, D

**Explanation**:
`terraform init` must be re-run when: a new provider plugin needs to be downloaded (A), the backend configuration changes (C), or a new module source is added. **Adding an `output` block** (B) and **changing a variable's `default` value** (D) are purely configuration changes — they do not affect provider plugins, backend configuration, or module sources. No plugin download or backend reconfiguration is required, so `terraform init` does not need to be re-run for either of these changes.

---

### Question 11 — `terraform console` Claimed to Modify State

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying the error in claiming `terraform console` writes to the state file when expressions are evaluated

**Question**:
An engineer warns their team:

> "Be careful when using `terraform console` interactively — if you evaluate an expression that references a managed resource, Terraform may update that resource's state entry as a side effect. Always use a read-only state backend when running `terraform console` in production."

What is the error in this warning?

- A) There is no error — `terraform console` can trigger state updates when resource attributes are evaluated
- B) The error is that `terraform console` requires a writable state backend to function correctly
- C) The error is that `terraform console` is a **read-only REPL** — it evaluates HCL expressions and functions against the current configuration and state without modifying state, without making any provider API calls, and without triggering any resource changes; it is completely safe to run in any environment
- D) The error is that `terraform console` only reads from local state files — it cannot evaluate expressions referencing resources in a remote backend

**Answer**: C

**Explanation**:
`terraform console` is a purely read-only interactive session. It evaluates HCL expressions and built-in functions (e.g., `length()`, `format()`, `toset()`) against the current configuration and state, but it never writes to the state file or makes any provider API calls. No resource changes occur regardless of what expressions are evaluated. The warning is factually incorrect and creates unnecessary fear about using a safe and useful debugging tool. `terraform console` can safely be used in any environment, including those connected to production remote backends.

---

### Question 12 — `terraform plan` Run Before `terraform init`

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Identifying the error in a workflow that runs `terraform plan` before `terraform init`

**Question**:
A new developer proposes this workflow for reviewing changes:

> "To quickly preview what Terraform would do without committing to downloading providers, run `terraform plan` first. Terraform will automatically infer the required providers from the configuration and generate a preview plan. Once you're satisfied, run `terraform init` to download the providers, then `terraform apply` to execute."

What is the error in this proposed workflow?

- A) There is no error — `terraform plan` can generate a preview plan before providers are downloaded
- B) The error is only cosmetic — `terraform plan` before `terraform init` is valid but produces a warning that providers are not yet downloaded
- C) The error is that `terraform plan` requires a **fully initialised working directory** — providers must already be downloaded and the backend must be configured before `plan` can execute; running `plan` before `init` returns an error such as "This configuration has not been initialized" and no plan is generated
- D) The error is only relevant for remote backends — for local state, `terraform plan` can run before `terraform init`

**Answer**: C

**Explanation**:
`terraform init` is a prerequisite for every other Terraform workflow command. It downloads provider plugins to `.terraform/`, configures the backend, and caches module sources. `terraform plan` cannot execute without provider binaries present — it needs the provider plugins to determine resource schemas, validate configuration, and compute the diff between desired and actual state. Running `plan` before `init` fails with an error indicating the directory is not initialized. The correct workflow is always: `init` → `fmt` → `validate` → `plan` → `apply`.

---

### Question 13 — Wrong Claims About `terraform apply -replace` and `terraform taint`

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Identifying TWO errors in claims comparing `terraform apply -replace` with `terraform taint`

**Question**:
A blog post makes four claims about `terraform apply -replace` and `terraform taint`. Which TWO contain errors? (Select two.)

- A) "`terraform taint` was deprecated in favour of `terraform apply -replace`, which is the recommended approach for forcing resource replacement in Terraform 1.x"
- B) "`terraform apply -replace` requires a configuration change to the targeted resource — if the configuration is unchanged, the command has no effect and Terraform skips the replacement"
- C) "Running `terraform apply -replace=<address>` causes the plan output to show a `-/+` symbol for the targeted resource, indicating destroy-then-recreate"
- D) "`terraform apply -replace` can only be used in conjunction with `terraform plan -out` — the saved plan file is required to invoke the replace behaviour"

**Answer**: B, D

**Explanation**:
Option B is incorrect: `terraform apply -replace` forces replacement **regardless of whether the configuration has changed**. Its entire purpose is to trigger a destroy-then-recreate cycle even when the HCL definition is identical to the last applied state — for example, to recover a corrupted or unhealthy resource. Option D is incorrect: `-replace` does not require a saved plan file; it is an inline flag passed directly to `terraform apply` (or `terraform plan`) and works without any prior `-out` step. Options A and C are both correct: `terraform taint` is deprecated and `-replace` is the modern equivalent that produces a `-/+` plan symbol.

---
