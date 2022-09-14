# syntax = docker/dockerfile:experimental

FROM openjdk:11-jdk-slim as bulid

WORKDIR application

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN chmod 777 mvnw
RUN --mount=type=cache,target=/root/.m2 ./mvnw clean install -U

RUN cp /application/target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract


RUN addgroup sngroup && adduser  --ingroup sngroup --disabled-password sn
USER sn

FROM openjdk:11-jdk-slim

WORKDIR application

COPY --from=bulid application/dependencies/ ./
COPY --from=bulid application/spring-boot-loader/ ./
COPY --from=bulid application/snapshot-dependencies/ ./
COPY --from=bulid application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]