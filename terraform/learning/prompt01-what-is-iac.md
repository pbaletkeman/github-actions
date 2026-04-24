# What is Infrastructure as Code?

> **HashiCorp Certified Terraform Associate (004)**
> Objective 1 — Understand Infrastructure as Code (IaC) Concepts

---

## Overview

Infrastructure as Code (IaC) is the practice of defining and managing infrastructure (servers, networks, databases, load balancers, etc.) through machine-readable configuration files instead of manual processes or graphical interfaces.

---

## The Problem IaC Solves

### Manual / ClickOps Approach

- Infrastructure provisioned via web console or CLI commands manually
- No reproducibility — hard to recreate exact same environment
- No audit trail — who changed what and when?
- Slow — humans clicking through UIs
- Drift — production drifts from staging over time

### IaC Approach

- Infrastructure described in code files checked into version control
- Same code deploys identical environments every time
- Every change is a code commit — full audit trail
- Automated and fast
- Drift detectable by comparing desired state (code) vs actual state

---

## Core IaC Concepts

### Desired State vs Current State

| Concept | Description |
|---------|-------------|
| **Desired state** | What your configuration files say infrastructure should look like |
| **Current state** | What actually exists in the cloud right now |
| **IaC tool's job** | Reconcile the gap — bring current state to match desired state |

Terraform continuously compares desired state (your `.tf` files) with current state (Terraform state file, which reflects actual cloud resources) and plans the minimal set of changes needed.

### Idempotency

Running the same configuration multiple times produces the same result. If the infrastructure already matches the desired state, no changes are made.

```
Apply once → creates 3 servers
Apply again → no changes (3 servers already exist, matches desired state)
Apply after deleting 1 server → creates 1 server (back to desired state)
```

---

## Declarative vs Imperative IaC

| Approach | Description | Tools | How it Works |
|----------|-------------|-------|-------------|
| **Declarative** | Describe the *desired end state*; tool figures out the steps | **Terraform**, CloudFormation, Pulumi (in declarative mode) | You say "I want 3 EC2 instances of type t3.micro in us-east-1" — Terraform creates them |
| **Imperative** | Describe the *steps to take* | Ansible playbooks, Chef recipes, shell scripts | You say "Run `aws ec2 run-instances` three times" |

### Terraform is Declarative

```hcl
# You declare WHAT you want, not HOW to create it
resource "aws_instance" "web" {
  ami           = "ami-0abcd1234"
  instance_type = "t3.micro"
  count         = 3
}
```

Terraform determines whether to create, update, or destroy based on the difference between desired and current state. You do not write the steps.

---

## Benefits of IaC

| Benefit | Description |
|---------|-------------|
| **Repeatability** | Same code → identical environment every time |
| **Documentation** | Config files serve as living documentation of infrastructure |
| **Speed** | Automated provisioning much faster than manual |
| **Audit trail** | Every infrastructure change is a version-controlled commit |
| **Disaster recovery** | Recreate entire environment from code after failure |
| **Drift detection** | IaC tool can detect and remediate drift |
| **Testing** | Infrastructure changes can be reviewed (plan), tested, and validated before applying |
| **Collaboration** | Teams can review infrastructure changes via pull requests |

---

## Multi-Cloud and Hybrid Cloud

IaC tools like Terraform manage infrastructure across multiple cloud providers and on-premises environments using a single workflow:

```hcl
# AWS provider
resource "aws_s3_bucket" "data" { ... }

# Azure provider in the same config
resource "azurerm_storage_account" "backup" { ... }

# GCP provider
resource "google_storage_bucket" "archive" { ... }
```

This is a key advantage of Terraform over provider-specific tools (CloudFormation is AWS-only; ARM templates are Azure-only).

---

## IaC Tooling Landscape

| Tool | Type | Approach | Cloud Support |
|------|------|----------|--------------|
| **Terraform** | Provisioning | Declarative | Multi-cloud |
| AWS CloudFormation | Provisioning | Declarative | AWS only |
| Azure ARM / Bicep | Provisioning | Declarative | Azure only |
| Google Cloud Deployment Manager | Provisioning | Declarative | GCP only |
| Pulumi | Provisioning | Declarative (code) | Multi-cloud |
| Ansible | Config management | Imperative/Declarative | Any |
| Chef | Config management | Primarily imperative | Any |
| Puppet | Config management | Declarative | Any |

---

## Exam Checklist

- [ ] Know the definition of IaC: infrastructure defined as machine-readable config files
- [ ] Know the difference between desired state and current state
- [ ] Know idempotency: same config applied N times = same result
- [ ] Know **Terraform is declarative** (describe WHAT, not HOW)
- [ ] Know declarative vs imperative: Ansible/Chef scripts = imperative
- [ ] Know the benefits: repeatability, documentation, speed, audit trail, DR, drift detection
- [ ] Know Terraform supports multi-cloud; CloudFormation/ARM are single-cloud

---

[⬅️ README](README.md) | **1 / 17** | [Next ➡️ prompt02-providers-plugin-model.md](prompt02-providers-plugin-model.md)
