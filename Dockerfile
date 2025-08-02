FROM ghcr.io/graalvm/native-image-community:21 AS build

WORKDIR /workspace

COPY gradlew *.gradle.kts ./
COPY gradle gradle
COPY src src

ARG version

RUN microdnf install findutils && \
    ./gradlew -Pversion=$version nativeCompile

FROM ubuntu:noble

COPY --from=build /workspace/build/native/nativeCompile/ /workspace/

ENTRYPOINT ["/workspace/api", "--spring.profiles.active=prd"]
