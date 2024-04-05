package com.mall4j.cloud.seckill.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 秒杀活动skuVO
 *
 * @author lhd
 * @date 2021-03-30 14:53:09
 */
public class AppSeckillSkuVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("秒杀活动单个skuid")
    private Long seckillSkuId;

    @ApiModelProperty("skuId")
    private Long skuId;

    @ApiModelProperty("秒杀活动id")
    private Long seckillId;

    @ApiModelProperty("用于秒杀的库存")
    private Integer stock;

    @ApiModelProperty("商品价格")
    private Long priceFee;

	@ApiModelProperty("秒杀价格")
	private Long seckillPrice;

	@ApiModelProperty("banner图片")
	private String imgUrl;

	@ApiModelProperty("sku名称")
	private String skuName;

	@ApiModelProperty("市场价，整数方式保存")
	private Long marketPriceFee;

	@ApiModelProperty("属性")
	private String properties;

	@ApiModelProperty(value = "状态")
	private Integer status;

	public Long getSeckillPrice() {
		return seckillPrice;
	}

	public void setSeckillPrice(Long seckillPrice) {
		this.seckillPrice = seckillPrice;
	}

	public Long getSeckillSkuId() {
		return seckillSkuId;
	}

	public void setSeckillSkuId(Long seckillSkuId) {
		this.seckillSkuId = seckillSkuId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(Long seckillId) {
		this.seckillId = seckillId;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Long getPriceFee() {
		return priceFee;
	}

	public void setPriceFee(Long priceFee) {
		this.priceFee = priceFee;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public Long getMarketPriceFee() {
		return marketPriceFee;
	}

	public void setMarketPriceFee(Long marketPriceFee) {
		this.marketPriceFee = marketPriceFee;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "SeckillSkuVO{" +
				"seckillSkuId=" + seckillSkuId +
				", skuId=" + skuId +
				", seckillId=" + seckillId +
				", stock=" + stock +
				", priceFee=" + priceFee +
				", imgUrl='" + imgUrl + '\'' +
				", skuName='" + skuName + '\'' +
				", marketPriceFee=" + marketPriceFee +
				", properties='" + properties + '\'' +
				", status=" + status +
				'}';
	}
}
