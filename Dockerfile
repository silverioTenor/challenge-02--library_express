# ============================================================================
# Stage 1: Build the multi-module Fat JAR compiler environment
# ============================================================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Copy the entire parent descriptor and modules tree into the build container
COPY . .

# Triggers compilation and forces the shade plugin execution inside infrastructure module
RUN mvn clean package -DskipTests

# ============================================================================
# Stage 2: Ultra-lightweight, secure runtime environment
# ============================================================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy strictly the final bundled Fat JAR from builder stage
COPY --from=builder /app/infrastructure/target/infrastructure-*.jar app.jar

# Application layer exposure configurations
EXPOSE 8080

# Secure application entrypoint execution mapping
ENTRYPOINT ["java", "-jar", "app.jar"]
