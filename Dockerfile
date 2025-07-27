FROM ghcr.io/graalvm/native-image-community:21 AS build

WORKDIR /workspace

COPY gradlew *.gradle.kts ./
COPY gradle gradle
COPY src src

RUN ./gradlew nativeCompile

FROM ubuntu:noble

WORKDIR /workspace

COPY --from=build /workspace/build/api /api

ENTRYPOINT ["/api"]
