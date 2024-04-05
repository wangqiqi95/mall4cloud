package com.mall4j.cloud.coupon.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

/**
 * @description 优惠券商品关联表
 * @author shijing
 * @date 2022-01-03
 */
@Data
@TableName("t_coupon_commodity")
public class TCouponCommodity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
    private Long id;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 商品id
     */
    private Long commodityId;

    /**
     * 商品编码 与三方一致
     */
    private String spuCode;

}
