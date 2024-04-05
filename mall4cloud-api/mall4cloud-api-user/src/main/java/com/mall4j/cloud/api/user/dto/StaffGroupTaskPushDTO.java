package com.mall4j.cloud.api.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Date 2023年3月6日
 * @Created by tan
 */
@Data
public class StaffGroupTaskPushDTO implements Serializable {

    @ApiModelProperty(value = "主任务ID")
    private Long taskId;

    @ApiModelProperty(value = "子任务ID")
    private Long sonTaskId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "导购ID")
    private Long staffId;

    @ApiModelProperty("发送状态 1发送成功，2发送失败")
    private Integer sendStatus;

}
