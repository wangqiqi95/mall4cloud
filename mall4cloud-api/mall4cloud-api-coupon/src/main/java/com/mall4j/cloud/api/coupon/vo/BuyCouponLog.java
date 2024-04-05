package com.mall4j.cloud.api.coupon.vo;

import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @description 购券记录
 * @author shijing
 * @date 2022-02-06
 */
@Data
public class BuyCouponLog implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 门店id
     */
    private Long shopId;

    /**
     * 价格
     */
    private Long price;

    /**
     * 支付状态（0：已支付/1：未支付）
     */
    private int payStatus;

    /**
     * 订单编号
     */
    private Long orderNo;

    /**
     * 微信支付编号
     */
    private String wechatPayNo;

    /**
     * create_id
     */
    private Long createId;

    /**
     * create_name
     */
    private String createName;

    /**
     * create_time
     */
    private Timestamp createTime;

    /**
     * update_id
     */
    private Long updateId;

    /**
     * update_name
     */
    private String updateName;

    /**
     * update_time
     */
    private Timestamp updateTime;

}
