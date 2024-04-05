package com.mall4j.cloud.biz.model;

import lombok.Data;

/**
 * @Date 2021年12月30日, 0030 15:04
 * @Created by eury
 */
@Data
public class ComponentAccessTokenResp {

    private String component_access_token;
    private Long expires_in;
}
