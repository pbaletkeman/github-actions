# Resource and Data Blocks

> **HashiCorp Certified Terraform Associate (004)**
> Objective 4 — Use Terraform Outside of Core Workflow

---

## Overview

Resources define infrastructure to create and manage. Data sources query existing infrastructure in read-only mode. Both are fundamental building blocks of every Terraform configuration.

---

## Resource Blocks

### Syntax

```hcl
resource "<PROVIDER_TYPE>" "<LOCAL_NAME>" {
  # configuration arguments
}
```

- `<PROVIDER_TYPE>` — e.g., `aws_instance`, `azurerm_resource_group`, `google_compute_instance`
- `<LOCAL_NAME>` — your name for this resource within the module (used in references)

### Resource Address

Within a module: `<TYPE>.<NAME>` (e.g., `aws_instance.web`)
From root module: `module.<module_name>.<TYPE>.<NAME>`

### Example

```hcl
resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"

  tags = {
    Name = "main-vpc"
  }
}

resource "aws_subnet" "public" {
  vpc_id     = aws_vpc.main.id   # implicit dependency on aws_vpc.main
  cidr_block = "10.0.1.0/24"
}
```

---

## Plan Symbols

| Symbol | Meaning |
|--------|---------|
| `+` | Resource will be **created** |
| `~` | Resource will be **updated in-place** |
| `-/+` | Resource will be **replaced** (destroyed then created) |
| `-` | Resource will be **destroyed** |

### Forcing Replacement

```bash
terraform apply -replace="aws_instance.web"
```

---

## Data Blocks (Data Sources)

Data sources query existing resources in read-only mode. They do NOT create or manage anything.

### Syntax

```hcl
data "<TYPE>" "<NAME>" {
  # filter/query arguments
}
```

### Reference Format

```hcl
data.<TYPE>.<NAME>.<ATTRIBUTE>
```

### Example

```hcl
# Look up the latest Ubuntu 22.04 AMI
data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"]  # Canonical

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"]
  }
}

resource "aws_instance" "web" {
  ami           = data.aws_ami.ubuntu.id   # reference the data source result
  instance_type = "t3.micro"
}
```

### When Data Sources Are Read

- Usually during **plan** phase
- If the result depends on a computed value (not known until apply), data source may be read during **apply**

---

## Meta-Arguments

Meta-arguments are special arguments available on all resource blocks (not provider-specific).

| Meta-argument | Purpose |
|--------------|---------|
| `count` | Create multiple instances of a resource |
| `for_each` | Create one instance per map key or set element |
| `depends_on` | Explicit dependency that Terraform can't auto-detect |
| `provider` | Specify which provider configuration to use (for aliases) |
| `lifecycle` | Control replace/destroy/update behaviour |

---

## Lifecycle Meta-Arguments

The `lifecycle` block is nested inside a resource and controls resource behaviour.

### `create_before_destroy`

Create the replacement before destroying the old resource. Avoids downtime.

```hcl
resource "aws_instance" "web" {
  ami           = data.aws_ami.ubuntu.id
  instance_type = "t3.micro"

  lifecycle {
    create_before_destroy = true
  }
}
```

### `prevent_destroy`

Terraform will return an error if any plan attempts to destroy this resource.

```hcl
resource "aws_db_instance" "production" {
  # ... config

  lifecycle {
    prevent_destroy = true   # guards against accidental deletion
  }
}
```

### `ignore_changes`

Terraform ignores drift for the listed attributes. Useful for resources that auto-manage their own attributes (e.g., Auto Scaling Groups, resources modified outside Terraform).

```hcl
resource "aws_instance" "web" {
  ami           = "ami-0abc123"
  instance_type = "t3.micro"
  tags          = { Name = "web" }

  lifecycle {
    ignore_changes = [
      ami,                  # ignore AMI changes (managed externally)
      tags["LastModified"], # ignore specific tag
    ]
  }
}
```

Use `ignore_changes = all` to ignore all attribute changes.

### `replace_triggered_by`

Force the resource to be replaced when any referenced resource or attribute changes.

```hcl
resource "aws_launch_template" "web" {
  # ...
}

resource "aws_autoscaling_group" "web" {
  # ...

  lifecycle {
    replace_triggered_by = [
      aws_launch_template.web  # replace ASG when launch template changes
    ]
  }
}
```

---

## Resource Dependencies (Review)

Terraform automatically creates a dependency graph based on references:

```hcl
resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"
}

resource "aws_subnet" "public" {
  vpc_id = aws_vpc.main.id   # ← implicit dependency: subnet depends on vpc
}
```

Use `depends_on` for dependencies Terraform cannot detect through attribute references:

```hcl
resource "aws_instance" "app" {
  # ...

  depends_on = [
    aws_iam_role_policy_attachment.s3_access  # IAM policy must be attached first
  ]
}
```

---

## Exam Checklist

- [ ] Know resource block syntax: `resource "<TYPE>" "<NAME>"`
- [ ] Know resource address format: `<TYPE>.<NAME>` within a module
- [ ] Know plan symbols: `+` create, `~` update, `-/+` replace, `-` destroy
- [ ] Know `terraform apply -replace=<addr>` forces replacement
- [ ] Know data block syntax: `data "<TYPE>" "<NAME>"`
- [ ] Know data source reference: `data.<TYPE>.<NAME>.<ATTRIBUTE>`
- [ ] Know data sources are read-only — they query existing resources
- [ ] Know all 5 meta-arguments: count, for_each, depends_on, provider, lifecycle
- [ ] Know all 4 lifecycle meta-arguments:
  - `create_before_destroy` — avoids downtime on replacement
  - `prevent_destroy` — blocks accidental deletion
  - `ignore_changes` — ignore specific attribute drift
  - `replace_triggered_by` — force replace when dependency changes

---

[⬅️ prompt04-core-workflow-cli.md](prompt04-core-workflow-cli.md) | **5 / 17** | [Next ➡️ prompt06-variables-locals-outputs.md](prompt06-variables-locals-outputs.md)
