FROM openjdk:21-ea-25-jdk-slim
VOLUME /tmp
COPY target/UserMicroService-1.0.jar UserService.jar
ENTRYPOINT ["java", "-jar", "UserService.jar"]