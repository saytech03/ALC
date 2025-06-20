# Stage 1: Build with JDK
FROM eclipse-temurin:17-jdk-focal as builder

# Set working directory
WORKDIR /app

# 1. Copy build files first for layer caching
COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .

# 2. Ensure mvnw is executable (fixes permission denied error)
RUN chmod +x mvnw

# 3. Download dependencies (faster rebuilds)
RUN ./mvnw dependency:go-offline

# 4. Copy source code
COPY src ./src

# 5. Build with skipTests (remove -DskipTests in production)
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime with JRE
FROM eclipse-temurin:17-jre-focal

# 1. Set workdir and copy built jar
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# 2. Dynamic port binding for Render
ENV PORT=8055
EXPOSE $PORT

# 3. Health check configuration (for Render monitoring)
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:$PORT/actuator/health || exit 1

# 4. Startup command (with JVM optimizations)
ENTRYPOINT ["java", "-jar", "-Dserver.port=${PORT}", "app.jar"]