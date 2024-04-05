package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

@Data
public class EcAfterSaleOrderInfo {
    //售后单ID
    private Long aftersale_order_id;
    //售后单状态(已废弃，请勿使用，售后信息请调用售后接口）
    private Integer status;
}
