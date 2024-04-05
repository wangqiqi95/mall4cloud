package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Peter_Tan
 * @date 2023/02/13
 */
@Data
public class StaffSaveUserTagVO {

    @NotNull(message = "标签组ID为必传项")
    @ApiModelProperty("标签组ID")
    private Long groupId;

    @NotNull(message = "标签ID为必传项")
    @ApiModelProperty("标签ID")
    private Long tagId;

    @NotNull(message = "标签与标签组关联表ID为必传项")
    @ApiModelProperty("标签与标签组关联表ID")
    private Long groupTagRelationId;

    @NotBlank(message = "会员码为必传项")
    @ApiModelProperty("会员码")
    private String vipCode;

}
