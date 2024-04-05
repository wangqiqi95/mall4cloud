package com.mall4j.cloud.coupon.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author shijing
 * @date 2022-02-27
 */
@Data
public class GoodsActivityVO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键id")
	private Long id;

	@ApiModelProperty(value = "活动名称")
	private String title;

	@ApiModelProperty(value = "优惠券id")
	private Long couponId;

	@ApiModelProperty(value = "商品数量")
	private Integer commodityNum;

	@ApiModelProperty(value = "商品集合")
	private List<Long> commodityIds;

	@ApiModelProperty(value = "是否全部门店")
	private Boolean isAllShop;

	@ApiModelProperty(value = "门店数量")
	private Integer shopNum;

	@ApiModelProperty("门店id列表")
	private List<Long> shops;

	@ApiModelProperty(value = "累计限制领取数")
	private Long personMaxAmount;

	@ApiModelProperty(value = "每人每天限制兑换数")
	private Long personDayAmount;

	@ApiModelProperty(value = "开始时间")
	private Date startTime;

	@ApiModelProperty(value = "结束时间")
	private Date endTime;

	@ApiModelProperty(value = "状态")
	private int status;


}
