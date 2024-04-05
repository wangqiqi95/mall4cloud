package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserStaffCpRelationSetTagRequest {
    @ApiModelProperty("id")
    private List<Long> ids;

    @ApiModelProperty("阶段id")
    private Long stageId;

    @ApiModelProperty("标签")
    private List<CrmUserTags> tags;
}
