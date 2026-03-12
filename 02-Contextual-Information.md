[← Back to Index](INDEX.md)

## Contextual Information in GitHub Workflows

GitHub provides rich contextual information through **contexts** that you can use in your workflows. These contexts contain information about the workflow run, the trigger event, the job, and the runner.

### 1. **github Context**

The `github` context contains information about the workflow run and the event that triggered it.

#### Key Variables in `github` Context

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

#### Example Usage

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

#### Example

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

#### Example — Runner Information

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

### 10. **strategy Context**

The `strategy` context contains information about the matrix execution strategy for the current job. It is available inside any job that defines a `strategy:` block.

| Property                | Type    | Description                                                           |
| ----------------------- | ------- | --------------------------------------------------------------------- |
| `strategy.fail-fast`    | boolean | Whether the workflow cancels remaining matrix jobs when any job fails |
| `strategy.job-index`    | number  | The 0-based index of the current job in the full set of matrix jobs   |
| `strategy.job-total`    | number  | The total number of jobs in the matrix                                |
| `strategy.max-parallel` | number  | The maximum number of simultaneous matrix jobs allowed                |

```yaml
name: Strategy Context Example

on: [push]

jobs:
  build:
    runs-on: ubuntu-latest
    strategy:
      fail-fast: false
      max-parallel: 2
      matrix:
        os: [ubuntu-latest, windows-latest, macos-latest]
        node: [18, 20]
    steps:
      - name: Print Strategy Context
        run: |
          echo "Job index:    ${{ strategy.job-index }}"
          echo "Total jobs:   ${{ strategy.job-total }}"
          echo "Fail-fast:    ${{ strategy.fail-fast }}"
          echo "Max parallel: ${{ strategy.max-parallel }}"

      - name: Only on first matrix job
        if: strategy.job-index == 0
        run: echo "This step only runs on the first matrix combination"
```

**Key use-cases:**

- `strategy.job-index == 0` — run a setup or notification step only once across the entire matrix
- `strategy.job-total` — display progress ("job 2 of 6")
- `strategy.fail-fast` — surface the effective fail-fast setting in logs for debugging

---



---

## Navigation

- [← Back to Index](INDEX.md)
- [← Previous: 01-GitHub-Actions-VS-Code-Extension](01-GitHub-Actions-VS-Code-Extension.md)
- [Next: 03-Context-Availability-Reference →](03-Context-Availability-Reference.md)
