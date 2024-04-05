package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class ExportUserTagRelationDTO {

    @ApiModelProperty("标签与标签组关联表ID")
    private Long groupTagRelationId;

    @NotNull(message = "标签组ID为必传项")
    @ApiModelProperty("标签组ID")
    private Long groupId;

    @NotNull(message = "标签ID为必传项")
    @ApiModelProperty("标签ID")
    private Long tagId;

    @NotNull(message = "请选择上传文件")
    @ApiModelProperty("文件")
    private MultipartFile file;

}
