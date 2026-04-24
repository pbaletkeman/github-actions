# Terraform Associate (004) Study Notebooks

> **Exam Prep:** These notebooks were created to help learn and prepare for the **HashiCorp Certified: Terraform Associate (004)** certification. See [`plan-terraformAssociateExamOverview.prompt.md`](../plan-terraformAssociateExamOverview.prompt.md) for full exam details (~57 questions, 60 minutes, $70.50, 70% passing score, 2-year validity).

Jupyter notebooks for studying the HashiCorp Terraform Associate (004) certification. The series covers all 8 official exam objectives across 17 prompts, ending with a 25-question practice exam and a hands-on capstone project that requires no cloud credentials.

All notebooks contain explanatory markdown cells and HCL (HashiCorp Configuration Language) code examples.

- [Terraform Associate (004) Study Notebooks](#terraform-associate-004-study-notebooks)
  - [Exam Objectives Coverage](#exam-objectives-coverage)
  - [Notebooks](#notebooks)
  - [Capstone Project (Prompt 17)](#capstone-project-prompt-17)
  - [Summary](#summary)


---

## Exam Objectives Coverage

| Objective | Topics Covered | Notebooks |
|-----------|---------------|-----------|
| 1 | Understand IaC concepts | Prompt 1 |
| 2 | Understand Terraform's purpose and general concepts | Prompts 2–4 |
| 3 | Understand Terraform basics | Prompts 5–6 |
| 4 | Use the Terraform CLI | Prompt 4 |
| 5 | Interact with Terraform modules | Prompt 11 |
| 6 | Navigate the core workflow | Prompts 7–10 |
| 7 | Implement and maintain state | Prompts 3, 12–13 |
| 8 | Read, generate, and modify configuration | Prompts 5–10 |
| HCP Terraform | Workspaces, runs, governance, security | Prompts 14–15 |

---

## Notebooks

| # | Notebook | Study Guide | Title |
|---|----------|-------------|-------|
| 01 | [prompt01-what-is-iac.ipynb](prompt01-what-is-iac.ipynb) | [📄 MD](prompt01-what-is-iac.md) | What is Infrastructure as Code (IaC)? |
| 02 | [prompt02-providers-plugin-model.ipynb](prompt02-providers-plugin-model.ipynb) | [📄 MD](prompt02-providers-plugin-model.md) | Terraform Providers and the Plugin Model |
| 03 | [prompt03-terraform-state.ipynb](prompt03-terraform-state.ipynb) | [📄 MD](prompt03-terraform-state.md) | Terraform State: Purpose and Management |
| 04 | [prompt04-core-workflow-cli.ipynb](prompt04-core-workflow-cli.ipynb) | [📄 MD](prompt04-core-workflow-cli.md) | The Core Terraform Workflow: All CLI Commands |
| 05 | [prompt05-resource-data-blocks.ipynb](prompt05-resource-data-blocks.ipynb) | [📄 MD](prompt05-resource-data-blocks.md) | Resource and Data Blocks |
| 06 | [prompt06-variables-locals-outputs.ipynb](prompt06-variables-locals-outputs.ipynb) | [📄 MD](prompt06-variables-locals-outputs.md) | Variables, Locals, and Outputs |
| 07 | [prompt07-complex-types-collections.ipynb](prompt07-complex-types-collections.ipynb) | [📄 MD](prompt07-complex-types-collections.md) | Complex Types and Collections |
| 08 | [prompt08-builtin-functions-expressions.ipynb](prompt08-builtin-functions-expressions.ipynb) | [📄 MD](prompt08-builtin-functions-expressions.md) | Built-in Functions and Expressions |
| 09 | [prompt09-dependencies-lifecycle.ipynb](prompt09-dependencies-lifecycle.ipynb) | [📄 MD](prompt09-dependencies-lifecycle.md) | Resource Dependencies and Lifecycle |
| 10 | [prompt10-custom-conditions-sensitive-data.ipynb](prompt10-custom-conditions-sensitive-data.ipynb) | [📄 MD](prompt10-custom-conditions-sensitive-data.md) | Custom Conditions and Sensitive Data |
| 11 | [prompt11-terraform-modules.ipynb](prompt11-terraform-modules.ipynb) | [📄 MD](prompt11-terraform-modules.md) | Terraform Modules |
| 12 | [prompt12-state-backends-locking-remote-state.ipynb](prompt12-state-backends-locking-remote-state.ipynb) | [📄 MD](prompt12-state-backends-locking-remote-state.md) | State Backends, Locking, and Remote State |
| 13 | [prompt13-importing-infrastructure-state-inspection.ipynb](prompt13-importing-infrastructure-state-inspection.ipynb) | [📄 MD](prompt13-importing-infrastructure-state-inspection.md) | Importing Existing Infrastructure and Inspecting State |
| 14 | [prompt14-hcp-terraform-workspaces-runs-state.ipynb](prompt14-hcp-terraform-workspaces-runs-state.ipynb) | [📄 MD](prompt14-hcp-terraform-workspaces-runs-state.md) | HCP Terraform — Workspaces, Runs, and State |
| 15 | [prompt15-hcp-terraform-governance-security-advanced.ipynb](prompt15-hcp-terraform-governance-security-advanced.ipynb) | [📄 MD](prompt15-hcp-terraform-governance-security-advanced.md) | HCP Terraform — Governance, Security, and Advanced Features |
| 16 | [prompt16-practice-exam-questions-all-objectives.ipynb](prompt16-practice-exam-questions-all-objectives.ipynb) | [📄 MD](prompt16-practice-exam-questions-all-objectives.md) | Practice Exam Questions — All Objectives (25 questions) |
| 17 | [prompt17-hands-on-capstone-project.ipynb](prompt17-hands-on-capstone-project.ipynb) | [📄 MD](prompt17-hands-on-capstone-project.md) | Hands-On Capstone Project |

---

## Capstone Project (Prompt 17)

The capstone uses only the `hashicorp/random`, `hashicorp/local`, and `hashicorp/null` providers — no cloud credentials required. It exercises all 8 exam objectives in a single end-to-end project:

- `versions.tf` with `required_providers` version pinning
- `variables.tf` with input validation
- `locals.tf` with `toset` and `for` expressions
- `main.tf` with `for_each`, `lifecycle { prevent_destroy = true }`, and a `check` block
- A child module under `modules/config-file/` with its own `variables.tf`, `main.tf`, and `outputs.tf`
- `outputs.tf` with sensitive outputs and `for` expression maps
- Full `terraform init → plan → apply → destroy` walkthrough
- `moved` and `removed` block references
- S3 backend migration workflow (documentation)

---

## Summary

| Stat | Value |
|------|-------|
| Total notebooks | 17 |
| Total size | ~870 KB |
| Exam objectives covered | All 8 |
| Practice notebooks | 2 (exam + capstone) |
| Cloud credentials required | None (capstone uses local/random/null providers) |
