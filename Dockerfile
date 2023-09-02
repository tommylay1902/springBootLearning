
FROM maven:3.8.2-eclipse-temurin-17 AS build

# Set default values for build arguments
ARG DATABASE_URL=default-database-url
ARG DATABASE_USERNAME=default-username
ARG DATABASE_PASSWORD=default-password
ARG SPRING_PROFILES_ACTIVE=default-profile

# Use the build arguments to set environment variables
ENV DATABASE_URL=$DATABASE_URL
ENV DATABASE_USERNAME=$DATABASE_USERNAME
ENV DATABASE_PASSWORD=$DATABASE_PASSWORD
ENV SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE




COPY ./backend ..
RUN mvn clean verify -DskipTests -Djib.to.auth.password=Tommyray15! -Djib.to.auth.username=tommylay1902

FROM openjdk:17.0.1-jdk-slim
COPY --from=build ./backend/target/amigoscode-api-1.0-SNAPSHOT.jar amigoscode-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "amigoscode-api.jar"]