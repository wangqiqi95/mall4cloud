package com.mall4j.cloud.coupon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(description = "券活动明细查询")
public class CouponActivityDTO implements Serializable {

    @ApiModelProperty(value = "页码",required = true)
    private Integer pageNo = 1;

    @ApiModelProperty(value = "每页长度",required = true)
    private Integer pageSize = 10;

    @ApiModelProperty(value = "活动来源",required = true)
    private Integer activitySource;

    @ApiModelProperty(value = "活动id",required = true)
    private Long activityId;

    @ApiModelProperty(value = "优惠券id",required = true)
    private Long couponId;

    @ApiModelProperty(value = "会员手机号")
    private String userPhone;

    @ApiModelProperty(value = "领取门店id")
    private List<Long> shopIds;

    @ApiModelProperty(value = "核销门店id")
    private List<Long> writeOffShopIds;

    @ApiModelProperty(value = "发放人信息")
    private String userInfo;

    @ApiModelProperty(value = "核销人信息")
    private String writeOffInfo;

    @ApiModelProperty(value = "领取开始时间")
    private Date receiveStartTime;

    @ApiModelProperty(value = "领取结束时间")
    private Date receiveEndTime;

    @ApiModelProperty(value = "核销开始时间")
    private Date writeOffStartTime;

    @ApiModelProperty(value = "核销结束时间")
    private Date writeOffEndTime;

    /**
     * 优惠券来源信息（1：小程序添加/2：CRM同步优惠券）
     */
    @ApiModelProperty(value = "优惠券来源信息： 1：小程序添加/2：CRM同步优惠券")
    private Integer couponSourceType;

}
