package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CancelTagDTO {

    @NotNull(message = "userTagRelationId为必传项")
    @ApiModelProperty("用户标签关联ID")
    private Long userTagRelationId;

}
