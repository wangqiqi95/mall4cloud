package com.mall4j.cloud.coupon.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 领取优惠券DTO
 *
 * @author shijing
 */
@Data
public class ReceiveCouponDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "优惠券ID",required = true)
    private Long couponId;

    @ApiModelProperty(value = "用户ID",required = true)
    private Long userId;

    @ApiModelProperty(value = "门店ID")
    private Long shopId;

    @ApiModelProperty(value = "活动ID",required = true)
    private Long activityId;

    @ApiModelProperty(value = "活动来源",required = true)
    private Integer activitySource;

    @ApiModelProperty("某次领取批次号")
    private Long batchId;

    @ApiModelProperty("导购id")
    private Long staffId;
    private String staffName;
    private String staffPhone;

    @ApiModelProperty("微信支付编号")
    private String wechatPayNo;

    @ApiModelProperty("发放人id")
    private Long createId;

    @ApiModelProperty("发放人名称")
    private String createName;

    @ApiModelProperty("发放人手机号")
    private String createPhone;

}
