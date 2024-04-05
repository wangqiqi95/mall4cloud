package com.mall4j.cloud.coupon.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChooseMemberEventCouponRelationVO {

    @ApiModelProperty(value = "活动ID（t_choose_member_event表ID）")
    private Long eventId;

    @ApiModelProperty(value = "优惠券ID")
    private Long couponId;
}
