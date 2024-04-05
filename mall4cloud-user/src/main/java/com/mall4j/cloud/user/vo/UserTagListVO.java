package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserTagListVO {

    @ApiModelProperty(value = "标签ID")
    private Long tagId;

    @ApiModelProperty(value = "标签名")
    private String tagName;

    @ApiModelProperty(value = "用户标签关系id")
    private Long userTagRelationId;

    @ApiModelProperty(value = "标签组与标签关联表ID")
    private Long groupTagRelationId;

    @ApiModelProperty(value = "标签组ID")
    private Long groupId;

}
