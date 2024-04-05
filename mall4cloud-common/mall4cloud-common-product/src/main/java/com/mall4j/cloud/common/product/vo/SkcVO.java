package com.mall4j.cloud.common.product.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * skc信息VO
 * 款色信息
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Data
public class SkcVO extends BaseVO {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("属性id")
	private Long skuId;

	@ApiModelProperty("SPU id")
	private Long spuId;

	@ApiModelProperty("banner图片")
	private String imgUrl;

	@ApiModelProperty("状态 1:enable, 0:disable, -1:deleted")
	private Integer status;

	@ApiModelProperty("商品重量")
	private BigDecimal weight;

	@ApiModelProperty("商品体积")
	private BigDecimal volume;

	@ApiModelProperty("积分价格")
	private Long scoreFee;

	@ApiModelProperty("商品编码")
	private String partyCode;

	@ApiModelProperty("商品条形码")
	private String modelId;

	@ApiModelProperty("款色编码，前端显示 ：skcCode")
	private String priceCode;

	@ApiModelProperty("库存")
	private Integer stock;

	private Integer storeStock;

	@ApiModelProperty("门店库存(用于取价判断)")
	private Integer storeSkuStock;

	@ApiModelProperty("门店保护价(用于取价判断)")
	private Long storeProtectPrice;

	@ApiModelProperty("售价，整数方式保存")
	private Long priceFee;

	@ApiModelProperty("市场价，整数方式保存")
	private Long marketPriceFee;

}
