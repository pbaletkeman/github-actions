# Terraform State: Purpose and Management

> **HashiCorp Certified Terraform Associate (004)**
> Objective 6 — Navigate Terraform Workflow

---

## Overview

Terraform state is the mechanism by which Terraform tracks the real-world resources it manages. Without state, Terraform cannot determine what exists, what has changed, or what needs to be created, updated, or deleted.

---

## What Is Terraform State?

`terraform.tfstate` is a JSON file that maps your HCL configuration to real-world resource IDs and attributes.

```json
{
  "version": 4,
  "resources": [
    {
      "type": "aws_instance",
      "name": "web",
      "instances": [
        {
          "attributes": {
            "id": "i-0abcd1234ef567890",
            "ami": "ami-0abc123",
            "instance_type": "t3.micro",
            "public_ip": "54.1.2.3"
          }
        }
      ]
    }
  ]
}
```

---

## Why State Is Required

| Purpose | Description |
|---------|-------------|
| **Resource identity mapping** | Maps `aws_instance.web` → `i-0abcd1234ef567890` |
| **Computing diffs (plan)** | Compares desired (config) vs known (state) vs actual (live API) |
| **Metadata tracking** | Dependencies, resource ordering, provider information |
| **Performance** | Cached attribute values — avoid querying every resource on every plan |

---

## Three Sources of Truth

When Terraform runs `plan`, it compares three things:

1. **Desired state** — your `.tf` configuration files
2. **Known state** — `terraform.tfstate` (Terraform's record of last-known resource state)
3. **Actual state** — live cloud resources (refreshed via API calls)

---

## State Files

| File | Description |
|------|-------------|
| `terraform.tfstate` | Current state |
| `terraform.tfstate.backup` | Previous state (auto-created before each apply; only **one** backup kept) |

---

## Local vs Remote State

| | Local State | Remote State |
|--|-------------|-------------|
| **Location** | `terraform.tfstate` in working dir | S3, Azure Blob, GCS, HCP Terraform, etc. |
| **Locking** | ❌ No locking | ✅ Locking prevents concurrent applies |
| **Sharing** | ❌ Must share file manually | ✅ All team members access same state |
| **Encryption** | ❌ Plain file on disk | ✅ Can encrypt at rest |
| **Versioning** | ❌ Only one backup | ✅ Can enable versioning (S3, GCS) |
| **Use case** | Solo development / learning | **All team/production work** |

---

## The Golden Rule: Never Manually Edit State

Manually editing `terraform.tfstate` can corrupt it and cause Terraform to behave unpredictably. Always use the `terraform state` commands.

---

## State Inspection Commands

```bash
# List all resources in state
terraform state list

# Show all attributes of a specific resource
terraform state show aws_instance.web

# Show entire state (or a saved plan)
terraform show

# Show state as JSON
terraform show -json
```

---

## State Manipulation Commands

```bash
# Move/rename a resource in state (rename without destroy+recreate)
terraform state mv aws_instance.old_name aws_instance.new_name

# Remove a resource from state (keeps it in cloud — stops Terraform managing it)
terraform state rm aws_instance.web

# Download current remote state to stdout
terraform state pull

# Upload local state to remote backend (⚠️ dangerous — use with caution)
terraform state push
```

---

## Sensitive Data in State

- **All resource attributes are stored in state in plaintext**, regardless of whether a variable or output has `sensitive = true`
- `sensitive = true` only masks output in the terminal — it does NOT encrypt state
- **Always use an encrypted remote backend** (e.g., S3 with server-side encryption) for production

```hcl
# sensitive = true masks terminal output only
output "db_password" {
  value     = aws_db_instance.main.password
  sensitive = true   # HIDDEN in terminal — still in state in plaintext
}
```

---

## Plan with No Refresh

```bash
# Use cached state without making API calls (faster but potentially stale)
terraform plan -refresh=false
```

Use `-refresh=false` when you know the state is accurate and want to speed up planning in large environments.

---

## Exam Checklist

- [ ] Know `terraform.tfstate` maps HCL config → real-world resource IDs
- [ ] Know the 4 purposes of state: identity, diffs, metadata, performance
- [ ] Know `terraform.tfstate.backup` = PREVIOUS state (only one backup kept)
- [ ] Know state stores 3 sources: desired (config), known (state), actual (live)
- [ ] Know local state has NO locking, NO sharing, NO encryption
- [ ] Know **NEVER manually edit state** — use `terraform state` commands
- [ ] Know `sensitive = true` masks terminal output but **data is still in state**
- [ ] Know all `terraform state` commands: list, show, mv, rm, pull, push
- [ ] Know `-refresh=false` uses cached state without API calls

---

[⬅️ prompt02-providers-plugin-model.md](prompt02-providers-plugin-model.md) | **3 / 17** | [Next ➡️ prompt04-core-workflow-cli.md](prompt04-core-workflow-cli.md)
