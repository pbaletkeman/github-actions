# GitHub Workflows: Complete Guide

## Table of Contents

1. [Introduction](#introduction)
2. [Contextual Information in GitHub Workflows](#contextual-information-in-github-workflows)
3. [GitHub Workflow File Structure](#github-workflow-file-structure)
4. [Common Failures and Troubleshooting](#common-failures-and-troubleshooting)

---

## Introduction

GitHub Actions is a continuous integration and continuous delivery (CI/CD) platform that allows you to automate your build, test, and deployment pipeline. Workflows are automated processes defined in YAML files that run jobs in response to events in your GitHub repository.

---

## Contextual Information in GitHub Workflows

GitHub provides rich contextual information through **contexts** that you can use in your workflows. These contexts contain information about the workflow run, the trigger event, the job, and the runner.

### 1. **github Context**

The `github` context contains information about the workflow run and the event that triggered it.

#### Key Variables in `github` Context:

| Variable                  | Description                                                                      | Example                                          |
| ------------------------- | -------------------------------------------------------------------------------- | ------------------------------------------------ |
| `github.action`           | The name of the action currently running                                         | `actions/checkout@v3`                            |
| `github.action_path`      | The path of the action in the repository                                         | `/home/runner/work/_actions/actions/checkout/v3` |
| `github.actor`            | The login of the user that initiated the workflow run                            | `octocat`                                        |
| `github.base_ref`         | The base branch of the pull request                                              | `main`                                           |
| `github.head_ref`         | The head branch of the pull request                                              | `feature-branch`                                 |
| `github.event_name`       | The name of the event that triggered the workflow                                | `push`, `pull_request`, `schedule`               |
| `github.ref`              | The branch or tag ref that triggered the workflow                                | `refs/heads/main`                                |
| `github.ref_name`         | The branch or tag name without `refs/heads/` or `refs/tags/`                     | `main`                                           |
| `github.repository`       | The owner and repository name                                                    | `octocat/Hello-World`                            |
| `github.repository_owner` | The repository owner's login                                                     | `octocat`                                        |
| `github.run_id`           | A unique number for each workflow run within a repository                        | `1296269`                                        |
| `github.run_number`       | A unique number for each run of a particular workflow in a repository            | `3`                                              |
| `github.server_url`       | Returns the URL of the GitHub server                                             | `https://github.com`                             |
| `github.sha`              | The commit SHA that triggered the workflow                                       | `e1c3a851c5caf1e2370a8d9ef4a18a1f6f26f34`        |
| `github.token`            | A token to authenticate on behalf of the GitHub App installed on your repository | (automatically provided)                         |
| `github.workflow`         | The name of the workflow                                                         | `CI`                                             |

#### Example Usage:

```yaml
name: Context Example

on: [push, pull_request]

jobs:
  context-demo:
    runs-on: ubuntu-latest
    steps:
      - name: Print GitHub Context
        run: |
          echo "Event Name: ${{ github.event_name }}"
          echo "Actor: ${{ github.actor }}"
          echo "Repository: ${{ github.repository }}"
          echo "Commit SHA: ${{ github.sha }}"
          echo "Branch: ${{ github.ref_name }}"
          echo "Run ID: ${{ github.run_id }}"
```

---

### 2. **env Context**

The `env` context contains environment variables that have been set in a workflow, job, or step.

```yaml
name: Environment Variables Example

env:
  REGISTRY: ghcr.io
  IMAGE_NAME: ${{ github.repository }}

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Use Environment Variables
        run: |
          echo "Registry: ${{ env.REGISTRY }}"
          echo "Image: ${{ env.IMAGE_NAME }}"
```

---

### 3. **secrets Context**

The `secrets` context contains the names and values of secrets that are available to a workflow run.

```yaml
name: Using Secrets

on: [push]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - name: Deploy with Secret
        env:
          DB_PASSWORD: ${{ secrets.DB_PASSWORD }}
        run: |
          echo "Deploying with secure credentials..."
          # Use the secret in your deployment script
```

**Important**: Secrets are never printed to logs. If you accidentally pass a secret to stdout, GitHub will automatically redact it from the logs.

---

### 4. **job Context**

The `job` context contains information about the currently executing job.

| Variable        | Description                                                                  |
| --------------- | ---------------------------------------------------------------------------- |
| `job.container` | Information about the container of the job                                   |
| `job.services`  | The services created for a job in a workflow                                 |
| `job.status`    | The current status of the job (`success`, `failure`, `cancelled`, `skipped`) |

#### Example:

```yaml
name: Job Status Example

on: [push]

jobs:
  check-status:
    runs-on: ubuntu-latest
    steps:
      - name: Check Job Status
        run: echo "Job Status: ${{ job.status }}"
```

---

### 5. **runner Context**

The `runner` context contains information about the runner that is executing the current job.

| Variable            | Description                                                                       | Example                     |
| ------------------- | --------------------------------------------------------------------------------- | --------------------------- |
| `runner.name`       | The name of the runner executing the job                                          | `GitHub Actions 1`          |
| `runner.os`         | The operating system of the runner                                                | `Linux`, `Windows`, `macOS` |
| `runner.arch`       | The architecture of the runner                                                    | `X64`, `ARM64`              |
| `runner.temp`       | The path of the temporary directory on the runner                                 | `/home/runner/work/_temp`   |
| `runner.tool_cache` | The path of the directory containing preinstalled tools for GitHub-hosted runners | `/opt/hostedtoolcache`      |
| `runner.workspace`  | The path of the workspace directory                                               | `/home/runner/work`         |

#### Example:

```yaml
name: Runner Information

on: [push]

jobs:
  runner-info:
    runs-on: ubuntu-latest
    steps:
      - name: Print Runner Info
        run: |
          echo "Runner OS: ${{ runner.os }}"
          echo "Runner Architecture: ${{ runner.arch }}"
          echo "Temp Directory: ${{ runner.temp }}"
```

---

### 6. **steps Context**

The `steps` context contains information about the steps that have already run in the current job.

```yaml
name: Steps Context Example

on: [push]

jobs:
  step-context-demo:
    runs-on: ubuntu-latest
    steps:
      - name: First Step
        id: first
        run: echo "result=Hello" >> $GITHUB_OUTPUT

      - name: Second Step
        run: echo "Output from first step: ${{ steps.first.outputs.result }}"
```

---

### 7. **matrix Context**

The `matrix` context contains the matrix properties defined in the workflow that apply to the current job. It's used when defining matrix builds.

```yaml
name: Matrix Context Example

on: [push]

jobs:
  build:
    runs-on: ${{ matrix.os }}
    strategy:
      matrix:
        os: [ubuntu-latest, windows-latest, macos-latest]
        node-version: [14, 16, 18]
    steps:
      - name: Print Matrix Context
        run: |
          echo "OS: ${{ matrix.os }}"
          echo "Node Version: ${{ matrix.node-version }}"
```

---

### 8. **inputs Context**

The `inputs` context contains input properties passed to a reusable workflow.

```yaml
name: Workflow triggered with inputs

on:
  workflow_call:
    inputs:
      environment:
        required: true
        type: string

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - name: Deploy to Environment
        run: echo "Deploying to ${{ inputs.environment }}"
```

---

### 9. **needs Context**

The `needs` context contains outputs from all jobs that are defined as a dependency of the current job.

```yaml
name: Job Dependencies

on: [push]

jobs:
  setup:
    runs-on: ubuntu-latest
    outputs:
      build-id: ${{ steps.build.outputs.id }}
    steps:
      - name: Generate Build ID
        id: build
        run: echo "id=$(date +%s)" >> $GITHUB_OUTPUT

  deploy:
    needs: setup
    runs-on: ubuntu-latest
    steps:
      - name: Deploy with Build ID
        run: echo "Deploying build: ${{ needs.setup.outputs.build-id }}"
```

---

## Context Availability Reference

This section shows which contexts can be used in different parts of a GitHub workflow file. Understanding context availability is crucial for proper workflow configuration.

### Contexts by Workflow Key

| Workflow Key                               | Available Contexts                                                                                    | Special Functions                                                  | Notes                                       |
| ------------------------------------------ | ----------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------ | ------------------------------------------- |
| `run-name`                                 | `github`, `inputs`, `vars`                                                                            | None                                                               | Runs at workflow initialization             |
| `concurrency`                              | `github`, `inputs`, `vars`                                                                            | None                                                               | Used to manage concurrent workflow runs     |
| `env`                                      | `github`, `inputs`, `vars`                                                                            | None                                                               | Workflow-level environment variables        |
| `defaults.run.shell`                       | `github`, `inputs`, `vars`                                                                            | None                                                               | Static configuration, no special evaluation |
| `defaults.run.working-directory`           | `github`, `inputs`, `vars`                                                                            | None                                                               | Static configuration                        |
| `jobs.<job_id>.name`                       | `github`, `inputs`, `vars`                                                                            | None                                                               | Job display name                            |
| `jobs.<job_id>.if`                         | `github`, `secrets`, `inputs`, `vars`, `needs`                                                        | `always()`, `success()`, `failure()`, `cancelled()`, `hashFiles()` | Conditional job execution                   |
| `jobs.<job_id>.runs-on`                    | `github`, `inputs`, `vars`                                                                            | None                                                               | Runner selection                            |
| `jobs.<job_id>.environment`                | `github`, `inputs`, `vars`                                                                            | None                                                               | Environment name selection                  |
| `jobs.<job_id>.outputs`                    | `github`, `inputs`, `vars`, `needs`, `steps`                                                          | None                                                               | Job output definitions                      |
| `jobs.<job_id>.strategy.matrix`            | `github`, `inputs`, `vars`                                                                            | None                                                               | Matrix strategy values                      |
| `jobs.<job_id>.steps[*].name`              | `github`, `env`, `secrets`, `inputs`, `vars`, `needs`, `job`, `runner`, `strategy`, `matrix`, `steps` | None                                                               | Step display name                           |
| `jobs.<job_id>.steps[*].if`                | `github`, `env`, `secrets`, `inputs`, `vars`, `needs`, `job`, `runner`, `strategy`, `matrix`, `steps` | `always()`, `success()`, `failure()`, `cancelled()`, `hashFiles()` | Conditional step execution                  |
| `jobs.<job_id>.steps[*].uses`              | `github`, `inputs`, `vars`                                                                            | None                                                               | Action selection (limited context)          |
| `jobs.<job_id>.steps[*].run`               | `github`, `env`, `secrets`, `inputs`, `vars`, `needs`, `job`, `runner`, `strategy`, `matrix`, `steps` | None                                                               | Shell script execution                      |
| `jobs.<job_id>.steps[*].with`              | `github`, `env`, `secrets`, `inputs`, `vars`, `needs`, `job`, `runner`, `strategy`, `matrix`, `steps` | None                                                               | Action input parameters                     |
| `jobs.<job_id>.steps[*].env`               | `github`, `env`, `secrets`, `inputs`, `vars`, `needs`, `job`, `runner`, `strategy`, `matrix`, `steps` | None                                                               | Step-level environment variables            |
| `jobs.<job_id>.steps[*].working-directory` | `github`, `env`, `inputs`, `vars`                                                                     | None                                                               | Working directory for run step              |
| `jobs.<job_id>.container`                  | `github`, `inputs`, `vars`                                                                            | None                                                               | Container image and options                 |
| `jobs.<job_id>.services.<service_id>`      | `github`, `inputs`, `vars`                                                                            | None                                                               | Service container configuration             |
| `jobs.<job_id>.timeout-minutes`            | `github`, `inputs`, `vars`                                                                            | None                                                               | Job timeout duration                        |

### Contexts by Scope

#### Workflow-Level Contexts

These contexts are available throughout the workflow:

```yaml
- github: Available everywhere
- inputs: Available everywhere (for workflows with inputs)
- vars: Available everywhere
- secrets: Available in jobs and steps (not in workflow-level keys)
```

#### Job-Level Contexts

These contexts are available within job and step configurations:

```yaml
- needs: Available after job dependencies
- job: Available within job steps
- runner: Available within job steps
- strategy: Available for matrix builds
- matrix: Available for matrix jobs
```

#### Step-Level Contexts

Full context availability within step execution:

```yaml
- env: Current and workflow-level environment variables
- secrets: All available secrets
- steps: Outputs from previous steps
```

### Usage Examples

#### Example 1: Using Contexts at Workflow Level

```yaml
name: ${{ github.event_name }} - Workflow
run-name: Run #${{ github.run_number }} of ${{ github.repository }}

on:
  push:
    branches: [main]
  workflow_dispatch:
    inputs:
      environment:
        required: true
        type: choice
        options: [dev, staging, prod]

concurrency:
  group: ${{ github.workflow }}-${{ github.ref }}
  cancel-in-progress: true

env:
  REGISTRY: ghcr.io
  IMAGE_NAME: ${{ github.repository }}
```

#### Example 2: Using Contexts at Job Level

```yaml
jobs:
  build:
    name: Build for ${{ matrix.os }}
    runs-on: ${{ matrix.os }}
    if: github.event_name == 'push' || github.event.pull_request.draft == false
    strategy:
      matrix:
        os: [ubuntu-latest, windows-latest]
    steps:
      - run: echo "Building on ${{ matrix.os }}"
```

#### Example 3: Using Contexts at Step Level

```yaml
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
        with:
          ref: ${{ github.head_ref || github.ref_name }}

      - name: Conditional step
        if: success() && github.event_name == 'push'
        env:
          DEPLOY_KEY: ${{ secrets.DEPLOY_KEY }}
          RUN_ID: ${{ github.run_id }}
          BUILD_NUMBER: ${{ github.run_number }}
        run: |
          echo "Run ID: $RUN_ID"
          echo "Build: $BUILD_NUMBER"
```

#### Example 4: Cross-Job Context Usage with needs

```yaml
jobs:
  setup:
    runs-on: ubuntu-latest
    outputs:
      version: ${{ steps.version.outputs.value }}
    steps:
      - id: version
        run: echo "value=$(date +%Y.%m.%d)" >> $GITHUB_OUTPUT

  deploy:
    needs: setup
    runs-on: ubuntu-latest
    if: success() # Job-level context
    steps:
      - name: Deploy
        run: echo "Deploying version ${{ needs.setup.outputs.version }}"
        env:
          DEPLOY_VERSION: ${{ needs.setup.outputs.version }}
```

### Important Notes on Context Availability

**1. Secret Redaction**
Secrets are never available at certain levels to prevent accidental exposure:

- Not available in `uses` (action selection)
- Always redacted in logs if accidentally output

**2. Limited Context in Dynamic Action Selection**

```yaml
# ❌ This will NOT work as expected
- uses: ${{ github.event_name == 'push' && 'actions/checkout@v3' || 'actions/download-artifact@v3' }}

# ✅ Use if condition instead
- uses: actions/checkout@v3
  if: github.event_name == 'push'
```

**3. Matrix Context Availability**
The `matrix` context is only available within the job where it's defined:

```yaml
strategy:
  matrix:
    os: [ubuntu-latest, windows-latest]
    node: [16, 18]

steps:
  - run: echo "OS: ${{ matrix.os }}, Node: ${{ matrix.node }}"  # ✓ Works

other-job:
  steps:
    - run: echo "OS: ${{ matrix.os }}"  # ✗ Does not work, no matrix in this job
```

**4. Passing Context Between Jobs**
To use values from one job in another, use job outputs:

```yaml
jobs:
  job1:
    outputs:
      value: ${{ steps.step.outputs.result }}

  job2:
    needs: job1
    steps:
      - run: echo ${{ needs.job1.outputs.value }}
```

---

## GitHub Workflow File Structure

A GitHub workflow file is a YAML file that defines one or more jobs. This section explains all the components and their purposes.

### 1. **Basic Structure**

```yaml
name: Workflow Name

on: [event] # Trigger events

env: # Environment variables
  VARIABLE_NAME: value

jobs:
  job-id:
    runs-on: runner
    steps:
      - uses: action-name@version
      - run: command
```

---

### 2. **name**

The name of your workflow. It's displayed on your repository's "Actions" page.

```yaml
name: CI/CD Pipeline
```

---

### 3. **on** (Events)

Specifies the events that trigger the workflow. Can be a single event or multiple events.

#### Common Trigger Events:

```yaml
# Single event
on: push

# Multiple events
on: [push, pull_request]

# Event with specific filters
on:
  push:
    branches:
      - main
      - develop
    paths:
      - 'src/**'
      - 'package.json'
  pull_request:
    branches: [main]

# Scheduled event (cron)
on:
  schedule:
    - cron: '0 0 * * *'  # Daily at midnight

# Manual trigger
on: workflow_dispatch

# External trigger
on:
  workflow_run:
    workflows: ["Deploy"]
    types: [completed]
```

---

### 4. **env**

Environment variables that are available to all jobs and steps in the workflow.

```yaml
env:
  NODE_ENV: production
  DATABASE_URL: ${{ secrets.DATABASE_URL }}
  CACHE_DIR: ./cache
```

---

### 5. **defaults**

Default settings for all jobs in the workflow.

```yaml
defaults:
  run:
    shell: bash
    working-directory: ./src
```

---

### 6. **concurrency**

Ensures that only a single job or workflow using the same concurrency group will run at a time.

```yaml
concurrency:
  group: ${{ github.workflow }}-${{ github.ref }}
  cancel-in-progress: true
```

---

### 7. **jobs**

A workflow run is made up of one or more jobs. Jobs run in parallel by default, unless configured otherwise.

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

  test:
    needs: build # This job depends on 'build'
    runs-on: ubuntu-latest
    steps:
      - run: npm test
```

#### Job Properties:

##### **name**

The display name of your job.

```yaml
jobs:
  test:
    name: Run Tests
    runs-on: ubuntu-latest
```

##### **runs-on**

The type of machine to run the job on.

```yaml
runs-on: ubuntu-latest
# or
runs-on: [ubuntu-latest, windows-latest]  # Matrix
# or
runs-on: self-hosted  # Self-hosted runner
```

##### **environment**

The environment that the job references.

```yaml
jobs:
  deploy:
    environment: production
    runs-on: ubuntu-latest
```

##### **outputs**

A map of outputs for a job. Job outputs are available to all downstream jobs that depend on this job.

```yaml
jobs:
  setup:
    runs-on: ubuntu-latest
    outputs:
      version: ${{ steps.vars.outputs.version }}
    steps:
      - id: vars
        run: echo "version=$(date +%Y.%m.%d)" >> $GITHUB_OUTPUT

  deploy:
    needs: setup
    runs-on: ubuntu-latest
    steps:
      - run: echo "Deploying version ${{ needs.setup.outputs.version }}"
```

##### **strategy**

Used for matrix builds and other strategies.

```yaml
strategy:
  matrix:
    node-version: [14, 16, 18]
    os: [ubuntu-latest, windows-latest]
  max-parallel: 2
  fail-fast: false
```

##### **if**

Prevents a job from running unless a condition is met.

```yaml
jobs:
  deploy:
    if: github.ref == 'refs/heads/main' && github.event_name == 'push'
    runs-on: ubuntu-latest
```

##### **steps**

A sequence of tasks that will be executed as part of the job.

```yaml
steps:
  - uses: actions/checkout@v3
  - run: npm install
  - run: npm run build
```

---

### 8. **Steps**

Steps are individual tasks that run sequentially within a job. Each step can run a script or an action.

#### Step Properties:

##### **name**

The name of the step.

```yaml
- name: Install dependencies
  run: npm install
```

##### **uses**

Selects an action to run as part of a step.

```yaml
- uses: actions/checkout@v3
  with:
    ref: main
    fetch-depth: 0
```

##### **run**

Runs command-line programs using the operating system's shell.

```yaml
- run: npm run build
  shell: bash
  working-directory: ./src
```

##### **with**

Input parameters defined by the action.

```yaml
- uses: docker/build-push-action@v4
  with:
    context: .
    push: true
    tags: myimage:latest
```

##### **env**

Environment variables specific to this step.

```yaml
- name: Deploy
  env:
    DEPLOY_KEY: ${{ secrets.DEPLOY_KEY }}
  run: ./deploy.sh
```

##### **if**

Conditional execution of a step.

```yaml
- name: Notify Slack
  if: failure()
  run: curl -X POST ${{ secrets.SLACK_WEBHOOK }}
```

##### **id**

A unique identifier for the step. Used to reference outputs from this step in other steps.

```yaml
- name: Build
  id: build
  run: npm run build

- name: Check Build Size
  run: ls -lah dist/
```

##### **timeout-minutes**

The maximum number of minutes to run the step before GitHub terminates the process.

```yaml
- name: Long-running task
  run: ./long-process.sh
  timeout-minutes: 60
```

##### **continue-on-error**

Prevents a job from failing when a step fails.

```yaml
- name: Test
  run: npm test
  continue-on-error: true
```

---

### 9. **Container**

Run Steps or Actions in a Docker container.

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    container:
      image: node:18
      env:
        NODE_ENV: development
      options: --cpus 1
    steps:
      - uses: actions/checkout@v3
      - run: npm install
```

---

### 10. **services**

Additional containers to host services for a job in a workflow.

```yaml
jobs:
  test:
    runs-on: ubuntu-latest
    services:
      postgres:
        image: postgres:13
        env:
          POSTGRES_PASSWORD: postgres
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5
        ports:
          - 5432:5432
    steps:
      - uses: actions/checkout@v3
      - run: npm test
```

---

### 11. **permissions**

Modify the default permissions granted to the GITHUB_TOKEN.

```yaml
permissions:
  contents: read
  pages: write
  id-token: write
```

---

### 12. **Complete Workflow Example**

```yaml
name: Full CI/CD Pipeline

on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main]
  schedule:
    - cron: "0 2 * * *"

env:
  NODE_ENV: production
  REGISTRY: ghcr.io

defaults:
  run:
    shell: bash

permissions:
  contents: read
  packages: write

jobs:
  lint:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Setup Node.js
        uses: actions/setup-node@v3
        with:
          node-version: 18
          cache: npm

      - name: Install dependencies
        run: npm ci

      - name: Run linter
        run: npm run lint

  test:
    runs-on: ubuntu-latest
    needs: lint
    strategy:
      matrix:
        node-version: [16, 18, 20]
    steps:
      - uses: actions/checkout@v3

      - name: Setup Node.js ${{ matrix.node-version }}
        uses: actions/setup-node@v3
        with:
          node-version: ${{ matrix.node-version }}
          cache: npm

      - name: Install dependencies
        run: npm ci

      - name: Run tests
        run: npm test
        env:
          CI: true

  build:
    runs-on: ubuntu-latest
    needs: test
    outputs:
      image-tag: ${{ steps.meta.outputs.tags }}
    steps:
      - uses: actions/checkout@v3

      - name: Set up Docker Buildx
        uses: docker/setup-buildx-action@v2

      - name: Generate image metadata
        id: meta
        uses: docker/metadata-action@v4
        with:
          images: ${{ env.REGISTRY }}/myapp
          tags: |
            type=ref,event=branch
            type=sha,prefix={{branch}}-
            type=semver,pattern={{version}}

      - name: Build Docker image
        uses: docker/build-push-action@v4
        with:
          context: .
          push: false
          tags: ${{ steps.meta.outputs.tags }}
          labels: ${{ steps.meta.outputs.labels }}

  deploy:
    if: github.ref == 'refs/heads/main' && github.event_name == 'push'
    runs-on: ubuntu-latest
    needs: build
    environment: production
    steps:
      - uses: actions/checkout@v3

      - name: Deploy to production
        run: |
          echo "Deploying to production..."
          echo "Build tag: ${{ needs.build.outputs.image-tag }}"
        env:
          DEPLOY_TOKEN: ${{ secrets.DEPLOY_TOKEN }}
```

---

## Common Failures and Troubleshooting

### 1. **Authentication Errors**

#### Problem: Permission Denied

```
fatal: could not read Username for 'https://github.com': No such file or directory
```

#### Causes:

- Missing or invalid GitHub token
- SSH key not configured for self-hosted runners
- GITHUB_TOKEN doesn't have sufficient permissions

#### Solutions:

**Using GITHUB_TOKEN (automatically provided):**

```yaml
- uses: actions/checkout@v3
  with:
    token: ${{ secrets.GITHUB_TOKEN }}
```

**Using Personal Access Token:**

```yaml
- uses: actions/checkout@v3
  with:
    token: ${{ secrets.PERSONAL_ACCESS_TOKEN }}
```

**Setting SSH key for private dependencies:**

```yaml
- uses: webfactory/ssh-agent@v0.7.0
  with:
    ssh-private-key: ${{ secrets.SSH_PRIVATE_KEY }}
```

---

### 2. **Dependency Installation Failures**

#### Problem: `npm ci` fails with version conflicts

```
npm ERR! code ERESOLVE
npm ERR! ERESOLVE could not resolve dependencies
```

#### Causes:

- Node.js version mismatch
- Lock file out of sync with package.json
- Conflicting peer dependencies

#### Solutions:

**Ensure Node.js version matches development environment:**

```yaml
- name: Setup Node.js
  uses: actions/setup-node@v3
  with:
    node-version: 18.16.0 # Pin exact version
    cache: npm
```

**Update lock file locally and commit:**

```bash
npm ci --force
# or
npm ci --legacy-peer-deps
```

**Add this to workflow if legacy deps are needed:**

```yaml
- name: Install dependencies
  run: npm ci --legacy-peer-deps
```

---

### 3. **Timeout Errors**

#### Problem: Job times out

```
The operation timed out because it took longer than 360 minutes
```

#### Causes:

- Long-running tests
- Network connectivity issues
- Waiting on external resources
- Infinite loops in workflow logic

#### Solutions:

**Set appropriate timeout:**

```yaml
jobs:
  slow-test:
    runs-on: ubuntu-latest
    timeout-minutes: 120
    steps:
      - run: ./slow-test.sh
```

**Set timeout for individual steps:**

```yaml
- name: Long-running task
  run: ./process.sh
  timeout-minutes: 60
```

**Add retry logic:**

```yaml
- name: Download artifact
  uses: actions/download-artifact@v3
  with:
    name: my-artifact
  continue-on-error: true
```

---

### 4. **Workflow File Syntax Errors**

#### Problem: Workflow doesn't trigger or shows validation error

```
Invalid workflow file at .github/workflows/main.yml: mapping values are not allowed in this context
```

#### Causes:

- Invalid YAML syntax
- Incorrect indentation
- Unclosed quotation marks
- Invalid context expressions

#### Solutions:

**Validate YAML syntax:** Use an online YAML validator or VS Code extension

**Common YAML mistakes:**

```yaml
# ❌ WRONG - Tabs instead of spaces
jobs:
	build:

# ✅ CORRECT - 2 spaces
jobs:
  build:

# ❌ WRONG - Missing quotes for strings with special chars
- run: echo ${{ secrets.TOKEN }}

# ✅ CORRECT - Use quotes
- run: echo "${{ secrets.TOKEN }}"

# ❌ WRONG - Incorrect context syntax
- run: echo $github.sha

# ✅ CORRECT - Use proper syntax
- run: echo ${{ github.sha }}
```

---

### 5. **Runner Issues**

#### Problem: `ubuntu-latest` runner has outdated software

```
The requested image with tag is not available
```

#### Causes:

- Using outdated runner images
- Self-hosted runner issues
- GitHub Hosted runner image update lag

#### Solutions:

**Use specific runner versions:**

```yaml
runs-on: ubuntu-22.04  # Instead of ubuntu-latest
# or
runs-on: macos-13
# or
runs-on: windows-2022
```

**For self-hosted runners, ensure they're up to date:**

```bash
# On the self-hosted machine
./config.sh remove
./config.sh
```

---

### 6. **Artifact and Caching Issues**

#### Problem: Artifact not found when downloading

```
An error occurred when trying to download an artifact using the provided path
```

#### Causes:

- Artifact upload failed silently
- Artifact deleted before download
- Job didn't run (skipped due to `if:` condition)

#### Solutions:

**Ensure artifact is uploaded:**

```yaml
- name: Build
  run: npm run build

- name: Upload artifact
  uses: actions/upload-artifact@v3
  if: success() # Only upload on success
  with:
    name: build-artifacts
    path: dist/
    retention-days: 5

- name: Download artifact
  uses: actions/download-artifact@v3
  with:
    name: build-artifacts
```

**Debug artifact issues:**

```yaml
- name: List artifacts
  if: always()
  run: ls -la dist/
```

---

### 7. **Matrix Build Failures**

#### Problem: One matrix combination fails and stops all others

```
Error building for node@16 with os@ubuntu
```

#### Causes:

- `fail-fast: true` (default behavior)
- One combination has specific issue

#### Solutions:

**Run all combinations even if one fails:**

```yaml
strategy:
  matrix:
    node-version: [14, 16, 18]
    os: [ubuntu-latest, windows-latest]
  fail-fast: false # Continue other jobs
```

**Skip specific combinations:**

```yaml
strategy:
  matrix:
    node-version: [14, 16, 18]
    os: [ubuntu-latest, windows-latest]
    exclude:
      - node-version: 14
        os: windows-latest # Skip this combination
```

---

### 8. **Secret Management Issues**

#### Problem: Secret is redacted/not available

```
Error: DEPLOY_TOKEN is not recognized
```

#### Causes:

- Secret name doesn't match
- Secret not added to repository
- Using wrong context syntax
- Scope issues for organization secrets

#### Solutions:

**Correct usage:**

```yaml
- name: Deploy
  env:
    TOKEN: ${{ secrets.DEPLOY_TOKEN }}
  run: ./deploy.sh "$TOKEN"
```

**Never echo secrets directly:**

```yaml
# ❌ WRONG - Will be redacted in logs
- run: echo ${{ secrets.TOKEN }}

# ✅ CORRECT - Use in environment variable
- env:
    SECRET: ${{ secrets.TOKEN }}
  run: |
    echo $SECRET | command
```

**Organization secrets:**

```yaml
env:
  TOKEN: ${{ secrets.ORG_SECRET }} # Requires permissions
```

---

### 9. **Step Output Issues**

#### Problem: Cannot reference step output in next step

```
echo ${{ steps.build.outputs.result }}  Returns empty string
```

#### Causes:

- Step doesn't have an `id` assigned
- Output not properly written to GITHUB_OUTPUT
- Step was skipped

#### Solutions:

**Properly set step outputs:**

```yaml
- name: Build
  id: build
  run: |
    VERSION=$(npm run get-version)
    echo "version=$VERSION" >> $GITHUB_OUTPUT
    echo "timestamp=$(date)" >> $GITHUB_OUTPUT

- name: Use outputs
  run: |
    echo "Version: ${{ steps.build.outputs.version }}"
    echo "Timestamp: ${{ steps.build.outputs.timestamp }}"
```

---

### 10. **Performance Issues**

#### Problem: Workflows run slowly

```
Workflow taking 30+ minutes for simple tasks
```

#### Causes:

- Jobs running sequentially unnecessarily
- Large dependencies being installed repeatedly
- No caching strategy

#### Solutions:

**Use job dependencies efficiently:**

```yaml
jobs:
  build:
    runs-on: ubuntu-latest

  test-unit:
    needs: build # Parallel okay
    runs-on: ubuntu-latest

  test-integration:
    needs: build # Also parallel okay
    runs-on: ubuntu-latest
```

**Implement caching:**

```yaml
- name: Setup Node.js
  uses: actions/setup-node@v3
  with:
    node-version: 18
    cache: npm # Cache node_modules

- name: Setup Gradle cache
  uses: gradle/gradle-build-action@v2 # Includes caching
```

**Limit concurrency:**

```yaml
strategy:
  matrix:
    # Keep matrix size reasonable
    node-version: [18, 20]
  max-parallel: 2 # Limit parallel runs
```

---

### 11. **Docker and Container Issues**

#### Problem: Docker image push fails

```
denied: requested access to the resource is denied
```

#### Causes:

- Authentication not configured
- Missing permissions for registry
- Tag format incorrect

#### Solutions:

**Authenticate with Docker registry:**

```yaml
- name: Login to Docker Hub
  uses: docker/login-action@v2
  with:
    username: ${{ secrets.DOCKER_USERNAME }}
    password: ${{ secrets.DOCKER_PASSWORD }}

- name: Build and push
  uses: docker/build-push-action@v4
  with:
    context: .
    push: true
    tags: myrepo/myimage:latest
```

**For GitHub Container Registry:**

```yaml
- name: Login to GHCR
  uses: docker/login-action@v2
  with:
    registry: ghcr.io
    username: ${{ github.actor }}
    password: ${{ secrets.GITHUB_TOKEN }}
```

---

### 12. **Notification and Rollback Issues**

#### Problem: Notifications fail silently

```
Workflow succeeds but no Slack message sent
```

#### Causes:

- Webhook URL incorrect or expired
- Step only runs on success
- Missing error handling

#### Solutions:

**Enable notifications on all job states:**

```yaml
- name: Slack notification
  if: always() # Run regardless of previous step outcome
  uses: slackapi/slack-github-action@v1.24.0
  with:
    webhook-url: ${{ secrets.SLACK_WEBHOOK }}
    payload: |
      {
        "text": "Build ${{ job.status }}: ${{ github.repository }} - ${{ github.ref_name }}"
      }
```

**Conditional notifications:**

```yaml
- name: Notify on failure
  if: failure()
  uses: slackapi/slack-github-action@v1.24.0
  with:
    webhook-url: ${{ secrets.SLACK_WEBHOOK }}
    payload-file-path: ./slack-payload.json
```

---

### 13. **Quick Troubleshooting Checklist**

- [ ] Check workflow syntax with YAML validator
- [ ] Verify all required contexts are available for the triggered event
- [ ] Check that secrets and environment variables are properly named
- [ ] Ensure steps have unique `id` values if outputs are referenced later
- [ ] Verify runner has required tools (Node, Docker, etc.)
- [ ] Check job `if:` conditions aren't blocking execution
- [ ] Look at step output for `==skip reason==` indicators
- [ ] Verify cache keys are stable and appropriate
- [ ] Check concurrent job limits aren't being exceeded
- [ ] Review GitHub Actions rate limits and API usage
- [ ] Ensure file permissions are correct for scripts
- [ ] Validate Docker image names and registry access

---

## Additional Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [GitHub Actions Contexts](https://docs.github.com/en/actions/learn-github-actions/contexts)
- [Workflow Syntax](https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions)
- [GitHub Actions Marketplace](https://github.com/marketplace?type=actions)

---
