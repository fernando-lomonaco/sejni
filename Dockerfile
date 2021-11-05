FROM gradle:7.2.0-jdk11 AS builder
LABEL maintainer="fernando_lomonaco@outlook.com"
WORKDIR /home/root/
COPY . .
RUN gradle build -x test

FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /home/root/app/
RUN echo "Copying config and static files"
COPY config/docker /home/root/app/config/
COPY static/banner-docker.txt /home/root/app/static/
RUN echo "Copying jar file"
COPY --from=builder /home/root/build/libs/sejni-0.0.1-SNAPSHOT.jar /home/root/app/
EXPOSE 8080
ENTRYPOINT  ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "sejni-0.0.1-SNAPSHOT.jar", "--spring.config.location=classpath:file:./config/"]