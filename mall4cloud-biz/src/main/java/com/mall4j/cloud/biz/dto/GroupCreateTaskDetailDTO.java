package com.mall4j.cloud.biz.dto;

import com.mall4j.cloud.biz.model.cp.CpGroupCodeRef;
import com.mall4j.cloud.biz.model.cp.CpTaskUserRef;
import com.mall4j.cloud.biz.model.cp.CpGroupCreateTask;
import com.mall4j.cloud.biz.model.cp.CpTaskStaffRef;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class GroupCreateTaskDetailDTO {

    @ApiModelProperty(value = "任务信息")
    private CpGroupCreateTask task;

    @ApiModelProperty(value = "员工信息")
    private List<CpTaskStaffRef> staffRefList;

    @ApiModelProperty(value = "好友信息")
    private List<CpTaskUserRef> userRefs;

    @ApiModelProperty(value = "群组集合")
    private List<CpGroupCodeRef> codeList;

    @ApiModelProperty("客户总人数")
    private Integer userCount=0;

    @ApiModelProperty("员工总人数")
    private Integer staffCount=0;

    @ApiModelProperty("客群总数")
    private Integer groupCount=0;

    @ApiModelProperty("邀请人数")
    private Integer InviteCount=0;
    @ApiModelProperty("入群人数")
    private Integer joinGroupCount=0;
    @ApiModelProperty("完成发送员工数")
    private Integer sendStaffCount=0;

    @ApiModelProperty("未邀请人数")
    private Integer noInviteCount;
    @ApiModelProperty("未入群人数")
    private Integer noJoinGroupCount;
    @ApiModelProperty("未完成发送员工数")
    private Integer noSendStaffCount;

    @ApiModelProperty(value = "发送范围筛选id集合：部门员工id/标签id/客户分组阶段id")
    private List<String> refIds;

    @ApiModelProperty("是否允许成员在待发送客户列表中重新进行选择：0否/1是")
    private Integer allowSelect;
}
