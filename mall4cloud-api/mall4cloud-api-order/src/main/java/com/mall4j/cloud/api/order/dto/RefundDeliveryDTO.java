package com.mall4j.cloud.api.order.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RefundDeliveryDTO {

    @ApiModelProperty(value = "退单号")
    private String refundNo;

    @ApiModelProperty(value = "微信退单编号")
    private Long aftersaleId;

    @ApiModelProperty(value = "物流单号")
    private String logisticNo;

    @ApiModelProperty(value = "物流公司名称")
    private String logisticsName;

    @ApiModelProperty(value = "物流公司代码")
    private String deliveryCode;

}
