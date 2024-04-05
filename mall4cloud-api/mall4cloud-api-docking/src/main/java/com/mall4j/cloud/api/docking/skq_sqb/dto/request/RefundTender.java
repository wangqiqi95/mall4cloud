package com.mall4j.cloud.api.docking.skq_sqb.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author ty
 * @ClassName RefundTender
 * @description
 * @date 2023/5/6 10:28
 */
@Data
public class RefundTender {

    /**
     * 商户系统流水号，在商户系统中唯一
     */
    @JsonProperty("transaction_sn")
    private String transaction_sn;

    /**
     * 退款金额，精确到分，退款为负
     */
    private String amount;

    /**
     * 标记该tender是否已经支付完成。
     * 0：待操作，
     * 1：已完成（原tender_type为99时必须为1：已完成；其他的tender_type必须为0：待操作）
     */
    @JsonProperty("pay_status")
    private String pay_status;

    /**
     * tender创建时间，当pay_status为1时必填
     */
    @JsonProperty("create_time")
    private String create_time;

    /**
     * 原购货订单完成后本系统返回的支付流水号，字段tender_sn值
     * （支持填写收钱吧传入支付宝/微信的流水号、以及微信/支付宝的流水号，
     * 对应字段internal_transaction_sn值、字段channel_sn值；
     * 支持工商银行信用卡分期的流水号，对应字段channel_sn值）
     */
    @JsonProperty("original_tender_sn")
    private String original_tender_sn;

}
