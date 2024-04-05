package com.mall4j.cloud.user.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddStaffBatchSendCpMsgBO {

    @ApiModelProperty(value = "推送任务ID")
    private Long pushTaskId;

    @ApiModelProperty(value = "推送子任务ID")
    private Long pushSonTaskId;

    @ApiModelProperty(value = "导购ID")
    private Long staffId;

    @ApiModelProperty(value = "企微群发任务ID")
    private String msgId;

    @ApiModelProperty(value = "0未发送，1已发送，2已停止")
    private Integer sendStatus;

    @ApiModelProperty(value = "导购企微ID")
    private String staffCpUserId;

    @ApiModelProperty(value = "总人数")
    private Integer headCount;

    @ApiModelProperty(value = "触达数")
    private Integer reachCount;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "完成状态；0未完成，1完成")
    private Integer finishState;

    @ApiModelProperty(value = "完成时间")
    private LocalDateTime finishTime;
}
