package com.mall4j.cloud.api.biz.dto.livestore.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import com.mall4j.cloud.api.biz.dto.livestore.request.ProductInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 退款售后
 * @author oy
 */
@Data
public class AfterSalesOrder {

    /**
     * 售后id
     */
    @JsonProperty("aftersale_id")
    private Long aftersaleId;

    /**
     * 售后id
     */
    @JsonProperty("out_aftersale_id")
    private String outAftersaleId;

    @JsonProperty("order_id")
    private Long orderId;

    /**
     * 售后单状态
     */
    @JsonProperty("status")
    private Integer status;

    /**
     * 商品信息
     */
    @JsonProperty("product_info")
    private ProductInfo productInfo;

    @JsonProperty("return_info")
    private ReturnInfo returnInfo;

    @JsonProperty("create_time")
    private Long createTime;

    @JsonProperty("update_time")
    private Long updateTime;

    @JsonProperty("refund_reason")
    private String refundReason;

    @JsonProperty("orderamt")
    private BigDecimal orderamt;

    @JsonProperty("refund_reason_type")
    private Integer refundReasonType;

    @JsonProperty("type")
    private Integer type;

    private List<Media> media_list;
}
