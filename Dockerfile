FROM gradle:7.2.0-jdk11 as builder
LABEL maintainer="fernando_lomonaco@outlook.com"
WORKDIR /app
COPY . .
RUN ./gradlew build --stacktrace

FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /app
EXPOSE 80
COPY --from=builder /app/build/libs/sejni-0.0.1-SNAPSHOT.jar .
CMD java -jar sejni-0.0.1-SNAPSHOT.jar