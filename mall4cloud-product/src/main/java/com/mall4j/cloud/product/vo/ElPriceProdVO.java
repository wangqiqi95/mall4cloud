package com.mall4j.cloud.product.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 电子价签商品VO
 *
 * @author FrozenWatermelon
 * @date 2022-03-27 22:24:29
 */
@Data
public class ElPriceProdVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("电子价签id")
    private Long elId;

    @ApiModelProperty("商品id")
    private Long spuId;
	@ApiModelProperty("商品名称")
	private String spuName;
	@ApiModelProperty("商品金额")
	private Long priceFee;
	@ApiModelProperty("商品介绍主图")
	private String mainImgUrl;
	@ApiModelProperty("商品介绍主图 多个图片逗号分隔")
	private String imgUrls;

    @ApiModelProperty("商品skuid")
    private Long skuId;
	@ApiModelProperty("商品款色")
	private String skuPriceCode;

}
