package com.mall4j.cloud.biz.vo.cp.taskInfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务素材信息
 */
@Data
public class TaskMaterialInfoVO {
    /**
     * 任务id
     */
    @ApiModelProperty("任务id")
    private Long taskId;
    /**
     * 素材类型 图片（image）、语音(voice）、视频(video），普通文件(file)
     */
    @ApiModelProperty("素材类型 图片（image）、语音(voice）、视频(video），普通文件(file〕")
    private String materialType;
    /**
     * 素材保存信息
     */
    @ApiModelProperty("文件信息")
    private String fileInfo;
}
