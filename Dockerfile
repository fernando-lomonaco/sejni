FROM adoptopenjdk/openjdk11:alpine-jre
LABEL maintainer="fernando_lomonaco@outlook.com"
# The application's jar file
ARG JAR_FILE=build/libs/sejni-0.0.1-SNAPSHOT.jar
RUN echo "Copying the Sejni jar to the container..."
COPY ${JAR_FILE} sejni.jar
RUN echo "Copying config and static files"
COPY config/docker ./config/
COPY static/banner-docker.txt ./static/
# Make port 8080 available to the world outside this container
EXPOSE 8080
ENTRYPOINT  ["java", "-Djava.security.egd=file:/dev/./urandom --spring.config.location=classpath:file:/config/docker/", "-jar", "sejni.jar"]
