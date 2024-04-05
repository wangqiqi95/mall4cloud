package com.mall4j.cloud.user.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class EditTagGroupDTO {

    @NotNull(message = "标签组ID为必传项")
    @ApiModelProperty(value = "标签组ID")
    private Long tagGroupId;

    @NotBlank(message = "标签组名称为必传项")
    @ApiModelProperty(value = "标签组名称")
    private String groupName;

    @NotNull(message = "组标识为必传项")
    @ApiModelProperty(value = "组标识，1导购助手打标，2导购助手显示，3管理后台打标")
    private List<Integer> authFlagArrays;

    @NotNull(message = "启用标识为必传项")
    @ApiModelProperty(value = "启用标识：0未启用，1启用")
    private Integer enableState;

}
