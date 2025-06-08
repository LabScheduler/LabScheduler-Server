# Stage 1
FROM maven:3.9.9-eclipse-temurin-23 as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2
FROM eclipse-temurin:23-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]