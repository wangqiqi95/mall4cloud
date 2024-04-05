package com.mall4j.cloud.user.model.scoreConvert;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @description 积分banner活动门店关联表
 * @author shijing
 * @date 2022-01-23
 */
@Data
public class ScoreBannerShop implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
    private Long id;

    /**
     * 积分banner活动id
     */
    private Long bannerId;

    /**
     * 关联门店id
     */
    private Long shopId;

}
