# Use Java 21 JRE as base runtime image
FROM eclipse-temurin:21-jre-jammy AS runtime
LABEL maintainer="nurudeen"

# Set timezone
ENV TZ="Africa/Lagos"

# Create application directories
RUN mkdir -p /app/logs

# Create non-root user for security
RUN addgroup --system spring && adduser --system spring --ingroup spring
RUN chown -R spring:spring /app

WORKDIR /app

# Copy the jar file built by the Jenkins agent
COPY target/*.jar app.jar

# Change ownership
RUN chown spring:spring app.jar

USER spring

# Spring Boot default port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD test -f /app/app.jar || exit 1

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
