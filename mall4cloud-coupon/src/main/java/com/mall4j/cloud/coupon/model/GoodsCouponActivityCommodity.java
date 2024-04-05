package com.mall4j.cloud.coupon.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;


/**
 * @description 优惠券商品关联表
 * @author shijing
 * @date 2022-02-27
 */
@Data
public class GoodsCouponActivityCommodity implements Serializable {

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
     * 商品id
     */
    private Long commodityId;

}
