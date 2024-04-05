package com.mall4j.cloud.api.docking.skq_sqb.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.common.SQBResponseData;
import lombok.Data;

/**
 * @Description 收钱吧购买出参-订单货物清单
 * @author Peter_Tan
 * @date 2023-05-08 14:30
 **/
@Data
public class PurchaseRespItem{

    /**
     * 商户系统中的商品编号
     */
    @JsonProperty("item_code")
    private String itemCode;

    /**
     * 商品描述信息，例"白色短袖"
     */
    @JsonProperty("item_desc")
    private String itemDesc;

    /**
     * 商品所属大类，例"短袖"
     */
    private String category;

    /**
     * 规格
     */
    private String spec;

    /**
     * 商品单位，例"件"
     */
    private String unit;

    /**
     * 商品数量，例"2"
     */
    @JsonProperty("item_qty")
    private String itemQty;

    /**
     * 商品单价，精确到分
     */
    @JsonProperty("item_price")
    private String itemPrice;

    /**
     * 商品成交价格,一般为数量*单价，如有折扣再进行扣减，精确到分；
     * 当退货时成交价为负数；目前不校验"数量*单价"结果是否与此字段值相等
     */
    @JsonProperty("sales_price")
    private String salesPrice;

    /**
     * 0-销售，1-退货
     */
    private String type;

    /**
     * 原商品销售门店号，退货时必填
     */
    @JsonProperty("return_store_sn")
    private String returnStoreSn;

    /**
     * 原商品销售收银机号，退货时必填
     */
    @JsonProperty("return_workstation_sn")
    private String returnWorkstationSn;

    /**
     * 原商品销售订单号，退货时必填
     */
    @JsonProperty("return_check_sn")
    private String returnCheckSn;

}
