# Terraform Associate (004) — Question Bank Iter 5 Batch 3

**Iteration**: 5
**Iteration Style**: Comparison and contrast — distinguish between two similar concepts
**Batch**: 3
**Objective**: 3 — Core Workflow & CLI
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 3 Easy / 8 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — `terraform fmt` vs `terraform validate`: What Each Checks

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contrast between what `fmt` enforces and what `validate` enforces, and what they share in common

**Question**:
Compare `terraform fmt` and `terraform validate`. What does each command check, and what do they have in common?

- A) `terraform fmt` checks for undeclared variables; `terraform validate` checks for inconsistent indentation — both require cloud provider credentials
- B) `terraform fmt` corrects HCL code style and formatting (indentation, alignment, whitespace); `terraform validate` checks syntax correctness, undeclared references, type errors, and invalid argument names — both are fully offline and require no provider credentials or network access
- C) `terraform fmt` validates that resource arguments match the provider schema; `terraform validate` formats the output of `terraform plan` for readability
- D) Both commands are identical in what they check — `validate` is simply the strict mode of `fmt`

**Answer**: B

**Explanation**:
`terraform fmt` is concerned exclusively with **code style** — it rewrites `.tf` files (or reports differences) to match HashiCorp's canonical formatting conventions: consistent indentation, aligned `=` signs, and normalised whitespace. `terraform validate` is concerned with **logical correctness** — it catches undeclared variable references, invalid resource argument names, type mismatches, and other configuration errors that formatting cannot detect. Crucially, both commands are entirely offline: neither makes any API calls or requires cloud provider credentials, making them safe to run in any environment.

---

### Question 2 — `terraform plan` vs `terraform apply`: State File Impact

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contrast between which command reads state without modifying it and which modifies state

**Question**:
Compare `terraform plan` and `terraform apply` with respect to how each interacts with the Terraform state file. Which statement correctly describes the difference?

- A) `terraform plan` reads and writes the state file; `terraform apply` writes a new state file and archives the old one
- B) `terraform plan` generates and displays an execution plan without modifying the state file; `terraform apply` executes the plan and updates the state file to reflect the changes made
- C) Both commands modify the state file — `plan` updates it with the proposed changes and `apply` confirms them
- D) Neither command modifies the state file — state is only changed by `terraform state mv` and `terraform state rm`

**Answer**: B

**Explanation**:
`terraform plan` is a **read-only** command with respect to state — it refreshes the current state by querying the provider API, computes the diff between desired and current state, and outputs the proposed changes, but it does not write any changes to the state file. `terraform apply` **writes** to the state file: as each resource is created, updated, or destroyed, the state is updated to record the new real-world attributes. This distinction is important — `plan` is always safe to run repeatedly; `apply` is the operation that causes real changes.

---

### Question 3 — `terraform workspace new` vs `terraform workspace select`

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contrast between creating a new workspace and selecting an existing one

**Question**:
Compare `terraform workspace new staging` and `terraform workspace select staging`. What is the key functional difference between these two commands?

- A) Both commands are identical — `new` and `select` are aliases for the same operation
- B) `terraform workspace new staging` creates a new workspace named `staging` (it fails if `staging` already exists) and switches to it; `terraform workspace select staging` switches to the existing workspace named `staging` (it fails if `staging` does not yet exist)
- C) `terraform workspace new staging` copies the current workspace's state to `staging`; `terraform workspace select staging` creates an empty new workspace
- D) `terraform workspace new staging` creates the workspace but does not switch to it; `terraform workspace select staging` switches to it without creating it

**Answer**: B

**Explanation**:
`terraform workspace new <name>` creates a brand-new, empty workspace with the given name — if a workspace with that name already exists, the command returns an error. It also automatically switches to the newly created workspace. `terraform workspace select <name>` switches the current context to an already-existing workspace — if no workspace with that name exists, the command returns an error. The two commands are complementary: `new` is for first-time creation; `select` is for switching between existing workspaces.

---

### Question 4 — `terraform apply` with No Saved Plan vs `terraform apply plan.tfplan`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between implicit re-planning and deterministic saved-plan execution

**Question**:
An engineer reviews the output of `terraform plan` showing 3 resources to add. They then wait 10 minutes before applying. Compare the two approaches below — what is the key difference in what actually gets applied?

- **Approach A**: Run `terraform apply` (no saved plan) 10 minutes after reviewing the plan
- **Approach B**: Run `terraform plan -out=release.tfplan`, review the output, then run `terraform apply release.tfplan`

- A) Both approaches apply the same changes — Terraform re-uses the plan output from the previous run regardless
- B) In Approach A, Terraform performs a **new implicit plan** at apply time — if the infrastructure changed in the 10-minute gap, the actual changes may differ from what was reviewed; in Approach B, the exact plan that was reviewed is applied with no re-planning, guaranteeing deterministic execution
- C) Approach A is safer because Terraform re-plans to incorporate any recent changes; Approach B may apply a stale plan that is no longer valid
- D) Both approaches result in an interactive prompt — the only difference is file I/O overhead

**Answer**: B

**Explanation**:
When `terraform apply` is run without a saved plan file, Terraform internally executes a fresh plan at that moment — this ensures it captures the most current state, but it also means the changes applied could differ from what was reviewed if the environment changed in between. When `terraform plan -out=release.tfplan` saves the plan and `terraform apply release.tfplan` applies it, Terraform executes **exactly** the operations captured in the plan file, with no re-evaluation. This is the production best practice: plan, review, then apply the exact saved plan to guarantee what was approved is what runs.

---

### Question 5 — `terraform init` vs `terraform init -reconfigure`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between standard backend init and forced backend reconfiguration

**Question**:
A team changes the backend configuration in their `terraform` block from one S3 bucket to a different S3 bucket. They run `terraform init`. Terraform prompts them to choose between migrating state or continuing without migrating. Compare this behaviour to running `terraform init -reconfigure`. What is the operational difference?

- A) `terraform init -reconfigure` migrates all state from the old backend to the new backend automatically; standard `terraform init` requires manual confirmation
- B) `terraform init -reconfigure` initialises the new backend configuration without migrating any existing state and without prompting — the old backend's state is left in place and the new backend starts empty; standard `terraform init` offers the option to migrate state to the new backend
- C) Both commands produce identical behaviour — `-reconfigure` is simply a flag that suppresses the interactive prompt while still migrating state
- D) `-reconfigure` is only valid for the first `terraform init` run; after a backend exists, only standard `terraform init` can be used

**Answer**: B

**Explanation**:
When a backend changes, standard `terraform init` detects the difference and prompts: "Do you want to copy existing state to the new backend?" — offering to migrate state via the equivalent of `terraform init -migrate-state`. `terraform init -reconfigure` takes a different approach: it **forces re-initialisation of the new backend without moving any state**. The old backend retains its state; the new backend starts with an empty state file. This is useful when you intentionally want a fresh state (e.g., starting a new environment) but risks state loss if used accidentally when migration was intended.

---

### Question 6 — `terraform fmt` (No Flags) vs `terraform fmt -diff`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between in-place formatting and diff-only preview

**Question**:
Compare running `terraform fmt` with no flags versus `terraform fmt -diff`. A `.tf` file has inconsistent indentation. What happens to the file in each case?

- A) Both commands modify the file — `-diff` additionally shows the changes that were made
- B) `terraform fmt` (no flags) rewrites the file directly on disk to canonical format; `terraform fmt -diff` displays a unified diff showing what would change **without writing any changes to disk**
- C) `terraform fmt` (no flags) only prints the file names that need formatting; `-diff` both shows and applies the changes
- D) Both commands are read-only — to actually reformat a file you must pipe the diff output back to the file manually

**Answer**: B

**Explanation**:
`terraform fmt` without flags is the **action** mode: it reads all `.tf` files, applies canonical formatting, and writes the corrected content back to disk. Files that were already correctly formatted are untouched. `terraform fmt -diff` is the **preview** mode: it computes the same formatting changes but outputs them as a unified diff to stdout without writing anything to disk. This is useful for reviewing what `fmt` would change before applying it. The `-check` flag is a third variant — also read-only, but exits with code 1 rather than displaying a diff.

---

### Question 7 — `terraform plan -refresh=false` vs Standard `terraform plan`

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Contrasting two key differences between a refresh-skipping plan and a full live-query plan

**Question**:
Compare `terraform plan` (standard) with `terraform plan -refresh=false`. Which TWO statements correctly describe a difference between the two? (Select two.)

- A) Standard `terraform plan` queries the live provider API to refresh each resource's actual current attributes before computing the diff; `terraform plan -refresh=false` uses the cached state from the last apply without making any API calls
- B) `terraform plan -refresh=false` is more accurate than standard `terraform plan` because it avoids potential API rate-limiting errors
- C) If an out-of-band change was made to a resource in the cloud (e.g., a tag changed via the console), standard `terraform plan` detects this and includes it in the plan diff; `terraform plan -refresh=false` does not detect the drift because it never queries the live API
- D) Both commands always produce identical plan output — the `-refresh=false` flag only affects performance, not the content of the plan

**Answer**: A, C

**Explanation**:
(A) Standard `terraform plan` begins with a **refresh** phase — it queries the provider API for each managed resource to get its current real-world attributes, updating the in-memory state used for planning. `-refresh=false` skips this phase entirely and uses the state file as-is. (C) As a direct consequence, standard plan detects **drift** (changes made outside Terraform) because it sees the live attribute values; `-refresh=false` misses any drift because it never checks live state. The trade-off is speed vs accuracy: `-refresh=false` is faster in large environments but risks producing an incomplete or incorrect plan if the real infrastructure has drifted from the cached state.

---

### Question 8 — `terraform output` vs `terraform output -json`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between human-readable output display and structured JSON output for scripting

**Question**:
Compare `terraform output` (no flags) and `terraform output -json`. A configuration has two outputs: a string `vpc_id` and a list `subnet_ids`. How does the format of each command's response differ, and which is more appropriate for use in a CI script?

- A) Both return identical data — the only difference is that `-json` wraps the output in a JSON array
- B) `terraform output` prints all output values in human-readable format (strings quoted, lists formatted); `terraform output -json` returns a structured JSON object with `sensitive`, `type`, and `value` metadata fields for each output — the JSON format is the appropriate choice for CI scripts and programmatic processing
- C) `terraform output -json` is only valid for outputs declared with `type = "string"` — list outputs require `terraform output` without flags
- D) `terraform output` returns values without quotes for scripting; `-json` adds quotes and type annotations for documentation purposes

**Answer**: B

**Explanation**:
`terraform output` formats values for human consumption — string values are displayed with surrounding double quotes (e.g. `"vpc-0abc123"`), and lists are displayed in a readable multi-line format. It is convenient for quick manual inspection. `terraform output -json` returns a machine-readable JSON document where each output name maps to an object containing the actual `value`, the Terraform `type`, and a `sensitive` boolean. This structured format is the correct choice for CI pipelines, shell scripts, or external tools that need to parse and use output values programmatically, as it avoids quote handling issues and provides type information.

---

### Question 9 — `terraform plan -target` vs Full `terraform plan`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between a targeted partial plan and a complete plan covering all resources

**Question**:
A configuration manages 30 resources. An engineer is debugging a single resource and runs `terraform plan -target=aws_instance.debug`. Compare this to running a full `terraform plan` with no target. What risk does the targeted approach introduce that the full plan does not?

- A) Targeted plans are more accurate because fewer resources are evaluated — there is no additional risk
- B) Targeted plans take longer because Terraform must first scan all 30 resources before filtering to the target
- C) A targeted plan only evaluates the specified resource and its direct dependencies — it may miss cascading changes to downstream resources that depend on the target, producing an **incomplete view** that does not reflect all the changes a full apply would cause; a full plan shows the complete picture
- D) Targeted plans lock the state file, preventing other engineers from running plans simultaneously

**Answer**: C

**Explanation**:
`terraform plan -target` is intentionally scoped — it limits Terraform's analysis to the targeted resource and the resources it explicitly depends on. Resources that **depend on** the target (downstream) are not evaluated. This means the plan may not show changes that would propagate through the dependency chain on a full apply. Terraform itself warns: "Note: Objects have changed outside of Terraform" or "This plan was saved to: ... you are responsible for knowing this is incomplete." `-target` is appropriate for emergency fixes or exploratory debugging, but should **not** be used as a normal workflow pattern — full plans give the complete and correct picture.

---

### Question 10 — `terraform destroy` (Full) vs `terraform destroy -target`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between full environment destruction and targeted single-resource destruction

**Question**:
A production environment has 50 resources. An engineer needs to destroy only one obsolete EC2 instance (`aws_instance.legacy`). Compare running `terraform destroy` (no flags) versus `terraform destroy -target=aws_instance.legacy`. What is the critical difference in scope?

- A) Both commands destroy the same set of resources — `-target` is only valid with `apply`, not `destroy`
- B) `terraform destroy` (no flags) plans to destroy **all 50 resources** managed by the configuration; `terraform destroy -target=aws_instance.legacy` destroys only the targeted resource (and any resources that depend on it) — the other 49 resources are unaffected
- C) `terraform destroy -target` is not recommended because it deletes the entire resource group containing the target
- D) `terraform destroy` (no flags) only destroys resources in the current workspace; `-target` destroys the resource across all workspaces

**Answer**: B

**Explanation**:
`terraform destroy` without flags is equivalent to `terraform apply -destroy` — it plans the destruction of **every** resource tracked in the current state. Running this accidentally in production would schedule 50 resources for deletion. `terraform destroy -target=aws_instance.legacy` limits the blast radius to only the specified resource and its dependents (if any). This is the safe, surgical approach when you need to remove a single resource without touching the rest of the environment. As with `plan -target`, Terraform warns that the resulting plan is incomplete.

---

### Question 11 — `~` Symbol vs `-/+` Symbol in Plan Output

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Contrasting two key differences between update-in-place and replacement plan operations

**Question**:
Compare the `~` symbol and the `-/+` symbol in `terraform plan` output. Which TWO statements correctly distinguish these two plan operations? (Select two.)

- A) `~` indicates an **update in-place** — the existing resource object continues to exist and only specific attribute values are changed without destroying and recreating the resource
- B) `-/+` indicates that the resource will be **destroyed and then recreated** — a new resource object is provisioned to replace the old one, and the resource experiences downtime during the transition
- C) Both `~` and `-/+` result in the same final state — they differ only in whether Terraform prompts for confirmation
- D) `~` means the resource is being moved to a different Terraform workspace; `-/+` means it is being moved between providers

**Answer**: A, B

**Explanation**:
(A) The `~` prefix signals an **in-place update** — Terraform modifies specific attributes of the existing resource object via the provider API (e.g., updating a tag or resizing a volume) without destroying it. The resource ID stays the same throughout. (B) The `-/+` prefix signals a **replacement** — Terraform destroys the existing resource and creates a brand-new one in its place. This happens when an attribute change cannot be applied in-place (e.g., changing an EC2 instance's AMI or a database's `engine_version`). Replacement operations cause service downtime and result in a new resource ID. Understanding which changes trigger replacement vs in-place update is an important exam skill.

---

### Question 12 — `terraform init -migrate-state` vs `terraform init -reconfigure`

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Deep contrast between migrating state to a new backend vs discarding the old backend's state record

**Question**:
A team is changing their Terraform backend from an S3 bucket in `us-east-1` to a new S3 bucket in `eu-west-1`. Their existing state file contains 40 resources. Compare `terraform init -migrate-state` and `terraform init -reconfigure` for this scenario. What is the critical operational difference?

- A) Both flags copy the state to the new backend — `-reconfigure` additionally cleans up the old backend
- B) `-migrate-state` and `-reconfigure` are equivalent; the distinction only matters when changing backend types, not when changing buckets within the same backend type
- C) `terraform init -migrate-state` copies the existing state from the old S3 bucket to the new S3 bucket before switching the active backend — all 40 resource records are preserved and Terraform continues managing them; `terraform init -reconfigure` initialises the new backend without copying any state — the new backend starts empty, and the 40 resources would appear unmanaged on the next plan
- D) `terraform init -reconfigure` is the recommended approach for cross-region state migration because `-migrate-state` only supports same-region moves

**Answer**: C

**Explanation**:
`terraform init -migrate-state` is the correct flag when you want to **move** your state from one backend to another — it reads the existing state from the current backend, writes it to the new backend, and updates the backend configuration. All 40 resource records transfer to the new location, and Terraform continues managing them seamlessly. `terraform init -reconfigure` initialises the new backend cleanly **without any state migration** — it is appropriate when you intentionally want a fresh state (e.g., setting up a new environment) or when the old state will be managed separately. Using `-reconfigure` when migration was intended would result in a new empty state, and the next `terraform plan` would propose creating all 40 resources again — a dangerous situation in production.

---

### Question 13 — `terraform graph` vs `terraform console`

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Contrast between static dependency graph output and interactive expression evaluation

**Question**:
Compare `terraform graph` and `terraform console` as diagnostic and development tools. What does each command help the operator understand, and how do they differ in their interaction model and output?

- A) Both tools are interactive REPLs — `terraform graph` explores the dependency graph interactively; `terraform console` explores resource attributes interactively
- B) `terraform graph` is used to test HCL expressions before committing them; `terraform console` generates a visual map of resource dependencies
- C) `terraform graph` produces a **one-shot DOT-format text output** of the static resource dependency graph — it shows how resources depend on each other and what ordering Terraform will use for operations; it is non-interactive and requires Graphviz to render visually. `terraform console` is an **interactive REPL** that evaluates HCL expressions and built-in functions against the current configuration and state — it is used to test and debug expressions, functions, and variable references before embedding them in configuration files
- D) Both commands are deprecated — `terraform graph` was replaced by `terraform state list` and `terraform console` was replaced by `terraform validate`

**Answer**: C

**Explanation**:
`terraform graph` and `terraform console` serve completely different purposes and have different interaction models. `terraform graph` is a **batch output tool** — running it produces a DOT language text graph to stdout representing the dependency relationships between all managed resources. It answers "in what order will Terraform create/destroy these resources?" You pipe this output to Graphviz to produce a visual diagram. It is non-interactive and makes no state modifications. `terraform console` is an **interactive REPL** — you type HCL expressions (`format("Hello, %s!", var.name)`, `length(var.subnets)`) and the console evaluates them immediately against the current module's variables and state. It is invaluable for debugging complex expressions and testing function calls before using them in production configuration.

---
