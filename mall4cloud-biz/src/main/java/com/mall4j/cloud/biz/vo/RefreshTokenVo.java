package com.mall4j.cloud.biz.vo;

import lombok.Data;

/**
 * @Date 2021年12月30日, 0030 15:08
 * @Created by eury
 */
@Data
public class RefreshTokenVo {
    private String component_appid;

    private String authorizer_appid;

    private String authorizer_refresh_token;
}
