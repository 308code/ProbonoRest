FROM maven:3-openjdk-17-slim AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app/pom.xml
RUN mvn -f /usr/src/app/pom.xml clean package -DskipTest

FROM openjdk:17-slim
COPY --from=build /usr/src/app/target/ProbonoRest-0.0.1-SNAPSHOT.jar /usr/app/ProbonoRest-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/ProbonoRest-0.0.1-SNAPSHOT.jar"]
