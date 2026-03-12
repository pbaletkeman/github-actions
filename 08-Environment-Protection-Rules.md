[← Back to Index](INDEX.md)

## Environment Protection Rules

Environment protection rules are powerful GitHub security features that allow you to control how and when workflows can access and deploy to specific environments. They help ensure that sensitive deployments require proper authorization, reviews, and validation before proceeding.

### Overview of Environment Protection Rules

Environment protection rules provide several key benefits:

- **Controlled Access**: Restrict who can deploy to production or other protected environments
- **Review Requirements**: Mandate that deployments require approval before execution
- **Branch Protection**: Specify which branches are allowed to deploy to an environment
- **Deployment Timing**: Control when deployments can occur
- **Audit Trail**: Track all deployment approvals and denials

### 1. **Required Reviewers**

Require one or more team members to approve deployments before they proceed.

#### Configuration in Repository Settings

Navigate to: `Settings > Environments > [Environment Name] > Deployment branches and reviewers`

Enable "Required reviewers" and select the GitHub users or teams who must approve deployments.

#### Workflow Implementation

```yaml
jobs:
  deploy:
    runs-on: ubuntu-latest
    environment: production # References the protected environment
    steps:
      - uses: actions/checkout@v3
      - name: Deploy to Production
        run: |
          echo "Deployment approved and proceeding..."
          bash deploy.sh
```

#### How It Works

1. Workflow reaches a step with `environment: production`
2. GitHub pauses the workflow and waits for approval
3. Designated reviewers receive a notification to review the deployment
4. Once approved (or rejected), the workflow continues or fails
5. Approval is recorded in the deployment history

#### Example: Multi-Reviewer Approval Process

```yaml
name: Production Deployment

on:
  push:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - run: npm test

  deploy-staging:
    needs: build
    runs-on: ubuntu-latest
    environment:
      name: staging
      url: https://staging.example.com
    steps:
      - uses: actions/checkout@v3
      - run: bash deploy-staging.sh

  deploy-production:
    needs: deploy-staging
    runs-on: ubuntu-latest
    environment:
      name: production
      url: https://example.com
    steps:
      - uses: actions/checkout@v3
      - name: Require manual approval (via environment settings)
        run: |
          echo "Waiting for production approval..."
          echo "Current deployments require review from senior engineers"
      - run: bash deploy-production.sh
      - name: Notify deployment
        run: |
          echo "Deployment to production completed"
          echo "Commit: ${{ github.sha }}"
          echo "Deployed by: ${{ github.actor }}"
```

### 2. **Deployment Branches**

Restrict which branches can deploy to a specific environment.

#### Configuration Options

##### Protected Branches Only

```text
Only allow deployments from protected branches
```

##### Specific Branches

```text
main
release/*
prodaction/*
```

**All Branches** (least restrictive)

#### Example Workflow

```yaml
name: Conditional Deployment

on:
  push:
    branches: [main, develop, "release/*"]

jobs:
  deploy:
    runs-on: ubuntu-latest
    environment:
      name: production
      url: https://example.com
    # If environment is configured to allow only 'main' branch,
    # this job will fail on other branches
    steps:
      - uses: actions/checkout@v3
      - name: Verify Branch
        run: |
          if [ "${{ github.ref_name }}" = "main" ]; then
            echo "✓ Production deployment approved (main branch)"
          else
            echo "✗ Production deployment only allowed from main"
            exit 1
          fi
      - run: bash deploy.sh
```

#### Use Case: Different Strategies for Different Branches

```yaml
jobs:
  deploy-staging:
    if: github.ref_name == 'develop'
    environment:
      name: staging
    runs-on: ubuntu-latest
    steps:
      - run: bash deploy-staging.sh

  deploy-production:
    if: github.ref_name == 'main'
    environment:
      name: production # Only allows main branch
    runs-on: ubuntu-latest
    steps:
      - run: bash deploy-production.sh
```

### 3. **Wait Timer**

Add a delay before deployment is allowed to proceed, providing time for validation or issue discovery.

#### Configuration

Set wait timer (in minutes): `0` to `43200` (30 days)

#### Example: 24-Hour Wait Timer for Production

```yaml
jobs:
  deploy:
    runs-on: ubuntu-latest
    environment:
      name: production
      # When environment has 24-hour wait timer:
      # - Workflow triggers and waits 24 hours before proceeding
    steps:
      - uses: actions/checkout@v3
      - name: Pre-deployment checklist
        run: |
          echo "Deployment initiated at: $(date)"
          echo "Will proceed in 24 hours..."
          echo "Waiting for:"
          echo "- QA verification"
          echo "- Security review"
          echo "- Stakeholder confirmation"
      - run: bash deploy.sh
```

#### Use Case: Staggered Deployment Strategy

```yaml
jobs:
  deploy-canary:
    runs-on: ubuntu-latest
    environment:
      name: canary
      # No wait timer - immediate deployment to 5% of users
    steps:
      - run: bash deploy-canary.sh

  deploy-production:
    needs: deploy-canary
    runs-on: ubuntu-latest
    environment:
      name: production
      # 1-hour wait timer for full production rollout
      # Allows time to monitor canary deployment
    steps:
      - run: bash deploy-production.sh
```

### 4. **Custom Deployment Protection Rules**

Use GitHub Scripts (available on GitHub Enterprise) to create custom logic for deployment approval.

#### Example: Automated Approval Based on Test Results

```yaml
name: Smart Deployment

on:
  push:
    branches: [main]

jobs:
  test:
    runs-on: ubuntu-latest
    outputs:
      test-status: ${{ steps.test.outputs.status }}
    steps:
      - uses: actions/checkout@v3
      - id: test
        run: |
          npm test
          echo "status=passed" >> $GITHUB_OUTPUT

  deploy:
    needs: test
    runs-on: ubuntu-latest
    environment:
      name: staging
    steps:
      - uses: actions/checkout@v3
      - name: Check Test Status
        if: needs.test.outputs.test-status == 'passed'
        run: |
          echo "✓ Tests passed - proceeding with deployment"
          bash deploy.sh
      - name: Deployment Blocked
        if: needs.test.outputs.test-status != 'passed'
        run: |
          echo "✗ Tests failed - deployment blocked"
          exit 1
```

### 5. **Complete Example: Multi-Environment Protection Strategy**

```yaml
name: Multi-Environment Deployment with Protection Rules

on:
  push:
    branches: [develop, main, "release/*"]

jobs:
  build-and-test:
    runs-on: ubuntu-latest
    outputs:
      version: ${{ steps.version.outputs.value }}
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-node@v3
        with:
          node-version: 18
      - run: npm ci
      - run: npm run lint
      - run: npm test
      - id: version
        run: echo "value=$(npm run get-version)" >> $GITHUB_OUTPUT

  deploy-dev:
    needs: build-and-test
    runs-on: ubuntu-latest
    if: github.ref_name == 'develop'
    environment:
      name: development
      url: https://dev.example.com
      # No protection rules - immediate deployment
    steps:
      - uses: actions/checkout@v3
      - run: |
          echo "Deploying version ${{ needs.build-and-test.outputs.version }} to dev"
          bash deploy-dev.sh

  deploy-staging:
    needs: build-and-test
    runs-on: ubuntu-latest
    if: github.ref_name == 'main' || startsWith(github.ref, 'refs/heads/release/')
    environment:
      name: staging
      url: https://staging.example.com
      # Protection rule: requires 1 reviewer approval
      # For QA verification before full deployment
    steps:
      - uses: actions/checkout@v3
      - run: |
          echo "Deploying version ${{ needs.build-and-test.outputs.version }} to staging"
          bash deploy-staging.sh

  deploy-production:
    needs: [build-and-test, deploy-staging]
    runs-on: ubuntu-latest
    if: github.ref_name == 'main'
    environment:
      name: production
      url: https://example.com
      # Protection rules:
      # - Requires 2 reviewer approvals from senior engineers
      # - Only main branch allowed
      # - 30-minute wait timer for final checks
    steps:
      - uses: actions/checkout@v3
      - name: Pre-production Checklist
        run: |
          echo "=== Pre-Production Deployment Checklist ==="
          echo "✓ Build completed: ${{ needs.build-and-test.outputs.version }}"
          echo "✓ Staging deployment successful"
          echo "✓ Approved by authorized reviewers"
          echo "✓ 30-minute wait timer passed"
      - run: |
          echo "Deploying version ${{ needs.build-and-test.outputs.version }} to production"
          bash deploy-production.sh
      - name: Post-Deployment Notification
        if: success()
        run: |
          echo "✓ Production deployment successful"
          echo "Version: ${{ needs.build-and-test.outputs.version }}"
          echo "Deployed by: ${{ github.actor }}"
          echo "Time: $(date)"

  rollback:
    runs-on: ubuntu-latest
    if: failure() && github.ref_name == 'main'
    environment: production
    steps:
      - name: Notify Deployment Failure
        run: |
          echo "⚠ Production deployment failed"
          echo "Rollback may be required"
          echo "Alert sent to on-call engineers"
```

### 6. **Best Practices for Environment Protection Rules**

#### ✓ Recommended Practices

```yaml
# ✓ Use different protection levels per environment
environments:
  development: {}  # No restrictions
  staging:
    reviewers: [qa-team]  # QA approval required
  production:
    reviewers: [senior-engineers, devops-team]  # Multiple approvals
    branch-restrictions: [main]  # Only main branch
    wait-timer: 30  # 30 minutes for final validation

# ✓ Clear environment names and URLs
environment:
  name: production
  url: https://example.com  # Helps reviewers understand what's being deployed

# ✓ Include context in workflow
- name: Deployment Information
  run: |
    echo "Environment: ${{ github.environment }}"
    echo "Branch: ${{ github.ref_name }}"
    echo "Commit: ${{ github.sha }}"
    echo "Triggered by: ${{ github.actor }}"
```

#### ✗ Anti-Patterns to Avoid

```yaml
# ✗ No protection rules for production
environment: production  # Dangerous - anyone can deploy

# ✗ Allowing all branches to all environments
environment:
  name: production
  # No branch restrictions - feature branches can deploy to production

# ✗ Disabling reviews for production
environment:
  name: production
  # No reviewers required - critical issue

# ✗ Bypassing protection with conditional logic
jobs:
  deploy:
    if: contains(github.actor, 'bot')
    environment: production
    # Bad practice - circumvents protection rules
```

### 7. **Testing Protection Rules**

Verify your environment protection rules are working correctly:

```yaml
name: Test Environment Protection

on:
  push:
    branches: [test-protection]

jobs:
  test-staging:
    runs-on: ubuntu-latest
    environment:
      name: staging
    steps:
      - run: echo "This should pause if staging has reviewers"

  test-production:
    runs-on: ubuntu-latest
    environment:
      name: production
    steps:
      - run: echo "This should pause if production has reviewers"

  test-branch-restriction:
    runs-on: ubuntu-latest
    if: github.ref_name == 'main'
    environment:
      name: production
    steps:
      - run: echo "This only runs on main branch"
```

### 8. **Monitoring and Auditing Deployments**

View deployment history and approvals:

```yaml
jobs:
  audit-deployment:
    runs-on: ubuntu-latest
    steps:
      - name: Check Deployment Status
        run: |
          echo "Deployment ID: ${{ github.run_id }}"
          echo "Environment: ${{ github.environment }}"
          echo "Requested by: ${{ github.actor }}"
          echo "Timestamp: $(date)"
```

View in GitHub UI:

- Navigate to `Settings > Environments > [Environment Name]`
- View "Deployments" tab for history
- See approvals and denials in the deployment timeline

---

### 9. **Advanced Environment Protections**

#### Deployment Branches and Restrictions

Control which branches can deploy to specific environments:

```yaml
environment:
  name: production
  deployment-branch-policy:
    protected-branches: true # Only protected branches can deploy
    custom-deployment-branch-policies:
      - main
      - release/*
```

**Branch Policy Matrix:**

| Setting                             | Effect                              | Use Case                           |
| ----------------------------------- | ----------------------------------- | ---------------------------------- |
| `protected-branches: true`          | Only branches with protection rules | Enterprise: compliance requirement |
| `custom-branches: [main, release/]` | Only specific branches/patterns     | Controlled rollout strategy        |
| No filter                           | Any branch                          | Development/staging environments   |

#### Required Reviewers with Role-Based Access

```yaml
environment:
  name: production
  reviewers:
    - security-team
    - platform-leads
  deployment-branch-policy:
    protected-branches: true
```

**Reviewer Access Control (in Settings):**

- **All users**: Anyone in org can review
- **Specific organizations**: External org teams
- **Specific users**: Named individuals with elevated credentials
- **Specific teams**: Role-based (engineering, devops, security)

#### Custom Deployment Scripts with Approval Gates

Combine deployment protection rules with custom scripts for maximum control:

```yaml
name: Controlled Deployment

on:
  push:
    branches: [main]

jobs:
  pre-deployment-checks:
    runs-on: ubuntu-latest
    outputs:
      business-hours: ${{ steps.check-time.outputs.is-business-hours }}
      approval-required: ${{ steps.check-approval.outputs.required }}
    steps:
      - id: check-time
        run: |
          HOUR=$(date +%H)
          if [ $HOUR -ge 9 ] && [ $HOUR -le 17 ]; then
            echo "is-business-hours=true" >> $GITHUB_OUTPUT
          else
            echo "is-business-hours=false" >> $GITHUB_OUTPUT
          fi

      - id: check-approval
        run: |
          # Check if risky files changed
          if git diff origin/main HEAD --name-only | grep -E '^(src/auth|src/payment)'; then
            echo "required=true" >> $GITHUB_OUTPUT
          else
            echo "required=false" >> $GITHUB_OUTPUT
          fi

  deploy:
    needs: pre-deployment-checks
    runs-on: ubuntu-latest
    environment:
      name: ${{ needs.pre-deployment-checks.outputs.approval-required == 'true' && 'production-with-approval' || 'production' }}
      url: https://example.com
    steps:
      - uses: actions/checkout@v3
      - name: Deploy
        run: bash deploy.sh
```

#### Approval Timeout and Wait Timers

Set how long reviewers have to approve:

```yaml
environment:
  name: production
  # Settings > Environments > Wait timer
  wait-timer: 24 # 24-hour window for approvals
  deployment-branch-policy:
    protected-branches: true
  reviewers:
    - senior-engineers
```

**Deployment Timeout Behavior:**

- Approval timeout = workflow waits max 24 hours for reviewer decision
- After timeout: deployment **queued** (doesn't fail), reviewer can still approve
- Re-run: manual re-run resets timer and restarts job

#### Environment Secrets with Restricted Scope

Secrets at environment level have stricter access controls:

```yaml
jobs:
  deploy:
    runs-on: ubuntu-latest
    environment:
      name: production
      # Secrets defined here automatically scoped to:
      # - This environment only
      # - This repository only
      # - Only accessible during deployments to this env
    steps:
      - run: echo ${{ secrets.PROD_API_KEY }} # Only available in production environment
```

**Multi-Environment Secret Strategy:**

```yaml
jobs:
  deploy:
    runs-on: ubuntu-latest
    environment: ${{ matrix.environment }}
    strategy:
      matrix:
        environment: [staging, production]
    steps:
      - name: Deploy to ${{ matrix.environment }}
        run: |
          export API_KEY=${{ secrets.API_KEY }}
          bash deploy.sh
      # Each environment has its own API_KEY secret
      # GitHub automatically injects the right one based on environment
```

#### Deployment Status Checks and CI Requirements

Require CI checks to pass before deployment:

```yaml
# In Settings > Branch Protection:
# - Require status checks to pass
# - Require branches to be up to date before merging
# - Dismiss stale pull request approvals

# In workflow:
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - run: npm test

  deploy:
    needs: test # Implicit dependency
    runs-on: ubuntu-latest
    environment: production
    if: success() # Fail fast if tests don't pass
    steps:
      - run: bash deploy.sh
```

#### Monitoring Sensitive Deployments

Create audit trail for all deployments:

```yaml
jobs:
  deploy:
    runs-on: ubuntu-latest
    environment:
      name: production
      url: https://example.com
    steps:
      - uses: actions/checkout@v3

      - name: Log Deployment Metadata
        run: |
          cat > /tmp/deployment.log <<EOF
          Deployment Audit Log
          ====================
          Timestamp: $(date -u)
          Repository: $GITHUB_REPOSITORY
          Branch: $GITHUB_REF_NAME
          Commit SHA: $GITHUB_SHA
          Commit Message: $(git log -1 --pretty=%B)
          Triggered By: $GITHUB_ACTOR
          Workflow: $GITHUB_WORKFLOW
          Run ID: $GITHUB_RUN_ID
          EOF
          cat /tmp/deployment.log

      - name: Upload Deployment Log
        uses: actions/upload-artifact@v3
        with:
          name: deployment-logs
          path: /tmp/deployment.log
          retention-days: 90

      - name: Notify Audit System
        run: |
          curl -X POST https://audit.example.com/deployments \
            -H "Content-Type: application/json" \
            -H "Authorization: Bearer ${{ secrets.AUDIT_TOKEN }}" \
            -d '{
              "repository": "${{ github.repository }}",
              "branch": "${{ github.ref_name }}",
              "sha": "${{ github.sha }}",
              "actor": "${{ github.actor }}",
              "timestamp": "'$(date -u +%Y-%m-%dT%H:%M:%SZ)'"
            }'
```

---



---

## Navigation

- [← Back to Index](INDEX.md)
- [← Previous: 07-Default-Environment-Variables](07-Default-Environment-Variables.md)
- [Next: 09-Workflow-Artifacts →](09-Workflow-Artifacts.md)
