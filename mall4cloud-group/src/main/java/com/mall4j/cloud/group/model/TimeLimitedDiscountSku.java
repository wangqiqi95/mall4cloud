package com.mall4j.cloud.group.model;

import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.io.Serializable;
/**
 * 限时调价活动 sku价格
 *
 * @author FrozenWatermelon
 * @date 2022-03-10 13:29:18
 */
@Data
public class TimeLimitedDiscountSku extends BaseModel implements Serializable{
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
    private Long spuId;

    /**
     * 
     */
    private Long skuId;

    /**
     * 售价，整数方式保存
     */
    private Long price;

}
