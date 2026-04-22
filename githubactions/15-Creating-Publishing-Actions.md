[← Back to Index](INDEX.md)

## Creating and Publishing Actions

### What are GitHub Actions

GitHub Actions are reusable units of code that perform specific tasks. You can create custom actions from Docker containers, JavaScript, or composite scripts, then publish them to the GitHub Marketplace or use them privately across repositories.

### Why Create Custom Actions

**Key Benefits:**

1. **Code Reuse**: Share functionality across multiple workflows
2. **Abstraction**: Hide complexity behind simple interface
3. **Maintainability**: Update logic in one place
4. **Standardization**: Enforce consistent practices
5. **Community**: Share utilities with broader developer ecosystem
6. **Discoverability**: Marketplace makes finding actions easy
7. **Versioning**: Release versions independently from workflows

### How Actions Work

**Action Types:**

1. **JavaScript Actions**: Node.js-based, fast execution
2. **Docker Container Actions**: Any language, larger file size
3. **Composite Actions**: Combine multiple steps using workflow syntax

### 1. **Creating a JavaScript Action**

**Project Structure:**

```text
my-action/
├── action.yml           # Action metadata
├── package.json         # Node.js dependencies
├── index.js            # Main action code
├── lib/
│   └── utils.js        # Helper functions
└── README.md           # Documentation
```

**action.yml** - Action Definition

```yaml
name: "Deploy App"
description: "Deploy application to server"

inputs:
  environment:
    description: "Target environment"
    required: true
    default: "staging"

  version:
    description: "Version to deploy"
    required: true

  debug:
    description: "Enable debug mode"
    required: false
    default: "false"

outputs:
  deployment-url:
    description: "URL of deployed application"
    value: ${{ steps.deploy.outputs.url }}

  deployment-id:
    description: "Deployment identifier"
    value: ${{ steps.deploy.outputs.id }}

runs:
  using: "node20"
  main: "index.js"

branding:
  icon: "send"
  color: "blue"
```

**index.js** - Action Implementation

```javascript
const core = require("@actions/core");
const exec = require("@actions/exec");
const github = require("@actions/github");
const fs = require("fs");
const path = require("path");

async function run() {
  try {
    // Get inputs
    const environment = core.getInput("environment");
    const version = core.getInput("version");
    const debug = core.getInput("debug") === "true";

    // Set debug mode
    if (debug) {
      core.debug("Debug mode enabled");
    }

    core.info(`Deploying version ${version} to ${environment}`);

    // Validate inputs
    if (!["staging", "production"].includes(environment)) {
      throw new Error(`Invalid environment: ${environment}`);
    }

    // Get context information
    const context = github.context;
    core.info(`Triggered by: ${context.actor}`);
    core.info(`Repository: ${context.repo.owner}/${context.repo.repo}`);
    core.info(`Branch: ${context.ref}`);

    // Perform deployment
    core.startGroup("Starting deployment");

    // Run deployment command
    let deployUrl = "";
    let deployId = "";

    let output = "";
    const myExec = core.getInput("exec") || "sh";

    await exec.exec("bash", ["./deploy.sh", environment, version], {
      listeners: {
        stdout: (data) => {
          output += data.toString();
        },
        stderr: (data) => {
          core.warning(data.toString());
        },
      },
    });

    // Parse output
    const lines = output.split("\n");
    const urlLine = lines.find((l) => l.includes("DEPLOYMENT_URL="));
    const idLine = lines.find((l) => l.includes("DEPLOYMENT_ID="));

    if (urlLine) {
      deployUrl = urlLine.split("=")[1];
    }
    if (idLine) {
      deployId = idLine.split("=")[1];
    }

    core.endGroup();

    // Set outputs
    core.setOutput("deployment-url", deployUrl);
    core.setOutput("deployment-id", deployId);

    // Create asset
    core.notice(`✓ Deployment successful!\nURL: ${deployUrl}\nID: ${deployId}`);
  } catch (error) {
    core.setFailed(`Action failed: ${error.message}`);
    process.exit(1);
  }
}

run();
```

#### package.json

```json
{
  "name": "deploy-app",
  "version": "1.0.0",
  "main": "index.js",
  "description": "Deploy application to server",
  "dependencies": {
    "@actions/core": "^1.10.0",
    "@actions/exec": "^1.1.1",
    "@actions/github": "^6.0.0"
  },
  "scripts": {
    "build": "npm install",
    "test": "jest"
  }
}
```

### 2. **Using Your JavaScript Action**

```yaml
jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Deploy with Custom Action
        id: deploy
        uses: ./ # Local action in same repo
        with:
          environment: production
          version: 1.2.3
          debug: true

      - name: Use Deployment Output
        run: |
          echo "Deployed URL: ${{ steps.deploy.outputs.deployment-url }}"
          echo "Deployment ID: ${{ steps.deploy.outputs.deployment-id }}"
```

### 3. **Creating a Composite Action**

**action.yml** - Composite Action

```yaml
name: "Build and Test"
description: "Build application and run tests"

inputs:
  node-version:
    description: "Node.js version"
    required: false
    default: "18"

  test-command:
    description: "Command to run tests"
    required: false
    default: "npm test"

outputs:
  build-time:
    description: "Time taken for build"
    value: ${{ steps.build.outputs.time }}

  test-results:
    description: "Test results summary"
    value: ${{ steps.test.outputs.summary }}

runs:
  using: "composite"
  steps:
    - name: Setup Node.js
      uses: actions/setup-node@v3
      with:
        node-version: ${{ inputs.node-version }}
        cache: npm

    - name: Install Dependencies
      run: npm ci
      shell: bash

    - name: Build
      id: build
      run: |
        START=$(date +%s%N)
        npm run build
        END=$(date +%s%N)
        TIME=$(( ($END - $START) / 1000000 ))
        echo "time=${TIME}ms" >> $GITHUB_OUTPUT
      shell: bash

    - name: Run Tests
      id: test
      run: |
        ${{ inputs.test-command }} 2>&1 | tee test-output.log
        PASSED=$(grep -c "passed" test-output.log || echo 0)
        echo "summary=${PASSED} tests passed" >> $GITHUB_OUTPUT
      shell: bash

    - name: Upload Coverage
      if: always()
      uses: codecov/codecov-action@v3
      with:
        files: ./coverage/coverage-final.json
```

### 4. **Publishing Action to Marketplace**

**Create Release Management Action - Marketplace Requirements:**

```markdown
# Checklist for Publishing to Marketplace

✓ Create public repository named [owner]/[action-name]
✓ Add `action.yml` with proper metadata
✓ Add `README.md` with:

- Description of what action does
- Screenshots (if applicable)
- Prequisites
- Usage examples
- Inputs and outputs
- Contributing guidelines
  ✓ Create release with semantic versioning (v1.0.0)
  ✓ Create major version tag (v1)
  ✓ Add LICENSE file (MIT recommended)
  ✓ Add action.yml to repository root
```

#### README.md Template

```markdown
# Deploy App Action

[![GitHub Actions](https://img.shields.io/badge/GitHub-Actions-blue)](https://github.com/features/actions)
[![Marketplace](https://img.shields.io/badge/Marketplace-Available-green)](https://github.com/marketplace/actions/deploy-app)

`Deploy App` is a GitHub Action that deploys your application to a server with automatic health checks and rollback capabilities.

## Features

- ✅ Deploy to staging and production
- ✅ Automatic health checks
- ✅ Rollback on failure
- ✅ Deployment notifications
- ✅ Debug mode support

## Usage

```yaml
- name: Deploy App
  uses: owner/deploy-app-action@v1
  with:
    environment: production
    version: 1.0.0
```
```

```

```

## Inputs

| Input         | Required | Default | Description                              |
| ------------- | -------- | ------- | ---------------------------------------- |
| `environment` | Yes      | -       | Target environment (staging, production) |
| `version`     | Yes      | -       | Version to deploy                        |
| `debug`       | No       | false   | Enable debug logging                     |

## Outputs

| Output           | Description                 |
| ---------------- | --------------------------- |
| `deployment-url` | URL of deployed application |
| `deployment-id`  | Deployment identifier       |

## Example

```yaml
jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: owner/deploy-app-action@v1
        id: deploy
        with:
          environment: production
          version: ${{ github.ref_name }}
      - run: echo "Deployed to ${{ steps.deploy.outputs.deployment-url }}"
```

## License

MIT

```text

```

**Release and Version Management**

```bash
# Create major version tag
git tag -a v1 -m "Release v1"
git push origin v1

# Create specific version tag
git tag -a v1.0.0 -m "Release v1.0.0"
git push origin v1.0.0

# Update major version tag to point to latest minor/patch
git tag -fa v1 -m "Update v1 to latest"
git push origin v1 --force
```

### 4. **Action Versioning & Release Strategies**

#### Semantic Versioning for Actions

GitHub Actions uses **semantic versioning** (MAJOR.MINOR.PATCH):

```text
v1.0.0 → v1.1.0 → v1.2.0 (patch/minor bumps)
v1.x.x → v2.0.0 (major version bump)
```

**Release Guidelines:**

| Version           | When to Release        | Example Change                            | Backward Compatible |
| ----------------- | ---------------------- | ----------------------------------------- | ------------------- |
| **v1.0.0→v1.0.1** | Bug fixes              | Fix typo in logging                       | ✅ Yes              |
| **v1.0.0→v1.1.0** | New features           | Add optional input parameter              | ✅ Yes              |
| **v1.x.x→v2.0.0** | Breaking changes       | Rename required input, remove old feature | ❌ No               |
| **v1→latest**     | Major version tracking | Keep v1 tag pointing to latest v1.x.x     | ✅ Yes              |

#### Major Version Tag Strategy

For optimal user experience, maintain a **major version tag** (e.g., `v1`, `v2`) that points to the latest stable minor/patch version:

```bash
# Release v1.1.0 (new feature)
git tag -a v1.1.0 -m "Add new parameter for cleanup"
git push origin v1.1.0

# Update v1 tag to point to v1.1.0
git tag -fa v1 -m "Latest v1.x release"
git push origin v1 --force-with-lease

# Users with v1 tag automatically get the improvement
# - GitHub Actions caches v1.0.0
# - New workflows using v1 get v1.1.0
# - Old references to v1 won't break
```

**Action Consumer Benefits:**

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      # Gets latest v1.x.x (currently v1.2.5)
      - uses: org/action@v1

      # Gets specific version (never changes)
      - uses: org/action@v1.2.0

      # Gets development version (for testing)
      - uses: org/action@main
```

#### Release Checklist

Before publishing a new version:

- [ ] **Test thoroughly** on target platforms (Linux, Windows, macOS if applicable)
- [ ] **Update action.yml**: Bump version in description/comments
- [ ] **Update README.md**: Document new features or breaking changes
- [ ] **Create CHANGELOG.md entry**: List breaking changes, new features, bug fixes
- [ ] **Merge to main**: All changes should be in main branch before tagging
- [ ] **Create git tag**: `git tag -a vX.Y.Z -m "Release message"`
- [ ] **Push tag**: `git push origin vX.Y.Z`
- [ ] **Update major version tag** (if stable): `git tag -fa vX -m "Latest"` and `git push origin vX --force-with-lease`
- [ ] **Create GitHub Release**: Link to CHANGELOG, include migration notes for major versions
- [ ] **Verify on Marketplace**: Check action display and documentation render correctly

#### Deprecation and Migration Guide

When deprecating or making breaking changes:

```yaml
action.yml:
  description: |
    ↓↓↓ BREAKING CHANGE in v2.0.0 ↓↓↓
    - Input 'docker-user' renamed to 'registry-user'
    - Input 'docker-tag' renamed to 'image-tag'
    See migration guide: github.com/org/action/wiki/v1-to-v2-migration
    ↑↑↑ Use action@v1 for old behavior ↑↑↑

inputs:
  registry-user: # New name
    description: "Docker registry username"
    required: true
  image-tag: # New name
    description: "Image tag (was docker-tag)"
    required: false
    default: "latest"
```

**Migration Guide Template (v1 → v2):**

```markdown
# Migration Guide: v1 → v2

## Breaking Changes

### 1. Input Renaming

- `docker-user` → `registry-user`
- `docker-tag` → `image-tag`

### 2. Output Format Change

- `image-uri` now includes registry
  - v1: `myimage:latest`
  - v2: `ghcr.io/org/myimage:latest`

### 3. GitHub Token Permissions

- Now requires `packages: write` (was `contents: write`)

## Migration Steps

### Before (v1)

```yaml
- uses: org/action@v1
  with:
    docker-user: myuser
    docker-tag: v1.0
```
```

```

### After (v2)

```yaml
- uses: org/action@v2
  permissions:
    packages: write
  with:
    registry-user: myuser
    image-tag: v1.0
```

## Support Timeline

- v1 support ends: December 31, 2025
- v2 is recommended for all new workflows

```text

```

#### Publishing Release Notes

Effective release notes guide users on when to upgrade:

```markdown
# v1.2.0 - New Features and Improvements

## What's New

- 🎉 Added support for multiple registries (GitHub Container Registry + Docker Hub)
- ✨ `registry-select` input allows choosing target registry
- ⚡ 25% faster image push with parallel uploads
- 🔧 Support for custom Dockerfile names via `dockerfile-path` input

## Bug Fixes

- Fixed issue where special characters in image names caused failures
- Corrected permission error when pushing to registry

## Backward Compatible

- All v1.x.x consumers unaffected
- Optional new inputs: `registry-select`, `dockerfile-path`
- Update to v1.2.0 when ready, or stay on v1.1.x

## Upgrade Path

For most users: `uses: org/action@v1` (auto-receives this update)
To opt out: Pin to `uses: org/action@v1.1.5`

## Contributors

Thanks to @user1 for registry support and @user2 for performance improvements!
```

---

### 4.5 **Action Distribution Models**

Actions can be distributed and consumed in different ways. Choose the model that best fits your use case, team structure, and organizational needs.

#### Distribution Models Comparison

| Model                   | Location                 | Visibility           | Discovery                      | Best For                               | Effort | Cost           |
| ----------------------- | ------------------------ | -------------------- | ------------------------------ | -------------------------------------- | ------ | -------------- |
| **Public Repo**         | GitHub repository        | Public               | Stars, search, forks           | Open source, community sharing         | Low    | Free           |
| **Private Repo**        | GitHub repository        | Private organization | Within org                     | Internal tools, proprietary code       | Low    | Free           |
| **GitHub Marketplace**  | Published to Marketplace | Public               | Marketplace UI, verified badge | Broad adoption, trusted actions        | Medium | Free           |
| **Private Marketplace** | Organization Marketplace | Private (org only)   | Within org, org registry       | Enterprise standards, curated list     | Medium | $21/user/month |
| **Git Submodule/Clone** | External repository      | Varies               | Manual checkout                | Version control integration, monorepos | Low    | Free           |
| **Package Registry**    | NPM, Docker Hub, etc.    | Varies               | npm/Docker registries          | Dual distribution (action + package)   | High   | Varies         |

#### Public Repository Model

**Setup:**

```bash
# Create public repository
gh repo create my-action --public --clone
cd my-action

# Create action.yml, index.js, action icons
# Commit and push to main
git add .
git commit -m "Initial action"
git push origin main

# Tag release
git tag -a v1.0.0 -m "Release v1.0.0"
git push origin v1.0.0
```

**Consumer Usage:**

```yaml
- uses: username/my-action@v1.0.0
- uses: username/my-action@v1 # Major version (gets updates)
- uses: username/my-action@main # Development version
```

**Discoverability:**

- GitHub topics/search: Users search "actions" find your repo
- Stars: Community validation
- Forks: Adoption signal

#### Private Repository Model

**Setup:**

```bash
# Create organization private repository
gh repo create my-internal-action --private --org=MY-ORG
```

**Consumer Usage:**

```yaml
jobs:
  build:
    steps:
      - uses: MY-ORG/my-internal-action@v1 # Requires org context
        with:
          api-key: ${{ secrets.INTERNAL_API_KEY }}
```

**Access Control:**

```text
Settings > Actions > Access:
- Accessible to: All repositories
- Or specific repositories
- Team/user permissions via organization roles
```

#### GitHub Marketplace Model

**Requirements:**

```yaml
# action.yml must be valid and complete
name: "My Action"
description: "Clear description"
author: "Your Name"
branding:
  icon: "check-square"
  color: "blue"
inputs:
  # Required: at least one input
  environment:
    description: "Target environment"
    required: true
outputs:
  result:
    description: "Action result"
runs:
  using: "composite"
  steps: [...]
```

**Publishing Steps:**

1. Go to repository → **Code** tab
2. Select "Release" on right side
3. **← or directly**: Settings > Code, security & analysis → Release new version
4. Click "Create a new release"
5. Select/create tag (e.g., `v1.0.0`)
6. Click "Publish release"
7. Action automatically appears on Marketplace (index nightly)

**Consumer Discovery:**

```text
GitHub.com/marketplace
Browse "Actions" category
Search for your action name
Click to view details, installation code
```

**Marketplace Badge:**

```markdown
# My Action

[![GitHub Marketplace](https://img.shields.io/badge/Marketplace-v1.0.0-blue?logo=github)](https://github.com/marketplace/actions/my-action)

[![Tests](https://github.com/username/my-action/workflows/tests/badge.svg)](https://github.com/username/my-action/actions)
```

#### Private Marketplace Model (Enterprise)

**Setup** (requires GitHub Enterprise Cloud):

```bash
# Organization > Settings > Actions > Runners > Private Marketplace

# Add approved actions to private marketplace:
# 1. Go to Action on public marketplace
# 2. Click "Add to organization private marketplace"
# 3. Or: Admin panel > Organization > Actions > Private Marketplace > Add
```

**Policies:**

```text
Organization Private Marketplace:
- Control which actions are approved for use
- Force specific versions (e.g., only v2.0.0+)
- Prevent use of unapproved external actions
- Track usage and compliance
- Curate internal best practices
```

**Consumer Experience:**

```text
When creating new workflow:
- "New workflow" → "Use a public marketplace action"
- Only shows private marketplace + approved public actions
- Enforces org security policies
```

#### Comparison: When to Use Each Model

**Use Public Repo If:**

- Action solves general problem
- Want community feedback and contributions
- Seeking broad adoption
- Open-source philosophy
- Low maintenance burden

**Use Private Repo If:**

- Action is proprietary or business-specific
- Team/organization use only
- Security-sensitive code
- Don't want external visibility
- Simplicity key

**Use Marketplace If:**

- Want maximum discoverability
- Believe many teams need this
- Willing to maintain documentation
- Ready for external feedback and issues
- Plan long-term support

**Use Private Marketplace If:**

- Enterprise organization
- Need governance/compliance
- Want to curate approved actions
- Force policy compliance (specific versions)
- Track organization-wide usage

#### Migration Path Example

```text
Phase 1: Development
┌─────────────────────┐
│ Private Repository  │ (Team develops)
│ + Local testing     │
└─────────────────────┘
           ↓
Phase 2: Stability
┌─────────────────────┐
│ Public Repository   │ (v1.0.0 released)
│ + Public Releases   │ (Get initial feedback)
└─────────────────────┘
           ↓
Phase 3: Discoverability
┌─────────────────────┐
│ GitHub Marketplace  │ (Published & indexed)
│ + Documentation     │ (Examples, use cases)
└─────────────────────┘
           ↓
Phase 4: Enterprise Adoption
┌─────────────────────┐
│ Private Marketplace │ (Org whitelist + governance)
│ + Usage Tracking    │ (Compliance dashboard)
└─────────────────────┘
```

---

### 5. **Best Practices for Actions**

#### ✓ Recommended Practices

```yaml
# ✓ Use semantic versioning
- uses: owner/action@v1.0.0  # Specific version
- uses: owner/action@v1      # Major version (auto-updates)
- uses: owner/action@main    # Development (for testing)

# ✓ Provide clear inputs and outputs
inputs:
  environment:
    description: 'Target environment for deployment'
    required: true
    type: choice
    options:
      - staging
      - production

# ✓ Add comprehensive documentation
# Include examples for common use cases
# Document all inputs, outputs, and error cases

# ✓ Cache dependencies
- name: Cache Node Modules
  uses: actions/cache@v3
  with:
    path: node_modules
    key: ${{ runner.os }}-npm-${{ hashFiles('**/package-lock.json') }}

# ✓ Provide informative output
core.info('Deployment started');
core.debug('Debug information');
core.warning('Warning message');
core.error('Error message');
```

#### ✗ Anti-Patterns to Avoid

```yaml
# ✗ Don't hardcode sensitive information
uses: owner/action@v1
  with:
    api-key: "sk-1234567890"  # NEVER!

# ✗ Don't create actions that require external setup
# Actions should be self-contained

# ✗ Don't ignore action versioning
- uses: owner/action@main  # Risky in production!

# ✗ Don't make breaking changes without major version update
# v1.1.0: backward compatible only
# v2.0.0: breaking changes allowed
```

---

### 6. **Debugging and Troubleshooting Actions**

#### Enabling Debug Logging

GitHub Actions provides two repository secrets that enable verbose diagnostic output:

| Secret                        | Effect                                                            |
| ----------------------------- | ----------------------------------------------------------------- |
| `ACTIONS_STEP_DEBUG = true`   | Shows `core.debug()` output and detailed step-level traces        |
| `ACTIONS_RUNNER_DEBUG = true` | Enables runner-level diagnostic logs (agent and environment info) |

Set these in **Repository Settings → Secrets → Actions** or enable debug logging when re-running a failed job via the **"Enable debug logging"** checkbox in the UI.

#### Debugging JavaScript Actions

```javascript
const core = require("@actions/core");

// Only visible in logs when ACTIONS_STEP_DEBUG=true
core.debug("Entering deployment phase with config: " + JSON.stringify(config));

// Always visible
core.info("Step starting");
core.warning("Non-fatal issue — proceeding with defaults");
core.error("Critical error encountered"); // marks the step as failed

async function run() {
  try {
    const value = core.getInput("my-input", { required: true });
    // ... logic
    core.setOutput("result", value);
  } catch (error) {
    core.setFailed(`Action failed: ${error.message}`);
  }
}

run();
```

> **Common issue:** `Cannot find module '@actions/core'` — `node_modules` were not bundled into `dist/`.
> Fix: run `npx @vercel/ncc build index.js -o dist` and commit the generated `dist/` directory.

#### Debugging Docker Container Actions

```bash
# Pull and inspect the action image locally before using in CI
docker pull ghcr.io/owner/my-docker-action:v1
docker run --rm -it --entrypoint /bin/sh ghcr.io/owner/my-docker-action:v1
# Inside the container, manually run the entrypoint to observe behavior
```

```yaml
# Pass a DEBUG env var — many Docker actions check this
- uses: my-org/my-docker-action@v1
  env:
    DEBUG: "1"
```

> Add `set -x` at the top of your entrypoint shell script to emit a full execution trace in the runner log.

#### Debugging Composite Actions

```yaml
steps:
  - name: Debug inputs
    shell: bash
    run: |
      echo "environment=${{ inputs.environment }}"
      echo "version=${{ inputs.version }}"
      echo "pwd=$(pwd)"

  - name: Your step
    id: main
    shell: bash
    run: ./deploy.sh
    continue-on-error: true  # Prevent early exit so the diagnosis step below runs

  - name: Debug outcome
    if: always()
    shell: bash
    run: echo "Step outcome: ${{ steps.main.outcome }}"
```

#### Common Action Failure Patterns

| Symptom                              | Likely Cause                                   | Resolution                                           |
| ------------------------------------ | ---------------------------------------------- | ---------------------------------------------------- |
| `Error: Required input missing`      | `with:` block in caller omits a required input | Add the missing input in the calling workflow        |
| `Cannot find module '@actions/core'` | `node_modules` not committed / bundled         | Run `ncc build` and commit `dist/`                   |
| Docker action exits with code 1      | Entrypoint script error                        | Add `set -x` in entrypoint for full trace            |
| Composite step silently skipped      | `if:` condition evaluates false                | Check step `if:` expressions and prior step outcomes |
| Output not available to caller       | Step is missing an `id:`                       | Add `id:` to the output-producing step               |
| Dependency version conflict          | `package-lock.json` mismatch                   | Delete `node_modules`, run `npm ci`, rebuild `dist/` |

### 7. **Using Workflow Commands Inside Custom Actions**

Custom actions can emit structured output to the runner log and pass data back to the calling workflow using **workflow commands**. These are special strings written to stdout in the format `::command parameter=value::message`.

#### Workflow Command Reference

| Command syntax                      | Purpose                                                             | Equivalent JS (via `@actions/core`)     |
| ----------------------------------- | ------------------------------------------------------------------- | --------------------------------------- |
| `::debug::message`                  | Print debug-level log (visible only with `ACTIONS_STEP_DEBUG=true`) | `core.debug()`                          |
| `::notice file=f,line=l::message`   | Create a notice annotation in the PR / summary                      | `core.notice()`                         |
| `::warning file=f,line=l::message`  | Create a warning annotation                                         | `core.warning()`                        |
| `::error file=f,line=l::message`    | Create an error annotation                                          | `core.error()`                          |
| `::group::title` / `::endgroup::`   | Collapse a log section in the UI                                    | `core.startGroup()` / `core.endGroup()` |
| `::add-mask::value`                 | Redact `value` from all subsequent log output                       | `core.setSecret()`                      |
| `echo "name=val" >> $GITHUB_OUTPUT` | Set a step output for the caller to use via `steps.<id>.outputs`    | `core.setOutput()`                      |
| `echo "VAR=val" >> $GITHUB_ENV`     | Append an environment variable for subsequent steps                 | `core.exportVariable()`                 |
| `echo "/path" >> $GITHUB_PATH`      | Prepend a directory to `PATH` for subsequent steps                  | `core.addPath()`                        |

> **Note:** The old `::set-output::`, `::set-env::`, and `::add-path::` command syntax is **deprecated**. Use the `GITHUB_OUTPUT`, `GITHUB_ENV`, and `GITHUB_PATH` environment files instead.

#### Using Workflow Commands in Composite Actions

Composite actions (which use shell `run:` steps) communicate with the caller using the same environment files:

```yaml
# .github/actions/my-composite/action.yml
name: My Composite Action
description: Demonstrates workflow command usage in a composite action
inputs:
  version:
    required: true
    description: Version to process
outputs:
  processed-version:
    description: The normalized version string
    value: ${{ steps.normalize.outputs.result }}

runs:
  using: composite
  steps:
    - name: Mask the version from logs if sensitive
      shell: bash
      run: echo "::add-mask::${{ inputs.version }}"

    - name: Normalize version
      id: normalize
      shell: bash
      run: |
        VERSION="${{ inputs.version }}"
        NORMALIZED="${VERSION#v}"    # Strip leading 'v'
        echo "result=$NORMALIZED" >> $GITHUB_OUTPUT
        echo "::notice::Processed version: $NORMALIZED"

    - name: Group diagnostic output
      shell: bash
      run: |
        echo "::group::Diagnostic info"
        echo "Runner OS: ${{ runner.os }}"
        echo "Version input: ${{ inputs.version }}"
        echo "::endgroup::"
```

#### Using Workflow Commands in JavaScript Actions

JavaScript actions use the `@actions/core` library which wraps the same workflow commands:

```javascript
const core = require("@actions/core");

async function run() {
  const version = core.getInput("version");

  // Mask a derived secret before logging
  const token = process.env.API_TOKEN;
  core.setSecret(token); // Equivalent to ::add-mask::

  core.startGroup("Processing version");
  core.debug(`Raw input: ${version}`);
  const normalized = version.replace(/^v/, "");
  core.info(`Normalized version: ${normalized}`);
  core.endGroup();

  // Pass output back to the workflow
  core.setOutput("processed-version", normalized);

  // Annotate a file with a warning (appears in PR diff)
  core.warning("Version format may be deprecated", {
    file: "package.json",
    startLine: 3,
  });
}

run().catch(core.setFailed);
```

#### Accessing Action Outputs in the Calling Workflow

```yaml
jobs:
  example:
    runs-on: ubuntu-latest
    steps:
      - name: Run composite action
        id: my-action
        uses: ./.github/actions/my-composite
        with:
          version: "v1.2.3"

      - name: Use the output
        run: echo "Processed: ${{ steps.my-action.outputs.processed-version }}"
```

---



---

## Navigation

- [← Back to Index](INDEX.md)
- [← Previous: 14-Reviewing-Deployments](14-Reviewing-Deployments.md)
- [Next: 16-Managing-Runners →](16-Managing-Runners.md)
