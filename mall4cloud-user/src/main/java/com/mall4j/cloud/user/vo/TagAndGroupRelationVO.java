package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TagAndGroupRelationVO {

    @ApiModelProperty("标签与标签组关联ID")
    private Long groupTagRelationId;

    @ApiModelProperty(value = "标签ID")
    private Long tagId;
}
