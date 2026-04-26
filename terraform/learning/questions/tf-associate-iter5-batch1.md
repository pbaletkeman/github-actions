# Terraform Associate (004) — Question Bank Iter 5 Batch 1

**Iteration**: 5
**Iteration Style**: Comparison and contrast — distinguish between two similar concepts
**Batch**: 1
**Objective**: 1 — IaC Concepts
**Generated**: 2026-04-25
**Total Questions**: 12
**Difficulty Split**: 2 Easy / 8 Medium / 2 Hard
**Answer Types**: 9 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — HCL Block vs CLI Command

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contrast between a declarative HCL resource block and an imperative CLI command for the same infrastructure

**Question**:
Compare these two approaches to creating an EC2 instance:

**Approach A:**
```hcl
resource "aws_instance" "web" {
  ami           = "ami-0abcd1234"
  instance_type = "t3.micro"
}
```

**Approach B:**
```bash
aws ec2 run-instances --image-id ami-0abcd1234 --instance-type t3.micro
```

What is the fundamental difference in *how* each approach describes the task?

- A) Approach A creates the instance immediately; Approach B creates it only after a `plan` step
- B) Approach A declares the desired end state and lets the tool determine whether to act; Approach B issues a direct instruction to create the resource right now
- C) Approach A is multi-cloud; Approach B is AWS-specific
- D) Approach A requires Terraform to be installed; Approach B runs natively in any shell

**Answer**: B

**Explanation**:
Approach A is **declarative** — it describes *what* should exist, not *how* to create it. Terraform compares this desired state to the current state and decides whether to create, update, or leave the resource unchanged. Approach B is **imperative** — it directly commands AWS to run instances right now, regardless of what already exists. Running Approach B twice would create two instances; running `terraform apply` with Approach A twice would create one instance on the first run and make no changes on the second.

---

### Question 2 — State File vs Configuration File

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contrast between what the state file records and what the configuration file expresses

**Question**:
What is the primary difference between a Terraform configuration file (`.tf`) and the Terraform state file (`terraform.tfstate`)?

- A) Configuration files are written in JSON; state files use HCL syntax
- B) Configuration files describe the **desired state** — what infrastructure should exist; the state file records the **current tracked state** — what Terraform believes currently exists based on the last successful apply
- C) The state file is the authoritative source for what Terraform will create next; the configuration file is advisory only
- D) Both files serve the same purpose — the state file is simply a binary-encoded version of the configuration file

**Answer**: B

**Explanation**:
`.tf` configuration files are the human-authored source of truth for **desired state** — they express what the infrastructure *should* look like. The `terraform.tfstate` file tracks **current state** — the real-world attributes of resources Terraform has previously provisioned, refreshed during the last plan or apply. During `terraform plan`, Terraform compares these two to determine the diff. Editing the state file directly is not equivalent to editing configuration files; the state file is not where desired state is expressed.

---

### Question 3 — Terraform vs AWS CloudFormation Scope

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between Terraform's multi-cloud capability and CloudFormation's AWS-only scope

**Question**:
A platform team needs to manage AWS RDS instances, Azure Key Vault secrets, and GCP Cloud Storage buckets from a single IaC workflow. How does Terraform differ from AWS CloudFormation in its ability to meet this requirement?

- A) Both tools support multi-cloud management using provider plugins — CloudFormation recently added Azure and GCP providers
- B) Terraform supports managing resources across AWS, Azure, GCP, and many other providers from a single root module; CloudFormation is AWS-native and cannot manage Azure or GCP resources
- C) CloudFormation supports multi-cloud through AWS Organizations; Terraform requires separate state files per cloud provider
- D) Terraform requires a dedicated workspace per cloud provider; CloudFormation handles cross-cloud with a single template

**Answer**: B

**Explanation**:
Terraform's provider plugin model supports 3,000+ providers, enabling a single configuration to manage resources across AWS, Azure, GCP, and other platforms simultaneously. AWS CloudFormation is an AWS service that provisions only AWS resources — it has no native mechanism for managing Azure Key Vault or GCP Cloud Storage. Teams with multi-cloud requirements should evaluate Terraform or similar multi-cloud tools rather than CloudFormation, which is designed specifically for the AWS ecosystem.

---

### Question 4 — IaC Audit Trail vs ClickOps Audit Trail

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between the audit trail produced by IaC commits vs manual console changes

**Question**:
An engineer makes the same infrastructure change — resizing an RDS instance from `db.t3.small` to `db.t3.medium` — in two different environments. In Environment A, the change is made by updating the Terraform configuration and committing to Git before applying. In Environment B, the change is made directly through the AWS console. Six months later, a compliance audit asks who made the change and why. How do the two environments differ in their ability to answer this question?

- A) Both environments provide equivalent information — AWS CloudTrail records all API calls regardless of origin, so the auditor can find the change in both cases
- B) Environment A provides a Git commit record showing the author, timestamp, commit message, and the exact configuration change; Environment B has no entry in version control — the change is traceable only through cloud provider logs with no context about intent or approval
- C) Environment B is more auditable because AWS console changes are logged with the IAM user's identity, while Terraform applies are anonymous
- D) Neither environment has a meaningful audit trail — cloud provider logs do not retain data for six months

**Answer**: B

**Explanation**:
In Environment A, the Terraform configuration change is a Git commit with an author, timestamp, message (explaining why), and a reviewable diff of exactly what changed. This is a structured, version-controlled audit trail that satisfies compliance requirements with full context. In Environment B, the change may appear in AWS CloudTrail logs with a caller identity, but there is no version-controlled record of *why* the change was made, no peer review, and no single source of truth in code. IaC provides a richer, more intentional audit trail than ClickOps.

---

### Question 5 — First Apply vs Subsequent Apply Behaviour

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between what happens on the first apply vs a second apply with an unchanged configuration

**Question**:
A Terraform configuration declares five S3 buckets. Compare what happens on the **first** `terraform apply` to what happens on a **second** `terraform apply` run immediately after, with no configuration changes and no manual changes to the cloud environment.

- A) Both runs produce identical output: `Plan: 5 to add, 0 to change, 0 to destroy`
- B) The first apply creates all five buckets; the second apply destroys and recreates them to verify they are correct
- C) The first apply creates all five buckets (`Plan: 5 to add`); the second apply detects no difference between desired and current state and reports `No changes` — no actions are taken
- D) The first apply creates five buckets; the second apply creates five more, totalling ten

**Answer**: C

**Explanation**:
On the first apply, Terraform detects that none of the five desired buckets exist and plans to create all of them. After a successful apply, the state file records all five buckets. On the second apply, Terraform refreshes each bucket's state from the provider API, finds it matches the configuration exactly, and reports "No changes. Your infrastructure matches the configuration." This idempotent behaviour is a defining characteristic of declarative IaC — repeated applies with an unchanged configuration are always safe.

---

### Question 6 — Provisioning Tools vs Configuration Management Tools

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between infrastructure provisioning (Terraform) and configuration management (Ansible)

**Question**:
A DevOps team uses both Terraform and Ansible. A new team member asks what each tool is responsible for. Which statement best distinguishes their roles?

- A) Terraform and Ansible are interchangeable — either tool can provision cloud infrastructure and configure software on servers
- B) Terraform is used to **provision infrastructure** — creating VMs, networks, and storage in the cloud; Ansible is used for **configuration management** — installing software, managing services, and configuring applications on existing servers
- C) Terraform configures software on servers; Ansible provisions cloud infrastructure resources
- D) Terraform handles AWS resources; Ansible handles Azure and GCP resources

**Answer**: B

**Explanation**:
Terraform's primary role is **infrastructure provisioning** — it creates, modifies, and destroys cloud resources such as VMs, VPCs, load balancers, databases, and storage. Ansible's primary role is **configuration management** — once a server is running, Ansible installs packages, manages config files, starts and stops services, and configures the operating system and applications. The two tools are complementary and often used together: Terraform brings up the infrastructure, then Ansible configures the servers it created.

---

### Question 7 — Single-Cloud vs Multi-Cloud IaC Tools

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Contrasting which IaC tools are limited to a single cloud provider vs those that support multiple providers

**Question**:
Consider four IaC tools: **Terraform**, **AWS CloudFormation**, **Azure Bicep**, and **Pulumi**. Which TWO of these tools are limited to managing resources from a single cloud provider and cannot natively manage resources across multiple clouds? (Select two.)

- A) Terraform — limited to AWS resources only
- B) AWS CloudFormation — limited to AWS resources only
- C) Azure Bicep — limited to Azure resources only
- D) Pulumi — limited to AWS resources only

**Answer**: B, C

**Explanation**:
AWS CloudFormation is an AWS-native service that provisions only AWS resources — it has no capability to manage Azure or GCP resources. Azure Bicep is a Microsoft-native IaC language that deploys only to Azure via Azure Resource Manager. Both are single-cloud tools. Terraform and Pulumi, by contrast, both support multiple cloud providers through plugin ecosystems, enabling management of AWS, Azure, GCP, and other resources from a single workflow. Pulumi uses general-purpose programming languages (TypeScript, Python, Go) rather than a DSL, but is provider-agnostic.

---

### Question 8 — Drift Detection with IaC vs without IaC

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between how a team detects drift when using IaC versus when managing infrastructure manually

**Question**:
Two teams manage similar AWS environments. Team A manages all infrastructure with Terraform. Team B manages all infrastructure manually through the AWS console. An EC2 instance is resized outside of the normal process in both environments. How does each team's ability to detect the drift differ?

- A) Both teams detect drift identically — AWS Config monitors all resources and alerts both teams automatically regardless of how the infrastructure was provisioned
- B) Team A can run `terraform plan` to compare the declared desired state against the actual cloud state and detect the resized instance; Team B has no single source of truth to compare against, making drift invisible unless noticed manually
- C) Team B is better positioned to detect drift because they interact with the console daily and are more likely to notice changes
- D) Neither team can detect drift without a third-party monitoring tool — Terraform itself does not detect resource attribute changes

**Answer**: B

**Explanation**:
Terraform maintains a record of desired state (configuration files) and can refresh current state during `terraform plan` by querying the provider API. If the EC2 instance's `instance_type` was changed outside of Terraform, `plan` will show a diff between the desired type and the actual type — drift is made explicit and actionable. Team B has no equivalent mechanism: there is no configuration file to compare against, so the only way to detect the change is manual inspection or a separate monitoring tool. IaC makes drift visible and measurable; ClickOps does not.

---

### Question 9 — Disaster Recovery: IaC vs No IaC

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between recreating a lost environment with IaC versus without it

**Question**:
Two companies lose their entire cloud environments in a catastrophic incident. Company A has all infrastructure defined in Terraform configuration files stored in Git. Company B provisioned everything manually through the cloud console with no IaC. How does the recovery path differ?

- A) Company A and Company B take the same amount of time — rebuilding from Terraform configs is not faster than rebuilding manually if the engineer knows the setup well
- B) Company A can run `terraform apply` against their configuration files to recreate the environment reproducibly and consistently; Company B must manually recreate each resource by memory or from documentation, with no guarantee of consistency or completeness
- C) Company A must first run `terraform import` to re-register all resources before applying — the advantage over Company B is minimal
- D) Both companies should use cloud provider snapshots for disaster recovery; IaC configurations are not a substitute for backup solutions

**Answer**: B

**Explanation**:
IaC's disaster recovery benefit is one of its most tangible advantages. Company A's Terraform configurations are the complete, executable definition of their environment — running `terraform apply` recreates every resource precisely as configured. Company B's engineers must rebuild each component manually, relying on memory, documentation (if it exists and is current), and individual expertise. Manual reconstruction is slow, error-prone, and often produces an environment that differs from the original. IaC enables reliable, repeatable recovery from code.

---

### Question 10 — Manual Change vs Config Change: Two Differences

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Contrasting how a manual cloud change and a Terraform configuration change are tracked and applied differently

**Question**:
An infrastructure change is made in two different ways. **Method X**: An engineer updates the Terraform configuration in a `.tf` file and runs `terraform apply`. **Method Y**: An engineer directly modifies the resource using the cloud provider's web console. Which TWO statements correctly describe a difference between these two methods? (Select two.)

- A) Only Method X produces a version-controlled record of the change that can be reviewed, reverted, and audited via Git history
- B) Only Method X changes can be peer-reviewed in a pull request before being applied — Method Y bypasses the review process entirely
- C) Method Y is safer because console changes go through stricter validation than Terraform plan
- D) Method X and Method Y are equivalent — Terraform detects and absorbs console changes automatically on the next plan

**Answer**: A, B

**Explanation**:
Method X (IaC) produces two key advantages over Method Y (ClickOps). (A) The configuration change is a Git commit with an author, timestamp, and diff — it can be reviewed, reverted, and included in compliance audits. Method Y leaves no record in version control. (B) Because Method X uses code, the change can go through a pull request review before apply, enabling peer oversight. Method Y bypasses any code review or approval process. Option D is incorrect: while Terraform detects the drift during the next plan, it does not silently absorb console changes — it proposes reverting them to match the desired state.

---

### Question 11 — Declarative Model vs Imperative Scripting: What the Operator Specifies

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Deep contrast between what an operator must define in a declarative model vs an imperative scripting approach

**Question**:
A DevOps engineer has two options for ensuring that exactly three web servers running `t3.micro` exist in AWS:

**Option 1 (Declarative):** Write a Terraform configuration declaring three `aws_instance` resources and run `terraform apply`.

**Option 2 (Imperative):** Write a bash script that checks how many instances exist, calculates the delta, and runs `aws ec2 run-instances` or `aws ec2 terminate-instances` as needed.

What is the key contrast in what the operator is responsible for defining in each option?

- A) In Option 1, the operator defines the steps; in Option 2, Terraform infers the steps from a high-level goal
- B) Option 1 requires more code — HCL is more verbose than bash for simple operations
- C) In Option 1, the operator defines *what* the end state should be; in Option 2, the operator must define *how* to reach the end state — including the logic to detect and correct any discrepancy
- D) Both options require the operator to define the same logic — the difference is only in the syntax used

**Answer**: C

**Explanation**:
This contrast is the essence of declarative vs imperative. In the declarative model (Option 1), the operator specifies only *what* should exist: "three `t3.micro` EC2 instances." Terraform determines the current state, calculates the diff, and decides the steps. In the imperative model (Option 2), the operator must code all of that logic themselves: query current count, compare to desired, decide to add or remove, handle API calls, deal with errors, and ensure convergence. The declarative approach offloads the how to the tool; the imperative approach keeps it in the operator's script.

---

### Question 12 — Provisioning vs Configuration Management: Full Stack Roles

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Contrasting the provisioning and configuration management layers in a complete infrastructure automation stack

**Question**:
A team is building a complete automation stack. They decide to use Terraform for one layer and Ansible for another. Which TWO statements correctly describe how the responsibilities of each tool differ? (Select two.)

- A) Terraform provisions and lifecycle-manages cloud infrastructure resources (VMs, networks, databases, storage) — it creates, modifies, and destroys these resources based on HCL configuration
- B) Ansible's primary function in this stack is to provision new cloud VMs — it submits API calls to the cloud provider to launch instances and create VPCs
- C) Ansible is used to configure the operating system and software on servers after they are provisioned — installing packages, writing config files, starting services, and managing application deployments
- D) Terraform installs application software on VMs after provisioning them — it includes a built-in configuration management agent that runs after `terraform apply` completes

**Answer**: A, C

**Explanation**:
The two tools cover complementary but non-overlapping primary responsibilities. (A) Terraform's role is infrastructure provisioning: it uses HCL to declare cloud resources and manages their full lifecycle via provider API calls. (C) Ansible's role is configuration management: once Terraform has created a VM, Ansible connects over SSH and configures it — installing packages, managing files, starting services. (B) is incorrect: launching VMs and creating VPCs are provisioning tasks handled by Terraform, not Ansible's primary purpose. (D) is incorrect: Terraform does not have a built-in configuration management agent; `terraform apply` manages cloud API resources, not in-guest software installation.

---
