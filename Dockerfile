FROM openjdk:22-slim

WORKDIR /app

EXPOSE 8001

CMD ["java", "-cp", "target/service_soap-jar-with-dependencies.jar", "com.wbd_soap.App"]

#FROM maven:3.6.3-jdk-8
#WORKDIR /app
#COPY . .
#EXPOSE 8001
#RUN mvn clean install
#ENTRYPOINT java -cp target/service_soap-jar-with-dependencies.jar com.wbd_soap.App
# ENTRYPOINT java --add-opens java.base/java.lang=ALL-UNNAMED -cp target/service_soap-jar-with-dependencies.jar com.wbd_soap.App
