package com.mall4j.cloud.group.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 促销位 活动 关联门店
 *
 * @author FrozenWatermelon
 * @date 2023-07-18 17:09:16
 */
@Data
@TableName(value = "promotion_column_shop")
public class PromotionColumnShop implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
	@TableId(type = IdType.AUTO)
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
