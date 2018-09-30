FROM java:8
EXPOSE 8080
VOLUME /tmp
COPY target/subscribers-manager-0.0.1-SNAPSHOT.one-jar.jar /opt/app.jar
WORKDIR /opt
CMD java -jar app.jar