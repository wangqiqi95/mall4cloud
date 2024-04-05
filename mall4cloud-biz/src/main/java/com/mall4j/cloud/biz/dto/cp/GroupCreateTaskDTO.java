package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 标签建群任务表DTO
 *
 * @author hwy
 * @date 2022-02-18 18:17:51
 */
@Data
public class GroupCreateTaskDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty(value = "任务名称",required = true)
    private String taskName;

    @ApiModelProperty(value = "发送范围: 0按部门员工/1按客户标签/2按客户分组",required = true)
    private Integer sendScope;

    @ApiModelProperty(value = "入群引导语",required = true)
    private String slogan;

    @ApiModelProperty("群活码id")
    private Long codeId;

    @ApiModelProperty("发送方式")
    private Integer sendType;

    @ApiModelProperty("是否允许成员在待发送客户列表中重新进行选择：0否/1是")
    private Integer allowSelect;

    @ApiModelProperty("发送方式 0 保存 1 保存并发送")
    private Integer saveBtnType;

    @ApiModelProperty(value = "员工列表",required = true)
    private List<TaskStaffRefDTO> staffs;

    @ApiModelProperty(value = "客户列表",required = false)
    private List<CpTaskUserRefDTO> users;

    @ApiModelProperty(value = "发送范围筛选id集合：部门员工id/标签id/客户分组阶段id",required = true)
    private List<String> refIds;

    @ApiModelProperty(value = "拉群方式：0企微群活码/1自建群活码",required = true)
    private Integer groupType;

    @ApiModelProperty(value = "群组集合",required = true)
    private List<CpGroupCodeListDTO> codeList;

}
