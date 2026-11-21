# GraalVM 25 supports the reachability-metadata schema used by the current
# Spring Boot dependency set. The application itself still targets Java 21.
FROM ghcr.io/graalvm/native-image-community:25 AS builder
WORKDIR /build

# Maven is not included in the Native Image builder.
RUN microdnf install -y maven findutils && microdnf clean all

COPY pom.xml .
COPY src ./src

# Native Image also needs memory beyond its Java heap. Keep both heap and
# compiler parallelism within the memory available to the deployment builder.
ENV NATIVE_IMAGE_OPTIONS="-J-Xmx7g --parallelism=2"
RUN mvn --batch-mode --no-transfer-progress -Pnative -DskipTests native:compile

# The GraalVM builder produces a glibc-linked executable. Use the matching
# Oracle Linux runtime instead of Alpine (which uses musl libc).
FROM oraclelinux:10-slim
WORKDIR /app

RUN microdnf install -y ca-certificates && \
    microdnf clean all && \
    groupadd --system appgroup && \
    useradd --system --gid appgroup --home-dir /app --no-create-home appuser && \
    mkdir -p /app/data && \
    chown -R appuser:appgroup /app

COPY --chown=appuser:appgroup --from=builder /build/target/portfolio-api app

USER appuser:appgroup

# Spring resolves this value at runtime (server.port=${PORT:8080}).
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["./app"]
