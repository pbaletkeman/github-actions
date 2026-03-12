[← Back to Index](INDEX.md)

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



---

## Navigation

- [← Back to Index](INDEX.md)
- [← Previous: 08-Environment-Protection-Rules](08-Environment-Protection-Rules.md)
- [Next: 10-Workflow-Caching →](10-Workflow-Caching.md)
