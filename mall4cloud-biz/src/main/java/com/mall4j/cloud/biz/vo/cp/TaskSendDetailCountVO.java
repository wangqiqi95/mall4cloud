package com.mall4j.cloud.biz.vo.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class TaskSendDetailCountVO {
    @ApiModelProperty("未完成转发数")
    private Integer unReplayTotal;

    @ApiModelProperty("完成转发数")
    private Integer replayTotal;

    @ApiModelProperty("转发率")
    private String replayPer;
}

