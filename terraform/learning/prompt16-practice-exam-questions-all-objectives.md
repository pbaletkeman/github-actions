# Practice Exam — All Objectives

> **HashiCorp Certified Terraform Associate (004)**
> 25 Questions Covering All 8 Exam Objectives

---

## Instructions

Answer each question before revealing the explanation. Pay attention to questions marked ⚠️ — these are commonly answered incorrectly.

---

## Objective 1 — Infrastructure as Code Concepts (Q1–3)

**Q1.** What does it mean for a tool to use a **declarative** approach to infrastructure provisioning?

- A) The tool executes commands step-by-step in the order you specify
- B) The tool describes the desired end state and determines how to achieve it
- C) The tool creates infrastructure only when triggered by events
- D) The tool requires you to specify both the current and desired states

<details><summary>Answer</summary>

**B** — Declarative means you describe the **desired end state**, not the step-by-step procedure.
Option A describes imperative/procedural tools like Bash scripts.

</details>

---

**Q2.** Which of the following is a benefit of using version control with Terraform configurations?

- A) It encrypts the state file automatically
- B) It enables team collaboration, change history, and rollback
- C) It replaces the need for a remote backend
- D) It automatically applies configurations when changes are pushed

<details><summary>Answer</summary>

**B** — Version control (e.g., Git) provides collaboration, change history, and rollback. It does not encrypt state, replace backends, or auto-apply.

</details>

---

**Q3.** A team uses Terraform to manage AWS infrastructure. What is the primary purpose of the `terraform.tfstate` file?

- A) It stores API credentials for providers
- B) It records the mapping between Terraform resources and real cloud objects
- C) It defines the desired infrastructure configuration
- D) It logs all Terraform command executions

<details><summary>Answer</summary>

**B** — State maps Terraform resource addresses to real infrastructure IDs and tracks current attribute values.

</details>

---

## Objective 2 — Terraform Fundamentals (Q4–6)

**Q4.** What is the role of a Terraform **provider**?

- A) To store Terraform state remotely
- B) To translate Terraform HCL into cloud provider API calls
- C) To validate Terraform configurations for syntax errors
- D) To manage team access to Terraform workspaces

<details><summary>Answer</summary>

**B** — Providers are plugins that interact with APIs (AWS, Azure, GCP, etc.) to create, read, update, and delete resources.

</details>

---

**Q5.** Which constraint operator restricts updates to patch versions only? ⚠️

- A) `>=`
- B) `=`
- C) `~>`
- D) `!=`

<details><summary>Answer</summary>

**C** — `~>` is the pessimistic constraint operator. `~> 1.2.3` allows `>= 1.2.3, < 1.3.0` (patch updates only). `~> 1.2` allows `>= 1.2, < 2.0` (minor updates).

</details>

---

**Q6.** After running `terraform apply`, a developer runs `terraform plan` again immediately with no changes to configuration. What is the expected output?

- A) Error: state file is locked
- B) Plan: 0 to add, 0 to change, 0 to destroy
- C) Plan: 1 to add (state needs to be refreshed)
- D) Warning: configuration drift detected

<details><summary>Answer</summary>

**B** — With no changes to configuration or infrastructure, plan shows no changes needed.

</details>

---

## Objective 3 — Terraform Workflow (Q7–10)

**Q7.** What does `terraform validate` check?

- A) Whether cloud resources match the configuration
- B) Whether the configuration is syntactically valid and internally consistent
- C) Whether provider credentials are correct
- D) Whether the state file is up to date

<details><summary>Answer</summary>

**B** — `terraform validate` checks syntax and configuration consistency. It does **not** contact providers or check credentials.

</details>

---

**Q8.** A developer wants to preview changes before applying. Which command should they run first?

- A) `terraform init`
- B) `terraform show`
- C) `terraform plan`
- D) `terraform apply -dry-run`

<details><summary>Answer</summary>

**C** — `terraform plan` shows what changes would be made without actually making them. Note: `-dry-run` is not a valid Terraform flag.

</details>

---

**Q9.** What is the effect of running `terraform apply -auto-approve`?

- A) Applies only resources marked with `auto_apply = true`
- B) Skips the interactive confirmation prompt
- C) Applies changes to all workspaces simultaneously
- D) Automatically approves pending policy checks

<details><summary>Answer</summary>

**B** — `-auto-approve` skips the "Do you want to perform these actions?" interactive prompt.

</details>

---

**Q10.** Which command formats Terraform configuration files to the canonical HCL style?

- A) `terraform lint`
- B) `terraform validate`
- C) `terraform fmt`
- D) `terraform style`

<details><summary>Answer</summary>

**C** — `terraform fmt` formats files. `terraform lint` is not a built-in command.

</details>

---

## Objective 4 — Configuration Language (Q11–16)

**Q11.** What is the difference between a `variable` block and a `local` block?

- A) Variables can be used in expressions; locals cannot
- B) Variables accept external input; locals compute internal values
- C) Locals are always strings; variables can be any type
- D) Variables are module-scoped; locals are global

<details><summary>Answer</summary>

**B** — Variables accept input from outside the module (CLI, tfvars, environment). Locals are computed values defined within the module for reuse.

</details>

---

**Q12.** A resource is using `for_each` with a map. How do you reference the key for each instance?

- A) `self.key`
- B) `count.index`
- C) `each.key`
- D) `item.key`

<details><summary>Answer</summary>

**C** — `each.key` is the map key (or set element) for the current instance. `each.value` is the map value.

</details>

---

**Q13.** What does this expression return? ⚠️
`lookup({a = 1, b = 2}, "c", 99)`

- A) `null`
- B) Error: key not found
- C) `99`
- D) `0`

<details><summary>Answer</summary>

**C** — `lookup(map, key, default)` returns the default value (`99`) when the key (`"c"`) does not exist.

</details>

---

**Q14.** Which lifecycle meta-argument creates a replacement resource before destroying the original?

- A) `prevent_destroy = true`
- B) `ignore_changes = [all]`
- C) `create_before_destroy = true`
- D) `replace_triggered_by = [self]`

<details><summary>Answer</summary>

**C** — `create_before_destroy = true` is used for zero-downtime replacements, ensuring the new resource is ready before the old one is removed.

</details>

---

**Q15.** What is the behaviour of a `check` block when its assertion fails? ⚠️

- A) The apply is rolled back
- B) The apply is blocked
- C) A warning is displayed but the apply continues
- D) The resource is marked as tainted

<details><summary>Answer</summary>

**C** — `check` blocks (Terraform 1.5+) produce warnings only. They never fail or block an apply.

</details>

---

**Q16.** A variable has `sensitive = true`. Where is its value visible? ⚠️

- A) Terminal output only
- B) State file only
- C) Neither terminal output nor state file
- D) State file, but masked in terminal output

<details><summary>Answer</summary>

**D** — `sensitive = true` masks the value in terminal/plan output but the value is **always stored in plaintext in the state file**. Protect state with encryption.

</details>

---

## Objective 5 — Modules (Q17–18)

**Q17.** In a module source URL, what does the `//` separator indicate? ⚠️

- A) A comment in the URL
- B) The separator between the repository root and a subdirectory path
- C) A syntax error — single slash should be used
- D) The separator between protocol and hostname

<details><summary>Answer</summary>

**B** — `github.com/org/repo//modules/vpc` — `//` separates the repository root from the subdirectory `modules/vpc`. This is correct Terraform syntax.

</details>

---

**Q18.** Which of the following is NOT a valid module source type?

- A) `./local/path`
- B) `hashicorp/consul/aws` (Terraform Registry)
- C) `github.com/org/repo`
- D) `terraform:built-in/networking`

<details><summary>Answer</summary>

**D** — `terraform:built-in/networking` is not a valid source type. Valid sources are local paths, registry addresses, git URLs, HTTP archives, S3, and GCS.

</details>

---

## Objective 6 — State (Q19–21)

**Q19.** What does `terraform.tfstate.backup` contain? ⚠️

- A) All historical state versions
- B) The state from before the most recent apply only
- C) A backup created when `terraform backup` is run
- D) The state from when the configuration was last validated

<details><summary>Answer</summary>

**B** — `terraform.tfstate.backup` contains only the **previous** state. Only one backup is kept; it is overwritten on each apply.

</details>

---

**Q20.** Which command detects drift between cloud resources and Terraform state without making any infrastructure changes?

- A) `terraform refresh`
- B) `terraform plan`
- C) `terraform plan -refresh-only`
- D) `terraform state sync`

<details><summary>Answer</summary>

**C** — `terraform plan -refresh-only` shows drift detected by comparing actual cloud state with state file, without proposing infrastructure changes.

</details>

---

**Q21.** A team accidentally deleted a resource from state using `terraform state rm`. The resource still exists in the cloud. What happens on the next `terraform plan`?

- A) Terraform shows the resource as already destroyed
- B) Terraform shows the resource as a new resource to create
- C) Terraform returns an error — missing state is not allowed
- D) Terraform automatically re-imports the resource

<details><summary>Answer</summary>

**B** — Without a state entry, Terraform does not know the resource exists and treats the resource block as a new resource to create.

</details>

---

## Objective 7 — Import and Logging (Q22–23)

**Q22.** What is the advantage of using an `import` block (Terraform 1.5+) over the `terraform import` CLI command?

- A) Import blocks are faster to execute
- B) Import blocks can import multiple resources simultaneously
- C) Import blocks can generate HCL configuration and integrate with `terraform plan`
- D) Import blocks do not require the resource to exist in the cloud

<details><summary>Answer</summary>

**C** — The `import` block allows `terraform plan -generate-config-out=file.tf` to generate HCL and integrates with the normal plan/apply workflow.

</details>

---

**Q23.** Which environment variable enables verbose Terraform debugging output?

- A) `TERRAFORM_DEBUG=1`
- B) `TF_DEBUG=true`
- C) `TF_LOG=DEBUG`
- D) `TF_VERBOSE=1`

<details><summary>Answer</summary>

**C** — `TF_LOG` accepts: TRACE, DEBUG, INFO, WARN, ERROR, OFF. `TF_LOG=TRACE` is the most verbose.

</details>

---

## Objective 8 — HCP Terraform (Q24–25)

**Q24.** A policy in HCP Terraform is set to `soft-mandatory`. An engineer's run fails this policy check. What can happen?

- A) The run is blocked with no option to proceed
- B) An authorised team member can override the policy and allow the run
- C) The policy is automatically downgraded to advisory for this run
- D) The engineer must delete and recreate the workspace

<details><summary>Answer</summary>

**B** — `soft-mandatory` blocks the run but allows an authorised user to override. Only `hard-mandatory` cannot be overridden.

</details>

---

**Q25.** How does HCP Terraform handle locking for concurrent runs?

- A) It uses a DynamoDB table like the S3 backend
- B) It uses OS-level file locks
- C) It has native built-in state locking — no external service required
- D) Locking must be configured separately in the workspace settings

<details><summary>Answer</summary>

**C** — HCP Terraform has native built-in locking with no additional configuration or external services required.

</details>

---

## Answer Key

| Q | Answer | Objective |
|---|--------|-----------|
| 1 | B | 1 — IaC Concepts |
| 2 | B | 1 — IaC Concepts |
| 3 | B | 1 — IaC Concepts |
| 4 | B | 2 — Fundamentals |
| 5 | C | 2 — Fundamentals |
| 6 | B | 2 — Fundamentals |
| 7 | B | 3 — Workflow |
| 8 | C | 3 — Workflow |
| 9 | B | 3 — Workflow |
| 10 | C | 3 — Workflow |
| 11 | B | 4 — Configuration |
| 12 | C | 4 — Configuration |
| 13 | C | 4 — Configuration |
| 14 | C | 4 — Configuration |
| 15 | C | 4 — Configuration |
| 16 | D | 4 — Configuration |
| 17 | B | 5 — Modules |
| 18 | D | 5 — Modules |
| 19 | B | 6 — State |
| 20 | C | 6 — State |
| 21 | B | 6 — State |
| 22 | C | 7 — Import |
| 23 | C | 7 — Logging |
| 24 | B | 8 — HCP Terraform |
| 25 | C | 8 — HCP Terraform |

---

[⬅️ prompt15-hcp-terraform-governance-security-advanced.md](prompt15-hcp-terraform-governance-security-advanced.md) | **16 / 17** | [Next ➡️ prompt17-hands-on-capstone-project.md](prompt17-hands-on-capstone-project.md)
