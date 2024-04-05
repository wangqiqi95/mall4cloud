package com.mall4j.cloud.group.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "签到活动列表")
public class SignActivityListVO implements Serializable {
    @ApiModelProperty("活动id")
    private Integer id;
    @ApiModelProperty("活动名称")
    private String activityName;

    @ApiModelProperty("活动状态 0 未启用 1已启用 2进行中 3 未开始 4已结束")
    private Integer activityStatus;

    @ApiModelProperty("活动开始时间")
    private Date activityBeginTime;

    @ApiModelProperty("活动结束时间")
    private Date activityEndTime;

    @ApiModelProperty("活动类型：0循环周期 1：自定义周期")
    private int activityType;

    @ApiModelProperty("活动周期")
    private Integer roundDayTime;

    @ApiModelProperty("创建人姓名")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("连续签到开关 0 常规签到 1连续签到")
    private Integer seriesSignSwitch;
}
