FROM openjdk:17.0-jdk as build

MAINTAINER abd_qadr

EXPOSE 8090

COPY target/bank_api-1.0.jar bank_api-1.0.jar

ENTRYPOINT ["java", "-jar", "bank_api-1.0.jar"]