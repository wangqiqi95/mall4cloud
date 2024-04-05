package com.mall4j.cloud.api.docking.skq_sqb.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.queryResultRespTenderChannelInfo.QueryResultRespTenderChannelInfo;
import lombok.Data;

/**
 * @Description 收钱吧购买出参-指定本订单的流水信息
 * @author Peter_Tan
 * @date 2023-05-08 14:30
 **/
@Data
public class QueryResultRespTender {

    /**
     * 支付/退款指定中商户系统流水号，在商户系统中唯一
     */
    @JsonProperty("original_transaction_sn")
    private String originalTransactionSn;

    /**
     * 退款对应的原购货订单完成后本系统返回的支付流水号
     */
    @JsonProperty("original_tender_sn")
    private String originalTenderSn;

    /**
     * 支付/退款成功后，轻POS生成的唯一流水号
     */
    @JsonProperty("tender_sn")
    private String tenderSn;

    /**
     * 支付金额，精确到分
     */
    private String amount;

    /**
     * 商家实收，精确到分
     */
    @JsonProperty("collected_amount")
    private String collectedAmount;

    /**
     * 消费者实付，精确到分。
     */
    @JsonProperty("paid_amount")
    private String paidAmount;

    /**
     * 支付源交易流水完成时间, 格式详见
     * 采用ISO 8601 Format. YYYY-MM-DDThh:mm:ssTZD
     * Example:
     * 2015-12-05T15:28:36+08:00
     */
    @JsonProperty("pay_time")
    private String payTime;

    /**
     * 支付状态, 1:待操作; 2:支付中; 3:支付成功;
     * 4:退款中; 5:退款成功;6:退款失败;7:支付失败;8:未知状态;
     */
    @JsonProperty("pay_status")
    private String payStatus;

    /**
     * 结果原因描述
     */
    private String reason;

    /**
     * 支付方式类型：0-其他，1-预授权完成，2-银行卡，3-QRCode，4-分期，99-外部
     */
    @JsonProperty("tender_type")
    private String tenderType;

    /**
     * 二级支付方式类型：
     * 001-现金，如需在轻POS录入其他支付方式，在对接时与收钱吧沟通配置；
     * 101-银行卡预授权完成，102-微信预授权完成，103-支付宝预授权完成；
     * 201-银行卡；
     * 301-微信，302-支付宝；
     * 401-银行卡分期，402-花呗分期
     */
    @JsonProperty("sub_tender_type")
    private String subTenderType;

    /**
     * 二级支付方式描述。如：微信支付
     */
    @JsonProperty("sub_tender_desc")
    private String subTenderDesc;

    /**
     * 支付渠道流水号，操作成功时存在。
     * 微信支付宝：微信支付宝流水号；
     * 银行卡：银行卡流水号。
     */
    @JsonProperty("channel_sn")
    private String channelSn;

    /**
     * 移动支付：收钱吧传入支付宝/微信的out_trade_no；
     * 银行卡支付：交易返回的交易授权码
     */
    @JsonProperty("internal_transaction_sn")
    private String internalTransactionSn;

    /**
     * 分期数，分期支付时返回
     */
    @JsonProperty("installment_number")
    private String installmentNumber	;

    /**
     * 商家贴息比例，分期支付时返回
     */
    @JsonProperty("installment_fee_merchant_percent")
    private String installmentFeeMerchantPercent;

    /**
     * 支付渠道（银行卡/移动支付/礼品卡等）交易信息。
     */
    @JsonProperty("channel_info")
    private QueryResultRespTenderChannelInfo channelInfo;

}
