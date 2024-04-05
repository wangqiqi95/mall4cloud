package com.mall4j.cloud.common.product.vo.app;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;

/**
 * sku信息VO
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public class SkuAppVO{

	@ApiModelProperty("属性id")
	private Long skuId;

	@ApiModelProperty("SPU id")
	private Long spuId;

	@ApiModelProperty("sku名称")
	private String skuName;

	@JsonSerialize(using = ImgJsonSerializer.class)
	@ApiModelProperty("banner图片")
	private String imgUrl;

	@ApiModelProperty("售价，整数方式保存")
	private Long priceFee;

	@ApiModelProperty("市场价，整数方式保存")
	private Long marketPriceFee;

	@ApiModelProperty("库存")
	private Integer stock;

	@ApiModelProperty("积分价格")
	private Long scoreFee;

	@ApiModelProperty("属性")
	private String properties;

	private String channelName;

	@ApiModelProperty("前端显示 ：skuCode")
	private String priceCode;
	@ApiModelProperty("门店库存(用于取价判断)")
	private Integer storeSkuStock;
	@ApiModelProperty("门店保护价(用于取价判断)")
	private Long storeProtectPrice;
	@ApiModelProperty("官店保护价(用于取价判断)")
	private Long skuProtectPrice;
	@ApiModelProperty("视频号库存")
	private Integer channelsStock;

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Long getPriceFee() {
		return priceFee;
	}

	public void setPriceFee(Long priceFee) {
		this.priceFee = priceFee;
	}

	public Long getMarketPriceFee() {
		return marketPriceFee;
	}

	public void setMarketPriceFee(Long marketPriceFee) {
		this.marketPriceFee = marketPriceFee;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Long getScoreFee() {
		return scoreFee;
	}

	public void setScoreFee(Long scoreFee) {
		this.scoreFee = scoreFee;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getPriceCode() {
		return priceCode;
	}

	public void setPriceCode(String priceCode) {
		this.priceCode = priceCode;
	}

	public Integer getStoreSkuStock() {
		return storeSkuStock;
	}

	public void setStoreSkuStock(Integer storeSkuStock) {
		this.storeSkuStock = storeSkuStock;
	}

	public Long getStoreProtectPrice() {
		return storeProtectPrice;
	}

	public void setStoreProtectPrice(Long storeProtectPrice) {
		this.storeProtectPrice = storeProtectPrice;
	}

	public Long getSkuProtectPrice() {
		return skuProtectPrice;
	}

	public void setSkuProtectPrice(Long skuProtectPrice) {
		this.skuProtectPrice = skuProtectPrice;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Integer getChannelsStock() {
		return channelsStock;
	}

	public void setChannelsStock(Integer channelsStock) {
		this.channelsStock = channelsStock;
	}

	@Override
	public String toString() {
		return "SkuAppVO{" +
				"skuId=" + skuId +
				", spuId=" + spuId +
				", skuName='" + skuName + '\'' +
				", imgUrl='" + imgUrl + '\'' +
				", priceFee=" + priceFee +
				", marketPriceFee=" + marketPriceFee +
				", stock=" + stock +
				", scoreFee=" + scoreFee +
				", properties='" + properties + '\'' +
				", channelName='" + channelName + '\'' +
				", priceCode='" + priceCode + '\'' +
				", storeSkuStock=" + storeSkuStock +
				", storeProtectPrice=" + storeProtectPrice +
				", skuProtectPrice=" + skuProtectPrice +
				", channelsStock=" + channelsStock +
				'}';
	}
}
