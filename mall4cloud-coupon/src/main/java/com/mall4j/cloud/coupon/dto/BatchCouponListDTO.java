package com.mall4j.cloud.coupon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 小程序优惠券列表搜索条件
 * @author shijing
 * @date 2022-02-07 14:55:56
 */
@Data
@ApiModel(description = "优惠券列表搜索条件")
public class BatchCouponListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "页码",required = true)
	private Integer pageNo = 1;

	@ApiModelProperty(value = "每页长度",required = true)
	private Integer pageSize = 10;

	@ApiModelProperty(value = "兑换批次号", required = true)
	private Long batchId;

	@ApiModelProperty(value = "用户id")
	private Long userId;
}
