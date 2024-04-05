package com.mall4j.cloud.group.dto;

import com.mall4j.cloud.common.order.dto.OrderDTO;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 拼团订单
 *
 * @author FrozenWatermelon
 * @date 2021-04-07 10:39:32
 */
public class GroupOrderDTO extends OrderDTO {

	@NotNull(message = "活动商品规格Id不能为空")
	@ApiModelProperty(value = "拼团商品规格Id", required = true)
	private Long groupSkuId;

	public Long getGroupSkuId() {
		return groupSkuId;
	}

	public void setGroupSkuId(Long groupSkuId) {
		this.groupSkuId = groupSkuId;
	}

	@Override
	public String toString() {
		return "GroupOrderDTO{" +
				", groupSkuId=" + groupSkuId +
				'}';
	}
}
