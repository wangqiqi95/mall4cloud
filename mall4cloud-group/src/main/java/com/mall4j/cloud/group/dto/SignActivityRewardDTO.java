package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "连签奖励实体")
public class SignActivityRewardDTO implements Serializable {
    /**
     * id
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 签到活动id
     */
    @ApiModelProperty("签到活动id")
    private Integer signActivityId;

    /**
     * 连签天数
     */
    @ApiModelProperty("连签天数")
    private Integer seriesSignDay;

    /**
     * 奖励名称
     */
    @ApiModelProperty("奖励名称")
    private String rewardName;

    /**
     * 奖励图片url
     */
    @ApiModelProperty("奖励图片url")
    private String rewardPicUrl;

    /**
     * 积分开关 0 关 1开
     */
    @ApiModelProperty("积分开关 0 关 1开")
    private int pointSwitch;

    /**
     * 积分数
     */
    @ApiModelProperty("积分数")
    private Integer pointNum;

    /**
     * 优惠券开关 0关 1开
     */
    @ApiModelProperty("优惠券开关 0关 1开")
    private int couponSwitch;

    /**
     * 优惠券id
     */
    @ApiModelProperty("优惠券id")
    private String couponId;
}
