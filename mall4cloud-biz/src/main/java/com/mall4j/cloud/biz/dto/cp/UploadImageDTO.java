package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 聊天场景配置表DTO
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Data
public class UploadImageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("图片的全路径")
    @NotBlank(message = "路径不能为空")
    private String imagePath;

    @ApiModelProperty("素材类型 图片（image）、语音（voice）、视频（video），普通文件(file)")
    private String type;

    private String fileName;
}
