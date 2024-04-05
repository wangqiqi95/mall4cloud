package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class EditTagDTO {

    @NotNull(message = "标签组ID为必传项")
    @ApiModelProperty("标签组ID")
    private Long groupId;

    @NotNull(message = "标签ID为必传项")
    @ApiModelProperty("标签ID")
    private Long tagId;

    @NotBlank(message = "标签名称为必传项")
    @ApiModelProperty("标签名")
    private String tagName;

//    @ApiModelProperty("会员码列表")
//    private List<String> vipCodeList;

    @NotNull(message = "启用状态为必传项")
    @ApiModelProperty(value = "启用状态，0未启用，1启用")
    private Integer enableState;

    @ApiModelProperty(value = "排序")
    private Integer sort;


}
