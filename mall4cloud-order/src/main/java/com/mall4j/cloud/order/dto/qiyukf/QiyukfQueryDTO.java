package com.mall4j.cloud.order.dto.qiyukf;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class QiyukfQueryDTO {

//    "appid": "qiyukf",
//            "token": "qiyukf_security_token",
//            "userid": "zhangsan",
//            "count": 10,
//            "from": 0,
//            "type": 0


    private String appid;

    private String token;

    private String userid;

    private Integer count;

    private Integer from;

    private Integer type;


}
