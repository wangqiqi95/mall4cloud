package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class QueryTagGroupGradeDTO {

    @NotNull(message = "手动标签为必传项")
    @ApiModelProperty(value = "1手动标签，2导购标签")
    private Integer groupType;

    @ApiModelProperty(value = "组名")
    private String groupName;

    @ApiModelProperty(value = "父类ID，一级分类ID")
    private Long parentId;

    @ApiModelProperty(value = "权限")
    private List<Integer> authFlagArray;

    @ApiModelProperty(value = "0未启用，1启用")
    private Integer enableState;
}
