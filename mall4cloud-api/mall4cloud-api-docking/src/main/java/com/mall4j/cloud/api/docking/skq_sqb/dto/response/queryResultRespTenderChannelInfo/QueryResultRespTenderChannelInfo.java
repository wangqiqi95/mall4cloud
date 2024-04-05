package com.mall4j.cloud.api.docking.skq_sqb.dto.response.queryResultRespTenderChannelInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Description 收钱吧购买出参-指定本订单的流水信息
 * @author Peter_Tan
 * @date 2023-05-08 14:30
 **/
@Data
public class QueryResultRespTenderChannelInfo {

    /**
     * 银行卡交易信息
     * 二级支付方式为“201-银行卡”返回
     * 定义见下表bank_card_info
     */
    @JsonProperty("bank_card_info")
    private BankCardInfo bankCardInfo;

    /**
     * 移动支付交易信息
     * 二级支付方式为“301-微信”，“302-支付宝”，“402-花呗分期”返回
     * 定义见下表mobile_payment_info
     */
    @JsonProperty("mobile_payment_info")
    private MobilePaymentInfo mobilePaymentInfo;

    /**
     * 卡券支付交易信息
     * 二级支付方式为“801-会员储值卡”、“804-单品券”时返回
     * 定义见下表card_payment_info
     */
    @JsonProperty("card_payment_info")
    private CardPaymentInfo cardPaymentInfo;

}
