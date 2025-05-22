FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/api_contable_360-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} api_contable_360.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "api_contable_360.jar"]