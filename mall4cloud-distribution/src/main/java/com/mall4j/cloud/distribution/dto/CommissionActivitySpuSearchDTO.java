package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author gww
 * @date 2021/12/12
 */
@Data
public class CommissionActivitySpuSearchDTO {

    @NotNull(message = "活动ID不能为空")
    @ApiModelProperty(value = "活动ID")
    private Long activityId;

    @ApiModelProperty(value = "商品名称")
    private String spuName;

    @ApiModelProperty(value = "平台一级分类id")
    private Long primaryCategoryId;

    @ApiModelProperty(value = "平台二级分类id")
    private Long secondaryCategoryId;

    @ApiModelProperty(value = "平台三级分类id")
    private Long categoryId;

    @ApiModelProperty("导购佣金状态 -1-全部 0-禁用 1-启用")
    private Integer guideRateStatus;

    @ApiModelProperty("微客佣金状态 -1-全部 0-禁用 1-启用")
    private Integer shareRateStatus;

    @ApiModelProperty("发展佣金状态 -1-全部 0-禁用 1-启用")
    private Integer developRateStatus;

}
