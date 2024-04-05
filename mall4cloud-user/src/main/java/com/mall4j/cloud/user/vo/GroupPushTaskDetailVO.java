package com.mall4j.cloud.user.vo;


import com.mall4j.cloud.user.model.GroupPushTag;
import com.mall4j.cloud.user.model.GroupPushTaskStaffRelation;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class GroupPushTaskDetailVO {

    @ApiModelProperty(value = "主键")
    private Long groupPushTaskId;

    @ApiModelProperty(value = "任务类型：0-SCRM任务 1-CDP任务")
    private Integer taskType;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "触达人群：0-全部客户触达 1-按客户标签筛选 2-按客户分组筛选 3-按部门员工筛选")
    private Integer tagType;

    @ApiModelProperty(value = "选择标签：可能为标签 客户分组 部门")
    private List<GroupPushTag> tagList;

    @ApiModelProperty(value = "执行员工staffList")
    private List<GroupPushTaskStaffRelation> staffList;

    @ApiModelProperty(value = "操作状态，0创建中，1可用，2创建失败，3编辑中, 4编辑失败")
    private Integer operateStatus;

    @ApiModelProperty(value = "失败时的参数")
    private String failParam;

    @ApiModelProperty("是否允许成员在待发送客户列表中重新进行选择：0否/1是")
    private Integer allowSelect;

    @ApiModelProperty(value = "子任务列表")
    private List<GroupPushSonTaskVO> groupPushSonTaskList;

}
