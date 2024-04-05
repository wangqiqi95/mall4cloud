package com.mall4j.cloud.user.dto;


import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class TerminateGroupPushSonTaskDTO {

    @ApiModelProperty(value = "子任务id")
    @NotNull(message = "子任务id")
    private Long sonTaskId;
}
