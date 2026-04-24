# Importing Infrastructure and State Inspection

> **HashiCorp Certified Terraform Associate (004)**
> Objective 7 — Implement and Maintain State

---

## Overview

Importing allows existing cloud resources (created manually, by other tools, or before Terraform adoption) to be brought under Terraform management. State inspection tools help debug and verify infrastructure.

---

## Why Import?

You need to import when:
- A resource was created manually in the cloud console
- A resource was created by a different tool (CloudFormation, scripts)
- You are adopting Terraform for an existing infrastructure

---

## Import Block (Terraform 1.5+ — Preferred)

```hcl
import {
  to = aws_instance.web
  id = "i-0abcd1234ef567890"    # cloud resource ID
}
```

### With Config Generation

```bash
terraform plan -generate-config-out=generated.tf
```

Terraform will generate the HCL configuration for the imported resource. Review and adjust the generated file.

### Workflow

1. Add `import` block to configuration
2. Run `terraform plan -generate-config-out=generated.tf` to generate/validate HCL
3. Review generated configuration
4. Run `terraform apply` to execute the import
5. Remove the `import` block (or keep for documentation)
6. Run `terraform plan` to verify no changes needed

### Advantages over CLI import

- Preview with `terraform plan` before applying
- Can generate configuration automatically
- Declarative — part of your configuration files

---

## CLI Import (Legacy)

```bash
terraform import aws_instance.web i-0abcd1234ef567890
```

**Requirements:**
- Resource block must already exist in your `.tf` files
- Only adds resource to state — does not generate HCL

```hcl
# Must exist in configuration BEFORE running terraform import
resource "aws_instance" "web" {
  # attributes to fill in after import
}
```

**Workflow:**
1. Write the resource block in configuration
2. Run `terraform import <address> <id>`
3. Run `terraform state show <address>` to see all attributes
4. Update configuration to match actual resource
5. Run `terraform plan` to verify no changes

---

## Resource ID Format

Each resource type uses its own format for the ID:

| Resource | ID Format | Example |
|----------|-----------|---------|
| `aws_instance` | Instance ID | `i-0abc123def456789` |
| `aws_s3_bucket` | Bucket name | `my-bucket-name` |
| `aws_vpc` | VPC ID | `vpc-0abc123` |
| `aws_security_group` | Security group ID | `sg-0abc123` |

Refer to each provider's documentation for the correct ID format.

---

## Logging and Debugging

### `TF_LOG` Environment Variable

```bash
# Set log level
export TF_LOG=DEBUG
terraform apply

# Available levels (most to least verbose)
# TRACE > DEBUG > INFO > WARN > ERROR > OFF
```

| Level | Description |
|-------|-------------|
| `TRACE` | Most verbose — includes all API calls and responses |
| `DEBUG` | Detailed debugging information |
| `INFO` | General operational messages |
| `WARN` | Warning conditions |
| `ERROR` | Error conditions only |
| `OFF` | Disable logging |

### `TF_LOG_PATH` — Write Logs to File

```bash
export TF_LOG=DEBUG
export TF_LOG_PATH=/path/to/terraform.log
terraform apply
```

### Separate Core vs Provider Logging

```bash
export TF_LOG_CORE=DEBUG      # Terraform core only
export TF_LOG_PROVIDER=TRACE  # Provider plugins only
```

---

## State Inspection Commands

### List All Resources

```bash
terraform state list
aws_instance.web
aws_s3_bucket.assets
module.vpc.aws_vpc.main
module.vpc.aws_subnet.public[0]
```

### Show Resource Details

```bash
terraform state show aws_instance.web
# id                           = "i-0abc123"
# instance_type                = "t3.micro"
# public_ip                    = "54.1.2.3"
# ...
```

### Show Full State

```bash
terraform show              # human-readable
terraform show -json        # machine-readable JSON
```

### Show Saved Plan

```bash
terraform plan -out=plan.tfplan
terraform show plan.tfplan
```

---

## Full Import Example

```hcl
# Step 1: Add import block
import {
  to = aws_s3_bucket.legacy_assets
  id = "my-existing-bucket-2024"
}

# Step 2: Add resource block (or let generate-config-out create it)
resource "aws_s3_bucket" "legacy_assets" {
  bucket = "my-existing-bucket-2024"
}
```

```bash
# Step 3: Plan with config generation (if needed)
terraform plan -generate-config-out=generated.tf

# Step 4: Apply the import
terraform apply

# Step 5: Verify clean state
terraform plan
# Output: No changes. Your infrastructure matches the configuration.
```

---

## Exam Checklist

- [ ] Know `import` block is preferred (Terraform 1.5+) over CLI import
- [ ] Know `import` block: `to = resource.name`, `id = "cloud-resource-id"`
- [ ] Know `terraform plan -generate-config-out=file.tf` generates HCL for imported resource
- [ ] Know CLI import requires resource block to exist first; only adds to state
- [ ] Know `TF_LOG` levels: TRACE, DEBUG, INFO, WARN, ERROR, OFF (most to least verbose)
- [ ] Know `TF_LOG_PATH` writes logs to a file
- [ ] Know `TF_LOG_CORE` and `TF_LOG_PROVIDER` for separate logging
- [ ] Know `terraform state list` — list all resources
- [ ] Know `terraform state show <address>` — show resource attributes
- [ ] Know `terraform show -json` — machine-readable state

---

[⬅️ prompt12-state-backends-locking-remote-state.md](prompt12-state-backends-locking-remote-state.md) | **13 / 17** | [Next ➡️ prompt14-hcp-terraform-workspaces-runs-state.md](prompt14-hcp-terraform-workspaces-runs-state.md)
