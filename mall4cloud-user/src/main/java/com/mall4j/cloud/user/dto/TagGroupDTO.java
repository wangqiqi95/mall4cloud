package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Peter_Tan
 * @date 2023/02/08
 */
@Data
public class TagGroupDTO {

    @ApiModelProperty("标签与标签组关联ID")
    private Long groupTagRelationId;

    @ApiModelProperty(value = "标签ID")
    private Long tagId;

    @ApiModelProperty(value = "标签名")
    private String tagName;

}
