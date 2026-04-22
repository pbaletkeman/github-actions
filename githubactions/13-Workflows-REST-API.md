[← Back to Index](INDEX.md)

## GitHub Workflows REST API

### What is the GitHub Workflows REST API

The GitHub Workflows REST API is a set of HTTP endpoints provided by GitHub that allow you to programmatically interact with GitHub Actions workflows. Instead of manually triggering workflows or managing them through the web UI, you can use the REST API to automate workflow management from external systems, scripts, or applications.

### Why Use the Workflows REST API

**Key Benefits:**

1. **Automation**: Programmatically trigger and manage workflows without UI interaction
2. **Integration**: Connect external systems (deployment tools, monitoring systems, etc.) with GitHub Actions
3. **Monitoring**: Retrieve workflow execution data for tracking, reporting, and analytics
4. **Control**: Dynamically manage workflow instances (list, view, cancel, re-run)
5. **Developer Experience**: Create custom tooling and dashboards around workflows
6. **Compliance**: Query workflow history for audit and compliance purposes
7. **CI/CD Enhancement**: Build sophisticated automation chains across platforms

**Real-World Applications:**

```text
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

```text
https://api.github.com
```

**Endpoint Format:**

```text
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

#### Example: Get Recent Failed Runs

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

#### Query Parameters

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

#### ✓ Recommended Practices — REST API Usage

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

#### ✗ Anti-Patterns to Avoid — REST API Usage

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

### 13. **API Rate Limits and Quotas**

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



---

## Navigation

- [← Back to Index](INDEX.md)
- [← Previous: 12-Workflow-Debugging](12-Workflow-Debugging.md)
- [Next: 14-Reviewing-Deployments →](14-Reviewing-Deployments.md)
