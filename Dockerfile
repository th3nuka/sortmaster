# Multi-stage Docker build for SortMaster DSA Academy
# Stage 1: Build the standalone Spring Boot JAR
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Minimal runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Non-root user for security
RUN addgroup -S sortmaster && adduser -S sortmaster -G sortmaster
USER sortmaster:sortmaster

COPY --from=builder /workspace/target/sortmaster-academy-1.0.0.jar app.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
