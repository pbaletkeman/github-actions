# Variables, Locals, and Outputs

> **HashiCorp Certified Terraform Associate (004)**
> Objective 4 — Use Terraform Outside of Core Workflow

---

## Overview

Variables make configurations reusable and parameterisable. Locals compute intermediate values. Outputs expose values to callers or for inspection after apply.

---

## Input Variables

### Syntax

```hcl
variable "region" {
  type        = string
  default     = "us-east-1"
  description = "AWS region to deploy resources in"
  sensitive   = false
  nullable    = false
}
```

### Variable Block Arguments

| Argument | Description |
|----------|-------------|
| `type` | Type constraint (optional but recommended) |
| `default` | Default value (makes variable optional) |
| `description` | Human-readable description |
| `sensitive` | Mask value in terminal output |
| `nullable` | Whether `null` is allowed (default: `true`) |
| `validation` | Custom validation rule |

### Type Constraints

| Type | Example |
|------|---------|
| `string` | `"us-east-1"` |
| `number` | `3` |
| `bool` | `true` |
| `list(string)` | `["a", "b", "c"]` |
| `map(string)` | `{ key = "value" }` |
| `set(string)` | `toset(["a", "b"])` |
| `object({name=string, port=number})` | Structured object |
| `tuple([string, number, bool])` | Fixed-length mixed list |
| `any` | No type checking |

---

## Variable Input Precedence

Highest priority wins. Listed highest to lowest:

1. **CLI `-var` flag**: `terraform apply -var "region=eu-west-1"`
2. **`TF_VAR_name` environment variable**: `export TF_VAR_region=eu-west-1`
3. **`*.auto.tfvars` files**: Automatically loaded, any filename
4. **`terraform.tfvars`**: Automatically loaded (specific filename)
5. **`-var-file` flag**: `terraform apply -var-file=prod.tfvars`
6. **`default` in variable block**: Used when no other value provided

> **Note**: CLI `-var` and `TF_VAR_*` environment variables are at the same highest precedence level.

### Env Var Format

```bash
export TF_VAR_region="us-east-1"
export TF_VAR_instance_count=5
```

### `.tfvars` File Format

```hcl
region         = "us-east-1"
instance_count = 3
tags = {
  Environment = "production"
  Team        = "platform"
}
```

---

## Sensitive Variables

```hcl
variable "db_password" {
  type      = string
  sensitive = true
}
```

- Value is **hidden in `terraform plan` and `terraform apply` terminal output**
- Value is **still stored in plaintext in `terraform.tfstate`**
- Always use an encrypted remote backend when handling sensitive data

---

## Validation Blocks

```hcl
variable "environment" {
  type        = string
  description = "Deployment environment"

  validation {
    condition     = contains(["dev", "staging", "production"], var.environment)
    error_message = "environment must be one of: dev, staging, production"
  }
}
```

Rules:
- `condition` must be a boolean expression
- `condition` can **only reference `var.<name>`** (no other resources, locals, or data sources)
- Validation runs before planning — invalid variable fails early

---

## Locals

Locals define computed values for reuse within a module. They are not inputs and not outputs.

```hcl
locals {
  app_name    = "my-app"
  environment = var.environment
  full_name   = "${local.app_name}-${local.environment}"  # computed

  common_tags = {
    App         = local.app_name
    Environment = local.environment
    ManagedBy   = "terraform"
  }
}
```

### Referencing Locals

```hcl
resource "aws_instance" "web" {
  tags = local.common_tags
}
```

### Locals vs Variables

| Aspect | Variables (`var.*`) | Locals (`local.*`) |
|--------|--------------------|--------------------|
| Purpose | External inputs | Internal computed values |
| Can be set by user? | ✅ Yes | ❌ No |
| Can reference other resources? | ❌ No (in validation) | ✅ Yes |
| Appears in module interface? | ✅ Yes | ❌ No |

---

## Output Values

Outputs expose values after apply and enable module composition.

```hcl
output "instance_ip" {
  value       = aws_instance.web.public_ip
  description = "Public IP address of the web server"
  sensitive   = false
}

output "db_password" {
  value     = aws_db_instance.main.password
  sensitive = true   # hidden in terminal
}
```

### Viewing Outputs

```bash
terraform output                      # all outputs
terraform output instance_ip          # specific output
terraform output -json                # all outputs as JSON
terraform output -raw db_connection   # raw string (no quotes)
```

### Module Outputs

A parent module accesses child module outputs with:

```hcl
module "network" {
  source = "./modules/network"
  # ...
}

resource "aws_instance" "web" {
  subnet_id = module.network.public_subnet_id   # module.<name>.<output_name>
}
```

---

## Practical Example

```hcl
# variables.tf
variable "instance_type" {
  type        = string
  default     = "t3.micro"
  description = "EC2 instance type"

  validation {
    condition     = startswith(var.instance_type, "t3.")
    error_message = "Only t3 family instances are allowed."
  }
}

# locals.tf
locals {
  name_prefix = "app-${var.environment}"
}

# main.tf
resource "aws_instance" "app" {
  instance_type = var.instance_type
  ami           = data.aws_ami.ubuntu.id

  tags = {
    Name = "${local.name_prefix}-server"
  }
}

# outputs.tf
output "app_public_ip" {
  value       = aws_instance.app.public_ip
  description = "Public IP of the app server"
}
```

---

## Exam Checklist

- [ ] Know variable block arguments: type, default, description, sensitive, validation, nullable
- [ ] Know all primitive types: string, number, bool
- [ ] Know all collection types: list(), map(), set(), object(), tuple(), any
- [ ] Know variable input precedence (highest to lowest)
- [ ] Know env var format: `TF_VAR_variable_name`
- [ ] Know `sensitive = true` masks terminal output but NOT state
- [ ] Know `validation` can only reference `var.<name>`
- [ ] Know locals syntax and reference: `local.<name>`
- [ ] Know locals vs variables: locals = internal computed; variables = external inputs
- [ ] Know output block syntax and `sensitive`, `description` arguments
- [ ] Know `terraform output -raw` for unquoted string
- [ ] Know module output reference: `module.<name>.<output_name>`

---

[⬅️ prompt05-resource-data-blocks.md](prompt05-resource-data-blocks.md) | **6 / 17** | [Next ➡️ prompt07-complex-types-collections.md](prompt07-complex-types-collections.md)
