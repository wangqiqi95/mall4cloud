package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务分页查询条件传输对象
 */
@Data
public class TaskExecuteInfoSearchParamDTO {

    @ApiModelProperty("任务id")
    private Long taskId;
    @ApiModelProperty("任务批次id")
    private Long taskBatchId;
    @ApiModelProperty("任务名称")
    private String taskName;
    @ApiModelProperty("任务类型 1加企微好友 2好友转会员 3分享素材 4回访客户")
    private Integer taskType;
    @ApiModelProperty("状态1完成 0未完成")
    private Integer status;
}
