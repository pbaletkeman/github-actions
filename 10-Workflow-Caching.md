[← Back to Index](INDEX.md)

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



---

## Navigation

- [← Back to Index](INDEX.md)
- [← Previous: 09-Workflow-Artifacts](09-Workflow-Artifacts.md)
- [Next: 11-Workflow-Sharing →](11-Workflow-Sharing.md)
