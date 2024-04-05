package com.mall4j.cloud.seckill.dto.app;

import com.mall4j.cloud.common.order.dto.OrderDTO;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 秒杀订单
 *
 * @author FrozenWatermelon
 * @date 2021-04-12 10:39:32
 */
public class SeckillOrderDTO extends OrderDTO {

	@NotNull(message = "活动商品规格Id不能为空")
	@ApiModelProperty(value = "拼团商品规格Id", required = true)
	private Long seckillSkuId;

	@NotNull
	@ApiModelProperty(value = "秒杀id", required = true)
	private Long seckillId;

	public Long getSeckillSkuId() {
		return seckillSkuId;
	}

	public void setSeckillSkuId(Long seckillSkuId) {
		this.seckillSkuId = seckillSkuId;
	}

	public Long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(Long seckillId) {
		this.seckillId = seckillId;
	}

	@Override
	public String toString() {
		return "SeckillOrderDTO{" +
				"seckillSkuId=" + seckillSkuId +
				", seckillId=" + seckillId +
				'}';
	}
}
