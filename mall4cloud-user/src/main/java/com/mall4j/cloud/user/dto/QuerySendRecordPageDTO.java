package com.mall4j.cloud.user.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QuerySendRecordPageDTO extends PageDTO {

    @NotNull(message = "主任务ID为必传项")
    @ApiModelProperty("主任务ID")
    private Long pushTaskId;

    @NotNull(message = "子任务ID为必传项")
    @ApiModelProperty("子任务ID")
    private Long sonTaskId;

    @NotNull(message = "导购ID为必传项")
    @ApiModelProperty("导购ID")
    private Long staffId;

}
