package com.mall4j.cloud.biz.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 任务调度信息
 */
@Data
public class TaskExecuteInfoDTO {

    @ApiModelProperty("任务id")
    private Long taskId;
    @ApiModelProperty("任务类型 1加企微好友 2好友转会员 3分享素材 4回访客户")
    private Integer taskType;
    @ApiModelProperty("导购id")
    private String shoppingGuideId;
    @ApiModelProperty("状态 1完成 0未完成")
    private Integer status;
    @ApiModelProperty("任务时间")
    private Date taskTime;
    @ApiModelProperty("需完成数量")
    private Integer clientSum;
    @ApiModelProperty("已完成数量")
    private Integer successClientSum;
}

