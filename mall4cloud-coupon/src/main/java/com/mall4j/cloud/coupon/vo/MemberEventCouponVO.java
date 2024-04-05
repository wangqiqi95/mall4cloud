package com.mall4j.cloud.coupon.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberEventCouponVO {
    @ApiModelProperty("活动优惠券关系表ID")
    private Long relationId;

    @ApiModelProperty("优惠券ID")
    private Long couponId;

    @ApiModelProperty("优惠券名称")
    private String name;

    @ApiModelProperty("优惠券类型")
    private Integer type;

    @ApiModelProperty("渠道类型")
    private Integer sourceType;

    @ApiModelProperty("优惠券面额/折扣")
    private Long amountLimitNum;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("有效期开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("有效期结束时间")
    private LocalDateTime endTime;
}
