package com.mall4j.cloud.user.dto.openapi;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SendTaskDTO {

    @ApiModelProperty("任务id")
    @NotNull(message = "任务id")
    private Long taskId;

    @ApiModelProperty("发送时间 时间戳")
    @NotNull(message = "发送时间")
    private Long sendTime;

    @ApiModelProperty("任务-活动人群")
    @NotNull(message = "活动人群不能为空")
    private List<String> userIds;

}
