package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VeekerStatus {

    @ApiModelProperty("微客状态 -1-初始化(普通用户) 0-禁用 1-启用 2-拉黑")
    private Integer veekerStatus;

    @ApiModelProperty("微客审核状态 -1-初始化(普通用户) 0-待审核 1-已同意 2-已拒绝")
    private Integer veekerAuditStatus;

}
