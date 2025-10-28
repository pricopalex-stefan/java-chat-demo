FROM maven:3.9.11-eclipse-temurin-24-jammy as build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:24-jdk
COPY --from=build /target/websocket_demo-0.01-SNAPSHOT.jar websocket_demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "websocket_demo.jar"]
