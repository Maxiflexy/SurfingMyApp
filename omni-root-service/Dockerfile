FROM openjdk:17
WORKDIR /
COPY ./config /config/
COPY ./src /src/
COPY ./target/*.jar /
EXPOSE 2759
RUN mkdir -p /mnt/paymentgateway/
ENTRYPOINT ["java", "-jar", "omni-root-service-1.0.0-SNAPSHOT.jar"]
