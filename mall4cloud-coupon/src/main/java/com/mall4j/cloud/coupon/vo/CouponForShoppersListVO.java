package com.mall4j.cloud.coupon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 活动优惠券详情信息
 * @author shijing
 * @date 2022-01-12
 */
@Data
@ApiModel(description = "导购端送券中心详情")
public class CouponForShoppersListVO {

    @ApiModelProperty(value = "打折券")
    private List<CouponForShoppersVO> discountCoupons;

    @ApiModelProperty(value = "抵用券")
    private List<CouponForShoppersVO> voucherCoupons;


}
