FROM adoptopenjdk/openjdk16:alpine-jre
ARG JAR_FILE=target/bookshop-0.0.1-SNAPSHOT.war
WORKDIR /opt/app
COPY ${JAR_FILE} app.war
ENTRYPOINT ["java","-jar","app.war"]