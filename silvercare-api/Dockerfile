# Multi-stage Dockerfile for silvercare-api (Spring Boot REST API)
# Advanced Feature D: Docker containerization

# Stage 1: Build the JAR file
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the Spring Boot application
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder /app/target/silvercare-api-*.jar app.jar

# Expose port 8081 (Spring Boot default for our API)
EXPOSE 8081

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8081/actuator/health || exit 1

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
