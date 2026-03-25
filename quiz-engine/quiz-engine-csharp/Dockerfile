# Build stage
FROM mcr.microsoft.com/dotnet/sdk:8.0 AS builder

WORKDIR /app

# Copy solution and project files first for layer caching
COPY QuizEngine.sln .
COPY QuizEngine.Entities/QuizEngine.Entities.csproj QuizEngine.Entities/
COPY QuizEngine.Data/QuizEngine.Data.csproj QuizEngine.Data/
COPY QuizEngine.Service/QuizEngine.Service.csproj QuizEngine.Service/
COPY QuizEngine.CLI/QuizEngine.CLI.csproj QuizEngine.CLI/
COPY QuizEngine.Tests/QuizEngine.Tests.csproj QuizEngine.Tests/

# Restore dependencies
RUN dotnet restore

# Copy all source files
COPY . .

# Build in Release mode
RUN dotnet build -c Release --no-restore

# Publish CLI project
RUN dotnet publish QuizEngine.CLI/QuizEngine.CLI.csproj -c Release -o /app/publish --no-build

# Runtime stage
FROM mcr.microsoft.com/dotnet/runtime:8.0 AS runtime

WORKDIR /app

# Copy published output
COPY --from=builder /app/publish .

# Create non-root user for security
RUN useradd -m -u 1000 dotnetuser && \
    mkdir -p /data && \
    chown -R dotnetuser:dotnetuser /app /data

USER dotnetuser

# Store DB in a writable data directory
ENV QUIZ_DB_PATH=/data/quiz.db

VOLUME ["/data"]

ENTRYPOINT ["dotnet", "QuizEngine.CLI.dll"]
CMD ["--help"]
