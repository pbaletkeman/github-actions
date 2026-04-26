# Terraform Associate (004) — Question Bank Iter 3 Batch 7

**Iteration**: 3
**Iteration Style**: HCL interpretation — read a snippet and identify output/effect
**Batch**: 7
**Objective**: 5 — Modules + 6 — State Backends
**Generated**: 2026-04-25
**Total Questions**: 13
**Difficulty Split**: 3 Easy / 8 Medium / 2 Hard
**Answer Types**: 10 `one` / 3 `many` / 0 `none`

---

## Questions

---

### Question 1 — S3 Backend `encrypt = true`

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Reading an S3 backend block to identify what `encrypt = true` protects

**Question**:
Read this backend configuration:

```hcl
terraform {
  backend "s3" {
    bucket         = "acme-tf-state"
    key            = "production/terraform.tfstate"
    region         = "us-east-1"
    dynamodb_table = "terraform-locks"
    encrypt        = true
  }
}
```

What does the `encrypt = true` argument configure?

- A) It enables TLS encryption for the network connection between Terraform and S3 — required to prevent data interception in transit
- B) It instructs Terraform to encrypt the state file in memory before writing it; the object stored in S3 remains plaintext
- C) It enables server-side encryption (SSE) on the state object stored in S3, so the file is encrypted at rest in the bucket
- D) It generates and rotates a local encryption key stored in `.terraform/` that wraps all sensitive values

**Answer**: C

**Explanation**:
`encrypt = true` in the S3 backend block enables **server-side encryption at rest** for the state object stored in S3. By default this uses SSE-S3 (AES-256 managed by S3). It does not configure the TLS connection (S3 always uses TLS for API calls regardless of this argument), does not encrypt in memory, and does not involve local key material. This is a recommended best-practice argument when storing Terraform state in S3 because state files may contain plaintext sensitive values.

---

### Question 2 — Module Source Git `?ref=` Query Parameter

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Reading a Git-based module source URL to identify what `?ref=v2.0.0` pins

**Question**:
Read this module block:

```hcl
module "vpc" {
  source = "git::https://github.com/acme/terraform-modules.git//modules/vpc?ref=v2.0.0"
}
```

What does the `?ref=v2.0.0` query parameter specify?

- A) It specifies the subdirectory within the repository to use as the module root — equivalent to the `//` separator
- B) It sets a registry version constraint equivalent to `version = "~> 2.0"` for the module
- C) It pins the module source to the `v2.0.0` git ref — which may be a tag, branch name, or commit SHA
- D) It instructs `terraform init` to validate that a GitHub Release named `v2.0.0` exists before downloading

**Answer**: C

**Explanation**:
For Git-based module sources, the `?ref=` query parameter pins the checkout to a specific **git ref** — a tag, branch name, or commit SHA. Here, `?ref=v2.0.0` checks out the `v2.0.0` tag. This is the Git-source equivalent of the `version` argument used with Terraform Registry sources (which cannot be used with Git URLs). The `//modules/vpc` portion before `?ref=` is the **double-slash subdirectory separator** — these are two separate mechanisms within the same URL.

---

### Question 3 — Module Output Reference with List Index

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Reading a root module resource that indexes a child module's list output

**Question**:
Read this configuration:

```hcl
# modules/networking/outputs.tf
output "public_subnet_ids" {
  value = aws_subnet.public[*].id
}

# root main.tf
resource "aws_instance" "web" {
  ami       = "ami-0abc123"
  subnet_id = module.networking.public_subnet_ids[0]
}
```

What does `module.networking.public_subnet_ids[0]` evaluate to?

- A) The integer `0` — the index is returned as a number when applied to a module output reference
- B) The first element of the `public_subnet_ids` list output from the `networking` module — the subnet ID string of the first public subnet
- C) The entire `public_subnet_ids` list — the `[0]` notation on a module output selects the full output, not a single element
- D) An error — module output references cannot be indexed with `[0]`; the full list must be assigned to a local first

**Answer**: B

**Explanation**:
`module.networking.public_subnet_ids` accesses the `public_subnet_ids` output from the child module, which is a list of subnet ID strings produced by the splat expression `aws_subnet.public[*].id`. Appending `[0]` retrieves the **first element** of that list — the subnet ID string of the first public subnet. Module outputs can be indexed, iterated with `for_each`, sliced, or used in `for` expressions just like any other list or map value. This is a common pattern for selecting specific subnets from a networking module.

---

### Question 4 — DynamoDB Lock Table `hash_key = "LockID"`

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reading a DynamoDB table resource for Terraform state locking to identify why `hash_key` must be `"LockID"`

**Question**:
Read this DynamoDB table resource, which will be used for Terraform state locking with an S3 backend:

```hcl
resource "aws_dynamodb_table" "tf_locks" {
  name         = "my-terraform-state-locks"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "LockID"

  attribute {
    name = "LockID"
    type = "S"
  }
}
```

Why must `hash_key` be set to exactly `"LockID"`, and not any other name?

- A) `"LockID"` is a DynamoDB-reserved keyword that automatically enables TTL expiry for abandoned lock items
- B) The Terraform S3 backend hardcodes the partition key attribute name `"LockID"` when writing and reading lock records — using any other name causes locking to fail
- C) `"LockID"` is required by the DynamoDB service API for all tables that will be accessed by AWS SDKs
- D) The value `"LockID"` is stored as the item value and must match the S3 bucket name in the backend block

**Answer**: B

**Explanation**:
The Terraform S3 backend writes lock entries to DynamoDB using a hardcoded item attribute named `"LockID"`. This is a **Terraform implementation requirement**, not a DynamoDB service restriction — DynamoDB accepts any valid attribute name as a hash key. If the table's `hash_key` is named anything other than `"LockID"`, Terraform cannot write or read lock items and state locking fails. This is why every S3 backend locking example uses exactly this attribute name. The `type = "S"` (String) is also required.

---

### Question 5 — Module `for_each` Map — Instance Addresses

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reading a module block with `for_each` on a map to identify the state addresses of the two instances

**Question**:
Read this module block:

```hcl
module "vpc" {
  for_each = {
    production = "10.0.0.0/16"
    staging    = "10.1.0.0/16"
  }

  source   = "./modules/vpc"
  vpc_cidr = each.value
  env_name = each.key
}
```

What are the state addresses for the two module instances?

- A) `module.vpc[0]` and `module.vpc[1]` — `for_each` on a map uses zero-based numeric indices for module addressing
- B) `module.vpc["production"]` and `module.vpc["staging"]` — `for_each` uses the map keys as instance identifiers in bracket notation
- C) `module.production` and `module.staging` — the map key replaces the module block label in state
- D) `module.vpc.production` and `module.vpc.staging` — dot notation is used for map-keyed module instances

**Answer**: B

**Explanation**:
When a `module` block uses `for_each`, Terraform creates one instance per collection element and addresses each using the element's key in bracket notation: `module.<label>["<key>"]`. For this map, the two instances are `module.vpc["production"]` and `module.vpc["staging"]`. Inside each instance, `each.key` holds the key string and `each.value` holds the CIDR block. This parallels `for_each` behaviour on resource blocks, where state addresses also use `resource_type.name["key"]`.

---

### Question 6 — S3 Backend `key` Argument

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reading an S3 backend block to identify what the `key` argument specifies

**Question**:
Read this backend configuration:

```hcl
terraform {
  backend "s3" {
    bucket = "acme-company-tfstate"
    key    = "environments/production/networking.tfstate"
    region = "us-east-1"
  }
}
```

What does the `key` argument specify?

- A) The AWS IAM access key ID used to authenticate Terraform's API calls to S3
- B) The name of the S3 bucket — it is an alias for the `bucket` argument
- C) The S3 object key (path and filename within the bucket) where Terraform writes and reads the state file
- D) The KMS key ID for server-side encryption — required when the bucket enforces SSE-KMS

**Answer**: C

**Explanation**:
In the S3 backend, `key` is the **S3 object key** — the full path and filename within the bucket at which Terraform stores the state file. It is analogous to a file path within a directory structure. In this example, the state is stored at `s3://acme-company-tfstate/environments/production/networking.tfstate`. Multiple configurations can share a single S3 bucket by using different `key` values — for example, one per workspace or environment. The `key` has no connection to authentication keys or encryption key IDs.

---

### Question 7 — Module `count = 2` — Instance Addresses and `count.index`

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Reading a module block with `count = 2` to identify TWO correct statements about instance addressing and `count.index`

**Question**:
Read this module block:

```hcl
module "web_server" {
  count         = 2
  source        = "./modules/web-server"
  ami           = "ami-0abc123"
  instance_name = "web-${count.index}"
}
```

Which TWO statements correctly describe the instances created by this `count = 2` module block? (Select two.)

- A) The two instances are addressed as `module.web_server[0]` and `module.web_server[1]` in the Terraform state
- B) The two instances are addressed as `module.web_server_0` and `module.web_server_1` — underscores replace brackets for count-indexed modules
- C) Inside the `module` block, `count.index` is available in argument expressions such as `"web-${count.index}"`, evaluating to `0` for the first instance and `1` for the second
- D) `count.index` is not usable within a `module` block — it is only valid inside `resource` blocks

**Answer**: A, C

**Explanation**:
When a `module` block uses `count`, instances are addressed using zero-based bracket notation: `module.web_server[0]` and `module.web_server[1]` (A is correct). Within the `module` block's own argument expressions, `count.index` is available and holds the current instance's zero-based index — exactly as it does for resource blocks. The expression `"web-${count.index}"` evaluates to `"web-0"` for the first instance and `"web-1"` for the second (C is correct). Option B uses underscores, which is incorrect syntax. Option D is incorrect — `count.index` is valid in both `resource` and `module` block expressions.

---

### Question 8 — Module `depends_on` Argument

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reading a module block with `depends_on` to identify what dependency it creates

**Question**:
Read this configuration:

```hcl
resource "aws_iam_role_policy_attachment" "lambda_logs" {
  role       = aws_iam_role.lambda.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}

module "lambda_function" {
  source        = "./modules/lambda"
  function_name = "data-processor"
  role_arn      = aws_iam_role.lambda.arn

  depends_on = [aws_iam_role_policy_attachment.lambda_logs]
}
```

What does `depends_on = [aws_iam_role_policy_attachment.lambda_logs]` achieve in this `module` block?

- A) It imports `aws_iam_role_policy_attachment.lambda_logs` into the module's state scope
- B) It creates an explicit dependency edge ensuring that `aws_iam_role_policy_attachment.lambda_logs` is fully applied before any resource inside `module.lambda_function` is created
- C) It applies the dependency only to the first resource declared inside the module — subsequent module resources still run in parallel with the policy attachment
- D) It is redundant because Terraform already infers this dependency from the `role_arn = aws_iam_role.lambda.arn` attribute reference

**Answer**: B

**Explanation**:
`depends_on` in a `module` block creates an **explicit dependency** that forces all resources inside `module.lambda_function` to wait until `aws_iam_role_policy_attachment.lambda_logs` is fully applied. This is necessary when the dependency cannot be inferred through attribute references — here, the Lambda function needs the policy already attached (an eventual-consistency side effect) even though its arguments reference only the role ARN. Option D is incorrect: the `role_arn` reference creates a dependency on `aws_iam_role.lambda` but not on the policy attachment, which is a separate resource. `depends_on` on a module applies to all resources within that module.

---

### Question 9 — Missing Required Variable in Module Call

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reading a child module's `variables.tf` alongside a root module call to identify which required input is missing

**Question**:
Read this configuration:

```hcl
# ./modules/database/variables.tf
variable "db_name"       { type = string }
variable "db_username"   { type = string }
variable "db_password"   { type = string }
variable "instance_class" {
  type    = string
  default = "db.t3.micro"
}

# root main.tf
module "database" {
  source      = "./modules/database"
  db_name     = "appdb"
  db_username = "admin"
}
```

What error does `terraform plan` produce with this configuration?

- A) A warning is displayed advising the team to supply `db_password`, but the plan proceeds
- B) Terraform generates the plan with `db_password = null` because variables without a passed value default to `null`
- C) Terraform raises an error because the required input variable `db_password` has no value — it has no default and is not passed in the `module` block
- D) Terraform raises an error because `instance_class` is not passed — all module variables must be explicitly provided

**Answer**: C

**Explanation**:
Every `variable` block without a `default` in a child module is a **required input**. The `module "database"` block provides `db_name` and `db_username` but omits `db_password`, which has no `default` value. Terraform raises a plan error identifying that the required input variable `db_password` is not set for module `database`. The `instance_class` variable (option D) is fine because it has `default = "db.t3.micro"`. Option B is incorrect — Terraform does not silently default missing required variables to `null`.

---

### Question 10 — `terraform state list` Output — Module Resource Address Format

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Reading `terraform state list` output to identify TWO correct statements about module resource address format

**Question**:
Read this `terraform state list` output:

```
aws_security_group.web
module.vpc.aws_vpc.main
module.vpc.aws_subnet.public[0]
module.vpc.aws_subnet.public[1]
module.vpc.module.nat.aws_eip.nat
```

Which TWO statements correctly describe what this output reveals about the configuration? (Select two.)

- A) `module.vpc.aws_vpc.main` identifies an `aws_vpc` resource named `main` that is managed inside the `vpc` child module
- B) `module.vpc.module.nat.aws_eip.nat` indicates a module named `nat` is nested inside the `vpc` module, and that nested module manages an `aws_eip` resource named `nat`
- C) `aws_security_group.web` is managed by a child module — the absence of a `module.` prefix means Terraform omitted the module path for brevity
- D) `module.vpc.aws_subnet.public[0]` and `module.vpc.aws_subnet.public[1]` are two separate modules with numeric labels

**Answer**: A, B

**Explanation**:
State addresses use a dotted path where `module.<name>.` prefixes any resource inside a module. `module.vpc.aws_vpc.main` (A) is an `aws_vpc` resource named `main` inside the `vpc` module. `module.vpc.module.nat.aws_eip.nat` (B) shows two module levels: `nat` is a child module nested inside `vpc`, and within `nat` there is an `aws_eip` resource named `nat`. Option C is incorrect — `aws_security_group.web` has no `module.` prefix because it is a **root-level resource**, not inside any child module. Option D is incorrect — `public[0]` and `public[1]` are two `count`-indexed instances of the same resource within the same `vpc` module, not separate modules.

---

### Question 11 — `version = "~> 5.0"` Registry Constraint Range

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Reading a registry module block to identify the version range allowed by the `~>` pessimistic constraint operator

**Question**:
Read this module block:

```hcl
module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "~> 5.0"
}
```

What range of module versions does `"~> 5.0"` allow `terraform init` to select?

- A) Exactly version `5.0.0` only — the `~>` operator pins to an exact version
- B) Any version `>= 5.0` with no upper bound — including `6.0`, `7.0`, and beyond
- C) Any version `>= 5.0` and `< 6.0` — the `~>` operator locks the major version and allows the minor to increment freely
- D) Any version `>= 5.0.0` and `< 5.1.0` — the constraint only allows patch releases within `5.0.x`

**Answer**: C

**Explanation**:
The `~>` pessimistic constraint operator allows the rightmost version component to increment while locking the component to its left. For `"~> 5.0"` (specified as `major.minor`), the major version is locked at `5` and the minor version may freely increment: `>= 5.0` and `< 6.0`. This accepts `5.0`, `5.1`, `5.9`, `5.14`, etc., but not `6.0`. For a three-part version like `"~> 5.0.1"`, only patch versions within `5.0.x` are allowed (`>= 5.0.1`, `< 5.1.0`). The behavior scales with the specificity of the version string provided.

---

### Question 12 — Module `providers` Argument with Provider Aliases

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Reading a module block with a `providers` map to identify what provider alias mapping achieves

**Question**:
Read this root module configuration:

```hcl
provider "aws" {
  alias  = "primary"
  region = "us-east-1"
}

provider "aws" {
  alias  = "secondary"
  region = "eu-west-1"
}

module "us_resources" {
  source = "./modules/regional"

  providers = {
    aws = aws.primary
  }
}

module "eu_resources" {
  source = "./modules/regional"

  providers = {
    aws = aws.secondary
  }
}
```

What does the `providers` argument in each `module` block achieve?

- A) It declares version constraints the module is allowed to use — overriding the root module's `required_providers` block
- B) It maps root-module provider aliases to the provider configuration each child module instance will use — enabling the same module source to deploy to different regions
- C) It creates a new isolated provider instance scoped only to that module — the root module loses access to the provider credentials passed to the child
- D) It is required for all module blocks — without a `providers` argument, child modules cannot access any provider configuration

**Answer**: B

**Explanation**:
The `providers` argument in a `module` block performs **provider alias mapping**: it assigns a specific root-module provider configuration (identified by its alias) to the provider slot the child module expects. Both `module.us_resources` and `module.eu_resources` use the same `./modules/regional` source, but one receives the `aws.primary` configuration (us-east-1) and the other receives `aws.secondary` (eu-west-1). Without this argument, a child module inherits the default (non-aliased) provider and cannot automatically use aliased configurations. The `providers` map does not declare version constraints, does not isolate credentials from the root, and is not required for modules that only use the default non-aliased provider.

---

### Question 13 — Nested Module Variable Passing Chain

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Reading a root → child → grandchild module chain to identify TWO correct statements about required explicit variable passing

**Question**:
Read this three-level module configuration:

```hcl
# root main.tf
variable "env" { default = "prod" }

module "app" {
  source = "./modules/app"
  env    = var.env
}

# modules/app/main.tf
variable "env" {}

module "db" {
  source = "./modules/db"
  # env is NOT passed here
}

# modules/db/variables.tf
variable "env" {}   # no default
```

Which TWO statements correctly describe the outcome of running `terraform plan`? (Select two.)

- A) `module.app.module.db` inherits `var.env` from `module.app` automatically — variables propagate down nested module chains
- B) Terraform raises an error because `variable "env"` in `./modules/db/variables.tf` has no default and is not passed by the `module "db"` block in `modules/app/main.tf`
- C) The value `"prod"` flows from the root through `module.app` into `module.db` as a module-scope global because all three declare a `variable "env"` with the same name
- D) To resolve the error, the `module "db"` block inside `modules/app/main.tf` must explicitly pass `env = var.env`, where `var.env` refers to the `env` input of `modules/app`

**Answer**: B, D

**Explanation**:
Terraform has **no implicit variable inheritance** at any module boundary, including nested modules. The root module correctly passes `env = var.env` to `module.app`. However, within `modules/app/main.tf`, the `module "db"` block does not pass `env` to `./modules/db`. Since `variable "env"` in `modules/db` has no `default`, Terraform raises a plan error: the required input variable is not set (B is correct). Options A and C describe non-existent automatic propagation. The fix (D) is to add `env = var.env` to the `module "db"` block inside `modules/app/main.tf` — where `var.env` refers to the `env` variable declared in `modules/app/variables.tf` that was passed in from the root.

---
