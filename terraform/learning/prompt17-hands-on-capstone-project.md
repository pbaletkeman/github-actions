# Hands-On Capstone Project

> **HashiCorp Certified Terraform Associate (004)**
> All 8 Objectives — End-to-End Practice Project

---

## Overview

This capstone project builds a complete Terraform configuration covering all 8 exam objectives. It uses only the `random`, `local`, and `null` providers — no cloud account or credentials required.

---

## What You Build

A multi-environment configuration generator that:
- Creates random pet names for environments
- Uses a child module to write local configuration files
- Demonstrates variables, locals, outputs, conditions, lifecycle rules, state operations, and workspace-ready backend configuration

---

## Directory Structure

```
capstone/
├── main.tf              # Root module — calls child module, defines resources
├── variables.tf         # Input variable definitions
├── outputs.tf           # Output value definitions
├── locals.tf            # Local value computations
├── versions.tf          # Required providers and version constraints
└── modules/
    └── config-file/     # Child module
        ├── main.tf
        ├── variables.tf
        └── outputs.tf
```

---

## Task 1 — Required Providers (`versions.tf`)

```hcl
terraform {
  required_version = ">= 1.5.0"

  required_providers {
    random = {
      source  = "hashicorp/random"
      version = "~> 3.5"
    }
    local = {
      source  = "hashicorp/local"
      version = "~> 2.4"
    }
    null = {
      source  = "hashicorp/null"
      version = "~> 3.2"
    }
  }
}
```

**Concepts covered:** Objective 2 — providers, `required_providers`, version constraints, `~>` operator.

---

## Task 2 — Input Variables (`variables.tf`)

```hcl
variable "environments" {
  type        = list(string)
  description = "List of environment names to create"
  default     = ["dev", "staging", "production"]

  validation {
    condition = alltrue([
      for env in var.environments : contains(["dev", "staging", "production", "qa"], env)
    ])
    error_message = "All environments must be one of: dev, staging, production, qa."
  }
}

variable "team_name" {
  type        = string
  description = "Name of the team that owns these resources"

  validation {
    condition     = length(var.team_name) >= 2
    error_message = "team_name must be at least 2 characters."
  }
}

variable "enable_high_availability" {
  type    = bool
  default = false
}
```

**Concepts covered:** Objective 4 — variable blocks, type constraints, `validation` blocks.

---

## Task 3 — Locals (`locals.tf`)

```hcl
locals {
  # Computed prefix for all resource names
  resource_prefix = "${var.team_name}-capstone"

  # Map of environment to configuration
  env_config = {
    for env in var.environments : env => {
      replicas     = env == "production" ? 3 : 1
      log_level    = env == "production" ? "WARN" : "DEBUG"
      feature_flag = env != "production"
    }
  }

  # Timestamp tag
  created_at = formatdate("YYYY-MM-DD", timestamp())
}
```

**Concepts covered:** Objective 4 — `locals` block, `for` expression, conditional expression, `formatdate`.

---

## Task 4 — Resources with `for_each` (`main.tf`)

```hcl
resource "random_pet" "env_names" {
  for_each = toset(var.environments)
  prefix   = local.resource_prefix
  length   = 2
}

resource "random_string" "tokens" {
  for_each = toset(var.environments)
  length   = 16
  special  = false
  upper    = false
}
```

**Concepts covered:** Objective 4 — `for_each`, `toset()`, `each.key`/`each.value`.

---

## Task 5 — Child Module Definition (`modules/config-file/`)

### `modules/config-file/variables.tf`

```hcl
variable "environment" {
  type = string
}

variable "config_data" {
  type = object({
    env_name     = string
    team         = string
    replicas     = number
    log_level    = string
    feature_flag = bool
  })
}

variable "output_dir" {
  type    = string
  default = "./generated"
}
```

### `modules/config-file/main.tf`

```hcl
resource "local_file" "config" {
  filename = "${var.output_dir}/${var.environment}-config.json"
  content  = jsonencode(var.config_data)
}
```

### `modules/config-file/outputs.tf`

```hcl
output "file_path" {
  value = local_file.config.filename
}

output "file_id" {
  value = local_file.config.id
}
```

**Concepts covered:** Objective 5 — module structure (main, variables, outputs), `local_file` resource, `jsonencode`.

---

## Task 6 — Module Call (`main.tf` continued)

```hcl
module "config_files" {
  for_each = toset(var.environments)
  source   = "./modules/config-file"

  environment = each.key
  config_data = {
    env_name     = random_pet.env_names[each.key].id
    team         = var.team_name
    replicas     = local.env_config[each.key].replicas
    log_level    = local.env_config[each.key].log_level
    feature_flag = local.env_config[each.key].feature_flag
  }
}
```

**Concepts covered:** Objective 5 — calling a module, `for_each` on a module, passing variables, module outputs via `module.<name>.<output>`.

---

## Task 7 — Outputs with Sensitive Data (`outputs.tf`)

```hcl
output "environment_names" {
  description = "Generated pet names for each environment"
  value       = {for k, v in random_pet.env_names : k => v.id}
}

output "config_file_paths" {
  description = "Paths of generated config files"
  value       = {for k, v in module.config_files : k => v.file_path}
}

output "api_tokens" {
  description = "Generated API tokens (sensitive)"
  value       = {for k, v in random_string.tokens : k => v.result}
  sensitive   = true
}
```

**Concepts covered:** Objective 4 — `for` expression in outputs, `sensitive = true`.

---

## Task 8 — Validation and `check` Block

```hcl
# Variable validation already in variables.tf

check "prod_ha_check" {
  assert {
    condition     = !var.enable_high_availability || contains(var.environments, "production")
    error_message = "High availability should only be enabled for configurations that include production."
  }
}
```

**Concepts covered:** Objective 4 — `check` block (warning only, does NOT fail apply).

---

## Task 9 — Lifecycle Rules (`main.tf` continued)

```hcl
resource "random_password" "master_key" {
  length  = 32
  special = true

  lifecycle {
    prevent_destroy = true
    ignore_changes  = [special]
  }
}
```

**Concepts covered:** Objective 4 — `lifecycle` block, `prevent_destroy`, `ignore_changes`.

---

## Task 10 — Backend Configuration (`versions.tf` addition)

```hcl
terraform {
  # Add to existing terraform block
  backend "s3" {
    bucket         = "my-company-tfstate"
    key            = "capstone/terraform.tfstate"
    region         = "us-east-1"
    dynamodb_table = "terraform-state-lock"
    encrypt        = true
  }
}
```

**Note:** For the capstone exercise without AWS credentials, comment out the backend block and use local state. Include it to demonstrate the configuration syntax.

**Concepts covered:** Objective 6 — backend configuration, S3 state, DynamoDB locking.

---

## Task 11 — `moved` Block

After initially creating `random_pet.env_names`, if you rename it:

```hcl
# In main.tf: rename resource
resource "random_pet" "environment_pet_names" {   # renamed from env_names
  for_each = toset(var.environments)
  prefix   = local.resource_prefix
  length   = 2
}

# Add moved block
moved {
  from = random_pet.env_names
  to   = random_pet.environment_pet_names
}
```

**Concepts covered:** Objective 6 / Objective 7 — `moved` block for state migration without destroy+recreate.

---

## Task 12 — Full Workflow

```bash
# 1. Initialise
cd capstone
terraform init

# 2. Format
terraform fmt -recursive

# 3. Validate
terraform validate
# Output: Success! The configuration is valid.

# 4. Plan
terraform plan
# Review: what will be created/changed

# 5. Apply
terraform apply
# Type 'yes' to confirm

# 6. Inspect outputs
terraform output environment_names
terraform output config_file_paths
terraform output -json | jq .   # machine-readable

# 7. Inspect state
terraform state list
terraform state show random_pet.environment_pet_names[\"dev\"]

# 8. Test refresh-only (detect drift)
terraform plan -refresh-only

# 9. Clean up
terraform destroy
# Note: random_password.master_key has prevent_destroy = true — must remove lifecycle block first
```

**Concepts covered:** Objective 3 — full workflow (init, fmt, validate, plan, apply, output, destroy).

---

## Objectives Coverage Matrix

| Objective | Tasks |
|-----------|-------|
| 1 — IaC Concepts | Overall project structure and state management |
| 2 — Fundamentals | Task 1 (providers, version constraints) |
| 3 — Workflow | Task 12 (init, fmt, validate, plan, apply, destroy) |
| 4 — Configuration | Tasks 2, 3, 4, 7, 8, 9 (variables, locals, for_each, outputs, check, lifecycle) |
| 5 — Modules | Tasks 5, 6 (child module, module call, inputs/outputs) |
| 6 — State | Tasks 10, 11 (backend config, moved block) |
| 7 — Import/Logging | Task 11 (moved block); TF_LOG during debugging |
| 8 — HCP Terraform | Task 10 (backend design aligns with HCP Terraform migration path) |

---

## Exam Checklist

- [ ] Know the `~>` constraint operator (pessimistic)
- [ ] Know `validation` block can only reference `var.<name>`
- [ ] Know `for_each` with `toset()` for list-to-set conversion
- [ ] Know module call with `for_each` requires separate instances
- [ ] Know `sensitive = true` on outputs masks value but not in state
- [ ] Know `check` block produces warnings only
- [ ] Know `lifecycle { prevent_destroy = true }` blocks terraform destroy
- [ ] Know `moved` block renames resource without destroy+recreate
- [ ] Know S3 backend requires DynamoDB for locking
- [ ] Know full workflow order: init → fmt → validate → plan → apply → destroy

---

[⬅️ prompt16-practice-exam-questions-all-objectives.md](prompt16-practice-exam-questions-all-objectives.md) | **17 / 17** | [Next ➡️ README.md](README.md)
