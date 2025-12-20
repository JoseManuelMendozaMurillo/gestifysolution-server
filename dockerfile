# Image based on Red Hat 
FROM maven:3.9.8-amazoncorretto-21-al2023

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project into the container
COPY . .

# Build the project (download dependencies, compile, etc.)
RUN mvn clean install -DskipTests

# Start the Spring Boot application
CMD ["mvn", "spring-boot:run"]