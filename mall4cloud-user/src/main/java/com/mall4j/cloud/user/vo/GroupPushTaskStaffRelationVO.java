package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupPushTaskStaffRelationVO {
    @ApiModelProperty(value = "主键")
    private Long taskStaffRelationId;

    @ApiModelProperty(value = "群发任务ID")
    private Long groupPushTaskId;

    @ApiModelProperty(value = "导购ID")
    private Long staffId;

    @ApiModelProperty(value = "导购所属门店ID")
    private Long staffStoreId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "导购企业微信ID")
    private String staffCpUserId;

    @ApiModelProperty(value = "完成时间")
    private LocalDateTime finishTime;
}
