# Terraform Associate (004) — Question Bank Iter 3 Batch 2

**Iteration**: 3
**Iteration Style**: HCL interpretation — read a snippet and identify output/effect
**Batch**: 2
**Objective**: 2 — Terraform Fundamentals (Providers & State)
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 3 Easy / 8 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — Reading a `~> 5.0` Version Constraint

**Difficulty**: Easy
**Answer Type**: one
**Topic**: What the pessimistic constraint operator `~> 5.0` permits

**Question**:
Read this `required_providers` block:

```hcl
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}
```

What version range does `"~> 5.0"` permit for the AWS provider?

- A) Exactly version `5.0.0` only
- B) Any version >= `5.0` and < `6.0` — minor and patch updates within major version 5
- C) Any version >= `5.0.0` and < `5.1.0` — patch updates within minor version 5.0 only
- D) Any version >= `5.0` with no upper limit

**Answer**: B

**Explanation**:
The `~>` pessimistic constraint operator allows updates to the rightmost non-zero component. `~> 5.0` means >= `5.0` and < `6.0` — permitting any `5.x.x` version. This keeps the provider within major version 5 while allowing minor and patch updates. By contrast, `~> 5.0.0` would allow only patch updates (>= `5.0.0` and < `5.1.0`).

---

### Question 2 — Resolving a Short-Form Provider Source Address

**Difficulty**: Easy
**Answer Type**: one
**Topic**: How Terraform resolves a two-part provider source address to its fully qualified form

**Question**:
Read this `required_providers` block:

```hcl
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}
```

The `source` value is written as `"hashicorp/aws"`. What is the fully qualified address Terraform resolves this to?

- A) `github.com/hashicorp/aws`
- B) `registry.terraform.io/hashicorp/aws`
- C) `releases.hashicorp.com/terraform/aws`
- D) `terraform.io/providers/hashicorp/aws`

**Answer**: B

**Explanation**:
The full provider source address format is `<hostname>/<namespace>/<type>`. When the hostname is omitted — as it is in `"hashicorp/aws"` — Terraform defaults to `registry.terraform.io`. The two-part shorthand `hashicorp/aws` therefore resolves to `registry.terraform.io/hashicorp/aws`, which is the address of the official HashiCorp AWS provider on the public Terraform registry.

---

### Question 3 — `sensitive = true` on an Output Block

**Difficulty**: Easy
**Answer Type**: one
**Topic**: What `sensitive = true` does and does not protect in Terraform

**Question**:
Read this output block:

```hcl
output "rds_password" {
  value     = aws_db_instance.primary.password
  sensitive = true
}
```

A developer claims that `sensitive = true` fully protects this password from exposure in all Terraform contexts. Which statement correctly evaluates that claim?

- A) Correct — `sensitive = true` encrypts the password in both terminal output and `terraform.tfstate`
- B) Incorrect — `sensitive = true` hides the password from terminal output but it is stored in plaintext in `terraform.tfstate`
- C) Correct — `sensitive = true` prevents the password from being written to state altogether
- D) Incorrect — `sensitive = true` has no effect; the password appears in plaintext in both the terminal and the state file

**Answer**: B

**Explanation**:
`sensitive = true` on an output suppresses the value from being printed in terminal output — it appears as `(sensitive value)`. However, it provides no protection for the state file: Terraform stores **all resource attributes in plaintext** in `terraform.tfstate`, regardless of the `sensitive` flag. The raw password is visible in the state file JSON. Protecting secrets in state requires encrypting the remote backend and restricting access to the state file.

---

### Question 4 — `~> 5.0.0` vs `~> 5.0` Constraint Range

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Distinguishing patch-only from minor+patch constraints using `~>`

**Question**:
Read this `required_providers` block:

```hcl
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0.0"
    }
  }
}
```

How does the range permitted by `"~> 5.0.0"` differ from `"~> 5.0"`?

- A) There is no difference — both allow any `5.x.x` version
- B) `"~> 5.0.0"` allows a wider range — it permits `5.x` and `6.x` versions
- C) `"~> 5.0.0"` allows a narrower range — only patch updates are permitted (>= `5.0.0` and < `5.1.0`)
- D) `"~> 5.0.0"` is identical to `"= 5.0.0"` and pins exactly version `5.0.0`

**Answer**: C

**Explanation**:
The `~>` operator allows updates to the rightmost component specified. `"~> 5.0"` has two components, so the rightmost (`0`) can increment — allowing `5.1`, `5.2`, ..., but not `6.0`. `"~> 5.0.0"` has three components, so only the last (`0`) can increment — allowing `5.0.1`, `5.0.2`, ..., but not `5.1.0`. This patch-only behaviour is common when stricter version pinning is required.

---

### Question 5 — Default Provider Selection When Alias Exists

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Which provider configuration is used by a resource that omits the `provider` argument

**Question**:
Read this configuration:

```hcl
provider "aws" {
  region = "us-east-1"
}

provider "aws" {
  alias  = "west"
  region = "us-west-2"
}

resource "aws_instance" "api" {
  ami           = "ami-0abc1234"
  instance_type = "t3.micro"
}
```

In which AWS region will `aws_instance.api` be created?

- A) `us-west-2` — Terraform uses the most recently declared provider configuration
- B) `us-east-1` — resources with no `provider` argument use the default (unaliased) provider configuration
- C) Terraform returns an error because the `provider` argument is required when multiple configurations of the same provider exist
- D) Both regions simultaneously — Terraform creates one instance per provider configuration

**Answer**: B

**Explanation**:
When multiple configurations of the same provider are declared (one default, one or more aliased), resources that omit the `provider` argument are automatically assigned to the **default (unaliased)** configuration. The aliased provider (`aws.west`) is only used by resources that explicitly declare `provider = aws.west`. Here, `aws_instance.api` has no `provider` argument, so it uses the default `"us-east-1"` configuration.

---

### Question 6 — Lock File `hashes` Field Purpose

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What the `hashes` entries in `.terraform.lock.hcl` provide

**Question**:
Read this `.terraform.lock.hcl` snippet:

```hcl
provider "registry.terraform.io/hashicorp/aws" {
  version     = "5.31.0"
  constraints = "~> 5.0"
  hashes = [
    "h1:abc123...",
    "zh:def456...",
  ]
}
```

What do the `hashes` entries in this lock file provide?

- A) The SHA-256 hash of the Terraform state file at the time the provider was last used
- B) Cryptographic checksums that allow Terraform to verify the downloaded provider binary has not been tampered with
- C) A record of which team member last ran `terraform init` to install this provider version
- D) Encoded authentication tokens used to download the provider from the registry

**Answer**: B

**Explanation**:
The `hashes` field in `.terraform.lock.hcl` contains cryptographic checksums (in both `h1:` and `zh:` formats) of the provider binary for each supported platform. When a team member runs `terraform init` with the lock file present, Terraform verifies that the downloaded binary matches these hashes. This prevents supply-chain attacks where a malicious version of a provider could be silently substituted for the expected one.

---

### Question 7 — State JSON `id` Field Role

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Identifying which field in `terraform.tfstate` links the config address to the real cloud resource

**Question**:
Read this `terraform.tfstate` excerpt:

```json
{
  "resources": [
    {
      "type": "aws_instance",
      "name": "db_server",
      "instances": [
        {
          "attributes": {
            "id": "i-0aaa1111bb2222cc3",
            "ami": "ami-0def5678",
            "instance_type": "t3.large",
            "private_ip": "10.0.1.5"
          }
        }
      ]
    }
  ]
}
```

Which field in this state excerpt is the primary link between the Terraform resource address `aws_instance.db_server` and the actual AWS cloud resource?

- A) `instance_type` — this uniquely identifies the resource family in AWS
- B) `ami` — AWS uses the AMI ID to track running instances
- C) `id` — `"i-0aaa1111bb2222cc3"` is the AWS-assigned instance ID that Terraform uses to reference the real resource in API calls
- D) `name` — `"db_server"` is registered as the unique identifier in the AWS API

**Answer**: C

**Explanation**:
The `id` field in a Terraform state resource entry is the cloud provider's unique identifier for the real resource — in this case, the AWS EC2 instance ID `i-0aaa1111bb2222cc3`. Terraform uses this ID in all subsequent provider API calls (reads, updates, deletes) to reference the correct real-world resource. Without this mapping, Terraform would have no way to associate a config-level name like `aws_instance.db_server` with the actual instance running in AWS.

---

### Question 8 — Provider Alias Reference on a Resource

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What `provider = aws.west` on a resource block instructs Terraform to do

**Question**:
Read this configuration:

```hcl
provider "aws" {
  region = "us-east-1"
}

provider "aws" {
  alias  = "west"
  region = "us-west-2"
}

resource "aws_instance" "replica" {
  provider      = aws.west
  ami           = "ami-0xyz"
  instance_type = "t3.micro"
}
```

What does `provider = aws.west` on the `aws_instance.replica` resource accomplish?

- A) It creates the instance in both `us-east-1` and `us-west-2` simultaneously
- B) It instructs Terraform to use the aliased provider configuration with `region = "us-west-2"` for this specific resource
- C) It overrides the provider's configuration at runtime to use a custom region named `aws.west`
- D) It causes a validation error because the `provider` argument cannot be set on individual resources — only at module level

**Answer**: B

**Explanation**:
`provider = aws.west` explicitly assigns `aws_instance.replica` to the aliased provider configuration that has `alias = "west"` and `region = "us-west-2"`. Without this argument, the resource would use the default (unaliased) `us-east-1` configuration. This per-resource provider assignment is the standard pattern for multi-region architectures where different resources must be created in different regions using the same provider type.

---

### Question 9 — Lock File Content After `terraform init`

**Difficulty**: Medium
**Answer Type**: many
**Topic**: What two pieces of information `.terraform.lock.hcl` records per provider

**Question**:
Read this `required_providers` block:

```hcl
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
    google = {
      source  = "hashicorp/google"
      version = "~> 5.0"
    }
  }
}
```

After running `terraform init`, which TWO statements correctly describe what is recorded in `.terraform.lock.hcl`? (Select two.)

- A) The lock file records the exact version installed for each provider (e.g., `5.31.0` for AWS and `5.12.0` for Google)
- B) The lock file records the full source address for each provider (e.g., `registry.terraform.io/hashicorp/aws`)
- C) The lock file records the Terraform workspace name active at the time `init` was run
- D) The lock file records the path to the `.terraform/providers/` cache directory on the local machine

**Answer**: A, B

**Explanation**:
`.terraform.lock.hcl` records three things per provider: the **exact version** installed, the **version constraint** from `required_providers`, and **cryptographic hashes** for verification. It also uses the **full source address** (e.g., `registry.terraform.io/hashicorp/aws`) as the key for each provider entry. Workspace names and local filesystem paths are not stored in the lock file. Committing this file ensures all team members install the same provider versions.

---

### Question 10 — Reading the `!=` Version Constraint

**Difficulty**: Medium
**Answer Type**: one
**Topic**: What the `!=` version constraint operator excludes

**Question**:
Read this `required_providers` block:

```hcl
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "!= 5.0.0"
    }
  }
}
```

What does this version constraint tell `terraform init` to do?

- A) Install only version `5.0.0` of the AWS provider
- B) Install any available AWS provider version except `5.0.0`
- C) This is a syntax error — the `!=` operator is not supported in provider version constraints
- D) Exclude the entire `5.x` version family from consideration

**Answer**: B

**Explanation**:
The `!=` operator is a valid Terraform version constraint that excludes a single specific version. `"!= 5.0.0"` tells Terraform to install any available AWS provider version except exactly `5.0.0` — for example, `4.67.0`, `5.0.1`, or `5.31.0` would all be acceptable. This operator is typically combined with other constraints (e.g., `>= 5.0, != 5.0.0`) to avoid a known broken release while still allowing other versions in a range.

---

### Question 11 — State Attributes Displayed by `terraform state show`

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Interpreting a state JSON excerpt to identify what `terraform state show` returns

**Question**:
Read this `terraform.tfstate` excerpt:

```json
{
  "type": "aws_instance",
  "name": "db_server",
  "instances": [
    {
      "attributes": {
        "id": "i-0aaa1111bb2222cc3",
        "ami": "ami-0def5678",
        "instance_type": "t3.large",
        "private_ip": "10.0.1.5",
        "public_ip": "52.1.2.3"
      }
    }
  ]
}
```

Which TWO statements correctly describe what `terraform state show aws_instance.db_server` would display? (Select two.)

- A) The command displays the state-tracked attributes of the instance, including `id`, `ami`, `instance_type`, `private_ip`, and `public_ip` as recorded in state
- B) The `id` value `"i-0aaa1111bb2222cc3"` is the AWS-assigned instance ID visible in the state show output
- C) The command displays only the `id` attribute — all other attributes require `terraform refresh` before they appear in `state show`
- D) `terraform state show` retrieves the instance's current live attributes from the AWS API, not from the state file

**Answer**: A, B

**Explanation**:
`terraform state show <address>` reads from the state file and displays all state-tracked attributes for the specified resource. It does not make live API calls — it shows the attributes as they were last recorded in state. From this excerpt, the output would include `id`, `ami`, `instance_type`, `private_ip`, and `public_ip`, along with `id = "i-0aaa1111bb2222cc3"` as the primary cloud identifier. Option C is incorrect (all attributes are shown, not just `id`); Option D is incorrect (`state show` reads from state, not from a live API refresh).

---

### Question 12 — Comparing Two `~>` Constraints

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Identifying the precise version ranges permitted by two different pessimistic constraints

**Question**:
Read these two version constraint values:

```hcl
# Constraint A
version = "~> 4.0"

# Constraint B
version = "~> 4.67.0"
```

Which TWO statements correctly describe the version range each constraint allows? (Select two.)

- A) Constraint A (`~> 4.0`) allows versions >= `4.0` and < `5.0` — any `4.x` minor or patch version
- B) Constraint A (`~> 4.0`) allows versions >= `4.0` and < `4.1` — only `4.0.x` patch versions
- C) Constraint B (`~> 4.67.0`) allows versions >= `4.67.0` and < `4.68.0` — only patch updates within `4.67`
- D) Constraint B (`~> 4.67.0`) allows versions >= `4.67.0` and < `5.0` — any version from `4.67` onward within major 4

**Answer**: A, C

**Explanation**:
`~>` allows increments to the rightmost version component. `~> 4.0` has two parts — the rightmost (`0`) can increment, so minor versions change: >= `4.0`, < `5.0`. `~> 4.67.0` has three parts — the rightmost (`0` in patch position) can increment, so only the patch changes: >= `4.67.0`, < `4.68.0`. Option B is incorrect (that would be the behaviour of `~> 4.0.0`). Option D is incorrect (stopping at `4.68.0`, not `5.0`).

---

### Question 13 — Lock File After `terraform init -upgrade`

**Difficulty**: Hard
**Answer Type**: one
**Topic**: What fields change in `.terraform.lock.hcl` when a newer provider version is installed via `-upgrade`

**Question**:
Read this `.terraform.lock.hcl` entry:

```hcl
provider "registry.terraform.io/hashicorp/aws" {
  version     = "5.10.0"
  constraints = "~> 5.0"
  hashes = [
    "h1:oldhash...",
  ]
}
```

AWS provider `5.31.0` has since been released. `terraform init -upgrade` is run. What does the lock file look like after the command completes?

- A) Nothing changes — the lock file is read-only and can only be modified by deleting and regenerating it
- B) The `version` field updates to `5.31.0` and the `hashes` field updates to reflect the new binary's checksums; `constraints` remains `"~> 5.0"` unchanged
- C) The `constraints` field changes to `"= 5.31.0"` to pin the exact new version; `version` and `hashes` also update
- D) The `version` field updates to `5.31.0` but the `hashes` field remains unchanged because hashes are stable across versions

**Answer**: B

**Explanation**:
`terraform init -upgrade` re-evaluates the version constraint and installs the newest available version within the constraint range. For `~> 5.0`, `5.31.0` qualifies (>= `5.0`, < `6.0`). Terraform updates the lock file with the new exact `version = "5.31.0"` and the corresponding `hashes` for the new binary. The `constraints` field is never modified by `init -upgrade` — it always reflects the constraint as written in `required_providers`. The old hashes are replaced because they are specific to the `5.10.0` binary.

---
