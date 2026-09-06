# Stage 1: Build the JAR with Maven using Java 21
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build

# Copy pom.xml and cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Lightweight Java 21 runtime image
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Create non-root user, data directory, and set permissions in a single layer
RUN addgroup -S appgroup && \
    adduser -S appuser -G appgroup && \
    mkdir -p /app/data && \
    chown -R appuser:appgroup /app

USER appuser:appgroup

# Copy the built JAR file
COPY --chown=appuser:appgroup --from=builder /build/target/*.jar app.jar

# JVM tuning for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Render provides a dynamic PORT environment variable
ENV PORT=8080
EXPOSE $PORT

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=$PORT -jar app.jar"]