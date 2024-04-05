package com.mall4j.cloud.api.order.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 物流订单项信息DTO
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:00
 */
@Data
public class DeliveryOrderItemDTO{

	@ApiModelProperty("id")
	private Long orderItemId;

    @ApiModelProperty("订单物流包裹id")
    private Long deliveryOrderId;

    @ApiModelProperty("商品图片")
    private String pic;

    @ApiModelProperty("商品名称")
    private String spuName;

    @ApiModelProperty("发货改变的数量")
    private Integer changeNum;
	@ApiModelProperty("发货改变的数量")
	private Long skuId;
	@ApiModelProperty("发货改变的数量")
	private Long spuId;

}
