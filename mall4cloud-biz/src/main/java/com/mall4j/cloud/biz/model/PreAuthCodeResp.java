package com.mall4j.cloud.biz.model;

import lombok.Data;

/**
 * @Date 2021年12月30日, 0030 15:06
 * @Created by eury
 */
@Data
public class PreAuthCodeResp {

    private String pre_auth_code;
    private Long expires_in;
}
