package com.mall4j.cloud.coupon.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;

/**
 * @description 商详领券活动表
 * @author shijing
 * @date 2022-02-27
 */
@Data
public class GoodsCouponActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
    private Long id;

    /**
     * 活动标题
     */
    private String title;

    /**
     * 活动状态（0：启用/1：禁用）
     */
    private int status;

    /**
     * 活动开始时间
     */
    private Date startTime;

    /**
     * 活动结束时间
     */
    private Date endTime;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 每人限制兑换总数
     */
    private Long personMaxAmount;

    /**
     * 每人每天限制兑换数
     */
    private Long personDayAmount;

    /**
     * 是否全部门店（0：全部门店/1：部分门店）
     */
    private Boolean isAllShop;

    /**
     * 创建人
     */
    private Long createId;

    /**
     * 创建人姓名
     */
    private String createName;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 修改人
     */
    private Long updateId;

    /**
     * 修改人姓名
     */
    private String updateName;

    /**
     * 修改时间
     */
    private Timestamp updateTime;

    /**
     * 是否删除
     */
    private int del;

}
