package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 导购端推送任务-子任务参数
 */
@Data
public class TaskSonVO implements Serializable {

    @ApiModelProperty("子任务id")
    private Long sonTaskId;

    @ApiModelProperty("子任务名称")
    private String sonTaskName;

    @ApiModelProperty("推送内容/群发话术")
    private String pushContent;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("截止时间")
    private Date endTime;

    @ApiModelProperty("小程序封面图片|图片|视频的素材")
    private List<TaskSonMediaVO> sonTaskMedia;

    private Integer taskType;

}
