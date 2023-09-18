FROM openjdk:17-alpine3.14
LABEL maintainer=miha
WORKDIR /app
COPY #JAR_NAME# ./
COPY #OPEN_TELEMETRY_JAR# ./
ENTRYPOINT ["java", "-javaagent:#OPEN_TELEMETRY_JAR#", "-jar", "#JAR_NAME#"]
#EXPOSE_PORT#