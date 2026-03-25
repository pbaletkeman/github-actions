#!/bin/bash
# scripts/check_coverage.sh
# Enforces a minimum 90% line coverage threshold.

set -euo pipefail

LCOV_FILE="coverage/lcov.info"

if [ ! -f "$LCOV_FILE" ]; then
  echo "ERROR: $LCOV_FILE not found. Run tests with coverage first."
  echo "  dart pub global activate coverage"
  echo "  dart test --coverage=coverage"
  echo "  dart pub global run coverage:format_coverage --lcov --in=coverage --out=coverage/lcov.info --report-on=lib"
  exit 1
fi

SUMMARY=$(lcov --summary "$LCOV_FILE" 2>&1)
COVERAGE=$(echo "$SUMMARY" | grep -i "lines" | awk '{print $2}' | tr -d '%')

if [ -z "$COVERAGE" ]; then
  echo "ERROR: Could not parse coverage percentage from lcov output."
  exit 1
fi

echo "Line coverage: ${COVERAGE}%"

if (( $(echo "$COVERAGE < 90" | bc -l) )); then
  echo "ERROR: Coverage ${COVERAGE}% is below the required 90%"
  exit 1
fi

echo "Coverage check passed: ${COVERAGE}%"
