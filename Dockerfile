FROM gradle:7.2.0-jdk11 AS build
LABEL maintainer="fernando_lomonaco@outlook.com"
RUN mkdir -p /workspace
WORKDIR /workspace
RUN echo "Copying build gradle"
COPY build.gradle.kts /workspace
RUN echo "Copying config and static files"
COPY config/docker /workspace/config
COPY static/banner-docker.txt /workspace/static
RUN echo "Copying source"
COPY src /workspace/src/
RUN gradle clean build -x test --no-daemon

FROM adoptopenjdk/openjdk11:alpine-jre
COPY --from=build /workspace/build/libs/*.jar sejni.jar
EXPOSE 8080
ENTRYPOINT  ["java", "-Djava.security.egd=file:/dev/./urandom --spring.config.location=classpath:file:/config/docker/", "-jar", "sejni.jar"]