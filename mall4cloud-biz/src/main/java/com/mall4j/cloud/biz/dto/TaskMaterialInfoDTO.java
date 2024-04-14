package com.mall4j.cloud.biz.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务素材信息传输对象
 */
@Data
public class TaskMaterialInfoDTO {
    @ApiModelProperty("任务id")
    private Long taskId;
    @ApiModelProperty("素材类型 图片（image）、语音(voice）、视频(video），普通文件(file〕")
    private String materialType;
    @ApiModelProperty("素材保存信息")
    private String fileInfo;
}

