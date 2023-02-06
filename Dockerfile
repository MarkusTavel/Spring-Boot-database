FROM adoptopenjdk/openjdk11:alpine-jre

# Refer to Maven build -> finalName
ARG JAR_FILE=demo-0.0.1-SNAPSHOT.jar

# cd /opt/app
WORKDIR /app

COPY mvnw pom.xml ./
COPY ${JAR_FILE} app.jar
COPY values.json /app/
COPY src ./src

ENTRYPOINT ["java","-jar","app.jar"]
