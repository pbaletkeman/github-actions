.PHONY: build run test coverage clean docker-build

build:
	CGO_ENABLED=1 go build -o bin/quiz-engine .

run:
	go run . quiz --questions 10

test:
	CGO_ENABLED=1 go test ./... -coverprofile=coverage.out -covermode=atomic
	go tool cover -func=coverage.out
	@TOTAL=$$(CGO_ENABLED=1 go test \
	  ./internal/database/... \
	  ./internal/engine/... \
	  ./internal/parser/... \
	  ./internal/service/... \
	  -coverprofile=coverage_core.out -covermode=atomic 2>/dev/null && \
	  go tool cover -func=coverage_core.out | grep "^total" | awk '{print $$3}' | tr -d '%'); \
	echo "Core coverage: $${TOTAL}%"; \
	awk "BEGIN { if ($${TOTAL}+0 < 90) { print \"FAIL: coverage below 90%\"; exit 1 } else { print \"PASS: coverage $${TOTAL}%\" } }"

coverage:
	CGO_ENABLED=1 go test ./... -coverprofile=coverage.out -covermode=atomic
	go tool cover -html=coverage.out -o coverage.html

clean:
	rm -rf bin/ coverage.out coverage.html quiz.db

docker-build:
	docker build -t quiz-engine:latest .
