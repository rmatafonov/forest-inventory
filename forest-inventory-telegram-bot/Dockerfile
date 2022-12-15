FROM amazoncorretto:17-alpine

WORKDIR /usr/local/

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ./lib/app.jar
COPY wait-for.sh ./bin/
RUN chmod +x ./bin/wait-for.sh
