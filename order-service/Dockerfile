# --- Stage 1: Build Phase ---
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Cache Maven dependencies aggressively
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and compile the JAR
COPY src ./src
RUN mvn clean package -DskipTests

# --- Stage 2: Runtime Phase ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the compiled executable JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Match the port specified in your properties and docker-compose
EXPOSE 8082

# Production-ready JVM flag optimizations
ENTRYPOINT ["java", "-XX:+UseG1GC", "-jar", "app.jar"]