package com.mall4j.cloud.coupon.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
public class TCouponSkuDTO {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * spuId
     */
    private Long spuId;

    /**
     * skuId
     */
    private Long skuId;
}
