package com.mall4j.cloud.biz.vo;

import lombok.Data;

/**
 * @Date 2021年12月30日, 0030 15:00
 * @Created by eury
 */
@Data
public class ComponentAccessTokenVo {

    private String component_appid;
    private String component_appsecret;
    private String component_verify_ticket;
}
