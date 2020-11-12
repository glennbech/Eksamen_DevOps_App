FROM maven:3.6-jdk-11 as builder

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build a release artifact.
RUN mvn package -DskipTests




FROM adoptopenjdk/openjdk11:alpine-slim
# Copy the jar to the production image from the builder stage.
COPY --from=builder /app/target/Eksamen-*.jar /Eksamen-0.0.1-SNAPSHOT.jar

# Run the web service on container startup.
CMD ["java", "-Djava.security.egdls =file:/dev/./urandom", "-jar", "/Eksamen-0.0.1-SNAPSHOT.jar"]