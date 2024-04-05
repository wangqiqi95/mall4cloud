package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 导购端推送任务-子任务推送素材参数
 */
@Data
public class TaskSonMediaVO implements Serializable {

    @ApiModelProperty("对应子任务ID")
    private Long sonTaskId;

    @ApiModelProperty("任务素材ID")
    private Long mediaId;

    @ApiModelProperty("内容类型，0图片，1视频，2H5，3小程序")
    private String type;

    @ApiModelProperty("素材内容JSON")
    private String media;

    @ApiModelProperty("素材内容VO")
    private MediaJsonVO mediaJsonVO;

}
