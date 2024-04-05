package com.mall4j.cloud.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 优惠券DTO
 *
 * @author shijing
 * @date 2022-03-02 16:22:56
 */
@Data
public class UpdateCouponStatusDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户优惠券主键id")
    private Long id;

	@ApiModelProperty("优惠券状态")
	private Integer status;

	@ApiModelProperty(value = "用户id")
	private Long userId;

	@ApiModelProperty(value = "门店编码")
	private String storeCode;

	@ApiModelProperty(value = "销售单号")
	private Long orderNo;

}
