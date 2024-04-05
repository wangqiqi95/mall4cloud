package com.mall4j.cloud.coupon.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 推券活动门店关联表
 * @author shijing
 * @date 2022-01-05
 */
@Data
public class BuyInventoryLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
    private Long id;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 关联优惠券id
     */
    private Long couponId;

    /**
     * 调整库存数
     */
    private Integer num;

    /**
     * 操作人
     */
    private Long createId;

    /**
     * 操作人工号
     */
    private String createCode;

    /**
     * 操作人姓名
     */
    private String createName;

    /**
     * 操作时间
     */
    private Date createTime;

}
