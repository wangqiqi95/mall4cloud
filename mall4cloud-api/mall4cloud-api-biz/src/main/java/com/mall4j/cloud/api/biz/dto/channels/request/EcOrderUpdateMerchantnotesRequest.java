package com.mall4j.cloud.api.biz.dto.channels.request;

import lombok.Data;

@Data
public class EcOrderUpdateMerchantnotesRequest {
    //订单id
    private String order_id;
    //备注内容
    private String merchant_notes;
}
