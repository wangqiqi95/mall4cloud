package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 导购端推送任务-子任务参数
 */
@Data
public class TaskSonItemVO implements Serializable {

    @ApiModelProperty("主任务id")
    private Long taskId;

    @ApiModelProperty("主任务名称")
    private String taskName;

    @ApiModelProperty("子任务id")
    private Long sonTaskId;

    @ApiModelProperty("子任务名称")
    private String sonTaskName;

    @ApiModelProperty("推送内容/群发话术")
    private String pushContent;

    @ApiModelProperty("截止时间")
    private Date endTime;

    @ApiModelProperty("邀约人群")
    private String invitedCrowd;

    @ApiModelProperty("总人数")
    private BigDecimal headCount;

    @ApiModelProperty("触达数")
    private BigDecimal reachCount;

    @ApiModelProperty("完成进度")
    private BigDecimal rate;

    @ApiModelProperty("是否群发过 1:没有 2:有")
    private Integer isToGroupPush;

    @ApiModelProperty("小程序封面图片|图片|视频的素材")
    private List<TaskSonMediaVO> sonTaskMedia;

}
