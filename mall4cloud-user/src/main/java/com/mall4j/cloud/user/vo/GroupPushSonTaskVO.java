package com.mall4j.cloud.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class GroupPushSonTaskVO {


    @ApiModelProperty(value = "主键")
    private Long groupPushSonTaskId;

    @ApiModelProperty(value = "父任务ID")
    private Long groupPushTaskId;

    @ApiModelProperty(value = "推送名称（子任务名称）")
    private String sonTaskName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "推送内容")
    private String pushContent;

    @ApiModelProperty(value = "子任务素材列表")
    private List<GroupPushSonTaskMediaVO> groupPushSonTaskMediaList;

    @ApiModelProperty(value = "1-正常/2-中止")
    private Integer status;

}
