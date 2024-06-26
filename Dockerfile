FROM openjdk:jdk
WORKDIR /appcontainerpayment
EXPOSE 9196
COPY ./target/payment-service-ms.jar /appcontainerpayment
ENTRYPOINT ["java","-jar","payment-service-ms.jar"]