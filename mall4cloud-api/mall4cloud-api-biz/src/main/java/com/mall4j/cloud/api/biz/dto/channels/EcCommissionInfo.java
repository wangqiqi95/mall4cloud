package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

@Data
public class EcCommissionInfo {

    //商品skuid
    private Long sku_id;
    //分账方昵称
    private String nickname;
    //分账方类型，0：达人，1：团长
    private Integer type;
    //分账状态， 1：未结算，2：已结算
    private Integer status;
    //分账金额
    private Long amount;
    //达人视频号id
    private String finder_id;
    //达人openfinderid
    private String openfinderid;

}
