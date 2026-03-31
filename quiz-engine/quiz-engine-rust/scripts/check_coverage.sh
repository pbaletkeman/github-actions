#!/usr/bin/env bash
# Run tests and enforce a minimum 90% coverage threshold.
# Requires cargo-llvm-cov; falls back to cargo-tarpaulin if unavailable.
set -e

THRESHOLD=90

if command -v cargo-llvm-cov &>/dev/null || cargo llvm-cov --version &>/dev/null 2>&1; then
    echo "Using cargo-llvm-cov..."
    OUTPUT=$(cargo llvm-cov --summary-only 2>&1)
    echo "$OUTPUT"

    # Parse the TOTAL line, e.g.: TOTAL  ... 91.23%
    TOTAL=$(echo "$OUTPUT" | grep -i '^TOTAL' | awk '{print $NF}' | tr -d '%')
elif command -v cargo-tarpaulin &>/dev/null || cargo tarpaulin --version &>/dev/null 2>&1; then
    echo "cargo-llvm-cov not found, using cargo-tarpaulin..."
    cargo tarpaulin --fail-under ${THRESHOLD}
    echo "Coverage check passed (tarpaulin enforced ${THRESHOLD}%)."
    exit 0
else
    echo "ERROR: Neither cargo-llvm-cov nor cargo-tarpaulin is installed."
    echo "Install one with:"
    echo "  cargo install cargo-llvm-cov"
    echo "  cargo install cargo-tarpaulin"
    exit 1
fi

echo ""
echo "Total coverage: ${TOTAL}%"

if awk "BEGIN { exit (${TOTAL} < ${THRESHOLD}) ? 0 : 1 }"; then
    echo "ERROR: Coverage ${TOTAL}% is below the required ${THRESHOLD}% threshold."
    exit 1
else
    echo "Coverage check passed (${TOTAL}% >= ${THRESHOLD}%)."
fi
