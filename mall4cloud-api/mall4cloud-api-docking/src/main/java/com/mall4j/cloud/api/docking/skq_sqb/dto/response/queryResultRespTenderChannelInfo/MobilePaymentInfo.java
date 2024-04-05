package com.mall4j.cloud.api.docking.skq_sqb.dto.response.queryResultRespTenderChannelInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.common.PaymentList;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.common.providerResponse.ProviderResponse;
import lombok.Data;

import java.util.List;

/**
 * @author ty
 * @ClassName MobilePaymentInfo
 * @description
 * @date 2023/5/8 15:23
 */
@Data
public class MobilePaymentInfo {

    /**
     * 付款人ID，支付平台（微信，支付宝）上的付款人ID
     */
    @JsonProperty("payer_uid")
    private String payerUid;

    /**
     * 活动优惠
     */
    @JsonProperty("payment_list")
    private List<PaymentList> paymentList;

    /**
     * 优惠详情
     */
    private ProviderResponse provider_response;

}
