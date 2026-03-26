# Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Cache dependencies first
COPY pom.xml ./
RUN mvn -B -DskipTests dependency:go-offline

# Build application
COPY src ./src
RUN mvn -B -DskipTests clean package

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy built Spring Boot jar
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
