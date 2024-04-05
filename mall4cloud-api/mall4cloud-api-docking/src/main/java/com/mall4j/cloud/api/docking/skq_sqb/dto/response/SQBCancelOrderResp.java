package com.mall4j.cloud.api.docking.skq_sqb.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.common.SQBResponseData;
import lombok.Data;

/**
 * @Description 收钱吧取消订单操作出参
 * @author Peter_Tan
 * @date 2023-05-06 16:30
 **/
@Data
public class SQBCancelOrderResp extends SQBResponseData {

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
    @JsonProperty("original_order_sn")
    private String originalOrderSn;

    /**
     * 反射参数; 任何开发者希望原样返回的信息，
     * 可以用于关联商户ERP系统的订单或记录附加订单内容。
     * 可以在订单结果通知中返回
     */
    private String reflect;

}
