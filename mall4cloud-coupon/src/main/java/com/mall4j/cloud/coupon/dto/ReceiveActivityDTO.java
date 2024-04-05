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
public class ReceiveActivityDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键id")
	private Long id;

	@ApiModelProperty(value = "活动名称")
	private String title;

	@ApiModelProperty(value = "券码集合")
	private List<ReceiveActivityCouponDTO> coupons;

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

	// 消费限制
	@ApiModelProperty("订单消费限制开关")
	private Integer orderSwitch;

	@ApiModelProperty("订单消费开始时间")
	private Date orderStartTime;

	@ApiModelProperty("订单消费结束时间")
	private Date orderEndTime;

	@ApiModelProperty("订单消费类型【0:表示累计消费;1:表示单笔消费】")
	private Integer orderExpendType;

	@ApiModelProperty("订单消费金额限制")
	private Long orderNum;

	@ApiModelProperty("订单类型限制")
	private String orderType;

	@ApiModelProperty("订单金额不足提示")
	private String orderTips;

	@ApiModelProperty("粉丝等级集合")
	private String fanLevels;

	@ApiModelProperty("粉丝等级不足提示")
	private String fanTips;

	@ApiModelProperty("指定消费门店")
	private String appointShop;

}
