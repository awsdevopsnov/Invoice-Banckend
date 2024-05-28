FROM ubuntu:20.04

# Set working directory
RUN apt-get update && apt-get install -y maven

RUN apt-get install openjdk-17-jdk -y

WORKDIR /app

COPY . .

# Copy application JAR
RUN mvn clean install -DskipTests

# Expose port for application
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "./target/AdminInvoice-0.0.1-SNAPSHOT.jar"]
