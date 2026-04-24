# Custom Conditions and Sensitive Data

> **HashiCorp Certified Terraform Associate (004)**
> Objective 4 — Use Terraform Outside of Core Workflow

---

## Overview

Terraform provides three mechanisms for asserting conditions and one mechanism for marking data as sensitive. Understanding which mechanism fails the apply vs. warns-only is a key exam distinction.

---

## Three Condition Mechanisms

| Mechanism | Where | When It Runs | Failure Behaviour |
|-----------|-------|-------------|-------------------|
| `validation` block | `variable` block | Before plan | Fails plan |
| `precondition`/`postcondition` | `lifecycle` block | Pre/post resource change | Fails apply |
| `check` block | Top-level | Every plan+apply | **Warning only — does NOT fail** |

---

## 1. `validation` Block (Variable Validation)

Validates input variable values before planning.

```hcl
variable "environment" {
  type    = string
  default = "dev"

  validation {
    condition     = contains(["dev", "staging", "production"], var.environment)
    error_message = "environment must be one of: dev, staging, production."
  }
}

variable "instance_count" {
  type    = number

  validation {
    condition     = var.instance_count >= 1 && var.instance_count <= 10
    error_message = "instance_count must be between 1 and 10."
  }
}
```

**Rules:**
- Condition can only reference `var.<variable_name>` (not other resources or data sources)
- Multiple `validation` blocks allowed per variable
- Runs before `terraform plan` evaluates any infrastructure

---

## 2. `precondition` and `postcondition` (Lifecycle Conditions)

Assertions inside resource or data source `lifecycle` blocks.

```hcl
resource "aws_instance" "web" {
  ami           = data.aws_ami.ubuntu.id
  instance_type = var.instance_type

  lifecycle {
    precondition {
      condition     = data.aws_ami.ubuntu.architecture == "x86_64"
      error_message = "The selected AMI must be x86_64 architecture."
    }

    postcondition {
      condition     = self.public_ip != null
      error_message = "Instance must have a public IP address."
    }
  }
}
```

| | `precondition` | `postcondition` |
|--|---------------|----------------|
| Runs | Before resource is changed | After resource is changed |
| `self` reference | Not available | References the created/updated resource |
| Failure | Fails apply before change | Fails apply after change (resource exists) |

---

## 3. `check` Block (Infrastructure Assertions)

Top-level block for asserting infrastructure health. Does **not** fail the apply.

```hcl
check "health_check" {
  data "http" "endpoint" {
    url = "https://${aws_lb.web.dns_name}/health"
  }

  assert {
    condition     = data.http.endpoint.status_code == 200
    error_message = "Health endpoint returned ${data.http.endpoint.status_code}, expected 200."
  }
}
```

```hcl
check "database_accessible" {
  assert {
    condition     = can(tostring(aws_db_instance.main.endpoint))
    error_message = "Database endpoint is not accessible."
  }
}
```

**Key facts:**
- Introduced in Terraform 1.5+
- Runs on every `plan` and `apply`
- Failure is a **warning** — does not block the apply
- Can optionally include a scoped `data` source block
- Useful for post-deployment health validation

---

## Sensitive Data

### Marking Variables as Sensitive

```hcl
variable "db_password" {
  type      = string
  sensitive = true
}
```

### Marking Outputs as Sensitive

```hcl
output "db_password" {
  value     = var.db_password
  sensitive = true
}
```

### What `sensitive = true` Does

- **Masks** the value in terminal output (shows `(sensitive value)`)
- **Redacts** the value in plan output
- Does **NOT** encrypt the value in state

### The Critical Security Fact

```
sensitive = true masks values in the terminal but does NOT protect them in state.
Sensitive values are always stored in plaintext in terraform.tfstate.
```

**Protect state files by:**
- Using encrypted remote backends (S3 with SSE, HCP Terraform)
- Restricting access to state files
- Never committing `terraform.tfstate` to source control

---

## Dynamic Secrets with HashiCorp Vault

```hcl
terraform {
  required_providers {
    vault = {
      source  = "hashicorp/vault"
      version = "~> 3.0"
    }
  }
}

provider "vault" {
  address = "https://vault.example.com:8200"
}

data "vault_generic_secret" "db_creds" {
  path = "secret/database/prod"
}

resource "aws_db_instance" "main" {
  username = data.vault_generic_secret.db_creds.data["username"]
  password = data.vault_generic_secret.db_creds.data["password"]
}
```

Benefits:
- Secrets never stored in Terraform configuration
- Vault manages secret rotation
- Dynamic credentials with short TTL

---

## Exam Checklist

- [ ] Know the 3 condition mechanisms and their failure behaviours
- [ ] Know `validation` block: before plan, only references `var.<name>`, fails plan
- [ ] Know `precondition`: before resource change, fails apply
- [ ] Know `postcondition`: after resource change, `self` references the resource, fails apply
- [ ] Know `check` block: every plan+apply, **warning only, does NOT fail apply**
- [ ] Know `check` block introduced in Terraform 1.5+
- [ ] Know `sensitive = true` masks in terminal but does NOT encrypt in state
- [ ] Know state always stores sensitive values in plaintext
- [ ] Know the mitigation: use encrypted backends + restrict state access

---

[⬅️ prompt09-dependencies-lifecycle.md](prompt09-dependencies-lifecycle.md) | **10 / 17** | [Next ➡️ prompt11-terraform-modules.md](prompt11-terraform-modules.md)
