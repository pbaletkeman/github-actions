# State Backends, Locking, and Remote State

> **HashiCorp Certified Terraform Associate (004)**
> Objective 6 — Navigate Terraform Workflow

---

## Overview

Terraform state is the source of truth for what infrastructure Terraform manages. Understanding backends, locking, and state manipulation commands is a large portion of the exam.

---

## Local Backend (Default)

By default, state is stored in the working directory.

```
terraform.tfstate        ← current state
terraform.tfstate.backup ← PREVIOUS state (not versioned history — only most recent)
```

**Limitations of local backend:**
- No collaboration — can't share with teammates
- No locking — concurrent applies can corrupt state
- No encryption — sensitive data in plaintext

---

## Backend Configuration

Backend is configured in the `terraform {}` block:

```hcl
terraform {
  backend "s3" {
    bucket         = "my-tf-state"
    key            = "prod/terraform.tfstate"
    region         = "us-east-1"
    dynamodb_table = "terraform-state-lock"
    encrypt        = true
    profile        = "aws-admin"
  }
}
```

### Changing Backends

```bash
terraform init -migrate-state   # migrate existing state to new backend
terraform init -reconfigure     # reconfigure without migrating
```

---

## State Locking

Prevents two concurrent operations from corrupting state.

| Backend | Locking Mechanism |
|---------|------------------|
| S3 | DynamoDB table with `LockID` attribute |
| Azure Blob Storage | Blob leases |
| GCS | Object locking |
| HCP Terraform | Native built-in locking |
| Local | OS file lock (unreliable) |

### DynamoDB Lock Table Setup (S3)

```hcl
resource "aws_dynamodb_table" "terraform_lock" {
  name         = "terraform-state-lock"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "LockID"            # ← attribute name must be "LockID"

  attribute {
    name = "LockID"
    type = "S"
  }
}
```

### Force Unlock

If a process dies while holding a lock:

```bash
terraform force-unlock LOCK_ID
```

**Warning:** Only use this if you're certain no other operation is running. The lock ID is shown in the error message when a lock is encountered.

---

## S3 Backend Full Example

```hcl
terraform {
  backend "s3" {
    bucket         = "my-company-tfstate"
    key            = "environments/production/terraform.tfstate"
    region         = "us-east-1"
    dynamodb_table = "terraform-state-lock"
    encrypt        = true
  }
}
```

---

## Resource Drift

Drift = when actual cloud resources differ from what's recorded in Terraform state (e.g., someone manually changed a resource in the console).

### Detect Drift Without Making Changes

```bash
terraform plan -refresh-only
```

Shows what changed in the cloud without proposing any infrastructure changes.

### Update State to Match Cloud (No Infrastructure Changes)

```bash
terraform apply -refresh-only
```

Updates state to reflect actual cloud state. Does not create, modify, or destroy resources.

---

## State Manipulation Commands

### `terraform state list`

```bash
terraform state list
# aws_instance.web
# aws_s3_bucket.assets
# module.vpc.aws_vpc.main
```

### `terraform state show`

```bash
terraform state show aws_instance.web
# Shows all attributes of the resource in state
```

### `terraform state mv`

Rename or move a resource in state without destroying it:

```bash
# Rename
terraform state mv aws_instance.web aws_instance.web_server

# Move into a module
terraform state mv aws_instance.web module.compute.aws_instance.server
```

### `terraform state rm`

Remove a resource from state (keeps it in the cloud — Terraform stops managing it):

```bash
terraform state rm aws_instance.legacy
```

After removing, if the resource is still in configuration, the next `plan` will show it as a new resource to create.

### `terraform state pull`

Download current remote state to stdout (for inspection or backup):

```bash
terraform state pull > backup.tfstate
```

### `terraform state push`

Upload a local state file to remote backend:

```bash
terraform state push backup.tfstate
```

**Warning:** Can overwrite remote state. Use with extreme caution.

---

## `terraform show`

```bash
terraform show              # human-readable current state
terraform show -json        # machine-readable JSON state
terraform show plan.tfplan  # show a saved plan file
```

---

## Exam Checklist

- [ ] Know local backend = `terraform.tfstate` in working directory, no locking/sharing
- [ ] Know `terraform.tfstate.backup` = PREVIOUS state (only most recent, not full history)
- [ ] Know `backend` block goes inside `terraform {}` block
- [ ] Know `terraform init -migrate-state` to change backends
- [ ] Know S3 locking uses DynamoDB with `LockID` attribute
- [ ] Know `terraform force-unlock <LOCK_ID>` for manual unlock
- [ ] Know `terraform plan -refresh-only` shows drift without infrastructure changes
- [ ] Know `terraform apply -refresh-only` updates state to match cloud (no infra changes)
- [ ] Know state commands: list, show, mv, rm, pull, push
- [ ] Know `terraform state rm` removes from state but keeps in cloud
- [ ] Know `terraform show -json` for machine-readable state

---

[⬅️ prompt11-terraform-modules.md](prompt11-terraform-modules.md) | **12 / 17** | [Next ➡️ prompt13-importing-infrastructure-state-inspection.md](prompt13-importing-infrastructure-state-inspection.md)
