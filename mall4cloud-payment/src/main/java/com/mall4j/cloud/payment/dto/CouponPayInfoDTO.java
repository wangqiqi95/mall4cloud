package com.mall4j.cloud.payment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 充值金额id
 *
 * @author FrozenWatermelon
 */
@Data
public class CouponPayInfoDTO extends BasePayInfoDTO {

	@NotNull
	@ApiModelProperty(value = "购买券订单号")
	private Long couponOrderId;

}
