package com.mall4j.cloud.api.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Date 2023年3月6日
 * @Created by tan
 * 新增推送完成记录表数据参数
 */
@Data
public class StaffSaveSonTaskSendRecordDTO implements Serializable {

    @ApiModelProperty(value = "子任务ID")
    private Long sonTaskId;

    @ApiModelProperty(value = "导购ID")
    private Long staffId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "客户企微id")
    private String vipCpUserId;

}
