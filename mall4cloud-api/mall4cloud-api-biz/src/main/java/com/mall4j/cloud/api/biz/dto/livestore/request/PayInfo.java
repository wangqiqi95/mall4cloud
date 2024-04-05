package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PayInfo {

    /**
     * 0: 微信支付, 1: 货到付款, 2: 商家会员储蓄卡（默认0）
     */
    @JsonProperty("pay_method_type")
    private Integer payMethodType = 0;
}
