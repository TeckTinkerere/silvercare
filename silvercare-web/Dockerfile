# Multi-stage Dockerfile for silvercare-web (Tomcat + JSP/Servlet)
# Advanced Feature D: Docker containerization

# Stage 1: Build the WAR file
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build WAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Deploy to Tomcat
FROM tomcat:10.1-jdk21-temurin-jammy

# Remove default Tomcat webapps and disable shutdown port (8005) to prevent Render health check log spam
RUN rm -rf /usr/local/tomcat/webapps/* && \
    sed -i 's/port="8005" shutdown="SHUTDOWN"/port="-1" shutdown="SHUTDOWN"/g' /usr/local/tomcat/conf/server.xml

# Copy WAR from builder stage
COPY --from=builder /app/target/silvercare-web.war /usr/local/tomcat/webapps/ROOT.war

# Expose port 8080 (Tomcat default)
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/ || exit 1

# Start Tomcat
CMD ["catalina.sh", "run"]
