package com.mall4j.cloud.biz.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务客户群表
 */
@Data
public class TaskClientGroupInfoDTO {
    @ApiModelProperty("客户群id)")
    private String clientGroupId;
    @ApiModelProperty("客户群名称")
    private String clientGroupName;
}

