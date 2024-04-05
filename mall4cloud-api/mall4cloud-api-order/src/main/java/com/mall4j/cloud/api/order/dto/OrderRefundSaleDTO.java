package com.mall4j.cloud.api.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 申请退款参数
 * @author FrozenWatermelon
 */
@ApiModel("app-申请退款参数")
@Data
public class OrderRefundSaleDTO {

    @ApiModelProperty(value = "订单号", required = true)
    @NotNull(message = "订单号不能为空")
    private String orderNumber;

    @ApiModelProperty(value = "退款单类型（1:整单退款,2:单个物品退款）", required = true)
    @NotNull(message = "退款单类型不能为空")
    private Integer refundType;

    @ApiModelProperty(value = "订单项ID(0:为全部订单项)", required = true)
    private Long orderItemId;

    @ApiModelProperty(value = "退货数量(0:为全部订单项)", required = true)
    private Integer refundCount;

    @ApiModelProperty(value = "申请类型:1,仅退款,2退款退货", required = true)
    @NotNull(message = "申请类型不能为空")
    private Integer applyType;

    @ApiModelProperty(value = "是否接收到商品(1:已收到,0:未收到)", required = true)
    @NotNull(message = "是否接收到商品不能为空")
    private Integer isReceived;

    @ApiModelProperty(value = "申请原因(下拉选择)")
    private Integer buyerReason;

    @ApiModelProperty(value = "退款金额", required = true)
    @NotNull(message = "退款金额不能为空")
    private Long refundAmount;

    @NotEmpty(message = "手机号码不能为空")
    private String buyerMobile;

    @ApiModelProperty(value = "申请说明", required = true)
    private String buyerDesc;

    @ApiModelProperty(value = "文件凭证(逗号隔开)")
    private String imgUrls;

    @ApiModelProperty(value = "文件凭证(逗号隔开)")
    private Long userId;

    @ApiModelProperty(value = "视频号售后单号)")
    private Long aftersaleId;


}
