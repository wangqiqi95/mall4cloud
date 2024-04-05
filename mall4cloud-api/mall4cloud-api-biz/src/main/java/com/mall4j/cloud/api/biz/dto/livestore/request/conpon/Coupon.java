package com.mall4j.cloud.api.biz.dto.livestore.request.conpon;

import lombok.Data;

@Data
public class Coupon {

    //商家侧优惠券ID
    private String out_coupon_id;
    /**
     * 优惠券类型
     * 枚举值	描述
     * 1	商品条件折扣券
     * 2	商品满减券
     * 3	商品统一折扣券
     * 4	商品直减券
     * 5	商品换购券
     * 6	商品买赠券
     * 101	店铺条件折扣券
     * 102	店铺满减券
     * 103	店铺统一折扣券
     * 104	店铺直减券
     */
    private Integer type;
    //优惠券推广类型
    //4	视频号直播
    private Integer promote_type;
    //优惠券状态
    private Integer status;
    //创建时间（秒级时间戳）
    private Long create_time;
    //更新时间（秒级时间戳）
    private Long update_time;

    private String appid;

    private CouponInfo coupon_info;

}
