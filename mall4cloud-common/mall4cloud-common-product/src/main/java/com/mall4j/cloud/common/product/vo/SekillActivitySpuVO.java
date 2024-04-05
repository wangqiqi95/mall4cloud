package com.mall4j.cloud.common.product.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 拼团活动表VO
 *
 * @author YXF
 * @date 2021-03-20 10:39:31
 */
public class SekillActivitySpuVO {

    @ApiModelProperty("秒杀活动id")
    private Long seckillId;

    @ApiModelProperty("商品id")
    private Long spuId;

	@ApiModelProperty("秒杀活动最低价")
	private Long seckillPrice;


	@ApiModelProperty("秒杀活动剩余总库存")
	private Integer seckillTotalStocks;

	@ApiModelProperty("秒杀活动原始库存")
	private Integer seckillOriginStocks;

	@ApiModelProperty("秒杀活动原价（最低价sku对应的原价）")
	private Long priceFee;

	/**
	 * skuId(最低秒杀价对应的s'kuId)
	 * @return
	 */
	private Long skuId;

	public Long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(Long seckillId) {
		this.seckillId = seckillId;
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

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Long getPriceFee() {
		return priceFee;
	}

	public void setPriceFee(Long priceFee) {
		this.priceFee = priceFee;
	}

	@Override
	public String toString() {
		return "SekillActivitySpuVO{" +
				"seckillId=" + seckillId +
				", spuId=" + spuId +
				", seckillPrice=" + seckillPrice +
				", seckillTotalStocks=" + seckillTotalStocks +
				", seckillOriginStocks=" + seckillOriginStocks +
				", skuId=" + skuId +
				", priceFee=" + priceFee +
				'}';
	}
}
