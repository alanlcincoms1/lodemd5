FROM openjdk:11

WORKDIR /app

ADD ./build/libs/webslots-0.0.1-SNAPSHOT.jar .

EXPOSE 8085

ENTRYPOINT ["java", "-Xmx200m", "-jar", "-Dspring.profiles.active=stag", "/app/webslots-0.0.1-SNAPSHOT.jar"]