package com.mall4j.cloud.common.product.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * sku信息VO
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Data
public class SkuProtectVO {
	private static final long serialVersionUID = 1L;

	private Long spuId;

	private Long skuId;

	@ApiModelProperty("前端显示 ：skuCode")
	private String priceCode;

	@ApiModelProperty("门店库存(用于取价判断)")
	private Integer storeSkuStock;

	@ApiModelProperty("门店保护价(用于取价判断)")
	private Long storeProtectPrice;

}
