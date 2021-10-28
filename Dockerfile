FROM gradle:7.2.0-jdk11 as builder
LABEL maintainer="fernando_lomonaco@outlook.com"
WORKDIR /app
COPY . .
RUN ./gradlew build --stacktrace

# final stage/image
FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /app
EXPOSE 8080
COPY --from=builder /app/build/libs/sejni*.jar .
CMD ["java", "-jar", "sejni-0.0.1-SNAPSHOT.jar"]
