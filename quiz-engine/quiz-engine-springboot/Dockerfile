# Build stage
FROM gradle:8-jdk17 AS builder

WORKDIR /app

COPY . .

RUN gradle build -x test --no-daemon

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

RUN addgroup -g 1000 springuser && adduser -D -u 1000 -G springuser springuser
RUN chown -R springuser:springuser /app
USER springuser

ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0 -Dserver.port=8080"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
