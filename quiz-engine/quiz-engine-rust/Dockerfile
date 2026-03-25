# Build stage
FROM rust:1.75 AS builder

WORKDIR /app

RUN apt-get update && apt-get install -y libsqlite3-dev && rm -rf /var/lib/apt/lists/*

# Cache dependencies layer
COPY Cargo.toml Cargo.lock ./
RUN mkdir src && echo "fn main() {}" > src/main.rs && echo "" > src/lib.rs \
    && cargo build --release \
    && rm -rf src

COPY . .
RUN cargo build --release

# Runtime stage
FROM debian:bookworm-slim

WORKDIR /app

RUN apt-get update && apt-get install -y libsqlite3-0 && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/target/release/quiz_engine .
COPY --from=builder /app/migrations ./migrations

# Create non-root user
RUN useradd -m -u 1000 rustuser && chown -R rustuser:rustuser /app
USER rustuser

ENTRYPOINT ["./quiz_engine"]
CMD ["--help"]
