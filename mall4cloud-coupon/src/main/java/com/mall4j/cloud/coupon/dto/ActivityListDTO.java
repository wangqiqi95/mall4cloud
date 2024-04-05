package com.mall4j.cloud.coupon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 优惠券列表搜索条件 *
 * @author shijing
 * @date 2022-01-05
 */
@Data
@ApiModel(description = "活动列表搜索条件")
public class ActivityListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "页码",required = true)
    private Integer pageNo = 1;
    @ApiModelProperty(value = "每页长度",required = true)
    private Integer pageSize = 10;

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @ApiModelProperty(value = "优惠券id")
    private Long couponId;

    @ApiModelProperty(value = "优惠券名称")
    private String CouponName;

    @ApiModelProperty(value = "活动状态（0：有效/1：失效）")
    private Short status;

    @ApiModelProperty("是否全部门店")
    private Boolean isAllShop;

    @ApiModelProperty("门店id列表")
    private List<Long> shops;
}
