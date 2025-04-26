FROM openjdk:21-jdk-slim

LABEL authors="Azamat"

WORKDIR /app

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
