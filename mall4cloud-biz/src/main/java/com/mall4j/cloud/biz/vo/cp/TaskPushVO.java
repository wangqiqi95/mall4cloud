package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.biz.model.cp.MaterialMsg;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class TaskPushVO {
    /**
     *
     */
    @ApiModelProperty("推送id")
    private Long id;

    @ApiModelProperty("任务id")
    private Long taskId;

    @ApiModelProperty("推送名称")
    private String pushName;

    @ApiModelProperty("推送开始时间")
    private Date startTime;

    @ApiModelProperty("推送截止时间")
    private Date endTime;

    @ApiModelProperty("推送内容")
    private String content;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("创建人id")
    private Long createBy;

    @ApiModelProperty("创建人名称")
    private String createName;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("附件信息")
    private MaterialMsg attachment;
}

