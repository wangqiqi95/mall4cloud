package com.mall4j.cloud.order.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DistributionOrderItemVO {

    @ApiModelProperty(value = "商品图片")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    @ApiModelProperty(value = "商品名称")
    private String spuName;

    @ApiModelProperty(value = "商品数量")
    private Integer count;

    @ApiModelProperty(value = "商品价格")
    private Long price;

    @ApiModelProperty(value = "skuId")
    private Long skuId;

    @ApiModelProperty(value = "skuName")
    private String skuName;

    @ApiModelProperty(value = "订单项id")
    private Long orderItemId;

    @ApiModelProperty(value = "商品id")
    private Long spuId;

    @ApiModelProperty(value = "商品实际金额 = 商品总金额 - 分摊的优惠金额")
    private Long actualTotal;

    @ApiModelProperty(value = "商品总金额")
    private Long spuTotalAmount;

    @ApiModelProperty(value = "订单项退款状态（1:申请退款 2:退款成功 3:部分退款成功 4:退款失败）")
    private Integer refundStatus;

    @ApiModelProperty(value = "推广员分销佣金")
    private Long distributionAmount;

    @ApiModelProperty(value = "上级推广员分销佣金")
    private Long distributionParentAmount;

    @ApiModelProperty(value = "推广员分销佣金快照")
    private Long distributionAmountSnapshot;

    @ApiModelProperty(value = "上级推广员分销佣金快照")
    private Long distributionParentAmountSnapshot;

}
