package com.mall4j.cloud.coupon.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @description 优惠券门店关联信息
 * @author shijing
 * @date 2022-02-04
 */
@Data
public class TCouponShop implements Serializable {

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
     * 门店id
     */
    private Long shopId;

    /**
     * 第三方门店code
     */
    private String shopCode;


}