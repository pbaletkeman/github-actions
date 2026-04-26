# Terraform Associate (004) — Question Bank Iter 6 Batch 3

**Iteration**: 6
**Iteration Style**: Scenario-based troubleshooting — an engineer encounters a problem or unexpected outcome; identify the most likely cause and correct resolution
**Batch**: 3
**Objective**: 3 — Core Workflow & CLI
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 3 Easy / 8 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — CI Pipeline Fails Because Files Are Not Formatted

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Using terraform fmt -check in CI to enforce formatting without writing files

**Question**:
A team's CI pipeline runs `terraform fmt` as a formatting gate. A developer notices that every time the pipeline runs, it reformats the committed files and then reports a diff that must be manually committed. The team wants the pipeline to **report a failure** when files are unformatted rather than silently rewrite them. What is the correct flag to use?

- A) `terraform fmt -dry-run` — preview changes without writing to disk
- B) `terraform fmt -no-write` — display unformatted files without modifying them
- C) `terraform fmt -check` — this flag makes `terraform fmt` **exit with a non-zero exit code** if any `.tf` files need reformatting, without writing any changes to disk; the CI pipeline fails on the non-zero exit, prompting the developer to format their code locally and recommit; this is the standard CI usage of `fmt`
- D) `terraform fmt -validate` — combines formatting and validation in a single pass

**Answer**: C

**Explanation**:
`terraform fmt -check` is the CI-friendly form of the command. Without `-check`, `terraform fmt` rewrites files in place — useful locally but counterproductive in CI where you want to fail fast and tell the developer to fix their code, not silently patch it in the pipeline. With `-check`, Terraform reads each `.tf` file, computes what the canonical format would be, and exits with code `1` if any file differs — without making any changes. The pipeline sees the non-zero exit code and reports a failure. Most teams also use `-recursive` to catch files in subdirectories: `terraform fmt -check -recursive`. Developers fix formatting locally (by running `terraform fmt` without `-check`) and push the corrected files.

---

### Question 2 — `terraform validate` Passes But `terraform plan` Fails

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Understanding the boundary between what validate checks (static) and what plan checks (live)

**Question**:
An engineer runs `terraform validate` on a configuration that references an AMI ID: `ami = "ami-0deadbeef1234567"`. Validation passes. They then run `terraform plan` and receive an error: `InvalidAMIID.NotFound: The image id '[ami-0deadbeef1234567]' does not exist`. The engineer is confused — they thought `validate` would catch this. What explains the discrepancy?

- A) `terraform validate` only checks files in the root module; AMI references in child modules are skipped
- B) `terraform validate` is **fully offline and makes no API calls** — it checks HCL syntax and static references but cannot verify whether cloud resources such as AMI IDs actually exist; `terraform plan` calls the AWS provider API, which verifies the AMI ID against the AWS catalogue and returns the error when it is not found; `validate` passing is correct — it cannot know the AMI is invalid without querying AWS
- C) `terraform validate` found the error but suppressed it because `ami` is a computed attribute
- D) `terraform plan` is stricter than `validate` because it re-parses the `.tf` files from scratch

**Answer**: B

**Explanation**:
`terraform validate` operates entirely without network access or provider credentials. It checks whether HCL syntax is correct, all references resolve to declared symbols, argument names are valid for each resource type, and types are compatible. It cannot check whether a specific value (like an AMI ID) actually exists in the cloud — that requires a live API call. `terraform plan` invokes the provider plugin, which in turn calls the AWS EC2 API to look up the AMI ID. When the API returns a "not found" error, Terraform surfaces it as a plan failure. This is expected and correct: `validate` is a fast, offline sanity check; live resource existence is validated during planning.

---

### Question 3 — Apply Prompt Appears Unexpectedly in Automation

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Using -auto-approve to suppress the interactive apply confirmation in CI/CD

**Question**:
An engineer sets up a CI/CD pipeline that runs `terraform apply` as the final step. The pipeline hangs indefinitely after printing the execution plan, waiting for user input. The engineer needs the apply to proceed automatically without prompting. What is the correct fix?

- A) Set the environment variable `TF_APPLY_AUTO=true` before running `terraform apply`
- B) Run `terraform apply -no-prompt` to skip the confirmation dialog
- C) Add **`-auto-approve`** to the `terraform apply` command — this flag suppresses the interactive confirmation prompt and applies the plan immediately; it is the standard flag for non-interactive environments such as CI/CD pipelines; without it, `terraform apply` always pauses and waits for `yes` before proceeding
- D) Use `terraform apply -force` to bypass the confirmation check

**Answer**: C

**Explanation**:
By default, `terraform apply` displays the execution plan and then pauses with the prompt `Do you want to perform these actions? Enter a value:`. This interactive step is intentional for human review but causes CI/CD pipelines to hang indefinitely because no one is there to type `yes`. The `-auto-approve` flag tells Terraform to skip the confirmation step and proceed directly with the apply. It is the standard flag for automated pipelines. Teams typically combine it with a saved plan file for fully deterministic CI runs: `terraform plan -out=plan.tfplan` in one stage, then `terraform apply plan.tfplan` (which does not prompt, because a saved plan file is provided) in the apply stage. `-auto-approve` is appropriate in that two-stage pattern as well.

---

### Question 4 — `-target` Used Regularly Causes State Drift

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Understanding why routine use of -target is an anti-pattern and what problems it causes

**Question**:
An engineer discovers they can run `terraform apply -target=aws_instance.web` to apply changes faster, skipping the other 40 resources in the configuration. They start using `-target` routinely for every change. A senior engineer warns them this is dangerous. What is the most likely problem this practice will cause over time?

- A) Using `-target` more than three times in a row permanently locks the state file
- B) `terraform apply -target` does not update the state file for the targeted resource — changes are applied to the cloud but not recorded
- C) **Routine use of `-target` causes state drift**: when only a subset of resources is planned and applied, Terraform does not evaluate the full dependency graph; changes to the targeted resource may leave dependent resources in an inconsistent or stale state that Terraform is unaware of; over time, the state file diverges from the full desired configuration, and a full `terraform apply` may plan unexpected changes or errors because the inter-resource relationships were never reconciled; `-target` is intended only for emergency recovery or one-off debugging, not routine use
- D) `-target` is deprecated in Terraform 1.x and will be removed in a future version

**Answer**: C

**Explanation**:
`terraform apply -target` is a surgical tool for emergency situations — for example, applying a fix to one broken resource while the rest of the plan has unrelated unresolved issues. HashiCorp explicitly warns against using it in normal workflows. The problem is that Terraform's dependency graph ensures resources are applied in the correct order and that dependent resources are updated when their dependencies change. When `-target` is used routinely, these cross-resource relationships are never evaluated fully. A change to a security group might affect five EC2 instances — applying only the security group leaves those instances in an unchecked state. The next full `terraform apply` may then produce a large and confusing plan because weeks of targeted applies never resolved the complete desired state. The correct practice is to always run full plans, and fix the root cause of issues that make full plans unworkable.

---

### Question 5 — Saved Plan File Applied After Configuration Change

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Understanding that a saved plan file is tied to the state and config at the time it was created

**Question**:
An engineer runs `terraform plan -out=plan.tfplan` and saves the plan. Before running `terraform apply plan.tfplan`, a colleague merges a pull request that adds a new resource to the configuration. The engineer applies the saved plan. What is the most likely problem?

- A) Terraform detects the configuration change and automatically re-plans before applying
- B) The apply fails immediately with a "plan file is out of date" error that prevents any changes from being made
- C) The **saved plan was generated against the configuration and state at the time `terraform plan` was run** — it does not include the new resource added by the colleague's PR; applying it will provision only the changes in the saved plan, not the new resource; the new resource will not be created until a fresh `terraform plan` and `terraform apply` are run; in some versions of Terraform, if the state has diverged significantly from the plan's recorded state, Terraform will also emit a warning or error
- D) The apply creates the new resource automatically because Terraform reads the current configuration files during `terraform apply plan.tfplan`

**Answer**: C

**Explanation**:
A saved plan file (`.tfplan`) captures a complete snapshot of the proposed changes at the moment `terraform plan -out` is run — the configuration, state, and intended cloud operations are all frozen into that file. Applying a saved plan does not re-read the current `.tf` files; it executes exactly what was planned. Changes made to configuration files after the plan was saved are therefore invisible to `terraform apply plan.tfplan`. The new resource will be absent from the apply. This is actually a feature in controlled pipelines (the plan is reviewed and then applied exactly as reviewed), but it requires discipline: if configuration changes occur between plan and apply, a new plan must be generated. CI/CD pipelines should always treat the plan and apply as a single atomic pipeline stage.

---

### Question 6 — `terraform graph` Output Is Unreadable

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Understanding that terraform graph outputs DOT format and requires an external tool to render

**Question**:
An engineer runs `terraform graph` to visualise the resource dependency graph. The terminal output is a large block of text that looks like this:

```
digraph {
  compound = "true"
  newrank  = "true"
  subgraph "root" {
    "[root] aws_instance.web (expand)" -> "[root] aws_security_group.allow_ssh (expand)"
  }
}
```

The engineer expects a visual diagram but sees only text. What is the correct explanation and next step?

- A) The engineer must run `terraform graph -render` to trigger the visual rendering
- B) `terraform graph` outputs the graph in **DOT format** — a plain-text graph description language used by the Graphviz tool; the text output is correct and expected; to render it as a visual diagram, the engineer should pipe the output to Graphviz: `terraform graph | dot -Tsvg > graph.svg` and then open `graph.svg` in a browser or image viewer
- C) The output indicates a circular dependency in the configuration — the `->` arrows show the cycle that must be fixed
- D) The DOT output is only generated when there are dependency errors; normal graphs are rendered as PNGs automatically

**Answer**: B

**Explanation**:
`terraform graph` produces output in the DOT language, which is a plain-text notation for directed graphs used by the Graphviz toolset. The raw output is not intended to be read directly — it describes nodes and edges in a structured text format. To produce a human-readable visual, the output must be passed to a Graphviz rendering tool such as `dot` (part of the Graphviz package). The standard pipeline is: `terraform graph | dot -Tsvg > graph.svg`. Other output formats are also supported: `-Tpng` for PNG, `-Tpdf` for PDF. Once rendered, the graph shows resource nodes connected by dependency arrows, making it easy to understand the apply order Terraform will use. This is primarily a debugging and documentation tool rather than a routine workflow step.

---

### Question 7 — `terraform output` Returns Nothing After Apply

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Understanding why terraform output returns nothing when no output blocks are declared

**Question**:
An engineer provisions infrastructure with `terraform apply`. After apply completes, they run `terraform output` expecting to see resource IDs and IP addresses, but the command prints nothing. The engineer is confused because the apply succeeded. What is the most likely cause?

- A) `terraform output` requires the `-state` flag to point to the state file — without it, the command cannot find the output values
- B) Output values are only available for 24 hours after apply; running the command the next day always returns nothing
- C) The engineer's configuration has **no `output` blocks declared** — `terraform output` can only display values that are explicitly defined in `output` blocks in the configuration; Terraform does not automatically expose resource attributes as outputs; to see the instance ID, the engineer must add an `output` block such as `output "instance_id" { value = aws_instance.web.id }` and re-apply
- D) The outputs were cleared because `terraform apply` was run with `-auto-approve`

**Answer**: C

**Explanation**:
`terraform output` reads from the `outputs` section of the state file. That section is only populated when the configuration contains `output` blocks. Terraform does not automatically export resource attributes — every value the engineer wants accessible via `terraform output` must be explicitly declared. This is by design: outputs are a structured interface for surfacing specific values (IDs, endpoints, IP addresses) without exposing the entire resource object. The fix is to add the desired `output` blocks to the configuration and run `terraform apply` (or `terraform refresh`) so the values are written to state. After that, `terraform output instance_id` will return the value.

---

### Question 8 — Plan Shows Resource Replaced When Only a Tag Changed

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Understanding that certain attributes force resource replacement and how to recognise this in plan output

**Question**:
An engineer changes only the `Name` tag on an RDS database instance and runs `terraform plan`. They expect to see `1 to change` (an in-place update) but instead see `-/+ 1 to add, 1 to destroy` with the note `# forces replacement`. The engineer is alarmed — they did not intend to recreate the database. What is the most likely explanation?

- A) Changing a tag on any AWS resource always forces a full replacement in Terraform — tag changes are never applied in place
- B) The engineer accidentally changed the `engine_version` attribute instead of the tag; engine version changes always force replacement
- C) **Some resource attributes are marked `ForceNew` in the provider schema** — changing them requires the resource to be destroyed and recreated because the underlying cloud API does not support in-place updates for that attribute; however, `Name` tags on RDS instances are not normally `ForceNew`; the most likely explanation is that the engineer also changed (intentionally or accidentally) a `ForceNew` attribute such as `identifier`, `engine`, `engine_version`, or `db_subnet_group_name` alongside the tag change; the plan output's `# forces replacement` note will list the specific attribute responsible
- D) `-/+` in the plan always means the engineer has changed more than five attributes at once, triggering a safety replacement

**Answer**: C

**Explanation**:
`-/+` (destroy then create) appears when one or more of the changed attributes are `ForceNew` in the provider's schema — meaning the underlying cloud API cannot update them on a live resource and Terraform must destroy and recreate it to apply the change. Tag changes on RDS instances are normally in-place updates (`~`), not replacements. The most reliable way to diagnose why `-/+` is appearing is to read the full plan output carefully: the specific attribute that `# forces replacement` will be identified by name in the plan. Common `ForceNew` attributes on RDS instances include `identifier`, `engine`, `engine_version`, `db_subnet_group_name`, `availability_zone`, and `allocated_storage` (in some configurations). The engineer should scroll up in the plan output to find the `# forces replacement` annotation on the specific attribute line.

---

### Question 9 — `terraform console` Used to Debug an Unexpected Expression

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Using terraform console to test and debug HCL expressions interactively before embedding in config

**Question**:
An engineer writes a complex `for` expression in their configuration but is unsure whether it produces the expected output. They keep running `terraform plan` to test it, but the plan is slow because it refreshes 300 resources each time. What is the most efficient way to test the expression in isolation?

- A) Run `terraform validate --expression` to evaluate and print the result of a single HCL expression
- B) Create a separate test `.tf` file with just the expression and run `terraform apply` on it in an empty workspace
- C) Use **`terraform console`** — an interactive REPL (Read-Eval-Print Loop) that evaluates HCL expressions against the current state and variable values without running a full plan or making any provider API calls; the engineer can type the `for` expression directly at the prompt and see the result immediately, iterating quickly until the output is correct before embedding it in the configuration
- D) Use `terraform output --eval` to evaluate an expression without declaring an output block

**Answer**: C

**Explanation**:
`terraform console` is the correct tool for this task. It opens an interactive session where any valid HCL expression can be typed and evaluated immediately. It reads the current state file (so `resource.type.name.attribute` references resolve to actual state values) and has access to all declared variables, locals, and functions. Critically, it does **not** run a plan, does not refresh provider state, and makes no API calls — it is very fast. The engineer can iteratively test the `for` expression, adjusting the logic and immediately seeing the result, without triggering a full plan cycle. Once the expression produces the expected output, it can be copied directly into the configuration. This is also useful for testing string interpolation, function calls (`cidrsubnet()`, `lookup()`, `merge()`), and type conversions before using them in resources.

---

### Question 10 — `terraform destroy` Run in Wrong Workspace

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Understanding terraform workspaces and the risk of running destructive commands in the wrong workspace

**Question**:
An engineer intends to destroy the `staging` environment. They run `terraform destroy -auto-approve` but realise halfway through that they are in the `default` workspace, which corresponds to the **production** environment. What TWO immediate actions should the engineer take? (Select two.)

**Answer Type**: many

- A) **Run `terraform apply -auto-approve` immediately** — `terraform apply` in the same workspace will recreate all the destroyed resources from the configuration files
- B) Run `terraform workspace select staging` to move to the correct workspace — this does not undo the destruction already performed in the `default` workspace
- C) Escalate to the team and use the **remote backend's state version history** (if available) to identify exactly which resources were destroyed and what their configuration was, then prioritise restoring production resources — either by re-running `terraform apply` or by recovering from backup depending on the resource type
- D) Run `terraform plan -destroy` — this re-creates the destroy plan and effectively reverses the previous destroy

**Answer**: A, C

**Explanation**:
This is a production incident scenario. **(A)** Running `terraform apply -auto-approve` in the same (`default`) workspace will re-plan against the current configuration and recreate any resources that were destroyed and are still declared in the configuration — this is the fastest path to restoring declaratively managed resources. For resources like EC2 instances and load balancers, this can restore service quickly. **(C)** Not all resources can be trivially recreated (databases, stateful storage, DNS records with downstream dependencies) — escalation and using state version history to understand the exact blast radius is essential for a structured recovery. (B) switching workspace is correct procedure for future work but does not help restore production. (D) `terraform plan -destroy` generates a destroy plan, not a creation plan — it would make the situation worse, not better.

---

### Question 11 — `terraform apply -replace` vs Deleting from Config

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Understanding terraform apply -replace as the correct way to force-recreate a resource without removing it from config

**Question**:
An EC2 instance has become corrupted. An engineer needs to destroy and recreate it using its current Terraform configuration, without changing any configuration attributes. A colleague suggests removing the resource block from the configuration, running `terraform apply`, and then adding it back and running `terraform apply` again. What is the problem with this approach, and what is the correct alternative?

- A) There is no problem — removing and re-adding a resource block is the standard Terraform procedure for recreating a resource
- B) The two-step remove-and-add approach has two significant problems: (1) removing the block plans a destroy, which deletes the instance — this is correct — but (2) re-adding the block then plans a **create of a brand-new instance** which has a **different resource ID, IP, and configuration** than the original; this process also requires two separate apply operations, increasing the window for errors; the correct approach is **`terraform apply -replace=aws_instance.web`**, which plans a single destroy-then-recreate (shown as `-/+` in the plan) in one apply operation, preserving all configuration attributes and minimising the outage window
- C) The approach is correct but only works if `lifecycle { create_before_destroy = true }` is set on the resource block
- D) The two-step approach is required because `terraform apply -replace` is only available in Terraform Enterprise

**Answer**: B

**Explanation**:
`terraform apply -replace=<address>` (introduced in Terraform 1.0) is the purpose-built command for force-recreating a specific resource. It generates a `-/+` plan for that resource — destroy followed immediately by create — in a single apply, using the current configuration attributes. The two-step approach (remove from config → apply → re-add → apply) technically achieves the same result but is inferior for several reasons: it requires two separate apply operations (doubling the change windows and state write events), the second apply creates a new resource with the same configuration but a new cloud resource ID, and the pattern is error-prone if the engineer forgets to re-add the block or makes a typo. Using `-replace` is cleaner, faster, and the HashiCorp-recommended approach for this scenario.

---

### Question 12 — `terraform output -raw` vs `terraform output` for Scripting

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Understanding why -raw is needed when using terraform output in shell scripts

**Question**:
An engineer writes a deployment script that retrieves a database endpoint from Terraform outputs:

```bash
DB_HOST=$(terraform output db_endpoint)
psql -h "$DB_HOST" -U admin mydb
```

The `psql` connection fails with: `could not translate host name '"db.example.com"' to address`. The engineer notices the hostname has double-quote characters around it. What is the cause and fix?

- A) The output block must include `quote = false` to suppress quote characters in the returned value
- B) The `terraform output` command returns values in **HCL-formatted strings with surrounding double quotes** by default (e.g., `"db.example.com"`); when this is captured in a shell variable, the quotes become part of the string value passed to `psql`; the fix is to use **`terraform output -raw db_endpoint`** — the `-raw` flag returns the value as a plain string without any surrounding quotes or newline formatting, making it safe for direct use in shell scripts and command substitutions
- C) The issue is that `psql` does not accept hostnames from environment variables — the engineer should hard-code the hostname in the script
- D) The output value must be marked `sensitive = true` to strip the formatting characters before shell capture

**Answer**: B

**Explanation**:
`terraform output <name>` returns a human-readable formatted value. For string outputs, this includes surrounding double quotes: `"db.example.com"`. When captured with `$(...)` in a shell script, the quotes are included in the variable, so `$DB_HOST` becomes `"db.example.com"` (with literal quote marks). Tools like `psql`, `curl`, and `ssh` receive this quoted string rather than the bare hostname, causing lookup failures. `terraform output -raw <name>` solves this by returning the raw string value with no surrounding quotes and no trailing newline — exactly what shell variable capture needs for downstream use. `-raw` only works for string-type outputs; for complex types (lists, maps), use `terraform output -json` and parse with `jq`.

---

### Question 13 — `terraform plan -destroy` Unexpected Scope

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Understanding that terraform plan -destroy plans destruction of ALL resources, not just unused ones

**Question**:
An engineer runs `terraform plan -destroy` expecting it to plan the removal of only the resources that are no longer in the configuration (i.e., orphaned resources). Instead, the plan shows all 47 managed resources will be destroyed. The engineer panics and asks: "Is something wrong?" What is the correct explanation?

- A) `terraform plan -destroy` has detected that all 47 resources are misconfigured and must be replaced — the engineer should review each resource
- B) `terraform plan -destroy` plans the **destruction of ALL resources currently tracked in state**, regardless of whether they are in the configuration or not — it is equivalent to removing every resource block and running `terraform plan`; it is used when the intention is to completely tear down the environment; it does not selectively destroy only orphaned resources; to remove only resources that have been removed from configuration, the engineer should simply run a normal `terraform plan` and look for any `-` (destroy) entries
- C) Something is wrong with the state file — `terraform plan -destroy` should never show more than 10 resources
- D) The engineer accidentally appended `-destroy` to their `terraform plan -target` command, overriding the target scope

**Answer**: B

**Explanation**:
`terraform plan -destroy` (equivalent to `terraform destroy` without applying) generates a plan to destroy **every resource Terraform currently tracks in state**. It is the preview of what `terraform destroy` would do. It is not a selective or differential command — it destroys everything managed in the current workspace's state. For reviewing what would be removed by a configuration change (removed resource blocks), the engineer should run a standard `terraform plan` and look for resources marked with `-` (destroy). Those are the orphaned resources whose blocks have been removed. `terraform plan -destroy` is appropriate for environment teardown scenarios: decommissioning a staging environment, cleaning up a feature branch, or verifying what a full destroy would affect before committing to it.

---
