package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class AddTaskManagementDTO {

    @NotBlank(message = "任务名称为必传项")
    @ApiModelProperty("任务名称")
    private String taskName;

    @NotNull(message = "任务类型为必传项")
    @ApiModelProperty(value = "任务类型：0加企微好友 1 分享素材 2回访客户")
    private Integer taskType;

    @ApiModelProperty(value = "任务客户列表")
    private List<Long> clientIds;

    @NotNull(message = "分配数量为必传项")
    @ApiModelProperty(value = "任务每天限制分配数量")
    private Integer quantityAllotted;

    @NotNull(message = "执行方式为必传项")
    @ApiModelProperty(value = "执行方式 0导购执行 1 系统执行(需要对接机器人) 2一键转发")
    private Integer executionMode;

    @ApiModelProperty(value = "任务话术")
    private String scriptContent;

    @NotNull(message = "公司类型为必传项")
    @ApiModelProperty(value = "公司类型 0麦吉利")
    private Integer affiliatedCompanyType;

    @NotNull(message = "门店任务类型为必传项")
    @ApiModelProperty(value = "门店任务类型 0全部 1指定")
    private Integer storeSelectionType;

    @NotNull(message = "导购任务类型为必传项")
    @ApiModelProperty(value = "导购任务范围类型 0全部 1指定")
    private Integer userRangeType;

    @NotNull(message = "导购任务类型为必传项")
    @ApiModelProperty(value = "任务目标比例")
    private BigDecimal taskObjectiveRatio;

    @NotNull(message = "任务开始时间为必传项")
    @ApiModelProperty(value = "任务开始时间")
    private Date taskStartTime;

    @NotNull(message = "任务结束时间为必传项")
    @ApiModelProperty(value = "任务结束时间")
    private Date taskEndTime;

    @ApiModelProperty(value = "门店与导购的关系列表")
    private List<TaskManagementInfoDTO> storeByUserIdList;

    @ApiModelProperty(value = "指定提醒导购list")
    private List<Long> reminderGuideList;

    @ApiModelProperty(value = "指定提醒员工list")
    private List<Long> remindEmployeesList;

    @ApiModelProperty(value = "提醒类型 0新建提醒 1任务距离多久未完成提醒 2任务还剩多久且完成率未达到多少提醒")
    private Integer reminderType;

    @ApiModelProperty(value = "自定义任务开始后")
    private Integer afterStartTime;

    @ApiModelProperty(value = "任务还剩时间百分比")
    private BigDecimal timeLeft;

    @ApiModelProperty(value = "任务完成率")
    private BigDecimal taskCompletionRate;

    @ApiModelProperty(value = "回访结果 0无需提交 1需要提交")
    private Integer returnVisitResult;

    @ApiModelProperty(value = "任务状态：0：未开始、1：进行中、2：已结束")
    private Integer status;

}
