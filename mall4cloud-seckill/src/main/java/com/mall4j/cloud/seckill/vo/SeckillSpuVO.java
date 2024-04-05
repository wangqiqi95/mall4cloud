package com.mall4j.cloud.seckill.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 秒杀商品信息VO
 *
 * @author lhd
 * @date 2021-03-30 14:53:09
 */
public class SeckillSpuVO extends BaseVO{
    private static final long serialVersionUID = 1L;

	@ApiModelProperty("秒杀活动id")
	private Long seckillId;

	@ApiModelProperty("spu id")
	private Long spuId;

	@ApiModelProperty("店铺id")
	private Long shopId;

	@ApiModelProperty("店铺名称")
	private String shopName;

	@ApiModelProperty("分类名称")
	private String categoryId;

	@ApiModelProperty("分类名称")
	private String categoryName;

	@ApiModelProperty("商品介绍主图")
	private String mainImgUrl;

	@ApiModelProperty("spu名称")
	private String name;

	@ApiModelProperty("售价，整数方式保存")
	private Long priceFee;

	@ApiModelProperty("限购数量")
	private Long maxNum;

	@ApiModelProperty("状态")
	private Integer status;

	@ApiModelProperty("秒杀活动最低价")
	private Long seckillPrice;

	@ApiModelProperty("立减xx元")
	private Long reducePrice;

	@ApiModelProperty("秒杀活动剩余总库存")
	private Integer seckillTotalStocks;

	@ApiModelProperty("秒杀活动原始库存")
	private Integer seckillOriginStocks;

	@ApiModelProperty("秒杀活动关联sku信息")
	private List<SeckillSkuVO> seckillSkuList;

	@ApiModelProperty("指定门店类型 0-全部 1-部分")
	private Integer limitStoreType;

	@ApiModelProperty("秒杀活动指定门店集合")
	private List<Long> limitStoreList;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(Long maxNum) {
		this.maxNum = maxNum;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Long getReducePrice() {
		return reducePrice;
	}

	public void setReducePrice(Long reducePrice) {
		this.reducePrice = reducePrice;
	}

	public Long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(Long seckillId) {
		this.seckillId = seckillId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public String getMainImgUrl() {
		return mainImgUrl;
	}

	public void setMainImgUrl(String mainImgUrl) {
		this.mainImgUrl = mainImgUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPriceFee() {
		return priceFee;
	}

	public void setPriceFee(Long priceFee) {
		this.priceFee = priceFee;
	}

	public Long getSeckillPrice() {
		return seckillPrice;
	}

	public void setSeckillPrice(Long seckillPrice) {
		this.seckillPrice = seckillPrice;
	}

	public Integer getSeckillTotalStocks() {
		return seckillTotalStocks;
	}

	public void setSeckillTotalStocks(Integer seckillTotalStocks) {
		this.seckillTotalStocks = seckillTotalStocks;
	}

	public Integer getSeckillOriginStocks() {
		return seckillOriginStocks;
	}

	public void setSeckillOriginStocks(Integer seckillOriginStocks) {
		this.seckillOriginStocks = seckillOriginStocks;
	}

	public List<SeckillSkuVO> getSeckillSkuList() {
		return seckillSkuList;
	}

	public void setSeckillSkuList(List<SeckillSkuVO> seckillSkuList) {
		this.seckillSkuList = seckillSkuList;
	}

	public List<Long> getLimitStoreList() {
		return limitStoreList;
	}

	public void setLimitStoreList(List<Long> limitStoreList) {
		this.limitStoreList = limitStoreList;
	}

	public Integer getLimitStoreType() {
		return limitStoreType;
	}

	public void setLimitStoreType(Integer limitStoreType) {
		this.limitStoreType = limitStoreType;
	}

	@Override
	public String toString() {
		return "SeckillSpuVO{" +
				"seckillId=" + seckillId +
				", spuId=" + spuId +
				", shopId=" + shopId +
				", shopName='" + shopName + '\'' +
				", categoryId='" + categoryId + '\'' +
				", categoryName='" + categoryName + '\'' +
				", mainImgUrl='" + mainImgUrl + '\'' +
				", name='" + name + '\'' +
				", priceFee=" + priceFee +
				", maxNum=" + maxNum +
				", status=" + status +
				", seckillPrice=" + seckillPrice +
				", reducePrice=" + reducePrice +
				", seckillTotalStocks=" + seckillTotalStocks +
				", seckillOriginStocks=" + seckillOriginStocks +
				", seckillSkuList=" + seckillSkuList +
				'}';
	}
}
