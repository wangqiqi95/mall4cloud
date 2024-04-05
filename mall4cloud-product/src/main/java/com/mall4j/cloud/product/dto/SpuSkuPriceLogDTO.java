package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * DTO
 *
 * @author FrozenWatermelon
 * @date 2022-05-31 16:07:13
 */
public class SpuSkuPriceLogDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("需要修改的商品货号")
    private String spuCode;

    @ApiModelProperty("改价条件，修改款内的全部sku_code")
    private String priceCode;

    @ApiModelProperty("实际价格来源(sku_store)")
    private Long storeSkuId;

    @ApiModelProperty("提取的最高库存")
    private Integer skuStoreStock;

    @ApiModelProperty("原价格(售价)")
    private Long priceFee;

    @ApiModelProperty("变更后价格(售价)")
    private Long priceFeeNew;

    @ApiModelProperty("1吊牌价，2保护价，3活动价，4渠道价")
    private Integer priceType;

    @ApiModelProperty("需要更新价格门店id")
    private Long toStoreId;

    @ApiModelProperty("价格来源门店id")
    private Long fromStoreId;

    @ApiModelProperty("渠道")
    private String channelName;

    @ApiModelProperty("折扣等级")
    private String discount;

    @ApiModelProperty("折扣计算金额(吊牌价)")
    private Long marketPriceFee;

    @ApiModelProperty("状态备注")
    private String remarks;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSpuCode() {
		return spuCode;
	}

	public void setSpuCode(String spuCode) {
		this.spuCode = spuCode;
	}

	public String getPriceCode() {
		return priceCode;
	}

	public void setPriceCode(String priceCode) {
		this.priceCode = priceCode;
	}

	public Long getStoreSkuId() {
		return storeSkuId;
	}

	public void setStoreSkuId(Long storeSkuId) {
		this.storeSkuId = storeSkuId;
	}

	public Integer getSkuStoreStock() {
		return skuStoreStock;
	}

	public void setSkuStoreStock(Integer skuStoreStock) {
		this.skuStoreStock = skuStoreStock;
	}

	public Long getPriceFee() {
		return priceFee;
	}

	public void setPriceFee(Long priceFee) {
		this.priceFee = priceFee;
	}

	public Long getPriceFeeNew() {
		return priceFeeNew;
	}

	public void setPriceFeeNew(Long priceFeeNew) {
		this.priceFeeNew = priceFeeNew;
	}

	public Integer getPriceType() {
		return priceType;
	}

	public void setPriceType(Integer priceType) {
		this.priceType = priceType;
	}

	public Long getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Long toStoreId) {
		this.toStoreId = toStoreId;
	}

	public Long getFromStoreId() {
		return fromStoreId;
	}

	public void setFromStoreId(Long fromStoreId) {
		this.fromStoreId = fromStoreId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public Long getMarketPriceFee() {
		return marketPriceFee;
	}

	public void setMarketPriceFee(Long marketPriceFee) {
		this.marketPriceFee = marketPriceFee;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "SpuSkuPriceLogDTO{" +
				"id=" + id +
				",spuCode=" + spuCode +
				",priceCode=" + priceCode +
				",storeSkuId=" + storeSkuId +
				",skuStoreStock=" + skuStoreStock +
				",priceFee=" + priceFee +
				",priceFeeNew=" + priceFeeNew +
				",priceType=" + priceType +
				",toStoreId=" + toStoreId +
				",fromStoreId=" + fromStoreId +
				",channelName=" + channelName +
				",discount=" + discount +
				",marketPriceFee=" + marketPriceFee +
				",remarks=" + remarks +
				'}';
	}
}
