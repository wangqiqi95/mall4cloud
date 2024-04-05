package com.mall4j.cloud.group.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "签到活动详情实体")
public class SignActivityVO implements Serializable {
    /**
     * id
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String activityName;

    /**
     * 活动开始时间
     */
    @ApiModelProperty("活动开始时间")
    private Date activityBeginTime;

    @ApiModelProperty("活动结束时间")
    private Date activityEndTime;
    /**
     * 适用范围：0：不限 1：仅会员
     */
    @ApiModelProperty("适用范围：0：不限 1：仅会员")
    private int applicationRange;

    /**
     * 活动类型：0循环周期 1：自定义周期
     */
    @ApiModelProperty("活动类型：0循环周期 1：自定义周期")
    private int activityType;

    /**
     * 循环周期类型： 0：周 1：月 2：年
     */
    @ApiModelProperty("循环周期类型： 0：周 1：月 2：年")
    private int roundType;

    /**
     * 自定义周期天数
     */
    @ApiModelProperty("自定义周期天数")
    private Integer roundDayTime;

    /**
     * 活动banner图片地址
     */
    @ApiModelProperty("活动banner图片地址")
    private String activityBannerUrl;

    /**
     * 日签奖励类型 0 积分 1 优惠券 2 无奖励
     */
    @ApiModelProperty("日签奖励类型 0 积分 1 优惠券 2 无奖励")
    private int daySignRewardType;

    /**
     * 日签奖励积分数
     */
    @ApiModelProperty("日签奖励积分数")
    private Integer daySignPoint;

    /**
     * 日签奖励优惠券id
     */
    @ApiModelProperty("日签奖励优惠券id")
    private String daySignCouponId;

    /**
     * 通知时间
     */
    @ApiModelProperty("通知时间")
    private String noticeTime;

    @ApiModelProperty("连续签到开关")
    private Integer seriesSignSwitch;

    /**
     * 连续签到奖励次数
     */
    @ApiModelProperty("连续签到奖励次数")
    private Integer seriesSignRewardTimes;

    /**
     * 活动规则
     */
    @ApiModelProperty("活动规则")
    private String activityRule;

    @ApiModelProperty("活动状态 0 未启用 1已启用")
    private Integer activityStatus;

    @ApiModelProperty("活动连签奖励列表")
    private List<SignActivityRewardVO> rewards;
}
