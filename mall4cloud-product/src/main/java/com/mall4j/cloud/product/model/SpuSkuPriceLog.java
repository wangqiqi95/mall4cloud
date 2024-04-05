package com.mall4j.cloud.product.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-05-31 16:07:13
 */
public class SpuSkuPriceLog extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

	private String businessId;

    /**
     * 需要修改的商品货号
     */
    private String spuCode;

    /**
     * 改价条件，修改款内的全部sku_code
     */
    private String priceCode;

    /**
     * 实际价格来源(sku_store)
     */
    private Long storeSkuId;

    /**
     * 提取的最高库存
     */
    private Integer skuStoreStock;

    /**
     * 原价格(售价)
     */
    private Long priceFee;

    /**
     * 变更后价格(售价)
     */
    private Long priceFeeNew;

    /**
     * 1吊牌价，2保护价，3活动价，4渠道价
     */
    private Integer priceType;

	/**
	 * 0设置价格 1取消价格
	 */
    private Integer type;

	/**
	 * 0库存价 1无库存价 2电商保护价
	 */
    private Integer stockType;

    /**
     * 需要更新价格门店id
     */
    private Long toStoreId;

    /**
     * 价格来源门店id
     */
    private Long fromStoreId;

    /**
     * 渠道
     */
    private String channelName;

    /**
     * 折扣等级
     */
    private String discount;

	/**
	 * 实际折扣等级
	 */
    private String actualDiscount;

    /**
     * 折扣计算金额(吊牌价)
     */
    private Long marketPriceFee;

    /**
     * 状态备注
     */
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStockType() {
		return stockType;
	}

	public void setStockType(Integer stockType) {
		this.stockType = stockType;
	}

	public String getActualDiscount() {
		return actualDiscount;
	}

	public void setActualDiscount(String actualDiscount) {
		this.actualDiscount = actualDiscount;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	@Override
	public String toString() {
		return "SpuSkuPriceLog{" +
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
				",updateTime=" + updateTime +
				",remarks=" + remarks +
				'}';
	}
}
