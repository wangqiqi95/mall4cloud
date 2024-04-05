package com.mall4j.cloud.api.docking.skq_sqb.dto.response.queryResultRespTenderChannelInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author ty
 * @ClassName CardPaymentInfo
 * @description
 * @date 2023/5/8 15:23
 */
@Data
public class CardPaymentInfo {

    /**
     * 付款人ID，（微信公众平台unionid）
     */
    @JsonProperty("payer_unionid")
    private String payerUnionId;

    /**
     * 卡号或者券号
     */
    @JsonProperty("card_number")
    private String cardNumber;

}
