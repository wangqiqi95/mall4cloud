package com.mall4j.cloud.coupon.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

/**
 * @description 优惠券码关联表
 * @author shijing
 * @date 2022-01-03
 */
@Data
@TableName("t_coupon_code")
public class TCouponCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
    private Long id;

    /**
     * 关联优惠券id
     */
    private Long couponId;

    /**
     * 券码
     */
    private String couponCode;

    /**
     * 状态（0：未核销/1：已核销）
     */
    private Short status;

}
