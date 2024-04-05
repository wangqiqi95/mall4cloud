package com.mall4j.cloud.user.dto;

import com.mall4j.cloud.user.model.GroupPushTag;
import com.mall4j.cloud.user.model.GroupPushTaskStaffRelation;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddPushTaskDTO {

    @ApiModelProperty(value = "群发任务ID（修改时必传）")
    private Long groupPushTaskId;

    @ApiModelProperty(value = "创建人ID（后台交互参数，不需要传）")
    private Long createUserId;

    @NotNull(message = "任务模式 为必传项")
    @ApiModelProperty(value = "任务模式：0-客户群发 1-群群发")
    private Integer taskMode;

    //@NotNull(message = "任务类型 为必传项")
    @ApiModelProperty(value = "任务类型：0-SCRM任务 1-CDP任务")
    private Integer taskType;

    @NotBlank(message = "任务名称为必传项")
    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty("是否允许成员在待发送客户列表中重新进行选择：0否/1是")
    private Integer allowSelect;

//    @NotNull(message = "执行员工staffList 为必传项")
    @ApiModelProperty(value = "执行员工staffList")
    private List<GroupPushTaskStaffRelation> staffList;

    //@NotNull(message = "触达人群 为必传项")
    @ApiModelProperty(value = "触达人群：0-全部客户触达 1-按客户标签筛选 2-按客户分组筛选 3-按部门员工筛选")
    private Integer tagType;

    @ApiModelProperty(value = "选择标签：可能为标签 客户分组 部门")
    private List<GroupPushTag> tagList;

    @ApiModelProperty(value = "推送信息列表")
    private List<AddPushSonTaskDTO> pushSonTaskList;

}
