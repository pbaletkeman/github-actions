[← Back to Index](INDEX.md)

## Common Failures and Troubleshooting

### 1. **Authentication Errors**

#### Problem: Permission Denied

```text
fatal: could not read Username for 'https://github.com': No such file or directory
```

#### Causes — Authentication Errors

- Missing or invalid GitHub token
- SSH key not configured for self-hosted runners
- GITHUB_TOKEN doesn't have sufficient permissions

#### Solutions — Authentication Errors

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

```text
npm ERR! code ERESOLVE
npm ERR! ERESOLVE could not resolve dependencies
```

#### Causes — Dependency Installation

- Node.js version mismatch
- Lock file out of sync with package.json
- Conflicting peer dependencies

#### Solutions — Dependency Installation

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

#### Causes — Job Timeout

- Long-running tests
- Network connectivity issues
- Waiting on external resources
- Infinite loops in workflow logic

#### Solutions — Job Timeout

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

```text
Invalid workflow file at .github/workflows/main.yml: mapping values are not allowed in this context
```

#### Causes — Workflow Syntax Errors

- Invalid YAML syntax
- Incorrect indentation
- Unclosed quotation marks
- Invalid context expressions

#### Solutions — Workflow Syntax Errors

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

```text
The requested image with tag is not available
```

#### Causes — Runner Outdated Software

- Using outdated runner images
- Self-hosted runner issues
- GitHub Hosted runner image update lag

#### Solutions — Runner Outdated Software

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

#### Problem: Self-hosted runner is offline or not picking up jobs

```
No runner is available to run this job; waiting for a self-hosted runner to come online...
```

**Causes:**

- Runner process crashed or was stopped
- Network connectivity lost between runner and GitHub
- Firewall blocking outbound HTTPS to `github.com` / `api.github.com` / `*.actions.githubusercontent.com`
- Runner registration token expired (tokens expire after 1 hour)

**Diagnosis steps:**

```bash
# 1. Check if the runner process is running (on the runner machine)
ps aux | grep Runner.Listener

# 2. Check runner logs (Linux/macOS)
cat ~/actions-runner/_diag/Runner_*.log | tail -100

# 3. Verify network connectivity from the runner
curl -I https://api.github.com      # Should return 200
curl -I https://github.com          # Should return 200

# 4. Check runner registration in the UI
# Repository → Settings → Actions → Runners
# Status will show: Active / Offline / Idle
```

**Resolution:**

```bash
# Restart the runner service (Linux systemd)
sudo systemctl restart actions.runner.<scope>-<name>.service

# Or restart manually
cd ~/actions-runner
./run.sh &

# If registration is broken, re-register with a fresh token
./config.sh remove --token OLD_TOKEN
./config.sh --url https://github.com/owner/repo --token NEW_TOKEN
```

#### Problem: Jobs are queued but no runner picks them up (label mismatch)

```
Waiting for a runner with labels: [self-hosted, gpu, linux]
```

**Cause:** No registered runner has ALL the labels specified in `runs-on:`.

**Diagnosis:**

```bash
# List runners and their labels via API
curl -H "Authorization: Bearer $TOKEN" \
  "https://api.github.com/repos/OWNER/REPO/actions/runners" \
  | jq '.runners[] | {name, labels: [.labels[].name], status}'
```

**Resolution:**

```bash
# Add a missing label to an existing runner via API
curl -X POST \
  -H "Authorization: Bearer $TOKEN" \
  -H "Accept: application/vnd.github+json" \
  "https://api.github.com/repos/OWNER/REPO/actions/runners/RUNNER_ID/labels" \
  -d '{"labels": ["gpu"]}'
```

#### Problem: Self-hosted runner fails with permission errors or environment issues

```
Error: EACCES: permission denied, open '/home/runner/work/_temp/_github_workflow/...'
```

**Causes:**

- Runner is running as root without proper workspace permissions
- Previous job left behind locked files
- Docker socket not accessible

**Resolution:**

```bash
# Clean the workspace manually
rm -rf ~/actions-runner/_work/*

# Run the runner process as a non-root user with proper group permissions
# For Docker socket access, add the runner user to the docker group:
sudo usermod -aG docker runner_user
```

---

### 6. **Artifact and Caching Issues**

#### Problem: Artifact not found when downloading

```
An error occurred when trying to download an artifact using the provided path
```

#### Causes — Artifact Download

- Artifact upload failed silently
- Artifact deleted before download
- Job didn't run (skipped due to `if:` condition)

#### Solutions — Artifact Download

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

#### Causes — Matrix Build

- `fail-fast: true` (default behavior)
- One combination has specific issue

#### Solutions — Matrix Build

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

#### Causes — Secret Management

- Secret name doesn't match
- Secret not added to repository
- Using wrong context syntax
- Scope issues for organization secrets

#### Solutions — Secret Management

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

#### Causes — Step Output

- Step doesn't have an `id` assigned
- Output not properly written to GITHUB_OUTPUT
- Step was skipped

#### Solutions — Step Output

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

#### Causes — Performance

- Jobs running sequentially unnecessarily
- Large dependencies being installed repeatedly
- No caching strategy
- Overly large matrix configurations
- Redundant workflow runs not cancelled when new commits push

#### Solutions — Performance

**Use a fan-out / fan-in pattern to maximize parallel execution:**

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    # ... build once

  test-unit:
    needs: build # starts immediately after build
    runs-on: ubuntu-latest

  test-integration:
    needs: build # also parallel with test-unit
    runs-on: ubuntu-latest

  deploy:
    needs: [test-unit, test-integration] # waits for both to pass
    runs-on: ubuntu-latest
```

#### Solutions: Dependency Caching

**Cache dependencies to skip reinstallation on every run:**

```yaml
- name: Setup Node.js with npm cache
  uses: actions/setup-node@v4
  with:
    node-version: 20
    cache: npm # automatically caches ~/.npm

- name: Setup Python with pip cache
  uses: actions/setup-python@v5
  with:
    python-version: "3.12"
    cache: pip

- name: Manual Maven cache
  uses: actions/cache@v4
  with:
    path: ~/.m2/repository
    key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}
    restore-keys: |
      ${{ runner.os }}-maven-
```

#### Solutions: Matrix Sizing and Concurrency Control

```yaml
strategy:
  matrix:
    node-version: [18, 20, 22]
  max-parallel: 2 # run at most 2 matrix jobs simultaneously
  fail-fast: false # don't cancel other matrix jobs on one failure

# Cancel in-progress runs for the same branch when a new commit pushes
concurrency:
  group: ${{ github.workflow }}-${{ github.ref }}
  cancel-in-progress: true
```

#### Solutions: Identifying Bottlenecks

**Measure step execution time inline:**

```yaml
- name: Timed build
  run: |
    START=$(date +%s)
    npm run build
    END=$(date +%s)
    echo "Build took $((END - START)) seconds"
```

**Query billable timing across runs via the API:**

```bash
# Get per-job billable milliseconds for a specific run
curl -H "Authorization: Bearer $TOKEN" \
  "https://api.github.com/repos/OWNER/REPO/actions/runs/RUN_ID/timing" \
  | jq '.billable'
```

#### Solutions: Cost Optimization

| Strategy                    | Impact                                        |
| --------------------------- | --------------------------------------------- |
| Use `ubuntu-latest` runners | Cheapest GitHub-hosted runner per minute      |
| Cache dependencies          | Fewer minutes spent downloading packages      |
| `cancel-in-progress: true`  | Avoid burning minutes on superseded runs      |
| `paths-ignore:` filters     | Skip CI when only docs or configs change      |
| Minimize matrix dimensions  | Each cell = a separate job = separate billing |
| Self-hosted runners         | No per-minute charge                          |

```yaml
# Skip CI entirely for documentation-only changes
on:
  push:
    branches: [main]
    paths-ignore:
      - "**.md"
      - "docs/**"
      - ".github/ISSUE_TEMPLATE/**"
```

#### Recommended Strategies for Scaling and Optimizing Workflows

When designing workflows for scale, apply these strategies in combination:

#### 1. Maximize job parallelism

```yaml
jobs:
  lint:
    runs-on: ubuntu-latest
    steps: [...]

  test:
    runs-on: ubuntu-latest # Runs in parallel with lint — no needs: dependency
    steps: [...]

  security-scan:
    runs-on: ubuntu-latest # Runs in parallel too
    steps: [...]

  build:
    needs: [lint, test, security-scan] # Waits for all three — creates a fan-in gate
    steps: [...]
```

#### 2. Use concurrency groups to cancel superseded runs

```yaml
concurrency:
  group: ${{ github.workflow }}-${{ github.ref }}
  cancel-in-progress: true # New push supersedes previous run on same branch
```

> Use `cancel-in-progress: false` for deployment workflows where partial runs could cause inconsistent state.

#### 3. Decompose large workflows — split slow jobs into a separate triggered workflow:

```yaml
# Fast CI: runs on every push (< 5 min target)
on: [push, pull_request]
jobs:
  fast-checks: { ... }   # lint, type-check, unit tests

# Slow CI: triggered only when fast-checks pass
on:
  workflow_run:
    workflows: ["Fast CI"]
    types: [completed]
jobs:
  integration-tests:
    if: github.event.workflow_run.conclusion == 'success'
    ...
```

#### 4. Use `paths:` filters to skip unnecessary runs

push:
paths: - "src/**" # Only run when source changes - "tests/**" - "package.json"

```

#### 5. Use self-hosted runners for cost-sensitive workloads

- GitHub-hosted runners charge per minute; self-hosted runners have no per-minute billing
- Use auto-scaling self-hosted runners (e.g., `actions/actions-runner-controller` on Kubernetes) to scale to zero when idle

#### 6. Set timeouts to avoid runaway jobs

```yaml
jobs:
  build:
    timeout-minutes: 30 # Kill job if it exceeds 30 minutes — prevent wasted minutes
    steps:
      - name: Long step
        timeout-minutes: 10 # Step-level timeout
```

#### 7. Summary decision table

| Problem                          | Strategy                                         |
| -------------------------------- | ------------------------------------------------ |
| Workflow takes too long          | Parallelize jobs; split workflow; cache deps     |
| Too many concurrent runs         | Use `concurrency:` with `cancel-in-progress`     |
| High compute cost                | Use `paths-ignore:`, reduce matrix, self-hosted  |
| Flaky/slow integration tests     | Separate into `workflow_run`-triggered workflow  |
| All matrix jobs fail on one bad  | Set `fail-fast: false`; analyze individually     |
| Runners always busy (queue wait) | Add more self-hosted runners or use auto-scaling |

---

### 11. **Docker and Container Issues**

#### Problem: Docker image push fails

```
denied: requested access to the resource is denied
```

#### Causes — Docker Push

- Authentication not configured
- Missing permissions for registry
- Tag format incorrect

#### Solutions — Docker Push

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

#### Causes — Notification Failure

- Webhook URL incorrect or expired
- Step only runs on success
- Missing error handling

#### Solutions — Notification Failure

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


---

## Navigation

- [← Back to Index](INDEX.md)
- [← Previous: 18-Security-and-Optimization](18-Security-and-Optimization.md)
