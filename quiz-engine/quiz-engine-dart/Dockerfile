# Build stage
FROM dart:3.0 AS builder

WORKDIR /app

COPY pubspec.* ./
RUN dart pub get

COPY . .
RUN dart compile exe lib/main.dart -o bin/quiz-engine

# Runtime stage
FROM alpine:latest

WORKDIR /app

RUN apk add --no-cache libc6-compat sqlite-libs

COPY --from=builder /app/bin/quiz-engine .

# Create non-root user for security
RUN addgroup -g 1000 dartuser && \
    adduser -D -u 1000 -G dartuser dartuser && \
    chown -R dartuser:dartuser /app

USER dartuser

ENTRYPOINT ["./quiz-engine"]
CMD ["--help"]
