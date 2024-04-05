package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * admin用户发布种草
 *
 * @author cg
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RecommendAdminDTO extends RecommendDTO {

    @NotNull
    @ApiModelProperty("分类")
    private Long recommendCateId;

    @NotNull
    @ApiModelProperty("权重")
    private Integer weight;
}
