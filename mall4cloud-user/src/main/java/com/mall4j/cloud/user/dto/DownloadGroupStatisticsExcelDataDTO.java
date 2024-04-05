package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DownloadGroupStatisticsExcelDataDTO {

    @ApiModelProperty("群发任务ID")
    private Long pushTaskId;
    @ApiModelProperty("群发子任务ID")
    private Long sonTaskId;
}
