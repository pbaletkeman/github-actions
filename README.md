# GitHub Actions Learning Repository

A structured learning repository for technical certification and exam preparation. Contains study materials, hands-on notebooks, and multi-language quiz engine implementations covering GitHub Actions, Databricks (Apache Spark), and Terraform.

---

## Repository Purpose

This repository serves as a comprehensive study platform for three certification paths:

| Certification | Provider | Study Materials |
|--------------|----------|----------------|
| GitHub Actions (GH-200) | GitHub / Microsoft | `githubactions/` |
| Databricks Certified Associate Developer for Apache Spark | Databricks | `databricks/learning/` |
| HashiCorp Certified Terraform Associate (004) | HashiCorp | `terraform/learning/` |

---

## Directory Structure

```
github-actions/
├── .github/                          # GitHub Actions workflow files
├── actions-source-material/          # Source prompts used to generate study content
│   ├── gh-200-iteration-1.md through gh-200-iteration-10.md
│   └── README.md
├── databricks/
│   └── learning/                     # 32 Jupyter notebooks + 32 markdown guides
├── engine/                           # Shared quiz engine logic
├── githubactions/                    # GitHub Actions study materials (19 topics)
│   ├── 01-GitHub-Actions-VS-Code-Extension.md
│   ├── 02-Contextual-Information.md
│   ├── ... (01–19)
│   ├── GitHub-Workflows-Guide.md
│   ├── study-plan.md
│   ├── exam-overview.md
│   ├── README.md
│   └── INDEX.md
├── quiz-engine/                      # Quiz engine in 8 languages
│   ├── quiz-engine-csharp/
│   ├── quiz-engine-dart/
│   ├── quiz-engine-golang/
│   ├── quiz-engine-java/
│   ├── quiz-engine-nodejs/
│   ├── quiz-engine-python/
│   ├── quiz-engine-rust/
│   └── quiz-engine-springboot/
└── terraform/
    └── learning/                     # 17 Jupyter notebooks + 17 markdown guides
```

---

## GitHub Actions Study Materials

**Location:** `githubactions/`

Comprehensive study materials for the **GitHub Actions (GH-200)** certification exam, covering all exam domains.

### Topics Covered

| File | Topic |
|------|-------|
| [01-GitHub-Actions-VS-Code-Extension.md](githubactions/01-GitHub-Actions-VS-Code-Extension.md) | VS Code extension setup and usage |
| [02-Contextual-Information.md](githubactions/02-Contextual-Information.md) | GitHub context and metadata |
| [03-Context-Availability-Reference.md](githubactions/03-Context-Availability-Reference.md) | Context availability by event |
| [04-Workflow-File-Structure.md](githubactions/04-Workflow-File-Structure.md) | YAML syntax and workflow anatomy |
| [05-Workflow-Trigger-Events.md](githubactions/05-Workflow-Trigger-Events.md) | Triggers: push, PR, schedule, dispatch |
| [06-Custom-Environment-Variables.md](githubactions/06-Custom-Environment-Variables.md) | Custom env vars and scoping |
| [07-Default-Environment-Variables.md](githubactions/07-Default-Environment-Variables.md) | Built-in GitHub environment variables |
| [08-Environment-Protection-Rules.md](githubactions/08-Environment-Protection-Rules.md) | Deployment environments and protection rules |
| [09-Workflow-Artifacts.md](githubactions/09-Workflow-Artifacts.md) | Uploading and downloading artifacts |
| [10-Workflow-Caching.md](githubactions/10-Workflow-Caching.md) | Dependency caching strategies |
| [11-Workflow-Sharing.md](githubactions/11-Workflow-Sharing.md) | Reusable workflows and composite actions |
| [12-Workflow-Debugging.md](githubactions/12-Workflow-Debugging.md) | Debugging and troubleshooting |
| [13-Workflows-REST-API.md](githubactions/13-Workflows-REST-API.md) | GitHub REST API for workflows |
| [14-Reviewing-Deployments.md](githubactions/14-Reviewing-Deployments.md) | Deployment review and approval gates |
| [15-Creating-Publishing-Actions.md](githubactions/15-Creating-Publishing-Actions.md) | Building and publishing custom actions |
| [16-Managing-Runners.md](githubactions/16-Managing-Runners.md) | GitHub-hosted and self-hosted runners |
| [17-GitHub-Actions-Enterprise.md](githubactions/17-GitHub-Actions-Enterprise.md) | Enterprise features and governance |
| [18-Security-and-Optimization.md](githubactions/18-Security-and-Optimization.md) | Security hardening and performance |
| [19-Common-Failures-Troubleshooting.md](githubactions/19-Common-Failures-Troubleshooting.md) | Common failure patterns and fixes |

Additional resources:
- [GitHub-Workflows-Guide.md](githubactions/GitHub-Workflows-Guide.md) — Complete reference guide
- [study-plan.md](githubactions/study-plan.md) — Exam study plan
- [exam-overview.md](githubactions/exam-overview.md) — Exam structure and domains

---

## Databricks Learning Materials

**Location:** `databricks/learning/`

Study materials for the **Databricks Certified Associate Developer for Apache Spark** exam. 32 Jupyter notebooks with paired markdown documentation covering all 7 topic areas.

### Topic Areas

| Topic | Notebooks | Focus |
|-------|-----------|-------|
| Topic 1 — Spark Architecture | prompts 1–5 | Spark fundamentals, clusters, execution model |
| Topic 2 — Spark SQL & DataFrames | prompts 6–10 | DataFrame API, SQL operations, schema management |
| Topic 3 — Data Sources & I/O | prompts 11–15 | Reading/writing files, formats (Parquet, Delta, JSON, CSV) |
| Topic 4 — Transformations | prompts 16–19 | Joins, aggregations, window functions |
| Topic 5 — Performance Tuning | prompts 20–24 | Caching, partitioning, shuffles, Catalyst optimizer |
| Topic 6 — Streaming | prompts 25–29 | Structured Streaming, watermarks, output modes |
| Topic 7 — Delta Lake | prompts 30–32 | ACID transactions, time travel, schema evolution |

Special notebooks:
- `topic-prompt31-practice-exam.ipynb` / `.md` — 20-question practice exam across all topics
- `topic-prompt32-capstone.ipynb` / `.md` — End-to-end ride-sharing pipeline project

### How to Use

1. Open notebooks in Databricks (Community Edition is free) or Jupyter
2. Read the paired `.md` file for a study-ready summary
3. Use the practice exam (prompt31) for self-assessment

See [databricks/learning/README.md](databricks/learning/README.md) for the full topic index.

---

## Terraform Learning Materials

**Location:** `terraform/learning/`

Study materials for the **HashiCorp Certified Terraform Associate (004)** exam. 17 Jupyter notebooks with paired markdown documentation covering all 8 exam objectives.

### Exam Objectives

| Objective | Notebooks | Topics |
|-----------|-----------|--------|
| 1 — IaC Concepts | prompt01 | Infrastructure as Code, idempotency, declarative vs imperative |
| 2 — Fundamentals | prompt02 | Providers, plugin model, `required_providers`, version constraints |
| 2 — State | prompt03 | State purpose, state file, `terraform.tfstate` |
| 3 — Core Workflow | prompt04 | init, plan, apply, destroy, fmt, validate |
| 4 — Configuration | prompt05–10 | Resources, data sources, variables, locals, outputs, functions, lifecycle |
| 5 — Modules | prompt11 | Module types, sources, inputs, outputs, registry |
| 6 — State Advanced | prompt12 | Backends, locking, S3+DynamoDB, drift detection |
| 7 — Import & Debug | prompt13 | `import` block, CLI import, TF_LOG, state inspection |
| 8 — HCP Terraform | prompt14–15 | Workspaces, runs, variables, policies, RBAC, OIDC |

### Notebook Index

| File | Topic |
|------|-------|
| [prompt01-what-is-iac.md](terraform/learning/prompt01-what-is-iac.md) | What is Infrastructure as Code? |
| [prompt02-providers-plugin-model.md](terraform/learning/prompt02-providers-plugin-model.md) | Providers and the plugin model |
| [prompt03-terraform-state.md](terraform/learning/prompt03-terraform-state.md) | Terraform state |
| [prompt04-core-workflow-cli.md](terraform/learning/prompt04-core-workflow-cli.md) | Core workflow and CLI commands |
| [prompt05-resource-data-blocks.md](terraform/learning/prompt05-resource-data-blocks.md) | Resource and data blocks |
| [prompt06-variables-locals-outputs.md](terraform/learning/prompt06-variables-locals-outputs.md) | Variables, locals, and outputs |
| [prompt07-complex-types-collections.md](terraform/learning/prompt07-complex-types-collections.md) | Complex types, count, for_each, dynamic blocks |
| [prompt08-builtin-functions-expressions.md](terraform/learning/prompt08-builtin-functions-expressions.md) | Built-in functions and expressions |
| [prompt09-dependencies-lifecycle.md](terraform/learning/prompt09-dependencies-lifecycle.md) | Resource dependencies and lifecycle |
| [prompt10-custom-conditions-sensitive-data.md](terraform/learning/prompt10-custom-conditions-sensitive-data.md) | Custom conditions and sensitive data |
| [prompt11-terraform-modules.md](terraform/learning/prompt11-terraform-modules.md) | Terraform modules |
| [prompt12-state-backends-locking-remote-state.md](terraform/learning/prompt12-state-backends-locking-remote-state.md) | State backends, locking, and remote state |
| [prompt13-importing-infrastructure-state-inspection.md](terraform/learning/prompt13-importing-infrastructure-state-inspection.md) | Importing infrastructure and state inspection |
| [prompt14-hcp-terraform-workspaces-runs-state.md](terraform/learning/prompt14-hcp-terraform-workspaces-runs-state.md) | HCP Terraform: workspaces, runs, and state |
| [prompt15-hcp-terraform-governance-security-advanced.md](terraform/learning/prompt15-hcp-terraform-governance-security-advanced.md) | HCP Terraform: governance and security |
| [prompt16-practice-exam-questions-all-objectives.md](terraform/learning/prompt16-practice-exam-questions-all-objectives.md) | 25-question practice exam |
| [prompt17-hands-on-capstone-project.md](terraform/learning/prompt17-hands-on-capstone-project.md) | Hands-on capstone project |

See [terraform/learning/README.md](terraform/learning/README.md) for the full topic index.

---

## Quiz Engine

**Location:** `quiz-engine/`

The quiz engine is implemented in 8 programming languages to allow practice with different technology stacks. Each implementation is functionally equivalent.

| Directory | Language / Framework |
|-----------|---------------------|
| `quiz-engine-csharp/` | C# (.NET) |
| `quiz-engine-dart/` | Dart |
| `quiz-engine-golang/` | Go |
| `quiz-engine-java/` | Java |
| `quiz-engine-nodejs/` | Node.js (JavaScript) |
| `quiz-engine-python/` | Python |
| `quiz-engine-rust/` | Rust |
| `quiz-engine-springboot/` | Java (Spring Boot) |

See [quiz-engine/README.md](quiz-engine/README.md) for setup instructions for each implementation.

---

## Prompt Engineering Source Material

**Location:** `actions-source-material/`

Iteration prompts used during the development of the GitHub Actions study content. Contains 10 iterations of refined prompt engineering (gh-200-iteration-1.md through gh-200-iteration-10.md) showing the evolution of the question-generation prompts.

---

## Getting Started

### For GitHub Actions Certification

1. Start with [githubactions/exam-overview.md](githubactions/exam-overview.md) for exam structure
2. Follow [githubactions/study-plan.md](githubactions/study-plan.md) for a guided study path
3. Work through topics 01–19 in order
4. Use the [quiz-engine](quiz-engine/) for practice questions

### For Databricks Certification

1. Review [databricks/learning/README.md](databricks/learning/README.md)
2. Work through notebooks by topic area (Topics 1–7)
3. Read paired `.md` files for quick review
4. Take the practice exam (prompt31) to assess readiness

### For Terraform Certification

1. Review [terraform/learning/README.md](terraform/learning/README.md)
2. Work through notebooks prompt01–prompt15 in order
3. Take the practice exam (prompt16) to assess readiness
4. Complete the capstone project (prompt17) for hands-on practice

---

## Prerequisites

| Section | Requirements |
|---------|-------------|
| GitHub Actions notebooks | GitHub account, VS Code with GitHub Actions extension |
| Databricks notebooks | Databricks account (Community Edition is free) or Jupyter |
| Terraform notebooks | Terraform CLI >= 1.5.0 installed |
| Quiz engine | Language runtime for the implementation you want to use |
