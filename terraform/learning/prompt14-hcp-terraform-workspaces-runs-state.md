# HCP Terraform — Workspaces, Runs, and State

> **HashiCorp Certified Terraform Associate (004)**
> Objective 8 — Understand HCP Terraform's Capabilities

---

## Overview

HCP Terraform (formerly Terraform Cloud) is a SaaS platform that extends Terraform with remote execution, remote state, collaboration features, and governance. It is the **second-largest exam topic** after core configuration (Objective 4).

---

## HCP Terraform vs Community Edition

| Feature | Community Edition | HCP Terraform |
|---------|------------------|---------------|
| State storage | Local or self-managed | Remote, encrypted, versioned |
| State locking | Backend-dependent | Native built-in |
| Remote execution | No | Yes (managed workers) |
| Team access controls | No | RBAC with roles |
| Policy enforcement | No | Sentinel and OPA |
| Audit logging | No | Yes |
| VCS integration | No | GitHub, GitLab, Bitbucket, Azure DevOps |
| Private module registry | No | Yes |
| Cost estimation | No | Yes |

---

## Workspaces

Each workspace in HCP Terraform is an isolated environment with:
- Its own **state** (versioned, encrypted)
- Its own **variables** (Terraform variables and environment variables)
- Its own **run history**
- Its own **access permissions**

```
Organisation
├── Project: Infrastructure
│   ├── Workspace: production
│   ├── Workspace: staging
│   └── Workspace: development
└── Project: Applications
    ├── Workspace: api-prod
    └── Workspace: api-dev
```

---

## Connecting to HCP Terraform

### `cloud` Block (Terraform 1.1+ — Preferred)

```hcl
terraform {
  cloud {
    organization = "my-organisation"

    workspaces {
      name = "production"   # single workspace by name
    }
  }
}
```

```hcl
terraform {
  cloud {
    organization = "my-organisation"

    workspaces {
      tags = ["app=api", "env=prod"]   # select workspaces by tags
    }
  }
}
```

### Legacy `backend "remote"` (Still valid but use `cloud` block)

```hcl
terraform {
  backend "remote" {
    organization = "my-organisation"
    workspaces {
      name = "production"
    }
  }
}
```

---

## Authentication

```bash
terraform login              # Prompts browser login, stores token in:
                             # ~/.terraform.d/credentials.tfrc.json
terraform logout             # Removes stored credentials
```

### Environment Variable Alternative

```bash
export TF_TOKEN_app_terraform_io="your-api-token"
```

---

## Migrating State to HCP Terraform

```bash
terraform init -migrate-state
```

Terraform will detect the new `cloud` block and prompt to migrate local state.

---

## Run Lifecycle

```
Triggered → Planning → Plan pending review → Applying → Applied
```

### Run Types

| Type | Description |
|------|-------------|
| Speculative plan | No apply; triggered by API or VCS PR — shows what would change |
| Plan-only | Plans but requires manual apply approval |
| Plan-and-apply | Plans then applies (auto-approve or manual confirmation) |

---

## VCS-Driven Workflow

1. Connect workspace to GitHub/GitLab repository
2. On pull request → speculative plan runs automatically
3. Merge to main branch → plan-and-apply run triggered

---

## Variables

### Workspace Variables

Set in workspace settings UI or via API. Overrides all other variable sources.

### Variable Sets

```
Variable Set "AWS Credentials"
├── AWS_ACCESS_KEY_ID = (env var)
└── AWS_SECRET_ACCESS_KEY = (env var, sensitive)

Assigned to: production, staging, development workspaces
```

Variable sets are reusable collections of variables that can be assigned to multiple workspaces or an entire organisation.

---

## Remote State Data Source

Read outputs from another workspace's state:

```hcl
data "terraform_remote_state" "networking" {
  backend = "remote"
  config = {
    organization = "my-org"
    workspaces = {
      name = "networking-production"
    }
  }
}

resource "aws_instance" "app" {
  subnet_id = data.terraform_remote_state.networking.outputs.public_subnet_id
}
```

---

## Run Triggers

Workspace B can be configured to automatically trigger a run when Workspace A completes an apply.

```
Workspace A (networking) → apply → triggers → Workspace B (compute)
```

---

## Projects

Projects group workspaces for organisational purposes.

```
Organisation
└── Project: Platform Engineering
    ├── Workspace: vpc-prod
    ├── Workspace: vpc-staging
    └── Workspace: shared-services
```

---

## Exam Checklist

- [ ] Know `cloud` block is preferred over `backend "remote"` (Terraform 1.1+)
- [ ] Know `cloud` block requires `organization` and `workspaces` arguments
- [ ] Know `terraform login` stores token in `~/.terraform.d/credentials.tfrc.json`
- [ ] Know `terraform init -migrate-state` to migrate to HCP Terraform
- [ ] Know run lifecycle: Triggered → Planning → Plan pending review → Applying → Applied
- [ ] Know 3 run types: speculative, plan-only, plan-and-apply
- [ ] Know VCS-driven workflow: PR = speculative plan, merge = apply
- [ ] Know variable sets: reusable variable collections for multiple workspaces
- [ ] Know `terraform_remote_state` data source reads outputs from another workspace
- [ ] Know run triggers: workspace B triggered when workspace A apply completes
- [ ] Know projects group workspaces organisationally

---

[⬅️ prompt13-importing-infrastructure-state-inspection.md](prompt13-importing-infrastructure-state-inspection.md) | **14 / 17** | [Next ➡️ prompt15-hcp-terraform-governance-security-advanced.md](prompt15-hcp-terraform-governance-security-advanced.md)
