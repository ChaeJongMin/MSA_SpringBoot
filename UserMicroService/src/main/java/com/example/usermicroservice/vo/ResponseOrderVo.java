package com.example.usermicroservice.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ResponseOrderVo {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private Date createdAt;

    private String orderId;
}
