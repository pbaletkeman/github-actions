# Terraform Providers and the Plugin Model

> **HashiCorp Certified Terraform Associate (004)**
> Objective 2 — Understand Terraform's Purpose (Terraform Basics)

---

## Overview

Providers are the bridge between Terraform's HCL configuration and a target cloud or service's API. Every resource in Terraform belongs to a provider.

---

## What Is a Provider?

A provider is a Terraform plugin that:

1. **Authenticates** to a cloud/service API
2. Implements **CRUD operations** for resources (create, read, update, delete)
3. Exposes **data sources** for reading existing resources
4. **Validates** resource configuration against a schema
5. **Maps** resource attributes to and from Terraform state

Examples: `hashicorp/aws`, `hashicorp/azurerm`, `hashicorp/google`, `hashicorp/kubernetes`, `hashicorp/vault`

---

## Plugin Architecture

```
Terraform Core (terraform binary)
        │
        │  gRPC (separate process)
        ▼
Provider Plugin (e.g., terraform-provider-aws)
        │
        │  HTTPS API calls
        ▼
Cloud / Service API (e.g., AWS EC2 API)
```

- The provider binary runs as a **separate process** from Terraform Core
- Communication between Core and provider uses **gRPC**
- This separation means providers can be upgraded independently of Terraform Core

---

## Declaring Required Providers

```hcl
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"       # <namespace>/<type>
      version = "~> 5.0"             # version constraint
    }
    random = {
      source  = "hashicorp/random"
      version = ">= 3.0"
    }
  }

  required_version = ">= 1.5"        # minimum Terraform Core version
}
```

**Source address format:** `<hostname>/<namespace>/<type>` (hostname defaults to `registry.terraform.io`).

---

## Provider Configuration Block

```hcl
provider "aws" {
  region  = "us-east-1"
  profile = "production"
}
```

The provider block configures authentication and defaults. Provider blocks are optional if environment variables or default configuration are sufficient.

---

## Provider Tiers

| Tier | Maintained By | Trust Level | Notes |
|------|-------------|-------------|-------|
| **Official** | HashiCorp | Highest | AWS, Azure, GCP, etc. |
| **Partner** | Technology partners | Verified | HashiCorp has reviewed/approved |
| **Community** | Individuals/organisations | Unverified | Use with care |

---

## Version Constraint Operators

| Operator | Meaning | Example | Allows |
|----------|---------|---------|--------|
| `=` | Exact version | `= 5.0.0` | Only 5.0.0 |
| `!=` | Not this version | `!= 5.0.0` | Any except 5.0.0 |
| `>` | Greater than | `> 5.0` | 5.1, 6.0, etc. |
| `>=` | Greater than or equal | `>= 5.0` | 5.0, 5.1, 6.0, etc. |
| `<` | Less than | `< 6.0` | Up to (not including) 6.0 |
| `<=` | Less than or equal | `<= 5.9` | Up to and including 5.9 |
| `~>` | Pessimistic constraint | `~> 5.0` | >= 5.0 and < 6.0 (minor+patch) |
| `~>` | Pessimistic constraint | `~> 5.0.0` | >= 5.0.0 and < 5.1.0 (patch only) |

**Key rule for `~>`:**
- `~> 5.0` allows minor and patch updates (stays in major version 5)
- `~> 5.0.0` allows only patch updates (stays in minor version 5.0)

---

## `terraform init`

`terraform init` downloads providers into `.terraform/providers/` and creates/updates the lock file.

```bash
terraform init                    # download required providers
terraform init -upgrade           # upgrade providers to latest matching constraint
terraform init -migrate-state     # migrate state when changing backends
```

---

## `.terraform.lock.hcl` — Dependency Lock File

```hcl
provider "registry.terraform.io/hashicorp/aws" {
  version     = "5.31.0"
  constraints = "~> 5.0"
  hashes = [
    "h1:abc123...",
    "zh:def456...",
  ]
}
```

| Property | Description |
|----------|-------------|
| `version` | Exact version that was installed |
| `constraints` | The constraint from `required_providers` |
| `hashes` | Cryptographic checksums for verification |

**Must be committed to version control** so all team members use the same provider versions.

To update to a newer version within constraints: `terraform init -upgrade`.

---

## Multiple Provider Configurations with Aliases

```hcl
# Primary provider (no alias needed)
provider "aws" {
  region = "us-east-1"
}

# Additional provider with alias
provider "aws" {
  alias  = "us-west"
  region = "us-west-2"
}

# Reference a specific provider configuration
resource "aws_instance" "west_server" {
  provider      = aws.us-west      # use aliased provider
  ami           = "ami-0xyz"
  instance_type = "t3.micro"
}
```

Aliases are required whenever you need more than one configuration of the same provider (e.g., multi-region, multi-account).

---

## Exam Checklist

- [ ] Know providers are plugins that bridge HCL ↔ cloud API
- [ ] Know the 5 provider responsibilities: auth, CRUD, data sources, validation, state mapping
- [ ] Know the plugin architecture: Core ↔ gRPC ↔ Provider binary ↔ API
- [ ] Know source address format: `namespace/type` (hostname defaults to registry.terraform.io)
- [ ] Know the 3 provider tiers: Official, Partner, Community
- [ ] Know all version constraint operators, especially `~>`
- [ ] Know `~> 5.0` = minor+patch; `~> 5.0.0` = patch only
- [ ] Know `terraform init` downloads providers to `.terraform/`
- [ ] Know `.terraform.lock.hcl` must be committed to VCS
- [ ] Know how to use provider `alias` for multi-region/multi-account configs

---

[⬅️ prompt01-what-is-iac.md](prompt01-what-is-iac.md) | **2 / 17** | [Next ➡️ prompt03-terraform-state.md](prompt03-terraform-state.md)
