FROM maven:3.9.6-eclipse-temurin-8 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:8-jre
WORKDIR /app
COPY --from=build /app/target/app.jar app.jar
RUN mkdir -p /data/uploads
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
