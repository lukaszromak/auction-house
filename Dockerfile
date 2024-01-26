FROM maven:3.8.3-openjdk-17 AS build

WORKDIR /workspace/app

COPY ./front ./front
COPY ./pom.xml .
COPY ./src src

RUN mvn install -DskipTests -P dev -q

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /workspace/app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-Dspring.profiles.active=usersInDB","-jar","/app/app.jar"]