ARG STUDENT_ID

FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Dependencias cacheadas antes del codigo fuente: los rebuilds no las vuelven a descargar.
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

ARG STUDENT_ID
RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:17-jre-alpine

RUN addgroup -g 10001 sensorgroup \
 && adduser -u 10001 -G sensorgroup -D -h /app sensor_user

WORKDIR /app

COPY --from=builder /app/target/eco-air-backend.jar app.jar

# MaxRAMPercentage: respeta el limite del cgroup. ExitOnOutOfMemoryError: muere limpio ante OOM.
ENV JAVA_OPTS="-XX:InitialRAMPercentage=50 -XX:MaxRAMPercentage=75 -XX:+UseG1GC -XX:+ExitOnOutOfMemoryError -Djava.security.egd=file:/dev/./urandom"

ARG STUDENT_ID
ENV OPERATOR_ID=${STUDENT_ID}

RUN chown -R sensor_user:sensorgroup /app

USER sensor_user

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]

# docker build --build-arg STUDENT_ID="871752" -t eco-air-backend:1.0.0 .
