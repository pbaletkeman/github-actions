[← Back to Index](INDEX.md)

## Workflow Sharing

### What is Workflow Sharing

Workflow sharing allows you to reuse workflow files across multiple repositories or share standardized automation patterns within your organization. Instead of duplicating workflow code, you can create a single source of truth and reference it from other repositories.

### Why Share Workflows

**Key Benefits:**

1. **Code Reuse**: Avoid duplicating workflows across repositories
2. **Consistency**: Ensure all projects follow the same CI/CD standards
3. **Maintainability**: Update workflows in one place, benefits all repositories
4. **Standardization**: Enforce organizational best practices
5. **Reduced Errors**: Centralized quality and security checks
6. **Quick Onboarding**: New projects inherit established workflows

**Real-World Scenario:**

```text
Scenario: Organization with 50 repositories

Without Sharing:
- Create workflows separately for each project
- Duplicate code across 50 repositories
- Update takes 50x effort
- Inconsistent standards across projects
- Risk of different security practices

With Sharing:
- Create workflow once in shared repository
- Import in all 50 projects with one line
- Update in one place, all projects updated
- Consistent standards organization-wide
- Centralized security policy enforcement
```

### How Workflow Sharing Works

**Sharing Methods:**

1. **Reusable Workflows**: Call workflows from other workflows (same/different repos)
2. **Shared Actions**: Create custom actions in a shared repository
3. **Workflow Templates**: GitHub provides starter templates
4. **Private Repository Actions**: Use actions from private repos with access token

### 1. **Reusable Workflows**

#### Creating a Reusable Workflow

```yaml
# .github/workflows/shared-tests.yml (in shared-workflows repository)
name: Shared Test Workflow

on:
  workflow_call:
    inputs:
      node-version:
        required: false
        type: string
        default: "18"
      test-command:
        required: false
        type: string
        default: "npm test"
    secrets:
      npm-token:
        required: false

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Setup Node.js
        uses: actions/setup-node@v3
        with:
          node-version: ${{ inputs.node-version }}
          registry-url: "https://registry.npmjs.org"

      - name: Install Dependencies
        run: npm ci
        env:
          NODE_AUTH_TOKEN: ${{ secrets.npm-token }}

      - name: Run Tests
        run: ${{ inputs.test-command }}
```

#### Using a Reusable Workflow

```yaml
# .github/workflows/ci.yml (in consuming repository)
name: CI

on: push

jobs:
  test:
    uses: org/shared-workflows/.github/workflows/shared-tests.yml@main
    with:
      node-version: "20"
      test-command: "npm test -- --coverage"
    secrets:
      npm-token: ${{ secrets.NPM_TOKEN }}
```

**Passing secrets to reusable workflows — two patterns:**

```yaml
# Pattern A: Explicit mapping — pass named secrets individually
jobs:
  call-workflow:
    uses: org/shared-workflows/.github/workflows/deploy.yml@main
    with:
      environment: production
    secrets:
      deploy-token: ${{ secrets.DEPLOY_TOKEN }}
      api-key: ${{ secrets.API_KEY }}

# Pattern B: secrets: inherit — passes ALL caller secrets automatically.
# The called workflow can access them by the same name via ${{ secrets.* }}.
jobs:
  call-workflow:
    uses: org/shared-workflows/.github/workflows/deploy.yml@main
    with:
      environment: production
    secrets: inherit
```

**Choosing between the two patterns:**

|                 | Explicit mapping                      | `secrets: inherit`                          |
| --------------- | ------------------------------------- | ------------------------------------------- |
| Security        | Higher — only named secrets flow      | Lower — all secrets flow automatically      |
| Maintenance     | Higher — must list every secret       | Lower — no updates when secrets change      |
| Visibility      | Clear which secrets are used          | Implicit; reader must check called workflow |
| Recommended for | Public/third-party reusable workflows | Internal org-owned workflows                |

> **Note:** The called workflow must declare the secret in its `on.workflow_call.secrets:` block to receive it via explicit mapping. When using `secrets: inherit`, secrets are passed without requiring declaration in the called workflow.

#### Key Components

```yaml
on:
  workflow_call: # Makes this workflow reusable
    inputs: # Define inputs from caller
      parameter-name:
        type: string # string, boolean, number, environment
        required: false
        default: "value"
    secrets: # Define secrets from caller
      secret-name:
        required: true
    outputs: # Declare values this workflow returns to the caller
      artifact-version:
        description: "The version string of the built artifact"
        value: ${{ jobs.job-name.outputs.version }} # Must reference a job output

jobs:
  job-name:
    runs-on: ubuntu-latest
    outputs:
      version: ${{ steps.get-version.outputs.value }} # Promote step output to job output
    steps:
      - run: echo ${{ inputs.parameter-name }}
      - run: echo ${{ secrets.secret-name }}
      - name: Get version
        id: get-version
        run: echo "value=1.2.3" >> $GITHUB_OUTPUT
```

**How the caller accesses reusable workflow outputs:**

```yaml
# Calling workflow
jobs:
  build:
    uses: org/shared-workflows/.github/workflows/build.yml@main
    with:
      parameter-name: "hello"
    secrets:
      secret-name: ${{ secrets.MY_SECRET }}

  deploy:
    needs: build
    runs-on: ubuntu-latest
    steps:
      - run: echo "Deploying version ${{ needs.build.outputs.artifact-version }}"
      # Access via: needs.<job-id>.outputs.<output-name>
      # where <job-id> is the job that called the reusable workflow
```

**Complete three-layer output chain:**

```text
Step output          →   Job output              →   Workflow output         →  Caller
echo "x=v" >>             jobs.<id>.outputs:          workflow_call.outputs:    needs.<job>.outputs.
$GITHUB_OUTPUT            key: ${{ steps.<id>         key:                      key
                              .outputs.key }}            value: ${{ jobs.<id>
                                                             .outputs.key }}
```

### 2. **Complete Reusable Workflow Examples**

#### Build and Push Docker Image (Reusable)

```yaml
# org/shared-workflows/.github/workflows/docker-build.yml
name: Docker Build and Push

on:
  workflow_call:
    inputs:
      image-name:
        required: true
        type: string
      dockerfile-path:
        required: false
        type: string
        default: "./Dockerfile"
      build-context:
        required: false
        type: string
        default: "."
      docker-tags:
        required: false
        type: string
        default: "latest"
    secrets:
      registry-username:
        required: true
      registry-password:
        required: true

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Set up Docker Buildx
        uses: docker/setup-buildx-action@v2

      - name: Login to Registry
        uses: docker/login-action@v2
        with:
          username: ${{ secrets.registry-username }}
          password: ${{ secrets.registry-password }}

      - name: Build and Push
        uses: docker/build-push-action@v4
        with:
          context: ${{ inputs.build-context }}
          file: ${{ inputs.dockerfile-path }}
          push: true
          tags: |
            ${{ inputs.image-name }}:${{ inputs.docker-tags }}
```

**Using the reusable workflow:**

```yaml
jobs:
  docker:
    uses: org/shared-workflows/.github/workflows/docker-build.yml@v1
    with:
      image-name: myregistry.azurecr.io/myapp
      dockerfile-path: "./docker/Dockerfile"
      docker-tags: |
        latest
        ${{ github.sha }}
    secrets:
      registry-username: ${{ secrets.REGISTRY_USERNAME }}
      registry-password: ${{ secrets.REGISTRY_PASSWORD }}
```

#### Code Quality Check (Reusable)

```yaml
# org/shared-workflows/.github/workflows/quality-checks.yml
name: Quality Checks

on:
  workflow_call:
    inputs:
      language:
        required: true
        type: string # javascript, python, java, etc.
      lint-command:
        required: true
        type: string
      build-command:
        required: false
        type: string

jobs:
  quality:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Initialize CodeQL
        uses: github/codeql-action/init@v2
        with:
          languages: ${{ inputs.language }}

      - name: Lint Code
        run: ${{ inputs.lint-command }}

      - name: Build (if specified)
        if: inputs.build-command != ''
        run: ${{ inputs.build-command }}

      - name: Perform CodeQL Analysis
        uses: github/codeql-action/analyze@v2
```

### 3. **Calling Reusable Workflows from Other Workflows**

```yaml
# Complete CI/CD using multiple reusable workflows
name: Full CI/CD

on: push

jobs:
  quality:
    uses: org/shared-workflows/.github/workflows/quality-checks.yml@main
    with:
      language: javascript
      lint-command: npm run lint
      build-command: npm run build

  test:
    needs: quality
    uses: org/shared-workflows/.github/workflows/shared-tests.yml@main
    with:
      node-version: "18"

  docker:
    needs: test
    uses: org/shared-workflows/.github/workflows/docker-build.yml@v1
    with:
      image-name: myregistry.azurecr.io/myapp
      docker-tags: ${{ github.sha }}
    secrets:
      registry-username: ${{ secrets.REGISTRY_USERNAME }}
      registry-password: ${{ secrets.REGISTRY_PASSWORD }}
```

### 4. **Creating Shared Actions**

#### Composite Action Example

```yaml
# org/shared-actions/deploy-to-azure/action.yml
name: Deploy to Azure
description: Deploy application to Azure App Service

inputs:
  resource-group:
    description: Azure resource group name
    required: true
  app-name:
    description: Azure App Service name
    required: true
  subscription-id:
    description: Azure subscription ID
    required: true
  azure-credentials:
    description: Azure credentials JSON
    required: true

runs:
  using: composite
  steps:
    - name: Azure Login
      uses: azure/login@v1
      with:
        creds: ${{ inputs.azure-credentials }}

    - name: Deploy to App Service
      uses: azure/webapps-deploy@v2
      with:
        app-name: ${{ inputs.app-name }}
        package: "."
        resource-group: ${{ inputs.resource-group }}

    - name: Logout from Azure
      run: az logout
      shell: bash
```

**Using the shared action:**

```yaml
jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Build Application
        run: npm run build

      - name: Deploy
        uses: org/shared-actions/deploy-to-azure@v1
        with:
          resource-group: prod-rg
          app-name: my-app-prod
          subscription-id: ${{ secrets.AZURE_SUBSCRIPTION_ID }}
          azure-credentials: ${{ secrets.AZURE_CREDENTIALS }}
```

### 5. **Best Practices for Workflow Sharing**

#### ✓ Recommended Practices — Workflow Sharing

```yaml
# ✓ Version your reusable workflows
uses: org/shared-workflows/.github/workflows/build.yml@v1.0.0
uses: org/shared-workflows/.github/workflows/build.yml@main  # or main branch

# ✓ Document inputs and secrets clearly
on:
  workflow_call:
    inputs:
      environment:
        description: 'Deployment environment (dev, staging, prod)'
        required: true
        type: choice
        default: dev
    secrets:
      api-key:
        description: 'API key for service authentication'
        required: true

# ✓ Use descriptive workflow file names
test.yml
test-nodejs.yml
test-python.yml

# ✓ Include usage documentation
# README.md in shared-workflows repository with examples
```

#### ✗ Anti-Patterns to Avoid — Workflow Sharing

```yaml
# ✗ Don't use workflows from untrusted sources
uses: random-org/workflows/.github/workflows/build.yml@main

# ✗ Don't expose secrets unnecessarily
outputs:  # Don't output secrets!
  api-key: ${{ secrets.API_KEY }}

# ✗ Don't make workflows overly rigid
# Allow customization via inputs

# ✗ Don't use latest without pinning versions
uses: org/workflows/.github/workflows/build.yml@main  # Risky!
uses: org/workflows/.github/workflows/build.yml@v1    # Better
```

---

### 6. **Starter Workflows**

Starter workflows are template workflows stored in a repository's `.github/workflows/` or in the org-level `.github` repository under `workflow-templates/`. When a user **creates a new workflow** in a repository, GitHub offers these templates in the Actions tab.

**Key characteristic:** Starter workflows are **copied** into the consuming repository and become independent. They are not linked back — changes to the template do not propagate.

| Feature                  | Starter Workflow              | Reusable Workflow               | Composite Action                |
| ------------------------ | ----------------------------- | ------------------------------- | ------------------------------- |
| How invoked              | Copied on creation (one-time) | `uses:` at runtime              | `uses:` at runtime              |
| Stays linked to source?  | ❌ No — independent copy      | ✅ Yes — always runs latest ref | ✅ Yes — always runs pinned ref |
| Versioned?               | No — snapshot at copy time    | Yes — via `@ref`                | Yes — via `@ref`                |
| Best for                 | Scaffolding new workflows     | Sharing execution logic         | Encapsulating step logic        |
| Customizable after copy? | ✅ Yes — full control         | ⚠️ Via inputs only              | ⚠️ Via inputs only              |

#### Creating an Organization Starter Workflow

Store the template file in `{org}/.github/workflow-templates/my-template.yml` and a corresponding metadata file `my-template.properties.json`:

```json
// workflow-templates/python-ci.properties.json
{
  "name": "Python CI",
  "description": "Lint and test Python packages",
  "iconName": "octicon python",
  "categories": ["Python", "CI"]
}
```

```yaml
# workflow-templates/python-ci.yml
name: Python CI

on:
  push:
    branches: [$default-branch] # placeholder replaced on copy
  pull_request:
    branches: [$default-branch]

jobs:
  lint-and-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-python@v5
        with:
          python-version: "3.x"
      - run: pip install ruff pytest
      - run: ruff check .
      - run: pytest
```

> For **public** repositories, starter workflows are visible to all GitHub users. For organization repositories, they are visible only to members with repository access.

#### Template Placeholder Variables

Starter workflow files support a small set of placeholder variables that GitHub replaces automatically when a user copies the template into their repository:

| Placeholder       | Replaced With                                                  |
| ----------------- | -------------------------------------------------------------- |
| `$default-branch` | The repository's default branch name (e.g. `main` or `master`) |

> **Note:** `$default-branch` is the only officially supported placeholder in workflow template YAML files. Additional metadata lives in the companion `.properties.json` file (fields: `name`, `description`, `iconName`, `categories`) and is not substituted into the YAML.

#### Organizational Workflow Templates vs GitHub-Provided Starter Workflows

**GitHub-Provided Starter Workflows** (on Marketplace):

- **Location**: Managed by GitHub, accessible from any repository's Actions tab
- **Visibility**: Public marketplace visible to all users
- **Best for**: Language/framework-specific scaffolding (Node.js, Python, Go, Rails, etc.)
- **Ownership**: Maintained by GitHub's community

**Organizational Workflow Templates** (private to your org):

- **Location**: Stored in `{org}/.github` repo under `workflow-templates/`
- **Visibility**: Private to organization members with access to `.github` repo
- **Best for**: Organization-specific standards, security policies, internal tooling
- **Ownership**: Your organization maintains

**Creating Private Org Templates - Complete Setup:**

1. **Create or update the `.github` repository** in your organization
2. **Add template files** in `.github/workflow-templates/`:

   ```plaintext
   .github/
   └── workflow-templates/
       ├── security-scan.properties.json
       ├── security-scan.yml
       ├── deploy-prod.properties.json
       └── deploy-prod.yml
   ```

   - **Template metadata** (`security-scan.properties.json`):

   ```json
   {
     "name": "Security Scanning Pipeline",
     "description": "Run CodeQL and SAST for all branches",
     "iconName": "octicon shield",
     "categories": ["Security", "CI"],
     "filePatterns": []
   }
   ```

   - **Template workflow with organization placeholders** (`security-scan.yml`):

   ```yaml
   name: Security Scanning

   on:
     push:
       branches: [$default-branch]
     pull_request:
       branches: [$default-branch]

   env:
     # Organization-wide defaults
     REGISTRY: ghcr.io
     IMAGE_NAME: ${{ github.repository }}

   jobs:
     security-scan:
       runs-on: ubuntu-latest
       permissions:
         # Org-standard permissions
         security-events: write
         contents: read
       steps:
         - uses: actions/checkout@v4

         - name: Initialize CodeQL
           uses: github/codeql-action/init@v2
           with:
             languages: javascript # Customize per repo on copy

         - name: Perform CodeQL Analysis
           uses: github/codeql-action/analyze@v2

         - name: Upload SARIF results
           uses: github/codeql-action/upload-sarif@v2
           with:
             sarif-file: results.sarif
   ```

**Discovering and Using Org Templates:**

1. In any organization repository, navigate to Actions → **"New workflow"**
2. Look for your **organization's templates** section (before GitHub's marketplace templates)
3. Click on a template to create a copy
4. The template is copied into `.github/workflows/` of that repository (fully independent)
5. Customize as needed

#### Access and Permission Model for Non-Public Org Templates

Understanding who can see and use non-public (private/internal) org workflow templates is important for governance.

**Visibility of the `.github` repository:**

| `.github` repo visibility                | Who sees org templates in "New workflow" UI                          |
| ---------------------------------------- | -------------------------------------------------------------------- |
| **Public**                               | All GitHub users (not just org members)                              |
| **Private**                              | Only org members with at least **Read** access to the `.github` repo |
| **Internal** _(GitHub Enterprise Cloud)_ | All members of the enterprise (across orgs)                          |

**Key rules:**

- The `.github` repository controls access to all templates stored inside it
- By default, organization members have **Read** access to repos in the org if the org's base permission is `Read` or higher — this applies to `.github` as well
- If the `.github` repo is **private** and a member has no explicit access, they will **not see the org's templates** in the new-workflow UI
- Owners and admins can restrict the base permission of the org to `None`, which means the `.github` repo must have explicit collaborator or team access granted for members to see the templates

**Granting access to non-public templates:**

```text
Organization Settings → Member privileges → Base permissions
  └─ Set to "Read" so all members can see the .github repo templates

  OR (more granular):

Organization Settings → Teams → [team-name] → Repositories
  └─ Add the .github repo with Read access for that team only
```

**Repository-level permission requirements to copy a template:**

A user can discover and **initiate** a template copy if they can see the `.github` repo. To actually save the copied workflow file, they need **Write** (or higher) access to the **target repository** where they are creating the new workflow — standard repository write permission applies.

**Enterprise considerations:**

On **GitHub Enterprise Cloud**, setting the `.github` repo visibility to **Internal** is the recommended approach for org-wide templates that all enterprise members should access without making them fully public:

```text
.github repo visibility: Internal
→ Visible to all enterprise members regardless of org membership
→ Not visible to external collaborators or the public
→ Templates appear in "New workflow" for any enterprise repo
```

On **GitHub Enterprise Server**, the same internal visibility model applies within the server instance.

**Restricting which repos can use certain templates (workaround):**

GitHub does not natively restrict which target repos can copy a specific template (all or nothing per `.github` repo visibility). For fine-grained control, split templates across separate template repositories and apply access controls per repository. A common pattern:

```text
{org}/.github                    → public templates (base scaffold)
{org}/.github-internal           → internal/compliance templates (restricted team access)
{org}/.github-security           → security team templates (security team only)
```

> **Note:** Only the special `.github` repository name triggers the "workflow templates" feature. Alternative repositories (`-internal`, `-security` etc.) will not show their templates in the new-workflow UI — they must be accessed and copied manually.

**Sync Pattern for Org Templates** (if you want changes to propagate):

Since templates are copied, they don't auto-update. If you need central control, use reusable workflows instead:

```yaml
# Option A: Use reusable workflow (stays linked)
name: Call Our Security Workflow

on:
  push:

jobs:
  security:
    uses: {org}/.github/.github/workflows/security-reusable.yml@main
```

#### Disabling vs Deleting a Workflow

| Action      | Effect                                                                                       | When to use                                            |
| ----------- | -------------------------------------------------------------------------------------------- | ------------------------------------------------------ |
| **Disable** | Workflow is paused; no new runs trigger. History and logs preserved. Re-enable at any time.  | Temporarily halt a workflow (e.g., during maintenance) |
| **Delete**  | Workflow file is removed from the repository. All run history is also deleted after 90 days. | Permanently remove an obsolete workflow                |

**Disable via UI:** Actions tab → click the workflow → ⋯ menu → **Disable workflow**

**Disable via REST API:**

```bash
# Disable a workflow
curl -X PUT \
  -H "Authorization: Bearer $GITHUB_TOKEN" \
  -H "Accept: application/vnd.github+json" \
  https://api.github.com/repos/OWNER/REPO/actions/workflows/WORKFLOW_ID/disable

# Re-enable a workflow
curl -X PUT \
  -H "Authorization: Bearer $GITHUB_TOKEN" \
  -H "Accept: application/vnd.github+json" \
  https://api.github.com/repos/OWNER/REPO/actions/workflows/WORKFLOW_ID/enable
```

---

### 7. **Workflow Status Badges**

Embed a live status badge in your README or other Markdown files to show whether the latest run of a workflow passed or failed.

**Badge URL format:**

```text
https://github.com/{OWNER}/{REPO}/actions/workflows/{WORKFLOW_FILE}/badge.svg
```

**Branch-specific badge** (defaults to the default branch):

```text
https://github.com/{OWNER}/{REPO}/actions/workflows/{WORKFLOW_FILE}/badge.svg?branch=main
```

**Markdown embed:**

```markdown
[![CI](https://github.com/myorg/myrepo/actions/workflows/ci.yml/badge.svg)](https://github.com/myorg/myrepo/actions/workflows/ci.yml)

<!-- Branch-specific badge -->

[![Build Status](https://github.com/myorg/myrepo/actions/workflows/build.yml/badge.svg?branch=release)](https://github.com/myorg/myrepo/actions/workflows/build.yml?query=branch%3Arelease)
```

**Badge states:** `passing`, `failing`, `no status` (no runs yet), `cancelled`, `skipped`

---



---

## Navigation

- [← Back to Index](INDEX.md)
- [← Previous: 10-Workflow-Caching](10-Workflow-Caching.md)
- [Next: 12-Workflow-Debugging →](12-Workflow-Debugging.md)
