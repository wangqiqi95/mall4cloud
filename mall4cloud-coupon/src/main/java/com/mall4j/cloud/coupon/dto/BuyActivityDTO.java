package com.mall4j.cloud.coupon.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 优惠券 *
 * @author shijing
 * @date 2022-01-05
 */
@Data
public class BuyActivityDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键id")
	private Long id;

	@ApiModelProperty(value = "活动名称")
	private String title;

	@ApiModelProperty(value = "券码集合")
	private List<BuyActivityCouponDTO> coupons;

	@ApiModelProperty(value = "是否全部门店")
	private Boolean isAllShop;

	@ApiModelProperty("门店id列表")
	private List<Long> shops;

	@ApiModelProperty("活动入口banner")
	private String banner;

	@ApiModelProperty(value = "开始时间")
	private Date startTime;

	@ApiModelProperty(value = "结束时间")
	private Date endTime;

	@ApiModelProperty(value = "权重")
	private Integer weight;

	@ApiModelProperty(value = "状态")
	private int status;


}
