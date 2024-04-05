package com.mall4j.cloud.group.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("sign_activity")
@TableName(value = "sign_activity")
public class SignActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
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
    private Integer applicationRange;

    /**
     * 活动类型：0循环周期 1：自定义周期
     */
    @ApiModelProperty("活动类型：0循环周期 1：自定义周期")
    private Integer activityType;

    /**
     * 循环周期类型： 0：周 1：月 2：年
     */
    @ApiModelProperty("循环周期类型： 0：周 1：月 2：年")
    private Integer roundType;

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
    private Integer daySignRewardType;

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

    private Integer deleted;

    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUserId;
    /**
     * 创建人姓名
     */
    @ApiModelProperty("创建人姓名")
    private String createUserName;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private Long updateUserId;

    /**
     * 更新人
     */
    @ApiModelProperty("更新人姓名")
    private String updateUserName;
}