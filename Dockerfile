# Alpine Linux with OpenJDK JRE
FROM openjdk:8-jre-alpine
EXPOSE 9092
COPY  ./agent/ /home/agent/
COPY  ./java.sh  java.sh
COPY ./target/*.jar /home/agent/app.jar
CMD ["sh","java.sh"]