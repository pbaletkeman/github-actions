[← Back to Index](INDEX.md)

## Reviewing Deployments

### What is Deployment Review

Deployment review is a process where designated team members must approve deployment actions before they proceed to production or other protected environments. GitHub requires explicit approval from reviewers before a workflow can access a protected environment, enabling governance, compliance, and quality assurance.

### Why Review Deployments

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

```text
✓ Checkout
✓ Tests passed
✓ Build successful
⏸ WAITING FOR REVIEW
  Environment: production
  Reviewers needed: 2 from [eng-leads]
```

#### Step 2: Reviewer Examines Deployment

**Reviewer's Perspective:**

```text
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

#### ✓ Recommended Practices — Deployment Review

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

#### ✗ Anti-Patterns to Avoid — Deployment Review

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



---

## Navigation

- [← Back to Index](INDEX.md)
- [← Previous: 13-Workflows-REST-API](13-Workflows-REST-API.md)
- [Next: 15-Creating-Publishing-Actions →](15-Creating-Publishing-Actions.md)
