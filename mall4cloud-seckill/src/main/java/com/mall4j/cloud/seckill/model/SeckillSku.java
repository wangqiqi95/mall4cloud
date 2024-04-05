package com.mall4j.cloud.seckill.model;

import java.io.Serializable;

import com.mall4j.cloud.common.model.BaseModel;
/**
 * 秒杀活动sku
 *
 * @author lhd
 * @date 2021-03-30 14:53:09
 */
public class SeckillSku extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 秒杀活动单个skuid
     */
    private Long seckillSkuId;

    /**
     * skuId
     */
    private Long skuId;

    /**
     * 秒杀活动id
     */
    private Long seckillId;

    /**
     * 用于秒杀的库存
     */
    private Integer seckillStocks;

    /**
     * 秒杀价格
     */
    private Long seckillPrice;

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

	public Integer getSeckillStocks() {
		return seckillStocks;
	}

	public void setSeckillStocks(Integer seckillStocks) {
		this.seckillStocks = seckillStocks;
	}

	public Long getSeckillPrice() {
		return seckillPrice;
	}

	public void setSeckillPrice(Long seckillPrice) {
		this.seckillPrice = seckillPrice;
	}

	@Override
	public String toString() {
		return "SeckillSku{" +
				"seckillSkuId=" + seckillSkuId +
				", skuId=" + skuId +
				", seckillId=" + seckillId +
				", seckillStocks=" + seckillStocks +
				", seckillPrice=" + seckillPrice +
				'}';
	}
}
