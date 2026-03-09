# GitHub Workflows: Complete Guide

## Table of Contents

1. [Introduction](#introduction)
2. [Contextual Information in GitHub Workflows](#contextual-information-in-github-workflows)
3. [GitHub Workflow File Structure](#github-workflow-file-structure)
4. [GitHub Workflow Trigger Events](#github-workflow-trigger-events)
5. [Creating and Using Custom Environment Variables](#creating-and-using-custom-environment-variables)
6. [Default Environment Variables](#default-environment-variables)
7. [Environment Protection Rules](#environment-protection-rules)
8. [Common Failures and Troubleshooting](#common-failures-and-troubleshooting)

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

## GitHub Workflow Trigger Events

GitHub provides numerous trigger events that determine when a workflow runs. Understanding all available trigger events allows you to create powerful automation tailored to your specific needs. This section comprehensively documents every trigger event available in GitHub Actions.

### Overview of Trigger Events

Trigger events fall into several categories:

- **Repository Events**: Triggered by changes in the repository (push, pull requests, etc.)
- **External Events**: Triggered by external systems (webhook events, repository dispatch)
- **Time-Based Events**: Triggered on a schedule (cron jobs)
- **Manual Events**: Triggered by user action (workflow dispatch)
- **Workflow Events**: Triggered by other workflows

### 1. **push** - Code Push Event

Triggers when code is pushed to the repository.

```yaml
on: push

# Trigger on specific branches
on:
  push:
    branches:
      - main
      - develop
      - 'release/**'  # Wildcard pattern

# Trigger on specific tags
on:
  push:
    tags:
      - 'v*'  # All versions like v1.0, v2.1.0, etc.
      - 'release-*'

# Trigger on specific file changes (paths)
on:
  push:
    paths:
      - 'src/**'
      - 'package.json'
      - '.github/workflows/**'

# Ignore specific paths
on:
  push:
    paths-ignore:
      - '*.md'
      - 'docs/**'
      - '.gitignore'

# Combine multiple conditions
on:
  push:
    branches: [main]
    paths: ['src/**', 'package.json']
```

#### Practical Example

```yaml
name: Push Event Handler

on:
  push:
    branches: [main, develop]
    paths: ['src/**', 'package.json']

jobs:
  on-push:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - run: echo "Code pushed to ${{ github.ref_name }}"
```

---

### 2. **pull_request** - Pull Request Event

Triggers when pull request events occur (opened, reopened, synchronized, etc.).

```yaml
on: pull_request

# Trigger on specific branches
on:
  pull_request:
    branches: [main, develop]

# Trigger on pull requests that modify specific paths
on:
  pull_request:
    paths:
      - '**/*.js'
      - 'package.json'

# Specific PR actions
on:
  pull_request:
    types:
      - opened
      - reopened
      - synchronize
      - ready_for_review
      - converted_to_draft

# Ignore drafts
on:
  pull_request:
    paths-ignore: ['*.md']
```

#### PR Event Types

| Type | Description |
|------|-------------|
| `opened` | Pull request created |
| `reopened` | Previously closed PR reopened |
| `synchronize` | PR commits added (code changed) |
| `converted_to_draft` | PR converted to draft |
| `ready_for_review` | Draft PR marked ready for review |
| `labeled` | Label added to PR |
| `unlabeled` | Label removed from PR |
| `assigned` | Assignee added |
| `unassigned` | Assignee removed |
| `edited` | PR title/description edited |
| `auto_merge_enabled` | Auto-merge enabled |
| `auto_merge_disabled` | Auto-merge disabled |
| `closed` | PR closed |
| `locked` | PR locked |
| `unlocked` | PR unlocked |
| `review_requested` | Review requested |
| `review_request_removed` | Review request removed |

#### Practical Example

```yaml
name: Pull Request Checks

on:
  pull_request:
    types: [opened, synchronize, ready_for_review]

jobs:
  checks:
    if: github.event.pull_request.draft == false
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - run: npm test
      - run: npm run lint
```

---

### 3. **pull_request_target** - PR Target Event

Similar to `pull_request` but with access to secrets and full write permissions. Use with caution for external contributions.

```yaml
on:
  pull_request_target:
    types: [opened, synchronize]

jobs:
  review:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
        with:
          ref: ${{ github.event.pull_request.head.sha }}
      - run: npm test
```

---

### 4. **workflow_dispatch** - Manual Trigger

Manual trigger via GitHub UI or API.

```yaml
on:
  workflow_dispatch:
    inputs:
      environment:
        description: 'Deployment environment'
        required: true
        type: choice
        options:
          - development
          - staging
          - production
      version:
        description: 'Version to deploy'
        required: false
        type: string
      dry_run:
        description: 'Run as dry-run'
        required: false
        type: boolean
        default: true

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - run: |
          echo "Deploying to ${{ inputs.environment }}"
          echo "Version: ${{ inputs.version }}"
          echo "Dry Run: ${{ inputs.dry_run }}"
```

#### Input Types

| Type | Description | Example |
|------|-------------|---------|
| `string` | Text input | `version: "1.0.0"` |
| `choice` | Dropdown selection | Environment selection |
| `boolean` | Checkbox | `true` or `false` |
| `environment` | Select environment | Production, staging |

---

### 5. **schedule** - Scheduled Events (Cron)

Trigger workflows on a schedule using cron syntax.

```yaml
on:
  schedule:
    # Run every day at midnight UTC
    - cron: '0 0 * * *'
    
    # Run every 6 hours
    - cron: '0 */6 * * *'
    
    # Run at 8 AM Monday-Friday
    - cron: '0 8 * * 1-5'
    
    # Run first day of month at 2 AM
    - cron: '0 2 1 * *'

jobs:
  scheduled-job:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Running scheduled workflow"
```

#### Cron Syntax Reference

```
┌───────────── minute (0 - 59)
│ ┌───────────── hour (0 - 23)
│ │ ┌───────────── day of month (1 - 31)
│ │ │ ┌───────────── month (1 - 12)
│ │ │ │ ┌───────────── day of week (0 - 6) (0 = Sunday)
│ │ │ │ │
│ │ │ │ │
* * * * *
```

#### Common Cron Patterns

| Pattern | Description |
|---------|-------------|
| `0 0 * * *` | Daily at midnight UTC |
| `0 */6 * * *` | Every 6 hours |
| `0 8 * * 1-5` | 8 AM Monday-Friday |
| `0 2 1 * *` | First day of month at 2 AM |
| `*/30 * * * *` | Every 30 minutes |
| `0 9 * * MON` | Every Monday at 9 AM |
| `0 0 * * 0` | Every Sunday at midnight |

---

### 6. **workflow_run** - Trigger on Another Workflow

Triggers based on another workflow's completion.

```yaml
on:
  workflow_run:
    workflows: ['Deploy']  # Workflow name or file path
    types:
      - completed
      - requested

jobs:
  follow-up:
    runs-on: ubuntu-latest
    if: github.event.workflow_run.conclusion == 'success'
    steps:
      - run: echo "Previous workflow completed successfully"
```

#### Workflow Run Types

| Type | Description |
|------|-------------|
| `completed` | Workflow run finished |
| `requested` | Workflow requested to run |

---

### 7. **release** - Release Events

Trigger when releases are created, edited, or deleted.

```yaml
on:
  release:
    types:
      - published
      - created
      - edited
      - deleted
      - prereleased
      - released

jobs:
  on-release:
    runs-on: ubuntu-latest
    steps:
      - run: |
          echo "Release: ${{ github.event.release.tag_name }}"
          echo "Name: ${{ github.event.release.name }}"
```

#### Release Types

| Type | Description |
|------|-------------|
| `published` | Release published (including pre-releases when published) |
| `unpublished` | Release unpublished |
| `created` | Release created (or a pre-release published) |
| `edited` | Release edited |
| `deleted` | Release deleted |
| `prereleased` | Marked as pre-release |
| `released` | Released (after being pre-release) |

---

### 8. **issues** - Issue Events

Trigger on issue activity.

```yaml
on:
  issues:
    types:
      - opened
      - closed
      - reopened
      - assigned
      - labeled
      - milestoned

jobs:
  on-issue:
    runs-on: ubuntu-latest
    steps:
      - run: |
          echo "Issue: ${{ github.event.issue.title }}"
          echo "Action: ${{ github.event.action }}"
```

#### Issue Types

| Type | Description |
|------|-------------|
| `opened` | Issue created |
| `closed` | Issue closed |
| `reopened` | Issue reopened |
| `assigned` | Assignee added |
| `unassigned` | Assignee removed |
| `labeled` | Label added |
| `unlabeled` | Label removed |
| `milestoned` | Milestone added |
| `demilestoned` | Milestone removed |
| `transferred` | Issue transferred |
| `pinned` | Issue pinned |
| `unpinned` | Issue unpinned |
| `deleted` | Issue deleted |

---

### 9. **issue_comment** - Issue Comment Events

Trigger when comments are added/edited on issues or PRs.

```yaml
on:
  issue_comment:
    types: [created, edited, deleted]

jobs:
  on-comment:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Comment: ${{ github.event.comment.body }}"
```

---

### 10. **discussion** - Discussion Events

Trigger on discussion activity (private beta).

```yaml
on:
  discussion:
    types:
      - created
      - edited
      - deleted
      - transferred
      - pinned
      - unpinned
      - labeled
      - unlabeled
      - answered
      - unanswered

jobs:
  on-discussion:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Discussion: ${{ github.event.discussion.title }}"
```

---

### 11. **discussion_comment** - Discussion Comment Events

Trigger on discussion comments.

```yaml
on:
  discussion_comment:
    types: [created, edited, deleted]

jobs:
  on-discussion-comment:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Added comment to discussion"
```

---

### 12. **fork** - Repository Fork Event

Trigger when repository is forked.

```yaml
on: fork

jobs:
  on-fork:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Repository was forked"
```

---

### 13. **gollum** - Wiki Changes

Trigger when wiki pages are created or updated.

```yaml
on: gollum

jobs:
  on-wiki-change:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Wiki updated"
```

---

### 14. **watch** - Star/Watch Event

Trigger when repository is starred.

```yaml
on: watch

jobs:
  on-star:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Repository was starred"
```

---

### 15. **create** - Branch/Tag Creation

Trigger when branch or tag is created.

```yaml
on: create

jobs:
  on-create:
    runs-on: ubuntu-latest
    steps:
      - run: |
          echo "Ref: ${{ github.event.ref }}"
          echo "Ref Type: ${{ github.event.ref_type }}"
```

---

### 16. **delete** - Branch/Tag Deletion

Trigger when branch or tag is deleted.

```yaml
on: delete

jobs:
  on-delete:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Branch/tag ${{ github.event.ref }} deleted"
```

---

### 17. **public** - Repository Public Event

Trigger when repository becomes public.

```yaml
on: public

jobs:
  on-public:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Repository is now public"
```

---

### 18. **push to protected branch** - Protected Branch Push

Automatic event when pushing to a branch with branch protection rules.

```yaml
on:
  push:
    branches: [main]  # Main is typically protected

jobs:
  protected-push:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Protected branch updated"
```

---

### 19. **repository_dispatch** - External Trigger via API

Trigger from external systems via GitHub API.

```yaml
on:
  repository_dispatch:
    types: [deploy-prod, run-tests, custom-event]

jobs:
  on-dispatch:
    runs-on: ubuntu-latest
    steps:
      - run: |
          echo "Event type: ${{ github.event.action }}"
          echo "Payload: ${{ toJson(github.event.client_payload) }}"
```

**Trigger via API:**

```bash
curl -X POST https://api.github.com/repos/owner/repo/dispatches \
  -H "Authorization: token YOUR_TOKEN" \
  -H "Accept: application/vnd.github.everest-preview+json" \
  -d '{
    "event_type": "deploy-prod",
    "client_payload": { "branch": "main" }
  }'
```

---

### 20. **check_run** - Check Run Events

Trigger when check run is created or updated.

```yaml
on:
  check_run:
    types: [created, rerequested, completed, requested_action]

jobs:
  on-check-run:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Check run event"
```

---

### 21. **check_suite** - Check Suite Events

Trigger when check suite is created or updated.

```yaml
on:
  check_suite:
    types: [completed]

jobs:
  on-check-suite:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Check suite completed"
```

---

### 22. **pull_request_review** - PR Review Events

Trigger on pull request review actions.

```yaml
on:
  pull_request_review:
    types:
      - submitted
      - edited
      - dismissed

jobs:
  on-review:
    runs-on: ubuntu-latest
    steps:
      - run: |
          echo "Review state: ${{ github.event.review.state }}"
```

---

### 23. **pull_request_review_comment** - PR Review Comment Events

Trigger on comments in pull request reviews.

```yaml
on:
  pull_request_review_comment:
    types: [created, edited, deleted]

jobs:
  on-review-comment:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Review comment added"
```

---

### 24. **member** - Collaborator Events

Trigger when collaborator added to repository.

```yaml
on: member

jobs:
  on-member-add:
    runs-on: ubuntu-latest
    steps:
      - run: echo "New collaborator added"
```

---

### 25. **team_add** - Team Added Event

Trigger when team is added to repository.

```yaml
on: team_add

jobs:
  on-team-add:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Team added to repository"
```

---

### 26. **push_protected_branch** - Protected Branch Push

Automatic trigger for protected branch updates.

```yaml
on:
  push:
    branches:
      - main
      - master
```

---

### Complete Example: All Event Types

```yaml
name: Comprehensive Trigger Events Example

on:
  # Repository events
  push:
    branches: [main]
    paths: ['src/**']
  pull_request:
    types: [opened, synchronize]
  release:
    types: [published]
  issues:
    types: [opened, labeled]
  issue_comment:
    types: [created]
  discussion:
    types: [created]
  fork: {}
  create: {}
  delete: {}
  public: {}
  
  # Scheduled events
  schedule:
    - cron: '0 0 * * *'
  
  # Manual trigger
  workflow_dispatch:
    inputs:
      debug:
        type: boolean
        default: false

jobs:
  handle-events:
    runs-on: ubuntu-latest
    steps:
      - name: Identify Event
        run: |
          echo "Event Name: ${{ github.event_name }}"
          echo "Action: ${{ github.event.action }}"
          case "${{ github.event_name }}" in
            push)
              echo "Code was pushed"
              ;;
            pull_request)
              echo "Pull request: ${{ github.event.action }}"
              ;;
            release)
              echo "Release: ${{ github.event.release.tag_name }}"
              ;;
            issues)
              echo "Issue: ${{ github.event.issue.title }}"
              ;;
            schedule)
              echo "Scheduled run"
              ;;
            workflow_dispatch)
              echo "Manual trigger - Debug: ${{ inputs.debug }}"
              ;;
          esac
```

### Event Availability Summary

| Event | When Triggered | Secret Access | Write Permissions |
|-------|---|---|---|
| `push` | Code push | ✓ | ✓ |
| `pull_request` | PR activity | Limited | Limited |
| `workflow_dispatch` | Manual | ✓ | ✓ |
| `schedule` | Cron schedule | ✓ | ✓ |
| `release` | Release published | ✓ | ✓ |
| `issues` | Issue activity | ✓ | ✓ |
| `issue_comment` | Comments | ✓ | ✓ |
| `workflow_run` | Workflow completes | ✓ | ✓ |
| `repository_dispatch` | API call | ✓ | ✓ |
| `fork` | Repository forked | Limited | Limited |

---

## Creating and Using Custom Environment Variables

Environment variables are a fundamental way to configure workflows dynamically, pass information between steps, and securely manage sensitive data. This section covers the various ways to create, use, and manage custom environment variables in GitHub workflows.

### 1. **Workflow-Level Environment Variables**

Workflow-level environment variables are defined at the top of your workflow file and are available to all jobs and steps.

#### Definition and Usage

```yaml
name: Workflow with Global Env Vars

on: [push]

env:
  NODE_ENV: production
  LOG_LEVEL: debug
  REGISTRY: ghcr.io
  IMAGE_NAME: myapp

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Print environment variables
        run: |
          echo "Environment: $NODE_ENV"
          echo "Log Level: $LOG_LEVEL"
          echo "Registry: $REGISTRY/$IMAGE_NAME"
```

#### With Context Variables

You can use contexts and expressions when defining workflow-level environment variables:

```yaml
env:
  REGISTRY: ghcr.io
  IMAGE_NAME: ${{ github.repository }}
  WORKFLOW_NAME: ${{ github.workflow }}
  RUN_ID: ${{ github.run_id }}
```

### 2. **Job-Level Environment Variables**

Environment variables can be scoped to a specific job, making them available to all steps within that job.

```yaml
jobs:
  deploy:
    runs-on: ubuntu-latest
    env:
      DEPLOY_REGION: us-east-1
      ENVIRONMENT: staging
    steps:
      - name: Deploy application
        run: |
          echo "Deploying to $DEPLOY_REGION"
          echo "Environment: $ENVIRONMENT"
```

#### Job-Level Override of Workflow-Level Variables

```yaml
env:
  ENVIRONMENT: production

jobs:
  test:
    runs-on: ubuntu-latest
    env:
      ENVIRONMENT: development  # Overrides workflow-level
    steps:
      - run: echo "Test environment: $ENVIRONMENT"  # Prints "development"

  deploy:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Deploy environment: $ENVIRONMENT"  # Prints "production"
```

### 3. **Step-Level Environment Variables**

Environment variables can be defined for individual steps, providing the most granular control.

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Build step
        env:
          BUILD_TYPE: release
          OPTIMIZATION_LEVEL: 3
        run: |
          echo "Build Type: $BUILD_TYPE"
          echo "Optimization Level: $OPTIMIZATION_LEVEL"

      - name: Test step
        env:
          TEST_ENV: staging
        run: |
          echo "Test Environment: $TEST_ENV"
          # BUILD_TYPE is not available here
```

#### Step-Level Override of All Levels

```yaml
env:
  CONFIG: default

jobs:
  test:
    runs-on: ubuntu-latest
    env:
      CONFIG: job-level
    steps:
      - name: Step with override
        env:
          CONFIG: step-level  # Most specific, takes precedence
        run: echo "Config: $CONFIG"  # Prints "step-level"

      - name: Step without override
        run: echo "Config: $CONFIG"  # Prints "job-level"
```

### 4. **Using Secrets in Environment Variables**

Secrets are a secure way to store sensitive information and can be referenced in environment variables.

```yaml
jobs:
  deploy:
    runs-on: ubuntu-latest
    env:
      API_KEY: ${{ secrets.API_KEY }}
      DATABASE_URL: ${{ secrets.DATABASE_URL }}
    steps:
      - name: Deploy with credentials
        run: ./deploy.sh
        # Secrets are automatically redacted from logs
```

#### Secret Masking and Redaction

GitHub automatically masks secret values in logs to prevent accidental exposure:

```yaml
jobs:
  secure:
    runs-on: ubuntu-latest
    steps:
      - name: Using secrets safely
        env:
          TOKEN: ${{ secrets.SECRET_TOKEN }}
        run: |
          # ✓ Safe: secret is passed to a command
          curl -H "Authorization: Bearer $TOKEN" https://api.example.com

          # ✗ Unsafe: echoing the secret (will be redacted in logs)
          echo "Token: $TOKEN"  # Logs will show: Token: ***
```

### 5. **Using Contexts and Expressions in Environment Variables**

Environment variables can reference GitHub contexts, providing dynamic configuration.

#### Using github Context

```yaml
env:
  REPO: ${{ github.repository }}
  BRANCH: ${{ github.ref_name }}
  COMMIT: ${{ github.sha }}
  WORKFLOW: ${{ github.workflow }}
  ACTOR: ${{ github.actor }}

jobs:
  info:
    runs-on: ubuntu-latest
    steps:
      - run: |
          echo "Repository: $REPO"
          echo "Branch: $BRANCH"
          echo "Commit: $COMMIT"
          echo "Workflow: $WORKFLOW"
          echo "Triggered by: $ACTOR"
```

#### Using runner Context

```yaml
jobs:
  debug-runner:
    runs-on: ${{ matrix.os }}
    strategy:
      matrix:
        os: [ubuntu-latest, windows-latest]
    env:
      RUNNER_OS: ${{ runner.os }}
      RUNNER_ARCH: ${{ runner.arch }}
      WORKSPACE: ${{ runner.workspace }}
    steps:
      - run: |
          echo "OS: $RUNNER_OS"
          echo "Architecture: $RUNNER_ARCH"
          echo "Workspace: $WORKSPACE"
```

#### Using Matrix Context

```yaml
jobs:
  build:
    strategy:
      matrix:
        node: [16, 18, 20]
        os: [ubuntu-latest, windows-latest]
    env:
      NODE_VERSION: ${{ matrix.node }}
      BUILD_OS: ${{ matrix.os }}
      BUILD_ID: ${{ matrix.node }}-${{ matrix.os }}
    runs-on: ${{ matrix.os }}
    steps:
      - run: |
          echo "Building for Node $NODE_VERSION on $BUILD_OS"
          echo "Build ID: $BUILD_ID"
```

### 6. **Creating Dynamic Environment Variables from Step Outputs**

Environment variables for subsequent steps can be created using step outputs and the special `GITHUB_OUTPUT` file.

#### Using GITHUB_OUTPUT

```yaml
jobs:
  dynamic-vars:
    runs-on: ubuntu-latest
    steps:
      - name: Set dynamic variables
        id: vars
        run: |
          VERSION=$(cat version.txt)
          TIMESTAMP=$(date +%Y%m%d_%H%M%S)
          echo "version=$VERSION" >> $GITHUB_OUTPUT
          echo "timestamp=$TIMESTAMP" >> $GITHUB_OUTPUT
          echo "build-id=$VERSION-$TIMESTAMP" >> $GITHUB_OUTPUT

      - name: Use dynamic variables
        env:
          APP_VERSION: ${{ steps.vars.outputs.version }}
          BUILD_TIMESTAMP: ${{ steps.vars.outputs.timestamp }}
          BUILD_ID: ${{ steps.vars.outputs.build-id }}
        run: |
          echo "Version: $APP_VERSION"
          echo "Timestamp: $BUILD_TIMESTAMP"
          echo "Build ID: $BUILD_ID"
```

#### Multiline Environment Variables

```yaml
- name: Create multiline variable
  id: config
  run: |
    echo "multiline-value<<EOF" >> $GITHUB_OUTPUT
    echo "Line 1"
    echo "Line 2"
    echo "Line 3"
    echo "EOF" >> $GITHUB_OUTPUT

- name: Use multiline variable
  env:
    CONFIG: ${{ steps.config.outputs.multiline-value }}
  run: echo "$CONFIG"
```

### 7. **Using environment Variables from Previous Jobs**

Access environment variables from dependencies using the `needs` context and job outputs:

```yaml
jobs:
  setup:
    runs-on: ubuntu-latest
    outputs:
      version: ${{ steps.version.outputs.result }}
      build-date: ${{ steps.date.outputs.result }}
    env:
      DEFAULT_VERSION: 1.0.0
    steps:
      - id: version
        run: echo "result=$DEFAULT_VERSION" >> $GITHUB_OUTPUT

      - id: date
        run: echo "result=$(date +%Y-%m-%d)" >> $GITHUB_OUTPUT

  build:
    needs: setup
    runs-on: ubuntu-latest
    env:
      APP_VERSION: ${{ needs.setup.outputs.version }}
      BUILD_DATE: ${{ needs.setup.outputs.build-date }}
    steps:
      - run: |
          echo "Building version: $APP_VERSION"
          echo "Build date: $BUILD_DATE"
```

### 8. **Environment Variables with Default Values**

Use bash parameter expansion to provide default values:

```yaml
jobs:
  defaults:
    runs-on: ubuntu-latest
    env:
      NODE_ENV: ${{ vars.NODE_ENV || 'development' }}
      LOG_LEVEL: DEBUG
      PORT: ${{ env.PORT || 3000 }}
    steps:
      - run: |
          echo "Environment: $NODE_ENV"
          echo "Log Level: $LOG_LEVEL"
          echo "Port: $PORT"
```

#### Using Variable Expansion

```yaml
steps:
  - name: Expand variables
    env:
      OPTIONAL_CONFIG: ${{ vars.OPTIONAL_CONFIG }}
    run: |
      # Use default if empty
      CONFIG=${OPTIONAL_CONFIG:=default-value}
      echo "Configuration: $CONFIG"
```

### 9. **Special Environment Variables**

GitHub automatically provides several special environment variables:

```yaml
jobs:
  special-vars:
    runs-on: ubuntu-latest
    steps:
      - name: Show special variables
        run: |
          echo "CI: $CI"
          echo "GITHUB_WORKSPACE: $GITHUB_WORKSPACE"
          echo "GITHUB_ACTION: $GITHUB_ACTION"
          echo "GITHUB_RUN_ID: $GITHUB_RUN_ID"
          echo "GITHUB_RUN_NUMBER: $GITHUB_RUN_NUMBER"
          echo "GITHUB_REF: $GITHUB_REF"
          echo "GITHUB_SHA: $GITHUB_SHA"
          echo "GITHUB_ACTOR: $GITHUB_ACTOR"
          echo "GITHUB_REPOSITORY: $GITHUB_REPOSITORY"
          echo "RUNNER_OS: $RUNNER_OS"
          echo "RUNNER_TEMP: $RUNNER_TEMP"
```

#### Special GitHub Environment Variables

| Variable                  | Description                                                           |
| ------------------------- | --------------------------------------------------------------------- |
| `CI`                      | Always set to `true`                                                  |
| `GITHUB_WORKSPACE`        | The path to the workspace directory                                   |
| `GITHUB_ACTION`           | The name of the action currently running                              |
| `GITHUB_ACTIONS`          | Always set to `true` when actions are running                         |
| `GITHUB_ACTOR`            | The username that triggered the workflow                              |
| `GITHUB_API_URL`          | The URL of the GitHub API                                             |
| `GITHUB_BASE_REF`         | The base branch name (pull requests only)                             |
| `GITHUB_ENV`              | Path to file for setting environment variables persisted across steps |
| `GITHUB_EVENT_NAME`       | The name of the webhook event                                         |
| `GITHUB_EVENT_PATH`       | Path to file containing webhook payload                               |
| `GITHUB_GRAPHQL_URL`      | The URL of the GitHub GraphQL API                                     |
| `GITHUB_HEAD_REF`         | The head branch name (pull requests only)                             |
| `GITHUB_JOB`              | The current job ID                                                    |
| `GITHUB_OUTPUT`           | Path to file for setting step outputs                                 |
| `GITHUB_REF`              | The fully-formed ref of the branch/tag                                |
| `GITHUB_REF_NAME`         | The name of the branch/tag without refs/ prefix                       |
| `GITHUB_REF_PROTECTED`    | Whether the ref is protected                                          |
| `GITHUB_REF_TYPE`         | The type of ref (branch or tag)                                       |
| `GITHUB_REPOSITORY`       | The repository in owner/repo format                                   |
| `GITHUB_REPOSITORY_OWNER` | The repository owner's username                                       |
| `GITHUB_RETENTION_DAYS`   | Artifact retention days                                               |
| `GITHUB_RUN_ATTEMPT`      | The attempt number of the workflow run                                |
| `GITHUB_RUN_ID`           | The unique ID of the workflow run                                     |
| `GITHUB_RUN_NUMBER`       | The run number of the workflow                                        |
| `GITHUB_SERVER_URL`       | The URL of the GitHub server                                          |
| `GITHUB_SHA`              | The commit SHA                                                        |
| `GITHUB_STEP_SUMMARY`     | Path to file for job summary                                          |
| `GITHUB_TOKEN`            | Token for authentication                                              |
| `GITHUB_TRIGGERING_ACTOR` | The username that triggered the workflow                              |
| `GITHUB_WORKFLOW`         | The name of the workflow                                              |
| `RUNNER_ARCH`             | The architecture of the runner                                        |
| `RUNNER_DEBUG`            | Enable debug logging when set to true                                 |
| `RUNNER_NAME`             | The name of the runner                                                |
| `RUNNER_OS`               | The operating system (Linux, Windows, or macOS)                       |
| `RUNNER_TEMP`             | Path to temporary directory                                           |
| `RUNNER_TOOL_CACHE`       | Path to tool cache directory                                          |
| `RUNNER_WORKSPACE`        | Path to workspace directory                                           |

### 10. **Best Practices for Environment Variables**

#### ✓ Do's

```yaml
# ✓ Use meaningful names
env:
  DATABASE_CONNECTION_TIMEOUT: 5000
  MAX_RETRY_ATTEMPTS: 3

# ✓ Use uppercase with underscores
env:
  NODE_ENV: production

# ✓ Store sensitive data in secrets
env:
  API_KEY: ${{ secrets.API_KEY }}

# ✓ Use contexts for dynamic values
env:
  BUILD_TAG: ${{ github.sha }}-${{ github.run_number }}

# ✓ Document complex variables with comments
env:
  # Format: registry/organization/image:tag
  DOCKER_IMAGE: ${{ env.REGISTRY }}/${{ github.repository }}:latest

# ✓ Use step outputs for values that depend on previous steps
- id: version
  run: echo "version=$(npm run get-version)" >> $GITHUB_OUTPUT

- env:
    APP_VERSION: ${{ steps.version.outputs.version }}
  run: echo "Version: $APP_VERSION"
```

#### ✗ Don'ts

```yaml
# ✗ Don't use lowercase or spaces
env:
  nodeEnv: production  # Wrong
  node env: production  # Wrong

# ✗ Don't store secrets in plain environment variables
env:
  PASSWORD: mysecret  # Wrong

# ✗ Don't echo secrets
- run: echo ${{ secrets.TOKEN }}  # Will be redacted but still unsafe

# ✗ Don't use overly long variable names
env:
  VERY_LONG_ENVIRONMENT_VARIABLE_NAME_THAT_NOBODY_CAN_REMEMBER: value

# ✗ Don't rely on shell-specific syntax at workflow level
env:
  # This won't work as expected
  EXPANDED: $HOME/mydir
```

### 11. **Complete Example: Multi-Stage Workflow with Environment Variables**

```yaml
name: Complete Environment Variables Example

on:
  push:
    branches: [main]

env:
  REGISTRY: ghcr.io
  IMAGE_NAME: myapp

jobs:
  prepare:
    runs-on: ubuntu-latest
    outputs:
      version: ${{ steps.version.outputs.value }}
      build-date: ${{ steps.date.outputs.value }}
    env:
      VERSION_FILE: ./version.txt
    steps:
      - uses: actions/checkout@v3

      - id: version
        run: echo "value=$(cat $VERSION_FILE)" >> $GITHUB_OUTPUT

      - id: date
        run: echo "value=$(date +%Y-%m-%d)" >> $GITHUB_OUTPUT

  build:
    needs: prepare
    runs-on: ubuntu-latest
    env:
      APP_VERSION: ${{ needs.prepare.outputs.version }}
      BUILD_DATE: ${{ needs.prepare.outputs.build-date }}
      IMAGE_TAG: ${{ env.REGISTRY }}/${{ github.repository }}:${{ needs.prepare.outputs.version }}
    steps:
      - uses: actions/checkout@v3

      - name: Build Docker image
        env:
          DOCKER_BUILDKIT: 1
        run: |
          echo "Building version: $APP_VERSION"
          echo "Build date: $BUILD_DATE"
          echo "Image tag: $IMAGE_TAG"

  deploy:
    needs: [prepare, build]
    runs-on: ubuntu-latest
    env:
      DEPLOY_ENVIRONMENT: production
      IMAGE_TAG: ${{ env.REGISTRY }}/${{ github.repository }}:${{ needs.prepare.outputs.version }}
    steps:
      - name: Deploy
        env:
          DEPLOY_TOKEN: ${{ secrets.DEPLOY_TOKEN }}
        run: |
          echo "Deploying version: ${{ needs.prepare.outputs.version }}"
          echo "To environment: $DEPLOY_ENVIRONMENT"
          echo "Image: $IMAGE_TAG"
```

---

## Default Environment Variables

GitHub Actions automatically provides a set of default environment variables in every workflow run. These variables contain information about the workflow execution, runner environment, and repository context. Understanding and using these default variables can simplify your workflows and reduce the need for manual configuration.

### Overview of Default Environment Variables

Default environment variables are automatically populated by GitHub and can be accessed without any additional setup. They provide critical information about:

- The workflow execution context (run ID, job name, step number)
- Repository and commit information (SHA, branch, repository name)
- Runner environment details (OS, architecture, temporary directories)
- Workflow trigger details (event name, actor)
- File paths and URLs

### 1. **Workflow and Execution Information**

These variables provide details about the current workflow run.

#### Common Workflow Variables

```yaml
jobs:
  info:
    runs-on: ubuntu-latest
    steps:
      - name: Print Workflow Information
        run: |
          echo "Workflow: $GITHUB_WORKFLOW"
          echo "Run ID: $GITHUB_RUN_ID"
          echo "Run Number: $GITHUB_RUN_NUMBER"
          echo "Run Attempt: $GITHUB_RUN_ATTEMPT"
          echo "Job: $GITHUB_JOB"
          echo "Event Name: $GITHUB_EVENT_NAME"
          echo "Actor: $GITHUB_ACTOR"
          echo "Triggering Actor: $GITHUB_TRIGGERING_ACTOR"
```

#### Output Example

```
Workflow: CI Pipeline
Run ID: 1234567890
Run Number: 42
Run Attempt: 2
Job: info
Event Name: push
Actor: octocat
Triggering Actor: octocat
```

#### Use Case: Creating Unique Build Identifiers

```yaml
- name: Create Build ID
  run: |
    BUILD_ID="${{ github.workflow }}-${{ github.run_id }}-${{ github.run_attempt }}"
    echo "Build Identifier: $BUILD_ID"
```

### 2. **Repository and Reference Information**

Variables containing details about the repository and Git references.

#### Repository Variables

```yaml
jobs:
  repo-info:
    runs-on: ubuntu-latest
    steps:
      - name: Repository Information
        run: |
          echo "Repository: $GITHUB_REPOSITORY"
          echo "Repository Owner: $GITHUB_REPOSITORY_OWNER"
          echo "Ref: $GITHUB_REF"
          echo "Ref Name: $GITHUB_REF_NAME"
          echo "Ref Type: $GITHUB_REF_TYPE"
          echo "Ref Protected: $GITHUB_REF_PROTECTED"
          echo "Commit SHA: $GITHUB_SHA"
          echo "Server URL: $GITHUB_SERVER_URL"
```

#### Output Example

```
Repository: octocat/Hello-World
Repository Owner: octocat
Ref: refs/heads/main
Ref Name: main
Ref Type: branch
Ref Protected: true
Commit SHA: abc123def456...
Server URL: https://github.com
```

#### Use Case: Building Docker Images with Semantic Tags

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Build Docker Image
        env:
          REGISTRY: ghcr.io
          IMAGE_NAME: ${{ env.REGISTRY }}/${{ github.repository }}
        run: |
          # Build with both branch name and commit SHA
          docker build -t $IMAGE_NAME:${{ github.ref_name }} .
          docker build -t $IMAGE_NAME:${{ github.sha }} .
          # Also tag as latest if on main
          if [ "${{ github.ref_name }}" = "main" ]; then
            docker build -t $IMAGE_NAME:latest .
          fi
```

### 3. **Branch and Pull Request Information**

When triggered by pull requests, these variables provide additional context.

#### Pull Request Variables

```yaml
on:
  pull_request:
    branches: [main]

jobs:
  pr-check:
    runs-on: ubuntu-latest
    steps:
      - name: PR Information
        run: |
          # Only available on pull_request events
          if [ "${{ github.event_name }}" = "pull_request" ]; then
            echo "Base Ref: ${{ github.base_ref }}"
            echo "Head Ref: ${{ github.head_ref }}"
            echo "Ref: ${{ github.ref }}"
            echo "Event Action: ${{ github.event.action }}"
          fi
```

#### Use Case: Version Control for Feature Branches

```yaml
- name: Generate Branch Version
  run: |
    if [ "${{ github.base_ref }}" = "main" ]; then
      VERSION_SUFFIX="pr-${{ github.event.number }}"
    else
      VERSION_SUFFIX=${{ github.head_ref }}
    fi
    echo "Deploying to: $VERSION_SUFFIX"
```

### 4. **File Path and Workspace Variables**

Variables that provide important file system paths.

#### Path Variables

```yaml
jobs:
  paths:
    runs-on: ubuntu-latest
    steps:
      - name: Show File Paths
        run: |
          echo "Workspace: $GITHUB_WORKSPACE"
          echo "Temp: $RUNNER_TEMP"
          echo "Tool Cache: $RUNNER_TOOL_CACHE"
          echo "Event Path: $GITHUB_EVENT_PATH"
          echo "Env File: $GITHUB_ENV"
          echo "Output File: $GITHUB_OUTPUT"
          echo "Step Summary: $GITHUB_STEP_SUMMARY"
```

#### Output Example (Ubuntu)

```
Workspace: /home/runner/work/repo/repo
Temp: /home/runner/_temp
Tool Cache: /opt/hostedtoolcache
Event Path: /home/runner/work/_temp/_github_workflow/event.json
Env File: /home/runner/work/_temp/_runner_file_commands/set_env_xxxxx
Output File: /home/runner/work/_temp/_runner_file_commands/set_output_xxxxx
Step Summary: /home/runner/work/_temp/_runner_file_commands/step_summary_xxxxx
```

#### Use Case: Temporary File Storage and Cleanup

```yaml
- name: Build Artifacts
  run: |
    # Use RUNNER_TEMP for temporary files
    mkdir -p $RUNNER_TEMP/build
    npm run build --output=$RUNNER_TEMP/build

- name: Upload from Temp
  uses: actions/upload-artifact@v3
  with:
    name: build-artifacts
    path: ${{ runner.temp }}/build
```

### 5. **Runner Information Variables**

Variables describing the runner executing the job.

#### Runner Variables

```yaml
jobs:
  runner-info:
    runs-on: ${{ matrix.os }}
    strategy:
      matrix:
        os: [ubuntu-latest, windows-latest, macos-latest]
    steps:
      - name: Runner Environment
        run: |
          echo "Runner OS: $RUNNER_OS"
          echo "Runner Architecture: $RUNNER_ARCH"
          echo "Runner Name: $RUNNER_NAME"
```

#### Output Examples

**On Ubuntu:**

```
Runner OS: Linux
Runner Architecture: X64
Runner Name: GitHub Actions 1
```

**On Windows:**

```
Runner OS: Windows
Runner Architecture: X64
Runner Name: GitHub Actions 2
```

**On macOS:**

```
Runner OS: macOS
Runner Architecture: X64
Runner Name: GitHub Actions 3
```

#### Use Case: OS-Specific Configuration

```yaml
jobs:
  build:
    runs-on: ${{ matrix.os }}
    strategy:
      matrix:
        os: [ubuntu-latest, windows-latest]
    steps:
      - name: Build
        run: |
          if [ "$RUNNER_OS" = "Windows" ]; then
            npm run build:windows
          else
            npm run build:unix
          fi
        shell: bash
```

### 6. **GitHub API and Token Variables**

Variables for API access and authentication.

#### API and Token Variables

```yaml
jobs:
  api-access:
    runs-on: ubuntu-latest
    steps:
      - name: Use GitHub API
        run: |
          # GITHUB_TOKEN is automatically provided
          curl -H "Authorization: Bearer ${{ env.GITHUB_TOKEN }}" \
               https://api.github.com/repos/${{ github.repository }}
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

#### URLs Example

```yaml
- name: API URLs
  run: |
    echo "API URL: $GITHUB_API_URL"
    echo "GraphQL URL: $GITHUB_GRAPHQL_URL"
    echo "Server URL: $GITHUB_SERVER_URL"
```

#### Output Example

```
API URL: https://api.github.com
GraphQL URL: https://api.github.com/graphql
Server URL: https://github.com
```

### 7. **Event Payload Information**

Access to the webhook event that triggered the workflow.

#### Event Payload Access

```yaml
jobs:
  event-info:
    runs-on: ubuntu-latest
    steps:
      - name: Read Event Payload
        run: |
          cat $GITHUB_EVENT_PATH | jq '.'
```

#### Use Case: Extract Commit Message

```yaml
- name: Get Commit Message
  run: |
    if [ "${{ github.event_name }}" = "push" ]; then
      COMMIT_MESSAGE=$(cat $GITHUB_EVENT_PATH | jq -r '.head_commit.message')
      echo "Commit: $COMMIT_MESSAGE"
    fi
```

### 8. **CI Environment Flag**

The CI environment variable indicates the workflow is running in CI.

#### CI Variable Usage

```yaml
jobs:
  example:
    runs-on: ubuntu-latest
    steps:
      - name: Check CI Environment
        run: |
          if [ "$CI" = "true" ]; then
            echo "Running in CI environment"
            npm run build -- --ci
          fi
```

#### Use Case: Conditional Build Configuration

```yaml
- name: Build
  run: npm run build
  env:
    CI: true # Enables CI-specific settings in build tools
    ENVIRONMENT: production
```

### 9. **Debug Mode**

Enable detailed logging with the RUNNER_DEBUG variable.

#### Enabling Debug Logging

```yaml
- name: Enable Debug
  env:
    RUNNER_DEBUG: true
  run: |
    # This will produce verbose output
    npm run build
```

#### Use Case: Troubleshooting

```yaml
jobs:
  debug:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
        with:
          token: ${{ secrets.GITHUB_TOKEN }}

      - name: Debug Step
        env:
          RUNNER_DEBUG: true
        run: |
          echo "Debug mode enabled"
          # Actions will output more verbose logs
```

### 10. **Complete Reference Table**

| Variable                  | Example Value                    | Description                                |
| ------------------------- | -------------------------------- | ------------------------------------------ |
| `CI`                      | `true`                           | Always true when running in GitHub Actions |
| `GITHUB_WORKSPACE`        | `/home/runner/work/repo/repo`    | Root directory of repository               |
| `GITHUB_ACTION`           | `Build`                          | Name of currently running action           |
| `GITHUB_ACTIONS`          | `true`                           | Always true when running in Actions        |
| `GITHUB_ACTOR`            | `octocat`                        | Username of user that triggered workflow   |
| `GITHUB_API_URL`          | `https://api.github.com`         | URL of GitHub REST API                     |
| `GITHUB_BASE_REF`         | `main`                           | Base branch name (PR only)                 |
| `GITHUB_ENV`              | `/home/runner/work/_temp/...`    | Path to set persistent env vars            |
| `GITHUB_EVENT_NAME`       | `push`                           | Name of webhook event                      |
| `GITHUB_EVENT_PATH`       | `/home/runner/work/_temp/...`    | Path to webhook payload JSON               |
| `GITHUB_GRAPHQL_URL`      | `https://api.github.com/graphql` | URL of GitHub GraphQL API                  |
| `GITHUB_HEAD_REF`         | `feature-branch`                 | Head branch name (PR only)                 |
| `GITHUB_JOB`              | `build`                          | Current job ID                             |
| `GITHUB_OUTPUT`           | `/home/runner/work/_temp/...`    | Path to set step outputs                   |
| `GITHUB_REF`              | `refs/heads/main`                | Fully-formed ref of branch/tag             |
| `GITHUB_REF_NAME`         | `main`                           | Branch or tag name without prefix          |
| `GITHUB_REF_PROTECTED`    | `true`                           | Whether ref is protected                   |
| `GITHUB_REF_TYPE`         | `branch`                         | Type of ref (branch or tag)                |
| `GITHUB_REPOSITORY`       | `octocat/Hello-World`            | Repository in owner/repo format            |
| `GITHUB_REPOSITORY_OWNER` | `octocat`                        | Repository owner username                  |
| `GITHUB_RETENTION_DAYS`   | `90`                             | Artifact retention days                    |
| `GITHUB_RUN_ATTEMPT`      | `2`                              | Current attempt number                     |
| `GITHUB_RUN_ID`           | `1234567890`                     | Unique workflow run ID                     |
| `GITHUB_RUN_NUMBER`       | `42`                             | Sequential workflow run number             |
| `GITHUB_SERVER_URL`       | `https://github.com`             | GitHub server URL                          |
| `GITHUB_SHA`              | `abc123def...`                   | Commit SHA that triggered                  |
| `GITHUB_STEP_SUMMARY`     | `/home/runner/work/_temp/...`    | Path to job summary                        |
| `GITHUB_TOKEN`            | (token)                          | Token for GitHub API auth                  |
| `GITHUB_TRIGGERING_ACTOR` | `octocat`                        | User that triggered workflow               |
| `GITHUB_WORKFLOW`         | `CI`                             | Workflow name                              |
| `RUNNER_ARCH`             | `X64`                            | Runner architecture                        |
| `RUNNER_DEBUG`            | `false`                          | Enable debug logging                       |
| `RUNNER_NAME`             | `GitHub Actions 1`               | Runner display name                        |
| `RUNNER_OS`               | `Linux`                          | Operating system                           |
| `RUNNER_TEMP`             | `/home/runner/_temp`             | Temporary directory path                   |
| `RUNNER_TOOL_CACHE`       | `/opt/hostedtoolcache`           | Tool cache directory                       |
| `RUNNER_WORKSPACE`        | `/home/runner/work`              | Workspace directory                        |

### 11. **Complete Practical Example**

Here's a comprehensive example using multiple default environment variables:

```yaml
name: Build and Deploy with Environment Vars

on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main]

jobs:
  build-and-test:
    runs-on: ${{ matrix.os }}
    strategy:
      matrix:
        os: [ubuntu-latest, windows-latest]
    steps:
      - uses: actions/checkout@v3

      - name: Display Environment Info
        run: |
          echo "=== Workflow Information ==="
          echo "Workflow: $GITHUB_WORKFLOW"
          echo "Run: $GITHUB_RUN_NUMBER (Attempt: $GITHUB_RUN_ATTEMPT)"
          echo ""
          echo "=== Repository Information ==="
          echo "Repository: $GITHUB_REPOSITORY"
          echo "Branch: $GITHUB_REF_NAME"
          echo "Commit: $GITHUB_SHA"
          echo ""
          echo "=== Event Information ==="
          echo "Event: $GITHUB_EVENT_NAME"
          echo "Actor: $GITHUB_ACTOR"
          echo ""
          echo "=== Runner Information ==="
          echo "OS: $RUNNER_OS"
          echo "Architecture: $RUNNER_ARCH"

      - name: Setup Build Environment
        run: |
          mkdir -p $RUNNER_TEMP/artifacts
          echo "Build directory: $RUNNER_TEMP/artifacts"

      - name: Build
        run: |
          npm run build
          cp -r dist $RUNNER_TEMP/artifacts/

      - name: Test
        env:
          CI: true
        run: npm test

      - name: Create Build Summary
        run: |
          cat > $GITHUB_STEP_SUMMARY << EOF
          ## Build Summary
          - **Workflow:** $GITHUB_WORKFLOW
          - **Branch:** $GITHUB_REF_NAME
          - **Commit:** $GITHUB_SHA
          - **OS:** $RUNNER_OS
          - **Status:** ✅ Success
          EOF

      - name: Upload Artifacts
        if: success()
        uses: actions/upload-artifact@v3
        with:
          name: build-${{ runner.os }}-${{ github.run_number }}
          path: ${{ runner.temp }}/artifacts

  deploy:
    if: github.event_name == 'push' && github.ref_name == 'main'
    needs: build-and-test
    runs-on: ubuntu-latest
    steps:
      - name: Deploy Information
        run: |
          echo "Deploying from: $GITHUB_REPOSITORY"
          echo "Triggered by: $GITHUB_ACTOR"
          echo "Commit: $GITHUB_SHA"
          echo "Build ID: $GITHUB_RUN_ID"

      - name: Call Deployment API
        run: |
          curl -X POST https://api.example.com/deploy \
            -H "Authorization: Bearer ${{ secrets.DEPLOY_TOKEN }}" \
            -d "{
              \"repository\": \"$GITHUB_REPOSITORY\",
              \"commit\": \"$GITHUB_SHA\",
              \"branch\": \"$GITHUB_REF_NAME\",
              \"run_id\": \"$GITHUB_RUN_ID\"
            }"
```

---

## Environment Protection Rules

Environment protection rules are powerful GitHub security features that allow you to control how and when workflows can access and deploy to specific environments. They help ensure that sensitive deployments require proper authorization, reviews, and validation before proceeding.

### Overview of Environment Protection Rules

Environment protection rules provide several key benefits:

- **Controlled Access**: Restrict who can deploy to production or other protected environments
- **Review Requirements**: Mandate that deployments require approval before execution
- **Branch Protection**: Specify which branches are allowed to deploy to an environment
- **Deployment Timing**: Control when deployments can occur
- **Audit Trail**: Track all deployment approvals and denials

### 1. **Required Reviewers**

Require one or more team members to approve deployments before they proceed.

#### Configuration in Repository Settings

Navigate to: `Settings > Environments > [Environment Name] > Deployment branches and reviewers`

Enable "Required reviewers" and select the GitHub users or teams who must approve deployments.

#### Workflow Implementation

```yaml
jobs:
  deploy:
    runs-on: ubuntu-latest
    environment: production # References the protected environment
    steps:
      - uses: actions/checkout@v3
      - name: Deploy to Production
        run: |
          echo "Deployment approved and proceeding..."
          bash deploy.sh
```

#### How It Works

1. Workflow reaches a step with `environment: production`
2. GitHub pauses the workflow and waits for approval
3. Designated reviewers receive a notification to review the deployment
4. Once approved (or rejected), the workflow continues or fails
5. Approval is recorded in the deployment history

#### Example: Multi-Reviewer Approval Process

```yaml
name: Production Deployment

on:
  push:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - run: npm test

  deploy-staging:
    needs: build
    runs-on: ubuntu-latest
    environment:
      name: staging
      url: https://staging.example.com
    steps:
      - uses: actions/checkout@v3
      - run: bash deploy-staging.sh

  deploy-production:
    needs: deploy-staging
    runs-on: ubuntu-latest
    environment:
      name: production
      url: https://example.com
    steps:
      - uses: actions/checkout@v3
      - name: Require manual approval (via environment settings)
        run: |
          echo "Waiting for production approval..."
          echo "Current deployments require review from senior engineers"
      - run: bash deploy-production.sh
      - name: Notify deployment
        run: |
          echo "Deployment to production completed"
          echo "Commit: ${{ github.sha }}"
          echo "Deployed by: ${{ github.actor }}"
```

### 2. **Deployment Branches**

Restrict which branches can deploy to a specific environment.

#### Configuration Options

**Protected Branches Only**

```
Only allow deployments from protected branches
```

**Specific Branches**

```
main
release/*
prodaction/*
```

**All Branches** (least restrictive)

#### Example Workflow

```yaml
name: Conditional Deployment

on:
  push:
    branches: [main, develop, "release/*"]

jobs:
  deploy:
    runs-on: ubuntu-latest
    environment:
      name: production
      url: https://example.com
    # If environment is configured to allow only 'main' branch,
    # this job will fail on other branches
    steps:
      - uses: actions/checkout@v3
      - name: Verify Branch
        run: |
          if [ "${{ github.ref_name }}" = "main" ]; then
            echo "✓ Production deployment approved (main branch)"
          else
            echo "✗ Production deployment only allowed from main"
            exit 1
          fi
      - run: bash deploy.sh
```

#### Use Case: Different Strategies for Different Branches

```yaml
jobs:
  deploy-staging:
    if: github.ref_name == 'develop'
    environment:
      name: staging
    runs-on: ubuntu-latest
    steps:
      - run: bash deploy-staging.sh

  deploy-production:
    if: github.ref_name == 'main'
    environment:
      name: production # Only allows main branch
    runs-on: ubuntu-latest
    steps:
      - run: bash deploy-production.sh
```

### 3. **Wait Timer**

Add a delay before deployment is allowed to proceed, providing time for validation or issue discovery.

#### Configuration

Set wait timer (in minutes): `0` to `43200` (30 days)

#### Example: 24-Hour Wait Timer for Production

```yaml
jobs:
  deploy:
    runs-on: ubuntu-latest
    environment:
      name: production
      # When environment has 24-hour wait timer:
      # - Workflow triggers and waits 24 hours before proceeding
    steps:
      - uses: actions/checkout@v3
      - name: Pre-deployment checklist
        run: |
          echo "Deployment initiated at: $(date)"
          echo "Will proceed in 24 hours..."
          echo "Waiting for:"
          echo "- QA verification"
          echo "- Security review"
          echo "- Stakeholder confirmation"
      - run: bash deploy.sh
```

#### Use Case: Staggered Deployment Strategy

```yaml
jobs:
  deploy-canary:
    runs-on: ubuntu-latest
    environment:
      name: canary
      # No wait timer - immediate deployment to 5% of users
    steps:
      - run: bash deploy-canary.sh

  deploy-production:
    needs: deploy-canary
    runs-on: ubuntu-latest
    environment:
      name: production
      # 1-hour wait timer for full production rollout
      # Allows time to monitor canary deployment
    steps:
      - run: bash deploy-production.sh
```

### 4. **Custom Deployment Protection Rules**

Use GitHub Scripts (available on GitHub Enterprise) to create custom logic for deployment approval.

#### Example: Automated Approval Based on Test Results

```yaml
name: Smart Deployment

on:
  push:
    branches: [main]

jobs:
  test:
    runs-on: ubuntu-latest
    outputs:
      test-status: ${{ steps.test.outputs.status }}
    steps:
      - uses: actions/checkout@v3
      - id: test
        run: |
          npm test
          echo "status=passed" >> $GITHUB_OUTPUT

  deploy:
    needs: test
    runs-on: ubuntu-latest
    environment:
      name: staging
    steps:
      - uses: actions/checkout@v3
      - name: Check Test Status
        if: needs.test.outputs.test-status == 'passed'
        run: |
          echo "✓ Tests passed - proceeding with deployment"
          bash deploy.sh
      - name: Deployment Blocked
        if: needs.test.outputs.test-status != 'passed'
        run: |
          echo "✗ Tests failed - deployment blocked"
          exit 1
```

### 5. **Complete Example: Multi-Environment Protection Strategy**

```yaml
name: Multi-Environment Deployment with Protection Rules

on:
  push:
    branches: [develop, main, "release/*"]

jobs:
  build-and-test:
    runs-on: ubuntu-latest
    outputs:
      version: ${{ steps.version.outputs.value }}
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-node@v3
        with:
          node-version: 18
      - run: npm ci
      - run: npm run lint
      - run: npm test
      - id: version
        run: echo "value=$(npm run get-version)" >> $GITHUB_OUTPUT

  deploy-dev:
    needs: build-and-test
    runs-on: ubuntu-latest
    if: github.ref_name == 'develop'
    environment:
      name: development
      url: https://dev.example.com
      # No protection rules - immediate deployment
    steps:
      - uses: actions/checkout@v3
      - run: |
          echo "Deploying version ${{ needs.build-and-test.outputs.version }} to dev"
          bash deploy-dev.sh

  deploy-staging:
    needs: build-and-test
    runs-on: ubuntu-latest
    if: github.ref_name == 'main' || startsWith(github.ref, 'refs/heads/release/')
    environment:
      name: staging
      url: https://staging.example.com
      # Protection rule: requires 1 reviewer approval
      # For QA verification before full deployment
    steps:
      - uses: actions/checkout@v3
      - run: |
          echo "Deploying version ${{ needs.build-and-test.outputs.version }} to staging"
          bash deploy-staging.sh

  deploy-production:
    needs: [build-and-test, deploy-staging]
    runs-on: ubuntu-latest
    if: github.ref_name == 'main'
    environment:
      name: production
      url: https://example.com
      # Protection rules:
      # - Requires 2 reviewer approvals from senior engineers
      # - Only main branch allowed
      # - 30-minute wait timer for final checks
    steps:
      - uses: actions/checkout@v3
      - name: Pre-production Checklist
        run: |
          echo "=== Pre-Production Deployment Checklist ==="
          echo "✓ Build completed: ${{ needs.build-and-test.outputs.version }}"
          echo "✓ Staging deployment successful"
          echo "✓ Approved by authorized reviewers"
          echo "✓ 30-minute wait timer passed"
      - run: |
          echo "Deploying version ${{ needs.build-and-test.outputs.version }} to production"
          bash deploy-production.sh
      - name: Post-Deployment Notification
        if: success()
        run: |
          echo "✓ Production deployment successful"
          echo "Version: ${{ needs.build-and-test.outputs.version }}"
          echo "Deployed by: ${{ github.actor }}"
          echo "Time: $(date)"

  rollback:
    runs-on: ubuntu-latest
    if: failure() && github.ref_name == 'main'
    environment: production
    steps:
      - name: Notify Deployment Failure
        run: |
          echo "⚠ Production deployment failed"
          echo "Rollback may be required"
          echo "Alert sent to on-call engineers"
```

### 6. **Best Practices for Environment Protection Rules**

#### ✓ Recommended Practices

```yaml
# ✓ Use different protection levels per environment
environments:
  development: {}  # No restrictions
  staging:
    reviewers: [qa-team]  # QA approval required
  production:
    reviewers: [senior-engineers, devops-team]  # Multiple approvals
    branch-restrictions: [main]  # Only main branch
    wait-timer: 30  # 30 minutes for final validation

# ✓ Clear environment names and URLs
environment:
  name: production
  url: https://example.com  # Helps reviewers understand what's being deployed

# ✓ Include context in workflow
- name: Deployment Information
  run: |
    echo "Environment: ${{ github.environment }}"
    echo "Branch: ${{ github.ref_name }}"
    echo "Commit: ${{ github.sha }}"
    echo "Triggered by: ${{ github.actor }}"
```

#### ✗ Anti-Patterns to Avoid

```yaml
# ✗ No protection rules for production
environment: production  # Dangerous - anyone can deploy

# ✗ Allowing all branches to all environments
environment:
  name: production
  # No branch restrictions - feature branches can deploy to production

# ✗ Disabling reviews for production
environment:
  name: production
  # No reviewers required - critical issue

# ✗ Bypassing protection with conditional logic
jobs:
  deploy:
    if: contains(github.actor, 'bot')
    environment: production
    # Bad practice - circumvents protection rules
```

### 7. **Testing Protection Rules**

Verify your environment protection rules are working correctly:

```yaml
name: Test Environment Protection

on:
  push:
    branches: [test-protection]

jobs:
  test-staging:
    runs-on: ubuntu-latest
    environment:
      name: staging
    steps:
      - run: echo "This should pause if staging has reviewers"

  test-production:
    runs-on: ubuntu-latest
    environment:
      name: production
    steps:
      - run: echo "This should pause if production has reviewers"

  test-branch-restriction:
    runs-on: ubuntu-latest
    if: github.ref_name == 'main'
    environment:
      name: production
    steps:
      - run: echo "This only runs on main branch"
```

### 8. **Monitoring and Auditing Deployments**

View deployment history and approvals:

```yaml
jobs:
  audit-deployment:
    runs-on: ubuntu-latest
    steps:
      - name: Check Deployment Status
        run: |
          echo "Deployment ID: ${{ github.run_id }}"
          echo "Environment: ${{ github.environment }}"
          echo "Requested by: ${{ github.actor }}"
          echo "Timestamp: $(date)"
```

View in GitHub UI:

- Navigate to `Settings > Environments > [Environment Name]`
- View "Deployments" tab for history
- See approvals and denials in the deployment timeline

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
