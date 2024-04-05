package com.mall4j.cloud.api.biz.vo;

import lombok.Data;

/**
 * @Date 2022年4月7日, 0007 15:32
 * @Created by eury
 */
@Data
public class WeixinWebAppInfoVo {

    private String name;

    /**
     * crm: 公众号类型 0成人公众号 1儿童公众号 2lifestyle公众号 3sport公众号
     */
    private Integer crmType;
}
