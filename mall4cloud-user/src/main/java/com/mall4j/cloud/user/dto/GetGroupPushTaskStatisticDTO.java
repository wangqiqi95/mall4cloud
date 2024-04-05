package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetGroupPushTaskStatisticDTO {


    @NotNull(message = "主任务ID为必传项")
    @ApiModelProperty("主任务ID")
    private Long pushTaskId;

//    @NotNull(message = "子任务ID为必传项")
//    @ApiModelProperty("子任务ID")
//    private Long sonTaskId;

}
