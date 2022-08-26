FROM openjdk:17.0-jdk as build

MAINTAINER abd_qadr

EXPOSE 8090

COPY target/banks_api.jar banks_api.jar

ENTRYPOINT ["java", "-jar", "banks_api.jar"]