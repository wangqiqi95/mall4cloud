package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务分页查询条件传输对象
 */
@Data
public class TaskInfoSearchParamDTO {
    @ApiModelProperty("任务名称")
    private String taskName;
    @ApiModelProperty("任务类型 1加企微好友 2好友转会员 3分享素材 4回访客户")
    private Integer taskType;
    @ApiModelProperty("企业id")
    private String companyId;
    @ApiModelProperty("任务状态 0未开始 1进行中 2已结束")
    private Integer taskStatus;
}
