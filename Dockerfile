ARG BASE_IMAGE=ghcr.io/ministryofjustice/hmpps-eclipse-temurin:25-jre-jammy

FROM --platform=$BUILDPLATFORM eclipse-temurin:25-jdk-jammy AS builder

ARG BUILD_NUMBER
ENV BUILD_NUMBER=${BUILD_NUMBER:-1_0_0}

WORKDIR /app
COPY . .
RUN ./gradlew --no-daemon assemble

FROM ${BASE_IMAGE}

USER root

RUN mkdir -p /home/appuser/.postgresql

ADD --chown=appuser:appgroup \
    https://s3.amazonaws.com/rds-downloads/rds-ca-2019-root.pem \
    /home/appuser/.postgresql/root.crt

WORKDIR /app

COPY --from=builder --chown=appuser:appgroup /app/build/libs/hmpps-offender-categorisation-api*.jar /app/app.jar
COPY --from=builder --chown=appuser:appgroup /app/build/libs/applicationinsights-agent*.jar /app/agent.jar
COPY --from=builder --chown=appuser:appgroup /app/applicationinsights.json /app
COPY --from=builder --chown=appuser:appgroup /app/applicationinsights.dev.json /app

ENTRYPOINT ["java", "-XX:+AlwaysActAsServerClassMachine", "-javaagent:/app/agent.jar", "-jar", "/app/app.jar"]