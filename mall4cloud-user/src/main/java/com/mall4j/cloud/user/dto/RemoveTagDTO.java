package com.mall4j.cloud.user.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RemoveTagDTO {

    @ApiModelProperty("标签ID")
    private Long tagId;
    @ApiModelProperty("标签组ID")
    private Long groupId;
}
