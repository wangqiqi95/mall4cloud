package com.mall4j.cloud.coupon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 商城小程序领券中心
 * @author shijing
 * @date 2022-02-05
 */
@Data
@ApiModel(description = "活动详情信息")
public class AppReceiveActivityVO {

    @ApiModelProperty(value = "领券活动id")
    private Long id;

    @ApiModelProperty(value = "活动banner")
    private String banner;

    @ApiModelProperty(value = "优惠券集合")
    private List<AppCouponVO> coupons;

}
