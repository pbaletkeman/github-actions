# PowerShell Script to Push All Quiz Engine Prompt Files to GitHub
# Usage: .\push-prompts-to-github.ps1
# This script commits all 8 quiz engine prompt files and pushes them to GitHub

param(
    [string]$CommitMessage = "Update all 8 quiz engine prompt implementations with Docker/containerization support",
    [string]$Branch = "main"
)

Set-Location "c:\Users\Pete\Desktop\github-actions"

Write-Host "[PUSH] Starting GitHub push for all 8 quiz engine prompt files..." -ForegroundColor Cyan
Write-Host ""

# Define the 8 prompt files
$promptFiles = @(
    ".\.github\prompts\01-plan-quizEngine-python.prompt.md",
    ".\.github\prompts\02-plan-quizEngine-nodejs.prompt.md",
    ".\.github\prompts\03-plan-quizEngine-java.prompt.md",
    ".\.github\prompts\04-plan-quizEngine-springboot.prompt.md",
    ".\.github\prompts\05-plan-quizEngine-csharp.prompt.md",
    ".\.github\prompts\06-plan-quizEngine-dart.prompt.md",
    ".\.github\prompts\07-plan-quizEngine-golang.prompt.md",
    ".\.github\prompts\08-plan-quizEngine-rust.prompt.md",
    ".\.github\prompts\00-quiz-engine-meta-prompt.md",
    ".\.github\prompts\GITHUB-COPILOT-USAGE.md"
)

# Verify all files exist
Write-Host "[INFO] Verifying all prompt files exist..." -ForegroundColor Yellow
$missingFiles = @()
foreach ($file in $promptFiles) {
    if (Test-Path $file) {
        $size = (Get-Item $file).Length / 1KB
        Write-Host "[OK] $file ($([Math]::Round($size, 2)) KB)"
    } else {
        Write-Host "[FAIL] MISSING: $file" -ForegroundColor Red
        $missingFiles += $file
    }
}

if ($missingFiles.Count -gt 0) {
    Write-Host ""
    Write-Host "[ERROR] $($missingFiles.Count) file(s) missing. Cannot proceed." -ForegroundColor Red
    exit 1
}

Write-Host ""

# Add all files to git stage
Write-Host "[INFO] Staging all prompt files..." -ForegroundColor Yellow
foreach ($file in $promptFiles) {
    git add $file
    if ($LASTEXITCODE -eq 0) {
        Write-Host "[OK] Staged: $file"
    } else {
        Write-Host "[FAIL] Failed to stage: $file" -ForegroundColor Red
    }
}

Write-Host ""

# Check git status
Write-Host "[INFO] Current git status:" -ForegroundColor Yellow
git status

Write-Host ""

# Commit the changes
Write-Host "[INFO] Creating commit..." -ForegroundColor Yellow
Write-Host "[MSG] $CommitMessage" -ForegroundColor Cyan
Write-Host ""

git commit -m "$CommitMessage"

if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Commit failed. Exiting." -ForegroundColor Red
    exit 1
}

Write-Host ""

# Push to GitHub
Write-Host "[INFO] Pushing to GitHub (branch: $Branch)..." -ForegroundColor Yellow
git push origin $Branch

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "[SUCCESS] All prompt files have been pushed to GitHub!" -ForegroundColor Green
    Write-Host ""
    Write-Host "[REPO] https://github.com/pbaletkeman/github-actions" -ForegroundColor Cyan
    Write-Host "[BRANCH] $Branch" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "[NEXT] You can now access these files via GitHub Copilot web interface." -ForegroundColor Green
} else {
    Write-Host ""
    Write-Host "[ERROR] Push failed. Check your GitHub credentials and internet connection." -ForegroundColor Red
    exit 1
}
