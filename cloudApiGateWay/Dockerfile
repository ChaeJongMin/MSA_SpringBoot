FROM openjdk:21-ea-25-jdk-slim
VOLUME /tmp
COPY target/cloudApiGateWay-1.0.jar ApiGateWay.jar
ENTRYPOINT ["java", "-jar", "ApiGateWay.jar"]