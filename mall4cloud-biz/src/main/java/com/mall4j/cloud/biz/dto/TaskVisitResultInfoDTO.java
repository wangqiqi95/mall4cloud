package com.mall4j.cloud.biz.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务回访信息信息
 */
@Data
public class TaskVisitResultInfoDTO {
    @ApiModelProperty("调度详情")
    private Long executeDetailId;
    @ApiModelProperty("回访信息")
    private String resultInfo;
    @ApiModelProperty("附件信息")
    private String fileInfo;
}

