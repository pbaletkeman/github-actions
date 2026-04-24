# Terraform Modules

> **HashiCorp Certified Terraform Associate (004)**
> Objective 5 — Interact with Terraform Modules

---

## Overview

Modules are the primary mechanism for code reuse and organisation in Terraform. Every Terraform configuration is a module; understanding module types, sources, inputs, and outputs is a major exam topic.

---

## What Is a Module?

Every Terraform configuration directory is a module. When you run `terraform apply` from a directory, you are executing the **root module**. Modules called from the root are **child modules**.

```
Root module (working directory)
├── main.tf
├── variables.tf
├── outputs.tf
└── modules/
    ├── networking/     ← child module
    │   ├── main.tf
    │   ├── variables.tf
    │   └── outputs.tf
    └── compute/        ← child module
        ├── main.tf
        ├── variables.tf
        └── outputs.tf
```

---

## Module Types

| Type | Description |
|------|-------------|
| **Root module** | The working directory where you run Terraform commands |
| **Child module** | A module called by another module (local or remote) |
| **Published module** | Module on the Terraform Registry or a private registry |

---

## Module Source Types

The `source` argument tells Terraform where to find the module.

### Local Path

```hcl
module "networking" {
  source = "./modules/networking"   # relative path
}
```

### Terraform Registry

```hcl
module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "~> 5.0"
}
```

Format: `<NAMESPACE>/<MODULE>/<PROVIDER>`

### GitHub

```hcl
# Entire repository
module "example" {
  source = "github.com/hashicorp/example"
}

# Specific subdirectory (note the double slash)
module "example" {
  source = "github.com/hashicorp/example//modules/vpc"
}
```

### Git (Generic)

```hcl
module "example" {
  source = "git::https://github.com/example/module.git?ref=v1.0.0"
}

module "example" {
  source = "git::ssh://git@github.com/example/module.git"
}
```

### Other Sources

```hcl
# HTTP archive
source = "https://example.com/modules/vpc.zip"

# S3 bucket
source = "s3::https://s3-us-west-2.amazonaws.com/mybucket/vpc.zip"

# GCS bucket
source = "gcs::https://www.googleapis.com/storage/v1/mybucket/vpc.zip"
```

---

## The Double-Slash `//` Convention

The double-slash `//` in a module source separates the **repository root** from a **subdirectory** within it. This is not a typo.

```
github.com/org/repo//modules/vpc
                   ──────────────
                   subdirectory path
```

---

## `version` Argument

Only valid for registry sources (Terraform Registry and private registries). Not valid for local paths or git URLs (use `?ref=` for git).

```hcl
module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "~> 5.0"    # ~> = compatible (>= 5.0, < 6.0)
}
```

---

## `terraform init` and Module Caching

`terraform init` downloads module source code to `.terraform/modules/`.

```
.terraform/
└── modules/
    ├── modules.json
    ├── vpc/          ← cached module source
    └── compute/      ← cached module source
```

Re-run `terraform init` when you add or change module sources.

---

## Passing Variables to Modules

Parent variables are **NOT inherited** by child modules. You must explicitly pass inputs.

```hcl
module "networking" {
  source = "./modules/networking"

  # Explicit input assignments
  vpc_cidr    = "10.0.0.0/16"
  environment = var.environment   # pass from root variable
  region      = "us-east-1"
}
```

---

## Accessing Module Outputs

```hcl
# In the child module outputs.tf
output "vpc_id" {
  value = aws_vpc.main.id
}

output "public_subnet_ids" {
  value = aws_subnet.public[*].id
}

# In the root module — reference as module.<name>.<output_name>
resource "aws_instance" "web" {
  subnet_id = module.networking.public_subnet_ids[0]
  vpc_security_group_ids = [aws_security_group.web.id]
}

output "vpc_id" {
  value = module.networking.vpc_id
}
```

---

## Standard Module Structure

```
module/
├── main.tf         # Core resources
├── variables.tf    # Input variable definitions
├── outputs.tf      # Output value definitions
├── versions.tf     # Required providers and version constraints
├── README.md       # Module documentation
└── examples/       # Usage examples
    └── simple/
        ├── main.tf
        └── README.md
```

---

## Module Composition

Modules can call other modules:

```
Root Module
├── calls module "networking"
│   └── calls module "subnet"  (nested child)
└── calls module "compute"
    └── calls module "autoscaling"  (nested child)
```

Each module is independent — variables must be explicitly passed at each level.

---

## Practical Example

### Child Module (`modules/web-server/main.tf`)

```hcl
resource "aws_instance" "this" {
  ami           = var.ami
  instance_type = var.instance_type

  tags = {
    Name        = var.name
    Environment = var.environment
  }
}
```

### Child Module (`modules/web-server/variables.tf`)

```hcl
variable "ami"           { type = string }
variable "instance_type" { type = string; default = "t3.micro" }
variable "name"          { type = string }
variable "environment"   { type = string }
```

### Child Module (`modules/web-server/outputs.tf`)

```hcl
output "instance_id"  { value = aws_instance.this.id }
output "public_ip"    { value = aws_instance.this.public_ip }
```

### Root Module (`main.tf`)

```hcl
module "web_server" {
  source = "./modules/web-server"

  ami         = data.aws_ami.ubuntu.id
  name        = "prod-web"
  environment = "production"
}

output "web_ip" {
  value = module.web_server.public_ip
}
```

---

## Exam Checklist

- [ ] Know every directory is a module; working directory = root module
- [ ] Know module source types: local path, registry, GitHub, git, HTTP, S3
- [ ] Know `//` in source = repo-to-subdirectory separator (not a typo)
- [ ] Know `version` argument only works for registry sources
- [ ] Know `terraform init` downloads modules to `.terraform/modules/`
- [ ] Know variables are NOT inherited — must be explicitly passed
- [ ] Know outputs accessed as `module.<name>.<output_name>`
- [ ] Know standard module files: main.tf, variables.tf, outputs.tf, versions.tf, README.md

---

[⬅️ prompt10-custom-conditions-sensitive-data.md](prompt10-custom-conditions-sensitive-data.md) | **11 / 17** | [Next ➡️ prompt12-state-backends-locking-remote-state.md](prompt12-state-backends-locking-remote-state.md)
