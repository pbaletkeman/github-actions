# Quiz Engine — Go

> Part of the [Quiz Engine multi-language collection](../README.md)

A CLI quiz engine for GitHub Actions certification practice.

## Prerequisites

### Required Software

- **Go 1.21+** - [Download Go](https://go.dev/dl)
- **C Compiler** - Required for CGO support
  - **Windows**: Visual C++ Build Tools or MinGW-w64
  - **macOS**: Xcode Command Line Tools (`xcode-select --install`)
  - **Linux**: GCC (`sudo apt-get install build-essential`)

### Verifying Prerequisites

```bash
# Check Go version
go version

# Check C compiler (Windows with MSVC)
cl /?

# Check C compiler (Windows with MinGW)
gcc --version
```

## Setup Build Environment

### Windows Setup

#### Option 1: Using Visual C++ Build Tools (Recommended)

1. Download [Visual C++ Build Tools](https://visualstudio.microsoft.com/downloads/)
2. Run the installer and select "Desktop development with C++"
3. Complete the installation
4. Restart your terminal/IDE

#### Option 2: Using MinGW-w64

1. Download [MinGW-w64](https://www.mingw-w64.org/)
2. Extract to a folder (e.g., `C:\mingw`)
3. Add to your PATH:
   ```cmd
   set PATH=C:\mingw\bin;%PATH%
   ```

### macOS/Linux Setup

```bash
# macOS
xcode-select --install

# Linux (Debian/Ubuntu)
sudo apt-get update
sudo apt-get install build-essential

# Linux (Fedora/RHEL)
sudo yum groupinstall "Development Tools"
```

## Building the Project

### Build Command

```bash
# Build the quiz engine executable
CGO_ENABLED=1 go build -o bin/quiz-engine .
```

**Note**: `CGO_ENABLED=1` is required for SQLite database support. The project uses SQLite for storing quiz questions and history.

### Verify Build

After building, verify the executable was created:

```bash
# Windows
.\bin\quiz-engine --help

# macOS/Linux
./bin/quiz-engine --help
```

## Running the Project

### Step 1: Import Quiz Questions

First, import questions from a markdown file:

```bash
./quiz-engine import --file questions.md
```

The markdown file should contain quiz questions in the expected format (see example below).

### Step 2: Start a Quiz

```bash
# Take a quiz with 20 questions
./quiz-engine quiz --questions 20
```

### Available Commands

```bash
# Import questions from file
./quiz-engine import --file questions.md

# Take a quiz with specified number of questions
./quiz-engine quiz --questions 20

# View quiz history and results
./quiz-engine history

# Clear all stored data (includes confirmation prompt)
./quiz-engine clear --confirm
```

## Usage

### Import questions
```bash
./quiz-engine import --file questions.md
```

### Take a quiz
```bash
./quiz-engine quiz --questions 20
```

### View history
```bash
./quiz-engine history
```

### Clear data
```bash
./quiz-engine clear --confirm
```

## Building

### Build the Executable

```bash
CGO_ENABLED=1 go build -o bin/quiz-engine .
```

The executable will be created in the `bin/` directory:
- **Windows**: `bin\quiz-engine.exe`
- **macOS/Linux**: `bin/quiz-engine`

### Troubleshooting Build Issues

**Error**: `cc1.exe: fatal error: sqlite3.h: No such file or directory`
- **Solution**: Install C compiler (Visual C++ Build Tools or MinGW-w64)

**Error**: `CGO not supported`
- **Solution**: Ensure Go is properly installed and `CGO_ENABLED=1` is set

## Testing

### Run All Tests

```bash
CGO_ENABLED=1 go test ./...
```

### Run Tests with Verbose Output

```bash
CGO_ENABLED=1 go test -v ./...
```

### Run Specific Test

```bash
CGO_ENABLED=1 go test -run TestName ./...
