package com.mall4j.cloud.common.product.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 类描述：提供爱铺货查询商品信息请求参数
 */
public class IProductSearchDto {

	@ApiModelProperty(value = "商品名称")
	private String spuName;

	@ApiModelProperty(value = "商品id")
	private Long spuId;

	@ApiModelProperty(value = "商品分组id列表")
	private List<Long> tagIds;

	@ApiModelProperty(value = "商品状态")
	private Integer spuStatus;

	// TODO 商家编码
	// TODO 货号
	// TODO 上架时间
	// TODO 下架时间


}
