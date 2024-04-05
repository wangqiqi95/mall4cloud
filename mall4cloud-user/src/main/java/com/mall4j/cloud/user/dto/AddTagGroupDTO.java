package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class AddTagGroupDTO {

    @ApiModelProperty("一级分类ID,非必传")
    private Long parentId;

    @NotBlank(message = "一级分类为必传项")
    @ApiModelProperty("一级分类")
    private String firstGrade;

    @NotBlank(message = "二级分类为必传项")
    @ApiModelProperty("二级分类")
    private String secondGrade;

    @NotNull(message = "标签组类型为必传项")
    @ApiModelProperty("标签组类型，1手动标签，2导购标签")
    private Integer groupType;

    @Size( min = 1,max = 3, message = "至少需要选择一种权限")
    @ApiModelProperty("组标识，1导购助手打标，2导购助手显示，3管理后台打标")
    private List<Integer> authFlagArrays;

    @NotNull(message = "启用状态为必传项")
    @ApiModelProperty("启用状态，0未启用，1启用")
    private Integer enableState;

    @NotNull(message = "单选与复选状态为必传项")
    @ApiModelProperty("单选状态，0多选，1单选")
    private Integer singleState;


}
