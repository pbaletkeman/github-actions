[← Back to Index](INDEX.md)

## GitHub Actions for the Enterprise

### Overview

Enterprise-scale usage of GitHub Actions requires governance, access control, and policy enforcement to ensure security, cost efficiency, and consistent standards across teams and repositories.

---

### 1. **Organizational Use Policies**

Organization and enterprise admins can restrict which actions and reusable workflows are allowed.

**Policy options (set in Organization Settings → Actions → General):**

| Policy                   | Description                                              |
| ------------------------ | -------------------------------------------------------- |
| Allow all actions        | No restrictions                                          |
| Allow local actions only | Only actions in the same organization/enterprise         |
| Allow select actions     | An explicit allow list of `owner/repo@ref` patterns      |
| Disable Actions          | Completely disable GitHub Actions for the org/enterprise |

**Allow-list patterns:**

```text
# Allow all verified creator actions
actions/*
github/*

# Allow specific third-party actions (pinned or wildcard)
docker/build-push-action@*
hashicorp/setup-terraform@*

# Allow all actions from a specific org
myorg/*
```

> Wildcard `*` matches any version/ref. Use full SHAs for stronger guarantees.

**Requiring approval for first-time contributors:**

In org settings you can require that first-time contributors have their workflow runs approved by a maintainer before they execute, reducing risk from fork-based attacks.

---

### 2. **Controlling Access to Actions and Workflows Within an Enterprise**

**Repository access controls:**

- Actions in private repositories are only callable by workflows in the same repository by default
- To share across repositories: **Settings → Actions → General → Access → Allow workflows from other repositories**
- For org-level reusable workflows: set the repository visibility to `internal` or configure cross-repo permissions

**Enterprise policies override org policies:**

```text
Enterprise admin → can restrict org admins from changing policies
Org admin        → can set policies within enterprise-allowed bounds
Repo admin       → can set policies within org-allowed bounds
```

**Required workflows (enterprise feature):**

Enterprise admins can enforce that specific reusable workflows run on all repositories matching a filter, regardless of the repository workflow configuration:

```text
Enterprise Settings → Policies → Required workflows
→ Add workflow: org/compliance-workflows/.github/workflows/scan.yml@main
→ Apply to: all repositories in selected organizations
```

**Required workflow enforcement behavior:**

- Required workflows run even if the repository owner disables Actions for the repo
- They are always added to workflow runs regardless of repo-level configuration
- Results appear in the PR checks list just like any other workflow

#### Fork-Specific Workflow Policies

Workflows triggered by pull requests from **forked repositories** run with restricted permissions by default to prevent untrusted code from accessing secrets. Understanding and configuring these policies is critical for public repositories and open-source projects.

**Default behavior for fork PRs:**

| Trigger event                    | Default behavior                                          |
| -------------------------------- | --------------------------------------------------------- |
| `pull_request`                   | Runs with read-only token; secrets NOT available          |
| `pull_request_target`            | Runs with write token and secrets (use with extreme care) |
| Fork from first-time contributor | Requires **manual approval** before workflow runs         |

**Configuring fork PR approval requirements:**

GitHub allows organizations to require manual approval before running workflows for PRs from outside collaborators or first-time contributors:

```
Organization Settings → Actions → General → Fork pull request workflows
→ Options:
   • "Require approval for first-time contributors who are new to GitHub"  (default)
   • "Require approval for first-time contributors"
   • "Require approval for all outside collaborators"
```

For **enterprise-level** enforcement:

```text
Enterprise Settings → Policies → Actions → Fork pull request workflows
→ Enforce one of the approval options across all organizations
```

**Security rules for fork workflows:**

- Secrets are **never** available to `pull_request` workflows from forks (safeguard against credential theft)
- The `GITHUB_TOKEN` in fork PR workflows is always **read-only**
- To grant write access intentionally, use `pull_request_target` — but ONLY after validating the forked code does not run in a privileged context:

```yaml
# ✅ SAFE: pull_request_target with explicit checkout of base ref (not fork code)
on:
  pull_request_target:
    types: [opened, labeled]

jobs:
  label-check:
    if: contains(github.event.pull_request.labels.*.name, 'approved-for-ci')
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          ref: ${{ github.event.pull_request.head.sha }} # Only after labeling approval
```

**Restricting which actions forks can invoke:**

For public repositories with CI triggered by fork PRs, restrict which actions the workflow can use via the organization policy:

```text
Organization → Settings → Actions → General → Policies
→ "Allow select actions and reusable workflows"
→ Add only verified/trusted actions to the allow list
→ This applies to ALL workflows, including those from forks
```

---

### 3. **Runner Groups**

Runner groups organize self-hosted runners and control which repositories or organizations can use them.

**Creating and managing runner groups:**

```bash
# Create a runner group (org level)
curl -X POST \
  -H "Authorization: Bearer $TOKEN" \
  -H "Accept: application/vnd.github+json" \
  https://api.github.com/orgs/ORG/actions/runner-groups \
  -d '{
    "name": "production-runners",
    "visibility": "selected",
    "selected_repository_ids": [12345, 67890],
    "allows_public_repositories": false
  }'
```

**Using a runner group in a workflow:**

```yaml
jobs:
  deploy:
    runs-on:
      group: production-runners # target the group
      labels: [self-hosted, linux] # optionally add label filters
    steps:
      - run: echo "Running on a production runner"
```

**Key access rules:**

- A runner group set to `visibility: private` is accessible only to the org
- A runner group set to `visibility: selected` allows specific repositories
- Enterprise-level groups can span organizations
- A runner can only belong to one group at a time

---

### 4. **IP Allow Lists**

GitHub-hosted runners use a range of dynamic IP addresses. For services that lock down inbound traffic, you have several options:

**Option A:** Retrieve GitHub-hosted runner IPs and add to your allow list

```bash
# Get current GitHub Actions IP ranges (changes frequently)
curl https://api.github.com/meta | jq '.actions'
```

> Note: IP ranges change frequently. GitHub provides webhooks (`meta` event) to notify when the list changes.

**Option B:** Use a self-hosted runner inside your network perimeter

Self-hosted runners run on infrastructure you control, so the source IP is predictable:

```yaml
jobs:
  deploy-internal:
    runs-on: [self-hosted, internal-network]
    steps:
      - run: curl https://internal.company.com/api/deploy
```

**Option C:** GitHub Enterprise Cloud — IP allow list integration

Enterprise Cloud customers can enable the "GitHub Actions" entry in the organization's IP allow list. This automatically allows traffic from GitHub-hosted runner IPs without manual maintenance.

---

### 5. **Preinstalled Software on GitHub-Hosted Runners**

GitHub-hosted runners include a broad set of preinstalled tools in the **tool cache** (`RUNNER_TOOL_CACHE`).

**Key preinstalled categories:**

| Category          | Examples                                                      |
| ----------------- | ------------------------------------------------------------- |
| Language runtimes | Node.js, Python, Ruby, Java (multiple LTS versions), Go, .NET |
| Build tools       | Maven, Gradle, Ant, CMake, make                               |
| Package managers  | npm, pip, bundler, nuget, Homebrew (macOS)                    |
| Cloud CLIs        | AWS CLI, Azure CLI, Google Cloud CLI                          |
| Container tools   | Docker, Docker Compose, kubectl, Helm                         |
| Version control   | Git, GitHub CLI (`gh`)                                        |
| Utilities         | tar, curl, wget, jq, yq                                       |

**Checking what's available:**

```yaml
- name: List available tools
  run: |
    echo "--- Node versions ---"
    ls $RUNNER_TOOL_CACHE/node/
    echo "--- Python versions ---"
    ls $RUNNER_TOOL_CACHE/Python/ || ls $RUNNER_TOOL_CACHE/python/
```

**Using `setup-*` actions to select a version:**

```yaml
- uses: actions/setup-node@v4
  with:
    node-version: "20" # reads from tool cache if available

- uses: actions/setup-python@v5
  with:
    python-version: "3.12"

- uses: actions/setup-java@v4
  with:
    distribution: "temurin"
    java-version: "21"
```

**Finding the full list:**

The complete software manifest for each image is published at:
`https://github.com/actions/runner-images`
Each runner image folder contains an `Included-Software.md` file.

> **Ubuntu 20.04 deprecation:** As of late 2024, `ubuntu-20.04` images are deprecated. Migrate to `ubuntu-22.04` or `ubuntu-latest`.
> **`windows-latest`** now points to Windows Server 2025 images.

**Installing additional software at runtime:**

When a required tool is not preinstalled, you can install it during the job using the runner's package manager:

```yaml
# Ubuntu/Debian — apt-get
- name: Install additional packages
  run: |
    sudo apt-get update
    sudo apt-get install -y jq wget gnupg lsof

# macOS — Homebrew
- name: Install additional packages (macOS)
  run: brew install libpq

# Windows — Chocolatey
- name: Install additional packages (Windows)
  run: choco install -y sysinternals

# Cross-platform — pip (Python)
- name: Install Python packages at runtime
  run: pip install boto3 requests

# Cross-platform — npm global tool
- name: Install Node.js CLI tool at runtime
  run: npm install -g typescript
```

**Strategies for providing custom software:**

| Approach                                                       | Best for                                   | Trade-off                                      |
| -------------------------------------------------------------- | ------------------------------------------ | ---------------------------------------------- |
| `apt-get` / `brew` / `choco` at runtime                        | One-off tools, CI-specific utilities       | Slower starts; network dependency              |
| `setup-*` actions (`setup-node`, `setup-python`, `setup-java`) | Language runtimes with version control     | Well-maintained; uses tool cache               |
| Job container (`container:` key)                               | Pre-baked Linux environments with all deps | Faster cold starts; requires image maintenance |
| Custom self-hosted runner AMI/image                            | Enterprise; consistent heavy toolchains    | High setup cost; own image lifecycle           |

**Using a job container to pre-bake software:**

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    container:
      image: node:20-alpine # all Node.js tools pre-installed in the image
    steps:
      - uses: actions/checkout@v4
      - run: npm ci && npm test # Node.js already available, no setup-* needed
```

**Using `setup-*` actions for version management:**

```yaml
- uses: actions/setup-node@v4
  with:
    node-version: "20"
    cache: "npm" # also caches npm dependencies in the tool cache

- uses: actions/setup-python@v5
  with:
    python-version: "3.12"
    cache: "pip"

- uses: actions/setup-java@v4
  with:
    distribution: "temurin"
    java-version: "21"
    cache: "maven"
```

**Caching custom installed tools across runs:**

```yaml
- name: Cache custom tool
  uses: actions/cache@v4
  id: cache-tool
  with:
    path: ~/.local/bin/mytool
    key: mytool-${{ runner.os }}-v1.2.3

- name: Install custom tool (only on cache miss)
  if: steps.cache-tool.outputs.cache-hit != 'true'
  run: |
    curl -Lo ~/.local/bin/mytool https://example.com/mytool-v1.2.3-linux-amd64
    chmod +x ~/.local/bin/mytool
```

---

### 6. **Secrets and Variables at Organization, Repository, and Environment Levels**

GitHub provides a three-tier hierarchy for secrets and variables, with more specific scopes overriding broader ones.

**Hierarchy (most specific wins):**

```text
Enterprise → Organization → Repository → Environment
```

**Secrets vs Variables:**

| Type     | Stored encrypted?       | Visible in logs? | Use for                     |
| -------- | ----------------------- | ---------------- | --------------------------- |
| Secret   | ✅ Yes — masked in logs | ❌ Never         | API keys, passwords, tokens |
| Variable | ❌ No — plain text      | ✅ Yes           | Non-sensitive config values |

**Accessing in workflows:**

```yaml
env:
  # Secret (from org, repo, or environment)
  API_KEY: ${{ secrets.API_KEY }}

  # Variable (from org, repo, or environment)
  DEPLOY_REGION: ${{ vars.DEPLOY_REGION }}

  # Environment-specific (takes priority over repo/org)
  DB_HOST: ${{ secrets.DB_HOST }} # could differ per environment
```

**Setting environment-level secrets/variables:**

```yaml
jobs:
  deploy:
    environment: production # activates environment-level secrets/vars
    runs-on: ubuntu-latest
    steps:
      - run: echo "Deploying to ${{ vars.ENVIRONMENT_NAME }}"
        env:
          PROD_TOKEN: ${{ secrets.DEPLOY_TOKEN }}
```

**Managing via REST API:**

```bash
# List organization secrets
curl -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/orgs/ORG/actions/secrets

# Create/update a repository secret (value must be encrypted with the repo's public key)
curl -X PUT \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/secrets/SECRET_NAME \
  -d '{"encrypted_value": "BASE64_ENCRYPTED", "key_id": "KEY_ID"}'

# Create a repository variable
curl -X POST \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/variables \
  -d '{"name": "DEPLOY_REGION", "value": "us-east-1"}'

# Create an organization variable (visibility controls which repos can read it)
curl -X POST \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/orgs/ORG/actions/variables \
  -d '{"name": "ORG_REGION", "value": "eu-west-1", "visibility": "selected", "selected_repository_ids": [111, 222]}'
```

#### Comprehensive REST API CRUD Examples for Secrets & Variables

**Prerequisites for Secret Encryption:**

```bash
# Get repository public key (needed to encrypt secret values)
curl -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/secrets/public-key | jq '.key, .key_id'

# Output:
# "key": "2Sg8z/NzSQpRjeBMHI..."
# "key_id": "568250167"

# Encrypt secret value using the public key (requires libsodium)
# In Python:
import base64
import nacl.public
import nacl.utils

public_key = nacl.public.PublicKey("2Sg8z/NzSQpRjeBMHI...", encoder=nacl.encoding.Base64Encoder)
secret_value = "my-secret-value"
sealed_box = nacl.public.SealedBox(public_key)
encrypted = sealed_box.encrypt(secret_value.encode())
encrypted_value = base64.b64encode(encrypted).decode()
```

**READ Operations:**

```bash
# List all repository secrets (names only, values never readable)
curl -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/secrets \
  | jq '.secrets[] | {name, created_at, updated_at}'

# List all repository variables
curl -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/variables \
  | jq '.variables[] | {name, value, created_at, updated_at}'

# Get specific secret metadata (not the value!)
curl -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/secrets/API_KEY \
  | jq '{name, created_at, updated_at}'

# Get specific variable (includes plaintext value)
curl -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/variables/DEPLOY_REGION \
  | jq '{name, value, created_at, updated_at}'

# List environment-level secrets
curl -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/environments/production/secrets

# List organization secrets
curl -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/orgs/ORG/actions/secrets
```

**CREATE Operations:**

```bash
# Create a new repository secret (must use encrypted value)
curl -X POST \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/secrets/NEW_SECRET \
  -d '{
    "encrypted_value": "ENCRYPTED_BASE64_VALUE",
    "key_id": "568250167"
  }'

# Create a repository variable
curl -X POST \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/variables \
  -d '{
    "name": "BUILD_TIMEOUT",
    "value": "600"
  }'

# Create an environment secret
curl -X POST \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/environments/staging/secrets/STAGING_TOKEN \
  -d '{
    "encrypted_value": "ENCRYPTED_VALUE",
    "key_id": "KEY_ID"
  }'

# Create an environment variable
curl -X POST \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/environments/production/variables \
  -d '{
    "name": "PROD_URL",
    "value": "https://prod.example.com"
  }'

# Create an organization secret (visible to specified repos)
curl -X POST \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/orgs/ORG/actions/secrets/ORG_SECRET \
  -d '{
    "encrypted_value": "ENCRYPTED_VALUE",
    "key_id": "KEY_ID",
    "visibility": "selected",
    "selected_repository_ids": [111, 222, 333]
  }'

# Create an organization variable
curl -X POST \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/orgs/ORG/actions/variables \
  -d '{
    "name": "ORG_REGISTRY",
    "value": "ghcr.io",
    "visibility": "all"  # or "selected" with repository IDs
  }'
```

**UPDATE Operations:**

```bash
# Update a repository secret (replace encrypted value)
curl -X PUT \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/secrets/API_KEY \
  -d '{
    "encrypted_value": "NEW_ENCRYPTED_VALUE",
    "key_id": "NEW_KEY_ID"
  }'

# Update a repository variable
curl -X PATCH \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/variables/BUILD_TIMEOUT \
  -d '{
    "value": "900"  # 15 minutes instead of 10
  }'

# Update environment variable
curl -X PATCH \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/environments/production/variables/PROD_URL \
  -d '{
    "value": "https://new-prod.example.com"
  }'

# Update organization variable visibility
curl -X PATCH \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/orgs/ORG/actions/variables/ORG_REGISTRY \
  -d '{
    "name": "ORG_REGISTRY",
    "value": "docker.io",
    "visibility": "selected",
    "selected_repository_ids": [111, 222]  # Change which repos can access
  }'
```

**DELETE Operations:**

```bash
# Delete a repository secret
curl -X DELETE \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/secrets/OLD_SECRET
# Returns 204 No Content on success

# Delete a repository variable
curl -X DELETE \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/variables/DEPRECATED_VAR

# Delete an environment secret
curl -X DELETE \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/environments/staging/secrets/STAGING_TOKEN

# Delete an environment variable
curl -X DELETE \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/environments/production/variables/PROD_URL

# Delete an organization secret
curl -X DELETE \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/orgs/ORG/actions/secrets/ORG_SECRET

# Delete an organization variable
curl -X DELETE \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/orgs/ORG/actions/variables/ORG_REGISTRY
```

---

### 7. **Audit Logging for Actions Events**

GitHub's audit log records Actions-related events for organization administrators, providing visibility into who triggered workflows, which actions were permitted or blocked, and what policy changes were made.

#### Accessing the Audit Log

**Via the GitHub UI:**

1. Navigate to **Organization Settings → Audit log**
2. Filter by category **Actions** to see workflow and policy events

**Via the REST API:**

```bash
# Query the audit log for Actions events (org admin token required)
curl -H "Authorization: Bearer $TOKEN" \
  "https://api.github.com/orgs/ORG/audit-log?phrase=action:workflows&per_page=50" \
  | jq '.[] | {action, actor, repo, created_at}'
```

**Via GraphQL (for richer structured filtering):**

```graphql
query {
  organization(login: "my-org") {
    auditLog(first: 20, query: "action:workflows.approved_workflow_run") {
      nodes {
        ... on WorkflowsApprovedWorkflowRunAuditEntry {
          action
          actor {
            login
          }
          createdAt
          repository {
            nameWithOwner
          }
        }
      }
    }
  }
}
```

#### Key Actions Audit Log Event Types

| Event                              | Description                                                  |
| ---------------------------------- | ------------------------------------------------------------ |
| `workflows.approved_workflow_run`  | A maintainer approved a pending run (first-time contributor) |
| `workflows.cancelled_workflow_run` | A workflow run was cancelled                                 |
| `org.actions_permission_updated`   | Organization's allowed-actions policy was changed            |
| `org.runner_group_created`         | A runner group was created                                   |
| `org.runner_group_deleted`         | A runner group was deleted                                   |
| `org.runner_group_updated`         | A runner group's visibility or access was changed            |
| `repo.actions_enabled`             | Actions were enabled or disabled for a repository            |

#### Streaming Audit Logs (Enterprise Cloud)

Enterprise Cloud organizations can stream audit log events to external systems in real time via **Organization Settings → Audit log → Log streaming**. Supported destinations:

- Amazon S3
- Azure Blob Storage
- Google Cloud Storage
- Datadog
- Splunk

This enables continuous compliance monitoring without polling the API.

---



---

## Navigation

- [← Back to Index](INDEX.md)
- [← Previous: 16-Managing-Runners](16-Managing-Runners.md)
- [Next: 18-Security-and-Optimization →](18-Security-and-Optimization.md)
