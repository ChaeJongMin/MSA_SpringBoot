package com.example.orderservice.dto;

import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Data
@Getter
public class OrderDto {

    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private Date createdAt;
    private String orderId;
    private String userId;
}
