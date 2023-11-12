FROM openjdk:17-jdk-alpine

ADD target/git-explorer-service-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080