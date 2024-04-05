package com.mall4j.cloud.group.model;

import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 限时调价活动 商铺
 *
 * @author FrozenWatermelon
 * @date 2022-03-10 01:55:14
 */
@Data
public class TimeLimitedDiscountShop extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Integer id;

    /**
     *
     */
    private Integer activityId;

    /**
     *
     */
    private Long shopId;
}