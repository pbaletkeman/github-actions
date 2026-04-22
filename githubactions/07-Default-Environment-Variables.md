[← Back to Index](INDEX.md)

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

```text
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

#### Output Example — Context Variables

```text
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

```text
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

```text
Runner OS: Linux
Runner Architecture: X64
Runner Name: GitHub Actions 1
```

**On Windows:**

```text
Runner OS: Windows
Runner Architecture: X64
Runner Name: GitHub Actions 2
```

**On macOS:**

```text
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

#### Output Example — GitHub API URLs

```text
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



---

## Navigation

- [← Back to Index](INDEX.md)
- [← Previous: 06-Custom-Environment-Variables](06-Custom-Environment-Variables.md)
- [Next: 08-Environment-Protection-Rules →](08-Environment-Protection-Rules.md)
