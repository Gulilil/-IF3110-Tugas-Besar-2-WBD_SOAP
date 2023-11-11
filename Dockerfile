FROM maven:3.6.3-jdk-8
WORKDIR /app
COPY . .
EXPOSE 8001
RUN mvn clean install
ENTRYPOINT java -jar /app/target/service_soap-jar-with-dependencies.jar