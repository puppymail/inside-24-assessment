# syntax=docker/dockerfile:1

FROM openjdk:16-alpine3.13
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw
RUN ["./mvnw", "dependency:go-offline"]
COPY src src/
COPY curl curl/
COPY flyway.conf ./
RUN ["apk", "add", "--no-cache", "--upgrade", "grep"]
RUN ["apk", "add", "--no-cache", "--upgrade", "curl"]
RUN ["./mvnw", "compile"]
RUN ["./mvnw", "test"]
EXPOSE 7000
CMD ["./mvnw", "spring-boot:run"]