[← Back to Index](INDEX.md)

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

#### Common Trigger Events

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

#### Job Properties

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

**Matrix with `include` and `exclude`:**

`include` adds extra combinations or extends existing ones. `exclude` removes specific combinations from the matrix.

```yaml
jobs:
  build:
    strategy:
      fail-fast: false
      max-parallel: 4
      matrix:
        os: [ubuntu-latest, windows-latest, macos-latest]
        node: [18, 20]
        # exclude an unsupported combination
        exclude:
          - os: windows-latest
            node: 18
        # include an extra combination with additional variables
        include:
          - os: ubuntu-latest
            node: 22
            experimental: true
          - os: macos-latest
            node: 20
            extra-flag: "--arm64"
    runs-on: ${{ matrix.os }}
    continue-on-error: ${{ matrix.experimental == true }}
    steps:
      - uses: actions/checkout@v3
      - run: echo "Node ${{ matrix.node }} on ${{ matrix.os }}"
```

**`fail-fast`** (default `true`): When `true`, GitHub cancels all in-progress matrix jobs if any job fails. Set to `false` to let all combinations run regardless.

**`max-parallel`**: Limits how many matrix jobs run concurrently. Reduces resource usage and cost.

**Selectively Re-running Individual Matrix Jobs:**

In the GitHub UI, after a workflow run with matrix jobs, you can re-run specific failed jobs instead of the entire matrix:

- Navigate to the workflow run
- Click the failed job
- Click **Re-run jobs > Re-run failed jobs** (re-runs only the failed matrix variants)

```yaml
# Matrix with OS-specific runner image notes:
strategy:
  matrix:
    os:
      # ubuntu-20.04 is deprecated; migrate to ubuntu-22.04 or ubuntu-latest
      - ubuntu-22.04
      # windows-latest points to Windows Server 2025 as of 2025
      - windows-latest
      - macos-latest
```

**Dynamic Matrix from JSON:**

```yaml
jobs:
  setup:
    runs-on: ubuntu-latest
    outputs:
      matrix: ${{ steps.set-matrix.outputs.matrix }}
    steps:
      - id: set-matrix
        run: |
          echo 'matrix={"include":[{"project":"foo","config":"a"},{"project":"bar","config":"b"}]}' >> $GITHUB_OUTPUT

  build:
    needs: setup
    runs-on: ubuntu-latest
    strategy:
      matrix: ${{ fromJSON(needs.setup.outputs.matrix) }}
    steps:
      - run: echo "Building ${{ matrix.project }} with config ${{ matrix.config }}"
```

**Matrix Cost Optimization and Deprecation Migration:**

Matrix builds multiply the number of jobs, increasing costs. For example, a 3×4 matrix (3 OS × 4 Node versions) = 12 parallel jobs.

**Cost Estimation:**

```yaml
# ❌ EXPENSIVE: 3 OS × 4 Node × 2 architectures = 24 jobs
strategy:
  matrix:
    os: [ubuntu-latest, windows-latest, macos-latest]
    node: [18, 19, 20, 21]
    arch: [x64, arm64]

# ✅ OPTIMIZED: ~8 jobs (exclude expensive combinations, reduce scope)
strategy:
  fail-fast: false
  max-parallel: 3  # Limit parallel jobs to reduce per-run cost
  matrix:
    include:
      # Test critical combinations only
      - os: ubuntu-latest
        node: 18
      - os: ubuntu-latest
        node: 21
      - os: windows-latest
        node: 20
      - os: macos-latest
        node: 20
      # Nightly extended matrix (use schedule trigger)
```

**GitHub-Hosted Runner Costs (approximate, 2026):**

- `ubuntu-latest`: Base cost (included in Actions minutes)
- `windows-latest`: 2x cost multiplier
- `macos-latest`: 10x cost multiplier

**Strategy examples by cost sensitivity:**

```yaml
# For open-source projects (cost-sensitive)
strategy:
  matrix:
    node: [18, 21]  # Only LTS + latest versions
    # Skip rare platforms, just test on ubuntu-latest
runs-on: ubuntu-latest

# For paid organizations (comprehensive testing)
strategy:
  fail-fast: false
  matrix:
    os: [ubuntu-latest, windows-latest, macos-latest]
    node: [18, 19, 20, 21]

# For enterprise (selective by branch)
strategy:
  matrix:
    include:
      # PR checks: minimal testing
      - { os: ubuntu-latest, node: 20 }
      # Main branch: comprehensive testing
      - { os: ubuntu-latest, node: 18 }
      - { os: ubuntu-latest, node: 21 }
      - { os: windows-latest, node: 20 }
      - { os: macos-latest, node: 20 }
conditionally_include: ${{ github.event_name == 'push' && github.ref == 'refs/heads/main' }}
```

**Runner Image Deprecation: Ubuntu 20.04 and windows-latest:**

**Ubuntu 20.04 Deprecation (as of March 2024):**

- `ubuntu-20.04` is no longer supported on GitHub-hosted runners
- **Action required**: Migrate workflows to `ubuntu-22.04` or `ubuntu-latest` (currently 24.04)

```yaml
# ❌ WILL FAIL
jobs:
  build:
    runs-on: ubuntu-20.04

# ✅ MIGRATE TO
jobs:
  build:
    runs-on: ubuntu-22.04  # or ubuntu-latest
```

**windows-latest Transition (Windows Server 2025):**

- As of March 2025, `windows-latest` points to Windows Server 2025
- `windows-2022` remains available
- Test for compatibility:
  - PowerShell version changes (7.4+ vs 7.1)
  - .NET runtime differences
  - Build tool versions

```yaml
strategy:
  matrix:
    os:
      - ubuntu-latest # Currently 24.04
      - windows-2022 # Stable, known versions
      - windows-latest # Windows Server 2025 (test for compat)
      - macos-latest # Currently macOS 14
```

**Deprecation Action Checklist:**

- [ ] Audit all workflows for `ubuntu-20.04` references
- [ ] Replace with `ubuntu-22.04` or `ubuntu-latest`
- [ ] Test `windows-latest` against Windows Server 2025 breaking changes
- [ ] Pin specific runner versions for production workflows: `ubuntu-22.04`, `windows-2022`, `macos-13`
- [ ] Update CI/CD docs with current runner image versions

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

#### Step Properties

##### **name** — Step Property

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

##### **if** — Step Condition

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

Service containers run additional Docker containers alongside job steps, providing dependent services like databases, caches, and message queues. They are started before job steps and stopped after, and are accessible via `localhost` or by their service ID as hostname.

**Full Service Container Configuration Options:**

| Option        | Description                                            |
| ------------- | ------------------------------------------------------ |
| `image`       | Docker image for the container                         |
| `env`         | Environment variables for the container                |
| `ports`       | Port mappings to expose (`host:container`)             |
| `options`     | Docker `--option` flags (health checks, volumes, etc.) |
| `credentials` | Credentials for private container registries           |
| `volumes`     | Volume mounts                                          |

**Single Service (PostgreSQL):**

```yaml
jobs:
  test:
    runs-on: ubuntu-latest
    services:
      postgres:
        image: postgres:15
        env:
          POSTGRES_DB: testdb
          POSTGRES_USER: testuser
          POSTGRES_PASSWORD: testpassword
        ports:
          - 5432:5432
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5
    steps:
      - uses: actions/checkout@v3
      - name: Run Tests Against PostgreSQL
        run: npm test
        env:
          DATABASE_URL: postgresql://testuser:testpassword@localhost:5432/testdb
```

**Multiple Services (Database + Redis):**

```yaml
jobs:
  integration-test:
    runs-on: ubuntu-latest
    services:
      postgres:
        image: postgres:15
        env:
          POSTGRES_PASSWORD: secret
        ports:
          - 5432:5432
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5

      redis:
        image: redis:7
        ports:
          - 6379:6379
        options: >-
          --health-cmd "redis-cli ping"
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5

      rabbitmq:
        image: rabbitmq:3-management
        env:
          RABBITMQ_DEFAULT_USER: user
          RABBITMQ_DEFAULT_PASS: password
        ports:
          - 5672:5672
          - 15672:15672
    steps:
      - uses: actions/checkout@v3
      - name: Wait for services to be ready
        run: |
          # Services health checks handle readiness automatically
          echo "All services ready"
      - name: Run Integration Tests
        run: pytest tests/integration/
        env:
          DATABASE_URL: postgresql://postgres:secret@localhost:5432/postgres
          REDIS_URL: redis://localhost:6379
          AMQP_URL: amqp://user:password@localhost:5672
```

**Service Containers in Job Containers (container: + services:):**

```yaml
jobs:
  test:
    runs-on: ubuntu-latest
    # When using a job container, use service ID (not localhost) as hostname
    container:
      image: node:18
    services:
      mysql:
        image: mysql:8
        env:
          MYSQL_ROOT_PASSWORD: rootpass
          MYSQL_DATABASE: testdb
        options: >-
          --health-cmd "mysqladmin ping -h localhost"
          --health-interval 10s
          --health-timeout 5s
          --health-retries 10
        ports:
          - 3306:3306
    steps:
      - uses: actions/checkout@v3
      - name: Test with MySQL
        run: npm test
        env:
          # Use service ID as hostname when job runs in container
          DATABASE_HOST: mysql
          DATABASE_PORT: 3306
          DATABASE_NAME: testdb
          DATABASE_USER: root
          DATABASE_PASSWORD: rootpass
```

> **Note:** When your job runs directly on the runner (`runs-on` without `container:`), services are accessible via `localhost`. When your job runs inside a container (`container:`), services are accessible via the **service ID** as the hostname (e.g., `mysql`, `postgres`).

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

### 12. **YAML Anchors and Aliases**

YAML anchors (`&`) and aliases (`*`) allow you to reuse YAML content, keeping workflows DRY. GitHub Actions uses standard YAML parsing, so anchors are resolved before the workflow engine processes the file.

**Syntax:**

| Symbol      | Role                                                                           |
| ----------- | ------------------------------------------------------------------------------ |
| `&name`     | Defines an anchor called `name` at the marked node                             |
| `*name`     | References (aliases) the anchor — replaces the alias with the anchored content |
| `<<: *name` | Merge key — merges the anchor's mapping into the current mapping               |

**Basic anchor and alias:**

```yaml
# Shared environment block
x-common-env: &common-env
  NODE_ENV: production
  LOG_LEVEL: info
  REGION: us-east-1

jobs:
  build:
    runs-on: ubuntu-latest
    env:
      <<: *common-env # merges NODE_ENV, LOG_LEVEL, REGION into this job
      APP_VERSION: "2.1.0"
    steps:
      - run: echo "Building $NODE_ENV app"

  deploy:
    runs-on: ubuntu-latest
    env:
      <<: *common-env # same shared env
      DEPLOY_TARGET: production
    steps:
      - run: echo "Deploying to $REGION"
```

**Reusing step definitions:**

```yaml
x-checkout-step: &checkout
  name: Checkout code
  uses: actions/checkout@v4
  with:
    fetch-depth: 0

x-setup-node: &setup-node
  name: Set up Node.js
  uses: actions/setup-node@v4
  with:
    node-version: "20"
    cache: "npm"

jobs:
  lint:
    runs-on: ubuntu-latest
    steps:
      - <<: *checkout
      - <<: *setup-node
      - run: npm run lint

  test:
    runs-on: ubuntu-latest
    steps:
      - <<: *checkout
      - <<: *setup-node
      - run: npm test
```

**Anchors in matrix strategy:**

```yaml
x-default-strategy: &default-strategy
  fail-fast: false
  max-parallel: 3

jobs:
  unit-tests:
    strategy:
      <<: *default-strategy
      matrix:
        node: [18, 20]
    runs-on: ubuntu-latest
    steps:
      - run: node --version

  integration-tests:
    strategy:
      <<: *default-strategy
      matrix:
        db: [postgres, mysql]
    runs-on: ubuntu-latest
    steps:
      - run: echo "Testing with ${{ matrix.db }}"
```

**Important notes:**

- Top-level YAML keys beginning with `x-` are ignored by GitHub Actions — use them as a convention for anchor definitions to avoid validation errors
- Anchors are resolved by the YAML parser; the **workflow logs show the resolved values**, not anchor names
- Anchors cannot span across files — they only work within the same `.yml` file
- The `<<` merge key merges all key–value pairs from the referenced mapping; existing keys in the current mapping take precedence

**Reading and interpreting YAML anchors (consumer perspective):**

When reviewing a workflow that uses anchors, mentally expand `<<: *anchor-name` by substituting the anchor's key–value pairs into the current mapping. Keys already defined in the current block override those from the anchor.

```yaml
# Before anchor expansion (what you read in the source file)
x-common-env: &common-env
  NODE_ENV: production
  LOG_LEVEL: info

jobs:
  build:
    env:
      <<: *common-env    # ← expands to NODE_ENV + LOG_LEVEL
      APP_VERSION: "2.1.0"

# After anchor expansion (what the runner actually sees)
jobs:
  build:
    env:
      NODE_ENV: production    # from anchor
      LOG_LEVEL: info         # from anchor
      APP_VERSION: "2.1.0"   # defined locally, not overridden
```

The **workflow run logs** always show the fully resolved values — you will never see `*common-env` or `<<:` in the expanded YAML that GitHub Actions processes. This means:

- Log output and GitHub's workflow visualization show concrete values, not anchor names
- If a local key conflicts with an anchor key, the **local key wins** (anchors cannot override explicit values)
- If you see unexpected environment variables in a workflow, check anchor definitions at the top (`x-` prefixed keys) of the file

---

### 13. **Complete Workflow Example**

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



---

## Navigation

- [← Back to Index](INDEX.md)
- [← Previous: 03-Context-Availability-Reference](03-Context-Availability-Reference.md)
- [Next: 05-Workflow-Trigger-Events →](05-Workflow-Trigger-Events.md)
