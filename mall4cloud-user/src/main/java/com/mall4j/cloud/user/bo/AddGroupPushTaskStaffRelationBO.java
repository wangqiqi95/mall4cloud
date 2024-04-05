package com.mall4j.cloud.user.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddGroupPushTaskStaffRelationBO {

    @ApiModelProperty(value = "群发任务ID")
    private Long groupPushTaskId;

    @ApiModelProperty(value = "导购ID")
    private Long staffId;

    @ApiModelProperty(value = "导购企微ID")
    private String staffCpUserId;
}
