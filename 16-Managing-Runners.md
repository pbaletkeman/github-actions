[← Back to Index](INDEX.md)

## Managing Runners

### What are Runners

Runners are servers that execute jobs in your GitHub Actions workflows. GitHub provides hosted runners (Ubuntu, Windows, macOS) or you can use self-hosted runners for custom environments, specific hardware, or private networks.

### Why Manage Runners

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

```text
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

**Monitoring runner health and status:**

Use the GitHub REST API to monitor runner availability, workload, and online/offline status programmatically.

```bash
# Runner status at repository level
curl -H "Authorization: Bearer $TOKEN" \
  "https://api.github.com/repos/OWNER/REPO/actions/runners" \
  | jq '.runners[] | {id, name, os, status, busy, labels: [.labels[].name]}'

# Runner status at organization level
curl -H "Authorization: Bearer $TOKEN" \
  "https://api.github.com/orgs/ORG/actions/runners" \
  | jq '[.runners[] | {name, status, busy}]'
```

**Key status fields:**

| Field    | Values               | Meaning                                           |
| -------- | -------------------- | ------------------------------------------------- |
| `status` | `online` / `offline` | Whether the runner process is reachable by GitHub |
| `busy`   | `true` / `false`     | Whether the runner is currently executing a job   |

**Detecting queue depth (no idle runners):**

```bash
#!/bin/bash
RUNNERS=$(curl -s -H "Authorization: Bearer $TOKEN" \
  "https://api.github.com/orgs/ORG/actions/runners" | jq '.runners')

ONLINE=$(echo "$RUNNERS" | jq '[.[] | select(.status=="online")] | length')
BUSY=$(echo "$RUNNERS" | jq '[.[] | select(.busy==true)] | length')
IDLE=$(( ONLINE - BUSY ))

echo "Online: $ONLINE | Busy: $BUSY | Idle: $IDLE"
if [ "$IDLE" -eq 0 ] && [ "$ONLINE" -gt 0 ]; then
  echo "⚠️  All runners busy — jobs may queue. Consider scaling up."
fi
```

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



---

## Navigation

- [← Back to Index](INDEX.md)
- [← Previous: 15-Creating-Publishing-Actions](15-Creating-Publishing-Actions.md)
- [Next: 17-GitHub-Actions-Enterprise →](17-GitHub-Actions-Enterprise.md)
