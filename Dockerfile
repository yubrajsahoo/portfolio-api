# Stage 1: Build GraalVM Native Image
FROM ghcr.io/graalvm/native-image-community:21 AS builder
WORKDIR /build

# Install Maven and dependencies
RUN microdnf install -y maven findutils

# Copy Maven descriptor and source code
COPY pom.xml .
COPY src ./src

# Compile GraalVM native binary using the native profile
RUN mvn -Pnative native:compile -DskipTests

# Stage 2: Ultra-minimal Alpine runtime image (no JVM required)
FROM alpine:3.20
WORKDIR /app

# Install libc compatibility and certificates for SSL/TLS connections
RUN apk add --no-cache libc6-compat ca-certificates && \
    addgroup -S appgroup && \
    adduser -S appuser -G appgroup && \
    mkdir -p /app/data && \
    chown -R appuser:appgroup /app

USER appuser:appgroup

# Copy compiled native binary
COPY --chown=appuser:appgroup --from=builder /build/target/portfolio-api app

# Render dynamic port configuration
ENV PORT=8080
EXPOSE $PORT

ENTRYPOINT ["./app"]