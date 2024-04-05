package com.mall4j.cloud.coupon.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
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

    @TableId(type = IdType.AUTO)
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
     * 活动来源（1：推券/2：领券/3：买券/4：积分活动）
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
     * 关联crm会员id
     */
    private String vipCode;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户手机号
     */
    private String userPhone;

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
     * 优惠券状态 0:冻结 1:有效 2:核销
     */
    private int status;

    /**
     * 导购id
     */
    private Long staffId;

    /**
     * 领取门店ID
     */
    private Long shopId;

    /**
     * 领取门店名称
     */
    private String shopName;

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
     * 核销人id
     */
    private Long writeOffUserId;

    /**
     * 核销人名称
     */
    private String writeOffUserName;

    /**
     * 核销人工号
     */
    private String writeOffUserCode;

    /**
     * 核销人手机号
     */
    private String writeOffUserPhone;


    /**
     * 核销门店id
     */
    private Long writeOffShopId;

    /**
     * 核销门店名称
     */
    private String writeOffShopName;

    /**
     * 核销时间
     */
    private Timestamp writeOffTime;

    /**
     * 微信支付编号
     */
    private String wechatPayNo;

    /**
     * 发放人id
     */
    private Long createId;

    /**
     * 发放人名称
     */
    private String createName;

    /**
     * 发放人手机号
     */
    private String createPhone;

    /**
     * 更新时间
     */
    private Timestamp updateTime;


}