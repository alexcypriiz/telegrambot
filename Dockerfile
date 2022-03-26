FROM maven:3.8.4-openjdk-17-slim as builder

COPY src/ /opt/src
COPY pom.xml /opt/

WORKDIR /opt/

RUN mvn clean package

FROM openjdk:17-jdk-slim

COPY --from=builder /opt/target/telegrambot-0.0.1-SNAPSHOT.jar /opt/
COPY StickerFiles /opt/

CMD [ "java", "-jar", "/opt/telegrambot-0.0.1-SNAPSHOT.jar" ]