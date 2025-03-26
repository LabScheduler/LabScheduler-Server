# Stage 1
FROM maven:3.9.4-eclipse-temurin-21 as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests -T 1C

# Stage 2
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/LabScheduler-Server-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]