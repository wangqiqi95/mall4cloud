package com.mall4j.cloud.group.vo.app;

import com.mall4j.cloud.api.coupon.vo.CouponDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("支付有礼小程序端实体")
public class PayActivityInfoAppVO implements Serializable {
    @ApiModelProperty("活动id")
    private Integer id;
    @ApiModelProperty("活动名称")
    private String activityName;
    @ApiModelProperty("活动图片地址")
    private String activityPicUrl;
    @ApiModelProperty("赠送积分开关")
    private Integer activityPointSwitch;
    @ApiModelProperty("赠送积分数")
    private Integer activityPointNumber;
    @ApiModelProperty("是否已领取积分")
    private boolean pointFlag;
    @ApiModelProperty("赠送优惠券开关")
    private Integer activityCouponSwitch;
    @ApiModelProperty("赠送优惠券id")
    private String activityCouponId;
    @ApiModelProperty("是否已领取优惠券")
    private boolean couponFlag;
    @ApiModelProperty("优惠券详情")
    private CouponDetailVO couponDetail;
}
