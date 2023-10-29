FROM eclipse-temurin:11-jdk-alpine
WORKDIR /app
COPY target/ShirtVirtual.jar ShirtVirtual.jar
EXPOSE 8080
CMD ["java","-jar","ShirtVirtual.jar"]
