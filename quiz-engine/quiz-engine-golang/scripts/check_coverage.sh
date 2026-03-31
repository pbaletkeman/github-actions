#!/usr/bin/env bash
# Run tests and enforce a minimum 90% coverage threshold.
set -e

echo "Running tests with coverage..."
CGO_ENABLED=1 go test ./... -coverprofile=coverage.out -covermode=atomic

echo "Coverage summary:"
go tool cover -func=coverage.out

TOTAL=$(go tool cover -func=coverage.out | grep '^total:' | awk '{print $3}' | tr -d '%')

echo ""
echo "Total coverage: ${TOTAL}%"

# Use awk for floating-point comparison
if awk "BEGIN { exit (${TOTAL} < 90) ? 0 : 1 }"; then
  echo "ERROR: Coverage ${TOTAL}% is below the required 90% threshold."
  exit 1
else
  echo "Coverage check passed (${TOTAL}% >= 90%)."
fi
