package com.example.orderservice.vo;

import lombok.Data;

@Data
public class RequestOrderVo {
    private String productId;
    private Integer qty;
    private Integer unitPrice;

}
