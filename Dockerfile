# Stage 1: Build the application
FROM eclipse-temurin:17-jdk-focal as builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-focal
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Set environment variables (override in Render)
ENV PORT=8055
EXPOSE $PORT

ENTRYPOINT ["java", "-jar", "app.jar"]