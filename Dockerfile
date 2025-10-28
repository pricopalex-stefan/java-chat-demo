FROM maven:3.9.11-eclipse-temurin-25-jammy as build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:25-jdk
COPY --from=build target/websocket-demo-0.01-SNAPSHOT.jar websocket-demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "websocket-demo.jar"]