[← Back to Index](INDEX.md)

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

#### 1. Secret Redaction

Secrets are never available at certain levels to prevent accidental exposure:

- Not available in `uses` (action selection)
- Always redacted in logs if accidentally output

### 2. Limited Context in Dynamic Action Selection

```yaml
# ❌ This will NOT work as expected
- uses: ${{ github.event_name == 'push' && 'actions/checkout@v3' || 'actions/download-artifact@v3' }}

# ✅ Use if condition instead
- uses: actions/checkout@v3
  if: github.event_name == 'push'
```

#### 3. Matrix Context Availability

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

#### 4. Passing Context Between Jobs

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

### 5. Static vs Runtime Expression Evaluation

GitHub Actions evaluates expressions in two phases:

- **Static (parse-time) evaluation** — happens before any runner is assigned. Only a limited set of contexts (`github`, `inputs`, `vars`) are available. Fields like `runs-on:`, `uses:`, `if:` at the job level, and `strategy.matrix` are evaluated statically.
- **Runtime evaluation** — happens on the runner during job execution. Full context access (`env`, `secrets`, `job`, `runner`, `steps`, `matrix`, `needs`, `strategy`) is available in `run:`, `with:`, step-level `if:`, and `env:` blocks.

```yaml
# ❌ FAILS — 'env' context is not available at static parse time for 'runs-on'
jobs:
  build:
    runs-on: ${{ env.RUNNER_LABEL }}  # parse-time: only github/inputs/vars allowed

# ✅ WORKS — use 'vars' context (available at parse time) or a literal value
jobs:
  build:
    runs-on: ${{ vars.RUNNER_LABEL }}  # vars is evaluated statically

# ✅ WORKS — secrets available in 'run:' (runtime) but NOT in 'uses:' (static)
steps:
  - uses: actions/checkout@v4   # 'secrets' NOT available here
  - run: echo "$MY_TOKEN"       # 'secrets' IS available here (passed via env)
    env:
      MY_TOKEN: ${{ secrets.GH_TOKEN }}
```

### 6. Secret Leakage Prevention in Expressions

Secrets are automatically masked in logs, but certain patterns can still expose them:

```yaml
# ❌ DANGEROUS — interpolating secrets directly into 'run:' embeds the value
# in the shell command line, which can appear in process lists or debug output
- run: curl -H "Authorization: Bearer ${{ secrets.API_TOKEN }}" https://api.example.com

# ✅ SAFE — pass secrets via environment variables (the value is passed out of
# the rendered script and into the process environment by the runner agent)
- run: curl -H "Authorization: Bearer $API_TOKEN" https://api.example.com
  env:
    API_TOKEN: ${{ secrets.API_TOKEN }}

# ❌ DANGEROUS — toJSON(secrets) prints ALL secret names and values as JSON
- run: echo '${{ toJSON(secrets) }}'

# ❌ DANGEROUS — using add-mask to re-mask a secret that leaked into a variable
# is a workaround, not a substitute for proper env-var passing
```

**Key rules:**

- Never write `${{ secrets.X }}` inside a `run:` block — always map to an `env:` variable first
- Never use `toJSON(secrets)` in any output or log step
- Use `::add-mask::` only as a last resort to suppress accidental exposures; it does not prevent the value from being passed to processes
- Secrets are automatically redacted from logs but not from external HTTP requests made by your scripts

---



---

## Navigation

- [← Back to Index](INDEX.md)
- [← Previous: 02-Contextual-Information](02-Contextual-Information.md)
- [Next: 04-Workflow-File-Structure →](04-Workflow-File-Structure.md)
