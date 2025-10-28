FROM maven:3.9.11-eclipse-temurin-25 as build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:24-jdk
COPY --from=build /target/websocket_demo-0.0.1-SNAPSHOT.jar websocket_demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "websocket_demo.jar"]
