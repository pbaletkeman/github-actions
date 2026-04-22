[← Back to Index](INDEX.md)

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

#### GITHUB_STEP_SUMMARY — Job Summary Reports

`GITHUB_STEP_SUMMARY` points to a per-job Markdown file. Content you write to it appears in the **Summary** tab of a workflow run, making it easy to surface test results, coverage, and other reports without downloading artifacts.

```yaml
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Run tests
        id: tests
        run: |
          npm test --reporter json > test-results.json
          echo "PASS_COUNT=$(jq '.numPassedTests' test-results.json)" >> $GITHUB_ENV
          echo "FAIL_COUNT=$(jq '.numFailedTests' test-results.json)" >> $GITHUB_ENV

      - name: Write job summary
        if: always() # run even if tests fail so summary is always produced
        run: |
          echo "## Test Results :test_tube:" >> $GITHUB_STEP_SUMMARY
          echo "" >> $GITHUB_STEP_SUMMARY
          echo "| Status | Count |" >> $GITHUB_STEP_SUMMARY
          echo "|--------|-------|" >> $GITHUB_STEP_SUMMARY
          echo "| ✅ Passed | $PASS_COUNT |" >> $GITHUB_STEP_SUMMARY
          echo "| ❌ Failed | $FAIL_COUNT |" >> $GITHUB_STEP_SUMMARY
          echo "" >> $GITHUB_STEP_SUMMARY
          if [ "$FAIL_COUNT" -gt 0 ]; then
            echo "> [!WARNING]" >> $GITHUB_STEP_SUMMARY
            echo "> $FAIL_COUNT test(s) failed. Check the logs above for details." >> $GITHUB_STEP_SUMMARY
          fi
```

**Multi-step summaries** accumulate — each step appends to the same file:

```yaml
steps:
  - name: Build summary
    run: |
      echo "## Build :hammer:" >> $GITHUB_STEP_SUMMARY
      echo "Built from \`${{ github.ref_name }}\` @ \`${{ github.sha }}\`" >> $GITHUB_STEP_SUMMARY

  - name: Deploy summary
    run: |
      echo "## Deployment :rocket:" >> $GITHUB_STEP_SUMMARY
      echo "Deployed to **production** at $(date -u)" >> $GITHUB_STEP_SUMMARY
      echo "[View deployment](${{ vars.PROD_URL }})" >> $GITHUB_STEP_SUMMARY
```

**Key rules:**

- Use `>> $GITHUB_STEP_SUMMARY` (append) not `>` (overwrite)
- Supports standard GitHub-flavored Markdown including tables, task lists, alerts, and collapsible sections
- Summary is per-job — each job has its own summary tab
- The file is automatically collected and shown on the workflow run Summary page

#### RUNNER_TOOL_CACHE — Preinstalled Software

`RUNNER_TOOL_CACHE` points to the directory where GitHub-hosted runner images pre-install language runtimes and tools (the "tool cache"). `setup-*` actions install into or read from this path.

```yaml
- name: List preinstalled tool cache contents
  run: ls $RUNNER_TOOL_CACHE
  # Typical output: Python/ node/ go/ java/...
```

GitHub publishes the preinstalled software lists for each runner image at:
`https://github.com/actions/runner-images` (see the `images/` subdirectory for each OS)

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



---

## Navigation

- [← Back to Index](INDEX.md)
- [← Previous: 05-Workflow-Trigger-Events](05-Workflow-Trigger-Events.md)
- [Next: 07-Default-Environment-Variables →](07-Default-Environment-Variables.md)
