FROM openjdk:21-ea-25-jdk-slim
VOLUME /tmp
COPY target/discoveryService-1.0.jar DiscoveryServer.jar
ENTRYPOINT ["java", "-jar", "DiscoveryServer.jar"]