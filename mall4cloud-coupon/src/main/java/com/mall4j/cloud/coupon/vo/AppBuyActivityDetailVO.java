package com.mall4j.cloud.coupon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 商城小程序现金买券
 * @author shijing
 * @date 2022-02-05
 */
@Data
@ApiModel(description = "活动详情信息")
public class AppBuyActivityDetailVO {

    @ApiModelProperty(value = "买券活动id")
    private Long id;

    @ApiModelProperty(value = "活动名称")
    private String title;

    @ApiModelProperty(value = "活动banner")
    private String banner;

    @ApiModelProperty(value = "优惠券")
    private List<AppBuyCouponVO> coupons;

    @ApiModelProperty(value = "适用门店")
    private List<Long> shops;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;


}
