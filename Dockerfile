# Stage 1: Builder (same as before)
FROM eclipse-temurin:17-jdk-focal as builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw .
RUN chmod +x mvnw
COPY pom.xml .
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-focal

# Install CA certificates for MongoDB SSL
RUN apt-get update && \
    apt-get install -y ca-certificates && \
    update-ca-certificates

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Environment variables
ENV PORT=8055
EXPOSE $PORT

# Health check
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:$PORT/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]