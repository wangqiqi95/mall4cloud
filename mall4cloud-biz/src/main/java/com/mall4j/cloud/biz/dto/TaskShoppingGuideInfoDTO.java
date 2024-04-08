package com.mall4j.cloud.biz.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务导购表
 */
@Data
public class TaskShoppingGuideInfoDTO {
    @ApiModelProperty("任务id")
    private Long taskId;
    @ApiModelProperty("导购id")
    private String shopGuideId;
}

