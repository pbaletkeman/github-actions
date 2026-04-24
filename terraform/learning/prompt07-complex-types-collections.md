# Complex Types and Collections

> **HashiCorp Certified Terraform Associate (004)**
> Objective 4 — Use Terraform Outside of Core Workflow

---

## Overview

Terraform supports complex collection types and provides powerful meta-arguments (`count`, `for_each`) and expressions (`for`, `dynamic`) for working with them.

---

## `count` Meta-Argument

Creates multiple identical instances of a resource.

```hcl
resource "aws_instance" "web" {
  count         = 3
  ami           = "ami-0abc123"
  instance_type = "t3.micro"

  tags = {
    Name = "web-server-${count.index}"   # count.index is 0-based
  }
}
```

### Accessing Instances

```hcl
aws_instance.web[0].id    # first instance
aws_instance.web[1].id    # second instance
aws_instance.web[*].id    # all IDs as a list (splat)
```

### Limitation

If you remove an element from the middle of a list, all subsequent resources are recreated. Use `for_each` when items have identity.

---

## `for_each` Meta-Argument

Creates one instance per key in a map or element in a set.

### With a Map

```hcl
variable "servers" {
  type = map(string)
  default = {
    web = "t3.micro"
    app = "t3.small"
    db  = "t3.medium"
  }
}

resource "aws_instance" "servers" {
  for_each      = var.servers
  ami           = "ami-0abc123"
  instance_type = each.value

  tags = {
    Name = each.key
  }
}
```

### With a Set

```hcl
resource "aws_iam_user" "users" {
  for_each = toset(["alice", "bob", "carol"])
  name     = each.key   # each.key == each.value for sets
}
```

### Accessing Instances

```hcl
aws_instance.servers["web"].id
aws_instance.servers["app"].id
```

### `count` vs `for_each`

| Aspect | `count` | `for_each` |
|--------|---------|-----------|
| Iteration over | Integer | Map or Set |
| Instance reference | `resource[0]` | `resource["key"]` |
| Removal | Renumbers all after removed index | Only removes specific key |
| Best for | When all instances are identical | When each has a unique identity |
| Recommendation | Avoid unless instances are truly identical | **Prefer for almost everything** |

---

## `for` Expressions

Transform collections inline.

### List Comprehension

```hcl
# Transform list
variable "names" {
  default = ["alice", "bob", "carol"]
}

locals {
  upper_names = [for name in var.names : upper(name)]
  # result: ["ALICE", "BOB", "CAROL"]

  # With filter
  long_names = [for name in var.names : name if length(name) > 4]
  # result: ["alice", "carol"]
}
```

### Map Comprehension

```hcl
variable "instances" {
  default = {
    web = "t3.micro"
    app = "t3.small"
  }
}

locals {
  instance_map = {for k, v in var.instances : k => "type: ${v}"}
  # result: { web = "type: t3.micro", app = "type: t3.small" }
}
```

---

## Splat Expressions

Shorthand for extracting an attribute from all elements of a list.

```hcl
# These two are equivalent
aws_instance.web[*].id
[for r in aws_instance.web : r.id]

# Use in outputs
output "all_instance_ids" {
  value = aws_instance.web[*].id
}
```

---

## `dynamic` Blocks

Generate repeated nested blocks from a collection. Useful when the number of nested blocks is variable.

```hcl
variable "ingress_rules" {
  type = list(object({
    from_port = number
    to_port   = number
    protocol  = string
  }))
  default = [
    { from_port = 80,  to_port = 80,  protocol = "tcp" },
    { from_port = 443, to_port = 443, protocol = "tcp" },
  ]
}

resource "aws_security_group" "web" {
  name = "web-sg"

  dynamic "ingress" {
    for_each = var.ingress_rules
    content {
      from_port   = ingress.value.from_port   # 'ingress' = iterator name (block type)
      to_port     = ingress.value.to_port
      protocol    = ingress.value.protocol
      cidr_blocks = ["0.0.0.0/0"]
    }
  }
}
```

The iterator variable name defaults to the block type (`ingress`). You can customise it with `iterator = rule`:

```hcl
dynamic "ingress" {
  for_each = var.ingress_rules
  iterator = rule          # custom iterator name

  content {
    from_port = rule.value.from_port
    to_port   = rule.value.to_port
  }
}
```

---

## Type Conversion Functions

```hcl
tostring(42)         # "42"
tonumber("42")       # 42
tobool("true")       # true
tolist(["a", "b"])   # list type (from tuple or set)
tomap({a = 1})       # map type
toset(["a","b","a"]) # {"a","b"} — removes duplicates
```

---

## Practical Example: Multi-Region Buckets

```hcl
variable "regions" {
  type    = set(string)
  default = ["us-east-1", "eu-west-1", "ap-southeast-1"]
}

resource "aws_s3_bucket" "backups" {
  for_each = var.regions
  bucket   = "my-company-backups-${each.key}"

  tags = {
    Region = each.key
  }
}

output "bucket_arns" {
  value = {for k, v in aws_s3_bucket.backups : k => v.arn}
}
```

---

## Exam Checklist

- [ ] Know `count` = integer; reference with `[index]`; `count.index` is 0-based
- [ ] Know `for_each` = map or set; reference with `["key"]`; `each.key` and `each.value`
- [ ] Know `for_each` is preferred over `count` when items have identity
- [ ] Know `for` expression syntax: `[for item in collection : transform if condition]`
- [ ] Know map `for` syntax: `{for k, v in map : k => transform}`
- [ ] Know splat `resource[*].attr` = list of all attribute values
- [ ] Know `dynamic` block generates repeated nested blocks from a collection
- [ ] Know `dynamic` iterator variable name defaults to the block type
- [ ] Know type conversion functions: tostring, tonumber, tobool, tolist, tomap, toset

---

[⬅️ prompt06-variables-locals-outputs.md](prompt06-variables-locals-outputs.md) | **7 / 17** | [Next ➡️ prompt08-builtin-functions-expressions.md](prompt08-builtin-functions-expressions.md)
