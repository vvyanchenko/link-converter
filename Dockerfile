FROM openjdk:11.0.8-jre

COPY ./build/libs/link-converter*.jar /app/application.jar

CMD java \
    -Dspring.profiles.active=$ACTIVE_PROFILE \
    -jar /app/application.jar