## Use an official OpenJDK runtime as a base image
FROM openjdk:17-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the generated JAR file into the container
COPY target/loan-management-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose port 8080 for the Spring Boot application
EXPOSE 8080

# Run the JAR file
CMD ["java", "-jar", "app.jar"]



