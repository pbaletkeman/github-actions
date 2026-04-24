# The Core Terraform Workflow: All CLI Commands

> **HashiCorp Certified Terraform Associate (004)**
> Objective 3 — Understand the Purpose of Terraform (vs Other IaC Tools)

---

## Overview

The Terraform CLI provides a complete set of commands for provisioning, inspecting, and managing infrastructure. Knowing the purpose, flags, and behaviour of each command is essential for the exam.

---

## Core Workflow Sequence

```
init → fmt → validate → plan → apply → (output / show) → destroy
```

---

## `terraform init`

Initialises a working directory — downloads providers, configures backend.

```bash
terraform init                     # standard initialisation
terraform init -upgrade            # upgrade providers to latest matching constraints
terraform init -migrate-state      # migrate state when switching backends
terraform init -reconfigure        # force backend reconfiguration
```

**What it does:**
1. Reads `required_providers` and downloads provider plugins to `.terraform/`
2. Creates or updates `.terraform.lock.hcl`
3. Configures the backend (if specified)
4. Downloads and caches module source code to `.terraform/modules/`

Must be re-run when: adding a new provider, changing backend, or adding a module source.

---

## `terraform fmt`

Formats HCL files to canonical style (consistent indentation, alignment).

```bash
terraform fmt                      # format all .tf files in current dir
terraform fmt -recursive           # include subdirectories
terraform fmt -check               # exit code 1 if any files need formatting (CI use)
terraform fmt -diff                # show what would change without writing files
```

---

## `terraform validate`

Checks configuration syntax and internal consistency.

```bash
terraform validate
```

**Key property: No provider connection required.** This means it runs without network access or credentials. It checks:
- Syntax errors
- Invalid references (undefined variables, resources, modules)
- Type errors

It does NOT check if referenced AMIs exist, if IAM roles exist, or any live cloud state.

---

## `terraform plan`

Generates and displays an execution plan (what Terraform will do on apply).

```bash
terraform plan                              # standard plan
terraform plan -out=plan.tfplan            # save plan to file (for apply later)
terraform plan -var "region=us-east-1"     # pass variable inline
terraform plan -var-file=prod.tfvars       # load variables from file
terraform plan -target=aws_instance.web   # plan only a specific resource
terraform plan -refresh=false              # skip API refresh (use cached state)
terraform plan -destroy                    # show what would be destroyed
```

Plan output symbols:
- `+` — create
- `~` — update in-place
- `-/+` — destroy then create (replacement)
- `-` — destroy

---

## `terraform apply`

Applies the execution plan (provisions, modifies, or destroys resources).

```bash
terraform apply                            # interactive — prompts for confirmation
terraform apply -auto-approve              # skip confirmation prompt (CI use)
terraform apply plan.tfplan                # apply a previously saved plan
terraform apply -var "key=val"             # pass variable
terraform apply -var-file=prod.tfvars      # load variables from file
terraform apply -target=aws_instance.web  # apply only a specific resource
terraform apply -replace=aws_instance.web # force destroy+recreate of resource
```

---

## `terraform destroy`

Destroys all resources managed by the current configuration.

```bash
terraform destroy                          # interactive confirmation
terraform destroy -auto-approve            # skip confirmation
terraform destroy -target=aws_instance.web # destroy only specific resource
```

`terraform destroy` is equivalent to `terraform apply -destroy`.

---

## `terraform output`

Reads output values after apply.

```bash
terraform output                           # show all outputs
terraform output db_endpoint               # show specific output value
terraform output -json                     # JSON format (all outputs)
terraform output -raw db_password          # raw string value (no quotes, for scripting)
```

---

## `terraform show`

Displays the current state or a saved plan file.

```bash
terraform show                             # current state (human-readable)
terraform show -json                       # current state (JSON)
terraform show plan.tfplan                 # inspect a saved plan file
terraform show -json plan.tfplan           # saved plan as JSON
```

---

## `terraform graph`

Outputs the dependency graph in DOT format.

```bash
terraform graph | dot -Tsvg > graph.svg   # render as SVG (requires Graphviz)
```

---

## `terraform console`

Interactive REPL for testing HCL expressions and functions.

```bash
terraform console
> length(["a","b","c"])
3
> format("Hello, %s!", "world")
"Hello, world!"
```

Useful for testing functions and expressions before using them in configuration.

---

## `terraform import`

Imports existing infrastructure into Terraform state.

```bash
# Legacy CLI import (requires resource config already written)
terraform import aws_instance.web i-0abcd1234ef567890
```

Newer: use the `import` block in HCL (Terraform 1.5+) — see prompt13 for details.

---

## `terraform taint` / `terraform apply -replace`

Force a resource to be destroyed and recreated on next apply.

```bash
# Deprecated approach (pre-1.5)
terraform taint aws_instance.web

# Current approach (1.5+)
terraform apply -replace="aws_instance.web"
```

---

## `terraform workspace`

Manage named workspaces (isolated state environments).

```bash
terraform workspace list               # list all workspaces
terraform workspace new staging        # create new workspace
terraform workspace select production  # switch to workspace
terraform workspace delete staging     # delete workspace
terraform workspace show               # show current workspace name
```

---

## Quick Reference

| Command | Network Required? | Modifies State? |
|---------|------------------|----------------|
| `fmt` | ❌ | ❌ |
| `validate` | ❌ | ❌ |
| `init` | ✅ (provider download) | ✅ (lock file) |
| `plan` | ✅ (refresh) | ❌ |
| `apply` | ✅ | ✅ |
| `destroy` | ✅ | ✅ |
| `output` | ❌ | ❌ |
| `show` | ❌ | ❌ |
| `state list/show` | ❌ | ❌ |
| `state mv/rm` | ❌ | ✅ |
| `console` | ❌ | ❌ |

---

## Exam Checklist

- [ ] Know the core workflow sequence: init → fmt → validate → plan → apply → destroy
- [ ] Know `terraform validate` requires NO provider connection
- [ ] Know `terraform plan -out=file` saves plan; `terraform apply file` applies it
- [ ] Know plan symbols: `+` create, `~` update, `-/+` replace, `-` destroy
- [ ] Know `-auto-approve` skips confirmation (for CI)
- [ ] Know `terraform apply -replace=<addr>` forces destroy+recreate
- [ ] Know `terraform output -raw` for unquoted string values
- [ ] Know `terraform graph` outputs DOT format for Graphviz
- [ ] Know `terraform console` is a REPL for testing expressions
- [ ] Know `terraform destroy` = `terraform apply -destroy`

---

[⬅️ prompt03-terraform-state.md](prompt03-terraform-state.md) | **4 / 17** | [Next ➡️ prompt05-resource-data-blocks.md](prompt05-resource-data-blocks.md)
