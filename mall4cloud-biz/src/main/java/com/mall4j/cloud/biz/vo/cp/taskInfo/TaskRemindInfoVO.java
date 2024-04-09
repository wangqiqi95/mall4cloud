package com.mall4j.cloud.biz.vo.cp.taskInfo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务提醒表
 */
@Data
public class TaskRemindInfoVO {
    @ApiModelProperty("提醒类型 1任务导购 2指定员工 3任务导购和指定员工")
    private Integer remindType;
    @ApiModelProperty("提醒场景 1新任务生成时 2当任务开始后x个小时 3当任务还剩x且任务完成率未达x")
    private Integer remindScenes;
    @ApiModelProperty("开始多少个小时后提醒")
    private Double startAfterHours;
    @ApiModelProperty("剩余多少个小时")
    private Double lastHours;
    @ApiModelProperty("达标比例")
    private Double standardScale;
}

