package com.mall4j.cloud.api.docking.skq_sqb.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author ty
 * @ClassName RefundItem
 * @description
 * @date 2023/5/6 10:28
 */
@Data
public class RefundItem{

    /**
     * 商户系统中的商品编号
     */
    @JsonProperty("item_code")
    private String item_code;

    /**
     * 商品描述信息，例"白色短袖"
     */
    @JsonProperty("item_desc")
    private String item_desc;

    /**
     * 商品所属大类，例"短袖"
     */
    private String category;

    /**
     * 商品单位，例"件"
     */
    private String unit;

    /**
     * 商品数量，例"2"；当退货时数量为负数，例："-2"
     */
    @JsonProperty("item_qty")
    private String item_qty;

    /**
     * 商品单价，精确到分
     */
    @JsonProperty("item_price")
    private String item_price;

    /**
     * 商品成交价格,一般为数量 * 单价，如有折扣再进行扣减，精确到分；
     * 当退货时成交价为负数；目前不校验"数量 * 单价"结果是否与此字段值相等
     */
    @JsonProperty("sales_price")
    private String sales_price;

    /**
     * 0-销售，1-退货
     */
    private String type;

    /**
     * 原商品销售门店号
     */
    @JsonProperty("return_store_sn")
    private String return_store_sn;

    /**
     * 原商品销售收银机号
     */
    @JsonProperty("return_workstation_sn")
    private String return_workstation_sn	;

    /**
     * 原商品销售订单号
     */
    @JsonProperty("return_check_sn")
    private String return_check_sn;

}
