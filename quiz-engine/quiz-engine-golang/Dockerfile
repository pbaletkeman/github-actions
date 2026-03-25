# Build stage
FROM golang:1.21-alpine AS builder

WORKDIR /app

RUN apk add --no-cache gcc musl-dev sqlite-dev

COPY go.mod go.sum ./
RUN go mod download

COPY . .
RUN CGO_ENABLED=1 GOOS=linux go build -a -installsuffix cgo -ldflags "-s -w" -o quiz-engine .

# Runtime stage
FROM alpine:latest

WORKDIR /app

RUN apk add --no-cache sqlite-libs

COPY --from=builder /app/quiz-engine .

RUN addgroup -g 1000 gouser && adduser -D -u 1000 -G gouser gouser
RUN chown -R gouser:gouser /app
USER gouser

ENTRYPOINT ["./quiz-engine"]
CMD ["--help"]
