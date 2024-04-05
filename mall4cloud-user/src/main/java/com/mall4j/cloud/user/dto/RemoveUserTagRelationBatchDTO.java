package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class RemoveUserTagRelationBatchDTO {

    @NotNull(message = "标签ID为必传项")
    @ApiModelProperty("标签ID")
    private Long tagId;

    @NotNull(message = "标签组ID为必传项")
    @ApiModelProperty("标签组ID")
    private Long groupId;

    @Size(min = 1, message = "需要最少选择一个用户")
    @ApiModelProperty("标签用户关联ID")
    private List<Long> userTagRelationIdList;

}
