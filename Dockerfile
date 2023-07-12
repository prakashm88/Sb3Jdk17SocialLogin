# Stage 1: Build the Maven project with a builder image
FROM maven:3.9.2-amazoncorretto-17 AS builder
LABEL maintainer="admin@itechgenie.com"
WORKDIR /application
COPY pom.xml .
#RUN mvn dependency:go-offline
RUN mvn dependency:resolve
COPY src ./src
RUN mvn clean package -DskipTests
COPY target/*.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

# Stage 2: create an image with the content of the jar
FROM amazoncorretto:17-alpine-jdk
WORKDIR /application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]