package com.mall4j.cloud.user.dto;


import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class QueryGroupPushTaskPageDTO extends PageDTO {

    @ApiModelProperty("任务名称")
    private String pushTaskName;

    @NotNull(message = "任务模式 为必传项")
    @ApiModelProperty(value = "任务模式：0-客户群发 1-群群发")
    private Integer taskMode;

    @ApiModelProperty("执行员工name")
    private String staffName;

    @ApiModelProperty(value = "操作状态：0 创建中 1 启用中 5 草稿")
    private Integer operateStatus;

    @ApiModelProperty(value = "任务类型：0-SCRM任务 1-CDP任务")
    private Integer taskType;

}
