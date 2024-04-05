package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EcaftersaleAddRequest {

    /**
     * 商家自定义订单ID
     */
    @JsonProperty("out_order_id")
    private String outOrderId;

    /**
     * 和out_order_id二选一
     */
    @JsonProperty("order_id")
    private String orderId;

    /**
     * 商家自定义售后ID
     */
    @JsonProperty("out_aftersale_id")
    private String outAftersaleId;

    /**
     * 用户的openid
     */
    @JsonProperty("openid")
    private String openid;

    /**
     * 1仅退款2退货退款
     */
    @JsonProperty("type")
    private Integer type;



    /**
     * 退款原因
     */
    @JsonProperty("refund_reason")
    private String refundReason;
    /**
     * 退款原因类型
     * 参考：https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/API/aftersale/data_structure.html
     */
    @JsonProperty("refund_reason_type")
    private Integer refundReasonType;
    /**
     * 退款金额，单位分
     */
    @JsonProperty("orderamt")
    private Long orderamt;


    /**
     * 售后商品
     */
    @JsonProperty("product_info")
    private AfterSaleProductInfo productInfo;





}
