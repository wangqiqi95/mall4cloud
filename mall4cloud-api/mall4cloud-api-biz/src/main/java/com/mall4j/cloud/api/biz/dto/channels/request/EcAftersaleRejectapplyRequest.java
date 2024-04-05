package com.mall4j.cloud.api.biz.dto.channels.request;

import lombok.Data;

@Data
public class EcAftersaleRejectapplyRequest {
    //售后单号
    private Long after_sale_order_id;
    //拒绝原因
    private String reject_reason;
}
