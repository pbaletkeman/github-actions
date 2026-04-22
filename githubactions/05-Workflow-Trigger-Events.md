[← Back to Index](INDEX.md)

## GitHub Workflow Trigger Events

GitHub provides numerous trigger events that determine when a workflow runs. Understanding all available trigger events allows you to create powerful automation tailored to your specific needs. This section comprehensively documents every trigger event available in GitHub Actions.

### Overview of Trigger Events

Trigger events fall into several categories:

- **Repository Events**: Triggered by changes in the repository (push, pull requests, etc.)
- **External Events**: Triggered by external systems (webhook events, repository dispatch)
- **Time-Based Events**: Triggered on a schedule (cron jobs)
- **Manual Events**: Triggered by user action (workflow dispatch)
- **Workflow Events**: Triggered by other workflows

### 1. **push** - Code Push Event

Triggers when code is pushed to the repository.

```yaml
on: push

# Trigger on specific branches
on:
  push:
    branches:
      - main
      - develop
      - 'release/**'  # Wildcard pattern

# Trigger on specific tags
on:
  push:
    tags:
      - 'v*'  # All versions like v1.0, v2.1.0, etc.
      - 'release-*'

# Trigger on specific file changes (paths)
on:
  push:
    paths:
      - 'src/**'
      - 'package.json'
      - '.github/workflows/**'

# Ignore specific paths
on:
  push:
    paths-ignore:
      - '*.md'
      - 'docs/**'
      - '.gitignore'

# Combine multiple conditions
on:
  push:
    branches: [main]
    paths: ['src/**', 'package.json']
```

#### Practical Example

```yaml
name: Push Event Handler

on:
  push:
    branches: [main, develop]
    paths: ["src/**", "package.json"]

jobs:
  on-push:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - run: echo "Code pushed to ${{ github.ref_name }}"
```

---

### 2. **pull_request** - Pull Request Event

Triggers when pull request events occur (opened, reopened, synchronized, etc.).

```yaml
on: pull_request

# Trigger on specific branches
on:
  pull_request:
    branches: [main, develop]

# Trigger on pull requests that modify specific paths
on:
  pull_request:
    paths:
      - '**/*.js'
      - 'package.json'

# Specific PR actions
on:
  pull_request:
    types:
      - opened
      - reopened
      - synchronize
      - ready_for_review
      - converted_to_draft

# Ignore drafts
on:
  pull_request:
    paths-ignore: ['*.md']
```

#### PR Event Types

| Type                     | Description                      |
| ------------------------ | -------------------------------- |
| `opened`                 | Pull request created             |
| `reopened`               | Previously closed PR reopened    |
| `synchronize`            | PR commits added (code changed)  |
| `converted_to_draft`     | PR converted to draft            |
| `ready_for_review`       | Draft PR marked ready for review |
| `labeled`                | Label added to PR                |
| `unlabeled`              | Label removed from PR            |
| `assigned`               | Assignee added                   |
| `unassigned`             | Assignee removed                 |
| `edited`                 | PR title/description edited      |
| `auto_merge_enabled`     | Auto-merge enabled               |
| `auto_merge_disabled`    | Auto-merge disabled              |
| `closed`                 | PR closed                        |
| `locked`                 | PR locked                        |
| `unlocked`               | PR unlocked                      |
| `review_requested`       | Review requested                 |
| `review_request_removed` | Review request removed           |

#### Practical Example — Pull Request Checks

```yaml
name: Pull Request Checks

on:
  pull_request:
    types: [opened, synchronize, ready_for_review]

jobs:
  checks:
    if: github.event.pull_request.draft == false
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - run: npm test
      - run: npm run lint
```

---

### 3. **pull_request_target** - PR Target Event

Similar to `pull_request` but with access to secrets and full write permissions. Use with caution for external contributions.

```yaml
on:
  pull_request_target:
    types: [opened, synchronize]

jobs:
  review:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
        with:
          ref: ${{ github.event.pull_request.head.sha }}
      - run: npm test
```

---

### 4. **workflow_dispatch** - Manual Trigger

Manual trigger via GitHub UI or API.

```yaml
on:
  workflow_dispatch:
    inputs:
      environment:
        description: "Deployment environment"
        required: true
        type: choice
        options:
          - development
          - staging
          - production
      version:
        description: "Version to deploy"
        required: false
        type: string
      dry_run:
        description: "Run as dry-run"
        required: false
        type: boolean
        default: true

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - run: |
          echo "Deploying to ${{ inputs.environment }}"
          echo "Version: ${{ inputs.version }}"
          echo "Dry Run: ${{ inputs.dry_run }}"
```

#### Input Types

| Type          | Description        | Example               |
| ------------- | ------------------ | --------------------- |
| `string`      | Text input         | `version: "1.0.0"`    |
| `choice`      | Dropdown selection | Environment selection |
| `boolean`     | Checkbox           | `true` or `false`     |
| `number`      | Numeric input      | `timeout: 30`         |
| `environment` | Select environment | Production, staging   |

---

### 5. **schedule** - Scheduled Events (Cron)

Trigger workflows on a schedule using cron syntax.

```yaml
on:
  schedule:
    # Run every day at midnight UTC
    - cron: "0 0 * * *"

    # Run every 6 hours
    - cron: "0 */6 * * *"

    # Run at 8 AM Monday-Friday
    - cron: "0 8 * * 1-5"

    # Run first day of month at 2 AM
    - cron: "0 2 1 * *"

jobs:
  scheduled-job:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Running scheduled workflow"
```

#### Cron Syntax Reference

```text
┌───────────── minute (0 - 59)
│ ┌───────────── hour (0 - 23)
│ │ ┌───────────── day of month (1 - 31)
│ │ │ ┌───────────── month (1 - 12)
│ │ │ │ ┌───────────── day of week (0 - 6) (0 = Sunday)
│ │ │ │ │
│ │ │ │ │
* * * * *
```

#### Common Cron Patterns

| Pattern        | Description                |
| -------------- | -------------------------- |
| `0 0 * * *`    | Daily at midnight UTC      |
| `0 */6 * * *`  | Every 6 hours              |
| `0 8 * * 1-5`  | 8 AM Monday-Friday         |
| `0 2 1 * *`    | First day of month at 2 AM |
| `*/30 * * * *` | Every 30 minutes           |
| `0 9 * * MON`  | Every Monday at 9 AM       |
| `0 0 * * 0`    | Every Sunday at midnight   |

---

### 6. **workflow_run** - Trigger on Another Workflow

Triggers based on another workflow's completion.

```yaml
on:
  workflow_run:
    workflows: ["Deploy"] # Workflow name or file path
    types:
      - completed
      - requested

jobs:
  follow-up:
    runs-on: ubuntu-latest
    if: github.event.workflow_run.conclusion == 'success'
    steps:
      - run: echo "Previous workflow completed successfully"
```

#### Workflow Run Types

| Type        | Description               |
| ----------- | ------------------------- |
| `completed` | Workflow run finished     |
| `requested` | Workflow requested to run |

---

### 7. **release** - Release Events

Trigger when releases are created, edited, or deleted.

```yaml
on:
  release:
    types:
      - published
      - created
      - edited
      - deleted
      - prereleased
      - released

jobs:
  on-release:
    runs-on: ubuntu-latest
    steps:
      - run: |
          echo "Release: ${{ github.event.release.tag_name }}"
          echo "Name: ${{ github.event.release.name }}"
```

#### Release Types

| Type          | Description                                               |
| ------------- | --------------------------------------------------------- |
| `published`   | Release published (including pre-releases when published) |
| `unpublished` | Release unpublished                                       |
| `created`     | Release created (or a pre-release published)              |
| `edited`      | Release edited                                            |
| `deleted`     | Release deleted                                           |
| `prereleased` | Marked as pre-release                                     |
| `released`    | Released (after being pre-release)                        |

---

### 8. **issues** - Issue Events

Trigger on issue activity.

```yaml
on:
  issues:
    types:
      - opened
      - closed
      - reopened
      - assigned
      - labeled
      - milestoned

jobs:
  on-issue:
    runs-on: ubuntu-latest
    steps:
      - run: |
          echo "Issue: ${{ github.event.issue.title }}"
          echo "Action: ${{ github.event.action }}"
```

#### Issue Types

| Type           | Description       |
| -------------- | ----------------- |
| `opened`       | Issue created     |
| `closed`       | Issue closed      |
| `reopened`     | Issue reopened    |
| `assigned`     | Assignee added    |
| `unassigned`   | Assignee removed  |
| `labeled`      | Label added       |
| `unlabeled`    | Label removed     |
| `milestoned`   | Milestone added   |
| `demilestoned` | Milestone removed |
| `transferred`  | Issue transferred |
| `pinned`       | Issue pinned      |
| `unpinned`     | Issue unpinned    |
| `deleted`      | Issue deleted     |

---

### 9. **issue_comment** - Issue Comment Events

Trigger when comments are added/edited on issues or PRs.

```yaml
on:
  issue_comment:
    types: [created, edited, deleted]

jobs:
  on-comment:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Comment: ${{ github.event.comment.body }}"
```

---

### 10. **discussion** - Discussion Events

Trigger on discussion activity (private beta).

```yaml
on:
  discussion:
    types:
      - created
      - edited
      - deleted
      - transferred
      - pinned
      - unpinned
      - labeled
      - unlabeled
      - answered
      - unanswered

jobs:
  on-discussion:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Discussion: ${{ github.event.discussion.title }}"
```

---

### 11. **discussion_comment** - Discussion Comment Events

Trigger on discussion comments.

```yaml
on:
  discussion_comment:
    types: [created, edited, deleted]

jobs:
  on-discussion-comment:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Added comment to discussion"
```

---

### 12. **fork** - Repository Fork Event

Trigger when repository is forked.

```yaml
on: fork

jobs:
  on-fork:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Repository was forked"
```

---

### 13. **gollum** - Wiki Changes

Trigger when wiki pages are created or updated.

```yaml
on: gollum

jobs:
  on-wiki-change:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Wiki updated"
```

---

### 14. **watch** - Star/Watch Event

Trigger when repository is starred.

```yaml
on: watch

jobs:
  on-star:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Repository was starred"
```

---

### 15. **create** - Branch/Tag Creation

Trigger when branch or tag is created.

```yaml
on: create

jobs:
  on-create:
    runs-on: ubuntu-latest
    steps:
      - run: |
          echo "Ref: ${{ github.event.ref }}"
          echo "Ref Type: ${{ github.event.ref_type }}"
```

---

### 16. **delete** - Branch/Tag Deletion

Trigger when branch or tag is deleted.

```yaml
on: delete

jobs:
  on-delete:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Branch/tag ${{ github.event.ref }} deleted"
```

---

### 17. **public** - Repository Public Event

Trigger when repository becomes public.

```yaml
on: public

jobs:
  on-public:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Repository is now public"
```

---

### 18. **push to protected branch** - Protected Branch Push

Automatic event when pushing to a branch with branch protection rules.

```yaml
on:
  push:
    branches: [main] # Main is typically protected

jobs:
  protected-push:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Protected branch updated"
```

---

### 19. **repository_dispatch** - External Trigger via API

Trigger from external systems via GitHub API.

```yaml
on:
  repository_dispatch:
    types: [deploy-prod, run-tests, custom-event]

jobs:
  on-dispatch:
    runs-on: ubuntu-latest
    steps:
      - run: |
          echo "Event type: ${{ github.event.action }}"
          echo "Payload: ${{ toJson(github.event.client_payload) }}"
```

**Trigger via API:**

```bash
curl -X POST https://api.github.com/repos/owner/repo/dispatches \
  -H "Authorization: token YOUR_TOKEN" \
  -H "Accept: application/vnd.github.everest-preview+json" \
  -d '{
    "event_type": "deploy-prod",
    "client_payload": { "branch": "main" }
  }'
```

---

### 20. **check_run** - Check Run Events

Trigger when check run is created or updated.

```yaml
on:
  check_run:
    types: [created, rerequested, completed, requested_action]

jobs:
  on-check-run:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Check run event"
```

---

### 21. **check_suite** - Check Suite Events

Trigger when check suite is created or updated.

```yaml
on:
  check_suite:
    types: [completed]

jobs:
  on-check-suite:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Check suite completed"
```

---

### 22. **pull_request_review** - PR Review Events

Trigger on pull request review actions.

```yaml
on:
  pull_request_review:
    types:
      - submitted
      - edited
      - dismissed

jobs:
  on-review:
    runs-on: ubuntu-latest
    steps:
      - run: |
          echo "Review state: ${{ github.event.review.state }}"
```

---

### 23. **pull_request_review_comment** - PR Review Comment Events

Trigger on comments in pull request reviews.

```yaml
on:
  pull_request_review_comment:
    types: [created, edited, deleted]

jobs:
  on-review-comment:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Review comment added"
```

---

### 24. **member** - Collaborator Events

Trigger when collaborator added to repository.

```yaml
on: member

jobs:
  on-member-add:
    runs-on: ubuntu-latest
    steps:
      - run: echo "New collaborator added"
```

---

### 25. **team_add** - Team Added Event

Trigger when team is added to repository.

```yaml
on: team_add

jobs:
  on-team-add:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Team added to repository"
```

---

### 26. **push_protected_branch** - Protected Branch Push

Automatic trigger for protected branch updates.

```yaml
on:
  push:
    branches:
      - main
      - master
```

---

### Complete Example: All Event Types

```yaml
name: Comprehensive Trigger Events Example

on:
  # Repository events
  push:
    branches: [main]
    paths: ["src/**"]
  pull_request:
    types: [opened, synchronize]
  release:
    types: [published]
  issues:
    types: [opened, labeled]
  issue_comment:
    types: [created]
  discussion:
    types: [created]
  fork: {}
  create: {}
  delete: {}
  public: {}

  # Scheduled events
  schedule:
    - cron: "0 0 * * *"

  # Manual trigger
  workflow_dispatch:
    inputs:
      debug:
        type: boolean
        default: false

jobs:
  handle-events:
    runs-on: ubuntu-latest
    steps:
      - name: Identify Event
        run: |
          echo "Event Name: ${{ github.event_name }}"
          echo "Action: ${{ github.event.action }}"
          case "${{ github.event_name }}" in
            push)
              echo "Code was pushed"
              ;;
            pull_request)
              echo "Pull request: ${{ github.event.action }}"
              ;;
            release)
              echo "Release: ${{ github.event.release.tag_name }}"
              ;;
            issues)
              echo "Issue: ${{ github.event.issue.title }}"
              ;;
            schedule)
              echo "Scheduled run"
              ;;
            workflow_dispatch)
              echo "Manual trigger - Debug: ${{ inputs.debug }}"
              ;;
          esac
```

### Event Availability Summary

| Event                 | When Triggered     | Secret Access | Write Permissions |
| --------------------- | ------------------ | ------------- | ----------------- |
| `push`                | Code push          | ✓             | ✓                 |
| `pull_request`        | PR activity        | Limited       | Limited           |
| `workflow_dispatch`   | Manual             | ✓             | ✓                 |
| `schedule`            | Cron schedule      | ✓             | ✓                 |
| `release`             | Release published  | ✓             | ✓                 |
| `issues`              | Issue activity     | ✓             | ✓                 |
| `issue_comment`       | Comments           | ✓             | ✓                 |
| `workflow_run`        | Workflow completes | ✓             | ✓                 |
| `repository_dispatch` | API call           | ✓             | ✓                 |
| `fork`                | Repository forked  | Limited       | Limited           |

---



---

## Navigation

- [← Back to Index](INDEX.md)
- [← Previous: 04-Workflow-File-Structure](04-Workflow-File-Structure.md)
- [Next: 06-Custom-Environment-Variables →](06-Custom-Environment-Variables.md)
