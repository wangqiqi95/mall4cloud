package com.mall4j.cloud.seckill.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
/**
 * 秒杀订单
 *
 * @author lhd
 * @date 2021-03-30 14:59:28
 */
public class SeckillOrder extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 秒杀订单id
     */
    private Long seckillOrderId;

    /**
     * 秒杀订单id
     */
    private Long seckillId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 秒杀到的商品数量
     */
    private Integer prodCount;
    /**
     * 商品id
     */
    private Long spuId;

    /**
     * -1指无效，0指待付款，1指已付款
     */
    private Integer state;

    /**
     * 秒杀到的秒杀商品sku
     */
    private Long seckillSkuId;

	public Long getSeckillOrderId() {
		return seckillOrderId;
	}

	public void setSeckillOrderId(Long seckillOrderId) {
		this.seckillOrderId = seckillOrderId;
	}

	public Long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(Long seckillId) {
		this.seckillId = seckillId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getProdCount() {
		return prodCount;
	}

	public void setProdCount(Integer prodCount) {
		this.prodCount = prodCount;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getSeckillSkuId() {
		return seckillSkuId;
	}

	public void setSeckillSkuId(Long seckillSkuId) {
		this.seckillSkuId = seckillSkuId;
	}

	@Override
	public String toString() {
		return "SeckillOrderVO{" +
				"seckillOrderId=" + seckillOrderId +
				",seckillId=" + seckillId +
				",userId=" + userId +
				",orderId=" + orderId +
				",prodCount=" + prodCount +
				",spuId=" + spuId +
				",state=" + state +
				",seckillSkuId=" + seckillSkuId +
				'}';
	}
}
