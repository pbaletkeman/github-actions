# GitHub Actions GH-200 Certification - 60-Hour Study Plan

**Total Duration:** 60 hours
**Sections:** 30 sections × 2 hours each
**Exam Pass Target:** >71%
**Format:** ~2 hours per section (mix of reading, practical work, and review)

---

## Study Plan Overview

This plan is designed to progressively build mastery from fundamentals to advanced enterprise concepts. Each section includes reading, practical exercises where applicable, and knowledge consolidation.

**Recommended Pace:** 2–3 sections per day over 10–15 days for intensive study, or 1–2 sections per day over 3–4 weeks for balanced learning.

---

## Week 1: Foundations & Core Concepts

### Section 1: GitHub Actions Fundamentals & Tooling (2 hrs)

**Topics:** 01-GitHub-Actions-VS-Code-Extension.md
**Overview:**

- GitHub Actions platform capabilities and use cases
- GitHub Actions VS Code Extension installation and setup
- YAML schema validation and IntelliSense features
- Local workflow validation with act or extension tools
- Best practices for local development and testing

**Deliverables:**

- Install and configure the VS Code extension
- Validate a sample workflow locally
- Understand extension features and troubleshooting

---

### Section 2: Understanding GitHub Contexts - Part 1 (2 hrs)

**Topics:** 02-Contextual-Information.md (first half)
**Overview:**

- What are contexts and why they matter in workflows
- The `github` context: repository info, ref, event data, actor
- The `env` context: environment variables at workflow/job/step level
- The `secrets` context: encrypted secret handling
- Context interpolation and expression syntax

**Practical Work:**

- Create a test workflow using `github.event_name` and `github.ref`
- Access repository owner and branch information
- Practice secret masking and context limitations

---

### Section 3: Understanding GitHub Contexts - Part 2 (2 hrs)

**Topics:** 02-Contextual-Information.md (second half) + 03-Context-Availability-Reference.md
**Overview:**

- The `job`, `runner`, `steps`, and `matrix` contexts
- The `inputs` context for workflow_dispatch and workflow_call
- The `needs` context for inter-job communication
- The `strategy` context for matrix configurations
- Static vs runtime evaluation of expressions

**Practical Work:**

- Use matrix context to access axis values
- Pass data between jobs using needs context
- Implement conditional expressions with context data
- Test context availability at different workflow scopes

---

### Section 4: YAML Syntax & Workflow File Structure (2 hrs)

**Topics:** 04-Workflow-File-Structure.md
**Overview:**

- GitHub Actions workflow file anatomy
- Top-level keys: name, on, env, jobs, defaults, concurrency
- YAML anchors (&), aliases (*), and merge operators (<<)
- Proper indentation and YAML nesting
- Workflow file naming and placement (.github/workflows/)
- Schema validation and common YAML errors

**Practical Work:**

- Create a workflow file from scratch with proper structure
- Implement YAML anchors to reduce duplication
- Fix syntax errors in sample workflows
- Use schema validation to catch mistakes early

---

## Week 2: Workflow Triggers & Events

### Section 5: Workflow Trigger Events - Part 1 (2 hrs)

**Topics:** 05-Workflow-Trigger-Events.md (first half)
**Overview:**

- Repository events: `push`, `pull_request`, `pull_request_review`
- Scheduled events: `schedule` with cron syntax
- Manual trigger: `workflow_dispatch` and inputs validation
- Repository lifecycle events: create, delete, fork, public
- Status events: workflow_run, deployment, deployment_status

**Practical Work:**

- Configure workflows to trigger on push to specific branches
- Set up scheduled workflows with cron expressions
- Validate cron syntax for different time zones
- Create workflow_dispatch with typed inputs

---

### Section 6: Workflow Trigger Events - Part 2 (2 hrs)

**Topics:** 05-Workflow-Trigger-Events.md (second half)
**Overview:**

- Advanced filtering: paths, branches, tags
- Webhook event patterns (push, pull request, release)
- Workflow_dispatch input types: string, choice, boolean, environment
- Default input values and required inputs
- Passing inputs to reusable workflows

**Practical Work:**

- Create workflows with branch and path filtering
- Implement sophisticated workflow_dispatch input validation
- Pass inputs from parent to reusable workflows
- Test trigger behavior with different event types

---

### Section 7: Environment & Runtime Configuration (2 hrs)

**Topics:** 06-Custom-Environment-Variables.md, 07-Default-Environment-Variables.md
**Overview:**

- Custom environment variables at workflow, job, and step levels
- Default environment variables (GITHUB_*, CI, RUNNER_*, etc.)
- Setting variables dynamically with GITHUB_ENV
- Variable scoping and precedence rules
- Secret-safe variable handling
- GitHub Actions context interpolation in env vars

**Practical Work:**

- Define custom environment variables at workflow scope
- Set runtime environment variables in steps
- Access default environment variables in scripts
- Implement environment-based configuration (dev/prod)
- Verify variable scope and precedence

---

### Section 8: Environment Protection Rules & Secrets (2 hrs)

**Topics:** 08-Environment-Protection-Rules.md
**Overview:**

- Environment concepts and deployment targets
- Environment-specific secrets and variables
- Protection rules: required reviewers, deployment branches
- Manual approval gates in workflows
- GITHUB_TOKEN permissions and scoping
- PAT (Personal Access Token) vs GITHUB_TOKEN lifecycle

**Practical Work:**

- Create environments with different configurations
- Set up environment-specific secrets
- Implement required reviewer protection rules
- Configure deployment branch restrictions
- Test approval workflows

---

## Week 3: Workflow Features & Data Flow

### Section 9: Jobs, Steps & Conditional Logic (2 hrs)

**Topics:** 04-Workflow-File-Structure.md (jobs section)
**Overview:**

- Job definition and identification
- Step structure: name, id, uses, run, with, env
- Sequential vs parallel job execution
- Job dependencies with `needs`
- Conditional execution: if expressions with success/failure functions
- Failure handling: continue-on-error, failure markers

**Practical Work:**

- Create multi-job workflows with dependencies
- Implement conditional steps using if expressions
- Use success(), failure(), and cancelled() functions
- Handle errors and continue execution strategically
- Test job ordering and failure scenarios

---

### Section 10: Job Strategy & Matrix (2 hrs)

**Topics:** 04-Workflow-File-Structure.md (matrix section)
**Overview:**

- Strategy matrix for generating job variations
- Multi-axis matrix (OS, Python version, Node version combinations)
- Include/exclude matrix configurations
- Fail-fast behavior and max-parallel settings
- Matrix context access and interpolation
- Cost optimization with matrix configuration

**Practical Work:**

- Create multi-OS test matrices (Ubuntu, Windows, macOS)
- Build version compatibility matrices (Node, Python, Java)
- Use include/exclude for special variations
- Optimize matrix for cost and speed
- Access and use matrix context values in steps

---

### Section 11: Service Containers & Dependencies (2 hrs)

**Topics:** 04-Workflow-File-Structure.md (services section)
**Overview:**

- Service containers for testing (databases, Redis, etc.)
- Container configuration: image, ports, options, volumes
- Port mapping and network connectivity
- Health checks and startup conditions
- Environment variables within containers
- Service container networking best practices

**Practical Work:**

- Set up PostgreSQL service container for integration tests
- Configure Redis service container
- Implement health checks for service readiness
- Test application code against service containers
- Debug service container networking issues

---

### Section 12: Workflow Artifacts - Storage & Retrieval (2 hrs)

**Topics:** 09-Workflow-Artifacts.md
**Overview:**

- Artifact concepts: upload, storage, retention
- Upload-artifact action: path patterns, name, retention-days
- Download-artifact action: name, path patterns
- Artifact download from UI and REST API
- Cross-job artifact sharing
- Retention policies and cost implications
- Artifact download links and expiration

**Practical Work:**

- Upload test results and coverage reports as artifacts
- Download artifacts in subsequent jobs
- Implement retention policies to manage storage
- Access artifacts via GitHub API
- Create artifact download workflows

---

### Section 13: Workflow Caching - Performance & Security (2 hrs)

**Topics:** 10-Workflow-Caching.md
**Overview:**

- Caching concepts: key, path, restore key
- Cache action: setup-cache vs generic cache
- Cache strategy: pip, npm, gradle, maven
- Cache key generation and versioning
- Scope: repository vs branch level
- Cache invalidation and cleanup
- Security considerations for cached data

**Practical Work:**

- Implement npm dependency caching
- Set up Python pip caching
- Create effective cache key strategies
- Test cache hits and misses
- Implement cache cleanup for sensitive data
- Compare caching performance benefits

---

## Week 4: Advanced Workflow Features

### Section 14: Inter-Job Communication & Outputs (2 hrs)

**Topics:** GitHub Workflows - Job Outputs section
**Overview:**

- Job outputs: defining and exporting step outputs
- Output interpolation in dependent jobs
- GITHUB_OUTPUT file for setting job outputs
- Artifact-based data passing for larger payloads
- Environment file sharing (GITHUB_ENV)
- Matrix job outputs and aggregation
- Output limits and best practices

**Practical Work:**

- Create workflows with job-to-job output passing
- Implement output aggregation from matrix jobs
- Use outputs in conditional job execution
- Pass complex data structures between jobs
- Test output limits and fallback strategies

---

### Section 15: Workflow Debugging & Troubleshooting (2 hrs)

**Topics:** 12-Workflow-Debugging.md, 19-Common-Failures-Troubleshooting.md (first half)
**Overview:**

- Debug logging: step context and secrets redaction
- Runner diagnostics information
- Log retention and access via UI/API
- Common failure patterns and causes
- Timeout and resource exhaustion issues
- YAML parsing and syntax errors
- Matrix job failure correlation

**Practical Work:**

- Enable debug logging in a workflow run
- Analyze workflow logs for errors
- Diagnose timeout issues
- Fix common YAML parsing errors
- Use run history to identify failure patterns
- Create diagnostic workflows

---

### Section 16: GitHub Workflow REST API (2 hrs)

**Topics:** 13-Workflows-REST-API.md
**Overview:**

- REST API endpoints for workflows
- List, get, and manage workflow runs
- Retrieve job information and logs
- Download artifacts programmatically
- Manage workflow runs: cancel, approve, review
- Retention policy management via API
- API rate limits and authentication

**Practical Work:**

- Query workflow runs using REST API
- Download artifacts via API scripts
- Cancel workflow runs programmatically
- Retrieve deployment status via API
- Build automation around workflow management

---

## Week 5: Reusable Workflows & Actions

### Section 17: Reusable Workflows & Workflow Sharing (2 hrs)

**Topics:** 11-Workflow-Sharing.md
**Overview:**

- Reusable workflow concept: workflow_call
- Input/output mapping for reusable workflows
- Secrets in reusable workflows
- Adding reusable workflows from same org/repo
- Public action repository workflows
- Versioning and tagging strategies
- Calling workflows from different repos/orgs

**Practical Work:**

- Create a reusable workflow for common tasks
- Call reusable workflows with various inputs
- Implement proper versioning with tags
- Pass secrets to reusable workflows
- Test reusable workflow output handling

---

### Section 18: Custom Actions - Types & Structure (2 hrs)

**Topics:** 15-Creating-Publishing-Actions.md (first half)
**Overview:**

- Action types: JavaScript, Docker, Composite
- Action metadata: action.yml structure
- Inputs and outputs definitions
- Branding and descriptions
- Running vs docker container execution
- Composite actions: combining existing actions
- Action composition patterns

**Practical Work:**

- Create a composite action combining multiple actions
- Build a JavaScript action with inputs/outputs
- Define action.yml metadata properly
- Test action with workflow
- Document action usage

---

### Section 19: Custom Actions - Distribution & Publishing (2 hrs)

**Topics:** 15-Creating-Publishing-Actions.md (second half)
**Overview:**

- Public vs private action distribution
- Publishing to GitHub Marketplace
- Action versioning and release management
- Marketplace discovery and best practices
- Major version tagging (@v1, @v2)
- Committing node_modules or using ncc bundler
- Action testing and CI/CD

**Practical Work:**

- Prepare action for marketplace publication
- Implement versioning strategy
- Create action releases
- Test action from marketplace
- Document action in README

---

## Week 6: Runners & Enterprise

### Section 20: GitHub-Hosted Runners (2 hrs)

**Topics:** 16-Managing-Runners.md (first half)
**Overview:**

- GitHub-hosted runner types (Ubuntu, Windows, macOS)
- Available runner images and preinstalled software
- Runner labels and selection
- Image updates and deprecations (Ubuntu 20.04, Windows Server 2025)
- Runner specifiers: runs-on syntax
- Concurrency limits and availability
- Hardware resources (CPU, memory, disk)

**Practical Work:**

- Create workflows targeting different runner types
- Use runner labels for specific requirements
- Check runner toolcache availability
- Handle runner image changes and updates
- Test workflow compatibility across runners

---

### Section 21: Self-Hosted Runners & Configuration (2 hrs)

**Topics:** 16-Managing-Runners.md (second half)
**Overview:**

- Self-hosted runner installation and setup
- Runner groups and organization
- Runner labels and selection logic
- Network configuration and IP whitelisting
- Authentication and security considerations
- Runner auto-scaling and orchestration
- Health checks and monitoring

**Practical Work:**

- Set up a self-hosted runner locally
- Configure runner groups and labels
- Implement IP allow lists
- Test runner selection with specific labels
- Monitor runner health and performance

---

### Section 22: Enterprise-Level Management (2 hrs)

**Topics:** 17-GitHub-Actions-Enterprise.md
**Overview:**

- Enterprise policy management
- Reusable workflows at enterprise scale
- Organization-level workflow templates
- Starter workflows for enterprise teams
- Usage policies: allow, deny, required
- Cost allocation and cleanup policies
- Audit logging and compliance tracking

**Practical Work:**

- Create organization-level reusable workflows
- Set up starter templates for standard CI/CD
- Configure access policies for workflows
- Implement audit logging
- Establish cost management practices

---

## Week 7: Security & Optimization

### Section 23: GitHub Actions Security Best Practices - Part 1 (2 hrs)

**Topics:** 18-Security-and-Optimization.md (first half)
**Overview:**

- Principle of least privilege for permissions
- GITHUB_TOKEN: scope, ephemeral nature, permissions
- Environment protection rules and approval gates
- Script injection prevention techniques
- Input sanitization and validation
- Avoiding untrusted data in shell commands
- Token security and credential management

**Practical Work:**

- Review GITHUB_TOKEN permissions
- Implement least-privilege permissions in workflows
- Sanitize user inputs to prevent injection
- Set up approval gates for sensitive deployments
- Audit workflow permissions

---

### Section 24: GitHub Actions Security Best Practices - Part 2 (2 hrs)

**Topics:** 18-Security-and-Optimization.md (second half)
**Overview:**

- Action pinning: commit SHA vs tags vs fuzzy versions
- Verifying action sources and trustworthiness
- OIDC tokens for cloud provider federation
- Artifact attestation and provenance (SLSA, build metadata)
- Deployment verification workflows
- Secrets scanning and rotation
- Vulnerability scanning in workflows

**Practical Work:**

- Pin third-party actions to commit SHAs
- Implement OIDC for AWS/Azure scenarios
- Verify artifact provenance
- Set up secrets scanning
- Implement dependency scanning

---

### Section 25: Workflow Performance Optimization (2 hrs)

**Topics:** 10-Workflow-Caching.md, 18-Security-and-Optimization.md (optimization section)
**Overview:**

- Identifying performance bottlenecks
- Caching strategy for build artifacts
- Parallel job execution and concurrency
- Matrix configuration optimization
- Container image layer caching
- Artifact size management
- Retention policy optimization for cost

**Practical Work:**

- Profile slow workflows
- Implement caching improvements
- Reduce artifact sizes
- Parallelize jobs appropriately
- Measure performance improvements

---

### Section 26: Troubleshooting & Common Failures - Part 2 (2 hrs)

**Topics:** 19-Common-Failures-Troubleshooting.md (second half)
**Overview:**

- Matrix job failure diagnosis
- Timeout and resource exhaustion
- Secret leakage and redaction
- Action versioning conflicts
- Environment variable scoping issues
- Caching invalidation problems
- Cross-platform compatibility issues
- Network and connectivity failures

**Practical Work:**

- Debug matrix job failures
- Identify and fix timeout issues
- Analyze logs for common patterns
- Test cross-platform workflows
- Implement recovery strategies

---

## Week 8: Job Summaries & Advanced Features

### Section 27: Job Summaries & Workflow Status (2 hrs)

**Topics:** 04-Workflow-File-Structure.md (job summaries section), 14-Reviewing-Deployments.md
**Overview:**

- GITHUB_STEP_SUMMARY for rich markdown reports
- Test result reporting in summaries
- Coverage reports and metrics
- Deployment workflow summaries
- Status badges and visualization
- Emergency check workflows
- Manual environment approvals

**Practical Work:**

- Generate test and coverage summaries
- Create formatted markdown reports in workflows
- Implement status badges
- Set up deployment review workflows
- Configure manual approvals

---

### Section 28: Comprehensive Practice Assessment (2 hrs)

**Topics:** All topic files - comprehensive review
**Overview:**

- Scenario-based problem solving
- Integration testing across workflow concepts
- Enterprise workflow design
- Security and compliance scenarios
- Performance optimization challenges
- Real-world troubleshooting cases

**Deliverables:**

- Complete practice scenarios
- Build one complex multi-feature workflow
- Solve 10+ practice questions
- Review explanations for any incorrect answers

---

## Week 9: Final Review & Assessment

### Section 29: Mock Exam - Full Practice Test (2 hrs)

**Topics:** question-prompt.md - Generate practice questions
**Overview:**

- Full 100-question practice exam
- Covers all 5 exam domains
- Timed assessment (2 hours simulating actual exam)
- Question difficulty distribution: 20% easy, 60% medium, 20% hard
- Performance tracking and weak area identification

**Deliverables:**

- Complete mock exam in 2 hours
- Identify weak knowledge areas
- Review explanations for all incorrect answers
- Track improvement from earlier assessments

---

### Section 30: Final Consolidation & Exam Prep (2 hrs)

**Topics:** All materials - focused review
**Overview:**

- Weak area deep-dive based on practice test results
- Quick reference guides for key concepts
- Common exam pitfalls and tricks
- Time management strategies
- Question-type strategies (scenario, troubleshooting, best practices)
- Final knowledge verification
- Exam day logistics and tips

**Deliverables:**

- Review notes on weak areas
- Create personal quick reference guide
- Verify readiness with final mini-quizzes
- Plan exam day approach

---

## Study Strategy & Tips

### Daily Checklist

- [ ] Read/watch assigned section content
- [ ] Complete hands-on practical work
- [ ] Answer quiz questions related to section
- [ ] Review and consolidate notes

### Knowledge Retention

- **Spaced Repetition:** Review topics 1 day, 3 days, 1 week later
- **Active Recall:** Quiz yourself without looking at notes
- **Practice Application:** Build actual workflows, not just theoretical work
- **Teaching Others:** Explain concepts to solidify understanding

### Weak Area Focus

- After each section quiz, identify topics scoring <80%
- Spend additional 30 mins on weak areas
- Re-do practice questions for those topics
- Build practice workflows targeting weak concepts

### Time Management

- **Intensive Track (10 days):** 3 sections/day, study 6 hours/day
- **Balanced Track (20 days):** 1.5 sections/day, study 3 hours/day
- **Relaxed Track (30 days):** 1 section/day, study 2 hours/day

### Success Criteria

- **Minimum:** >71% on practice exams consistently
- **Target:** >80% on practice exams (indicates 85%+ on actual exam)
- **Excellent:** >85% on practice exams (indicates 90%+ on actual exam)

---

## Resources & Materials

- **Study Materials:** 19 topic markdown files in root directory
- **Practice Questions:** question-prompt.md (generates 100 questions per iteration)
- **Hands-On Labs:** All sections include practical GitHub workflow building
- **Reference:** GitHub-Workflows-Guide.md and exam-overview.md
- **External:** Official GitHub Actions documentation and tutorials

---

## Success Tracking Template

Use this to track progress:

```plaintext
Date: ___________

Section: ___________

Time Spent: ___________

Topics Covered:
- [ ] Topic 1
- [ ] Topic 2
- [ ] Topic 3

Hands-On Work Completed:
- [ ] Practical 1
- [ ] Practical 2

Quiz Score: _____ / 100 (Target: >80%)

Weak Areas Identified:
- _________
- _________

Next Review: _________

Notes:
_________
```

---

**Good luck with your GH-200 certification! A solid 60 hours of focused study with this plan should put you well above the 71% pass threshold.**
