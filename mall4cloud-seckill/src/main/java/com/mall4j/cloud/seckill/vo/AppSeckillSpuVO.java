package com.mall4j.cloud.seckill.vo;

import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 秒杀商品信息VO
 *
 * @author lhd
 * @date 2021-03-30 14:53:09
 */
public class AppSeckillSpuVO extends BaseVO{
    private static final long serialVersionUID = 1L;

	@ApiModelProperty("spu id")
	private Long spuId;

	@ApiModelProperty("spu信息")
	private SpuVO spuVO;

	@ApiModelProperty("店铺id")
	private Long shopId;

	@ApiModelProperty("限购数量")
	private Integer maxNum;

	@ApiModelProperty(value = "活动状态（活动状态：1:未开始、2:进行中、3:已结束）")
	private Integer activityStatus;

	@ApiModelProperty("秒杀活动最低价")
	private Long seckillPrice;

	@ApiModelProperty("秒杀活动剩余总库存")
	private Integer seckillTotalStocks;

	@ApiModelProperty("秒杀活动原始库存")
	private Integer seckillOriginStocks;

	@ApiModelProperty("秒杀活动关联sku信息")
	private List<AppSeckillSkuVO> seckillSkuList;

	@ApiModelProperty(value = "在多少秒后过期")
	private Long expiresIn;

	@ApiModelProperty(value = "在多少秒后开始")
	private Long startIn;

	public Integer getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(Integer activityStatus) {
		this.activityStatus = activityStatus;
	}

	public Integer getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(Integer maxNum) {
		this.maxNum = maxNum;
	}

	public SpuVO getSpuVO() {
		return spuVO;
	}

	public void setSpuVO(SpuVO spuVO) {
		this.spuVO = spuVO;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}


	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
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

	public List<AppSeckillSkuVO> getSeckillSkuList() {
		return seckillSkuList;
	}

	public void setSeckillSkuList(List<AppSeckillSkuVO> seckillSkuList) {
		this.seckillSkuList = seckillSkuList;
	}

	public Long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public Long getStartIn() {
		return startIn;
	}

	public void setStartIn(Long startIn) {
		this.startIn = startIn;
	}

	@Override
	public String toString() {
		return "SeckillSpuVO{" +
				"spuId=" + spuId +
				", seckillPrice=" + seckillPrice +
				", seckillTotalStocks=" + seckillTotalStocks +
				", seckillOriginStocks=" + seckillOriginStocks +
				", seckillSkuList=" + seckillSkuList +
				'}';
	}
}
