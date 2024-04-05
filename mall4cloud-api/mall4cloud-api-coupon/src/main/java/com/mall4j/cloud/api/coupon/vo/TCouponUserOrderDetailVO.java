package com.mall4j.cloud.api.coupon.vo;

import lombok.Data;

/**
 * 订单优惠券使用对象
 *
 * @luzhengxiang
 * @create 2022-03-31 4:38 PM
 **/
@Data
public class TCouponUserOrderDetailVO {
    /**
     * 主键id
     */
    private Long id;

    private Long couponId;

    private String couponName;

    private String couponCode;

    private Long orderNo;

    private Long couponAmount;

    private Integer kind;

    private Integer sourceType;

}
