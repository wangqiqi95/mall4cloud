package com.mall4j.cloud.user.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddGroupSonTaskSendRecordBO {

    @ApiModelProperty(value = "群发任务ID")
    private Long pushTaskId;

    @ApiModelProperty(value = "群发子任务ID")
    private Long pushSonTaskId;

    @ApiModelProperty(value = "用户ID")
    private Long vipUserId;

    @ApiModelProperty(value = "导购ID")
    private Long staffId;

    @ApiModelProperty("发送类型，1：1v1，2：批量群发")
    private Integer sendModel;

    @ApiModelProperty("发送状态，0未发送，1已发送，2已停止")
    private Integer sendStatus;

    @ApiModelProperty("用户企业微信ID")
    private String vipCpUserId;

    @ApiModelProperty("0非最终发送，1最终发送（完成）")
    private Integer finalSend;

    @ApiModelProperty(value = "完成时间")
    private LocalDateTime finishTime;

}
