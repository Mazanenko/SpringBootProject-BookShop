#FROM tomcat:10-jdk16
#ADD target/firstSpringCRUDApp-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/firstSpringCRUDApp-0.0.1-SNAPSHOT.war
#EXPOSE 8081
#CMD ["catalina.sh", "run"]

FROM adoptopenjdk/openjdk16:alpine-jre
ARG JAR_FILE=target/firstSpringCRUDApp-0.0.1-SNAPSHOT.war
WORKDIR /opt/app
COPY ${JAR_FILE} app.war
ENTRYPOINT ["java","-jar","app.war"]