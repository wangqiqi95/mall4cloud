package com.mall4j.cloud.api.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StartGroupPushDTO {

    @ApiModelProperty(value = "子任务ID")
    private Long sonTaskId;

    @ApiModelProperty(value = "导购ID")
    private Long staffId;
}
