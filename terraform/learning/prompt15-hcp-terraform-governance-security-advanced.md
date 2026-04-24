# HCP Terraform — Governance, Security, and Advanced Features

> **HashiCorp Certified Terraform Associate (004)**
> Objective 8 — Understand HCP Terraform's Capabilities

---

## Overview

HCP Terraform provides enterprise-grade governance, security, and operational features that extend beyond community Terraform. This includes RBAC, policy enforcement, private registries, and compliance tooling.

---

## Roles and Access Control

### Organisation-Level Roles

| Role | Access |
|------|--------|
| **Owner** | Full access to all organisation settings, workspaces, teams |
| **Member** | Access defined at workspace level only |

### Workspace-Level Permissions

| Permission | Description |
|-----------|-------------|
| **Read** | View runs, state, variables |
| **Plan** | Trigger speculative plans |
| **Write** | Trigger and apply runs |
| **Admin** | Manage workspace settings, variables, team access |

### Team Tokens

A single API token can represent an entire team's access to all assigned workspaces. Used in CI/CD pipelines.

---

## Policy Enforcement

HCP Terraform supports two policy frameworks:

| Framework | Language | Managed By |
|-----------|----------|-----------|
| Sentinel | HashiCorp Sentinel DSL | HashiCorp |
| OPA | Rego | Open Policy Agent community |

### Policy Enforcement Levels

| Level | Behaviour |
|-------|-----------|
| `advisory` | Policy failure = warning only; run continues |
| `soft-mandatory` | Policy failure blocks run; can be overridden by authorised user |
| `hard-mandatory` | Policy failure blocks run; **cannot** be overridden |

### Policy Sets

Collections of policies assigned to workspaces or entire organisations.

```
Policy Set "Security Baseline"
├── Policy: deny-public-s3-buckets.sentinel
├── Policy: require-encryption.sentinel
└── Policy: enforce-tagging.sentinel

Assigned to: ALL workspaces in organisation
```

---

## Health Assessments

Scheduled drift detection:
- Runs `terraform plan -refresh-only` on a configurable schedule
- Detects if cloud resources have drifted from state
- Results visible in HCP Terraform UI with notifications

---

## Dynamic Provider Credentials (OIDC)

Workspaces can authenticate to cloud providers using OIDC (OpenID Connect) tokens instead of storing static credentials.

```
HCP Terraform Workspace
└── Requests short-lived token via OIDC
    └── AWS/Azure/GCP validates token against trusted OIDC provider
        └── Returns short-lived cloud credentials
```

**Benefits:**
- No static credentials stored in HCP Terraform
- Credentials automatically expire
- Full audit trail of what each run accessed

---

## Private Registry

HCP Terraform organisations can publish and version their own private modules.

```
Private Registry
├── modules/
│   ├── vpc/aws v2.1.0
│   ├── eks-cluster/aws v1.0.3
│   └── rds/aws v3.2.1
└── providers/
    └── internal-provider v0.5.0
```

### Using Private Registry Modules

```hcl
module "vpc" {
  source  = "app.terraform.io/my-org/vpc/aws"
  version = "~> 2.1"
}
```

Same syntax as public registry — `terraform init` handles authentication.

---

## Audit Logging

Every action in HCP Terraform is logged:
- Plans, applies, destroys
- Variable changes
- Team membership changes
- API calls

Properties:
- **Exportable** to external SIEM systems
- **Immutable** — cannot be modified
- Complete change history for compliance

---

## Cost Estimation

Before applying, HCP Terraform shows estimated monthly cost change:

```
Plan: 2 to add, 1 to change, 0 to destroy.

Cost estimation:
  + $24.00/month  (aws_instance.web)
  + $5.00/month   (aws_ebs_volume.data)
  ─────────────────────────────────────
  Total change: +$29.00/month
```

---

## Sentinel Policy Example

```python
# Require all S3 buckets to have encryption enabled
import "tfplan/v2" as tfplan

s3_buckets = filter tfplan.resource_changes as _, rc {
  rc.type is "aws_s3_bucket" and
  rc.change.actions is not ["delete"]
}

violating_buckets = filter s3_buckets as _, bucket {
  not (bucket.change.after.server_side_encryption_configuration else null) != null
}

main = rule {
  length(violating_buckets) == 0
}
```

---

## Exam Checklist

- [ ] Know two organisation roles: Owner (full) and Member (workspace-level)
- [ ] Know four workspace permissions: Read, Plan, Write, Admin
- [ ] Know team tokens: single token for entire team's workspace access
- [ ] Know two policy frameworks: Sentinel (HashiCorp) and OPA (Rego)
- [ ] Know three enforcement levels: advisory (warn), soft-mandatory (can override), hard-mandatory (cannot override)
- [ ] Know policy sets: collections of policies assigned to workspaces/organisation
- [ ] Know health assessments: scheduled drift detection via `plan -refresh-only`
- [ ] Know dynamic provider credentials use OIDC to avoid storing static credentials
- [ ] Know private registry: publish/version private modules; same syntax as public registry
- [ ] Know audit logging: every action logged, immutable, exportable

---

[⬅️ prompt14-hcp-terraform-workspaces-runs-state.md](prompt14-hcp-terraform-workspaces-runs-state.md) | **15 / 17** | [Next ➡️ prompt16-practice-exam-questions-all-objectives.md](prompt16-practice-exam-questions-all-objectives.md)
