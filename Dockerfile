FROM maven:3.8.2-eclipse-temurin-17 AS build

COPY ./backend ..
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /backend/target/amigoscode-api-1.0-SNAPSHOT.jar amigoscode-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "amigoscode-api.jar"]