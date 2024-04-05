package com.mall4j.cloud.seckill.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 秒杀订单VO
 *
 * @author lhd
 * @date 2021-03-30 14:59:28
 */
public class SeckillOrderVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("秒杀订单id")
    private Long seckillOrderId;

    @ApiModelProperty("秒杀订单id")
    private Long seckillId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("秒杀到的商品数量")
    private Integer prodCount;

    @ApiModelProperty("商品id")
    private Long spuId;

    @ApiModelProperty("-1指无效，0指成功，1指已付款")
    private Integer state;

    @ApiModelProperty("秒杀到的秒杀商品sku")
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
