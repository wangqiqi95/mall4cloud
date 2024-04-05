package com.mall4j.cloud.user.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EditParentTagGroupDTO {

    @NotNull(message = "标签组ID为必传项")
    @ApiModelProperty(value = "标签组ID")
    private Long tagGroupId;

    @NotBlank(message = "标签组名称为必传项")
    @ApiModelProperty(value = "标签组名称")
    private String groupName;

}
