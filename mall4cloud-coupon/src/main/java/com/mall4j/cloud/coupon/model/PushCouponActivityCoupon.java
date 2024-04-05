package com.mall4j.cloud.coupon.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;

/**
 * @description 推券活动优惠券关联表
 * @author shijing
 * @date 2022-01-05
 */
@Data
public class PushCouponActivityCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 推券活动id
     */
    private Long activityId;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 是否限量
     */
    private Boolean isStocksLimit;

    /**
     * 库存
     */
    private Integer stocks;

    /**
     * 是否累计限领
     */
    private Boolean isPersonLimit;

    /**
     * 累计限领
     */
    private Integer limitNum;

    /**
     * 是否每人每天限领
     */
    private Boolean isDailyLimit;

    /**
     * 每人每天限领
     */
    private Integer dailyLimitNum;

    /**
     * 版本号
     */
    private Integer version;

}
