package com.mall4j.cloud.biz.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 任务调度详情信息
 */
@Data
public class TaskExecuteDetailInfoDTO {

    @ApiModelProperty("调度表id")
    private Long executeId;
    @ApiModelProperty("任务id")
    private Long taskId;
    @ApiModelProperty("客户id")
    private String clientId;
    @ApiModelProperty("任务类型为加企微好友时特有字段。0未添加 1已添加 2添加失败")
    private Integer addStatus;
    @ApiModelProperty("客户群id")
    private String clientGroupId;
    @ApiModelProperty("状态 1完成 0未完成")
    private Integer status;
    @ApiModelProperty("结束时间")
    private Date endTime;
}

