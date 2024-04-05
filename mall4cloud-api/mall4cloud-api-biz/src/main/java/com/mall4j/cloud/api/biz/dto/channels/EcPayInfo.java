package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

@Data
public class EcPayInfo {
    //预支付id
    private String prepay_id;
    //预支付时间，秒级时间戳
    private Long prepay_time;
    //支付时间，秒级时间戳
    private Long pay_time;
    //支付订单号
    private String transaction_id;

}
