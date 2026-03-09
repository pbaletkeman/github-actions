# GitHub Workflows: Complete Guide

## Table of Contents

1. [Introduction](#introduction)
2. [Contextual Information in GitHub Workflows](#contextual-information-in-github-workflows)
3. [GitHub Workflow File Structure](#github-workflow-file-structure)
4. [GitHub Workflow Trigger Events](#github-workflow-trigger-events)
5. [Creating and Using Custom Environment Variables](#creating-and-using-custom-environment-variables)
6. [Default Environment Variables](#default-environment-variables)
7. [Environment Protection Rules](#environment-protection-rules)
8. [GitHub Workflow Artifacts](#github-workflow-artifacts)
9. [GitHub Workflow Caching](#github-workflow-caching)
10. [Workflow Sharing](#workflow-sharing)
11. [Workflow Debugging](#workflow-debugging)
12. [GitHub Workflows REST API](#github-workflows-rest-api)
13. [Reviewing Deployments](#reviewing-deployments)
14. [Creating and Publishing Actions](#creating-and-publishing-actions)
15. [Managing Runners](#managing-runners)
16. [Common Failures and Troubleshooting](#common-failures-and-troubleshooting)

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
    paths: ["src/**", "package.json"]

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

| Type                     | Description                      |
| ------------------------ | -------------------------------- |
| `opened`                 | Pull request created             |
| `reopened`               | Previously closed PR reopened    |
| `synchronize`            | PR commits added (code changed)  |
| `converted_to_draft`     | PR converted to draft            |
| `ready_for_review`       | Draft PR marked ready for review |
| `labeled`                | Label added to PR                |
| `unlabeled`              | Label removed from PR            |
| `assigned`               | Assignee added                   |
| `unassigned`             | Assignee removed                 |
| `edited`                 | PR title/description edited      |
| `auto_merge_enabled`     | Auto-merge enabled               |
| `auto_merge_disabled`    | Auto-merge disabled              |
| `closed`                 | PR closed                        |
| `locked`                 | PR locked                        |
| `unlocked`               | PR unlocked                      |
| `review_requested`       | Review requested                 |
| `review_request_removed` | Review request removed           |

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
        description: "Deployment environment"
        required: true
        type: choice
        options:
          - development
          - staging
          - production
      version:
        description: "Version to deploy"
        required: false
        type: string
      dry_run:
        description: "Run as dry-run"
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

| Type          | Description        | Example               |
| ------------- | ------------------ | --------------------- |
| `string`      | Text input         | `version: "1.0.0"`    |
| `choice`      | Dropdown selection | Environment selection |
| `boolean`     | Checkbox           | `true` or `false`     |
| `environment` | Select environment | Production, staging   |

---

### 5. **schedule** - Scheduled Events (Cron)

Trigger workflows on a schedule using cron syntax.

```yaml
on:
  schedule:
    # Run every day at midnight UTC
    - cron: "0 0 * * *"

    # Run every 6 hours
    - cron: "0 */6 * * *"

    # Run at 8 AM Monday-Friday
    - cron: "0 8 * * 1-5"

    # Run first day of month at 2 AM
    - cron: "0 2 1 * *"

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

| Pattern        | Description                |
| -------------- | -------------------------- |
| `0 0 * * *`    | Daily at midnight UTC      |
| `0 */6 * * *`  | Every 6 hours              |
| `0 8 * * 1-5`  | 8 AM Monday-Friday         |
| `0 2 1 * *`    | First day of month at 2 AM |
| `*/30 * * * *` | Every 30 minutes           |
| `0 9 * * MON`  | Every Monday at 9 AM       |
| `0 0 * * 0`    | Every Sunday at midnight   |

---

### 6. **workflow_run** - Trigger on Another Workflow

Triggers based on another workflow's completion.

```yaml
on:
  workflow_run:
    workflows: ["Deploy"] # Workflow name or file path
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

| Type        | Description               |
| ----------- | ------------------------- |
| `completed` | Workflow run finished     |
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

| Type          | Description                                               |
| ------------- | --------------------------------------------------------- |
| `published`   | Release published (including pre-releases when published) |
| `unpublished` | Release unpublished                                       |
| `created`     | Release created (or a pre-release published)              |
| `edited`      | Release edited                                            |
| `deleted`     | Release deleted                                           |
| `prereleased` | Marked as pre-release                                     |
| `released`    | Released (after being pre-release)                        |

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

| Type           | Description       |
| -------------- | ----------------- |
| `opened`       | Issue created     |
| `closed`       | Issue closed      |
| `reopened`     | Issue reopened    |
| `assigned`     | Assignee added    |
| `unassigned`   | Assignee removed  |
| `labeled`      | Label added       |
| `unlabeled`    | Label removed     |
| `milestoned`   | Milestone added   |
| `demilestoned` | Milestone removed |
| `transferred`  | Issue transferred |
| `pinned`       | Issue pinned      |
| `unpinned`     | Issue unpinned    |
| `deleted`      | Issue deleted     |

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
    branches: [main] # Main is typically protected

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
    paths: ["src/**"]
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
    - cron: "0 0 * * *"

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

| Event                 | When Triggered     | Secret Access | Write Permissions |
| --------------------- | ------------------ | ------------- | ----------------- |
| `push`                | Code push          | ✓             | ✓                 |
| `pull_request`        | PR activity        | Limited       | Limited           |
| `workflow_dispatch`   | Manual             | ✓             | ✓                 |
| `schedule`            | Cron schedule      | ✓             | ✓                 |
| `release`             | Release published  | ✓             | ✓                 |
| `issues`              | Issue activity     | ✓             | ✓                 |
| `issue_comment`       | Comments           | ✓             | ✓                 |
| `workflow_run`        | Workflow completes | ✓             | ✓                 |
| `repository_dispatch` | API call           | ✓             | ✓                 |
| `fork`                | Repository forked  | Limited       | Limited           |

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

## GitHub Workflow Artifacts

Artifacts are files or collections of files created during a workflow run that you can use to share data between jobs in a workflow or store the outputs from individual jobs. Artifacts are essential for preserving build outputs, test results, logs, and other important data generated during your CI/CD pipeline.

### Overview of GitHub Artifacts

Artifacts provide several critical capabilities:

- **Data Persistence**: Store workflow outputs beyond the job execution
- **Inter-Job Communication**: Share files between different jobs in a workflow
- **Build Artifacts**: Preserve compiled binaries, containers, and deployment packages
- **Test Results**: Store test reports, coverage data, and screenshots
- **Logs and Debugging**: Keep detailed logs for troubleshooting failed workflows
- **Performance Metrics**: Archive performance benchmarks and metrics
- **Retention**: Control how long artifacts are stored (5 to 90 days)

### Why Use Artifacts?

**Common Use Cases:**

1. **Build Outputs**: Save compiled code, binaries, and distributions
2. **Test Evidence**: Archive test reports, screenshots, and video recordings
3. **Cross-Job Dependencies**: Build in one job, test in another, deploy in a third
4. **Deployment Packages**: Store packaged applications for deployment jobs
5. **Performance Data**: Archive benchmark results and performance metrics
6. **Debugging**: Store logs and diagnostics from failed jobs
7. **Documentation**: Archive generated documentation from builds
8. **Publishing**: Store artifacts for release and distribution

### 1. **Uploading Artifacts**

Use the `actions/upload-artifact` action to save files and directories.

#### Basic Upload

```yaml
name: Upload Artifact Example

on: push

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Build Project
        run: |
          npm install
          npm run build

      - name: Upload Build Artifacts
        uses: actions/upload-artifact@v3
        with:
          name: build-output
          path: dist/
```

#### Upload Multiple Files

```yaml
- name: Upload Multiple Artifacts
  uses: actions/upload-artifact@v3
  with:
    name: build-artifacts
    path: |
      dist/
      build/
      coverage/
```

#### Upload with Patterns

```yaml
- name: Upload Specific Files
  uses: actions/upload-artifact@v3
  with:
    name: app-package
    path: |
      dist/**/*.js
      dist/**/*.css
      public/index.html
      !dist/**/*.map
```

#### Upload with Retention

```yaml
- name: Upload with Retention
  uses: actions/upload-artifact@v3
  with:
    name: build-output
    path: dist/
    retention-days: 30 # Keep for 30 days (default is 5)
```

### 2. **Downloading Artifacts**

Use the `actions/download-artifact` action to retrieve uploaded artifacts.

#### Download in Same Workflow

```yaml
name: Build and Deploy

on: push

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Build
        run: npm run build

      - name: Upload Build
        uses: actions/upload-artifact@v3
        with:
          name: build-dist
          path: dist/

  deploy:
    needs: build # Wait for build job to complete
    runs-on: ubuntu-latest
    steps:
      - name: Download Build Artifacts
        uses: actions/download-artifact@v3
        with:
          name: build-dist
          path: ./app-dist/

      - name: Deploy
        run: |
          echo "Deploying files from $(ls ./app-dist/)"
          # Deploy logic here
```

#### Download All Artifacts

```yaml
- name: Download All Artifacts
  uses: actions/download-artifact@v3
  with:
    path: artifacts/ # Downloads all artifacts to this directory
```

#### Download Across Workflows

```yaml
- name: Download from Previous Workflow
  uses: actions/download-artifact@v3
  with:
    name: build-dist
    github-token: ${{ secrets.GITHUB_TOKEN }}
    run-id: 1234567890 # Specific workflow run ID
```

### 3. **Practical Example: Build, Test, and Deploy**

```yaml
name: Complete CI/CD Pipeline

on:
  push:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    outputs:
      version: ${{ steps.version.outputs.version }}
    steps:
      - uses: actions/checkout@v3

      - name: Set Version
        id: version
        run: echo "version=v1.0.${{ github.run_number }}" >> $GITHUB_OUTPUT

      - name: Build Application
        run: |
          npm install
          npm run build
          echo "Build version: ${{ steps.version.outputs.version }}"

      - name: Create Build Info
        run: |
          cat > build-info.json <<EOF
          {
            "version": "${{ steps.version.outputs.version }}",
            "commit": "${{ github.sha }}",
            "branch": "${{ github.ref_name }}",
            "timestamp": "$(date -u +%Y-%m-%dT%H:%M:%SZ)"
          }
          EOF

      - name: Upload Build
        uses: actions/upload-artifact@v3
        with:
          name: app-build-${{ steps.version.outputs.version }}
          path: |
            dist/
            build-info.json
          retention-days: 30

  test:
    needs: build
    runs-on: ubuntu-latest
    strategy:
      matrix:
        node-version: [14, 16, 18]
    steps:
      - uses: actions/checkout@v3

      - name: Use Node.js ${{ matrix.node-version }}
        uses: actions/setup-node@v3
        with:
          node-version: ${{ matrix.node-version }}

      - name: Install Dependencies
        run: npm ci

      - name: Run Tests
        run: npm test -- --coverage

      - name: Upload Coverage Reports
        uses: actions/upload-artifact@v3
        with:
          name: coverage-node-${{ matrix.node-version }}
          path: coverage/

  deploy:
    needs: [build, test]
    runs-on: ubuntu-latest
    if: success()
    environment:
      name: production
      url: https://example.com
    steps:
      - name: Download Build Artifacts
        uses: actions/download-artifact@v3
        with:
          name: app-build-${{ needs.build.outputs.version }}
          path: ./app/

      - name: Read Build Info
        id: buildinfo
        run: cat ./app/build-info.json | jq '.' > $GITHUB_OUTPUT

      - name: Deploy to Production
        run: |
          echo "Deploying version: ${{ needs.build.outputs.version }}"
          echo "Build Time: $(jq -r '.timestamp' ./app/build-info.json)"
          # Deploy logic
          ls -la ./app/dist/

      - name: Upload Deployment Log
        if: always()
        uses: actions/upload-artifact@v3
        with:
          name: deployment-log-${{ needs.build.outputs.version }}
          path: deploy.log
```

### 4. **Artifact Storage and Limits**

Understand GitHub's artifact storage policies:

#### Storage Limits

| Plan       | Storage   | Retention |
| ---------- | --------- | --------- |
| Free       | 500 MB    | 5 days    |
| Pro        | 1 GB      | 5 days    |
| Team       | 2 GB      | 5 days    |
| Enterprise | Unlimited | 5 days    |

#### Retention Policy

```yaml
- name: Short Retention (CI Logs)
  uses: actions/upload-artifact@v3
  with:
    name: ci-logs
    path: logs/
    retention-days: 3 # Quick cleanup

- name: Long Retention (Releases)
  uses: actions/upload-artifact@v3
  with:
    name: release-package
    path: dist/
    retention-days: 90 # Keep longer for releases
```

### 5. **Advanced Artifact Usage**

#### Conditional Artifact Upload

```yaml
- name: Upload on Failure
  if: failure() # Only upload if previous step failed
  uses: actions/upload-artifact@v3
  with:
    name: failure-logs
    path: |
      logs/
      error-dump/

- name: Upload on Success
  if: success()
  uses: actions/upload-artifact@v3
  with:
    name: success-build
    path: dist/
```

#### Merging Multiple Artifacts

```yaml
jobs:
  collect-results:
    needs: [test-unit, test-integration, test-e2e]
    runs-on: ubuntu-latest
    if: always() # Run even if previous jobs failed
    steps:
      - name: Download All Test Results
        uses: actions/download-artifact@v3
        with:
          path: test-results/

      - name: Combine Results
        run: |
          mkdir -p combined-results
          find test-results -name "*.xml" -exec cp {} combined-results/ \;
          echo "Combined $(find combined-results -type f | wc -l) test result files"

      - name: Upload Combined Results
        uses: actions/upload-artifact@v3
        with:
          name: all-test-results
          path: combined-results/
```

#### Artifact with Metadata

```yaml
- name: Create Artifact with Metadata
  run: |
    mkdir -p release-package
    cp -r dist release-package/

    # Create metadata file
    cat > release-package/metadata.json <<EOF
    {
      "version": "${{ github.ref_name }}",
      "commit": "${{ github.sha }}",
      "author": "${{ github.actor }}",
      "timestamp": "$(date -u +%Y-%m-%dT%H:%M:%SZ)",
      "build_number": ${{ github.run_number }}
    }
    EOF

- name: Upload with Metadata
  uses: actions/upload-artifact@v3
  with:
    name: release-${{ github.ref_name }}
    path: release-package/
```

### 6. **Using Artifacts for Releases**

```yaml
name: Build and Release

on:
  push:
    tags:
      - "v*"

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Build Release Package
        run: |
          npm install
          npm run build
          npm run package

      - name: Upload Release Assets
        uses: actions/upload-artifact@v3
        with:
          name: release-assets
          path: |
            dist/
            CHANGELOG.md
            LICENSE

  release:
    needs: build
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Download Artifacts
        uses: actions/download-artifact@v3
        with:
          name: release-assets
          path: ./release/

      - name: Create GitHub Release
        uses: softprops/action-gh-release@v1
        with:
          files: |
            ./release/dist/**
            ./release/CHANGELOG.md
          draft: false
          prerelease: false
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

### 7. **Best Practices for Artifacts**

#### ✓ Recommended Practices

```yaml
# ✓ Use descriptive artifact names
- name: Upload Build
  uses: actions/upload-artifact@v3
  with:
    name: build-${{ runner.os }}-${{ github.run_number }}
    path: dist/
    retention-days: 30

# ✓ Upload artifacts only when needed
- name: Upload on Failure
  if: failure()
  uses: actions/upload-artifact@v3
  with:
    name: failure-diagnostics
    path: logs/

# ✓ Use paths to filter included files
- name: Upload Slim Artifacts
  uses: actions/upload-artifact@v3
  with:
    name: app
    path: |
      dist/**/*.js
      dist/**/*.css
      dist/index.html
    # Excludes source maps and other unnecessary files

# ✓ Set appropriate retention times
- name: Short-lived CI Logs
  uses: actions/upload-artifact@v3
  with:
    name: logs
    retention-days: 3

# ✓ Download artifacts with clear paths
- name: Download for Deployment
  uses: actions/download-artifact@v3
  with:
    name: production-build
    path: ./deploy-package/
```

#### ✗ Anti-Patterns to Avoid

```yaml
# ✗ Don't upload entire workspace
- name: Bad Upload
  uses: actions/upload-artifact@v3
  with:
    name: everything
    path: .  # Uploads everything, including node_modules!

# ✗ Don't forget retention management
- name: Unlimited Retention
  uses: actions/upload-artifact@v3
  with:
    name: artifact
    path: dist/
    # Retention-days not set, defaults to 5

# ✗ Don't upload sensitive information
- name: Bad Security Practice
  uses: actions/upload-artifact@v3
  with:
    name: secrets  # NEVER do this!
    path: |
      .env
      secrets.json
      private-keys/

# ✗ Don't rely on artifacts for permanent storage
jobs:
  build:
    steps:
      - run: npm run build
      - uses: actions/upload-artifact@v3
        with:
          name: build
          path: dist/
          retention-days: 90  # Still temporary!
          # Don't use for permanent archives; use releases instead
```

### 8. **Troubleshooting Artifacts**

#### Artifact Not Found

**Problem**: `Artifact not found: build-output`

**Solutions**:

```yaml
# Verify artifact exists before download
- name: Download Artifact
  uses: actions/download-artifact@v3
  with:
    name: build-output

- name: Check Contents
  run: ls -la ./build-output/ || echo "Artifact not found"
```

#### Storage Quota Exceeded

**Problem**: Workflow fails due to storage limits

**Solution**: Manage retention and cleanup

```yaml
# Upload with shorter retention
- name: Upload Logs
  uses: actions/upload-artifact@v3
  with:
    name: build-logs
    path: logs/
    retention-days: 3 # Cleanup faster

# Delete old artifacts via API
- name: Cleanup Old Artifacts
  run: |
    # Script to delete artifacts older than 30 days
    hub api repos/$GITHUB_REPOSITORY/actions/artifacts \
      --paginate | \
      jq -r '.artifacts[] | select(.created_at < now - "30 days") | .id' | \
      xargs -I {} hub api repos/$GITHUB_REPOSITORY/actions/artifacts/{} -X DELETE
```

#### Large File Size

**Problem**: Upload is slow or times out

**Solution**: Compress before uploading

```yaml
- name: Compress Artifacts
  run: |
    tar -czf build-archive.tar.gz dist/
    du -h build-archive.tar.gz  # Check size

- name: Upload Compressed
  uses: actions/upload-artifact@v3
  with:
    name: build-compressed
    path: build-archive.tar.gz

- name: Extract After Download
  run: tar -xzf build-archive.tar.gz
```

---

## GitHub Workflow Caching

### What is Workflow Caching?

Workflow caching is a mechanism that stores files and directories during a workflow run and retrieves them in subsequent runs. Instead of downloading or rebuilding dependencies every time your workflow runs, cached files are restored, significantly reducing workflow execution time and bandwidth usage.

### Why Use Caching?

**Key Benefits:**

1. **Performance**: Dramatically reduce workflow execution time by avoiding redundant downloads and builds
2. **Cost Efficiency**: Lower bandwidth usage and reduced resource consumption
3. **Reliability**: Reduce dependency on external services and network issues
4. **Developer Experience**: Faster feedback loops for CI/CD pipelines
5. **Scalability**: Enable faster builds as your project grows

**Real-World Impact Example:**

```
Without Caching:
- Install dependencies: 3-5 minutes
- Build: 2 minutes
- Test: 3 minutes
- Total: 8-10 minutes per run

With Caching:
- Restore cache: 10-20 seconds
- Build: 2 minutes (unchanged)
- Test: 3 minutes (unchanged)
- Total: 5-6 minutes per run (40-50% improvement)
```

### How Caching Works

**Cache Mechanism:**

1. **Save Phase**: At end of workflow, specified files are zipped and stored
2. **Key Generation**: Cache is identified by a unique key based on files or inputs
3. **Restore Phase**: On next run, if key matches, cache is restored before workflow starts
4. **Fallback**: If exact key doesn't match, fallback keys are tried in order

**Storage Details:**

- Storage limit: 5 GB per repository
- Cache accessible only to same branch
- Cache expires after 7 days of no access
- Free tier provides full cache access

### 1. **Basic Caching**

#### Caching Dependencies

```yaml
name: Cache Dependencies

on: push

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      # Restore cache from previous runs
      - uses: actions/cache@v3
        with:
          path: ~/.npm
          key: ${{ runner.os }}-npm-${{ hashFiles('**/package-lock.json') }}
          restore-keys: |
            ${{ runner.os }}-npm-

      # Cache hit will skip this install if exact key matches
      - name: Install Dependencies
        run: npm ci

      - name: Build
        run: npm run build
```

#### How This Works

```
First Run:
- Cache key: ubuntu-npm-abc123def456
- Key not found in cache
- npm ci runs and downloads dependencies
- ~/.npm directory is cached

Second Run (same dependencies):
- Cache key: ubuntu-npm-abc123def456
- Key found! Cache restored
- npm ci runs but dependencies already present
- Execution ~50x faster

Third Run (dependencies updated):
- Cache key: ubuntu-npm-xyz789uvw012 (new hash)
- Key not found (new dependency hash)
- Falls back to: ubuntu-npm- (tries ubuntu-npm-*)
- Finds previous cache, uses as starting point
- Only new dependencies downloaded
```

#### Check if Cache Hit Occurred

```yaml
- uses: actions/cache@v3
  id: cache
  with:
    path: ~/.npm
    key: ${{ runner.os }}-npm-${{ hashFiles('**/package-lock.json') }}

- name: Cache Status
  run: |
    if [ "${{ steps.cache.outputs.cache-hit }}" = "true" ]; then
      echo "✓ Cache hit! Dependencies restored"
    else
      echo "✗ Cache miss. Fresh dependencies installed"
    fi
```

### 2. **Multiple Cache Paths**

```yaml
- uses: actions/cache@v3
  with:
    path: |
      ~/.npm
      ~/.gradle/caches
      ~/.gradle/wrapper
    key: ${{ runner.os }}-build-${{ hashFiles('**/package-lock.json', '**/gradle.properties') }}
    restore-keys: |
      ${{ runner.os }}-build-
```

### 3. **Language-Specific Caching**

#### Node.js / npm / yarn

```yaml
# Cache npm dependencies
- uses: actions/cache@v3
  with:
    path: ~/.npm
    key: ${{ runner.os }}-npm-${{ hashFiles('**/package-lock.json') }}

# Cache yarn dependencies
- uses: actions/cache@v3
  with:
    path: ~/.yarn/cache
    key: ${{ runner.os }}-yarn-${{ hashFiles('**/yarn.lock') }}
```

#### Python / pip

```yaml
- uses: actions/cache@v3
  with:
    path: ~/.cache/pip
    key: ${{ runner.os }}-pip-${{ hashFiles('**/requirements.txt') }}
    restore-keys: |
      ${{ runner.os }}-pip-
```

#### Java / Maven

```yaml
- uses: actions/cache@v3
  with:
    path: ~/.m2/repository
    key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}
```

#### Java / Gradle

```yaml
- uses: actions/cache@v3
  with:
    path: |
      ~/.gradle/caches
      ~/.gradle/wrapper
    key: ${{ runner.os }}-gradle-${{ hashFiles('**/gradle.properties', '**/gradle/wrapper/gradle-wrapper.properties') }}
```

#### Ruby / Bundler

```yaml
- uses: actions/cache@v3
  with:
    path: vendor/bundle
    key: ${{ runner.os }}-bundle-${{ hashFiles('**/Gemfile.lock') }}
```

### 4. **Practical Full CI Pipeline with Caching**

```yaml
name: Optimized CI with Caching

on: push

jobs:
  build-and-test:
    runs-on: ubuntu-latest
    strategy:
      matrix:
        node-version: [16, 18, 20]

    steps:
      - uses: actions/checkout@v3

      # Cache node_modules by Node version
      - name: Cache Node Modules
        uses: actions/cache@v3
        id: node-cache
        with:
          path: node_modules
          key: ${{ runner.os }}-node-${{ matrix.node-version }}-${{ hashFiles('**/package-lock.json') }}
          restore-keys: |
            ${{ runner.os }}-node-${{ matrix.node-version }}-
            ${{ runner.os }}-node-

      - name: Setup Node.js
        uses: actions/setup-node@v3
        with:
          node-version: ${{ matrix.node-version }}

      # Only run if cache miss
      - name: Install Dependencies
        if: steps.node-cache.outputs.cache-hit != 'true'
        run: npm ci

      # Cache build output
      - name: Cache Build Directory
        uses: actions/cache@v3
        id: build-cache
        with:
          path: dist
          key: ${{ runner.os }}-build-${{ matrix.node-version }}-${{ github.sha }}
          restore-keys: |
            ${{ runner.os }}-build-${{ matrix.node-version }}-

      - name: Build
        run: npm run build

      # Cache test coverage results
      - name: Run Tests
        run: npm test -- --coverage

      - name: Upload Coverage
        uses: actions/upload-artifact@v3
        with:
          name: coverage-node-${{ matrix.node-version }}
          path: coverage/
```

### 5. **Advanced Caching Strategies**

#### Restore Multiple Cache Keys in Order

```yaml
- uses: actions/cache@v3
  with:
    path: ~/.cache/build
    # Exact match first, then broader matches
    key: build-${{ runner.os }}-${{ github.sha }}
    restore-keys: |
      build-${{ runner.os }}-
      build-
```

#### Cache with Conditional Logic

```yaml
- name: Determine Cache Key
  id: cache-key
  run: |
    if [ "${{ github.event_name }}" = "pull_request" ]; then
      echo "key=pr-cache-${{ github.head_ref }}" >> $GITHUB_OUTPUT
    else
      echo "key=main-cache-${{ github.ref }}" >> $GITHUB_OUTPUT
    fi

- uses: actions/cache@v3
  with:
    path: build-cache/
    key: ${{ steps.cache-key.outputs.key }}
```

#### Clearing Cache When Needed

```bash
# Via GitHub CLI
gh actions-cache delete CACHE_KEY --repo OWNER/REPO --branch BRANCH

# Delete all caches for a branch
gh actions-cache list --repo OWNER/REPO --branch BRANCH | \
  cut -f 1 | xargs -I {} gh actions-cache delete {} \
  --repo OWNER/REPO --branch BRANCH --confirm
```

### 6. **Caching Best Practices**

#### ✓ Recommended Practices

```yaml
# ✓ Use hashFiles for cache keys based on lock files
- uses: actions/cache@v3
  with:
    path: ~/.npm
    key: ${{ runner.os }}-npm-${{ hashFiles('**/package-lock.json') }}

# ✓ Include fallback restore keys
    restore-keys: |
      ${{ runner.os }}-npm-
      ${{ runner.os }}-

# ✓ Cache only necessary files
    path: node_modules  # Don't cache entire project

# ✓ Cache language-specific directories
    path: |
      ~/.npm
      ~/.cargo
      ~/.m2

# ✓ Check cache hit status
- if: steps.cache.outputs.cache-hit != 'true'
  run: npm ci
```

#### ✗ Anti-Patterns to Avoid

```yaml
# ✗ Don't cache entire repository
- uses: actions/cache@v3
  with:
    path: .  # BAD - caches entire project

# ✗ Don't use dynamic content in cache key
    key: ${{ github.run_number }}  # Changes every run!

# ✗ Don't cache files with secrets
    path: |
      ~/.ssh
      ~/.aws/credentials
      .env

# ✗ Don't ignore cache hit status
- run: npm ci  # Runs every time, defeating cache purpose
```

### 7. **Troubleshooting Caching**

#### Cache Not Being Used

```yaml
# Debug: Print cache key that would be generated
- name: Debug Cache Key
  run: |
    echo "Cache key: ${{ runner.os }}-npm-${{ hashFiles('**/package-lock.json') }}"
    ls -la package-lock.json

# Check if cache is enabled
- name: Verify Cache Hit
  run: echo "Cache hit: ${{ steps.cache.outputs.cache-hit }}"
```

#### Cache Size Growing Too Large

```yaml
# Monitor cache size
- name: Check Cache Size
  run: |
    du -sh ~/.npm
    du -sh ~/.gradle/caches
    du -sh node_modules
```

---

## Workflow Sharing

### What is Workflow Sharing?

Workflow sharing allows you to reuse workflow files across multiple repositories or share standardized automation patterns within your organization. Instead of duplicating workflow code, you can create a single source of truth and reference it from other repositories.

### Why Share Workflows?

**Key Benefits:**

1. **Code Reuse**: Avoid duplicating workflows across repositories
2. **Consistency**: Ensure all projects follow the same CI/CD standards
3. **Maintainability**: Update workflows in one place, benefits all repositories
4. **Standardization**: Enforce organizational best practices
5. **Reduced Errors**: Centralized quality and security checks
6. **Quick Onboarding**: New projects inherit established workflows

**Real-World Scenario:**

```
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

jobs:
  job-name:
    runs-on: ubuntu-latest
    steps:
      - run: echo ${{ inputs.parameter-name }}
      - run: echo ${{ secrets.secret-name }}
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

#### ✓ Recommended Practices

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

#### ✗ Anti-Patterns to Avoid

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

## Workflow Debugging

### What is Workflow Debugging?

Workflow debugging is the process of identifying and fixing issues in GitHub Actions workflows. It involves understanding why workflows fail, examining logs, adding diagnostic output, and validating configurations. Debugging techniques range from simple log inspection to advanced tracing and performance analysis.

### Why Debug Workflows?

**Key Reasons:**

1. **Failure Resolution**: Quickly identify and fix workflow failures
2. **Performance Optimization**: Identify slow steps and bottlenecks
3. **Cost Reduction**: Optimize resource usage and execution time
4. **Reliability**: Ensure workflows run consistently
5. **Learning**: Understand workflow behavior and best practices
6. **Prevention**: Catch issues before they reach production

### How Debugging Works

**Debugging Workflow:**

1. **Identify**: Recognize workflow has failed or behaves unexpectedly
2. **Inspect**: Review workflow logs and error messages
3. **Analyze**: Determine root cause using available information
4. **Test**: Add diagnostic steps to gather more information
5. **Fix**: Apply solution based on findings
6. **Verify**: Confirm workflow works as expected

### 1. **Understanding Workflow Logs**

#### Accessing Workflow Logs

**Via GitHub Web UI:**

```
1. Navigate to Repository → Actions tab
2. Click on specific workflow run
3. View logs for each job and step
4. Click on individual steps to expand logs
```

**Log Levels:**

```
[INFO] Standard information messages
[WARNING] Potential issues
[ERROR] Error conditions
[DEBUG] Detailed diagnostic information (when enabled)
```

#### Environment Information in Logs

```yaml
jobs:
  debug:
    runs-on: ubuntu-latest
    steps:
      - name: Print Environment
        run: |
          echo "=== GitHub Context ==="
          echo "Event: $GITHUB_EVENT_NAME"
          echo "Repository: $GITHUB_REPOSITORY"
          echo "Branch: $GITHUB_REF_NAME"
          echo "Actor: $GITHUB_ACTOR"
          echo "Workspace: $GITHUB_WORKSPACE"

          echo ""
          echo "=== Runner Info ==="
          echo "OS: $RUNNER_OS"
          echo "Arch: $RUNNER_ARCH"
          uname -a

          echo ""
          echo "=== Disk Space ==="
          df -h
```

### 2. **Enabling Debug Logging**

#### RUNNER_DEBUG Variable

**Enable in Workflow:**

```yaml
- name: Run with Debug
  env:
    RUNNER_DEBUG: true
  run: npm test
```

**Enable via Secrets:**

1. Go to `Settings → Secrets → Actions`
2. Create secret: `ACTIONS_STEP_DEBUG: true`
3. Re-run workflow - all steps produce verbose output

#### Output with RUNNER_DEBUG

```bash
# Without RUNNER_DEBUG:
/usr/bin/npm test
Test Results: PASS

# With RUNNER_DEBUG:
::debug::Preparing command: npm test
::debug::PWD: /home/runner/work/repo/repo
::debug::PATH: /usr/local/sbin:/usr/local/bin:...
::debug::Arguments: ['test']
::debug::Exit code: 0
/usr/bin/npm test
Test Results: PASS
```

### 3. **Using Workflow Commands**

#### Add Diagnostic Markers

```yaml
- name: Step with Diagnostics
  run: |
    echo "::debug::Starting build process"
    npm run build
    echo "::notice::Build completed successfully"
    echo "::warning::Deprecated feature used in code"
    echo "::error::Critical issue found"
```

#### Output Variables for Debugging

```yaml
- name: Capture Build Output
  id: build
  run: |
    echo "::debug::Running build..."
    BUILD_OUTPUT=$(npm run build 2>&1)
    echo "output=$BUILD_OUTPUT" >> $GITHUB_OUTPUT
    echo "::debug::Build output: $BUILD_OUTPUT"

- name: Check Build Output
  run: echo "Build: ${{ steps.build.outputs.output }}"
```

#### Grouping Output

```yaml
- name: Complex Step
  run: |
    echo "::group::Build Process"
    echo "Starting build..."
    npm run build
    echo "Build complete"
    echo "::endgroup::"

    echo "::group::Test Process"
    npm test
    echo "::endgroup::"
```

### 4. **Common Debugging Scenarios**

#### Scenario 1: Authentication Failures

```yaml
jobs:
  debug-auth:
    runs-on: ubuntu-latest
    steps:
      - name: Debug GitHub Token
        run: |
          # Check if token is present
          if [ -z "$GITHUB_TOKEN" ]; then
            echo "::error::GITHUB_TOKEN not set"
            exit 1
          fi

          # Check token permissions
          echo "::debug::Checking GitHub token permissions"
          curl -H "Authorization: token $GITHUB_TOKEN" \
               https://api.github.com/user \
               -o /dev/null -w "HTTP Status: %{http_code}\n"

          if [ $? -ne 0 ]; then
            echo "::error::Token authentication failed"
            exit 1
          fi
          echo "::notice::Token authentication successful"
```

#### Scenario 2: Dependency Issues

```yaml
jobs:
  debug-deps:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Debug Dependencies
        run: |
          echo "::group::Dependency Information"

          echo "Node version:"
          node --version

          echo "\nNPM version:"
          npm --version

          echo "\nChecking package-lock.json:"
          if [ -f package-lock.json ]; then
            echo "package-lock.json exists"
            echo "Hash: $(md5sum package-lock.json)"
          else
            echo "::warning::package-lock.json not found"
          fi

          echo "\nDisk space available:"
          df -h | grep -E '^/dev/|Available'

          echo "::endgroup::"

      - name: Install with Verbose Output
        run: npm ci --verbose
```

#### Scenario 3: Timeout Issues

```yaml
jobs:
  debug-timeout:
    runs-on: ubuntu-latest
    timeout-minutes: 10
    steps:
      - uses: actions/checkout@v3

      - name: Check Start Time
        id: start
        run: echo "time=$(date +%s)" >> $GITHUB_OUTPUT

      - name: Long Running Task
        run: |
          echo "::debug::Task started at ${{ steps.start.outputs.time }}"
          ./long-task.sh
          echo "::debug::Task completed at $(date +%s)"

      - name: Check Elapsed Time
        if: always()
        run: |
          START=${{ steps.start.outputs.time }}
          END=$(date +%s)
          ELAPSED=$((END - START))
          echo "::notice::Elapsed time: ${ELAPSED}s"

          if [ $ELAPSED -gt 540 ]; then
            echo "::warning::Task approaching timeout (9 minutes)"
          fi
```

### 5. **Performance and Profiling**

#### Identify Slow Steps

```yaml
name: Performance Profiling

on: push

jobs:
  profile:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Profile Step Times
        run: |
          #!/bin/bash
          declare -A times

          echo "::group::Performance Metrics"

          # Step 1: Setup
          START=$(date +%s%N)
          npm install
          END=$(date +%s%N)
          TIME=$(( ($END - $START) / 1000000 ))
          echo "Setup time: ${TIME}ms"
          times[setup]=$TIME

          # Step 2: Build
          START=$(date +%s%N)
          npm run build
          END=$(date +%s%N)
          TIME=$(( ($END - $START) / 1000000 ))
          echo "Build time: ${TIME}ms"
          times[build]=$TIME

          # Step 3: Test
          START=$(date +%s%N)
          npm test
          END=$(date +%s%N)
          TIME=$(( ($END - $START) / 1000000 ))
          echo "Test time: ${TIME}ms"
          times[test]=$TIME

          # Find slowest step
          slowest_key=$(for k in "${!times[@]}"; do echo "$k:${times[$k]}"; done | sort -t: -k2 -nr | head -1 | cut -d: -f1)
          echo "::notice::Slowest step: $slowest_key (${times[$slowest_key]}ms)"

          echo "::endgroup::"
```

#### Cache Hit Analysis

```yaml
- uses: actions/cache@v3
  id: cache
  with:
    path: node_modules
    key: ${{ runner.os }}-npm-${{ hashFiles('**/package-lock.json') }}

- name: Analyze Cache Performance
  run: |
    echo "::group::Cache Analysis"
    CACHE_HIT="${{ steps.cache.outputs.cache-hit }}"

    if [ "$CACHE_HIT" = "true" ]; then
      echo "✓ Cache hit - dependencies restored"
    else
      echo "✗ Cache miss - fresh dependencies installed"
      echo "::warning::Consider checking if lock file changed unexpectedly"
    fi

    echo "Node modules size:"
    du -sh node_modules

    echo "::endgroup::"
```

### 6. **Debugging Common Failures**

#### File Not Found Error

```yaml
- name: Debug File Issue
  run: |
    echo "::group::File Debugging"

    TARGET_FILE="dist/index.js"

    if [ ! -f "$TARGET_FILE" ]; then
      echo "::error::File not found: $TARGET_FILE"

      echo "Current directory: $(pwd)"
      echo "Directory contents:"
      ls -la

      echo "\nSearching for index.js:"
      find . -name "index.js" -type f

      exit 1
    fi

    echo "✓ File found: $TARGET_FILE"
    echo "::endgroup::"
```

#### Environment Variable Issues

```yaml
- name: Debug Environment Variables
  run: |
    echo "::group::Environment Variables"

    # Check if expected variables exist
    REQUIRED_VARS=("DATABASE_URL" "API_KEY" "ENVIRONMENT")

    for var in "${REQUIRED_VARS[@]}"; do
      if [ -z "${!var}" ]; then
        echo "::error::Required variable not set: $var"
      else
        echo "✓ $var is set"
      fi
    done

    echo "All available workflow variables:"
    compgen -e | sort

    echo "::endgroup::"
```

### 7. **Best Practices for Debugging**

#### ✓ Recommended Practices

```yaml
# ✓ Add strategic debug output at key points
- run: |
    echo "::debug::Starting build process"
    npm run build
    echo "::debug::Build completed"

# ✓ Capture and analyze logs
- run: npm test 2>&1 | tee test-output.log
- uses: actions/upload-artifact@v3
  if: always()
  with:
    name: test-logs
    path: test-output.log

# ✓ Use meaningful error messages
- run: |
    if [ ! -f config.json ]; then
      echo "::error::config.json required but not found"
      exit 1
    fi

# ✓ Group related debugging information
- run: |
    echo "::group::System Information"
    uname -a
    df -h
    echo "::endgroup::"

# ✓ Enable debugging only when needed
- name: Run with Debug (if triggered)
  env:
    RUNNER_DEBUG: ${{ secrets.ACTIONS_STEP_DEBUG }}
  run: npm test
```

#### ✗ Anti-Patterns to Avoid

```yaml
# ✗ Don't expose secrets in debug output
- run: echo "::debug::API Key: ${{ secrets.API_KEY }}"  # NEVER!

# ✗ Don't leave debug logging on permanently
# (Wastes resources and clutters logs)
- run: |
    set -x  # Debug mode - only for troubleshooting
    npm test
    set +x

# ✗ Don't ignore failed steps
- run: npm test || true  # BAD - hides failures

# ✗ Don't use hardcoded test paths
- run: /home/runner/work/specific-repo/specific-repo/test.sh  # Not portable!
```

### 8. **Advanced Debugging Techniques**

#### Real-time Log Streaming

```yaml
- name: Stream Logs in Real-time
  run: |
    (
      npm test
    ) 2>&1 | while IFS= read -r line; do
      echo "[$(date +'%Y-%m-%d %H:%M:%S')] $line"
    done
```

#### Conditional Debugging

```yaml
- name: Run with Conditional Debug
  run: |
    if [[ "${{ github.event_name }}" == "pull_request" ]]; then
      echo "::debug::PR detected - enabling verbose mode"
      DEBUG_FLAGS="--verbose"
    else
      DEBUG_FLAGS=""
    fi

    npm test $DEBUG_FLAGS
```

#### Artifact Collection for Analysis

```yaml
- name: Collect Debug Artifacts
  if: always()
  run: |
    mkdir -p debug-artifacts

    # Collect logs
    cp /var/log/syslog debug-artifacts/ || true

    # Collect build outputs
    cp -r build debug-artifacts/ || true

    # Collect test results
    cp -r coverage debug-artifacts/ || true

    # Collect environment info
    env > debug-artifacts/environment.txt

    # Create archive
    tar -czf debug-artifacts.tar.gz debug-artifacts/

- uses: actions/upload-artifact@v3
  if: always()
  with:
    name: debug-artifacts
    path: debug-artifacts.tar.gz
```

---

## GitHub Workflows REST API

### What is the GitHub Workflows REST API?

The GitHub Workflows REST API is a set of HTTP endpoints provided by GitHub that allow you to programmatically interact with GitHub Actions workflows. Instead of manually triggering workflows or managing them through the web UI, you can use the REST API to automate workflow management from external systems, scripts, or applications.

### Why Use the Workflows REST API?

**Key Benefits:**

1. **Automation**: Programmatically trigger and manage workflows without UI interaction
2. **Integration**: Connect external systems (deployment tools, monitoring systems, etc.) with GitHub Actions
3. **Monitoring**: Retrieve workflow execution data for tracking, reporting, and analytics
4. **Control**: Dynamically manage workflow instances (list, view, cancel, re-run)
5. **Developer Experience**: Create custom tooling and dashboards around workflows
6. **Compliance**: Query workflow history for audit and compliance purposes
7. **CI/CD Enhancement**: Build sophisticated automation chains across platforms

**Real-World Applications:**

```
Scenario 1: Trigger deployment on external event
- External monitoring system detects issue
- Calls GitHub API to trigger deployment workflow
- Automatically starts incident response

Scenario 2: Monitor workflow execution
- Dashboard queries workflow runs
- Displays status across multiple repositories
- Alerts team on failures

Scenario 3: Automated workflow management
- Cleanup script removes old workflow runs
- Analyzes execution times for performance optimization
- Manages workflow artifacts
```

### How the REST API Works

**Authentication:**

All API requests require authentication using:

- Personal Access Token (PAT)
- GitHub App Token
- OAuth token

**Base URL:**

```
https://api.github.com
```

**Endpoint Format:**

```
GET /repos/{owner}/{repo}/actions/workflows
GET /repos/{owner}/{repo}/actions/runs
GET /repos/{owner}/{repo}/actions/runs/{run_id}
POST /repos/{owner}/{repo}/actions/workflows/{workflow_id}/dispatches
```

### 1. **List Workflows**

**Endpoint**: `GET /repos/{owner}/{repo}/actions/workflows`

**What it does**: Returns all workflows in a repository

**Basic Example**:

```bash
curl -H "Authorization: token YOUR_TOKEN" \
  https://api.github.com/repos/octocat/Hello-World/actions/workflows
```

**With Filters**:

```bash
curl -H "Authorization: token YOUR_TOKEN" \
  "https://api.github.com/repos/octocat/Hello-World/actions/workflows?per_page=10&page=1"
```

**Response Example**:

```json
{
  "total_count": 2,
  "workflows": [
    {
      "id": 123456,
      "node_id": "MDg6V29ya2Zsb3cxMjM0NTY=",
      "name": "CI",
      "path": ".github/workflows/ci.yml",
      "state": "active",
      "created_at": "2024-01-15T10:30:00Z",
      "updated_at": "2024-03-09T14:20:00Z",
      "url": "https://api.github.com/repos/octocat/Hello-World/actions/workflows/123456",
      "html_url": "https://github.com/octocat/Hello-World/blob/main/.github/workflows/ci.yml",
      "badges_url": "https://github.com/octocat/Hello-World/workflows/CI/badge.svg"
    }
  ]
}
```

#### Using in a Script

```bash
#!/bin/bash

REPO_OWNER="octocat"
REPO_NAME="Hello-World"
GITHUB_TOKEN="your_token_here"

# Get all workflows
WORKFLOWS=$(curl -s -H "Authorization: token $GITHUB_TOKEN" \
  "https://api.github.com/repos/$REPO_OWNER/$REPO_NAME/actions/workflows")

# Parse and display
echo "$WORKFLOWS" | jq '.workflows[] | {id, name, state, path}'

# Output:
# {
#   "id": 123456,
#   "name": "CI",
#   "state": "active",
#   "path": ".github/workflows/ci.yml"
# }
```

### 2. **Get Workflow Details**

**Endpoint**: `GET /repos/{owner}/{repo}/actions/workflows/{workflow_id}`

**What it does**: Retrieve detailed information about a specific workflow

```bash
curl -H "Authorization: token YOUR_TOKEN" \
  https://api.github.com/repos/octocat/Hello-World/actions/workflows/123456
```

**Response**:

```json
{
  "id": 123456,
  "name": "CI Pipeline",
  "path": ".github/workflows/ci.yml",
  "state": "active",
  "created_at": "2024-01-15T10:30:00Z",
  "updated_at": "2024-03-09T14:20:00Z"
}
```

### 3. **List Workflow Runs**

**Endpoint**: `GET /repos/{owner}/{repo}/actions/runs`

**What it does**: List all workflow runs (executions) in a repository

**Example: Get Recent Failed Runs**

```bash
curl -H "Authorization: token YOUR_TOKEN" \
  "https://api.github.com/repos/octocat/Hello-World/actions/runs?status=failure&per_page=10"
```

**Response**:

```json
{
  "total_count": 5,
  "workflow_runs": [
    {
      "id": 987654,
      "name": "CI",
      "node_id": "WFR123456",
      "head_branch": "main",
      "head_sha": "abc123def456",
      "status": "failure",
      "conclusion": "failure",
      "workflow_id": 123456,
      "check_suite_id": 555555,
      "check_suite_node_id": "CS555555",
      "url": "https://api.github.com/repos/octocat/Hello-World/actions/runs/987654",
      "html_url": "https://github.com/octocat/Hello-World/actions/runs/987654",
      "created_at": "2024-03-09T10:15:00Z",
      "updated_at": "2024-03-09T10:20:00Z",
      "run_number": 42,
      "event": "push",
      "display_title": "Deployment workflow",
      "actor": {
        "login": "octocat",
        "id": 1
      }
    }
  ]
}
```

#### Query Parameters:

| Parameter    | Values                                                   | Description                  |
| ------------ | -------------------------------------------------------- | ---------------------------- |
| `status`     | queued, in_progress, completed                           | Filter by workflow status    |
| `conclusion` | success, failure, neutral, cancelled, skipped, timed_out | Filter by result             |
| `per_page`   | 1-100                                                    | Items per page (default: 30) |
| `page`       | Integer                                                  | Page number (default: 1)     |
| `branch`     | Branch name                                              | Filter by branch             |
| `actor`      | Username                                                 | Filter by user who triggered |
| `event`      | Event name                                               | Filter by trigger event      |
| `created`    | Date range                                               | Filter by creation date      |

### 4. **Get Workflow Run Details**

**Endpoint**: `GET /repos/{owner}/{repo}/actions/runs/{run_id}`

**What it does**: Get detailed information about a specific workflow run

```bash
curl -H "Authorization: token YOUR_TOKEN" \
  https://api.github.com/repos/octocat/Hello-World/actions/runs/987654
```

#### Complete Python Example

```python
import requests
import json
from datetime import datetime, timedelta

class GitHubWorkflowAPI:
    def __init__(self, owner, repo, token):
        self.owner = owner
        self.repo = repo
        self.token = token
        self.base_url = "https://api.github.com"
        self.headers = {
            "Authorization": f"token {token}",
            "Accept": "application/vnd.github.v3+json"
        }

    def get_workflow_runs(self, status=None, conclusion=None, limit=10):
        """Get workflow runs with optional filtering"""
        url = f"{self.base_url}/repos/{self.owner}/{self.repo}/actions/runs"

        params = {"per_page": limit}
        if status:
            params["status"] = status
        if conclusion:
            params["conclusion"] = conclusion

        response = requests.get(url, headers=self.headers, params=params)
        response.raise_for_status()
        return response.json()["workflow_runs"]

    def get_run_details(self, run_id):
        """Get detailed information about a specific workflow run"""
        url = f"{self.base_url}/repos/{self.owner}/{self.repo}/actions/runs/{run_id}"
        response = requests.get(url, headers=self.headers)
        response.raise_for_status()
        return response.json()

    def cancel_workflow_run(self, run_id):
        """Cancel a running workflow"""
        url = f"{self.base_url}/repos/{self.owner}/{self.repo}/actions/runs/{run_id}/cancel"
        response = requests.post(url, headers=self.headers)
        return response.status_code == 202

    def get_failed_runs_in_last_day(self):
        """Find all failed runs from the last 24 hours"""
        failed_runs = self.get_workflow_runs(status="completed", conclusion="failure", limit=50)
        yesterday = datetime.utcnow() - timedelta(days=1)

        return [
            run for run in failed_runs
            if datetime.fromisoformat(run["updated_at"].replace("Z", "+00:00")) > yesterday
        ]

# Usage
api = GitHubWorkflowAPI("octocat", "Hello-World", "YOUR_TOKEN")

# Get failed runs from last day
failed = api.get_failed_runs_in_last_day()
for run in failed:
    print(f"Run #{run['run_number']}: {run['name']} - {run['conclusion']}")
    print(f"  Started: {run['created_at']}")
    print(f"  URL: {run['html_url']}")
```

### 5. **Trigger Workflow (workflow_dispatch)**

**Endpoint**: `POST /repos/{owner}/{repo}/actions/workflows/{workflow_id}/dispatches`

**What it does**: Manually trigger a workflow run with optional inputs

**Requirements**: Workflow must have `workflow_dispatch` trigger event

```bash
curl -X POST \
  -H "Authorization: token YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "ref": "main",
    "inputs": {
      "environment": "production",
      "version": "1.0.0"
    }
  }' \
  https://api.github.com/repos/octocat/Hello-World/actions/workflows/123456/dispatches
```

#### Workflow with Inputs

```yaml
name: Deployment

on:
  workflow_dispatch:
    inputs:
      environment:
        description: "Environment to deploy to"
        required: true
        type: choice
        options:
          - development
          - staging
          - production
      version:
        description: "Version tag"
        required: true
        type: string

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Deploying ${{ inputs.version }} to ${{ inputs.environment }}"
```

#### Python Example: Trigger Deployment

```python
import requests

def trigger_deployment(owner, repo, workflow_id, environment, version, token):
    url = f"https://api.github.com/repos/{owner}/{repo}/actions/workflows/{workflow_id}/dispatches"

    headers = {
        "Authorization": f"token {token}",
        "Accept": "application/vnd.github.v3+json"
    }

    payload = {
        "ref": "main",
        "inputs": {
            "environment": environment,
            "version": version
        }
    }

    response = requests.post(url, headers=headers, json=payload)

    if response.status_code == 204:
        print(f"✓ Workflow triggered successfully")
        return True
    else:
        print(f"✗ Error: {response.status_code} - {response.text}")
        return False

# Usage
trigger_deployment(
    "octocat",
    "Hello-World",
    "123456",
    "production",
    "v2.0.0",
    "YOUR_TOKEN"
)
```

### 6. **Re-run Workflow**

**Endpoint**: `POST /repos/{owner}/{repo}/actions/runs/{run_id}/rerun`

**What it does**: Re-run a completed workflow

```bash
curl -X POST \
  -H "Authorization: token YOUR_TOKEN" \
  https://api.github.com/repos/octocat/Hello-World/actions/runs/987654/rerun
```

**Response**: `HTTP 201` (Created)

#### Script: Re-run Failed Workflows

```bash
#!/bin/bash

REPO_OWNER="octocat"
REPO_NAME="Hello-World"
GITHUB_TOKEN="YOUR_TOKEN"

# Get failed runs
FAILED_RUNS=$(curl -s -H "Authorization: token $GITHUB_TOKEN" \
  "https://api.github.com/repos/$REPO_OWNER/$REPO_NAME/actions/runs?status=completed&conclusion=failure&per_page=5" | jq '.workflow_runs[].id')

echo "Re-running failed workflows..."
for RUN_ID in $FAILED_RUNS; do
  curl -X POST \
    -H "Authorization: token $GITHUB_TOKEN" \
    https://api.github.com/repos/$REPO_OWNER/$REPO_NAME/actions/runs/$RUN_ID/rerun
  echo "✓ Re-ran run #$RUN_ID"
done
```

### 7. **Cancel Workflow Run**

**Endpoint**: `POST /repos/{owner}/{repo}/actions/runs/{run_id}/cancel`

**What it does**: Cancel a running workflow

```bash
curl -X POST \
  -H "Authorization: token YOUR_TOKEN" \
  https://api.github.com/repos/octocat/Hello-World/actions/runs/987654/cancel
```

### 8. **Delete Workflow Run**

**Endpoint**: `DELETE /repos/{owner}/{repo}/actions/runs/{run_id}`

**What it does**: Delete a workflow run and associated artifacts

```bash
curl -X DELETE \
  -H "Authorization: token YOUR_TOKEN" \
  https://api.github.com/repos/octocat/Hello-World/actions/runs/987654
```

### 9. **List Jobs in a Workflow Run**

**Endpoint**: `GET /repos/{owner}/{repo}/actions/runs/{run_id}/jobs`

**What it does**: Get all jobs within a workflow run

```bash
curl -H "Authorization: token YOUR_TOKEN" \
  https://api.github.com/repos/octocat/Hello-World/actions/runs/987654/jobs
```

**Response**:

```json
{
  "total_count": 2,
  "jobs": [
    {
      "id": 555555,
      "run_id": 987654,
      "workflow_name": "CI",
      "status": "completed",
      "conclusion": "success",
      "name": "build",
      "steps": [
        {
          "name": "Checkout code",
          "status": "completed",
          "conclusion": "success"
        }
      ]
    }
  ]
}
```

### 10. **Get Job Logs**

**Endpoint**: `GET /repos/{owner}/{repo}/actions/jobs/{job_id}/logs`

**What it does**: Download complete job logs

```bash
curl -H "Authorization: token YOUR_TOKEN" \
  https://api.github.com/repos/octocat/Hello-World/actions/jobs/555555/logs \
  > job-logs.txt
```

### 11. **List Artifacts**

**Endpoint**: `GET /repos/{owner}/{repo}/actions/artifacts`

**What it does**: Get all artifacts in a repository

```bash
curl -H "Authorization: token YOUR_TOKEN" \
  "https://api.github.com/repos/octocat/Hello-World/actions/artifacts?per_page=10"
```

**Response**:

```json
{
  "total_count": 5,
  "artifacts": [
    {
      "id": 111111,
      "name": "build-output",
      "size_in_bytes": 2048576,
      "url": "https://api.github.com/repos/octocat/Hello-World/actions/artifacts/111111",
      "archive_download_url": "https://api.github.com/repos/octocat/Hello-World/actions/artifacts/111111/zip",
      "expired": false,
      "created_at": "2024-03-09T10:15:00Z",
      "expires_at": "2024-03-16T10:15:00Z",
      "updated_at": "2024-03-09T10:20:00Z"
    }
  ]
}
```

#### Clean Up Old Artifacts

```bash
#!/bin/bash

REPO="octocat/Hello-World"
TOKEN="YOUR_TOKEN"
THRESHOLD_DAYS=7

# Get all artifacts
ARTIFACTS=$(curl -s -H "Authorization: token $TOKEN" \
  "https://api.github.com/repos/$REPO/actions/artifacts?per_page=100" | jq '.artifacts[]')

echo "$ARTIFACTS" | jq -r '.expires_at' | while read EXPIRY_DATE; do
  ARTIFACT_ID=$(echo "$ARTIFACTS" | jq -r 'select(.expires_at == "'"$EXPIRY_DATE"'") | .id')

  DAYS_UNTIL_EXPIRY=$(( ($(date -d "$EXPIRY_DATE" +%s) - $(date +%s)) / 86400 ))

  if [ $DAYS_UNTIL_EXPIRY -lt $THRESHOLD_DAYS ]; then
    echo "Deleting artifact $ARTIFACT_ID (expires in $DAYS_UNTIL_EXPIRY days)"
    curl -X DELETE -H "Authorization: token $TOKEN" \
      "https://api.github.com/repos/$REPO/actions/artifacts/$ARTIFACT_ID"
  fi
done
```

### 12. **Best Practices for REST API Usage**

#### ✓ Recommended Practices

```bash
# ✓ Always use authentication
curl -H "Authorization: token YOUR_TOKEN" ...

# ✓ Use pagination for large result sets
"per_page=100&page=1"

# ✓ Use descriptive error handling
if [ $? -ne 0 ]; then
  echo "::error::API call failed"
  exit 1
fi

# ✓ Rate limit headers
curl -i -H "Authorization: token YOUR_TOKEN" ... | grep X-RateLimit
# X-RateLimit-Limit: 5000
# X-RateLimit-Remaining: 4999
# X-RateLimit-Reset: 1372700873

# ✓ Store token securely (use environment variables or GitHub Secrets)
export GITHUB_TOKEN=$(cat ~/.github/token)

# ✓ Check API response status codes
if response.status_code == 201:
    print("Created successfully")
elif response.status_code == 204:
    print("Action completed successfully")
elif response.status_code == 404:
    print("Resource not found")
```

#### ✗ Anti-Patterns to Avoid

```bash
# ✗ Never hardcode tokens
curl -H "Authorization: token ghp_1234567890abcdefg" ...  # BAD!

# ✗ Don't ignore rate limits
# API: 5000 requests/hour for authenticated users
# 60 requests/hour for unauthenticated users

# ✗ Don't make duplicate API calls
# Bad: Query same endpoint multiple times
# Good: Cache results or batch requests

# ✗ Don't expose response data with secrets
echo "API Response: $RESPONSE"  # If contains sensitive data!
```

### 13. **Complete Real-World Automation Example**

**Use Case**: Monitor workflows and send alerts for failures

```python
import requests
import json
from datetime import datetime, timedelta
from typing import List, Dict

class WorkflowMonitor:
    def __init__(self, owner: str, repo: str, token: str, webhook_url: str = None):
        self.owner = owner
        self.repo = repo
        self.token = token
        self.webhook_url = webhook_url
        self.base_url = "https://api.github.com"
        self.headers = {
            "Authorization": f"token {token}",
            "Accept": "application/vnd.github.v3+json"
        }

    def get_recent_runs(self, hours: int = 24) -> List[Dict]:
        """Get workflow runs from last N hours"""
        url = f"{self.base_url}/repos/{self.owner}/{self.repo}/actions/runs"
        params = {
            "per_page": 100,
            "status": "completed"
        }

        response = requests.get(url, headers=self.headers, params=params)
        response.raise_for_status()

        cutoff_time = datetime.utcnow() - timedelta(hours=hours)
        runs = response.json()["workflow_runs"]

        return [
            run for run in runs
            if datetime.fromisoformat(run["updated_at"].replace("Z", "+00:00")) > cutoff_time
        ]

    def get_failure_summary(self) -> Dict:
        """Get summary of failed workflows"""
        runs = self.get_recent_runs(hours=24)
        failed = [r for r in runs if r["conclusion"] == "failure"]

        return {
            "total_runs": len(runs),
            "failed_runs": len(failed),
            "success_rate": ((len(runs) - len(failed)) / len(runs) * 100) if runs else 0,
            "failed_workflows": [
                {
                    "run_number": r["run_number"],
                    "workflow_name": r["name"],
                    "branch": r["head_branch"],
                    "actor": r["actor"]["login"],
                    "url": r["html_url"]
                }
                for r in failed
            ]
        }

    def send_alert(self, summary: Dict) -> None:
        """Send alert via webhook (e.g., Slack)"""
        if not self.webhook_url or not summary["failed_runs"]:
            return

        message = f"""🚨 Workflow Alert

Repository: {self.owner}/{self.repo}
Success Rate: {summary['success_rate']:.1f}%
Failed Runs: {summary['failed_runs']}

Failed Workflows:
"""

        for workflow in summary["failed_workflows"]:
            message += f"\n- {workflow['workflow_name']} (run #{workflow['run_number']})\n  Branch: {workflow['branch']}\n  Triggered by: {workflow['actor']}\n  Link: {workflow['url']}"

        payload = {"text": message}
        requests.post(self.webhook_url, json=payload)

    def auto_retry_failed_runs(self, max_retries: int = 3) -> int:
        """Automatically retry failed workflow runs"""
        runs = self.get_recent_runs(hours=1)
        failed = [r for r in runs if r["conclusion"] == "failure"]

        retried_count = 0
        for run in failed:
            url = f"{self.base_url}/repos/{self.owner}/{self.repo}/actions/runs/{run['id']}/rerun"
            response = requests.post(url, headers=self.headers)

            if response.status_code == 201:
                retried_count += 1
                print(f"✓ Retried run #{run['run_number']}")

        return retried_count

# Usage
if __name__ == "__main__":
    monitor = WorkflowMonitor(
        owner="octocat",
        repo="Hello-World",
        token="YOUR_GITHUB_TOKEN",
        webhook_url="https://hooks.slack.com/services/YOUR_WEBHOOK"
    )

    # Get failure summary
    summary = monitor.get_failure_summary()
    print(json.dumps(summary, indent=2))

    # Send alert if failures detected
    monitor.send_alert(summary)

    # Auto-retry failures
    retried = monitor.auto_retry_failed_runs()
    print(f"Auto-retried {retried} workflows")
```

### 14. **API Rate Limits and Quotas**

| Category                 | Limit | Resets     |
| ------------------------ | ----- | ---------- |
| Authenticated Requests   | 5,000 | Per hour   |
| Unauthenticated Requests | 60    | Per hour   |
| Search Queries           | 30    | Per minute |
| Concurrent Requests      | 10    | Per second |

#### Check Rate Limit Status

```bash
curl -H "Authorization: token YOUR_TOKEN" \
  https://api.github.com/rate_limit | jq '.rate_limit'

# Output:
# {
#   "limit": 5000,
#   "remaining": 4999,
#   "reset": 1372700873
# }
```

#### Handle Rate Limit Errors

```python
import time
import requests
from datetime import datetime

def safe_api_call(url, headers, max_retries=3):
    for attempt in range(max_retries):
        response = requests.get(url, headers=headers)

        if response.status_code == 200:
            return response.json()

        elif response.status_code == 403:
            # Rate limited
            reset_time = int(response.headers.get('X-RateLimit-Reset', 0))
            wait_seconds = reset_time - int(time.time())

            if wait_seconds > 0:
                print(f"Rate limited. Waiting {wait_seconds} seconds...")
                time.sleep(wait_seconds + 1)
                continue

        elif response.status_code == 404:
            raise Exception(f"Resource not found: {url}")

        elif response.status_code >= 500:
            # Server error, retry
            if attempt < max_retries - 1:
                wait = 2 ** attempt
                print(f"Server error. Retrying in {wait} seconds...")
                time.sleep(wait)
                continue

        raise Exception(f"API error: {response.status_code} - {response.text}")
```

---

## Reviewing Deployments

### What is Deployment Review?

Deployment review is a process where designated team members must approve deployment actions before they proceed to production or other protected environments. GitHub requires explicit approval from reviewers before a workflow can access a protected environment, enabling governance, compliance, and quality assurance.

### Why Review Deployments?

**Key Benefits:**

1. **Compliance**: Enforce organizational policies and regulatory requirements
2. **Quality Assurance**: Catch issues before they reach production
3. **Risk Mitigation**: Reduce blast radius of failed deployments
4. **Accountability**: Create audit trail of deployment decisions
5. **Knowledge Sharing**: Team members stay informed about changes
6. **Context Review**: Reviewers can check related code changes, test results
7. **Scheduled Deployment**: Deployments can be held until convenient time

### How Deployment Review Works

**Workflow:**

1. Workflow reaches deployment step with protected environment
2. Execution pauses and requests approval from designated reviewers
3. Reviewers can examine job execution logs, code changes, and context
4. Reviewer approves or rejects deployment
5. If approved, deployment proceeds; if rejected, workflow stops

### 1. **Configuring Environment for Review**

**Repository Settings:**

Navigate to: `Settings > Environments > Create environment or select protection rules`

```yaml
# Enable Required Reviewers in GitHub UI:
# 1. Go to Settings > Environments
# 2. Select or create environment (e.g., production)
# 3. Check "Required reviewers"
# 4. Select users/teams who must review deployments
# 5. Optionally set wait timer before deployment
```

**Workflow Configuration:**

```yaml
name: Deploy with Review

on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    environment:
      name: production
      url: https://example.com
    steps:
      - uses: actions/checkout@v3

      - name: Pre-deployment checks
        run: |
          echo "Running pre-deployment checks..."
          npm run test
          npm run lint

      - name: Deploy
        run: npm run deploy
```

### 2. **Review Process**

#### Step 1: Workflow Pauses for Review

When workflow reaches a protected environment step:

```
✓ Checkout
✓ Tests passed
✓ Build successful
⏸ WAITING FOR REVIEW
  Environment: production
  Reviewers needed: 2 from [eng-leads]
```

#### Step 2: Reviewer Examines Deployment

**Reviewer's Perspective:**

```
GitHub Actions > Your Workflow > Review Deployment

Deployment Details:
- Environment: production
- Triggered by: john-dev
- Branch: main
- Commit: abc123def456
- Tests: PASSED
- Build: PASSED

Linked Changes:
- 5 files changed
- 150 additions
- 20 deletions

Review Options:
[✓ Approve]  [✗ Reject]
```

#### Step 3: Reviewer Action

**Approve Deployment:**

```yaml
# Reviewer clicks "Approve"
# Workflow continues immediately

- name: Deploy to Production
  run: |
    npm run deploy:prod
    echo "Deployment successful"
```

**Reject Deployment:**

```yaml
# Reviewer clicks "Reject" with comment: "Test coverage insufficient"
# Workflow stops, deployment does not proceed
# Notification sent to original trigger user
```

### 3. **Complete Deployment Review Workflow**

```yaml
name: Production Deployment with Multi-Stage Review

on:
  push:
    branches: [main]
  workflow_dispatch:
    inputs:
      environment:
        description: "Target environment"
        required: true
        type: choice
        options:
          - staging
          - production

jobs:
  build-and-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Setup Node.js
        uses: actions/setup-node@v3
        with:
          node-version: "18"
          cache: npm

      - name: Install Dependencies
        run: npm ci

      - name: Run Tests
        run: npm test -- --coverage

      - name: Run Lint
        run: npm run lint

      - name: Build Application
        run: npm run build

      - name: Upload Build Artifacts
        uses: actions/upload-artifact@v3
        with:
          name: build-artifacts
          path: dist/
          retention-days: 1

      - name: Create Deployment Summary
        run: |
          cat > deployment-summary.md <<EOF
          # Deployment Summary
          - **Branch**: ${{ github.ref_name }}
          - **Commit**: ${{ github.sha }}
          - **Author**: ${{ github.actor }}
          - **Triggered at**: $(date)
          - **Test Status**: ✅ PASSED
          - **Build Status**: ✅ SUCCESS
          EOF

      - name: Upload Summary
        uses: actions/upload-artifact@v3
        with:
          name: deployment-summary
          path: deployment-summary.md

  deploy-staging:
    needs: build-and-test
    runs-on: ubuntu-latest
    # Staging doesn't require review
    environment:
      name: staging
      url: https://staging.example.com
    steps:
      - uses: actions/checkout@v3

      - name: Download Build Artifacts
        uses: actions/download-artifact@v3
        with:
          name: build-artifacts
          path: ./dist/

      - name: Deploy to Staging
        run: |
          echo "=== Deploying to Staging ==="
          echo "Build artifacts size: $(du -sh dist/)"
          # Deploy script here
          # ./scripts/deploy-staging.sh

      - name: Run Staging Tests
        run: |
          echo "Running integration tests against staging..."
          # npm run test:integration -- --env=staging

      - name: Notify Deployment
        run: |
          echo "✓ Staging deployment successful"
          echo "URL: https://staging.example.com"

  deploy-production:
    needs: deploy-staging
    runs-on: ubuntu-latest
    # Production requires review from DevOps team
    environment:
      name: production
      url: https://example.com
    steps:
      - uses: actions/checkout@v3

      - name: Download Build Artifacts
        uses: actions/download-artifact@v3
        with:
          name: build-artifacts
          path: ./dist/

      - name: Pre-production Checklist
        run: |
          echo "=== Pre-production Checks ==="
          echo "✓ Build artifacts verified"
          echo "✓ Staging tests passed"
          echo "✓ Awaiting production reviewer approval"
          echo "Review environment is set up"

      - name: Deploy to Production
        run: |
          echo "=== DEPLOYING TO PRODUCTION ==="
          echo "Timestamp: $(date)"
          echo "Version: ${{ github.ref_name }}-${{ github.run_number }}"
          # ./scripts/deploy-prod.sh

      - name: Verify Deployment
        run: |
          # Health checks
          echo "Running post-deployment health checks..."
          sleep 5
          echo "✓ Application health: OK"
          echo "✓ API responding: OK"
          echo "✓ Database connected: OK"

      - name: Create Release Annotation
        run: |
          echo "Release deployed to production"
          echo "Commit: ${{ github.sha }}"
          echo "Deployed by: ${{ github.actor }} (with approval)"

      - name: Notify Team
        if: success()
        run: echo "🚀 Production deployment successful!"
```

### 4. **Reviewing Deployment Best Practices**

#### ✓ Recommended Practices

```yaml
# ✓ Require reviewers for production
environment:
  name: production
  url: https://example.com
  # Configured in settings with Required Reviewers

# ✓ Include wait timer for safety
# Settings > Environments > 30-minute wait timer

# ✓ Add clear pre-deployment information
- name: Deployment Information
  run: |
    echo "=== Deployment Details ==="
    echo "Environment: ${{ github.environment }}"
    echo "Triggered by: ${{ github.actor }}"
    echo "Branch: ${{ github.ref_name }}"
    echo "Commit: ${{ github.sha }}"

# ✓ Document purpose of each deployment
- name: Deployment Purpose
  run: |
    cat > DEPLOYMENT_NOTES.md <<EOF
    ## Changes in This Deployment
    - Feature: New user authentication system
    - Breaking changes: API v1 deprecated
    - Rollback plan: Use v2.0.0 tag
    EOF

# ✓ Implement gradual deployments
- name: Canary Deployment
  run: |
    ./deploy.sh --canary --percentage=10
    sleep 300  # Monitor for 5 minutes
    ./deploy.sh --full
```

#### ✗ Anti-Patterns to Avoid

```yaml
# ✗ Don't bypass reviews even in emergency
if: github.actor == 'admin'
  environment: production  # Bad - circumvents review

# ✗ Don't auto-approve without manual check
# Reviews MUST be manual human decisions

# ✗ Don't deploy without collecting metrics
- name: Deploy
  run: ./deploy.sh  # No health checks!

# ✗ Don't ignore wait timers
# Setting 0 wait timer for production is risky
```

### 5. **Monitoring Reviewed Deployments**

```bash
#!/bin/bash

# Get all deployments with review status
curl -H "Authorization: token YOUR_TOKEN" \
  "https://api.github.com/repos/owner/repo/deployments?environment=production" | \
  jq '.[] | {id, status, created_at, creator, environment}'

# Output:
# {
#   "id": 123456,
#   "status": "success",
#   "created_at": "2024-03-09T14:30:00Z",
#   "creator": {"login": "reviewer-name"},
#   "environment": "production"
# }
```

---

## Creating and Publishing Actions

### What are GitHub Actions?

GitHub Actions are reusable units of code that perform specific tasks. You can create custom actions from Docker containers, JavaScript, or composite scripts, then publish them to the GitHub Marketplace or use them privately across repositories.

### Why Create Custom Actions?

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

```
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

**package.json**

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

**README.md Template**

````markdown
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
````

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

````

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
````

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

## Managing Runners

### What are Runners?

Runners are servers that execute jobs in your GitHub Actions workflows. GitHub provides hosted runners (Ubuntu, Windows, macOS) or you can use self-hosted runners for custom environments, specific hardware, or private networks.

### Why Manage Runners?

**Key Benefits:**

1. **Control**: Run workflows on specific hardware or software
2. **Cost**: Self-hosted runners reduce per-minute charges
3. **Privacy**: Keep code on your own infrastructure
4. **Speed**: Local runners eliminate network latency
5. **Customization**: Custom tools, libraries, and configurations
6. **Compliance**: Meet security and regulatory requirements
7. **Capacity**: Scale without GitHub's limitations

### How Runners Work

**Hosted Runners**: GitHub-managed servers with standard operating systems
**Self-Hosted Runners**: Your own servers or machines running the GitHub Actions agent

### 1. **Understanding Hosted Runners**

**Runner Types and Specifications:**

```yaml
jobs:
  # Ubuntu hosted runner (most common)
  ubuntu-job:
    runs-on: ubuntu-latest # or ubuntu-22.04, ubuntu-20.04
    steps:
      - run: echo "Running on Ubuntu"

  # Windows hosted runner
  windows-job:
    runs-on: windows-latest # or windows-2022, windows-2019
    steps:
      - run: echo "Running on Windows"

  # macOS hosted runner
  macos-job:
    runs-on: macos-latest # or macos-12, macos-11
    steps:
      - run: echo "Running on macOS"
```

**Hosted Runner Specifications:**

| Runner         | CPUs | Memory | Storage | Network |
| -------------- | ---- | ------ | ------- | ------- |
| ubuntu-latest  | 2+   | 7 GB   | 14 GB   | 1 Gbps  |
| windows-latest | 2+   | 7 GB   | 14 GB   | 1 Gbps  |
| macos-latest   | 3+   | 14 GB  | 14 GB   | 1 Gbps  |

### 2. **Setting Up Self-Hosted Runners**

**Installation on Linux:**

```bash
#!/bin/bash

# On your server/machine
mkdir actions-runner && cd actions-runner

# Download latest runner
wget https://github.com/actions/runner/releases/download/v2.311.0/actions-runner-linux-x64-2.311.0.tar.gz
tar xzf ./actions-runner-linux-x64-2.311.0.tar.gz

# Configure runner
./config.sh --url https://github.com/owner/repo --token YOUR_REGISTRATION_TOKEN

# Install as service (optional)
sudo ./svc.sh install
sudo ./svc.sh start
```

**The GitHub UI provides specific token and setup instructions:**

```
Repository Settings > Actions > Runners > New self-hosted runner

1. Select Operating System (Linux, Windows, macOS)
2. Select Architecture (x64, ARM64, ARM)
3. Copy and run provided commands
4. Runner automatically registers with your repository
```

### 3. **Using Self-Hosted Runners in Workflows**

```yaml
jobs:
  build:
    # Run on specific self-hosted runner
    runs-on: self-hosted
    steps:
      - uses: actions/checkout@v3
      - run: ./build.sh

  deploy:
    # Use runner with specific label
    runs-on: [self-hosted, linux, x64]
    steps:
      - run: ./deploy.sh

  deploy-special:
    # Run on runner with GPU
    runs-on: [self-hosted, gpu, cuda-12]
    steps:
      - run: python train_model.py # GPU-accelerated
```

### 4. **Managing Self-Hosted Runners via API**

**List Runners:**

```bash
curl -H "Authorization: token YOUR_TOKEN" \
  https://api.github.com/repos/owner/repo/actions/runners
```

**Response:**

```json
{
  "total_count": 2,
  "runners": [
    {
      "id": 1,
      "name": "runner-1",
      "os": "linux",
      "status": "online",
      "busy": false,
      "labels": [
        { "name": "self-hosted" },
        { "name": "gpu" },
        { "name": "linux" }
      ]
    }
  ]
}
```

**Python Script: Runner Management**

```python
import requests

class RunnerManager:
    def __init__(self, owner, repo, token):
        self.owner = owner
        self.repo = repo
        self.token = token
        self.headers = {"Authorization": f"token {token}"}
        self.base_url = "https://api.github.com"

    def list_runners(self):
        """List all self-hosted runners"""
        url = f"{self.base_url}/repos/{self.owner}/{self.repo}/actions/runners"
        response = requests.get(url, headers=self.headers)
        return response.json()["runners"]

    def get_runner_status(self):
        """Get status of all runners"""
        runners = self.list_runners()
        status = {
            "online": 0,
            "offline": 0,
            "busy": 0
        }

        for runner in runners:
            if runner["status"] == "online":
                status["online"] += 1
                if runner["busy"]:
                    status["busy"] += 1
            else:
                status["offline"] += 1

        return status

    def remove_runner(self, runner_id):
        """Remove a self-hosted runner"""
        url = f"{self.base_url}/repos/{self.owner}/{self.repo}/actions/runners/{runner_id}"
        response = requests.delete(url, headers=self.headers)
        return response.status_code == 204

# Usage
manager = RunnerManager("owner", "repo", "YOUR_TOKEN")

# Get status
status = manager.get_runner_status()
print(f"Runners - Online: {status['online']}, Offline: {status['offline']}, Busy: {status['busy']}")

# List all runners
for runner in manager.list_runners():
    print(f"{runner['name']}: {runner['status']} (busy: {runner['busy']})")
```

### 5. **Runner Labels and Organization**

```yaml
name: Pipeline with Runner Selection

on: push

jobs:
  quick-tests:
    runs-on: [self-hosted, linux, fast]
    steps:
      - run: echo "Running on fast runner"

  heavy-build:
    runs-on: [self-hosted, linux, gpu, high-memory]
    steps:
      - run: echo "Running on high-performance runner with GPU"

  mobile-build:
    runs-on: [self-hosted, macos, arm64]
    steps:
      - run: echo "Building for iOS/macOS on Apple Silicon"

  integration-tests:
    runs-on: [self-hosted, docker, docker-in-docker]
    steps:
      - run: docker build -t myapp .
```

### 6. **Scaling and Monitoring Runners**

**Auto-Scaling Setup (Cloud Provider Example):**

```bash
#!/bin/bash

# Script to check runner workload and scale

API_TOKEN="YOUR_TOKEN"
REPO="owner/repo"

# Get runner status
RUNNERS=$(curl -s -H "Authorization: token $API_TOKEN" \
  https://api.github.com/repos/$REPO/actions/runners | jq '.runners')

BUSY_COUNT=$(echo $RUNNERS | jq '[.[] | select(.busy == true)] | length')
ONLINE_COUNT=$(echo $RUNNERS | jq '[.[] | select(.status == "online")] | length')

echo "Runners - Online: $ONLINE_COUNT, Busy: $BUSY_COUNT"

# If more than 80% busy, scale up
BUSY_PERCENT=$(( (BUSY_COUNT * 100) / ONLINE_COUNT ))

if [ $BUSY_PERCENT -gt 80 ]; then
    echo "High load detected ($BUSY_PERCENT% busy). Scaling up..."
    # Launch new runner instance (cloud-specific command)
    # aws ec2 run-instances --image-id ami-xxx --count 1
fi
```

### 7. **Runner Maintenance and Updates**

```bash
#!/bin/bash

# Graceful runner shutdown
# On the runner machine

cd ~/actions-runner

# Stop accepting new jobs
./run.sh --once

# Wait for current jobs to complete
while ps aux | grep -v grep | grep -q Runner.Listener; do
    echo "Waiting for current job to complete..."
    sleep 10
done

# Remove runner from GitHub
./config.sh remove --token YOUR_REMOVAL_TOKEN

# Update runner
wget https://github.com/actions/runner/releases/download/v2.311.0/actions-runner-linux-x64-2.311.0.tar.gz
tar xzf ./actions-runner-linux-x64-2.311.0.tar.gz

# Re-register
./config.sh --url https://github.com/owner/repo --token YOUR_REGISTRATION_TOKEN
```

### 8. **Best Practices for Runner Management**

#### ✓ Recommended Practices

```yaml
# ✓ Use specific runner labels
runs-on: [self-hosted, linux, docker]

# ✓ Tag runners by capability
# Labels: gpu, docker, high-memory, fast

# ✓ Monitor runner health
- name: Check Runner Health
  run: |
    echo "CPU Usage: $(top -bn1 | grep load)"
    echo "Disk Space: $(df -h /)"
    echo "Memory: $(free -h)"

# ✓ Update runners regularly
# Check for new runner agent versions monthly

# ✓ Secure self-hosted runners
# Run privileged jobs in containers
# Limit network access
# Keep OS and tools updated
```

#### ✗ Anti-Patterns to Avoid

```yaml
# ✗ Don't run PRs from untrusted sources on self-hosted runners
runs-on: self-hosted  # Risky for public repositories!

# ✗ Don't store secrets on runner machines
# Use GitHub Secrets instead

# ✗ Don't run without runner labels
runs-on: self-hosted  # Ambiguous which runner

# ✗ Don't ignore runner isolation
# Each job should be isolated
# Don't share state between runs
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
