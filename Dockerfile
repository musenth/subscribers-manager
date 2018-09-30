FROM openjdk:8-jdk-alpine
VOLUME /tmp
RUN mkdir /work
COPY . /work
WORKDIR /work
RUN /work/mvnw install -DskipTests
RUN mv /work/target/*.jar /work/app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/work/app.jar"]