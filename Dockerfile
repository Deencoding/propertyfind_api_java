# This is a multi-stage Dockerfile
# Stage 1: Build the application (we don't need to keep build tools in final image)
FROM maven:3.9-eclipse-temurin-21 AS build

# Set working directory inside container
WORKDIR /app

# Copy pom.xml first (this helps Docker cache dependencies)
# If pom.xml doesn't change, Docker won't re-download dependencies
COPY pom.xml .

# Download dependencies (this layer will be cached)
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the application (skip tests for faster builds, run tests separately)
RUN mvn clean package -DskipTests

# Stage 2: Create the final lightweight image
# We use a smaller JRE image (no Maven, no source code)
FROM eclipse-temurin:21-jre

# Set working directory
WORKDIR /app

# Copy only the built JAR file from the build stage
# This makes the final image much smaller
COPY --from=build /app/target/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Command to run the application
# Using shell form to allow environment variable expansion
ENTRYPOINT ["java", "-jar", "app.jar"]
