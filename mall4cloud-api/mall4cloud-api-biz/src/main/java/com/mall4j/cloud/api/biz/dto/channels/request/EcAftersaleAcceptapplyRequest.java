package com.mall4j.cloud.api.biz.dto.channels.request;

import lombok.Data;

@Data
public class EcAftersaleAcceptapplyRequest {
    //售后单号
    private Long after_sale_order_id;
    //同意退货时传入地址id
    private Long address_id;
}
