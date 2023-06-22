package com.example.usermicroservice.dto;

import com.example.usermicroservice.vo.ResponseOrderVo;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String userId;
    private Date createAt;

    private String encryptedPwd;

    private List<ResponseOrderVo> orderList;

}
