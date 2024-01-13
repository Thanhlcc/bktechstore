FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

COPY gradle .
COPY .gradle .gradle
COPY build.gradle.kts .
COPY src src

RUN gradle clean bootJar -x test
RUN mkdir -p build/libs/dependency && (cd build/libs/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY run.sh .
ARG DEPENDENCY=build/libs/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["/run.sh"]