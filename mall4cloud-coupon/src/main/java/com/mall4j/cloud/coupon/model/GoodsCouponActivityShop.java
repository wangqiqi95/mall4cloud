package com.mall4j.cloud.coupon.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;

/**
 * @description 推券活动门店关联表
 * @author shijing
 * @date 2022-02-27
 */
@Data
public class GoodsCouponActivityShop implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
    private Long id;

    /**
     * 商详领券活动id
     */
    private Long activityId;

    /**
     * 关联门店id
     */
    private Long shopId;

}
