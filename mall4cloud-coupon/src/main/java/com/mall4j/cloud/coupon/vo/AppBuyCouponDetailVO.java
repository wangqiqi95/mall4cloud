package com.mall4j.cloud.coupon.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.License;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 优惠券
 *
 * @author shijing
 */
@Data
public class AppBuyCouponDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty("活动id")
	private Long activityId;

    @ApiModelProperty("优惠券ID")
    private Long couponId;

	@ApiModelProperty(value = "优惠券名称")
	private String name;

	@ApiModelProperty(value = "优惠券封面")
	private String coverUrl;

	@ApiModelProperty(value = "价格")
	private Long price;

	@ApiModelProperty(value = "生效时间类型（0：固定时间/1：领取后生效）")
	private int timeType;

	@ApiModelProperty(value = "生效开始时间")
	private Date startTime;

	@ApiModelProperty(value = "生效结束时间")
	private Date endTime;

	@ApiModelProperty(value = "领券后x天起生效")
	private Integer afterReceiveDays;

	@ApiModelProperty(value = "库存")
	private Integer stocks;

	@ApiModelProperty(value = "适用门店")
	private List<Long> shops;


}
