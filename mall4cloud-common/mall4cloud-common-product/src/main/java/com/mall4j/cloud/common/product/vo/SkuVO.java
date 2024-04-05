package com.mall4j.cloud.common.product.vo;

import com.alibaba.excel.annotation.ExcelProperty;
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
public class SkuVO extends BaseVO {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("属性id")
	private Long skuId;

	@ApiModelProperty("SPU id")
	private Long spuId;

	@ApiModelProperty("sku名称")
	private String skuName;

	@ApiModelProperty("spu名称")
	private String spuName;

	@ApiModelProperty("sku表里的名称")
	private String name;

	@ApiModelProperty("sku国际化信息")
	private List<SkuLangVO> skuLangList;

	@ApiModelProperty("banner图片")
	private String imgUrl;

	@ApiModelProperty("售价，整数方式保存")
	private Long priceFee;

	@ApiModelProperty("市场价，整数方式保存")
	private Long marketPriceFee;
	@ApiModelProperty("保护价")
	private Long protectPrice;

	@ApiModelProperty("状态 1:enable, 0:disable, -1:deleted")
	private Integer status;

	@ApiModelProperty("库存")
	private Integer stock;

	@ApiModelProperty("视频号库存")
	private Integer channelsStock;

	@ApiModelProperty("积分价格")
	private Long scoreFee;

	@ApiModelProperty("商品编码")
	private String partyCode;

	@ApiModelProperty("商品条形码")
	private String modelId;

	@ApiModelProperty("商品重量")
	private BigDecimal weight;

	@ApiModelProperty("商品体积")
	private BigDecimal volume;

	@ApiModelProperty("当前sku规格列表")
	private List<SpuSkuAttrValueVO> spuSkuAttrValues;
	@ApiModelProperty("前端显示 barCode")
	private String skuCode;
	@ApiModelProperty("条形码")
	private String intscode;
	@ApiModelProperty("前端显示 ：skuCode")
	private String priceCode;
	private String channelName;

	@ApiModelProperty("中台分类code")
	private String styleCode;

	private String properties;

	private Integer storeStock;

	@ApiModelProperty("门店库存(用于取价判断)")
	private Integer storeSkuStock;
	@ApiModelProperty("门店保护价(用于取价判断)")
	private Long storeProtectPrice;
	@ApiModelProperty("门店保护价(用于取价判断)")
	private Long skuProtectPrice;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getScoreFee() {
		return scoreFee;
	}

	public void setScoreFee(Long scoreFee) {
		this.scoreFee = scoreFee;
	}

	public String getPartyCode() {
		return partyCode;
	}

	public void setPartyCode(String partyCode) {
		this.partyCode = partyCode;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public List<SpuSkuAttrValueVO> getSpuSkuAttrValues() {
		return spuSkuAttrValues;
	}

	public void setSpuSkuAttrValues(List<SpuSkuAttrValueVO> spuSkuAttrValues) {
		this.spuSkuAttrValues = spuSkuAttrValues;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Long getSkuId() {
		return skuId;
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

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public List<SkuLangVO> getSkuLangList() {
		return skuLangList;
	}

	public void setSkuLangList(List<SkuLangVO> skuLangList) {
		this.skuLangList = skuLangList;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
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

	public String getPriceCode() {
		return priceCode;
	}

	public void setPriceCode(String priceCode) {
		this.priceCode = priceCode;
	}

	@Override
	public String toString() {
		return "SkuVO{" +
				"skuId=" + skuId +
				", spuId=" + spuId +
				", skuName='" + skuName + '\'' +
				", spuName='" + spuName + '\'' +
				", name='" + name + '\'' +
				", skuLangList=" + skuLangList +
				", imgUrl='" + imgUrl + '\'' +
				", priceFee=" + priceFee +
				", marketPriceFee=" + marketPriceFee +
				", status=" + status +
				", stock=" + stock +
				", channelsStock=" + channelsStock +
				", scoreFee=" + scoreFee +
				", partyCode='" + partyCode + '\'' +
				", modelId='" + modelId + '\'' +
				", weight=" + weight +
				", volume=" + volume +
				", spuSkuAttrValues=" + spuSkuAttrValues +
				", skuCode='" + skuCode + '\'' +
				", intscode='" + intscode + '\'' +
				", priceCode='" + priceCode + '\'' +
				", channelName='" + channelName + '\'' +
				", styleCode='" + styleCode + '\'' +
				", properties='" + properties + '\'' +
				", storeStock=" + storeStock +
				", storeSkuStock=" + storeSkuStock +
				", storeProtectPrice=" + storeProtectPrice +
				", skuProtectPrice=" + skuProtectPrice +
				'}';
	}
}
