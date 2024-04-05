package com.mall4j.cloud.user.model.scoreConvert;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;

/**
 * @description 积分活动禁用手机号关联表
 * @author shijing
 * @date 2022-01-23
 */
@Data
public class ScoreConvertPhone implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
    private Long id;

    /**
     * 积分活动id
     */
    private Long convertId;

    /**
     * 禁用手机号
     */
    private String phoneNum;

}