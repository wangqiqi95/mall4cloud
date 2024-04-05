package com.mall4j.cloud.api.docking.skq_sqb.dto.response.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author ty
 * @ClassName PaymentList
 * @description
 * @date 2023/5/8 15:31
 */
@Data
public class PaymentList {

    /**
     * 活动优惠的类型
     */
    private String type;

    /**
     * 活动出资总金额
     */
    @JsonProperty("amount_total")
    private String amountTotal;

}
