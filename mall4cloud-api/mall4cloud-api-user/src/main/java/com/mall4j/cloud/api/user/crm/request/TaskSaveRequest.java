package com.mall4j.cloud.api.user.crm.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

@Data
public class TaskSaveRequest {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long taskId;

    @ApiModelProperty(value = "任务模式：0-客户群发 1-群群发")
    private Integer taskMode;

    @ApiModelProperty(value = "任务类型：0-SCRM任务 1-CDP任务")
    private Integer taskType;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "触达人群：0-全部客户触达 1-按客户标签筛选 2-按客户分组筛选 3-按部门员工筛选")
    private Integer tagType;

    private Integer enableState;

    @ApiModelProperty(value = "操作状态，0创建中，1可用，2创建失败，3编辑中, 4编辑失败")
    private Integer operateStatus;

    @ApiModelProperty(value = "创建人")
    private Long createUserId;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "更新人")
    private String updater;

    @ApiModelProperty(value = "更新用户ID")
    private Long updateUserId;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "更新用户ID")
    private String updateTime;

    @ApiModelProperty(value = "删除标识，0未删除，1删除")
    private Integer deleteFlag;

}
