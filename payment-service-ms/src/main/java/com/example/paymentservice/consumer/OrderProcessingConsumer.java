package com.example.paymentservice.consumer;

import com.example.paymentservice.dto.Order;
import com.example.paymentservice.dto.User;
import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.repository.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Date;

@Component
public class OrderProcessingConsumer {


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PaymentRepository paymentRepository;


    private final String USER_BASE_URL = "http://user-service-ms/users/";


//,groupId = "payment_consumer_group"
    @KafkaListener(topics = "ORDER_PAYMENT_TOPIC")
    public void processOrder(String orderJsonString){
        try {
          Order order=  new ObjectMapper().readValue(orderJsonString,Order.class);

          Payment payment =  Payment.builder().payMode(order.getPaymentMode())
                    .amount(order.getPrice())
                    .paidDate(new Date())
                    .userId(order.getUserId())
                    .orderId(order.getOrderId())
                    .build();


          if(payment.getPayMode().equals("COD")){
              payment.setPaymentStatus("PENDING");
              paymentRepository.save(payment);
          }else {
              User user =  restTemplate.getForObject(USER_BASE_URL+payment.getUserId(), User.class);
              if(payment.getAmount() > user.getAvailableAmount()){
                  throw  new RuntimeException("Insufficent Amount..!!");
              }
              else{
                  payment.setPaymentStatus("PAID");
                  paymentRepository.save(payment);



                  restTemplate.put(USER_BASE_URL+payment.getUserId()+"/"+payment.getAmount(),null);
              }
          }



        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
