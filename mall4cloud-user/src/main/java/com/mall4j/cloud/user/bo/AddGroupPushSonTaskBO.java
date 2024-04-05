package com.mall4j.cloud.user.bo;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddGroupPushSonTaskBO {

    @ApiModelProperty(value = "推送名称（子任务名称）")
    private String sonTaskName;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "推送内容")
    private String pushContent;

    private Long groupPushTaskId;

    @ApiModelProperty(value = "创建人ID")
    private Long createUserId;

    private Integer taskType;

}
