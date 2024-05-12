package com.example.paymentservice.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Date;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    private int id;
    private String name;

    private String category;

    private double price;

    private Date purchaseDate;

    private String orderId;

    private int userId;

    private String paymentMode;

    public Order(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Order order = objectMapper.readValue(json, Order.class);
        this.id = order.getId();
        this.name = order.getName();
        this.category = order.getCategory();
        this.price = order.getPrice();
        this.orderId = order.getOrderId();
        this.userId = order.getUserId();
        this.paymentMode = order.getPaymentMode();
        // Set other fields as needed...
    }
}
