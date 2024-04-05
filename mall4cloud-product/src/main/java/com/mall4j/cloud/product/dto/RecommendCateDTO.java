package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * @author cg
 */
@Data
public class RecommendCateDTO {

    @ApiModelProperty("主键id-修改时必传")
    private Long recommendCateId;

    @NotBlank(message = "分类名称不能为空")
    @ApiModelProperty("分类名称")
    private String name;

    @NotNull
    @ApiModelProperty("是否显示")
    private Integer isShow;

    @NotNull
    @ApiModelProperty("是否默认")
    private Integer isDefault;

    @NotBlank(message = "封面不能为空")
    @ApiModelProperty("封面")
    private String coverUrl;

}
