# Terraform Associate (004) — Question Bank Iter 1 Batch 3

**Iteration**: 1
**Iteration Style**: Foundational recall — define terms, name commands, identify flags
**Batch**: 3
**Objective**: 3 — Core Workflow & CLI
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 2 Easy / 9 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — Core Workflow Sequence

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Correct order of the core Terraform CLI workflow

**Question**:
Which of the following represents the correct core Terraform workflow sequence?

- A) `validate → init → fmt → plan → apply → destroy`
- B) `init → fmt → validate → plan → apply → destroy`
- C) `fmt → init → plan → validate → apply → destroy`
- D) `init → plan → validate → fmt → apply → destroy`

**Answer**: B

**Explanation**:
The standard Terraform workflow is: `init` (download providers/backend), `fmt` (format HCL), `validate` (check syntax and references), `plan` (preview changes), `apply` (provision resources), and finally `destroy` (tear down). Running `init` first is mandatory — no other command can succeed until the working directory is initialised.

---

### Question 2 — `terraform init` Purpose

**Difficulty**: Easy
**Answer Type**: one
**Topic**: What `terraform init` does

**Question**:
Which of the following is a primary action performed by `terraform init`?

- A) It applies all pending changes from the most recent execution plan
- B) It downloads required provider plugins into the `.terraform/` directory
- C) It validates that all resource configurations are syntactically correct
- D) It formats all `.tf` files in the working directory to canonical style

**Answer**: B

**Explanation**:
`terraform init` initialises the working directory. Its key actions include: downloading provider plugins to `.terraform/providers/`, creating or updating `.terraform.lock.hcl`, configuring the backend, and caching module source code to `.terraform/modules/`. It must be run before any other workflow command.

---

### Question 3 — When to Re-run `terraform init`

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Conditions requiring `terraform init` to be re-run

**Question**:
In which TWO situations must `terraform init` be re-run? (Select two.)

- A) When a new provider is added to `required_providers`
- B) When an output value is added to an existing module
- C) When the backend configuration is changed to a different provider
- D) When a variable default value is updated in `variables.tf`

**Answer**: A, C

**Explanation**:
`terraform init` must be re-run when: (1) a new provider is added (the plugin needs to be downloaded), (2) the backend is changed or reconfigured (use `-migrate-state` or `-reconfigure`), or (3) a new module source is added. Adding output values or changing variable defaults does not require re-initialisation because no plugins or backend configuration changes occur.

---

### Question 4 — `terraform validate` Network Requirement

**Difficulty**: Medium
**Answer Type**: one
**Topic**: `terraform validate` network access and what it checks

**Question**:
Which statement correctly describes `terraform validate`?

- A) It requires active cloud provider credentials to verify that referenced resources exist in the target environment
- B) It performs a live API call to confirm that the AMI ID or other resource identifiers in the config are valid
- C) It checks configuration syntax and internal references without requiring network access or provider credentials
- D) It generates a full execution plan and outputs the number of resources that will be created, updated, or destroyed

**Answer**: C

**Explanation**:
`terraform validate` checks HCL syntax, ensures variables and references are valid, and confirms type compatibility — all without connecting to any provider API. It does **not** verify whether referenced cloud resources (e.g., an AMI ID) actually exist. This makes it safe to run in air-gapped or unauthenticated environments.

---

### Question 5 — `terraform fmt` Flags

**Difficulty**: Medium
**Answer Type**: one
**Topic**: `terraform fmt -check` flag behaviour

**Question**:
What is the purpose of the `-check` flag on `terraform fmt`?

- A) It checks that all variable names follow HashiCorp's naming conventions
- B) It validates the syntax of all HCL files but does not reformat them
- C) It exits with a non-zero exit code if any files need reformatting, without modifying them — useful for CI pipelines
- D) It checks whether provider versions are compatible with the current Terraform version

**Answer**: C

**Explanation**:
`terraform fmt -check` scans all `.tf` files and exits with code 1 if any file is not already in canonical format. It makes **no changes** to any file. This is the standard way to enforce formatting rules in a CI pipeline — the build fails if a developer submits unformatted code.

---

### Question 6 — `terraform plan -out` Flag

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Saving and applying a plan file

**Question**:
What is the benefit of running `terraform plan -out=plan.tfplan` followed by `terraform apply plan.tfplan`?

- A) It allows Terraform to skip provider validation when applying the plan
- B) It guarantees that the exact set of changes reviewed in `plan` is applied, with no drift between the two steps
- C) It compresses the plan file so that it can be stored in version control alongside `.tf` files
- D) It allows `terraform apply` to run without any state file present

**Answer**: B

**Explanation**:
When `terraform apply` is run without a saved plan, Terraform re-runs the plan internally — if infrastructure changed between the plan and apply, the actual changes may differ from what was reviewed. Saving the plan with `-out` and then applying it directly ensures the **exact** plan that was reviewed is what gets executed, which is the recommended practice for production workflows and CI/CD pipelines.

---

### Question 7 — Plan Output Symbols

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Meaning of `terraform plan` output symbols

**Question**:
In `terraform plan` output, what does the symbol `-/+` indicate will happen to a resource?

- A) The resource will be updated in-place with no interruption
- B) The resource will be imported into state from an existing infrastructure object
- C) The resource will be destroyed and then recreated (replacement)
- D) The resource will be moved to a different Terraform workspace

**Answer**: C

**Explanation**:
The four plan symbols are: `+` (create), `~` (update in-place), `-` (destroy), and `-/+` (destroy then recreate — replacement). A replacement occurs when a change to a resource argument requires destroying the existing object and creating a new one, such as changing the `ami` attribute on an `aws_instance`.

---

### Question 8 — `terraform apply -auto-approve`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Purpose and use case of `-auto-approve`

**Question**:
What does the `-auto-approve` flag do when passed to `terraform apply`?

- A) It automatically selects the most recent saved plan file in the working directory
- B) It skips the interactive confirmation prompt before applying changes — commonly used in CI/CD pipelines
- C) It approves all individual resource creation steps sequentially without pausing
- D) It causes Terraform to apply changes to approved resources only and skip unapproved ones

**Answer**: B

**Explanation**:
By default, `terraform apply` prints the execution plan and requires the user to type `yes` before proceeding. The `-auto-approve` flag suppresses this prompt, allowing the command to run non-interactively. It is commonly used in CI/CD pipelines where human interaction is not possible, but should be used with caution in production environments.

---

### Question 9 — `terraform apply -replace`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Forcing resource replacement with `-replace`

**Question**:
Which command is the current (Terraform 1.5+) recommended way to force a specific resource to be destroyed and recreated on the next apply?

- A) `terraform taint aws_instance.web`
- B) `terraform apply -replace="aws_instance.web"`
- C) `terraform destroy -target=aws_instance.web` followed by `terraform apply`
- D) `terraform state rm aws_instance.web` followed by `terraform apply`

**Answer**: B

**Explanation**:
`terraform apply -replace="<address>"` is the current approach for forcing a resource recreation. The older `terraform taint` command has been deprecated since Terraform 1.5. While `state rm` followed by `apply` would also recreate the resource, it removes the resource from state first (losing tracked attributes), which is a different and more disruptive operation.

---

### Question 10 — `terraform output -raw`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: `terraform output -raw` use case

**Question**:
What does `terraform output -raw db_password` return, and when is this flag useful?

- A) The output value formatted as a JSON object with metadata fields
- B) The raw string value with no surrounding quotes — useful when piping the value into scripts or other commands
- C) The encrypted form of the output value for use with external secrets managers
- D) A diff showing the current output value versus the previous apply's value

**Answer**: B

**Explanation**:
`terraform output -raw <name>` prints the string value of a named output with no surrounding quotes or newline padding. Without `-raw`, string outputs are printed with double quotes (e.g., `"mypassword"`). The raw form is useful when piping the value to another command such as `aws secretsmanager put-secret-value --secret-string $(terraform output -raw db_password)`.

---

### Question 11 — `terraform console` Purpose

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What `terraform console` is used for

**Question**:
What is the primary purpose of `terraform console`?

- A) To display the current Terraform state in an interactive paginated viewer
- B) To provide an interactive REPL where Terraform expressions and built-in functions can be tested
- C) To monitor the progress of a running `terraform apply` operation in real time
- D) To inspect and modify resource attributes in the state file interactively

**Answer**: B

**Explanation**:
`terraform console` launches an interactive Read-Eval-Print Loop (REPL) that evaluates HCL expressions against the current state and configuration. It is primarily used to test functions (e.g., `length(["a","b","c"])`) and expressions before using them in configuration files. It does not modify state.

---

### Question 12 — `terraform destroy` Equivalence

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Relationship between `terraform destroy` and `terraform apply -destroy`

**Question**:
Which statement about `terraform destroy` is correct?

- A) `terraform destroy` permanently deletes the state file after all resources are removed
- B) `terraform destroy` is equivalent to `terraform apply -destroy` and both prompt for confirmation by default
- C) `terraform destroy` only removes resources created in the current workspace, while `terraform apply -destroy` removes all workspaces
- D) `terraform destroy` is deprecated and has been replaced by `terraform apply -destroy` in Terraform 1.5+

**Answer**: B

**Explanation**:
`terraform destroy` is a convenience alias for `terraform apply -destroy`. Both commands plan and execute the destruction of all resources managed by the current configuration, and both prompt for interactive confirmation unless `-auto-approve` is supplied. Neither deletes the state file — they update it to reflect that all resources have been removed.

---

### Question 13 — `terraform fmt` and `terraform validate` Scope

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Distinguishing what `fmt` and `validate` check

**Question**:
Which TWO of the following issues would `terraform validate` detect but `terraform fmt` would NOT? (Select two.)

- A) Inconsistent indentation in a `resource` block
- B) A variable referenced in a resource block that is not declared anywhere in the configuration
- C) Misaligned `=` signs in argument assignments
- D) An unsupported argument name passed to a resource type

**Answer**: B, D

**Explanation**:
`terraform fmt` only applies cosmetic formatting — it fixes indentation, alignment, and whitespace but cannot detect logical or semantic errors. `terraform validate` checks for: undeclared variables or references (B), unsupported argument names for a given resource type (D), type mismatches, and invalid module inputs. Indentation issues (A) and misaligned `=` signs (C) are purely formatting concerns that `fmt` corrects but `validate` ignores.

---
