# Resource Dependencies and Lifecycle

> **HashiCorp Certified Terraform Associate (004)**
> Objective 4 — Use Terraform Outside of Core Workflow

---

## Overview

Terraform automatically infers dependencies from resource attribute references. Understanding how the dependency graph is built—and how to manage it—is essential for both the exam and real-world configurations.

---

## Implicit Dependencies

Terraform automatically detects a dependency when resource B references an attribute of resource A.

```hcl
resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"
}

resource "aws_subnet" "public" {
  vpc_id = aws_vpc.main.id   # ← reference creates implicit dependency
  cidr_block = "10.0.1.0/24"
}
```

Terraform will create `aws_vpc.main` before `aws_subnet.public` because it detects the reference.

### What Creates Dependency Edges

- Resource references: `resource_type.name.attribute`
- Module output references: `module.name.output_name`

### What Does NOT Create Dependency Edges

- `var.*` references — variables are just values, not resources
- `local.*` references — locals are just computed values

---

## Explicit Dependencies with `depends_on`

Use `depends_on` when Terraform cannot detect the relationship through attribute references. Most common for IAM permissions.

```hcl
resource "aws_iam_role_policy" "s3_read" {
  role   = aws_iam_role.app.name
  policy = data.aws_iam_policy_document.s3_read.json
}

resource "aws_instance" "app" {
  ami           = data.aws_ami.ubuntu.id
  instance_type = "t3.micro"
  iam_instance_profile = aws_iam_instance_profile.app.name

  depends_on = [
    aws_iam_role_policy.s3_read   # must be attached before instance starts using it
  ]
}
```

Use `depends_on` sparingly — it reduces parallelism.

---

## Parallelism

Terraform applies operations in parallel for resources with no dependencies:

```bash
terraform apply -parallelism=10   # default: 10 concurrent operations
```

The dependency graph determines the maximum safe parallelism.

---

## Partial Apply with `-target`

Apply changes to only specific resources:

```bash
terraform apply -target=aws_instance.web
terraform apply -target=module.vpc
```

**Warning:** Using `-target` frequently can cause state drift (other resources may depend on skipped changes). Use sparingly.

---

## Dependency Graph Visualisation

```bash
terraform graph | dot -Tsvg > graph.svg   # requires Graphviz installed
```

The output is in DOT format (graph description language).

---

## Lifecycle Meta-Arguments

```hcl
resource "aws_instance" "web" {
  ami           = "ami-0abc123"
  instance_type = "t3.micro"

  lifecycle {
    create_before_destroy = true
    prevent_destroy       = true
    ignore_changes        = [ami, tags["LastModified"]]
    replace_triggered_by  = [aws_launch_template.web]
  }
}
```

| Lifecycle Argument | Effect |
|-------------------|--------|
| `create_before_destroy = true` | Create replacement before destroying old (zero-downtime) |
| `prevent_destroy = true` | Error if plan includes destroy of this resource |
| `ignore_changes = [...]` | Don't detect drift on listed attributes |
| `replace_triggered_by = [...]` | Force replace when referenced resource/attribute changes |

---

## Provider Meta-Argument with Alias

```hcl
provider "aws" {
  alias  = "us-west"
  region = "us-west-2"
}

resource "aws_instance" "west" {
  provider      = aws.us-west   # reference aliased provider
  ami           = "ami-0abc123"
  instance_type = "t3.micro"
}
```

---

## `moved` Block

Rename or move a resource in state without destroying and recreating it.

```hcl
moved {
  from = aws_instance.old_name
  to   = aws_instance.new_name
}
```

Use cases:
- Renaming a resource in configuration
- Moving a resource into or out of a module

```hcl
# Moving from root into a module
moved {
  from = aws_instance.web
  to   = module.web_tier.aws_instance.server
}
```

After Terraform applies the `moved` block successfully, you can remove it.

---

## `removed` Block (Terraform 1.7+)

Stop managing a resource but keep it in the cloud:

```hcl
removed {
  from = aws_instance.old_server

  lifecycle {
    destroy = false   # don't destroy — just stop tracking
  }
}
```

---

## Order of Operations

Terraform builds a DAG (Directed Acyclic Graph) from all dependencies and executes in topological order:

1. Resources with no dependencies → created first (in parallel)
2. Resources that depend on others → created after dependencies complete
3. Destroy order → reversed (dependents destroyed before dependencies)

---

## Exam Checklist

- [ ] Know implicit dependencies come from resource attribute references
- [ ] Know `var.*` and `local.*` references do NOT create dependency edges
- [ ] Know `depends_on` is for non-attribute dependencies (e.g., IAM policy attachment)
- [ ] Know default parallelism is 10 (`-parallelism=10`)
- [ ] Know `-target` for partial apply (use sparingly — can cause drift)
- [ ] Know `terraform graph` outputs DOT format
- [ ] Know all 4 lifecycle meta-arguments and their effects
- [ ] Know `provider = aws.us-west` for aliased provider reference
- [ ] Know `moved` block renames resource in state without destroy+recreate
- [ ] Know `removed` block stops managing a resource without destroying it (1.7+)

---

[⬅️ prompt08-builtin-functions-expressions.md](prompt08-builtin-functions-expressions.md) | **9 / 17** | [Next ➡️ prompt10-custom-conditions-sensitive-data.md](prompt10-custom-conditions-sensitive-data.md)
