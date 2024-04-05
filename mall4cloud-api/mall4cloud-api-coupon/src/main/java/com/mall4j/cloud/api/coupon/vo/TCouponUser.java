package com.mall4j.cloud.api.coupon.vo;

import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @description 优惠券用户关联信息
 * @author shijing
 * @date 2022-01-15
 */
@Data
public class TCouponUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 优惠券券码
     */
    private String couponCode;

    /**
     * 优惠券来源信息（1：小程序添加/2：CRM同步优惠券）
     */
    private Short couponSourceType;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 活动来源
     */
    private Integer activitySource;
    /**
     * 某次领取批次号
     */
    private Long batchId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 领券时间
     */
    private Date receiveTime;

    /**
     * 开始时间
     */
    private Date userStartTime;

    /**
     * 结束时间
     */
    private Date userEndTime;

    /**
     * 优惠券状态 0:失效 1:有效 2:使用过
     */
    private int status;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    /**
     * 导购id
     */
    private Long staffId;

    /**
     * 订单编号
     */
    private Long orderNo;

    /**
     * 订单金额
     */
    private Long orderAmount;

    /**
     * 优惠金额
     */
    private Long couponAmount;

    /**
     * 核销时间
     */
    private Timestamp writeOffTime;

    /**
     * crm返回token
     */
    private String token;

}