FROM maven:3.8.2-eclipse-temurin-17 AS build

COPY ./backend ..
RUN mvn clean verify -DskipTests -Djib.to.auth.password=${DOCKERHUB_ACCESS_TOKEN} -Djib.to.auth.username=${DOCKERHUB_USERNAME}

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /backend/target/amigoscode-api-1.0-SNAPSHOT.jar amigoscode-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "amigoscode-api.jar"]