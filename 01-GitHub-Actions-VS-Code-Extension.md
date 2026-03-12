[← Back to Index](INDEX.md)

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



---

## Navigation

- [← Back to Index](INDEX.md)
- [Next: 02-Contextual-Information →](02-Contextual-Information.md)
