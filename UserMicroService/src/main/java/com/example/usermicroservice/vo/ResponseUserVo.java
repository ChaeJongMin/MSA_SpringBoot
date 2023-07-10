package com.example.usermicroservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUserVo {
    private String email;
    private String name;
    private String userId;

    private List<ResponseOrderVo> orderList;
}
