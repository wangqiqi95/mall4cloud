package com.mall4j.cloud.user.model.scoreConvert;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

/**
 * 积分活动门店表
 *
 * @author shijing
 * @date 2021-12-10 17:18:04
 */

@Data
@TableName("score_convert_shop")
public class ScoreConvertShop implements Serializable {

    private static final long serialVersionUID = -6991847232558878772L;

    /**
     * 主键ID
     */
    @TableId(type= IdType.AUTO)
    private Long id;

    /**
     * 积分兑换活动ID
     */
    private Long convertId;


    /**
     * 门店ID
     */
    private Long shopId;

    /**
     * 类型（0：积分活动相关门店/1：积分活动线下兑换门店/2：优惠券适用门店）
     */
    private Short type;

}
