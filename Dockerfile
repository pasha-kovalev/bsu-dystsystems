FROM openjdk:17-jdk-slim

COPY build/libs/counter-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
