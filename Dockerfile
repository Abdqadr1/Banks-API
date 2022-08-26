FROM openjdk:17.0-jdk as build

MAINTAINER abd_qadr

EXPOSE 8090

COPY target/api-1.0.jar api-1.0.jar

ENTRYPOINT ["java", "-jar", "api-1.0.jar"]