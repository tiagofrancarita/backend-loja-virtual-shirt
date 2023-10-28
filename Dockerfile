FROM eclipse-temurin:11-jdk-alpine
RUN mkdir /app
COPY target/ShirtVirtual.jar /app/ShirtVirtual.jar
WORKDIR /app
EXPOSE 8080
CMD ["java","-jar","springboot-example.jar"]