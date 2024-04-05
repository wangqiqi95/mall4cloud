package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Peter_Tan
 * @date 2023/02/13
 */
@Data
public class StaffSaveUserTagDTO {

    @NotNull(message = "标签ID为必传项")
    @ApiModelProperty("标签ID")
    private Long tagId;

    @NotNull(message = "标签组ID为必传项")
    @ApiModelProperty("标签组ID")
    private Long groupId;

    @NotNull(message = "标签与标签组关联表ID为必传项")
    @ApiModelProperty("标签与标签组关联表ID")
    private Long groupTagRelationId;

    @NotNull(message = "标签用户关联ID为必传项")
    @ApiModelProperty("标签用户关联ID")
    private Long userTagRelationId;

    @NotBlank(message = "会员码为必传项")
    @ApiModelProperty("会员码")
    private String vipCode;

}
