package com.mall4j.cloud.user.model.scoreConvert;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;

/**
 * @description 兑换活动优惠券中间表
 * @author shijing
 * @date 2022-01-29
 */
@Data
public class ScoreConvertCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
    private Long id;

    /**
     * 兑换活动id
     */
    private Long convertId;

    /**
     * 优惠券id
     */
    private Long couponId;

}
