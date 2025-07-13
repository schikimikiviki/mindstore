FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY .env /app/.env

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
