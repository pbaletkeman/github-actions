# Terraform Associate (004) — Question Bank Iter 5 Batch 8

**Iteration**: 5
**Iteration Style**: Comparison and contrast — distinguish between two similar concepts
**Batch**: 8
**Objective**: 7 — Maintaining State + 8 — HCP Terraform
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 3 Easy / 8 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — Local Backend vs S3 Backend: Collaboration and Safety

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contrast between the limitations of the local backend and the capabilities of a remote S3 backend

**Question**:
Compare the **local backend** (default `terraform.tfstate` file) with the **S3 backend**. What is the most significant operational difference between the two?

- A) The local backend encrypts state automatically; the S3 backend stores state in plaintext unless `encrypt = true` is set
- B) The local backend stores state in a file on the workstation running Terraform — it offers no collaboration support, no built-in locking, and no encryption; the S3 backend stores state in a shared S3 bucket — it enables team collaboration, supports state locking via DynamoDB, and supports server-side encryption at rest
- C) Both backends support state locking — the local backend uses a file lock while S3 uses DynamoDB; encryption is optional in both cases
- D) The S3 backend can only be used with AWS resources; the local backend supports all providers

**Answer**: B

**Explanation**:
The local backend is the default and requires no configuration, but it has three critical limitations for team use: (1) state is stored on a single workstation, so teammates cannot share it; (2) there is no reliable locking mechanism — two engineers running `terraform apply` simultaneously can corrupt the state; (3) sensitive values in state are in plaintext in a local file. The S3 backend addresses all three: state is stored in a central, accessible S3 bucket; locking is provided by a DynamoDB table with a `LockID` hash key; and `encrypt = true` enables SSE at rest. These differences make the S3 backend (or HCP Terraform) the standard choice for any team environment.

---

### Question 2 — `terraform plan -refresh-only` vs `terraform apply -refresh-only`

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contrast between the plan-only and apply forms of the refresh-only flag

**Question**:
Compare `terraform plan -refresh-only` with `terraform apply -refresh-only`. What is the key difference between the two commands?

- A) `terraform plan -refresh-only` updates state to match the cloud; `terraform apply -refresh-only` only shows what has drifted without changing state
- B) `terraform plan -refresh-only` shows what drift exists between actual cloud resources and Terraform state — it **does not change state** and makes no modifications anywhere; `terraform apply -refresh-only` **updates state** to match the actual cloud resource values, reconciling drift — no infrastructure is created, modified, or destroyed in either case
- C) Both commands are identical — `-refresh-only` has the same effect regardless of whether it is used with `plan` or `apply`
- D) `terraform apply -refresh-only` creates new resources to match any detected drift; `terraform plan -refresh-only` only shows the diff

**Answer**: B

**Explanation**:
Both commands are read-side operations — neither creates, modifies, nor destroys infrastructure. The distinction is about what changes. `terraform plan -refresh-only` is purely observational: it queries the cloud provider for current resource attributes, compares them to state, and shows any differences. No state file is written. `terraform apply -refresh-only` takes that same refresh and **writes the result to state**, updating Terraform's recorded values to match what actually exists in the cloud. This is the appropriate command when you want to acknowledge drift (e.g., a team member manually updated a tag in the console) and align state without reverting the cloud change.

---

### Question 3 — `cloud` Block vs `backend "remote"`: Preferred HCP Terraform Connection

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Contrast between the cloud block and the legacy remote backend for connecting to HCP Terraform

**Question**:
Compare the `cloud` block (Terraform 1.1+) with the `backend "remote"` block as methods for connecting to HCP Terraform. What is the key difference?

- A) `backend "remote"` is the current standard; the `cloud` block is a deprecated alias introduced for backward compatibility
- B) The `cloud` block is the **preferred, modern method** for connecting to HCP Terraform — it supports workspace selection by tags (in addition to name) and integrates features specific to HCP Terraform; `backend "remote"` is the **legacy method** — it is still valid and functional but predates HCP Terraform-specific features; HashiCorp recommends migrating to the `cloud` block
- C) Both blocks are exactly equivalent — the `cloud` block is simply renamed from `backend "remote"` with no functional differences
- D) The `cloud` block only supports HCP Terraform Free tier; `backend "remote"` is required for paid HCP Terraform tiers

**Answer**: B

**Explanation**:
Both blocks connect a local Terraform configuration to HCP Terraform, but they differ in capability and intent. The `cloud` block was introduced in Terraform 1.1 specifically for HCP Terraform and supports features that `backend "remote"` does not — most notably the ability to select workspaces by **tags** rather than only by name. This allows a single configuration to target multiple workspaces matching a tag filter, enabling workspace-per-environment patterns. `backend "remote"` predates these additions and only supports workspace selection by name. HashiCorp's documentation now directs users to the `cloud` block as the canonical way to integrate with HCP Terraform, while `backend "remote"` remains supported for existing configurations.

---

### Question 4 — `terraform init -migrate-state` vs `terraform init -reconfigure`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between the two init flags used when changing a backend configuration

**Question**:
Compare `terraform init -migrate-state` with `terraform init -reconfigure`. When would you use each, and what does each do with existing state?

- A) Both flags are equivalent — they both migrate state to the new backend without user confirmation
- B) `terraform init -migrate-state` **copies existing state** from the old backend to the newly configured backend — it is used when you want to preserve your state history when switching backends; `terraform init -reconfigure` **ignores the existing state** and reconfigures the backend without migrating — it is used when you want to start fresh or when the state has already been manually moved
- C) `terraform init -reconfigure` migrates state; `terraform init -migrate-state` discards it — the flags are inverted from what their names suggest
- D) `-migrate-state` only works when switching between two remote backends; it cannot migrate from the local backend to a remote backend

**Answer**: B

**Explanation**:
When you change the backend configuration in a `terraform {}` block (e.g., switching from local to S3, or changing the S3 bucket), `terraform init` detects the change and refuses to proceed without one of these flags. `-migrate-state` is the safe option: it reads the state from the old backend and copies it to the new backend, so no state history is lost. You would use this when adopting a remote backend for the first time (migrating local state to S3 or HCP Terraform) or when reorganising where state is stored. `-reconfigure` skips migration entirely — it just applies the new backend configuration and treats it as starting fresh. Use this when the state has already been moved manually, when you know the new backend already contains the correct state, or when you intentionally want to abandon the current state location.

---

### Question 5 — `terraform state mv` vs `terraform state rm`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between the purpose and effect of terraform state mv and terraform state rm

**Question**:
Compare `terraform state mv` with `terraform state rm`. Both modify what Terraform tracks in state — what is the fundamental difference between them?

- A) `terraform state mv` deletes a resource from both state and the cloud; `terraform state rm` only removes it from state while leaving the cloud resource intact
- B) `terraform state mv` **relocates** a resource within state — it renames or moves the resource's address (e.g., from `aws_instance.web` to `aws_instance.web_server`, or from a root resource into a module) without destroying or re-creating anything; `terraform state rm` **removes** a resource entry from state entirely — Terraform stops managing it, but the actual cloud resource is left untouched; the resource becomes unmanaged drift
- C) `terraform state rm` renames a resource; `terraform state mv` deletes it permanently from both state and the cloud provider
- D) Both commands are equivalent — the only difference is that `terraform state mv` requires confirmation while `terraform state rm` does not

**Answer**: B

**Explanation**:
`terraform state mv` is used for **refactoring** — when you rename a resource label in HCL or move a resource into a module, `terraform state mv` updates the state entry to match the new address. Without it, Terraform would plan to destroy the old resource and create a new one. Examples: renaming `aws_instance.web` to `aws_instance.web_server`, or moving it into `module.compute.aws_instance.server`. The cloud resource is untouched throughout. `terraform state rm` is used for **abandonment** — when you want Terraform to stop managing a resource. The state entry is deleted, the cloud resource remains, and on the next `terraform plan` the resource appears as new (because it is in the config but not in state). Both are state-only operations with no cloud API calls.

---

### Question 6 — HCP Terraform Workspace Variables vs Variable Sets

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between workspace-specific variables and variable sets in HCP Terraform

**Question**:
Compare **workspace variables** with **variable sets** in HCP Terraform. What is the key difference in scope and reusability?

- A) Workspace variables are reusable across all workspaces in an organisation; variable sets are scoped to a single workspace only
- B) Workspace variables are defined **per workspace** — they apply only to that specific workspace and must be configured individually for each workspace that needs them; variable sets are **reusable collections** of variables that can be assigned to multiple workspaces or an entire organisation — a single change to a variable set propagates to all assigned workspaces
- C) Both workspace variables and variable sets work identically — the only difference is that variable sets have a different UI panel in HCP Terraform
- D) Variable sets are used only for Terraform input variables; workspace variables are used only for environment variables (like `AWS_ACCESS_KEY_ID`)

**Answer**: B

**Explanation**:
Workspace variables are the simplest form of variable management in HCP Terraform — you define a value directly on a workspace and it applies to runs in that workspace only. This is fine for workspace-specific values but becomes repetitive when multiple workspaces need the same credentials or settings. Variable sets solve this: you define a named collection of variables once (e.g., "AWS Credentials" containing `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY`) and assign it to as many workspaces as needed — or to the entire organisation. When credentials rotate, updating the variable set automatically propagates the change to all assigned workspaces. Variable sets can contain both Terraform input variables and environment variables.

---

### Question 7 — Speculative Plan vs Plan-and-Apply Run in HCP Terraform

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between a speculative plan run and a plan-and-apply run in HCP Terraform

**Question**:
Compare a **speculative plan** run with a **plan-and-apply** run in HCP Terraform. What is the fundamental difference?

- A) A speculative plan is faster because it skips the refresh phase; a plan-and-apply run always refreshes state before planning
- B) A speculative plan is **read-only** — it computes what changes would occur but **can never progress to an apply**; it is triggered by pull requests or API calls and posts its results as status checks; a plan-and-apply run **can apply changes** — it proceeds through planning and, based on workspace settings, either auto-applies or waits for manual confirmation before applying
- C) A plan-and-apply run is triggered only by manual CLI commands; a speculative plan is triggered only by VCS events
- D) Both run types can apply infrastructure changes — the difference is that a speculative plan requires an additional approval step before applying

**Answer**: B

**Explanation**:
The speculative plan is designed for **visibility without risk** — it evaluates what a configuration change would do, but it is architecturally incapable of applying. No matter how long you wait, a speculative plan will never apply. It is used in VCS workflows to give pull request reviewers a preview of infrastructure impact. A plan-and-apply run is the standard operational run — it computes a plan and then, depending on the workspace's apply method setting, either automatically applies (auto-apply mode) or pauses for a human to review and confirm. Understanding that speculative plans are permanently non-applying is important for VCS workflow questions on the exam.

---

### Question 8 — `TF_LOG_CORE` vs `TF_LOG_PROVIDER` vs `TF_LOG`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between the granular log variables and the combined TF_LOG variable

**Question**:
Compare `TF_LOG_CORE`, `TF_LOG_PROVIDER`, and `TF_LOG`. How do the granular variables differ from the single combined variable?

- A) `TF_LOG_CORE` and `TF_LOG_PROVIDER` are deprecated aliases for `TF_LOG` — they all control the same log stream
- B) `TF_LOG` sets a **single log level for all Terraform output** — both the core engine and provider plugins log at the same verbosity; `TF_LOG_CORE` and `TF_LOG_PROVIDER` are **independent controls** — `TF_LOG_CORE` controls only the Terraform core engine's log output, and `TF_LOG_PROVIDER` controls only the provider plugin log output; this allows operators to enable `TRACE` for a specific provider while keeping core logs at a quieter level (or vice versa), reducing noise when debugging provider-specific issues
- C) `TF_LOG_PROVIDER` sets log output for all Terraform runs in a project; `TF_LOG_CORE` sets it for a specific workspace; `TF_LOG` is the organisation-wide default
- D) `TF_LOG_CORE` and `TF_LOG_PROVIDER` only apply when `TF_LOG_PATH` is also set — without a path, only `TF_LOG` produces output

**Answer**: B

**Explanation**:
`TF_LOG` is the coarse-grained control: set it to `DEBUG` or `TRACE` and everything — core engine, provider plugins, gRPC calls — logs at that level. This is often more output than needed. The granular variables offer surgical control: `TF_LOG_CORE=TRACE` turns on maximum verbosity for the Terraform core engine (graph traversal, state operations, plan computation) while provider logs remain silent, and `TF_LOG_PROVIDER=TRACE` does the opposite — maximally verbose provider plugin logging while core remains quiet. This separation is particularly useful when debugging why a provider API call is failing (use `TF_LOG_PROVIDER=TRACE`) versus debugging unexpected plan behaviour (use `TF_LOG_CORE=DEBUG`).

---

### Question 9 — Sentinel vs OPA: Two HCP Terraform Policy Frameworks

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between Sentinel and OPA as the two policy enforcement frameworks in HCP Terraform

**Question**:
HCP Terraform supports two policy enforcement frameworks: **Sentinel** and **OPA (Open Policy Agent)**. What is the key difference between them?

- A) Sentinel is used for cost estimation policies; OPA is used for security compliance policies — they target different policy domains
- B) Sentinel uses **HashiCorp's proprietary Sentinel DSL** — it is developed and maintained by HashiCorp and is tightly integrated with HCP Terraform's policy workflow; OPA uses **Rego**, an open-source policy language maintained by the Open Policy Agent community — it offers broader ecosystem support and is used across many tools beyond Terraform; both frameworks enforce the same three enforcement levels (advisory, soft-mandatory, hard-mandatory) in HCP Terraform
- C) OPA policies run before the plan; Sentinel policies run after the plan but before the apply — they execute at different points in the run lifecycle
- D) Sentinel is available on all HCP Terraform tiers; OPA is only available on the Enterprise tier and cannot be used in the Free or Plus plans

**Answer**: B

**Explanation**:
Both Sentinel and OPA are supported policy-as-code frameworks in HCP Terraform that evaluate infrastructure plans before apply. The primary difference is language and origin. **Sentinel** is HashiCorp's own policy language — it was designed specifically for the HashiCorp ecosystem and integrates deeply with HCP Terraform's run workflow. **OPA** (Open Policy Agent) uses the Rego language, is open-source, and has a large community outside Terraform. Teams that already use OPA across their infrastructure toolchain (Kubernetes admission control, API gateway policies, etc.) may prefer OPA for consistency. Both frameworks support the same three enforcement levels and both can access the Terraform plan as structured data for evaluation. For the exam, know that both exist and that Sentinel is HashiCorp-native while OPA is community/open-source.

---

### Question 10 — Three Policy Enforcement Levels: Two Key Contrasts

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Contrasting TWO of the three HCP Terraform policy enforcement levels

**Question**:
HCP Terraform policy sets support three enforcement levels: `advisory`, `soft-mandatory`, and `hard-mandatory`. Which TWO statements correctly describe a contrast between these levels? (Select two.)

- A) An `advisory` policy failure displays a warning but **does not block the run** — the plan-and-apply continues regardless of the policy result; a `hard-mandatory` policy failure **always blocks the run** and cannot be overridden by any user, including organisation owners
- B) A `soft-mandatory` policy failure blocks the run by default but can be **overridden** by a user with sufficient permissions (typically an organisation owner or designated override role) — the run can then proceed despite the policy failure; `hard-mandatory` failure **cannot be overridden** by any user under any circumstances
- C) `advisory` policies only apply to speculative plans; `soft-mandatory` and `hard-mandatory` policies apply to plan-and-apply runs
- D) All three enforcement levels cause the run to fail with a non-zero exit code — the difference is only in how the failure message is displayed in the UI

**Answer**: A, B

**Explanation**:
(A) captures the contrast between the weakest and strongest levels. `advisory` is informational — the policy result is shown in the run output, but a failure is a warning only and the apply proceeds normally. `hard-mandatory` is an absolute gate — no one can override it, not even organisation owners; the run cannot proceed until the policy violation is resolved in the configuration. (B) captures the middle ground: `soft-mandatory` blocks like `hard-mandatory` initially, but designated users (typically organisation owners or a specific override role) can manually override the block and allow the run to continue. This gives organisations a "break glass" option for exceptional circumstances while still enforcing a default gate. (C) is incorrect — enforcement levels apply to plan-and-apply runs, not selectively to speculative vs. non-speculative. (D) is incorrect — `advisory` does not cause run failure.

---

### Question 11 — HCP Terraform Owner Role vs Workspace Admin Permission

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Contrast between organisation-level Owner role and workspace-level Admin permission in HCP Terraform

**Question**:
Compare the **Owner** organisation-level role with the **Admin** workspace-level permission in HCP Terraform. What is the scope difference?

- A) Owner and Admin are the same role — "Owner" is the UI label and "Admin" is the API name for the same permission level
- B) **Owner** is an **organisation-level role** — it grants full access to all settings, workspaces, teams, and governance across the entire organisation; **Admin** is a **workspace-level permission** — it grants full control over a specific workspace (managing variables, team access, workspace settings, and run approvals) but has no authority over other workspaces or organisation-level settings unless that permission is explicitly granted on each workspace
- C) Admin is a superset of Owner — an Admin can manage organisation-level settings in addition to workspace-level settings
- D) Owner role is limited to read-only access across all workspaces; Admin is the role that allows applying changes

**Answer**: B

**Explanation**:
HCP Terraform has a two-tier permission model. At the **organisation level**, there are two roles: **Owner** (full access to everything — workspaces, teams, policies, billing, SSO, audit logs, registry) and **Member** (access defined at the workspace level only). At the **workspace level**, four permissions exist: Read, Plan, Write, and Admin. Workspace **Admin** grants full control over that workspace — managing variables, configuring VCS connection, managing team access to the workspace, triggering and approving runs — but this authority is scoped to that single workspace. An operator with workspace Admin on `production` cannot modify `staging` or organisation-level policies unless they are also an organisation Owner or have Admin on `staging` separately. This granular model allows organisations to delegate workspace management without granting organisation-wide authority.

---

### Question 12 — Import Block Workflow vs CLI Import Workflow: Two Key Differences

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Contrasting TWO workflow differences between the declarative import block (Terraform 1.5+) and the legacy terraform import CLI command

**Question**:
Compare the workflow for importing an existing resource using the declarative `import` block versus the legacy `terraform import` CLI command. Which TWO statements correctly describe a difference between the two workflows? (Select two.)

- A) The `import` block workflow supports **running `terraform plan` before any state changes are made** — the engineer can preview the import (and optionally generate HCL with `-generate-config-out`) before committing; the CLI `terraform import` command **immediately writes the resource to state** with no plan preview step — there is no way to preview what will be imported before the state is modified
- B) The CLI `terraform import` command **requires an existing resource block** in the configuration before it can run — the block body can be empty, but the block must exist; the `import` block can be added to configuration and used with `terraform plan -generate-config-out=generated.tf` to have Terraform **generate the resource block automatically** — the engineer starts with only the `import` block and no resource block
- C) Both workflows require the resource block to exist before the import command runs — the `import` block offers no config generation capability
- D) The CLI `terraform import` command can import multiple resources in a single run; the `import` block can only import one resource per block

**Answer**: A, B

**Explanation**:
(A) is the most operationally significant difference: the `import` block is declarative and integrates with the standard plan/apply cycle. You can run `terraform plan` to see exactly what will be imported — and Terraform will validate that the configuration matches the resource — before a single change is made to state. The CLI command bypasses this safety net: it writes to state immediately upon execution. (B) is the second major difference: the CLI command has always required the resource block to pre-exist (otherwise Terraform has no address to write the state entry to). The `import` block enables a new workflow where you start with only the `import` block and run `terraform plan -generate-config-out=generated.tf` — Terraform queries the provider, reads the resource's current attributes, and generates a complete HCL resource block. You then review and clean up the generated file before running `terraform apply` to complete the import. (C) is incorrect — the `import` block does support config generation. (D) is incorrect — you can have multiple `import` blocks and the CLI can be scripted to run multiple times.

---

### Question 13 — Dynamic OIDC Credentials vs Static Environment Variable Credentials in HCP Terraform

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Deep contrast between dynamic OIDC-based provider authentication and static credential storage in HCP Terraform workspaces

**Question**:
Compare two approaches for authenticating an HCP Terraform workspace to AWS:

- **Approach A**: Store `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` as sensitive environment variables in an HCP Terraform variable set
- **Approach B**: Configure dynamic provider credentials using OIDC — the workspace requests a short-lived token from HCP Terraform's OIDC provider, which AWS validates against a trusted identity provider, and returns temporary credentials

What are the two most significant security differences between these approaches?

- A) Approach A is more secure because the credentials are marked sensitive and are never shown in logs; Approach B exposes credentials in the plan output because OIDC tokens are not marked sensitive
- B) In Approach A, the **static credentials are long-lived** — they persist in HCP Terraform's variable storage indefinitely and if exfiltrated (e.g., from a compromised CI system or leaked variable export) they remain valid until manually rotated; in Approach B, credentials are **short-lived and scoped per run** — they are issued fresh for each Terraform run with a TTL, so a captured credential becomes useless quickly; additionally, Approach B **eliminates static secret storage** entirely — no `AWS_ACCESS_KEY_ID` or `AWS_SECRET_ACCESS_KEY` is stored anywhere in HCP Terraform, which removes a class of secret-leakage risk
- C) Approach A credentials are encrypted using HCP Terraform's AES-256 key management; Approach B credentials are stored in plaintext because they are temporary
- D) Both approaches have equivalent security — the static credentials in Approach A are marked sensitive, which provides the same protection as OIDC's short-lived tokens

**Answer**: B

**Explanation**:
The security difference is fundamental, not cosmetic. **Approach A** stores IAM credentials that are valid indefinitely (or until manually rotated) as workspace variables. Even though HCP Terraform marks them sensitive and encrypts them at rest, they represent a persistent secret that must be managed, rotated, and protected. Any system with access to the HCP Terraform API or the workspace can potentially exfiltrate them, and a leaked key remains dangerous until it is revoked. **Approach B** (dynamic OIDC credentials) eliminates the concept of a stored secret entirely. HCP Terraform acts as an OIDC identity provider — it issues a signed JWT for each run, AWS's IAM identity provider validates the JWT, and AWS returns temporary credentials (via `AssumeRoleWithWebIdentity`) with a short TTL. These credentials are unique per run, expire automatically, and nothing persistent is stored in HCP Terraform. The residual risk of a leaked credential is minimal because it expires quickly. This architecture — often called "keyless authentication" or "workload identity" — is the recommended security model for HCP Terraform-to-cloud authentication.

---
