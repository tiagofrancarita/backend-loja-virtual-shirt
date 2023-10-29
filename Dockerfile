FROM eclipse-temurin:11-jdk-alpine
RUN apk add curl
VOLUME /tmp
EXPOSE 8080
ADD target/ShirtVirtual.jar ShirtVirtual.jar
ENTRYPOINT ["java","-jar","/ShirtVirtual.jar"]

