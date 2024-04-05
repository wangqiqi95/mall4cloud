package com.mall4j.cloud.api.user.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLevelVO implements Serializable {
    /**
     * 主键
     */
    private Long userLevelId;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 等级名称
     */
    private String levelName;

    /**
     * 等级类型 0:普通会员 1:付费会员
     */
    private Integer levelType;

    /**
     * 所需成长值
     */
    private Integer needGrowth;

    /**
     * 1:已更新 0:等待更新(等级修改后，用户等级的更新)
     */
    private Integer updateStatus;

    /**
     * 付费会员，是否可以招募会员；1可以招募，0停止招募
     */
    private Integer recruitStatus;
}
