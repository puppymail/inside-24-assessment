# syntax=docker/dockerfile:1

FROM openjdk:16-alpine3.13
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ["./mvnw", "dependency:go-offline"]
COPY src ./src
COPY curl ./curl
COPY flyway.conf README.md ./
RUN ["./mvnw", "compile"]
RUN ["./mvnw", "test"]
CMD ["./mvnw", "spring-boot:run"]