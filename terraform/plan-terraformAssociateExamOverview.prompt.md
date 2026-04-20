# HashiCorp Certified: Terraform Associate (004)
## Exam Overview

### Certification Summary

The **HashiCorp Certified: Terraform Associate (004)** certification validates foundational knowledge and skills in using Terraform Community Edition and HCP Terraform to automate infrastructure. It tests your ability to understand IaC principles, use the core Terraform workflow, write HCL configuration, manage state, use modules, and work with HCP Terraform.

---

### Exam Details

| Aspect | Details |
|--------|---------|
| **Certification Name** | HashiCorp Certified: Terraform Associate (004) |
| **Exam Type** | Proctored Certification |
| **Delivery Method** | Online (PSI Online) |
| **Total Questions** | ~57 (multiple choice and true/false) |
| **Time Limit** | 1 hour (60 minutes) |
| **Registration Fee** | $70.50 USD |
| **Product Version Tested** | Terraform 1.12 |
| **Passing Score** | 70% |
| **Languages Supported** | English |
| **Prerequisites** | Basic terminal skills; basic understanding of on-premises and cloud architecture |
| **Certificate Validity** | 2 years |
| **Recertification** | Required every 2 years |
| **Test Aids** | None allowed |
| **Registration** | developer.hashicorp.com/certifications |

---

### Exam Coverage by Objective

#### 1. **Infrastructure as Code (IaC) with Terraform**
- What IaC is and why it matters
- Advantages of IaC patterns over manual provisioning
- How Terraform manages multi-cloud, hybrid cloud, and service-agnostic workflows

#### 2. **Terraform Fundamentals**
- Installing and versioning Terraform providers
- How Terraform uses providers and the plugin model
- Writing Terraform configuration using multiple providers
- How Terraform uses and manages state

#### 3. **Core Terraform Workflow**
- The write → plan → apply workflow
- Initializing a working directory (`terraform init`)
- Validating configuration (`terraform validate`)
- Generating and reviewing execution plans (`terraform plan`)
- Applying changes to infrastructure (`terraform apply`)
- Destroying managed infrastructure (`terraform destroy`)
- Formatting configuration (`terraform fmt`)

#### 4. **Terraform Configuration (HCL)**
- `resource` blocks and `data` blocks — differences and usage
- Resource attribute references and cross-resource dependencies
- Input variables (`variable` block) and output values (`output` block)
- Complex types: list, map, set, object, tuple
- Expressions and built-in functions
- `depends_on` meta-argument and explicit dependencies
- Custom conditions with `validation` blocks and `check` blocks
- Sensitive data management and Vault integration

#### 5. **Terraform Modules**
- How Terraform sources modules (registry, local path, Git)
- Variable scope within modules
- Using modules in configuration (`module` block)
- Managing module versions

#### 6. **Terraform State Management**
- The local backend and state file
- State locking — what it is and why it matters
- Configuring remote state using the `backend` block
- Managing resource drift and using refresh-only mode
- `moved` block and `removed` block for state refactoring

#### 7. **Maintaining Infrastructure with Terraform**
- Importing existing infrastructure into Terraform state
- Inspecting state with CLI commands (`terraform state list`, `terraform show`)
- Verbose logging with `TF_LOG` environment variable

#### 8. **HCP Terraform**
- Using HCP Terraform to create and manage infrastructure
- Workspaces, projects, and variable sets
- Run triggers and remote state data sources
- Collaboration and governance features: teams, permissions, policy enforcement (OPA/Sentinel)
- Health assessments and drift detection
- CLI-driven and VCS-driven workflows
- Dynamic provider credentials

---

### Key Skills Assessed

- [ ] Explain IaC principles and the specific advantages Terraform provides
- [ ] Initialize, plan, apply, and destroy a Terraform workspace from the CLI
- [ ] Write valid HCL using resource, data, variable, output, locals, and provider blocks
- [ ] Understand how the Terraform dependency graph drives execution order
- [ ] Use meta-arguments: `depends_on`, `count`, `for_each`, `lifecycle`, `provider`
- [ ] Work with complex types (list, map, set, object) in variables and locals
- [ ] Use built-in functions: string, numeric, collection, date, encoding, filesystem
- [ ] Use dynamic expressions: `for` expressions, `dynamic` blocks, `conditional` expressions
- [ ] Create, publish, and consume modules from the Terraform Public Registry
- [ ] Explain the purpose of Terraform state and manage it safely
- [ ] Configure remote backends and understand state locking
- [ ] Import existing infrastructure and manage resource drift
- [ ] Distinguish HCP Terraform features from Terraform Community Edition
- [ ] Manage sensitive variables and integrate with HashiCorp Vault

---

### Preparation Recommendations

1. **Complete the official HashiCorp learning path**: [developer.hashicorp.com/terraform/tutorials/certification-004](https://developer.hashicorp.com/terraform/tutorials/certification-004)
2. **Practice hands-on** with a real cloud provider (AWS, Azure, or GCP) or with local Docker
3. **Study the exam content list** at [associate-review-004](https://developer.hashicorp.com/terraform/tutorials/certification-004/associate-review-004) — each objective maps to a specific doc page
4. **Review sample questions** at [associate-questions-004](https://developer.hashicorp.com/terraform/tutorials/certification-004/associate-questions-004)
5. **Practice all 8 objectives** — the exam covers all areas; do not skip state or HCP Terraform

---

### Important Notes

- The exam tests **Terraform 1.12** specifically — be aware of features and syntax changes between versions
- Provider-specific cloud knowledge is **not required** — examples use cloud providers only to illustrate concepts
- HCP Terraform (formerly Terraform Cloud) is a **heavily tested** area (Objective 8)
- The exam includes questions on **Community Edition vs HCP Terraform** feature differences
- `terraform.tfstate` file handling and the implications of remote vs local state are exam-critical
- The `for_each` and `count` meta-arguments are commonly tested

---

### Registration

- Register at: [developer.hashicorp.com/certifications](https://developer.hashicorp.com/certifications)
- Exam delivered via **PSI Online** (remote proctored)
- Renew before expiry by passing the current version of the exam (every 2 years)

---

### What to Expect

- Multiple-choice and true/false questions
- Questions test both conceptual understanding and ability to interpret HCL configuration
- Some questions present a code snippet and ask what the output, behavior, or error would be
- Expect questions comparing `terraform plan` vs `terraform apply` behavior
- Expect questions on state file contents and what commands modify state

---

### Related Certifications

| Certification | Level | Notes |
|---|---|---|
| Terraform Associate (003) | Associate | Previous version — superseded by 004 |
| **Terraform Associate (004)** | **Associate** | **This exam** |
| Terraform Authoring and Operations Professional | Professional | Advanced, lab-based, tests Terraform 1.6 |

---

*Source: [developer.hashicorp.com/certifications/infrastructure-automation](https://developer.hashicorp.com/certifications/infrastructure-automation)*
