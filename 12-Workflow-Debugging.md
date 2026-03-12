[← Back to Index](INDEX.md)

## Workflow Debugging

### What is Workflow Debugging

Workflow debugging is the process of identifying and fixing issues in GitHub Actions workflows. It involves understanding why workflows fail, examining logs, adding diagnostic output, and validating configurations. Debugging techniques range from simple log inspection to advanced tracing and performance analysis.

### Why Debug Workflows

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

```text
1. Navigate to Repository → Actions tab
2. Click on specific workflow run
3. View logs for each job and step
4. Click on individual steps to expand logs
```

**Log Levels:**

```text
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

#### Scenario 4: Matrix Selective Reruns

When a matrix job fails, you may want to rerun only the specific failing combinations instead of all matrix jobs.

**Understanding Matrix Job Names:**

Matrix jobs are labelled by their axes combination. For a matrix with `os: [ubuntu, windows]` and `node: [18, 20]`:

```yaml
strategy:
  matrix:
    os: [ubuntu-latest, windows-latest]
    node: [18, 20]

jobs:
  test:
    runs-on: ${{ matrix.os }}
    steps:
      - run: npm test
```

Generated job names will be:

- `test (ubuntu-latest, 18)`
- `test (ubuntu-latest, 20)`
- `test (windows-latest, 18)`
- `test (windows-latest, 20)`

**Selective Rerun via UI:**

```text
1. Go to "Actions" tab
2. Click workflow run that had failures
3. Under "Jobs" section, each matrix combination is listed separately
4. Click "Re-run jobs" → "Re-run failed jobs" only reruns failed combinations
5. Or click "..." on specific job → "Re-run job [combination]"
```

**Selective Rerun via GitHub CLI:**

```bash
# Get workflow run ID
gh run list --workflow=test.yml --limit=5

# Rerun only failed jobs (of the entire matrix)
gh run rerun RUNID --failed

# Rerun a specific job (need job ID from workflow)
gh run rerun RUNID --job JOBID
```

**Matrix Selective Rerun Example Workflow:**

```yaml
name: Multi-Platform Tests

on:
  push:
    branches: [main]
  workflow_dispatch:

jobs:
  test:
    name: test (${{ matrix.os }}, Node ${{ matrix.node }})
    runs-on: ${{ matrix.os }}
    strategy:
      fail-fast: false # Let all matrix jobs run even if one fails
      matrix:
        os: [ubuntu-latest, windows-latest, macos-latest]
        node: [18, 20, 21]
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with:
          node-version: ${{ matrix.node }}

      - name: Run Tests
        run: npm test

      - name: Generate Report
        if: failure()
        run: |
          echo "Test failed on ${{ matrix.os }} with Node ${{ matrix.node }}"
          # Save failure details for reference
          echo "OS: ${{ matrix.os }}" >> test-report.txt
          echo "Node: ${{ matrix.node }}" >> test-report.txt

      - name: Upload Test Report
        if: failure()
        uses: actions/upload-artifact@v4
        with:
          name: test-report-${{ matrix.os }}-node${{ matrix.node }}
          path: test-report.txt
```

**REST API for Selective Rerun:**

```bash
# Rerun all failed jobs in a workflow run
curl -X POST \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/runs/RUNID/rerun-failed-jobs

# Get all jobs in a workflow run (to find job IDs)
curl -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/runs/RUNID/jobs \
  | jq '.jobs[] | {id, name, status, conclusion}'

# Rerun a specific job
curl -X POST \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/jobs/JOBID/rerun
```

**Best Practices for Matrix Debugging:**

1. **Use descriptive job names** - Include matrix values in the job name for clarity
2. **Set `fail-fast: false`** - Let all combinations run to see all failures at once
3. **Use artifacts for debug info** - Upload logs specific to each matrix combination
4. **Group output** - Use `::group::` to organize per-combination logs
5. **Conditional debugging steps** - Add debug steps only when tests fail

```yaml
jobs:
  test:
    name: test (${{ matrix.os }}, ${{ matrix.node }})
    runs-on: ${{ matrix.os }}
    strategy:
      fail-fast: false
      matrix:
        os: [ubuntu-latest, windows-latest]
        node: [18, 20]
    steps:
      - run: npm test

      - name: Debug Info (on failure)
        if: failure()
        run: |
          echo "::group::Failure Details"
          echo "Platform: ${{ matrix.os }}"
          echo "Node Version: ${{ matrix.node }}"
          npm --version
          npm ls
          echo "::endgroup::"
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

#### ✓ Recommended Practices — Debugging

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

#### ✗ Anti-Patterns to Avoid — Runner Debugging

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



---

## Navigation

- [← Back to Index](INDEX.md)
- [← Previous: 11-Workflow-Sharing](11-Workflow-Sharing.md)
- [Next: 13-Workflows-REST-API →](13-Workflows-REST-API.md)
