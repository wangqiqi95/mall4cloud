package com.mall4j.cloud.biz.dto;


import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TaskManagementPageDTO extends PageDTO {

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty(value = "任务类型：0加企微好友 1 分享素材 2回访客户")
    private Integer taskType;

    @ApiModelProperty(value = "公司类型 0麦吉利")
    private Integer affiliatedCompanyType;

    @ApiModelProperty(value = "任务状态：0：未开始、1：进行中、2：已结束")
    private Integer status;

}
