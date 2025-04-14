FROM eclipse-temurin:21-jdk

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradlew .

RUN ./gradlew --no-daemon dependencies

COPY src src
COPY config config

RUN ./gradlew --no-daemon build
RUN chmod +x /app/build/install/App/bin/App
CMD ["app/build/install/App/bin/App"]
