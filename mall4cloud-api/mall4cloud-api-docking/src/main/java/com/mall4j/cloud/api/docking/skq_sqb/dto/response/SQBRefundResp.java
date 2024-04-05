package com.mall4j.cloud.api.docking.skq_sqb.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.common.SQBResponseData;
import lombok.Data;

/**
 * @Description 收钱吧退款出参
 * @author Peter_Tan
 * @date 2023-04-26 11:30
 **/
@Data
public class SQBRefundResp extends SQBResponseData {

    /**
     * 品牌编号，系统对接前由"收钱吧"分配并提供，返回调用方传入的值
     */
    @JsonProperty("brand_code")
    private String brandCode;

    /**
     * 商户内部使用的门店编号，返回调用方传入的值
     */
    @JsonProperty("store_sn")
    private String storeSn;

    /**
     * 门店收银机编号，返回调用方传入的值
     */
    @JsonProperty("workstation_sn")
    private String workstationSn;

    /**
     * 商户订单号，返回调用方传入的值
     */
    @JsonProperty("check_sn")
    private String checkSn;

    /**
     * 本系统为该订单生成的订单序列号
     */
    @JsonProperty("order_sn")
    private String orderSn;

    /**
     * POS 或 电商等业务系统内的实际销售订单号，不同于check_sn。
     * 如果发起支付请求时该订单号已经生成，强烈建议传入，
     * 方便后续对账和运营流程使用。本字段不影响交易本身。
     */
    @JsonProperty("sales_sn")
    private String salesSn;

    /**
     * 订单来源：1=商户系统，3=智能终端，4=门店码牌，5=商户后台
     */
    @JsonProperty("order_source")
    private String orderSource;

    /**
     * 反射参数; 任何开发者希望原样返回的信息，
     * 可以用于关联商户ERP系统的订单或记录附加订单内容。
     * 可以在订单结果通知中返回
     */
    private String reflect;

}
