# Quiz Engine — Python

> Part of the [Quiz Engine multi-language collection](../README.md)

A Python/SQLite quiz engine for GH-200 GitHub Actions Certification preparation.

## Prerequisites

### Required Software

- **Python 3.8+** - [Download Python](https://www.python.org/downloads/)
- **pip** - Included with Python 3.4+
- **git** (Optional) - For cloning the repository

### Verifying Prerequisites

```bash
# Check Python version
python --version

# Check pip version
pip --version
```

## Setting Up Python

### Windows Installation

1. Download [Python](https://www.python.org/downloads/) (Latest stable release)
2. Run the installer
3. **Important**: Check "Add Python to PATH"
4. Click "Install Now" or customize installation
5. Verify installation:
   ```cmd
   python --version
   pip --version
   ```

### macOS Installation

#### Option 1: Using Homebrew (Recommended)

```bash
brew install python@3.11
```

#### Option 2: Using Official Installer

1. Download [Python](https://www.python.org/downloads/)
2. Run the installer
3. Verify installation:
   ```bash
   python3 --version
   pip3 --version
   ```

### Linux Installation

#### Debian/Ubuntu

```bash
sudo apt-get update
sudo apt-get install python3 python3-pip python3-venv
```

#### Fedora/RHEL

```bash
sudo dnf install python3 python3-pip
```

#### Verify Installation

```bash
python3 --version
pip3 --version
```

## Setting Up Virtual Environment (venv)

A virtual environment isolates project dependencies and prevents conflicts with system packages.

### Create Virtual Environment

#### Windows

```cmd
# Create virtual environment
python -m venv venv

# Activate virtual environment
venv\Scripts\activate
```

#### macOS/Linux

```bash
# Create virtual environment
python3 -m venv venv

# Activate virtual environment
source venv/bin/activate
```

### Verify Virtual Environment is Active

Your terminal prompt will show `(venv)` prefix:
```
(venv) $
```

### Deactivate Virtual Environment

When finished, deactivate the environment:

```bash
deactivate
```

## Setup Project Dependencies

After activating the virtual environment, install project dependencies:

```bash
# Install main dependencies
pip install -r requirements.txt

# Install development dependencies (for testing)
pip install -r requirements-dev.txt
```

**Note**: Always ensure the virtual environment is activated before running commands

## Import Questions

Ensure the virtual environment is activated before running:

```bash
# Activate venv if not already active
# Windows: venv\Scripts\activate
# macOS/Linux: source venv/bin/activate

python scripts/import_questions.py --dir /path/to/markdown/files
```

## Run Quiz

```bash
# Activate venv if not already active
# Windows: venv\Scripts\activate
# macOS/Linux: source venv/bin/activate

python -m quiz_engine.main --questions 20
```

## Run Tests

```bash
# Activate venv if not already active
# Windows: venv\Scripts\activate
# macOS/Linux: source venv/bin/activate

cd quiz-engine
python -m pytest tests/ -v
```

## View History

```bash
# Activate venv if not already active
# Windows: venv\Scripts\activate
# macOS/Linux: source venv/bin/activate

# View summary of quiz history
python scripts/view_history.py --summary

# Export history as CSV
python scripts/view_history.py --export csv --output history.csv
```

## Troubleshooting

### Common Issues

**Error**: `ModuleNotFoundError: No module named 'pip'`
- **Solution**: Ensure Python is installed correctly and added to PATH

**Error**: `command not found: python` (macOS/Linux)
- **Solution**: Use `python3` instead of `python`
- Or create an alias: `alias python=python3`

**Error**: `venv not activated`
- **Solution**: Run activation command for your OS:
  - Windows: `venv\Scripts\activate`
  - macOS/Linux: `source venv/bin/activate`

**Error**: `ModuleNotFoundError` after activating venv
- **Solution**: Install dependencies: `pip install -r requirements.txt`
